package com.cricket.commerce.endeca.index.producer;

import java.util.Map;

import com.cricket.common.constants.CricketCommonConstants;

import atg.nucleus.GenericService;
import atg.repository.NamedQueryView;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.QueryExpression;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.search.indexing.Context;
import atg.repository.search.indexing.VariantProducer;

public class CricketPriceListPairVariantProducer extends GenericService implements VariantProducer {
	
	private Repository priceListRepository;
	private RepositoryItem[] priceLists;
	private String priceListPairUniqueParamName = "priceListPair";
	
	public synchronized boolean prepareNextVariant(Context pContext, String pPropertyName, RepositoryItem pItem, int pIndex, Map pUniqueParams) {
		if (isLoggingDebug()) {
			logDebug("preparing variant for pricelist");
		}
		RepositoryView priceListView = null;
		RepositoryItem[] priceLists = null;
		if(pIndex == 0) {
			try {
				priceListView = getPriceListRepository().getView(CricketCommonConstants.PROP_PRICE_LIST_ITEM);
				QueryBuilder builder = priceListView.getQueryBuilder();
				Query priceListQuery = builder.createUnconstrainedQuery();
				priceLists = priceListView.executeQuery(priceListQuery);
				this.setPriceLists(priceLists);
				
			} catch (RepositoryException e) {
				logError(e);
			}
			if (this.getPriceLists() != null) {
				if (isLoggingDebug()) {
					logDebug("creating variant no " + pIndex);
				}
				for (int priceListCount=0; priceListCount<this.getPriceLists().length;priceListCount++) {
					String priceListId = (String)priceLists[priceListCount].getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
					if (priceListId != null && !priceListId.isEmpty()) {
						if (priceListCount == pIndex) {
							String[] priceListIdsArray = new String[1];
							priceListIdsArray[0] = priceListId;
							pContext.setAttribute("atg.priceListVariantProdcer.priceListIds", priceListIdsArray);
							pUniqueParams.put(this.getPriceListPairUniqueParamName(), priceListId);
							return true;
						} else {
							return false;
						}
					}
				}
			}
		}
		
		return false;
	}

	public RepositoryItem[] getPriceLists() {
		return priceLists;
	}

	public void setPriceLists(RepositoryItem[] priceLists) {
		this.priceLists = priceLists;
	}

	public Repository getPriceListRepository() {
		return priceListRepository;
	}

	public void setPriceListRepository(Repository priceListRepository) {
		this.priceListRepository = priceListRepository;
	}

	public String getPriceListPairUniqueParamName() {
		return priceListPairUniqueParamName;
	}

	public void setPriceListPairUniqueParamName(
			String priceListPairUniqueParamName) {
		this.priceListPairUniqueParamName = priceListPairUniqueParamName;
	}

}
