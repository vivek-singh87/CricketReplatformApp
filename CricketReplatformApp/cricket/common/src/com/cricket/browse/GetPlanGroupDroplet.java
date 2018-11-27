package com.cricket.browse;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;

import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.catalog.CatalogManager;
import com.cricket.common.constants.CricketCommonConstants;

 
public class GetPlanGroupDroplet extends DynamoServlet{
	
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
	@SuppressWarnings("unchecked")
	public void service(DynamoHttpServletRequest pRequest,DynamoHttpServletResponse pResponse) 
			throws ServletException,IOException {
				
			Set<RepositoryItem> planGroupItems  = (Set<RepositoryItem>)pRequest.getObjectParameter(CricketCommonConstants.PLAN_GROUP_ITEMS);	    	
	    	RepositoryItem planGroupItem = getCatalogManager().getPlanGroup(planGroupItems);
	    	pRequest.setParameter(CricketCommonConstants.PLAN_GROUP, planGroupItem);
			pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);			
	}	
}
