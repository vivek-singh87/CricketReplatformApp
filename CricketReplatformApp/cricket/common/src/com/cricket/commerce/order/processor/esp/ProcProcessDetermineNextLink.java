package com.cricket.commerce.order.processor.esp;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.PipelineConstants;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.DeliveryServiceVO;
import com.cricket.integration.esp.vo.InquireDeliveryEstimateResponseVO;
import com.cricket.integration.esp.vo.ResponseVO;

import static com.cricket.commerce.order.processor.CricketPipleLineConstants.*;

/**
 * @author TechM
 * Description: This processor is used to determine the next chain execute, if order is downgrade invoke update subcriber otherwise invoke sendMessage
 */
public class ProcProcessDetermineNextLink extends GenericService
		implements PipelineProcessor {
	/** holding CricketESPAdapter component. */
	private CricketESPAdapter cricketESPAdapter;
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

    /** Value SUCCESS return. */
    private static final int SUCCESS = 1;
	/* holds esp sucess value */
	private static final String ESP_SUCCESS = "0";
	/* holds return value for UPDATE_SUBSCRIBER esp call */
	private static final int UPDATE_SUBSCRIBER = 2;
	/* holds return value for SEND_PROTOMOTIONAL_MESSAGE esp call */
	private static final int SEND_PROTOMOTIONAL_MESSAGE = 1;
	/* holds return value for DELIVERY_ESTIMATE_DATE esp call */
	private static final int DELIVERY_ESTIMATE_DATE=3;
 	/**
	 * 0 - call stop chain.
	 * 1 - call SendMessage ESP
	 * 2 - call UpdateSubscriber ESP
	 * 3 - call InquireDeliveryEstimate ESP
 	 * @return an integer array of the valid return codes.
	 */
	public int[] getRetCodes() {
		int[] ret = {PipelineProcessor.STOP_CHAIN_EXECUTION , UPDATE_SUBSCRIBER, SEND_PROTOMOTIONAL_MESSAGE,DELIVERY_ESTIMATE_DATE };
		return ret;
	}

	/**
	 * @author TechM
	 * Description: This processor is used to determine the next chain execute, if order is downgrade invoke update subcriber otherwise invoke sendMessage
	 */
	public int runProcess(Object pParam, PipelineResult pResult)
			throws InvalidParameterException {
		//logInfo("[ProcProcessDetermineNextLink->runProcess()]: Entering into runProcess()...");
 		HashMap<?,?> paramMap = (HashMap<?,?>) pParam;
		Object order = paramMap.get(PipelineConstants.ORDER);
		boolean emailNotification = Boolean.FALSE;
		String workOrderType = null;
		if (order == null) {
			throw new InvalidParameterException("[ProcProcessDetermineNextLink->runProcess()]: order Id is not valid");
		}
		CricketOrderImpl cricketOrder = (CricketOrderImpl) order;
		if (isLoggingDebug()) {						
	    		logDebug("Inside the ProcProcessDetermineNextLink class of runProcess() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}
		workOrderType = cricketOrder.getWorkOrderType();
        CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData) paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
        if(null!=accountHolderAddressData){
          	  logInfo("[ProcProcessDetermineNextLink->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
        				accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
        				accountHolderAddressData.getAccountAddress().getPhoneNumber());
          emailNotification=accountHolderAddressData.isEmailnotification();
        }
        
 		try {
			 	if(cricketOrder.isDownGrade()){
			 		if(isLoggingDebug()){
			 			logDebug("ProcProcessDetermineNextLink->runProcess() :: Order is Downgrade");
			 		}
			 		return UPDATE_SUBSCRIBER;
			 	}else if(emailNotification) {
			 		if(isLoggingDebug()){
			 			logDebug("ProcProcessDetermineNextLink->runProcess() :: Order is "+cricketOrder.getWorkOrderType());
			 		}
			 		return SEND_PROTOMOTIONAL_MESSAGE;
			 	}if (CricketESPConstants.TRANSACTION_TYPE_ACT.equalsIgnoreCase(workOrderType) || 
			 			CricketESPConstants.TRANSACTION_TYPE_ADD.equalsIgnoreCase(workOrderType) ||
			 			CricketESPConstants.TRANSACTION_TYPE_OXC.equalsIgnoreCase(workOrderType) ||
			 			CricketESPConstants.TRANSACTION_TYPE_OUP.equalsIgnoreCase(workOrderType)) {
					return DELIVERY_ESTIMATE_DATE;
				} else if (CricketESPConstants.TRANSACTION_TYPE_RRC.equalsIgnoreCase(workOrderType)) {
					return UPDATE_SUBSCRIBER;
				}  
				 
			 	logInfo("[ProcProcessDetermineNextLink->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
			    return SUCCESS;
			}catch(Exception e){
				vlogError("ProcProcessDetermineNextLink->runProcess()"  + CricketESPConstants.WHOOP_KEYWORD + "Exception while checking the order type:" + "ConversationId: " + getCricketESPAdapter().getConversationId()   + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
		}
		logInfo("[ProcProcessDetermineNextLink->runProcess()]: Exiting runProcess()::");
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
	
}
