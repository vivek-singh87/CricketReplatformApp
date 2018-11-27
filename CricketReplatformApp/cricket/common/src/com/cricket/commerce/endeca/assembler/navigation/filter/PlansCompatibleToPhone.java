/**
 * 
 */
package com.cricket.commerce.endeca.assembler.navigation.filter;

import com.cricket.common.constants.CricketCommonConstants;

import atg.endeca.assembler.navigation.filter.FilterUtils;
import atg.endeca.assembler.navigation.filter.PropertyConstraint;
import atg.endeca.assembler.navigation.filter.RecordFilterBuilderImpl;
import atg.repository.Repository;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.ServletUtil;

public class PlansCompatibleToPhone extends RecordFilterBuilderImpl{
	
	private String compatiblePlansPropName = "plan.compatiblePhone";
	private Repository catalogRepository;		
	/**
	 * @return the catalogRepository
	 */
	public Repository getCatalogRepository() {
		return catalogRepository;
	}

	/**
	 * @param catalogRepository the catalogRepository to set
	 */
	public void setCatalogRepository(Repository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}
	

	
	/**
	 * @return the compatiblePlansPropName
	 */
	public String getCompatiblePlansPropName() {
		return compatiblePlansPropName;
	}

	/**
	 * @param compatiblePlansPropName the compatiblePlansPropName to set
	 */
	public void setCompatiblePlansPropName(String compatiblePlansPropName) {
		this.compatiblePlansPropName = compatiblePlansPropName;
	}

	public String buildRecordFilter() {		
	
		DynamoHttpServletRequest request = ServletUtil.getCurrentRequest();
		String phoneId =  request.getParameter(CricketCommonConstants.PHONE_ID);
		PropertyConstraint[] propertyConstraints = new PropertyConstraint[1];
		if (phoneId != null && !phoneId.isEmpty()) {
		    propertyConstraints = new PropertyConstraint[1];
		    propertyConstraints[0] = FilterUtils.constraint(getCompatiblePlansPropName(), phoneId);
		
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
