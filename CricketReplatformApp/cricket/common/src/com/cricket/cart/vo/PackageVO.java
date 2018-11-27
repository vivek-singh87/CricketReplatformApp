package com.cricket.cart.vo;

import java.util.List;

import atg.commerce.pricing.ItemPriceInfo;
import atg.commerce.pricing.PricingAdjustment;
import atg.repository.RepositoryItem;

public class PackageVO {
	
	RepositoryItem mPhoneCommerceItem;
	RepositoryItem mPlanCommerceItem;
	List<RepositoryItem> mAddOnsCommerceItems;
	ItemPriceInfo phonePriceInfo;
	ItemPriceInfo planPriceInfo;
	List<ItemPriceInfo> addOnPriceInfo;
	String packageId;
	double activationFee;
	List<PricingAdjustment>	phoneDiscountAdjustments;
	List<PricingAdjustment>	planDiscountAdjustments;
	String packageTotal;
	
	
	
	
	/**
	 * @return the packageTotal
	 */
	public String getPackageTotal() {
		return packageTotal;
	}
	/**
	 * @param pPackageTotal the packageTotal to set
	 */
	public void setPackageTotal(final String pPackageTotal) {
		packageTotal = pPackageTotal;
	}
	/**
	 * @return the phoneCommerceItem
	 */
	public RepositoryItem getPhoneCommerceItem() {
		return mPhoneCommerceItem;
	}
	/**
	 * @param pPhoneCommerceItem the phoneCommerceItem to set
	 */
	public void setPhoneCommerceItem(final RepositoryItem pPhoneCommerceItem) {
		mPhoneCommerceItem = pPhoneCommerceItem;
	}
	/**
	 * @return the planCommerceItem
	 */
	public RepositoryItem getPlanCommerceItem() {
		return mPlanCommerceItem;
	}
	/**
	 * @param pPlanCommerceItem the planCommerceItem to set
	 */
	public void setPlanCommerceItem(final RepositoryItem pPlanCommerceItem) {
		mPlanCommerceItem = pPlanCommerceItem;
	}
	/**
	 * @return the addOnsCommerceItems
	 */
	public List<RepositoryItem> getAddOnsCommerceItems() {
		return mAddOnsCommerceItems;
	}
	/**
	 * @param pAddOnsCommerceItems the addOnsCommerceItems to set
	 */
	public void setAddOnsCommerceItems(final List<RepositoryItem> pAddOnsCommerceItems) {
		mAddOnsCommerceItems = pAddOnsCommerceItems;
	}
	/**
	 * @return the phonePriceInfo
	 */
	public ItemPriceInfo getPhonePriceInfo() {
		return phonePriceInfo;
	}
	/**
	 * @param phonePriceInfo the phonePriceInfo to set
	 */
	public void setPhonePriceInfo(final ItemPriceInfo phonePriceInfo) {
		this.phonePriceInfo = phonePriceInfo;
	}
	/**
	 * @return the planPriceInfo
	 */
	public ItemPriceInfo getPlanPriceInfo() {
		return planPriceInfo;
	}
	/**
	 * @param planPriceInfo the planPriceInfo to set
	 */
	public void setPlanPriceInfo(final ItemPriceInfo planPriceInfo) {
		this.planPriceInfo = planPriceInfo;
	}
	/**
	 * @return the addOnPriceInfo
	 */
	public List<ItemPriceInfo> getAddOnPriceInfo() {
		return addOnPriceInfo;
	}
	/**
	 * @param addOnPriceInfo the addOnPriceInfo to set
	 */
	public void setAddOnPriceInfo(final List<ItemPriceInfo> addOnPriceInfo) {
		this.addOnPriceInfo = addOnPriceInfo;
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
	public void setActivationFee(final double activationFee) {
		this.activationFee = activationFee;
	}
	/**
	 * @return the packageId
	 */
	public String getPackageId() {
		return packageId;
	}
	/**
	 * @param packageId the packageId to set
	 */
	public void setPackageId(final String packageId) {
		this.packageId = packageId;
	}
	/**
	 * @return the phoneDiscountAdjustments
	 */
	public List<PricingAdjustment> getPhoneDiscountAdjustments() {
		return phoneDiscountAdjustments;
	}
	/**
	 * @param phoneDiscountAdjustments the phoneDiscountAdjustments to set
	 */
	public void setPhoneDiscountAdjustments(
			final List<PricingAdjustment> phoneDiscountAdjustments) {
		this.phoneDiscountAdjustments = phoneDiscountAdjustments;
	}
	/**
	 * @return the planDiscountAdjustments
	 */
	public List<PricingAdjustment> getPlanDiscountAdjustments() {
		return planDiscountAdjustments;
	}
	/**
	 * @param planDiscountAdjustments the planDiscountAdjustments to set
	 */
	public void setPlanDiscountAdjustments(
			List<PricingAdjustment> planDiscountAdjustments) {
		this.planDiscountAdjustments = planDiscountAdjustments;
	}
}
