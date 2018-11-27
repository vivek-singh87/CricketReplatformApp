/**
 * 
 */
package com.cricket.email;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import atg.droplet.GenericFormHandler;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.integration.esp.vo.NameValuePairVO;
import com.cricket.integration.esp.vo.SendMessageRequestVO;

/**
 * CricketEmailFormHandler willl inovke the webservice sendMessage to ESP, for the functionaltiy Email-Signup and Promotional email.
 * It will collect the values based on functionality and it will send details to ESP.
 * 
 * @author Cricket
 * 
 */
public class CricketEmailFormHandler extends GenericFormHandler {

	private String mFirstName;
	
	private String mLastName;	 

	private String mZipCode;

	private String mExistingCustomer;

	private String mCustomerType;

	private String mCurrentCarrier;

	private String mServiceProvider;

	private CricketEmailManager mEmailManager;

	private String mToEmailAddress;
	
	
	
	

	
	/**
	 * 
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws ServiceException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 * @throws ESPApplicationException 
	 */
	public boolean handleCricketEmailSignUP(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ESPApplicationException, MalformedURLException, RemoteException, ServiceException {

		 
		SendMessageRequestVO requestVO = validateEmailSignUPRequestInfo();

		getEmailManager().sendEmailInfoToESP(requestVO,null);

		return true;
	}
	
	
	/**
	 * 
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws ServiceException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 * @throws ESPApplicationException 
	 */
	public boolean handleCricketPromoEmail(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ESPApplicationException, MalformedURLException, RemoteException, ServiceException {

		 
		SendMessageRequestVO requestVO = validateEmailPromoRequestInfo();

		getEmailManager().sendEmailInfoToESP(requestVO,null);

		return true;
	}
	
	
	
	private SendMessageRequestVO validateEmailPromoRequestInfo() {
		SendMessageRequestVO requestInfo = populateRequestInfo();
		
		 
		requestInfo.setTemplateIdentifier(getEmailManager().getPromoEmailTemplate());
		requestInfo.setSubjectLine(getEmailManager().getPromotionalEmailSubject());
		requestInfo.setTemplateType(getEmailManager().getTemplateType());
	
	 return requestInfo;
	}


	/**
	 * 
	 * @param pRequestInfo
	 * @return 
	 */
	private SendMessageRequestVO validateEmailSignUPRequestInfo() {

		SendMessageRequestVO requestInfo = populateRequestInfo();
		
		requestInfo.setTemplateIdentifier(getEmailManager().getWelcomeEmailTemplate());
		requestInfo.setSubjectLine(getEmailManager().getWelecomeEmailSubject());
		requestInfo.setTemplateType(getEmailManager().getTemplateType());
		
		 return requestInfo;
	 
	}


	/**
	 * @return
	 */
	private SendMessageRequestVO populateRequestInfo() {
		
		SendMessageRequestVO requestInfo = new SendMessageRequestVO();
		NameValuePairVO[] substitutionVariables = new NameValuePairVO[2];
		int index=0;
		if (null != getFirstName()) {
			substitutionVariables[index].setName(CricketCookieConstants.FIRST_NAME);
			substitutionVariables[index].setValue(getFirstName());
			index++;
		}
		
		if(null!=getLastName()){
			substitutionVariables[index].setName(CricketCookieConstants.LAST_NAME);
			substitutionVariables[index].setValue(getLastName());
			index++;
		}
		
		requestInfo.setSubstitutionVariables(substitutionVariables);
		
		 
			 requestInfo.setFromEmail(getEmailManager().getFromEmailAddress());
		 
		if(null!=getToEmailAddress()){
			 requestInfo.setToEmail(getToEmailAddress());
		}
		return requestInfo;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return mFirstName;
	}

	/**
	 * @param pFirstName
	 *            the firstName to set
	 */
	public void setFirstName(String pFirstName) {
		mFirstName = pFirstName;
	}

	 

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return mZipCode;
	}

	/**
	 * @param pZipCode
	 *            the zipCode to set
	 */
	public void setZipCode(String pZipCode) {
		mZipCode = pZipCode;
	}

	/**
	 * @return the existingCustomer
	 */
	public String getExistingCustomer() {
		return mExistingCustomer;
	}

	/**
	 * @param pExistingCustomer
	 *            the existingCustomer to set
	 */
	public void setExistingCustomer(String pExistingCustomer) {
		mExistingCustomer = pExistingCustomer;
	}

	/**
	 * @return the customerType
	 */
	public String getCustomerType() {
		return mCustomerType;
	}

	/**
	 * @param pCustomerType
	 *            the customerType to set
	 */
	public void setCustomerType(String pCustomerType) {
		mCustomerType = pCustomerType;
	}

	/**
	 * @return the currentCarrier
	 */
	public String getCurrentCarrier() {
		return mCurrentCarrier;
	}

	/**
	 * @param pCurrentCarrier
	 *            the currentCarrier to set
	 */
	public void setCurrentCarrier(String pCurrentCarrier) {
		mCurrentCarrier = pCurrentCarrier;
	}

	/**
	 * @return the serviceProvider
	 */
	public String getServiceProvider() {
		return mServiceProvider;
	}

	/**
	 * @param pServiceProvider
	 *            the serviceProvider to set
	 */
	public void setServiceProvider(String pServiceProvider) {
		mServiceProvider = pServiceProvider;
	}

	/**
	 * @return the emailManager
	 */
	public CricketEmailManager getEmailManager() {
		return mEmailManager;
	}

	/**
	 * @param pEmailManager
	 *            the emailManager to set
	 */
	public void setEmailManager(CricketEmailManager pEmailManager) {
		mEmailManager = pEmailManager;
	}

	 


	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return mLastName;
	}


	/**
	 * @param pLastName the lastName to set
	 */
	public void setLastName(String pLastName) {
		mLastName = pLastName;
	}


	/**
	 * @return the toEmailAddress
	 */
	public String getToEmailAddress() {
		return mToEmailAddress;
	}


	/**
	 * @param pToEmailAddress the toEmailAddress to set
	 */
	public void setToEmailAddress(String pToEmailAddress) {
		mToEmailAddress = pToEmailAddress;
	}


	 

}
