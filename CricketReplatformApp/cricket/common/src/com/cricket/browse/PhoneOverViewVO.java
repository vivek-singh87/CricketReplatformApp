package com.cricket.browse;

import java.util.List;
import java.util.Map;

public class PhoneOverViewVO {
	
	private Map<String,String> overviewInfo;
	
	private List<FeatureVO> features;
	
	private List<AccessoryVO> includedAccessories;

	public Map<String, String> getOverviewInfo() {
		return overviewInfo;
	}

	public void setOverviewInfo(Map<String, String> overviewInfo) {
		this.overviewInfo = overviewInfo;
	}

	public List<FeatureVO> getFeatures() {
		return features;
	}

	public void setFeatures(List<FeatureVO> features) {
		this.features = features;
	}

	public List<AccessoryVO> getIncludedAccessories() {
		return includedAccessories;
	}

	public void setIncludedAccessories(List<AccessoryVO> includedAccessories) {
		this.includedAccessories = includedAccessories;
	}
	
	
}
