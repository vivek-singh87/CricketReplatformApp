package com.cricket.search;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.configuration.CricketConfiguration;

import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class GetParentCatNameForAddonProduct extends DynamoServlet {
	
	private CricketConfiguration cricketConfiguration;
	
	@SuppressWarnings({ "unchecked", "unused" })
	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		
		RepositoryItem product = (RepositoryItem)pReq.getObjectParameter(CricketCommonConstants.PRODUCT);
		String parentCategoryName = "Others";
		boolean catNameFound = false;
		if (product != null) {
			String productType = (String)product.getPropertyValue(CricketCommonConstants.TYPE);
			List<RepositoryItem> ancestorCategories = (List<RepositoryItem>)product.getPropertyValue(CricketCommonConstants.ANCESTOR_CATEGORIES);
			List<String> addonRelatedCategories = getCricketConfiguration().getAddonRelatedCategories();
			if(productType != null && productType.equals(CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT)) {
				for (String addOnRelCatId : addonRelatedCategories) {
					for (RepositoryItem ancestorCategory : ancestorCategories) {
						String categoryId = (String)ancestorCategory.getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
						if(addonRelatedCategories.contains(categoryId)) {
							parentCategoryName = (String)ancestorCategory.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME);
							catNameFound = true;
							break;
						}
					}
					if(catNameFound) {
						break;
					}
				}
			}
		}
		pReq.setParameter("parentCatName", parentCategoryName);
		try {
			pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
		} catch (ServletException e) {
			if(isLoggingError()) {
				logError("error occurred while servicing the content inside GetParentCatNameForAddonProduct"+e);
			}
		} catch (IOException e) {
			if(isLoggingError()) {
				logError("error occurred while servicing the content inside GetParentCatNameForAddonProduct"+e);
			}
		}
	}

	public CricketConfiguration getCricketConfiguration() {
		return cricketConfiguration;
	}

	public void setCricketConfiguration(CricketConfiguration cricketConfiguration) {
		this.cricketConfiguration = cricketConfiguration;
	} 
}
