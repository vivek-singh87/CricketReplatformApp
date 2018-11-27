package com.cricket.browse;

public class PlanSpecsVO {
	
	private String specName;
	
	private String specValue;
	
	private boolean specBoolValue;
	
	/**
	 * spec type can be BOOL or TEXT or INFOICON.
	 */
	private String specType;

	/**
	 * @return
	 */
	public String getSpecName() {
		return specName;
	}

	/**
	 * @param specName
	 */
	public void setSpecName(String specName) {
		this.specName = specName;
	}

	/**
	 * @return
	 */
	public String getSpecValue() {
		return specValue;
	}

	/**
	 * @param specValue
	 */
	public void setSpecValue(String specValue) {
		this.specValue = specValue;
	}

	/**
	 * @return
	 */
	public String getSpecType() {
		return specType;
	}

	/**
	 * @param specType
	 */
	public void setSpecType(String specType) {
		this.specType = specType;
	}

	public boolean isSpecBoolValue() {
		return specBoolValue;
	}

	public void setSpecBoolValue(boolean specBoolValue) {
		this.specBoolValue = specBoolValue;
	}
	
	
}