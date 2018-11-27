package com.cricket.integration.esp.vo;

public class ValidateAddressRequestVO extends ESPRequestVO{
	
	private AddressVO address;
	private String geoCodeType;
	private boolean isShippingAddress = true;
	
	public AddressVO getAddress() {
		return address;
	}
	public void setAddress(AddressVO address) {
		this.address = address;
	}
	public String getGeoCodeType() {
		return geoCodeType;
	}
	public void setGeoCodeType(String geoCodeType) {
		this.geoCodeType = geoCodeType;
	}
	public boolean isShippingAddress() {
		return isShippingAddress;
	}
	public void setShippingAddress(boolean isShippingAddress) {
		this.isShippingAddress = isShippingAddress;
	}
	
}
