package com.cricket.commerce.order.payment;

import java.io.Serializable;

/**
 * Extension of ATG CreditCard to have additional properties needed. NOTE : This
 * class  
 *
 * @author TechM
 * @version 1.0
 */
public class CricketABPPaymentData implements Serializable {

	private static final long serialVersionUID = 6441740439073851585L;
	private String fristName;
	private String lastName;
	private String cardNumber;
	private String expirationMonth;
	private String expirationYear;
	private String cvcNumber;
	private boolean abpFlag;
	private String vestaToken;
	private String creditCardType;
	
	public String getVestaToken() {
		return vestaToken;
	}
	public void setVestaToken(String vestaToken) {
		this.vestaToken = vestaToken;
	}
	public boolean getAbpFlag() {
		return abpFlag;
	}
	public void setAbpFlag(boolean abpFlag) {
		this.abpFlag = abpFlag;
	}
	public String getFristName() {
		return fristName;
	}
	public void setFristName(String fristName) {
		this.fristName = fristName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getExpirationMonth() {
		return expirationMonth;
	}
	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}
	public String getExpirationYear() {
		return expirationYear;
	}
	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}
	public String getCvcNumber() {
		return cvcNumber;
	}
	public void setCvcNumber(String cvcNumber) {
		this.cvcNumber = cvcNumber;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the creditCardType
	 */
	public String getCreditCardType() {
		return creditCardType;
	}
	/**
	 * @param creditCardType the creditCardType to set
	 */
	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}
	/*@Override
	public String toString() {
		StringBuffer cricketABPPaymentData = new StringBuffer();
		cricketABPPaymentData.append(" First Name: "+getFristName()).
		append("LastName: "+getLastName()).append(" abp Flag"+getAbpFlag()).
		append("Token for ABP payment[showing no.of digits]: "+getVestaToken()!=null?getVestaToken().length():null).
		append(" CredictCardType[not taking from UI may be null]:"+getCreditCardType()).
		append(" CVC: "+getCvcNumber()!=null?getCvcNumber().length():null).append(" Month: "+getExpirationMonth()).append(" Year: "+getExpirationYear());
		return cricketABPPaymentData.toString();
	}*/
}
