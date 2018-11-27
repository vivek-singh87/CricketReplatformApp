package com.cricket.commerce.order.processor.esp;

import java.math.BigDecimal;
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

public class ProcProcessAssignSaleTax extends GenericService implements PipelineProcessor {
  	  
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
	public int runProcess(Object pParam, PipelineResult pResult) throws Exception
	  { 
		//logInfo("[ProcProcessAssignSaleTax->runProcess()]: Entering into runProcess()...");
		
		double totalTax = 0.0;	 
	    HashMap paramMap = (HashMap) pParam;
        Object order = paramMap.get(PipelineConstants.ORDER);
          if (order == null) {
            throw new InvalidParameterException("[ProcProcessAssignSaleTax->runProcess()]: order Id is not valid");
        }
        CricketOrderImpl cricketOrder = (CricketOrderImpl) order;
        if (isLoggingDebug()) {		
	    		logDebug("Inside the ProcProcessAssignSaleTax class of runProcess() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}
        Object accountHolderAddressDataObj = paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
        CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData)accountHolderAddressDataObj;
         logInfo("[ProcProcessAssignSaleTax->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
       			accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
       			accountHolderAddressData.getAccountAddress().getPhoneNumber());
        BigDecimal  serviceTaxObj =(BigDecimal) paramMap.get(CricketESPConstants.SERVICE_TAX);
        double salesTaxObj = (Double)paramMap.get(CricketESPConstants.SALES_TAX);
        
        if(isLoggingDebug()){
         	 logDebug("[ProcProcessAssignSaleTax->runProcess()]: SalesTax - for Order id: "+cricketOrder.getId()+" is : "+salesTaxObj);
        }
        
	    if(serviceTaxObj!=null){

	        if(isLoggingDebug()){
	        	 logDebug("[ProcProcessAssignSaleTax->runProcess()]: ServiceTax - for Order id: "+cricketOrder.getId()+" is : "+serviceTaxObj.doubleValue());
 	        }
	    	totalTax = serviceTaxObj.doubleValue()+salesTaxObj;
	    } else {
		   totalTax =salesTaxObj;
	    }
	    
       cricketOrder.setTotalTax(totalTax);
       logInfo("[ProcProcessAssignSaleTax->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
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
 