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
import atg.userprofiling.Profile;

import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.commerce.order.CricketPackage;
import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.commerce.order.session.UpgradeItemDetailsSessionBean;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;

public class DisplayCheckoutButtonDroplet extends DynamoServlet {
	
	
	private CricketOrderTools mOrderTools;
	 public static final String OPARAM_OUTPUT = "output";
	 private ComponentName mSessionBeanComponentName = null;
	 private ComponentName mProfileComponentName = null;
	 private String mSessionBeanPath;
	 private CartConfiguration cartConfiguration;
	 private String mProfilePath;
	 private String toolTipMessage = null;
	 
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		boolean showCheckOutButton = false;
		String productType = null;
		CricketOrderImpl order = (CricketOrderImpl) pRequest.getObjectParameter(CricketCookieConstants.ORDER2);
		
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
	    		logDebug("Entering into DisplayCheckoutButtonDroplet class of service() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean = (UpgradeItemDetailsSessionBean) pRequest
				.resolveName("/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean");
		@SuppressWarnings("unchecked")
		List<CricketCommerceItemImpl> commerceItems = order.getCommerceItems();
		Profile profile = (Profile) pRequest.resolveName(mProfileComponentName);
		if (profile.isTransient()) {
			for (CricketCommerceItemImpl packItem : commerceItems) {
				String cricItemTypes = (String) packItem.getCricItemTypes();
				if(!StringUtils.isEmpty(cricItemTypes) && (cricItemTypes.equalsIgnoreCase(getCartConfiguration().getUpgradeAddonIntention())
						|| cricItemTypes.equalsIgnoreCase(getCartConfiguration().getUpgradePhoneIntention())
						|| cricItemTypes.equalsIgnoreCase(getCartConfiguration().getUpgradePlanIntention()))) {
					showCheckOutButton = false;
					/*setToolTipMessage("checkoutbutton_default_message");*/
					pRequest.setParameter(CricketCookieConstants.SHOW_CHECK_OUT_BUTTON, showCheckOutButton);
					pRequest.setParameter(CricketCookieConstants.TOOL_TIP_MESSAGE, getToolTipMessage());
					pRequest.serviceLocalParameter(OPARAM_OUTPUT, pRequest,pResponse);
					return;
				}
			}
		}
		if(upgradeItemDetailsSessionBean != null && null != commerceItems && !commerceItems.isEmpty() && commerceItems.size() > 0){
			String userIntention = upgradeItemDetailsSessionBean.getUserIntention();
			if(userIntention != null && ((userIntention.equalsIgnoreCase(CricketCookieConstants.UPGRADE_FEATURE)) || (userIntention.equalsIgnoreCase(CricketCookieConstants.UPGRADE_PHONE)) || (userIntention.equalsIgnoreCase(CricketCookieConstants.UPGRADE_PLAN)))){
				showCheckOutButton = true;
				pRequest.setParameter(CricketCookieConstants.SHOW_CHECK_OUT_BUTTON, showCheckOutButton);
				pRequest.setParameter(CricketCookieConstants.TOOL_TIP_MESSAGE, getToolTipMessage());
				pRequest.serviceLocalParameter(OPARAM_OUTPUT, pRequest,pResponse);
				return;
			}
		}
		
		List<CricketPackage> packageItem = order.getCktPackages();
		for (CricketPackage packItem : packageItem) { 
			List<CricketCommerceItemImpl> commerceItemswithUniquePackages = getOrderTools().getCommerceItemswithUniquePackageId(packItem.getId(), order);
			if (null != commerceItemswithUniquePackages) {
				for (CricketCommerceItemImpl commerceItem : commerceItemswithUniquePackages) {
					productType = (String) commerceItem.getCricItemTypes(); 
					if ((commerceItemswithUniquePackages.size() >= 2) && ((CricketCookieConstants.ADD_ON_PRODUCT.equalsIgnoreCase(productType)) || (CricketCookieConstants.PLAN_PRODUCT.equalsIgnoreCase(productType)) || (CricketCookieConstants.PHONE_PRODUCT.equalsIgnoreCase(productType)))) { 
						showCheckOutButton=true;
					} else{
						showCheckOutButton=false;
						if(CricketCookieConstants.PHONE_PRODUCT.equalsIgnoreCase(productType)){
							setToolTipMessage("Almost there. Please choose a plan to continue.");
						}
						if(CricketCookieConstants.PLAN_PRODUCT.equalsIgnoreCase(productType)){
							setToolTipMessage("Almost there. Please choose a phone to continue.");
						}
						if(CricketCookieConstants.ADD_ON_PRODUCT.equalsIgnoreCase(productType)){
							setToolTipMessage("Almost there. Please select a phone and plan to complete your package.");
						}						
						pRequest.setParameter(CricketCookieConstants.SHOW_CHECK_OUT_BUTTON, showCheckOutButton);
						pRequest.setParameter(CricketCookieConstants.TOOL_TIP_MESSAGE, getToolTipMessage());
						pRequest.serviceLocalParameter(OPARAM_OUTPUT, pRequest,pResponse);
						return;
					}
				}
				
			}
		} 
		if(packageItem.isEmpty()) {
	
			for (CricketCommerceItemImpl packItem : commerceItems) {
				String cricItemTypes = (String) packItem.getCricItemTypes();
				if(!StringUtils.isEmpty(cricItemTypes) && cricItemTypes.equalsIgnoreCase(CricketCookieConstants.ACCESSORY_PRODUCT)){
					showCheckOutButton=true;
				}
			}
		}
		
		if(showCheckOutButton!=true){
			setToolTipMessage("Almost there. Please select a phone and plan to complete your package.");
		}
		pRequest.setParameter(CricketCookieConstants.SHOW_CHECK_OUT_BUTTON, showCheckOutButton);
		pRequest.setParameter(CricketCookieConstants.TOOL_TIP_MESSAGE, getToolTipMessage());
		pRequest.serviceLocalParameter(OPARAM_OUTPUT, pRequest,pResponse);
		if(isLoggingDebug()){
			logDebug("[DisplayCheckoutButtonDroplet->service()]: Exiting service()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId );
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

	
	/**
	 * @return the sessionBeanPath
	 */
	public String getProfilePath() {
		return mProfilePath;
	}

	/**
	 * @param pSessionBeanPath the sessionBeanPath to set
	 */
	public void setProfilePath(String pProfilePath) {
		mProfilePath = pProfilePath;
		if (mProfilePath != null) {
			mProfileComponentName = ComponentName.getComponentName(mProfilePath);
		} else {
			mProfileComponentName = null;
		}
	}

	public String getToolTipMessage() {
		return toolTipMessage;
	}

	public void setToolTipMessage(String pToolTipMessage) {
		toolTipMessage = pToolTipMessage;
	}
	
	
}
