package com.cricket.commerce.order.purchase;

import static com.cricket.common.constants.CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.BILLING_ADDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.BUSINESS_ADDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.CRICKET_PROFILE;
import static com.cricket.common.constants.CricketESPConstants.ESP_SERVICE_RESPONSE_DATA;
import static com.cricket.common.constants.CricketESPConstants.IP_ADDRESS;
import static com.cricket.common.constants.CricketESPConstants.PROFILE_ITEM;
import static com.cricket.common.constants.CricketESPConstants.SHIPPING_ADDRESS_DATA;
import static com.cricket.common.constants.CricketESPConstants.USER_COOKIE_INFO;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import atg.commerce.CommerceException;
import atg.commerce.order.PipelineConstants;
import atg.commerce.order.purchase.CommitOrderFormHandler;
import atg.commerce.states.OrderStates;
import atg.commerce.util.RepeatingRequestMonitor;
import atg.core.util.StringUtils;
import atg.droplet.DropletException;
import atg.service.pipeline.PipelineResult;
import atg.service.pipeline.RunProcessException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;

import com.cricket.browse.StorePackageFeaturesDroplet;
import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketBillingAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketPackage;
import com.cricket.commerce.order.OrderConstants;
import com.cricket.commerce.order.payment.CricketABPPaymentData;
import com.cricket.commerce.order.payment.CricketPaymentData;
import com.cricket.commerce.order.session.UpgradeItemDetailsSessionBean;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.esp.ESP.Namespaces.Container.Public.SendMessageResponse_xsd.SendMessageResponseInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.order.CricketOrder;
import com.cricket.user.session.UserSessionBean;
import com.cricket.util.EspServiceResponseData;
import com.cricket.vo.CricketProfile;
 

/** this formhandlder handover the order to atg oob and calls different esps.
 * @author TechM
 * 
 */ 
public class CricketCommitOrderFormHandler extends CommitOrderFormHandler {
        /* holds userCookieInfo component */
	 	private CricketProfile mUserCookieInfo;
	 	/* holds espServiceResponseData to calculate service amounts */
	 	private EspServiceResponseData   mEspServiceResponseData;
	 	/* holds accountHolderAddress Data component */ 
	 	private CricketAccountHolderAddressData   mAccountHolderAddressData;
	 	/* holds shippingAddressData component */
	 	private CricketShippingAddressData mShippingAddressData;
	 	/* holds billing address data component */
	 	private CricketBillingAddressData mBillingAddressData;
	 	/* variable holds the ip Address of client - need to pass in esp */
	 	private String ipAddress;
		/* variable holds the payment data for normal credit card */
		private CricketPaymentData paymentData;
		/* variable holds the payment data for credit card data in case of auto bill pay for diff card*/
		private CricketABPPaymentData abpPaymentData;
		/* variable holds the cricketProfile. */
		private CricketProfile cricketProfile;
		/* variable holds the UpgradeItemDetailsSessionBean. */
		private UpgradeItemDetailsSessionBean sessionBeanPath;
		/* variable holds the StorePackageFeaturesDroplet. */
		private StorePackageFeaturesDroplet storePackageFeaturesDroplet;
		/* variable holds the user session bean. */
		private UserSessionBean userSessionBean;
		private ProfileServices profileServices;
		private String successURL;
		private String failureURL;
		/* holds cricketESPVestaPaymentChain chain name */
		private String cricketESPVestaPaymentChain;
		/* holds CricketESPAdapter instance. */
	   	private CricketESPAdapter cricketESPAdapter;
		
	 /* (non-Javadoc)
	 * @see atg.commerce.order.purchase.CommitOrderFormHandler#handleCommitOrder(atg.servlet.DynamoHttpServletRequest, atg.servlet.DynamoHttpServletResponse)
	 */
	@Override
 
	public boolean handleCommitOrder(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
		 CricketOrderImpl order = (CricketOrderImpl) getOrder();
		 String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank( order.getId())){
	    			orderId = order.getId();
				}
			}
		if (isLoggingDebug()) {
						
	    		logDebug("Entering into CricketCommitOrderFormHandler class of handleCommitOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		 boolean flag=false; 
		 String l_sMethod = "CricketCommitOrderFormHandler.handleCommitOrder";
			RepeatingRequestMonitor l_oRepeatingRequestMonitor = getRepeatingRequestMonitor();
			if ((l_oRepeatingRequestMonitor == null)
					|| (l_oRepeatingRequestMonitor
							.isUniqueRequestEntry(l_sMethod))) {
				 try {
					runProcessRepriceOrder(getOrder(), getUserPricingModels(), getUserLocale(), getProfile(), createRepriceParameterMap());
				} catch (RunProcessException e1) {
					vlogError("Error while repricing the order in handleCommitOrder() of CricketCommitOrderFormHandler::: " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e1);
				}
				//AMP:code change begin 
				
				//PipelineResult result = applyEspServiceChainOnOrder(order,pRequest);
				//processPiplelineResult(result);
			 
				 double orderTotal = getOrder().getPriceInfo().getTotal();
				 vlogDebug("orderTotal is :"+ orderTotal);
					if(orderTotal > 0){
						vlogDebug("Inside orderTotal > 0, and calling payment pipeline");
						PipelineResult result = applyEspServiceChainOnOrder(order,pRequest);
						processPiplelineResult(result);
					}
					else{
						vlogDebug("Inside ELSE orderTotal > 0, and DO NOT call payment pipeline");
						
					}
					//AMP:code change end
					
				 super.handleCommitOrder(pRequest, pResponse);
			 
			setIpAddress(getClientIPAddress(pRequest));
			  
			javax.transaction.Transaction tr = null;
			try {
				tr = ensureTransaction();
				if (getUserLocale() == null)
					setUserLocale(getUserLocale(pRequest, pResponse));
			  
					synchronized (getOrder()) {
						
						try {
						//	((CricketOrderImpl) getOrder()).updateVersion(); 
							Profile profile = getProfileServices().getCurrentProfile();	
							((CricketOrderImpl) getOrder()).setTotalTax(((CricketOrderImpl) getOrder()).getTaxPriceInfo().getAmount());
							order = (CricketOrderImpl) getOrder();	
							order.setTotalTax(order.getTaxPriceInfo().getAmount());
							profile.setPropertyValue(CricketCommonConstants.EMAIL,getAccountHolderAddressData().getAccountAddress().getEmail());
							getOrderManager().updateOrder(getOrder());
						} catch (CommerceException e) {
							vlogError("Error while updating the order in handleCommitOrder() of CricketCommitOrderFormHandler::: " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
						}
					}
					
					flag=	checkFormRedirect(getSuccessURL(),getFailureURL()+CricketCommonConstants.WHOOPS_ERROR_YES, pRequest, pResponse);
					
				} finally {
					if (tr != null)
						commitTransaction(tr);
					// Commit or rolback the transaction depending on the
					// status.
					if (l_oRepeatingRequestMonitor != null) {
						l_oRepeatingRequestMonitor.removeRequestEntry(l_sMethod);
					}
				}
			}
			if (isLoggingDebug()) {							
		    		logDebug("Exiting from CricketCommitOrderFormHandler class of handleCommitOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
			return flag;
	}

	
	/** this method is used to send the required data to ESP call chain.
	 * @param order
	 * @return
	 */
	public PipelineResult applyEspServiceChainOnOrder(CricketOrderImpl order,DynamoHttpServletRequest dynamohttpservletrequest) {
		
		String orderId = null;
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(dynamohttpservletrequest.getRequestURIWithQueryString())){
					pageURL = dynamohttpservletrequest.getRequestURIWithQueryString();
				}

			 // Getting the Order Id			
			orderId = CricketCommonConstants.EMPTY_STRING;
	    		if(null != order){
	    			if(!StringUtils.isBlank( order.getId())){
		    			orderId = order.getId();
					}
	    		}			
	    		logDebug("Entering into CricketPaymentGroupFormHandler class of applyEspServiceChainOnOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		// prepare params
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(PipelineConstants.ORDER, order);
		params.put(CRICKET_PROFILE,getCricketProfile());
		params.put(ESP_SERVICE_RESPONSE_DATA, getEspServiceResponseData());
		params.put(ACCOUNT_HOLDER_ADDDRESS_DATA, getAccountHolderAddressData());
		params.put(SHIPPING_ADDRESS_DATA, getShippingAddressData());
		params.put(BUSINESS_ADDRESS_DATA, getBillingAddressData());
		params.put(PROFILE_ITEM, getProfile());
		params.put(IP_ADDRESS, getClientIPAddress(dynamohttpservletrequest));
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(dynamohttpservletrequest.getRequestURIWithQueryString())){
					pageURL = dynamohttpservletrequest.getRequestURIWithQueryString();
				}

			 // Getting the Order Id			
			 orderId = CricketCommonConstants.EMPTY_STRING;
	    		if(null != order){
	    			if(!StringUtils.isBlank( order.getId())){
		    			orderId = order.getId();
					}
	    		}			
	    		logDebug("Exiting from CricketPaymentGroupFormHandler class of applyEspServiceChainOnOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
 		// delegate
		try {
			return getOrderManager().getPipelineManager().runProcess(getCricketESPVestaPaymentChain(), params);
		} catch (RunProcessException e) {
			vlogError("Unexpected exception while executing esp calls::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			return null;
		}
	}
	/** Error handling - this method is used to popup the error if any while placing the order in billing system.
	 * @param result
	 * @return true
	 */
	private boolean processPiplelineResult(final PipelineResult result) {
		if(result != null && result.hasErrors()) {
			String errorKey = null;
			// Add error messages to form
			 Object errorKeys[] = result.getErrorKeys();
			 Object error = null;
	            if(errorKeys != null)
	            {
	                for(int index = 0; index < errorKeys.length; index++)
	                {
	                    error = result.getError(errorKeys[index]);
	                    if(isLoggingDebug())
	                        logDebug((new StringBuilder()).append("PipelineError: key=").append(errorKeys[index]).append("; error=").append(error).toString());
	                    if(error == null)
	                        continue;
	                    errorKey = errorKeys[index].toString();
	                    addFormException(new DropletException(errorKey));
	                }
	            }
			// done
			return false;
		}
		// all ok
		return true;
	}



	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	/* (non-Javadoc)
	 * @see atg.commerce.order.purchase.CommitOrderFormHandler#preCommitOrder(atg.servlet.DynamoHttpServletRequest, atg.servlet.DynamoHttpServletResponse)
	 */
	@Override
	public void preCommitOrder(
			DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse)
			throws ServletException, IOException {

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
		CricketOrder order = (CricketOrder) getOrder();
		 String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank( order.getId())){
	    			orderId = order.getId();
				}
			}	
		if (isLoggingDebug()) {
					
	    		logDebug("Enter into CricketCommitOrderFormHandler class of preCommitOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		String packageId = null;
		String packageSubTotal = null;
		String[] packageTotal = null;
		if(null != getSessionBeanPath().getUserIntention() && !StringUtils.isEmpty(getSessionBeanPath().getUserIntention())){
			getAccountHolderAddressData().setUserIntentionVar(getSessionBeanPath().getUserIntention().toString());
		}	
		order = (CricketOrder) getOrder();
		List<CricketPackage> cktPackages = ((CricketOrder)getOrder()).getCktPackages();
		if(!cktPackages.isEmpty()){
		String packagesTotals = pRequest.getParameter(CricketCommonConstants.SPLIT_PACKAGE_TOTAL);
		if(null!=packagesTotals && !StringUtils.isBlank(packagesTotals)){
			synchronized (order) {				
				try {
					String[] splitPackagesTotals = packagesTotals.split(CricketCommonConstants.COMMA_SEPARATOR);
					
					for(String packagseTotal:splitPackagesTotals){
						 
						packageTotal = packagseTotal.split(CricketCommonConstants.SLASH_SEPARATOR);
					    packageId=packageTotal[0];
						packageSubTotal=packageTotal[1];
						 
						for(CricketPackage packages:cktPackages ){
							if(packageId.equalsIgnoreCase(packages.getId())){
								packages.setPackageTotal(packageSubTotal);	
								break;
							}														
						}					
					}
				
					getOrderManager().updateOrder(order);
				} catch (CommerceException e) {
					vlogError("CommerceException while updating the order in preCommitOrder() of CricketCommitOrderFormHandler::: " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
					logError("Error while updating the order "+getClass().getName() +"   "+e,e);
				}
			}
		}
	}
		super.preCommitOrder(pRequest, pResponse);
		if (isLoggingDebug()) {					
	    		logDebug("Exit from CricketCommitOrderFormHandler class of preCommitOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}





	/** This method invokes oob post commitOrder method and clean the payment data like card number, vestaToken etc..
	 * @author TechM
	 * 
	 */
		@Override
		public void postCommitOrder(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
			super.postCommitOrder(pRequest, pResponse);
			 CricketOrderImpl order = (CricketOrderImpl) getOrder();				
			if (isLoggingDebug()) {
				// Getting the Page url
					String pageURL = CricketCommonConstants.EMPTY_STRING;
					if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
						pageURL = pRequest.getRequestURIWithQueryString();
					}

				 // Getting the Order Id
				
				 String orderId = CricketCommonConstants.EMPTY_STRING;
		    		if(null != order){
		    			if(!StringUtils.isBlank( order.getId())){
			    			orderId = order.getId();
						}
		    		}			
		    		logDebug("Enter into CricketCommitOrderFormHandler class of postCommitOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId()+ CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
			
			if(null != order &&(null!=order.getWorkOrderType() && order.getWorkOrderType().equalsIgnoreCase(CricketESPConstants.TRANSACTION_TYPE_RRC) && order.getStateAsString().equalsIgnoreCase(OrderStates.SUBMITTED))){
	         	try {
					SendMessageResponseInfo response = getCricketESPAdapter().sendMessageRRC(order, getAccountHolderAddressData().getAccountAddress().getEmail());
	         	if(null!=response &&(null!= response.getResponse() && response.getResponse().getCode().equalsIgnoreCase(CricketESPConstants.ESP_RESPONSE_CODE_SUCCESS))){
	         	  logInfo("CricketCommitOrderFormHandler-> postCommitOrder: Calling SendMessage ESP- for RatePlan/Change Feature");
	         	}else{
	         		logError("CricketCommitOrderFormHandler -> postCommitOrder: Error while Calling SendMessage ESP - from RatePlan/Change Featuer");
	         	}
	         	
	         	} catch (ServiceException e) {
					logError("Error while calling SendMessage ESP call"+e);
				}		         	
	         	catch (ESPApplicationException eae) {
	         		logError("CricketCommitOrderFormHandler -> postCommitOrder: Error while Calling SendMessage ESP - from RatePlan/Change Featuer "+eae,eae);
	     		}
	    		catch (RemoteException re) {
	    			logError("CricketCommitOrderFormHandler -> postCommitOrder: Error while Calling SendMessage ESP - from RatePlan/Change Featuer"+re,re);
	    		} catch (Exception e){
	    			logError("CricketCommitOrderFormHandler -> postCommitOrder: Error while Calling SendMessage ESP - from RatePlan/Change Featuer"+e,e);
	    		}
	          }
			
			if(!getFormError()){
				
				// clearing payment data
				pRequest.getNucleus().removeLiveComponent(getPaymentData());
				pRequest.getNucleus().removeLiveComponent(getAbpPaymentData());
				pRequest.getNucleus().removeLiveComponent(getEspServiceResponseData());
			 
				//clearing address data 
				if(getProfile().isTransient()){
					pRequest.getNucleus().removeLiveComponent(getAccountHolderAddressData().getAccountAddress());
					pRequest.getNucleus().removeLiveComponent(getShippingAddressData().getShippingAddress());
					pRequest.getNucleus().removeLiveComponent(getBillingAddressData().getBillingAddress());
					pRequest.getNucleus().removeLiveComponent(getAccountHolderAddressData());
					pRequest.getNucleus().removeLiveComponent(getShippingAddressData());
					pRequest.getNucleus().removeLiveComponent(getBillingAddressData());
				}
			  // cleraring session data
				pRequest.getNucleus().removeLiveComponent(getSessionBeanPath());
				pRequest.getNucleus().removeLiveComponent(getStorePackageFeaturesDroplet());
			//	pRequest.getNucleus().removeLiveComponent(getUserSessionBean());
				getUserSessionBean().setAutoBillPayment(false);
				cricketProfile.setMdn(null);
				getUserSessionBean().setMandatoryOffersMap(null);
				getUserSessionBean().setMandatoryOffersUpgradePlanMap(null);
			
			}
			 
			
			if (isLoggingDebug()) {
				// Getting the Page url
					String pageURL = CricketCommonConstants.EMPTY_STRING;
					if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
						pageURL = pRequest.getRequestURIWithQueryString();
					}
				 // Getting the Order Id
 				 String orderId = CricketCommonConstants.EMPTY_STRING;
		    		if(null != order){
		    			if(!StringUtils.isBlank( order.getId())){
			    			orderId = order.getId();
						}
		    		}			
		    		logDebug("Exit from CricketCommitOrderFormHandler class of postCommitOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
		}
	 
	/*	@Override
		protected boolean processPipelineErrors(PipelineResult pResult) {
			if(pResult != null && pResult.hasErrors()) {
				String errorKey = null;
				// Add error messages to form
				 Object errorKeys[] = pResult.getErrorKeys();
				 Object error = null;
		            if(errorKeys != null)
		            {
		                for(int index = 0; index < errorKeys.length; index++)
		                {
		                    error = pResult.getError(errorKeys[index]);
		                    if(isLoggingDebug())
		                        logDebug((new StringBuilder()).append("PipelineError: key=").append(errorKeys[index]).append("; error=").append(error).toString());
		                    if(error == null)
		                        continue;
		                    errorKey = errorKeys[index].toString();
		                    if(error!=null && errorKey.equalsIgnoreCase("outOfStock")){
		                    	addFormException(new DropletException("outOfStock"+error));
		                    } else{
		                    addFormException(new DropletException(errorKey));
		                    }
		                }
		            }
				// done
				return false;
			}
			// all ok
			return true;
		}*/
	/** getProcessOrderMap will carry the session component data to pipelines to fulfill the esp request params.
	 * @author TechM
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> getProcessOrderMap(Locale pLocale) throws CommerceException {
		
		HashMap<String, Object> pomap = getOrderManager().getProcessOrderMap(pLocale, null);
        if(!StringUtils.isEmpty(getSiteId()))
            pomap.put(CricketCommonConstants.SITE_ID, getSiteId());
        if(!StringUtils.isEmpty(getSalesChannel()))
            pomap.put(CricketCommonConstants.SALES_CHANNEL, getSalesChannel());
        
        CricketProfile cookieInfo = getUserCookieInfo();
        pomap.put(USER_COOKIE_INFO,cookieInfo);
		pomap.put(ESP_SERVICE_RESPONSE_DATA, getEspServiceResponseData());
        pomap.put(ACCOUNT_HOLDER_ADDDRESS_DATA, getAccountHolderAddressData());
        pomap.put(IP_ADDRESS, getIpAddress());
        pomap.put(SHIPPING_ADDRESS_DATA, getShippingAddressData());
        pomap.put(BILLING_ADDRESS_DATA, getBillingAddressData());
        pomap.put(PROFILE_ITEM, getProfile());
        pomap.put(CRICKET_PROFILE,getCricketProfile());
          // please remove and work out on payment process - crs application causing some issues while calling check this.
    //    pomap.put("bypassPaymentAuthorizations", "bypassPaymentAuthorizations");
        
        return pomap;
		 
	}


	/** this method is used to get the client id address - managepayment ESP requires this value.
	 * @param dynamoHttpServletRequest
	 * @return
	 */
	private String getClientIPAddress(
			DynamoHttpServletRequest dynamoHttpServletRequest) {
		String clientIPAddress = null;
		if(isLoggingDebug()){
        logDebug("[CricketCommitOrderFormHandler->getClientIPAddress()]: IP Address X-Forwarded-For: "+dynamoHttpServletRequest.getHeader("X-Forwarded-For"));
        logDebug("[CricketCommitOrderFormHandler->getClientIPAddress()]: IP Address X-FORWARDED-FOR: "+dynamoHttpServletRequest.getHeader("X-FORWARDED-FOR"));
        logDebug("[CricketCommitOrderFormHandler->getClientIPAddress()]: IP Address x-forwarded-for: "+dynamoHttpServletRequest.getHeader("x-forwarded-for"));
		}
		if ((clientIPAddress = dynamoHttpServletRequest.getHeader("X-Forwarded-For")) != null) {
			String[] ipAddresses = clientIPAddress.split(",");
			clientIPAddress = (ipAddresses != null && ipAddresses.length > 0) ? ipAddresses[0] : dynamoHttpServletRequest.getRemoteAddr();
		} else {
			logDebug("[CricketCommitOrderFormHandler->getClientIPAddress()]: Remote IP Address: "+dynamoHttpServletRequest.getRemoteAddr());
			clientIPAddress = dynamoHttpServletRequest.getRemoteAddr();
		}
 		return (clientIPAddress != null) ? clientIPAddress.trim() : clientIPAddress;
	}

	
	

	 

	/* (non-Javadoc)
	 * @see atg.commerce.order.purchase.PurchaseProcessFormHandler#handlePipelineError(java.lang.Object, java.lang.String)
	 */
	@Override
	public void handlePipelineError(Object pError, String pErrorKey) {
		addFormException(new DropletException( pErrorKey));
	}

	/**
	 * @return the userCookieInfo
	 */
	public CricketProfile getUserCookieInfo() {
		return mUserCookieInfo;
	}

	/**
	 * @param pUserCookieInfo the userCookieInfo to set
	 */
	public void setUserCookieInfo(CricketProfile pUserCookieInfo) {
		mUserCookieInfo = pUserCookieInfo;
	}


	/**
	 * @return the espServiceResponseData
	 */
	public EspServiceResponseData getEspServiceResponseData() {
		return mEspServiceResponseData;
	}

	/**
	 * @param pEspServiceResponseData the espServiceResponseData to set
	 */
	public void setEspServiceResponseData(EspServiceResponseData pEspServiceResponseData) {
		mEspServiceResponseData = pEspServiceResponseData;
	}

  	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}
 
	/**
	 * @param pIpAddress the ipAddress to set
	 */
	public void setIpAddress(String pIpAddress) {
		ipAddress = pIpAddress;
	}
 
	/**
	 * @return the shippingAddressData
	 */
	public CricketShippingAddressData getShippingAddressData() {
		return mShippingAddressData;
	}
 
	/**
	 * @param pShippingAddressData the shippingAddressData to set
	 */
	public void setShippingAddressData(CricketShippingAddressData pShippingAddressData) {
		mShippingAddressData = pShippingAddressData;
	}
 
	/**
	 * @return the billingAddressData
	 */
	public CricketBillingAddressData getBillingAddressData() {
		return mBillingAddressData;
	}
 
	/**
	 * @param pBillingAddressData the billingAddressData to set
	 */
	public void setBillingAddressData(CricketBillingAddressData pBillingAddressData) {
		mBillingAddressData = pBillingAddressData;
	}
 
	/**
	 * @return the accountHolderAddressData
	 */
	public CricketAccountHolderAddressData getAccountHolderAddressData() {
		return mAccountHolderAddressData;
	}

	/**
	 * @param pAccountHolderAddressData the accountHolderAddressData to set
	 */
	public void setAccountHolderAddressData(CricketAccountHolderAddressData pAccountHolderAddressData) {
		mAccountHolderAddressData = pAccountHolderAddressData;
	}

	/**
	 * @return the paymentData
	 */
	public CricketPaymentData getPaymentData() {
		return paymentData;
	}

	/**
	 * @param paymentData the paymentData to set
	 */
	public void setPaymentData(CricketPaymentData paymentData) {
		this.paymentData = paymentData;
	}


	/**
	 * @return the abpPaymentData
	 */
	public CricketABPPaymentData getAbpPaymentData() {
		return abpPaymentData;
	}

	/**
	 * @param abpPaymentData the abpPaymentData to set
	 */
	public void setAbpPaymentData(CricketABPPaymentData abpPaymentData) {
		this.abpPaymentData = abpPaymentData;
	}

	/**
	 * @return the cricketProfile
	 */
	public CricketProfile getCricketProfile() {
		return cricketProfile;
	}

	/**
	 * @param cricketProfile
	 *            the cricketProfile to set
	 */
	public void setCricketProfile(CricketProfile cricketProfile) {
		this.cricketProfile = cricketProfile;
	}





	/**
	 * @return the sessionBeanPath
	 */
	public UpgradeItemDetailsSessionBean getSessionBeanPath() {
		return sessionBeanPath;
	}





	/**
	 * @param sessionBeanPath the sessionBeanPath to set
	 */
	public void setSessionBeanPath(UpgradeItemDetailsSessionBean sessionBeanPath) {
		this.sessionBeanPath = sessionBeanPath;
	}





	/**
	 * @return the storePackageFeaturesDroplet
	 */
	public StorePackageFeaturesDroplet getStorePackageFeaturesDroplet() {
		return storePackageFeaturesDroplet;
	}





	/**
	 * @param storePackageFeaturesDroplet the storePackageFeaturesDroplet to set
	 */
	public void setStorePackageFeaturesDroplet(
			StorePackageFeaturesDroplet storePackageFeaturesDroplet) {
		this.storePackageFeaturesDroplet = storePackageFeaturesDroplet;
	}

	/**
	 * 
	 * @return the userSessionBean
	 */
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}


	/**
	 * 
	 * @param userSessionBean
	 */
	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}





	/**
	 * @return the profileServices
	 */
	public ProfileServices getProfileServices() {
		return profileServices;
	}





	/**
	 * @param profileServices the profileServices to set
	 */
	public void setProfileServices(ProfileServices profileServices) {
		this.profileServices = profileServices;
	}





	public String getSuccessURL() {
		return successURL;
	}





	public void setSuccessURL(String successURL) {
		this.successURL = successURL;
	}





	public String getFailureURL() {
		return failureURL;
	}





	public void setFailureURL(String failureURL) {
		this.failureURL = failureURL;
	}

	public String getCricketESPVestaPaymentChain() {
		return cricketESPVestaPaymentChain;
	}


	public void setCricketESPVestaPaymentChain(
			String cricketESPVestaPaymentChain) {
		this.cricketESPVestaPaymentChain = cricketESPVestaPaymentChain;
	}


	/**
	 * @return the cricketESPAdapter
	 */
	public CricketESPAdapter getCricketESPAdapter() {
		return cricketESPAdapter;
	}


	/**
	 * @param cricketESPAdapter the cricketESPAdapter to set
	 */
	public void setCricketESPAdapter(CricketESPAdapter cricketESPAdapter) {
		this.cricketESPAdapter = cricketESPAdapter;
	}
	
	
}
