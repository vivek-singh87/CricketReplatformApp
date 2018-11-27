package com.cricket.commerce.order.processor.esp;

import static com.cricket.commerce.order.processor.CricketPipleLineConstants.CRICKET_ITEM_DISPLAYNAME;
import static com.cricket.commerce.order.processor.CricketPipleLineConstants.CRICKET_SHIPPING_ADDRESS_DATA;
import static com.cricket.commerce.order.processor.CricketPipleLineConstants.PROFILE_ITEM;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.OrderManager;
import atg.commerce.order.PipelineConstants;
import atg.core.util.ResourceUtils;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.repository.RepositoryItem;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.email.CricketEmailManager;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.NameValuePairVO;
import com.cricket.integration.esp.vo.SendMessageRequestVO;

/**
 * @author TechM
 * Description: This processor is used to invoke the SendMessage ESP call to send the promotional email if user interested.
 */
public class ProcProcessSendMessage extends GenericService implements PipelineProcessor {
	/** holding CricketESPAdapter component. */
    private CricketESPAdapter cricketESPAdapter;
    /** Value SUCCESS return. */
    private static final int SUCCESS = 1;
    /** Value UPDATE_SUBSCRIBER return. */
    private static final int  UPDATE_SUBSCRIBER = 2;
 	/** holding cricketEmail Manager. */
    private CricketEmailManager emailManager;
    /** holding order resources messages */
    private static final String MY_RESOURCE_NAME = "atg.commerce.order.OrderResources";
    /** holding order manager component */
    OrderManager orderManager;
    
   
  /**
	 * @return the orderManager
	 */
	public OrderManager getOrderManager() {
		return orderManager;
	}

	/**
	 * @param orderManager the orderManager to set
	 */
	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}

/**  
   * 1 - The processor completed
   * @return an integer array of the valid return codes.
   */ 
	  public int[] getRetCodes()
	  {
	    int[] ret = {SUCCESS,UPDATE_SUBSCRIBER};
	    return ret;
	  }  
  /** Resource Bundle **/
  private static ResourceBundle sResourceBundle = atg.core.i18n.LayeredResourceBundle.getBundle(MY_RESOURCE_NAME, atg.service.dynamo.LangLicense.getLicensedDefault());
                 
	/**
	 *  Description:This runProcess is used to invoke the SendMessage ESP call to send the promotional email if user interested.
	 *  @param Object pParam
	 *  @param PipelineResult pResult
	 *  @return int 
	 *  @throws Exception 
	 */
	public int runProcess(Object pParam, PipelineResult pResult) throws Exception
	{		
		//logInfo("[ProcProcessSendMessage->runProcess()]: Entering into runProcess()...");
		
		long starttime	= 0L;
		long endtime 	= 0L;
	    HashMap<?,?> paramMap = (HashMap<?,?>) pParam;
	   	String orderType = null;     
	    CricketOrderImpl cricketOrder = (CricketOrderImpl) paramMap.get(PipelineConstants.ORDER);
        CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData) paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
        CricketShippingAddressData  shippingAddressData = (CricketShippingAddressData) paramMap.get(CricketESPConstants.SHIPPING_ADDRESS_DATA);
        RepositoryItem  profileItem = (RepositoryItem)paramMap.get(CricketESPConstants.PROFILE_ITEM);
       
         if (cricketOrder == null)
		      throw new InvalidParameterException(ResourceUtils.getMsgResource("InvalidOrderParameter",
		                                      MY_RESOURCE_NAME, sResourceBundle));
         if (isLoggingDebug()) {			 					
	    		logDebug("Inside the ProcProcessSendMessage class of runProcess() method if order is not null:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}
         if (accountHolderAddressData == null)
		      throw new InvalidParameterException(CRICKET_ITEM_DISPLAYNAME);
         if (shippingAddressData == null)
		      throw new InvalidParameterException(CRICKET_SHIPPING_ADDRESS_DATA);
         if (profileItem == null)
		      throw new InvalidParameterException(PROFILE_ITEM);
         try{
        	 orderType = cricketOrder.getWorkOrderType();
          	  logInfo("[ProcProcessSendMessage->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
        				accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
        				accountHolderAddressData.getAccountAddress().getPhoneNumber());
        	 if(isLoggingDebug()){
        		 CricketOrderTools ordertools =(CricketOrderTools) getOrderManager().getOrderTools();
        	    logDebug("[ProcProcessSendMessage->runProcess()]: Order Details :");
        	    logDebug(ordertools.orderDetailsToTrack(cricketOrder,accountHolderAddressData.getAccountAddress(),this.getClass().getName()));
        	 }
        	 
        	 if(getCricketESPAdapter().isSendMessageESPEnabled()){
		        boolean emailNotification=accountHolderAddressData.isEmailnotification();
		        if(isLoggingDebug()){
	        	    logDebug("[ProcProcessSendMessage->runProcess()]: Is email enable :" +emailNotification);
	        	 }
				 if(emailNotification){
					
					 SendMessageRequestVO requestInfo = populatePromoEmailInfoToESP(
							 							accountHolderAddressData,
							 							shippingAddressData,
							 							cricketOrder,
							 							profileItem);
					
					starttime = System.currentTimeMillis();
					String status = getEmailManager().sendEmailInfoToESP(requestInfo,cricketOrder.getId());
					endtime = System.currentTimeMillis();
					
					logInfo("[ProcProcessSendMessage->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));				
					
				 }
        	 }
        		logInfo("[ProcProcessSendMessage->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
        	    return SUCCESS;
        }
         catch (ServiceException se) {
        	vlogError("[ProcProcessSendMessage->runProcess()]:"+ CricketESPConstants.WHOOP_KEYWORD +" ServiceException while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), se);
 			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
 		}			
		catch (ESPApplicationException eae) {
			vlogError("[ProcProcessSendMessage->runProcess()]:"+ CricketESPConstants.WHOOP_KEYWORD +" ESPApplicationException while getting sendMessage response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), eae);
			getCricketESPAdapter().getEspFaultCode(eae);       	
 		}
		catch (RemoteException re) {
			vlogError("[ProcProcessSendMessage->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + " RemoteException while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), re);
			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		} catch (Exception e){
			vlogError("[ProcProcessSendMessage->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + " Exception while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}
         
 			if(null!=orderType && CricketESPConstants.TRANSACTION_TYPE_RRC.equalsIgnoreCase(orderType)) {
 				 if (isLoggingDebug()) {			 					
 		    		logDebug("Subscriber Update...");
 				 }
				return UPDATE_SUBSCRIBER;
			}

 			logInfo("ProcProcessSendMessage->runProcess()]: Exiting runProcess(), with return SUCCESS...");
	    return SUCCESS;
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
	 *  Description:This runProcess is used to invoke the SendMessage ESP call to send the promotional email if user interested.
	 *  @param CricketAccountHolderAddressData accountHolderAddressData
	 *  @param CricketShippingAddressData shippingAddressData
	 *  @param CricketOrderImpl order
	 *  @param RepositoryItem profile
	 *  @return SendMessageRequestVO 
	 *  @throws Exception 
	 */
	private SendMessageRequestVO populatePromoEmailInfoToESP(CricketAccountHolderAddressData  accountHolderAddressData,
			CricketShippingAddressData  shippingAddressData,CricketOrderImpl order,RepositoryItem profile) {
		
		// Getting the Order Id
		String orderId = CricketCommonConstants.EMPTY_STRING;
		if(null != order){
			if(!StringUtils.isBlank(order.getId())){
				orderId = order.getId();
			}
		}
		if (isLoggingDebug()) {
			 					
	    		logDebug("Inside the ProcProcessSendMessage class of populatePromoEmailInfoToESP() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId);
		}
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		SendMessageRequestVO requestInfo = new SendMessageRequestVO();
		NameValuePairVO[] substitutionVariables = new NameValuePairVO[11];
		int index=0;
		if (!StringUtils.isBlank(accountHolderAddressData.getAccountAddress().getFirstName())) {
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName(CricketCookieConstants.FIRST_NAME);
			substitutionVariables[index].setValue(accountHolderAddressData.getAccountAddress().getFirstName());
			index++;			
		}
		
		if(!StringUtils.isBlank(accountHolderAddressData.getAccountAddress().getLastName())){
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName(CricketCookieConstants.LAST_NAME);
			substitutionVariables[index].setValue(accountHolderAddressData.getAccountAddress().getLastName());
			index++;
		}
		 
		if(!StringUtils.isBlank(order.getWorkOrderType())){
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName("saleType");
			substitutionVariables[index].setValue(order.getWorkOrderType());
			index++;
		}
		
		//Zip Code,Customer (aka opt in flag),Purchase_Date,City,State,Market  (HO code),Market Display Name ,Join_Date
		
		if(!StringUtils.isBlank(accountHolderAddressData.getAccountAddress().getPostalCode())){
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName("Zip Code");
			substitutionVariables[index].setValue(accountHolderAddressData.getAccountAddress().getPostalCode());
			index++;
		}
		
		
		if(!StringUtils.isBlank(accountHolderAddressData.getAccountAddress().getCity())){
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName("City");
			substitutionVariables[index].setValue(accountHolderAddressData.getAccountAddress().getCity());
			index++;
		}
		
		
		if(!StringUtils.isBlank(accountHolderAddressData.getAccountAddress().getStateAddress())){
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName("State");
			substitutionVariables[index].setValue(accountHolderAddressData.getAccountAddress().getStateAddress());
			index++;
		}
		
		if(!StringUtils.isBlank(accountHolderAddressData.getAccountAddress().getStateAddress())){
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName("Market");
			substitutionVariables[index].setValue((String)profile.getPropertyValue(CricketCommonConstants.PROP_MARKET_ID));
			index++;
		}
		
		if(!StringUtils.isBlank(accountHolderAddressData.getAccountAddress().getStateAddress())){
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName("Market Display Name");
			substitutionVariables[index].setValue((String)profile.getPropertyValue("marketName"));
			index++;
		}
		
		
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName("Join_Date");
			substitutionVariables[index].setValue(dateFormat.format(new java.util.Date()));
			index++;
			
		
		substitutionVariables[index] = new NameValuePairVO();
		substitutionVariables[index].setName("Purchase_Date");
		substitutionVariables[index].setValue(dateFormat.format(new java.util.Date()));
		index++;
		
		
		if(profile.isTransient()){
			
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName("IsCustomer");
			substitutionVariables[index].setValue("1");
			requestInfo.setZipCode((String)profile.getPropertyValue(CricketCommonConstants.USER_LOC_ZIP_CODE));
		}else{
			substitutionVariables[index] = new NameValuePairVO();
			substitutionVariables[index].setName("IsCustomer");
			substitutionVariables[index].setValue("0");
			requestInfo.setZipCode(shippingAddressData.getShippingAddress().getPostalCode());
		}		
		
		requestInfo.setSubstitutionVariables(substitutionVariables);
		
		requestInfo.setTemplateIdentifier(getEmailManager().getPromoEmailTemplate());
		requestInfo.setSubjectLine(getEmailManager().getPromotionalEmailSubject());
		requestInfo.setTemplateType(getEmailManager().getTemplateType());
 		requestInfo.setTemplateLanguage(order.getLanguageIdentifier());
 		
		if(!StringUtils.isBlank(accountHolderAddressData.getAccountAddress().getEmail())){
				 requestInfo.setToEmail(accountHolderAddressData.getAccountAddress().getEmail());
			}
		if (isLoggingDebug()) {
						
	    		logDebug("Exiting from ProcProcessSendMessage class of populatePromoEmailInfoToESP() method :::");
		}
	return requestInfo;	
	}

	/**
	 * @return the cricketESPAdapter
	 */
	public CricketESPAdapter getCricketESPAdapter() {
		return cricketESPAdapter;
	}

	/**
	 * @param pCricketESPAdapter the cricketESPAdapter to set
	 */
	public void setCricketESPAdapter(CricketESPAdapter pCricketESPAdapter) {
		cricketESPAdapter = pCricketESPAdapter;
	}

	/**
	 * @return the emailManager
	 */
	public CricketEmailManager getEmailManager() {
		return emailManager;
	}

	/**
	 * @param emailManager the emailManager to set
	 */
	public void setEmailManager(CricketEmailManager emailManager) {
		this.emailManager = emailManager;
	}	
	
}
 