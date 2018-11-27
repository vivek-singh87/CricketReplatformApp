package com.cricket.util;

import java.math.BigDecimal;

import atg.nucleus.GenericService;

public class CAQServiceAmount extends GenericService{

	private BigDecimal serviceTax;
	/**
	 * @return the serviceTax
	 */
	public BigDecimal getServiceTax() {
		return serviceTax;
	}
	/**
	 * @param serviceTax the serviceTax to set
	 */
	public void setServiceTax(BigDecimal serviceTax) {
		this.serviceTax = serviceTax;
	}
	/**
	 * @return the serviceAmount
	 */
	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}
	/**
	 * @param serviceAmount the serviceAmount to set
	 */
	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
	}
	/**
	 * @return the mdn
	 */
	public String getMdn() {
		return mdn;
	}
	/**
	 * @param mdn the mdn to set
	 */
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}
	private BigDecimal serviceAmount;
	private String mdn;
	
	@Override
	public String toString() {
	
		StringBuilder strValue = new StringBuilder();
		strValue.append("CAQServiceAmount : [ ");
		strValue.append("serviceTax = "+serviceTax);
		strValue.append(" : mdn = "+mdn);
		strValue.append(" : serviceAmount = "+serviceAmount);
		strValue.append(" ]");
		return strValue.toString();
	}
	
}
