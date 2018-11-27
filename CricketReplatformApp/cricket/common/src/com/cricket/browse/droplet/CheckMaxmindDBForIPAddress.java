package com.cricket.browse.droplet;

import com.cricket.browse.droplet.util.LocationVO;
import com.cricket.common.constants.CricketCommonConstants;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import source.com.maxmind.geoip.Location;
import source.com.maxmind.geoip.LookupService;

public class CheckMaxmindDBForIPAddress extends DynamoServlet {
	
	private String geoIpCityUsFilePath;
	
	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		LookupService lookupService = null;
		String ipAdress = pReq.getParameter("ipAdress");
		if(isLoggingDebug()){
			logDebug("entering into CheckMaxmindDBForIPAddress service method");
			logDebug("ip address entered: " + ipAdress);
		}
		try {
			//get the LookupService object from the Max Mind
			 lookupService = new LookupService(getGeoIpCityUsFilePath(), LookupService.GEOIP_STANDARD );			
			 Location location = null;
			 if(lookupService != null) {
				 //get location object by passing ip address
				 location = lookupService.getLocation(ipAdress);
				 if(location != null) {
					 if(isLoggingDebug()){
							logDebug("location found with following details::");
							logDebug("city::" + location.city);
							logDebug("region::" + location.region);
							logDebug("postal code::" + location.postalCode);
					 }
					 LocationVO locationVO = new LocationVO();
					 locationVO.setCity(location.city);
					 locationVO.setCountry(location.countryName);
					 locationVO.setCountryCode(location.countryCode);
					 locationVO.setLatitude(location.latitude);
					 locationVO.setLongitude(location.longitude);
					 locationVO.setPostalCode(location.postalCode);
					 locationVO.setRegion(location.region);
					 pReq.setParameter(CricketCommonConstants.LOCATION, locationVO);
					 pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
				 } else {
					 if(isLoggingDebug()){
						 logDebug("no location found for given ip address");
					 }
					 pReq.serviceLocalParameter(CricketCommonConstants.ERROR, pReq, pRes);
				 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(isLoggingDebug()){
			 logDebug("exiting from CheckMaxmindDBForIPAddress service method");
		 }
	}

	public String getGeoIpCityUsFilePath() {
		return geoIpCityUsFilePath;
	}

	public void setGeoIpCityUsFilePath(String geoIpCityUsFilePath) {
		this.geoIpCityUsFilePath = geoIpCityUsFilePath;
	}

}
