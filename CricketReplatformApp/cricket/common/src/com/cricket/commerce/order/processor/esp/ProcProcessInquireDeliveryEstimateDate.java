package com.cricket.commerce.order.processor.esp;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.OrderManager;
import atg.commerce.order.PipelineConstants;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
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
 * Description: This processor is used to invoke the InquireDeliveryEstimateDate ESP call to get different delivery dates for order
 * based shipping method, appropriate shipping date will be picked from response and set order's deliver date property.
 */
public class ProcProcessInquireDeliveryEstimateDate extends GenericService
		implements PipelineProcessor {
	/** holding CricketESPAdapter component. */
	private CricketESPAdapter cricketESPAdapter;
    /** Value SUCCESS return. */
    private static final int SUCCESS = 1;
	private static final String ESP_SUCCESS = "0";
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
	 * 0 - call stop chain.
	 * 1 - call createActivationQuote ESP
	 * 2 - call UpdateSubscriber ESP
	 * 3 - call createShippingQuote ESP
	 * @return an integer array of the valid return codes.
	 */
	public int[] getRetCodes() {
		int[] ret = { CREATE_ACTIVATION_QUOTE, UPDATE_SUBSCRIBER,
				CREATE_SHIPPING_QUOTE, PipelineProcessor.STOP_CHAIN_EXECUTION };
		return ret;
	}

	/**
	 * @author TechM
	 * Description: This processor is used to invoke the InquireDeliveryEstimateDate ESP call to get different delivery dates for order
	 * based shipping method, appropriate shipping date will be picked from response and set order's deliver date property.
	 */
	public int runProcess(Object pParam, PipelineResult pResult)
			throws InvalidParameterException {
	//	logInfo("[ProcProcessInquireDeliveryEstimateDate->runProcess()]: Entering into runProcess()...");
		
		long starttime = 0L;
		long endtime = 0L;
		String orderType = null;
		HashMap paramMap = (HashMap) pParam;
		Object order = paramMap.get(PipelineConstants.ORDER);
		
		if (order == null) {
			throw new InvalidParameterException("[ProcProcessInquireDeliveryEstimateDate->runProcess()]: order Id is not valid");
		}

		Object shippingAddressDataObj = paramMap.get(CricketESPConstants.SHIPPING_ADDRESS_DATA);
		CricketOrderImpl cricketOrder = (CricketOrderImpl) order;
		if (isLoggingDebug()) {				
	    		logDebug("Inside the ProcProcessInquireDeliveryEstimateDate class of runProcess() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}
        CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData) paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
		CricketShippingAddressData shippingAddressData = (CricketShippingAddressData) shippingAddressDataObj;
		
		if (accountHolderAddressData == null)
		      throw new InvalidParameterException(CRICKET_ITEM_DISPLAYNAME);
       if (shippingAddressData == null)
		      throw new InvalidParameterException(CRICKET_SHIPPING_ADDRESS_DATA);
		orderType = cricketOrder.getWorkOrderType();
  		  logInfo("[ProcProcessInquireDeliveryEstimateDate->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
					accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
					accountHolderAddressData.getAccountAddress().getPhoneNumber());
		try {
			 if(isLoggingDebug()){
				 CricketOrderTools ordertools =(CricketOrderTools) getOrderManager().getOrderTools();
     	    	logDebug("[ProcProcessInquireDeliveryEstimateDate->runProcess()]: Order Details :");
     	    	logDebug(ordertools.orderDetailsToTrack(cricketOrder,accountHolderAddressData.getAccountAddress(),this.getClass().getName())); 
         	   
	        }
			starttime = System.currentTimeMillis();
			InquireDeliveryEstimateResponseVO responseVO = getCricketESPAdapter().inquireDeliveryEstimate(shippingAddressData.getShippingAddress().getPostalCode(),
																											shippingAddressData.getShippingAddress().getCountry(), cricketOrder.getId());
			endtime = System.currentTimeMillis();
			
			logInfo("[ProcProcessInquireDeliveryEstimateDate->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));			

			ResponseVO responseStatus = responseVO.getResponse();
			if (!responseStatus.getCode().equalsIgnoreCase(ESP_SUCCESS)) {
				pResult.addError(responseStatus.getCode(),
						responseStatus.getDescription());
			} else {
					
				String serviceCode = DELIVERY_SERVICE_CODE_UPS_NEXT_DAY_AIR_SAVER; 
				String shippingMethod = cricketOrder.getShippingMethod();
				// user entered po box address?	
				if(shippingMethod != null && CricketCommonConstants.POBOX.equals(shippingMethod)){				
					serviceCode = DELIVERY_SERVICE_CODE_UPS_GROUND;
				}				
			
				List<DeliveryServiceVO> deliveryServiceList = responseVO.getDeliveryService();
				if (deliveryServiceList != null && deliveryServiceList.size() > 0) {
					Date estimatedDeliveryDate = null;
					for (DeliveryServiceVO deliveryServiceVO : deliveryServiceList) {
						if (serviceCode.equals(deliveryServiceVO.getServiceCode())) {
							estimatedDeliveryDate = deliveryServiceVO.getEstimatedDeliveryDate();
							break;
						}
					}
					if (estimatedDeliveryDate != null) {
						cricketOrder.setEstimatedDeliveryDate(estimatedDeliveryDate);
					}
				}
				
				logInfo("[ProcProcessInquireDeliveryEstimateDate->runProcess()]: Exiting runProcess(), with return SUCCESS...");
				
				if (CricketESPConstants.TRANSACTION_TYPE_ACT.equalsIgnoreCase(orderType) || CricketESPConstants.TRANSACTION_TYPE_ADD.equalsIgnoreCase(orderType)) {
					return CREATE_ACTIVATION_QUOTE;
				} else if (CricketESPConstants.TRANSACTION_TYPE_RRC.equalsIgnoreCase(orderType)) {
					return UPDATE_SUBSCRIBER;
				} else if (CricketESPConstants.TRANSACTION_TYPE_OXC.equalsIgnoreCase(orderType) || CricketESPConstants.TRANSACTION_TYPE_OUP.equalsIgnoreCase(orderType)) {
					return CREATE_SHIPPING_QUOTE;
				}
			}
		     logInfo("[ProcProcessInquireDeliveryEstimateDate->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
		     return SUCCESS;	
		} 		
		catch (ServiceException se) {
			vlogError("[ProcProcessInquireDeliveryEstimateDate->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ServiceException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), se);
			getCricketESPAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, se, "ServiceException");
			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}		
		catch (ESPApplicationException eae) {
			vlogError("[ProcProcessInquireDeliveryEstimateDate->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ESPApplicationException while getting response:::" + "ConversationId: " + getCricketESPAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), eae);
			String errorCode = getCricketESPAdapter().getEspFaultCode(eae);
			if(CricketESPConstants.ESP_TIMEOUT.equalsIgnoreCase(errorCode)){
				int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);	
				if(returnCode <= 0){
					getCricketESPAdapter().showErrorLogs(pResult, errorCode, errorCode, eae, "ESPApplicationException");

					//pResult.addError(errorCode,errorCode);
				}else{
					return returnCode;
				}
			} else{
				getCricketESPAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_BUSINESS_EXCEPTIONS, CricketESPConstants.ESP_BUSINESS_EXCEPTIONS, eae, "ESPApplicationException");

			//	pResult.addError(CricketESPConstants.ESP_BUSINESS_EXCEPTIONS,CricketESPConstants.ESP_BUSINESS_EXCEPTIONS);
			}
		}
		catch (RemoteException re) {
			vlogError("[ProcProcessInquireDeliveryEstimateDate->runProcess()]:"+ CricketESPConstants.WHOOP_KEYWORD +" RemoteException while getting response::::" + "ConversationId: " + getCricketESPAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), re);
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
			vlogError("[ProcProcessInquireDeliveryEstimateDate->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " Exception while getting response::::" + "ConversationId: " + getCricketESPAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
			getCricketESPAdapter().showErrorLogs(pResult,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, e, "ESPApplicationException");

			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}
		logInfo("[ProcProcessInquireDeliveryEstimateDate->runProcess()]: Exiting runProcess()::");
		
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
	 * @return the cricketESPAdapter
	 */
	public CricketESPAdapter getCricketESPAdapter() {
		return cricketESPAdapter;
	}

	/**
	 * @param pCricketESPAdapter
	 *            the cricketESPAdapter to set
	 */
	public void setCricketESPAdapter(CricketESPAdapter pCricketESPAdapter) {
		cricketESPAdapter = pCricketESPAdapter;
	}

}
