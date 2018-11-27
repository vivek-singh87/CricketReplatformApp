package com.cricket.integration.esp.vo;

import java.util.List;

public class ManageSaleItemResponseVO extends ESPResponseVO {
	private ResponseVO response;
	private List<ShoppingCartVO> shoppingCarResponse;
	public ResponseVO getResponse() {
		return response;
	}
	public void setResponse(ResponseVO response) {
		this.response = response;
	}
	public List<ShoppingCartVO> getShoppingCarResponse() {
		return shoppingCarResponse;
	}
	public void setShoppingCarResponse(List<ShoppingCartVO> shoppingCarResponse) {
		this.shoppingCarResponse = shoppingCarResponse;
	}	
	public double totalTax;
	/**
	 * @return the totalTax
	 */
	public double getTotalTax() {
		return totalTax;
	}
	/**
	 * @param totalTax the totalTax to set
	 */
	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}
	
}
