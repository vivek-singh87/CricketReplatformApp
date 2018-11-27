package com.cricket.userprofiling;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.integration.maxmind.CitySessionInfoObject;
import com.cricket.myaccount.CricketProfileTools;

import atg.commerce.order.OrderHolder;
import atg.core.util.StringUtils;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileRequest;
import atg.userprofiling.ProfileServices;

public class CricketProfileServices extends ProfileServices {
	
	private static final String FORWARDSLASH = "/";
	
	@Override
	protected void postLogoutUser(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException {
		super.postLogoutUser(pRequest, pResponse);

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
			 // Getting the Order Id
			 OrderHolder cart = (OrderHolder) pRequest.resolveName("/atg/commerce/ShoppingCart");
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(null != cart){
					if(null != cart.getCurrent()){
						if(!StringUtils.isBlank(cart.getCurrent().getId())){
							orderId = cart.getCurrent().getId();
						}
					}
				}			
				logDebug("Entering in to CricketProfileServices class of postLogoutUser() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		Cookie[] cookies = pRequest.getCookies();
		
		CitySessionInfoObject citySessionInfoObject = (CitySessionInfoObject) pRequest.resolveName("/com/cricket/integration/maxmind/CitySessionInfoObject");
		citySessionInfoObject.setLoggedIn(false);
		pRequest.setParameter(CricketCookieConstants.LOGOUT_REQUEST, CricketCommonConstants.TRUE);
		if(cookies!=null && cookies.length>0){
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (CricketCookieConstants.CRICKET.equalsIgnoreCase(cookie.getName())) {
					cookie.setDomain(((CricketProfileTools) getProfileTools()).getDomainName());
					cookie.setPath(FORWARDSLASH);
					cookie.setMaxAge(0);
					if (isLoggingDebug()) {
						logDebug("After Resetting in Cricket Cookie From CricketProfileFormHandler");
						logDebug("cookie.getName()   = " + cookie.getName());
						logDebug("cookie.getDomain() = " + cookie.getDomain());
						logDebug("cookie.getMaxAge() = " + cookie.getMaxAge());
						logDebug("cookie.getPath()   = " + cookie.getPath());
					}
					pResponse.addCookie(cookie);
				}
				if (CricketCookieConstants.LOCATION_INFO.equalsIgnoreCase(cookie.getName())) {
					/*cookie.setDomain(((CricketProfileTools) getProfileTools()).getDomainName());
		            cookie.setPath(FORWARDSLASH);
		            cookie.setMaxAge(0);*/
		            if(isLoggingDebug()){
						  logDebug("After Resetting in Cricket Cookie From CricketProfileFormHandler");
						  logDebug("cookie.getName()   = "+cookie.getName());	
						  logDebug("cookie.getDomain() = "+cookie.getDomain());	
						  logDebug("cookie.getMaxAge() = "+cookie.getMaxAge());	
						  logDebug("cookie.getPath()   = "+cookie.getPath() );	
					  }
					/*pResponse.addCookie(cookie);*/
				}
				if(CricketCommonConstants.ORDER_PERSISTENT_COOKIE.equalsIgnoreCase(cookie.getName())){
					cookie.setPath("/");
					cookie.setMaxAge(0);
					cookie.setValue(null);
					pResponse.addCookie(cookie);
				}
			}
		}
		ProfileRequest profileRequest = (ProfileRequest) pRequest.resolveName("/atg/userprofiling/ProfileRequest");
		Profile profile = (Profile) pRequest.resolveName("/atg/userprofiling/Profile");
		getProfileTools().createNewUser("user", profile);
		ServletUtil.setCurrentUserProfile(profile);
		profileRequest.setRequestInvalid(true);
		profileRequest.setExtractProfileFromBasicAuthentication(false);
		profileRequest.setExtractProfileFromCookieParameter(false);
		profileRequest.setExtractProfileFromPostParameter(false);
		profileRequest.setExtractProfileFromQueryParameter(false);
		profileRequest.setExtractProfileFromURLParameter(false);
		
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
			 // Getting the Order Id
			 OrderHolder cart = (OrderHolder) pRequest.resolveName("/atg/commerce/ShoppingCart");
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(null != cart){
					if(null != cart.getCurrent()){
						if(!StringUtils.isBlank(cart.getCurrent().getId())){
							orderId = cart.getCurrent().getId();
						}
					}
				}			
				logDebug("Exiting from CricketProfileServices class of postLogoutUser() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}

}
