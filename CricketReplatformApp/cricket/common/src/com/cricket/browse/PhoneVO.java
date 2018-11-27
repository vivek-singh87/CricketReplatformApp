package com.cricket.browse;

import java.util.List;
import java.util.Map;

public class PhoneVO extends ProductVO {
	
	/**
	 * manufacturer
	 */
	private String manufacturer;
	
	/**
	 * seoString
	 */
	private String seoString;
	
	/**
	 * 
	 */
	private boolean OOFPhone;
	
	/**
	 * 
	 */
	private String availForCustUppgrades;
	
	/**
	 * 
	 * @return the availForCustUppgrades
	 */
	public String getAvailForCustUppgrades() {
		return availForCustUppgrades;
	}

	/**
	 * 
	 * @param pAvailForCustUppgrades
	 */
	public void setAvailForCustUppgrades(String pAvailForCustUppgrades) {
		availForCustUppgrades = pAvailForCustUppgrades;
	}

	/**
	 * 
	 * @return the OOFPhone
	 */
	public boolean isOOFPhone() {
		return OOFPhone;
	}

	/**
	 * 
	 * @param oOFPhone
	 */
	public void setOOFPhone(boolean oOFPhone) {
		OOFPhone = oOFPhone;
	}

	/**
	 * modelNumber
	 */
	private String modelNumber;
	
	/**
	 * phoneOverViewVO
	 */
	private PhoneOverViewVO phoneOverViewVO;
	
	/**
	 * retailPrice
	 */
	private double retailPrice;
	
	/**
	 *discounts 
	 */
	private List<String> discounts;
	
	/**
	 * finalPrice
	 */
	private double finalPrice;
	
	/**
	 * shippingInfo
	 */
	private String shippingInfo;
	
	/**
	 * shippingInfo
	 */
	private boolean isFinancingAvailable;
	
	/**
	 * phoneAccessories
	 */
	private List<AccessoryVO> phoneAccessories;
	
	/**
	 * similarPhones
	 */
	private List<PhoneVO> similarPhones;
	
	/**
	 * plansWithPhone
	 */
	private List<PlanVO> plansWithPhone;
	
	/**
	 * specifications
	 */
	private List<SpecificationInfoVO> specifications;
	
	/**
	 * helpAndResourceVO
	 */
	private List<HelpAndResourceVO> helpAndResourceVO;
	
	/**
	 * phoneSkuPrice
	 */
	private Map<String,Double> phoneSkuPrice;
	
	/**
	 * discountDisplay
	 */
	private String discountType;
	/**
	 * discountDisplay
	 */
	private double discountAmt;
	

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public PhoneOverViewVO getPhoneOverViewVO() {
		return phoneOverViewVO;
	}

	public void setPhoneOverViewVO(PhoneOverViewVO phoneOverViewVO) {
		this.phoneOverViewVO = phoneOverViewVO;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public List<String> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<String> discounts) {
		this.discounts = discounts;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getShippingInfo() {
		return shippingInfo;
	}

	public void setShippingInfo(String shippingInfo) {
		this.shippingInfo = shippingInfo;
	}

	public List<AccessoryVO> getPhoneAccessories() {
		return phoneAccessories;
	}

	public void setPhoneAccessories(List<AccessoryVO> phoneAccessories) {
		this.phoneAccessories = phoneAccessories;
	}

	public List<PhoneVO> getSimilarPhones() {
		return similarPhones;
	}

	public void setSimilarPhones(List<PhoneVO> similarPhones) {
		this.similarPhones = similarPhones;
	}

	public List<PlanVO> getPlansWithPhone() {
		return plansWithPhone;
	}

	public void setPlansWithPhone(List<PlanVO> plansWithPhone) {
		this.plansWithPhone = plansWithPhone;
	}

	public List<SpecificationInfoVO> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(List<SpecificationInfoVO> specifications) {
		this.specifications = specifications;
	}

	public List<HelpAndResourceVO> getHelpAndResourceVO() {
		return helpAndResourceVO;
	}

	public void setHelpAndResourceVO(List<HelpAndResourceVO> helpAndResourceVO) {
		this.helpAndResourceVO = helpAndResourceVO;
	}
	
	/**
	 * @return the phoneSkuPrice
	 */
	public Map<String, Double> getPhoneSkuPrice() {
		return phoneSkuPrice;
	}

	/**
	 * @param phoneSkuPrice the phoneSkuPrice to set
	 */
	public void setPhoneSkuPrice(Map<String, Double> phoneSkuPrice) {
		this.phoneSkuPrice = phoneSkuPrice;
	}	
	

	public boolean isFinancingAvailable() {
		return isFinancingAvailable;
	}

	public void setFinancingAvailable(boolean isFinancingAvailable) {
		this.isFinancingAvailable = isFinancingAvailable;
	}
	
	/**
	 * @return the discountType
	 */
	public String getDiscountType() {
		return discountType;
	}

	/**
	 * @param discountType the discountType to set
	 */
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	/**
	 * @return the discountAmt
	 */
	public double getDiscountAmt() {
		return discountAmt;
	}

	/**
	 * @param discountAmt the discountAmt to set
	 */
	public void setDiscountAmt(double discountAmt) {
		this.discountAmt = discountAmt;
	}

	@Override
	public String toString() {
		return "PhoneVO [manufacturer=" + manufacturer + ", modelNumber="
				+ modelNumber + ", phoneOverViewVO=" + phoneOverViewVO
				+ ", retailPrice=" + retailPrice + ", discountType="
				+ discountType + ", discountAmt=" + discountAmt
				+ ", finalPrice=" + finalPrice + ", shiipingInfo="
				+ shippingInfo + ", phoneAccessories=" + phoneAccessories
				+ ", similarPhones=" + similarPhones + ", plansWithPhone="
				+ plansWithPhone + ", specifications=" + specifications
				+ ", helpAndResourceVO=" + helpAndResourceVO + "]";
	}

	public String getSeoString() {
		return seoString;
	}

	public void setSeoString(String seoString) {
		this.seoString = seoString;
	}
}
