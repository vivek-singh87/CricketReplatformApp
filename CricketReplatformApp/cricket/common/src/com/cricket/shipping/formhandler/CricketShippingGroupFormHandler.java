package com.cricket.shipping.formhandler;

import static com.cricket.common.constants.CricketCommonConstants.EMPTY_STRING;
import static com.cricket.common.constants.CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.BILLING_ADDRESS_RADIO_VALUE;
import static com.cricket.common.constants.CricketESPConstants.BUSINESS_ADDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.CRICKET_PROFILE;
import static com.cricket.common.constants.CricketESPConstants.DIFFERENT_ADDRESS;
import static com.cricket.common.constants.CricketESPConstants.ESP_RESPONSE_CODE_SUCCESS;
import static com.cricket.common.constants.CricketESPConstants.ESP_SERVICE_RESPONSE_DATA;
import static com.cricket.common.constants.CricketESPConstants.ESP_TIMEOUT;
import static com.cricket.common.constants.CricketESPConstants.HOME_ADDRESS;
import static com.cricket.common.constants.CricketESPConstants.INVALID_ACCOUNT_ADDRESS;
import static com.cricket.common.constants.CricketESPConstants.INVALID_BILLING_ADDRESS;
import static com.cricket.common.constants.CricketESPConstants.INVALID_SHIPPING_ADDRESS;
import static com.cricket.common.constants.CricketESPConstants.LOCATION_INFO;
import static com.cricket.common.constants.CricketESPConstants.PROFILE_ITEM;
import static com.cricket.common.constants.CricketESPConstants.SAME_AS_ACCOUNT_HOLDER_ADDRESS;
import static com.cricket.common.constants.CricketESPConstants.SAME_AS_SHIPPING_ADDRESS;
import static com.cricket.common.constants.CricketESPConstants.SHIPPING_ADDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.SHIPPING_ADDRESS_RADIO_VALUE;
import static com.cricket.common.constants.CricketESPConstants.USER_ACCOUNT_INFORMATION;
import static com.cricket.common.constants.CricketESPConstants.USER_SESSION_BEAN;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import atg.commerce.CommerceException;
import atg.commerce.order.PipelineConstants;
import atg.commerce.order.purchase.ShippingGroupFormHandler;
import atg.commerce.util.RepeatingRequestMonitor;
import atg.core.util.ContactInfo;
import atg.core.util.StringUtils;
import atg.droplet.DropletException;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.service.pipeline.PipelineResult;
import atg.service.pipeline.RunProcessException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketBillingAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.checkout.utils.CricketAddress;
import com.cricket.checkout.utils.CricketShippingAddress;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.payment.CricketABPPaymentData;
import com.cricket.commerce.order.payment.CricketPaymentData;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.email.CricketEmailManager;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.AddressVO;
import com.cricket.integration.esp.vo.ResponseVO;
import com.cricket.integration.esp.vo.ValidateAddressResponseVO;
import com.cricket.integration.maxmind.CitySessionInfoObject;
import com.cricket.shipping.tools.CricketAddressTools;
import com.cricket.user.session.UserAccountInformation;
import com.cricket.user.session.UserSessionBean;
import com.cricket.util.EspServiceResponseData;
import com.cricket.vo.CricketProfile;
import com.cricket.vo.MyCricketCookieLocationInfo;

/** This class is used to capture the shipping/billing/account holder address.
 * @author TechM
 *
 */
public class CricketShippingGroupFormHandler extends ShippingGroupFormHandler {	
	/* holds account holder address data.*/
	private CricketAccountHolderAddressData accountHolderAddressData;
	/* holds shipping address data.*/
	private CricketShippingAddressData shippingAddressData;
	/* holds billing address data.*/
	private CricketBillingAddressData billingAddressData;
	/* holds address tool to perform operation on address.*/
	private CricketAddressTools addressTools;
	/* holds emailManager.*/
	private CricketEmailManager emailManager;
	/* flag to hold isNewAccountHolderAddrss. */
	private boolean newAccountHolderAddress;
	/* flag to hold isNewShippingAddrss. */
	private boolean newShippingAddress;
	/* flag to hold isNewBillingAddress. */
	private boolean newBillingAddress;
	/* holds successURL. */
	private String shippingBillingAddressSuccessURL;
	/* holds Error URL. */
	private String shippingBillingAddressErrorURL;
	/* holds CricketESPAdapter instance. */
   	private CricketESPAdapter cricketESPAdapter;
   	/* flag to hold isNewBillingAddress. */
	private boolean openAddressModel = Boolean.FALSE;
	/* holds CitySessionInfoObject to interact with ESP layer*/
	private CitySessionInfoObject citySessionInfoObject;
   /* holds MyCricketCookieLocationInfo to get locationInfo*/    
	private MyCricketCookieLocationInfo locationInfo;
   /* holds CricketProfile to get logged in user profile*/  
	private CricketProfile cricketProfile;
	/* holds EspServiceResponseData */
	private EspServiceResponseData espServiceResponseData;
	/* holds UserAccountInformation */
	private UserAccountInformation userAccountInformation; 
	/* holds helper class instance */
	/* holds cricketEspService chain name. */
	private String cricketESPServicesChainName;
    /* holds userSessionBean to get feature add ons by package*/
	private UserSessionBean userSessionBean;
	/* holds radio button value of shippingRadioValue*/
	String shippingRadioValue;
	/* holds radio button value of billingRadioValue*/
	String billingRadioValue;
	/* variable holds the payment data for normal credit card */
	private CricketPaymentData paymentData;
	/* variable holds the payment data for credit card data in case of auto bill pay for diff card*/
	private CricketABPPaymentData abpPaymentData;
	
	private MutableRepository profileAdapterRepository;

  // * note - if user logged in
	// copy the accountholderaddress,shippingAddress and billing address to
	// cricketAccountHolderAddressData,cricketShippingAddressData
	// CricketBillingAddressData in profile component, it simply the most of the
	// logic

	private String getPageUrlForLogInfo() {
		// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(null != ServletUtil.getCurrentRequest()){
					if(!StringUtils.isBlank( ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
						pageURL =  ServletUtil.getCurrentRequest().getRequestURI();
					}
				}				
				return pageURL;
	}
	
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	
	
	public boolean handleShippingandBillingAddress(
			DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		{
			boolean flag = false;
			long starttime = 0L;
			long endtime = 0L;
			String l_sMethod = "CricketCheckoutFormHandler.handleShippingandBillingAddress";
 			if(isLoggingDebug()){
				logDebug("Order Id :"+getOrder().getId());
				logDebug("Account Address :"+getAccountHolderAddressData().getAccountAddress().toString());
				logDebug("SHipping Address :"+getShippingAddressData().getShippingAddress().toString());
				logDebug("Billing Address :"+getBillingAddressData().getBillingAddress().toString());
				logDebug("CricketShippingGroupFormHandler:handleShippingandBillingAddress()... "  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
			}
			RepeatingRequestMonitor l_oRepeatingRequestMonitor = getRepeatingRequestMonitor();
			if ((l_oRepeatingRequestMonitor == null)
					|| (l_oRepeatingRequestMonitor
							.isUniqueRequestEntry(l_sMethod))) {
				Transaction l_oTransaction = null;
				try {
					l_oTransaction = ensureTransaction();
					synchronized (getOrder()) {
						try {
							starttime = System.currentTimeMillis();
							precreateShippingandBillingAddress(pRequest, pResponse);
							if (getProfile().isTransient()) {
								try {
									MutableRepository profileAdapterRepository = getProfileAdapterRepository();
									 MutableRepositoryItem profile = profileAdapterRepository.getItemForUpdate(getProfile().getRepositoryId(),CricketCommonConstants.USER_ITEM_DESCRIPTOR);
									//profileAdapterRepository.addItem(profile);
									profileAdapterRepository.updateItem(profile);
								} catch (RepositoryException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							createShippingandBillingAddress(pRequest, pResponse);
							if(!getFormError()) {
								postcreateShippingandBillingAddress(pRequest, pResponse);
								runProcessRepriceOrder(getOrder(), getUserPricingModels(), getUserLocale(), getProfile(), createRepriceParameterMap());								
								//((CricketOrderImpl) getOrder()).updateVersion(); 
								getOrderManager().updateOrder(getOrder());
							}
							endtime = System.currentTimeMillis();
							logInfo("[CricketShippingGroupFormHandler->handleShippingandBillingAddress()]: time taken to complete shipping and billing page in milliseconds:  "+ (endtime - starttime));
						} catch (CommerceException e) {
							logError(e);
						} catch (RunProcessException e) {
							logError(e);
						}
					}
					
					if (getFormError()) {
						// this is for bypassing special url parameter i.e whoopsError=true when an error occurs in address validation.
						Vector errorVector = getFormExceptions();
						String errorUrl = null;
						DropletException ex = null;
						boolean isWhoopsError = true;
						if(errorVector!=null && errorVector.size()>0){
							for(int i=0; i<errorVector.size();i++){
								ex = (DropletException)errorVector.get(i);
								 if(null !=ex && null!=ex.getMessage()&&ex.getMessage().contains(CricketESPConstants.ADDRES)){
									 isWhoopsError = false;
									 break;
								 }
							}
						}
						if(isWhoopsError){
							errorUrl =getShippingBillingAddressErrorURL()+CricketCommonConstants.WHOOPS_ERROR_YES;
						}else{
							errorUrl = getShippingBillingAddressErrorURL();
						}
					 return	checkFormRedirect(null, 
							 errorUrl, pRequest, pResponse);
					}else{
						if(((CricketOrderImpl)getOrder()).isDownGrade()){
							String redirectionUrl = getShippingBillingAddressSuccessURL().replace(CricketCommonConstants.PAYMENT_PAGE_PATH,CricketCommonConstants.CONFIRM_PAGE_PATH);
 							flag= checkFormRedirect(redirectionUrl,null, pRequest, pResponse);
						}else{
						flag= checkFormRedirect(getShippingBillingAddressSuccessURL(),
							null, pRequest, pResponse);
						}
					}
				} finally {
					if (getFormError()) {
						try {
							setTransactionToRollbackOnly();
						} catch (SystemException pSystemException) {
							if (isLoggingError()) {
								logError("SystemException", pSystemException);
							}
						}
					}
					// Commit or rolback the transaction depending on the
					// status.
					if (l_oRepeatingRequestMonitor != null) {
						l_oRepeatingRequestMonitor.removeRequestEntry(l_sMethod);
					}
					if (l_oTransaction != null) {
						commitTransaction(l_oTransaction);
					}
				}
			}
			logDebug("CricketShippingGroupFormHandler:handleShippingandBillingAddress()...Exiting from the method "  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
			return flag;
		}
	}

	/**
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void precreateShippingandBillingAddress(
			DynamoHttpServletRequest req, DynamoHttpServletResponse res)
			throws ServletException, IOException {
	}

	

	/** this method is used to create shipping address,billing, account holder address and assign them to atg shipping group,profile
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void createShippingandBillingAddress(
			DynamoHttpServletRequest req, DynamoHttpServletResponse res)
			throws ServletException, IOException {
		if(isLoggingDebug()){
			logDebug("CricketShippingGroupFormHandler:createShippingandBillingAddress()... " + CricketCommonConstants.SESSION_ID + getSessionId()  + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}
		
		String shippingRadioValue = req.getParameter(SHIPPING_ADDRESS_RADIO_VALUE);
		String billingRadioValue  = req.getParameter(BILLING_ADDRESS_RADIO_VALUE);
		setShippingRadioValue(shippingRadioValue);
		setBillingRadioValue(billingRadioValue);
		boolean isAlternativeAddressSeletected = false;
		String normalizationAddressKey = req.getParameter(CricketCommonConstants.ALTERNATIVE_ADDRESS);
		if(null!=normalizationAddressKey){
		HashMap<Integer,AddressVO> normalizationAddressMap= getShippingAddressData().getNormalizedAddressMap();
		Integer keyInt = new Integer(normalizationAddressKey);
		AddressVO normalizationShippingAddress = normalizationAddressMap.get(keyInt);
		if(null!=normalizationShippingAddress){
			isAlternativeAddressSeletected = true;
		 	getShippingAddressData().getShippingAddress().setAddress1((String) normalizationShippingAddress.getAddressLine1());
			if(null !=getShippingAddressData().getShippingAddress().getAddress2()){ // missing this place check
				getShippingAddressData().getShippingAddress().setAddress2(getShippingAddressData().getShippingAddress().getAddress2());
			}
			getShippingAddressData().getShippingAddress().setCity((String) normalizationShippingAddress.getCity());
			getShippingAddressData().getShippingAddress().setCountry((String) normalizationShippingAddress.getCountry());
			getShippingAddressData().getShippingAddress().setStateAddress((String) normalizationShippingAddress.getState());
			getShippingAddressData().getShippingAddress().setPostalCode((String) normalizationShippingAddress.getZipCode());
		
			
			
		 }
		}
		
		ResponseVO responseStatus = null;
		CricketOrderImpl cricketOrder = (CricketOrderImpl) getOrder();
		if(isLoggingDebug()){
			if(!StringUtils.isBlank(cricketOrder.getId())){
				logDebug("  [CricketShippingGroupFormHandler: createShippingandBillingAddress ].. Order Id :.." +cricketOrder.getId());
			}
		}
		String workOrderType = cricketOrder.getWorkOrderType();
		// Setting Shipping Method value
				setShippingMethodType();
		if (getProfile().isTransient()) {
			ValidateAddressResponseVO validateAccountAddressData = getAddressTools().validateAccountAddressData(getAccountHolderAddressData().getAccountAddress(),false,getOrder().getId());
			if(null != validateAccountAddressData){			
				responseStatus = validateAccountAddressData.getResponse();
				if(null!=responseStatus && ESP_RESPONSE_CODE_SUCCESS.equals(responseStatus.getCode())){
					if(validateAccountAddressData.getGeoCodeValue() == null){
						addFormException(new DropletException(INVALID_ACCOUNT_ADDRESS));
						return;
					}else if(validateAccountAddressData.getGeoCodeValue().equals(CricketESPConstants.FAILED_TO_GEOCODE)){
						addFormException(new DropletException(CricketESPConstants.GEOCODE_API_TIMEOUT));
						return;
					}
					List<AddressVO> normalizedAddresses = validateAccountAddressData.getNormalizedAddresses();
					if (null != normalizedAddresses
								&& normalizedAddresses.size() > 0) {						
							AddressVO addressVO = normalizedAddresses.get(0);
							getAccountHolderAddressData().getAccountAddress().setAddress1(addressVO.getAddressLine1());
							if(null !=(String) addressVO.getAddressLine2()){
								getAccountHolderAddressData().getAccountAddress().setAddress2((String) addressVO.getAddressLine2());
							}
							getAccountHolderAddressData().getAccountAddress().setCity(addressVO.getCity());
							getAccountHolderAddressData().getAccountAddress().setCountry(addressVO.getCountry());
							getAccountHolderAddressData().getAccountAddress().setStateAddress(addressVO.getState());
							getAccountHolderAddressData().getAccountAddress().setPostalCode(addressVO.getZipCode());
						
					}
					createAccountAddressinProfile(req, res);
					// setting phone and email to order repository - otherwise we can find the anonymous user details.
					((CricketOrderImpl)getOrder()).setPhoneNumber(getAccountHolderAddressData().getAccountAddress().getPhoneNumber());
					((CricketOrderImpl)getOrder()).setEmailId(getAccountHolderAddressData().getAccountAddress().getEmail());
				}else if(null!=responseStatus && ESP_TIMEOUT.equals(responseStatus.getCode())){
					addFormException(new DropletException(ESP_TIMEOUT));
					return;
				}else {
					addFormException(new DropletException(INVALID_ACCOUNT_ADDRESS));
					return;
				}
			}else {
				addFormException(new DropletException(INVALID_ACCOUNT_ADDRESS));
				return;
			}
		} else {
			// for logged in user also need to be update the home address
			createAccountAddressinProfile(req, res);
			((CricketOrderImpl)getOrder()).setPhoneNumber(getAccountHolderAddressData().getAccountAddress().getPhoneNumber());
			((CricketOrderImpl)getOrder()).setEmailId(getUserAccountInformation().getEmailAddress());
		}
		if (DIFFERENT_ADDRESS.equalsIgnoreCase(shippingRadioValue)  ) {
			((CricketOrderImpl)getOrder()).setShippingAddressType("DifferentAddress");
			if(!isAlternativeAddressSeletected){
			ValidateAddressResponseVO validateShippingAddressData = getAddressTools().validateShippingAddressData(
																		getShippingAddressData().getShippingAddress(),
																		true, getOrder().getId());
			if (null != validateShippingAddressData) {				
				// getShippingAddressData().setNormalizedAddresses(validateShippingAddressData.getNormalizedAddresses());
				responseStatus = validateShippingAddressData.getResponse();
				if(null!=responseStatus && ESP_RESPONSE_CODE_SUCCESS.equals(responseStatus.getCode())){
					List<AddressVO> normalizedAddresses = validateShippingAddressData
							.getNormalizedAddresses();
					if (null != normalizedAddresses
							&& normalizedAddresses.size() > 0) {	
						HashMap<Integer,AddressVO> mapOfNormalizationAddress = new HashMap<Integer,AddressVO>();
						if(normalizedAddresses.size()==1 && normalizedAddresses.get(0) != null && normalizedAddresses.get(0).getZipCode().equals(getShippingAddressData().getShippingAddress().getPostalCode())){
							AddressVO addressVO = normalizedAddresses.get(0);
							getShippingAddressData().getShippingAddress().setAddress1((String) addressVO.getAddressLine1());
							if(null !=(String) addressVO.getAddressLine2()){
								getShippingAddressData().getShippingAddress().setAddress2((String) addressVO.getAddressLine2());
							}
							getShippingAddressData().getShippingAddress().setCity((String) addressVO.getCity());
							getShippingAddressData().getShippingAddress().setCountry((String) addressVO.getCountry());
							getShippingAddressData().getShippingAddress().setStateAddress((String) addressVO.getState());
							getShippingAddressData().getShippingAddress().setPostalCode((String) addressVO.getZipCode());
						
					}else{
						//
						 int key = 1;
						  for(AddressVO  norAddresses:normalizedAddresses){
							  mapOfNormalizationAddress.put(key, norAddresses);
							  ++key;
						  }
						  getShippingAddressData().setNormalizedAddressMap(mapOfNormalizationAddress);
						  addFormException(new DropletException(INVALID_SHIPPING_ADDRESS));
						  return;
						}
					}else {
						addFormException(new DropletException(INVALID_SHIPPING_ADDRESS));
						return;
					}
				}else if(null!=responseStatus && ESP_TIMEOUT.equals(responseStatus.getCode())){
					addFormException(new DropletException(ESP_TIMEOUT));
					return;
				}else {
					addFormException(new DropletException(INVALID_SHIPPING_ADDRESS));
					return;
				}
			
				}else {
					addFormException(new DropletException(INVALID_SHIPPING_ADDRESS));
					return;
				}
			}
			getAddressTools().assignShippingAddressToShippingGroup(getShippingAddressData().getShippingAddress(),getOrder(), getProfile(),getShippingAddressData().getShippingMethodValue());
			
			
		} else if(SAME_AS_ACCOUNT_HOLDER_ADDRESS.equalsIgnoreCase(shippingRadioValue)||CricketESPConstants.TRANSACTION_TYPE_RRC.equalsIgnoreCase(workOrderType)){
			if(isAlternativeAddressSeletected){
				getShippingAddressData().setShippingAddressAsAccountHolderAddress(false);
				((CricketOrderImpl)getOrder()).setShippingAddressType("DifferentAddress");
			}else{
				getShippingAddressData().setShippingAddressAsAccountHolderAddress(true);
				((CricketOrderImpl)getOrder()).setShippingAddressType("AccountAddress");
			}
			//shippingAddressAsAccountHolderAddress=true;
			boolean status = copyExistingAddressFiledToAddressData(getAccountHolderAddressData().getAccountAddress(),isAlternativeAddressSeletected, workOrderType);
			if(status){
				getAddressTools().assignShippingAddressToShippingGroup(getShippingAddressData().getShippingAddress(),getOrder(), getProfile(),getShippingAddressData().getShippingMethodValue());
			}else {
				//addFormException(new DropletException(INVALID_SHIPPING_ADDRESS));
				return;
			}
		}
 		if(!getFormError()) {
			if (DIFFERENT_ADDRESS.equalsIgnoreCase(billingRadioValue)) {
				((CricketOrderImpl)getOrder()).setBillingAddressType("DifferentAddress");
				ValidateAddressResponseVO validateBillingAddressData = getAddressTools().validateBillingAddressData(getBillingAddressData().getBillingAddress(),false, getOrder().getId());
				if(null != validateBillingAddressData){				
					responseStatus = validateBillingAddressData.getResponse();
					if(null!=responseStatus && ESP_RESPONSE_CODE_SUCCESS.equals(responseStatus.getCode())){
						if(validateBillingAddressData.getGeoCodeValue() == null){
							addFormException(new DropletException(INVALID_BILLING_ADDRESS));
							return;
						}else if(validateBillingAddressData.getGeoCodeValue().equals(CricketESPConstants.FAILED_TO_GEOCODE)){
							addFormException(new DropletException(CricketESPConstants.GEOCODE_API_TIMEOUT));
							return;
						}
						List<AddressVO> normalizedAddresses = validateBillingAddressData.getNormalizedAddresses();	
						if (null != normalizedAddresses
								&& normalizedAddresses.size() > 0) {
							AddressVO addressVO = normalizedAddresses.get(0);
							getBillingAddressData().getBillingAddress().setAddress1((String) addressVO.getAddressLine1());
							if(null !=(String) addressVO.getAddressLine2()){
								getBillingAddressData().getBillingAddress().setAddress2((String) addressVO.getAddressLine2());
							}
							getBillingAddressData().getBillingAddress().setCity((String) addressVO.getCity());
							getBillingAddressData().getBillingAddress().setCountry((String) addressVO.getCountry());
							getBillingAddressData().getBillingAddress().setStateAddress((String) addressVO.getState());
							getBillingAddressData().getBillingAddress().setPostalCode((String) addressVO.getZipCode());
							}
							createBillingAddressandComponent(req, res);
						
					}else {
						if(null!=responseStatus && ESP_TIMEOUT.equals(responseStatus.getCode())){
							addFormException(new DropletException(ESP_TIMEOUT));	
						}else {
							addFormException(new DropletException(INVALID_BILLING_ADDRESS));
						}				
						return;
					}
				}else {
					addFormException(new DropletException(INVALID_BILLING_ADDRESS));
					return;
				}	
				getShippingAddressData().setBillingAddressAsShippingAddress(false);
				getShippingAddressData().setBillingAddressAsAccountHolderAddress(false);
				
				
			} else if(SAME_AS_ACCOUNT_HOLDER_ADDRESS.equalsIgnoreCase(billingRadioValue)){
				getShippingAddressData().setBillingAddressAsAccountHolderAddress(true);
				//below setting is required as the same method copyExistingAddressFiledToAddressData(address) is called for setting both 
				//shipping address and billing address to account address 
				getShippingAddressData().setShippingAddressAsAccountHolderAddress(false);
				//billingAddressAsAccountHolderAddress = true;
				copyExistingAddressFiledToAddressData(getAccountHolderAddressData().getAccountAddress(),isAlternativeAddressSeletected, null);
				
				((CricketOrderImpl)getOrder()).setBillingAddressType("AccountAddress");
				
			} else if(SAME_AS_SHIPPING_ADDRESS.equalsIgnoreCase(billingRadioValue)){
				getShippingAddressData().setBillingAddressAsShippingAddress(true);
				//billingAddressAsShippingAddress = true;
				copyShippingAddressFiledToBillingAddressData(getShippingAddressData().getShippingAddress());
				
				((CricketOrderImpl)getOrder()).setBillingAddressType("ShippingAddress");
			}
		}
 		if(isLoggingDebug()){
			logDebug("CricketShippingGroupFormHandler:createShippingandBillingAddress()...Exiting from the method " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}
	}
	
	
	/**
	 * 
	 */
	public void setShippingMethodType() {
		String shippingMethod = getShippingAddressData().getShippingAddress().address1;
		if (shippingMethod != null) {
			shippingMethod = (shippingMethod.replaceAll("[^a-zA-Z+]", CricketCommonConstants.EMPTY_STRING))
					.toUpperCase();
			boolean containPOXAddress = shippingMethod
					.contains(CricketCommonConstants.POBOX);
			if (containPOXAddress) {
				getShippingAddressData().setShippingMethodValue(
						CricketCommonConstants.POBOX);
			} else {
				getShippingAddressData().setShippingMethodValue(
						CricketCommonConstants.OVERNIGHT);
			}
		}
	}

	/**
	 * @param req
	 * @param res
	 */
	private void createAccountAddressinProfile(DynamoHttpServletRequest req,
			DynamoHttpServletResponse res) {
			MutableRepositoryItem profile = (MutableRepositoryItem) getProfile();
			ContactInfo shippingAddr = getAddressTools().getNewAddress(getAccountHolderAddressData().getAccountAddress());
			MutableRepositoryItem copyAddressTOContactInfo = getAddressTools().copyAddressTOContactInfo(shippingAddr,profile,getOrder().getId());
			CricketOrderImpl order = (CricketOrderImpl) getOrder();
			order.setAccountAddressId(copyAddressTOContactInfo.getRepositoryId());
			profile.setPropertyValue(HOME_ADDRESS, copyAddressTOContactInfo);			
			logDebug("CricketShippingGroupFormHandler:createAccountAddressinProfile()...Exiting from the method " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
	}

	/** this method will be used to call different ESP Calls - 
	 *  - SendMessageESP - to send the welcome email
	 *  - InquireDeliveryEstimate - to get the delivery date of the current order from UPS 
	 *  - CreateActivationQuote - to get Service tax,MDN and Meid for new activation or add a line from billing system (NEC)
	 *  - UpdateSubscriber- to get the service tax for change plan and change feature from billing system (NEC)
	 *  - ManageSale  - to BillingQuoteId from POS , this billingQuote id will be used in most of the ESP services.
	 *  - FinalizeSale - to finalize the order in POS - we are calling this as per existing html logs.
	 *  - ManageSaleItem - to get the sale Tax from POS.
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void postcreateShippingandBillingAddress(
			DynamoHttpServletRequest req, DynamoHttpServletResponse res)
			throws ServletException, IOException {
		if(isLoggingDebug()){
			logDebug("Order Id :"+getOrder().getId());
			logDebug("AccountAddress :"+getAccountHolderAddressData().getAccountAddress().toString());
			logDebug("SHipping Address :"+getShippingAddressData().getShippingAddress().toString());
			logDebug("Billing Address :"+getBillingAddressData().getBillingAddress().toString());
			logDebug("CricketShippingGroupFormHandler:postcreateShippingandBillingAddress()... " + CricketCommonConstants.SESSION_ID + getSessionId()  + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}		
		 CricketOrderImpl order = (CricketOrderImpl)getOrder(); 
		 order.setShippingMethod(getShippingAddressData().getShippingMethodValue());
		 PipelineResult result = applyEspServiceChainOnOrder(order);
		 processPiplelineResult(result);    
		 
		// now clearing main payment data
			getPaymentData().setAbpFlag(false);
			getPaymentData().setFristName(EMPTY_STRING);
			getPaymentData().setLastName(EMPTY_STRING);
			getPaymentData().setCardNumber(EMPTY_STRING);
			getPaymentData().setExpirationMonth(EMPTY_STRING);
			getPaymentData().setExpirationYear(EMPTY_STRING);
			getPaymentData().setVestaToken(EMPTY_STRING);
			getPaymentData().setCreditCardType(EMPTY_STRING);
			getPaymentData().setCvcNumber(EMPTY_STRING);
			//  now clearing abp
			
			getAbpPaymentData().setAbpFlag(false);
			getAbpPaymentData().setFristName(EMPTY_STRING);
			getAbpPaymentData().setLastName(EMPTY_STRING);
			getAbpPaymentData().setCardNumber(EMPTY_STRING);
			getAbpPaymentData().setExpirationMonth(EMPTY_STRING);
			getAbpPaymentData().setExpirationYear(EMPTY_STRING);
			getAbpPaymentData().setVestaToken(EMPTY_STRING);
			getAbpPaymentData().setCreditCardType(EMPTY_STRING);
			getAbpPaymentData().setCvcNumber(EMPTY_STRING);
			logDebug("CricketShippingGroupFormHandler:postcreateShippingandBillingAddress()...Exiting from the method " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
			 getShippingAddressData().setNormalizedAddressMap(null);
	
	} // end of postcreateShippingandBillingAddress
 
	
 
	/**
	 * @param order
	 * @return
	 */
	public PipelineResult applyEspServiceChainOnOrder(CricketOrderImpl order) {
		// prepare params
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(PipelineConstants.ORDER, order);
		params.put(CRICKET_PROFILE,getCricketProfile());
		params.put(ESP_SERVICE_RESPONSE_DATA, getEspServiceResponseData());
		params.put(ACCOUNT_HOLDER_ADDDRESS_DATA, getAccountHolderAddressData());
		params.put(SHIPPING_ADDRESS_DATA, getShippingAddressData());
		params.put(BUSINESS_ADDRESS_DATA, getBillingAddressData());
		params.put(PROFILE_ITEM, getProfile());
		params.put(LOCATION_INFO,getLocationInfo());
		params.put(USER_ACCOUNT_INFORMATION,getUserAccountInformation());
		params.put(USER_SESSION_BEAN,getUserSessionBean()); 	 
		 
		// delegate
		try {
			return getOrderManager().getPipelineManager().runProcess(
					getCricketESPServicesChainName(), params);
		} catch (RunProcessException e) {
			logError("Unexpected exception while executing business rules", e);
			return null;
		}
	}
	private boolean processPiplelineResult(final PipelineResult result) {
		if(result != null && result.hasErrors()) {			 
				logError("processPiplelineResult() - Error : "  + result.toString());				
				  Object errorKeys[] = result.getErrorKeys();
				  Object error = null;
				  String errorKey =  null;
		            if(errorKeys != null)
		            {
		                for(int index = 0; index < errorKeys.length; index++)
		                {
		                      error = result.getError(errorKeys[index]);
		                    if(isLoggingDebug())
		                        logDebug((new StringBuilder()).append("PipelineError: key=").append(errorKeys[index]).append("; error=").append(error).toString());
		                    if(error == null)
		                        continue;
		                     errorKey = errorKeys[index].toString();
		                    addFormException(new DropletException(errorKey));				
		                }
		            }					
				 return false;		 
		}		
		return true;
	}	

	

	/** this method is used to get the contactInfo object from repository.
	 * @param contactInfoItemId
	 * @return
	 */
	public MutableRepositoryItem getContactInfoRepositoryItem(
			String contactInfoItemId) {
		Repository profileMutable = (Repository) getProfile().getRepository();
		MutableRepositoryItem contactInfoItem = null;
		try {
			contactInfoItem = (MutableRepositoryItem) profileMutable.getItem(
					contactInfoItemId, "contactInfo");
		} catch (RepositoryException e) {
			logError(e);
		}
		return contactInfoItem;
	}

	// ** BILLING ADDRESS

	/**
	 * This method is used to assign the given address(from Form) to selected
	 * payment type. if the saveToProfile flag is true , it will add the same
	 * address as a default address.
	 * 
	 * @param pRequest
	 * @param pResponse
	 */
	public void createBillingAddressandComponent(
			DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {

		ContactInfo newBillingAddress = null;
		newBillingAddress = getAddressTools().getNewBillingAddress(getBillingAddressData().getBillingAddress());
		//getAddressTools().copyAddressTOContactInfo(newBillingAddress,(MutableRepositoryItem) getProfile(),null);
	}

	public boolean  copyExistingAddressFiledToAddressData(CricketAddress pAccountAddress,boolean alternativeAddressFlag, String workOrderType) {		
		if(!alternativeAddressFlag){
		if (getShippingAddressData().isShippingAddressAsAccountHolderAddress()) {
			getAddressTools().copyAddressFiledToShippingAddressData(getShippingAddressData(),pAccountAddress);
			
			//below if condition is added to fix QC 8106, 8107 
			//skipping ShippingAddress validation in case of Change plan/ Change feature
			if(workOrderType == null || !(CricketESPConstants.TRANSACTION_TYPE_RRC.equalsIgnoreCase(workOrderType))){
			//TODO Need to check with Client what is the use of Same as shipping Address.
			 ValidateAddressResponseVO validateShippingAddressData = getAddressTools().validateShippingAddressData(getShippingAddressData().getShippingAddress(),true, getOrder().getId());			
			
				if (null != validateShippingAddressData) {
					ResponseVO errorInfo = validateShippingAddressData.getResponse();
					if(!"0".equals(errorInfo.getCode())){
						if(ESP_TIMEOUT.equals(errorInfo.getCode())){
							addFormException(new DropletException(errorInfo.getCode()));
						}else{
							addFormException(new DropletException(INVALID_SHIPPING_ADDRESS));
						}
						 return false; 
					}
						//setOpenAddressModel(Boolean.TRUE);
						List<AddressVO> normalizedAddresses = validateShippingAddressData.getNormalizedAddresses(); 
					if(null!=normalizedAddresses && normalizedAddresses.size()>0){	
						HashMap<Integer,AddressVO> mapOfNormalizationAddress = new HashMap<Integer,AddressVO>();
						if(normalizedAddresses.size()==1 && normalizedAddresses.get(0) != null && normalizedAddresses.get(0).getZipCode().equals(getShippingAddressData().getShippingAddress().getPostalCode())){
							AddressVO addressVO = normalizedAddresses.get(0);
							getShippingAddressData().getShippingAddress().setAddress1((String) addressVO.getAddressLine1());
							if(null !=(String) addressVO.getAddressLine2()){
								getShippingAddressData().getShippingAddress().setAddress2((String) addressVO.getAddressLine2());
							}
							getShippingAddressData().getShippingAddress().setCity((String) addressVO.getCity());
							getShippingAddressData().getShippingAddress().setCountry((String) addressVO.getCountry());
							getShippingAddressData().getShippingAddress().setStateAddress((String) addressVO.getState());
							getShippingAddressData().getShippingAddress().setPostalCode((String) addressVO.getZipCode());
						}else{
						//
						 int key = 1;
						  for(AddressVO  norAddresses:normalizedAddresses){
							  mapOfNormalizationAddress.put(key, norAddresses);
							  ++key;
						  }
						  getShippingAddressData().setNormalizedAddressMap(mapOfNormalizationAddress);
						  addFormException(new DropletException(INVALID_SHIPPING_ADDRESS));
						  return false;
						}
					}else{
						//no result, is a fail
						addFormException(new DropletException(INVALID_SHIPPING_ADDRESS));
						 return false;
					}
				}
			}
		  }
		}
		if (getShippingAddressData().isBillingAddressAsAccountHolderAddress()) {
			getAddressTools().copyAddressFiledToBillingAddressData(pAccountAddress,getBillingAddressData());
			// like all fields
		}
		return true;
	}

	public void copyShippingAddressFiledToBillingAddressData(CricketShippingAddress pAccountAddress) {		
		
		if (getShippingAddressData().isBillingAddressAsShippingAddress()) {
			getBillingAddressData().getBillingAddress().setFirstName((String) pAccountAddress.getFirstName());
			getBillingAddressData().getBillingAddress().setLastName((String) pAccountAddress.getLastName());
			getBillingAddressData().getBillingAddress().setAddress1((String) pAccountAddress.getAddress1());
			getBillingAddressData().getBillingAddress().setAddress2((String) pAccountAddress.getAddress2());
			getBillingAddressData().getBillingAddress().setCompanyName((String) pAccountAddress.getCompanyName());
			getBillingAddressData().getBillingAddress().setCity((String) pAccountAddress.getCity());
			getBillingAddressData().getBillingAddress().setCountry((String) pAccountAddress.getCountry());
			getBillingAddressData().getBillingAddress().setStateAddress((String) pAccountAddress.getStateAddress());
			getBillingAddressData().getBillingAddress().setMiddleName((String) pAccountAddress.getMiddleName());
			getBillingAddressData().getBillingAddress().setPostalCode((String) pAccountAddress.getPostalCode());
			ValidateAddressResponseVO validateBillingAddressData = getAddressTools().validateBillingAddressData(getBillingAddressData().getBillingAddress(),false, getOrder().getId());
			if (null != validateBillingAddressData) {
				//setOpenAddressModel(Boolean.TRUE);
				ResponseVO errorInfo = validateBillingAddressData.getResponse();
				if(!"0".equals(errorInfo.getCode())){
					if(null!=errorInfo && ESP_TIMEOUT.equals(errorInfo.getCode())){
						addFormException(new DropletException(ESP_TIMEOUT));	
					}else {
						addFormException(new DropletException(INVALID_BILLING_ADDRESS));
					}
					 return;
				}else{
					if(validateBillingAddressData.getGeoCodeValue() == null){
						addFormException(new DropletException(INVALID_BILLING_ADDRESS));
						return;
					}else if(validateBillingAddressData.getGeoCodeValue().equals(CricketESPConstants.FAILED_TO_GEOCODE)){
						addFormException(new DropletException(CricketESPConstants.GEOCODE_API_TIMEOUT));
						return;
					}
				}
				List<AddressVO> normalizedAddresses = validateBillingAddressData.getNormalizedAddresses();
			if(null!=normalizedAddresses && normalizedAddresses.size()>0){				
				AddressVO addressVO = normalizedAddresses.get(0);
				getBillingAddressData().getBillingAddress().setAddress1((String) addressVO.getAddressLine1());
				if(null !=(String) addressVO.getAddressLine2()){
					getBillingAddressData().getBillingAddress().setAddress2((String) addressVO.getAddressLine2());
				}
				getBillingAddressData().getBillingAddress().setCity((String) addressVO.getCity());
				getBillingAddressData().getBillingAddress().setCountry((String) addressVO.getCountry());
				getBillingAddressData().getBillingAddress().setStateAddress((String) addressVO.getState());
				getBillingAddressData().getBillingAddress().setPostalCode((String) addressVO.getZipCode());
				
			}
		}
			// like all fields
		}
	}

	public void sendEmailtoAdmin() {

	}

	/**
	 * @return the shippingBillingAddressErrorURL
	 */
	public String getShippingBillingAddressErrorURL() {
		return shippingBillingAddressErrorURL;
	}

	/**
	 * @param shippingBillingAddressErrorURL
	 *            the shippingBillingAddressErrorURL to set
	 */
	public void setShippingBillingAddressErrorURL(
			String shippingBillingAddressErrorURL) {
		this.shippingBillingAddressErrorURL = shippingBillingAddressErrorURL;
	}

	/**
	 * @return the shippingBillingAddressSuccessURL
	 */
	public String getShippingBillingAddressSuccessURL() {
		return shippingBillingAddressSuccessURL;
	}

	/**
	 * @param shippingBillingAddressSuccessURL
	 *            the shippingBillingAddressSuccessURL to set
	 */
	public void setShippingBillingAddressSuccessURL(
			String shippingBillingAddressSuccessURL) {
		this.shippingBillingAddressSuccessURL = shippingBillingAddressSuccessURL;
	}

	/**
	 * @return the isNewAccountHolderAddress
	 */
	public boolean isNewAccountHolderAddress() {
		return newAccountHolderAddress;
	}

	/**
	 * @param isNewAccountHolderAddress
	 *            the isNewAccountHolderAddress to set
	 */
	public void setNewAccountHolderAddress(boolean isNewAccountHolderAddress) {
		this.newAccountHolderAddress = isNewAccountHolderAddress;
	}

	/**
	 * @return the isNewShippingAddress
	 */
	public boolean isNewShippingAddress() {
		return newShippingAddress;
	}

	/**
	 * @param newShippingAddress
	 *            the isNewShippingAddress to set
	 */
	public void setNewShippingAddress(boolean newShippingAddress) {
		this.newShippingAddress = newShippingAddress;
	}

	/**
	 * @return the isNewBillingAddress
	 */
	public boolean isNewBillingAddress() {
		return newBillingAddress;
	}

	/**
	 * @param newBillingAddress
	 *            the isNewBillingAddress to set
	 */
	public void setNewBillingAddress(boolean newBillingAddress) {
		this.newBillingAddress = newBillingAddress;
	}

	
	/**
	 * @return the accountHolderAddressData
	 */
	public CricketAccountHolderAddressData getAccountHolderAddressData() {
		return accountHolderAddressData;
	}

	/**
	 * @param accountHolderAddressData
	 *            the accountHolderAddressData to set
	 */
	public void setAccountHolderAddressData(
			CricketAccountHolderAddressData accountHolderAddressData) {
		this.accountHolderAddressData = accountHolderAddressData;
	}

	/**
	 * @return the shippingAddressData
	 */
	public CricketShippingAddressData getShippingAddressData() {
		return shippingAddressData;
	}

	/**
	 * @param shippingAddressData
	 *            the shippingAddressData to set
	 */
	public void setShippingAddressData(
			CricketShippingAddressData shippingAddressData) {
		this.shippingAddressData = shippingAddressData;
	}

	/**
	 * @return the billingAddressData
	 */
	public CricketBillingAddressData getBillingAddressData() {
		return billingAddressData;
	}

	/**
	 * @param billingAddressData
	 *            the billingAddressData to set
	 */
	public void setBillingAddressData(
			CricketBillingAddressData billingAddressData) {
		this.billingAddressData = billingAddressData;
	}
	/**
	 * @return the cricketESPAdapter
	 */
	public CricketESPAdapter getCricketESPAdapter() {
		return cricketESPAdapter;
	}

	/**
	 * @param cricketESPAdapter the cricketESPAdapter to set
	 */
	public void setCricketESPAdapter(CricketESPAdapter cricketESPAdapter) {
		this.cricketESPAdapter = cricketESPAdapter;
	}

	/**
	 * @return the addressTools
	 */
	public CricketAddressTools getAddressTools() {
		return addressTools;
	}

	/**
	 * @param addressTools the addressTools to set
	 */
	public void setAddressTools(CricketAddressTools addressTools) {
		this.addressTools = addressTools;
	}

	/**
	 * @return the citySessionInfoObject
	 */
	public CitySessionInfoObject getCitySessionInfoObject() {
		return citySessionInfoObject;
	}

	/**
	 * @param citySessionInfoObject the citySessionInfoObject to set
	 */
	public void setCitySessionInfoObject(CitySessionInfoObject citySessionInfoObject) {
		this.citySessionInfoObject = citySessionInfoObject;
	}

	/**
	 * @return the locationInfo
	 */
	public MyCricketCookieLocationInfo getLocationInfo() {
		return locationInfo;
	}

	/**
	 * @param locationInfo the locationInfo to set
	 */
	public void setLocationInfo(MyCricketCookieLocationInfo locationInfo) {
		this.locationInfo = locationInfo;
	}

	/**
	 * @return the cricketProfile
	 */
	public CricketProfile getCricketProfile() {
		return cricketProfile;
	}

	/**
	 * @param cricketProfile the cricketProfile to set
	 */
	public void setCricketProfile(CricketProfile cricketProfile) {
		this.cricketProfile = cricketProfile;
	}

	/**
	 * @return the emailManager
	 */
	public CricketEmailManager getEmailManager() {
		return emailManager;
	}

	/**
	 * @param pEmailManager the emailManager to set
	 */
	public void setEmailManager(CricketEmailManager pEmailManager) {
		emailManager = pEmailManager;
	}

	/**
	 * @return the espServiceResponseData
	 */
	public EspServiceResponseData getEspServiceResponseData() {
		return espServiceResponseData;
	}

	/**
	 * @param espServiceResponseData the espServiceResponseData to set
	 */
	public void setEspServiceResponseData(
			EspServiceResponseData espServiceResponseData) {
		this.espServiceResponseData = espServiceResponseData;
	}

	/**
	 * @return the openAddressModel
	 */
	public boolean isOpenAddressModel() {
		return openAddressModel;
	}
	/**
	 * @return the userAccountInformation
	 */
	public UserAccountInformation getUserAccountInformation() {
		return userAccountInformation;
	}

	/**
	 * @param openAddressModel the openAddressModel to set
	 */
	public void setOpenAddressModel(boolean openAddressModel) {
		this.openAddressModel = openAddressModel;
	}


	/**
	 * @param userAccountInformation the userAccountInformation to set
	 */
	public void setUserAccountInformation(
			UserAccountInformation userAccountInformation) {
		this.userAccountInformation = userAccountInformation;
	}

	/**
	 * @return the cricketESPServicesChainName
	 */
	public String getCricketESPServicesChainName() {
		return cricketESPServicesChainName;
	}

	/**
	 * @param cricketESPServicesChainName the cricketESPServicesChainName to set
	 */
	public void setCricketESPServicesChainName(String cricketESPServicesChainName) {
		this.cricketESPServicesChainName = cricketESPServicesChainName;
	}

	/**
	 * 
	 * @return
	 */
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}

	/**
	 * 
	 * @param userSessionBean
	 */
	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

	/**
	 * @return the shippingRadioValue
	 */
	public String getShippingRadioValue() {
		return shippingRadioValue;
	}

	/**
	 * @param shippingRadioValue the shippingRadioValue to set
	 */
	public void setShippingRadioValue(String shippingRadioValue) {
		this.shippingRadioValue = shippingRadioValue;
	}

	/**
	 * @return the billingRadioValue
	 */
	public String getBillingRadioValue() {
		return billingRadioValue;
	}

	/**
	 * @param billingRadioValue the billingRadioValue to set
	 */
	public void setBillingRadioValue(String billingRadioValue) {
		this.billingRadioValue = billingRadioValue;
	}
	
	/**
	 * @return the paymentData
	 */
	public CricketPaymentData getPaymentData() {
		return paymentData;
	}

	/**
	 * @param paymentData the paymentData to set
	 */
	public void setPaymentData(CricketPaymentData paymentData) {
		this.paymentData = paymentData;
	}

	/**
	 * @return the abpPaymentData
	 */
	public CricketABPPaymentData getAbpPaymentData() {
		return abpPaymentData;
	}

	/**
	 * @param abpPaymentData the abpPaymentData to set
	 */
	public void setAbpPaymentData(CricketABPPaymentData abpPaymentData) {
		this.abpPaymentData = abpPaymentData;
	}

	public MutableRepository getProfileAdapterRepository() {
		return profileAdapterRepository;
	}

	public void setProfileAdapterRepository(
			MutableRepository pProfileAdapterRepository) {
		profileAdapterRepository = pProfileAdapterRepository;
	}
	
}
