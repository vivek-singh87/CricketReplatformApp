package com.cricket.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import atg.core.util.StringUtils;
import atg.repository.xml.AddException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.InquireFeaturesResponseVO;

 
public class DisplayFeaturesDroplet extends DynamoServlet{
	
	private CricketESPAdapter cricketESPAdapter;
	private DisplayFeaturesManager displayFeaturesManager;
	

	
	

	public void service(DynamoHttpServletRequest pRequest,DynamoHttpServletResponse pResponse) 
			throws ServletException,IOException {
		
		
	    	String modelNumber = pRequest.getParameter(CricketCommonConstants.MODEL_NUMBER);
	    	String planCode = pRequest.getParameter(CricketCommonConstants.PLAN_CODE);
	    	String marketCode = pRequest.getParameter(CricketCommonConstants.MARKET_CODE);
	    	String transactionName = pRequest.getParameter(CricketCommonConstants.TRANS_NAME);
	    	String orderId = pRequest.getParameter(CricketCommonConstants.ORDER_ID_PARAM);
	    	List userAddons = (ArrayList) pRequest.getObjectParameter("userAddons");   	
	    	boolean hasEsnHistory = Boolean.parseBoolean(pRequest.getParameter(CricketCommonConstants.HAS_ESN_HISTORY));
	    	boolean isCricketPhone = Boolean.parseBoolean(pRequest.getParameter(CricketCommonConstants.IS_CKT_PHONE));
	    	if (!StringUtils.isEmpty(modelNumber) && !StringUtils.isEmpty(planCode) && !StringUtils.isEmpty(marketCode)
	    			&& !StringUtils.isEmpty(transactionName)) {
		    	Map<String, List<InquireFeaturesResponseVO>> planAddOnsMap = getDisplayFeaturesManager().getCompatibleAddons(
																modelNumber, CricketCommonConstants.PHONE_TYPE, planCode, marketCode,
																	transactionName, userAddons, hasEsnHistory, isCricketPhone, CricketCommonConstants.CKT_SALES_CHANNEL_NAME,orderId);
		    	if(planAddOnsMap == null){
		    		pRequest.serviceLocalParameter(CricketCommonConstants.ERROR, pRequest, pResponse);		    		
		    	} else {
			    	pRequest.setParameter(CricketCommonConstants.INCLUDED_ADDONS, planAddOnsMap.get(CricketCommonConstants.INCLUDED_ADDONS));
			    	pRequest.setParameter(CricketCommonConstants.MANDATORY_ADDONS, planAddOnsMap.get(CricketCommonConstants.MANDATORY_ADDONS));
			    	pRequest.setParameter(CricketCommonConstants.OPTIONAL_ADDONS, planAddOnsMap.get(CricketCommonConstants.OPTIONAL_ADDONS));
			    	pRequest.setParameter(CricketCommonConstants.ALL_INCLUDED_ADDONS, planAddOnsMap.get(CricketCommonConstants.ALL_INCLUDED_ADDONS));
			    	pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
	    	}
	       // pRequest.setParameter("AddonDetail", inquireFeaturesResponseVO);
		//	pRequest.setParameter("planIncludedFeatures", getDisplayFeaturesManager().getPlanFeatures());
		//	pRequest.setParameter("planAddOns", getDisplayFeaturesManager().getPlanAddOns());
			
	    	}
			
	}


	/**
	 * 
	 * @return
	 */
	public CricketESPAdapter getCricketESPAdapter() {
		return cricketESPAdapter;
	}

	/**
	 * 
	 * @param cricketESPAdapter
	 */
	public void setCricketESPAdapter(CricketESPAdapter cricketESPAdapter) {
		this.cricketESPAdapter = cricketESPAdapter;
	}

	/**
	 * 
	 * @return
	 */
	public DisplayFeaturesManager getDisplayFeaturesManager() {
		return displayFeaturesManager;
	}

	/**
	 * 
	 * @param displayFeaturesManager
	 */
	public void setDisplayFeaturesManager(
			DisplayFeaturesManager displayFeaturesManager) {
		this.displayFeaturesManager = displayFeaturesManager;
	}	
}
