/**
 * 
 */
package com.cricket.integration.esp.vo;

/**
 * @author MM112358
 *
 */
public class NameBusinessVO {

	 private  String businessName;

	 private NameVO contact;
	    
	/**
	 * 
	 */
	public NameBusinessVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pBusinessName
	 * @param pContact
	 */
	public NameBusinessVO(String pBusinessName, NameVO pContact) {
		super();
		businessName = pBusinessName;
		contact = pContact;
	}

	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}

	/**
	 * @param pBusinessName the businessName to set
	 */
	public void setBusinessName(String pBusinessName) {
		businessName = pBusinessName;
	}

	/**
	 * @return the contact
	 */
	public NameVO getContact() {
		return contact;
	}

	/**
	 * @param pContact the contact to set
	 */
	public void setContact(NameVO pContact) {
		contact = pContact;
	}

}
