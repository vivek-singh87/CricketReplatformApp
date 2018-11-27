package com.cricket.browse;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.nucleus.GenericService;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

/**
 * @author rm112139
 * 
 */
public class AccessoryDetailsDroplet extends GenericService {
	
	/**
	 * Property PRODUCT_ID of type String
	 */
	private static final String PRODUCT_ID = "productId";
	/**
	 * Property ACCESSORIES_DETAILS of type String
	 */
	private static final String ACCESSORIES_DETAILS = "ACCESSORIES_DETAILS";

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
	public void setBrowseShopManager(BrowseShopManager browseShopManager) {
		this.browseShopManager = browseShopManager;
	}

	/**
	 * @param pRequest
	 * @param pResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		String productId = (String) pRequest.getParameter(PRODUCT_ID);
		AccessoryVO accessoryVO = getBrowseShopManager().getAccessoryDetails(
				productId);
		pRequest.setParameter(ACCESSORIES_DETAILS, accessoryVO);
		pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
	}

}
