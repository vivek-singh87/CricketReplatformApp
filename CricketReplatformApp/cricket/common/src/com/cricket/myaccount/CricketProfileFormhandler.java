package com.cricket.myaccount;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import atg.commerce.order.OrderHolder;
import atg.core.util.StringUtils;
import atg.droplet.DropletException;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.projects.b2cstore.B2CProfileFormHandler;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.security.IdentityManager;
import atg.security.SecurityException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileRequestServlet;
import atg.userprofiling.ProfileTools;
import atg.userprofiling.PropertyManager;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.InquireAccountResponseVO;
import com.cricket.integration.maxmind.CitySessionInfoObject;
import com.cricket.user.session.UserAccountInformation;
import com.cricket.user.session.UserSessionBean;
import com.cricket.vo.CricketProfile;
import com.cricket.vo.MyCricketCookieLocationInfo;
/**
 * 
 * This calss extends CommerceProfileFormHandler
 * 
 * @author AK112151
 *
 */

public class CricketProfileFormhandler extends B2CProfileFormHandler {
	
	/**
	 * constant for property isUserLoggedIn
	 */
	private static final String IS_USER_LOGGED_IN = "isUserLoggedIn";
	/**
	 * constant for String login=?0
	 */
	private static final String LOGIN_0 = "login=?0";
	/**
	 * constant for String user
	 */
	private static final String USER = "user";
	
	private CricketProfile cricketProfile;
	private CricketESPAdapter cricketEspAdapter;
	private CricketAccountHolderAddressData accountHolderAddressData;
	private UserAccountInformation userAccountInformation; 
	private UserSessionBean userSessionBean; 
	private static final String FORWARDSLASH = "/";
	private MyCricketCookieLocationInfo loggedInUserLocationInfo;
	
	 
	@Override
	public boolean handleLogin(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		if(isLoggingDebug()){
			logDebug("Entering in to Handle Login in CricketProfile Form Handler");
		}
		boolean handleLogin = super.handleLogin(pRequest, pResponse);
		if(isLoggingDebug()){
			logDebug("Exiting Handle Login in CricketProfile Form Handler");
		}
				return handleLogin;
	}
	@Override
	/**
	 * 
	 * This method executes after the handleLogin(), and it will update the transient property
	 * isUserLoggedIn to true to check whether the user is logged in or not. 
	 * 
	 */
	protected void postLoginUser(final DynamoHttpServletRequest pRequest,
			final DynamoHttpServletResponse pResponse) throws ServletException,IOException {
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				
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
		logDebug("Entering into CricketProfileFormhandler class of postLoginUser() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
	}
		PropertyManager propertyManager = null;
		String loginPropertyName = null;
		String login = null;
		MutableRepository profileRepository = null;
		MutableRepositoryItem userItem = null;
		RepositoryView view = null;
		RepositoryItem[] items = null;
		RqlStatement statement = null;
		try {
			propertyManager = getProfileTools().getPropertyManager();
			loginPropertyName = propertyManager.getLoginPropertyName();
			login = getStringValueProperty(loginPropertyName).trim().toLowerCase();
			profileRepository = getProfileTools().getProfileRepository();
			Object[] objects = new Object[1];
			objects[0] = login;
			view = profileRepository.getView(USER);
			statement = RqlStatement.parseRqlStatement(LOGIN_0);
			items = statement.executeQuery(view, objects);
			for (RepositoryItem repositoryItem : items) {
				userItem = (MutableRepositoryItem) repositoryItem;
				userItem.setPropertyValue(IS_USER_LOGGED_IN, true);
			}
			getUserSessionBean().setAutoBillPayment((Boolean) items[0].getPropertyValue(CricketCookieConstants.ABP_FLAG));
			if(isLoggingDebug()){
				logDebug("Calling getCricketEspAdapter().inquireAccount in Post Login of CricketProfile Form Handler");
				logDebug("Post Login of CricketProfile Form Handler- logged in user BillingAcctNumber[should not be null]: "+
						getCricketProfile().getAccountNumber());
			}
			
			InquireAccountResponseVO inquireAccount =null;
			if(null!= getCricketProfile().getAccountNumber()){
			    inquireAccount = getCricketEspAdapter().inquireAccount(getCricketProfile().getAccountNumber(),getShoppingCart().getCurrent().getId());//"90003086101");//);
			}else{
				logError("[CricketProfileFormhandler->postLoginUser()]: AccountNumber is null...please check whether cookies are set/received properly");
			}
			if (null != inquireAccount && inquireAccount.getBillingAccountNumber() != null && inquireAccount.getBillingAccountNumber().length() > 0) {
				setInquireAccountValuesToSessionScope(inquireAccount);
			}else{
				CitySessionInfoObject citySessionInfoObject = (CitySessionInfoObject) pRequest.resolveName("/com/cricket/integration/maxmind/CitySessionInfoObject");
				citySessionInfoObject.setInquireAccountTimeOut(true);
				logError("[CricketProfileFormhandler->postLoginUser()]: inquireAccount is null...some functionalities may not work properly");
			}
			// making this to null to invoke drm values with logged in user zip code.
 			 getProfile().setPropertyValue(CricketCommonConstants.PROP_MARKET_ID,null);
 			 if (null!= getLoggedInUserLocationInfo() && getLoggedInUserLocationInfo().getZip() != null ) {
 				getProfile().setPropertyValue(CricketCommonConstants.MANUALLY_ENTERED_ZIP, true);	
 			 }
 			OrderHolder cart = (OrderHolder) pRequest.resolveName("/atg/commerce/ShoppingCart");
 			int commerceItemCount = cart.getCurrent().getCommerceItemCount();
			if(commerceItemCount > 0){
				String orderId =  cart.getCurrent().getId();
				cart.deleteOrder(orderId);
			}
			// TODO Auto-generated method stub
			
		} catch (RepositoryException e) {
			logError("Error while Getting storelocator vo  :" + e);
		} catch (CricketException e) {
			if (isLoggingError()) {
				logError("CricketException", e);
			}
		}
		super.postLoginUser(pRequest, pResponse);
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
		logDebug("Exiting from CricketProfileFormhandler class of postLoginUser() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
	}
	}
	
	private void setInquireAccountValuesToSessionScope(InquireAccountResponseVO inquireAccount) {
		if(isLoggingDebug()){
			logDebug("Entering in to  setInquireAccountValuesToSessionScope of CricketProfile Form Handler");
		}
		if(null != inquireAccount.getMarketAddress()){
			if(null != inquireAccount.getFirstName() ){
				getAccountHolderAddressData().getAccountAddress().setFirstName(inquireAccount.getFirstName());
			}
			if(null != inquireAccount.getLastName()){
				getAccountHolderAddressData().getAccountAddress().setLastName(inquireAccount.getLastName());
			}
			if(null != inquireAccount.getEmailAddress()){
				getAccountHolderAddressData().getAccountAddress().setEmail(inquireAccount.getEmailAddress());
			}
			if(null != inquireAccount.getMarketAddress().getAddressLine1()){
				getAccountHolderAddressData().getAccountAddress().setAddress1(inquireAccount.getMarketAddress().getAddressLine1());
			}
			if(null != inquireAccount.getMarketAddress().getAddressLine2()){
				getAccountHolderAddressData().getAccountAddress().setAddress2(inquireAccount.getMarketAddress().getAddressLine2());
			}
			/*if(null != inquireAccount.getMarketAddress().getAddressLine2() && null != inquireAccount.getMarketAddress().getPostOfficeBox()){
				getAccountHolderAddressData().getAccountAddress().setAddress1(inquireAccount.getMarketAddress().getAddressLine2()+inquireAccount.getMarketAddress().getPostOfficeBox());
			} else if(null != inquireAccount.getMarketAddress().getPostOfficeBox()){
				getAccountHolderAddressData().getAccountAddress().setAddress1(inquireAccount.getMarketAddress().getPostOfficeBox());
			} else if(null != inquireAccount.getMarketAddress().getAddressLine2()){
				getAccountHolderAddressData().getAccountAddress().setAddress1(inquireAccount.getMarketAddress().getAddressLine2());
			}*/
			if(null != inquireAccount.getMarketAddress().getAddressLine3()){
				getAccountHolderAddressData().getAccountAddress().setAddress3(inquireAccount.getMarketAddress().getAddressLine3());
			}
			if(null != inquireAccount.getMarketAddress().getCity()){
				getAccountHolderAddressData().getAccountAddress().setCity(inquireAccount.getMarketAddress().getCity());
			}
			if(null != inquireAccount.getMarketAddress().getCountry()){
				getAccountHolderAddressData().getAccountAddress().setCountry(inquireAccount.getMarketAddress().getCountry());	
			}
			if(null != inquireAccount.getMarketAddress().getState()){
				getAccountHolderAddressData().getAccountAddress().setStateAddress(inquireAccount.getMarketAddress().getState());
			}
			//getAccountHolderAddressData().getAccountAddress().setMiddleName(inquireAccount.getMarketAddress().getM);
			if(null != inquireAccount.getMarketAddress().getZipCode()){
				getAccountHolderAddressData().getAccountAddress().setPostalCode(inquireAccount.getMarketAddress().getZipCode());
			}
			if(null != getCricketProfile().getUserId()){
				getAccountHolderAddressData().getAccountAddress().setPhoneNumber(getCricketProfile().getUserId());
			}
			
			if(null != inquireAccount.getDob()){
				getAccountHolderAddressData().setYear(String.valueOf(inquireAccount.getDob().getYear()));
				getAccountHolderAddressData().setMonth(String.valueOf(inquireAccount.getDob().getMonth()));
				getAccountHolderAddressData().setDay(String.valueOf(inquireAccount.getDob().getDate()));
			}
			
			if(null != inquireAccount.getSolicitationContactPreference() && CricketESPConstants.CONTACT_PREFERENCE_SMS_EMAIL.equalsIgnoreCase(inquireAccount.getSolicitationContactPreference())){
				getAccountHolderAddressData().setEmailnotification(true);
			}
			if(null != inquireAccount.getSocialSecurityNumber()){
				getAccountHolderAddressData().setSocialSecurityNumber(inquireAccount.getSocialSecurityNumber());
			}
			
		}
		
		getUserAccountInformation().setBillingAccountNumber(inquireAccount.getBillingAccountNumber());
		getUserAccountInformation().setAccountType(inquireAccount.getAccountType());
		getUserAccountInformation().setAccountStatus(inquireAccount.getAccountStatus());
		getUserAccountInformation().setMarketId(inquireAccount.getMarketId());
		getUserAccountInformation().setJointVentureCode(inquireAccount.getJointVentureCode());
		getUserAccountInformation().setRateCenterId(inquireAccount.getRateCenterId());
		getUserAccountInformation().setBillingCycleDate(inquireAccount.getBillingCycleDate());
		getUserAccountInformation().setBillingPreferencesLanguage(inquireAccount.getBillingPreferencesLanguage());
		getUserAccountInformation().setBillingPreferencesABP(inquireAccount.isBillingPreferencesABP());
		getUserAccountInformation().setSolicitationContactPreference(inquireAccount.getSolicitationContactPreference());
		getUserAccountInformation().setCustomerId(inquireAccount.getCustomerId());
		getUserAccountInformation().setCustomerType(inquireAccount.getCustomerType());
		getUserAccountInformation().setFirstName(inquireAccount.getFirstName());
		getUserAccountInformation().setLastName(inquireAccount.getLastName());
		getUserAccountInformation().setEmailAddress(inquireAccount.getEmailAddress());
		getUserAccountInformation().setPhone(inquireAccount.getPhone());
		getUserAccountInformation().setDob(inquireAccount.getDob());
		getUserAccountInformation().setSubscribers(inquireAccount.getSubscribers());
		getUserAccountInformation().setResponse(inquireAccount.getResponse());
		getUserAccountInformation().setMarketAddress(inquireAccount.getMarketAddress());
		getUserAccountInformation().setBillingAddress(inquireAccount.getBillingAddress());
		//setting - used in createActivationQuoteRequest
		getCricketProfile().setCustomerId(inquireAccount.getCustomerId());
		getCricketProfile().setEspCustomerType(inquireAccount.getCustomerType());
		getCricketProfile().setAccountType(inquireAccount.getAccountType());
		if(null!=inquireAccount.getBillingCycleDate() && (inquireAccount.getBillingCycleDate()).getTime() !=null ){
			getCricketProfile().setBillCycleDay((inquireAccount.getBillingCycleDate()).getTime().getDay());
		}
		//getProfile().setPropertyValue("accountType", inquireAccount.getAccountType());
		//getProfile().setPropertyValue("customerType", inquireAccount.getCustomerType());
		//getProfile().setPropertyValue("customerNumber", inquireAccount.getCustomerId());
		if(isLoggingDebug()){
			logDebug("Exiting setInquireAccountValuesToSessionScope of CricketProfile Form Handler");
		}
	}
	@Override
	protected void preLoginUser(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		
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
		logDebug("Entering into CricketProfileFormhandler class of preLoginUser() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
	}
		Cookie[] cookies = pRequest.getCookies();		
		if(cookies!=null && cookies.length>0){
			for (Cookie cookie : cookies) {
				if("sharedLocationCookie".equalsIgnoreCase(cookie.getName())){
					cookie.setPath("/");
					cookie.setMaxAge(0);
					cookie.setValue(null);
					pResponse.addCookie(cookie);
				}
				if (cookie.getName().equals(CricketCommonConstants.ORDER_PERSISTENT_COOKIE)) {
					cookie.setPath(FORWARDSLASH);
					cookie.setMaxAge(0);
					cookie.setValue(null);
					pResponse.addCookie(cookie);
				}
			}
		}
/*		OrderHolder cart = (OrderHolder) pRequest
				.resolveName("/atg/commerce/ShoppingCart");
		String orderId = cart.getCurrent().getId();
		cart.deleteOrder(orderId);*/
		Profile profile = getProfile();
		ProfileRequestServlet prs = (ProfileRequestServlet) pRequest
				.resolveName("/atg/userprofiling/ProfileRequestServlet", false);
		if (profile.isTransient() || prs == null
				|| prs.isPersistentAnonymousProfiles()) {
			return;
		}
		ProfileTools prTools = getProfileTools();
		PropertyManager pmgr = prTools.getPropertyManager();
		String loginPropertyName = pmgr.getLoginPropertyName();
		String login = getStringValueProperty(loginPropertyName);
		if (login != null)
			login = login.trim();
		String pwdPropertyName = pmgr.getPasswordPropertyName();
		String pwd = getStringValueProperty(pwdPropertyName);
		if (pwd != null)
			pwd = pwd.trim();
		Object prLoginObj = profile.getPropertyValue(loginPropertyName);
		String prLogin = null;
		if (prLoginObj != null)
			prLogin = prLoginObj.toString();
		IdentityManager identityManager = mUserLoginManager
				.getIdentityManager(pRequest);
		boolean authSuccessful;
		try {
			authSuccessful = identityManager.checkAuthenticationByPassword(
					login, pwd);
			authSuccessful=true;
		} catch (SecurityException e) {
			authSuccessful = true;
		}
		if (authSuccessful && login.equals(prLogin)) {
			boolean addError = true;
			if (prTools.isEnableSecurityStatus()) {
				int securityStatus = -1;
				String securityStatusPropertyName = pmgr
						.getSecurityStatusPropertyName();
				Object ss = profile
						.getPropertyValue(securityStatusPropertyName);
				if (ss != null)
					securityStatus = ((Integer) ss).intValue();
				addError = !getProfileTools().isAutoLoginSecurityStatus(
						securityStatus);
				try {
					if (securityStatus != pmgr.getSecurityStatusLogin())
						prTools.setLoginSecurityStatus(profile, pRequest);
				} catch (RepositoryException exc) {
					/*
					 * addFormException(new DropletException( sResourceBundle
					 * .getString("errorUpdatingSecurityStatus")));
					 */
					if (isLoggingError())
						logError(exc);
				}
			}
			if (addError) {
			}
			/* break MISSING_BLOCK_LABEL_505; */return;
		}
		javax.transaction.TransactionManager tm = getTransactionManager();
		TransactionDemarcation td = getTransactionDemarcation();
		boolean expire;
		DropletException dropletExc;
		try {
			if (tm != null)
				td.begin(tm, 3);
			expire = getExpireSessionOnLogout();
			setExpireSessionOnLogout(false);
			preLogoutUser(pRequest, pResponse);
			int status = checkFormError(null, pRequest, pResponse);
			if (status != 0)
				return;
		} catch (TransactionDemarcationException e) {
			throw new ServletException(e);
		} finally {
			try {
				if (tm != null)
					td.end();
			} catch (TransactionDemarcationException e) {
			}
		}
		postLogoutUser(pRequest, pResponse);
		setExpireSessionOnLogout(expire);
		/*
		 * msg = MessageFormat.format(
		 * sResourceBundle.getString("invalidLoginDifferentUser"), new Object[]
		 * { (Object) null });
		 */
		dropletExc = new DropletException(CricketCommonConstants.EMPTY_STRING);
		if (isLoggingError())
			logError(CricketCommonConstants.EMPTY_STRING);
		addFormException(dropletExc);
		
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
		logDebug("Exiting from CricketProfileFormhandler class of preLoginUser() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
	}
	}

	@Override
	protected RepositoryItem findUser(String pLogin, String pPassword,
			Repository pProfileRepository, DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws RepositoryException,
			ServletException, IOException {
		if(isLoggingDebug()){
			logDebug("Entering in to  findUser of CricketProfile Form Handler");
		}
		IdentityManager identityManager = mUserLoginManager
				.getIdentityManager(pRequest);
		try {
			if (!identityManager.checkAuthenticationByPassword(pLogin,
					pPassword)){
				if(isLoggingDebug()){
					logDebug("Escaping the authontication in find User and Return");
				}
				return getProfileTools().getItem(pLogin, null,
						getLoginProfileType());
			}
		} catch (SecurityException e) {
			throw new RepositoryException(e);
		}
		if(isLoggingDebug()){
			logDebug("Exiting findUser of CricketProfile Form Handler");
		}
		return getProfileTools().getItem(pLogin, null, getLoginProfileType());

	}
	@Override
	public boolean handleLogout(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		if(isLoggingDebug()){
			logDebug("Entering in to  HandleLogOut of CricketProfile Form Handler");
		}
		return super.handleLogout(pRequest, pResponse); 		
	}
	@Override
	public void postLogoutUser(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {

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
				logDebug("Entering into CricketProfileFormhandler class of postLogoutUser() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		Cookie[] cookies = pRequest.getCookies();
		
		super.postLogoutUser(pRequest, pResponse); 
		CitySessionInfoObject citySessionInfoObject = (CitySessionInfoObject) pRequest.resolveName("/com/cricket/integration/maxmind/CitySessionInfoObject");
		citySessionInfoObject.setLoggedIn(false);
		citySessionInfoObject.setInquireAccountTimeOut(false);
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
			if (null != cart) {
				if (null != cart.getCurrent()) {
					if (!StringUtils.isBlank(cart.getCurrent().getId())) {
						orderId = cart.getCurrent().getId();
					}
				}
			}			
				logDebug("Exiting from CricketProfileFormhandler class of postLogoutUser() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
	/**
	 * @return the cricketProfile
	 */
	public CricketProfile getCricketProfile() {
		return cricketProfile;
	}
	/**
	 * @param cricketProfile the cricketProfile to set
	 */
	public void setCricketProfile(CricketProfile cricketProfile) {
		this.cricketProfile = cricketProfile;
	}
	/**
	 * @return the cricketEspAdapter
	 */
	public CricketESPAdapter getCricketEspAdapter() {
		return cricketEspAdapter;
	}
	/**
	 * @param cricketEspAdapter the cricketEspAdapter to set
	 */
	public void setCricketEspAdapter(CricketESPAdapter cricketEspAdapter) {
		this.cricketEspAdapter = cricketEspAdapter;
	}
	/**
	 * @return the accountHolderAddressData
	 */
	public CricketAccountHolderAddressData getAccountHolderAddressData() {
		return accountHolderAddressData;
	}
	/**
	 * @param accountHolderAddressData the accountHolderAddressData to set
	 */
	public void setAccountHolderAddressData(CricketAccountHolderAddressData accountHolderAddressData) {
		this.accountHolderAddressData = accountHolderAddressData;
	}
	/**
	 * @return the userAccountInformation
	 */
	public UserAccountInformation getUserAccountInformation() {
		return userAccountInformation;
	}
	/**
	 * @param userAccountInformation the userAccountInformation to set
	 */
	public void setUserAccountInformation(UserAccountInformation userAccountInformation) {
		this.userAccountInformation = userAccountInformation;
	}
	/**
	 * @return the userSessionBean
	 */
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}
	/**
	 * @param userSessionBean the userSessionBean to set
	 */
	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}
	/**
	 * @return the loggedInUserLocationInfo
	 */
	public MyCricketCookieLocationInfo getLoggedInUserLocationInfo() {
		return loggedInUserLocationInfo;
	}
	/**
	 * @param loggedInUserLocationInfo the loggedInUserLocationInfo to set
	 */
	public void setLoggedInUserLocationInfo(
			MyCricketCookieLocationInfo loggedInUserLocationInfo) {
		this.loggedInUserLocationInfo = loggedInUserLocationInfo;
	}
	
	
}
