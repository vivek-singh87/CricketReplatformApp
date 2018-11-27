package com.cricket.seo;

import java.util.List;

import com.cricket.catalog.CatalogManager;
import com.cricket.configuration.CricketConfiguration;
import com.cricket.vo.CricketProfile;
import com.cricket.commerce.endeca.cache.CricketDimensionValueCacheTools;
import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.service.webappregistry.WebApp;
import atg.servlet.DynamoHttpServletRequest;
import atg.repository.seo.IndirectUrlTemplate;
import atg.repository.seo.ItemLinkException;
import atg.commerce.endeca.cache.DimensionValueCacheObject;
import atg.core.util.StringUtils;

/**
 * 
 * @author 
 *
 */
public class UpgradeIndirectUrlTemplate extends IndirectUrlTemplate {

	private CatalogManager catalogManager;
	
	private CricketDimensionValueCacheTools mDimensionValueCacheTools;
	
	private CricketConfiguration cricketConfiguration;
	
	private String defaultPhoneDimValId;
	
	private String defaultPlanDimValId;
	
	private String defaultFeatureDimValId;
	
	public String getDefaultPhoneDimValId() {
		return defaultPhoneDimValId;
	}

	public void setDefaultPhoneDimValId(String phoneDimValId) {
		this.defaultPhoneDimValId = phoneDimValId;
	}
	
	
	public String getDefaultPlanDimValId() {
		return defaultPlanDimValId;
	}

	public void setDefaultPlanDimValId(String planDimValId) {
		this.defaultPlanDimValId = planDimValId;
	}
	
	public String getDefaultFeatureDimValId() {
		return defaultFeatureDimValId;
	}

	public void setDefaultFeatureDimValId(String featureDimValId) {
		this.defaultFeatureDimValId = featureDimValId;
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

	
	
	public CricketDimensionValueCacheTools getDimensionValueCacheTools() {
		return mDimensionValueCacheTools;
	}

	public void setDimensionValueCacheTools(
			CricketDimensionValueCacheTools dimensionValueCacheTools) {
		this.mDimensionValueCacheTools = dimensionValueCacheTools;
	}

	/**
	 * @return
	 */
	public CatalogManager getCatalogManager() {
		return catalogManager;
	}

	/**
	 * @param catalogManager
	 */
	public void setCatalogManager(CatalogManager catalogManager) {
		this.catalogManager = catalogManager;
	}

	
	/**
	 *  
	 */
	public String getForwardUrl(DynamoHttpServletRequest pRequest, String pIndirectUrl, WebApp pDefaultWebApp, Repository pDefaultRepository)
		    throws ItemLinkException {
		
		String forwardUrl = null;
		forwardUrl = super.getForwardUrl(pRequest, pIndirectUrl, pDefaultWebApp, pDefaultRepository);
		
		CricketProfile cricketProfile = (CricketProfile) pRequest.resolveName("/atg/cricket/util/CricketProfile");
		
		if(isLoggingDebug()){			
			logDebug("pIndirectUrl = "+ pIndirectUrl);			
			logDebug("MDN ="+cricketProfile.getMdn()+" PhoneCode = "+cricketProfile.getPhoneCode() + " DeviceModel = "+cricketProfile.getDeviceModel()+" PlanCode = "+cricketProfile.getPlanCode()
					 +" ProductType = " + cricketProfile.getProductType() + " MarketCode = " + cricketProfile.getMarketCode() + " FirstName = " + cricketProfile.getFirstName()
					 +" userId = " + cricketProfile.getUserId() +" User Name = "+cricketProfile.getUserName() );
			logDebug(" forwardUrl = "+forwardUrl);
		}

	   String dimValId = null;
	   StringBuffer forwardUrlSB = new StringBuffer();	
      
        if(pIndirectUrl.contains("upgrade-phone") && !StringUtils.isBlank(forwardUrl)) {
        	String seoString = null;
        	try {
				RepositoryItem category = (RepositoryItem)pDefaultRepository.getItem(getCricketConfiguration().getAllPhonesCategoryId(), CricketCommonConstants.CATEGORY);
				seoString = (String)category.getPropertyValue(CricketCommonConstants.SEO_STRING);
			} catch (RepositoryException e) {
				logError(e);
			}
        	String planCode = cricketProfile.getRatePlanCode();
    		DimensionValueCacheObject dimensionValueCacheObject = getDimensionValueCacheTools().get(getCricketConfiguration().getAllPhonesCategoryId(),null);
      	  	if(dimensionValueCacheObject != null) {
    		  dimValId = dimensionValueCacheObject.getDimvalId();
      	  	}
        	if(isLoggingDebug()) {
        		logDebug("planCode = " + planCode);
        		logDebug("dimValId = " + dimValId);
        	}
        	forwardUrlSB.append(forwardUrl);
        	if(!StringUtils.isBlank(seoString)) {
      	  		forwardUrlSB.append("/" + seoString);
      	  	}
        	if(!StringUtils.isBlank(dimValId)) {
        		forwardUrlSB.append("?N=" + dimValId);
        	}
        	if(!StringUtils.isBlank(planCode)) {
        		forwardUrlSB.append("&planId=" + planCode);
        	}
        	forwardUrlSB.append("&intention=upgradePhone");
            
        } else if(pIndirectUrl.contains("/cell-phones/"+getCricketConfiguration().getSeoKeyword()) && !StringUtils.isBlank(forwardUrl)) {
        	String seoString = null;
        	try {
				RepositoryItem category = (RepositoryItem)pDefaultRepository.getItem(getCricketConfiguration().getAllPhonesCategoryId(), CricketCommonConstants.CATEGORY);
				seoString = (String)category.getPropertyValue(CricketCommonConstants.SEO_STRING);
			} catch (RepositoryException e) {
				logError(e);
			}
        	DimensionValueCacheObject dimensionValueCacheObject = getDimensionValueCacheTools().get(getCricketConfiguration().getAllPhonesCategoryId(),null);
      	  	if(dimensionValueCacheObject != null) {
    		  dimValId = dimensionValueCacheObject.getDimvalId();
      	  	}
        	if(isLoggingDebug()) {
        		logDebug("dimValId = " + dimValId);
        	}
      	  	forwardUrlSB.append(forwardUrl);
        	if(!StringUtils.isBlank(seoString)) {
      	  		forwardUrlSB.append("/" + seoString);
      	  	}
        	if(!StringUtils.isBlank(dimValId)) {
        		forwardUrlSB.append("?N=" + dimValId);
        	}
            
        } else if(pIndirectUrl.contains("/cell-phones/smartphones/"+getCricketConfiguration().getSeoKeyword()) && !StringUtils.isBlank(forwardUrl)) {
        	String seoString = null;
        	try {
				RepositoryItem category = (RepositoryItem)pDefaultRepository.getItem(getCricketConfiguration().getSmartPhonesCategoryId(), CricketCommonConstants.CATEGORY);
				seoString = (String)category.getPropertyValue(CricketCommonConstants.SEO_STRING);
			} catch (RepositoryException e) {
				logError(e);
			}
        	DimensionValueCacheObject dimensionValueCacheObject = getDimensionValueCacheTools().get(getCricketConfiguration().getSmartPhonesCategoryId(),null);
      	  	if(dimensionValueCacheObject != null) {
    		  dimValId = dimensionValueCacheObject.getDimvalId();
      	  	}
        	if(isLoggingDebug()) {
        		logDebug("dimValId = " + dimValId);
        	}
      	  	forwardUrlSB.append(forwardUrl);
        	if(!StringUtils.isBlank(seoString)) {
      	  		forwardUrlSB.append("/" + seoString);
      	  	}
        	if(!StringUtils.isBlank(dimValId)) {
        		forwardUrlSB.append("?N=" + dimValId);
        	}
            
        } else if(pIndirectUrl.contains("/cell-phones/basic/"+getCricketConfiguration().getSeoKeyword()) && !StringUtils.isBlank(forwardUrl)) {
        	String seoString = null;
        	try {
				RepositoryItem category = (RepositoryItem)pDefaultRepository.getItem(getCricketConfiguration().getBasicPhonesCategoryId(), CricketCommonConstants.CATEGORY);
				seoString = (String)category.getPropertyValue(CricketCommonConstants.SEO_STRING);
			} catch (RepositoryException e) {
				logError(e);
			}
        	DimensionValueCacheObject dimensionValueCacheObject = getDimensionValueCacheTools().get(getCricketConfiguration().getBasicPhonesCategoryId(),null);
      	  	if(dimensionValueCacheObject != null) {
    		  dimValId = dimensionValueCacheObject.getDimvalId();
      	  	}
        	if(isLoggingDebug()) {
        		logDebug("dimValId = " + dimValId);
        	}
      	  	forwardUrlSB.append(forwardUrl);
        	if(!StringUtils.isBlank(seoString)) {
      	  		forwardUrlSB.append("/" + seoString);
      	  	}
        	if(!StringUtils.isBlank(dimValId)) {
        		forwardUrlSB.append("?N=" + dimValId);
        	}
            
        } else if(pIndirectUrl.contains("/cell-phone-accessories/"+getCricketConfiguration().getSeoKeyword()) && !StringUtils.isBlank(forwardUrl)) {
        	String seoString = null;
        	try {
				RepositoryItem category = (RepositoryItem)pDefaultRepository.getItem(getCricketConfiguration().getAccessoriesCategoryId(), CricketCommonConstants.CATEGORY);
				seoString = (String)category.getPropertyValue(CricketCommonConstants.SEO_STRING);
			} catch (RepositoryException e) {
				logError(e);
			}
        	DimensionValueCacheObject dimensionValueCacheObject = getDimensionValueCacheTools().get(getCricketConfiguration().getAccessoriesCategoryId(),null);
      	  	if(dimensionValueCacheObject != null) {
    		  dimValId = dimensionValueCacheObject.getDimvalId();
      	  	}
        	if(isLoggingDebug()) {
        		logDebug("dimValId = " + dimValId);
        	}
      	  	forwardUrlSB.append(forwardUrl);
        	if(!StringUtils.isBlank(seoString)) {
      	  		forwardUrlSB.append("/" + seoString);
      	  	}
        	if(!StringUtils.isBlank(dimValId)) {
        		forwardUrlSB.append("?N=" + dimValId);
        	}
            
        } else if(pIndirectUrl.contains("change-plan") && !StringUtils.isBlank(forwardUrl)) {
        	String seoString = null;
        	try {
				RepositoryItem category = (RepositoryItem)pDefaultRepository.getItem(getCricketConfiguration().getAllPlansCategoryId(), CricketCommonConstants.CATEGORY);
				seoString = (String)category.getPropertyValue(CricketCommonConstants.SEO_STRING);
			} catch (RepositoryException e) {
				logError(e);
			}
        	String phoneCode = null;
        	phoneCode = cricketProfile.getDeviceModel();
        	String planCategoryId = null;
        	String phoneId = null;
    		planCategoryId = getCricketConfiguration().getAllPlansCategoryId();
    		DimensionValueCacheObject dimensionValueCacheObject = getDimensionValueCacheTools().get(planCategoryId,null);
      	  	if(dimensionValueCacheObject != null) {
      	  		dimValId = dimensionValueCacheObject.getDimvalId();
      	  	}
        	if(!StringUtils.isBlank(phoneCode)) {
        		List<RepositoryItem> phoneProducts = getCatalogManager().getPhonesForModelNumber(phoneCode);       	
            	RepositoryItem phone = null;
            	if(phoneProducts != null && phoneProducts.size() > 0) {
	        		phone = phoneProducts.get(0);
	        		phoneId = phone.getRepositoryId();
            	}
        	}           	
           	forwardUrlSB.append(forwardUrl);
           	if(!StringUtils.isBlank(seoString)) {
      	  		forwardUrlSB.append("/" + seoString);
      	  	}
        	if(!StringUtils.isBlank(dimValId)) {
        		forwardUrlSB.append("?N=" + dimValId);
        	}
        	if(!StringUtils.isBlank(phoneId)) {
        		forwardUrlSB.append("&phoneId=" + phoneId);
        	}
      	    forwardUrlSB.append("&intention=upgradePlan");      	    
      	    
      	  if(isLoggingDebug()){
      		logDebug("UpgradeIndirectUrlTemplate->change-plan");
      		logDebug("mdn = " + cricketProfile.getMdn());   
      		logDebug("ratePlanCode = " + cricketProfile.getRatePlanCode());
      		logDebug("dimValId = " + dimValId);
      		logDebug("phoneId = " + phoneId);
      		logDebug("phoneCode = " + phoneCode); 		 		
      		
      	}
      	    
        } else if(pIndirectUrl.contains("/cell-phone-plans/"+getCricketConfiguration().getSeoKeyword()) && !StringUtils.isBlank(forwardUrl)) {
        	String seoString = null;
        	try {
				RepositoryItem category = (RepositoryItem)pDefaultRepository.getItem(getCricketConfiguration().getAllPlansCategoryId(), CricketCommonConstants.CATEGORY);
				seoString = (String)category.getPropertyValue(CricketCommonConstants.SEO_STRING);
			} catch (RepositoryException e) {
				logError(e);
			}
        	String planCategoryId = null;
    		planCategoryId = getCricketConfiguration().getAllPlansCategoryId();
    		DimensionValueCacheObject dimensionValueCacheObject = getDimensionValueCacheTools().get(planCategoryId,null);
      	  	if(dimensionValueCacheObject != null) {
      	  		dimValId = dimensionValueCacheObject.getDimvalId();
      	  	}
           	if(isLoggingDebug()){
        		logDebug("dimValId = " + dimValId);
        	}
           	forwardUrlSB.append(forwardUrl);
           	if(!StringUtils.isBlank(seoString)) {
      	  		forwardUrlSB.append("/" + seoString);
      	  	}
        	if(!StringUtils.isBlank(dimValId)) {
        		forwardUrlSB.append("?N=" + dimValId);
        	}
        } else if (pIndirectUrl.contains("change-feature") && !StringUtils.isBlank(forwardUrl)) {
        	String seoString = null;
        	try {
				RepositoryItem category = (RepositoryItem)pDefaultRepository.getItem(getCricketConfiguration().getAddonsCategoryId(), CricketCommonConstants.CATEGORY);
				seoString = (String)category.getPropertyValue(CricketCommonConstants.SEO_STRING);
			} catch (RepositoryException e) {
				logError(e);
			}
        	//Add-ons will be fetched by calling  InquireAccount service.
        	String addOnCategoryId = null;
        	//logic to fetch add on category id - -TBD
        	addOnCategoryId = getCricketConfiguration().getOtherAddonsCategoryId();
        	if(!StringUtils.isBlank(addOnCategoryId)){
        	  DimensionValueCacheObject dimensionValueCacheObject = getDimensionValueCacheTools().get(addOnCategoryId,null);
        	  if(dimensionValueCacheObject != null){
        		  dimValId = dimensionValueCacheObject.getDimvalId();
        	  }
    		  
        	}
           	if(isLoggingDebug()){
        		logDebug("dimValId = "+dimValId);
        	}
        	forwardUrlSB.append(forwardUrl);
        	if(!StringUtils.isBlank(seoString)) {
      	  		forwardUrlSB.append("/" + seoString);
      	  	}
           	if(!StringUtils.isBlank(dimValId)){
           		forwardUrlSB.append("?N=").append(dimValId).append("&intention=upgradeFeature");
           	}else{
              	if(isLoggingDebug()){
            		logDebug("getDefaultFeatureDimValId = "+getDefaultFeatureDimValId());
            	}              		
           		forwardUrlSB.append("?N=").append(getDefaultFeatureDimValId()).append("&intention=upgradeFeature");
           	}
        	//forwardUrlSB.append(forwardUrl).append("?N=10192").append("&intention=upgradeFeature");

        } else if (pIndirectUrl.contains("/cell-phone-features/"+getCricketConfiguration().getSeoKeyword()) && !StringUtils.isBlank(forwardUrl)) {
        	String seoString = null;
        	try {
				RepositoryItem category = (RepositoryItem)pDefaultRepository.getItem(getCricketConfiguration().getAddonsCategoryId(), CricketCommonConstants.CATEGORY);
				seoString = (String)category.getPropertyValue(CricketCommonConstants.SEO_STRING);
			} catch (RepositoryException e) {
				logError(e);
			}
        	//Add-ons will be fetched by calling  InquireAccount service.
        	String addOnCategoryId = null;
        	//logic to fetch add on category id - -TBD
        	addOnCategoryId = getCricketConfiguration().getOtherAddonsCategoryId();
        	if(!StringUtils.isBlank(addOnCategoryId)){
        	  DimensionValueCacheObject dimensionValueCacheObject = getDimensionValueCacheTools().get(addOnCategoryId,null);
        	  if(dimensionValueCacheObject != null){
        		  dimValId = dimensionValueCacheObject.getDimvalId();
        	  }
    		  
        	}
           	if(isLoggingDebug()){
        		logDebug("dimValId = "+dimValId);
        		logDebug("seoString = "+"/" + seoString);
        	}
        	forwardUrlSB.append(forwardUrl);
        	if(!StringUtils.isBlank(seoString)) {
      	  		forwardUrlSB.append("/" + seoString);
      	  	}
        	if(!StringUtils.isBlank(dimValId)) {
        		forwardUrlSB.append("?N=" + dimValId);
        	}
        	//forwardUrlSB.append(forwardUrl).append("?N=10192").append("&intention=upgradeFeature");

        } else if(pIndirectUrl.contains(CricketCommonConstants.ADD_A_LINE) && !StringUtils.isBlank(forwardUrl)) {
        	
        	//forwardUrlSB.append(forwardUrl);
        	forwardUrl = forwardUrl+"?"+"intention=addLine";
        	forwardUrlSB.append(forwardUrl);
        } else if(pIndirectUrl.contains(CricketCommonConstants.WHY_CRICKET) && !StringUtils.isBlank(forwardUrl)) {
        	
        	//forwardUrl = forwardUrl+"?"+"intention=addLine";
        	forwardUrlSB.append(forwardUrl);
        } else if(pIndirectUrl.contains("billing-ordersummary/enter-billing-orderid") && !StringUtils.isBlank(forwardUrl)) {
        	
        	//forwardUrl = forwardUrl+"?"+"intention=addLine";
        	forwardUrlSB.append(forwardUrl);
        }
        
        if(!StringUtils.isBlank(forwardUrl)){
        	forwardUrlSB.append("&sort=").append(getCricketConfiguration().getDefaultSortParam());
        	forwardUrl = forwardUrlSB.toString();
        }
        
        if(isLoggingDebug()){
        	
        logDebug(" After appending intention & DimentionCacheId - forwardUrl = "+forwardUrl);
        }
        
		return forwardUrl;
		
	}
		    
		    
}