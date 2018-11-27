/**
 * 
 */
package com.cricket.email;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.cricket.integration.esp.vo.SendMessageResponseVO;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.SendMessageRequestVO;

import atg.nucleus.GenericService;

/**
 * @author Cricket
 *
 */
public class CricketEmailManager extends GenericService {
	
	
	private CricketESPAdapter mEspAdapter;	
	 
	private String mFromEmailAddress;
	
	private String mTemplateIdentifier;
	
	private String mTemplateLanguage;
	
	private String mTemplateType;
	
	private String mWelcomeEmailTemplate;
	
	private String mPromoEmailTemplate;
	
	private String mWelecomeEmailSubject;
	
	private String mPromotionalEmailSubject;
	
	private final static String SUCCESS="0";
	
	private final static String SUCCESS_TEXT="SUCCESS";
	
	private String templateSpanishLanguage;
	
	/**
	 * @param pRequestInfo
	 * @param pString 
	 * @return
	 * @throws ServiceException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 * @throws ESPApplicationException 
	 */
	public String sendEmailInfoToESP(SendMessageRequestVO pRequestInfo, String pOrderId) throws ESPApplicationException, MalformedURLException, RemoteException, ServiceException{
		
		boolean status=false;
		String emailStatus="SUCCESS";
		SendMessageResponseVO responseVO=null;
		try {
			responseVO = getEspAdapter().sendMessage(pRequestInfo, pOrderId);
			
		} catch (CricketException e) {
			logError("Error while sending email info details to ESP call sendmessage "+e,e);
		}
	  
		if(null!=responseVO){
			if(isLoggingDebug()){
		    	logDebug(" sendMessage ESP Resonse code and description in CricketEmailManager -> sendEmailInfoToESP() ..."+
		    			"Code: "+responseVO.getCode() +" Description: "+ responseVO.getDescription());
		    }
			if(responseVO.getCode().equalsIgnoreCase(SUCCESS)){
				emailStatus= SUCCESS_TEXT;
			} else{
				// getting errors
				logError("sendMessage ESP Resonse error  code and description in CricketEmailManager -> sendEmailInfoToESP() ..."+
		    			"Code: "+responseVO.getCode() +" Description: "+ responseVO.getDescription());
				status=false;
				emailStatus= responseVO.getCode();
			 
			}
			 
		}
		
		 
		return emailStatus;
		
		
	}
	 
	

	 



	/**
	 * @return the fromEmailAddress
	 */
	public String getFromEmailAddress() {
		return mFromEmailAddress;
	}



	/**
	 * @param pFromEmailAddress the fromEmailAddress to set
	 */
	public void setFromEmailAddress(String pFromEmailAddress) {
		mFromEmailAddress = pFromEmailAddress;
	}



	/**
	 * @return the templateIdentifier
	 */
	public String getTemplateIdentifier() {
		return mTemplateIdentifier;
	}



	/**
	 * @param pTemplateIdentifier the templateIdentifier to set
	 */
	public void setTemplateIdentifier(String pTemplateIdentifier) {
		mTemplateIdentifier = pTemplateIdentifier;
	}



	/**
	 * @return the templateLanguage
	 */
	public String getTemplateLanguage() {
		return mTemplateLanguage;
	}



	/**
	 * @param pTemplateLanguage the templateLanguage to set
	 */
	public void setTemplateLanguage(String pTemplateLanguage) {
		mTemplateLanguage = pTemplateLanguage;
	}
	
	
	public String getTemplateSpanishLanguage() {
		return templateSpanishLanguage;
	}


	public void setTemplateSpanishLanguage(String templateSpanishLanguage) {
		this.templateSpanishLanguage = templateSpanishLanguage;
	}

	
	/**
	 * @return the templateType
	 */
	public String getTemplateType() {
		return mTemplateType;
	}



	/**
	 * @param pTemplateType the templateType to set
	 */
	public void setTemplateType(String pTemplateType) {
		mTemplateType = pTemplateType;
	}



	/**
	 * @return the welcomeEmailTemplate
	 */
	public String getWelcomeEmailTemplate() {
		return mWelcomeEmailTemplate;
	}



	/**
	 * @param pWelcomeEmailTemplate the welcomeEmailTemplate to set
	 */
	public void setWelcomeEmailTemplate(String pWelcomeEmailTemplate) {
		mWelcomeEmailTemplate = pWelcomeEmailTemplate;
	}



	/**
	 * @return the promoEmailTemplate
	 */
	public String getPromoEmailTemplate() {
		return mPromoEmailTemplate;
	}



	/**
	 * @param pPromoEmailTemplate the promoEmailTemplate to set
	 */
	public void setPromoEmailTemplate(String pPromoEmailTemplate) {
		mPromoEmailTemplate = pPromoEmailTemplate;
	}



	/**
	 * @return the welecomeEmailSubject
	 */
	public String getWelecomeEmailSubject() {
		return mWelecomeEmailSubject;
	}



	/**
	 * @param pWelecomeEmailSubject the welecomeEmailSubject to set
	 */
	public void setWelecomeEmailSubject(String pWelecomeEmailSubject) {
		mWelecomeEmailSubject = pWelecomeEmailSubject;
	}



	/**
	 * @return the promotionalEmailSubject
	 */
	public String getPromotionalEmailSubject() {
		return mPromotionalEmailSubject;
	}



	/**
	 * @param pPromotionalEmailSubject the promotionalEmailSubject to set
	 */
	public void setPromotionalEmailSubject(String pPromotionalEmailSubject) {
		mPromotionalEmailSubject = pPromotionalEmailSubject;
	}



	/**
	 * @return the espAdapter
	 */
	public CricketESPAdapter getEspAdapter() {
		return mEspAdapter;
	}



	/**
	 * @param pEspAdapter the espAdapter to set
	 */
	public void setEspAdapter(CricketESPAdapter pEspAdapter) {
		mEspAdapter = pEspAdapter;
	}
	
	
	
	
	

}
