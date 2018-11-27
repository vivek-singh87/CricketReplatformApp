package com.cricket.integration.esp.vo;

import java.math.BigDecimal;

public class OrderLineItemVO {
	private String orderLineNumber;
	private String itemId;
	private int quantity;
	private String unitOfMeasure;
	private String productName;
	private ManufacturerVO manufacturer;
	private BigDecimal basePrice;
	private BigDecimal lineTax1;
	public String getOrderLineNumber() {
		return orderLineNumber;
	}
	public void setOrderLineNumber(String orderLineNumber) {
		this.orderLineNumber = orderLineNumber;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public ManufacturerVO getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(ManufacturerVO manufacturer) {
		this.manufacturer = manufacturer;
	}
	public BigDecimal getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}
	public BigDecimal getLineTax1() {
		return lineTax1;
	}
	public void setLineTax1(BigDecimal lineTax1) {
		this.lineTax1 = lineTax1;
	}
	
}
