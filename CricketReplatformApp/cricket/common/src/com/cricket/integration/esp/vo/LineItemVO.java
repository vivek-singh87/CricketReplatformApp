package com.cricket.integration.esp.vo;

import java.math.BigDecimal;

public class LineItemVO {
	/**
	 * Sale ID associated with the point of sale system
	 */
	  private String saleId; 
	  
	  /**
	   * Line Number associated sales line item in the point of sale system. packageId in ATG
	   */
	  private String lineNumber; 
	  
	  private int quantity; 
	  /**
	   * Bar code used to identify equipment or devices for easy scanning at POS
	   */
	  private String barCode;
	  
	  /**
	   * 
	   */
	  private String lineOfServiceId; 
	  
	  /**
	   * 
	   */
	  private BigDecimal price; 
	  
	  /**
	   * 
	   */
	  private BigDecimal taxAmount;
	  
	  /**
	   * 
	   */
	  private BigDecimal salesTax;
	  
	  /**
	   * 
	   */
	  private String note; 
	  
	  /**
	   * Standard 10 digit Telephone Number
	   */
	  private String mdn;
	  
	  /**
	   * Product types SERIALIZABLE NON_SERIALIZABLE VIRTUAL
	   */
	  private String type; 
	  
	  /**
	   * 
	   */
	  private String name;
	  
	  /**
	   * 
	   */
	  private String manufacturer;
	  
	  /**
	   * 
	   */
	  private String description; 
	  
	  /**
	   * activation status ACTIVE INACTIVE
	   */
	  private String status;
	  
	  /**
	   * Mobile equipment identifier - IMEI, ESN or MEID
	   */
	  private String equipmentIdentifier; 
	  
	  /**
	   * 
	   */
	  private BigDecimal defaultPrice; 
	  
	  /**
	   * 
	   */
	  private String category; 

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getLineOfServiceId() {
		return lineOfServiceId;
	}

	public void setLineOfServiceId(String lineOfServiceId) {
		this.lineOfServiceId = lineOfServiceId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getSalesTax() {
		return salesTax;
	}

	public void setSalesTax(BigDecimal salesTax) {
		this.salesTax = salesTax;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getMdn() {
		return mdn;
	}

	public void setMdn(String mdn) {
		this.mdn = mdn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEquipmentIdentifier() {
		return equipmentIdentifier;
	}

	public void setEquipmentIdentifier(String equipmentIdentifier) {
		this.equipmentIdentifier = equipmentIdentifier;
	}

	public BigDecimal getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(BigDecimal defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}	
}
