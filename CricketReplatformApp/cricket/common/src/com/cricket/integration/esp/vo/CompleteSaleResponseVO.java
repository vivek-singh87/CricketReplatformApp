package com.cricket.integration.esp.vo;

public class CompleteSaleResponseVO extends ESPResponseVO {	
	/**
	 * 
	 */
	private ShoppingCartVO shoppingCartResponse;
	
	/**
	 * 
	 */
	private ResponseVO response;
	
	public ShoppingCartVO getShoppingCartResponse() {
		return shoppingCartResponse;
	}

	public void setShoppingCartResponse(ShoppingCartVO shoppingCartResponse) {
		this.shoppingCartResponse = shoppingCartResponse;
	}

	public ResponseVO getResponse() {
		return response;
	}

	public void setResponse(ResponseVO response) {
		this.response = response;
	}
	
}
