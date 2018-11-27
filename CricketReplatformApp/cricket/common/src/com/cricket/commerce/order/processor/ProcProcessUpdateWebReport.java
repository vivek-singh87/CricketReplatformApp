package com.cricket.commerce.order.processor;

import static com.cricket.common.constants.CricketESPConstants.ACCESSORY_ONLY;
import static com.cricket.common.constants.CricketESPConstants.ACCESSORY_PRODUCT;
import static com.cricket.common.constants.CricketESPConstants.ACCESSRORY;
import static com.cricket.common.constants.CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.ADD_A_LINE;
import static com.cricket.common.constants.CricketESPConstants.ADD_ON_PRODUCT;
import static com.cricket.common.constants.CricketESPConstants.BILLING_ADDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.CHANGE_ADD_ON;
import static com.cricket.common.constants.CricketESPConstants.CHANGE_PLAN;
import static com.cricket.common.constants.CricketESPConstants.ESP_BUSINESS_EXCEPTIONS;
import static com.cricket.common.constants.CricketESPConstants.ESP_SYSTEM_EXCEPTIONS;
import static com.cricket.common.constants.CricketESPConstants.ESP_TIMEOUT;
import static com.cricket.common.constants.CricketESPConstants.FEATURE;
import static com.cricket.common.constants.CricketESPConstants.FEATURE_CHANGE;
import static com.cricket.common.constants.CricketESPConstants.NEW_ACTIVATION;
import static com.cricket.common.constants.CricketESPConstants.PHONE;
import static com.cricket.common.constants.CricketESPConstants.PHONE_PRODUCT;
import static com.cricket.common.constants.CricketESPConstants.PLAN;
import static com.cricket.common.constants.CricketESPConstants.PLAN_PRODUCT;
import static com.cricket.common.constants.CricketESPConstants.PROFILE_ITEM;
import static com.cricket.common.constants.CricketESPConstants.PROPERTY_MARKET_ID;
import static com.cricket.common.constants.CricketESPConstants.RATE_PLAN_MIGRATION;
import static com.cricket.common.constants.CricketESPConstants.READ_TIMED_OUT;
import static com.cricket.common.constants.CricketESPConstants.SHIPPING_ADDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.TRANSACTION_TYPE_ACT;
import static com.cricket.common.constants.CricketESPConstants.TRANSACTION_TYPE_ADD;
import static com.cricket.common.constants.CricketESPConstants.TRANSACTION_TYPE_OUP;
import static com.cricket.common.constants.CricketESPConstants.TRANSACTION_TYPE_OXC;
import static com.cricket.common.constants.CricketESPConstants.TRANSACTION_TYPE_RRC;
import static com.cricket.common.constants.CricketESPConstants.UPGRADEPHONE;
import static com.cricket.common.constants.CricketESPConstants.UPGRADEPLAN;
import static com.cricket.common.constants.CricketESPConstants.UPGRADE_PHONE;
import static com.cricket.common.constants.CricketESPConstants.USER_COOKIE_INFO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.TransactionManager;
import javax.xml.rpc.ServiceException;

import atg.commerce.order.CommerceItemImpl;
import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.OrderManager;
import atg.commerce.order.PipelineConstants;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;
import atg.userprofiling.ProfileTools;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketBillingAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.commerce.order.CricketPackage;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.CricketESPAdapterHelper;
import com.cricket.integration.esp.vo.AddressVO;
import com.cricket.integration.esp.vo.ESPResponseVO;
import com.cricket.integration.esp.vo.EmailVO;
import com.cricket.integration.esp.vo.NameVO;
import com.cricket.integration.esp.vo.PhoneVO;
import com.cricket.integration.esp.vo.UpdateWebReportRequestVO;
import com.cricket.integration.esp.vo.UpdateWebReportRequestVOCustomer;
import com.cricket.integration.esp.vo.UpdateWebReportRequestVOItems;
import com.cricket.integration.esp.vo.UpdateWebReportRequestVOItemsItem;
import com.cricket.vo.CricketProfile;

public class ProcProcessUpdateWebReport extends GenericService implements PipelineProcessor {
  	  
	/** The profileTools. */
    private ProfileTools profileTools;

    /** The transactionManager. */
    private TransactionManager transactionManager;

    /** The orderManger. */
    private OrderManager orderManager;
    
    private CricketESPAdapter cricketESPAdapter;
    
    /** cricketOrderTools */
    private CricketOrderTools cricketOrderTools;
   
    private static final int MANAGEPAYMENT_FOR_RRC=1;
    private static final int FINALIZE_SALE=2;
    private static final String ESP_SUCCESS="0";
    /** Value SUCCESS return. */
    private static final int SUCCESS = 1;
    
    /* holds CricketESPAdapterHelper */
	private CricketESPAdapterHelper cricketESPAdapterHelper;		 
		
 	  /**
	   * 1 - The processor completed
	   * @return an integer array of the valid return codes.
	   */ 
	  public int[] getRetCodes()
	  {
	    int[] ret = {MANAGEPAYMENT_FOR_RRC,FINALIZE_SALE,PipelineProcessor.STOP_CHAIN_EXECUTION};
	    return ret;
	  }  
                
	
	  /* (non-Javadoc)
	 * @see atg.service.pipeline.PipelineProcessor#runProcess(java.lang.Object, atg.service.pipeline.PipelineResult)
	 */
	public int runProcess(Object pParam, PipelineResult pResult) throws InvalidParameterException  
	  { 
	  
 	 //  logInfo("[ProcProcessUpdateWebReport->runProcess()]: enters into ProcProcessUpdateWebReport processor -");
	  
	    HashMap<String, Object> paramMap = (HashMap<String, Object>) pParam;
        Object order = paramMap.get(PipelineConstants.ORDER);
        if (order == null) {
            throw new InvalidParameterException("order Id is not valid");
        }
        boolean flag=false;
        CricketOrderImpl cricketOrder = (CricketOrderImpl) order;
        if (isLoggingDebug()) {			 					
	    		logDebug("Inside the ProcProcessUpdateWebReport class of runProcess() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}
        try{
        	 Object accountHolderAddressDataObj = paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
        	 CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData)accountHolderAddressDataObj;
        	  logInfo("[ProcProcessUpdateWebReport->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
        				accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
        				accountHolderAddressData.getAccountAddress().getPhoneNumber());
		        ESPResponseVO[] response =null; 
		        response = updateWebReport(cricketOrder,paramMap);
		        
		        if(null!=response && response.length>0) {
			        for(ESPResponseVO res:response){
			        	if(!res.getCode().equalsIgnoreCase(ESP_SUCCESS)){
			        		flag=true;
			        		pResult.addError(res.getCode(), res.getDescription());			        		 
			        	}			        	 
			        }		        
		        }
		        
			    logInfo("[ProcProcessUpdateWebReport->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
			    return SUCCESS; 	 
       } catch (ServiceException se) {
    	   	vlogError("[ProcProcessUpdateWebReport->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ServiceException while getting UpdateWebReport response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), se);
			flag=true;
			pResult.addError("SYSTEM_EXCEPTIONS", "SYSTEM_EXCEPTIONS");			
			 
		} catch (ESPApplicationException eae) {
			vlogError("[ProcProcessUpdateWebReport->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ESPApplicationException while getting UpdateWebReport response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), eae);
			 flag=true;
			 String errorCode = getCricketESPAdapter().getEspFaultCode(eae);		
			 if(ESP_TIMEOUT.equalsIgnoreCase(errorCode)){
				int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);	
				if(returnCode <= 0){
					pResult.addError(errorCode,errorCode);
				}else{
					return returnCode;
				}
			} else{
				pResult.addError(ESP_BUSINESS_EXCEPTIONS,ESP_BUSINESS_EXCEPTIONS);
			}
		 
			
		} catch (RemoteException re) {			
			flag=true;
			vlogError("[ProcProcessUpdateWebReport->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " RemoteException while getting UpdateWebReport response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), re);
			if(re.getCause() != null && re.getCause().getMessage() != null && re.getCause().getMessage().contains(READ_TIMED_OUT)){
				int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);
				if(returnCode <= 0){
					pResult.addError(ESP_TIMEOUT,ESP_TIMEOUT);
				}else{
					return returnCode;
				}
			}else{
				pResult.addError(ESP_SYSTEM_EXCEPTIONS,ESP_SYSTEM_EXCEPTIONS);
			}
		} catch (Exception e) {
			flag=true;
			vlogError("[ProcProcessUpdateWebReport->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Exception while getting UpdateWebReport response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
			pResult.addError("SYSTEM_EXCEPTIONS", "SYSTEM_EXCEPTIONS");
		}
        if(flag){
        	if (isLoggingDebug()) {			 					
        		logDebug("[ProcProcessUpdateWebReport->runProcess()]: Exiting runProcess(), with STOP_CHAIN_EXECUTION..");
            }
        	return PipelineProcessor.STOP_CHAIN_EXECUTION;
        }
        else{
        	if(cricketOrder.getWorkOrderType().equalsIgnoreCase("RRC")){
        		if (isLoggingDebug()) {			 					
            		logDebug("[ProcProcessUpdateWebReport->runProcess()]: Exiting runProcess(), with MANAGEPAYMENT_FOR_RRC..");
                }
        		return MANAGEPAYMENT_FOR_RRC;
        	}else{
        		if (isLoggingDebug()) {			 					
            		logDebug("[ProcProcessUpdateWebReport->runProcess()]: Exiting runProcess(), with FINALIZE_SALE..");
                }
        		return FINALIZE_SALE;
        	}
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
	
	/**
	 * @param pCricketOrder
	 * @param pParamMap 
	 * @param pUserCookieInfo 
	 * @param pAddressInfo 
	 * @param pCookieInfo 
	 * @return 
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws ESPApplicationException 
	 */
	private ESPResponseVO[] updateWebReport(CricketOrderImpl pCricketOrder, HashMap<String, Object> pParamMap) throws ESPApplicationException, RemoteException, MalformedURLException, ServiceException {	
		
		com.cricket.integration.esp.vo.ESPResponseVO[] response = null;
	    long starttime	= 0L;
		long endtime 	= 0L;	
		starttime = System.currentTimeMillis();
		
		if (isLoggingDebug()) {			
    		logDebug("Inside the ProcProcessUpdateWebReport class of updateWebReport() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId());
		}
		try {
			UpdateWebReportRequestVO requestInfo =  populateUpdateWebReportRequest(pCricketOrder, pParamMap);
			starttime = System.currentTimeMillis();
			response = getCricketESPAdapter().updateWebReport(requestInfo,pCricketOrder);
			endtime = System.currentTimeMillis();
			logInfo("[ProcProcessUpdateWebReport->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));				
		} catch (CricketException e) {
			vlogError("ProcProcessUpdateWebReport:updateWebReport" + CricketESPConstants.WHOOP_KEYWORD +  "Error while ESP call updateWebReport while submitting the order:.. " + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  pCricketOrder.getId(), e);
		}
		if (isLoggingDebug()) {			
    		logDebug("Exiting from ProcProcessUpdateWebReport class of updateWebReport() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId() + "response::" + response);
		}
		return response;
	}

	/**
	 * @param pCricketOrder
	 * @param pParamMap 
	 * @param pUserCookeInfo 
	 * @return
	 * @throws CricketException 
	 */
	private UpdateWebReportRequestVO populateUpdateWebReportRequest(
			CricketOrderImpl pCricketOrder, HashMap<String, Object> pParamMap) throws CricketException {
		
		if (isLoggingDebug()) {			
    		logDebug("Inside the ProcProcessUpdateWebReport class of populateUpdateWebReportRequest() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId());
		}
		CricketProfile userCookieInfo = (CricketProfile) pParamMap.get(USER_COOKIE_INFO);
		RepositoryItem profileItem = (RepositoryItem)pParamMap.get(PROFILE_ITEM);
		 
		if(null==userCookieInfo){
			throw new CricketException("Error while fetching cookie info while invoking webservice updatewebreport - "+getClass().getName());
		}
		
		CricketAccountHolderAddressData accountAddressInfo = (CricketAccountHolderAddressData) pParamMap.get(ACCOUNT_HOLDER_ADDDRESS_DATA);
		if(null==accountAddressInfo){
			throw new CricketException("Error while fetching cookie info while invoking webservice updatewebreport - "+getClass().getName());
		}
		
		CricketShippingAddressData shippingAddressInfo = (CricketShippingAddressData) pParamMap.get(SHIPPING_ADDRESS_DATA);
		CricketBillingAddressData billingAddressInfo = (CricketBillingAddressData) pParamMap.get(BILLING_ADDRESS_DATA);		 
		 if(isLoggingDebug()){
	        	CricketOrderTools ordertools =(CricketOrderTools) getOrderManager().getOrderTools();
	    		logDebug("[ProcProcessUpdateBillingQuoteStatus->runProcess()]: Order Details :");
	    		logDebug(ordertools.orderDetailsToTrack(pCricketOrder,null,this.getClass().getName()));
             }
		
		UpdateWebReportRequestVO webReportRequest = new UpdateWebReportRequestVO();		
		
		webReportRequest.setBillingQuoteNumber(pCricketOrder.getBillingQuoteId());
		webReportRequest.setBillingOrderId(pCricketOrder.getBillingSystemOrderId());
		Date orderDate = pCricketOrder.getCreationDate();
		Calendar orderCreationDate = Calendar.getInstance();
		orderCreationDate.setTime(orderDate);
		webReportRequest.setOrderCreatedDate(orderCreationDate);
		webReportRequest.setAccountNumber(userCookieInfo.getAccountNumber());
		if(userCookieInfo.getIntention() != null && TRANSACTION_TYPE_RRC.equals(pCricketOrder.getWorkOrderType())){
			if(userCookieInfo.getIntention() != null && userCookieInfo.getIntention().toLowerCase().contains(CricketESPConstants.PLAN)){ 
				webReportRequest.setTransactionType(RATE_PLAN_MIGRATION);
			}else{
				webReportRequest.setTransactionType(FEATURE_CHANGE);
			}
		}else{
			webReportRequest.setTransactionType(getTransactionTypeName(pCricketOrder.getWorkOrderType()));
		}
		
		if(!StringUtils.isBlank(userCookieInfo.getMarketCode()))
				webReportRequest.setMarketId(userCookieInfo.getMarketCode());
		else
			webReportRequest.setMarketId((String) profileItem.getPropertyValue(PROPERTY_MARKET_ID));
		webReportRequest.setSalesRepresentative(getCricketESPAdapter().getSalesRepresentative());
		
		webReportRequest.setLanguage("E");
		Calendar quoteCreateDate = Calendar.getInstance();
		quoteCreateDate.setTime(new Date());
		webReportRequest.setQuoteCreatedDate(quoteCreateDate);		
		
		UpdateWebReportRequestVOCustomer customer = new UpdateWebReportRequestVOCustomer();
		customer.setCustomerId(pCricketOrder.getCricCustomerId());
		boolean isLoggedIn = !profileItem.isTransient();
		if(isLoggedIn){
			customer.setCustomerType(userCookieInfo.getCustomerType());	
		} 
		else{
			customer.setCustomerType("S");
		}		
		
		NameVO customerName = new NameVO();
		if(!StringUtils.isBlank(userCookieInfo.getFirstName())){
			customerName.setFirstName(userCookieInfo.getFirstName());
		}
		else{
			customerName.setFirstName(accountAddressInfo.getAccountAddress().getFirstName());
		}
		if(!StringUtils.isEmpty(userCookieInfo.getLastName()))
			customerName.setLastName(userCookieInfo.getLastName());
		else
			customerName.setLastName(accountAddressInfo.getAccountAddress().getLastName());
		
		customer.setName(customerName);
		AddressVO billingAddress = new AddressVO();		
		 
		boolean sameAsAccountAdd = false;
		boolean sameAsShippingAddress = false;		 
		
		if(null!=shippingAddressInfo) {
			sameAsAccountAdd = shippingAddressInfo.isBillingAddressAsAccountHolderAddress();
			sameAsShippingAddress = shippingAddressInfo.isBillingAddressAsShippingAddress();
		}	
				
		if((sameAsAccountAdd && null!=accountAddressInfo) ){					 
			billingAddress.setAddressLine1(accountAddressInfo.getAccountAddress().getAddress1());
			billingAddress.setAddressLine2(accountAddressInfo.getAccountAddress().getAddress2());			
			billingAddress.setCity(accountAddressInfo.getAccountAddress().getCity());
			billingAddress.setZipCode(accountAddressInfo.getAccountAddress().getPostalCode());
			billingAddress.setState(accountAddressInfo.getAccountAddress().getStateAddress());
			billingAddress.setFirstName(accountAddressInfo.getAccountAddress().getFirstName());
			billingAddress.setLastName(accountAddressInfo.getAccountAddress().getLastName());
				
		}
				
		else if(sameAsShippingAddress ){
				billingAddress.setAddressLine1(shippingAddressInfo.getShippingAddress().getAddress1());
				billingAddress.setAddressLine2(shippingAddressInfo.getShippingAddress().getAddress2());			
				billingAddress.setCity(shippingAddressInfo.getShippingAddress().getCity());
				billingAddress.setZipCode(shippingAddressInfo.getShippingAddress().getPostalCode());
				billingAddress.setState(shippingAddressInfo.getShippingAddress().getStateAddress());
				billingAddress.setFirstName(shippingAddressInfo.getShippingAddress().getFirstName());
				billingAddress.setLastName(shippingAddressInfo.getShippingAddress().getLastName());
		}
			
		else if(null!=billingAddressInfo){					
			billingAddress.setAddressLine1(billingAddressInfo.getBillingAddress().getAddress1());
			billingAddress.setAddressLine2(billingAddressInfo.getBillingAddress().getAddress2());			
			billingAddress.setCity(billingAddressInfo.getBillingAddress().getCity());
			billingAddress.setZipCode(billingAddressInfo.getBillingAddress().getPostalCode());
			billingAddress.setState(billingAddressInfo.getBillingAddress().getStateAddress());
			billingAddress.setFirstName(billingAddressInfo.getBillingAddress().getFirstName());
			billingAddress.setLastName(billingAddressInfo.getBillingAddress().getLastName());
		}
			
		customer.setBillingAddress(billingAddress);
		
		AddressVO shippingAddress = new AddressVO();
		
		if(null!=shippingAddressInfo) {
			if( shippingAddressInfo.isShippingAddressAsAccountHolderAddress()){
				
				shippingAddress.setAddressLine1(accountAddressInfo.getAccountAddress().getAddress1());
				shippingAddress.setAddressLine2(accountAddressInfo.getAccountAddress().getAddress2());			
				shippingAddress.setCity(accountAddressInfo.getAccountAddress().getCity());
				shippingAddress.setZipCode(accountAddressInfo.getAccountAddress().getPostalCode());		
				shippingAddress.setState(accountAddressInfo.getAccountAddress().getStateAddress());				
				shippingAddress.setFirstName(accountAddressInfo.getAccountAddress().getFirstName());
				shippingAddress.setLastName(accountAddressInfo.getAccountAddress().getLastName());
			}
			else {
				
				shippingAddress.setAddressLine1(shippingAddressInfo.getShippingAddress().getAddress1());
				shippingAddress.setAddressLine2(shippingAddressInfo.getShippingAddress().getAddress2());			
				shippingAddress.setCity(shippingAddressInfo.getShippingAddress().getCity());
				shippingAddress.setZipCode(shippingAddressInfo.getShippingAddress().getPostalCode());
				shippingAddress.setState(shippingAddressInfo.getShippingAddress().getStateAddress());
				shippingAddress.setFirstName(shippingAddressInfo.getShippingAddress().getFirstName());
				shippingAddress.setLastName(shippingAddressInfo.getShippingAddress().getLastName());
			}
		}
		customer.setShippingAddress(shippingAddress);
		
		if(null!=accountAddressInfo){
			PhoneVO customerPhone = new PhoneVO();			
			customerPhone.setHomePhone(getCricketESPAdapter().getPhoneNumberInDigits(accountAddressInfo.getAccountAddress().getPhoneNumber()));			
			customer.setPhone(customerPhone);
			
			EmailVO customerEmail = new EmailVO();
			customerEmail.setEmailAddress(accountAddressInfo.getAccountAddress().getEmail());
			customer.setEmail(customerEmail);
		}
		
		webReportRequest.setCustomer(customer);
		UpdateWebReportRequestVOItems items[] = popoulateItemsToRequest(pCricketOrder, userCookieInfo);
		webReportRequest.setItems(items);
		if (isLoggingDebug()) {			
    		logDebug("Exiting from ProcProcessUpdateWebReport class of populateUpdateWebReportRequest() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId() + "webReportRequest::" + webReportRequest);
		}
		return webReportRequest;
	}

	/**
	 * @param pCricketOrder
	 * @return
	 */
	private UpdateWebReportRequestVOItems[] popoulateItemsToRequest(
			CricketOrderImpl pCricketOrder, CricketProfile userCookieInfo) {
		
		if (isLoggingDebug()) {			
    		logDebug("Inside the ProcProcessUpdateWebReport class of popoulateItemsToRequest() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId());
		}
		UpdateWebReportRequestVOItems[] requestItems= null;		
		List<CricketCommerceItemImpl> allCommerceItems = pCricketOrder.getCommerceItems();	
		
		List<CricketPackage> cktPackages = pCricketOrder.getCktPackages(); 
		int packageCount = 0;			
		
		if(cktPackages != null && cktPackages.size() > 0){
			packageCount = cktPackages.size();			
		}
		
		if(packageCount == 0){
			//Handling all scenarios except LOS
			
			UpdateWebReportRequestVOItemsItem updateWebReportRequestVOItemsItem = null;
			UpdateWebReportRequestVOItemsItem itemInfo[] = new UpdateWebReportRequestVOItemsItem[allCommerceItems.size()];
			requestItems= new UpdateWebReportRequestVOItems[1];
			int itemLength=0;
			
			for(CricketCommerceItemImpl item: allCommerceItems){				
				updateWebReportRequestVOItemsItem = createUpdateWebReportRequestVOItemsItem(item);
				itemInfo[itemLength] = updateWebReportRequestVOItemsItem;		
				itemLength++;
			}
		
			requestItems[0]=new UpdateWebReportRequestVOItems();
			if(userCookieInfo.getIntention() != null && (userCookieInfo.getIntention().contains("Upgrade") || userCookieInfo.getIntention().contains("Change"))){
				requestItems[0].setMdn(userCookieInfo.getMdn());
			}
			requestItems[0].setItem(itemInfo);
		
		}else{
			//Handling LOS and Multi LOS scenarios
			
			UpdateWebReportRequestVOItemsItem updateWebReportRequestVOItemsItem = null;
			UpdateWebReportRequestVOItemsItem itemInfo[] = null;			
			CricketCommerceItemImpl[] packageCommerceItemList = null;
			UpdateWebReportRequestVOItems updateWebReportRequestVOItem = null;
			int index = 0;
			int accessoriesCount = getCricketESPAdapterHelper().getAccessoriesItemCount(allCommerceItems);
			boolean includeAccessoryItem = false;
			String productType = null;
			
			requestItems= new UpdateWebReportRequestVOItems[packageCount];			
			
			for(CricketPackage packageItem : cktPackages){
				
				packageCommerceItemList = getCricketOrderTools().getCommerceItemsForPackage(packageItem, pCricketOrder);
				
				if(packageCommerceItemList != null && packageCommerceItemList.length > 0){
					
					int itemLength=0;					
					String mdn = (String) packageItem.getPropertyValue(CricketPipleLineConstants.CRICKET_NEW_MDN);
					
					if(index + 1 == packageCount){
						itemInfo = new UpdateWebReportRequestVOItemsItem[packageCommerceItemList.length + accessoriesCount];
						if(accessoriesCount > 0){
							includeAccessoryItem = true;
						}
					}else{
						itemInfo = new UpdateWebReportRequestVOItemsItem[packageCommerceItemList.length];
					}
					
					for(CricketCommerceItemImpl commerceItem : packageCommerceItemList)
					{
						updateWebReportRequestVOItemsItem = createUpdateWebReportRequestVOItemsItem(commerceItem);
						itemInfo[itemLength] = updateWebReportRequestVOItemsItem;		
						itemLength++;
						
					}
					
					if(includeAccessoryItem){
						for(CricketCommerceItemImpl accessoryItem : allCommerceItems){
							productType = (String) accessoryItem.getCricItemTypes();			
							if(ACCESSORY_PRODUCT.equalsIgnoreCase(productType)){
								updateWebReportRequestVOItemsItem = createUpdateWebReportRequestVOItemsItem(accessoryItem);
								itemInfo[itemLength] = updateWebReportRequestVOItemsItem;		
								itemLength++;
							}					
						}
					}
					
					updateWebReportRequestVOItem = new UpdateWebReportRequestVOItems();
					updateWebReportRequestVOItem.setMdn(mdn);
					updateWebReportRequestVOItem.setItem(itemInfo);
					
					requestItems[index] = updateWebReportRequestVOItem;
					index++;										
				}				
			}		
			
		}
		if (isLoggingDebug()) {			
    		logDebug("Exiting from ProcProcessUpdateWebReport class of popoulateItemsToRequest() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId() + "requestItems::" + requestItems);
		}
		return requestItems;
	}
	
	private String getElligibleItemCode(String itemType, RepositoryItem sku, RepositoryItem productItem, String catalogRefId) {
		String itemCode = catalogRefId;
		String modelNumber = null;
		String parcOfferId = null;
		try {
			if(PHONE.equals(itemType) && sku != null && sku.getItemDescriptor().hasProperty(CricketCommonConstants.MODEL_NUMBER)) {
				modelNumber = (String)sku.getPropertyValue(CricketCommonConstants.MODEL_NUMBER);
				if(modelNumber != null && modelNumber.length() > 0) {
					itemCode = modelNumber;
				}
			} else if(FEATURE.equals(itemType) && productItem != null && productItem.getItemDescriptor().hasProperty(CricketCommonConstants.PROP_OFFER_ID)) {
				parcOfferId = (String)productItem.getPropertyValue(CricketCommonConstants.PROP_OFFER_ID);
				if(parcOfferId != null && parcOfferId.length() > 0) {
					itemCode = parcOfferId;
				}
			}
		} catch (RepositoryException e) {
			if(isLoggingError()) {
				logError(e.getMessage());
			}
		}
		return itemCode;
	}

	/**
	 * @param item
	 * @return
	 */
	private UpdateWebReportRequestVOItemsItem createUpdateWebReportRequestVOItemsItem(CommerceItemImpl item) {
		
		UpdateWebReportRequestVOItemsItem updateWebReportRequestVOItemsItem = new UpdateWebReportRequestVOItemsItem();
		RepositoryItem sku = (RepositoryItem) item.getAuxiliaryData().getCatalogRef();
		String productType = (String) item.getPropertyValue(CricketPipleLineConstants.CRICKET_ITEM_TYPE);
		String itemType = getProductItemType(productType);
		RepositoryItem productItem = (RepositoryItem)item.getAuxiliaryData().getProductRef();
		String itemCode = getElligibleItemCode(itemType, sku, productItem, item.getCatalogRefId());
		updateWebReportRequestVOItemsItem.setItemCode(itemCode);
		updateWebReportRequestVOItemsItem.setItemDescription((String) sku.getPropertyValue(CricketPipleLineConstants.CRICKET_ITEM_DISPLAYNAME));
		updateWebReportRequestVOItemsItem.setItemType(itemType);  		
		
		BigInteger qty = new BigInteger(String.valueOf(item.getQuantity()));
		updateWebReportRequestVOItemsItem.setQuantity(qty);
		
		if(PHONE_PRODUCT.equals(productType) || ACCESSORY_PRODUCT.equals(productType) || UPGRADE_PHONE.equals(productType)){
			updateWebReportRequestVOItemsItem.setSku(item.getCatalogRefId());
		}	
		
		if(item.getPriceInfo() != null){
			BigDecimal unitPrice = BigDecimal.valueOf(Double.valueOf(new DecimalFormat("###.##").format(item.getPriceInfo().getListPrice())));
			updateWebReportRequestVOItemsItem.setUnitPrice(unitPrice);		
		
			BigDecimal unitActualPrice = BigDecimal.valueOf(Double.valueOf(new DecimalFormat("###.##").format(item.getPriceInfo().getAmount())));
			updateWebReportRequestVOItemsItem.setUnitActualPrice(unitActualPrice);
			
			if(PLAN_PRODUCT.equals(productType) || CHANGE_PLAN.equals(productType) || ADD_ON_PRODUCT.equals(productType) || CHANGE_ADD_ON.equals(productType) ){
				BigDecimal monthlyPrice = BigDecimal.valueOf(Double.valueOf(new DecimalFormat("###.##").format(item.getPriceInfo().getAmount())));
				updateWebReportRequestVOItemsItem.setUnitMonthlyPrice(monthlyPrice);
			}
		}
		// TODO itemInfo.setUnitMonthlyPrice(pUnitMonthlyPrice);		
		// TODO itemInfo.setUnitPrice(pUnitPrice);
		
		return updateWebReportRequestVOItemsItem;
	}


	/**
	 * @param productItemType
	 * @return
	 */
	private String getProductItemType(String productItemType){
		String itemTypeDescription = null;
		
		if(productItemType != null && productItemType.length() > 0){
			if(ACCESSORY_PRODUCT.equals(productItemType)){
				itemTypeDescription = ACCESSRORY;
			}else if(PHONE_PRODUCT.equals(productItemType) || UPGRADE_PHONE.equals(productItemType)){
				itemTypeDescription = PHONE;
			}else if(PLAN_PRODUCT.equals(productItemType) || CHANGE_PLAN.equals(productItemType)){
				itemTypeDescription = PLAN;
			}else if(ADD_ON_PRODUCT.equals(productItemType) || CHANGE_ADD_ON.equals(productItemType)){
				itemTypeDescription = FEATURE;
			}
		}
		return itemTypeDescription != null ? itemTypeDescription: productItemType;
	}
	
	/**
	 * @param transactionType
	 * @return
	 */
	private String getTransactionTypeName(String transactionType){
		String transactionTypeDescription = null;
		
		if(TRANSACTION_TYPE_ADD.equals(transactionType)){
			transactionTypeDescription = ADD_A_LINE;
		}else if(TRANSACTION_TYPE_ACT.equals(transactionType)){
			transactionTypeDescription = NEW_ACTIVATION;
		}else if(TRANSACTION_TYPE_RRC.equals(transactionType)){
			//transactionTypeDescription = FEATURE_CHANGE;			
		}else if(TRANSACTION_TYPE_OXC.equals(transactionType)){
			transactionTypeDescription = ACCESSORY_ONLY;
		}else if(TRANSACTION_TYPE_OUP.equals(transactionType)){
			transactionTypeDescription = UPGRADEPHONE;
		}	
		return transactionTypeDescription != null ? transactionTypeDescription: transactionType;
	}

	/**
	 * @return the profileTools
	 */
	public ProfileTools getProfileTools() {
		return profileTools;
	}


	/**
	 * @param pProfileTools the profileTools to set
	 */
	public void setProfileTools(ProfileTools pProfileTools) {
		profileTools = pProfileTools;
	}


	/**
	 * @return the transactionManager
	 */
	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

	/**
	 * @param pTransactionManager the transactionManager to set
	 */
	public void setTransactionManager(TransactionManager pTransactionManager) {
		transactionManager = pTransactionManager;
	}

	/**
	 * @return the orderManager
	 */
	public OrderManager getOrderManager() {
		return orderManager;
	}

	/**
	 * @param pOrderManager the orderManager to set
	 */
	public void setOrderManager(OrderManager pOrderManager) {
		orderManager = pOrderManager;
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
	 * @return the cricketESPAdapterHelper
	 */
	public CricketESPAdapterHelper getCricketESPAdapterHelper() {
		return cricketESPAdapterHelper;
	}


	/**
	 * @param cricketESPAdapterHelper the cricketESPAdapterHelper to set
	 */
	public void setCricketESPAdapterHelper(
			CricketESPAdapterHelper cricketESPAdapterHelper) {
		this.cricketESPAdapterHelper = cricketESPAdapterHelper;
	}
	
}
 