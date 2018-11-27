package com.cricket.integration.esp.vo;

import java.math.BigInteger;

import com.cricket.commerce.order.payment.CricketCreditCard;

public class ManagePaymentResponseVO extends ESPResponseVO {
	private String transactionId;
	private BigInteger orderId;
	private BigInteger vestaResponse_responseCode;
	private String vestaResponse_responseText;
	private ResponseVO response;
	private CricketCreditCard paymentGroup;
	
	/**
	 * @return the paymentGroup
	 */
	public CricketCreditCard getPaymentGroup() {
		return paymentGroup;
	}
	/**
	 * @param paymentGroup the paymentGroup to set
	 */
	public void setPaymentGroup(CricketCreditCard paymentGroup) {
		this.paymentGroup = paymentGroup;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public BigInteger getOrderId() {
		return orderId;
	}
	public void setOrderId(BigInteger orderId) {
		this.orderId = orderId;
	}
	public BigInteger getVestaResponse_responseCode() {
		return vestaResponse_responseCode;
	}
	public void setVestaResponse_responseCode(BigInteger vestaResponse_responseCode) {
		this.vestaResponse_responseCode = vestaResponse_responseCode;
	}
	public String getVestaResponse_responseText() {
		return vestaResponse_responseText;
	}
	public void setVestaResponse_responseText(String vestaResponse_responseText) {
		this.vestaResponse_responseText = vestaResponse_responseText;
	}
	public ResponseVO getResponse() {
		return response;
	}
	public void setResponse(ResponseVO response) {
		this.response = response;
	}
	
}
