/**
 * 
 */
package com.cricket.integration.esp.vo;

/**
 * @author Cricket
 *
 */
public class UpdateSubscriberResponseVO {
	
	
	private BillingquoteResponseVO billingQuote;
	
	private SubscriberProductsVO[] subscriberProducts;
	
	private ESPResponseVO[] response;
	
	private double serviceTax;

	/**
	 * 
	 */
	public UpdateSubscriberResponseVO() {
		// TODO Auto-generated constructor stub
	}


	 


	/**
	 * @param pBillingQuote
	 * @param pSubscriberProducts
	 * @param pResponse
	 */
	public UpdateSubscriberResponseVO(BillingquoteResponseVO pBillingQuote,
			SubscriberProductsVO[] pSubscriberProducts,
			ESPResponseVO[] pResponse) {
		super();
		billingQuote = pBillingQuote;
		subscriberProducts = pSubscriberProducts;
		response = pResponse;
	}





	/**
	 * @return the billingQuote
	 */
	public BillingquoteResponseVO getBillingQuote() {
		return billingQuote;
	}


	/**
	 * @param pBillingQuote the billingQuote to set
	 */
	public void setBillingQuote(BillingquoteResponseVO pBillingQuote) {
		billingQuote = pBillingQuote;
	}


	/**
	 * @return the subscriberProducts
	 */
	public SubscriberProductsVO[] getSubscriberProducts() {
		return subscriberProducts;
	}


	/**
	 * @param pSubscriberProducts the subscriberProducts to set
	 */
	public void setSubscriberProducts(SubscriberProductsVO[] pSubscriberProducts) {
		subscriberProducts = pSubscriberProducts;
	}


	/**
	 * @return the response
	 */
	public ESPResponseVO[] getResponse() {
		return response;
	}


	/**
	 * @param pResponse the response to set
	 */
	public void setResponse(ESPResponseVO[] pResponse) {
		response = pResponse;
	}





	/**
	 * @return the serviceTax
	 */
	public double getServiceTax() {
		return serviceTax;
	}





	/**
	 * @param serviceTax the serviceTax to set
	 */
	public void setServiceTax(double serviceTax) {
		this.serviceTax = serviceTax;
	}	

}
