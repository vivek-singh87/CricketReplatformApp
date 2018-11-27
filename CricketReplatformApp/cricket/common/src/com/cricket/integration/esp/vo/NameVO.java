/**
 * 
 */
package com.cricket.integration.esp.vo;

/**
 * @author MM112358
 *
 */
public class NameVO {

	 /* Name prefix or title (e.g. Dr., Mr., Mrs.,						etc.) */
    private String namePrefix;

    /* First Name */
    private String firstName;

    /* Middle name. */
    private String middleName;

    /* Last name. */
    private String lastName;

    /* Name suffix (e.g. Jr. - Junior, Sr. - Senior,						etc.) */
    private String nameSuffix;

    /* A free text field that holds the additional title of the name
     * 						(e.g. President, Major, General, etc.) */
    private String additionalTitle;
    
	/**
	 * 
	 */
	public NameVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pNamePrefix
	 * @param pFirstName
	 * @param pMiddleName
	 * @param pLastName
	 * @param pNameSuffix
	 * @param pAdditionalTitle
	 */
	public NameVO(String pNamePrefix, String pFirstName, String pMiddleName,
			String pLastName, String pNameSuffix, String pAdditionalTitle) {
		super();
		namePrefix = pNamePrefix;
		firstName = pFirstName;
		middleName = pMiddleName;
		lastName = pLastName;
		nameSuffix = pNameSuffix;
		additionalTitle = pAdditionalTitle;
	}

	/**
	 * @return the namePrefix
	 */
	public String getNamePrefix() {
		return namePrefix;
	}

	/**
	 * @param pNamePrefix the namePrefix to set
	 */
	public void setNamePrefix(String pNamePrefix) {
		namePrefix = pNamePrefix;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param pFirstName the firstName to set
	 */
	public void setFirstName(String pFirstName) {
		firstName = pFirstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param pMiddleName the middleName to set
	 */
	public void setMiddleName(String pMiddleName) {
		middleName = pMiddleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param pLastName the lastName to set
	 */
	public void setLastName(String pLastName) {
		lastName = pLastName;
	}

	/**
	 * @return the nameSuffix
	 */
	public String getNameSuffix() {
		return nameSuffix;
	}

	/**
	 * @param pNameSuffix the nameSuffix to set
	 */
	public void setNameSuffix(String pNameSuffix) {
		nameSuffix = pNameSuffix;
	}

	/**
	 * @return the additionalTitle
	 */
	public String getAdditionalTitle() {
		return additionalTitle;
	}

	/**
	 * @param pAdditionalTitle the additionalTitle to set
	 */
	public void setAdditionalTitle(String pAdditionalTitle) {
		additionalTitle = pAdditionalTitle;
	}

}
