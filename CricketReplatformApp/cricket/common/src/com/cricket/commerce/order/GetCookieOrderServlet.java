/**
 * 
 */
package com.cricket.commerce.order;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.transaction.TransactionManager;

import atg.commerce.CommerceException;
import atg.commerce.order.OrderHolder;
import atg.commerce.profile.CommercePropertyManager;
import atg.core.util.StringUtils;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;
import atg.servlet.pipeline.InsertableServletImpl;
import atg.userprofiling.Profile;

import com.cricket.common.constants.CricketCommonConstants;

public class GetCookieOrderServlet extends InsertableServletImpl{

	private CricketOrderManager cricketOrderManager;

	boolean mEnabled;
	
	private static final String INCOMPLETE="INCOMPLETE";
	
	private CommercePropertyManager mCommercePropertyManager;
	
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return mEnabled;
	}

	/**
	 * @param pEnabled the enabled to set
	 */
	public void setEnabled(boolean pEnabled) {
		mEnabled = pEnabled;
	}
	
	/**
	 * @return the cricketOrderManager
	 */
	public CricketOrderManager getCricketOrderManager() {
		return cricketOrderManager;
	}

	/**
	 * @param cricketOrderManager the cricketOrderManager to set
	 */
	public void setCricketOrderManager(CricketOrderManager cricketOrderManager) {
		this.cricketOrderManager = cricketOrderManager;
	}
	
	
	/**
	 * @return the mCommercePropertyManager
	 */
	public CommercePropertyManager getCommercePropertyManager() {
		return mCommercePropertyManager;
	}

	/**
	 * @param mCommercePropertyManager the mCommercePropertyManager to set
	 */
	public void setCommercePropertyManager(
			CommercePropertyManager pCommercePropertyManager) {
		this.mCommercePropertyManager = pCommercePropertyManager;
	}

	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws IOException,
			ServletException {
		if (isLoggingDebug()) {
			logDebug("Inside GetCookieOrderServlet:service method"
					+ CricketCommonConstants.SESSION_ID + getSessionId());
		}

		if (isEnabled()) {
			Profile profile = (Profile) pRequest
					.resolveName("/atg/userprofiling/Profile");
			
			String orderId = null;
			// get all cookies
			if(profile.isTransient()){
				Cookie[] cookies = pRequest.getCookies();
				if (cookies != null && cookies.length > 0) {
					for (int i = 0; i < cookies.length; i++) {
						Cookie cookie1 = cookies[i];
						// Check if contains created cookie
						
						if (cookie1.getName().equals(CricketCommonConstants.ORDER_PERSISTENT_COOKIE)) {
							// Get order id from the cookie
							orderId = cookie1.getValue();
							OrderHolder cart = (OrderHolder) pRequest
									.resolveName("/atg/commerce/ShoppingCart");
							if (null != orderId
									&& !orderId.equalsIgnoreCase(cart.getCurrent()
											.getId())) {
								TransactionManager tm = getCricketOrderManager()
										.getOrderTools().getTransactionManager();
								TransactionDemarcation td = new TransactionDemarcation();
								try {
									td.begin(tm);
									CricketOrderImpl order = (CricketOrderImpl) getCricketOrderManager()
											.loadOrder(orderId);
									synchronized (order) {
										if (!order.getId().equals(
												cart.getCurrent().getId()) && INCOMPLETE.equalsIgnoreCase(order.getStateAsString())
												&& order.getCommerceItemCount() > 0) {
											order.setProfileId(profile
													.getRepositoryId());
											cart.setCurrent(order);
											
											/*RepositoryItem pricelist = (RepositoryItem) profile.getPropertyValue("priceList");
											HashMap pExtraParameters = new HashMap();
										      if(pricelist != null) {
										          
										          //add an entry for the price list
										          CommercePropertyManager props = getCommercePropertyManager();
										          if(props != null) {
										              String priceListPropertyName = getCommercePropertyManager().getPriceListPropertyName();
										              pExtraParameters.put(priceListPropertyName,pricelist);
										          }
										      }
										      PricingModelHolder pricingModelHolder = (PricingModelHolder) pRequest
												.resolveName("/atg/commerce/pricing/UserPricingModels");
											HashMap params = new HashMap();
										      params.put("Order", order);
										      params.put("PricingModels", pricingModelHolder);
										      params.put("Locale", pRequest.getLocale());
										      params.put("Profile", profile);
										      params.put("OrderManager", getCricketOrderManager());
										      params.put("ExtraParameters", pExtraParameters);
											getCricketOrderManager().getPipelineManager().runProcess("repriceOrder", params);*/
											getCricketOrderManager().updateOrder(
													order);
										}
									}
								} catch (CommerceException e) {
									if (isLoggingError()) {
										logError("Error while loading the order for cookie and setting into current shopping cart : " 
												+ CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
									}
								} catch (TransactionDemarcationException e) {
									if (isLoggingError()) {
										logError("TransactionDemarcationException while loading the order for cookie and setting into current shopping cart : "
												+ CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
									}
								} finally {
									try {
										td.end();
									} catch (TransactionDemarcationException e) {
										vlogError("TransactionDemarcationException in service method of class GetCookieOrderServlet: "
												+ CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
										if (isLoggingDebug()) {
											logDebug("There was a TransactionDemarcationException while updating order the service Method of class GetCookieOrderServlet"
													+ CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId );
										}
									}
								}
							}
						}
						//changes for defect 8318 start 
						if (cookie1.getName().equals(CricketCommonConstants.ACTIVATION_FEE_COOKIE)) {
							String activationFee = cookie1.getValue();
							if(null != activationFee){
								profile.setPropertyValue(CricketCommonConstants.ACTIVATION_FEE, Double.valueOf(activationFee));
							}
						}
						//changes for defect 8318 end 
					}
				}
			}


		}

		if (isLoggingDebug()) {
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
				pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
			}
			logDebug("Exit GetCookieOrderServlet:service method"+ CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		super.service(pRequest, pResponse);
	}
	
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	
	/*private void createCookiesForTesting(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse) {
		String phoneId = pRequest.getQueryParameter("phoneId");
		if (!StringUtils.isEmpty(phoneId)) {
			Cookie phoneIdCookie = new Cookie("phoneId","phoneId");
			phoneIdCookie.setMaxAge(3 * 24 * 60 * 60);
			phoneIdCookie.setValue(phoneId);
			phoneIdCookie.setPath("/");
			pResponse.addCookie(phoneIdCookie);
		}
		String phoneSkuId = pRequest.getQueryParameter("phoneSkuId");
		if (!StringUtils.isEmpty(phoneSkuId)) {
			Cookie phoneSkuIdCookie = new Cookie("phoneSkuId","phoneSkuId");
			phoneSkuIdCookie.setMaxAge(3 * 24 * 60 * 60);
			phoneSkuIdCookie.setValue(phoneSkuId);
			phoneSkuIdCookie.setPath("/");
			pResponse.addCookie(phoneSkuIdCookie);
		}
		
		String planId = pRequest.getQueryParameter("planId");
		if (!StringUtils.isEmpty(planId)) {
			Cookie planIdCookie = new Cookie("planId","planId");
			planIdCookie.setMaxAge(3 * 24 * 60 * 60);
			planIdCookie.setValue(planId);
			planIdCookie.setPath("/");
			pResponse.addCookie(planIdCookie);
		}
		
		String planSkuId = pRequest.getQueryParameter("planSkuId");
		if (!StringUtils.isEmpty(planSkuId)) {
			Cookie planSkuIdCookie = new Cookie("planSkuId","planSkuId");
			planSkuIdCookie.setMaxAge(3 * 24 * 60 * 60);
			planSkuIdCookie.setValue(planSkuId);
			planSkuIdCookie.setPath("/");
			pResponse.addCookie(planSkuIdCookie);
		}
		
		String addonProd1Id = pRequest.getQueryParameter("addonProd1Id");
		if (!StringUtils.isEmpty(addonProd1Id)) {
			Cookie addonProd1IdCookie = new Cookie("addonProd1Id","addonProd1Id");
			addonProd1IdCookie.setMaxAge(3 * 24 * 60 * 60);
			addonProd1IdCookie.setValue(addonProd1Id);
			addonProd1IdCookie.setPath("/");
			pResponse.addCookie(addonProd1IdCookie);
		}
		
		String addonProd2Id = pRequest.getQueryParameter("addonProd2Id");
		if (!StringUtils.isEmpty(addonProd2Id)) {
			Cookie addonProd2IdCookie = new Cookie("addonProd2Id","addonProd2Id");
			addonProd2IdCookie.setMaxAge(3 * 24 * 60 * 60);
			addonProd2IdCookie.setValue(addonProd2Id);
			addonProd2IdCookie.setPath("/");
			pResponse.addCookie(addonProd2IdCookie);
		}
		
		String addonProd3Id = pRequest.getQueryParameter("addonProd3Id");
		if (!StringUtils.isEmpty(addonProd3Id)) {
			Cookie addonProd3IdCookie = new Cookie("addonProd3Id","addonProd3Id");
			addonProd3IdCookie.setMaxAge(3 * 24 * 60 * 60);
			addonProd3IdCookie.setValue(addonProd3Id);
			addonProd3IdCookie.setPath("/");
			pResponse.addCookie(addonProd3IdCookie);
		}
		
		String addonSku1Id = pRequest.getQueryParameter("addonSku1Id");
		if (!StringUtils.isEmpty(addonSku1Id)) {
			Cookie addonSku1IdCookie = new Cookie("addonSku1Id","addonSku1Id");
			addonSku1IdCookie.setMaxAge(3 * 24 * 60 * 60);
			addonSku1IdCookie.setValue(addonSku1Id);
			addonSku1IdCookie.setPath("/");
			pResponse.addCookie(addonSku1IdCookie);
		}
		
		String addonSku2Id = pRequest.getQueryParameter("addonSku2Id");
		if (!StringUtils.isEmpty(addonSku2Id)) {
			Cookie addonSku2IdCookie = new Cookie("addonSku2Id","addonSku2Id");
			addonSku2IdCookie.setMaxAge(3 * 24 * 60 * 60);
			addonSku2IdCookie.setValue(addonSku2Id);
			addonSku2IdCookie.setPath("/");
			pResponse.addCookie(addonSku2IdCookie);
		}
		
		String addonSku3Id = pRequest.getQueryParameter("addonSku3Id");
		if (!StringUtils.isEmpty(addonSku3Id)) {
			Cookie addonSku3IdCookie = new Cookie("addonSku3Id","addonSku3Id");
			addonSku3IdCookie.setMaxAge(3 * 24 * 60 * 60);
			addonSku3IdCookie.setValue(addonSku3Id);
			addonSku3IdCookie.setPath("/");
			pResponse.addCookie(addonSku3IdCookie);
		}
		
		String addonSkuId = pRequest.getQueryParameter("addonSkuId");
		if (!StringUtils.isEmpty(addonSkuId)) {
			Cookie addonSkuIdCookie = new Cookie("addonSkuId","addonSkuId");
			addonSkuIdCookie.setMaxAge(3 * 24 * 60 * 60);
			addonSkuIdCookie.setValue(addonSkuId);
			addonSkuIdCookie.setPath("/");
			pResponse.addCookie(addonSkuIdCookie);
		}
		
		String addonId = pRequest.getQueryParameter("addonId");
		if (!StringUtils.isEmpty(addonId)) {
			Cookie addonIdCookie = new Cookie("addonId","addonId");
			addonIdCookie.setMaxAge(3 * 24 * 60 * 60);
			addonIdCookie.setValue(addonId);
			addonIdCookie.setPath("/");
			pResponse.addCookie(addonIdCookie);
		}
	}*/
}
