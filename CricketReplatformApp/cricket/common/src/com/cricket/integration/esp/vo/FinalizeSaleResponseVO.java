package com.cricket.integration.esp.vo;

public class FinalizeSaleResponseVO extends ESPResponseVO {
	
	/**
	 * 
	 */
	private ShoppingCartVO ShoppingCartResponse;
	
	/**
	 * 
	 */
	private ResponseVO response;

	public ShoppingCartVO getShoppingCartResponse() {
		return ShoppingCartResponse;
	}

	public void setShoppingCartResponse(ShoppingCartVO shoppingCartResponse) {
		ShoppingCartResponse = shoppingCartResponse;
	}

	public ResponseVO getResponse() {
		return response;
	}

	public void setResponse(ResponseVO response) {
		this.response = response;
	}	
}
