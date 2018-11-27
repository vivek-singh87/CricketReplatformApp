package com.cricket.integration.esp.vo;

import java.util.Date;

public class OfferingsVO {
	private String offeringCode;
	private String offeringName;
	private String offeringDescription;
	private String offeringValue;
	private Date effectiveDate;
	private int offerTypeId;
	public String getOfferingCode() {
		return offeringCode;
	}
	public void setOfferingCode(String offeringCode) {
		this.offeringCode = offeringCode;
	}
	public String getOfferingName() {
		return offeringName;
	}
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}
	public String getOfferingValue() {
		return offeringValue;
	}
	public void setOfferingValue(String offeringValue) {
		this.offeringValue = offeringValue;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public int getOfferTypeId() {
		return offerTypeId;
	}
	public void setOfferTypeId(int offerTypeId) {
		this.offerTypeId = offerTypeId;
	}
	public String getOfferingDescription() {
		return offeringDescription;
	}
	public void setOfferingDescription(String offeringDescription) {
		this.offeringDescription = offeringDescription;
	}	
	
}
