package com.cricket.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.adapter.gsa.ChangeAwareSet;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

/**
 * @author RM112139
 *
 */
public class CompatibleProductListingDroplet extends DynamoServlet{
	
		
	/**
	 * 
	 */
	private BrowseShopManager browseShopManager;
	
		
	/**
	 * @return
	 */
	public BrowseShopManager getBrowseShopManager() {
		return browseShopManager;
	}



	/**
	 * @param browseShopManager
	 */
	public void setBrowseShopManager(final BrowseShopManager browseShopManager) {
		this.browseShopManager = browseShopManager;
	}	



	/**
	 * @param pRequest
	 * @param pResponse
	 */
	@SuppressWarnings("unchecked")
	public void service(final DynamoHttpServletRequest pRequest,
			final DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		
		ChangeAwareSet planItemsSet = (ChangeAwareSet)pRequest.getObjectParameter("planItems");
		List<RepositoryItem> planItemList = new ArrayList<RepositoryItem>();
		for (Object planItem : planItemsSet) {
			planItemList.add((RepositoryItem) planItem);
		}
		List<PlanVO> planVOs = getBrowseShopManager().getListOfCompatiblePlans(planItemList);
		pRequest.setParameter("planVOList", planVOs);
		pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
	}

}
