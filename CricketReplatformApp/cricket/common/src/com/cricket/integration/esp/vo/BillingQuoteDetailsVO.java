/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Cricket
 *
 */
public class BillingQuoteDetailsVO {
	
	private RecurringChargesVO totalRecurringCharges;
	
	private RecurringChargesVO totalRecurringSavings;
	
	private BigDecimal oneTimeCharge;
	
	private Date nextBillDate;
	
	
	

	/**
	 * @param pTotalRecurringCharges
	 * @param pTotalRecurringSavings
	 * @param pOneTimeCharge
	 * @param pNextBillDate
	 */
	public BillingQuoteDetailsVO(RecurringChargesVO pTotalRecurringCharges,
			RecurringChargesVO pTotalRecurringSavings, BigDecimal pOneTimeCharge,
			Date pNextBillDate) {
		super();
		totalRecurringCharges = pTotalRecurringCharges;
		totalRecurringSavings = pTotalRecurringSavings;
		oneTimeCharge = pOneTimeCharge;
		nextBillDate = pNextBillDate;
	}




	/**
	 * 
	 */
	public BillingQuoteDetailsVO() {
		// TODO Auto-generated constructor stub
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
	public void setTotalRecurringCharges(RecurringChargesVO pTotalRecurringCharges) {
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
	public void setTotalRecurringSavings(RecurringChargesVO pTotalRecurringSavings) {
		totalRecurringSavings = pTotalRecurringSavings;
	}




	/**
	 * @return the oneTimeCharge
	 */
	public BigDecimal getOneTimeCharge() {
		return oneTimeCharge;
	}




	/**
	 * @param pBigDecimal the oneTimeCharge to set
	 */
	public void setOneTimeCharge(BigDecimal pBigDecimal) {
		oneTimeCharge = pBigDecimal;
	}




	/**
	 * @return the nextBillDate
	 */
	public Date getNextBillDate() {
		return nextBillDate;
	}




	/**
	 * @param pNextBillDate the nextBillDate to set
	 */
	public void setNextBillDate(Date pNextBillDate) {
		nextBillDate = pNextBillDate;
	}

}
