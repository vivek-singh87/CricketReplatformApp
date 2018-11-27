package com.cricket.commerce.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import atg.commerce.order.OrderHolder;
import atg.core.util.StringUtils;
import atg.nucleus.naming.ComponentName;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;
import atg.servlet.pipeline.InsertableServletImpl;

import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.commerce.order.session.UpgradeItemDetailsSessionBean;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.integration.esp.vo.OfferingsVO;
import com.cricket.integration.esp.vo.SubscriberVO;
import com.cricket.user.session.UserAccountInformation;
import com.cricket.vo.CricketProfile;

public class UserIntentionServlet extends InsertableServletImpl {
	

	private boolean mEnabled;
	
	private CartConfiguration cartConfiguration;
	
	private ComponentName mSessionBeanComponentName = null;
	
	private String mSessionBeanPath;
	private Repository catalogRepository;

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
	 * @return the sessionBeanPath
	 */
	public String getSessionBeanPath() {
		return mSessionBeanPath;
	}

	/**
	 * @param pSessionBeanPath the sessionBeanPath to set
	 */
	public void setSessionBeanPath(String pSessionBeanPath) {
		mSessionBeanPath = pSessionBeanPath;
		if (mSessionBeanPath != null) {
			mSessionBeanComponentName = ComponentName.getComponentName(mSessionBeanPath);
		} else {
			mSessionBeanComponentName = null;
		}
	}
	
	/**
	 * @return the sessionBeanComponentName
	 */
	public ComponentName getSessionBeanComponentName() {
		return mSessionBeanComponentName;
	}

	/**
	 * @param pSessionBeanComponentName the sessionBeanComponentName to set
	 */
	public void setSessionBeanComponentName(ComponentName pSessionBeanComponentName) {
		mSessionBeanComponentName = pSessionBeanComponentName;
	}

	/**
	 * @return the cartConfiguration
	 */
	public CartConfiguration getCartConfiguration() {
		return cartConfiguration;
	}

	/**
	 * @param pCartConfiguration the cartConfiguration to set
	 */
	public void setCartConfiguration(CartConfiguration pCartConfiguration) {
		cartConfiguration = pCartConfiguration;
	}

	/**
	 * Service method
	 */
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws IOException,
			ServletException {
		
		if (isLoggingDebug()) {
			logDebug("Inside UserIntentionServlet : service method");
		}
		
		String userIntention = pRequest.getQueryParameter(getCartConfiguration().getIntentionalURLParameterName());
		if (isEnabled() && !StringUtils.isEmpty(userIntention)) {
			
			if (isLoggingDebug()) {
				logDebug("User Intention is : " + userIntention);
			}
			UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean = (UpgradeItemDetailsSessionBean) pRequest
					.resolveName(mSessionBeanComponentName);
			upgradeItemDetailsSessionBean.setUserIntention(userIntention);
			if (userIntention.equalsIgnoreCase(getCartConfiguration().getAddLineIntention())
					|| userIntention.equalsIgnoreCase(getCartConfiguration().getUpgradePhoneIntention())
					|| userIntention.equalsIgnoreCase(getCartConfiguration().getUpgradePlanIntention())
					|| userIntention.equalsIgnoreCase(getCartConfiguration().getUpgradeAddonIntention())) {
			clearSessionBean(upgradeItemDetailsSessionBean);
			upgradeItemDetailsSessionBean.setUserIntention(userIntention);
			setSessionDataFromCookie(pRequest, upgradeItemDetailsSessionBean);
			}
		}
		
		if (isLoggingDebug()) {
			logDebug("Exit UserIntentionServlet : service method");
		}
		super.service(pRequest, pResponse);
	}

	/**
	 * 
	 * @param pRequest
	 * @param upgradeItemDetailsSessionBean
	 */
	@SuppressWarnings("unchecked")
	private void setSessionDataFromCookie(DynamoHttpServletRequest pRequest,
			UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean) {
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }		
	    		logDebug("Entering into UserIntentionServlet class of setSessionDataFromCookie() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		//String addonId1 = null;
		//String addonSkuId1 = null;
		//String addonId2 = null;
		//String addonSkuId2 = null;
		//String addonId3 = null;
		//String addonSkuId3 = null;
		String addonProductId = null;
		String addonSkuId = null;
		String phone_skuId = null;
		Set<RepositoryItem> parentProducts = new HashSet<RepositoryItem>();
		String parentProduct = null;
		String planSkuId=null;
		List<String> addonSkuIds = new ArrayList<String>();
		List<String> addonProductIds = new ArrayList<String>();
		Map<String, String> removedAddons = new HashMap<String, String>();
		Cookie[] cookies = pRequest.getCookies();
		CricketProfile cricketProfile = (CricketProfile) pRequest.resolveName(CricketCookieConstants.ATG_CRICKET_UTIL_CRICKET_PROFILE);
		UserAccountInformation userAccountInfo = (UserAccountInformation) pRequest.resolveName("/com/cricket/user/session/UserAccountInformation");
		/*if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];*/
				// Check if contains created cookie
				
			if (!StringUtils.isEmpty(cricketProfile.getDeviceModel())) {
					
					try {
						RepositoryView reposView = getCatalogRepository().getView(CricketCommonConstants.PHONE_SKU);
						RqlStatement statement = RqlStatement.parseRqlStatement("modelNumber=?0");
						Object[] inputParams = new Object[1];
						inputParams[0] = cricketProfile.getDeviceModel();
						RepositoryItem[] phone_skus = statement.executeQuery(reposView, inputParams);
						if (null != phone_skus && phone_skus.length > 0) {
						for (int j = 0; j < phone_skus.length; j++) {
							RepositoryItem repositoryItem = phone_skus[j];
							phone_skuId=(String)repositoryItem.getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
							parentProducts=(Set)repositoryItem.getPropertyValue(CricketCommonConstants.PROP_PARENT_PRODUCTS);
							for (Iterator iterator = parentProducts.iterator(); iterator.hasNext();) {
								RepositoryItem rItem = (RepositoryItem) iterator.next();
								parentProduct = rItem.getRepositoryId();
								break;
							}
						}
						}
					} catch (RepositoryException e) {      
						if(isLoggingError()){
							logError("Repository Exception in setSessionDataFromCookie of CricketOrderTools",e);
						}
					}
					upgradeItemDetailsSessionBean.setRemovedPhoneId(parentProduct);
					upgradeItemDetailsSessionBean.setRemovedPhoneSkuId(phone_skuId);
					upgradeItemDetailsSessionBean.setModelNumber(cricketProfile.getDeviceModel());
				}

				if (!StringUtils.isEmpty(cricketProfile.getRatePlanCode())) {
					try {/*
						RepositoryView reposView = getCatalogRepository().getView("phone-product");
						RqlStatement statement = RqlStatement.parseRqlStatement("id=?0");
						Object[] inputParams = new Object[1];
						inputParams[0] = cricketProfile.getRatePlanCode();
						RepositoryItem[] phone_skus = statement.executeQuery(reposView, inputParams);
						if (null != phone_skus && phone_skus.length > 0) {
						for (int j = 0; j < phone_skus.length; j++) {
							RepositoryItem repositoryItem = phone_skus[j];
							planSkuId=(String)repositoryItem.getPropertyValue("childSKUs");
						}
					}
					*/
						RepositoryItem planProductItem = getCatalogRepository().getItem(cricketProfile.getRatePlanCode(),CricketCommonConstants.PLAN_PRODUCT);
						if (null != planProductItem) {
							List<RepositoryItem> plan_skus = (List<RepositoryItem>) planProductItem.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
						if (null != plan_skus && !plan_skus.isEmpty()) {
							for (RepositoryItem repositoryItem : plan_skus) {
								planSkuId = repositoryItem.getRepositoryId();
							}
						}
						}
					} catch (RepositoryException e) {      
						if(isLoggingError()){
							logError("Repository Exception in getCommerceItemsForPackage of UserIntentionServlet",e);
						}
					}
					if(cricketProfile.getRatePlanCode().equalsIgnoreCase(CricketCommonConstants._4P5)){
						upgradeItemDetailsSessionBean.setPlan4p5(true);
					}
					upgradeItemDetailsSessionBean.setRemovedPlanId(cricketProfile.getRatePlanCode());
					upgradeItemDetailsSessionBean.setRemovedPlanSkuId(planSkuId);
				}
				
				if (!StringUtils.isEmpty(cricketProfile.getMdn())) {
					//Fixing Null Pointer issue 
					if(isLoggingDebug()){
						logDebug("UserAccountInfo:::"+userAccountInfo);
						logDebug("Subscribers:::"+userAccountInfo.getSubscribers());
					}
					if(userAccountInfo!=null && userAccountInfo.getSubscribers()!=null) {
						
					List<SubscriberVO> subscribers = userAccountInfo.getSubscribers();
					if (null != subscribers && !subscribers.isEmpty()) {
					for (SubscriberVO subscriberVO : subscribers) {
						if(cricketProfile.getMdn().equalsIgnoreCase(subscriberVO.getMdn())) {
							List<OfferingsVO> additionalOfferings = subscriberVO.getAdditionalOfferings();
							if(additionalOfferings != null && additionalOfferings.size() > 0){
								for (OfferingsVO offeringsVO : additionalOfferings) {
									if (null != offeringsVO && offeringsVO.getOfferTypeId() != 3 
											&& !StringUtils.isEmpty(offeringsVO.getOfferingName())
											&& !CricketCommonConstants.REC_OFFERING_NAME.equals(offeringsVO.getOfferingName())) {
										addonProductId = offeringsVO.getOfferingName();
										addonProductIds.add(addonProductId);
										try {
											RepositoryItem addOnProductItem = getCatalogRepository().getItem(addonProductId,CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT);
											if (null != addOnProductItem) {
												List<RepositoryItem> addon_skus =  (List<RepositoryItem>) addOnProductItem.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
											if (null != addon_skus && !addon_skus.isEmpty()) {
												for (RepositoryItem repositoryItem : addon_skus) {
													addonSkuId = repositoryItem.getRepositoryId();
												}
											}
											addonSkuIds.add(addonSkuId);
											removedAddons.put(addonProductId, addonSkuId);
											}
										} catch (RepositoryException e) {      
											if(isLoggingError()){
												logError("Repository Exception in getCommerceItemsForPackage of UserIntentionServlet",e);
											}
										}
									}
								}
							}
						}
					}
				}
			if (!addonProductIds.isEmpty()) {
				upgradeItemDetailsSessionBean.setAddonProductIds(addonProductIds);;
			}
			if (!addonSkuIds.isEmpty()) {
				upgradeItemDetailsSessionBean.setAddonSkuIds(addonSkuIds);
			}
			if (!removedAddons.isEmpty()) {
				upgradeItemDetailsSessionBean.setRemovedAddons(removedAddons);
			}
			}
			upgradeItemDetailsSessionBean.setMdn(cricketProfile.getMdn());
		}
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 	
	    		logDebug("Exiting from UserIntentionServlet class of setSessionDataFromCookie() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
	
	private void clearSessionBean(UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean) {
		upgradeItemDetailsSessionBean.setRemovedAddons(null);
		upgradeItemDetailsSessionBean.setRemovedPhoneId(null);
		upgradeItemDetailsSessionBean.setRemovedPhoneSkuId(null);
		upgradeItemDetailsSessionBean.setRemovedPlanId(null);
		upgradeItemDetailsSessionBean.setRemovedPlanSkuId(null);
		upgradeItemDetailsSessionBean.setUserIntention(null);
		upgradeItemDetailsSessionBean.setMdn(null);
		upgradeItemDetailsSessionBean.setModelNumber(null);
	}

	/**
	 * @return the catalogRepository
	 */
	public Repository getCatalogRepository() {
		return catalogRepository;
	}

	/**
	 * @param catalogRepository the catalogRepository to set
	 */
	public void setCatalogRepository(Repository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}
	
}
