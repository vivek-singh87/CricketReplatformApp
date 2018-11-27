package com.cricket.integration.esp.vo;

import java.math.BigDecimal;
import java.util.List;

public class SubscriberQuoteChargesVO {
	private BigDecimal chargeTotal;
	private BigDecimal taxTotal;
	private String mdn;
	private String min;
	private String equipmentIdentifer;
	private List<SubscriberChargesVO> subscriberCharges;
	public BigDecimal getChargeTotal() {
		return chargeTotal;
	}
	public void setChargeTotal(BigDecimal chargeTotal) {
		this.chargeTotal = chargeTotal;
	}
	public BigDecimal getTaxTotal() {
		return taxTotal;
	}
	public void setTaxTotal(BigDecimal taxTotal) {
		this.taxTotal = taxTotal;
	}
	public String getMdn() {
		return mdn;
	}
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public List<SubscriberChargesVO> getSubscriberCharges() {
		return subscriberCharges;
	}
	public void setSubscriberCharges(List<SubscriberChargesVO> subscriberCharges) {
		this.subscriberCharges = subscriberCharges;
	}
	public String getEquipmentIdentifer() {
		return equipmentIdentifer;
	}
	public void setEquipmentIdentifer(String equipmentIdentifer) {
		this.equipmentIdentifer = equipmentIdentifer;
	}
	
}
