package com.cricket.integration.esp;

import static com.cricket.common.constants.CricketESPConstants.*;
import static com.cricket.common.constants.CricketCommonConstants.REC_OFFERING_NAME;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;

import org.apache.axis.client.Stub;
import org.apache.axis.message.MessageElement;
import org.apache.axis.types.PositiveInteger;

import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.PaymentGroup;
import atg.commerce.pricing.PricingAdjustment;
import atg.core.i18n.LayeredResourceBundle;
import atg.core.util.Address;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.repository.RepositoryItem;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;

import com.cricket.browse.DisplayFeaturesManager;
import com.cricket.catalog.CatalogManager;
import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketBillingAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.checkout.utils.CricketShippingAddress;
import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.commerce.order.CricketPackage;
import com.cricket.commerce.order.payment.CricketCreditCard;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.esp.ESP.Namespaces.Container.Public.CompleteSaleRequest_xsd.CompleteSaleRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CompleteSaleResponse_xsd.CompleteSaleResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfoAccount;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfoAccountSubscriber;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfoAccountSubscriberContactInformation;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfoCustomer;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfoCustomerIdentity;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfoOrderInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteResponse_xsd.CreateActivationQuoteResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateShippingQuoteRequest_xsd.CreateShippingQuoteRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateShippingQuoteResponse_xsd.CreateShippingQuoteResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.FinalizeSaleRequest_xsd.FinalizeSaleRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.FinalizeSaleRequest_xsd.FinalizeSaleRequestInfoCustomer;
import com.cricket.esp.ESP.Namespaces.Container.Public.FinalizeSaleResponse_xsd.FinalizeSaleResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireAccountRequest_xsd.InquireAccountRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireAccountResponse_xsd.InquireAccountResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireBillingOrderDetailsRequest_xsd.InquireBillingOrderDetailsRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireBillingOrderDetailsResponse_xsd.InquireBillingOrderDetailsResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireCoverageRequest_xsd.InquireCoverageRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireCoverageResponse_xsd.InquireCoverageResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireDeliveryEstimateRequest_xsd.InquireDeliveryEstimateRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireDeliveryEstimateResponse_xsd.InquireDeliveryEstimateResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireFeaturesRequest_xsd.FeatureSelectionCriteriaInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireFeaturesRequest_xsd.InquireFeaturesRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireFeaturesResponse_xsd.InquireFeaturesResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireFeaturesResponse_xsd.InquireFeaturesResponseInfoInquireFeaturesResponseFeatures;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageABPRequest_xsd.ManageABPRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageABPRequest_xsd.ManageABPRequestInfoABPEnrollmentInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageABPResponse_xsd.ManageABPResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManagePaymentRequest_xsd.ManagePaymentRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManagePaymentRequest_xsd.ManagePaymentRequestInfoCompletePaymentInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManagePaymentRequest_xsd.ManagePaymentRequestInfoCustomerContactInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManagePaymentRequest_xsd.ManagePaymentRequestInfoNewPaymentInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManagePaymentResponse_xsd.ManagePaymentResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageSaleItemRequest_xsd.ManageSaleItemRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageSaleItemRequest_xsd.ManageSaleItemRequestInfoLineItem;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageSaleItemResponse_xsd.ManageSaleItemResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageSaleRequest_xsd.ManageSaleRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageSaleRequest_xsd.ManageSaleRequestInfoNewSaleInformation;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageSaleResponse_xsd.ManageSaleResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.MessageHeader_xsd.MessageHeaderInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.SendMessageRequest_xsd.SendMessageRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.SendMessageRequest_xsd.SendMessageRequestInfoMessageTemplate;
import com.cricket.esp.ESP.Namespaces.Container.Public.SendMessageRequest_xsd.SendMessageRequestInfoMessageType;
import com.cricket.esp.ESP.Namespaces.Container.Public.SendMessageRequest_xsd.SendMessageRequestInfoMessageTypeEmailInformation;
import com.cricket.esp.ESP.Namespaces.Container.Public.SendMessageResponse_xsd.SendMessageResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateBillingQuoteRequest_xsd.UpdateBillingQuoteRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateBillingQuoteStatusRequest_xsd.UpdateBillingQuoteStatusRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateBillingQuoteStatusResponse_xsd.UpdateBillingQuoteStatusResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateSubscriberRequest_xsd.UpdateSubscriberRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateSubscriberResponse_xsd.UpdateSubscriberResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateWebReportRequest_xsd.UpdateWebReportRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ValidateAddressRequest_xsd.ValidateAddressRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ValidateAddressResponse_xsd.ValidateAddressResponseInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.*;
import com.cricket.esp.ESP.Namespaces.v1.wsdl.Cricket_ESP_HTTP_wsdl.*;
import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.vo.*;
import com.cricket.order.CricketOrder;
import com.cricket.user.session.UserAccountInformation;
import com.cricket.user.session.UserSessionBean;
import com.cricket.util.EspServiceResponseData;
import com.cricket.util.SOAPLogHandler;
import com.cricket.vo.CricketProfile;
import com.cricket.vo.MyCricketCookieLocationInfo;
 
/**
 * @author TechM
 *
 */
public class CricketESPAdapter extends GenericService{
	/*ESP request/response header elements */
	private String userName;
	private String password;
	private String timeToLive;
	private String messageId;
	private String versionId;
	private String sequenceNumber;
	private String totalInSequence;
	private String espServiceUrl;
	private int espTimeOutInMilliSeconds;
	/** The Resource bundle. */
    private ResourceBundle mResourceBundle;
	private boolean isManualOrderApprovalNeeded;	
	/*holds drawerId for manageSale ESP */
	private String drawerId;
	/*holds salesRepresentative for manageSale ESP POSContext */
	private	String salesRepresentative;	
	/*holds salesTransactionType for manageSale ESP */
	private	 String salesTransactionType;	
	/*holds rateCenterId for CreateActivationQuote ESP  */
	private	String	rateCenterId;
	/*holds jointVentureCode for CreateActivationQuote ESP  */
	private	String	jointVentureCode;
	/*holds solicitationContactPreference for CreateActivationQuote ESP  */
	private	String	solicitationContactPreference;
	/*holds salesChannel for CreateActivationQuote ESP  */
	private	String	salesChannel;
	/*holds shippingOrderType for CreateActivationQuote ESP */
	private	String	shippingOrderType;
	/*holds isBillingResponsibility for CreateActivationQuote ESP */
	private boolean isBillingResponsibility;
	/*holds profile object */		
	/*holds sprintCsa term for CreateActivationQuote ESP */
	private String sprintCsa;
	/*holds catalog  tools instance */
	//private CatalogTools catalogTools;
	/* holds mask value for inquireAccount */
	private String mask;
	/*holds lineTax1 for createActivationQuote */
	private int lineTax1;
	/*holds orderShipmentTaxAmount for createActivationQuote */
	private int orderShipmentTaxAmount;
	/* holds barCodeServicePayment for manageSaleItem ESP */
  	private String barCodeServicePayment;
  	/* holds barCodeShipping for manageSaleItem ESP */
 	private String barCodeShipping;
 	/* holds settleForLessThanAuthorized value for managePayment ESP */
	private boolean settleForLessThanAuthorized;
	/* holds allowPendingPayment value for managePayment ESP */
	private boolean allowPendingPayment;	
	/* holds channelCode value for managePayment ESP */
	private String channelCode;
	/* holds orderAction value for managePayment ESP */
	private String orderAction;
	/* holds equimentOrderAction value for managePayment ESP */
	private String equimentOrderAction;
	/*holds fromShippingAddress Zip code for InquireEstimateDelivery ESP calls*/
	private String  shipFromAddressZipCode;
	/* holds customer Type for createActivation ESP */
	private String customerType;
	/* holds shipVia for CreateActivationESP */
	private String shipVia;
	/* conversationId constant property of type String */
	private String conversationId;
	/* holds subscriberStatus for updateSubscriber ESP	 */
	private String subscriberStatus;
	/* holds pricePlanStatus for updateSubscriber ESP	 */
	private String pricePlanStatus;
	/* holds socialSecurityNumber for CreateActivationESP */
	private String socialSecurityNumber;
	/* holds displayFeaturesManager for DisplayFeaturesManager */
	private DisplayFeaturesManager displayFeaturesManager;
	/* holds catalogManager */
	private CatalogManager catalogManager;
	/* holds CricketESPAdapterHelper */
	private CricketESPAdapterHelper cricketESPAdapterHelper;
	/* holds no of retries for ESP in case of espTimeOut*/
	private int retryTimes;
	/* holds no of milliseconds to wait before retrying ESP in case of espTimeOut*/
	private int retryWait;
	/* holds boolean to check whether to retry ESP calls or not in case of espTimeOut*/
	private boolean retryEnabled;
	/* holds boolean to check whether to log SOAP request/responses to server logs folder*/
	private boolean loggingSOAPRequestResponse;
	/* holds boolean to check whether sendMessage ESP is enabled / disabled*/
	private boolean sendMessageESPEnabled;
	
	/* holds ProfileServices */
	private ProfileServices profileServices;
	
	private boolean errorDiscription;
	
	/* hold cricketOffereing Code for MultiLineDiscount in CreateActivationQuote*/
	private String multilineDiscountOfferId;
	/*hold cricketOffereing Code for ActivationFee in CreateActivationQuote*/
	private String activationFeeOfferId;
	/* holds multilien discount offer id for existing user add a line */
	private String  multilineDiscountOfferIdForADD;

	/** this method is used to initialize the ESP request with required tags.
	 * @param stub
	 */
	void intializeHeader(Stub stub, String orderId){
		
		Calendar dateTimeStamp = java.util.Calendar.getInstance();
		/* START:MessageHeader */
		MessageHeaderInfo header = new MessageHeaderInfo();
		MessageHeaderSecurity security = new MessageHeaderSecurity();
		MessageHeaderSequence sequence = new MessageHeaderSequence();
		MessageHeaderTracking tracking = new MessageHeaderTracking();
		stub.setTimeout(getEspTimeOutInMilliSeconds());
 
		security.setUserName(getUserName());
		security.setUserPassword(getPassword());
		sequence.setSequenceNumber(getSequenceNumber());
		sequence.setTotalInSequence(getTotalInSequence());

 		if(getConversationId() != null){
			tracking.setConversationId(getConversationId());		
		}
		tracking.setVersion(MessageHeaderTrackingVersion.v1);
		setMessageId(MESSAGE_ID_PREFIX + dateTimeStamp.getTimeInMillis());
		tracking.setMessageId(getMessageId());
		BigInteger timeToLive = new BigInteger(getTimeToLive());
		tracking.setTimeToLive(timeToLive);
		
		if(null!=orderId)
			stub._setProperty(ORDER_ID, orderId );
		else
			stub._setProperty(ORDER_ID, NOORDER );
		 
		tracking.setDateTimeStamp(dateTimeStamp);
		header.setSecurityMessageHeader(security);
		header.setSequenceMessageHeader(sequence);
		header.setTrackingMessageHeader(tracking);
		/* END:MessageHeader */		

		stub.setHeader(NAMESPACE, PART_NAME, header);
		registerSOAPLogHandler(stub);
	}
	
	/**
	 * @param stub
	 * This method is used to register the stub to the soap handler
	 * This is done to intercept and log soap request and responses
	 */
	@SuppressWarnings("unchecked")
	public void registerSOAPLogHandler(Stub stub){		
		if (isLoggingSOAPRequestResponse()) {			
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->registerSOAPLogHandler()]: Logging SOAP request & response for ESP call: "+stub.getPortName());
			}
			HandlerRegistry handlerRegistry = stub._getService().getHandlerRegistry();
			@SuppressWarnings("rawtypes")
			List chain = handlerRegistry.getHandlerChain(new QName(EMPTY_STRING, stub.getPortName().toString()));
			HandlerInfo info = new HandlerInfo();
			info.setHandlerClass(SOAPLogHandler.class);
			chain.add(info);
        }
	}
	
	/**
	 * @param request
	 * @throws CricketException if the request object is null
	 */
	void validateRequest(Object request)throws CricketException {
		if(request==null){
			throw new CricketException("[CricketESPAdapter->validateRequest()]: Request cannot be null.");
		}
	}

	/**
	 * This is the method for Coverage Details of the Network
	 * @param requestVO - request parameter of type InquireCoverageRequestVO
	 * @return responseVO - response parameter of type InquireCoverageResponseVO
	 * @throws CricketException - Exception type if any
	 */
	public InquireCoverageResponseVO inquireCoverage(InquireCoverageRequestVO requestVO) throws CricketException {
		logInfo("[CricketESPAdapter->inquireCoverage()]: Entering into inquireCoverage()...");
		
		AddressInfo address = null;
		AddressZipInfo zip = null;
		InquireCoverage coverage = null;		
		InquireCoverageResponseVO responseVO=null; 
		try {			
			try {
				coverage = new CricketESPHTTPServicesLocator().getInquireCoverage(new URL(getEspServiceUrl()+ESP_NAME_INQUIRECOVERAGE));
			} catch (MalformedURLException mue) {				 
				vlogError("[CricketESPAdapter->inquireCoverage()]:" + CricketESPConstants.WHOOP_KEYWORD +  " MalformedURLException while getting the InquireCoverage Stub" + "ConversationId: " + getConversationId(), mue);
			}
			InquireCoverageStub coverageStub = (InquireCoverageStub) coverage;
			intializeHeader(coverageStub,null);
			//Populate InquireCoverageRequestInfo
			InquireCoverageRequestInfo requestInfo = new InquireCoverageRequestInfo();
			
			address = new AddressInfo();
			address.setCountry(getCountry(requestVO.getAddress().getCountry()));

			zip = new AddressZipInfo();		
			zip.setZipCode(requestVO.getAddress().getZip().getZipCode());
			// Set the Zip code in the AddressInfo
			address.setZip(zip);
			// Setting the AddressInfo in the InquireCoverageRequestInfo
			requestInfo.setAddress(address);
			// Passing the 	InquireCoverageRequestInfo as parameter to get the coverage details 		
			InquireCoverageResponseInfo inquireCoverageResponseInfo = coverageStub.inquireCoverage(requestInfo);
			
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->inquireCoverage()]: inquireCoverageResponse from ESP is: "+ inquireCoverageResponseInfo);
			}
			
			/* call this method to set conversation id for the first time from ESP response
			 * First time ESP call can happen from InquireCoverage or InquireFeatures or InquireAccount or ValidateAddress
			 * So this call is present in all the above 4 mentioned ESP calls
			 */			
			setConversationIdFromESPResponse(coverageStub);
			
			responseVO = getCricketESPAdapterHelper().getInquireCoverageResponseVO(inquireCoverageResponseInfo);				
			
		} catch (ServiceException se) {
			vlogError("[CricketESPAdapter->inquireCoverage()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ServiceException while getting inqureCoverage response:"  + "ConversationId: " + getConversationId(), se);
		} catch (ESPApplicationException eae) {
			vlogError("[CricketESPAdapter->inquireCoverage()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ESPApplicationException while getting inqureCoverage response:" + "ConversationId: " + getConversationId(), eae);
		} catch (RemoteException re) {
			vlogError("[CricketESPAdapter->inquireCoverage()]:" + CricketESPConstants.WHOOP_KEYWORD +  " RemoteException while getting inqureCoverage response:" + "ConversationId: " + getConversationId(), re);
		} catch (Exception re) {
		vlogError("[CricketESPAdapter->inquireCoverage()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Exception while getting inqureCoverage response:"  + "ConversationId: " + getConversationId(), re);
		}
		logInfo("[CricketESPAdapter->inquireCoverage()]: Exiting inquireCoverage()...");
		return responseVO;
	}
	
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	
	public String getSessionOrderRequestConversationInfo(CricketOrderImpl order) {
		
		String sessionOrderRequestStringInfo = CricketCommonConstants.EMPTY_STRING;
		
		// Getting the Session id				 
		 String sessionID = CricketCommonConstants.SESSION_ID + getSessionId();
		
		// Getting the Order Id
		 String orderId = CricketCommonConstants.EMPTY_STRING;
    		if(null != order){
    			if(!StringUtils.isBlank( order.getId())){
	    			orderId = order.getId();
				}
    		}
    		String orderID = CricketCommonConstants.ORDER_ID + orderId;
    		
    	// Getting the Page url
			String pageUrl = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageUrl = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}
			String pageURL = CricketCommonConstants.PAGE_URL + pageUrl;
		// Getting Conversation id
			String conversationId  =  CricketCommonConstants.CONVERSATION_ID + getConversationId();
			
			sessionOrderRequestStringInfo = sessionID + orderID + conversationId  + pageURL;
			return sessionOrderRequestStringInfo;
	}
	/**
	 * This method is used to to interact with the VESTA for payment authorize, debit and etc.
	 * @param requestInfo
	 * @return
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 */
	public ManagePaymentResponseVO managePayment(CricketAccountHolderAddressData accountHolderAddress,RepositoryItem profile, CricketProfile cricketProfile,
													CricketOrderImpl order,
													String ipAddress) throws CricketException, ESPApplicationException, RemoteException, MalformedURLException, ServiceException {
		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->managePayment()]: Entering into managePayment()...");
		}
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketESPAdapter->managePayment()]: Entering into managePayment()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		ManagePaymentResponseVO managePaymentResponseVO = null;
		ManagePayment managePayment = null;
		CricketCreditCard creditCard=null;
		long starttime = 0L;
		managePayment = new CricketESPHTTPServicesLocator().getManagePayment(new URL(getEspServiceUrl()+ESP_NAME_MANAGEPAYMENT));
		ManagePaymentStub managePaymentStub = (ManagePaymentStub) managePayment;
		
		intializeHeader(managePaymentStub,order.getId());
		//Populate ManagePaymentRequestInfo			
		ManagePaymentRequestInfoNewPaymentInfo newPaymentInfo = new ManagePaymentRequestInfoNewPaymentInfo();
		//for non logged in user - get it from createaActivationResponse otherwise from cookie		
		newPaymentInfo.setBillingAccountNumber(cricketProfile.getAccountNumber());
		newPaymentInfo.setMdn(getPhoneNumberInDigits(accountHolderAddress.getAccountAddress().getPhoneNumber()));
		newPaymentInfo.setTransactionId(order.getId()+ starttime);
		newPaymentInfo.setChannelCode(PaymentChannelCodeInfo.fromValue(new BigInteger(
							String.valueOf((getChannelCode())))));
		// get it from profile
		String markerCode = (String) profile.getPropertyValue(PROPERTY_MARKET_ID);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->managePayment()]: marketCode "+markerCode);
		}
		newPaymentInfo.setMarketCode(markerCode);
		
		PositiveInteger rateCenterIdPositiveInteger = new PositiveInteger(String.valueOf(getRateCenterId()));
		newPaymentInfo.setRateCenterId(rateCenterIdPositiveInteger);					
		newPaymentInfo.setJointVentureCode(getJointVentureCode());
		newPaymentInfo.setSettleForLessThanAuthorized(isSettleForLessThanAuthorized());
		newPaymentInfo.setIPAddress(ipAddress);
		newPaymentInfo.setAllowPendingPayment(isAllowPendingPayment());
		
		// get it from payment group -- checking for autobill payment
		@SuppressWarnings("unchecked")
		List<PaymentGroup> pgs = order.getPaymentGroups();
				
		for(PaymentGroup pg:pgs){
			 if(pg instanceof CricketCreditCard){
				 creditCard  =(CricketCreditCard)pg;
				 if(!creditCard.isDiffernetCard()){
					if(creditCard.getAutoBillPayment()){
					 newPaymentInfo.setEnrollABP(true);
					 } else {
						 newPaymentInfo.setEnrollABP(false);
						 break;
				  }
			 }
		 }
		}
 		// now getting the shipping address from shipping group.
		Address shippingAddress = getCricketESPAdapterHelper().getShippingAddressfromShippingGroup(order); 
		
		if(shippingAddress != null){			
			newPaymentInfo.setShippingAddress(getCricketESPAdapterHelper().getAddressInfo(shippingAddress));
		}
				
		ChargeCardInfo chargeCardInfo = getCricketESPAdapterHelper().createChargeCardInfo(creditCard, false);
		newPaymentInfo.setChargeCard(chargeCardInfo);
		// billing address
		Address billingAddress = getCricketESPAdapterHelper().getBillingAddressfromPaymentGroup(order);
		if(billingAddress != null){		
			chargeCardInfo.setChargeCardBillingAddress(getCricketESPAdapterHelper().getAddressInfo(billingAddress));
 		}
  		// creating request now...
		ManagePaymentRequestInfo requestInfo = new ManagePaymentRequestInfo();
		requestInfo.setCommission(getCricketESPAdapterHelper().getDealerCommission());
		requestInfo.setNewPaymentInfo(newPaymentInfo);
		requestInfo.setAmount(BigDecimal.valueOf(creditCard.getAmount())); 
		requestInfo.setPOSSalesId(((CricketOrderImpl)order).getPosSaleId());
	 	ManagePaymentRequestInfoCustomerContactInfo contactInfo = new ManagePaymentRequestInfoCustomerContactInfo();
	 		
		contactInfo.setBillingPhoneNumber(getPhoneNumberInDigits(accountHolderAddress.getAccountAddress().getPhoneNumber()));
		contactInfo.setContactPhoneNumber(getPhoneNumberInDigits(accountHolderAddress.getAccountAddress().getPhoneNumber()));
		contactInfo.setEmailAddress(accountHolderAddress.getAccountAddress().getEmail());
		requestInfo.setCustomerContactInfo(contactInfo);
		if(TRANSACTION_TYPE_RRC.equalsIgnoreCase(order.getWorkOrderType())){
			requestInfo.setAction(PaymentServiceActionInfo.fromValue(PSA_CREATE_ORDER));
		} else {
			requestInfo.setAction(PaymentServiceActionInfo.fromValue(PSA_CREATE_EQUIPMENT_ORDER));
		}
		
		// invoking esp1500100
		ManagePaymentResponseInfo managePaymentResponseInfo  = managePaymentStub.managePayment(requestInfo);
		if(isLoggingDebug()){			
			logDebug("[CricketESPAdapter->managePayment()]: managePaymentResponseInfo from ESP is: "+managePaymentResponseInfo);
		}		
		
		managePaymentResponseVO = getCricketESPAdapterHelper().getManagePaymentResponseVO(managePaymentResponseInfo, creditCard);

		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->managePayment()]: Exiting managePayment()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
	return managePaymentResponseVO;
	}
	
	
	/** managePaymentRRC - will be invoked in case of change feature and chagne plan only and that to up grade  i.e price increases.
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * 
	 */
	 public ManagePaymentResponseVO managePaymnetRRC(CricketOrderImpl order) throws ESPApplicationException, RemoteException, MalformedURLException, ServiceException{
		 
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketESPAdapter->managePayment()]: Entering into managePaymnetRRC()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		ManagePaymentResponseVO managePaymentResponseVO = null;
		ManagePayment managePayment = null;

		managePayment = new CricketESPHTTPServicesLocator().getManagePayment(new URL(getEspServiceUrl()+ESP_NAME_MANAGEPAYMENT));
		ManagePaymentStub managePaymentStub = (ManagePaymentStub) managePayment;
		intializeHeader(managePaymentStub,order.getId());
		
		ManagePaymentRequestInfo requestInfo = new ManagePaymentRequestInfo();
		ManagePaymentRequestInfoCompletePaymentInfo   completePaymentInfo = new ManagePaymentRequestInfoCompletePaymentInfo ();
		String vestaOrderId = order.getVestaSystemOrderId();
		long orderId = Long.parseLong(vestaOrderId);
		completePaymentInfo.setOrderId(BigInteger.valueOf(orderId));
		completePaymentInfo.setIsFulfillmentSuccessful(true);
		completePaymentInfo.setBillingOrderId(order.getBillingSystemOrderId());
		requestInfo.setAmount(BigDecimal.valueOf(Double.valueOf(new DecimalFormat("###.##").format(order.getPriceInfo().getTotal()))));
		
		// check this 
		requestInfo.setAction(PaymentServiceActionInfo.fromValue(PSA_COMPLETE_ORDER));
		requestInfo.setCompletePaymentInfo(completePaymentInfo); 
		// invoking ..
		ManagePaymentResponseInfo managePaymentResponseInfo  = managePaymentStub.managePayment(requestInfo);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->managePaymnetRRC()]: managePaymentResponseInfo from ESP is: "+managePaymentResponseInfo);			
		}
		managePaymentResponseVO = getCricketESPAdapterHelper().getManagePaymentResponseVO(managePaymentResponseInfo, null);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->managePaymnetRRC()]: Exiting managePaymnetRRC()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
			return managePaymentResponseVO;
	 }	
	
	/**
	 * This method is used to to interact with the VESTA for enroll/Unenroll Auto Bill Payment.
	 * @param requestInfo
	 * @return
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 */
	public ManageABPResponseVO manageABP(CricketOrderImpl order,RepositoryItem profile,CricketProfile cricketProfile) throws CricketException, MalformedURLException, ServiceException, ESPApplicationException, RemoteException {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketESPAdapter->manageABP()]: Entering into manageABP()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		ManageABPResponseVO manageABPResponseVO = null;
		ManageABP manageABP = null;
		manageABP = new CricketESPHTTPServicesLocator().getManageABP(new URL(getEspServiceUrl()+ESP_NAME_MANAGEABP));
		ManageABPStub manageABPStub = (ManageABPStub) manageABP;
		intializeHeader(manageABPStub,order.getId());
		//Populate ManageABPRequestInfo		
		
		ManageABPRequestInfoABPEnrollmentInfo enrollmentInfo = new ManageABPRequestInfoABPEnrollmentInfo();
		enrollmentInfo.setMdn(cricketProfile.getMdn());
		boolean isLoggedIn = !profile.isTransient();
		if(isLoggedIn){
			enrollmentInfo.setAccountType(AccountTypeIndicatorInfo.fromValue(cricketProfile.getEspCustomerType()));
			enrollmentInfo.setCustomerId((String)profile.getPropertyValue(CUSTOMER_TYPE));	
			enrollmentInfo.setBillCycleDay(BigInteger.valueOf(cricketProfile.getBillCycleDay())); 
		}else{
			String networkProviderName = (String)profile.getPropertyValue(NETWORK_PROVIDER);
			if(NETWORK_PROVIDER_NAME_CRICKET.equalsIgnoreCase(networkProviderName)){
				enrollmentInfo.setAccountType(AccountTypeIndicatorInfo.fromValue(ACCOUNT_TYPE_INDICATOR_PREPAID));
			}else if (NETWORK_PROVIDER_NAME_SPRINT.equalsIgnoreCase(networkProviderName)){
				enrollmentInfo.setAccountType(AccountTypeIndicatorInfo.fromValue(ACCOUNT_TYPE_INDICATOR_PAYGO));
			} 
			 enrollmentInfo.setBillCycleDay(BigInteger.valueOf(new Date().getDay())); 
		}		 		
		// some logic is der please check with client should be 401 or 537
		enrollmentInfo.setJointVentureCode(getJointVentureCode());
		// get Payment group from order
		CricketCreditCard creditCard = getCricketESPAdapterHelper().getPaymentGroup(order); 	
		ChargeCardInfo chargeCardInfo = getCricketESPAdapterHelper().createChargeCardInfo(creditCard, true);						
		enrollmentInfo.setChargeCard(chargeCardInfo);
		 
		ManageABPRequestInfo requestInfo = new ManageABPRequestInfo();
		requestInfo.setABPEnrollmentInfo(enrollmentInfo);
		// check with client
		requestInfo.setAction(ABPServiceActionInfo.fromValue(ENROLL_ABP));		
		requestInfo.setBillingAccountNumber(cricketProfile.getAccountNumber());
					
		requestInfo.setCommission(getCricketESPAdapterHelper().getDealerCommission());
	 					
		ManageABPResponseInfo manageABPResponseInfo  = manageABPStub.manageABP(requestInfo);
		//fill the responseVO based on success response
		ResponseInfo responseInfo = manageABPResponseInfo.getResponse();
		if(responseInfo != null){
			manageABPResponseVO = new ManageABPResponseVO();
			ResponseVO responseVO = new ResponseVO();
			responseVO.setCode(responseInfo.getCode());
			responseVO.setDescription(responseInfo.getDescription());		
			manageABPResponseVO.setResponse(responseVO);
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->manageABP()]: response code: "+responseInfo.getCode()+ " response description: " + responseInfo.getDescription());
			}
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->manageABP()]: Exiting manageABP()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
		return manageABPResponseVO;
	}	
 
	/**
	 * This method is used to interact with the Exact Target for sending email sign up and promotional Messages. 
	 * @param requestInfo
	 * @return
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 */
	public SendMessageResponseVO sendMessage(SendMessageRequestVO requestVO, String orderId) throws CricketException, MalformedURLException, ServiceException, ESPApplicationException, RemoteException {
	 
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			
	    		logDebug("[CricketESPAdapter->sendMessage()]: Entering into sendMessage()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		validateRequest(requestVO);
		SendMessageResponseVO responseVO = null;
		SendMessage sendMessage = null;
				
		sendMessage = new CricketESPHTTPServicesLocator().getSendMessage(new URL(getEspServiceUrl()+ESP_NAME_SENDMESSAGE));
		SendMessageStub sendMessageStub = (SendMessageStub) sendMessage;
		intializeHeader(sendMessageStub,orderId);
		//Populate SendMessageRequestInfo
		SendMessageRequestInfo requestInfo = new SendMessageRequestInfo();
		SendMessageRequestInfoMessageType msgTypeInfo = new SendMessageRequestInfoMessageType();
		
		SendMessageRequestInfoMessageTypeEmailInformation emailInfo = new SendMessageRequestInfoMessageTypeEmailInformation();
		emailInfo.setFromEmailAddress(requestVO.getFromEmail());
		emailInfo.setToEmailAddress(requestVO.getToEmail());
		String[] subjectLine = new String[1];
		subjectLine[0]=requestVO.getSubjectLine();
		emailInfo.setSubjectLine(subjectLine);
		
		msgTypeInfo.setEmailInformation(emailInfo);
		
		requestInfo.setMessageType(msgTypeInfo);
		
		SendMessageRequestInfoMessageTemplate msgTemplate = new SendMessageRequestInfoMessageTemplate();
		
		msgTemplate.setTemplateIdentifier(requestVO.getTemplateIdentifier());
		LanguagePreferenceInfo tmpltLanguage = LanguagePreferenceInfo.fromValue(requestVO.getTemplateLanguage());
		msgTemplate.setTemplateLanguage(tmpltLanguage);
		MessageTemplateTypeInfo tmpltType = MessageTemplateTypeInfo.fromValue(requestVO.getTemplateType());
		msgTemplate.setTemplateType(tmpltType);
		
		
		NameValuePairVO[] substitutionVariables = requestVO.getSubstitutionVariables();
		NameValuePairInfo[]  substitutionVariablesInfo = new NameValuePairInfo[substitutionVariables.length];
		int index=0;
		
		for(NameValuePairVO nameValue:substitutionVariables ){
			substitutionVariablesInfo[index] = new NameValuePairInfo();
			if(null!=nameValue){
			substitutionVariablesInfo[index].setName(nameValue.getName());
			substitutionVariablesInfo[index].setValue(nameValue.getValue());
			index++;
			}
		}
		
		msgTemplate.setSubstitutionVariables(substitutionVariablesInfo);
		
		requestInfo.setMessageTemplate(msgTemplate);
		SendMessageResponseInfo sendMessageResponseInfo = sendMessageStub.sendMessage(requestInfo);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->sendMessage()]: sendMessageResponseInfo: "+sendMessageResponseInfo);
		}
		responseVO = new SendMessageResponseVO();
		
		responseVO.setCode(sendMessageResponseInfo.getResponse().getCode());
		responseVO.setDescription(sendMessageResponseInfo.getResponse().getDescription());
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->sendMessage()]: Exiting sendMessage()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId );
		}
		return responseVO;
	}
	
	
	
	/**
	 * @param pCricketOrder
	 * @param pEmailId
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws ESPApplicationException
	 * @throws RemoteException
	 */
	public SendMessageResponseInfo sendMessageRRC(CricketOrder pCricketOrder,String pEmailId) throws MalformedURLException, ServiceException, ESPApplicationException, RemoteException{
	 
		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			
	    		logDebug("[CricketESPAdapter->sendMessageRRC()]: Entering into sendMessage()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getBillingSystemOrderId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		SendMessage sendMessage = null;
		SendMessageResponseInfo sendMessageresponseInfo=null;	
		 
			sendMessage = new CricketESPHTTPServicesLocator().getSendMessage(new URL(getEspServiceUrl()+ESP_NAME_SENDMESSAGE));
			SendMessageStub sendMessageStub = (SendMessageStub) sendMessage;
			intializeHeader(sendMessageStub,pCricketOrder.getId());
			//Populate SendMessageRequestInfo
			SendMessageRequestInfo requestInfo = new SendMessageRequestInfo();
			SendMessageRequestInfoMessageType msgTypeInfo = new SendMessageRequestInfoMessageType();
			SendMessageRequestInfoMessageTypeEmailInformation emailInfo = new SendMessageRequestInfoMessageTypeEmailInformation();
			emailInfo.setToEmailAddress(pEmailId);
			msgTypeInfo.setEmailInformation(emailInfo);
			requestInfo.setMessageType(msgTypeInfo);
			
			SendMessageRequestInfoMessageTemplate msgTemplate = new SendMessageRequestInfoMessageTemplate();
		 
			msgTemplate.setTemplateIdentifier(getCricketESPAdapterHelper().getCktConfiguration().getSendMessageTemplateIdentifier());
			if(!StringUtils.isBlank(pCricketOrder.getLanguageIdentifier()))
				msgTemplate.setTemplateLanguage(LanguagePreferenceInfo.fromValue(pCricketOrder.getLanguageIdentifier()));
			else
				msgTemplate.setTemplateLanguage(LanguagePreferenceInfo.fromValue("E"));
			requestInfo.setMessageTemplate(msgTemplate);
			
			
			NameValuePairInfo substitutionVariables[] = new NameValuePairInfo[2];
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getCricketESPAdapterHelper().getCktConfiguration().getSendMessagedateFormat());
			String orderDate = simpleDateFormat.format(pCricketOrder.getCreationDate());
			substitutionVariables[0]= new NameValuePairInfo();
			substitutionVariables[0].setName(ORDER_DATE);
			substitutionVariables[0].setValue(orderDate);
			
			substitutionVariables[1]= new NameValuePairInfo();
			substitutionVariables[1].setName(ORDER_NUMBER);
			substitutionVariables[1].setValue(pCricketOrder.getBillingSystemOrderId());
			
			msgTemplate.setSubstitutionVariables(substitutionVariables);
			
			requestInfo.setMessageTemplate(msgTemplate);
			
			sendMessageresponseInfo = sendMessageStub.sendMessage(requestInfo);
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->sendMessageRRC()]: sendMessageResponseInfo: "+sendMessageresponseInfo);
			}
			
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->sendMessageRRC()]: Exiting sendMessageRRC()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getBillingSystemOrderId() );
			}
		
		
		return sendMessageresponseInfo;
		
		
	}
	/**
	 * This method is used to to interact with the ManageSale.
	 * The service calls Inpowered anytime a sale needs to be initiated, updated, or cancelled. 
	 * Specifically, this service coordinates the following Inpowered APIs	 * 
	 * @param pString 
	 * 
	 * @param requestInfo
	 * @return
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 */
	
	public  ManageSaleResponseVO manageSale(String pOrderId) throws CricketException, MalformedURLException, ServiceException, ESPApplicationException, RemoteException {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 
	    		logDebug("[CricketESPAdapter->manageSale()]: Entering into manageSale()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		ManageSaleResponseVO manageSaleResponseVO = null;
		ManageSale manageSale = null;
		ManageSaleResponseInfo manageSaleResponseInfo = null;
 
	    manageSale = new CricketESPHTTPServicesLocator().getManageSale(new URL(getEspServiceUrl()+ESP_NAME_MANAGESALE));
		ManageSaleStub manageSaleStub = (ManageSaleStub) manageSale;
		
		intializeHeader(manageSaleStub,pOrderId);		
		
		ManageSaleRequestInfoNewSaleInformation manageInfoNewSaleInformation = new ManageSaleRequestInfoNewSaleInformation();			
		manageInfoNewSaleInformation.setDrawerId(getDrawerId());
		manageInfoNewSaleInformation.setSalesTransactionType(SalesTransactionTypeInfo.S);
		ManageSaleRequestInfo manageSaleRequestInfo = new ManageSaleRequestInfo();
			
		manageSaleRequestInfo.setPOSContext(getCricketESPAdapterHelper().getDealerCommission());		 
		manageSaleRequestInfo.setNewSaleInformation(manageInfoNewSaleInformation);			
		// invoking stub
		manageSaleResponseInfo = manageSaleStub.manageSale(manageSaleRequestInfo);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->manageSale()]: manageSaleResponseInfo: "+manageSaleResponseInfo);
		}		
		
		manageSaleResponseVO = getCricketESPAdapterHelper().getManageSaleResponseVO(manageSaleResponseInfo);
		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->manageSale()]: Exiting manageSale()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId );
		}
		return manageSaleResponseVO;
	}

	
	/**
	 * This method is used to to interact with the ManageSaleItem.
	 * This Service manages line items within each sale.  
	 * 
	 * @param requestInfo
	 * @return
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 */	
	@SuppressWarnings("unchecked")
	public  ManageSaleItemResponseVO manageSaleItem(CricketOrderImpl order,EspServiceResponseData espServiceResponseData ) throws CricketException, MalformedURLException, ServiceException, ESPApplicationException, RemoteException {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 				
	    		logDebug("[CricketESPAdapter->manageSaleItem()]: Entering into manageSaleItem()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
 		ManageSaleItemResponseVO manageSaleItemResponseVO = null;
		ManageSaleItem manageSaleItem = null;
 
		manageSaleItem = new CricketESPHTTPServicesLocator().getManageSaleItem(new URL(getEspServiceUrl()+ESP_NAME_MANAGESALEITEM));
		ManageSaleItemStub manageSaleItemStub = (ManageSaleItemStub) manageSaleItem;
		
		intializeHeader(manageSaleItemStub,order.getId());
		
		ManageSaleItemRequestInfo manageSaleItemRequestInfo = new ManageSaleItemRequestInfo();
		ManageSaleItemRequestInfoLineItem manageSaleItemRequestInfoLineItem = new ManageSaleItemRequestInfoLineItem();
		SalesLineItemInfo[] salesLineItemInfos = null;
		SalesLineItemInfo saleLineItemInfo = null;
		// loop the commerceItems
		List<CricketCommerceItemImpl> saleLineItems = order.getCommerceItems();

		List <PricingAdjustment> adjustments = null;
		StringBuilder notesList = new StringBuilder();
		RepositoryItem skuItem = null;
		String description=null;
		String productType = null;
		
		if(saleLineItems != null){
			int saleLineItemsCnt = 0;
			SalesLineItemPriceInfo priceInfo = null;
			salesLineItemInfos = getCricketESPAdapterHelper().createSaleLineItemInfo(order, saleLineItems);
		 // down grade plan - dont call
			if(!order.isDownGrade()){
					for(CricketCommerceItemImpl commerceItem : saleLineItems){
						productType = (String) commerceItem.getCricItemTypes();
						
						if(PHONE_PRODUCT.equalsIgnoreCase(productType) || 
							UPGRADE_PHONE.equalsIgnoreCase(productType) || 
							ACCESSORY_PRODUCT.equalsIgnoreCase(productType)){
							saleLineItemInfo = new SalesLineItemInfo();
							saleLineItemInfo.setSaleId(order.getPosSaleId());
							saleLineItemInfo.setQuantity((int)commerceItem.getQuantity());
							saleLineItemInfo.setBarCode(commerceItem.getCatalogRefId());  //sku id
							adjustments = commerceItem.getPriceInfo().getAdjustments();
						 
							for(PricingAdjustment adjustment:adjustments){
								if(ITEM_DISCOUNT.equalsIgnoreCase(adjustment.getAdjustmentDescription())){
									notesList.append((String)adjustment.getPricingModel().getPropertyValue(PROPERTY_ITEM_DISCOUNT_TYPE)).append(DELIMITER_PIPE);
								}
							}
							if(notesList.toString().length() > 0){
								saleLineItemInfo.setNote(notesList.toString());
							}
							saleLineItemInfo.setName((String)((RepositoryItem)commerceItem.getAuxiliaryData().getProductRef()).getPropertyValue(PROPERTY_DISPLAY_NAME));
							description = ((String)((RepositoryItem)commerceItem.getAuxiliaryData().getProductRef()).getPropertyValue(PROPERTY_DESCRIPTION));
							
							if(null!=description){
								saleLineItemInfo.setDescription(description);
							}else{
								saleLineItemInfo.setDescription((String)((RepositoryItem)commerceItem.getAuxiliaryData().getProductRef()).getPropertyValue(PROPERTY_DISPLAY_NAME));
							}
							priceInfo = new SalesLineItemPriceInfo();
							skuItem = (RepositoryItem)commerceItem.getAuxiliaryData().getCatalogRef();
							Double price =Double.valueOf(new DecimalFormat("###.##").format(commerceItem.getPriceInfo().getAmount()));
							 
							priceInfo.setPrice(BigDecimal.valueOf(price));
							saleLineItemInfo.setPricing(priceInfo);
							salesLineItemInfos[saleLineItemsCnt] = saleLineItemInfo;
							saleLineItemsCnt++;
						}
					}
					// shipping line item 
					if(!TRANSACTION_TYPE_RRC.equalsIgnoreCase(order.getWorkOrderType())) {
						saleLineItemInfo = new SalesLineItemInfo();
						saleLineItemInfo.setSaleId(order.getPosSaleId());
						saleLineItemInfo.setQuantity(1);
						saleLineItemInfo.setBarCode(getBarCodeShipping());  //sku id
						priceInfo = new SalesLineItemPriceInfo();
						Double price = 0.0;
						 
						priceInfo.setPrice(BigDecimal.valueOf(price));
						saleLineItemInfo.setPricing(priceInfo);
						salesLineItemInfos[saleLineItemsCnt] = saleLineItemInfo;
						saleLineItemsCnt++;
					}
			}// end of down grade plan
			// service payment for plan 
			saleLineItemInfo = new SalesLineItemInfo();
			saleLineItemInfo.setSaleId(order.getPosSaleId());
			saleLineItemInfo.setQuantity(1);
			saleLineItemInfo.setBarCode(getBarCodeServicePayment());  //sku id
			 
			saleLineItemInfo.setName(SERVICE_CHARGES);
			 
			priceInfo = new SalesLineItemPriceInfo();
			Double servicePrice =Double.valueOf(new DecimalFormat("###.##").format(espServiceResponseData.getTotalServiceAmount()));
			priceInfo.setPrice(BigDecimal.valueOf(servicePrice));
			saleLineItemInfo.setPricing(priceInfo);
			salesLineItemInfos[saleLineItemsCnt] = saleLineItemInfo;
			manageSaleItemRequestInfoLineItem.setSaleLineItem(salesLineItemInfos);
			}	 
		
			manageSaleItemRequestInfo.setPOSContext(getCricketESPAdapterHelper().getDealerCommission());
 			
			manageSaleItemRequestInfo.setLineItem(manageSaleItemRequestInfoLineItem);
			/********** invocation *********/
			ManageSaleItemResponseInfo response =  manageSaleItemStub.manageSaleItem(manageSaleItemRequestInfo);
			
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->manageSaleItem()]: manageSaleItemResponseInfo: "+ response);
			}
			
			manageSaleItemResponseVO = getCricketESPAdapterHelper().getManageSaleItemResponseVO(response);				
			
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->manageSaleItem()]: Exiting manageSaleItem()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
			}
		return manageSaleItemResponseVO;
	}	
	
	/**
	 * This method is used to to interact with the FinalizeSale.
	 * This set of services provides support for operations that pertain to Shopping Cart transactions. 
	 * This service supports the Finalize transaction. It passes final payment information through ESP to Inpowered.
	 * 
	 * @param requestInfo
	 * @return
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 */
	
	public  FinalizeSaleResponseVO finalizeSale(CricketOrderImpl order, CricketShippingAddressData shippingAddress, CricketProfile cricketProfile) throws CricketException, ESPApplicationException, RemoteException, MalformedURLException, ServiceException {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}								
	    		logDebug("[CricketESPAdapter->finalizeSale()]: Entering into finalizeSale()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
 		FinalizeSaleResponseVO finalizeSaleResponseVO = null;
		FinalizeSale finalizeSale = null;	
		FinalizeSaleResponseInfo finalizeSaleResponseInfo = null;
 			
		finalizeSale = new CricketESPHTTPServicesLocator().getFinalizeSale(new URL(getEspServiceUrl()+ESP_NAME_FINALIZESALE));				
		FinalizeSaleStub finalizeSaleStub  = (FinalizeSaleStub)finalizeSale;
		
		intializeHeader(finalizeSaleStub,order.getId());

		// Populate FinalizeSaleRequestInfo
		FinalizeSaleRequestInfo finalizeSaleRequestInfo = new FinalizeSaleRequestInfo();
		FinalizeSaleRequestInfoCustomer finalizeSaleRequestInfoCustomer = new FinalizeSaleRequestInfoCustomer();
		finalizeSaleRequestInfo.setSaleId(order.getPosSaleId());
		CricketShippingAddress shipAddress = shippingAddress.getShippingAddress();
		finalizeSaleRequestInfoCustomer.setBillingAccountNumber(cricketProfile.getAccountNumber());
		NameInfo name = new NameInfo();
		name.setFirstName(shipAddress.getFirstName());
		name.setLastName(shipAddress.getLastName());
		finalizeSaleRequestInfoCustomer.setName(name);
		finalizeSaleRequestInfo.setCustomer(finalizeSaleRequestInfoCustomer);
						
		WebCustomerAddressInfo shippingInfo = new WebCustomerAddressInfo();
		shippingInfo.setAddressLine1(shipAddress.getAddress1());
		shippingInfo.setCity(shipAddress.getCity());
		shippingInfo.setState(shipAddress.getStateAddress());
		shippingInfo.setCountry(getCountry(shipAddress.getCountry()));
		shippingInfo.setPostalCode(shipAddress.getPostalCode());
		shippingInfo.setFirstName(shipAddress.getFirstName());
		shippingInfo.setLastName(shipAddress.getLastName());
		finalizeSaleRequestInfo.setShippingInfo(shippingInfo);	
		
		finalizeSaleRequestInfo.setPOSContext(getCricketESPAdapterHelper().getDealerCommission());
			
		finalizeSaleResponseInfo = finalizeSaleStub.finalizeSale(finalizeSaleRequestInfo);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->finalizeSale()]: finalizeSaleResponseInfo: "+finalizeSaleResponseInfo);
		}
		
		finalizeSaleResponseVO = getCricketESPAdapterHelper().getFinalizeResponseVO(finalizeSaleResponseInfo);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->finalizeSale()]: Exiting finalizeSale()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
		return finalizeSaleResponseVO;
	}
	
	
	/** finalizeSaleforRRC - this method will be invoked in case of changeFeature/ChangePlan from processOrder pipeline
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * 
	 */
	public FinalizeSaleResponseVO finalizeSalePostBillingOrder(CricketOrderImpl order) throws ESPApplicationException, RemoteException, MalformedURLException, ServiceException
	{

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketESPAdapter->finalizeSalePostBillingOrder()]: Entering into finalizeSalePostBillingOrder()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		FinalizeSaleResponseVO finalizeSaleResponseVO = null;
		FinalizeSale finalizeSale = null;	
		FinalizeSaleResponseInfo finalizeSaleResponseInfo = null;
 			
		finalizeSale = new CricketESPHTTPServicesLocator().getFinalizeSale(new URL(getEspServiceUrl()+ESP_NAME_FINALIZESALE));				
		FinalizeSaleStub finalizeSaleStub  = (FinalizeSaleStub)finalizeSale;
		
		intializeHeader(finalizeSaleStub,order.getId());

		// Populate FinalizeSaleRequestInfo
		FinalizeSaleRequestInfo finalizeSaleRequestInfo = new FinalizeSaleRequestInfo();
		finalizeSaleRequestInfo.setSaleId(order.getPosSaleId());
		PaymentInfo[] paymentInfoList = new PaymentInfo[1];
		PaymentInfo paymentInfo = new PaymentInfo();
		
		paymentInfo.setPaymentType(PaymentTypeInfo.fromValue(PAYMENT_TYPE_SERVICE));
		paymentInfo.setPaymentOrigination(PaymentOriginationInfo.fromValue(PAYMENT_ORIGINATION_WEB));
		paymentInfo.setAutoConfirmPayment(true); 		
	 
 		if(isLoggingDebug()){
 			logDebug("[CricketESPAdapter->finalizeSalePostBillingOrder()]: isDowngrade..." + order.isDownGrade());
 		}
 		
 		if(!order.isDownGrade()){
 			paymentInfo.setPaymentAmount(BigDecimal.valueOf((Double.valueOf(new DecimalFormat("###.##").format(order.getPriceInfo().getTotal())))));
			// get it from payment group -- checking for autobill payment
			@SuppressWarnings("unchecked")
			List<PaymentGroup> pgs = order.getPaymentGroups();
			CricketCreditCard creditCard;
			for(PaymentGroup pg:pgs){
				 if(pg instanceof CricketCreditCard){
					 creditCard  =(CricketCreditCard)pg;
					 if(!creditCard.isDiffernetCard()){
						 CreditCardInfo card = new CreditCardInfo();
						 card.setCreditCardType(CreditCardTypeInfo.fromValue((creditCard.getCreditCardType())));
						 if(null==creditCard.getVestaToken()){
							 	if(isLoggingDebug()){
							 		logDebug("[CricketESPAdapter->finalizeSalePostBillingOrder()]: Taking Default Vesta Token");
							 	}
								card.setCreditCardNumber(getCricketESPAdapterHelper().getVestaTokenStub()); 
							} else {
								if(isLoggingDebug()){
									logDebug("[CricketESPAdapter->finalizeSalePostBillingOrder()]: Taking Dynamic Vesta Token");
								}
								card.setCreditCardNumber(creditCard.getVestaToken());
							}
									
						 CreditCardExpirationDateInfo creditCardExpirationDateInfo = new CreditCardExpirationDateInfo();
						 creditCardExpirationDateInfo.setExpirationMonth(creditCard.getExpirationMonth());
						 String twoDigitYear = creditCard.getExpirationYear().substring(2,creditCard.getExpirationYear().length());
						 creditCardExpirationDateInfo.setExpirationYear(twoDigitYear);
						 card.setCreditCardExpirationDate(creditCardExpirationDateInfo);
						 card.setCreditCardCustomerName(creditCard.getCcFirstName()+ SINGLE_SPACE + creditCard.getCcLastName());
						 card.setIsDebitCard(false);
						 card.setSecurity(creditCard.getCardVerificationNumber());
						 paymentInfo.setCreditDebitCard(card);
						 break;
					 } 
				 }
			}
 		}else{
 			paymentInfo.setPaymentAmount(BigDecimal.valueOf(0.00));
 			paymentInfo.setCashPayment(CricketCommonConstants.TRUE);
 		}
		paymentInfoList[0]=paymentInfo;
		DealerCommissionInfo dealerCommissionInfo = getCricketESPAdapterHelper().getDealerCommission();	
		dealerCommissionInfo.setCommissionableSalesRepresentative(order.getTeleSaleCode());
		finalizeSaleRequestInfo.setPOSContext(dealerCommissionInfo);
		
		long starttime = System.currentTimeMillis();
		paymentInfo.setSourceSystemPaymentTransactionId(order.getId()+ DELIMITER_UNDERSCORE + starttime); 
		
		finalizeSaleRequestInfo.setPayment(paymentInfoList);
		finalizeSaleResponseInfo = finalizeSaleStub.finalizeSale(finalizeSaleRequestInfo);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->finalizeSalePostBillingOrder()]: finalizeSaleResponseInfo: "+finalizeSaleResponseInfo);	
		}
		
		finalizeSaleResponseVO = getCricketESPAdapterHelper().getFinalizeResponseVO(finalizeSaleResponseInfo);	
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->finalizeSalePostBillingOrder()]: Exiting finalizeSalePostBillingOrder()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
		return finalizeSaleResponseVO;
	}
	
	/**
	 * @param pString 
	 * @param completeSaleRequestVO
	 * @return
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 * @throws CricketException
	 */
	public  CompleteSaleResponseVO completeSale(CricketOrderImpl order) throws MalformedURLException, ServiceException, ESPApplicationException, RemoteException   {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}		 					
	    		logDebug("[CricketESPAdapter->completeSale()]: Entering into completeSale()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
 		CompleteSaleResponseVO completeSaleResponseVO = null;
		CompleteSale completeSale = null;	
		CompleteSaleResponseInfo completeSaleResponseInfo = null;
 			
		completeSale = new CricketESPHTTPServicesLocator().getCompleteSale(new URL(getEspServiceUrl()+ESP_NAME_COMPLETESALE));			
		CompleteSaleStub completeSaleStub  = (CompleteSaleStub)completeSale;
		
		intializeHeader(completeSaleStub, order.getId());
		
		CompleteSaleRequestInfo completeSaleRequestInfo = new CompleteSaleRequestInfo();			
		completeSaleRequestInfo.setSaleId(order.getPosSaleId());		
		
		completeSaleRequestInfo.setPOSContext(getCricketESPAdapterHelper().getDealerCommission());		 	
			
		completeSaleResponseInfo = completeSaleStub.completeSale(completeSaleRequestInfo);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->completeSale()]: completeSaleResponseInfo: "+completeSaleRequestInfo);
		}
		
		completeSaleResponseVO = getCricketESPAdapterHelper().getCompleteSaleResponseVO(completeSaleResponseInfo);		

		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->completeSale()]: Exiting completeSale()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
		return completeSaleResponseVO;
	}	
		
	/**
	 * This method will return all(included, mandatory, optional) the features related to a particular plan
	 * @param requestVO
	 * @param pOrderId 
	 * @return
	 * @throws CricketException
	 */
	public Map<String,List<InquireFeaturesResponseVO>> inquireFeatures(InquireFeaturesRequestVO requestVO, String pOrderId) throws CricketException {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 				
	    		logDebug("[CricketESPAdapter->inquireFeatures()]: Entering into inquireFeatures()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		validateRequest(requestVO);
		Map<String,List<InquireFeaturesResponseVO>> planAddOnsMap = null;
		List<InquireFeaturesResponseVO> planIncludedAddOns = new ArrayList<InquireFeaturesResponseVO>();
		List<InquireFeaturesResponseVO> planMandatoryAddOns = new ArrayList<InquireFeaturesResponseVO>();
		List<InquireFeaturesResponseVO> planOptionalAddOns = new ArrayList<InquireFeaturesResponseVO>();
		List<InquireFeaturesResponseVO> planAllIncludedAddOns = new ArrayList<InquireFeaturesResponseVO>();
		List<InquireFeaturesResponseVO> hppAddOns = new ArrayList<InquireFeaturesResponseVO>();
		
		try {
			InquireFeaturesResponseVO inquireFeaturesResponseVO =  null;
			InquireFeatures inquireFeatures  = new CricketESPHTTPServicesLocator().getInquireFeatures(new URL(getEspServiceUrl()+ESP_NAME_INQUIREFEATURES));
			InquireFeaturesStub inquireFeaturesStub = (InquireFeaturesStub) inquireFeatures;
			InquireFeaturesRequestInfo inquireFeaturesRequestInfo = new InquireFeaturesRequestInfo();
			
			/*	Initializing the header */	
			intializeHeader(inquireFeaturesStub,pOrderId);
						 
			/* * Object for Each parameter*/		 
			DeviceInfo deviceInfo = new DeviceInfo();
			ManufacturerInfo manufacturerInfo = new ManufacturerInfo();
			FeatureSelectionCriteriaInfo featureSelectionCriteria = new FeatureSelectionCriteriaInfo();
			manufacturerInfo.setPhoneCode(requestVO.getPhoneCode());
			manufacturerInfo.setPhoneType(requestVO.getPhoneType());
			
			deviceInfo.setManufacturer(manufacturerInfo);
			deviceInfo.setHasEsnHistory(requestVO.getHasEsnHistory());
			deviceInfo.setIsCricketPhone(requestVO.getIsCricketPhone());
			featureSelectionCriteria.setDeviceInfo(deviceInfo);
			featureSelectionCriteria.setPricePlanCode(requestVO.getPricePlanCode());
			featureSelectionCriteria.setTransactionName(requestVO.getTransactionName());
			featureSelectionCriteria.setMarketId(requestVO.getMarketId());
			featureSelectionCriteria.setCustomerType(CustomerTypeInfo.S);
			featureSelectionCriteria.setAccountType(AccountTypeIndicatorInfo.P);
			featureSelectionCriteria.setSalesChannelName(requestVO.getSalesChannelName());
			if (!StringUtils.isEmpty(requestVO.getTransactionName()) && null != requestVO.getFeatureCodes() 
					&& CricketESPConstants.TRANSACTION_TYPE_RRC.equalsIgnoreCase(requestVO.getTransactionName())) {
				featureSelectionCriteria.setFeatureCodes(requestVO.getFeatureCodes());
			}
			inquireFeaturesRequestInfo.setFeatureSelectionCriteria(featureSelectionCriteria);						
			InquireFeaturesResponseInfo inquireFeaturesResponseInfo  = inquireFeaturesStub.inquireFeatures(inquireFeaturesRequestInfo);
			
			/* call this method to set conversation id for the first time from ESP response
			 * First time ESP call can happen from InquireCoverage or InquireFeatures or InquireAccount or ValidateAddress
			 * So this call is present in all the above 4 mentioned ESP calls
			 */	
			setConversationIdFromESPResponse(inquireFeaturesStub);
			
			InquireFeaturesResponseInfoInquireFeaturesResponseFeatures planfeatures[] = inquireFeaturesResponseInfo.getInquireFeaturesResponse();
			if(planfeatures != null && planfeatures.length > 0){
				for(InquireFeaturesResponseInfoInquireFeaturesResponseFeatures planFeature : planfeatures){
					inquireFeaturesResponseVO =  new InquireFeaturesResponseVO();
					inquireFeaturesResponseVO.setGroupName(planFeature.getGroupName());
					inquireFeaturesResponseVO.setId(planFeature.getId());
					inquireFeaturesResponseVO.setMandatory(planFeature.isIsMandatory());
					inquireFeaturesResponseVO.setName(planFeature.getName());
					inquireFeaturesResponseVO.setPrice(planFeature.getPrice());
					inquireFeaturesResponseVO.setType(planFeature.getType());
					if(planFeature.getType().equals(BigInteger.valueOf(FEATURE_TYPE_INCLUDED))) {
						planIncludedAddOns.add(inquireFeaturesResponseVO);
					}
					else if(!planFeature.getType().equals(BigInteger.valueOf(FEATURE_TYPE_INCLUDED)) && !planFeature.getType().equals(BigInteger.valueOf(FEATURE_TYPE_MANDATORY))){
						planOptionalAddOns.add(inquireFeaturesResponseVO);
					}
					else if(planFeature.getType().equals(BigInteger.valueOf(FEATURE_TYPE_MANDATORY))) {
						planMandatoryAddOns.add(inquireFeaturesResponseVO);
					}	
					if (null != planFeature.getGroupName() && planFeature.getGroupName().equalsIgnoreCase(CricketCommonConstants.HANDSET_PROTECTION_GROUP_NAME)) {
						hppAddOns.add(inquireFeaturesResponseVO);
					}
				}
			}
			planAllIncludedAddOns.addAll(planIncludedAddOns);
			planAllIncludedAddOns.addAll(planMandatoryAddOns);
			planAddOnsMap = new HashMap<String,List<InquireFeaturesResponseVO>>();
			planAddOnsMap.put(CricketCommonConstants.INCLUDED_ADDONS, planIncludedAddOns);
			planAddOnsMap.put(CricketCommonConstants.MANDATORY_ADDONS, planMandatoryAddOns);
			planAddOnsMap.put(CricketCommonConstants.OPTIONAL_ADDONS, planOptionalAddOns);
			planAddOnsMap.put(CricketCommonConstants.ALL_INCLUDED_ADDONS, planAllIncludedAddOns);
			planAddOnsMap.put(CricketCommonConstants.HPP_ADDONS, hppAddOns);
		} catch (ServiceException se) {
			vlogError("[CricketESPAdapter->inquireFeatures()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ServiceException while getting inquireFeatures response:"  + "ConversationId: " + getConversationId(), se);
		} catch (ESPApplicationException eae) {
			vlogError("[CricketESPAdapter->inquireFeatures()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ESPApplicationException while getting inquireFeatures response:"  + "ConversationId: " + getConversationId(), eae);
		} catch (RemoteException re) {
			vlogError("[CricketESPAdapter->inquireFeatures()]:" + CricketESPConstants.WHOOP_KEYWORD +  " RemoteException while getting inquireFeatures response:"  + "ConversationId: " + getConversationId(), re);
		} catch(Exception e){
			vlogError("[CricketESPAdapter->inquireFeatures()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Exception while getting inquireFeatures response:"  + "ConversationId: " + getConversationId(), e);
		}

		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->inquireFeatures()]: Exiting inquireFeatures()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId );
		}
		return planAddOnsMap;
	}
		
	/**
	 * This method is used to validate address provided by the address
	 * based on IsShippingAddress value the ESP will contact UPS or NEC
	 * IsShippingAddress -> true, this ESP validates the address with UPS
	 * IsShippingAddress -> false, this ESP validates the address with NEC
	 * @param requestVO
	 * @param pOrderId 
	 * @return
	 * @throws ServiceException 
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 */
	public ValidateAddressResponseVO validateAddress(ValidateAddressRequestVO requestVO, boolean isShippingAddress, String pOrderId) throws ServiceException, ESPApplicationException, RemoteException {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}							
	    		logDebug("[CricketESPAdapter->validateAddress()]: Entering into validateAddress()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		ValidateAddressResponseVO validateAddressResponseVO = null;
		
 		ValidateAddress validateAddress=null;
		try {
			validateAddress = new CricketESPHTTPServicesLocator().getValidateAddress(new URL(getEspServiceUrl()+ESP_NAME_VALIDATEADDRESS));
		} catch (MalformedURLException murle) {
			vlogError("[CricketESPAdapter->validateAddress()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Error in ValidateAddress ServiceLocator" + "ConversationId: " + getConversationId(), murle);
		}
		ValidateAddressStub validateAddressStub = (ValidateAddressStub)validateAddress;
		
		intializeHeader(validateAddressStub, pOrderId);		
		
		
		AddressInfo address = new AddressInfo();
		AddressVO addressVO = requestVO.getAddress();
		
		if(isLoggingDebug() && addressVO != null){
			logDebug("[CricketESPAdapter->validateAddress()]: requestVO details...");
			logDebug("AddressLine1: "+addressVO.getAddressLine1()+" City: "+addressVO.getCity()+" State: "+addressVO.getState()+" Country: "+addressVO.getCountry()+" ZipCode:"+addressVO.getZipCode()+" isShippingAddress: "+isShippingAddress+" pOrderId:"+pOrderId);
		}
		
		address.setAddressLine1(addressVO.getAddressLine1());
		address.setCity(addressVO.getCity());				
		address.setCountry(getCountry(addressVO.getCountry()));						
		address.setState(AddressStateInfo.fromString(addressVO.getState()));
		AddressZipInfo zip = new AddressZipInfo();					
		zip.setZipCode(addressVO.getZipCode());	
		address.setZip(zip);
		
		ValidateAddressRequestInfo addressReqInfo = new ValidateAddressRequestInfo();
		addressReqInfo.setAddress(address);
		addressReqInfo.setIsShippingAddress(isShippingAddress);	
	

		ValidateAddressResponseInfo validateAddressResponseInfo =  validateAddressStub.validateAddress(addressReqInfo);
		
		if(validateAddressResponseInfo != null){
		/* call this method to set conversation id for the first time from ESP response
		 * First time ESP call can happen from InquireCoverage or InquireFeatures or InquireAccount or ValidateAddress
		 * So this call is present in all the above 4 mentioned ESP calls
		 */	
			setConversationIdFromESPResponse(validateAddressStub);
		
			validateAddressResponseVO = getCricketESPAdapterHelper().getValidateAddressResponseVO(validateAddressResponseInfo);
		}				
				
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->validateAddress()]: Exiting validateAddress()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId );
		}
		return validateAddressResponseVO;
	}
	
	
	/**
	 * This method is used to calculate the delivery date of the items 
	 * @param pOrderId 
	 * @param requestVO
	 * @return
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 */
	public InquireDeliveryEstimateResponseVO inquireDeliveryEstimate(String zipCode,String country, String pOrderId) throws MalformedURLException, ServiceException, ESPApplicationException, RemoteException {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 				
	    		logDebug("[CricketESPAdapter->inquireDeliveryEstimate()]: Entering into inquireDeliveryEstimate()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		InquireDeliveryEstimateResponseVO inquireDeliveryEstimateResponseVO = null;
 		InquireDeliveryEstimate inquireDeliveryEstimate = new CricketESPHTTPServicesLocator().getInquireDeliveryEstimate(new URL(getEspServiceUrl()+ESP_NAME_INQUIREDELIVERYESTIMATE));
		InquireDeliveryEstimateStub inquireDeliveryEstimateStub = (InquireDeliveryEstimateStub)inquireDeliveryEstimate;
				
		intializeHeader(inquireDeliveryEstimateStub,pOrderId);	
		
		InquireDeliveryEstimateRequestInfo requestInfo = new InquireDeliveryEstimateRequestInfo();
		
		AddressInfo shipFromAddress = new AddressInfo();
		AddressZipInfo shipFromAddressZip = new AddressZipInfo();					
		shipFromAddressZip.setZipCode(getShipFromAddressZipCode());
		shipFromAddress.setZip(shipFromAddressZip);	
		shipFromAddress.setCountry(getCountry(country));
		requestInfo.setShipFromAddress(shipFromAddress);

		AddressInfo shipToAddress = new AddressInfo();		
		AddressZipInfo shipToAddressZip = new AddressZipInfo();					
		shipToAddressZip.setZipCode(zipCode);
		shipToAddress.setZip(shipToAddressZip);	
		shipToAddress.setCountry(getCountry(country));
		requestInfo.setShipToAddress(shipToAddress);
 	
		requestInfo.setResidentialAddressIndicator(true);
		requestInfo.setShipDate(new Date());	 
				
		InquireDeliveryEstimateResponseInfo inquireDeliveryEstimateResponseInfo = inquireDeliveryEstimateStub.inquireDeliveryEstimate(requestInfo);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->inquireDeliveryEstimate()]: response from inquireDeliveryEstimate(): "+ inquireDeliveryEstimateResponseInfo);
		}
		
		inquireDeliveryEstimateResponseVO = getCricketESPAdapterHelper().getInquireDeliveryEstimateResponseVO(inquireDeliveryEstimateResponseInfo);
		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->inquireDeliveryEstimate()]: Exiting inquireDeliveryEstimate()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId );
		}
		return inquireDeliveryEstimateResponseVO;
	}
	
	
	/**
	 * This method is used to create the billing quote for the Line of Service
	 * @param billingAddressData
	 * @param shippingAddressData
	 * @param accountHolderAddressData
	 * @param catalogTools
	 * @param cricketProfile
	 * @param profile
	 * @param locationInfo
	 * @param order
	 * @return
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 */
	public ActivationQuoteResponseVO createActivationQuote( CricketBillingAddressData billingAddressData,
															CricketShippingAddressData shippingAddressData,
															CricketAccountHolderAddressData accountHolderAddressData,
 															CricketProfile cricketProfile,
															RepositoryItem profile, 
															MyCricketCookieLocationInfo locationInfo,
															CricketOrderImpl order, UserSessionBean userSessionBean ) throws ESPApplicationException, RemoteException, MalformedURLException, ServiceException {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketESPAdapter->createActivationQuote()]: Entering into createActivationQuote()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		ActivationQuoteResponseVO activationQuoteResponseVO = null;
		Profile profileItem = getProfileServices().getCurrentProfile();
		 
		//profile.isTransient() -> false for a Loggedin  User
		//profile.isTransient() -> true for a Non Loggedin User
		boolean isLoggedIn = !profile.isTransient();
		 String orderType = order.getWorkOrderType();
		CreateActivationQuote activationQuote = new CricketESPHTTPServicesLocator().getCreateActivationQuote(new URL(getEspServiceUrl()+ESP_NAME_CREATEACTIVATIONQUOTE));
		CreateActivationQuoteStub activationQuoteStub = (CreateActivationQuoteStub)activationQuote;
				
		intializeHeader(activationQuoteStub,order.getId());	
		
		CreateActivationQuoteRequestInfoOrderInfo orderInfo = new CreateActivationQuoteRequestInfoOrderInfo();
		orderInfo.setTransactionType(orderType); 
		CreateActivationQuoteRequestInfoCustomer customer = null;
		NameInfo name = null;
		DeviceInfo deviceInfo = null;
		ManufacturerInfo manufacturerInfo = null;
 						
		customer = new CreateActivationQuoteRequestInfoCustomer();
		if(isLoggingDebug() && cricketProfile != null){
			logDebug("[CricketESPAdapter->createActivationQuote()]: cookie Info..."+cricketProfile.toString());
		}
		if(isLoggedIn){
			customer.setCustomerId(cricketProfile.getCustomerId());
			customer.setCustomerType(CustomerTypeInfo.fromValue(cricketProfile.getEspCustomerType()));
		} else{
			customer.setCustomerType(CustomerTypeInfo.fromValue(getCustomerType()));
		}
		name = new NameInfo();
		name.setFirstName(accountHolderAddressData.getAccountAddress().getFirstName());
		name.setLastName(accountHolderAddressData.getAccountAddress().getLastName());
		customer.setName(name);
		
		AddressInfo billingAddress = new AddressInfo();
		billingAddress.setAddressLine1(billingAddressData.getBillingAddress().getAddress1());
		billingAddress.setCity(billingAddressData.getBillingAddress().getCity());
		billingAddress.setState(AddressStateInfo.fromValue(billingAddressData.getBillingAddress().getStateAddress()));
		AddressZipInfo zipInfo = new AddressZipInfo();
		zipInfo.setZipCode(billingAddressData.getBillingAddress().getPostalCode());
		billingAddress.setZip(zipInfo);
		billingAddress.setCountry(getCountry(billingAddressData.getBillingAddress().getCountry()));
		customer.setBillingAddress(billingAddress); 
		
		PhoneInfo phoneInfo = new PhoneInfo();				 
		phoneInfo.setHomePhone(getPhoneNumberInDigits(accountHolderAddressData.getAccountAddress().getPhoneNumber()));
		customer.setPhone(phoneInfo);
			
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setEmailAddress(accountHolderAddressData.getAccountAddress().getEmail());
		customer.setEmail(emailInfo);
		/* how to get the language in case of spanish */
		customer.setPreferedLanguage(LanguagePreferenceInfo.fromValue(order.getLanguageIdentifier()));//LANGUAGE_PREFERENCE_ENGLISH)); 
		
		CreateActivationQuoteRequestInfoCustomerIdentity identity = new 
				CreateActivationQuoteRequestInfoCustomerIdentity();
 		if(accountHolderAddressData.getYear() != null && accountHolderAddressData.getMonth() != null &&
 				accountHolderAddressData.getDay() != null){
			BirthInfo dob = new BirthInfo();
			dob.setDateOfBirth(new GregorianCalendar(Integer.parseInt(accountHolderAddressData.getYear()), 
					Integer.parseInt(accountHolderAddressData.getMonth()), Integer.parseInt(accountHolderAddressData.getDay())).getTime());//customerVO.getDateOfBirth().getTime());
			identity.setBirth(dob);
		}
 		if(isLoggedIn){
 			identity.setSocialSecurityNumber(accountHolderAddressData.getSocialSecurityNumber());
 		}else{
 			identity.setSocialSecurityNumber(getSocialSecurityNumber());
 		}
 		customer.setIdentity(identity);
		CreateActivationQuoteRequestInfoAccount account = new CreateActivationQuoteRequestInfoAccount();
		// waiting for business clarification to set billing
			
		if(isLoggedIn){
			account.setAccountType(AccountTypeIndicatorInfo.fromValue(cricketProfile.getAccountType()));
			account.setBillingAccountNumber(cricketProfile.getAccountNumber());
		}else{
			String networkProviderName = (String)profile.getPropertyValue(NETWORK_PROVIDER);
			if(NETWORK_PROVIDER_NAME_CRICKET.equalsIgnoreCase(networkProviderName)){
				account.setAccountType(AccountTypeIndicatorInfo.fromValue(ACCOUNT_TYPE_INDICATOR_PREPAID));
			}else if (NETWORK_PROVIDER_NAME_SPRINT.equalsIgnoreCase(networkProviderName)){
				account.setAccountType(AccountTypeIndicatorInfo.fromValue(ACCOUNT_TYPE_INDICATOR_PAYGO));
			} else {
				vlogError("[CricketESPAdapter->createActivationQuote()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Unknown networkProviderName ....... "  + "ConversationId: " + getConversationId() +networkProviderName);
			}
		}
		EffectiveDatesInfo effectiveDates = new EffectiveDatesInfo();
		effectiveDates.setEffectiveDate(getCricketESPAdapterHelper().getPacificDateTime(new Date())); 
		account.setEffectiveDates(effectiveDates);
 
		BillingMarketInfo billingMarketInfo = new BillingMarketInfo();
		String marketId = null;
		if(isLoggedIn){
			marketId = cricketProfile.getMarketCode();			
		}else{
			marketId = (String)profile.getPropertyValue(PROPERTY_MARKET_ID);
		}
		 billingMarketInfo.setMarketId(marketId);
		 String networkProviderName = (String)profile.getPropertyValue(NETWORK_PROVIDER);
		if(NETWORK_PROVIDER_NAME_SPRINT.equalsIgnoreCase(networkProviderName)){
			account.setNetworkProviderName(NetworkProviderNameInfo.fromValue((String)profile.getPropertyValue(NETWORK_PROVIDER)));
			account.setSprintCsa((String)profile.getPropertyValue(SPRINT_CSA));
		} else {
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->createActivationQuote()]: RateCenter Id from Profile:"+(String)(profile.getPropertyValue(PROPERTY_RATE_CENTER_ID)));
			}
			PositiveInteger rateCenterIdPositiveInteger = new PositiveInteger((String)(profile.getPropertyValue(PROPERTY_RATE_CENTER_ID))); 
			billingMarketInfo.setRateCenterId(rateCenterIdPositiveInteger);
		}
		billingMarketInfo.setJointVentureCode((String)profile.getPropertyValue(PROPERTY_JOINT_VENTURE)); 
		account.setBillingMarket(billingMarketInfo);
			
		AddressInfo marketAddress = new AddressInfo();
		marketAddress.setAddressLine1(accountHolderAddressData.getAccountAddress().getAddress1());
		marketAddress.setCity(accountHolderAddressData.getAccountAddress().getCity());
		marketAddress.setState(AddressStateInfo.fromValue(accountHolderAddressData.getAccountAddress().getStateAddress()));
		AddressZipInfo zip = new AddressZipInfo();					
		zip.setZipCode(accountHolderAddressData.getAccountAddress().getPostalCode());	
		marketAddress.setZip(zip);
		marketAddress.setCountry(getCountry(accountHolderAddressData.getAccountAddress().getCountry()));
		account.setMarketAddress(marketAddress);
		
		BillingPreferencesInfo billingPreferencesInfo = new BillingPreferencesInfo();
		billingPreferencesInfo.setLanguage(LanguagePreferenceInfo.fromValue(order.getLanguageIdentifier()));//LANGUAGE_PREFERENCE_ENGLISH)); 
		account.setBillingPreferences(billingPreferencesInfo);
		if(accountHolderAddressData.isEmailnotification()){
			account.setSolicitationContactPreference(SolicitationContactPreferenceInfo.fromValue(CONTACT_PREFERENCE_SMS_EMAIL));
		} else {
			account.setSolicitationContactPreference(SolicitationContactPreferenceInfo.fromValue(getSolicitationContactPreference()));

		}
		// loop the packages
		List<CricketPackage> packageItem = order.getCktPackages(); 
		CreateActivationQuoteRequestInfoAccountSubscriber[] subscriberInfoList = new CreateActivationQuoteRequestInfoAccountSubscriber[getCricketESPAdapterHelper().getCricketPackageCount(order)];
		int subcriberIndex=0;
		int orderLinenumber = 0;
		String productName = null;
		ShipmentLineInfo shipmentLineInfo = null;
		double taxAmount = 0.0;
		List<String> parcMandatoryOfferCodes = null;
		List<String> chargableAddons = null;
		CreateActivationQuoteRequestInfoAccountSubscriber subscriberInfo =  null;		
 		int multilineDiscountIndex = 0;
		int packageIndex = 0;
		for (CricketPackage packItem : packageItem) {	
			multilineDiscountIndex = 0;
			packageIndex++;
		    subscriberInfo = new CreateActivationQuoteRequestInfoAccountSubscriber();
			subscriberInfo.setBillingResponsibility(isBillingResponsibility());				 
			
			if(null!=order.getWorkOrderType() &&( order.getWorkOrderType().equalsIgnoreCase(CricketESPConstants.TRANSACTION_TYPE_ACT) || order.getWorkOrderType().equalsIgnoreCase(CricketESPConstants.TRANSACTION_TYPE_ADD))){
				if(packageIndex==1 && order.getWorkOrderType().equalsIgnoreCase(CricketESPConstants.TRANSACTION_TYPE_ACT)){
					multilineDiscountIndex = 0;
				}else if(packageItem.size()>1 && order.getWorkOrderType().equalsIgnoreCase(CricketESPConstants.TRANSACTION_TYPE_ACT)){
					multilineDiscountIndex = 1;
				}else if(order.getWorkOrderType().equalsIgnoreCase(CricketESPConstants.TRANSACTION_TYPE_ADD)){
					multilineDiscountIndex = 1;
				}
			}
			EffectiveDatesInfo effectiveDatesInfo = new EffectiveDatesInfo();
			effectiveDatesInfo.setEffectiveDate(getCricketESPAdapterHelper().getPacificDateTime(new Date())); 
			subscriberInfo.setEffectiveDate(effectiveDatesInfo);				 
				
			ContractTermInfo contractTermInfo = new ContractTermInfo();
			contractTermInfo.setTerm(new BigInteger(String.valueOf(getCricketESPAdapterHelper().getContractTerm()))); 
			contractTermInfo.setCommission(getCricketESPAdapterHelper().getDealerCommission());
			subscriberInfo.setContract(contractTermInfo);						
				 
			CreateActivationQuoteRequestInfoAccountSubscriberContactInformation subscriberInfoContactInformation = new CreateActivationQuoteRequestInfoAccountSubscriberContactInformation();
			name = new NameInfo();
			name.setFirstName(accountHolderAddressData.getAccountAddress().getFirstName());
			name.setLastName(accountHolderAddressData.getAccountAddress().getLastName());
			subscriberInfoContactInformation.setName(name);
			subscriberInfo.setContactInformation(subscriberInfoContactInformation);

			String[] cricketOfferingCodes = null;
			CricketCommerceItemImpl[] commerceItemList = getCricketOrderTools().getCommerceItemsForPackage(packItem,order);
			int commerceItemCount = commerceItemList.length;
			RepositoryItem product=null;
			String productType=null;
			RepositoryItem skuItem =null;
			String ratePlanId=null;
			chargableAddons = new ArrayList<String>();
			parcMandatoryOfferCodes = new ArrayList<String>();
			String manufacturerCode = null;
			String modelNumber = null;
			String serviceType=null;
			String itemId = null;
			String addOnOfferId = null;
			// normal items.		
			if(commerceItemList != null && commerceItemCount >0){
				ShipmentLineInfo[] shipmentLineInfos = new ShipmentLineInfo[1];
				for(CricketCommerceItemImpl commerceItem:commerceItemList)
				{					
					product = (RepositoryItem)commerceItem.getAuxiliaryData().getProductRef();
					productType =(String) commerceItem.getCricItemTypes();
					productName=(String)product.getPropertyValue(PROPERTY_DISPLAY_NAME);
					itemId = commerceItem.getCatalogRefId();
					if(isLoggingDebug()){
						logDebug("[CricketESPAdapter->createActivationQuote()]: CommerceItem info - ProductName: "+ productName + " ProductId: "+ itemId + " ProductType: "+productType);
					}					
				
					if ((null!=productType &&  PHONE_PRODUCT.equalsIgnoreCase(productType))){
						shipmentLineInfo = new ShipmentLineInfo();
						shipmentLineInfo.setOrderLineNumber(String.valueOf(orderLinenumber));
						shipmentLineInfo.setItemId(itemId);							
						shipmentLineInfo.setQuantity((int)commerceItem.getQuantity());
						shipmentLineInfo.setUnitOfMeasure(UnitOfMeasureInfo.fromValue(UNIT_OF_MEASURE_EA));
						shipmentLineInfo.setProductName(productName);
						
						deviceInfo = new DeviceInfo();
						manufacturerInfo = new ManufacturerInfo();
						skuItem =(RepositoryItem)(commerceItem.getAuxiliaryData().getCatalogRef());
						manufacturerCode =((String)skuItem.getPropertyValue(MANUFACTURER_CODE)) == null ? EMPTY_STRING :
											((String)skuItem.getPropertyValue(MANUFACTURER_CODE)).trim();
						modelNumber=((String)skuItem.getPropertyValue(MODEL_NUMBER)) == null ? EMPTY_STRING :
							((String)skuItem.getPropertyValue(MODEL_NUMBER)).trim();
						serviceType=((String)skuItem.getPropertyValue(SERVICE_TYPE)) == null ? EMPTY_STRING :
							((String)skuItem.getPropertyValue(SERVICE_TYPE)).trim();
						manufacturerInfo.setModel(manufacturerCode); 
						manufacturerInfo.setPhoneCode(modelNumber);
						manufacturerInfo.setPhoneType(serviceType);
					 	deviceInfo.setManufacturer(manufacturerInfo);
						shipmentLineInfo.setDeviceInfo(deviceInfo);
						subscriberInfo.setDevice(deviceInfo);
						shipmentLineInfo.setBasePrice(BigDecimal.valueOf(commerceItem.getPriceInfo().getAmount()));
						shipmentLineInfo.setLineTax1(BigDecimal.valueOf(taxAmount));
						
						shipmentLineInfos[orderLinenumber] = shipmentLineInfo;
					
						// manual approval need for physical goods
						orderInfo.setManualOrderApprovalNeeded(true);					
					}					
					if (null!=productType &&  PLAN_PRODUCT.equalsIgnoreCase(productType)) {
						// get the list of retePlan for multiple package
						ratePlanId = (String)product.getPropertyValue(PROPERTY_PARC_PLAN_ID);
						if(isLoggingDebug()){
							logDebug("[CricketESPAdapter->createActivationQuote()]: CommerceItem info - RatePlanId "+ratePlanId+ " RepositoryId: "+ product.getRepositoryId() + " ProductType: "+productType);
						}					
						
						//getting mandatoryoffercodes from inquirefeatures
						Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersMap = null;
						List<InquireFeaturesResponseVO> mandatoryOffersList = null;
						if(null != userSessionBean){
							mandatoryOffersMap = userSessionBean.getMandatoryOffersMap();
							if(null != mandatoryOffersMap){
								mandatoryOffersList = mandatoryOffersMap.get(packItem.getId());
							}
						}
						if(mandatoryOffersMap == null || mandatoryOffersList == null || mandatoryOffersList.size() == 0){											 
							
							Map<String, List<InquireFeaturesResponseVO>> planAllAddOnsMap = getDisplayFeaturesManager().getCompatibleAddons(modelNumber,
																																			PHONE_TYPE_VOICE, 
																																			(String)((RepositoryItem)commerceItem.getAuxiliaryData().getProductRef()).getRepositoryId(),
																																			marketId,
																																			order.getWorkOrderType(), null,
																																			false,
																																			true,
																																			getSalesChannel(),order.getId());
							if(null!= planAllAddOnsMap){
								mandatoryOffersList = planAllAddOnsMap.get(CricketCommonConstants.MANDATORY_ADDONS);
								List<InquireFeaturesResponseVO> optionalAddOns = planAllAddOnsMap.get(CricketCommonConstants.OPTIONAL_ADDONS);
								
								InquireFeaturesResponseVO activationFeeFeature = getCricketESPAdapterHelper().getActivationFeeAddOn(optionalAddOns);
								if(activationFeeFeature != null && activationFeeFeature.getPrice() != null) {
						    		profileItem.setPropertyValue(CricketCommonConstants.ACTIVATION_FEE, activationFeeFeature.getPrice().doubleValue());
						    	}
								
								
								InquireFeaturesResponseVO administrationFeeFeature = getCricketESPAdapterHelper().getAdministrationFeeAddOn(optionalAddOns);								
								if(mandatoryOffersList != null && administrationFeeFeature != null){
									//Administration Fee addOn needs to be sent to billing system for charging the user on monthly basis
					    			//Hence adding to mandatoryAddOns list which will be used in CreateActivationQuote request for building CricketOfferingCodes
									mandatoryOffersList.add(administrationFeeFeature);
								}
							}
						}
						if(mandatoryOffersList != null && mandatoryOffersList.size() > 0){							
							for(InquireFeaturesResponseVO mandatoryOfferVO : mandatoryOffersList){
								if(isLoggingDebug()){
									logDebug("[CricketESPAdapter->createActivationQuote()]: FeatureCodes:- " + mandatoryOfferVO.getId() + " - Type" + mandatoryOfferVO.getType());
								}
								parcMandatoryOfferCodes.add(mandatoryOfferVO.getId());
							}
						}
					} // end of plan product
								
					if(null!=productType && ADD_ON_PRODUCT.equalsIgnoreCase(productType)){
						RepositoryItem addonProductItem = (RepositoryItem)commerceItem.getAuxiliaryData().getProductRef();
						
						if(addonProductItem.getPropertyValue(PROPERTY_OFFER_ID) != null){
							addOnOfferId = (String)addonProductItem.getPropertyValue(PROPERTY_OFFER_ID);
							if(isLoggingDebug()){
								logDebug("[CricketESPAdapter->createActivationQuote()]: CommerceItem info - addOnOfferId: "+ addOnOfferId + " AddonProductItem's RepositoryId: "+ addonProductItem.getRepositoryId() + " ProductType: "+productType);
							}
							chargableAddons.add(addOnOfferId);
						}
					}
					product = null;
				} // end
				
				// now setting offering codes 
				int offeringCodesindex = 0;
				int parcOfferCodesSize = 0;
				if(null!= parcMandatoryOfferCodes &&parcMandatoryOfferCodes.size()>0 ){
					parcOfferCodesSize = parcMandatoryOfferCodes.size();
				}
				int activationFeeQuoteIndex = 0;
				if(profile.getPropertyValue(CricketCommonConstants.ACTIVATION_FEE)!=null && ((Double)profile.getPropertyValue(CricketCommonConstants.ACTIVATION_FEE))>0.0){
					activationFeeQuoteIndex = 1;
				}
				if(null!=chargableAddons && chargableAddons.size()>0){
					int chargeOfferCodesSize = chargableAddons.size();
 					if(profile.getPropertyValue(CricketCommonConstants.ACTIVATION_FEE)!=null && ((Double)profile.getPropertyValue(CricketCommonConstants.ACTIVATION_FEE))>0.0){
						cricketOfferingCodes = new String[parcOfferCodesSize+chargeOfferCodesSize+1+multilineDiscountIndex+activationFeeQuoteIndex];
						cricketOfferingCodes[offeringCodesindex] = getActivationFeeOfferId(); //1076
						offeringCodesindex++;
						if(multilineDiscountIndex ==1){
							if(TRANSACTION_TYPE_ADD.equalsIgnoreCase(order.getWorkOrderType())){
								// add aline - exising user
								cricketOfferingCodes[offeringCodesindex] = getMultilineDiscountOfferIdForADD();
							}
							else{
								// new user
								cricketOfferingCodes[offeringCodesindex] = getMultilineDiscountOfferId();
							}
							offeringCodesindex++;
						}
						
					}else{
						cricketOfferingCodes = new String[parcOfferCodesSize+chargeOfferCodesSize+1+multilineDiscountIndex];
						if(multilineDiscountIndex ==1){
							if(TRANSACTION_TYPE_ADD.equalsIgnoreCase(order.getWorkOrderType())){
								// add aline - exising user
								cricketOfferingCodes[offeringCodesindex] = getMultilineDiscountOfferIdForADD();
							}
							else{
								// new user
								cricketOfferingCodes[offeringCodesindex] = getMultilineDiscountOfferId();
							}
							offeringCodesindex++;
						}
					}
					for(int addOnIndex=0; addOnIndex < chargeOfferCodesSize; addOnIndex++){
						cricketOfferingCodes[offeringCodesindex] = chargableAddons.get(addOnIndex);
						offeringCodesindex++;
					}
				}else{
 					if(profile.getPropertyValue(CricketCommonConstants.ACTIVATION_FEE)!=null && ((Double)profile.getPropertyValue(CricketCommonConstants.ACTIVATION_FEE))>0.0){
						cricketOfferingCodes = new String[parcOfferCodesSize+1+multilineDiscountIndex+activationFeeQuoteIndex];
						cricketOfferingCodes[offeringCodesindex] = getActivationFeeOfferId();
						offeringCodesindex++;
						if(multilineDiscountIndex ==1){
							if(TRANSACTION_TYPE_ADD.equalsIgnoreCase(order.getWorkOrderType())){
								// add aline - exising user
								cricketOfferingCodes[offeringCodesindex] = getMultilineDiscountOfferIdForADD();
							}
							else{
								// new user
								cricketOfferingCodes[offeringCodesindex] = getMultilineDiscountOfferId();
							}
							offeringCodesindex++;
						}
					
					}else{
						cricketOfferingCodes = new String[parcOfferCodesSize+1+multilineDiscountIndex];
						if(multilineDiscountIndex ==1){
							if(TRANSACTION_TYPE_ADD.equalsIgnoreCase(order.getWorkOrderType())){
								// add aline - exising user
								cricketOfferingCodes[offeringCodesindex] = getMultilineDiscountOfferIdForADD();
							}
							else{
								// new user
								cricketOfferingCodes[offeringCodesindex] = getMultilineDiscountOfferId();
							}
							offeringCodesindex++;
						}
					}
				}
				
				if(isLoggingDebug()){
					logDebug("CreateActivationQuote - ratePlan Id: "+ratePlanId);
				}
				
				//setting rateplan id
				cricketOfferingCodes[offeringCodesindex] = ratePlanId;
				
				if(null!= parcMandatoryOfferCodes &&parcMandatoryOfferCodes.size()>0 ){
					for(int addOnIndex=0; addOnIndex < parcOfferCodesSize; addOnIndex++){
						offeringCodesindex++;
						cricketOfferingCodes[offeringCodesindex] = parcMandatoryOfferCodes.get(addOnIndex);
					 }
				}
	 		
				subscriberInfo.setCricketOfferingCodes(cricketOfferingCodes);
				
				// final set
				subscriberInfo.setShippingOrderLines(shipmentLineInfos);
				subscriberInfoList[subcriberIndex] =subscriberInfo;
				subcriberIndex++;
			}
		}// end of package loop
			
		// now checking accessories and adding to subscriber
		List<CricketCommerceItemImpl> commerceItemList = order.getCommerceItems();		
		int accessoryItemCount = getCricketESPAdapterHelper().getAccessoriesItemCount(commerceItemList);
		String accessoryProductType = null;
		RepositoryItem accessoryProductItem = null;
		int accessoryLineItemIndex = 0;
		String accessoryProductName = null;
		
		if(commerceItemList != null && accessoryItemCount >0){
			orderLinenumber++;
				ShipmentLineInfo[] shipmentLineInfosforAccessories = new ShipmentLineInfo[accessoryItemCount];
			for(CricketCommerceItemImpl accessoryItem:commerceItemList)
			{
				accessoryProductType = accessoryItem.getCricItemTypes();
				if(null!=accessoryProductType && ACCESSORY_PRODUCT.equalsIgnoreCase(accessoryProductType)){
					  accessoryProductItem = (RepositoryItem)accessoryItem.getAuxiliaryData().getProductRef();
					  accessoryProductName=(String)accessoryProductItem.getPropertyValue(PROPERTY_DISPLAY_NAME);	
					  if(isLoggingDebug()){
							logDebug("[CricketESPAdapter->createActivationQuote()]: CommerceItem info - AccessoryProductId: "+ accessoryItem.getCatalogRefId());
						}
					shipmentLineInfo = new ShipmentLineInfo();
					shipmentLineInfo.setOrderLineNumber(String.valueOf(orderLinenumber));
					shipmentLineInfo.setItemId(accessoryItem.getCatalogRefId());							
					shipmentLineInfo.setQuantity((int)accessoryItem.getQuantity());
					shipmentLineInfo.setUnitOfMeasure(UnitOfMeasureInfo.fromValue(UNIT_OF_MEASURE_EA));
					shipmentLineInfo.setProductName(accessoryProductName);																
					shipmentLineInfo.setBasePrice(BigDecimal.valueOf(accessoryItem.getPriceInfo().getAmount()));
					shipmentLineInfo.setLineTax1(BigDecimal.valueOf(taxAmount));
					shipmentLineInfosforAccessories[accessoryLineItemIndex] = shipmentLineInfo;	
					orderLinenumber++;		
					accessoryLineItemIndex++;
				}							
			} // end of for loop
				
			 if(shipmentLineInfosforAccessories.length >0){
				  subscriberInfo= subscriberInfoList[0];
				  ShipmentLineInfo[] shipmentLineInfoForPackage =  subscriberInfo.getShippingOrderLines();
				  ShipmentLineInfo[] allShipmentLineInfoList  = new ShipmentLineInfo[shipmentLineInfosforAccessories.length+shipmentLineInfoForPackage.length];
				  int index = 0;
				  //copying shipping orderline from package1
				  for(index = 0 ; index < shipmentLineInfoForPackage.length; index++){
					  allShipmentLineInfoList[index] = shipmentLineInfoForPackage[index];
				  }
				  //copying shipping order lines from accessories
				  for(int j=0; j< shipmentLineInfosforAccessories.length; j++){
					  allShipmentLineInfoList[index] = shipmentLineInfosforAccessories[j];
					  index++;
				  }
				  subscriberInfo.setShippingOrderLines(allShipmentLineInfoList);
				  subscriberInfoList[0] = subscriberInfo;
			 }
			 
		}
 					 
		// now setting subscription to account
		account.setSubscriber(subscriberInfoList);
 				
		CreateActivationQuoteRequestInfoAccount[] accounts = getCricketESPAdapterHelper().createCAQRequestInfoAccounts(shippingAddressData, emailInfo, account,order);			 
			
		CreateActivationQuoteRequestInfo requestInfo = new CreateActivationQuoteRequestInfo();
		requestInfo.setOrderInfo(orderInfo);
		requestInfo.setCustomer(customer);
		requestInfo.setAccount(accounts);			
			
		// invoking create activation quote
		CreateActivationQuoteResponseInfo activationQuoteResponseInfo = activationQuoteStub.createActivationQuote(requestInfo);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->createActivationQuote()]: activationQuoteResponseInfo " + activationQuoteResponseInfo);
		}		
		if(activationQuoteResponseInfo != null && activationQuoteResponseInfo.getResponse(INT_ZERO) != null &&
				ESP_RESPONSE_CODE_SUCCESS.equals(activationQuoteResponseInfo.getResponse(INT_ZERO).getCode())){
			activationQuoteResponseVO = getCricketESPAdapterHelper().createActivationQuoteResponse(activationQuoteResponseInfo);
		}

		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->createActivationQuote()]: Exiting createActivationQuote()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
		return activationQuoteResponseVO;
	}	
	
	/**
	 * This method returns all the information(accounttype, customertype, plans, add-ons etc) of the cricket registered user
	 * @param accountNumber
	 * @param pOrderId
	 * @param mask
	 * @return
	 * @throws CricketException
	 */
	public InquireAccountResponseVO inquireAccount(String accountNumber, String pOrderId) throws CricketException {
		InquireAccountResponseVO inquireAccountResponseVO = null;

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 									
	    		logDebug("[CricketESPAdapter->inquireAccount()]: Entering into inquireAccount()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		try{	
			String mask = getMask();
			InquireAccount inquireAccount = new CricketESPHTTPServicesLocator().getInquireAccount(new URL(getEspServiceUrl()+ESP_NAME_INQUIREACCOUNT));
			InquireAccountStub inquireAccountStub = (InquireAccountStub) inquireAccount;
			
			intializeHeader(inquireAccountStub,pOrderId);		
			
			GenericRecordLocatorInfo genericRecordLocator = new GenericRecordLocatorInfo();
			GenericRecordLocatorInfoAccountSelector accountSelector = new GenericRecordLocatorInfoAccountSelector();
			accountSelector.setBillingAccountNumber(accountNumber);
			genericRecordLocator.setAccountSelector(accountSelector);
			
			InquireAccountRequestInfo inquireAccountRequestInfo = new InquireAccountRequestInfo();
			inquireAccountRequestInfo.setGenericRecordLocator(genericRecordLocator);
			inquireAccountRequestInfo.setMask(mask);
						
			InquireAccountResponseInfo inquireAccountResponseInfo = inquireAccountStub.inquireAccount(inquireAccountRequestInfo);
			
			/* call this method to set conversation id for the first time from ESP response
			 * First time ESP call can happen from InquireCoverage or InquireFeatures or InquireAccount or ValidateAddress
			 * So this call is present in all the above 4 mentioned ESP calls
			 */	
			setConversationIdFromESPResponse(inquireAccountStub);
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->inquireAccount()]: response from inquireAccountResponseInfo: "+ inquireAccountResponseInfo);
			}
			
			inquireAccountResponseVO = getCricketESPAdapterHelper().inquireAccountResponse(inquireAccountResponseInfo);
				
			
		} catch (ServiceException se) {
			vlogError("[CricketESPAdapter->inquireAccount()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ServiceException while getting inquireAccount response:" + "ConversationId: " + getConversationId(), se);		
		}		
		catch (ESPApplicationException eae) {
			vlogError("[CricketESPAdapter->inquireAccount()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ESPApplicationException while getting inquireAccount response:"  + "ConversationId: " + getConversationId(), eae);
			getEspFaultCode(eae);		
		}
		catch (RemoteException re) {
			vlogError("[CricketESPAdapter->inquireAccount()]:" + CricketESPConstants.WHOOP_KEYWORD +  " RemoteException while getting inquireAccount response:"  + "ConversationId: " + getConversationId(), re);	
		}
		catch (Exception e) {
			vlogError("[CricketESPAdapter->inquireAccount()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Exception while getting inquireAccount response:"  + "ConversationId: " + getConversationId(), e);		
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->inquireAccount()]: Exiting inquireAccount()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderId );
		}
		return inquireAccountResponseVO;
	}	
	
	/**
	 * This method creates the billingorderid in the billing system
	 * @param pBillingQuoteRequest
	 * @param pOrderId 
	 * @return
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 */
	public UpdateBillingQuoteStatusResponseVO updateBillingQuoteStatus(CricketOrderImpl order,EspServiceResponseData espServiceResponseData) throws CricketException, ESPApplicationException, RemoteException, MalformedURLException, ServiceException{

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}								
	    		logDebug("[CricketESPAdapter->updateBillingQuoteStatus()]: Entering into updateBillingQuoteStatus()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		UpdateBillingQuoteStatusResponseInfo billingQuoteresponse = null;
		UpdateBillingQuoteStatusResponseVO billingQuoteStatusResponseVO=null;
		UpdateBillingQuoteStatus billingQuoteStatus = new CricketESPHTTPServicesLocator().getUpdateBillingQuoteStatus(new URL(getEspServiceUrl()+ESP_NAME_UPDATEBILLINGQUOTESTATUS));
		UpdateBillingQuoteStatusStub billingStatusStub = (UpdateBillingQuoteStatusStub) billingQuoteStatus;
		intializeHeader(billingStatusStub,order.getId());	
		UpdateBillingQuoteStatusRequestInfo billingQuoteStatusRequestInfo;		
		billingQuoteStatusRequestInfo = getCricketESPAdapterHelper().mapToBillingQuoteStatusRequest(order,espServiceResponseData); 
		
		billingQuoteresponse = billingStatusStub.updateBillingQuoteStatus(billingQuoteStatusRequestInfo);
		billingQuoteStatusResponseVO=   getCricketESPAdapterHelper().mapToBillingQuoteStatusResponse(billingQuoteresponse);		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->updateBillingQuoteStatus()]: Exiting updateBillingQuoteStatus()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
		return billingQuoteStatusResponseVO;		
	}
	
 
	/**
	 * @param pBillingQuoteRequest
	 * @param pOrderId 
	 * @return
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 */
	public UpdateBillingQuoteResponseVO updateBillingQuote(CricketOrderImpl order,EspServiceResponseData espServiceResponseData) throws CricketException, MalformedURLException, ServiceException, ESPApplicationException, RemoteException{

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}								
	    		logDebug("[CricketESPAdapter->updateBillingQuote()]: Entering into updateBillingQuote()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		UpdateBillingQuoteResponseVO billingQuoteResponseVO =null;
		UpdateBillingQuote billingQuote = new CricketESPHTTPServicesLocator().getUpdateBillingQuote(new URL(getEspServiceUrl()+ESP_NAME_UPDATEBILLINGQUOTE));
		UpdateBillingQuoteStub billingQuoteStub = (UpdateBillingQuoteStub) billingQuote;
		intializeHeader(billingQuoteStub,order.getId());	
		
		UpdateBillingQuoteRequestInfo billingQuoteRequestInfo = getCricketESPAdapterHelper().mapTOBillingQuoteRequest(order,espServiceResponseData);					 
		
		ResponseInfo[] responseInfo = billingQuoteStub.updateBillingQuote(billingQuoteRequestInfo);
	
		ESPResponseVO responseVO[] = getCricketESPAdapterHelper().mapToBillingQuoteResponse(responseInfo);
					
		billingQuoteResponseVO = new UpdateBillingQuoteResponseVO();
		billingQuoteResponseVO.setResponse(responseVO); 
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->updateBillingQuote()]: Exiting updateBillingQuote()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
		return billingQuoteResponseVO;
		
	}	
	
	/**
	 * @param pBillingOrderDetailsRequest
	 * @return
	 * @throws CricketException
	 */
	public InquireBillingOrderDetailsResponseVO getInquireBillingOrderDetails(InquireBillingOrderDetailsRequestVO pBillingOrderDetailsRequest) throws CricketException{
		logInfo("[CricketESPAdapter->getInquireBillingOrderDetails()]: Entering into getInquireBillingOrderDetails()...");
		InquireBillingOrderDetailsResponseVO responseVO=null;
		try {
			InquireBillingOrderDetails inquireBillingOrder = new CricketESPHTTPServicesLocator().getInquireBillingOrderDetails(new URL(getEspServiceUrl()+ESP_NAME_INQUIREBILLINGORDERDETAILS));
			InquireBillingOrderDetailsStub inquireBillingOrderStub = (InquireBillingOrderDetailsStub) inquireBillingOrder;
			intializeHeader(inquireBillingOrderStub,pBillingOrderDetailsRequest.getBillingOrderNumber());
			InquireBillingOrderDetailsRequestInfo requestInfo = mapTOInquireBillingOrderRequestVO(pBillingOrderDetailsRequest);
			
			InquireBillingOrderDetailsResponseInfo responseInfo = inquireBillingOrderStub.inquireBillingOrderDetails(requestInfo);
			responseVO = getCricketESPAdapterHelper().mapTOBillingOrderDetailsResponse(responseInfo);
			
		} catch (ServiceException e) {
			vlogError("[CricketESPAdapter->getInquireBillingOrderDetails()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ServiceException while getting getInquireBillingOrderDetails response:" + "ConversationId: " + getConversationId(), e);
		} catch (ESPApplicationException eae) {
			vlogError("[CricketESPAdapter->getInquireBillingOrderDetails()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ESPApplicationException while getting getInquireBillingOrderDetails response:" + "ConversationId: " + getConversationId(), eae);
			getEspFaultCode(eae);	
		} catch (RemoteException re) {
			vlogError("[CricketESPAdapter->getInquireBillingOrderDetails()]:" + CricketESPConstants.WHOOP_KEYWORD +  " RemoteException while getting getInquireBillingOrderDetails response:" + "ConversationId: " + getConversationId(), re);			 
		}catch (Exception e) {
			vlogError("[CricketESPAdapter->getInquireBillingOrderDetails()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Exception while getting getInquireBillingOrderDetails response:" + "ConversationId: " + getConversationId(), e);
			 
		}		
		logInfo("[CricketESPAdapter->getInquireBillingOrderDetails()]: Exiting getInquireBillingOrderDetails()...");
		return responseVO;	
	}

	
	/**
	 * @param pBillingOrderDetailsRequest
	 * @return
	 */
	private InquireBillingOrderDetailsRequestInfo mapTOInquireBillingOrderRequestVO(
			InquireBillingOrderDetailsRequestVO pBillingOrderDetailsRequest) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->mapTOInquireBillingOrderRequestVO()]: Entering into mapTOInquireBillingOrderRequestVO()...");
		}
		
		InquireBillingOrderDetailsRequestInfo requestInfo = new InquireBillingOrderDetailsRequestInfo();
		requestInfo.setBillingOrderNumber(pBillingOrderDetailsRequest.getBillingOrderNumber());
		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->mapTOInquireBillingOrderRequestVO()]: Exiting mapTOInquireBillingOrderRequestVO()...");
		}
		return requestInfo;
	}
	
	public UpdateSubscriberResponseVO updateSubscriber( CricketProfile cricketProfile,
 														MyCricketCookieLocationInfo locationInfo,
														CricketOrderImpl order,
														UserAccountInformation userAccountInformation,
														UserSessionBean userSessionBean) throws MalformedURLException, ServiceException, ESPApplicationException, RemoteException {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 					
	    		logDebug("[CricketESPAdapter->updateSubscriber()]: Entering into updateSubscriber()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		UpdateSubscriberResponseVO responseVO=null;
		UpdateSubscriber updateSubscriberService = new CricketESPHTTPServicesLocator().getUpdateSubscriber(new URL(getEspServiceUrl()+ESP_NAME_UPDATESUBSCRIBER));
		UpdateSubscriberStub updateSubscriberStub = (UpdateSubscriberStub) updateSubscriberService;
		intializeHeader(updateSubscriberStub,order.getId());
		
		UpdateSubscriberRequestInfo requestInfo = mapTOUpdateSubscriberRequestInfo(cricketProfile, locationInfo, order, userAccountInformation,userSessionBean);
		UpdateSubscriberResponseInfo resposneInfo = updateSubscriberStub.updateSubscriber(requestInfo);
		responseVO = getCricketESPAdapterHelper().mapTOUpdateSubscriberResponseVO(resposneInfo);
		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->updateSubscriber()]: Exiting updateSubscriber()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
		return responseVO;		
	}	

	/**
	 * @param pRequestVO
	 * @return
	 */
	private UpdateSubscriberRequestInfo mapTOUpdateSubscriberRequestInfo(
																			CricketProfile cricketProfile,
																			MyCricketCookieLocationInfo locationInfo,
																			CricketOrderImpl order, 
																			UserAccountInformation userAccountInformation, 
																			UserSessionBean userSessionBean) {

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}			 				
	    		logDebug("[CricketESPAdapter->mapTOUpdateSubscriberRequestInfo()]: Entering into mapTOUpdateSubscriberRequestInfo()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		Profile profileItem = getProfileServices().getCurrentProfile();
		
		UpdateSubscriberRequestInfo requestInfo = new UpdateSubscriberRequestInfo();
		
		GenericRecordLocatorInfo recordLocatorInfo = new GenericRecordLocatorInfo();
		GenericRecordLocatorInfoSubscriberSelector subscriber = new GenericRecordLocatorInfoSubscriberSelector();
		GenericRecordLocatorInfoSubscriberSelectorBillingAccountInformation billingInfo = new GenericRecordLocatorInfoSubscriberSelectorBillingAccountInformation();
		
		subscriber.setMdn(cricketProfile.getMdn());
		billingInfo.setBillingAccountNumber(cricketProfile.getAccountNumber());
				
		AccountTypeIndicatorInfo accountType = AccountTypeIndicatorInfo.fromValue(cricketProfile.getAccountType());
		billingInfo.setAccountType(accountType);
		
		subscriber.setBillingAccountInformation(billingInfo);
		recordLocatorInfo.setSubscriberSelector(subscriber);
		GenericRecordLocatorInfoSubscriberSelectorCustomerInformation customerInformation = new GenericRecordLocatorInfoSubscriberSelectorCustomerInformation();
		customerInformation.setCustomerId(cricketProfile.getCustomerId());
 		
		subscriber.setCustomerInformation(customerInformation);
		requestInfo.setGenericRecordLocator(recordLocatorInfo);
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setMdn(cricketProfile.getMdn());
		// get the below fields from inquireAccount 
		MobileStatusInfo mobileStatus = MobileStatusInfo.fromValue(getSubscriberStatus());
		subscriberInfo.setSubscriberStatus(mobileStatus);
		// get it from order **
		subscriberInfo.setTransactionType(order.getWorkOrderType());
		EffectiveDatesInfo effectiveDate = new EffectiveDatesInfo();
		effectiveDate.setEffectiveDate(getCricketESPAdapterHelper().getPacificDateTime(new Date()));
		subscriberInfo.setEffectiveDate(effectiveDate);
		
		ContractTermInfo contractInfo = new ContractTermInfo();
		contractInfo.setTerm(new BigInteger(String.valueOf(getCricketESPAdapterHelper().getContractTerm())));		
		contractInfo.setCommission(getCricketESPAdapterHelper().getDealerCommission());
		subscriberInfo.setContract(contractInfo);
		
		PricePlanInfo pricePlan = new PricePlanInfo();
 		// need to get it from order
		List<CricketCommerceItemImpl> commerceItemList = order.getCommerceItems();
		  
		RepositoryItem product=null;
		String productType=null;		
		List<String> oldCricketOfferingCodes = null;
		String[] oldCricOfferingCodesArray = null; 		
		String ratePlanId = null;
		List<String> chargableAddons = new ArrayList<String>();	 
	 	List<String> parcMandatoryOfferCodes = new ArrayList<String>();
	 	
	 	Map<String,Boolean> compabibilityAddonsMap =  null;
 		String[] newCricketOfferingCodes = null;
 		Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersMap = null;
		List<InquireFeaturesResponseVO> mandatoryOffersList = null;
		String parcPlanId = null;
		boolean isChangeFeature = true;
		List<String> existingCompatibleAddonList = new ArrayList<String>();
		Map<String, List<InquireFeaturesResponseVO>> planAllAddOnsMap = null;
		String planCode = null;		
		
		if(commerceItemList != null){			
			//get parc_plan_id from repository where product_id=cricketprofile.getRatePlanCode()
			parcPlanId = getCatalogManager().getParcPlanId(cricketProfile.getRatePlanCode());
			if(isLoggingDebug()){
	 			logDebug("[CricketESPAdapter->mapTOUpdateSubscriberRequestInfo()]: parcPlanId - "+parcPlanId);
	 		}
			for(CricketCommerceItemImpl commerceItem:commerceItemList)
			{				
				productType = commerceItem.getCricItemTypes();
				if ((null!=productType &&  CHANGE_PLAN.equalsIgnoreCase(productType))){
					product = (RepositoryItem)commerceItem.getAuxiliaryData().getProductRef();
 					// please check the condition -is 
					ratePlanId =   (String)product.getPropertyValue(PROPERTY_PARC_PLAN_ID);
					if(isLoggingDebug()){
			 			logDebug("[CricketESPAdapter->mapTOUpdateSubscriberRequestInfo()]: ratePlanId - "+ratePlanId);
			 		}
					pricePlan.setPlanCode(ratePlanId); 
					isChangeFeature = false;
					
					if(parcPlanId != null){
						if(parcPlanId.contains(MINUS)){
							pricePlan.setOldPlanCode(parcPlanId.substring(1, parcPlanId.length()));
						}else{
							pricePlan.setOldPlanCode(parcPlanId);
						}
					}
					
					planCode = product.getRepositoryId();					
					
					PricePlanStatusInfo pricePlanStatus = PricePlanStatusInfo.fromValue(getPricePlanStatus());
					pricePlan.setPricePlanStatus(pricePlanStatus);
					
					EffectiveDatesInfo pricePlanDate = new EffectiveDatesInfo();
					pricePlanDate.setEffectiveDate(getCricketESPAdapterHelper().getPacificDateTime(new Date()));
					pricePlan.setEffectiveDate(pricePlanDate);					
					PricePlanDetailInfo pricePlanDetailInfo = new PricePlanDetailInfo();
					pricePlan.setPrimaryPricePlan(pricePlanDetailInfo);
					subscriberInfo.setPricePlan(pricePlan);
					
					
				} else if(null!=productType && (CHANGE_ADD_ON.equalsIgnoreCase(productType) || ADD_ON_PRODUCT.equalsIgnoreCase(productType))){					
						RepositoryItem addonProductItem = (RepositoryItem)commerceItem.getAuxiliaryData().getProductRef();
						if(addonProductItem.getPropertyValue(PROPERTY_OFFER_ID) != null){
							chargableAddons.add((String)addonProductItem.getPropertyValue(PROPERTY_OFFER_ID));
						}								
					}
			}
			
			// checking removableCompatibility addons
			compabibilityAddonsMap =  order.getRemovedAddonCompatibility();			
			String addOnOfferId = null;
			if(compabibilityAddonsMap != null && compabibilityAddonsMap.size() > 0){
				for (Map.Entry<String, Boolean> entry : compabibilityAddonsMap.entrySet()) {
				 	if((Boolean)entry.getValue()){
				 		if(isLoggingDebug()){
				 			logDebug("[CricketESPAdapter->mapTOUpdateSubscriberRequestInfo()]: productIds from compabibilityAddonsMap - "+entry.getKey());
				 		}
						addOnOfferId = getCatalogManager().getAddonOfferId((String)entry.getKey());
						existingCompatibleAddonList.add(addOnOfferId);
					}
				}
			}			
			
			//getting Mandatory, Adminstration fee Addons
			if(isChangeFeature){							
				planCode = cricketProfile.getRatePlanCode();		
				
			}
			
			if(null != userSessionBean){ 
				mandatoryOffersMap = userSessionBean.getMandatoryOffersMap();
				if(null !=mandatoryOffersMap && planCode != null){
					mandatoryOffersList = mandatoryOffersMap.get(planCode);
				}
			} 
			
			if(mandatoryOffersMap == null || mandatoryOffersList == null || mandatoryOffersList.size() == 0){											 
				planAllAddOnsMap = getDisplayFeaturesManager().getCompatibleAddons(cricketProfile.getDeviceModel(),
																					PHONE_TYPE_VOICE, 
																					planCode,
																					cricketProfile.getMarketCode(),
																					TRANSACTION_TYPE_RRC, cricketProfile.getUserPurchasedOfferingProducts(),
																					false,
																					true,
																					getSalesChannel(),order.getId());
			
				if(null!= planAllAddOnsMap){
					mandatoryOffersList = planAllAddOnsMap.get(CricketCommonConstants.MANDATORY_ADDONS);
					
					List<InquireFeaturesResponseVO> optionalAddOns = planAllAddOnsMap.get(CricketCommonConstants.OPTIONAL_ADDONS);
					
					InquireFeaturesResponseVO activationFeeFeature = getCricketESPAdapterHelper().getActivationFeeAddOn(optionalAddOns);
					if(activationFeeFeature != null && activationFeeFeature.getPrice() != null) {
			    		profileItem.setPropertyValue(CricketCommonConstants.ACTIVATION_FEE, activationFeeFeature.getPrice().doubleValue());
			    	}
					
					InquireFeaturesResponseVO administrationFeeFeature = getCricketESPAdapterHelper().getAdministrationFeeAddOn(optionalAddOns);
					if(mandatoryOffersList != null && administrationFeeFeature != null){
						//Administration Fee addOn needs to be sent to billing system for charging the user on monthly basis
		    			//Hence adding to mandatoryAddOns list which will be used in CreateActivationQuote request for building CricketOfferingCodes
						mandatoryOffersList.add(administrationFeeFeature);
					}
				}			
				
			} //end
			
			if(mandatoryOffersList != null && mandatoryOffersList.size() > 0){							
				for(InquireFeaturesResponseVO mandatoryOfferVO : mandatoryOffersList){
					if(isLoggingDebug()){
						logDebug("[CricketESPAdapter->mapTOUpdateSubscriberRequestInfo()]: mandatoryOffersList - FeatureCodes:- "+mandatoryOfferVO.getId()+" - Type"+mandatoryOfferVO.getType());
					}
					parcMandatoryOfferCodes.add(mandatoryOfferVO.getId());
				}
			}			
				
			int offeringCodesindex = 0;
			int parcOfferCodesSize = parcMandatoryOfferCodes.size();
			int compatibleAddonsSize = existingCompatibleAddonList.size();
			int payAsYouGoOfferIdSize = 0; 
			
			//fix for QC 8166
			boolean payAsYouGoOfferOn = getCricketESPAdapterHelper().isDataAutoMetered(userAccountInformation, cricketProfile.getMdn());
			if(payAsYouGoOfferOn){
				payAsYouGoOfferIdSize = 1; 
			}
			
			if(null!=chargableAddons && chargableAddons.size()>0){
				int chargeOfferCodesSize = chargableAddons.size();
				newCricketOfferingCodes = new String[parcOfferCodesSize + compatibleAddonsSize + chargeOfferCodesSize + payAsYouGoOfferIdSize + 1];
				for(int addOnIndex=0; addOnIndex < chargeOfferCodesSize; addOnIndex++){
					newCricketOfferingCodes[offeringCodesindex] = chargableAddons.get(addOnIndex);
					offeringCodesindex++;
				}
			}else{
				newCricketOfferingCodes = new String[parcOfferCodesSize + compatibleAddonsSize + payAsYouGoOfferIdSize + 1];
			}
			
			if(isLoggingDebug()){
	 			logDebug("[CricketESPAdapter->mapTOUpdateSubscriberRequestInfo()]: isChangeFeature - "+ isChangeFeature);
	 		}
			//setting rateplan id
			if(isChangeFeature){
				newCricketOfferingCodes[offeringCodesindex] = parcPlanId;
			}else{
				newCricketOfferingCodes[offeringCodesindex] = ratePlanId;
			}
			
			for(int addOnIndex=0; addOnIndex < parcOfferCodesSize; addOnIndex++){
				offeringCodesindex++;
				newCricketOfferingCodes[offeringCodesindex] = parcMandatoryOfferCodes.get(addOnIndex);
			}
			
			for(int compatibleAddOnIndex=0; compatibleAddOnIndex < compatibleAddonsSize; compatibleAddOnIndex++){
				offeringCodesindex++;
				newCricketOfferingCodes[offeringCodesindex] = existingCompatibleAddonList.get(compatibleAddOnIndex);
			}
			//fix for QC 8166
			if(payAsYouGoOfferOn){
				offeringCodesindex++;
				newCricketOfferingCodes[offeringCodesindex] = PAY_AS_YOU_GO_OFFER_ID;
			}
			
			//adding oldoffering codes
			oldCricketOfferingCodes = userAccountInformation.getOfferingCodesPerMdn(subscriber.getMdn());
			
			if(oldCricketOfferingCodes != null && oldCricketOfferingCodes.size() > 0){
				oldCricOfferingCodesArray = new String[oldCricketOfferingCodes.size()];					
				for(int i=0; i <oldCricketOfferingCodes.size(); i++){   
					oldCricOfferingCodesArray[i] = oldCricketOfferingCodes.get(i);
				}
				subscriberInfo.setOldCricketOfferingCodes(oldCricOfferingCodesArray); 						
			}
			
			subscriberInfo.setNewCricketOfferingCodes(newCricketOfferingCodes );
			requestInfo.setSubscriber(subscriberInfo); 
		}
		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->mapTOUpdateSubscriberRequestInfo()]: Exiting mapTOUpdateSubscriberRequestInfo()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
		return requestInfo;
	}	
	
	/**
	 * @param shippingAddressData
	 * @param accountHolderAddressData
	 * @param cricketProfile
	 * @param profile
	 * @param locationInfo
	 * @param order
	 * @return
	 * @throws CricketException
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 */
	public CreateShippingQuoteResponseVO createShippingQuote(CricketShippingAddressData shippingAddressData,
															CricketAccountHolderAddressData accountHolderAddressData,
															CricketProfile cricketProfile,
															RepositoryItem profile, 
															MyCricketCookieLocationInfo locationInfo,
															CricketOrderImpl order) throws CricketException, MalformedURLException, ServiceException, ESPApplicationException, RemoteException{

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}								
	    		logDebug("[CricketESPAdapter->createShippingQuote()]: Entering into createShippingQuote()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		CreateShippingQuoteResponseVO shippingQuoteResponse= null;
		CreateShippingQuote shippingQuoteService = new CricketESPHTTPServicesLocator().getCreateShippingQuote(new URL(getEspServiceUrl()+ESP_NAME_CREATESHIPPINGQUOTE));
		
		CreateShippingQuoteStub shippingQuoteStub = (CreateShippingQuoteStub) shippingQuoteService;
		intializeHeader(shippingQuoteStub,order.getId());
		
		
		CreateShippingQuoteRequestInfo requestInfo = getCricketESPAdapterHelper().mapTOShippingQuoteRequest(	 shippingAddressData,
																				 accountHolderAddressData,
																				 cricketProfile,
																				 profile, 
																				 locationInfo,
																				 order);
		CreateShippingQuoteResponseInfo responseInfo = shippingQuoteStub.createShippingQuote(requestInfo);
		shippingQuoteResponse = getCricketESPAdapterHelper().mapTOCreateShippingResponseVO(responseInfo);
	 
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->createShippingQuote()]: Exiting createShippingQuote()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
		}
		return shippingQuoteResponse;
	}
	

	
	/**
	 * @param webReportRequest
	 * @param pOrderId 
	 * @return
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 */
	public ESPResponseVO[] updateWebReport(UpdateWebReportRequestVO webReportRequest, CricketOrderImpl pOrder) throws CricketException, ESPApplicationException, RemoteException, MalformedURLException, ServiceException{

		if (isLoggingDebug()) {
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}								
	    		logDebug("[CricketESPAdapter->updateWebReport()]: Entering into updateWebReport()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		ESPResponseVO[] updateWebReportResponse= null;
 			
		UpdateWebReport updateWebReportService = new CricketESPHTTPServicesLocator().getUpdateWebReport(new URL(getEspServiceUrl()+ESP_NAME_UPDATEWEBREPORT));
		
		UpdateWebReportStub updateWebReportStub = (UpdateWebReportStub) updateWebReportService;
		intializeHeader(updateWebReportStub,pOrder.getId());		
		UpdateWebReportRequestInfo requestInfo = getCricketESPAdapterHelper().mapToUpdateWebReportRequest(webReportRequest,pOrder);
		
		ResponseInfo[] responseInfo = updateWebReportStub.updateWebReport(requestInfo);
		
		updateWebReportResponse = getCricketESPAdapterHelper().mapTOUpdateWebReportResponse(responseInfo);
		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->updateWebReport()]: Exiting updateWebReport()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrder.getId() );
		}
		return updateWebReportResponse;
	}		
	
	
	 /**
	 * @return
	 */
	public ResourceBundle getResourceBundle() {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->getResourceBundle()]: Entering into getResourceBundle()...");
		}
	    if (this.mResourceBundle == null) {
	        this.mResourceBundle = LayeredResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, ServletUtil.getUserLocale());
	    }
	    if(isLoggingDebug()){
	    	logDebug("[CricketESPAdapter->getResourceBundle()]: Exiting getResourceBundle()...");
	    }
	    return this.mResourceBundle;
	 }	

	/**
	 * @return
	 */
	public String getShipFromAddressZipCode() {
		return shipFromAddressZipCode;
	}

	public void setShipFromAddressZipCode(String shipFromAddressZipCode) {
		this.shipFromAddressZipCode = shipFromAddressZipCode;
	}
	
	/**
	 * @return the conversationId
	 */
	public String getConversationId() {
		return conversationId;
	}

	/**
	 * @param conversationId the conversationId to set
	 */
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	/**
	 * @return
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * @return the isManualOrderApprovalNeeded
	 */
	public boolean isManualOrderApprovalNeeded() {
		return isManualOrderApprovalNeeded;
	}

	/**
	 * @param isManualOrderApprovalNeeded the isManualOrderApprovalNeeded to set
	 */
	public void setManualOrderApprovalNeeded(boolean isManualOrderApprovalNeeded) {
		this.isManualOrderApprovalNeeded = isManualOrderApprovalNeeded;
	}

	/**
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public String getTimeToLive() {
		return timeToLive;
	}

	/**
	 * @param timeToLive
	 */
	public void setTimeToLive(String timeToLive) {
		this.timeToLive = timeToLive;
	}
	

	/**
	 * @return
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}		

	/**
	 * @return
	 */
	public String getVersionId() {
		return versionId;
	}

	/**
	 * @param versionId
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	/**
	 * @return
	 */
	public String getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 */
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return
	 */
	public String getTotalInSequence() {
		return totalInSequence;
	}

	/**
	 * @param totalInSequence
	 */
	public void setTotalInSequence(String totalInSequence) {
		this.totalInSequence = totalInSequence;
	}	

	/**
	 * @return the drawerId
	 */
	public String getDrawerId() {
		return drawerId;
	}

	/**
	 * @param drawerId the drawerId to set
	 */
	public void setDrawerId(String drawerId) {
		this.drawerId = drawerId;
	}

	/**
	 * @return the salesTransactionType
	 */
	public String getSalesTransactionType() {
		return salesTransactionType;
	}

	/**
	 * @param salesTransactionType the salesTransactionType to set
	 */
	public void setSalesTransactionType(String salesTransactionType) {
		this.salesTransactionType = salesTransactionType;
	}
	

	/**
	 * @return the rateCenterId
	 */
	public String getRateCenterId() {
		return rateCenterId;
	}

	/**
	 * @param rateCenterId the rateCenterId to set
	 */
	public void setRateCenterId(String rateCenterId) {
		this.rateCenterId = rateCenterId;
	}

	/**
	 * @return the jointVentureCode
	 */
	public String getJointVentureCode() {
		return jointVentureCode;
	}

	/**
	 * @param jointVentureCode the jointVentureCode to set
	 */
	public void setJointVentureCode(String jointVentureCode) {
		this.jointVentureCode = jointVentureCode;
	}

	/**
	 * @return the solicitationContactPreference
	 */
	public String getSolicitationContactPreference() {
		return solicitationContactPreference;
	}

	/**
	 * @param solicitationContactPreference the solicitationContactPreference to set
	 */
	public void setSolicitationContactPreference(
			String solicitationContactPreference) {
		this.solicitationContactPreference = solicitationContactPreference;
	}

	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}

	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	/**
	 * @return the shippingOrderType
	 */
	public String getShippingOrderType() {
		return shippingOrderType;
	}

	/**
	 * @param shippingOrderType the shippingOrderType to set
	 */
	public void setShippingOrderType(String shippingOrderType) {
		this.shippingOrderType = shippingOrderType;
	}

	
	/**
	 * @return the isBillingResponsibility
	 */
	public boolean isBillingResponsibility() {
		return isBillingResponsibility;
	}

	/**
	 * @param isBillingResponsibility the isBillingResponsibility to set
	 */
	public void setBillingResponsibility(boolean isBillingResponsibility) {
		this.isBillingResponsibility = isBillingResponsibility;
	}	

	/**
	 * @return the sprintCsa
	 */
	public String getSprintCsa() {
		return sprintCsa;
	}

	/**
	 * @param sprintCsa the sprintCsa to set
	 */
	public void setSprintCsa(String sprintCsa) {
		this.sprintCsa = sprintCsa;
	}		
		
	/**
	 * @param country
	 * @return
	 */
	public String getCountry(String country){
		return 	country == null ? COUNTRY_USA_CODE_THREE_LETTER : (COUNTRY_USA_CODE_TWO_LETTER.equals(country) ? COUNTRY_USA_CODE_THREE_LETTER : country);
	}

	/**
	 * @return the mask
	 */
	public String getMask() {
		return mask;
	}

	/**
	 * @param mask the mask to set
	 */
	public void setMask(String mask) {
		this.mask = mask;
	}

	/**
	 * @return the lineTax1
	 */
	public int getLineTax1() {
		return lineTax1;
	}

	/**
	 * @param lineTax1 the lineTax1 to set
	 */
	public void setLineTax1(int lineTax1) {
		this.lineTax1 = lineTax1;
	}

	/**
	 * @return the orderShipmentTaxAmount
	 */
	public int getOrderShipmentTaxAmount() {
		return orderShipmentTaxAmount;
	}

	/**
	 * @param orderShipmentTaxAmount the orderShipmentTaxAmount to set
	 */
	public void setOrderShipmentTaxAmount(int orderShipmentTaxAmount) {
		this.orderShipmentTaxAmount = orderShipmentTaxAmount;
	}
	 
	/**
	 * @return the barCodeServicePayment
	 */
	public String getBarCodeServicePayment() {
		return barCodeServicePayment;
	}
	/**
	 * @param barCodeServicePayment the barCodeServicePayment to set
	 */
	public void setBarCodeServicePayment(String barCodeServicePayment) {
		this.barCodeServicePayment = barCodeServicePayment;
	}
	/**
	 * @return the barCodeShipping
	 */
	public String getBarCodeShipping() {
		return barCodeShipping;
	}
	/**
	 * @param barCodeShipping the barCodeShipping to set
	 */
	public void setBarCodeShipping(String barCodeShipping) {
		this.barCodeShipping = barCodeShipping;
	}
	/**
	 * @return the settleForLessThanAuthorized
	 */
	public boolean isSettleForLessThanAuthorized() {
		return settleForLessThanAuthorized;
	}

	/**
	 * @param settleForLessThanAuthorized the settleForLessThanAuthorized to set
	 */
	public void setSettleForLessThanAuthorized(boolean settleForLessThanAuthorized) {
		this.settleForLessThanAuthorized = settleForLessThanAuthorized;
	}

	/**
	 * @return the allowPendingPayment
	 */
	public boolean isAllowPendingPayment() {
		return allowPendingPayment;
	}

	/**
	 * @param allowPendingPayment the allowPendingPayment to set
	 */
	public void setAllowPendingPayment(boolean allowPendingPayment) {
		this.allowPendingPayment = allowPendingPayment;
	}	

	/**
	 * @return the channelCode
	 */
	public String getChannelCode() {
		return channelCode;
	}

	/**
	 * @param channelCode the channelCode to set
	 */
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	/**
	 * @return the orderAction
	 */
	public String getOrderAction() {
		return orderAction;
	}

	/**
	 * @param orderAction the orderAction to set
	 */
	public void setOrderAction(String orderAction) {
		this.orderAction = orderAction;
	}
	
	/**
	 * @return the equimentOrderAction
	 */
	public String getEquimentOrderAction() {
		return equimentOrderAction;
	}
	
	/**
	 * @param equimentOrderAction the equimentOrderAction to set
	 */
	public void setEquimentOrderAction(String equimentOrderAction) {
		this.equimentOrderAction = equimentOrderAction;
	}
	/**
	 * @return the customerType
	 */
	public String getCustomerType() {
		return customerType;
	}
	/**
	 * @param customerType the customerType to set
	 */
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	/**
	 * @return the shipVia
	 */
	public String getShipVia() {
		return shipVia;
	}
	/**
	 * @param shipVia the shipVia to set
	 */
	public void setShipVia(String shipVia) {
		this.shipVia = shipVia;
	}
	
	/**
	 * @return the subscriberStatus
	 */
	public String getSubscriberStatus() {
		return subscriberStatus;
	}
	/**
	 * @param subscriberStatus the subscriberStatus to set
	 */
	public void setSubscriberStatus(String subscriberStatus) {
		this.subscriberStatus = subscriberStatus;
	}
	/**
	 * @return the pricePlanStatus
	 */
	public String getPricePlanStatus() {
		return pricePlanStatus;
	}
	/**
	 * @param pricePlanStatus the pricePlanStatus to set
	 */
	public void setPricePlanStatus(String pricePlanStatus) {
		this.pricePlanStatus = pricePlanStatus;
	}
	/**
	 * @return the socialSecurityNumber
	 */
	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}
	/**
	 * @param socialSecurityNumber the socialSecurityNumber to set
	 */
	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}
	
	/**
	 * @return the espTimeOutInMilliSeconds
	 */
	public int getEspTimeOutInMilliSeconds() {
		return espTimeOutInMilliSeconds;
	}
	/**
	 * @param espTimeOutInMilliSeconds the espTimeOutInMilliSeconds to set
	 */
	public void setEspTimeOutInMilliSeconds(int espTimeOutInMilliSeconds) {
		this.espTimeOutInMilliSeconds = espTimeOutInMilliSeconds;
	}
	
	 /** cricketOrderTools */
    private CricketOrderTools cricketOrderTools;
    
	/**
	 * @return the cricketOrderTools
	 */
	public CricketOrderTools getCricketOrderTools() {
		return cricketOrderTools;
	}
	/**
	 * @param cricketOrderTools the cricketOrderTools to set
	 */
	public void setCricketOrderTools(CricketOrderTools cricketOrderTools) {
		this.cricketOrderTools = cricketOrderTools;
	}
	/**
	 * @return the espServiceUrl
	 */
	public String getEspServiceUrl() {
		return espServiceUrl;
	}
	
	/**
	 * @param espServiceUrl the espServiceUrl to set
	 */
	public void setEspServiceUrl(String espServiceUrl) {
		this.espServiceUrl = espServiceUrl;
	}
	
	/**
	 * @return the displayFeaturesManager
	 */
	public DisplayFeaturesManager getDisplayFeaturesManager() {
		return displayFeaturesManager;
	}

	/**
	 * @param displayFeaturesManager the displayFeaturesManager to set
	 */
	public void setDisplayFeaturesManager(
			DisplayFeaturesManager displayFeaturesManager) {
		this.displayFeaturesManager = displayFeaturesManager;
	}
	/**
	 * @param stub
	 */
	private void setConversationIdFromESPResponse(Stub stub){
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->setConversationIdFromESPResponse()]: Entering into setConversationIdFromESPResponse()...");
		}
		if(getConversationId() == null){
			if(stub.getResponseHeaders() != null && stub.getResponseHeaders()[0] != null){
				List<MessageElement> messageElementList = stub.getResponseHeaders()[0].getChildren();
				if(messageElementList != null && messageElementList.size() > 0){
					try {
						String messageElementHeaderAsString = messageElementList.get(0).getAsString();						 
					      Matcher matcher = Pattern.compile(CONVERSATIONID_REGEX).matcher(messageElementHeaderAsString);
					      if(matcher.find()){
					    	  String convId = matcher.group(1);
					    	  setConversationId(convId);
					    	  if(isLoggingDebug()){
					    		  logDebug("[CricketESPAdapter->setConversationIdFromESPResponse()]: getting conversationId from "+ stub+" response "+ convId);
					    	  }
					      }
					} catch (Exception e) {
						vlogError("[CricketESPAdapter->setConversationIdFromESPResponse()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Error while getting conversationId from "+ stub+" response "  + "ConversationId: " + getConversationId(), e);
						logError(e);
					}
				}
			}else{
				logError("[CricketESPAdapter->setConversationIdFromESPResponse()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ResponseHeader is null"  + "ConversationId: " + getConversationId());
			}
		}	
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->setConversationIdFromESPResponse()]: Exiting setConversationIdFromESPResponse()...");
		}
	}	

	/** this method will be used to capture the error code in the ESP response.
	 * @param ESPApplicationException
	 */
	public String getEspFaultCode(ESPApplicationException eae) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->getEspFaultCode()]: Entering into getEspFaultCode()...");
		}
		ServiceEntityFaultInfo[] serviceEntityFaultInfo = eae.getServiceEntityFault();
		if(serviceEntityFaultInfo != null && serviceEntityFaultInfo.length > 0){
			ServiceEntityFaultInfo faultInfo = serviceEntityFaultInfo[0];
			logError("[CricketESPAdapter->getEspFaultCode()]:" + CricketESPConstants.WHOOP_KEYWORD +  " FaultDescription: "+faultInfo.getFaultDescription()  + "ConversationId: " + getConversationId());
			logError("[CricketESPAdapter->getEspFaultCode()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ReportingServiceEntity "+faultInfo.getReportingServiceEntity()  + "ConversationId: " + getConversationId());
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->getEspFaultCode()]: Exiting getEspFaultCode()...");
			}
				if(isErrorDiscription()){
					return 	faultInfo.getFaultDescription(); //getFaultCode();
				}else {
					return 	faultInfo.getFaultCode();
				}
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->getEspFaultCode()]: Exiting getEspFaultCode()...");
		}
		return 	null;
	}

	/**
	 * @return the catalogManager
	 */
	public CatalogManager getCatalogManager() {
		return catalogManager;
	}

	/**
	 * @param catalogManager the catalogManager to set
	 */
	public void setCatalogManager(CatalogManager catalogManager) {
		this.catalogManager = catalogManager;
	}
	
	public CricketESPAdapterHelper getCricketESPAdapterHelper() {
		return cricketESPAdapterHelper;
	}

	public void setCricketESPAdapterHelper(
			CricketESPAdapterHelper cricketESPAdapterHelper) {
		this.cricketESPAdapterHelper = cricketESPAdapterHelper;
	}

	public String getSalesRepresentative() {
		return salesRepresentative;
	}

	public void setSalesRepresentative(String salesRepresentative) {
		this.salesRepresentative = salesRepresentative;
	}

	/**
	 * @return the retryTimes
	 */
	public int getRetryTimes() {
		return retryTimes;
	}

	/**
	 * @param retryTimes the retryTimes to set
	 */
	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	/**
	 * @return the retryWait
	 */
	public int getRetryWait() {
		return retryWait;
	}

	/**
	 * @param retryWait the retryWait to set
	 */
	public void setRetryWait(int retryWait) {
		this.retryWait = retryWait;
	}

	/**
	 * @return the retryEnabled
	 */
	public boolean isRetryEnabled() {
		return retryEnabled;
	}

	/**
	 * @param retryEnabled the retryEnabled to set
	 */
	public void setRetryEnabled(boolean retryEnabled) {
		this.retryEnabled = retryEnabled;
	}
	
	/**
	 * @return the loggingSOAPRequestResponse
	 */
	public boolean isLoggingSOAPRequestResponse() {
		return loggingSOAPRequestResponse;
	}

	/**
	 * @param loggingSOAPRequestResponse the loggingSOAPRequestResponse to set
	 */
	public void setLoggingSOAPRequestResponse(boolean loggingSOAPRequestResponse) {
		this.loggingSOAPRequestResponse = loggingSOAPRequestResponse;
	}
	
	/**
	 * @return the profileServices
	 */
	public ProfileServices getProfileServices() {
		return profileServices;
	}

	/**
	 * @param profileServices the profileServices to set
	 */
	public void setProfileServices(ProfileServices profileServices) {
		this.profileServices = profileServices;
	}
	/**
	 * @return the sendMessageESPEnabled
	 */
	public boolean isSendMessageESPEnabled() {
		return sendMessageESPEnabled;
	}

	/**
	 * @param sendMessageESPEnabled the sendMessageESPEnabled to set
	 */
	public void setSendMessageESPEnabled(boolean sendMessageESPEnabled) {
		this.sendMessageESPEnabled = sendMessageESPEnabled;
	}
	
	/**
	 * retries the ESP calls x no of times where x is configuration parameter(retryTimes)
	 * @param pParam
	 * @param pResult
	 * @param cricketOrder
	 * @param pipelineProcessor
	 * @throws InvalidParameterException
	 */
	public int retry(Object pParam, PipelineResult pResult, CricketOrderImpl cricketOrder, PipelineProcessor pipelineProcessor)
			throws InvalidParameterException {
		
		boolean isRetryEnabled = isRetryEnabled();
		int returnCode = -99;
		if(isRetryEnabled){
			
			if (isLoggingDebug()) {
				// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}									
		    		logDebug("[CricketESPAdapter->retry()]: Entering into retry() the ESP calls x no. of times..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
			}
			int configuredRetryCount = getRetryTimes();
			int configuredWaitTimeInMillis = getRetryWait();	
			
			String pipelineProcessorName = pipelineProcessor.getClass().getName();
			if(pipelineProcessorName != null && pipelineProcessorName.lastIndexOf(".") > -1){
				pipelineProcessorName = pipelineProcessorName.substring(pipelineProcessorName.lastIndexOf(".")+1, pipelineProcessorName.length());
			}
			logInfo("["+pipelineProcessorName +"->retry()]: ************************** inside retry : ");
			
			synchronized (pipelineProcessor) {
				if(isLoggingDebug()){
					logDebug("["+pipelineProcessorName +"->retry()]: Thread executing retry...Name: "+ Thread.currentThread().getName()+" Id: "+Thread.currentThread().getId());													
				}
				HashMap paramMap = (HashMap) pParam;			
								
				int currentRetryCount = (paramMap.get(CURRENT_RETRY_COUNT) != null)  ? ((Integer)paramMap.get(CURRENT_RETRY_COUNT)) : 0;
				boolean isMaxRetryCountReached = (paramMap.get(IS_MAX_RETRY_COUNT_REACHED) != null) ? ((Boolean)paramMap.get(IS_MAX_RETRY_COUNT_REACHED)) : false;			
							
				
				if(isLoggingDebug()){
					logDebug("["+pipelineProcessorName +"->retry()]: currentRetryCount: "+ currentRetryCount + " configuredRetryCount: "+ configuredRetryCount+ " configuredWaitTimeInMillis: "+ configuredWaitTimeInMillis + " isRetryEnabled "+ isRetryEnabled);								
				}
				
							
					while(currentRetryCount < configuredRetryCount){
						try {
							if(isLoggingDebug()){
								logDebug("["+pipelineProcessorName +"->retry()]: set isMaxRetryCountReached to false, increment currentRetryCount going to sleep for "+ (configuredWaitTimeInMillis/1000) + " seconds");													
							}
							
							isMaxRetryCountReached = false;
							paramMap.put(IS_MAX_RETRY_COUNT_REACHED, isMaxRetryCountReached);
							currentRetryCount++;
							paramMap.put(CURRENT_RETRY_COUNT, currentRetryCount);
							
							Thread.sleep(configuredWaitTimeInMillis);						
							
							//invoke the run process again
							try {
								logInfo("["+pipelineProcessorName +"->retry()]: ************************** invoking "+pipelineProcessorName +"->runProcess() again for orderId :"+ cricketOrder.getId() +", retryCount: "+ currentRetryCount);
								returnCode = pipelineProcessor.runProcess(pParam, pResult);
								if(returnCode > 0){
									isMaxRetryCountReached = true;
									paramMap.put(IS_MAX_RETRY_COUNT_REACHED, isMaxRetryCountReached);
									currentRetryCount = 0;
									paramMap.put(CURRENT_RETRY_COUNT, currentRetryCount);
									return returnCode;
								}
							} catch (Exception e) {
								if(isLoggingDebug()){
									logDebug("["+pipelineProcessorName +"->retry()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Exception while invoking pipelineProcessor.runProcess");
								}
								logError(e);
							}
							if((Boolean)paramMap.get(IS_MAX_RETRY_COUNT_REACHED) || !isRetryEnabled()){
								break;
							}
						} catch (InterruptedException ie) {
							vlogError("["+pipelineProcessorName +"->retry()]:" + CricketESPConstants.WHOOP_KEYWORD +  " InterruptedException while executing Thread.sleep():"  + "ConversationId: " + getConversationId(), ie);							
						}				
					}
					if(currentRetryCount == configuredRetryCount && !((Boolean)paramMap.get(IS_MAX_RETRY_COUNT_REACHED))){
						logInfo("["+pipelineProcessorName +"->retry()]: ************************** Giving up after : "+ currentRetryCount+" retries, for orderId :"+ cricketOrder.getId());
						if(isLoggingDebug()){
							logDebug("["+pipelineProcessorName +"->retry()]: currentRetryCount == configuredRetryCount, set isMaxRetryCountReached to true and currentRetryCount = 0");
						}
						isMaxRetryCountReached = true;
						paramMap.put(IS_MAX_RETRY_COUNT_REACHED, isMaxRetryCountReached);
						
						//reinitializing currentRetryCount to zero for other threads to run the retry logic in case of espTimeOut
						currentRetryCount = 0;
						paramMap.put(CURRENT_RETRY_COUNT, currentRetryCount);						
					}				
				}
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->retry()]: Exiting retry()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId() );
			}
		}
		return returnCode;
	}
	
	/**
	 * @param phoneNumber
	 * @return
	 */
	public String getPhoneNumberInDigits(String phoneNumber){
		if(phoneNumber != null && phoneNumber.length() > 0){
			phoneNumber = phoneNumber.replaceAll(PHONE_REGEX, EMPTY_STRING);
		}
		return phoneNumber;
	}

	/**
	 * @return the errorDiscription
	 */
	public boolean isErrorDiscription() {
		return errorDiscription;
	}

	/**
	 * @param errorDiscription the errorDiscription to set
	 */
	public void setErrorDiscription(boolean errorDiscription) {
		this.errorDiscription = errorDiscription;
	}
	/**
	 * @param pResult
	 * @param errorCode
	 * @param errorNameCode
	 * @param execptionType
	 * @param typeofException
	 */
	public void showErrorLogs(PipelineResult pResult,
								String errorCode,String errorDescription,
								Object execption,String typeofException){
		
		if(isErrorDiscription()){
				if(typeofException.equalsIgnoreCase(("ESPApplicationException"))){
					if(execption instanceof ESPApplicationException){
						ESPApplicationException eae =(ESPApplicationException)execption;
						if(errorCode.equals(CricketESPConstants.ESP_BUSINESS_EXCEPTIONS)){
							pResult.addError(eae.getCause().getMessage(), eae.getCause().getMessage());					
						}else{
							pResult.addError(errorCode, errorDescription);
						}
					}
				}else if(typeofException.equalsIgnoreCase(("RemoteException"))){
					if(execption instanceof RemoteException){
					RemoteException re =(RemoteException)execption;
	 					pResult.addError(re.getCause().getMessage(), re.getCause().getMessage());
					}
				}else if(typeofException.equalsIgnoreCase(("ServiceException"))){
					if(execption instanceof ServiceException){
					ServiceException se =(ServiceException)execption;
	 					pResult.addError(se.getCause().getMessage(), se.getCause().getMessage());	
					}
				}else{
					if(execption instanceof Exception){
						Exception exp = (Exception)execption;
 	 					pResult.addError(exp.getCause().getMessage(), exp.getCause().getMessage());	
					} else {
						pResult.addError(errorCode, errorDescription);	
					}
				}
	 	}else {
			pResult.addError(errorCode, errorDescription);	
		}
	}
	
	// cancel billingquote 
	public boolean cancelBillingQuoteStatus(CricketOrderImpl order) {
        
		try{
		if(order.getBillingQuoteId() != null && order.getBillingQuoteId().length() > 0){
	       // UpdateBillingQuoteStatusResponseInfo billingQuoteresponse = null;
			if (isLoggingDebug()) {
				// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}				 					
		    		logDebug("[CricketESPAdapter->cancelBillingQuoteStatus()]: Entering into cancelBillingQuoteStatus()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
			}
			
			UpdateBillingQuoteStatusResponseInfo billingQuoteResponse = null;
			
			UpdateBillingQuoteStatus billingQuoteStatus = new CricketESPHTTPServicesLocator().getUpdateBillingQuoteStatus(new URL(getEspServiceUrl()+ESP_NAME_UPDATEBILLINGQUOTESTATUS));
			UpdateBillingQuoteStatusStub billingStatusStub = (UpdateBillingQuoteStatusStub) billingQuoteStatus;
			intializeHeader(billingStatusStub,order.getId());       
	     
	        UpdateBillingQuoteStatusRequestInfo billingQuoteStatusRequestInfo = new UpdateBillingQuoteStatusRequestInfo();          
	        
	        billingQuoteStatusRequestInfo.setQuoteId(order.getBillingQuoteId());
	        billingQuoteStatusRequestInfo.setQuoteStatus(BillingQuoteStatusInfo.fromString(UPDATE_BILLING_QUOTE_STATUS_CANCEL));       
	        billingQuoteStatusRequestInfo.setCommission(getCricketESPAdapterHelper().getDealerCommission());
	        billingQuoteResponse = billingStatusStub.updateBillingQuoteStatus(billingQuoteStatusRequestInfo);
	        
	        if(billingQuoteResponse != null) {
	        	ResponseInfo[] responseInfos = billingQuoteResponse.getResponse();
	        	if(responseInfos != null && responseInfos.length > 0){
	        		if(ESP_RESPONSE_CODE_SUCCESS.equalsIgnoreCase(responseInfos[0].getCode())){
	        			return true;
	        		}
	        	}
	        }
	        
	       
	        if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->cancelBillingQuoteStatus()]: Exiting cancelBillingQuoteStatus()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() );
			} 
		}
		} catch (ServiceException se) {
			vlogError("[CricketESPAdapter->cancelBillingQuoteStatus()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ServiceException while getting cancelBillingQuoteStatus response:" + "ConversationId: " + getConversationId(), se);
		} catch (ESPApplicationException eae) {
			vlogError("[CricketESPAdapter->cancelBillingQuoteStatus()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ESPApplicationException while getting cancelBillingQuoteStatus response:" + "ConversationId: " + getConversationId(), eae);
		} catch (RemoteException re) {
			vlogError("[CricketESPAdapter->cancelBillingQuoteStatus()]:" + CricketESPConstants.WHOOP_KEYWORD +  " RemoteException while getting cancelBillingQuoteStatus response:" + "ConversationId: " + getConversationId(), re);
		} catch(Exception e){
			vlogError("[CricketESPAdapter->cancelBillingQuoteStatus()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Exception while getting cancelBillingQuoteStatus response:" + "ConversationId: " + getConversationId(), e);
		}
       return false;       
  }

	
	/**
	 * @return the multilineDiscountOfferId
	 */
	public String getMultilineDiscountOfferId() {
		return multilineDiscountOfferId;
	}

	/**
	 * @param multilineDiscountOfferId the multilineDiscountOfferId to set
	 */
	public void setMultilineDiscountOfferId(String multilineDiscountOfferId) {
		this.multilineDiscountOfferId = multilineDiscountOfferId;
	}

	/**
	 * @return the activationFeeOfferId
	 */
	public String getActivationFeeOfferId() {
		return activationFeeOfferId;
	}

	/**
	 * @param activationFeeOfferId the activationFeeOfferId to set
	 */
	public void setActivationFeeOfferId(String activationFeeOfferId) {
		this.activationFeeOfferId = activationFeeOfferId;
	}

	/**
	 * @return the multilineDiscountOfferIdForADD
	 */
	public String getMultilineDiscountOfferIdForADD() {
		return multilineDiscountOfferIdForADD;
	}

	/**
	 * @param multilineDiscountOfferIdForADD the multilineDiscountOfferIdForADD to set
	 */
	public void setMultilineDiscountOfferIdForADD(
			String multilineDiscountOfferIdForADD) {
		this.multilineDiscountOfferIdForADD = multilineDiscountOfferIdForADD;
	}

}// end of file
