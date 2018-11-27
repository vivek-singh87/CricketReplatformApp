package com.cricket.integration.maxmind;

import java.util.Date;

/* 
* CitySessionInfoObject,this class ensure that the city/state lookup(interacting with MaxMind API) 
* is not performed after the initial session page request.
* @author : techM
* @version 1.0
*/ 
public class CitySessionInfoObject {
	
	//property : mCityVO
	CityVO mCityVO;
	
	boolean triedMobileLocation;
	
	//property : mFirstAccess
	boolean mFirstAccess;
	
	//property loggedIn
	boolean mLoggedIn;
	
	//property : mFirstAccess
	String mLocationInRepository;
	// holds delivery date of order
	Date estimateDeliveryDate;
	
	boolean openLocationDrawer;
	private boolean inquireCoverageTimeOut = false;
	private boolean inquireAccountTimeOut = false;

	/**
	 * @return the locationInRepository
	 */
	public String getLocationInRepository() {
		return mLocationInRepository;
	}

	/**
	 * @param pLocationInRepository the locationInRepository to set
	 */
	public void setLocationInRepository(String pLocationInRepository) {
		mLocationInRepository = pLocationInRepository;
	}

	public CitySessionInfoObject() {
		mCityVO = new CityVO();
	}

	/**
	 * @return the cityVO
	 */
	public CityVO getCityVO() {
		return mCityVO;
	}
	/**
	 * @param pCityVO the cityVO to set
	 */
	public void setCityVO(CityVO pCityVO) {
		mCityVO = pCityVO;
	}
	/**
	 * @return the firstAccess
	 */
	public boolean isFirstAccess() {
		return mFirstAccess;
	}
	/**
	 * @param pFirstAccess the firstAccess to set
	 */
	public void setFirstAccess(boolean pFirstAccess) {
		mFirstAccess = pFirstAccess;
	}

	/**
	 * @return the mLoggedIn
	 */
	public boolean isLoggedIn() {
		return mLoggedIn;
	}

	/**
	 * @param mLoggedIn the mLoggedIn to set
	 */
	public void setLoggedIn(boolean mLoggedIn) {
		this.mLoggedIn = mLoggedIn;
	}
	
	/**
	 * @return
	 */
	public Date getEstimateDeliveryDate() {
		return estimateDeliveryDate;
	}

	/**
	 * @param estimateDeliveryDate
	 */
	public void setEstimateDeliveryDate(Date estimateDeliveryDate) {
		this.estimateDeliveryDate = estimateDeliveryDate;
	}

	public boolean isOpenLocationDrawer() {
		return openLocationDrawer;
	}

	public void setOpenLocationDrawer(boolean openLocationDrawer) {
		this.openLocationDrawer = openLocationDrawer;
	}

	public boolean isTriedMobileLocation() {
		return triedMobileLocation;
	}

	public void setTriedMobileLocation(boolean triedMobileLocation) {
		this.triedMobileLocation = triedMobileLocation;
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

	/**
	 * @return the inquireAccountTimeOut
	 */
	public boolean isInquireAccountTimeOut() {
		return inquireAccountTimeOut;
	}

	/**
	 * @param inquireAccountTimeOut the inquireAccountTimeOut to set
	 */
	public void setInquireAccountTimeOut(boolean inquireAccountTimeOut) {
		this.inquireAccountTimeOut = inquireAccountTimeOut;
	}
	
	
}
