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
import atg.repository.rql.RqlStatement;
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
import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.ESPResponseVO;
import com.cricket.integration.esp.vo.UpdateBillingQuoteResponseVO;
import com.cricket.util.EspServiceResponseData;

public class ProcProcessUpdateBillingQuote extends GenericService implements PipelineProcessor {
  	  
	/** The profileTools. */
    private ProfileTools profileTools;

    /** The transactionManager. */
    private TransactionManager transactionManager;

    /** The orderManger. */
    private OrderManager orderManager;
    
    private CricketESPAdapter cricketESPAdapter;
    
    private RqlStatement packageRQLStatement;

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
	public int runProcess(Object pParam, PipelineResult pResult) throws InvalidParameterException, CricketException 
	{ 
		//logInfo("[ProcProcessUpdateBillingQuote->runProcess()]: Entering into runProcess()...");
		
		long starttime	= 0L;
		long endtime 	= 0L;	
		HashMap paramMap = (HashMap) pParam;
        Object order = paramMap.get(PipelineConstants.ORDER);
        EspServiceResponseData serviceResponseData =  (EspServiceResponseData) paramMap.get(CricketESPConstants.ESP_SERVICE_RESPONSE_DATA);
              
        if (order == null) {
            throw new InvalidParameterException("[ProcProcessUpdateBillingQuote->runProcess()]: order Id is not valid");
        }
        
        if(null==serviceResponseData)
    	{
    		throw new CricketException("[ProcProcessUpdateBillingQuote->runProcess()]: There is no EspServiceResponseData ");
    	}
        CricketOrderImpl cricketOrder = (CricketOrderImpl) order;
        
        if (isLoggingDebug()) {			 					
	    		logDebug("Inside the ProcProcessUpdateBillingQuote class of runProcess() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}
        
        String orderType = cricketOrder.getWorkOrderType();
        Object accountHolderAddressDataObj = paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
        CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData)accountHolderAddressDataObj;
         logInfo("[ProcProcessUpdateBillingQuote->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
       			accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
       			accountHolderAddressData.getAccountAddress().getPhoneNumber());
        if(isLoggingDebug()){
        	 CricketOrderTools ordertools =(CricketOrderTools) getOrderManager().getOrderTools();
	    		logDebug("[ProcProcessUpdateBillingQuote->runProcess()]: Order Details :");
	    		logDebug(ordertools.orderDetailsToTrack(cricketOrder,null,this.getClass().getName()));
        	}
        try 
        {        
	        if(null!=orderType && orderType.equalsIgnoreCase(CricketPipleLineConstants.ORDERTYPE_RRC)){
	        	logInfo("[ProcProcessUpdateBillingQuote->runProcess()]: transaction type is RRC, so not calling this process and exiting with return SUCCESS...");
	        	return SUCCESS;
	        }
	        
	    	starttime = System.currentTimeMillis();
	    	updateBillingQuote(cricketOrder, pResult, serviceResponseData);  
	    	endtime = System.currentTimeMillis();	    	
	    	
	    	logInfo("[ProcProcessUpdateBillingQuote->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));		
			        
		    logInfo("[ProcProcessUpdateBillingQuote->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));   
			return SUCCESS;
        } 
        catch (ServiceException se) {
        	vlogError("[ProcProcessUpdateBillingQuote->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ServiceException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), se);
			pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}		
		catch (ESPApplicationException eae) {
			vlogError("[ProcProcessUpdateBillingQuote->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ESPApplicationException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), eae);
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
			vlogError("[ProcProcessUpdateBillingQuote->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " RemoteException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), re);
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
			vlogError("[ProcProcessUpdateBillingQuote->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Exception while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
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
	 * @param pServiceResponseData 
	 * @param pResult 
	 * @return
	 * @throws ServiceException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 * @throws ESPApplicationException 
	 */
	private boolean updateBillingQuote(CricketOrderImpl pCricketOrder, PipelineResult pPipeLineResult, EspServiceResponseData pServiceResponseData) throws ESPApplicationException, MalformedURLException, RemoteException, ServiceException {
		boolean status=true;		 
		UpdateBillingQuoteResponseVO responseVO;
		if (isLoggingDebug()) {			
    		logDebug("Inside the ProcProcessUpdateBillingQuote class of updateBillingQuote() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId());
		}
		try 
		{			
			responseVO = getCricketESPAdapter().updateBillingQuote(pCricketOrder, pServiceResponseData);
 			if(null!=responseVO){
				ESPResponseVO[] responseStatus = responseVO.getResponse();
				for(ESPResponseVO response: responseStatus){
					if(response.getCode().equalsIgnoreCase(ESP_SUCCESS)){
						pCricketOrder.setBillSysPaymentRefId(pCricketOrder.getVestaSystemOrderId());
						status=true;
					}
					else{
						pPipeLineResult.addError(response.getCode(), response.getDescription());
						status= false;
					}
				 }
			} else {//responseVO is null
				return false;
			}
			
		} catch (CricketException e) {
			vlogError("[ProcProcessUpdateBillingQuote->updateBillingQuote()]:" + CricketESPConstants.WHOOP_KEYWORD +  "CricketException Error while processing the order:" + "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  pCricketOrder.getId(), e);
			return false;
		}
		if (isLoggingDebug()) {			
    		logDebug("Exiting from ProcProcessUpdateBillingQuote class of updateBillingQuote() method..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pCricketOrder.getId() + "status::" +status);
		}
		return status;
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
	 * @return the packageRQLStatement
	 */
	public RqlStatement getPackageRQLStatement() {
		return packageRQLStatement;
	}

	/**
	 * @param pPackageRQLStatement the packageRQLStatement to set
	 */
	public void setPackageRQLStatement(RqlStatement pPackageRQLStatement) {
		packageRQLStatement = pPackageRQLStatement;
	}	
	
}
 