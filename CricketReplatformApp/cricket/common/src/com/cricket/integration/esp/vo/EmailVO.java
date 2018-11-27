/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.util.Date;

/**
 * @author MM112358
 *
 */
public class EmailVO {

	private Date effectiveDate;

    /* E-mail address */
    private String emailAddress;

    /* Business = B, Personal =P, Other = O */
    private String emailType;

    /* Email primary address indicator. */
    private Boolean primaryAddressIndicator;

    /* Language preference for email content if						known. */
    private String language;
    
	/**
	 * 
	 */
	public EmailVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pEffectiveDate
	 * @param pEmailAddress
	 * @param pEmailType
	 * @param pPrimaryAddressIndicator
	 * @param pLanguage
	 */
	public EmailVO(Date pEffectiveDate, String pEmailAddress,
			String pEmailType, Boolean pPrimaryAddressIndicator,
			String pLanguage) {
		super();
		effectiveDate = pEffectiveDate;
		emailAddress = pEmailAddress;
		emailType = pEmailType;
		primaryAddressIndicator = pPrimaryAddressIndicator;
		language = pLanguage;
	}

	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param pEffectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date pEffectiveDate) {
		effectiveDate = pEffectiveDate;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param pEmailAddress the emailAddress to set
	 */
	public void setEmailAddress(String pEmailAddress) {
		emailAddress = pEmailAddress;
	}

	/**
	 * @return the emailType
	 */
	public String getEmailType() {
		return emailType;
	}

	/**
	 * @param pEmailType the emailType to set
	 */
	public void setEmailType(String pEmailType) {
		emailType = pEmailType;
	}

	/**
	 * @return the primaryAddressIndicator
	 */
	public Boolean getPrimaryAddressIndicator() {
		return primaryAddressIndicator;
	}

	/**
	 * @param pPrimaryAddressIndicator the primaryAddressIndicator to set
	 */
	public void setPrimaryAddressIndicator(Boolean pPrimaryAddressIndicator) {
		primaryAddressIndicator = pPrimaryAddressIndicator;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param pLanguage the language to set
	 */
	public void setLanguage(String pLanguage) {
		language = pLanguage;
	}

}
