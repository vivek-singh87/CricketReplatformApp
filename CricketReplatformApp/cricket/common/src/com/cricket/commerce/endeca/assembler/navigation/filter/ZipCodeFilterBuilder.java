package com.cricket.commerce.endeca.assembler.navigation.filter;

import com.cricket.common.constants.CricketCommonConstants;

import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;
import atg.endeca.assembler.navigation.filter.FilterUtils;
import atg.endeca.assembler.navigation.filter.PropertyConstraint;
import atg.endeca.assembler.navigation.filter.RecordFilterBuilderImpl;

public class ZipCodeFilterBuilder extends RecordFilterBuilderImpl {
	
	private ProfileServices profileService;
	private String locationPropertyName = "marketId";
	private String locationRecordPropName = "market.code";
	private String productTypePropertyName = "product.type";
	private String oofPropertyName = "product.isOOFPhone";
	private String planTypePropertyName = "plan.type";
	private String payGoPlanType = "PAYGO";


public String buildRecordFilter() {
	Profile profile = profileService.getCurrentProfile();
	String location = (String) profile.getPropertyValue(locationPropertyName);
	String marketType = (String) profile.getPropertyValue(CricketCommonConstants.MARKET_TYPE);
	if (isLoggingDebug()) {
		logDebug("current profile location: " + location);
		logDebug("current profile marketType: " + marketType);
	}
	
	if (location == null || (marketType!=null && marketType.equals(CricketCommonConstants.OOF_CUSTOMER))) {
		PropertyConstraint[] propertyConstraints = new PropertyConstraint[4];
		propertyConstraints[0] = FilterUtils.constraint(getOofPropertyName(), "1");
		propertyConstraints[1] = FilterUtils.constraint(getPlanTypePropertyName(), getPayGoPlanType());
	    propertyConstraints[2] = FilterUtils.constraint(getProductTypePropertyName(), CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT);
	    propertyConstraints[3] = FilterUtils.constraint(getProductTypePropertyName(), CricketCommonConstants.ACCESSORY_PRODUCT);
	    String filter = FilterUtils.or(propertyConstraints);
	    if (isLoggingDebug()) {
	      logDebug("Catalog filter: " + filter);
	    }
	    return filter;
	} else if (location != null) {
		PropertyConstraint[] propertyConstraints = new PropertyConstraint[3];
	    propertyConstraints[0] = FilterUtils.constraint(getLocationRecordPropName(), location);
	    propertyConstraints[1] = FilterUtils.constraint(getProductTypePropertyName(), CricketCommonConstants.ACCESSORY_PRODUCT);
	    propertyConstraints[2] = FilterUtils.constraint(getProductTypePropertyName(), CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT);
	    String filter = FilterUtils.or(propertyConstraints);
	    if (isLoggingDebug()) {
	      logDebug("Catalog filter: " + filter);
	    }
	    return filter;
	} else {
		return CricketCommonConstants.EMPTY_STRING;
	}
  }


public ProfileServices getProfileService() {
	return profileService;
}


public void setProfileService(ProfileServices profileService) {
	this.profileService = profileService;
}


public String getLocationRecordPropName() {
	return locationRecordPropName;
}


public void setLocationRecordPropName(String locationRecordPropName) {
	this.locationRecordPropName = locationRecordPropName;
}


public String getLocationPropertyName() {
	return locationPropertyName;
}


public void setLocationPropertyName(String locationPropertyName) {
	this.locationPropertyName = locationPropertyName;
}


public String getProductTypePropertyName() {
	return productTypePropertyName;
}


public void setProductTypePropertyName(String productTypePropertyName) {
	this.productTypePropertyName = productTypePropertyName;
}


public String getOofPropertyName() {
	return oofPropertyName;
}


public void setOofPropertyName(String oofPropertyName) {
	this.oofPropertyName = oofPropertyName;
}


public String getPlanTypePropertyName() {
	return planTypePropertyName;
}


public void setPlanTypePropertyName(String planTypePropertyName) {
	this.planTypePropertyName = planTypePropertyName;
}


public String getPayGoPlanType() {
	return payGoPlanType;
}


public void setPayGoPlanType(String payGoPlanType) {
	this.payGoPlanType = payGoPlanType;
}
}