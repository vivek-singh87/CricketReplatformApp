package com.cricket.integration.esp.vo;

import java.util.List;

public class ValidateAddressResponseVO extends ESPResponseVO{
	
	private List<AddressVO> normalizedAddresses;
	private double latitude;
	private double longitude;
	private String geoCodeType;
	private String geoCodeValue;
    private ResponseVO response;
	
	
	/**
	 * @return the normalizedAddresses
	 */
	public List<AddressVO> getNormalizedAddresses() {
		return normalizedAddresses;
	}
	/**
	 * @param normalizedAddresses the normalizedAddresses to set
	 */
	public void setNormalizedAddresses(List<AddressVO> normalizedAddresses) {
		this.normalizedAddresses = normalizedAddresses;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getGeoCodeType() {
		return geoCodeType;
	}
	public void setGeoCodeType(String geoCodeType) {
		this.geoCodeType = geoCodeType;
	}
	public String getGeoCodeValue() {
		return geoCodeValue;
	}
	public void setGeoCodeValue(String geoCodeValue) {
		this.geoCodeValue = geoCodeValue;
	}
	public ResponseVO getResponse() {
		return response;
	}
	public void setResponse(ResponseVO response) {
		this.response = response;
	}
	
}
