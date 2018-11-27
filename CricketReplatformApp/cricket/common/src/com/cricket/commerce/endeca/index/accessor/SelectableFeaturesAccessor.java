package com.cricket.commerce.endeca.index.accessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.RepositoryItem;
import atg.repository.search.indexing.Context;
import atg.repository.search.indexing.PropertyAccessorImpl;
import atg.repository.search.indexing.specifier.PropertyTypeEnum;

public class SelectableFeaturesAccessor extends PropertyAccessorImpl {
	
	private List<String> displayableFeatures;
	
	private List<String> negatingValues;
	
	private List<String> valueFeaturesList;
	
	private Map<String, String> featuresMap = new HashMap<String, String>();
	
	protected Object getTextOrMetaPropertyValue(Context pContext, RepositoryItem pItem, String pPropertyName, PropertyTypeEnum pType) {
		
		if(isLoggingDebug()) {
			logDebug("entering SelectableFeaturesAccessor getTextOrMetaPropertyValue");
		}
		
		List<String> calculatedFeaturesList = new ArrayList<String>();
		List<RepositoryItem> phoneSpecifications = (List<RepositoryItem>)pItem.getPropertyValue("phoneSpecifications");
		if(phoneSpecifications != null) {
			for (RepositoryItem specificationGroup : phoneSpecifications) {
				List<RepositoryItem> specifications = (List<RepositoryItem>)specificationGroup.getPropertyValue("specifications");
				if(specifications != null) {
					for (RepositoryItem specification : specifications) {
						String specName = (String)specification.getPropertyValue("specName");
						if(specName != null && !specName.isEmpty()) {
							Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
					        Matcher match= pt.matcher(specName);
					        while(match.find()) {
					        	String s = match.group();
					        	specName = specName.replaceAll("\\"+s, CricketCommonConstants.EMPTY_STRING);
					        }
							specName = specName.trim().toUpperCase();
							boolean isValueFeature = false;
							if(displayableFeatures.contains(specName)) {
								String specValue = (String)specification.getPropertyValue("specValue");
								if(specValue != null && !specValue.isEmpty()) {
									Matcher matchValue = pt.matcher(specValue);
							        while(matchValue.find()) {
							        	String s = matchValue.group();
							        	specValue = specValue.replaceAll("\\"+s, CricketCommonConstants.EMPTY_STRING);
							        }
							        specValue = specValue.toUpperCase();
									for(String valueFeature : valueFeaturesList) {
										if(specValue.contains(valueFeature)) {
											String displayableValueFeature = featuresMap.get(valueFeature);
											calculatedFeaturesList.add(displayableValueFeature);
											if(isLoggingDebug()) {
												logDebug("value feature added :: " + specValue + " :: it qualifies for indexing");
											}
											isValueFeature = true;
										}
									}
								}
								if(!isValueFeature){
									if(negatingValues.contains(specValue)) {
										if(isLoggingDebug()) {
											logDebug("ignoring specName :: " + specName + " :: it doesnt qualify for indexing");
										}
									} else {
										if(isLoggingDebug()) {
											logDebug("adding specName :: " + specName + " :: it qualifies for indexing");
										}
										String displayableFeature = featuresMap.get(specName);
										calculatedFeaturesList.add(displayableFeature);
									}
								}
							}
						}
					}
				}
			}
		}
		if(isLoggingDebug()) {
			logDebug("features list prepared for item :: " + pItem + " is :: " + calculatedFeaturesList);
		}
		if(isLoggingDebug()) {
			logDebug("exiting SelectableFeaturesAccessor getTextOrMetaPropertyValue");
		}
		return calculatedFeaturesList;
	}

	public List<String> getDisplayableFeatures() {
		return displayableFeatures;
	}

	public void setDisplayableFeatures(List<String> displayableFeatures) {
		this.displayableFeatures = displayableFeatures;
	}

	public List<String> getNegatingValues() {
		return negatingValues;
	}

	public void setNegatingValues(List<String> negatingValues) {
		this.negatingValues = negatingValues;
	}

	public Map<String, String> getFeaturesMap() {
		return featuresMap;
	}

	public void setFeaturesMap(Map<String, String> featuresMap) {
		this.featuresMap = featuresMap;
	}

	public List<String> getValueFeaturesList() {
		return valueFeaturesList;
	}

	public void setValueFeaturesList(List<String> valueFeaturesList) {
		this.valueFeaturesList = valueFeaturesList;
	}

}
