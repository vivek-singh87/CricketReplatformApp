package com.cricket.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class FilterProductsBasedOnLocation extends DynamoServlet {
	public static final String PRODUCT_LIST = "productList";
	public static final String USER_LOCATION = "userLocation";
	public static final String FILTERED_PRODUCTS = "filteredChildProducts";
	
	public String locationProperty = "zipCode";
	

	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		List <RepositoryItem> childProducts = (List <RepositoryItem>) pReq.getObjectParameter(PRODUCT_LIST);
		List <RepositoryItem> filteredChildProducts = new ArrayList<RepositoryItem>();
		String userLocation = pReq.getParameter(USER_LOCATION);
		for (RepositoryItem childProduct : childProducts) {
			List <RepositoryItem> markets = (List<RepositoryItem>) childProduct.getPropertyValue(CricketCommonConstants.PROP_MARKETS);
			if (markets != null) {
				for (RepositoryItem market : markets) {
					String location = (String) market.getPropertyValue(getLocationProperty());
					if (location.equalsIgnoreCase(userLocation)) {
						filteredChildProducts.add(childProduct);
						break;
					}
				}
			}
		}
		if(userLocation == null || userLocation.isEmpty()) {
			filteredChildProducts = childProducts;
		}
		pReq.setParameter(FILTERED_PRODUCTS, filteredChildProducts);
		try {
			pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
		} catch (ServletException e) {
			if (isLoggingError()) {
				logError("there was a problem servicing the required content inside FilterProductsBasedOnLocation droplet:" + e);
			}
		} catch (IOException e) {
			if (isLoggingError()) {
				logError("there was a problem servicing the required content inside FilterProductsBasedOnLocation droplet:" + e);		
			}
		}
	}
	
	public String getLocationProperty() {
		return locationProperty;
	}

	public void setLocationProperty(String locationProperty) {
		this.locationProperty = locationProperty;
	}
}