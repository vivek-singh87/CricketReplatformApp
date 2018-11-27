package com.cricket.commerce.order.processor;

import java.net.MalformedURLException;
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
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;
import atg.userprofiling.ProfileTools;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.esp.ESP.Namespaces.Container.Public.SendMessageResponse_xsd.SendMessageResponseInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.CompleteSaleResponseVO;
import com.cricket.integration.esp.vo.ResponseVO;

public class ProcProcessCompleteSale extends GenericService implements PipelineProcessor {
  	  
	/** The profileTools. */
    private ProfileTools profileTools;

    /** The transactionManager. */
    private TransactionManager transactionManager;

    /** The orderManger. */
    private OrderManager orderManager;
    
    private CricketESPAdapter cricketESPAdapter;

    /** Value SUCCESS return. */
    private static final int SUCCESS = 1;
    
    private static final String ESP_SUCCESS="0";
		
 	  /**
	   * 1 - The processor completed
	   * @return an integer array of the valid return codes.
	   */ 
	  public int[] getRetCodes()
	  {
	    int[] ret = {SUCCESS, PipelineProcessor.STOP_CHAIN_EXECUTION};
	    return ret;
	  }                  
	
	  /* (non-Javadoc)
	 * @see atg.service.pipeline.PipelineProcessor#runProcess(java.lang.Object, atg.service.pipeline.PipelineResult)
	 */
	public int runProcess(Object pParam, PipelineResult pResult) throws InvalidParameterException  
	  {
		long starttime	= 0L;
		long endtime 	= 0L;	
	    HashMap paramMap = (HashMap) pParam;
        Object order = paramMap.get(PipelineConstants.ORDER);
        if (order == null) {
            throw new InvalidParameterException("[ProcProcessCompleteSale->runProcess()]: order Id is not valid");
        }	       
        CricketOrderImpl cricketOrder = (CricketOrderImpl) order;
        if (isLoggingDebug()) {			 					
	    		logDebug("Inside the ProcProcessCompleteSale class of runProcess() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}
	    try{		  
	    	if(isLoggingDebug()){
	    		 CricketOrderTools ordertools =(CricketOrderTools) getOrderManager().getOrderTools();
  	    		logDebug("[ProcProcessCompleteSale->runProcess()]: Order Details :");
  	    		logDebug(ordertools.orderDetailsToTrack(cricketOrder,null,this.getClass().getName()));
  	    }
	        String orderType = cricketOrder.getWorkOrderType();
	        Object accountHolderAddressDataObj = paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
	        CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData)accountHolderAddressDataObj;
	       
	        if(CricketESPConstants.TRANSACTION_TYPE_RRC.equalsIgnoreCase(orderType)){
	        	  logInfo("[ProcProcessCompleteSale->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
	  	       			accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
	  	       			accountHolderAddressData.getAccountAddress().getPhoneNumber());
	        	starttime = System.currentTimeMillis();
	        	CompleteSaleResponseVO responseVO = completeSale(cricketOrder);
	        	endtime = System.currentTimeMillis();
	        	logInfo("[ProcProcessCompleteSale->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));
	            ResponseVO responseStatus = responseVO.getResponse();
	            if(!responseStatus.getCode().equalsIgnoreCase(ESP_SUCCESS)){
	            	pResult.addError(responseStatus.getCode(), responseStatus.getDescription());
	            	logInfo("[ProcProcessCompleteSale->runProcess()]: Exiting runProcess(), with return STOP_CHAIN_EXECUTION...");
	            	return PipelineProcessor.STOP_CHAIN_EXECUTION;
	            }
	            else{
	            	if(isLoggingDebug()){
	            		logDebug("Invoking sendMessage for RRC flow "+getClass().getName());
	            	}
	            	/* starttime = System.currentTimeMillis();
	            	SendMessageResponseInfo response = getCricketESPAdapter().sendMessageRRC(cricketOrder, accountHolderAddressData.getAccountAddress().getEmail());	
	            	endtime = System.currentTimeMillis();*/
		        	logInfo("[ProcProcessCompleteSale.sendMessageRRC()->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));
	            }
	        }
	        logInfo("[ProcProcessCompleteSale->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
		    return SUCCESS;
	   
	  } catch (ServiceException se) {
		  	vlogError("[ProcProcessCompleteSale->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + "ServiceException while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), se);
			pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}		
		catch (ESPApplicationException eae) {
			vlogError("[ProcProcessCompleteSale->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + "ESPApplicationException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), eae);
			String errorCode = getCricketESPAdapter().getEspFaultCode(eae);
			if(CricketESPConstants.ESP_TIMEOUT.equalsIgnoreCase(errorCode)){
				int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);	
				if(returnCode <= 0){
					pResult.addError(errorCode,errorCode);
				}else{
					return returnCode;
				}
			} else{
				pResult.addError(CricketESPConstants.ESP_BUSINESS_EXCEPTIONS,CricketESPConstants.ESP_BUSINESS_EXCEPTIONS);
			}
 		}
		catch (RemoteException re) {
			vlogError("[ProcProcessCompleteSale->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + "RemoteException while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), re);
			if(re.getCause() != null && re.getCause().getMessage() != null && re.getCause().getMessage().contains(CricketESPConstants.READ_TIMED_OUT)){
				int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);
				if(returnCode <= 0){
					pResult.addError(CricketESPConstants.ESP_TIMEOUT,CricketESPConstants.ESP_TIMEOUT);
				}else{
					return returnCode;
				}
			}else{
				pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
			}
		} catch (Exception e){
			vlogError("[ProcProcessCompleteSale->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + "Exception while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
			pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}

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
	 * @param pCricketOrder
	 * @return 
	 * @throws CricketException
	 * @throws ServiceException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 * @throws ESPApplicationException 
	 */
	private CompleteSaleResponseVO completeSale(CricketOrderImpl pCricketOrder) throws CricketException, ESPApplicationException, MalformedURLException, RemoteException, ServiceException {		
				
		if (isLoggingDebug()) {			
    		logDebug("Inside the ProcProcessCompleteSale class of completeSale() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId());
		}
		CompleteSaleResponseVO compleSaleResponse = getCricketESPAdapter().completeSale(pCricketOrder);
		if (isLoggingDebug()) {			
    		logDebug("Exiting from ProcProcessCompleteSale class of completeSale() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId());
		}
		return compleSaleResponse;	
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
 