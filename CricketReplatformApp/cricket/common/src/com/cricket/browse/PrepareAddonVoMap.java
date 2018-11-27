package com.cricket.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import atg.commerce.catalog.CatalogTools;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.configuration.CricketConfiguration;

public class PrepareAddonVoMap extends DynamoServlet {
	
	/**
	 * Variable to hold cktConfiguration
	 */
	private CricketConfiguration cktConfiguration;

	/**
	 * Variable to hold catalogTools
	 */
	private CatalogTools catalogTools;

	/**
	 * Variable to hold addonOtherCategoryName
	 */
	private String addonOtherCategoryName;
	
	/**
	 * Variable to hold callingFeaturesName
	 */
	private String callingFeaturesName;
	
	/**
	 * Variable to hold messagingFeaturesName
	 */
	private String messagingFeaturesName;
	
	/**
	 * Variable to hold musicGamesFeaturesName
	 */
	private String musicGamesFeaturesName;
	
	/**
	 * Variable to hold internationalCallingAndRoamingFeaturesName
	 */
	private String internationalCallingAndRoamingFeaturesName;
	
	/**
	 * Variable to hold dataFeaturesName
	 */
	private String dataFeaturesName;
	
	/**
	 * This droplet prepares a map for the whole addon listing results.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		List<AddonVO> addonsList = (List<AddonVO>)pRequest.getObjectParameter(CricketCommonConstants.PRODUCT_List);
		List<RepositoryItem> addOnCategoryItems = new ArrayList<RepositoryItem>();
		List<String> addOnCategories = new ArrayList<String>();
		 try {
			 addOnCategoryItems = (List<RepositoryItem>) getCatalogTools().getCatalog().
					 getItem(getCktConfiguration().getAddonsCategoryId(), CricketCommonConstants.CATEGORY).getPropertyValue(CricketCommonConstants.CHILD_CATEGORIES);
		} catch (RepositoryException e) {
			vlogError("RepositoryException");
		}
		Map<String, List<AddonVO>> addonsSegregated = new HashMap<String, List<AddonVO>>();
		for(RepositoryItem addOnCategoryItem : addOnCategoryItems){
				addOnCategories.add(addOnCategoryItem.getRepositoryId());
		}
		for (AddonVO addonVO : addonsList) {
			Map<String, String> ancestorCategories = addonVO.getAncestorCategories();
			for (Map.Entry<String, String> entry : ancestorCategories.entrySet()) {
			    if (addOnCategories.contains(entry.getKey())) {
			    	if (addonsSegregated.containsKey(entry.getValue())) {
			    		List<AddonVO> addOnVoTemp2 = addonsSegregated.get(entry.getValue());
			    		addOnVoTemp2.add(addonVO);
			    		addonsSegregated.put(entry.getValue(),addOnVoTemp2);
			    	} else {
			    		List<AddonVO> addOnVoTemp = new ArrayList<AddonVO>();
			    		addOnVoTemp.add(addonVO);
			    		addonsSegregated.put(entry.getValue(),addOnVoTemp);
			    	}
			    }
			}
		}
		addonsSegregated = removeFromOtherWhichAreSegregated(addonsSegregated);
		LinkedHashMap<String, List<AddonVO>> sortedMap =  sortMap(addonsSegregated, addOnCategoryItems);
		pRequest.setParameter(CricketCommonConstants.FINAL_MAP, sortedMap);
		pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
	}

	/**
	 * This method is used to sort the map for displaying purpose.
	 * @param pAddonsSegregated
	 * @return sortedMap
	 */
	@SuppressWarnings("unused")
	private LinkedHashMap<String, List<AddonVO>>sortMap(Map<String, List<AddonVO>> pAddonsSegregated, List<RepositoryItem> addOnCategoryItems) {
		LinkedHashMap<String, List<AddonVO>> sortedMap = new LinkedHashMap<String, List<AddonVO>>();
		for(RepositoryItem addOnCategoryItem : addOnCategoryItems){
			String categoryName = (String)addOnCategoryItem.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME);
			if(categoryName != null) {
				if(pAddonsSegregated.containsKey(categoryName)) {
					sortedMap.put(categoryName, pAddonsSegregated.get(categoryName));
				}				
			}
		}
		return sortedMap;
	}

	/**
	 * @param pAddonsSegregated
	 * @return pAddonsSegregated
	 */
	private Map<String, List<AddonVO>> removeFromOtherWhichAreSegregated(Map<String, List<AddonVO>> pAddonsSegregated) {
		List <AddonVO> otherAddonList = pAddonsSegregated.get(addonOtherCategoryName);
		pAddonsSegregated.remove(addonOtherCategoryName);
		for (Map.Entry<String, List<AddonVO>> entry : pAddonsSegregated.entrySet()) {
			if (entry.getValue() != null && entry.getKey() != addonOtherCategoryName) {
				for(AddonVO addonVo : entry.getValue()) {
					if(otherAddonList.contains(addonVo)) {
						otherAddonList.remove(addonVo);
					}
				}
			}
		}
		pAddonsSegregated.put(addonOtherCategoryName, otherAddonList);
		return pAddonsSegregated;
	}

	/**
	 * @return addOnCategories
	 *//*
	public List<String> getAddOnCategories() {
		return addOnCategories;
	}

	*//**
	 * @param pAddOnCategories
	 *//*
	public void setAddOnCategories(List<String> pAddOnCategories) {
		addOnCategories = pAddOnCategories;
	}*/

	/**
	 * 
	 * @return the cktConfiguration
	 */
	public CricketConfiguration getCktConfiguration() {
		return cktConfiguration;
	}

	/**
	 * 
	 * @param pCktConfiguration
	 */
	public void setCktConfiguration(CricketConfiguration pCktConfiguration) {
		cktConfiguration = pCktConfiguration;
	}
	
	/**
	 * @return addonOtherCategoryName
	 */
	public String getAddonOtherCategoryName() {
		return addonOtherCategoryName;
	}

	/**
	 * @param pAddonOtherCategoryName
	 */
	public void setAddonOtherCategoryName(String pAddonOtherCategoryName) {
		addonOtherCategoryName = pAddonOtherCategoryName;
	}

	/**
	 * @return callingFeaturesName
	 */
	public String getCallingFeaturesName() {
		return callingFeaturesName;
	}

	/**
	 * @param pCallingFeaturesName
	 */
	public void setCallingFeaturesName(String pCallingFeaturesName) {
		callingFeaturesName = pCallingFeaturesName;
	}

	/**
	 * @return messagingFeaturesName
	 */
	public String getMessagingFeaturesName() {
		return messagingFeaturesName;
	}

	/**
	 * @param pMessagingFeaturesName
	 */
	public void setMessagingFeaturesName(String pMessagingFeaturesName) {
		messagingFeaturesName = pMessagingFeaturesName;
	}

	/**
	 * @return musicGamesFeaturesName
	 */
	public String getMusicGamesFeaturesName() {
		return musicGamesFeaturesName;
	}

	/**
	 * @param pMusicGamesFeaturesName
	 */
	public void setMusicGamesFeaturesName(String pMusicGamesFeaturesName) {
		musicGamesFeaturesName = pMusicGamesFeaturesName;
	}

	/**
	 * @return internationalCallingAndRoamingFeaturesName
	 */
	public String getInternationalCallingAndRoamingFeaturesName() {
		return internationalCallingAndRoamingFeaturesName;
	}

	/**
	 * @param pInternationalCallingAndRoamingFeaturesName
	 */
	public void setInternationalCallingAndRoamingFeaturesName(
			String pInternationalCallingAndRoamingFeaturesName) {
		internationalCallingAndRoamingFeaturesName = pInternationalCallingAndRoamingFeaturesName;
	}

	/**
	 * @return dataFeaturesName
	 */
	public String getDataFeaturesName() {
		return dataFeaturesName;
	}

	/**
	 * @param pDataFeaturesName
	 */
	public void setDataFeaturesName(String pDataFeaturesName) {
		dataFeaturesName = pDataFeaturesName;
	}

	/**
	 * 
	 * @return the catalogTools
	 */
	public CatalogTools getCatalogTools() {
		return catalogTools;
	}

	/**
	 * 
	 * @param pCatalogTools
	 */
	public void setCatalogTools(CatalogTools pCatalogTools) {
		catalogTools = pCatalogTools;
	}
}

