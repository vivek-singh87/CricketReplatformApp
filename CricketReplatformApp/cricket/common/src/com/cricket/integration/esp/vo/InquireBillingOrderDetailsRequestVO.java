package com.cricket.integration.esp.vo;

public class InquireBillingOrderDetailsRequestVO {

	
	private String billingOrderNumber;

	
	
	
	/**
	 * 
	 */
	public InquireBillingOrderDetailsRequestVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pBillingOrderNumber
	 */
	public InquireBillingOrderDetailsRequestVO(String pBillingOrderNumber) {
		super();
		billingOrderNumber = pBillingOrderNumber;
	}

	/**
	 * @return the billingOrderNumber
	 */
	public String getBillingOrderNumber() {
		return billingOrderNumber;
	}

	/**
	 * @param pBillingOrderNumber the billingOrderNumber to set
	 */
	public void setBillingOrderNumber(String pBillingOrderNumber) {
		billingOrderNumber = pBillingOrderNumber;
	}
	
	
}
