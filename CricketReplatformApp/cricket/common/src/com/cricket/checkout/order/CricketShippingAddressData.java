package com.cricket.checkout.order;

import java.util.HashMap;
import java.util.List;

import atg.nucleus.GenericService;

import com.cricket.checkout.utils.CricketShippingAddress;
import com.cricket.integration.esp.vo.AddressVO;



public class CricketShippingAddressData extends GenericService {
	
	private CricketShippingAddress shippingAddress;
	private boolean shippingAddressAsAccountHolderAddress;
	private boolean billingAddressAsAccountHolderAddress;
	private boolean billingAddressAsShippingAddress;
	private List<AddressVO> normalizedAddresses;
	private String shippingMethodValue;
	private HashMap<Integer,AddressVO> normalizedAddressMap;

	/**
	 * @return the shippingAddress
	 */
	public CricketShippingAddress getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * @param shippingAddress the shippingAddress to set
	 */
	public void setShippingAddress(CricketShippingAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public boolean isShippingAddressAsAccountHolderAddress() {
		return shippingAddressAsAccountHolderAddress;
	}

	public void setShippingAddressAsAccountHolderAddress(
			boolean shippingAddressAsAccountHolderAddress) {
		this.shippingAddressAsAccountHolderAddress = shippingAddressAsAccountHolderAddress;
	}

	public boolean isBillingAddressAsAccountHolderAddress() {
		return billingAddressAsAccountHolderAddress;
	}

	public void setBillingAddressAsAccountHolderAddress(
			boolean billingAddressAsAccountHolderAddress) {
		this.billingAddressAsAccountHolderAddress = billingAddressAsAccountHolderAddress;
	}

	public boolean isBillingAddressAsShippingAddress() {
		return billingAddressAsShippingAddress;
	}

	public void setBillingAddressAsShippingAddress(
			boolean billingAddressAsShippingAddress) {
		this.billingAddressAsShippingAddress = billingAddressAsShippingAddress;
	}

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

	/**
	 * @return the shippingMethodValue
	 */
	public String getShippingMethodValue() {
		return shippingMethodValue;
	}

	/**
	 * @param shippingMethodValue the shippingMethodValue to set
	 */
	public void setShippingMethodValue(String shippingMethodValue) {
		this.shippingMethodValue = shippingMethodValue;
	}

	/**
	 * @return the normalizedAddressMap
	 */
	public HashMap<Integer, AddressVO> getNormalizedAddressMap() {
		return normalizedAddressMap;
	}

	/**
	 * @param normalizedAddressMap the normalizedAddressMap to set
	 */
	public void setNormalizedAddressMap(
			HashMap<Integer, AddressVO> normalizedAddressMap) {
		this.normalizedAddressMap = normalizedAddressMap;
	}

}
