package com.cricket.commerce.order.droplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import atg.core.util.StringUtils;
import atg.droplet.Range;
import atg.nucleus.naming.ParameterName;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.servlet.ServletUtil;

import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.common.constants.CricketCommonConstants;

/**
 * This Droplet is used to display the user Addons section in addon listing
 * page.
 * 
 */
public class DisplayUserAddonsForRemoval extends DynamoServlet {

	private CricketOrderTools mOrderTools;
	private CartConfiguration cartConfiguration;
	/** The input parameter name for the order item. */
	public static final ParameterName ORDER = ParameterName
			.getParameterName(CricketCommonConstants.ITEM_DESC_ORDER);

	/** The oparam name rendered once during processing. */
	public static final String OPARAM_OUTPUT = "output";
	
	public static final String USER_ADDON_DISPLAY = "userAddonsForDisplay";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		CricketOrderImpl order = (CricketOrderImpl) pRequest
				.getObjectParameter(ORDER);

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}
			}
		if (isLoggingDebug()) {							
	    		logDebug("Entering into DisplayUserAddonsForRemoval class of service() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		List mdnAddons = (List) pRequest.getObjectParameter(Range.ARRAY);
		List userAddonsForDisplay = new ArrayList();
		if (null != mdnAddons) {
			userAddonsForDisplay = mdnAddons;
			if (null != order && order.getCommerceItemCount() > 0 && order.getCommerceItems() != null && !order.getCommerceItems().isEmpty()) {
				List<String> removedAddonsList= getOrderTools().getRemovedAddonCommerceItems(order);
				userAddonsForDisplay.removeAll(removedAddonsList);
			}
		}
		pRequest.setParameter(USER_ADDON_DISPLAY, userAddonsForDisplay);
		pRequest.serviceLocalParameter(OPARAM_OUTPUT, pRequest,pResponse);
		if(isLoggingDebug()){
			logDebug("Exiting from DisplayUserAddonsForRemoval class of service() method  : "  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId);
		}
	}
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	/**
	 * @return the mOrderTools
	 */
	public CricketOrderTools getOrderTools() {
		return mOrderTools;
	}

	/**
	 * @param mOrderTools the mOrderTools to set
	 */
	public void setOrderTools(CricketOrderTools mOrderTools) {
		this.mOrderTools = mOrderTools;
	}
	
	/**
	 * @return the cartConfiguration
	 */
	public CartConfiguration getCartConfiguration() {
		return cartConfiguration;
	}

	/**
	 * @param pCartConfiguration the cartConfiguration to set
	 */
	public void setCartConfiguration(CartConfiguration pCartConfiguration) {
		cartConfiguration = pCartConfiguration;
	}
}
