package com.cricket.endeca.price;

import atg.servlet.DynamoServlet;

public class PriceRangeVO extends DynamoServlet {
	
	private String rangeDisplay;
	
	private String navLink;

	public String getRangeDisplay() {
		return rangeDisplay;
	}

	public void setRangeDisplay(String rangeDisplay) {
		this.rangeDisplay = rangeDisplay;
	}

	public String getNavLink() {
		return navLink;
	}

	public void setNavLink(String navLink) {
		this.navLink = navLink;
	}
	
	
}