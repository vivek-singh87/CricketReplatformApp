package com.cricket.commerce.order.session;

import java.util.List;
import java.util.Map;

import atg.nucleus.GenericService;

public class UpgradeItemDetailsSessionBean  extends GenericService {
	
	private String removedPhoneId;
	
	private String userIntention;
	
	private String removedPhoneSkuId;
	
	private String removedPlanId;
	
	private String removedPlanSkuId;
	
	private String mdn;
	
	private String modelNumber;
	
	private Map<String,String> removedAddons;
	
	private List<String> addonSkuIds;
	private List<String> addonProductIds;
	private boolean plan4p5;

	/**
	 * @return the removedPhoneId
	 */
	public String getRemovedPhoneId() {
		return removedPhoneId;
	}

	/**
	 * @param pRemovedPhoneId the removedPhoneId to set
	 */
	public void setRemovedPhoneId(String pRemovedPhoneId) {
		removedPhoneId = pRemovedPhoneId;
	}
	
	/**
	 * @return the userIntention
	 */
	public String getUserIntention() {
		return userIntention;
	}

	/**
	 * @param pUserIntention the userIntention to set
	 */
	public void setUserIntention(String pUserIntention) {
		userIntention = pUserIntention;
	}

	/**
	 * @return the removedPhoneSkuId
	 */
	public String getRemovedPhoneSkuId() {
		return removedPhoneSkuId;
	}

	/**
	 * @param pRemovedPhoneSkuId the removedPhoneSkuId to set
	 */
	public void setRemovedPhoneSkuId(String pRemovedPhoneSkuId) {
		removedPhoneSkuId = pRemovedPhoneSkuId;
	}

	/**
	 * @return the removedPlanId
	 */
	public String getRemovedPlanId() {
		return removedPlanId;
	}

	/**
	 * @param pRemovedPlanId the removedPlanId to set
	 */
	public void setRemovedPlanId(String pRemovedPlanId) {
		removedPlanId = pRemovedPlanId;
	}

	/**
	 * @return the removedPlanSkuId
	 */
	public String getRemovedPlanSkuId() {
		return removedPlanSkuId;
	}

	/**
	 * @param pRemovedPlanSkuId the removedPlanSkuId to set
	 */
	public void setRemovedPlanSkuId(String pRemovedPlanSkuId) {
		removedPlanSkuId = pRemovedPlanSkuId;
	}

	/**
	 * @return the removedAddons
	 */
	public Map<String, String> getRemovedAddons() {
		return removedAddons;
	}

	/**
	 * @param pRemovedAddons the removedAddons to set
	 */
	public void setRemovedAddons(Map<String, String> pRemovedAddons) {
		removedAddons = pRemovedAddons;
	}

	/**
	 * @return the mdn
	 */
	public String getMdn() {
		return mdn;
	}

	/**
	 * @param mdn the mdn to set
	 */
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}

	/**
	 * @return the addonSkuIds
	 */
	public List<String> getAddonSkuIds() {
		return addonSkuIds;
	}

	/**
	 * @param addonSkuIds the addonSkuIds to set
	 */
	public void setAddonSkuIds(List<String> addonSkuIds) {
		this.addonSkuIds = addonSkuIds;
	}

	/**
	 * @return the addonProductIds
	 */
	public List<String> getAddonProductIds() {
		return addonProductIds;
	}

	/**
	 * @param addonProductIds the addonProductIds to set
	 */
	public void setAddonProductIds(List<String> addonProductIds) {
		this.addonProductIds = addonProductIds;
	}

	/**
	 * @return the modelNumber
	 */
	public String getModelNumber() {
		return modelNumber;
	}

	/**
	 * @param modelNumber the modelNumber to set
	 */
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	/**
	 * @return the plan4p5
	 */
	public boolean isPlan4p5() {
		return plan4p5;
	}

	/**
	 * @param plan4p5 the plan4p5 to set
	 */
	public void setPlan4p5(boolean plan4p5) {
		this.plan4p5 = plan4p5;
	}
	
	

}
