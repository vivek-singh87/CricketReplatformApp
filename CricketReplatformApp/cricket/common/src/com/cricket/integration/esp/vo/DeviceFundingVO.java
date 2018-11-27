/**
 * 
 */
package com.cricket.integration.esp.vo;

 
public class DeviceFundingVO {

	private String mDeviceFundingType;

	private String mDeviceFundingId;

	/**
	 * 
	 */
	public DeviceFundingVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pDeviceFundingType
	 * @param pDeviceFundingId
	 */
	public DeviceFundingVO(String pDeviceFundingType, String pDeviceFundingId) {
		super();
		mDeviceFundingType = pDeviceFundingType;
		mDeviceFundingId = pDeviceFundingId;
	}

	/**
	 * @return the deviceFundingType
	 */
	public String getDeviceFundingType() {
		return mDeviceFundingType;
	}

	/**
	 * @param pDeviceFundingType
	 *            the deviceFundingType to set
	 */
	public void setDeviceFundingType(String pDeviceFundingType) {
		mDeviceFundingType = pDeviceFundingType;
	}

	/**
	 * @return the deviceFundingId
	 */
	public String getDeviceFundingId() {
		return mDeviceFundingId;
	}

	/**
	 * @param pDeviceFundingId
	 *            the deviceFundingId to set
	 */
	public void setDeviceFundingId(String pDeviceFundingId) {
		mDeviceFundingId = pDeviceFundingId;
	}

}
