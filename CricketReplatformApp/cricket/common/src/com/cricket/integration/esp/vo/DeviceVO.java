/**
 * 
 */
package com.cricket.integration.esp.vo;


/**
 * @author Cricket
 *
 */
public class DeviceVO {
	
	
	private ManufacturerVO manufacturerVO;
	private String esn;
	private String meid;
	private String imei;
	/**
	 * @param pManufacturerVO
	 */
	public DeviceVO(ManufacturerVO pManufacturerVO) {
		super();
		manufacturerVO = pManufacturerVO;
	}

	/**
	 * 
	 */
	public DeviceVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the manufacturerVO
	 */
	public ManufacturerVO getManufacturerVO() {
		return manufacturerVO;
	}

	/**
	 * @param pManufacturerVO the manufacturerVO to set
	 */
	public void setManufacturerVO(ManufacturerVO pManufacturerVO) {
		manufacturerVO = pManufacturerVO;
	}

	public String getEsn() {
		return esn;
	}

	public void setEsn(String esn) {
		this.esn = esn;
	}

	public String getMeid() {
		return meid;
	}

	public void setMeid(String meid) {
		this.meid = meid;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

}
