package com.cricket.commerce.order;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpSession;

import atg.commerce.CommerceException;
import atg.commerce.claimable.ClaimableException;
import atg.commerce.claimable.ClaimableManager;
import atg.commerce.order.Order;
import atg.commerce.pricing.PricingConstants;
import atg.commerce.pricing.PricingException;
import atg.commerce.pricing.PricingModelHolder;
import atg.commerce.pricing.PricingTools;
import atg.commerce.promotion.DuplicatePromotionException;
import atg.commerce.promotion.PromotionException;
import atg.commerce.promotion.PromotionTools;
import atg.core.util.StringUtils;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.projects.store.logging.LogUtils;
import atg.projects.store.order.purchase.StorePurchaseProcessHelper;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.ServletUtil;

import com.cricket.common.constants.CricketCommonConstants;

public class CricketPurchaseProcessHelper extends StorePurchaseProcessHelper {

	
	public static final String MSG_EMPTY_TELESALES_CODE = "cricket_checkout_telesales_code";
	
	public static String couponErrorMessage = null;
	
	
	private ClaimableManager mClaimableManager;
	
	PromotionTools mPromotionTools;	
	
	/**
	 * @return the couponErrorMessage - Error Message.
	 */
	public String getCouponErrorMessage() {
		return couponErrorMessage;
	}
	/**
	 * @param couponErrorMessage
	 *            - couponErrorMessage to set.
	 */
	public void setCouponErrorMessage(String pCouponErrorMessage) {
		couponErrorMessage = pCouponErrorMessage;
	}
	/**
	 * @return the mPromotionTools.
	 */
	public PromotionTools getPromotionTools() {
		return mPromotionTools;
	}
	/**
	 * @param mPromotionTools
	 *            - the mPromotionTools to set.
	 */
	public void setPromotionTools(PromotionTools pPromotionTools) {
		mPromotionTools = pPromotionTools;
	}

	/**
	 * @return the claimable manager.
	 */
	public ClaimableManager getClaimableManager() {
		return mClaimableManager;
	}

	/**
	 * @param pClaimableManager
	 *            - the claimable manager to set.
	 */
	public void setClaimableManager(ClaimableManager pClaimableManager) {
		mClaimableManager = pClaimableManager;
	}

	private PricingTools mPricingTools;

	/**
	 * @param pPricingTools
	 *            - pricing tools.
	 */
	public void setPricingTools(PricingTools pPricingTools) {
		mPricingTools = pPricingTools;
	}

	/**
	 * @return mPricingTools - pricing tools.
	 */
	public PricingTools getPricingTools() {
		return mPricingTools;
	}

	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	public void repriceOrder(Order pOrder,
			PricingModelHolder pUserPricingModels, Locale pUserLocale,
			RepositoryItem pProfile) throws PricingException {

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
	    		logDebug("Entering into CricketPurchaseProcessHelper class of repriceOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}

		try {

			if (isLoggingDebug()) {
				logDebug("Repricing w/ pricing tools (priceOrderTotal.");
			}

			getPricingTools().performPricingOperation(
					PricingConstants.OP_REPRICE_ORDER_TOTAL, pOrder,
					pUserPricingModels, pUserLocale, pProfile, null);
		} catch (PricingException pe) {
			if (isLoggingError()){
				vlogError("Error w/ PricingTools.priceOrderTotal::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, pe);
			}			
			throw pe;
		} catch (Exception e) {
			if (isLoggingError()){
				vlogError("Exception w/ CricketPurchaseProcessHelper::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
			if (isLoggingError()) {
				logError(LogUtils.formatMajor(CricketCommonConstants.EMPTY_STRING), e);
			}
		}
		if (isLoggingDebug()) {				
	    		logDebug("Exiting into CricketPurchaseProcessHelper class of repriceOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean tenderCoupon(String pCouponCode, CricketOrderImpl pOrder,
			RepositoryItem pProfile, PricingModelHolder pUserPricingModels,
			Locale pUserLocale) throws CommerceException,
			IllegalArgumentException {

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
	    		logDebug("Entering into CricketPurchaseProcessHelper class of tenderCoupon() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}

		TransactionDemarcation td = new TransactionDemarcation();
		CricketOrderManager orderManager = (CricketOrderManager) getOrderManager();
		String currentCouponCode = orderManager.getCouponCode(pOrder);
		String tenderedCouponCode = pCouponCode;
		setCouponErrorMessage(null);

		boolean removeCouponOnly = false;
		boolean rollback = true;
		boolean canClaimTenderedCoupon = false;

		// If this coupon has already been claimed just return.
		if (!StringUtils.isEmpty(tenderedCouponCode)
				&& tenderedCouponCode.equals(currentCouponCode)) {
			if (isLoggingDebug()) {
				logDebug("Coupon already Applied");
			}
			return true;
		}

		// An empty coupon indicates we want to remove the currently applied
		// coupon.
		if (StringUtils.isEmpty(tenderedCouponCode)) {
			removeCouponOnly = true;
		}

		// If there is no coupon applied and no coupon entered just return.
		if (removeCouponOnly && StringUtils.isEmpty(currentCouponCode)) {
			setCouponErrorMessage(CricketCommonConstants.ENTER_PROMO_CODE_ERROR);
			return true;
			
		}

		try {
			td.begin(getTransactionManager(), TransactionDemarcation.REQUIRED);

			// Remove Existing Coupon.
			if (!StringUtils.isEmpty(currentCouponCode)) {
				if (removeCouponOnly) {
					removeCoupon(pOrder, pProfile, true, pUserPricingModels,
							pUserLocale);
					if (isLoggingDebug()) {
						logDebug("Removing Coupon Code:" + currentCouponCode);
					}
				} else {
					removeCoupon(pOrder, pProfile, false, pUserPricingModels,
							pUserLocale);
				}
			}
			
			if (!removeCouponOnly) {
				canClaimTenderedCoupon = getClaimableManager().canClaimCoupon(
						pProfile.getRepositoryId(), tenderedCouponCode);

				// get promotion exception message.
				if (canClaimTenderedCoupon != true) {
					RepositoryItem coupon = getClaimableManager()
							.getClaimableTools().getClaimableItem(
									tenderedCouponCode);
					if (coupon != null) {
						String promotionsPropertyName = getClaimableManager()
								.getClaimableTools()
								.getPromotionsPropertyName();
						Set promotions = (Set) coupon
								.getPropertyValue(promotionsPropertyName);
						for (Iterator it = promotions.iterator(); it.hasNext();) {
							RepositoryItem repositoryItemPromotion = (RepositoryItem) it
									.next();
							try {
								canClaimTenderedCoupon = getClaimableManager()
										.getPromotionTools()
										.checkPromotionGrant(pProfile,
												repositoryItemPromotion);
							} catch (PromotionException pe) {
								if (isLoggingError()){
									vlogError("PromotionException w/ CricketPurchaseProcessHelper.tenderCoupon::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, pe);
								}
								setCouponErrorMessage(pe.getMessage());
								logDebug("Unable to claim coupon: {0} "+ pe.getMessage());
							}
						}
					} else {
						setCouponErrorMessage(CricketCommonConstants.INVALID_PROMO_CODE_MESSAGE);
					}					
				} 
				//Check Coupon is expired or not 
				RepositoryItem coupon = getClaimableManager().getClaimableTools().getClaimableItem(tenderedCouponCode);
				if(coupon!= null){
					if(!getClaimableManager().checkExpireDate(coupon)){
						if (canClaimTenderedCoupon) {
							claimCoupon(tenderedCouponCode, pOrder, pProfile, true,
									pUserPricingModels, pUserLocale);
							if (isLoggingDebug()) {
								logDebug("Applied Coupon Code:" + tenderedCouponCode);
							}
						} else {
							if (!StringUtils.isEmpty(currentCouponCode)) {
								claimCoupon(currentCouponCode, pOrder, pProfile, false,
										pUserPricingModels, pUserLocale);
								if (isLoggingDebug()) {
									logDebug("Retaining old Coupon Code:" + currentCouponCode);
								}
							}
						}
					} else {
						if (isLoggingDebug()) {
							logDebug("Unable to Claim Expired Coupon:" + coupon);
						}
						setCouponErrorMessage(CricketCommonConstants.COUPON_CODE_EXPIRED_MESSAGE);
					}
				}
			}
			getOrderManager().updateOrder(pOrder);
			rollback = false;
			return (removeCouponOnly || canClaimTenderedCoupon);
		} catch (Exception exception) {
			if (isLoggingError()){
				vlogError("Exception w/ CricketPurchaseProcessHelper.tenderCoupon::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, exception);
			}
			logError("cupon error : " + exception);
			throw new CommerceException(exception);

		} finally {
			try {
				td.end(rollback);
			} catch (TransactionDemarcationException transactionDemarcationException) {
				if (isLoggingError()){
					vlogError("TransactionDemarcationException w/ CricketPurchaseProcessHelper.tenderCoupon::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, transactionDemarcationException);
				}
				throw new CommerceException(transactionDemarcationException);
			}
			if (isLoggingDebug()) {	
		    		logDebug("Exiting from CricketPurchaseProcessHelper class of tenderCoupon() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
		}
	}

	/**
	 * This method claims a coupon specified by its code for a specific user and
	 * order and reprices the order.
	 * 
	 * @param pCouponCode
	 *            - coupon code to be claimed.
	 * @param pOrder
	 *            - order to be re-priced when the coupon has been claimed.
	 * @param pProfile
	 *            - user who claims a coupon.
	 * @param pUserPricingModels
	 *            - user's pricing models to be used for order reprice process.
	 * @param pUserLocale
	 *            - user's locale to be used when re-pricing order.
	 * 
	 * @throws CommerceException
	 *             - if something goes wrong.
	 * @throws IllegalArgumentException
	 *             - if order has a claimed coupon already.
	 */
	public void claimCoupon(String pCouponCode, CricketOrderImpl pOrder,
			RepositoryItem pProfile, PricingModelHolder pUserPricingModels,
			Locale pUserLocale) throws CommerceException,
			IllegalArgumentException {

		claimCoupon(pCouponCode, pOrder, pProfile, true, pUserPricingModels,
				pUserLocale);
	}

	/**
	 * This method claims a coupon specified by its code for a specific user and
	 * order. The order is re-priced if the 'pRepriceOrder' parameter is true.
	 * 
	 * @param pCouponCode
	 *            - coupon code to be claimed.
	 * @param pOrder
	 *            - order to be re-priced when the coupon has been claimed.
	 * @param pProfile
	 *            - user who claims a coupon.
	 * @param pRepriceOrder
	 *            - boolean flag to indicate if order should be re-priced.
	 * @param pUserPricingModels
	 *            - user's pricing models to be used for order re-price process.
	 * @param pUserLocale
	 *            - user's locale to be used when re-pricing order.
	 * 
	 * @throws CommerceException
	 *             - if something goes wrong.
	 * @throws IllegalArgumentException
	 *             - if order has a claimed coupon already.
	 */
	public void claimCoupon(String pCouponCode, CricketOrderImpl pOrder,
			RepositoryItem pProfile, boolean pRepriceOrder,
			PricingModelHolder pUserPricingModels, Locale pUserLocale)
			throws CommerceException, IllegalArgumentException {

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
	    		logDebug("Entering into CricketPurchaseProcessHelper class of claimCoupon() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}

		// First, check the coupon code to be used.
		if (StringUtils.isBlank(pCouponCode)) {
			return;
		}

		CricketOrderManager orderManager = (CricketOrderManager) getOrderManager();

		if (!StringUtils.isEmpty(orderManager.getCouponCode(pOrder))) {
			throw new IllegalArgumentException(
					"There is a coupon claimed for order specified!");
		}
		TransactionDemarcation td = new TransactionDemarcation();
		boolean shouldRollback = true;
		try {
			td.begin(getTransactionManager());
			
			getClaimableManager().claimCoupon(pProfile.getRepositoryId(),
					pCouponCode);

			if (pRepriceOrder) {
				pUserPricingModels.initializePricingModels();
				repriceOrder(pOrder, pUserPricingModels, pUserLocale, pProfile);
			}
		
			shouldRollback = false;
		} catch (ClaimableException e) {
			if (isLoggingError()){
				vlogError("ClaimableException w/ CricketPurchaseProcessHelper.claimCoupon::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
			if (e.getCause() instanceof DuplicatePromotionException) {
				return;
			}
			throw e;
		} catch (TransactionDemarcationException e) {
			if (isLoggingError()){
				vlogError("TransactionDemarcationException w/ CricketPurchaseProcessHelper.claimCoupon::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
			throw new CommerceException(e);
		} finally {
			try {
				td.end(shouldRollback);
			} catch (TransactionDemarcationException e) {
				if (isLoggingError()){
					vlogError("TransactionDemarcationException w/ CricketPurchaseProcessHelper.claimCoupon::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
				throw new CommerceException(e);
			}
			if (isLoggingDebug()) {						
		    		logDebug("Exiting from CricketPurchaseProcessHelper class of claimCoupon() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
		}
	}

	/**
	 * This method removes a coupon from the order specified and re-prices the
	 * order.
	 * 
	 * @param pOrder
	 *            - order with coupon claimed.
	 * @param pProfile
	 *            - user who removes a coupon.
	 * @param pUserPricingModels
	 *            - user's pricing models to be used in order re-pricing
	 *            process.
	 * @param pUserLocale
	 *            - user's locale to be used when re-pricing order.
	 * 
	 * @throws CommerceException
	 *             - if something goes wrong.
	 */
	public void removeCoupon(CricketOrderImpl pOrder, RepositoryItem pProfile,
			PricingModelHolder pUserPricingModels, Locale pUserLocale)
			throws CommerceException {

		removeCoupon(pOrder, pProfile, true, pUserPricingModels, pUserLocale);
	}

	/**
	 * This method removes a coupon from the order specified. The order is
	 * re-priced if the 'pRepriceOrder' parameter is true.
	 * 
	 * @param pOrder
	 *            - order with coupon claimed.
	 * @param pProfile
	 *            - user who removes a coupon.
	 * @param pRepriceOrder
	 *            - boolean flag to indicate if order should be re-priced.
	 * @param pUserPricingModels
	 *            - user's pricing models to be used in order re-pricing
	 *            process.
	 * @param pUserLocale
	 *            - user's locale to be used when re-pricing order.
	 * 
	 * @throws CommerceException
	 *             - if something goes wrong.
	 */
	public void removeCoupon(CricketOrderImpl pOrder, RepositoryItem pProfile,
			boolean pRepriceOrder, PricingModelHolder pUserPricingModels,
			Locale pUserLocale) throws CommerceException {

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
	    		logDebug("Entering into CricketPurchaseProcessHelper class of removeCoupon() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		RepositoryItem profile = pProfile;

		CricketOrderManager orderManager = (CricketOrderManager) getOrderManager();
		String couponCode = orderManager.getCouponCode(pOrder);

		if (couponCode == null) {
			return;
		}
		TransactionDemarcation td = new TransactionDemarcation();
		boolean shouldRollback = true;

		try {
			td.begin(getTransactionManager());
			RepositoryItem coupon = getClaimableManager().claimItem(couponCode);
			String promotionsPropertyName = getClaimableManager()
					.getClaimableTools().getPromotionsPropertyName();
			// Ok to suppress because coupon.promotions contains a set of
			// promotions (in form of repository items).
			@SuppressWarnings("unchecked")
			Collection<RepositoryItem> promotions = (Collection<RepositoryItem>) coupon
					.getPropertyValue(promotionsPropertyName);

			if (!(profile instanceof MutableRepositoryItem)) {

				profile = ((MutableRepository) profile.getRepository())
						.getItemForUpdate(profile.getRepositoryId(), profile
								.getItemDescriptor().getItemDescriptorName());
			}
			for (RepositoryItem promotion : promotions) {
				getClaimableManager().getPromotionTools().removePromotion(
						(MutableRepositoryItem) profile, promotion, false);
			}
			if (pRepriceOrder) {
				pUserPricingModels.initializePricingModels();
				repriceOrder(pOrder, pUserPricingModels, pUserLocale, profile);
			}
			shouldRollback = false;
		} catch (TransactionDemarcationException e) {
			if (isLoggingError()){
				vlogError("TransactionDemarcationException w/ CricketPurchaseProcessHelper.removeCoupon::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
			throw new CommerceException(e);
		} catch (RepositoryException e) {
			if (isLoggingError()){
				vlogError("RepositoryException w/ CricketPurchaseProcessHelper.removeCoupon::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
			throw new CommerceException(e);
		} finally {
			try {
				td.end(shouldRollback);
			} catch (TransactionDemarcationException e) {
				if (isLoggingError()){
					vlogError("TransactionDemarcationException w/ CricketPurchaseProcessHelper.removeCoupon::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
				throw new CommerceException(e);
			}
			if (isLoggingDebug()) {					
		    		logDebug("Exiting from CricketPurchaseProcessHelper class of removeCoupon() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
		}
	}

}
