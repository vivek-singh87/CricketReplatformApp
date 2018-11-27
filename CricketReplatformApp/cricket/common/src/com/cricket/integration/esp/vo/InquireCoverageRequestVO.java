package com.cricket.integration.esp.vo;

import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.AddressInfo;

public class InquireCoverageRequestVO extends ESPRequestVO {

	/**
	 * Reference to the AddressInfo 
	 */
	private AddressInfo mAddress;

	/**
	 * @return the mAddress
	 */
	public AddressInfo getAddress() {
		return mAddress;
	}

	/**
	 * @param pAddress the mAddress to set
	 */
	public void setAddress(AddressInfo pAddress) {
		this.mAddress = pAddress;
	}
	
}
