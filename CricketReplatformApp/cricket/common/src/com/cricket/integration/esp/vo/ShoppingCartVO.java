package com.cricket.integration.esp.vo;

import java.util.List;

public class ShoppingCartVO {
	/**
	 * 
	 */
	private String saleId;
	
	/**
	 * 
	 */
	private String billingAccountNumber;
	
	/**
	 * Customer Name Info
	 */
	private String customerFirstName;
	private String customerLastName;
	
	/**
	 * 
	 */
	 private List<LineItemVO> lineItems;
	 
	 /**
	  * 
	  */
	 private List<String> sourceSystemPaymentTransactionId;
	 
	 /**
	  * 
	  */
	 private String note;
	 
	 /**
	 * sales type: 
	 * D - Direct Sale 
	 * I - Indirect Sale 
	 * S - Web Drop Shipment Sale 
	 * P - Web Store Pickup Sale 
	 * R - With sales receipt (History) 
	 * W - Without sale reciept (No History)
	 */
	private String salesTransactionType;
		
	/**
	  * 
	  */
	 private AddressVO shippingAddress;
	 
	 /**
	  * 
	  */
	 private ResponseVO response;

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}	

	public List<LineItemVO> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItemVO> lineItems) {
		this.lineItems = lineItems;
	}

	public AddressVO getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(AddressVO shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public ResponseVO getResponse() {
		return response;
	}

	public void setResponse(ResponseVO response) {
		this.response = response;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	} 
	public String getSalesTransactionType() {
		return salesTransactionType;
	}

	public void setSalesTransactionType(String salesTransactionType) {
		this.salesTransactionType = salesTransactionType;
	}

	
	/**
	 * @return the customerFirstName
	 */
	public String getCustomerFirstName() {
		return customerFirstName;
	}

	/**
	 * @param customerFirstName the customerFirstName to set
	 */
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	/**
	 * @return the customerLastName
	 */
	public String getCustomerLastName() {
		return customerLastName;
	}

	/**
	 * @param customerLastName the customerLastName to set
	 */
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getBillingAccountNumber() {
		return billingAccountNumber;
	}

	public void setBillingAccountNumber(String billingAccountNumber) {
		this.billingAccountNumber = billingAccountNumber;
	}

	public List<String> getSourceSystemPaymentTransactionId() {
		return sourceSystemPaymentTransactionId;
	}

	public void setSourceSystemPaymentTransactionId(
			List<String> sourceSystemPaymentTransactionId) {
		this.sourceSystemPaymentTransactionId = sourceSystemPaymentTransactionId;
	}	
}
