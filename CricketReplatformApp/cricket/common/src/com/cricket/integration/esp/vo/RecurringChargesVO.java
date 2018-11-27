/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.math.BigDecimal;

/**
 * @author Cricket
 *
 */
public class RecurringChargesVO {

	
	 private  BigDecimal amount;

	    private String chargePeriodUom;

	    private Integer chargePeriod;

	/**
		 * @param pAmount
		 * @param pChargePeriodUom
		 * @param pChargePeriod
		 */
		public RecurringChargesVO(BigDecimal pAmount,
				String pChargePeriodUom, Integer pChargePeriod) {
			super();
			amount = pAmount;
			chargePeriodUom = pChargePeriodUom;
			chargePeriod = pChargePeriod;
		}

	/**
	 * 
	 */
	public RecurringChargesVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param pAmount the amount to set
	 */
	public void setAmount(BigDecimal pAmount) {
		amount = pAmount;
	}

	/**
	 * @return the chargePeriodUom
	 */
	public String getChargePeriodUom() {
		return chargePeriodUom;
	}

	/**
	 * @param pChargePeriodUom the chargePeriodUom to set
	 */
	public void setChargePeriodUom(String pChargePeriodUom) {
		chargePeriodUom = pChargePeriodUom;
	}

	/**
	 * @return the chargePeriod
	 */
	public Integer getChargePeriod() {
		return chargePeriod;
	}

	/**
	 * @param pChargePeriod the chargePeriod to set
	 */
	public void setChargePeriod(Integer pChargePeriod) {
		chargePeriod = pChargePeriod;
	}

}
