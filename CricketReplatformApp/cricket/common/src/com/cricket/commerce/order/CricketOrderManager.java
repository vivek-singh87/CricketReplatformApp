package com.cricket.commerce.order;


import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.transaction.TransactionManager;

import atg.commerce.CommerceException;
import atg.commerce.claimable.ClaimableManager;
import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.OrderTools;
import atg.commerce.pricing.PricingModelHolder;
import atg.commerce.profile.CommerceProfileTools;
import atg.commerce.promotion.PromotionTools;
import atg.core.util.StringUtils;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.projects.store.order.StoreOrderManager;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.servlet.ServletUtil;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.order.CricketOrder;

public class CricketOrderManager extends StoreOrderManager {
	
	private CricketOrderTools cricketOrderTools;
	
	private RqlStatement mBillingSystemOrderQuery;
	
	private CricketPurchaseProcessHelper purchaseProcessHelper;
	
	public CricketPurchaseProcessHelper getPurchaseProcessHelper() {
		return purchaseProcessHelper;
	}

	public void setPurchaseProcessHelper(
			CricketPurchaseProcessHelper pPurchaseProcessHelper) {
		purchaseProcessHelper = pPurchaseProcessHelper;
	}

	/**
	 * @return the cricketOrderTools
	 */
	public CricketOrderTools getCricketOrderTools() {
		return cricketOrderTools;
	}

	/**
	 * @param cricketOrderTools the cricketOrderTools to set
	 */
	public void setCricketOrderTools(CricketOrderTools cricketOrderTools) {
		this.cricketOrderTools = cricketOrderTools;
	}
	
	/**
	 * @param packageId
	 * @return
	 */
/*	public RepositoryItem[] getCommerceItemsByPackageId(String packageId) {
		return getCricketOrderTools().getCommerceItemswithUniquePackageId(packageId);
		
	}*/
	
	/*public RepositoryItem findItem(String productId,String skuId){
		return getCricketOrderTools().findItem(productId,skuId);
	}*/
	
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	
	public void clearCart(CricketOrderImpl pOrder,RepositoryItem pProfile,PricingModelHolder pUserPricingModels,Locale pUserLocale) {

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
			pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
		}

		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != pOrder){
				if(!StringUtils.isBlank(pOrder.getId())){
					orderId = pOrder.getId();
				}
			}
				
		if (isLoggingDebug()) {				
	    		logDebug("Entering into CricketOrderManager class of clearCart() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		TransactionManager tm = getOrderTools().getTransactionManager();
		TransactionDemarcation td = new TransactionDemarcation();
		try {
			td.begin(tm);
			synchronized (pOrder) {
				if(isLoggingDebug()){
					logDebug("Clearing Cart Items as Zipcode is Changed ");
				}
				getCommerceItemManager().removeAllCommerceItemsFromOrder(pOrder);
				pOrder.removeAllCricketPackages();
				getPurchaseProcessHelper().removeCoupon(pOrder, pProfile, pUserPricingModels,pUserLocale);
				updateOrder(pOrder);
			}
		} catch (CommerceException e) {
			if (isLoggingError()){
				vlogError("Error while Clearing items from Cart in ChangeUserLocationBasedOnZipCode FormHandler : " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		} catch (TransactionDemarcationException e) {
			if (isLoggingError()){
				vlogError("TransactionDemarcationException while Clearing items from Cart in ChangeUserLocationBasedOnZipCode FormHandler : " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		}  finally {
			try {
				td.end();
			} catch (TransactionDemarcationException e) {
				if (isLoggingError()){
					vlogError("TransactionDemarcationException in service method of class ChangeUserLocationBasedOnZipCode: " + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
				if (isLoggingDebug()) {
					logDebug("There was a TransactionDemarcationException while updating order in clearCart Method of class CircketOrderManager");
				}
			}
		}
		if (isLoggingDebug()) {				
	    		logDebug("Exiting from CricketOrderManager class of clearCart() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
	@SuppressWarnings("unchecked")
	public String getCouponCode(CricketOrderImpl pOrder)
			throws CommerceException {

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
			pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
		}

		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(pOrder.getId())){
				orderId = pOrder.getId();
			}	
		if (isLoggingDebug()) {				
	    		logDebug("Entering into CricketOrderManager class of getCouponCode() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		try {

			OrderTools orderTools = getOrderTools();
			Repository profileRepository = orderTools.getProfileRepository();
			CommerceProfileTools profileTools = orderTools.getProfileTools();
			ClaimableManager claimableManager = profileTools
					.getClaimableManager();
			PromotionTools promotionTools = claimableManager
					.getPromotionTools();

			RepositoryItem profile = profileRepository.getItem(pOrder.getProfileId(), profileTools.getDefaultProfileType());

			// Take its active promotions and iterate over them.
			if(null!=profile){
			Collection<RepositoryItem> activePromotions = (Collection<RepositoryItem>) profile.getPropertyValue(promotionTools.getActivePromotionsProperty());
			
			if(activePromotions!= null && activePromotions.size() > 0){
			for (RepositoryItem promotionStatus : activePromotions) {

				// Each promotionStatus contains a list of coupons that
				// activated a promotion. Inspect these coupons.
				Collection<RepositoryItem> coupons = (Collection<RepositoryItem>) promotionStatus
						.getPropertyValue(promotionTools
								.getPromoStatusCouponsPropertyName());

				for (RepositoryItem coupon : coupons) {
					// Proper coupon? I.e. does it came from a shared cart site
					// group?
					if (claimableManager.checkCouponSite(coupon)) {
						// True, return this coupon code.
						String couponId = (String) coupon
								.getPropertyValue(claimableManager
										.getClaimableTools()
										.getIdPropertyName());

						return couponId;
					}
				}
			}}
		}
		} catch (RepositoryException e) {
			throw new CommerceException(e);
		}
		if (isLoggingDebug()) {				
	    		logDebug("Exiting from CricketOrderManager class of getCouponCode() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		// Can't find proper coupon, return nothing.
		return null;
	}
	
	/**
	 * @param pBillingOrderNumber
	 * @return
	 * @throws InvalidParameterException 
	 */
	public CricketOrder getOrderByBillingOrderNumber(String pBillingOrderNumber) throws InvalidParameterException{
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
		
	    		logDebug("Entering into CricketOrderManager class of getOrderByBillingOrderNumber() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		if(isLoggingDebug()){
			logDebug("Fetching order details based on billing order number for InquireOrderDetails Webservice "+getClass().getName());
		}
			
		if(StringUtils.isBlank(pBillingOrderNumber)){
    		  throw new InvalidParameterException("Invoice number cannot be null");
    	}
		 		Repository orderRepository = getOrderTools().getOrderRepository();
		CricketOrder cktOrder=null;
		try {
			RepositoryItem[] orders;
			// It will return the orders by filtering with orderId
			if(isLoggingDebug()){
				logDebug("Running RQL Query to fetch order details based on billingOrder "+pBillingOrderNumber);
			}
			orders = executeRQLQuery(orderRepository, OrderConstants.ORDER,
					getBillingSystemOrderQuery(), pBillingOrderNumber);
			if (orders.length > 0) {
				cktOrder = (CricketOrder)loadOrder(orders[0].getRepositoryId());
				
			} else {
				return null;
			}
			if(isLoggingDebug()){
				logDebug("Fetched order details for InquireOrderDetailsWebservice  "+cktOrder);
			}
		}  catch (CommerceException e) {
			logError("Error while fetching the order "+getClass().getName()+"  "+e,e);
		} catch (RepositoryException e) {
			logError("Error while fetching the order "+getClass().getName() +"  "+e,e);
		}
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}

			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(cktOrder.getId())){
					orderId = cktOrder.getId();
				}		
	    		logDebug("Exiting from CricketOrderManager class of getOrderByBillingOrderNumber() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		 return cktOrder;
		
		
	}
	/**
	 * @param repo
	 * @param itemDescriptor
	 * @param statement
	 * @param params
	 * @return
	 * @throws RepositoryException
	 */
	private static RepositoryItem[] executeRQLQuery(Repository repo,
			String itemDescriptor, RqlStatement statement, Object... params)
			throws RepositoryException {

		RepositoryView view = repo.getView(itemDescriptor);

		RepositoryItem[] items = statement.executeQuery(view, params);
		if (items == null) {
			items = new RepositoryItem[0];
		}

		return items;
	}
	/**
	 * @return the billingSystemOrderQuery
	 */
	public RqlStatement getBillingSystemOrderQuery() {
		return mBillingSystemOrderQuery;
	}

	/**
	 * @param pBillingSystemOrderQuery the billingSystemOrderQuery to set
	 */
	public void setBillingSystemOrderQuery(RqlStatement pBillingSystemOrderQuery) {
		mBillingSystemOrderQuery = pBillingSystemOrderQuery;
	}

}
