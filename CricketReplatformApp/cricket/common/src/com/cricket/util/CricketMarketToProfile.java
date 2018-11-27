package com.cricket.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import atg.nucleus.GenericService;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.vo.InquireCoverageResponseVO;
import com.cricket.integration.maxmind.CityVO;
import com.cricket.integration.networkcoverage.NetworkCoverageManager;

public class CricketMarketToProfile extends GenericService{

	private NetworkCoverageManager networkCoverageManager;
	
	private Repository productCatalog;
	
	private ProfileServices profileServices;
	
	private String marketId;
	
	private String marketName;
	private boolean inquireCoverageTimeOut = false;
	
	/**
	 * The method is used to get the coverage details based on the city info of the current user 
	 * @param pRequest - CityVO
	 * */
	public boolean setCoverageToProfile(CityVO cityVO, Profile profile) {
		if (isLoggingDebug()) {
			logDebug("inside setCoverageToProfile");
		}

		//Profile profile = getProfileServices().getCurrentProfile();
		boolean isNoService = false;
		// Set the Market Type based on the market ID
		int networkProviderId;
		if (null != cityVO && null != cityVO.getPostalCode()) {

			networkProviderId = getMarketFromDRM(cityVO, profile);
			
			// Get details from ESP inquireCoverage if Network id is 2(Sprint or No data was found in market DRM)
			if (networkProviderId != 1) {
				try {
					InquireCoverageResponseVO responseVO = getNetworkCoverageManager().getCoverageDetails(cityVO);
					if (null != responseVO && responseVO.getCoverageMarket() != null) {						
						// Set network details to profile
						String sprintCSA = responseVO.getCoverageMarket().getSprintCsa();
						String networkProviderName = null;
						if(responseVO.getCoverageMarket().getNetworkProvider() != null && responseVO.getCoverageMarket().getNetworkProvider().getName() != null) {
							networkProviderName = responseVO.getCoverageMarket().getNetworkProvider().getName().getValue();
						}
						if(sprintCSA != null && !sprintCSA.isEmpty()) {
							profile.setPropertyValue(CricketCommonConstants.SPRINT_CSA, sprintCSA);
						}
						if(networkProviderName == null || networkProviderName.isEmpty()) {
							isNoService = true;
						}
						if (isLoggingDebug()) {
							logDebug(CricketCommonConstants.SPRINT_CSA + " : "
									+ responseVO.getCoverageMarket().getSprintCsa() + " "
									+ CricketCommonConstants.MARKET_ID + " : "
									+ responseVO.getCoverageMarket().getMarketID());
						}
					} else if(responseVO == null){
						setInquireCoverageTimeOut(true);
						//return true;
					} else {
						isNoService = true;
						if (isLoggingDebug()) {
							logDebug("No Coverage Information received for city : "
									+ cityVO.getCity());
						}
					}
				} catch (CricketException e) {
					if (isLoggingError()) {
						logError("Error while fetching network information from ESP : "
								+ e.getMessage());
					}
					if(isLoggingDebug()) {
						logDebug("there was an error while fetching inquireCoverage response : ");
					}
					setInquireCoverageTimeOut(true);
				}
			} else {
				setInquireCoverageTimeOut(false);
			}

			if (null != getMarketId()) {
				setMarketTypeToProfile(profile, getMarketId());
			} else {
				profile.setPropertyValue(CricketCommonConstants.NETWORK_PROVIDER, null);
				profile.setPropertyValue(CricketCommonConstants.MARKET_ID, null);
				profile.setPropertyValue(CricketCommonConstants.SPRINT_CSA, null);
				profile.setPropertyValue(CricketCommonConstants.MARKET_TYPE, null);				
			}
		}
		if (isLoggingDebug()) {
			logDebug("Exit setCoverageToProfile");
		}
		return isNoService;
	}
	
	private int getMarketFromDRM(CityVO cityVO, Profile profile){
		// get market details from Market DRM info
		int networkProviderId = 0;
		String marketId = null;
		String jointVenture = null;		 
		String rateCenterId = null;		
		String marketName = null;
		try {
			RepositoryView catalogRepoView = getProductCatalog().getView(CricketCommonConstants.MARKET_DRM_INFO);		
			Object[] objects = new Object[1];
			objects[0] = cityVO.getPostalCode();
			RqlStatement statement = RqlStatement.parseRqlStatement("zipCode=?0");
			RepositoryItem[] marketIds = statement.executeQuery(catalogRepoView, objects);
			if(isLoggingDebug()) {
				if(cityVO != null) {
					logDebug("zipcode to query:: " + cityVO.getPostalCode());
				}
				logDebug("markets returned from query:: " + marketIds);
			}
			if (null != marketIds){
				logDebug("marketIds length:: " + marketIds.length);
				RepositoryItem market = null;
				if(marketIds.length>1){
					for(RepositoryItem mkt : marketIds){
						if ((Integer)mkt.getPropertyValue(CricketCommonConstants.NETWORK_PROVIDER_ID) == 1){
							market = mkt;
							break;
						}
					}					
				}
				if(isLoggingDebug()) {
					logDebug("selected market:: " + market);
				}
				if(null == market){
					market = marketIds[0];
					if(isLoggingDebug()) {
						logDebug("selected market:: " + market);
					}
				}
				if(market != null){
					networkProviderId = (Integer) market.getPropertyValue(CricketCommonConstants.NETWORK_PROVIDER_ID);
					marketId = (String) market.getPropertyValue(CricketCommonConstants.MARKET_CODE);
					jointVenture = (String) market.getPropertyValue(CricketCommonConstants.COMPANY_CODE);					
					rateCenterId = (String) market.getPropertyValue(CricketCommonConstants.RATE_CENTER_ID);		
					marketName = (String) market.getPropertyValue(CricketCommonConstants.PROP_MARKET_NAME);
				}
			}
			
			// Set marketId to Profile
			if (null != marketId) {
				profile.setPropertyValue(CricketCommonConstants.MARKET_ID, marketId);
				setMarketId(marketId);
				if (isLoggingDebug()) 
					logDebug(CricketCommonConstants.MARKET_ID + " : " + marketId);
			}
			
			if(null != marketName){
				
				profile.setPropertyValue(CricketCommonConstants.PROP_MARKET_NAME, marketName);
				setMarketName(marketName);
				if (isLoggingDebug()) 
					logDebug(CricketCommonConstants.PROP_MARKET_NAME + " : " + marketName);
				
			}
			
			
			// Set jointVenture to Profile
//			if (null != jointVenture) {
				profile.setPropertyValue(CricketCommonConstants.JOINT_VENTURE, jointVenture);
				if (isLoggingDebug()) 
					logDebug(CricketCommonConstants.JOINT_VENTURE + " : " + jointVenture);
//			}
			// Set rateCenterId to Profile
//			if (null != rateCenterId) {
				profile.setPropertyValue(CricketCommonConstants.RATE_CENTER_ID, rateCenterId);
				if (isLoggingDebug()) 
					logDebug(CricketCommonConstants.RATE_CENTER_ID + " : " + rateCenterId);				
//			}
			// Set networkProvider to Profile
			if (networkProviderId == 1) {
				profile.setPropertyValue(CricketCommonConstants.NETWORK_PROVIDER, CricketCommonConstants.NETWORKTYPE_CRICKET);
			}
			else if (networkProviderId == 2) {
				profile.setPropertyValue(CricketCommonConstants.NETWORK_PROVIDER, CricketCommonConstants.NETWORKTYPE_SPRINT);
			} 
			if (isLoggingDebug()) {
				logDebug(CricketCommonConstants.NETWORK_PROVIDER_ID + " : "+ networkProviderId);
				logDebug(CricketCommonConstants.NETWORK_PROVIDER + " : " + profile.getPropertyValue(CricketCommonConstants.NETWORK_PROVIDER));
			}			
			
		} catch (RepositoryException repositoryException) {
			if (isLoggingError()) {
				logError("Error while fetching Market details : ", repositoryException);
			}
		}
		return networkProviderId;		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private void setMarketTypeToProfile(Profile profile,String marketId){
		if(isLoggingDebug()) {
			logDebug("trying to get the market type for marketId:::: " + marketId);
		}
		String marketType = null;
		//String marketSubType = null;
		List<String> marketSubTypeList = new ArrayList<String>(); 
		try {			
			// Get MarketType info from PARC Market Info
			RepositoryView catalogRepoView = getProductCatalog().getView(CricketCommonConstants.MARKET_INFO);
			Object[] objects = new Object[1];
			objects[0] = marketId;
			RqlStatement statement = RqlStatement.parseRqlStatement("marketCode=?0");
			RepositoryItem[] marketItems = statement.executeQuery(catalogRepoView, objects);
			if (null != marketItems) {
				if(isLoggingDebug()) {
					logDebug("marketItems returned for current marketId:::: " + marketItems.toString());
				}
				Set<RepositoryItem> marketTypes = (Set<RepositoryItem>) marketItems[0].getPropertyValue(CricketCommonConstants.MARKET_TYPES);
				if(null != marketTypes && !marketTypes.isEmpty()){
					List marketTypesList = new ArrayList(marketTypes);
					marketType = (String)((RepositoryItem)marketTypesList.get(0)).getPropertyValue(CricketCommonConstants.MARKET_TYPE_NAME);
				}
				Set<RepositoryItem> marketSubTypes = (Set<RepositoryItem>) marketItems[0].getPropertyValue(CricketCommonConstants.MARKET_SUB_TYPES);
				if(null != marketSubTypes && !marketSubTypes.isEmpty()){
					List marketSubTypesList = new ArrayList(marketSubTypes);
					//marketSubType = (String)((RepositoryItem)marketSubTypesList.get(0)).getPropertyValue(CricketCommonConstants.MARKET_SUB_TYPE_NAME);
					for(Object marketSubTypeItem : marketSubTypesList){
						String marketSubTypeFromList = (String)((RepositoryItem)marketSubTypesList.get(0)).getPropertyValue(CricketCommonConstants.MARKET_SUB_TYPE_NAME);
						if(marketSubTypeFromList != null){
							marketSubTypeList.add(marketSubTypeFromList);
						}
					}
				}
			}
			if (null != marketType) {
				if (isLoggingDebug()) 
					logDebug(CricketCommonConstants.MARKET_TYPE + " : " + marketType);
				profile.setPropertyValue(CricketCommonConstants.MARKET_TYPE, marketType);
			}
			vlogDebug(CricketCommonConstants.MARKET_SUB_TYPES + " : " + marketSubTypeList);
			profile.setPropertyValue(CricketCommonConstants.MARKET_SUB_TYPES, marketSubTypeList);
		} catch (RepositoryException repositoryException) {
			if (isLoggingError()) {
				logError("Error while fetching Market details : ", repositoryException);
			}
		}
	}

	/**
	 * @return the networkCoverageManager
	 */
	public NetworkCoverageManager getNetworkCoverageManager() {
		return networkCoverageManager;
	}

	/**
	 * @param networkCoverageManager the networkCoverageManager to set
	 */
	public void setNetworkCoverageManager(
			NetworkCoverageManager networkCoverageManager) {
		this.networkCoverageManager = networkCoverageManager;
	}

	/**
	 * @return the productCatalog
	 */
	public Repository getProductCatalog() {
		return productCatalog;
	}

	/**
	 * @param productCatalog the productCatalog to set
	 */
	public void setProductCatalog(Repository productCatalog) {
		this.productCatalog = productCatalog;
	}

	/**
	 * @return the profileServices
	 */
	public ProfileServices getProfileServices() {
		return profileServices;
	}

	/**
	 * @param profileServices the profileServices to set
	 */
	public void setProfileServices(ProfileServices profileServices) {
		this.profileServices = profileServices;
	}

	/**
	 * @return the marketId
	 */
	public String getMarketId() {
		return marketId;
	}

	/**
	 * @param marketId the marketId to set
	 */
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	
	/**
	 * @return the marketName
	 */
	public String getMarketName() {
		return marketName;
	}

	/**
	 * @param marketName the marketName to set
	 */
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	/**
	 * @return the inquireCoverageTimeOut
	 */
	public boolean isInquireCoverageTimeOut() {
		return inquireCoverageTimeOut;
	}

	/**
	 * @param inquireCoverageTimeOut the inquireCoverageTimeOut to set
	 */
	public void setInquireCoverageTimeOut(boolean inquireCoverageTimeOut) {
		this.inquireCoverageTimeOut = inquireCoverageTimeOut;
	}
}
