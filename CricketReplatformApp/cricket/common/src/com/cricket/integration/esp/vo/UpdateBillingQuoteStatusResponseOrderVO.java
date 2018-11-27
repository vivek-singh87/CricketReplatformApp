/**
 * 
 */
package com.cricket.integration.esp.vo;

 
public class UpdateBillingQuoteStatusResponseOrderVO {
	
	private java.lang.String mOrderId;

    private UpdateBillingQuoteStatusResponseOrderInfoPaymentResultVO[] mPaymentResult;

	/**
	 * 
	 */
	public UpdateBillingQuoteStatusResponseOrderVO() {
		 
	}

	/**
	 * @param pOrderId
	 * @param pPaymentResult
	 */
	public UpdateBillingQuoteStatusResponseOrderVO(
			String pOrderId,
			UpdateBillingQuoteStatusResponseOrderInfoPaymentResultVO[] pPaymentResult) {
		super();
		mOrderId = pOrderId;
		mPaymentResult = pPaymentResult;
	}

	/**
	 * @return the orderId
	 */
	public java.lang.String getOrderId() {
		return mOrderId;
	}

	/**
	 * @param pOrderId the orderId to set
	 */
	public void setOrderId(java.lang.String pOrderId) {
		mOrderId = pOrderId;
	}

	/**
	 * @return the paymentResult
	 */
	public UpdateBillingQuoteStatusResponseOrderInfoPaymentResultVO[] getPaymentResult() {
		return mPaymentResult;
	}

	/**
	 * @param pPaymentResult the paymentResult to set
	 */
	public void setPaymentResult(
			UpdateBillingQuoteStatusResponseOrderInfoPaymentResultVO[] pPaymentResult) {
		mPaymentResult = pPaymentResult;
	}

 

}
