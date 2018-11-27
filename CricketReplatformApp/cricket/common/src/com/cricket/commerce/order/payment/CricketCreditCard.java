package com.cricket.commerce.order.payment;

import atg.commerce.order.CreditCard;

import com.cricket.common.constants.CricketCommonConstants;

/**
 * Extension of ATG CreditCard to have additional properties needed. NOTE : This
 * class extends from OOTB CreditCard
 *
 * @author TechM
 * @version 1.0
 */
public class CricketCreditCard extends  CreditCard {

	private static final long serialVersionUID = 6441740439073851585L;

	
	private static final String VESTA_TOKEN="vestaToken";
	private static final String TRANSACTION_ID="transactionId";
 	
	/* holds current customer auto bill payment options */
	public String autoBillPayment;
	/* hodls first name */
	private String ccFirstName;
	
	/* holds last name */
	private String ccLastName;
	
	/* holds flag to identify the different card for autobill pay */
	private boolean isDiffernetCard=false;
	
 	 
	
	/**
	 * @return
	 */
	public String getVestaToken()
	{
		return (String) getPropertyValue(VESTA_TOKEN);
	}

	/**
	 * @param vestaToken
	 */
	public void setVestaToken(final String vestaToken)
	{
		setPropertyValue(VESTA_TOKEN, vestaToken.trim());
	}
	
	
	/**
	 * @return
	 */
	public String getTransactionId()
	{
		return (String) getPropertyValue(TRANSACTION_ID);
	}

	/**
	 * @param transactionId
	 */
	public void setTransactionId(final String transactionId)
	{
		setPropertyValue(TRANSACTION_ID, transactionId.trim());
	}
	
	/**
	 * @return
	 */
	public boolean getAutoBillPayment()
	{
		return (Boolean) getPropertyValue(CricketCommonConstants.AUTO_BILL_PAYMENT);
	}

	/**
	 * @param autoBillPayment
	 */
	public void setAutoBillPayment(final boolean autoBillPayment)
	{
		setPropertyValue(CricketCommonConstants.AUTO_BILL_PAYMENT, autoBillPayment);
	}
	
	 

	/**
	 * @return the ccFirstName
	 */
	public String getCcFirstName() {
		return ccFirstName;
	}

	/**
	 * @param ccFirstName the ccFirstName to set
	 */
	public void setCcFirstName(String ccFirstName) {
		this.ccFirstName = ccFirstName;
	}

	/**
	 * @return the ccLastName
	 */
	public String getCcLastName() {
		return ccLastName;
	}

	/**
	 * @param ccLastName the ccLastName to set
	 */
	public void setCcLastName(String ccLastName) {
		this.ccLastName = ccLastName;
	}

	/**
	 * @return the isDiffernetCard
	 */
	public boolean isDiffernetCard() {
		return isDiffernetCard;
	}

	/**
	 * @param isDiffernetCard the isDiffernetCard to set
	 */
	public void setDiffernetCard(boolean isDiffernetCard) {
		this.isDiffernetCard = isDiffernetCard;
	}

	/**
	 * @param autoBillPayment the autoBillPayment to set
	 */
	public void setAutoBillPayment(String autoBillPayment) {
		this.autoBillPayment = autoBillPayment;
	}
	 
	/*
	 * (non-Javadoc)
	 * @see
	 * /
	public void initFromPaymentGroup( PaymentGroup paymentGroup)
	{
		if (!(paymentGroup instanceof EfiCreditCard))
		{
			// Go away!
			throw new IllegalArgumentException(
				"EfiCreditCard::initFromPaymentGroup() - The given paymentGroup is not of type EfiCreditCard. Given : "
					+ paymentGroup);
		}
		// Copying only user input fields
		final EfiCreditCard givenCard = (EfiCreditCard) paymentGroup;
		setCreditCardType(givenCard.getCreditCardType());
		setPseudoCardNumber(givenCard.getPseudoCardNumber());
		setExpirationYear(givenCard.getExpirationYear());
		setExpirationMonth(givenCard.getExpirationMonth());
		setCurrencyCode(paymentGroup.getCurrencyCode());
		setCcTransactionId(givenCard.getCcTransactionId());
		setPaymentProvider(givenCard.getPaymentProvider());
		setCustomerId(givenCard.getCustomerId());
		setRefNr(givenCard.getRefNr());
	} */


}
