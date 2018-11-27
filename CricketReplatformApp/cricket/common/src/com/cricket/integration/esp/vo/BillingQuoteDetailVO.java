package com.cricket.integration.esp.vo;

import java.math.BigDecimal;
import java.util.Calendar;

public class BillingQuoteDetailVO {
	private BigDecimal totalRecurringCharges_amount;
	private String totalRecurringCharges_chargePeriodUom;
	private Integer totalRecurringCharges_chargePeriod;
	private BigDecimal totalRecurringSavings_amount;
	private String totalRecurringSavings_chargePeriodUom;
	private Integer totalRecurringSavings_chargePeriod;
	private BigDecimal oneTimeCharge;
	private Calendar nextBillDate;
	public BigDecimal getTotalRecurringCharges_amount() {
		return totalRecurringCharges_amount;
	}
	public void setTotalRecurringCharges_amount(
			BigDecimal totalRecurringCharges_amount) {
		this.totalRecurringCharges_amount = totalRecurringCharges_amount;
	}
	public String getTotalRecurringCharges_chargePeriodUom() {
		return totalRecurringCharges_chargePeriodUom;
	}
	public void setTotalRecurringCharges_chargePeriodUom(
			String totalRecurringCharges_chargePeriodUom) {
		this.totalRecurringCharges_chargePeriodUom = totalRecurringCharges_chargePeriodUom;
	}
	public Integer getTotalRecurringCharges_chargePeriod() {
		return totalRecurringCharges_chargePeriod;
	}
	public void setTotalRecurringCharges_chargePeriod(
			Integer totalRecurringCharges_chargePeriod) {
		this.totalRecurringCharges_chargePeriod = totalRecurringCharges_chargePeriod;
	}
	public BigDecimal getTotalRecurringSavings_amount() {
		return totalRecurringSavings_amount;
	}
	public void setTotalRecurringSavings_amount(
			BigDecimal totalRecurringSavings_amount) {
		this.totalRecurringSavings_amount = totalRecurringSavings_amount;
	}
	public String getTotalRecurringSavings_chargePeriodUom() {
		return totalRecurringSavings_chargePeriodUom;
	}
	public void setTotalRecurringSavings_chargePeriodUom(
			String totalRecurringSavings_chargePeriodUom) {
		this.totalRecurringSavings_chargePeriodUom = totalRecurringSavings_chargePeriodUom;
	}
	public Integer getTotalRecurringSavings_chargePeriod() {
		return totalRecurringSavings_chargePeriod;
	}
	public void setTotalRecurringSavings_chargePeriod(
			Integer totalRecurringSavings_chargePeriod) {
		this.totalRecurringSavings_chargePeriod = totalRecurringSavings_chargePeriod;
	}
	public BigDecimal getOneTimeCharge() {
		return oneTimeCharge;
	}
	public void setOneTimeCharge(BigDecimal oneTimeCharge) {
		this.oneTimeCharge = oneTimeCharge;
	}
	public Calendar getNextBillDate() {
		return nextBillDate;
	}
	public void setNextBillDate(Calendar nextBillDate) {
		this.nextBillDate = nextBillDate;
	}	
}
