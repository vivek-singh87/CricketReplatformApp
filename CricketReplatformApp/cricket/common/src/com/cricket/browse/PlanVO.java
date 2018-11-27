package com.cricket.browse;

import java.util.List;
import java.util.Map;

/**
 * @author RM112139
 *
 */
public class PlanVO extends ProductVO{
	
	/**
	 * finalPrice
	 */
	private double finalPrice;
	
	/**
	 * features
	 */
	private List<String> features;
	
	private Map<String, PlanSpecsVO> planSpecs;
	
	private String planType;
	
	private String ratePlanType;	
	
	private String dataMessage;
	
	private String dataLimit;
	
	private String groupCode;
	
	private String greenBoxContent;
	

	/**
	 * 
	 * @return the greenBoxContent
	 */
	public String getGreenBoxContent() {
		return greenBoxContent;
	}

	/**
	 * 
	 * @param pGreenBoxContent
	 */
	public void setGreenBoxContent(String pGreenBoxContent) {
		greenBoxContent = pGreenBoxContent;
	}
	
	/**
	 * 
	 * @return the groupCode
	 */
	public String getGroupCode() {
		return groupCode;
	}
	
	/**
	 * 
	 * @param groupCode
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	public double getFinalPrice() {
		return finalPrice;
	}
	/**
	 * @param finalPrice
	 */
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}
	/**
	 * @return features
	 */
	public List<String> getFeatures() {
		return features;
	}
	/**
	 * @param features
	 */
	public void setFeatures(List<String> features) {
		this.features = features;
	}
	public Map<String, PlanSpecsVO> getPlanSpecs() {
		return planSpecs;
	}
	public void setPlanSpecs(Map<String, PlanSpecsVO> planSpecs) {
		this.planSpecs = planSpecs;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public String getRatePlanType() {
		return ratePlanType;
	}
	public void setRatePlanType(String ratePlanType) {
		this.ratePlanType = ratePlanType;
	}
	public String getDataMessage() {
		return dataMessage;
	}
	public void setDataMessage(String dataMessage) {
		this.dataMessage = dataMessage;
	}
	public String getDataLimit() {
		return dataLimit;
	}
	public void setDataLimit(String dataLimit) {
		this.dataLimit = dataLimit;
	}
}
