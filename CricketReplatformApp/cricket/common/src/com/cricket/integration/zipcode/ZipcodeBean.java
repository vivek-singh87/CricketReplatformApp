package com.cricket.integration.zipcode;

import java.util.HashSet;
import java.util.Set;

public class ZipcodeBean {
	
	private String mZipcode;
	private String mState;
	private String mCity;
	private String mCountry;
	private String mCitytype;
	private String mAreacode;
	private String mLatitude;
	private String mLongitude;
	private String mClassificationcode;
	private String mTimezone;
	private String mCityAliasMixedCase;
	
	 public String getCityAliasMixedCase() {
		return mCityAliasMixedCase;
	}

	public void setCityAliasMixedCase(String pCityAliasMixedCase) {
		mCityAliasMixedCase = pCityAliasMixedCase;
	}

	Set<String> zipCodeSet = new HashSet<String>();

		public Set<String> getZipCodeSet() {
			return zipCodeSet;
		}

		public void setZipCodeSet(Set<String> pZipCodeSet) {
			zipCodeSet = pZipCodeSet;
		}

	public String getZipcode() {
		return mZipcode;
	}

	public void setZipcode(String pZipcode) {
		mZipcode = pZipcode;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String pCity) {
		mCity = pCity;
	}

	public String getState() {
		return mState;
	}

	public void setState(String pState) {
		mState = pState;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String pCountry) {
		mCountry = pCountry;
	}

	public String getCitytype() {
		return mCitytype;
	}

	public void setCitytype(String pCitytype) {
		mCitytype = pCitytype;
	}	

	public String getAreacode() {
		return mAreacode;
	}

	public void setAreacode(String pAreacode) {
		mAreacode = pAreacode;
	}

	public String getLatitude() {
		return mLatitude;
	}

	public void setLatitude(String pLatitude) {
		mLatitude = pLatitude;
	}

	public String getLongitude() {
		return mLongitude;
	}

	public void setLongitude(String pLongitude) {
		mLongitude = pLongitude;
	}

	public String getClassificationcode() {
		return mClassificationcode;
	}

	public void setClassificationcode(String pClassificationcode) {
		mClassificationcode = pClassificationcode;
	}

	public String getTimezone() {
		return mTimezone;
	}

	public void setTimezone(String pTimezone) {
		mTimezone = pTimezone;
	}

}
