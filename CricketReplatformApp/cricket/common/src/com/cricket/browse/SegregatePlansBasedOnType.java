package com.cricket.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class SegregatePlansBasedOnType extends DynamoServlet {
	
	private List<String> specValues;
	/**
	 * @param pRequest
	 * @param pResponse
	 */
	public void service(final DynamoHttpServletRequest pRequest,
			final DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		
		if(isLoggingDebug()) {
			logDebug("entering SegregatePlansBasedOnType service method");
		}
		List<PlanVO> planVos = (List<PlanVO>)pRequest.getObjectParameter("planVOsList");
		List<PlanVO> featurePlanVos = new ArrayList<PlanVO>();
		List<PlanVO> smartPlanVos = new ArrayList<PlanVO>();
		Map<String, PlanSpecsVO> orderedSpecsMap = new HashMap();
		List<String> specNameKeys = new ArrayList<String>();
		for (PlanVO planVO : planVos) {
			String ratePlanType = planVO.getRatePlanType();
			if(ratePlanType != null && ratePlanType.equalsIgnoreCase(CricketCommonConstants.CHAR_V)) {
				featurePlanVos.add(planVO);
			}
			if(ratePlanType != null && ratePlanType.equalsIgnoreCase(CricketCommonConstants.CHAR_D)) {
				smartPlanVos.add(planVO);
			}
			Map<String, PlanSpecsVO> planSpecs = planVO.getPlanSpecs();
			
			if (planSpecs != null) {
				for (Map.Entry<String, PlanSpecsVO> entry : planSpecs.entrySet()) {
					String keySpec = planVO.getProductId() + "||" + entry.getKey();
					orderedSpecsMap.put(keySpec, entry.getValue());
					if(!specNameKeys.contains(entry.getKey())){
						specNameKeys.add(entry.getKey());
					}
				}
			}
		}
		int numberOfFeaturePlans = featurePlanVos.size();
		int numberOfSmartPlans = smartPlanVos.size();
		pRequest.setParameter(CricketCommonConstants.NUMBEROF_FEATUREPLANS, numberOfFeaturePlans);
		pRequest.setParameter(CricketCommonConstants.NUMBEROF_SMARTPLANS, numberOfSmartPlans);
		pRequest.setParameter(CricketCommonConstants.FEATURE_PLAN_VO_LIST, featurePlanVos);
		pRequest.setParameter(CricketCommonConstants.SMART_PLAN_VO_LIST, smartPlanVos);
		pRequest.setParameter(CricketCommonConstants.ORDERED_SPECS_MAP, orderedSpecsMap);
		pRequest.setParameter(CricketCommonConstants.SPEC_NAME_KEYS, specNameKeys);
		if(isLoggingDebug()) {
			logDebug("numberOfFeaturePlans :" + numberOfFeaturePlans);
			logDebug("numberOfSmartPlans :" + numberOfSmartPlans);
			logDebug("featurePlanVOList :" + featurePlanVos);
			logDebug("smartPlanVOList :" + smartPlanVos);
			logDebug("orderedSpecsMap :" + orderedSpecsMap);
			logDebug("specNameKeys :" + specNameKeys);
		}
		if(isLoggingDebug()) {
			logDebug("exiting SegregatePlansBasedOnType service method");
		}
		pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
		
	}


	public List<String> getSpecValues() {
		return specValues;
	}


	public void setSpecValues(List<String> specValues) {
		this.specValues = specValues;
	}
}