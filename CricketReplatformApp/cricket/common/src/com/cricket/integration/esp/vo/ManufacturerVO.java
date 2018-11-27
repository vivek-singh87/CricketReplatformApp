package com.cricket.integration.esp.vo;

public class ManufacturerVO {
	/**
	 * Example: Motorola
	 */
	private String make;
	
	/**
	 * Example: 2267
	 */
	private String model;
	
	/**
	 * Example: MOTO 2267
	 */
	private String phoneCode;
	
	/**
	 * Example: Voice, Data
	 */
	private String phoneType;

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	
}
