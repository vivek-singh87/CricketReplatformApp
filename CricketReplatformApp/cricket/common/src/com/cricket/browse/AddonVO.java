package com.cricket.browse;

import java.util.Map;

/**
 * @author hp112045
 *
 */
public class AddonVO extends ProductVO {

	/**
	 * variable to hold final price
	 */
	private double finalPrice;
	
	/**
	 * variable to determine whether addon is included in plan or not
	 */
	private boolean isIncluded;

	/**
	 * variable to hold ancestor categories
	 */
	private Map<String, String> ancestorCategories;

	/**
	 * variable to hold skuId
	 */
	private String skuId;
	
	/**
	 * seoString
	 */
	private String seoString;
	
	/**
	 * @return the skuId
	 */
	public String getSkuId() {
		return skuId;
	}

	/**
	 * @param pSkuId the skuId to set
	 */
	public void setSkuId(String pSkuId) {
		skuId = pSkuId;
	}

	/**
	 * @return finalPrice
	 */
	public double getFinalPrice() {
		return finalPrice;
	}

	/**
	 * @param pFinalPrice
	 */
	public void setFinalPrice(double pFinalPrice) {
		finalPrice = pFinalPrice;
	}

	/**
	 * @return ancestorCategories
	 */
	public Map<String, String> getAncestorCategories() {
		return ancestorCategories;
	}

	/**
	 * @param pAncestorCategories
	 */
	public void setAncestorCategories(Map<String, String> pAncestorCategories) {
		ancestorCategories = pAncestorCategories;
	}

	/**
	 * 
	 * @return the isIncluded
	 */
	public boolean isIncluded() {
		return isIncluded;
	}

	/**
	 * 
	 * @param isIncluded
	 */
	public void setIncluded(boolean isIncluded) {
		this.isIncluded = isIncluded;
	}

	public String getSeoString() {
		return seoString;
	}

	public void setSeoString(String seoString) {
		this.seoString = seoString;
	}
}
