package com.cricket.checkout.order;

import atg.nucleus.GenericService;

import com.cricket.checkout.utils.CricketBillingAddress;

public class CricketBillingAddressData extends GenericService {
	private CricketBillingAddress billingAddress;

	/**
	 * @return the billingAddress
	 */
	public CricketBillingAddress getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param billingAddress the billingAddress to set
	 */
	public void setBillingAddress(CricketBillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

}
