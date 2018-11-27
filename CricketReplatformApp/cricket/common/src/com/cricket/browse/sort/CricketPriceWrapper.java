package com.cricket.browse.sort;

public class CricketPriceWrapper {
	private double finalPrice;
	
	private Object productVO;
	
	
	CricketPriceWrapper(Object pProduct, double pPrice){
		setProductVO(pProduct);
		setFinalPrice(pPrice);
	}
	
	public String toString(){
	    return this.getProductVO() + " " + this.getFinalPrice();
	}



	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public Object getProductVO() {
		return productVO;
	}

	public void setProductVO(Object productVO) {
		this.productVO = productVO;
	}
	
}