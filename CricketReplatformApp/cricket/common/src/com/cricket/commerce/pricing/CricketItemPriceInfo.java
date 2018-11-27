package com.cricket.commerce.pricing;

import atg.projects.store.pricing.StoreItemPriceInfo;

/**
 * @author TechM
 *
 */
public class CricketItemPriceInfo extends StoreItemPriceInfo{

	private static final long serialVersionUID = 1L;
	private double adminFee=0.00;
	private double activationFee=0.00;
	private double proRatedPrice=0.00;
   
	/**
	 * @return the adminFee
	 */
	public double getAdminFee() {
		return adminFee;
	}

	 
	/**
	 * @param adminFee
	 */
	public void setAdminFee(double adminFee) {
		this.adminFee = adminFee;
	}

	/**
	 * @return the activationFee
	 */
	public double getActivationFee() {
		return activationFee;
	}

	/**
	 * @param activationFee the activationFee to set
	 */
	public void setActivationFee(double activationFee) {
		this.activationFee = activationFee;
	}

	/**
	 * @return the proRatedPrice
	 */
	public double getProRatedPrice() {
		return proRatedPrice;
	}

	/**
	 * @param proRatedPrice the proRatedPrice to set
	 */
	public void setProRatedPrice(double proRatedPrice) {
		this.proRatedPrice = proRatedPrice;
	}

	
}