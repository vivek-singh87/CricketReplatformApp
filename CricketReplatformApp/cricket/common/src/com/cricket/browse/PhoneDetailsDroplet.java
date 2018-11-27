package com.cricket.browse;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

/**
 * @author rm112139
 * 
 */
public class PhoneDetailsDroplet extends DynamoServlet {
	//public static String PHONE_DETAILS = "phoneVO";
	
	/**
	 * Property PHONE_DETAILS of type String
	 */
	private static final String PHONE_DETAILS = "phoneVO";
	/**
	 * Property PRODUCT_ID of type String
	 */
	private static final String PRODUCT_ID = "productId";

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
		String zipCode = (String) pRequest.getParameter(CricketCommonConstants.ZIP_CODE);
		PhoneVO phoneVO = getBrowseShopManager().getPhoneDetails(productId,zipCode);
		pRequest.setParameter(PHONE_DETAILS, phoneVO);
		pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
	}

}
