package com.cricket.shipping.tools;

import static com.cricket.common.constants.CricketESPConstants.ESP_BUSINESS_EXCEPTIONS;
import static com.cricket.common.constants.CricketESPConstants.ESP_RESPONSE_CODE_SUCCESS;
import static com.cricket.common.constants.CricketESPConstants.ESP_SYSTEM_EXCEPTIONS;
import static com.cricket.common.constants.CricketESPConstants.ESP_TIMEOUT;
import static com.cricket.common.constants.CricketESPConstants.READ_TIMED_OUT;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import atg.commerce.order.DuplicateShippingGroupException;
import atg.commerce.order.HardgoodShippingGroup;
import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.Order;
import atg.core.util.ContactInfo;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.ServletUtil;

import com.cricket.checkout.order.CricketBillingAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.checkout.utils.CricketAddress;
import com.cricket.checkout.utils.CricketBillingAddress;
import com.cricket.checkout.utils.CricketShippingAddress;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.payment.CricketCreditCard;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.AddressVO;
import com.cricket.integration.esp.vo.ResponseVO;
import com.cricket.integration.esp.vo.ValidateAddressRequestVO;
import com.cricket.integration.esp.vo.ValidateAddressResponseVO;

public class CricketAddressTools extends GenericService {
	
	
	/* holds CricketESPAdapter to interact with ESP layer*/
	private CricketESPAdapter espAdapter;
 
	/* gets CricketESPAdapter instance*/
	public CricketESPAdapter getEspAdapter() {
		return espAdapter;
	}
	/* sets CricketESPAdapter instance*/
	public void setEspAdapter(CricketESPAdapter espAdapter) {
		this.espAdapter = espAdapter;
	}
	
	private int currentRetryCount = 0;
	private boolean isMaxRetryCountReached = false;

	/**
	 * @param pAddress
	 * @param flag
	 * @param pOrderId
	 * @return
	 */
	public ValidateAddressResponseVO validateAccountAddressData(CricketAddress pAddress, boolean flag, String pOrderId) {
		
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketAddressTools->validateAccountAddressData()]: Entering into validateAccountAddressData()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		AddressVO addressVO = null;
		ValidateAddressResponseVO validateAddressResponseVO = null;
		
		if(pAddress != null){	
			addressVO = new AddressVO();
			addressVO.setAddressLine1(pAddress.getAddress1());
			addressVO.setCity(pAddress.getCity());
			addressVO.setCountry(pAddress.getCountry());
			addressVO.setState(pAddress.getStateAddress());	 
			addressVO.setZipCode(pAddress.getPostalCode());
			
			validateAddressResponseVO = validateAddressData(pAddress, flag, pOrderId, addressVO, "validateAccountAddressData");
		}
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketAddressTools->validateAccountAddressData()]: Exiting from validateAccountAddressData()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return validateAddressResponseVO;		
	}
	
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	/**
	 * @param pAddress
	 * @param flag
	 * @param pOrderId
	 * @return
	 */
	public ValidateAddressResponseVO validateShippingAddressData(CricketShippingAddress pAddress, boolean flag, String pOrderId) {
	 
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketAddressTools->validateShippingAddressData()]: Entering into validateShippingAddressData()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		AddressVO addressVO = null;
		ValidateAddressResponseVO validateAddressResponseVO = null;
		
		if(pAddress != null){	
			addressVO = new AddressVO();
			addressVO.setAddressLine1(pAddress.getAddress1());
			addressVO.setCity(pAddress.getCity());
			addressVO.setCountry(pAddress.getCountry());
			addressVO.setState(pAddress.getStateAddress());	 
			addressVO.setZipCode(pAddress.getPostalCode());
			//flag=false;
			validateAddressResponseVO = validateAddressData(pAddress, flag, pOrderId, addressVO, "validateShippingAddressData");
		}
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketAddressTools->validateShippingAddressData()]: Exiting from validateShippingAddressData()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return validateAddressResponseVO;		
	}
	
	/**
	 * @param pAddress
	 * @param flag
	 * @param pOrderId
	 * @return
	 */
	public ValidateAddressResponseVO validateBillingAddressData(CricketBillingAddress pAddress,boolean flag, String pOrderId) {
		
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketAddressTools->validateBillingAddressData()]: Entering into validateBillingAddressData()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		AddressVO addressVO = null;
		ValidateAddressResponseVO validateAddressResponseVO = null;
		
		if(pAddress != null){	
			addressVO = new AddressVO();
			addressVO.setAddressLine1(pAddress.getAddress1());
			addressVO.setCity(pAddress.getCity());
			addressVO.setCountry(pAddress.getCountry());
			addressVO.setState(pAddress.getStateAddress());	 
			addressVO.setZipCode(pAddress.getPostalCode());
			
			validateAddressResponseVO = validateAddressData(pAddress, flag, pOrderId, addressVO, "validateBillingAddressData");
		}
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketAddressTools->validateBillingAddressData()]: Exiting from validateBillingAddressData()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return validateAddressResponseVO;
	}
	
	/**
	 * @param pAddress
	 * @param flag
	 * @param pOrderId
	 * @param addressVO
	 * @param methodName
	 * @return
	 */
	private ValidateAddressResponseVO validateAddressData(
			Object pAddress, boolean flag, String pOrderId,
			AddressVO addressVO, String methodName) {
		ValidateAddressResponseVO validateAddressResponseVO = null;
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		 
			// Getting the Page url
		
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logInfo("[VadlidateAddress ESP->validateAddressData()]:Entering into validateAddressData()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.CONVERSATION_ID + getEspAdapter().getConversationId() +CricketCommonConstants.PAGE_URL + pageURL);
		 
		try{			
			ValidateAddressRequestVO requestVO = null;			
			long starttime = 0L;
			long endtime = 0L;
			
			requestVO = new ValidateAddressRequestVO();
			requestVO.setAddress(addressVO);
			requestVO.setShippingAddress(false);
			
			starttime = System.currentTimeMillis();
			validateAddressResponseVO = getEspAdapter().validateAddress(requestVO,flag,pOrderId);
		    endtime = System.currentTimeMillis();
	    
		    logInfo("[VadlidateAddress ESP->" + methodName + "()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));		
			logInfo("[VadlidateAddress ESP->" + methodName + "()]: Validating address completed...Exiting");			
		}
		catch (ServiceException se) {
			logError("[VadlidateAddress ESP->" + methodName +"()]:"+CricketESPConstants.WHOOP_KEYWORD + " ServiceException while getting validateAddress response: " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.CONVERSATION_ID + getEspAdapter().getConversationId() + CricketCommonConstants.PAGE_URL + pageURL,se);
 			validateAddressResponseVO = setExceptionInResponseVO(validateAddressResponseVO, ESP_SYSTEM_EXCEPTIONS);			
		}		
		catch (ESPApplicationException eae) {	
			logError("[VadlidateAddress ESP->" + methodName +"()]:"+CricketESPConstants.WHOOP_KEYWORD + " ESPApplicationException while getting validateAddress response:" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.CONVERSATION_ID + getEspAdapter().getConversationId() + CricketCommonConstants.PAGE_URL + pageURL,eae);
 			
			String errorCode = getEspAdapter().getEspFaultCode(eae);			
			if(ESP_TIMEOUT.equalsIgnoreCase(errorCode)){
				validateAddressResponseVO = retry(pAddress, flag, pOrderId, methodName);
				if(validateAddressResponseVO == null){
					validateAddressResponseVO = setExceptionInResponseVO(validateAddressResponseVO, errorCode);
				}
			} else{
				validateAddressResponseVO = setExceptionInResponseVO(validateAddressResponseVO, ESP_BUSINESS_EXCEPTIONS);				
			}			
		
		} catch (RemoteException re) {
			logError("[VadlidateAddress ESP->" + methodName +"()]:"+CricketESPConstants.WHOOP_KEYWORD + " RemoteException while getting response:" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.CONVERSATION_ID + getEspAdapter().getConversationId() + CricketCommonConstants.PAGE_URL + pageURL,re);
 			if(re.getCause() != null && re.getCause().getMessage() != null && re.getCause().getMessage().contains(READ_TIMED_OUT)){				
				validateAddressResponseVO = retry(pAddress, flag, pOrderId, methodName);
				if(validateAddressResponseVO == null){
					validateAddressResponseVO = setExceptionInResponseVO(validateAddressResponseVO, ESP_TIMEOUT);
				}
			}						
		}
		catch (Exception e){
			logError("[VadlidateAddress ESP->" + methodName +"()]:"+CricketESPConstants.WHOOP_KEYWORD + " Exception while getting response:" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.CONVERSATION_ID + getEspAdapter().getConversationId() + CricketCommonConstants.PAGE_URL + pageURL,e);
 			validateAddressResponseVO = setExceptionInResponseVO(validateAddressResponseVO, ESP_SYSTEM_EXCEPTIONS);
		}
		return validateAddressResponseVO;
	}

	/**
	 * @param validateAddressResponseVO
	 * @param exceptionType
	 * @return
	 */
	private ValidateAddressResponseVO setExceptionInResponseVO(ValidateAddressResponseVO validateAddressResponseVO, String exceptionType) {		
		ResponseVO responseStatus = new ResponseVO();	
		responseStatus.setCode(exceptionType);
		responseStatus.setDescription(exceptionType);
		if(validateAddressResponseVO == null){
			validateAddressResponseVO = new ValidateAddressResponseVO();
			validateAddressResponseVO.setResponse(responseStatus); 
		}
		return validateAddressResponseVO;
	}
	
	/**
	 * @param pAddressVo
	 * @return ContactInfo
	 */
	public ContactInfo getNewAddress(CricketAddress pAddressVo) {
		ContactInfo newAccountAddress = new ContactInfo();
		newAccountAddress.setFirstName(pAddressVo.getFirstName());
		newAccountAddress.setLastName(pAddressVo.getLastName());
		newAccountAddress.setCompanyName(pAddressVo.getCompanyName());
		newAccountAddress.setAddress1(pAddressVo.getAddress1());
		newAccountAddress.setAddress2(pAddressVo.getAddress2());
		newAccountAddress.setCity(pAddressVo.getCity());
		newAccountAddress.setState(pAddressVo.getStateAddress());
		newAccountAddress.setCountry(pAddressVo.getCountry());
		newAccountAddress.setPostalCode(pAddressVo.getPostalCode());
		newAccountAddress.setPhoneNumber(pAddressVo.getPhoneNumber());
		return newAccountAddress;

	}
	
	/**
	 * @param pAddressVo
	 * @return ContactInfo
	 */
	public ContactInfo getNewShippingAddress(CricketShippingAddress pAddressVo) {
		ContactInfo newShippingAddress = new ContactInfo();
		newShippingAddress.setFirstName(pAddressVo.getFirstName());
		newShippingAddress.setLastName(pAddressVo.getLastName());
		newShippingAddress.setCompanyName(pAddressVo.getCompanyName());
		newShippingAddress.setAddress1(pAddressVo.getAddress1());
		newShippingAddress.setAddress2(pAddressVo.getAddress2());
		newShippingAddress.setCity(pAddressVo.getCity());
		newShippingAddress.setState(pAddressVo.getStateAddress());
		newShippingAddress.setCountry(pAddressVo.getCountry());
		newShippingAddress.setPostalCode(pAddressVo.getPostalCode());
		newShippingAddress.setPhoneNumber(pAddressVo.getPhoneNumber());
		newShippingAddress.setEmail(pAddressVo.getEmail());
		return newShippingAddress;

	}
	
	/**
	 * @param pAddressVo
	 * @return ContactInfo
	 */
	public ContactInfo getNewBillingAddress(CricketBillingAddress pAddressVo) {
		ContactInfo newBillingAddress = new ContactInfo();
		newBillingAddress.setFirstName(pAddressVo.getFirstName());
		newBillingAddress.setLastName(pAddressVo.getLastName());
		newBillingAddress.setCompanyName(pAddressVo.getCompanyName());
		newBillingAddress.setAddress1(pAddressVo.getAddress1());
		newBillingAddress.setAddress2(pAddressVo.getAddress2());
		newBillingAddress.setCity(pAddressVo.getCity());
		newBillingAddress.setState(pAddressVo.getStateAddress());
		newBillingAddress.setCountry(pAddressVo.getCountry());
		newBillingAddress.setPostalCode(pAddressVo.getPostalCode());
		newBillingAddress.setPhoneNumber(pAddressVo.getPhoneNumber());
		return newBillingAddress;

	}

	/**
	 * @param pAddressVo
	 * @return ContactInfo
	 */
	public void assignBillingAddressToPaymentGroup(CricketBillingAddress pAddress,CricketOrderImpl order) {
		

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
			pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
		}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}
			}
		if (isLoggingDebug()) {
								
	    		logDebug("Entering into CricketAddressTools class of assignBillingAddressToPaymentGroup() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		ContactInfo billingAddr =  getNewBillingAddress(pAddress);
		List<?> pgs = order.getPaymentGroups();
		CricketCreditCard creditCard = null;
		for(Object pg:pgs){
			 if(pg instanceof CricketCreditCard){
				 creditCard  =(CricketCreditCard)pg;
				 creditCard.setBillingAddress(billingAddr);
		   }
		}
		if (isLoggingDebug()) {		 					
	    		logDebug("[CricketAddressTools->assignBillingAddressToPaymentGroup()]: Exiting from assignBillingAddressToPaymentGroup()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
	/**
	 * This Method used to generate the Repository Item for profile Billing
	 * Address
	 * 
	 * @param bAddress
	 * @param profile
	 * @param string 
	 * @return
	 */
	public MutableRepositoryItem copyAddressTOContactInfo(ContactInfo bAddress, MutableRepositoryItem profile, String orderId) {
		MutableRepository profileMutable = (MutableRepository) profile.getRepository();
		MutableRepositoryItem contactInfo = null;
		try {
			contactInfo = profileMutable.createItem(CricketCookieConstants.CONTACT_INFO);
			contactInfo.setPropertyValue(CricketCookieConstants.FIRST_NAME, bAddress.getFirstName());
			contactInfo.setPropertyValue(CricketCookieConstants.LAST_NAME, bAddress.getLastName());
			contactInfo.setPropertyValue(CricketCookieConstants.COMPANY_NAME, bAddress.getCompanyName());
			contactInfo.setPropertyValue(CricketCookieConstants.ADDRESS1, bAddress.getAddress1());
			contactInfo.setPropertyValue(CricketCookieConstants.ADDRESS2, bAddress.getAddress2());
			contactInfo.setPropertyValue(CricketCookieConstants.CITY, bAddress.getCity());
			contactInfo.setPropertyValue(CricketCookieConstants.STATE, bAddress.getState());
			contactInfo.setPropertyValue(CricketCookieConstants.COUNTRY, bAddress.getCountry());
			contactInfo.setPropertyValue(CricketCookieConstants.POSTAL_CODE, bAddress.getPostalCode());
			contactInfo.setPropertyValue(CricketCookieConstants.PHONE_NUMBER,	bAddress.getPhoneNumber());
			//contactInfo.setPropertyValue(CricketCookieConstants.ORDER_ID,	orderId);
			profileMutable.addItem(contactInfo);
			profileMutable.updateItem(contactInfo);
		} catch (RepositoryException e) {
			logError(e);
		}
		return contactInfo;
	}
	
	/**
	 * @param pAddress
	 * @param flag
	 * @param pOrderId
	 * @param methodName
	 */
	public ValidateAddressResponseVO retry(Object pAddress, boolean flag, String pOrderId, String methodName) {
		logInfo("[CricketAddressTools->retry()]: ************************** Entering into retry() while invoking: "+methodName+"()");
		
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketAddressTools->retry()]: ************************** inside retry() while invoking:..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		boolean isRetryEnabled = getEspAdapter().isRetryEnabled();
		ValidateAddressResponseVO validateAddressResponseVO = null;
			if(isRetryEnabled){
				synchronized (this) {		
				
					int configuredRetryCount = getEspAdapter().getRetryTimes();
					int configuredWaitTimeInMillis = getEspAdapter().getRetryWait();
				
					if(isLoggingDebug()){
						logDebug("[CricketAddressTools->retry()]: currentRetryCount: "+ currentRetryCount + " configuredRetryCount: "+ configuredRetryCount+ " configuredWaitTimeInMillis: "+ configuredWaitTimeInMillis + " isRetryEnabled "+ isRetryEnabled);				
						logDebug("[CricketAddressTools->retry()]: Thread executing retry...Name: "+ Thread.currentThread().getName()+" Id: "+Thread.currentThread().getId());				
					}			
							
					while(currentRetryCount < configuredRetryCount){
						try {
							if(isLoggingDebug()){
								logDebug("[CricketAddressTools->retry()]: set isMaxRetryCountReached to false, increment currentRetryCount going to sleep for "+ (configuredWaitTimeInMillis) + " milliseconds");											
							}					
							isMaxRetryCountReached = false;
							Thread.sleep(configuredWaitTimeInMillis);
							currentRetryCount++;
							
							logInfo("[CricketAddressTools->retry()]: ************************** invoking validateAddress again for orderId :"+ pOrderId +", retryCount: "+ currentRetryCount);
							
							//invoke the validate address method once again
							if(pAddress instanceof CricketAddress){
								validateAddressResponseVO = this.validateAccountAddressData((CricketAddress)pAddress, flag, pOrderId);
							}else if(pAddress instanceof CricketShippingAddress){
								validateAddressResponseVO = this.validateShippingAddressData((CricketShippingAddress)pAddress, flag, pOrderId);						
							}else if(pAddress instanceof CricketBillingAddress){
								validateAddressResponseVO = this.validateBillingAddressData((CricketBillingAddress)pAddress, flag, pOrderId);						
							}
							if(validateAddressResponseVO != null && validateAddressResponseVO.getResponse() != null && ESP_RESPONSE_CODE_SUCCESS.equals(validateAddressResponseVO.getResponse().getCode())){
								isMaxRetryCountReached = true;
								currentRetryCount = 0;
								return validateAddressResponseVO;
							}
							if(isMaxRetryCountReached || !getEspAdapter().isRetryEnabled()){//isMaxRetryCountReached -> set to true first time currentRetryCount == configuredRetryCount, used to control infinite loop
								break;
							}
						} catch (InterruptedException ie) {
							logError("[CricketAddressTools->retry()]: InterruptedException while executing Thread.sleep():", ie);							
						}				
					}
					if(currentRetryCount == configuredRetryCount){
						if(isLoggingDebug()){
							logDebug("[CricketAddressTools->retry()]: set isMaxRetryCountReached to true");
						}
						isMaxRetryCountReached = true;
						logInfo("[CricketAddressTools->retry()]: ************************** Giving up after : "+ currentRetryCount+" retries, for orderId :"+ pOrderId + ", methodname: " + methodName + "()");
					}		
						
					//reinitializing currentRetryCount to zero for other threads to run the retry logic in case of espTimeOut
					currentRetryCount = 0;			
			}
		}
			if (isLoggingDebug()) {
				// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 					
		    		logDebug("[CricketAddressTools->retry()]: ************************** Exiting from retry() method :..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
		return validateAddressResponseVO;
	}
	
	/**
	 * @param pRequest
	 * @param pResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void assignShippingAddressToShippingGroup(
			CricketShippingAddress shippingAddress,Order order,
			RepositoryItem profile,String shippingMethod) throws ServletException,
			IOException {
		
		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
			pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
		}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}
			}
		if (isLoggingDebug()) {								
	    		logDebug("Entering into CricketAddressTools class of assignShippingAddressToShippingGroup() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		ContactInfo shippingAddr = null;		
		List<?> sgList = order.getShippingGroups();
		HardgoodShippingGroup shippingType = null;
		for (Object sg : sgList) {
			if (sg instanceof HardgoodShippingGroup) {
				shippingType = (HardgoodShippingGroup) sg;
				shippingType.setShippingMethod(shippingMethod);
			} else {
				// create new shipping Group
			}
		}
		MutableRepositoryItem profileItem = (MutableRepositoryItem) profile;
		shippingAddr = getNewShippingAddress(shippingAddress);
		//copyAddressTOContactInfo(shippingAddr,profileItem,null);
		shippingType.setShippingAddress(shippingAddr);
		order.removeAllShippingGroups();
		try {
			order.addShippingGroup(shippingType);
		} catch (DuplicateShippingGroupException e) {
			logError("Error while adding the shippingGroup to Order at assignShippingAddressToShippingGroup in CricketShippingGroupFormHandler:"+e);
		} catch (InvalidParameterException e) {
			logError("Error while adding the shippingGroup to Order at assignShippingAddressToShippingGroup in CricketShippingGroupFormHandler:"+e);
		}
		if (isLoggingDebug()) {								
	    		logDebug("Entering into CricketAddressTools class of assignShippingAddressToShippingGroup() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId);
		}
	}
	 
	/**
	 * @param shippingAddressData
	 * @param pAccountAddress
	 */
	public void copyAddressFiledToShippingAddressData(CricketShippingAddressData shippingAddressData,CricketAddress pAccountAddress){
		
		shippingAddressData.getShippingAddress().setFirstName((String) pAccountAddress.getFirstName());
		shippingAddressData.getShippingAddress().setLastName((String) pAccountAddress.getLastName());
		shippingAddressData.getShippingAddress().setAddress1((String) pAccountAddress.getAddress1());
		shippingAddressData.getShippingAddress().setAddress2((String) pAccountAddress.getAddress2());
		shippingAddressData.getShippingAddress().setCompanyName((String) pAccountAddress.getCompanyName());
		shippingAddressData.getShippingAddress().setCity((String) pAccountAddress.getCity());
		shippingAddressData.getShippingAddress().setCountry((String) pAccountAddress.getCountry());
		shippingAddressData.getShippingAddress().setStateAddress((String) pAccountAddress.getStateAddress());
		shippingAddressData.getShippingAddress().setMiddleName((String) pAccountAddress.getMiddleName());
		shippingAddressData.getShippingAddress().setPostalCode((String) pAccountAddress.getPostalCode());
		shippingAddressData.getShippingAddress().setEmail(pAccountAddress.getEmail());
	}
	/**
	 * @param pAccountAddress
	 * @param billingAddressData
	 */
	public void copyAddressFiledToBillingAddressData(CricketAddress pAccountAddress, CricketBillingAddressData billingAddressData){
		
		billingAddressData.getBillingAddress().setFirstName((String) pAccountAddress.getFirstName());
		billingAddressData.getBillingAddress().setLastName((String) pAccountAddress.getLastName());
		billingAddressData.getBillingAddress().setAddress1((String) pAccountAddress.getAddress1());
		billingAddressData.getBillingAddress().setAddress2((String) pAccountAddress.getAddress2());
		billingAddressData.getBillingAddress().setCompanyName((String) pAccountAddress.getCompanyName());
		billingAddressData.getBillingAddress().setCity((String) pAccountAddress.getCity());
		billingAddressData.getBillingAddress().setCountry((String) pAccountAddress.getCountry());
		billingAddressData.getBillingAddress().setStateAddress((String) pAccountAddress.getStateAddress());
		billingAddressData.getBillingAddress().setMiddleName((String) pAccountAddress.getMiddleName());
		billingAddressData.getBillingAddress().setPostalCode((String) pAccountAddress.getPostalCode());
		billingAddressData.getBillingAddress().setEmail((String) pAccountAddress.getEmail());
	}
}
