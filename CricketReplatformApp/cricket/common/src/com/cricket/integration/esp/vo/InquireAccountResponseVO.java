package com.cricket.integration.esp.vo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.axis.types.PositiveInteger;

public class InquireAccountResponseVO extends ESPResponseVO {
	private String billingAccountNumber;
	private String accountType;
	private String accountStatus;
	private String marketId;
	private String jointVentureCode;
	private PositiveInteger rateCenterId;
	private Calendar billingCycleDate;
	private String billingPreferencesLanguage;
	private boolean billingPreferencesABP;
	private String solicitationContactPreference;
	private String customerId;
	private String customerType;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String phone;
	private Date dob;
	private List<SubscriberVO> subscribers;
	private ResponseVO response;
	private AddressVO marketAddress;
	private AddressVO billingAddress;
	private String socialSecurityNumber;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public PositiveInteger getRateCenterId() {
		return rateCenterId;
	}
	public void setRateCenterId(PositiveInteger rateCenterId) {
		this.rateCenterId = rateCenterId;
	}
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public String getJointVentureCode() {
		return jointVentureCode;
	}
	public void setJointVentureCode(String jointVentureCode) {
		this.jointVentureCode = jointVentureCode;
	}
	public String getBillingAccountNumber() {
		return billingAccountNumber;
	}
	public void setBillingAccountNumber(String billingAccountNumber) {
		this.billingAccountNumber = billingAccountNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Calendar getBillingCycleDate() {
		return billingCycleDate;
	}
	public void setBillingCycleDate(Calendar billingCycleDate) {
		this.billingCycleDate = billingCycleDate;
	}
	public String getBillingPreferencesLanguage() {
		return billingPreferencesLanguage;
	}
	public void setBillingPreferencesLanguage(String billingPreferencesLanguage) {
		this.billingPreferencesLanguage = billingPreferencesLanguage;
	}
	
	public boolean isBillingPreferencesABP() {
		return billingPreferencesABP;
	}
	public void setBillingPreferencesABP(boolean billingPreferencesABP) {
		this.billingPreferencesABP = billingPreferencesABP;
	}
	public String getSolicitationContactPreference() {
		return solicitationContactPreference;
	}
	public void setSolicitationContactPreference(
			String solicitationContactPreference) {
		this.solicitationContactPreference = solicitationContactPreference;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public List<SubscriberVO> getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(List<SubscriberVO> subscribers) {
		this.subscribers = subscribers;
	}
	
	public ResponseVO getResponse() {
		return response;
	}
	public void setResponse(ResponseVO response) {
		this.response = response;
	}
	public AddressVO getMarketAddress() {
		return marketAddress;
	}
	public void setMarketAddress(AddressVO marketAddress) {
		this.marketAddress = marketAddress;
	}
	public AddressVO getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(AddressVO billingAddress) {
		this.billingAddress = billingAddress;
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
