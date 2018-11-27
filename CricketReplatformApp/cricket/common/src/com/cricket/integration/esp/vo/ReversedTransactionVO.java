/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.math.BigDecimal;

 
public class ReversedTransactionVO {
	
	private  String mOriginalPaymentReferenceId;

    private  String mReversalTransactionType;

    private  BigDecimal mReversalAmount;

    private  String mTenderType;

    private  String mCreditCardType;

    private  PrePaidVO mPrePaid;

    private  DeviceFundingVO mDeviceFunding;
        
    private  BucketDetailsVO mNewBucketDetailsInfo;

    private  String mReasonText;

    private  String[] mNotes;
	/**
	 * 
	 */
	public ReversedTransactionVO() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param pOriginalPaymentReferenceId
	 * @param pReversalTransactionType
	 * @param pReversalAmount
	 * @param pTenderType
	 * @param pCreditCardType
	 * @param pPrePaid
	 * @param pDeviceFunding
	 * @param pNewBucketDetailsInfo
	 * @param pReasonText
	 * @param pNotes
	 */
	public ReversedTransactionVO(String pOriginalPaymentReferenceId,
			String pReversalTransactionType, BigDecimal pReversalAmount,
			String pTenderType, String pCreditCardType, PrePaidVO pPrePaid,
			DeviceFundingVO pDeviceFunding,
			BucketDetailsVO pNewBucketDetailsInfo, String pReasonText,
			String[] pNotes) {
		super();
		mOriginalPaymentReferenceId = pOriginalPaymentReferenceId;
		mReversalTransactionType = pReversalTransactionType;
		mReversalAmount = pReversalAmount;
		mTenderType = pTenderType;
		mCreditCardType = pCreditCardType;
		mPrePaid = pPrePaid;
		mDeviceFunding = pDeviceFunding;
		mNewBucketDetailsInfo = pNewBucketDetailsInfo;
		mReasonText = pReasonText;
		mNotes = pNotes;
	}
	/**
	 * @return the originalPaymentReferenceId
	 */
	public String getOriginalPaymentReferenceId() {
		return mOriginalPaymentReferenceId;
	}
	/**
	 * @param pOriginalPaymentReferenceId the originalPaymentReferenceId to set
	 */
	public void setOriginalPaymentReferenceId(String pOriginalPaymentReferenceId) {
		mOriginalPaymentReferenceId = pOriginalPaymentReferenceId;
	}
	/**
	 * @return the reversalTransactionType
	 */
	public String getReversalTransactionType() {
		return mReversalTransactionType;
	}
	/**
	 * @param pReversalTransactionType the reversalTransactionType to set
	 */
	public void setReversalTransactionType(String pReversalTransactionType) {
		mReversalTransactionType = pReversalTransactionType;
	}
	/**
	 * @return the reversalAmount
	 */
	public BigDecimal getReversalAmount() {
		return mReversalAmount;
	}
	/**
	 * @param pReversalAmount the reversalAmount to set
	 */
	public void setReversalAmount(BigDecimal pReversalAmount) {
		mReversalAmount = pReversalAmount;
	}
	/**
	 * @return the tenderType
	 */
	public String getTenderType() {
		return mTenderType;
	}
	/**
	 * @param pTenderType the tenderType to set
	 */
	public void setTenderType(String pTenderType) {
		mTenderType = pTenderType;
	}
	/**
	 * @return the creditCardType
	 */
	public String getCreditCardType() {
		return mCreditCardType;
	}
	/**
	 * @param pCreditCardType the creditCardType to set
	 */
	public void setCreditCardType(String pCreditCardType) {
		mCreditCardType = pCreditCardType;
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
	 * @return the deviceFunding
	 */
	public DeviceFundingVO getDeviceFunding() {
		return mDeviceFunding;
	}
	/**
	 * @param pDeviceFunding the deviceFunding to set
	 */
	public void setDeviceFunding(DeviceFundingVO pDeviceFunding) {
		mDeviceFunding = pDeviceFunding;
	}
	/**
	 * @return the newBucketDetailsInfo
	 */
	public BucketDetailsVO getNewBucketDetailsInfo() {
		return mNewBucketDetailsInfo;
	}
	/**
	 * @param pNewBucketDetailsInfo the newBucketDetailsInfo to set
	 */
	public void setNewBucketDetailsInfo(BucketDetailsVO pNewBucketDetailsInfo) {
		mNewBucketDetailsInfo = pNewBucketDetailsInfo;
	}
	/**
	 * @return the reasonText
	 */
	public String getReasonText() {
		return mReasonText;
	}
	/**
	 * @param pReasonText the reasonText to set
	 */
	public void setReasonText(String pReasonText) {
		mReasonText = pReasonText;
	}
	/**
	 * @return the notes
	 */
	public String[] getNotes() {
		return mNotes;
	}
	/**
	 * @param pNotes the notes to set
	 */
	public void setNotes(String[] pNotes) {
		mNotes = pNotes;
	}

}
