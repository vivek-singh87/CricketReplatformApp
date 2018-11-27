/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.math.BigInteger;

/**
 * @author Cricket
 *
 */
public class SubscriberProductsVO {
	
	 private String productName;

	    /* PARC Benefit ID */
	    private String productId;

	    private  BigInteger productTypeId;

	    private String action;

	/**
	 * 
	 */
	public SubscriberProductsVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pProductName
	 * @param pProductId
	 * @param pProductTypeId
	 * @param pAction
	 */
	public SubscriberProductsVO(String pProductName, String pProductId,
			BigInteger pProductTypeId, String pAction) {
		super();
		productName = pProductName;
		productId = pProductId;
		productTypeId = pProductTypeId;
		action = pAction;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param pProductName the productName to set
	 */
	public void setProductName(String pProductName) {
		productName = pProductName;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param pProductId the productId to set
	 */
	public void setProductId(String pProductId) {
		productId = pProductId;
	}

	/**
	 * @return the productTypeId
	 */
	public BigInteger getProductTypeId() {
		return productTypeId;
	}

	/**
	 * @param pProductTypeId the productTypeId to set
	 */
	public void setProductTypeId(BigInteger pProductTypeId) {
		productTypeId = pProductTypeId;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param pAction the action to set
	 */
	public void setAction(String pAction) {
		action = pAction;
	}

}
