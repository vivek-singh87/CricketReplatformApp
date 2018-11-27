package com.cricket.search.droplet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;
import com.endeca.infront.cartridge.model.Attribute;
import com.endeca.infront.cartridge.model.Record;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class GetProductTypesFromSearchResultsDroplet extends DynamoServlet {
	
private static final String PHONE_PRODUCT = "phone-product";
	
	private static final String PLAN_PRODUCT = "plan-product";
	
	private static final String ACCESSORY_PRODUCT = "accessory-product";
	
	private static final String ADDON_PRODUCT = "addOn-product";
	
	private String typeDistinguishProperty = "common.id";
	
	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) throws ServletException, IOException {
		if(isLoggingDebug()) {
			logDebug("entering service method of GetProductTypesFromSearchResultsDroplet");
		}
		List<Record> records = (List<Record>)pReq.getObjectParameter("records");
		String productType = null;
		if(records != null) {
			for (Record record : records) {
				Map<String, Attribute> recordProps = record.getAttributes();
				Attribute productTypeAttr = recordProps.get(typeDistinguishProperty);
				if (productTypeAttr != null) {
					String commonId = (String)productTypeAttr.get(0);
					if (commonId.contains(PHONE_PRODUCT)) {
						productType = "Phones";
					}
					if (commonId.contains(PLAN_PRODUCT)) {
						productType = "Plans";
					}
					if (commonId.contains(ACCESSORY_PRODUCT)) {
						productType = "Accessories";
					}
					if (commonId.contains(ADDON_PRODUCT)) {
						productType = "AddOns";
					}
				}
				if(productType != null && !productType.isEmpty()) {
					break;
				}
			}
			if(isLoggingDebug()) {
				logDebug("product type determined was :: " + productType + ":: this will be "
						+ "used in case only one type of result is returned by endeca for a search query");
			}
			pReq.setParameter("productType", productType);
			pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
		}
	}

	public String getTypeDistinguishProperty() {
		return typeDistinguishProperty;
	}

	public void setTypeDistinguishProperty(String typeDistinguishProperty) {
		this.typeDistinguishProperty = typeDistinguishProperty;
	}

}
