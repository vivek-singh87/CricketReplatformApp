package com.cricket.integration.esp.vo;

import java.math.BigDecimal;

	public class SubscriberChargesVO {
	private String chargeItemName;
	private int chargeItemTypeId;
	private BigDecimal chargeAmount;
	private BigDecimal taxAmount;
	private boolean recurringCharge;
	private String action;
	private String code;
	private String description;
	private BigDecimal dollarAdjustment;
	public String getChargeItemName() {
		return chargeItemName;
	}
	public void setChargeItemName(String chargeItemName) {
		this.chargeItemName = chargeItemName;
	}
	public int getChargeItemTypeId() {
		return chargeItemTypeId;
	}
	public void setChargeItemTypeId(int chargeItemTypeId) {
		this.chargeItemTypeId = chargeItemTypeId;
	}
	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	public boolean isRecurringCharge() {
		return recurringCharge;
	}
	public void setRecurringCharge(boolean recurringCharge) {
		this.recurringCharge = recurringCharge;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getDollarAdjustment() {
		return dollarAdjustment;
	}
	public void setDollarAdjustment(BigDecimal dollarAdjustment) {
		this.dollarAdjustment = dollarAdjustment;
	}	
	
	@Override
	public String toString() {
		

		StringBuilder strValue = new StringBuilder();
		strValue.append("SubscriberChargesVO : [ ");
		strValue.append("chargeItemName = "+chargeItemName);
		strValue.append(" : chargeItemTypeId = "+chargeItemTypeId);
		strValue.append(" : chargeAmount = "+chargeAmount);
		strValue.append(" : taxAmount = "+taxAmount);
		strValue.append(" : recurringCharge = "+recurringCharge);
		strValue.append(" : action = "+action);
		strValue.append(" : code = "+code);
		strValue.append(" : description = "+description);
		strValue.append(" : dollarAdjustment = "+dollarAdjustment);
		strValue.append(" ]");
		return strValue.toString();
	
	}
		
}
