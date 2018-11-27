package com.cricket.commerce.order.payment;

import java.io.Serializable;

/**
 * Extension of ATG CreditCard to have additional properties needed. NOTE : This
 * class  
 *
 * @author TechM
 * @version 1.0
 */
public class CricketPaymentData implements Serializable {

	private static final long serialVersionUID = 6441740439073851585L;
 	
	/* holds first name of card holder */
	private String fristName;
	/* holds first name of card holder */
	private String lastName;
	/* holds first cardNumber of card holder */
	private String cardNumber;
	/* holds first expirationMonth of card holder */
	private String expirationMonth;
	/* holds first expirationYear of card holder */
	private String expirationYear;
	/* holds first cvcNumber of card holder */
	private String cvcNumber;
	/* holds first creditCardType of card holder */
	private String creditCardType;
	/* holds abp flag for auto bill payment */
	private boolean abpFlag;
	/* holds vesta token for credit card number - it will come from vesta js */
	private String vestaToken;
	
	public String getCreditCardType() {
		return creditCardType;
	}
	public void setCreditCardType(String pCreditCardType) {
		creditCardType = pCreditCardType;
	}
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

	/*@Override
	public String toString() {
		StringBuffer cricketPaymentData = new StringBuffer();
		cricketPaymentData.append(" First Name: "+getFristName()).
		append(" LastName: "+getLastName()).append("abp Flag"+getAbpFlag()).
		append(" Token[showing no.of digits]: "+getVestaToken()!=null?getVestaToken().length():null).
		append(" CredictCardType[not taking from UI may be null] : "+getCreditCardType()).
		append(" CVC: "+getCvcNumber()!=null?getCvcNumber().length():null).append(" Month: "+getExpirationMonth()).append(" Year: "+getExpirationYear());
		return cricketPaymentData.toString();
	}*/
}
