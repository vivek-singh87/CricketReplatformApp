package com.cricket.commerce.order.payment.processor;

import static com.cricket.common.constants.CricketESPConstants.ESP_RESPONSE_CODE_SUCCESS;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpSession;
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

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.ManageABPResponseVO;
import com.cricket.vo.CricketProfile;

public class ProcProcessManageABP extends GenericService implements PipelineProcessor {
  	  
	  private final int SUCCESS = 1;
	
	  /* holds espAdapter instance to communicate ESP Layer */
	  private CricketESPAdapter espAdapter;
	    /** The orderManger. */
	    private OrderManager orderManager;
		
		/** get the CricketESPAdapter instance.
		 * @return
		 */
		public CricketESPAdapter getEspAdapter() {
			return espAdapter;
		}

		/** set the CricketESPAdapter instance.
		 * @param espAdapter
		 */
		public void setEspAdapter(CricketESPAdapter espAdapter) {
			this.espAdapter = espAdapter;
		}
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
	@SuppressWarnings("rawtypes")
	public int runProcess(Object pParam, PipelineResult pResult) throws InvalidParameterException
	  { 
		logInfo("[ProcProcessManageABP->runProcess()]: Entering into runProcess()...");
		
		long starttime	= 0L;
		long endtime 	= 0L;	
				 	 
	    HashMap paramMap = (HashMap) pParam;
        Object order = paramMap.get(PipelineConstants.ORDER);
        if (order == null) {
            throw new InvalidParameterException("[ProcProcessManageABP->runProcess()]: order Id is not valid");
        }
        CricketOrderImpl cricketOrder = (CricketOrderImpl) order;        
        if (isLoggingDebug()) {			 					
	    		logDebug("Entering into ProcProcessManageABP class of runProcess() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}       
        Object profileItem = paramMap.get(CricketESPConstants.PROFILE_ITEM);
        Object cricketProfileDataObj = paramMap.get(CricketESPConstants.CRICKET_PROFILE);
                  
        RepositoryItem  profile = (RepositoryItem)profileItem;
        CricketProfile  cricketProfile = (CricketProfile)cricketProfileDataObj;
        CricketOrderTools ordertools =(CricketOrderTools) getOrderManager().getOrderTools();
        Object accountHolderAddressDataObj = paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
        CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData)accountHolderAddressDataObj;
         logInfo("[ProcProcessManageABP->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
       			accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
       			accountHolderAddressData.getAccountAddress().getPhoneNumber());
 		try {
 			 if(isLoggingDebug()){
 				
       	    	logDebug("[ProcProcessManageABP->runProcess()]: Order Details :");
       	    	logDebug(ordertools.orderDetailsToTrack(cricketOrder,null,this.getClass().getName())+ 
       	    	  "** cricketProfile Details : "+cricketProfile.toString()+
         		  		 "** profile Details : "+ordertools.getProfileString(profile));
                	}
 			starttime = System.currentTimeMillis(); 
			ManageABPResponseVO manageABPResponseVO = getEspAdapter().manageABP(cricketOrder,profile, cricketProfile);
		 	endtime = System.currentTimeMillis();			
		 	
		 	logInfo("[ProcProcessManageABP->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));		 	
		 	
		 	if(manageABPResponseVO != null && manageABPResponseVO.getResponse() != null && ESP_RESPONSE_CODE_SUCCESS.equals(manageABPResponseVO.getResponse().getCode())){
		 		logInfo("[ProcProcessManageABP->runProcess()]: Exiting runProcess(), with return SUCCESS...");		 	
				return SUCCESS;		 		
		 	}else{
		 		if(manageABPResponseVO != null && manageABPResponseVO.getResponse() != null){
		 			logError("[ProcProcessManageABP->runProcess()]: Vesta error code: "+ manageABPResponseVO.getResponse().getCode()+" for orderId: "+cricketOrder.getId());
		 			logError("[ProcProcessManageABP->runProcess()]: Unable to enroll to ABP: response code: "+manageABPResponseVO.getResponse().getCode()+ " response description: " + manageABPResponseVO.getResponse().getDescription());
		 		}		 		
		 		pResult.addError(CricketESPConstants.ESP_BUSINESS_EXCEPTIONS,CricketESPConstants.ESP_BUSINESS_EXCEPTIONS);
		 	}
		 	
	        logInfo("[ProcProcessManageABP->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getEspAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
	        return SUCCESS;
		} 
 		catch (ServiceException se) {
 			if (isLoggingError()) {
      		  vlogError("[ProcProcessManageABP->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + " ServiceException while getting response:" + "ConversationId: "+ getEspAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), se);
            }
			getEspAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, se, "ServiceException");
			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}		
		catch (ESPApplicationException eae) {
			if (isLoggingError()) {
	      		  vlogError("[ProcProcessManageABP->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + " ESPApplicationException while getting response:" + "ConversationId: "+getEspAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), eae);
	            }
			String errorCode = getEspAdapter().getEspFaultCode(eae);
			if(CricketESPConstants.ESP_TIMEOUT.equalsIgnoreCase(errorCode)){
				int returnCode = getEspAdapter().retry(pParam, pResult, cricketOrder, this);	
				if(returnCode <= 0){
					getEspAdapter().showErrorLogs(pResult, errorCode, errorCode, eae, "ESPApplicationException");
				//	pResult.addError(errorCode,errorCode);
				}else{
					return returnCode;
				}
			} else{
				vlogError("[ProcProcessManageABP->runProcess()]: ESPApplicationException while getting response - Vesta Error Code :"+errorCode);
				getEspAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_BUSINESS_EXCEPTIONS, CricketESPConstants.ESP_BUSINESS_EXCEPTIONS, eae, "ESPApplicationException");
				//pResult.addError(CricketESPConstants.ESP_BUSINESS_EXCEPTIONS,CricketESPConstants.ESP_BUSINESS_EXCEPTIONS);
			}
 		}
		catch (RemoteException re) {
			if (isLoggingError()) {
	      		  vlogError("[ProcProcessManageABP->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + " RemoteException while getting response:" + "ConversationId: "+getEspAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), re);
	            }
			if(re.getCause() != null && re.getCause().getMessage() != null && re.getCause().getMessage().contains(CricketESPConstants.READ_TIMED_OUT)){
				int returnCode = getEspAdapter().retry(pParam, pResult, cricketOrder, this);
				if(returnCode <= 0){
					getEspAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_TIMEOUT, CricketESPConstants.ESP_TIMEOUT, re, "ESPApplicationException");
					//pResult.addError(CricketESPConstants.ESP_TIMEOUT,CricketESPConstants.ESP_TIMEOUT);
				}else{
					return returnCode;
				}
			}else{
				getEspAdapter().showErrorLogs(pResult,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, re, "ESPApplicationException");
			//	pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
			}
		} catch (Exception e){
			if (isLoggingError()) {
	      		  vlogError("[ProcProcessManageABP->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + " Exception while getting response:" + "ConversationId: "+getEspAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
	            }
			getEspAdapter().showErrorLogs(pResult,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, e, "ESPApplicationException");
			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
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
	
}
 