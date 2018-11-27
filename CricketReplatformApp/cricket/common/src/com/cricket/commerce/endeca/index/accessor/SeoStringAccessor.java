package com.cricket.commerce.endeca.index.accessor;

import java.util.List;

import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.RepositoryItem;
import atg.repository.search.indexing.Context;
import atg.repository.search.indexing.PropertyAccessorImpl;
import atg.repository.search.indexing.specifier.PropertyTypeEnum;

public class SeoStringAccessor extends PropertyAccessorImpl {
	protected Object getTextOrMetaPropertyValue(Context pContext, RepositoryItem pItem, String pPropertyName, PropertyTypeEnum pType) {
		if(isLoggingDebug()) {
			logDebug("entering SelectableFeaturesAccessor getTextOrMetaPropertyValue");
		}
		List<RepositoryItem> parentCategories = (List<RepositoryItem>)pItem.getPropertyValue(CricketCommonConstants.ANCESTOR_CATEGORIES);
		String seoString = null;
		if(parentCategories != null) {
			for (RepositoryItem parentCategory : parentCategories) {
				seoString = (String)parentCategory.getPropertyValue(CricketCommonConstants.SEO_STRING);
				if(seoString != null && !seoString.isEmpty()) {
					break;
				}
			}
		}
		return seoString;
	}

}
