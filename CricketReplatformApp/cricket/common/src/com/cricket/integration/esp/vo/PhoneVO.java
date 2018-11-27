/**
 * 
 */
package com.cricket.integration.esp.vo;

/**
 * @author MM112358
 *
 */
public class PhoneVO {

	 /* Home phone */
    private java.lang.String homePhone;

    /* Work phone */
    private java.lang.String workPhone;

    /* Work phone extension. */
    private java.lang.String workPhoneExtension;

    /* Can be reached phone */
    private java.lang.String canBeReachedPhone;

    
	/**
	 * 
	 */
	public PhoneVO() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param pHomePhone
	 * @param pWorkPhone
	 * @param pWorkPhoneExtension
	 * @param pCanBeReachedPhone
	 */
	public PhoneVO(String pHomePhone, String pWorkPhone,
			String pWorkPhoneExtension, String pCanBeReachedPhone) {
		super();
		homePhone = pHomePhone;
		workPhone = pWorkPhone;
		workPhoneExtension = pWorkPhoneExtension;
		canBeReachedPhone = pCanBeReachedPhone;
	}


	/**
	 * @return the homePhone
	 */
	public java.lang.String getHomePhone() {
		return homePhone;
	}


	/**
	 * @param pHomePhone the homePhone to set
	 */
	public void setHomePhone(java.lang.String pHomePhone) {
		homePhone = pHomePhone;
	}


	/**
	 * @return the workPhone
	 */
	public java.lang.String getWorkPhone() {
		return workPhone;
	}


	/**
	 * @param pWorkPhone the workPhone to set
	 */
	public void setWorkPhone(java.lang.String pWorkPhone) {
		workPhone = pWorkPhone;
	}


	/**
	 * @return the workPhoneExtension
	 */
	public java.lang.String getWorkPhoneExtension() {
		return workPhoneExtension;
	}


	/**
	 * @param pWorkPhoneExtension the workPhoneExtension to set
	 */
	public void setWorkPhoneExtension(java.lang.String pWorkPhoneExtension) {
		workPhoneExtension = pWorkPhoneExtension;
	}


	/**
	 * @return the canBeReachedPhone
	 */
	public java.lang.String getCanBeReachedPhone() {
		return canBeReachedPhone;
	}


	/**
	 * @param pCanBeReachedPhone the canBeReachedPhone to set
	 */
	public void setCanBeReachedPhone(java.lang.String pCanBeReachedPhone) {
		canBeReachedPhone = pCanBeReachedPhone;
	}

}
