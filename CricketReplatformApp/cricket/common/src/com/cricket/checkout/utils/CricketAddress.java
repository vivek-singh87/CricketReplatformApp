/**
 * 
 */
package com.cricket.checkout.utils;

import java.io.Serializable;

/**
 * @author ak112151
 *
 */
public class CricketAddress implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String jobTitle;
	public String firstName;
	public String middleName;
	public String lastName;
	public String companyName;
	public String address1;
	public String address2;
	public String address3;
	public String city;
	public String country;
	public String stateAddress;
	public String postalCode;
	public String phoneNumber;
	public String faxNumber;
	public String email;
	public String saveAsBillingAddressYes;
	public String saveAsBillingAddressNo;

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}
	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}
	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	/**
	 * @return the address3
	 */
	public String getAddress3() {
		return address3;
	}
	/**
	 * @param address3 the address3 to set
	 */
	public void setAddress3(String address3) {
		this.address3 = address3;
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
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the stateAddress
	 */
	public String getStateAddress() {
		return stateAddress;
	}
	/**
	 * @param stateAddress the stateAddress to set
	 */
	public void setStateAddress(String stateAddress) {
		this.stateAddress = stateAddress;
	}
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the faxNumber
	 */
	public String getFaxNumber() {
		return faxNumber;
	}
	/**
	 * @param faxNumber the faxNumber to set
	 */
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the saveAsBillingAddressYes
	 */
	public String getSaveAsBillingAddressYes() {
		return saveAsBillingAddressYes;
	}
	/**
	 * @param saveAsBillingAddressYes the saveAsBillingAddressYes to set
	 */
	public void setSaveAsBillingAddressYes(String saveAsBillingAddressYes) {
		this.saveAsBillingAddressYes = saveAsBillingAddressYes;
	}
	/**
	 * @return the saveAsBillingAddressNo
	 */
	public String getSaveAsBillingAddressNo() {
		return saveAsBillingAddressNo;
	}
	/**
	 * @param saveAsBillingAddressNo the saveAsBillingAddressNo to set
	 */
	public void setSaveAsBillingAddressNo(String saveAsBillingAddressNo) {
		this.saveAsBillingAddressNo = saveAsBillingAddressNo;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}
	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CricketAddress [jobTitle=" + jobTitle + ", firstName="
				+ firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", companyName=" + companyName + ", address1="
				+ address1 + ", address2=" + address2 + ", address3="
				+ address3 + ", city=" + city + ", country=" + country
				+ ", stateAddress=" + stateAddress + ", postalCode="
				+ postalCode + ", phoneNumber=" + phoneNumber + ", faxNumber="
				+ faxNumber + ", email=" + email + ", saveAsBillingAddressYes="
				+ saveAsBillingAddressYes + ", saveAsBillingAddressNo="
				+ saveAsBillingAddressNo + "]";
	}
	
}
