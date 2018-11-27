package com.cricket.browse;

import java.util.List;

public class SpecificationInfoVO {
	
	private String specificationGroupName;
	
	/**
	 * @return the specificationGroupName
	 */
	public String getSpecificationGroupName() {
		return specificationGroupName;
	}

	/**
	 * @param pSpecificationGroupName the specificationGroupName to set
	 */
	public void setSpecificationGroupName(String pSpecificationGroupName) {
		specificationGroupName = pSpecificationGroupName;
	}

	private List<SpecificationVO> specifications;

	/**
	 * @return the specifications
	 */
	public List<SpecificationVO> getSpecifications() {
		return specifications;
	}

	/**
	 * @param pSpecifications the specifications to set
	 */
	public void setSpecifications(List<SpecificationVO> pSpecifications) {
		specifications = pSpecifications;
	}
	
	
	
}
