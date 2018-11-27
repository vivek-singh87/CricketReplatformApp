/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.math.BigDecimal;
 
public class BucketDetailsVO {

	private String mBucketIdentifier;

	private String mBucketName;

	private String mBucketType;

	private BigDecimal mBucketBalance;

	private String mBucketUnits;

	/**
		 * 
		 */
	public BucketDetailsVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pBucketIdentifier
	 * @param pBucketName
	 * @param pBucketType
	 * @param pBucketBalance
	 * @param pBucketUnits
	 */
	public BucketDetailsVO(String pBucketIdentifier, String pBucketName,
			String pBucketType, BigDecimal pBucketBalance, String pBucketUnits) {
		super();
		mBucketIdentifier = pBucketIdentifier;
		mBucketName = pBucketName;
		mBucketType = pBucketType;
		mBucketBalance = pBucketBalance;
		mBucketUnits = pBucketUnits;
	}

	/**
	 * @return the bucketIdentifier
	 */
	public String getBucketIdentifier() {
		return mBucketIdentifier;
	}

	/**
	 * @param pBucketIdentifier
	 *            the bucketIdentifier to set
	 */
	public void setBucketIdentifier(String pBucketIdentifier) {
		mBucketIdentifier = pBucketIdentifier;
	}

	/**
	 * @return the bucketName
	 */
	public String getBucketName() {
		return mBucketName;
	}

	/**
	 * @param pBucketName
	 *            the bucketName to set
	 */
	public void setBucketName(String pBucketName) {
		mBucketName = pBucketName;
	}

	/**
	 * @return the bucketType
	 */
	public String getBucketType() {
		return mBucketType;
	}

	/**
	 * @param pBucketType
	 *            the bucketType to set
	 */
	public void setBucketType(String pBucketType) {
		mBucketType = pBucketType;
	}

	/**
	 * @return the bucketBalance
	 */
	public BigDecimal getBucketBalance() {
		return mBucketBalance;
	}

	/**
	 * @param pBucketBalance
	 *            the bucketBalance to set
	 */
	public void setBucketBalance(BigDecimal pBucketBalance) {
		mBucketBalance = pBucketBalance;
	}

	/**
	 * @return the bucketUnits
	 */
	public String getBucketUnits() {
		return mBucketUnits;
	}

	/**
	 * @param pBucketUnits
	 *            the bucketUnits to set
	 */
	public void setBucketUnits(String pBucketUnits) {
		mBucketUnits = pBucketUnits;
	}

}
