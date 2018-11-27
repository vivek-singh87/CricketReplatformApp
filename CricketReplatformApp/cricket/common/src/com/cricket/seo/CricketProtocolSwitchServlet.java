package com.cricket.seo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import atg.core.util.StringUtils;
import atg.projects.store.servlet.pipeline.ProtocolSwitchServlet;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;

import com.cricket.common.constants.CricketCommonConstants;

/**
 * 
 * @author 
 *
 */
public class CricketProtocolSwitchServlet extends ProtocolSwitchServlet {
	
	/**
	 * 
	 */
	public void service(DynamoHttpServletRequest cricRequest, DynamoHttpServletResponse cricResponse)
		    throws ServletException, IOException{
		
		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(cricRequest.getRequestURIWithQueryString())){
			pageURL = cricRequest.getRequestURIWithQueryString();
		}		 
		if (isLoggingDebug()) {									
	    		logDebug("Entering into CricketProtocolSwitchServlet class of service() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		if (mEnabled) {
 		      String pathWithoutQueryString = cricRequest.getRequestURI();
 		      String path = cricRequest.getRequestURIWithQueryString();

		      if (isLoggingDebug()) {
		        logDebug("now doing protocal switch " + path);
		      }
 		      if (isCurrentPathInIgnoreList(pathWithoutQueryString)) {
		        if (isLoggingDebug()) {
		          logDebug("cricket - Ignoring " + path);
		        }
		        passRequest(cricRequest, cricResponse);
		      } else {
 		        boolean uriRequireSecure = isCurrentPathInSecureList(pathWithoutQueryString);
 		        boolean isSchemeSecure = false;

		        if (isLoggingDebug()) {
		          logDebug("Cricket Scheme is " + cricRequest.getScheme());
		        }

		        if ((cricRequest.getScheme() != null) && cricRequest.getScheme().equalsIgnoreCase(SECURE_PROTOCOL)) {
		          isSchemeSecure = true;
		        }
		        if (mEnabled && uriRequireSecure && !isSchemeSecure) {
		          // get the URL to the secure server
		          String redirectURL = getSecureUrl(path);

		          if (isLoggingDebug()) {
		            logDebug("Page is secure but is on non-secure server");
		            logDebug("Redirecting to: " + redirectURL);
		            logDebug("Encoded URL: " + cricResponse.encodeRedirectURL(redirectURL));
		          } 
		          cricResponse.sendRedirect(cricResponse.encodeRedirectURL(redirectURL));
		        } 
		        else if (!uriRequireSecure && isSchemeSecure) {
 		          String redirectURL = getNonSecureUrl(path);

		          if (isLoggingDebug()) {
		            logDebug("CricketProtocalSwitchServlet - Page is non-secure but is on secure server");
		            logDebug("CricketProtocalSwitchServlet - Redirecting to: " + redirectURL);
		            logDebug("CricketProtocalSwitchServlet - Encoded URL: " + cricResponse.encodeRedirectURL(redirectURL));
		          }
		          cricResponse.sendRedirect(cricResponse.encodeRedirectURL(redirectURL));
		        } else {
		          if (isLoggingDebug()) {
		            logDebug("CricketProtocalSwitchServlet - protocal - No need to change");
		          }
		          passRequest(cricRequest, cricResponse);
		        }
		      }
		    } else {
		      passRequest(cricRequest, cricResponse);
		    }
		
		if (isLoggingDebug()) {									
	    		logDebug("Exiting from CricketProtocolSwitchServlet class of service() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.PAGE_URL + pageURL);
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
}