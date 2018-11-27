/**
 * 
 */
package com.cricket.integration.esp.vo;

 
public class UpdateBillingQuoteStatusResponseVO {

	 private UpdateBillingQuoteStatusResponseOrderVO mOrderInfo;
	 private String paymentApprovalTransactionId;

	 private ESPResponseVO[] mResponse;
	/**
	 * 
	 */
	public UpdateBillingQuoteStatusResponseVO() {
		 
	}

	/**
	 * @param pOrderInfo
	 */
	public UpdateBillingQuoteStatusResponseVO(
			UpdateBillingQuoteStatusResponseOrderVO pOrderInfo) {
		 
		setOrderInfo(pOrderInfo);
	}
	public ESPResponseVO getResponse(int i)
    {
        return mResponse[i];
    }

    public void setResponse(int i, ESPResponseVO value)
    {
    	mResponse[i] = value;
    }
	/**
	 * @return the orderInfo
	 */
	public UpdateBillingQuoteStatusResponseOrderVO getOrderInfo() {
		return mOrderInfo;
	}

	/**
	 * @param pOrderInfo the orderInfo to set
	 */
	public void setOrderInfo(UpdateBillingQuoteStatusResponseOrderVO pOrderInfo) {
		mOrderInfo = pOrderInfo;
	}

	/**
	 * @return the response
	 */
	public ESPResponseVO[] getResponse() {
		return mResponse;
	}

	/**
	 * @param pResponse the response to set
	 */
	public void setResponse(ESPResponseVO[] pResponse) {
		mResponse = pResponse;
	}

	/**
	 * @return the paymentApprovalTransactionId
	 */
	public String getPaymentApprovalTransactionId() {
		return paymentApprovalTransactionId;
	}

	/**
	 * @param paymentApprovalTransactionId the paymentApprovalTransactionId to set
	 */
	public void setPaymentApprovalTransactionId(String paymentApprovalTransactionId) {
		this.paymentApprovalTransactionId = paymentApprovalTransactionId;
	}

 
	 
}
