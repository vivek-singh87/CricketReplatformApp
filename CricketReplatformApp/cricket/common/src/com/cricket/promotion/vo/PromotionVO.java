package com.cricket.promotion.vo;

import java.util.Map;

public class PromotionVO {
	
	private Map<String,Double> promotions;
	private double amtMIR;
	private double instantDiscount;
	private double webInstantDiscount;
	private double totalDiscount;
	
	
	/**
	 * @return the promotions
	 */
	public Map<String,Double> getPromtions() {
		return promotions;
	}
	/**
	 * @param promtions the promotions to set
	 */
	public void setPromtions(Map<String,Double> promotions) {
		this.promotions = promotions;
	}
	/**
	 * @return the amtMIR
	 */
	public double getAmtMIR() {
		return amtMIR;
	}
	/**
	 * @param amtMIR the amtMIR to set
	 */
	public void setAmtMIR(double amtMIR) {
		this.amtMIR = amtMIR;
	}
	
	public double getInstantDiscount() {
		return instantDiscount;
	}
	public void setInstantDiscount(double pInstantDiscount) {
		instantDiscount = pInstantDiscount;
	}
	public double getWebInstantDiscount() {
		return webInstantDiscount;
	}
	public void setWebInstantDiscount(double pWebInstantDiscount) {
		webInstantDiscount = pWebInstantDiscount;
	}
	/**
	 * @return the totalDiscount
	 */
	public double getTotalDiscount() {
		return totalDiscount;
	}
	/**
	 * @param totalDiscount the totalDiscount to set
	 */
	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}
}
