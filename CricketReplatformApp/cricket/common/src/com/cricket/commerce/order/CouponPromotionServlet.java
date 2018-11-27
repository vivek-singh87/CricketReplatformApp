package com.cricket.commerce.order;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.user.session.UserSessionBean;

import atg.commerce.order.OrderHolder;
import atg.core.util.StringUtils;
import atg.nucleus.naming.ComponentName;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;
import atg.servlet.pipeline.InsertableServletImpl;

public class CouponPromotionServlet extends InsertableServletImpl {
	
	boolean mEnabled;
	
	private String mCouponParameter;
	
	private ComponentName mSessionBeanComponentName = null;
	
	private String mSessionBeanPath;

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return mEnabled;
	}

	/**
	 * @param pEnabled the enabled to set
	 */
	public void setEnabled(boolean pEnabled) {
		mEnabled = pEnabled;
	}


	/**
	 * @return the mCouponParameter
	 */
	public String getCouponParameter() {
		return mCouponParameter;
	}

	/**
	 * @param mCouponParameter the mCouponParameter to set
	 */
	public void setCouponParameter(String pCouponParameter) {
		this.mCouponParameter = pCouponParameter;
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
	 * Service method
	 */
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws IOException,
			ServletException {
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }		
	    		logDebug("Entering into CouponPromotionServlet class of service() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		String specialURLCouponCode = pRequest.getQueryParameter(getCouponParameter());
		if (isEnabled() && !StringUtils.isEmpty(specialURLCouponCode)) {
			UserSessionBean userSessionBean = (UserSessionBean) pRequest
					.resolveName(mSessionBeanComponentName);
			userSessionBean.setSpecialURLCouponCode(specialURLCouponCode);
			if (isLoggingDebug()) {
				logDebug("Special URL Coupon code is  : " + specialURLCouponCode);
			}
			pResponse.sendRedirect(pRequest
					.getRequestURI());
		}
		
		super.service(pRequest, pResponse);
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }		
	    		logDebug("Exiting from CouponPromotionServlet class of service() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
}
