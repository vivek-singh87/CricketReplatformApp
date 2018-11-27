package com.cricket.commerce.endeca.index.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import atg.nucleus.GenericService;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.search.indexing.Context;
import atg.repository.search.indexing.VariantProducer;

public class AppliedPromotionPriceVariantProducer extends GenericService implements VariantProducer {
	
	String uniqueParamName = "marketTypeUniqueParam";
	Repository productCatalog;
	
	public synchronized boolean prepareNextVariant(Context pContext, String pPropertyName, RepositoryItem pItem, int pIndex, Map pUniqueParams) {
		if (isLoggingDebug()) {
			logDebug("preparing variant for pricelist");
		}
		RepositoryItem[] marketTypeItems = null;
		try {
			RepositoryView marketTypeView = productCatalog.getView("marketType");
			QueryBuilder qBuilder = marketTypeView.getQueryBuilder();
			Query marketTypeQuery = qBuilder.createUnconstrainedQuery();
			marketTypeItems = marketTypeView.executeQuery(marketTypeQuery);
		} catch (RepositoryException e) {
			logError(e);
		}
		if (marketTypeItems != null) {
			if (pIndex >= marketTypeItems.length) {
				pContext.setAttribute("atg.AppliedPromotionPriceVariantProducer.marketType", null);
				return false;
			}
			pContext.setAttribute("atg.AppliedPromotionPriceVariantProducer.marketType", marketTypeItems[pIndex].getPropertyValue("marketTypeName"));
			pUniqueParams.put(getUniqueParamName(), marketTypeItems[pIndex].getPropertyValue("marketTypeName"));
			return true;
		} else {
			pContext.setAttribute("atg.AppliedPromotionPriceVariantProducer.marketType", null);
			return false;
		}
	}

	public String getUniqueParamName() {
		return uniqueParamName;
	}

	public void setUniqueParamName(String uniqueParamName) {
		this.uniqueParamName = uniqueParamName;
	}

	public Repository getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(Repository productCatalog) {
		this.productCatalog = productCatalog;
	}

}
