package com.cricket.commerce.order.droplet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import atg.core.util.StringUtils;
import atg.nucleus.naming.ComponentName;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.servlet.ServletUtil;

import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.commerce.order.CricketPackage;
import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.commerce.order.session.UpgradeItemDetailsSessionBean;
import com.cricket.common.constants.CricketCommonConstants;

public class DisplayAddToCartForAddOnDroplet extends DynamoServlet {
	
	 private CricketOrderTools mOrderTools;
	 public static final String OPARAM_OUTPUT = "output";
	 public static final String SHOW_CART_BUTTON = "showCartButton";
	 private ComponentName mSessionBeanComponentName = null;
	 private String mSessionBeanPath;
	 private CartConfiguration cartConfiguration;
	 
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		boolean showCartButton = false;
		String oldProdType=null;
		int count=0;
		String productType = null;
		
		CricketOrderImpl order = (CricketOrderImpl) pRequest.getObjectParameter(CricketCommonConstants.ITEM_DESC_ORDER);

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
	    		logDebug("Entering into DisplayAddToCartForAddOnDroplet class of service() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		List<CricketPackage> packageItem = order.getCktPackages(); 
		for (CricketPackage packItem : packageItem) {
			List<CricketCommerceItemImpl> commerceItemswithUniquePackages = getOrderTools().getCommerceItemswithUniquePackageId(packItem.getId(), order);
			if (null != commerceItemswithUniquePackages) {
				for (CricketCommerceItemImpl commerceItem : commerceItemswithUniquePackages) {
					productType = (String) commerceItem.getCricItemTypes();
					if (StringUtils.isEmpty(oldProdType)) {
						oldProdType = productType;
					}
					if (!oldProdType.equalsIgnoreCase(productType) && count == 0) {
						count++;
						showCartButton=true;
					}
				}
				
			}
		}
		//Added to display add to cart button in case of upgrade plan and upgrade add-on flows.
		if (!showCartButton) {
			UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean = (UpgradeItemDetailsSessionBean) pRequest
					.resolveName(mSessionBeanComponentName);
			if (upgradeItemDetailsSessionBean != null &&
					!StringUtils.isEmpty(upgradeItemDetailsSessionBean.getUserIntention())
					&& (upgradeItemDetailsSessionBean.getUserIntention().equalsIgnoreCase(getCartConfiguration().getUpgradePlanIntention())
							|| upgradeItemDetailsSessionBean.getUserIntention().equalsIgnoreCase(getCartConfiguration().getUpgradeAddonIntention()))) {
				showCartButton=true;
			}
		}
		pRequest.setParameter(SHOW_CART_BUTTON, showCartButton);
		pRequest.serviceLocalParameter(OPARAM_OUTPUT, pRequest,pResponse);
		if(isLoggingDebug()){
			logDebug("[DisplayAddToCartForAddOnDroplet->service()]: Exiting service()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId );
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
	 * @return the sessionBeanComponentName
	 */
	public ComponentName getSessionBeanComponentName() {
		return mSessionBeanComponentName;
	}

	/**
	 * @param pSessionBeanComponentName the sessionBeanComponentName to set
	 */
	public void setSessionBeanComponentName(ComponentName pSessionBeanComponentName) {
		mSessionBeanComponentName = pSessionBeanComponentName;
	}
	
	/**
	 * @return the sessionBeanPath
	 */
	public String getSessionBeanPath() {
		return mSessionBeanPath;
	}

	/**
	 * @param pSessionBeanPath the sessionBeanPath to set
	 */
	public void setSessionBeanPath(String pSessionBeanPath) {
		mSessionBeanPath = pSessionBeanPath;
		if (mSessionBeanPath != null) {
			mSessionBeanComponentName = ComponentName.getComponentName(mSessionBeanPath);
		} else {
			mSessionBeanComponentName = null;
		}
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
