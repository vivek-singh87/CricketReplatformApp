package com.cricket.common.util.droplet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.QueryExpression;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.integration.maxmind.CitySessionInfoObject;
import com.cricket.integration.maxmind.CityVO;
import com.cricket.integration.maxmind.GeoIpLocationManager;
import com.cricket.myaccount.CricketProfileTools;
import com.cricket.util.CricketMarketToProfile;

public class GetLocationInfoFromZipCode extends DynamoServlet {
	
	private Repository geoRepository;	
	
	//property : CricketMarketToProfile
	private CricketMarketToProfile cricketMarketToProfile;
	
	private ProfileServices profileServices;
	
	// property : mGeoIpLocationManager
	private	GeoIpLocationManager geoIpLocationManager;
	private String sharedCookieExpiredVal;
	private String mCitySessionInfoObjectPath;
	
	private String defaultIpAddr;
	
	private static final String ZIP_CODE = "zipCode";
	private static final String GEO_LOC_LIST = "geoLocationList";
	private static final String PRIMARY_RECORD = "primaryRecord";
	private static final String CONSTANT_P= "P";
	private static final String CRICKET_ZIPCODE_ITEM = "CricketzipCodeItem";
	private static final String CITY_ALIAS_CODE= "cityAliasCode";
	private static final String CITYVO= "CITYVO";
	private static final String NO_COVERAGE= "NoCoverage";
	
	
	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		String zipCode = pReq.getParameter(ZIP_CODE);		
		RepositoryItem[] geoItems = null;	
		CityVO cityVO = null;
		Profile profile = getProfileServices().getCurrentProfile();			
		try {				
				cityVO = new CityVO();
				cityVO.setPostalCode(zipCode);
				cityVO.setManulaEntry(true);
				cityVO.setGeoIpDetecred(true);
				//getCricketMarketToProfile().setCoverageToProfile(cityVO);					
				if(null != profile && null != profile.getPropertyValue(CricketCommonConstants.NETWORK_PROVIDER)){	
					 geoItems =getZipCodeInfo(zipCode);
				}				
				
			} catch (RepositoryException e) {
				if(isLoggingError()) {
					logError("view does not exist GetLocationInfoFromZipCode");
				}
			} 
		
		try {
			if (geoItems!= null && geoItems.length > 0) {
				if (geoItems.length == 1) {
					//commenting this code because we dont necessarily change the zipcode even if the length is one, this should be done in the form handler
					/*CitySessionInfoObject citySessionInfoObject = (CitySessionInfoObject) pReq.resolveName(getCitySessionInfoObjectPath());
					cityVO.setCity(geoItems[0].getPropertyValue(CricketCookieConstants.CITY).toString());
					cityVO.setState(geoItems[0].getPropertyValue(CricketCookieConstants.STATE).toString());
					citySessionInfoObject.setCityVO(cityVO);
					createSharedLocationCookie(pReq, pRes, cityVO, profile);*/
				}
				pReq.setParameter(GEO_LOC_LIST, geoItems); 
				pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
			} else {				
				cityVO = getGeoIpLocationManager().fetchCityForIp(getDefaultIpAddr());
				cityVO.setGeoIpDetecred(false);
				cityVO.setManulaEntry(false);
				cityVO.setDefaultLocation(true);
				profile.setPropertyValue(CricketCommonConstants.IS_DEFAULT_LOCATION, true);
				pReq.setParameter(CITYVO, cityVO);
				pReq.serviceLocalParameter(NO_COVERAGE, pReq, pRes);				
				//pReq.serviceLocalParameter("empty", pReq, pRes);
			}
		} catch (ServletException e) {
			if(isLoggingError()) {
				logError("there was a problem servicing content GetLocationInfoFromZipCode");
			}
		} catch (IOException e) {
			if(isLoggingError()) {
				logError("there was a problem servicing content GetLocationInfoFromZipCode");
			}
		}
		
	}

	public Repository getGeoRepository() {
		return geoRepository;
	}

	public void setGeoRepository(Repository geoRepository) {
		this.geoRepository = geoRepository;
	}
	
	/**
	 * @return the cricketMarketToProfile
	 */
	public CricketMarketToProfile getCricketMarketToProfile() {
		return cricketMarketToProfile;
	}
	
	/**
	 * @param cricketMarketToProfile the cricketMarketToProfile to set
	 */
	public void setCricketMarketToProfile(
			CricketMarketToProfile cricketMarketToProfile) {
		this.cricketMarketToProfile = cricketMarketToProfile;
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
	
	
	
	public GeoIpLocationManager getGeoIpLocationManager() {
		return geoIpLocationManager;
	}

	public void setGeoIpLocationManager(GeoIpLocationManager geoIpLocationManager) {
		this.geoIpLocationManager = geoIpLocationManager;
	}	
	

	public String getDefaultIpAddr() {
		return defaultIpAddr;
	}

	public void setDefaultIpAddr(String defaultIpAddr) {
		this.defaultIpAddr = defaultIpAddr;
	}
	

	/**
	 * @param zipCode
	 * @return
	 * @throws RepositoryException 
	 */
	private RepositoryItem[] getZipCodeInfo(String zipCode) throws RepositoryException{
		RepositoryView geoView = getGeoRepository().getView(CRICKET_ZIPCODE_ITEM);
		QueryBuilder builder = geoView.getQueryBuilder();
		
		QueryExpression propertyA = builder.createPropertyQueryExpression(PRIMARY_RECORD);
		QueryExpression valueA = builder.createConstantQueryExpression(CONSTANT_P);
		Query queryA = builder.createComparisonQuery(propertyA, valueA, QueryBuilder.EQUALS);
		
		
		QueryExpression propertyB = builder.createPropertyQueryExpression(CITY_ALIAS_CODE);
		Query queryB = builder.createIsNullQuery(propertyB);
		
		//OR QUERY
		Query[] ORQueryArry = {queryA, queryB};
		Query ORQuery = builder.createOrQuery(ORQueryArry);
		
		QueryExpression propertyC = builder.createPropertyQueryExpression(ZIP_CODE);
		QueryExpression valueC = builder.createConstantQueryExpression(zipCode);
		
		//OR QUERY
		Query ANDQuery = builder.createComparisonQuery(propertyC, valueC, QueryBuilder.EQUALS);		
		Query[] ANDQueryArry = {ORQuery, ANDQuery};
		
		//select zip_code, id, city_alias_mixed_case from zipcode where (city_alias_code is null OR primary_record = 'P') and zip_code = 80111
		
		// FINAL QUERY
		Query finalQuery = builder.createAndQuery(ANDQueryArry);
		
		return geoView.executeQuery(finalQuery);
	}
	
	/**
	 * @return the sharedCookieExpiredVal
	 */
	public String getSharedCookieExpiredVal() {
		return sharedCookieExpiredVal;
	}

	/**
	 * @param sharedCookieExpiredVal the sharedCookieExpiredVal to set
	 */
	public void setSharedCookieExpiredVal(String sharedCookieExpiredVal) {
		this.sharedCookieExpiredVal = sharedCookieExpiredVal;
	}

	/**
	 * @return the mCitySessionInfoObjectPath
	 */
	public String getCitySessionInfoObjectPath() {
		return mCitySessionInfoObjectPath;
	}

	/**
	 * @param mCitySessionInfoObjectPath the mCitySessionInfoObjectPath to set
	 */
	public void setCitySessionInfoObjectPath(String mCitySessionInfoObjectPath) {
		this.mCitySessionInfoObjectPath = mCitySessionInfoObjectPath;
	}

}
