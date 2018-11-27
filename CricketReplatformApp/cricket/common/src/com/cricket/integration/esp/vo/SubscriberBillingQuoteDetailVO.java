/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author Cricket
 *
 */
public class SubscriberBillingQuoteDetailVO {

	
	private RecurringChargesVO totalRecurringCharges;
	
	private RecurringChargesVO totalRecurringSavings;
	
	private BigDecimal oneTimeCharge;
	
	private Calendar nextBillDate;
	
	
	
	/**
	 * 
	 */
	public SubscriberBillingQuoteDetailVO() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * @param pTotalRecurringCharges
	 * @param pTotalRecurringSavings
	 * @param pOneTimeCharge
	 * @param pNextBillDate
	 */
	public SubscriberBillingQuoteDetailVO(
			RecurringChargesVO pTotalRecurringCharges,
			RecurringChargesVO pTotalRecurringSavings,
			BigDecimal pOneTimeCharge, Calendar pNextBillDate) {
		super();
		totalRecurringCharges = pTotalRecurringCharges;
		totalRecurringSavings = pTotalRecurringSavings;
		oneTimeCharge = pOneTimeCharge;
		nextBillDate = pNextBillDate;
	}



	/**
	 * @return the totalRecurringCharges
	 */
	public RecurringChargesVO getTotalRecurringCharges() {
		return totalRecurringCharges;
	}



	/**
	 * @param pTotalRecurringCharges the totalRecurringCharges to set
	 */
	public void setTotalRecurringCharges(
			RecurringChargesVO pTotalRecurringCharges) {
		totalRecurringCharges = pTotalRecurringCharges;
	}



	/**
	 * @return the totalRecurringSavings
	 */
	public RecurringChargesVO getTotalRecurringSavings() {
		return totalRecurringSavings;
	}



	/**
	 * @param pTotalRecurringSavings the totalRecurringSavings to set
	 */
	public void setTotalRecurringSavings(
			RecurringChargesVO pTotalRecurringSavings) {
		totalRecurringSavings = pTotalRecurringSavings;
	}



	/**
	 * @return the oneTimeCharge
	 */
	public BigDecimal getOneTimeCharge() {
		return oneTimeCharge;
	}



	/**
	 * @param pOneTimeCharge the oneTimeCharge to set
	 */
	public void setOneTimeCharge(BigDecimal pOneTimeCharge) {
		oneTimeCharge = pOneTimeCharge;
	}



	/**
	 * @return the nextBillDate
	 */
	public Calendar getNextBillDate() {
		return nextBillDate;
	}



	/**
	 * @param pNextBillDate the nextBillDate to set
	 */
	public void setNextBillDate(Calendar pNextBillDate) {
		nextBillDate = pNextBillDate;
	}

}
