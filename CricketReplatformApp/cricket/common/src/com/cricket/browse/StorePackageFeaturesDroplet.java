package com.cricket.browse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.integration.esp.CricketESPAdapterHelper;
import com.cricket.integration.esp.vo.InquireFeaturesResponseVO;
import com.cricket.user.session.UserSessionBean;

 
public class StorePackageFeaturesDroplet extends DynamoServlet{
	
	private UserSessionBean userSessionBean;
	/* holds CricketESPAdapterHelper */
	private CricketESPAdapterHelper cricketESPAdapterHelper;
	/**
	 * Property PACKAGE_ID of type String
	 */
	private static final String PACKAGE_ID = "packageId";
	
	private ProfileServices profileServices;
	
	/**
	 * @return the profileServices
	 */
	public ProfileServices getProfileServices() {
		return profileServices;
	}

	/**
	 * @param profileServices the profileServices to set
	 */
	public void setProfileServices(ProfileServices profileServices) {
		this.profileServices = profileServices;
	}
	
	/**
	 * 
	 * @return
	 */
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}


	/**
	 * 
	 * @param userSessionBean
	 */
	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

	/**
	 * @return the cricketESPAdapterHelper
	 */
	public CricketESPAdapterHelper getCricketESPAdapterHelper() {
		return cricketESPAdapterHelper;
	}


	/**
	 * @param cricketESPAdapterHelper the cricketESPAdapterHelper to set
	 */
	public void setCricketESPAdapterHelper(
			CricketESPAdapterHelper cricketESPAdapterHelper) {
		this.cricketESPAdapterHelper = cricketESPAdapterHelper;
	}


	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void service(DynamoHttpServletRequest pRequest,DynamoHttpServletResponse pResponse) 
			throws ServletException,IOException {
		
		
	    	String packageId = pRequest.getParameter(PACKAGE_ID);
	    	String planId = pRequest.getParameter(CricketCommonConstants.PLAN_ID);
	    	Profile profile = getProfileServices().getCurrentProfile();
	    	Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersMap = null;
	    	Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersUpgradePlanMap = null;
	    	List<InquireFeaturesResponseVO> mandatoryAddOns = (List<InquireFeaturesResponseVO>)pRequest.getObjectParameter(CricketCommonConstants.MANDATORY_ADDONS);
	    	List<InquireFeaturesResponseVO> optionalAddOns = (List<InquireFeaturesResponseVO>)pRequest.getObjectParameter(CricketCommonConstants.OPTIONAL_ADDONS);
	    	List<InquireFeaturesResponseVO> hppAddOns = (List<InquireFeaturesResponseVO>)pRequest.getObjectParameter(CricketCommonConstants.HPP_ADDONS);
	    	if (null != hppAddOns && !hppAddOns.isEmpty()) {
	    		getUserSessionBean().setHppAddOns(hppAddOns);
	    	}
	    	
	    	InquireFeaturesResponseVO administrationFeeFeature = getCricketESPAdapterHelper().getAdministrationFeeAddOn(optionalAddOns);	
	    	InquireFeaturesResponseVO activationFeeFeature = getCricketESPAdapterHelper().getActivationFeeAddOn(optionalAddOns);
	    	if(activationFeeFeature != null && activationFeeFeature.getPrice() != null) {
	    		profile.setPropertyValue(CricketCommonConstants.ACTIVATION_FEE, activationFeeFeature.getPrice().doubleValue());
	    	}
	    	
		    if(getUserSessionBean()!=null){
	    		mandatoryOffersMap = getUserSessionBean().getMandatoryOffersMap();
		    	mandatoryOffersUpgradePlanMap = getUserSessionBean().getMandatoryOffersUpgradePlanMap();
		    	if (mandatoryOffersMap == null) {
		    		mandatoryOffersMap = new HashMap<String, List<InquireFeaturesResponseVO>>();
		    	}
		    	if (mandatoryOffersUpgradePlanMap == null) {
		    		mandatoryOffersUpgradePlanMap = new HashMap<String, List<InquireFeaturesResponseVO>>();
		    	}
		    	if(packageId != null && mandatoryAddOns != null && mandatoryAddOns.size() > 0) {		    		
		    		if(administrationFeeFeature != null){
		    			//Administration Fee addOn needs to be sent to billing system for charging the user on monthly basis
		    			//Hence adding to mandatoryAddOns list which will be used in CreateActivationQuote request for building CricketOfferingCodes
		    			mandatoryAddOns.add(administrationFeeFeature);
		    		}
			    	mandatoryOffersMap.put(packageId, mandatoryAddOns);	    		
			    	getUserSessionBean().setMandatoryOffersMap(mandatoryOffersMap);
		    	}
		    	if(packageId == null && planId != null && mandatoryAddOns != null && mandatoryAddOns.size() > 0) {
		    		if(administrationFeeFeature != null){
		    			//Administration Fee addOn needs to be sent to billing system for charging the user on monthly basis
		    			//Hence adding to mandatoryAddOns list which will be used in CreateActivationQuote request for building CricketOfferingCodes
		    			mandatoryAddOns.add(administrationFeeFeature);
		    		}
			    	mandatoryOffersMap.put(planId, mandatoryAddOns);	    
			    	getUserSessionBean().setMandatoryOffersUpgradePlanMap(mandatoryOffersUpgradePlanMap);		
		    	}
		    }
	}
	
}
