/**
 * 
 */
package com.cricket.vo;

import atg.nucleus.GenericService;

/**
 * Component created as a VO.
 * 
 * @author AK112151
 *
 */
public class MyCricketCookieLocationInfo extends GenericService {
	/**
	 * member variable marketID 
	 */
	private String marketCode;
	/**
	 * member variable zip 
	 */
	private String zip;
	/**
	 * member variable city 
	 */
	private String city;
	/**
	 * member variable state 
	 */
	private String state;
	
	private String networkProviderId;
	private String networkProviderName;
	private boolean geoIpDetecred = false;
	private boolean defaultLocation = false;
	
	
	/**
	 * @return the marketID
	 */
	public String getMarketCode() {
		return marketCode;
	}
	/**
	 * @param marketCode the marketID to set
	 */
	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the networkProviderName
	 */
	public String getNetworkProviderName() {
		return networkProviderName;
	}
	/**
	 * @param networkProviderName the networkProviderName to set
	 */
	public void setNetworkProviderName(String networkProviderName) {
		this.networkProviderName = networkProviderName;
	}
	/**
	 * @return the networkProviderId
	 */
	public String getNetworkProviderId() {
		return networkProviderId;
	}
	/**
	 * @param networkProviderId the networkProviderId to set
	 */
	public void setNetworkProviderId(String networkProviderId) {
		this.networkProviderId = networkProviderId;
	}
	public boolean isGeoIpDetecred() {
		return geoIpDetecred;
	}
	public void setGeoIpDetecred(boolean pGeoIpDetecred) {
		geoIpDetecred = pGeoIpDetecred;
	}
	public boolean isDefaultLocation() {
		return defaultLocation;
	}
	public void setDefaultLocation(boolean pDefaultLocation) {
		defaultLocation = pDefaultLocation;
	}
	
	@Override
	public String toString() {
		StringBuilder strValue = new StringBuilder();
		strValue.append("\n-------------------------------     MyCricketCookieLocationInfo      -------------------------------");
		strValue.append("marketCode = "+marketCode);
		strValue.append(" : zip = "+zip);
		strValue.append(" : city = "+city);
		strValue.append(" : state = "+state);
		strValue.append(" : networkProviderId = "+networkProviderId);
		strValue.append(" : networkProviderName = "+networkProviderName);
		strValue.append(" : geoIpDetecred = "+geoIpDetecred);
		strValue.append(" : defaultLocation = "+defaultLocation);
		return strValue.toString();
	}
	
}
