package com.cricket.commerce.order.processor.esp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.cricket.commerce.order.CricketPackage;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.ESPResponseVO;
import com.cricket.integration.esp.vo.HotBillChargeVO;
import com.cricket.integration.esp.vo.SubscriberChargesVO;
import com.cricket.integration.esp.vo.SubscriberQuoteChargesVO;
import com.cricket.integration.esp.vo.UpdateSubscriberResponseVO;
import com.cricket.user.session.UserAccountInformation;
import com.cricket.user.session.UserSessionBean;
import com.cricket.util.CAQServiceAmount;
import com.cricket.util.EspServiceResponseData;
import com.cricket.vo.CricketProfile;
import com.cricket.vo.MyCricketCookieLocationInfo;

public class ProcProcessUpdateSubscriber extends GenericService implements PipelineProcessor {
  	  
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
		//logInfo("[ProcProcessUpdateSubscriber->runProcess()]: Entering into runProcess()...");
		
		long starttime	= 0L;
		long endtime 	= 0L;
		HashMap paramMap = (HashMap) pParam;
        Object order = paramMap.get(PipelineConstants.ORDER);
        
        if (order == null) {
            throw new InvalidParameterException("[ProcProcessUpdateSubscriber->runProcess()]: order Id is not valid");
        }
        CricketOrderImpl cricketOrder = (CricketOrderImpl) order;        
        if (isLoggingDebug()) {							
	    		logDebug("Inside the ProcProcessUpdateSubscriber class of runProcess() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}
        Object locationInfoObj = paramMap.get(CricketESPConstants.LOCATION_INFO);
        Object cricketProfileDataObj = paramMap.get(CricketESPConstants.CRICKET_PROFILE);
        Object espServiceResponseDataObj = paramMap.get(CricketESPConstants.ESP_SERVICE_RESPONSE_DATA);
        Object userAccountInformationObj = paramMap.get(CricketESPConstants.USER_ACCOUNT_INFORMATION);
        Object userSessionBeanObj = paramMap.get(CricketESPConstants.USER_SESSION_BEAN);         
        CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData) paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);

        MyCricketCookieLocationInfo locataionInfoData = (MyCricketCookieLocationInfo)locationInfoObj;       
        CricketProfile  cricketProfile = (CricketProfile)cricketProfileDataObj;
        EspServiceResponseData espServiceResponseData = (EspServiceResponseData)espServiceResponseDataObj;
        UserAccountInformation userAccountInformation = (UserAccountInformation)userAccountInformationObj;
        UserSessionBean  userSessionBean 	=	(UserSessionBean)	userSessionBeanObj;
        logInfo("[ProcProcessUpdateSubscriber->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
        		accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
        		accountHolderAddressData.getAccountAddress().getPhoneNumber());
        if(isLoggingDebug()){
        	 CricketOrderTools ordertools =(CricketOrderTools) getOrderManager().getOrderTools();
     	    		logDebug("[ProcProcessUpdateSubscriber->runProcess()]: Order Details :");
     	    		logDebug(ordertools.orderDetailsToTrack(cricketOrder,accountHolderAddressData.getAccountAddress(),this.getClass().getName())+
     	    				"** CricketProfile Details : "+cricketProfile.toString()+
  		  			  "** LocationInfo Details : "+locataionInfoData.toString()+
  		  			  "** EspServiceResponseData Details : "+espServiceResponseData.toString()+
  		  			  "** UserAccountInformation Details : "+userAccountInformation.toString()+
  		  			  "** UserSessionBean Details : "+userSessionBean.toString());
        	}
        try{
        	starttime = System.currentTimeMillis();
	        UpdateSubscriberResponseVO updateSubscriberResponseVO = getCricketESPAdapter().updateSubscriber(   			 																			 
	        		cricketProfile, 
	        		locataionInfoData, 
	        		cricketOrder, 
					userAccountInformation,
					userSessionBean); 
	      
			endtime = System.currentTimeMillis();
		
			logInfo("[ProcProcessUpdateSubscriber->runProcess()]: time taken to get ESP response in milliseconds:  "+ (endtime - starttime));		
		
	
		 BigDecimal serviceTax 	= new BigDecimal(0.00);
		 BigDecimal totalCharge = new BigDecimal(0.00);
		 
		 if(null!= updateSubscriberResponseVO){
			
			 ESPResponseVO[] listOfResponse=  updateSubscriberResponseVO.getResponse();
		 
             if(listOfResponse!=null && !listOfResponse[0].getCode().equalsIgnoreCase(ESP_SUCCESS)){
            	pResult.addError(listOfResponse[0].getCode(),listOfResponse[0].getDescription());
            }else{           	
            	if(updateSubscriberResponseVO!=null){
            		cricketOrder.setBillingQuoteId(updateSubscriberResponseVO.getBillingQuote().getBillingQuoteId());
       			  // need to check on this
            		cricketOrder.setCricCustomerId(cricketProfile.getCustomerId());
            		cricketOrder.setCricCustmerBillingNumber(cricketProfile.getAccountNumber());
            		List<HotBillChargeVO> hotbillChargeList = updateSubscriberResponseVO.getBillingQuote().getHotBillCharge();
      			  
      			  
      			  	if(null!=hotbillChargeList && hotbillChargeList.size()>0){
      			  		for(HotBillChargeVO hotBillChargeVO : hotbillChargeList){
      			  			if(!hotBillChargeVO.isFuturePeriodBoo()){
		      			  		 //this is the current charges, isFuturePeriodBoo is false
			      				  totalCharge = hotBillChargeVO.getChargeTotal();
			      				  // we have to show this value in page level tax
			      				  serviceTax =  hotBillChargeVO.getTaxTotal();
			      				  updateSubscriberResponseVO.setServiceTax(serviceTax.doubleValue());
			      				  double totalAmount = totalCharge.doubleValue()+serviceTax.doubleValue();
			      				  if( totalAmount>0.0){
			      					  cricketOrder.setDownGrade(false);
			      				  }else{
			      					 cricketOrder.setDownGrade(true);  
			      				  }
			      				 List<SubscriberQuoteChargesVO> subscriberQuoteChargesList = hotBillChargeVO.getSubscriberQuoteCharges();
			             		setMdnAndServiceAmounts(cricketOrder, subscriberQuoteChargesList,espServiceResponseData,cricketProfile);
      			  			}
      			  		}
      			  	}
      			  	// set this totalTax to tax engine.
      			  	paramMap.put(CricketESPConstants.SERVICE_TAX, serviceTax);      		
         	 	  }  
            }
		}
		 logInfo("[ProcProcessUpdateSubscriber->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
	    return SUCCESS;
	    
        } 
        catch (ServiceException se) {
        	vlogError("[ProcProcessUpdateSubscriber->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + "ServiceException while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), se);
			getCricketESPAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, se, "ServiceException");
			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}
        catch (ESPApplicationException eae) {
        	vlogError("[ProcProcessUpdateSubscriber->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + "ESPApplicationException while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), eae);
			String errorCode = getCricketESPAdapter().getEspFaultCode(eae);
			if(CricketESPConstants.ESP_TIMEOUT.equalsIgnoreCase(errorCode)){			
				int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);	
				if(returnCode <= 0){
					getCricketESPAdapter().showErrorLogs(pResult, errorCode, errorCode, eae, "ESPApplicationException");
					//pResult.addError(errorCode,errorCode);
				}else{
					return returnCode;
				}
			} else if (errorCode.contains(CricketESPConstants.GIVEN_OFFER_EXISTS_FOR_CUST)){
				boolean cancelBillingQuoteStatus = getCricketESPAdapter().cancelBillingQuoteStatus(cricketOrder);
				if(cancelBillingQuoteStatus){
					int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);
					if(returnCode <= 0){
						pResult.addError("OFFEREXISTSFORCUST_EXCEPTION","OFFEREXISTSFORCUST_EXCEPTION");
					}else{
						return returnCode;
					}
				}else{
					pResult.addError("OFFEREXISTSFORCUST_EXCEPTION","OFFEREXISTSFORCUST_EXCEPTION");
				}
			}else{
				getCricketESPAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_BUSINESS_EXCEPTIONS, CricketESPConstants.ESP_BUSINESS_EXCEPTIONS, eae, "ESPApplicationException");
				//pResult.addError(CricketESPConstants.ESP_BUSINESS_EXCEPTIONS,CricketESPConstants.ESP_BUSINESS_EXCEPTIONS);
			}
		} 
		catch (RemoteException re) {
			vlogError("[ProcProcessUpdateSubscriber->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + "RemoteException while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), re);
			if(re.getCause() != null && re.getCause().getMessage() != null && re.getCause().getMessage().contains(CricketESPConstants.READ_TIMED_OUT)){
				int returnCode = getCricketESPAdapter().retry(pParam, pResult, cricketOrder, this);
				if(returnCode <= 0){
					getCricketESPAdapter().showErrorLogs(pResult, CricketESPConstants.ESP_TIMEOUT, CricketESPConstants.ESP_TIMEOUT, re, "RemoteException");
					//pResult.addError(CricketESPConstants.ESP_TIMEOUT,CricketESPConstants.ESP_TIMEOUT);
				}else{
					return returnCode;
				}
			}else{
				getCricketESPAdapter().showErrorLogs(pResult,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, re, "RemoteException");
				//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
			}
		} catch (Exception e){
			vlogError("[ProcProcessUpdateSubscriber->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + "Exception while getting response:"+ "ConversationId: " + getCricketESPAdapter().getConversationId() + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
			getCricketESPAdapter().showErrorLogs(pResult,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, CricketESPConstants.ESP_SYSTEM_EXCEPTIONS, e, "Exception");
			//pResult.addError(CricketESPConstants.ESP_SYSTEM_EXCEPTIONS,CricketESPConstants.ESP_SYSTEM_EXCEPTIONS);
		}
        logInfo("ProcProcessUpdateSubscriber->runProcess()]: Exiting runProcess():::");
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
	 * @param order
	 * @param subscriberQuoteChargesList
	 */
	private void setMdnAndServiceAmounts(CricketOrderImpl order,
			List<SubscriberQuoteChargesVO> subscriberQuoteChargesList,
			EspServiceResponseData espServiceResponseData,
			CricketProfile cricketProfile) {
		
		 // Getting the Order Id
		String orderId = CricketCommonConstants.EMPTY_STRING;
		if(null != order){
			if(!StringUtils.isBlank(order.getId())){
				orderId = order.getId();
			}
		}
		if (isLoggingDebug()) {								
	    		logDebug("Inside the ProcProcessUpdateSubscriber class of setMdnAndServiceAmounts() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId);
		}
		
		if(null!=subscriberQuoteChargesList && subscriberQuoteChargesList.size()>0){
			 List<CAQServiceAmount> caqServiceAmountList = new  ArrayList<CAQServiceAmount>();
			 CAQServiceAmount caqServiceAmount = null;
			 for(SubscriberQuoteChargesVO sqcVo:subscriberQuoteChargesList)
			 {
				 caqServiceAmount = new CAQServiceAmount();
				 caqServiceAmount.setServiceAmount(sqcVo.getChargeTotal());
				 caqServiceAmount.setServiceTax(sqcVo.getTaxTotal());
				 caqServiceAmount.setMdn(sqcVo.getMdn());
				 caqServiceAmountList.add(caqServiceAmount);				
			 }
			 espServiceResponseData.setServiceAmountList(caqServiceAmountList);			
			  
			  //if this method is generalized, please consider below scenario, the code will get change
			  //As this is a local method of UpdateSubscriber process, we are assuming there will be only 
			  //one element in subscriberQuoteChargesList as the user can do either change plan / change feature
			  
			  SubscriberQuoteChargesVO subscriberQuoteChargesVO =  subscriberQuoteChargesList.get(0);
			  if(subscriberQuoteChargesVO != null){
				  
				  List<SubscriberChargesVO> subscriberChargesList = subscriberQuoteChargesVO.getSubscriberCharges();
				  List<SubscriberChargesVO> recurringSubscriberChargesList = null;
				  
				  if(subscriberChargesList != null && subscriberChargesList.size() > 0){
					  recurringSubscriberChargesList = new ArrayList<SubscriberChargesVO>();
					  
					  for(SubscriberChargesVO scvo : subscriberChargesList){
						  if(scvo.isRecurringCharge()){
							  recurringSubscriberChargesList.add(scvo);
						  }
						  
					  }
					  if(recurringSubscriberChargesList != null && recurringSubscriberChargesList.size() >0){
						  espServiceResponseData.setRecurringSubscriberChargesList(recurringSubscriberChargesList);
					  }
				  }
			  }			  
		 }
		if (isLoggingDebug()) {			 					
    		logDebug("[ProcProcessUpdateSubscriber->runProcess()]: Exiting setMdnAndServiceAmounts(),..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId);
			}
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
 