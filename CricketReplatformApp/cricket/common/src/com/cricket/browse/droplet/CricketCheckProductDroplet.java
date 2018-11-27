package com.cricket.browse.droplet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.cricket.catalog.CatalogManager;
import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class CricketCheckProductDroplet extends DynamoServlet {
	
	
	public static final String PRODUCT = "product";
	public static final String MODEL_NUM = "modelNum";
	public static final String RATE_PLAN_CODE = "ratePlanCode";
	
	CatalogManager catalogManager;

	/* (non-Javadoc)
	 * @see atg.servlet.DynamoServlet#service(atg.servlet.DynamoHttpServletRequest, atg.servlet.DynamoHttpServletResponse)
	 */
	@Override
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		String modelNum = pRequest.getParameter(MODEL_NUM);
		String ratePlanCode = pRequest.getParameter(RATE_PLAN_CODE);
		if(modelNum != null ){
			List<RepositoryItem> phoneProducts = getCatalogManager().getPhonesForModelNumber(modelNum);
			String planProducts = getCatalogManager().CheckPlanEndDate(ratePlanCode);
			if(phoneProducts.size() == 0 || planProducts.equalsIgnoreCase(CricketCommonConstants.YES)){
				pRequest.setParameter(PRODUCT, false);
				pRequest.serviceParameter(CricketCommonConstants.ERROR, pRequest, pResponse);
			}else{
				pRequest.setParameter(PRODUCT, true);
				pRequest.serviceParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
			}
		}
		
		
	}

	/**
	 * @return the catalogManager
	 */
	public CatalogManager getCatalogManager() {
		return catalogManager;
	}

	/**
	 * @param mCatalogManager the catalogManager to set
	 */
	public void setCatalogManager(CatalogManager mCatalogManager) {
		catalogManager = mCatalogManager;
	}
	
	
	

}
