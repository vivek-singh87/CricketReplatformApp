package com.cricket.integration.maxmind;

/* 
* CityVO is use to hold values for city, countryCode, countryName...etc
* @author : techM
* @version 1.0
*/ 
public class CityVO {
	
	//property : mCountryCode
	String mCountryCode;
	//property : mCountryName
	String mCountryName;
	//property : mRegioncode
	String mRegioncode;
	//property : mRegionName
	String mRegionName;
	//property : mCity
	String mCity;
	//property : mPostalCode
	String mPostalCode;
	//property : mLatitude
	double mLatitude;
	//property : mLongitude
	double mLongitude;
	//property : mMetroCode
	int mMetroCode;
	//property : mAreaCode
	String mAreaCode;
	//property : mTimeZone
	String mTimeZone;
	//property : mState
	String mState;
	
	private boolean manulaEntry = false;
	private boolean geoIpDetecred = false;
	private boolean defaultLocation = false;
	/**
	 * @return state
	 */
	public String getState() {
		return mState;
	}
	/**
	 * @param pState
	 */
	public void setState(String pState) {
		mState = pState;
	}
	/**
	 * @return the metroCode
	 */
	public int getMetroCode() {
		return mMetroCode;
	}
	/**
	 * @param pMetroCode the metroCode to set
	 */
	public void setMetroCode(int pMetroCode) {
		mMetroCode = pMetroCode;
	}
	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return mAreaCode;
	}
	/**
	 * @param pAreaCode the areaCode to set
	 */
	public void setAreaCode(String pAreaCode) { 
		mAreaCode = pAreaCode;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return mCity;
	}
	/**
	 * @param pCity the city to set
	 */
	public void setCity(String pCity) {
		mCity = pCity;
	}
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return mPostalCode;
	}
	/**
	 * @param pPostalCode the postalCode to set
	 */
	public void setPostalCode(String pPostalCode) {
		mPostalCode = pPostalCode;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return mLatitude;
	}
	/**
	 * @param pLatitude the latitude to set
	 */
	public void setLatitude(double pLatitude) {
		mLatitude = pLatitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return mLongitude;
	}
	/**
	 * @param pLongitude the longitude to set
	 */
	public void setLongitude(double pLongitude) {
		mLongitude = pLongitude;
	}
	/**
	 * @return the metroCode
	 */

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return mTimeZone;
	}
	/**
	 * @param pTimeZone the timeZone to set
	 */
	public void setTimeZone(String pTimeZone) {
		mTimeZone = pTimeZone;
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return mCountryCode;
	}
	/**
	 * @param pCountryCode the countryCode to set
	 */
	public void setCountryCode(String pCountryCode) {
		mCountryCode = pCountryCode;
	}
	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return mCountryName;
	}
	/**
	 * @param pCountryName the countryName to set
	 */
	public void setCountryName(String pCountryName) {
		mCountryName = pCountryName;
	}
	/**
	 * @return the regioncode
	 */
	public String getRegioncode() {
		return mRegioncode;
	}
	/**
	 * @param pRegioncode the regioncode to set
	 */
	public void setRegioncode(String pRegioncode) {
		mRegioncode = pRegioncode;
	}
	/**
	 * @return the regionName
	 */
	public String getRegionName() {
		return mRegionName;
	}
	/**
	 * @param pRegionName the regionName to set
	 */
	public void setRegionName(String pRegionName) {
		mRegionName = pRegionName;
	}
	@Override
	public String toString() {
		return "CityVO [mCountryCode=" + mCountryCode + ", mCountryName="
				+ mCountryName + ", mRegioncode=" + mRegioncode
				+ ", mRegionName=" + mRegionName + ", mCity=" + mCity
				+ ", mPostalCode=" + mPostalCode + ", mLatitude=" + mLatitude
				+ ", mLongitude=" + mLongitude + ", mMetroCode=" + mMetroCode
				+ ", mAreaCode=" + mAreaCode + ", mTimeZone=" + mTimeZone
				+ ", mState=" + mState + "]";
	}
	/**
	 * @return the manulaEntry
	 */
	public boolean isManulaEntry() {
		return manulaEntry;
	}
	/**
	 * @param manulaEntry the manulaEntry to set
	 */
	public void setManulaEntry(boolean manulaEntry) {
		this.manulaEntry = manulaEntry;
	}
	/**
	 * @return the geoIpDetecred
	 */
	public boolean isGeoIpDetecred() {
		return geoIpDetecred;
	}
	/**
	 * @param geoIpDetecred the geoIpDetecred to set
	 */
	public void setGeoIpDetecred(boolean geoIpDetecred) {
		this.geoIpDetecred = geoIpDetecred;
	}
	public boolean isDefaultLocation() {
		return defaultLocation;
	}
	public void setDefaultLocation(boolean defaultLocation) {
		this.defaultLocation = defaultLocation;
	}
	
	
	

}
