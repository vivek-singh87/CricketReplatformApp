/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author MM112358
 *
 */
public class UpdateWebReportRequestVOItemsItem {

	
	 private String itemCode;

	 private String itemDescription;
	 
	 private String itemType;

	 private BigInteger quantity;

	 private BigDecimal unitPrice;

	 private BigDecimal unitActualPrice;

	 private BigDecimal unitMonthlyPrice;

	 private String sku;
	    
	/**
	 * 
	 */
	public UpdateWebReportRequestVOItemsItem() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pItemCode
	 * @param pItemDescription
	 * @param pItemType
	 * @param pQuantity
	 * @param pUnitPrice
	 * @param pUnitActualPrice
	 * @param pUnitMonthlyPrice
	 * @param pSku
	 */
	public UpdateWebReportRequestVOItemsItem(String pItemCode,
			String pItemDescription, String pItemType, BigInteger pQuantity,
			BigDecimal pUnitPrice, BigDecimal pUnitActualPrice,
			BigDecimal pUnitMonthlyPrice, String pSku) {
		super();
		itemCode = pItemCode;
		itemDescription = pItemDescription;
		itemType = pItemType;
		quantity = pQuantity;
		unitPrice = pUnitPrice;
		unitActualPrice = pUnitActualPrice;
		unitMonthlyPrice = pUnitMonthlyPrice;
		sku = pSku;
	}

	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * @param pItemCode the itemCode to set
	 */
	public void setItemCode(String pItemCode) {
		itemCode = pItemCode;
	}

	/**
	 * @return the itemDescription
	 */
	public String getItemDescription() {
		return itemDescription;
	}

	/**
	 * @param pItemDescription the itemDescription to set
	 */
	public void setItemDescription(String pItemDescription) {
		itemDescription = pItemDescription;
	}

	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param pItemType the itemType to set
	 */
	public void setItemType(String pItemType) {
		itemType = pItemType;
	}

	/**
	 * @return the quantity
	 */
	public BigInteger getQuantity() {
		return quantity;
	}

	/**
	 * @param pQuantity the quantity to set
	 */
	public void setQuantity(BigInteger pQuantity) {
		quantity = pQuantity;
	}

	/**
	 * @return the unitPrice
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param pUnitPrice the unitPrice to set
	 */
	public void setUnitPrice(BigDecimal pUnitPrice) {
		unitPrice = pUnitPrice;
	}

	/**
	 * @return the unitActualPrice
	 */
	public BigDecimal getUnitActualPrice() {
		return unitActualPrice;
	}

	/**
	 * @param pUnitActualPrice the unitActualPrice to set
	 */
	public void setUnitActualPrice(BigDecimal pUnitActualPrice) {
		unitActualPrice = pUnitActualPrice;
	}

	/**
	 * @return the unitMonthlyPrice
	 */
	public BigDecimal getUnitMonthlyPrice() {
		return unitMonthlyPrice;
	}

	/**
	 * @param pUnitMonthlyPrice the unitMonthlyPrice to set
	 */
	public void setUnitMonthlyPrice(BigDecimal pUnitMonthlyPrice) {
		unitMonthlyPrice = pUnitMonthlyPrice;
	}

	/**
	 * @return the sku
	 */
	public String getSku() {
		return sku;
	}

	/**
	 * @param pSku the sku to set
	 */
	public void setSku(String pSku) {
		sku = pSku;
	}

}
