package com.cricket.commerce.order.droplet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import atg.core.util.StringUtils;
import atg.nucleus.naming.ParameterName;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.servlet.ServletUtil;

import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.common.constants.CricketCommonConstants;

/**
 * This Droplet is used to get the commerce items in the order,
 * segregated based on packages and product type.
 * the output of this droplet is a map of all the data aligned to the VO.
 */
public class DisplayShoppingCartDroplet extends DynamoServlet {

	  /** The input parameter name for the order item. */
	  public static final ParameterName ORDER = ParameterName.getParameterName(CricketCommonConstants.ITEM_DESC_ORDER);

	  /** The oparam name rendered once during processing.*/
	  public static final String OPARAM_OUTPUT = "output";
	  
	   /** The oparam name rendered once during processing.*/
	  public static final String ACCESSORIES_OUTPUT = "accessories";

	   /** The oparam name rendered once during processing.*/
	  public static final String PACKAGES_OUTPUT = "packages";
	  
	   /** The oparam name rendered once during processing.*/
	  public static final String UPGRADE_PHONE_OUTPUT = "upgradePhone";
	  
	   /** The oparam name rendered once during processing.*/
	  public static final String CHANGE_PLAN_OUTPUT = "changePlan";
	  
	   /** The oparam name rendered once during processing.*/
	  public static final String CHANGE_ADDONS = "changeAddons";
	  
	   /** The oparam name rendered once during processing.*/
	  public static final String REMOVED_ADDONS = "removedAddon";
	  
	  CricketOrderTools mOrderTools;
	  
	/** (non-Javadoc)
	 * @see atg.servlet.DynamoServlet#service(atg.servlet.DynamoHttpServletRequest, atg.servlet.DynamoHttpServletResponse)
	 */
	@Override
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {		
		CricketOrderImpl order = (CricketOrderImpl) pRequest.getObjectParameter(ORDER);

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
								
	    		logDebug("Entering into DisplayShoppingCartDroplet class of service() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		Map<String, Object> displyItemsMap = getOrderTools().getDisplayItems(order, pRequest);
		if(isLoggingDebug()){
			logDebug("Start Fetching the CommerceItem details for Order : " + order.getId());
		}

		if(displyItemsMap != null && displyItemsMap.size() > 0){
			pRequest.setParameter(ACCESSORIES_OUTPUT, displyItemsMap.get(CricketCommonConstants.ACCESSORIES));
			pRequest.setParameter(UPGRADE_PHONE_OUTPUT, displyItemsMap.get(CricketCommonConstants.UPGRADE_PHONE));
			pRequest.setParameter(CHANGE_PLAN_OUTPUT, displyItemsMap.get(CricketCommonConstants.CHANGE_PLAN));
			pRequest.setParameter(CHANGE_ADDONS, displyItemsMap.get(CricketCommonConstants.CHANGE_ADDONS));
			pRequest.setParameter(PACKAGES_OUTPUT, displyItemsMap.get(CricketCommonConstants.PACKAGES));
			pRequest.setParameter(REMOVED_ADDONS, displyItemsMap.get(CricketCommonConstants.REMOVED_ADDONS));
		}
		if(isLoggingDebug()){
			logDebug("End Fetching the CommerceItem details for Order : "  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId);
		}
		pRequest.serviceLocalParameter(OPARAM_OUTPUT, pRequest, pResponse);
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
	 * @return the orderTools
	 */
	public CricketOrderTools getOrderTools() {
		return mOrderTools;
	}

	/**
	 * @param pOrderTools the orderTools to set
	 */
	public void setOrderTools(CricketOrderTools pOrderTools) {
		mOrderTools = pOrderTools;
	}
}
