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
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.ManagePaymentResponseVO;
import com.cricket.integration.esp.vo.ResponseVO;

public class ProcProcessManagePaymentRRC extends GenericService implements PipelineProcessor {
  	  
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
	    int[] ret = {SUCCESS,PipelineProcessor.STOP_CHAIN_EXECUTION};
	    return ret;
	  }                  
	
	  /* (non-Javadoc)
	 * @see atg.service.pipeline.PipelineProcessor#runProcess(java.lang.Object, atg.service.pipeline.PipelineResult)
	 */
	public int runProcess(Object pParam, PipelineResult pResult) throws InvalidParameterException  
	  { 
		//logInfo("[ProcProcessManagePaymentRRC->runProcess()]: Entering into runProcess()...");
		long starttime	= 0L;
		long endtime 	= 0L; 	 
	    
	    HashMap paramMap = (HashMap) pParam;
        Object order = paramMap.get(PipelineConstants.ORDER);
       
        if (order == null) {
            throw new InvalidParameterException("[ProcProcessManagePaymentRRC->runProcess()]: order Id is not valid");
        }
        CricketOrderImpl cricketOrder = (CricketOrderImpl) order;
        try
        {
	        String orderType = cricketOrder.getWorkOrderType();
	        if(orderType.equalsIgnoreCase(CricketPipleLineConstants.ORDERTYPE_RRC) && cricketOrder.getPriceInfo().getTotal()>0){

	        Object accountHolderAddressDataObj = paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
	        CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData)accountHolderAddressDataObj;
	         logInfo("[ProcProcessManagePaymentRRC->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
	       			accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
	       			accountHolderAddressData.getAccountAddress().getPhoneNumber());
 	        	starttime = System.currentTimeMillis();
	        	 if(isLoggingDebug()){
	        		 CricketOrderTools ordertools =(CricketOrderTools) getOrderManager().getOrderTools();
	    	    		logDebug("[ProcProcessManagePaymentRRC->runProcess()]: Order Details :");
	    	    		logDebug(ordertools.orderDetailsToTrack(cricketOrder,null,this.getClass().getName()));
	    	    }
	        	 ManagePaymentResponseVO responseVO = managePayment(cricketOrder);
	        	 endtime = System.currentTimeMillis();
	        	 
	        	 logInfo("[ProcProcessManagePaymentRRC->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));
	 			
	             ResponseVO responseStatus = responseVO.getResponse();
	             if(!responseStatus.getCode().equalsIgnoreCase(ESP_SUCCESS)){
			 			logError("[ProcProcessManagePaymentRRC->runProcess()]: Vesta error code: "+ responseStatus.getCode()+" for orderId: "+cricketOrder.getId());

	             	pResult.addError(responseStatus.getCode(), responseStatus.getCode());
	             	return PipelineProcessor.STOP_CHAIN_EXECUTION;
	             }
	             logInfo("[ProcProcessManagePaymentRRC->runProcess()]: Exiting runProcess(), if Order Type equals RRC ::return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
	          } 	 
	        return SUCCESS;
        } 
        catch (ServiceException se) {
        	vlogError("[ProcProcessManagePaymentRRC->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ServiceException while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), se);
			pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}		
		catch (ESPApplicationException eae) {
			vlogError("[ProcProcessManagePaymentRRC->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ESPApplicationException while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), eae);
			String errorCode = getCricketESPAdapter().getEspFaultCode(eae);
			if(CricketESPConstants.ESP_TIMEOUT.equalsIgnoreCase(errorCode)){
				int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);	
				if(returnCode <= 0){
					pResult.addError(errorCode,errorCode);
				}else{
					return returnCode;
				}
			} else{
				vlogError("[ProcProcessManagePaymentRRC->runProcess()]: ESPApplicationException while getting response - Vesta Error Code :"+errorCode);
				pResult.addError(CricketESPConstants.ESP_BUSINESS_EXCEPTIONS,CricketESPConstants.ESP_BUSINESS_EXCEPTIONS);
			}
 		}
		catch (RemoteException re) {
			vlogError("[ProcProcessManagePaymentRRC->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " RemoteException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), re);
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
			vlogError("[ProcProcessManagePaymentRRC->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Exception while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
			pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}
		return PipelineProcessor.STOP_CHAIN_EXECUTION; 
		//return SUCCESS;
	  }
	
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	
	private ManagePaymentResponseVO managePayment(CricketOrderImpl pCricketOrder) throws ESPApplicationException, RemoteException, MalformedURLException, ServiceException {		 
		
		if (isLoggingDebug()) {			
    		logDebug("Inside the ProcProcessManagePaymentRRC class of managePayment() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId());
		}
		ManagePaymentResponseVO response=null;
		response = getCricketESPAdapter().managePaymnetRRC(pCricketOrder);
		if (isLoggingDebug()) {			
    		logDebug("Exiting from ProcProcessManagePaymentRRC class of managePayment() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId() + "response::" +response);
		}
		return response;		
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
 