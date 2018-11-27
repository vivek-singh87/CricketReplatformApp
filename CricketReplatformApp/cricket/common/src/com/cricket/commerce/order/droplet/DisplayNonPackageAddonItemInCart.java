package com.cricket.commerce.order.droplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.common.constants.CricketCommonConstants;

import atg.core.util.StringUtils;
import atg.nucleus.naming.ParameterName;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.servlet.ServletUtil;

public class DisplayNonPackageAddonItemInCart extends DynamoServlet {

	  /** The input parameter name for the order item. */
	  public static final ParameterName ORDER = ParameterName.getParameterName(CricketCommonConstants.ITEM_DESC_ORDER);

	  /** The oparam name rendered once during processing.*/
	  public static final String OPARAM_OUTPUT = "output";
	  
	  /** The oparam name rendered once during processing.*/
	  public static final String OPARAM_EMPTY = "empty";
	  
	  /** Param name for size.*/
	  public static final String SIZE = "Size";
	  
	  /**Param name for addons. **/
	  public static final String ADDONS = "addons";
	  
	  private CartConfiguration cartConfiguration;
		 
	  @SuppressWarnings("unchecked")
	@Override
		public void service(DynamoHttpServletRequest pRequest,
				DynamoHttpServletResponse pResponse) throws ServletException,
				IOException {
			CricketOrderImpl order = (CricketOrderImpl) pRequest.getObjectParameter(ORDER);
			int addonSize = 0;

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
		    		logDebug("Entering into DisplayNonPackageAddonItemInCart class of service() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
			List<CricketCommerceItemImpl> addonsItems = new ArrayList<CricketCommerceItemImpl>();
			if(order != null && order.getCommerceItemCount() > 0){
				List<CricketCommerceItemImpl> commerceItemList = order.getCommerceItems();
				for(CricketCommerceItemImpl commerceItem : commerceItemList){
					//product type of commerce item
					String productType = commerceItem.getCricItemTypes();
					String packageId = commerceItem.getPackageId();
					if (productType != null && productType.equalsIgnoreCase(getCartConfiguration().getAddonProductItemType())
							&& StringUtils.isEmpty(packageId)) {
						addonSize+=1;
						addonsItems.add(commerceItem);
					}
				}
			}
			if (addonsItems.isEmpty()) {
				pRequest.setParameter(SIZE, addonSize);
				pRequest.serviceLocalParameter(OPARAM_EMPTY, pRequest, pResponse);
			} else {
				pRequest.setParameter(ADDONS, addonsItems);
				pRequest.setParameter(SIZE, addonSize);
				pRequest.serviceLocalParameter(OPARAM_OUTPUT, pRequest, pResponse);
			}
			if(isLoggingDebug()){
				logDebug("[DisplayNonPackageAddonItemInCart->service()]: Exiting service()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId );
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
