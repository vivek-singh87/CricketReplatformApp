package atg.servlet.pipeline;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import atg.core.util.StringUtils;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;
import atg.userprofiling.Profile;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.userprofiling.CricketProfileServices;
import com.cricket.util.CricketUtils;
import com.cricket.vo.CricketProfile;

import de.ailis.pherialize.Mixed;

public class SingleSignOnServlet extends InsertableServletImpl {
	
	private boolean enabled;
	private CricketUtils cricketUtils;
	private CricketProfileServices profileServices;
	private String profileComponentPath;
	private boolean redirectEnabled;
	private String redirectURL;

	@Override
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws IOException,
			ServletException {
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				}
	    		logDebug("Entering into SingleSignOnServlet class of service() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() +  CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		Mixed decriptedCookieInfo = null;
		Mixed decriptedLocationInfo = null;
		String cricketCookieValue = null;
		boolean cricketCookieExists = Boolean.FALSE;
		Cookie cricketCookieObject = null;
		Cookie LocationCookieObject = null;
		//CricketUtils cricketUtils = (CricketUtils) pRequest.resolveName("/atg/cricket/util/CricketUtils");
		CricketProfile cricketProfile = (CricketProfile) pRequest.resolveName(CricketCookieConstants.ATG_CRICKET_UTIL_CRICKET_PROFILE);
		if (isEnabled() && StringUtils.isEmpty((String) pRequest.getParameter(CricketCookieConstants.LOGOUT_REQUEST))) {
			Cookie[] cookies = pRequest.getCookies();
			pRequest.getCookies();
			if (cookies != null && cookies.length > 0) {
				List<Cookie> lList = Arrays.asList(cookies);
				for (Cookie cookieList : lList) {
					String cookieName = cookieList.getName();
					if(cookieName.equalsIgnoreCase(CricketCookieConstants.CRICKET)){
						if(isLoggingDebug()){
							logDebug("cookie.getName() = "+cookieName);
						}
						cricketCookieValue = cookieList.getValue();
						cricketCookieExists = Boolean.TRUE;
						cricketCookieObject = cookieList;
					} else if (CricketCookieConstants.LOCATION_INFO.equalsIgnoreCase(cookieName)) {
						if(isLoggingDebug()){
							logDebug("cookie.getName() = "+cookieName);
						}
						LocationCookieObject = cookieList;
					}
					else if(CricketCookieConstants.MP_LANG_COOKIE.equalsIgnoreCase(cookieName))
					{	
						cricketProfile.setLanguageIdentifier(cookieList.getValue());
					}
					 
				}

				//for (int i = 0; i < cookies.length; i++) {
					//Cookie cookie = cookies[i];
				if (cricketCookieExists
						&& null != cricketCookieObject
						&& !StringUtils.isEmpty(cricketCookieValue)
						&& !cricketCookieValue.equals(cricketProfile
								.getEncryptedCricketCookieValue())) {
					if (CricketCookieConstants.CRICKET.equalsIgnoreCase(cricketCookieObject.getName())) {
						if (isLoggingDebug()) {
							logDebug("cookie.getName()   = "+ cricketCookieObject.getName());
							logDebug("cookie.getDomain() = "+ cricketCookieObject.getDomain());
							logDebug("cookie.getMaxAge() = "+ cricketCookieObject.getMaxAge());
							logDebug("cookie.getPath()   = "+ cricketCookieObject.getPath());
						}
						cricketProfile.setEncryptedCricketCookieValue(cricketCookieValue);
						decriptedCookieInfo = getCricketUtils().getDecriptedCookieInfo(cricketCookieObject,pRequest);
					}
					
				}
				if (null != LocationCookieObject) {
					if (CricketCookieConstants.LOCATION_INFO.equalsIgnoreCase(LocationCookieObject.getName())) {
						if(isLoggingDebug()){	
							  logDebug("cookie.getName()   = "+LocationCookieObject.getName());	
							  logDebug("cookie.getDomain() = "+LocationCookieObject.getDomain());	
							  logDebug("cookie.getMaxAge() = "+LocationCookieObject.getMaxAge());	
							  logDebug("cookie.getPath()   = "+LocationCookieObject.getPath() );	
						  }
						decriptedLocationInfo = getCricketUtils().getDecriptedCookieInfo(LocationCookieObject,pRequest);
					}
				}
			}
			if (!cricketCookieExists) {
				Profile profile = (Profile) pRequest
						.resolveName(getProfileComponentPath());
				if (null != profile && !profile.isTransient()) {
					getProfileServices().logoutUser();
					if(isRedirectEnabled() ){
						pResponse.sendRedirect(getRedirectURL());
					}
				}
			
			}
			if (decriptedCookieInfo != null|| decriptedLocationInfo != null) {
				if(isLoggingDebug()){
					logDebug("Passing request to Next Servlet after Decrypting the Cookie");
				}
				passRequest(pRequest, pResponse);
			} else {
				passRequest(pRequest, pResponse);
			}
			
		} else {
			if(isLoggingDebug()){
				logDebug("Passing request to Next Servlet with out entering to decrypt");
			}
			passRequest(pRequest, pResponse);
		}
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				}
	    		logDebug("Exiting from SingleSignOnServlet class of service() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() +  CricketCommonConstants.PAGE_URL + pageURL);
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
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	/**
	 * @return the profileServices
	 */
	public CricketProfileServices getProfileServices() {
		return profileServices;
	}

	/**
	 * @param mProfileServices the profileServices to set
	 */
	public void setProfileServices(CricketProfileServices mProfileServices) {
		profileServices = mProfileServices;
	}

	/**
	 * @return the profileComponentPath
	 */
	public String getProfileComponentPath() {
		return profileComponentPath;
	}

	/**
	 * @param mProfileComponentPath the profileComponentPath to set
	 */
	public void setProfileComponentPath(String mProfileComponentPath) {
		profileComponentPath = mProfileComponentPath;
	}

	/**
	 * @return the redirectEnabled
	 */
	public boolean isRedirectEnabled() {
		return redirectEnabled;
	}

	/**
	 * @param mRedirectEnabled the redirectEnabled to set
	 */
	public void setRedirectEnabled(boolean mRedirectEnabled) {
		redirectEnabled = mRedirectEnabled;
	}

	/**
	 * @return the redirectURL
	 */
	public String getRedirectURL() {
		return redirectURL;
	}

	/**
	 * @param mRedirectURL the redirectURL to set
	 */
	public void setRedirectURL(String mRedirectURL) {
		redirectURL = mRedirectURL;
	}

	
}
