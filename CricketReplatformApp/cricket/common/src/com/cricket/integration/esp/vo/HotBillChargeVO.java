package com.cricket.integration.esp.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class HotBillChargeVO {
	/**
	 * Boolean to indicate if the charges are for the current or future service period. FALSE indicates current charges and TRUE indicates future charges.
	 */
	private boolean futurePeriodBoo;
	private Date effectiveDate;
	private Date expirationDate;
	private BigDecimal chargeTotal;
	private BigDecimal taxTotal;
	private boolean accountQuoteCharges_futurePeriodBoo;
	private Date accountQuoteCharges_effectiveDate;
	private Date accountQuoteCharges_expirationDate;
	private BigDecimal accountQuoteCharges_chargeTotal;
	private BigDecimal accountQuoteCharges_taxTotal;	
	private List<SubscriberQuoteChargesVO> subscriberQuoteCharges;	
	
	public boolean isFuturePeriodBoo() {
		return futurePeriodBoo;
	}
	public void setFuturePeriodBoo(boolean futurePeriodBoo) {
		this.futurePeriodBoo = futurePeriodBoo;
	}	
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
	public boolean isAccountQuoteCharges_futurePeriodBoo() {
		return accountQuoteCharges_futurePeriodBoo;
	}
	public void setAccountQuoteCharges_futurePeriodBoo(
			boolean accountQuoteCharges_futurePeriodBoo) {
		this.accountQuoteCharges_futurePeriodBoo = accountQuoteCharges_futurePeriodBoo;
	}	
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Date getAccountQuoteCharges_effectiveDate() {
		return accountQuoteCharges_effectiveDate;
	}
	public void setAccountQuoteCharges_effectiveDate(
			Date accountQuoteCharges_effectiveDate) {
		this.accountQuoteCharges_effectiveDate = accountQuoteCharges_effectiveDate;
	}
	public Date getAccountQuoteCharges_expirationDate() {
		return accountQuoteCharges_expirationDate;
	}
	public void setAccountQuoteCharges_expirationDate(
			Date accountQuoteCharges_expirationDate) {
		this.accountQuoteCharges_expirationDate = accountQuoteCharges_expirationDate;
	}
	public BigDecimal getAccountQuoteCharges_chargeTotal() {
		return accountQuoteCharges_chargeTotal;
	}
	public void setAccountQuoteCharges_chargeTotal(
			BigDecimal accountQuoteCharges_chargeTotal) {
		this.accountQuoteCharges_chargeTotal = accountQuoteCharges_chargeTotal;
	}
	public BigDecimal getAccountQuoteCharges_taxTotal() {
		return accountQuoteCharges_taxTotal;
	}
	public void setAccountQuoteCharges_taxTotal(
			BigDecimal accountQuoteCharges_taxTotal) {
		this.accountQuoteCharges_taxTotal = accountQuoteCharges_taxTotal;
	}
	
	public List<SubscriberQuoteChargesVO> getSubscriberQuoteCharges() {
		return subscriberQuoteCharges;
	}
	public void setSubscriberQuoteCharges(
			List<SubscriberQuoteChargesVO> subscriberQuoteCharges) {
		this.subscriberQuoteCharges = subscriberQuoteCharges;
	}	
}
