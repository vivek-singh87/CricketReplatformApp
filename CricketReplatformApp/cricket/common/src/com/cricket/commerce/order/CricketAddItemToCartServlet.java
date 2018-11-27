package com.cricket.commerce.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import atg.commerce.CommerceException;
import atg.commerce.claimable.ClaimableException;
import atg.commerce.order.AddItemToCartServlet;
import atg.commerce.order.CommerceItem;
import atg.commerce.order.purchase.PurchaseProcessConfiguration;
import atg.commerce.order.purchase.PurchaseProcessHelper;
import atg.commerce.order.purchase.PurchaseUserMessage;
import atg.commerce.pricing.PricingModelHolder;
import atg.commerce.promotion.PromotionException;
import atg.commerce.util.NoLockNameException;
import atg.commerce.util.TransactionLockService;
import atg.core.util.ResourceUtils;
import atg.core.util.StringUtils;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.service.lockmanager.DeadlockException;
import atg.service.lockmanager.LockManagerException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;

import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.commerce.order.session.UpgradeItemDetailsSessionBean;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.configuration.CricketConfiguration;

public class CricketAddItemToCartServlet extends AddItemToCartServlet {

	private String couponParameter;
	protected String couponCode;
	private PurchaseProcessHelper purchaseProcessHelper;
	private PricingModelHolder userPricingModels;
	private CricketConfiguration cricketConfiguration;
	private UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean;
	private CartConfiguration cartConfiguration;
	/**
	 * @return the userPricingModels
	 */
	public PricingModelHolder getUserPricingModels() {
		return userPricingModels;
	}

	/**
	 * @param userPricingModels
	 *            the userPricingModels to set
	 */
	public void setUserPricingModels(PricingModelHolder userPricingModels) {
		this.userPricingModels = userPricingModels;
	}

	public CricketAddItemToCartServlet() {
		super();
	}

	protected boolean validateParameters() {
		if (getCatalogRefId() == null) {
			if (isLoggingError())
				logError(ResourceUtils.getMsgResource("NullCatalogRefId",
						"atg.commerce.order.OrderResources", sResourceBundle));
			return false;
		}
		if (getCouponCode() == null) {
			if (isLoggingError())
				logError("Coupon code in url is NULL");
			return false;
		}
		if (getProductId() == null) {
			if (isLoggingError())
				logError(ResourceUtils.getMsgResource("NullProductId",
						"atg.commerce.order.OrderResources", sResourceBundle));
			return false;
		}
		if (getQuantityString() == null) {
			if (isLoggingError())
				logError(ResourceUtils.getMsgResource("NullQuantity",
						"atg.commerce.order.OrderResources", sResourceBundle));
			return false;
		}
		long quantity = 0L;
		try {
			quantity = Long.parseLong(getQuantityString());
		} catch (NumberFormatException e) {
			if (isLoggingError())
				logError(e);
			return false;
		}
		if (quantity <= 0L) {
			if (isLoggingError())
				logError(ResourceUtils.getMsgResource(
						"InvalidQuantityParameter",
						"atg.commerce.order.OrderResources", sResourceBundle));
			return false;
		} else {
			setQuantity(quantity);
			return true;
		}
	}

	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws IOException,
			ServletException {
		
		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}

		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
		if (isLoggingDebug()) {				
	    		logDebug("Entering into CricketAddItemToCartServlet class of service() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		String profileId;
		TransactionLockService service;
		PurchaseProcessConfiguration configuration = getConfiguration();
		profileId = configuration.getProfile().getRepositoryId();
		service = configuration.getTransactionLockFactory().getServiceInstance(
				profileId, this);
		TransactionDemarcation td = new TransactionDemarcation();
		try {
			if (!StringUtils.isEmpty(profileId)) {
				service.acquireTransactionLock(profileId);

				td.begin(getTransactionManager(), 3);
				synchronized (getOrder()) {
					if (getParameters(pRequest) && validateParameters()
							&& isCouponValid(getCouponCode()) && checkItemAddable(pRequest, pResponse)
							&& checkUserIntention(pRequest, pResponse)) {
						String locale = null;
						Locale usersLocale = getUserLocale(pRequest, pResponse);
						if (usersLocale != null) {
							locale = usersLocale.toString();
						}
						CommerceItem ci = createCommerceItem(locale);
						if (ci != null) {
							ci = addItemToCart(ci);
							getOrderManager().updateOrder(getOrder());
						}
					} else if (!isCouponValid(getCouponCode())) {
						String error = ((CricketPurchaseProcessHelper) getPurchaseProcessHelper())
								.getCouponErrorMessage();
						if (StringUtils.isEmpty(error)) {
							error = CricketCommonConstants.ADD_TO_CART_ERROR;
						}
						pRequest.getSession().setAttribute(CricketCommonConstants.SPECIAL_URL_ERROR,
								error);
					}
					pResponse.sendRedirect(pResponse.encodeRedirectURL(pRequest
							.getContextPath() + "/?openCart=true"));
				}
			}
		} catch (DeadlockException e) {
			if (isLoggingError()){
				vlogError("isLoggingError:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		} catch (NoLockNameException e) {
			if (isLoggingError()){
				vlogError("NoLockNameException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		} catch (TransactionDemarcationException e) {
			if (isLoggingError()){
				vlogError("TransactionDemarcationException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		} catch (CommerceException e) {
			if (isLoggingError()){
				vlogError("CommerceException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		} finally {
			try {
				td.end();
				service.releaseTransactionLock(profileId);
			} catch (LockManagerException lme) {
				if (isLoggingError()){
					vlogError("LockManagerException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, lme);
				}
			} catch (TransactionDemarcationException e) {
				if (isLoggingError()){
					vlogError("LockManagerException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			}
		}
		if (isLoggingDebug()) {		
	    		logDebug("Exiting from CricketAddItemToCartServlet class of service() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}

	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	
	private boolean checkUserIntention(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}

		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
		if (isLoggingDebug()) {
					
	    		logDebug("Entering into CricketAddItemToCartServlet class of checkUserIntention() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		Boolean isUserIntentionCorrect = Boolean.TRUE;
		String profileId = getOrder().getProfileId();
		RepositoryItem profile = null;
		try {
			profile = getPricingTools().getProfile(profileId);
		} catch (RepositoryException re) {
			if (isLoggingError()){
				vlogError("RepositoryException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, re);
			}
		}
		String msg = null;
		if (!profile.isTransient()) {
			if (getUpgradeItemDetailsSessionBean().getUserIntention()
					.equalsIgnoreCase(
							getCartConfiguration().getUpgradeAddonIntention())
					|| getUpgradeItemDetailsSessionBean().getUserIntention()
							.equalsIgnoreCase(
									getCartConfiguration()
											.getUpgradePlanIntention())) {
				msg = PurchaseUserMessage.format("completeChangePlanAction",
						getUserLocale(pRequest, pResponse));
				pRequest.getSession().setAttribute(CricketCommonConstants.SPECIAL_URL_ERROR, msg);
				isUserIntentionCorrect = Boolean.FALSE;
			}
		}
		if (isUpgradeItemPresentInCart()) {
			msg = PurchaseUserMessage.format("cannotAddPromoItemWithUpgradeItem",
					getUserLocale(pRequest, pResponse));
			pRequest.getSession().setAttribute(CricketCommonConstants.SPECIAL_URL_ERROR, msg);
		}
		if (isLoggingDebug()) {	
	    		logDebug("Exiting from CricketAddItemToCartServlet class of checkUserIntention() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return isUserIntentionCorrect;
	}
	
	@SuppressWarnings("unchecked")
	private boolean isUpgradeItemPresentInCart() {
		boolean isUpgradeItemPresentInCart = Boolean.FALSE;
		//check if only accessories items in cart
		//else error message
		List<CricketCommerceItemImpl> cricketCommerceItemList = getOrder().getCommerceItems();
		for (CricketCommerceItemImpl cricketCommerceItemImpl : cricketCommerceItemList) {
			if (getCartConfiguration().getChangePlanItemType().equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())
					|| getCartConfiguration().getChangeAddOnItemType().equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())
					|| getCartConfiguration().getUpgradePhoneItemType().equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())
					|| getCartConfiguration().getChangeAddOnItemType().equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())) {
				isUpgradeItemPresentInCart = Boolean.TRUE;
				break;
			}
		}
		return isUpgradeItemPresentInCart;
	}
	
	private boolean checkItemAddable(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}

		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
		if (isLoggingDebug()) {		
	    		logDebug("Entering into CricketAddItemToCartServlet class of checkItemAddable() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		Boolean checkItemAddable = Boolean.TRUE;
		String profileId = getOrder().getProfileId();
		RepositoryItem profile = null;
		try {
			profile = getPricingTools().getProfile(profileId);
		} catch (RepositoryException re) {
			if (isLoggingError()){
				vlogError("RepositoryException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, re);
			}
		}
		String networkType = (String) profile
				.getPropertyValue(CricketCommonConstants.NETWORK_PROVIDER);
		String msg = null;
		if (null != networkType) {
			int pkgSize = ((CricketOrderImpl) getOrder()).getCktPackages()
					.size();
			if (pkgSize >= getCricketConfiguration()
					.getMaxNoOfPackageForPayGo()
					&& null != networkType
					&& networkType
							.equalsIgnoreCase(CricketCommonConstants.NETWORKTYPE_SPRINT)) {
				msg = PurchaseUserMessage.format(CricketCommonConstants.PAYGO_PACKAGE_LIMIT,
						getUserLocale(pRequest, pResponse));
				pRequest.getSession().setAttribute(CricketCommonConstants.SPECIAL_URL_ERROR, msg);
				checkItemAddable = Boolean.FALSE;
			} else if (pkgSize >= getCricketConfiguration()
					.getMaxNoOfPackageForPIA()
					&& null != networkType
					&& networkType
							.equalsIgnoreCase(CricketCommonConstants.NETWORKTYPE_CRICKET)) {
				msg = PurchaseUserMessage.format(CricketCommonConstants.PIA_PACKAGE_LIMIT,
						getUserLocale(pRequest, pResponse));
				pRequest.getSession().setAttribute(CricketCommonConstants.SPECIAL_URL_ERROR, msg);
				checkItemAddable = Boolean.FALSE;
			}
		}
		if (isLoggingDebug()) {					
	    		logDebug("Exiting into CricketAddItemToCartServlet class of checkItemAddable() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return checkItemAddable;
	}
	
	@SuppressWarnings("rawtypes")
	private boolean isCouponValid(String tenderedCouponCode) {
		Boolean canClaimTenderedCoupon = Boolean.FALSE;
		String profileId = getOrder().getProfileId();
		RepositoryItem profile = null;
		try {
			profile = getPricingTools().getProfile(profileId);

			canClaimTenderedCoupon = ((CricketPurchaseProcessHelper) getPurchaseProcessHelper())
					.getClaimableManager().canClaimCoupon(
							profile.getRepositoryId(), tenderedCouponCode);

			// get promotion exception message.
			if (canClaimTenderedCoupon != true) {
				RepositoryItem coupon = ((CricketPurchaseProcessHelper) getPurchaseProcessHelper())
						.getClaimableManager().getClaimableTools()
						.getClaimableItem(tenderedCouponCode);
				if (coupon != null) {
					String promotionsPropertyName = ((CricketPurchaseProcessHelper) getPurchaseProcessHelper())
							.getClaimableManager().getClaimableTools()
							.getPromotionsPropertyName();
					Set promotions = (Set) coupon
							.getPropertyValue(promotionsPropertyName);
					for (Iterator it = promotions.iterator(); it.hasNext();) {
						RepositoryItem repositoryItemPromotion = (RepositoryItem) it
								.next();
						try {
							canClaimTenderedCoupon = ((CricketPurchaseProcessHelper) getPurchaseProcessHelper())
									.getClaimableManager()
									.getPromotionTools()
									.checkPromotionGrant(profile,
											repositoryItemPromotion);
						} catch (PromotionException pe) {
							((CricketPurchaseProcessHelper) getPurchaseProcessHelper())
									.setCouponErrorMessage(pe.getMessage());
							logDebug("Unable to claim coupon: {0} "
									+ pe.getMessage());
						}
					}
				}
			}
		} catch (RepositoryException re) {
			if (isLoggingWarning())
				logWarning(re);
		} catch (ClaimableException e) {
			// TODO Auto-generated catch block
			logError(e);
		}
		return canClaimTenderedCoupon;
	}

	protected CommerceItem addItemToCart(CommerceItem ci) {

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
			pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
		}

		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
				
		if (isLoggingDebug()) {				
	    		logDebug("Entering into CricketAddItemToCartServlet class of addItemToCart() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		try {

			ci = getCommerceItemManager().addItemToOrder(getOrder(), ci);

			String profileId = getOrder().getProfileId();
			RepositoryItem profile = null;
			try {
				profile = getPricingTools().getProfile(profileId);
			} catch (RepositoryException re) {
				if (isLoggingError()){
					vlogError("RepositoryException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, re);
				}
			}
			Locale locale = getPricingTools().getLocale(null);

			getPricingTools().performPricingOperation(getPricingOperation(),
					getOrder(), null, locale, profile, null);
			((CricketPurchaseProcessHelper) getPurchaseProcessHelper())
					.tenderCoupon(getCouponCode(),
							((CricketOrderImpl) getOrder()), profile,
							getUserPricingModels(), locale);

		} catch (CommerceException e) {
			if (isLoggingError()){
				vlogError("CommerceException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
			return null;
		}
		if (isLoggingDebug()) {					
	    		logDebug("Exiting from CricketAddItemToCartServlet class of addItemToCart() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return ci;
	}

	protected CricketCommerceItemImpl createCommerceItem(String pLocale) {

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
			pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
		}

		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
		if (isLoggingDebug()) {
					
	    		logDebug("Entering into CricketAddItemToCartServlet class of createCommerceItem() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		CricketCommerceItemImpl ci = null;
		RepositoryItem sku;
		RepositoryItem product;
		try {
			sku = getCatalogTools().findSKU(getCatalogRefId());

			if (sku == null) {
				if (isLoggingError())
					logError(ResourceUtils.getMsgResource("NotValidItem",
							"atg.commerce.order.OrderResources",
							sResourceBundle));
				return null;
			}
			product = getCatalogTools().findProduct(getProductId());
			if (product == null) {
				if (isLoggingError())
					logError(ResourceUtils.getMsgResource("NotValidItem",
							"atg.commerce.order.OrderResources",
							sResourceBundle));
				return null;
			}
			try {
				ci = (CricketCommerceItemImpl) getCommerceItemManager()
						.createCommerceItem(getCatalogRefId(), getProductId(),
								getQuantity(), pLocale);
				String pkgProductType = (String) product
						.getPropertyValue(CricketCommonConstants.TYPE);
				if (!StringUtils.isEmpty(pkgProductType)) {
					ci.setCricItemTypes(pkgProductType);
					if (pkgProductType.equalsIgnoreCase(CricketCommonConstants.PHONE_PRODUCT)) {
						CricketPackage cricketPackage = ((CricketOrderTools) getOrderManager()
								.getOrderTools()).createCricketPackage();
						((CricketOrderTools) getOrderManager().getOrderTools())
								.updateCommerceItem(ci, pkgProductType,
										cricketPackage);
						List<CricketPackage> cricketPackages = new ArrayList<CricketPackage>();
						cricketPackages.add(cricketPackage);
						((CricketOrderImpl) getOrder())
								.setCktPackages(cricketPackages);
					}
				}
			} catch (CommerceException e) {
				if (isLoggingError()){
					vlogError("CommerceException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
				return null;
			}
			if (isLoggingDebug())
				logDebug((new StringBuilder())
						.append("Created commerce item of type ")
						.append(ci.getClass().getName()).toString());
			if (ci == null) {
				if (isLoggingError())
					logError(ResourceUtils.getMsgResource("NotValidItem",
							"atg.commerce.order.OrderResources",
							sResourceBundle));
				return null;
			}
		} catch (IllegalArgumentException e) {
			if (isLoggingError()){
				vlogError("IllegalArgumentException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		} catch (RepositoryException e) {
			if (isLoggingError()){
				vlogError("RepositoryException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		}
		if (isLoggingDebug()) {					
	    		logDebug("Exiting from CricketAddItemToCartServlet class of createCommerceItem() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return ci;
	}

	protected boolean getParameters(DynamoHttpServletRequest pRequest) {
		Boolean valid = super.getParameters(pRequest);
		if (valid) {
			setCouponCode(pRequest.getQueryParameter(getCouponParameter()));
		}
		return valid;
	}

	/**
	 * @return the couponParameter
	 */
	public String getCouponParameter() {
		return couponParameter;
	}

	/**
	 * @param couponParameter
	 *            the couponParameter to set
	 */
	public void setCouponParameter(String couponParameter) {
		this.couponParameter = couponParameter;
	}

	/**
	 * @return the couponCode
	 */
	public String getCouponCode() {
		return couponCode;
	}

	/**
	 * @param couponCode
	 *            the couponCode to set
	 */
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	/**
	 * @return the purchaseProcessHelper
	 */
	public PurchaseProcessHelper getPurchaseProcessHelper() {
		return purchaseProcessHelper;
	}

	/**
	 * @param purchaseProcessHelper
	 *            the purchaseProcessHelper to set
	 */
	public void setPurchaseProcessHelper(
			PurchaseProcessHelper purchaseProcessHelper) {
		this.purchaseProcessHelper = purchaseProcessHelper;
	}

	/**
	 * @return the cricketConfiguration
	 */
	public CricketConfiguration getCricketConfiguration() {
		return cricketConfiguration;
	}

	/**
	 * @param cricketConfiguration the cricketConfiguration to set
	 */
	public void setCricketConfiguration(CricketConfiguration cricketConfiguration) {
		this.cricketConfiguration = cricketConfiguration;
	}

	/**
	 * @return the upgradeItemDetailsSessionBean
	 */
	public UpgradeItemDetailsSessionBean getUpgradeItemDetailsSessionBean() {
		return upgradeItemDetailsSessionBean;
	}

	/**
	 * @param upgradeItemDetailsSessionBean the upgradeItemDetailsSessionBean to set
	 */
	public void setUpgradeItemDetailsSessionBean(
			UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean) {
		this.upgradeItemDetailsSessionBean = upgradeItemDetailsSessionBean;
	}

	/**
	 * @return the cartConfiguration
	 */
	public CartConfiguration getCartConfiguration() {
		return cartConfiguration;
	}

	/**
	 * @param cartConfiguration the cartConfiguration to set
	 */
	public void setCartConfiguration(CartConfiguration cartConfiguration) {
		this.cartConfiguration = cartConfiguration;
	}

}
