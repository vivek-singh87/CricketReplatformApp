package com.cricket.pricing;

import java.util.List;

import atg.nucleus.GenericService;
import atg.repository.NamedQueryView;
import atg.repository.ParameterSupportView;
import atg.repository.Query;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;

import com.cricket.common.constants.CricketCommonConstants;

public class PriceListTools extends GenericService{
	
	private Repository priceListRepository;
	
	/**
	 * @return the priceListRepository
	 */
	public Repository getPriceListRepository() {
		return priceListRepository;
	}

	/**
	 * @param priceListRepository the priceListRepository to set
	 */
	public void setPriceListRepository(Repository priceListRepository) {
		this.priceListRepository = priceListRepository;
	}

	/**
	 * This method gets the City, coverage and market info as input
	 * It retrieves the List of price lists based on the coverage and 
	 * checks the price list for the current city and returns the item.
	 * If no price list is found for the city, then the base price list 
	 * for the coverage area is returned.
	 * @param cityVO
	 * @param coverage
	 * @param marketId
	 * @return RepositoryItem
	 */
	public RepositoryItem getPriceListForMarket(String profileCity, String profileZipcode, String coverage, String marketId){		
		if(isLoggingDebug()){  
			logDebug("Inside getPriceListFotMarket method");
			logDebug("Coverage : " + coverage +" Market Id : " + marketId);
		}
		String marketType = "0";
		if(coverage !=null){
			marketType = (coverage.equals(CricketCommonConstants.PROP_CRICKET)) ? "0" : "1";
		}	
		if(isLoggingDebug()){			
			logDebug("marketType : " + marketType);
		}
		RepositoryItem profilePriceList = null;	
		try {
			Repository pricerepository = getPriceListRepository();			
			RepositoryItem basePriceList = null;	
			NamedQueryView namedQueryView = (NamedQueryView)pricerepository.getView(CricketCommonConstants.PROP_PRICE_LIST_ITEM);
			Query nQuery = namedQueryView.getNamedQuery(CricketCommonConstants.PROP_GET_PRICE_LIST_TYPE);
			String[] inputParams = new String[1];
			inputParams[0] = marketType;
			ParameterSupportView paramSupportView = (ParameterSupportView)namedQueryView;
			RepositoryItem[] priceItems = (RepositoryItem[])paramSupportView.executeQuery(nQuery, inputParams);
			if(isLoggingDebug()){			
				logDebug("priceItems : " + priceItems);
			}
			String city=null;
			String marketCode=null;
			List<?> zipcodes=null;
			if(null!= priceItems){
				for(RepositoryItem priceList : priceItems){
					RepositoryItem market = (RepositoryItem) priceList.getPropertyValue(CricketCommonConstants.PROP_MARKET);
					RepositoryItem inheritPL = (RepositoryItem) priceList.getPropertyValue(CricketCommonConstants.PROP_BASE_PRICE_lIST);
					if(isLoggingDebug()){			
						logDebug("priceList : " + priceList + " market : " + market);
					}
					//Setting BasePriceList
					if (null == market && null == inheritPL) {
						basePriceList = priceList;
						
					}else if(null == market && null == basePriceList){
						basePriceList = priceList;
					}					
					else if (null != profileCity && null != profileZipcode && null != market){
						city = (String) market.getPropertyValue(CricketCommonConstants.PROP_CITY);
						zipcodes = (List<?>) market.getPropertyValue(CricketCommonConstants.PROP_ZIPCODES);
						marketCode = (String) market.getPropertyValue(CricketCommonConstants.PROP_MARKET_CODE);
						if (null!=marketId && marketCode.equals(marketId) && city.contains(profileCity) && zipcodes.contains(profileZipcode)) {
							profilePriceList = priceList;
							break;
						}
					}
					if(isLoggingDebug()){			
						logDebug("priceList : " + priceList + "basePriceList : " + basePriceList + " profilePriceList : " + profilePriceList);
					}
			    }
				// setting basePriceList as profilePriceList if PriceList for the city is not found
				if(null == profilePriceList){
					if(isLoggingDebug()){
						logDebug("profilePriceList is Null. Setting BasePriceList");
						logDebug("basePriceList : " + basePriceList + " profilePriceList : " + profilePriceList);
					}
					profilePriceList = basePriceList;
				}				
			}			
		} catch (RepositoryException exc) {
			if (isLoggingError()) {
				logError("Error while retriving PriceList");
				logError("RepositoryException" + exc);
			}
		}
		if(isLoggingDebug()){
			logDebug("Exit getPriceListFotMarket method");
			logDebug("Coverage : " + coverage +" profilePriceList : " + profilePriceList);
		}
		return profilePriceList;		
	}
}
