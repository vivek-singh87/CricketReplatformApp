package com.cricket.vo;

import java.util.List;

import atg.nucleus.GenericService;

/**
 * Component created as a VO.
 * 
 * @author AK112151
 *
 */
public class CricketProfile extends GenericService {

	
	// MobileNumber from account.mycricket.com
	private String mdn;
	// PhoneCode from account.mycricket.com
	private String phoneCode;
	// PlaneName from account.mycricket.com
	private String planeName;
	// PlanCode from account.mycricket.com
	private String planCode;
	// ProductType from account.mycricket.com
	private String productType;
	// CustomerType from account.mycricket.com
	private String customerType;
	// NumberofLines from account.mycricket.com
	private String NumberOfLines ;
	// UserName from account.mycricket.com
	private String userName;
	// password from account.mycricket.com
	private String password;
	// FirstName from account.mycricket.com
	private String firstName;
	// LastName from account.mycricket.com
	private String lastName;
	// MiddleName from account.mycricket.com
	private String middleName;
	// ABPFlag from account.mycricket.com
	private boolean aBPFlag;
	// AccountNumber from account.mycricket.com
	private String userId;
	private String marketCode;
	private String custTypeName;
	private String ratePlanGroupCode;
	private String deviceModel;
	private String voice;
	private String data = null;
	private String familyplanLOS =null;
	private String accountNumber;
	private String accountName;
	private String ratePlanTypeId;
	private String ratePlanTypeName;
	private String intention;
	private String familyPlan;
	private String ratePlanCode;
	private String date;
	private List<String> additionalOfferingProducts;
	private List<String> bundledOfferingProducts;	
	private List<String> userPurchasedOfferingProducts;	

	// these are not related cookie , setting from inquireAccount response - esp calls
	private String customerId;
	private String accountType;
	/* This will be set from InquireAccount response and used in ESP calls.
	 * It has been observed that customerType from cookie is different from customerType from InquireAccount response
	 * Hence using new variable 
	  */
	private String espCustomerType;
	/* billcycle date, set from inquireAccount for logged in user */
	private int billCycleDay;

	private String encryptedCricketCookieValue;
	
	private String languageIdentifier;
	/**
	 * @return the numberOfLines
	 */
	public String getNumberOfLines() {
		return NumberOfLines;
	}

	/**
	 * @param numberOfLines the numberOfLines to set
	 */
	public void setNumberOfLines(String numberOfLines) {
		NumberOfLines = numberOfLines;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the ratePlanTypeId
	 */
	public String getRatePlanTypeId() {
		return ratePlanTypeId;
	}

	/**
	 * @param ratePlanTypeId the ratePlanTypeId to set
	 */
	public void setRatePlanTypeId(String ratePlanTypeId) {
		this.ratePlanTypeId = ratePlanTypeId;
	}

	/**
	 * @return the ratePlanTypeName
	 */
	public String getRatePlanTypeName() {
		return ratePlanTypeName;
	}

	/**
	 * @param ratePlanTypeName the ratePlanTypeName to set
	 */
	public void setRatePlanTypeName(String ratePlanTypeName) {
		this.ratePlanTypeName = ratePlanTypeName;
	}

	/**
	 * @return the intention
	 */
	public String getIntention() {
		return intention;
	}

	/**
	 * @param intention the intention to set
	 */
	public void setIntention(String intention) {
		this.intention = intention;
	}

	/**
	 * @return the familyPlan
	 */
	public String getFamilyPlan() {
		return familyPlan;
	}

	/**
	 * @param familyPlan the familyPlan to set
	 */
	public void setFamilyPlan(String familyPlan) {
		this.familyPlan = familyPlan;
	}

	/**
	 * @return the mdn
	 */
	public String getMdn() {
		return mdn;
	}

	/**
	 * @param mdn the mdn to set
	 */
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}

	/**
	 * @return the aBPFlag
	 */
	public boolean isaBPFlag() {
		return aBPFlag;
	}

	/**
	 * @param aBPFlag the aBPFlag to set
	 */
	public void setaBPFlag(boolean aBPFlag) {
		this.aBPFlag = aBPFlag;
	}

	/**
	 * @return the marketCode
	 */
	public String getMarketCode() {
		return marketCode;
	}

	/**
	 * @param marketCode the marketCode to set
	 */
	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}

	/**
	 * @return the custTypeName
	 */
	public String getCustTypeName() {
		return custTypeName;
	}

	/**
	 * @param custTypeName the custTypeName to set
	 */
	public void setCustTypeName(String custTypeName) {
		this.custTypeName = custTypeName;
	}

	/**
	 * @return the ratePlanGroupCode
	 */
	public String getRatePlanGroupCode() {
		return ratePlanGroupCode;
	}

	/**
	 * @param ratePlanGroupCode the ratePlanGroupCode to set
	 */
	public void setRatePlanGroupCode(String ratePlanGroupCode) {
		this.ratePlanGroupCode = ratePlanGroupCode;
	}

	/**
	 * @return the deviceModel
	 */
	public String getDeviceModel() {
		return deviceModel;
	}

	/**
	 * @param deviceModel the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	/**
	 * @return the voice
	 */
	public String getVoice() {
		return voice;
	}

	/**
	 * @param voice the voice to set
	 */
	public void setVoice(String voice) {
		this.voice = voice;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the familyplanLOS
	 */
	public String getFamilyplanLOS() {
		return familyplanLOS;
	}

	/**
	 * @param familyplanLOS the familyplanLOS to set
	 */
	public void setFamilyplanLOS(String familyplanLOS) {
		this.familyplanLOS = familyplanLOS;
	}

	
	/**
	 * @return the phoneCode
	 */
	public String getPhoneCode() {
		return phoneCode;
	}

	/**
	 * @param phoneCode
	 *            the phoneCode to set
	 */
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	/**
	 * @return the planeName
	 */
	public String getPlaneName() {
		return planeName;
	}

	/**
	 * @param planeName
	 *            the planeName to set
	 */
	public void setPlaneName(String planeName) {
		this.planeName = planeName;
	}

	/**
	 * @return the planCode
	 */
	public String getPlanCode() {
		return planCode;
	}

	/**
	 * @param planCode
	 *            the planCode to set
	 */
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}

	/**
	 * @param productType
	 *            the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	/**
	 * @return the customerType
	 */
	public String getCustomerType() {
		return customerType;
	}

	/**
	 * @param customerType
	 *            the customerType to set
	 */
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the aBPFlag
	 */
	public boolean isABPFlag() {
		return aBPFlag;
	}

	/**
	 * @param aBPFlag
	 *            the aBPFlag to set
	 */
	public void setABPFlag(boolean aBPFlag) {
		this.aBPFlag = aBPFlag;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the ratePlanCode
	 */
	public String getRatePlanCode() {
		return ratePlanCode;
	}

	/**
	 * @param ratePlanCode the ratePlanCode to set
	 */
	public void setRatePlanCode(String ratePlanCode) {
		this.ratePlanCode = ratePlanCode;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getAdditionalOfferingProducts() {
		return additionalOfferingProducts;
	}

	public void setAdditionalOfferingProducts(
			List<String> pAdditionalOfferingProducts) {
		additionalOfferingProducts = pAdditionalOfferingProducts;
	}

	public List<String> getBundledOfferingProducts() {
		return bundledOfferingProducts;
	}

	public void setBundledOfferingProducts(List<String> pBundledOfferingProducts) {
		bundledOfferingProducts = pBundledOfferingProducts;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the espCustomerType
	 */
	public String getEspCustomerType() {
		return espCustomerType;
	}

	/**
	 * @param espCustomerType the espCustomerType to set
	 */
	public void setEspCustomerType(String espCustomerType) {
		this.espCustomerType = espCustomerType;
	}

	/**
	 * @return the billCycleDay
	 */
	public int getBillCycleDay() {
		return billCycleDay;
	}

	/**
	 * @param billCycleDay the billCycleDay to set
	 */
	public void setBillCycleDay(int billCycleDay) {
		this.billCycleDay = billCycleDay;
	}

	/**
	 * @return the encryptedCricketCookieValue
	 */
	public String getEncryptedCricketCookieValue() {
		return encryptedCricketCookieValue;
	}

	/**
	 * @param encryptedCricketCookieValue the encryptedCricketCookieValue to set
	 */
	public void setEncryptedCricketCookieValue(String encryptedCricketCookieValue) {
		this.encryptedCricketCookieValue = encryptedCricketCookieValue;
	}

	/**
	 * @return the userPurchasedOfferingProducts
	 */
	public List<String> getUserPurchasedOfferingProducts() {
		return userPurchasedOfferingProducts;
	}

	/**
	 * @param userPurchasedOfferingProducts the userPurchasedOfferingProducts to set
	 */
	public void setUserPurchasedOfferingProducts(
			List<String> userPurchasedOfferingProducts) {
		this.userPurchasedOfferingProducts = userPurchasedOfferingProducts;
	}

 
	 
	/**
	 * @return the languageIdentifier
	 */
	public String getLanguageIdentifier() {
		return languageIdentifier;
	}

	/**
	 * @param languageIdentifier the languageIdentifier to set
	 */
	public void setLanguageIdentifier(String languageIdentifier) {
		this.languageIdentifier = languageIdentifier;
	}
	
	@Override
	public String toString() {
		
		StringBuilder strValue = new StringBuilder();
		strValue.append("\n-------------------------------     CricketProfile Info     -------------------------------");
		strValue.append("mdn = "+mdn);
		strValue.append(" : phoneCode = "+phoneCode);
		strValue.append(" : planeName = "+planeName);
		strValue.append(" : planCode = "+planCode);
		strValue.append(" : productType = "+productType);
		strValue.append(" : customerType = "+customerType);
		strValue.append(" : NumberOfLines = "+NumberOfLines);
		strValue.append(" : userName = "+userName);
		strValue.append(" : password = "+password);
		strValue.append(" : firstName = "+firstName);
		strValue.append(" : lastName = "+lastName);
		strValue.append(" : middleName = "+middleName);
		strValue.append(" : aBPFlag = "+aBPFlag);
		strValue.append(" : userId = "+userId);
		strValue.append(" : marketCode = "+marketCode);
		strValue.append(" : custTypeName = "+custTypeName);
		strValue.append(" : ratePlanGroupCode = "+ratePlanGroupCode);
		strValue.append(" : deviceModel = "+deviceModel);
		strValue.append(" : voice = "+voice);
		strValue.append(" : data = "+data);
		strValue.append(" : familyplanLOS = "+familyplanLOS);
		strValue.append(" : accountNumber = "+accountNumber);
		strValue.append(" : accountName = "+accountName);
		strValue.append(" : ratePlanTypeId = "+ratePlanTypeId);
		strValue.append(" : ratePlanTypeName = "+ratePlanTypeName);
		strValue.append(" : intention = "+intention);
		strValue.append(" : familyPlan = "+familyPlan);
		strValue.append(" : ratePlanCode = "+ratePlanCode);
		strValue.append(" : date = "+date);
		strValue.append(" : customerId = "+customerId);
		strValue.append(" : accountType = "+accountType);
		strValue.append(" : espCustomerType = "+espCustomerType);
		strValue.append(" : billCycleDay = "+billCycleDay);
		
		if(additionalOfferingProducts != null){
			strValue.append(" : additionalOfferingProducts = "+additionalOfferingProducts);
		}
		if(bundledOfferingProducts != null){
			strValue.append(" : bundledOfferingProducts = "+bundledOfferingProducts);
		}
		if(userPurchasedOfferingProducts != null){
			strValue.append(" : additionalOfferingProducts = "+userPurchasedOfferingProducts);
		}
		
		return strValue.toString();
	}

	
}
