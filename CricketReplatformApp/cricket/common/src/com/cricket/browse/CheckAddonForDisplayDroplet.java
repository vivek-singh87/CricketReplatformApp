package com.cricket.browse;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.catalog.CatalogManager;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.integration.esp.vo.InquireFeaturesResponseVO;

 
public class CheckAddonForDisplayDroplet extends DynamoServlet{
	
	private CatalogManager catalogManager;
	
	/**
	 * @return
	 */
	public CatalogManager getCatalogManager() {
		return catalogManager;
	}

	/**
	 * @param catalogManager
	 */
	public void setCatalogManager(CatalogManager catalogManager) {
		this.catalogManager = catalogManager;
	}

	/**
	 * 
	 */
	public void service(DynamoHttpServletRequest pRequest,DynamoHttpServletResponse pResponse) 
			throws ServletException,IOException {
				
	    	@SuppressWarnings("unchecked")
			List<InquireFeaturesResponseVO> optionalAddOns = (List<InquireFeaturesResponseVO>)pRequest.getObjectParameter(CricketCommonConstants.OPTIONAL_ADDONS);
	    	List<InquireFeaturesResponseVO> allIncludedAddOns = (List<InquireFeaturesResponseVO>)pRequest.getObjectParameter(CricketCommonConstants.ALL_INCLUDED_ADDONS);	    	
	    	List<AddonVO>  addOnsForDisplay = getCatalogManager().getAddOns(optionalAddOns, allIncludedAddOns);
	    	pRequest.setParameter(CricketCommonConstants.ADDONS_FOR_DISPLAY, addOnsForDisplay);
			pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);			
	}	
}
