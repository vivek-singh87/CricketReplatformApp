/**
 * 
 */
package com.cricket.integration.esp.vo;


/**
 * @author Cricket
 *
 */
public class CreateShippingQuoteResponseVO  {

	private String billingQuoteId;

    private ESPResponseVO[] responseStatus;

    private String billingAccountNumber;
    
    private BillingQuoteDetailsVO billingQuoteDetails;
    
	
	/**
	 * @param pBillingQuoteId
	 * @param pBillingAccountNumber
	 * @param pBillingQuoteDetails
	 */
	public CreateShippingQuoteResponseVO(String pBillingQuoteId,
			String pBillingAccountNumber,
			BillingQuoteDetailsVO pBillingQuoteDetails,ESPResponseVO[] pResponseStatus) {
		super();
		billingQuoteId = pBillingQuoteId;
		billingAccountNumber = pBillingAccountNumber;
		billingQuoteDetails = pBillingQuoteDetails;
		responseStatus=pResponseStatus;
	}


	/**
	 * 
	 */
	public CreateShippingQuoteResponseVO() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the billingQuoteId
	 */
	public String getBillingQuoteId() {
		return billingQuoteId;
	}


	/**
	 * @param pBillingQuoteId the billingQuoteId to set
	 */
	public void setBillingQuoteId(String pBillingQuoteId) {
		billingQuoteId = pBillingQuoteId;
	}


	/**
	 * @return the billingAccountNumber
	 */
	public String getBillingAccountNumber() {
		return billingAccountNumber;
	}


	/**
	 * @param pBillingAccountNumber the billingAccountNumber to set
	 */
	public void setBillingAccountNumber(String pBillingAccountNumber) {
		billingAccountNumber = pBillingAccountNumber;
	}


	/**
	 * @return the billingQuoteDetails
	 */
	public BillingQuoteDetailsVO getBillingQuoteDetails() {
		return billingQuoteDetails;
	}


	/**
	 * @param pBillingQuoteDetails the billingQuoteDetails to set
	 */
	public void setBillingQuoteDetails(BillingQuoteDetailsVO pBillingQuoteDetails) {
		billingQuoteDetails = pBillingQuoteDetails;
	}


	/**
	 * @return the responseStatus
	 */
	public ESPResponseVO[] getResponseStatus() {
		return responseStatus;
	}


	/**
	 * @param pResponseStatus the responseStatus to set
	 */
	public void setResponseStatus(ESPResponseVO[] pResponseStatus) {
		responseStatus = pResponseStatus;
	}

}
