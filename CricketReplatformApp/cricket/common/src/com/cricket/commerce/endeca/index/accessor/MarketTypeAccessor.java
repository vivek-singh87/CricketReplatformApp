package com.cricket.commerce.endeca.index.accessor;

import atg.repository.RepositoryItem;
import atg.repository.search.indexing.Context;
import atg.repository.search.indexing.PropertyAccessorImpl;
import atg.repository.search.indexing.specifier.PropertyTypeEnum;

public class MarketTypeAccessor extends PropertyAccessorImpl {
	
	protected Object getTextOrMetaPropertyValue(Context pContext, RepositoryItem pItem, String pPropertyName, PropertyTypeEnum pType) {
		String marketType = (String)pContext.getAttribute("atg.AppliedPromotionPriceVariantProducer.marketType");
		return marketType;
	}

}
