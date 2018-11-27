package com.cricket.integration.esp.vo;

import java.util.List;

public class ActivationQuoteResponseVO extends ESPResponseVO {
	private String billingQuoteId;
	private String customerId;
	private String billingAccountNumber;
	private BillingQuoteDetailVO billingQuoteDetail;
	private List<HotBillChargeVO> hotBillCharge ;
	private double serviceTax;
	
	/**
	 * @return the serviceTax
	 */
	public double getServiceTax() {
		return serviceTax;
	}
	/**
	 * @param serviceTax the serviceTax to set
	 */
	public void setServiceTax(double serviceTax) {
		this.serviceTax = serviceTax;
	}
	public ResponseVO getResponseVO() {
		return responseVO;
	}
	public void setResponseVO(ResponseVO responseVO) {
		this.responseVO = responseVO;
	}
	public void setHotBillCharge(List<HotBillChargeVO> hotBillCharge) {
		this.hotBillCharge = hotBillCharge;
	}
	private ResponseVO responseVO;
	
	public String getBillingQuoteId() {
		return billingQuoteId;
	}
	public void setBillingQuoteId(String billingQuoteId) {
		this.billingQuoteId = billingQuoteId; 
	} 
	public String getCustomerId() {
		return customerId; 
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getBillingAccountNumber() {
		return billingAccountNumber;
	}
	public void setBillingAccountNumber(String billingAccountNumber) {
		this.billingAccountNumber = billingAccountNumber;
	}
	public BillingQuoteDetailVO getBillingQuoteDetail() {
		return billingQuoteDetail;
	}
	public void setBillingQuoteDetail(BillingQuoteDetailVO billingQuoteDetail) {
		this.billingQuoteDetail = billingQuoteDetail;
	}
	public List<HotBillChargeVO> getHotBillCharge() {
		return hotBillCharge;
	}
	 
	
}
