package com.cricket.inventory;

public class InventoryResponseVO {
	
	private boolean isOutOfStock;
	
	private String thresholdMessage;

	public boolean isOutOfStock() {
		return isOutOfStock;
	}

	public void setOutOfStock(boolean isOutOfStock) {
		this.isOutOfStock = isOutOfStock;
	}

	public String getThresholdMessage() {
		return thresholdMessage;
	}

	public void setThresholdMessage(String thresholdMessage) {
		this.thresholdMessage = thresholdMessage;
	}

}
