package com.cricket.commerce.endeca.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cricket.configuration.CricketConfiguration;
import atg.commerce.endeca.cache.DimensionValueCacheObject;
import atg.commerce.endeca.cache.DimensionValueCacheTools;

public class CricketDimensionValueCacheTools extends DimensionValueCacheTools {
	
	private CricketConfiguration cricketConfiguration;
	
	private Map<String, String> catTypeToPageMap = new HashMap<String, String>();
	
	  protected DimensionValueCacheObject getEntryWithSpecificAncestors(String pRepositoryId, List<String> pAncestorIds) {
		  
		  DimensionValueCacheObject result = super.getEntryWithSpecificAncestors(pRepositoryId, pAncestorIds);
		  if(isLoggingDebug()) {
			  logDebug("inside CricketDimensionValueCacheTools getEntryWithSpecificAncestors method");
		  }
		  
		  if(result != null) {
			  String dimValId = result.getDimvalId();
			  StringBuilder dimUrlBuilder = new StringBuilder();
			  if(pRepositoryId.equals(cricketConfiguration.getAllPhonesCategoryId())) {
				  dimUrlBuilder.append(catTypeToPageMap.get("allPhones"));
			  } else if(pRepositoryId.equals(cricketConfiguration.getAllPlansCategoryId())) {
				  dimUrlBuilder.append(catTypeToPageMap.get("allPlans"));
			  } else if(pRepositoryId.equals(cricketConfiguration.getAccessoriesCategoryId())) {
				  dimUrlBuilder.append(catTypeToPageMap.get("accessories"));
			  } else if(pRepositoryId.equals(cricketConfiguration.getOtherAddonsCategoryId())) {
				  dimUrlBuilder.append(catTypeToPageMap.get("addOns"));
			  } else if(pRepositoryId.equals(cricketConfiguration.getSmartPhonesCategoryId())) {
				  dimUrlBuilder.append(catTypeToPageMap.get("smartPhones"));
			  } else if(pRepositoryId.equals(cricketConfiguration.getBasicPhonesCategoryId())) {
				  dimUrlBuilder.append(catTypeToPageMap.get("featurePhones"));
			  } else {
				  dimUrlBuilder.append(catTypeToPageMap.get("default"));
			  }
			  dimUrlBuilder.append("?N=");
			  dimUrlBuilder.append(dimValId);
			  if(dimUrlBuilder != null) {
				  if(isLoggingDebug()) {
					  logDebug("dimUrl changed to :: " + dimUrlBuilder.toString());
				  }
				  result.setURL(dimUrlBuilder.toString());
			  }
		  }
		  if(isLoggingDebug()) {
			  logDebug("exiting CricketDimensionValueCacheTools getEntryWithSpecificAncestors method");
		  }
		  return result;
	  }

	public CricketConfiguration getCricketConfiguration() {
		return cricketConfiguration;
	}

	public void setCricketConfiguration(CricketConfiguration cricketConfiguration) {
		this.cricketConfiguration = cricketConfiguration;
	}

	public Map<String, String> getCatTypeToPageMap() {
		return catTypeToPageMap;
	}

	public void setCatTypeToPageMap(Map<String, String> catTypeToPageMap) {
		this.catTypeToPageMap = catTypeToPageMap;
	}
}
