package com.cricket.checkout.order;

import atg.nucleus.GenericService;

import com.cricket.checkout.utils.CricketAddress;

public class CricketAccountHolderAddressData extends GenericService {
	private CricketAddress accountAddress;
	private String day;
	private String month;
	private String Year;
	private boolean emailnotification;
	private String socialSecurityNumber;
	private String userIntentionVar;
	
	/**
	 * @return the userIntention
	 */
	public String getUserIntentionVar() {
		return userIntentionVar;
	}
	/**
	 * @param userIntentionVar the userIntentionVar to set
	 */
	public void setUserIntentionVar(String userIntentionVar) {
		this.userIntentionVar = userIntentionVar;
	}

	/**
	 * @return the accountAddress
	 */
	public CricketAddress getAccountAddress() {
		return accountAddress;
	}

	/**
	 * @param accountAddress the accountAddress to set
	 */
	public void setAccountAddress(CricketAddress accountAddress) {
		this.accountAddress = accountAddress;
	}

	

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return Year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		Year = year;
	}

	/**
	 * @return the emailnotification
	 */
	public boolean isEmailnotification() {
		return emailnotification;
	}

	/**
	 * @param emailnotification the emailnotification to set
	 */
	public void setEmailnotification(boolean emailnotification) {
		this.emailnotification = emailnotification;
	}

	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}

	/**
	 * @return the socialSecurityNumber
	 */
	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	/**
	 * @param socialSecurityNumber the socialSecurityNumber to set
	 */
	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}	

}
