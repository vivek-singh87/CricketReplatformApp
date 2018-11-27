/**
 * 
 */
package com.cricket.integration.esp.vo;
 
public class UpdateBillingQuoteStatusResponseOrderInfoPaymentResultVO extends ESPResponseVO {
	
	 
		private ESPResponseVO mResponse;		
	    
		private  String mPaymentApprovalTransactionId;

	    private  PrePaidVO mPrePaid;

	    private PostPaymentReferenceVO mPaymentReference;
	    

	    /**
		 * @param pResponse
		 * @param pPaymentApprovalTransactionId
		 * @param pPrePaid
		 * @param pPaymentReference
		 */
		public UpdateBillingQuoteStatusResponseOrderInfoPaymentResultVO(
				ESPResponseVO pResponse, String pPaymentApprovalTransactionId,
				PrePaidVO pPrePaid, PostPaymentReferenceVO pPaymentReference) {
			super();
			mResponse = pResponse;
			mPaymentApprovalTransactionId = pPaymentApprovalTransactionId;
			mPrePaid = pPrePaid;
			mPaymentReference = pPaymentReference;
		}

		/**
		 * 
		 */
		public UpdateBillingQuoteStatusResponseOrderInfoPaymentResultVO() {
			super();
			// TODO Auto-generated constructor stub
		}

		/**
		 * @return the paymentApprovalTransactionId
		 */
		public java.lang.String getPaymentApprovalTransactionId() {
			return mPaymentApprovalTransactionId;
		}

		/**
		 * @param pPaymentApprovalTransactionId the paymentApprovalTransactionId to set
		 */
		public void setPaymentApprovalTransactionId(
				java.lang.String pPaymentApprovalTransactionId) {
			mPaymentApprovalTransactionId = pPaymentApprovalTransactionId;
		}

		/**
		 * @return the prePaid
		 */
		public PrePaidVO getPrePaid() {
			return mPrePaid;
		}

		/**
		 * @param pPrePaid the prePaid to set
		 */
		public void setPrePaid(PrePaidVO pPrePaid) {
			mPrePaid = pPrePaid;
		}

		/**
		 * @return the paymentReference
		 */
		public PostPaymentReferenceVO getPaymentReference() {
			return mPaymentReference;
		}

		/**
		 * @param pPaymentReference the paymentReference to set
		 */
		public void setPaymentReference(PostPaymentReferenceVO pPaymentReference) {
			mPaymentReference = pPaymentReference;
		}

		/**
		 * @return the response
		 */
		public ESPResponseVO getResponse() {
			return mResponse;
		}

		/**
		 * @param pResponse the response to set
		 */
		public void setResponse(ESPResponseVO pResponse) {
			mResponse = pResponse;
		}
	    
	    
	 

}
