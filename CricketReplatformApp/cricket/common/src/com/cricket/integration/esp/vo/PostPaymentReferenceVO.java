/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.math.BigDecimal;

 
public class PostPaymentReferenceVO {

	 private  String mPaymentReferenceId;

	 private  BigDecimal mPaymentAmount;

	 private ReversedTransactionVO[] mReversedTransactionInfo;
	/**
	 * 
	 */
	public PostPaymentReferenceVO() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param pPaymentReferenceId
	 * @param pPaymentAmount
	 * @param pReversedTransactionInfo
	 */
	public PostPaymentReferenceVO(String pPaymentReferenceId,
			BigDecimal pPaymentAmount,
			ReversedTransactionVO[] pReversedTransactionInfo) {
		super();
		mPaymentReferenceId = pPaymentReferenceId;
		mPaymentAmount = pPaymentAmount;
		mReversedTransactionInfo = pReversedTransactionInfo;
	}
	/**
	 * @return the paymentReferenceId
	 */
	public String getPaymentReferenceId() {
		return mPaymentReferenceId;
	}
	/**
	 * @param pPaymentReferenceId the paymentReferenceId to set
	 */
	public void setPaymentReferenceId(String pPaymentReferenceId) {
		mPaymentReferenceId = pPaymentReferenceId;
	}
	/**
	 * @return the paymentAmount
	 */
	public BigDecimal getPaymentAmount() {
		return mPaymentAmount;
	}
	/**
	 * @param pPaymentAmount the paymentAmount to set
	 */
	public void setPaymentAmount(BigDecimal pPaymentAmount) {
		mPaymentAmount = pPaymentAmount;
	}
	/**
	 * @return the reversedTransactionInfo
	 */
	public ReversedTransactionVO[] getReversedTransactionInfo() {
		return mReversedTransactionInfo;
	}
	/**
	 * @param pReversedTransactionInfo the reversedTransactionInfo to set
	 */
	public void setReversedTransactionInfo(
			ReversedTransactionVO[] pReversedTransactionInfo) {
		mReversedTransactionInfo = pReversedTransactionInfo;
	}

}
