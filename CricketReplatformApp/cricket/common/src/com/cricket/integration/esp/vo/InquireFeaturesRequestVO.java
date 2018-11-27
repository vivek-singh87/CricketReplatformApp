package com.cricket.integration.esp.vo;

public class InquireFeaturesRequestVO {

	private String salesChannelName;
	private String marketId;
	private String transactionName;	
	private String pricePlanCode;
	private String phoneCode;
	private String phoneType;		
	private Boolean hasEsnHistory;
	private Boolean isCricketPhone;
	private String [] featureCodes;
	
	/**
	 * 
	 * @return
	 */
	public String getSalesChannelName() {
		return salesChannelName;
	}
	
	/**
	 * 
	 * @param salesChannelName
	 */
	public void setSalesChannelName(String salesChannelName) {
		this.salesChannelName = salesChannelName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMarketId() {
		return marketId;
	}
	
	/**
	 * 
	 * @param marketId
	 */
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTransactionName() {
		return transactionName;
	}
	
	/**
	 * 
	 * @param transactionName
	 */
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPricePlanCode() {
		return pricePlanCode;
	}
	
	/**
	 * 
	 * @param pricePlanCode
	 */
	public void setPricePlanCode(String pricePlanCode) {
		this.pricePlanCode = pricePlanCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPhoneCode() {
		return phoneCode;
	}
	
	/**
	 * 
	 * @param phoneCode
	 */
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPhoneType() {
		return phoneType;
	}
	
	/**
	 * 
	 * @param phoneType
	 */
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean getHasEsnHistory() {
		return hasEsnHistory;
	}
	
	/**
	 * 
	 * @param hasEsnHistory
	 */
	public void setHasEsnHistory(Boolean hasEsnHistory) {
		this.hasEsnHistory = hasEsnHistory;
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean getIsCricketPhone() {
		return isCricketPhone;
	}
	
	/**
	 * 
	 * @param isCricketPhone
	 */
	public void setIsCricketPhone(Boolean isCricketPhone) {
		this.isCricketPhone = isCricketPhone;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getFeatureCodes() {
		return featureCodes;
	}
	
	/**
	 * 
	 * @param featureCodes
	 */
	public void setFeatureCodes(String[] featureCodes) {
		this.featureCodes = featureCodes;
	}
}
