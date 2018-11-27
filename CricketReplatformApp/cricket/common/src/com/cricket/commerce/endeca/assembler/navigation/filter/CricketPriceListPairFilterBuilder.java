package com.cricket.commerce.endeca.assembler.navigation.filter;

import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;
import atg.endeca.assembler.navigation.filter.FilterUtils;
import atg.endeca.assembler.navigation.filter.PropertyConstraint;
import atg.endeca.assembler.navigation.filter.RecordFilterBuilderImpl;

public class CricketPriceListPairFilterBuilder extends RecordFilterBuilderImpl {
	
	private ProfileServices profileService;
	private String locationPropertyName = "userLocationZipCode";
	private String locationRecordPropName = "market.zipCode";


public String buildRecordFilter() {
	Profile profile = profileService.getCurrentProfile();
	String location = (String) profile.getPropertyValue(locationPropertyName);
	if (isLoggingDebug()) {
		logDebug("current profile location: " + location);
	}
	PropertyConstraint[] propertyConstraints = new PropertyConstraint[1];
	if (location != null) {
	    propertyConstraints = new PropertyConstraint[1];
	    propertyConstraints[0] = FilterUtils.constraint(getLocationRecordPropName(), location);
	
	    String filter = FilterUtils.or(propertyConstraints);
	    if (isLoggingDebug()) {
	      logDebug("Catalog filter: " + filter);
	    }
	    return filter;
	} else {
		String filter = FilterUtils.or(propertyConstraints);
		return filter;
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
}