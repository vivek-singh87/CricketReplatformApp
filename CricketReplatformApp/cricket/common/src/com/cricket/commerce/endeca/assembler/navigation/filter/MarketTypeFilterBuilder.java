package com.cricket.commerce.endeca.assembler.navigation.filter;

import com.cricket.common.constants.CricketCommonConstants;

import atg.endeca.assembler.navigation.filter.FilterUtils;
import atg.endeca.assembler.navigation.filter.PropertyConstraint;
import atg.endeca.assembler.navigation.filter.RecordFilterBuilderImpl;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;

public class MarketTypeFilterBuilder extends RecordFilterBuilderImpl {
	
	private ProfileServices profileService;
	
	private String marketTypePropertyName = "product.marketType";
	
	private boolean active = true;
	
	public String buildRecordFilter() {
		Profile profile = profileService.getCurrentProfile();
		String filter = null;
		if(profile != null && isActive()) {
			String marketType = (String) profile.getPropertyValue(CricketCommonConstants.MARKET_TYPE);
			if (isLoggingDebug()) {
				logDebug("current profile marketType: " + marketType);
			}
			if(marketType != null) {
				PropertyConstraint[] propertyConstraints = new PropertyConstraint[1];
				propertyConstraints[0] = FilterUtils.constraint(getMarketTypePropertyName(), marketType);
				filter = FilterUtils.or(propertyConstraints);
			}
		}
		if (isLoggingDebug()) {
			logDebug("marketType filter: " + filter);
		}
		return filter;
	}

	public ProfileServices getProfileService() {
		return profileService;
	}

	public void setProfileService(ProfileServices profileService) {
		this.profileService = profileService;
	}

	public String getMarketTypePropertyName() {
		return marketTypePropertyName;
	}

	public void setMarketTypePropertyName(String marketTypePropertyName) {
		this.marketTypePropertyName = marketTypePropertyName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
