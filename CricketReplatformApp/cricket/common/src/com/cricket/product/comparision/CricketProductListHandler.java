package com.cricket.product.comparision;

import java.io.IOException;

import javax.servlet.ServletException;

import atg.commerce.catalog.comparison.ProductListHandler;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

/* 
 * CricketProductListHandler extends  ProductListHandler
 * @author : techM
 */
public class CricketProductListHandler extends ProductListHandler {

	/*
	 * property : productIDs
	 */
	private String productIDs;

	/**
	 * . The method is used to get the set product ids to productIDList property
	 * 
	 * @param pRequest
	 *            - DynamoHttpServletRequest
	 * @param pResponse
	 *            - DynamoHttpServletResponse
	 * @return Boolean
	 * @throws ServletException
	 * @throws IOException
	 * @return boolean
	 * */
	@Override
	public boolean handleAddProductList(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {

		if (isLoggingDebug()) {
			logDebug("Inside handleAddProductList method");
			logDebug("Product Ids :: "+getProductIDs());
		}
		if (null != getProductIDs()) {
			if (getProductIDs().indexOf(",") == -1) {
				setProductIDList(new String[]{getProductIDs()});
			} else {
				setProductIDList(getProductIDs().split(","));
			}
		}

		if (isLoggingDebug()) {
			logDebug("Exit handleAddProductList method");
		}
		return super.handleAddProductList(pRequest, pResponse);
	}

	/**
	 * .The method is preAddProductList
	 * @param pRequest
	 *            - DynamoHttpServletRequest
	 * @param pResponse
	 *            - DynamoHttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 * */
	@Override
	public void preAddProductList(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		getProductList().clear();
	}

	/**
	 * @return the productIDs
	 */
	public String getProductIDs() {
		return productIDs;
	}

	/**
	 * @param productIDs the productIDs to set
	 */
	public void setProductIDs(String productIDs) {
		this.productIDs = productIDs;
	}

}
