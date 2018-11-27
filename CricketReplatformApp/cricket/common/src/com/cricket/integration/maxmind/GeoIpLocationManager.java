package com.cricket.integration.maxmind;

import atg.core.util.StringUtils;
import atg.nucleus.GenericService;

/* 
* GeoIpLocationManager extends  GenericService, this class is used for getting 
* the city info by passing ip address of the user
* @author : techM
* @version 1.0
*/ 
public class GeoIpLocationManager extends GenericService{
	
	//property : mGeoIpLocation
	GeoIpLocationTools mGeoIpLocationTools;
	


	/**
	 * @return the geoIpLocationTools
	 */
	public GeoIpLocationTools getGeoIpLocationTools() {
		return mGeoIpLocationTools;
	}

	/**
	 * @param pGeoIpLocationTools the geoIpLocationTools to set
	 */
	public void setGeoIpLocationTools(GeoIpLocationTools pGeoIpLocationTools) {
		mGeoIpLocationTools = pGeoIpLocationTools;
	}

	/**
	 * The method is used to get the city details by using the ip address of the user
	 * @param pIpAddress - String
	 * @return CityVO
	 * */
	public CityVO fetchCityForIp(String pIpAddress){
		if(isLoggingDebug()){
			logDebug("Inside fetchCityForIp");
		}
		CityVO cityVO= new CityVO();
		if(pIpAddress != null && !StringUtils.isEmpty(pIpAddress)){
			cityVO = getGeoIpLocationTools().getCityInfoObject(pIpAddress);
		}
		if(isLoggingDebug()){
			logDebug("Exit fetchCityForIp");
		}
		return cityVO;
		
	}

	CityVO fetchCityForMobileUsers(String pLatitude, String pLongitude, String zipCode){
		
		if(isLoggingDebug()){
			logDebug("Inside fetchCityForMobileUsers");
		}
		CityVO cityVO= new CityVO();
		if(pLatitude != null && pLongitude != null){
			cityVO = getGeoIpLocationTools().getCityInfoFromRepository(pLatitude, pLongitude, zipCode);
		}
		if(isLoggingDebug()){
			logDebug("Exit fetchCityForMobileUsers:"+cityVO);			
		}
		return cityVO;
	}
}
