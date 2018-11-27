package com.cricket.commerce.order.processor.esp;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.transaction.TransactionManager;
import javax.xml.rpc.ServiceException;

import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.OrderManager;
import atg.commerce.order.PipelineConstants;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.repository.RepositoryItem;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;
import atg.userprofiling.ProfileTools;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.CreateShippingQuoteResponseVO;
import com.cricket.integration.esp.vo.ESPResponseVO;
import com.cricket.vo.CricketProfile;
import com.cricket.vo.MyCricketCookieLocationInfo;

public class ProcProcessCreateShippingQuote extends GenericService implements PipelineProcessor {
  	  
	/** The profileTools. */
    private ProfileTools profileTools;

    /** The transactionManager. */
    private TransactionManager transactionManager;

    /** The orderManger. */
     
    private CricketESPAdapter cricketESPAdapter;

    /** Value SUCCESS return. */
    private static final int SUCCESS = 1;
    
    private static final String ESP_SUCCESS="0"; 
    /** holding order manager component */
    OrderManager orderManager;
    
  	
 	  /**
	   * 1 - The processor completed
	   * @return an integer array of the valid return codes.
	   */ 
	  public int[] getRetCodes()
	  {
	    int[] ret = {SUCCESS,PipelineProcessor.STOP_CHAIN_EXECUTION};
	    return ret;
	  }                  
	
	  /* (non-Javadoc)
	 * @see atg.service.pipeline.PipelineProcessor#runProcess(java.lang.Object, atg.service.pipeline.PipelineResult)
	 */
	public int runProcess(Object pParam, PipelineResult pResult) throws InvalidParameterException 
	  { 
		//logInfo("[ProcProcessCreateShippingQuote->runProcess()]: Entering into runProcess()...");
		
		long starttime	= 0L;
		long endtime 	= 0L;
		HashMap paramMap = (HashMap) pParam;
	    Object order = paramMap.get(PipelineConstants.ORDER);
	    
        if (order == null) {
            throw new InvalidParameterException("[ProcProcessCreateShippingQuote->runProcess()]: order Id is not valid");
        }
        CricketOrderImpl cricketOrder = (CricketOrderImpl) order;
        if (isLoggingDebug()) {	        	
	    		logDebug("Inside the ProcProcessCreateShippingQuote class of runProcess() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}
        Object accountHolderAddressDataObj = paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
        Object shippingAddressDataObj = paramMap.get(CricketESPConstants.SHIPPING_ADDRESS_DATA);
        Object profileItem = paramMap.get(CricketESPConstants.PROFILE_ITEM);
        Object locationInfoObj = paramMap.get(CricketESPConstants.LOCATION_INFO);
        Object cricketProfileDataObj = paramMap.get(CricketESPConstants.CRICKET_PROFILE);
             
        CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData)accountHolderAddressDataObj;
        CricketShippingAddressData  shippingAddressData = (CricketShippingAddressData)shippingAddressDataObj;
        MyCricketCookieLocationInfo locataionInfoData = (MyCricketCookieLocationInfo)locationInfoObj;
        RepositoryItem  profile = (RepositoryItem)profileItem;
        CricketProfile  cricketProfile = (CricketProfile)cricketProfileDataObj;
       
        logInfo("[ProcProcessCreateShippingQuote->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
        		accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
        		accountHolderAddressData.getAccountAddress().getPhoneNumber());
        try{ 
        	 if(isLoggingDebug()){
        		 	CricketOrderTools ordertools =(CricketOrderTools) getOrderManager().getOrderTools();
        	    	logDebug("[ProcProcessCreateShippingQuote->runProcess()]: Order Details :");
        	    	logDebug(ordertools.orderDetailsToTrack(cricketOrder,accountHolderAddressData.getAccountAddress(),this.getClass().getName())+ 
            	     		  "** CricketProfile Details : "+cricketProfile.toString()+
          		  			  "** LocationInfo Details : "+locataionInfoData.toString()+
           		  			  "** profile Details : "+ordertools.getProfileString(profile));
                 	}
        	starttime = System.currentTimeMillis();
	        CreateShippingQuoteResponseVO createShippingQuoteResponseVO =  getCricketESPAdapter().createShippingQuote(shippingAddressData, 
																								        				accountHolderAddressData,
																								        				cricketProfile, 
																								        				profile, 
																								        				locataionInfoData, 
																								        				cricketOrder);
      
		endtime = System.currentTimeMillis();		
		
		logInfo("[ProcProcessCreateShippingQuote->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));		
	 
		ESPResponseVO[] listOfResponse=  createShippingQuoteResponseVO.getResponseStatus();
		 
		if(!listOfResponse[0].getCode().equalsIgnoreCase(ESP_SUCCESS)){
			pResult.addError(listOfResponse[0].getCode(),listOfResponse[0].getDescription());
		} else {
			cricketOrder.setBillingQuoteId(createShippingQuoteResponseVO.getBillingQuoteId()); 
			cricketProfile.setAccountNumber(createShippingQuoteResponseVO.getBillingAccountNumber());
			cricketOrder.setCricCustmerBillingNumber(createShippingQuoteResponseVO.getBillingAccountNumber());
		}
		
		logInfo("[ProcProcessCreateShippingQuote->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
	    return SUCCESS;
	    
       }         
        catch (ServiceException se) {
        	vlogError("[ProcProcessCreateShippingQuote->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ServiceException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), se);
			getCricketESPAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, se, "ServiceException");
			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}		
		catch (ESPApplicationException eae) {
			vlogError("[ProcProcessCreateShippingQuote->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ESPApplicationException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId()   + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), eae);
			String errorCode = getCricketESPAdapter().getEspFaultCode(eae);					
			if(CricketESPConstants.ESP_TIMEOUT.equalsIgnoreCase(errorCode)){
				int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);	
				if(returnCode <= 0){
					getCricketESPAdapter().showErrorLogs(pResult, errorCode, errorCode, eae, "ESPApplicationException");
					//pResult.addError(errorCode,errorCode);
				}else{
					return returnCode;
				}
			} else if (errorCode.contains(CricketESPConstants.ESP_GNVOFFER_EXISTS_FOR_CUST)){
				boolean cancelBillingQuoteStatus = getCricketESPAdapter().cancelBillingQuoteStatus(cricketOrder);
				if(cancelBillingQuoteStatus){
					int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);
					if(returnCode <= 0){
						pResult.addError(CricketESPConstants.ESP_OFFEREXISTSFORCUST_EXCEPTION, CricketESPConstants.ESP_OFFEREXISTSFORCUST_EXCEPTION);
					}else{
						return returnCode;
					}
				}else{
					pResult.addError(CricketESPConstants.ESP_OFFEREXISTSFORCUST_EXCEPTION, CricketESPConstants.ESP_OFFEREXISTSFORCUST_EXCEPTION);
				}			
				
			}else{
				getCricketESPAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_BUSINESS_EXCEPTIONS, CricketESPConstants.ESP_BUSINESS_EXCEPTIONS, eae, "ESPApplicationException");
				//pResult.addError(CricketESPConstants.ESP_BUSINESS_EXCEPTIONS,CricketESPConstants.ESP_BUSINESS_EXCEPTIONS);
			}			
 		}
		catch (RemoteException re) {
			vlogError("[ProcProcessCreateShippingQuote->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " RemoteException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId()   + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), re);
			if(re.getCause() != null && re.getCause().getMessage() != null && re.getCause().getMessage().contains(CricketESPConstants.READ_TIMED_OUT)){
				int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);
				if(returnCode <= 0){
					getCricketESPAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_TIMEOUT, CricketESPConstants.ESP_TIMEOUT, re, "ESPApplicationException");
					//pResult.addError(CricketESPConstants.ESP_TIMEOUT,CricketESPConstants.ESP_TIMEOUT);
				}else{
					return returnCode;
				}
			}else{
				getCricketESPAdapter().showErrorLogs(pResult,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, re, "ESPApplicationException");

				//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
			}
		} catch (Exception e){
			vlogError("[ProcProcessCreateShippingQuote->runProcess()]:"  + CricketESPConstants.WHOOP_KEYWORD + " Exception while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId()   + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
			 getCricketESPAdapter().showErrorLogs(pResult,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, e, "ESPApplicationException");
			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}
		logInfo("[ProcProcessCreateShippingQuote->runProcess()]: Exiting runProcess(), with return STOP_CHAIN_EXECUTION...");
		return PipelineProcessor.STOP_CHAIN_EXECUTION;
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
		
}
 