/**
 * 
 */
package com.cricket.commerce.endeca.assembler.navigation.filter;

import com.cricket.common.constants.CricketCommonConstants;

import atg.endeca.assembler.navigation.filter.FilterUtils;
import atg.endeca.assembler.navigation.filter.PropertyConstraint;
import atg.endeca.assembler.navigation.filter.RecordFilterBuilderImpl;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.ServletUtil;


public class PhonesCompatibleToPlan extends RecordFilterBuilderImpl{
	
	private String compatiblePhonesPropName = "phone.compatiblePlan";
	
	/**
	 * @return the compatiblePhonesPropName
	 */
	public String getCompatiblePhonesPropName() {
		return compatiblePhonesPropName;
	}

	/**
	 * @param compatiblePhonesPropName the compatiblePhonesPropName to set
	 */
	public void setCompatiblePhonesPropName(String compatiblePhonesPropName) {
		this.compatiblePhonesPropName = compatiblePhonesPropName;
	}

	public String buildRecordFilter() {		
		
		DynamoHttpServletRequest request = ServletUtil.getCurrentRequest();
		String planId =  request.getParameter(CricketCommonConstants.PLAN_ID);
		PropertyConstraint[] propertyConstraints = new PropertyConstraint[1];
		if (planId != null) {
		    propertyConstraints = new PropertyConstraint[1];
		    propertyConstraints[0] = FilterUtils.constraint(getCompatiblePhonesPropName(), planId);
		
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

}
