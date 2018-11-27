package com.cricket.commerce.order.droplet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.common.constants.CricketCommonConstants;

import atg.core.util.StringUtils;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.servlet.ServletUtil;

public class CricketCheckChangeAddonAndPlanDroplet extends DynamoServlet {

	private CartConfiguration cartConfiguration;
	public static final String CHANGE_ADDON_PLAN = "changeAddonAndPlan";
	public static final String OPARAM_OUTPUT = "output";
	@Override
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		Boolean changeAddonAndPlan = false;
		CricketOrderImpl order = (CricketOrderImpl) pRequest.getObjectParameter(CricketCommonConstants.ITEM_DESC_ORDER);

		// Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}
			}
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
				pageURL = pRequest.getRequestURIWithQueryString();
			}
		if (isLoggingDebug()) {						 					
	    		logDebug("Entering into CricketCheckChangeAddonAndPlanDroplet class of service() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		List<CricketCommerceItemImpl> commerceItem = order.getCommerceItems();
		for(CricketCommerceItemImpl commItem : commerceItem){
			String cricItemTypes = commItem.getCricItemTypes();
			if(cricItemTypes != null && getCartConfiguration().getChangePlanItemType().equalsIgnoreCase(cricItemTypes)
					|| getCartConfiguration().getChangeAddOnItemType().equalsIgnoreCase(cricItemTypes) || getCartConfiguration().getRemovedAddonItemType().equalsIgnoreCase(cricItemTypes)){
				changeAddonAndPlan = true;
			}
		}
		pRequest.setParameter(CHANGE_ADDON_PLAN, changeAddonAndPlan);
		pRequest.serviceLocalParameter(OPARAM_OUTPUT, pRequest,pResponse);
		
		if(isLoggingDebug()){
			logDebug("[CricketCheckChangeAddonAndPlanDroplet->service()]: Exiting service()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId );
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
	public CartConfiguration getCartConfiguration() {
		return cartConfiguration;
	}
	public void setCartConfiguration(CartConfiguration cartConfiguration) {
		this.cartConfiguration = cartConfiguration;
	}

	
	
}
