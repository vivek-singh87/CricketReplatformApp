package com.cricket.commerce.order.purchase;

import static com.cricket.common.constants.CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.BUSINESS_ADDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.CRICKET_PROFILE;
import static com.cricket.common.constants.CricketESPConstants.ESP_SERVICE_RESPONSE_DATA;
import static com.cricket.common.constants.CricketESPConstants.IP_ADDRESS;
import static com.cricket.common.constants.CricketESPConstants.IS_ABP_DIFF_CREDIT_CARD;
import static com.cricket.common.constants.CricketESPConstants.LOCATION_INFO;
import static com.cricket.common.constants.CricketESPConstants.PROFILE_ITEM;
import static com.cricket.common.constants.CricketESPConstants.SHIPPING_ADDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.UNDEFINED;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import atg.commerce.CommerceException;
import atg.commerce.catalog.CatalogTools;
import atg.commerce.order.CreditCard;
import atg.commerce.order.PaymentGroup;
import atg.commerce.order.PipelineConstants;
import atg.commerce.order.purchase.PaymentGroupFormHandler;
import atg.commerce.util.RepeatingRequestMonitor;
import atg.core.util.ContactInfo;
import atg.core.util.StringUtils;
import atg.droplet.DropletException;
import atg.droplet.IsEmpty;
import atg.service.pipeline.PipelineResult;
import atg.service.pipeline.RunProcessException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketBillingAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.payment.CricketABPPaymentData;
import com.cricket.commerce.order.payment.CricketCreditCard;
import com.cricket.commerce.order.payment.CricketPaymentData;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.maxmind.CitySessionInfoObject;
import com.cricket.shipping.tools.CricketAddressTools;
import com.cricket.user.session.UserSessionBean;
import com.cricket.util.EspServiceResponseData;
import com.cricket.vo.CricketProfile;
import com.cricket.vo.MyCricketCookieLocationInfo;

public class CricketPaymentGroupFormHandler extends PaymentGroupFormHandler {

	/* variable holds the Error URL */
	private String newPaymentGroupErrorURL;
	/* variable holds the Success URL */
	private String newPaymentGroupSuccessURL;
	/* variable holds the Success URL */
	private CricketAddressTools addressTools;
	/* variable holds the payment data for normal credit card */
	private CricketPaymentData paymentData;
	/* variable holds the payment data for credit card data in case of auto bill pay for diff card*/
	private CricketABPPaymentData abpPaymentData;
    /* regEx to check   visa card */
	private String visaRegex;
	 /* regEx to check   master card card */
	private String mastercardRegex;
	 /* regEx to check   american express card */
	private String americanexpressRegex;
	 /* regEx to check   discover card */
	private String discoverRegex;
    /* holds billing address data address */
	private CricketBillingAddressData billingAddressData;
	/* variable holds shipping address */
	private CricketShippingAddressData shippingAddressData;
	/* holds CricketESPAdapter to interact with ESP layer */
	private CricketESPAdapter espAdapter;
	/* holds CitySessionInfoObject to interact with ESP layer */
	private CitySessionInfoObject citySessionInfoObject;
	private MyCricketCookieLocationInfo locationInfo;
	private CricketProfile cricketProfile;
	/* holds CricketAccountHolderAddressData to interact with ESP layer */
	private CricketAccountHolderAddressData accountHolderAddressData;
	/* gets CricketESPAdapter instance */
	/* holds catalog tools instance */
	private CatalogTools catalogTools;
	/* holds EspServiceResponseData */
	private EspServiceResponseData espServiceResponseData;
	/* holds cricketESPVestaPaymentChain chain name */
	private String cricketESPVestaPaymentChain;
	/* holds isDifferCard for abp */
	boolean abpDiffCreditCard = false;
	/* holds isDifferCard for abp */
	private UserSessionBean userSessionBean;
	/* problem in generation of token in development environment so hard coded the token value. if this flag true 
	 * dev env takes hardcoded token value
	 */
	private boolean devEnvironmentEnabled;
	/* holds autobill payment flag for order confirm page, thankyou page */
	 private boolean autobillPaymentFlag;
	 
	/* holds dummy value of vesta token */
	 private String vestaTokenStub;
	 
	 /**
	 * This method is used to create new payment billing address based on
	 * selection of payment method
	 * 
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean handleNewPaymentGroup(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		
		 CricketOrderImpl order = (CricketOrderImpl) getOrder();
		if (isLoggingDebug()) {						
	    		logDebug("Enter into CricketPaymentGroupFormHandler class of handleNewPaymentGroup() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + getOrderidForLogInfo() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}
		boolean flag = false;
 		String l_sMethod = "CricketPaymentGroupFormHandler.handleNewPaymentGroup";
		RepeatingRequestMonitor l_oRepeatingRequestMonitor = getRepeatingRequestMonitor();
		if ((l_oRepeatingRequestMonitor == null)
				|| (l_oRepeatingRequestMonitor
						.isUniqueRequestEntry(l_sMethod))) {
 		javax.transaction.Transaction tr = null;
		try {
				tr = ensureTransaction();
				if (getUserLocale() == null)
					setUserLocale(getUserLocale(pRequest, pResponse));
				synchronized (getOrder()) {
					try {
						  	preCreatePaymentGroup(pRequest, pResponse);
						  	flag = isErrorExistInPaymentPage(pRequest, pResponse);
							if(!getFormError()){
								order = (CricketOrderImpl) getOrder();	
								order.setTotalTax(order.getTaxPriceInfo().getAmount());
								createPaymentGroup(pRequest, pResponse);
								postCreatePaymentGroup(pRequest, pResponse);
								getPaymentData().setAbpFlag(isAutobillPaymentFlag());								
								getOrderManager().updateOrder(getOrder());	
							}
					} catch (CommerceException e) {
						vlogError("CommerceException in CricketPaymentGroupFormHandler at handleNewPaymentGroup : " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  getOrderidForLogInfo(), e);
					}
				}
				flag = isErrorExistInPaymentPage(pRequest, pResponse);
			} finally {
				if (tr != null)
					commitTransaction(tr);
				if (l_oRepeatingRequestMonitor != null) {
					l_oRepeatingRequestMonitor.removeRequestEntry(l_sMethod);
				}
			}
		}
		if (isLoggingDebug()) {						
	    		logDebug("Exit from CricketPaymentGroupFormHandler class of handleNewPaymentGroup() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + getOrderidForLogInfo() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}
		return flag;
	}

	/**
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private boolean isErrorExistInPaymentPage(
			DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		boolean flag;
		if (getFormError()) {
			//  setTransactionToRollbackOnly();
			 flag = checkFormRedirect(null,
					getNewPaymentGroupErrorURL()+CricketCommonConstants.WHOOPS_ERROR_YES, pRequest, pResponse); 
		}
		else {
			flag = checkFormRedirect(getNewPaymentGroupSuccessURL(),
					null, pRequest, pResponse);
		  }
		return flag;
	}

	/**
	 * @param pRequest
	 * @param pResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void preCreatePaymentGroup(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		
		if(!isDevEnvironmentEnabled()){
			if(null!=getPaymentData()){
				String vestaToken = getPaymentData().getVestaToken();
				if(null == vestaToken || StringUtils.isEmpty(vestaToken) || StringUtils.isBlank(vestaToken)){
				  logError("CricketPaymentGroupFormHandler at preCreatePaymentGroup()-: Unable to generate the token from Vesta Js that why throwing error");
					addFormException(new DropletException(CricketCommonConstants.TOKEN_NOT_GENERATED_BY_VESTA_JS));
					getPaymentData().setCardNumber(null);
					getPaymentData().setExpirationMonth(null);
					getPaymentData().setExpirationYear(null);
					getPaymentData().setCvcNumber(null);
					return;
				}
			}
			String autoBillOption[] = pRequest.getParameterValues(CricketCommonConstants.PAYMENT_AUTO_PAY_PAYMENT);
			if (null != autoBillOption && autoBillOption.length > 0) {
				if (autoBillOption[0].equalsIgnoreCase(CricketCommonConstants.PAYMENT_USE_DIFF_CARD_FOR_ABP)) {
 					if(null!=getAbpPaymentData()){
						String vestaToken = getAbpPaymentData().getVestaToken();
						if(null == vestaToken || StringUtils.isEmpty(vestaToken) || StringUtils.isBlank(vestaToken)){
							  logError("CricketPaymentGroupFormHandler at preCreatePaymentGroup()-: Unable to generate the abp token from Vesta Js that why throwing error");
							addFormException(new DropletException(CricketCommonConstants.TOKEN_NOT_GENERATED_BY_VESTA_JS));
							getAbpPaymentData().setCardNumber(null);
							getAbpPaymentData().setExpirationMonth(null);
							getAbpPaymentData().setExpirationYear(null);
							getAbpPaymentData().setCvcNumber(null);
							return;
						}
					}
				}
			}
		}
			
	}
	/**
	 * This method is used to check Credit card type on the basis of entered
	 * Credit card number. It will match with the credit card regex pattern
	 * defined for each credit card type.
	 * 
	 * @Inputparam credit card number
	 * @outputparam credit card type
	 * @deprecated
	 */
	public String creditCardType(String cardNumber) {

		String cardType = null;

		Pattern visaPattern = Pattern.compile(getVisaRegex());
		Pattern mastercardPattern = Pattern.compile(getMastercardRegex());
		Pattern americanexpressPattern = Pattern
				.compile(getAmericanexpressRegex());
		Pattern discoverPattern = Pattern.compile(getDiscoverRegex());

		Matcher visaMatcher = visaPattern.matcher(cardNumber);
		Matcher mastercardMatcher = mastercardPattern.matcher(cardNumber);
		Matcher americaexpressMatcher = americanexpressPattern
				.matcher(cardNumber);
		Matcher discoverMatcher = discoverPattern.matcher(cardNumber);

		if (visaMatcher.matches() != false) {
			cardType = CricketCommonConstants.VISA_CARD;
		}
		if (mastercardMatcher.matches() != false) {
			cardType =CricketCommonConstants.MASTER_CARD;
		}
		if (americaexpressMatcher.matches() != false) {
			cardType = CricketCommonConstants.AMERICAN_CARD;
		}
		if (discoverMatcher.matches() != false) {
			cardType = CricketCommonConstants.DISCOVER_CARD;
		}
		paymentData.setCreditCardType(cardType);
		return cardType;

	}
	
 
	/**
	 * This method is used to assign the given address(from Form) to selected
	 * payment type. if the saveToProfile flag is true , it will add the same
	 * address as a default address.
	 * 
	 * @param pRequest
	 * @param pResponse
	 */
	public void createPaymentGroup(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {

		if (isLoggingDebug()) {						
	    		logDebug("Entering into CricketPaymentGroupFormHandler class of createPaymentGroup() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + getOrderidForLogInfo() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}
		// check auto bill options
		boolean abpFlag = false;
		CricketCreditCard abpPaymentGroup = null;
		
		String autoBillOption[] = pRequest.getParameterValues(CricketCommonConstants.PAYMENT_AUTO_PAY_PAYMENT);
		if (null != autoBillOption && autoBillOption.length > 0) {
			if (autoBillOption[0].equalsIgnoreCase(CricketCommonConstants.PAYMENT_USE_SAME_CARD_FOR_ABP)) {
				abpFlag = true;
				setAbpDiffCreditCard(false);
			} else if ((autoBillOption[0]
					.equalsIgnoreCase(CricketCommonConstants.PAYMENT_USE_DIFF_CARD_FOR_ABP) && !(autoBillOption[0]
					.equalsIgnoreCase(CricketCommonConstants.PAYMENT_USE_SAME_CARD_FOR_ABP)))) {
				abpFlag = false;
				setAbpDiffCreditCard(true);
			} else if (autoBillOption[0].equalsIgnoreCase(CricketCommonConstants.PAYMENT_NO_AUTOBILL_PAY)) {
				abpFlag = false;
			}
			if (isLoggingDebug()) {
				logDebug("CricketPaymentGroupFromHandler - selected abp flg:- "
						+ autoBillOption[0]);
			}
		}// end of auto bill options
 		 setAutobillPaymentFlag(abpFlag);
		// get the payment methods from order
		List<?> pgList = getOrder().getPaymentGroups();
		CricketCreditCard paymentType = null;

		for (Object pg : pgList) {
			if (pg instanceof CreditCard) {
				paymentType = (CricketCreditCard) pg;
				setPaymentDataFileds(paymentType, abpFlag, false);
			} else {
				// create new payment method
			}
		}
		// create new credit Card payment group in case of userDifferentCardForAutoBillPay
		
		if (abpDiffCreditCard) {
 			try {
				abpPaymentGroup = (CricketCreditCard) getPaymentGroupManager().createPaymentGroup(CricketCommonConstants.PAYMENT_GROUP_CREDIT_CARD);
				setPaymentDataFileds(abpPaymentGroup, false,
						isAbpDiffCreditCard());
			} catch (CommerceException e1) {
				vlogError("CommerceException while creating New payment method in case of userDifferentCardForAutoBillPay : " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  getOrderidForLogInfo(), e1);
			}
		}
		// now adding the payment group to order
			try {
				//getOrder().removeAllPaymentGroups();
				if (abpDiffCreditCard) {
					getPaymentGroupManager().addPaymentGroupToOrder(getOrder(), abpPaymentGroup);
				}  
 				//getPaymentGroupManager().addPaymentGroupToOrder(getOrder(), paymentType);
				
			} catch (CommerceException e) {
				vlogError("Error while updating the order in CricketPaymentGroupFormHandler : " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  getOrderidForLogInfo(), e);
			}
		getPaymentData().setAbpFlag(abpFlag);
		if (isLoggingDebug()) {						
	    		logDebug("Exiting from CricketPaymentGroupFormHandler class of createPaymentGroup() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + getOrderidForLogInfo() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}
	}
 
	/**
	 * this method is used to invoke the esp call chain.
	 * are calling below p
	 * 
	 * @param dynamohttpservletrequest
	 * @param dynamohttpservletresponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void postCreatePaymentGroup(
			DynamoHttpServletRequest dynamohttpservletrequest,
			DynamoHttpServletResponse dynamohttpservletresponse)
			throws ServletException, IOException {
		 // Getting the Order Id
		 CricketOrderImpl order = (CricketOrderImpl) getOrder();		
		if (isLoggingDebug()) {						
	    		logDebug("Entering into CricketPaymentGroupFormHandler class of postCreatePaymentGroup() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + getOrderidForLogInfo() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}
		order = (CricketOrderImpl) getOrder();	
	
		List<PaymentGroup> pgs = order.getPaymentGroups();
		
		boolean isCreditCardTokenValid = validateCreditCardToken(pgs);
		
		if(!isCreditCardTokenValid){
			return;
		}
		
//		PipelineResult result = applyEspServiceChainOnOrder(order,dynamohttpservletrequest);
//		processPiplelineResult(result);
		if (isLoggingDebug()) {						
	    		logDebug("Exiting from CricketPaymentGroupFormHandler class of postCreatePaymentGroup() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + getOrderidForLogInfo() + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}
	}
	
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	
	private String getOrderidForLogInfo() {
		 // Getting the Order Id
		 CricketOrderImpl order = (CricketOrderImpl) getOrder();
		 String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank( order.getId())){
	    			orderId = order.getId();
				}
			}
		return orderId;
	}
	
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
	/**
	 * @param pgs
	 */
	private boolean validateCreditCardToken(List<PaymentGroup> pgs) {
		CricketCreditCard creditCard = null;
		CricketCreditCard abpCreditCard = null;
		for(PaymentGroup pg:pgs){
			 if(pg instanceof CricketCreditCard){
				 
				 if(((CricketCreditCard)pg).isDiffernetCard()){
					 abpCreditCard = (CricketCreditCard)pg;
				 } else {
					 creditCard = (CricketCreditCard)pg;
				 }
				
			 }
		}		
		
		if(creditCard != null && creditCard.getVestaToken() != null && UNDEFINED.equalsIgnoreCase(creditCard.getVestaToken())){
			addFormException(new DropletException(CricketESPConstants.VESTA_RESPONSE_CODE_1001));
			return false;			
		}
		
		if(abpCreditCard != null && abpCreditCard.getVestaToken() != null && UNDEFINED.equalsIgnoreCase(abpCreditCard.getVestaToken())){
			addFormException(new DropletException(CricketESPConstants.VESTA_RESPONSE_CODE_1001_ABP));
			return false;			
		}
		
		return true;
				
	}
	
	/** this method is used to send the required data to ESP call chain.
	 * @param order
	 * @return
	 */
	public PipelineResult applyEspServiceChainOnOrder(CricketOrderImpl order,DynamoHttpServletRequest dynamohttpservletrequest) {
	
		 // Getting the Order Id			
		String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank( order.getId())){
	    			orderId = order.getId();
				}
			}
		if (isLoggingDebug()) {						
	    		logDebug("Entering into CricketPaymentGroupFormHandler class of applyEspServiceChainOnOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}
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
		params.put(IP_ADDRESS, getClientIPAddress(dynamohttpservletrequest));
		params.put(IS_ABP_DIFF_CREDIT_CARD, isAbpDiffCreditCard());
		
		if (isLoggingDebug()) {			
	    		logDebug("Exiting from CricketPaymentGroupFormHandler class of applyEspServiceChainOnOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + getPageUrlForLogInfo());
		}
 		// delegate
		try {
			return getOrderManager().getPipelineManager().runProcess(getCricketESPVestaPaymentChain(), params);
		} catch (RunProcessException e) {
			vlogError("Unexpected exception while executing esp calls::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			return null;
		}
	}
	/** Error handling - this method is used to popup the error if any while placing the order in billing system.
	 * @param result
	 * @return true
	 */
	private boolean processPiplelineResult(final PipelineResult result) {
		if(result != null && result.hasErrors()) {
			String errorKey = null;
			// Add error messages to form
			 Object errorKeys[] = result.getErrorKeys();
			 Object error = null;
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
			// done
			return false;
		}
		// all ok
		return true;
	}

	/** This method is used the set the payment data fileds to payment group.
	 * @param paymentType
	 * @param abpFlag
	 * @param abpDiffCreditCard
	 */
	public void setPaymentDataFileds(CricketCreditCard paymentType,
			boolean abpFlag, boolean abpDiffCreditCard) {

		if (!abpDiffCreditCard) {
			if(isLoggingDebug()){
 			 logDebug("Main payment section - user entered payment details in page - :"+paymentData.toString());
			}
 			
 			String vestaTokenSubValue = getVestaTokenStub();
 			
 			paymentType.setCcFirstName(paymentData.getFristName() == null ? CricketCommonConstants.EMPTY_STRING
					: paymentData.getFristName());
			paymentType.setCcLastName(paymentData.getLastName() == null ? CricketCommonConstants.EMPTY_STRING
					: paymentData.getLastName());
			if(paymentData.getVestaToken() !=null && paymentData.getVestaToken().length()>0){
				paymentType.setVestaToken(paymentData.getVestaToken());
			}else{
 				paymentType.setVestaToken(vestaTokenSubValue);
			}
			paymentType.setCreditCardNumber(paymentData.getCardNumber().substring(paymentData.getCardNumber().length() - 4));
			paymentType.setCardVerficationNumber(paymentData.getCvcNumber());
			paymentType.setExpirationMonth(paymentData.getExpirationMonth());
			paymentType.setExpirationYear(paymentData.getExpirationYear());
			paymentType.setCreditCardType(paymentData.getCreditCardType()); 
			// paymentType.setautoBillPayment(paymentData.getAbpFlag());
			paymentType.setAutoBillPayment(abpFlag);
			paymentType.setDiffernetCard(false);
			paymentType.getBillingAddress().setFirstName(paymentData.getFristName());
			paymentType.getBillingAddress().setLastName(paymentData.getLastName());
			getBillingAddressData().getBillingAddress().setFirstName(paymentData.getFristName());
			getBillingAddressData().getBillingAddress().setLastName(paymentData.getLastName());
			//paymentType.setVestaToken(paymentData.getVestaToken());
		} else {
			if(isLoggingDebug()){
 				logDebug("Auto bill payment section - user entered payment details in page -:"+abpPaymentData.toString());
			}
			paymentType.setCcFirstName(abpPaymentData.getFristName() == null ? CricketCommonConstants.EMPTY_STRING
							: abpPaymentData.getFristName());
			paymentType.setCcLastName(abpPaymentData.getLastName() == null ? CricketCommonConstants.EMPTY_STRING
					: abpPaymentData.getLastName());
			String vestaTokenSubValueAbp = getVestaTokenStub();
			if(abpPaymentData.getVestaToken() !=null && abpPaymentData.getVestaToken().length()>0){
				paymentType.setVestaToken(abpPaymentData.getVestaToken());
			}else{
				paymentType.setVestaToken(vestaTokenSubValueAbp);
			}
			paymentType.setCreditCardNumber(abpPaymentData.getCardNumber().substring(abpPaymentData.getCardNumber().length() - 4));
  			paymentType.setCardVerficationNumber(abpPaymentData.getCvcNumber());
			paymentType.setExpirationMonth(abpPaymentData.getExpirationMonth());
			paymentType.setExpirationYear(abpPaymentData.getExpirationYear());
			paymentType.setCreditCardType(abpPaymentData.getCreditCardType()); 
 			paymentType.setAutoBillPayment(abpDiffCreditCard);
			paymentType.setDiffernetCard(true);
			paymentType.setAmount(0.0);
			paymentType.getBillingAddress().setFirstName(abpPaymentData.getFristName());
			paymentType.getBillingAddress().setLastName(abpPaymentData.getLastName());
			getBillingAddressData().getBillingAddress().setFirstName(abpPaymentData.getFristName());
			getBillingAddressData().getBillingAddress().setLastName(abpPaymentData.getLastName());
			//paymentType.setVestaToken(abpPaymentData.getVestaToken());
		}
 		ContactInfo billingAddr = getAddressTools().getNewBillingAddress(getBillingAddressData().getBillingAddress());
		paymentType.setBillingAddress(billingAddr);
   		paymentType.setAmount(Double.valueOf(new DecimalFormat("###.##").format(((CricketOrderImpl)getOrder()).getPriceInfo().getTotal())));
	}

	/* generate mask string */
	public String getMaskToken(String token) {
		if (null != token && token.length() > 0) {
			int tokenLength = token.length();
			String lastFourDigit = token
					.substring(tokenLength - 4, tokenLength);
			int maskCharCount = tokenLength - 4;
			StringBuilder maskToken = new StringBuilder();
			for (int i = 0; i < maskCharCount; i++) {
				maskToken.append(CricketCommonConstants.MASK_TOKEN);
			}
			return maskToken.append(lastFourDigit).toString();
		}
		return token;

	}

	public String getNewPaymentGroupErrorURL() {
		return newPaymentGroupErrorURL;
	}

	public void setNewPaymentGroupErrorURL(String newPaymentGroupErrorURL) {
		this.newPaymentGroupErrorURL = newPaymentGroupErrorURL;
	}

	public String getNewPaymentGroupSuccessURL() {
		return newPaymentGroupSuccessURL;
	}

	public void setNewPaymentGroupSuccessURL(String newPaymentGroupSuccessURL) {
		this.newPaymentGroupSuccessURL = newPaymentGroupSuccessURL;
	}

	/**
	 * @return
	 */
	public CricketABPPaymentData getAbpPaymentData() {
		return abpPaymentData;
	}

	/**
	 * @param abpPaymentData
	 */
	public void setAbpPaymentData(CricketABPPaymentData abpPaymentData) {
		this.abpPaymentData = abpPaymentData;
	}

	/**
	 * @return
	 */
	public CricketPaymentData getPaymentData() {
		return paymentData;
	}

	/**
	 * @param paymentData
	 */
	public void setPaymentData(CricketPaymentData paymentData) {
		this.paymentData = paymentData;
	}

	/**
	 * @return
	 */
	public CricketAddressTools getAddressTools() {
		return addressTools;
	}

	/**
	 * @param addressTools
	 */
	public void setAddressTools(CricketAddressTools addressTools) {
		this.addressTools = addressTools;
	}

	/**
	 * @return
	 */
	public CricketBillingAddressData getBillingAddressData() {
		return billingAddressData;
	}

	/**
	 * @param billingAddressData
	 */
	public void setBillingAddressData(
			CricketBillingAddressData billingAddressData) {
		this.billingAddressData = billingAddressData;
	}

	public CricketShippingAddressData getShippingAddressData() {
		return shippingAddressData;
	}

	public void setShippingAddressData(
			CricketShippingAddressData shippingAddressData) {
		this.shippingAddressData = shippingAddressData;
	}

	public String getVisaRegex() {
		return visaRegex;
	}

	public void setVisaRegex(String visaRegex) {
		this.visaRegex = visaRegex;
	}

	public String getMastercardRegex() {
		return mastercardRegex;
	}

	public void setMastercardRegex(String mastercardRegex) {
		this.mastercardRegex = mastercardRegex;
	}

	public String getAmericanexpressRegex() {
		return americanexpressRegex;
	}

	public void setAmericanexpressRegex(String americanexpressRegex) {
		this.americanexpressRegex = americanexpressRegex;
	}

	public String getDiscoverRegex() {
		return discoverRegex;
	}

	public void setDiscoverRegex(String discoverRegex) {
		this.discoverRegex = discoverRegex;
	}

	public String getClientIPAddress(
			DynamoHttpServletRequest dynamoHttpServletRequest) {
		String clientIPAddress = null;
		if(isLoggingDebug()){
			  logDebug("[CricketPaymentGroupFormHandler->getClientIPAddress()]: IP Address X-Forwarded-For: "+dynamoHttpServletRequest.getHeader("X-Forwarded-For"));
			  logDebug("[CricketPaymentGroupFormHandler->getClientIPAddress()]: IP Address X-FORWARDED-FOR: "+dynamoHttpServletRequest.getHeader("X-FORWARDED-FOR"));
			  logDebug("[CricketPaymentGroupFormHandler->getClientIPAddress()]: IP Address x-forwarded-for: "+dynamoHttpServletRequest.getHeader("x-forwarded-for"));
		}
		if ((clientIPAddress = dynamoHttpServletRequest
				.getHeader("X-Forwarded-For")) != null) {
			String[] ipAddresses = clientIPAddress.split(",");
			clientIPAddress = (ipAddresses != null && ipAddresses.length > 0) ? ipAddresses[0] : dynamoHttpServletRequest.getRemoteAddr();
		} else {
			if(isLoggingDebug()){
				logDebug("[CricketPaymentGroupFormHandler->getClientIPAddress()]: Remote IP Address: "+dynamoHttpServletRequest.getRemoteAddr());
			}
			clientIPAddress = dynamoHttpServletRequest.getRemoteAddr();
		}
		return (clientIPAddress != null) ? clientIPAddress.trim() : clientIPAddress;
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
	 * @return the cricketESPVestaPaymentChain
	 */
	public String getCricketESPVestaPaymentChain() {
		return cricketESPVestaPaymentChain;
	}

	/**
	 * @param cricketESPVestaPaymentChain the cricketESPVestaPaymentChain to set
	 */
	public void setCricketESPVestaPaymentChain(String cricketESPVestaPaymentChain) {
		this.cricketESPVestaPaymentChain = cricketESPVestaPaymentChain;
	}
	/**
	 * @return the locationInfo
	 */
	public MyCricketCookieLocationInfo getLocationInfo() {
		return locationInfo;
	}

	/**
	 * @param locationInfo
	 *            the locationInfo to set
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
	 * @param cricketProfile
	 *            the cricketProfile to set
	 */
	public void setCricketProfile(CricketProfile cricketProfile) {
		this.cricketProfile = cricketProfile;
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
	 * @return the catalogTools
	 */
	public CatalogTools getCatalogTools() {
		return catalogTools;
	}

	/**
	 * @param catalogTools
	 *            the catalogTools to set
	 */
	public void setCatalogTools(CatalogTools catalogTools) {
		this.catalogTools = catalogTools;
	}

	public CricketESPAdapter getEspAdapter() {
		return espAdapter;
	}

	/* sets CricketESPAdapter instance */
	public void setEspAdapter(CricketESPAdapter espAdapter) {
		this.espAdapter = espAdapter;
	}

	/* gets CitySessionInfoObject instance */
	public CitySessionInfoObject getCitySessionInfoObject() {
		return citySessionInfoObject;
	}

	/* sets CitySessionInfoObject instance */
	public void setCitySessionInfoObject(
			CitySessionInfoObject citySessionInfoObject) {
		this.citySessionInfoObject = citySessionInfoObject;
	}
	/**
	 * @return the abpDiffCreditCard
	 */
	public boolean isAbpDiffCreditCard() {
		return abpDiffCreditCard;
	}

	/**
	 * @param abpDiffCreditCard
	 *            the abpDiffCreditCard to set
	 */
	public void setAbpDiffCreditCard(boolean abpDiffCreditCard) {
		this.abpDiffCreditCard = abpDiffCreditCard;
	}

	/**
	 * @return the userSessionBean
	 */
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}

	/**
	 * @param userSessionBean the userSessionBean to set
	 */
	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

	/**
	 * @return the autobillPaymentFlag
	 */
	public boolean isAutobillPaymentFlag() {
		return autobillPaymentFlag;
	}

	/**
	 * @param autobillPaymentFlag the autobillPaymentFlag to set
	 */
	public void setAutobillPaymentFlag(boolean autobillPaymentFlag) {
		this.autobillPaymentFlag = autobillPaymentFlag;
	}

	/**
	 * @return the vestaTokenStub
	 */
	public String getVestaTokenStub() {
		return vestaTokenStub;
	}

	/**
	 * @param vestaTokenStub the vestaTokenStub to set
	 */
	public void setVestaTokenStub(String vestaTokenStub) {
		this.vestaTokenStub = vestaTokenStub;
	}
	/**
	 * @return the devEnvironmentEnabled
	 */
	public boolean isDevEnvironmentEnabled() {
		return devEnvironmentEnabled;
	}

	/**
	 * @param devEnvironmentEnabled the devEnvironmentEnabled to set
	 */
	public void setDevEnvironmentEnabled(boolean devEnvironmentEnabled) {
		this.devEnvironmentEnabled = devEnvironmentEnabled;
	}

}
 