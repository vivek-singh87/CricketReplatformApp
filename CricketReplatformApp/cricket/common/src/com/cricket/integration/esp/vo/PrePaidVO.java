/**
 * 
 */
package com.cricket.integration.esp.vo;

 
public class PrePaidVO {

	private String mPrePaidPin;

	private String mPrePaidPinProductType;

	private String mPrePaidSerialNumber;

	/**
	 * @param pPrePaidPin
	 * @param pPrePaidPinProductType
	 * @param pPrePaidSerialNumber
	 */
	public PrePaidVO(String pPrePaidPin, String pPrePaidPinProductType,
			String pPrePaidSerialNumber) {
		super();
		mPrePaidPin = pPrePaidPin;
		mPrePaidPinProductType = pPrePaidPinProductType;
		mPrePaidSerialNumber = pPrePaidSerialNumber;
	}

	/**
	 * 
	 */
	public PrePaidVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the prePaidPin
	 */
	public String getPrePaidPin() {
		return mPrePaidPin;
	}

	/**
	 * @param pPrePaidPin the prePaidPin to set
	 */
	public void setPrePaidPin(String pPrePaidPin) {
		mPrePaidPin = pPrePaidPin;
	}

	/**
	 * @return the prePaidPinProductType
	 */
	public String getPrePaidPinProductType() {
		return mPrePaidPinProductType;
	}

	/**
	 * @param pPrePaidPinProductType the prePaidPinProductType to set
	 */
	public void setPrePaidPinProductType(String pPrePaidPinProductType) {
		mPrePaidPinProductType = pPrePaidPinProductType;
	}

	/**
	 * @return the prePaidSerialNumber
	 */
	public String getPrePaidSerialNumber() {
		return mPrePaidSerialNumber;
	}

	/**
	 * @param pPrePaidSerialNumber the prePaidSerialNumber to set
	 */
	public void setPrePaidSerialNumber(String pPrePaidSerialNumber) {
		mPrePaidSerialNumber = pPrePaidSerialNumber;
	}
	
	
}
