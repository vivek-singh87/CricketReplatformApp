package com.cricket.seo;

import java.io.IOException;

import javax.servlet.ServletException;

import atg.repository.seo.JumpServlet;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.util.CricketUtils;

import de.ailis.pherialize.Mixed;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import atg.commerce.order.OrderHolder;
import atg.core.util.StringUtils;

/**
 * 
 * @author 
 *
 */
public class CricketJumpServlet extends JumpServlet {
	
	private boolean mEnabled;
	
	private CricketUtils cricketUtils;
	/**
	 * 
	 */
	public void service(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse)
		    throws ServletException, IOException{
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				}						
	    		logDebug("Entering into CricketJumpServlet class of service() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		if ( pRequest.getRequestURI().contains(CricketCookieConstants.UPGRADE_PHONE)  || pRequest.getRequestURI().contains(CricketCookieConstants.UPGRADE_PLAN) || pRequest.getRequestURI().contains(CricketCookieConstants.UPGRADE_FEATURE) ||
				pRequest.getRequestURI().contains("addLine") ) {
		
			if(isLoggingDebug()){
				logDebug(" Start to Fectch Cookie Information from Account system");
			}
			Mixed decriptedCookieInfo = null;
			Mixed decriptedLocationInfo = null;
			//CricketUtils cricketUtils = (CricketUtils) pRequest.resolveName("/atg/cricket/util/CricketUtils");
			if(isLoggingDebug()){
				logDebug("isEnabled = "+isEnabled());
			}
			if (isEnabled() && StringUtils.isEmpty((String) pRequest.getParameter(CricketCookieConstants.LOGOUT_REQUEST))) {
				Cookie[] cookies = pRequest.getCookies();
				pRequest.getCookies();
				if(isLoggingDebug()){
					logDebug("cookies = "+cookies);
				}				
				if (cookies != null && cookies.length > 0) {
					if(isLoggingDebug()){
						logDebug("cookies.length = "+cookies.length);
					}	
					
					for (int i = 0; i < cookies.length; i++) {
						Cookie cookie = cookies[i];
						if(isLoggingDebug()){
							logDebug("cookie.getName() = "+cookie.getName());
						}	
						
						if (CricketCookieConstants.CRICKET.equalsIgnoreCase(cookie.getName())) {
							decriptedCookieInfo = getCricketUtils().getDecriptedCookieInfo(cookie,pRequest);
							/*cookie.setValue(CricketCommonConstants.EMPTY_STRING);
							cookie.setMaxAge(0);
							pResponse.addCookie(cookie);*/
						}
						if (CricketCookieConstants.LOCATION_INFO.equalsIgnoreCase(cookie.getName())) {
							decriptedLocationInfo = getCricketUtils().getDecriptedCookieInfo(cookie,pRequest);
							/*cookie.setValue(CricketCommonConstants.EMPTY_STRING);
							cookie.setMaxAge(0);
							pResponse.addCookie(cookie);*/
						}
						
					}
				}
				
				
			} 
		
		}
		
		super.service(pRequest, pResponse);
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				}	
	    		logDebug("Exiting from CricketJumpServlet class of service() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.PAGE_URL + pageURL);
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
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return mEnabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean pEnabled) {
		this.mEnabled = pEnabled;
	}
	
	/**
	 * @return the cricketUtils
	 */
	public CricketUtils getCricketUtils() {
		return cricketUtils;
	}

	/**
	 * @param cricketUtils the cricketUtils to set
	 */
	public void setCricketUtils(CricketUtils cricketUtils) {
		this.cricketUtils = cricketUtils;
	}
	
	
	
}