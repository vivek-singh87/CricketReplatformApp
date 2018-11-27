package com.cricket.browse;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import atg.commerce.pricing.GetApplicableItemPromotions;
import atg.commerce.pricing.PricingModelHolder;
import atg.core.util.StringUtils;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.userprofiling.Profile;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.configuration.CricketConfiguration;
import com.cricket.vo.MyCricketCookieLocationInfo;

/**
 * @author RM112139
 *
 */
public class ProductListingDroplet extends GetApplicableItemPromotions {
	
	/**
	 * 
	 */
	private BrowseShopManager browseShopManager;
	
	private CricketConfiguration cricketConfiguration;
	
	/**
	 * @return
	 */
	public BrowseShopManager getBrowseShopManager() {
		return browseShopManager;
	}



	/**
	 * @param browseShopManager
	 */
	public void setBrowseShopManager(final BrowseShopManager browseShopManager) {
		this.browseShopManager = browseShopManager;
	}	

	/**
	 * @return
	 */
	public CricketConfiguration getCricketConfiguration() {
		return cricketConfiguration;
	}

	/**
	 * @param cricketConfiguration
	 */
	public void setCricketConfiguration(final CricketConfiguration cricketConfiguration) {
		this.cricketConfiguration = cricketConfiguration;
	}



	/**
	 * @param pRequest
	 * @param pResponse
	 */
	public void service(final DynamoHttpServletRequest pRequest,
			final DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		logInfo("Entering into the service method...");
		long start = System.currentTimeMillis();
		String marketCode = null;
		String marketType = null;
		String categoryId = null;
		String categoryType = null;
		String editPhone = null;
		Profile profile = null;
		Boolean isUserLoggedIn = null;
		boolean upgradePhoneIntention = false;
		String intention = null;
		String phoneId = null;
		categoryId = (String)pRequest.getParameter(CricketCommonConstants.CATEGORY_ID);
		categoryType = (String)pRequest.getParameter(CricketCommonConstants.CATEGORY_TYPE2);
		profile = (Profile)pRequest.resolveName(CricketCommonConstants.PROFILE_PATH);
		isUserLoggedIn = (Boolean)profile.getPropertyValue(CricketCommonConstants.IS_LOGGED_IN);
		intention = (String)pRequest.getParameter(CricketCommonConstants.INTENTION);
		phoneId = (String)pRequest.getParameter(CricketCommonConstants.PHONE_PRODUCT_ID);
		editPhone = (String)pRequest.getParameter(CricketCommonConstants.EDIT_PHONE);
		
		if (intention != null && intention.equalsIgnoreCase(CricketCommonConstants.BUY_ASSOC_ACCESSORY)) {
			if (phoneId != null) {
				List<AccessoryVO>  assocAccessoryList  = getBrowseShopManager().getListOfAccessoryProducts (categoryId, phoneId);
				pRequest.setParameter(CricketCommonConstants.LIST_OF_PRODUCTS, assocAccessoryList);
			}
		} else {
			if(profile.isTransient()){//Non Logged In User
				String marketCodeProfile = null;
				marketCodeProfile = (String)profile.getPropertyValue(CricketCommonConstants.USER_MARKETCODE);
				//String userEnteredZipCode = (String)profile.getPropertyValue("userEnteredZipCode");
				marketCode = marketCodeProfile;
				marketType = (String)profile.getPropertyValue(CricketCommonConstants.MARKET_TYPE);
				/*if(!StringUtils.isEmpty(userEnteredZipCode)){
					zipCode = userEnteredZipCode;
				}*/
			} else {
				
				MyCricketCookieLocationInfo loggedInUserLocationInfo = (MyCricketCookieLocationInfo)pRequest.resolveName(CricketCommonConstants.LOCATION_INFO_PATH);				
				marketCode = loggedInUserLocationInfo.getMarketCode();		
				marketType = (String)profile.getPropertyValue(CricketCommonConstants.MARKET_TYPE);
				if(!StringUtils.isEmpty(intention) && CricketCommonConstants.UPGRADE_PHONE2.equalsIgnoreCase(intention)){
					categoryId = getCricketConfiguration().getAllPhonesCategoryId();
					upgradePhoneIntention = true;
				}
				else if(!StringUtils.isEmpty(intention) && CricketCommonConstants.UPGRADE_PLAN.equalsIgnoreCase(intention)){
					categoryId = getCricketConfiguration().getAllPlansCategoryId();
				}
				else if(!StringUtils.isEmpty(intention) && CricketCommonConstants.UPGRADE_FEATURE.equalsIgnoreCase(intention)){
					categoryId = getCricketConfiguration().getOtherAddonsCategoryId();
				}
				else{}
			}
			
			if(isLoggingDebug()) {
				logDebug("CategoryId:"+categoryId);
				logDebug("CategoryType:"+categoryType);
				logDebug("isUserLoggedIn:"+isUserLoggedIn);
				logDebug("User Intention:"+intention);
				logDebug("MarketCode :"+marketCode);			
			}
			
			if(CricketCommonConstants.PHONES.equalsIgnoreCase(categoryType)) {
				final String planProductId = (String)pRequest.getParameter(CricketCommonConstants.PLAN_PRODUCT_ID);//When the Plan is there in the cart
				if(isLoggingDebug()){
					logDebug("Plan ProductId:"+planProductId);
				}
				PricingModelHolder userPricingHolder = (PricingModelHolder)pRequest.resolveName(CricketCommonConstants.USER_PRICING_MODELS_PATH);
				final List<PhoneVO>  phoneProductsList  = getBrowseShopManager().getListOfPhoneProducts(categoryId, planProductId, marketCode,userPricingHolder,marketType,isUserLoggedIn,upgradePhoneIntention,editPhone);
				pRequest.setParameter(CricketCommonConstants.LIST_OF_PRODUCTS, phoneProductsList);
			}		
			else if(CricketCommonConstants.PLANS.equalsIgnoreCase(categoryType)) {
				final String phoneProductId = (String)pRequest.getParameter(CricketCommonConstants.PHONE_PRODUCT_ID);//When the phone is there in the cart
				if(isLoggingDebug()){
					logDebug("Phone ProductId:"+phoneProductId);
				}
				final List<PlanVO>  planProductsList  = getBrowseShopManager().getListOfPlanProducts (categoryId, phoneProductId,marketCode, marketType);
				pRequest.setParameter(CricketCommonConstants.LIST_OF_PRODUCTS, planProductsList);
			}
			else if(CricketCommonConstants.ACCESSORY.equalsIgnoreCase(categoryType)) {
				String phoneProductId = (String)pRequest.getParameter(CricketCommonConstants.PHONE_PRODUCT_ID);//When the phone is there in the cart
				if(isLoggingDebug()){
					logDebug("Phone ProductId:"+phoneProductId);
				}
				List<AccessoryVO>  accessoryProductsList  = getBrowseShopManager().getListOfAccessoryProducts (categoryId, phoneProductId);
				pRequest.setParameter(CricketCommonConstants.LIST_OF_PRODUCTS, accessoryProductsList);

			}
			else if(CricketCommonConstants.PLAN_ADDONS.equalsIgnoreCase(categoryType)) {
				String planProductId = (String)pRequest.getParameter(CricketCommonConstants.PLAN_PRODUCT_ID);//When the Plan is there in the cart
				if(isLoggingDebug()){
					//logDebug("Addon ProductId:"+planProductId);
					logDebug("Plan ProductId:"+planProductId);
				}
				List<AddonVO>  addonProductsList  = getBrowseShopManager().getListOfAddonProducts (categoryId, planProductId);
				//Call for Plan Addons based on the Category id, If the user clicks on All Plan Addons
				//Call for Plan Addons based on the Plan id and Phone Id If the user clicks on All Plan Addons from the Cart
				pRequest.setParameter(CricketCommonConstants.LIST_OF_PRODUCTS, addonProductsList);				
			}
			else {
				pRequest.setParameter(CricketCommonConstants.LIST_OF_PRODUCTS, null);
			}
		}
		
		pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
		long end = System.currentTimeMillis();
		logInfo("Execution time was ::::: "+(end-start)+" milliseconds.");
		logInfo("Exiting the service method...");
	}

}
