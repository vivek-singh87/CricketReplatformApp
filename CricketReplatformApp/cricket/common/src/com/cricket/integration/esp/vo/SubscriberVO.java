package com.cricket.integration.esp.vo;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SubscriberVO {
	private String mdn;
	private String subscriberStatus;
	/**
	 * 
	 */
	 private boolean billingResponsibility;
	 
	 /**
	  * 
	  */
	 private String subscriptionType;
	 private DeviceVO device; 
	 
	 /**
	  * 
	  */
	 private Date effectiveDate;
	 
	 /**
	  * 
	  */
	 private Calendar expirationDate;
	 
	 /**
	  * 
	  */
	 private BigInteger term;
	 
	 private String planCode;
	 private String planType;
	 private String primaryPricePlanName;
	 private String primaryPricePlanDescription;
	 private List<OfferingsVO> bundledOfferings;
	 private List<OfferingsVO> additionalOfferings;
	 private String ratePlanTypeId;
	 private String ratePlanTypeName;
	 private int networkProviderId;
	//added to fix QC 8166
	//holds isDataAutoMetered value of DataMeterInfo element - InquireAccount ESP response
	private boolean dataAutoMetered;
	 
	 public String getMdn() {
		return mdn;
	}

	public void setMdn(String mdn) {
		this.mdn = mdn;
	}

	public String getSubscriberStatus() {
		return subscriberStatus;
	}

	public void setSubscriberStatus(String subscriberStatus) {
		this.subscriberStatus = subscriberStatus;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public DeviceVO getDevice() {
		return device;
	}

	public void setDevice(DeviceVO device) {
		this.device = device;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getPrimaryPricePlanName() {
		return primaryPricePlanName;
	}

	public void setPrimaryPricePlanName(String primaryPricePlanName) {
		this.primaryPricePlanName = primaryPricePlanName;
	}

	public String getPrimaryPricePlanDescription() {
		return primaryPricePlanDescription;
	}

	public void setPrimaryPricePlanDescription(String primaryPricePlanDescription) {
		this.primaryPricePlanDescription = primaryPricePlanDescription;
	}

	public List<OfferingsVO> getBundledOfferings() {
		return bundledOfferings;
	}

	public void setBundledOfferings(List<OfferingsVO> bundledOfferings) {
		this.bundledOfferings = bundledOfferings;
	}

	public List<OfferingsVO> getAdditionalOfferings() {
		return additionalOfferings;
	}

	public void setAdditionalOfferings(List<OfferingsVO> additionalOfferings) {
		this.additionalOfferings = additionalOfferings;
	}

	public String getRatePlanTypeId() {
		return ratePlanTypeId;
	}

	public void setRatePlanTypeId(String ratePlanTypeId) {
		this.ratePlanTypeId = ratePlanTypeId;
	}

	public String getRatePlanTypeName() {
		return ratePlanTypeName;
	}

	public void setRatePlanTypeName(String ratePlanTypeName) {
		this.ratePlanTypeName = ratePlanTypeName;
	}

	public int getNetworkProviderId() {
		return networkProviderId;
	}

	public void setNetworkProviderId(int networkProviderId) {
		this.networkProviderId = networkProviderId;
	}

	public String getNetworkProviderName() {
		return networkProviderName;
	}

	public void setNetworkProviderName(String networkProviderName) {
		this.networkProviderName = networkProviderName;
	}

	private String networkProviderName;
	 /**
	  * 
	  */
	 private String locationId;
	 
	 /**
	  * 
	  */
	 private String salesRepresentative;
	 
	 /**
	  * 
	  */
	 private String commissionableSalesRepresentative;
	 
	 /**
	  * 
	  */
	 private String salesChannel;
	 	 
	 /**
	  * 
	  */
	 private CustomerNameVO customerName;
	 
	 /**
	  * 
	  */
	 private String[] cricketOfferingCodes;
	 
	 /**
	  * 
	  */
	 private List<OrderLineItemVO> shippingOrderLines;

	public boolean isBillingResponsibility() {
		return billingResponsibility;
	}

	public void setBillingResponsibility(boolean billingResponsibility) {
		this.billingResponsibility = billingResponsibility;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Calendar getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BigInteger getTerm() {
		return term;
	}

	public void setTerm(BigInteger term) {
		this.term = term;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getSalesRepresentative() {
		return salesRepresentative;
	}

	public void setSalesRepresentative(String salesRepresentative) {
		this.salesRepresentative = salesRepresentative;
	}

	public String getSalesChannel() {
		return salesChannel;
	}

	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}	
	
	public List<OrderLineItemVO> getShippingOrderLines() {
		return shippingOrderLines;
	}

	public void setShippingOrderLines(List<OrderLineItemVO> shippingOrderLines) {
		this.shippingOrderLines = shippingOrderLines;
	}

	public String[] getCricketOfferingCodes() {
		return cricketOfferingCodes;
	}

	public void setCricketOfferingCodes(String[] cricketOfferingCodes) {
		this.cricketOfferingCodes = cricketOfferingCodes;
	}

	public String getCommissionableSalesRepresentative() {
		return commissionableSalesRepresentative;
	}

	public void setCommissionableSalesRepresentative(
			String commissionableSalesRepresentative) {
		this.commissionableSalesRepresentative = commissionableSalesRepresentative;
	}

	public CustomerNameVO getCustomerName() {
		return customerName;
	}

	public void setCustomerName(CustomerNameVO customerName) {
		this.customerName = customerName;
	}
	
	/**
	 * @return the dataAutoMetered
	 */
	public boolean isDataAutoMetered() {
		return dataAutoMetered;
	}

	/**
	 * @param dataAutoMetered the dataAutoMetered to set
	 */
	public void setDataAutoMetered(boolean dataAutoMetered) {
		this.dataAutoMetered = dataAutoMetered;
	} 	
}
