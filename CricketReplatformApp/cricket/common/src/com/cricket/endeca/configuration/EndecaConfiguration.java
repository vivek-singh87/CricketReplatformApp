package com.cricket.endeca.configuration;

import java.util.HashMap;
import java.util.Map;

import atg.nucleus.GenericService;

public class EndecaConfiguration extends GenericService {
	private Map<String, String> dimensionLabels = new HashMap<String, String>();

	public Map<String, String> getDimensionLabels() {
		return dimensionLabels;
	}

	public void setDimensionLabels(Map<String, String> dimensionLabels) {
		this.dimensionLabels = dimensionLabels;
	}
	
}