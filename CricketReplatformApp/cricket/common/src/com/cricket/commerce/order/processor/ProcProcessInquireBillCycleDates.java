package com.cricket.commerce.order.processor;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.transaction.TransactionManager;

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
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.integration.esp.CricketESPAdapter;

public class ProcProcessInquireBillCycleDates extends GenericService implements PipelineProcessor {
  	  
	/** The profileTools. */
    private ProfileTools profileTools;

    /** The transactionManager. */
    private TransactionManager transactionManager;

    /** The orderManger. */
    private OrderManager orderManager;
    
    private CricketESPAdapter cricketESPAdapter;

    /** Value SUCCESS return. */
    private static final int SUCCESS = 1;
		
 	  /**
	   * 1 - The processor completed
	   * @return an integer array of the valid return codes.
	   */ 
	  public int[] getRetCodes()
	  {
	    int[] ret = {SUCCESS};
	    return ret;
	  }                  
	
	  /* (non-Javadoc)
	 * @see atg.service.pipeline.PipelineProcessor#runProcess(java.lang.Object, atg.service.pipeline.PipelineResult)
	 */
	public int runProcess(Object pParam, PipelineResult pPipelineresult) throws Exception
	  { 
		//logInfo("[ProcProcessInquireBillCycleDates->runProcess()]: Entering into runProcess()...");
		
		long starttime	= 0L;
		long endtime 	= 0L;	    
	    boolean result = false;
		HashMap paramMap = (HashMap) pParam;
		Object order = paramMap.get(PipelineConstants.ORDER);
		
		if (order == null) {
		    throw new InvalidParameterException("[ProcProcessInquireBillCycleDates->runProcess()]: INVALID ORDER ID PARAMETER");
		}
		 Object accountHolderAddressDataObj = paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
		 CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData)accountHolderAddressDataObj;
		/*  logInfo("[ProcProcessInquireBillCycleDates->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
					accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
					accountHolderAddressData.getAccountAddress().getPhoneNumber());*/
		CricketOrderImpl mtnOrderImpl = (CricketOrderImpl) order;
		
        if (isLoggingDebug()) {			 					
	    		logDebug("Inside the ProcProcessInquireBillCycleDates class of runProcess() method :::"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + mtnOrderImpl.getId());
		}
		starttime = System.currentTimeMillis();
		result = inquireBillCycleDateService(mtnOrderImpl);
		endtime = System.currentTimeMillis();

		
		logInfo("[ProcProcessInquireBillCycleDates->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));
		
		
		if (result) {
			if (isLoggingDebug()) {			 					
	    		logDebug("[ProcProcessInquireBillCycleDates->runProcess()]: Exiting runProcess(), with return success..." + result);
	        }
			logInfo("[ProcProcessInquireBillCycleDates->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(mtnOrderImpl) + result);
		    return SUCCESS;
		} else {
			//logError("[ProcProcessInquireBillCycleDates->runProcess()]: Error in ESP:");
			pPipelineresult.addError(CricketCommonConstants.EMPTY_STRING, "error msg");
			if (isLoggingDebug()) {			 					
	    		logDebug("[ProcProcessInquireBillCycleDates->runProcess()]: Exiting runProcess(), with return Error message...");
	        }
		}
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
	
	private boolean inquireBillCycleDateService(CricketOrderImpl pMtnOrderImpl) {
	
		//getCricketESPAdapter().updateBillingQuote(pBillingQuoteRequest)
		return false;
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
	 * @return the success
	 */
	public static int getSuccess() {
		return SUCCESS;
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
 