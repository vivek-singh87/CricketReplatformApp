package com.cricket.commerce.endeca.assembler.navigation;

import java.util.Set;

import com.endeca.infront.navigation.NavigationState;

import atg.projects.store.assembler.navigation.StoreNavigationStateProcessor;

public class CricketNavigationStateProcessor extends StoreNavigationStateProcessor {
	
	private boolean enabled;
	
	private String searchUserSegment;
	
	@Override
	public void process(NavigationState pNavigationState) {
		super.process(pNavigationState);
		
		if(enabled) {
			Set<String> userSegments = getUserState().getUserSegments();
			if(userSegments.contains(getUserSegment())) {
				
			} else {
				getUserState().addUserSegments(searchUserSegment);
			}
		}
		
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getSearchUserSegment() {
		return searchUserSegment;
	}

	public void setSearchUserSegment(String searchUserSegment) {
		this.searchUserSegment = searchUserSegment;
	}

}
