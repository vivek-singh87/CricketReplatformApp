package com.cricket.endeca.droplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.ServletException;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;
import com.endeca.navigation.AssocDimLocations;
import com.endeca.navigation.AssocDimLocationsList;
import com.endeca.navigation.DimLocation;
import com.endeca.navigation.DimVal;
import com.endeca.navigation.ENEConnection;
import com.endeca.navigation.ENEQuery;
import com.endeca.navigation.ENEQueryException;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.ERec;
import com.endeca.navigation.ERecList;
import com.endeca.navigation.HttpENEConnection;
import com.endeca.navigation.Navigation;
import com.endeca.navigation.PropertyMap;
import com.endeca.navigation.UrlENEQuery;
import com.endeca.navigation.UrlENEQueryParseException;

public class PopulatePhoneBrandsList extends DynamoServlet {
	private String eneHost;
	private Integer enePort;
	
	private String workBenchPage;

	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		
		if(isLoggingDebug()) {
			logDebug("entering PopulatePhoneBrandsList service method");
		}
		String allPhonesQuery = (String)pReq.getParameter(CricketCommonConstants.QUERY_PARAM);
		String partToRemove = new StringBuilder(CricketCommonConstants.FORWARD_SLASH).append(workBenchPage).append(CricketCommonConstants.Q_MARK).toString();
		allPhonesQuery = allPhonesQuery.replace(partToRemove, CricketCommonConstants.EMPTY_STRING);
		ENEConnection nec = new HttpENEConnection(eneHost, enePort);
		ENEQuery nequery;
		ENEQueryResults qr = null;
		List<String> brands = new ArrayList<String>();
		Map<String, Map<String, String>> brandMap = new HashMap<String, Map<String, String>>();
		Map<String, String> productIdDisplayNameMap = new HashMap<String, String>();
		Map<String, String> productIdBrandNameMap = new HashMap<String, String>();
		if(isLoggingDebug()) {
			logDebug("hitting endeca with following query");
			logDebug("endeca host : " + eneHost);
			logDebug("endeca port : " + enePort);
			logDebug("endeca query : " + allPhonesQuery);
		}
		try {
			nequery = new UrlENEQuery(allPhonesQuery,CricketCommonConstants.UTF_8);
			//nequery.setDimSearchDimension(Long.parseLong("10153"));
			nequery.setNavNumERecs(100);
			qr = nec.query(nequery);
		} catch (UrlENEQueryParseException e) {
			// TODO Auto-generated catch block
			logError(e);
		} catch (ENEQueryException e) {
			// TODO Auto-generated catch block
			logError(e);
		}
		// Get navigation object result
		Navigation nav = qr.getNavigation();
		ERecList records = nav.getERecs();
		ListIterator i = records.listIterator();
		while (i.hasNext()) {
			ERec record = (ERec)i.next();
			AssocDimLocationsList dims = (AssocDimLocationsList)record.getDimValues();
			 for (int j=0; j < dims.size(); j++) {
				// Get individual dimension and loop over its values
				AssocDimLocations dim = (AssocDimLocations)dims.get(j);
				for (int k=0; k < dim.size(); k++) {
				// Get attributes from a specific dim val
					DimLocation dimLoc = (DimLocation)dim.get(k);
					DimVal dval = dimLoc.getDimValue();
					String dimensionName = dval.getDimensionName();
					String dimValName = dval.getName();
					if (dimensionName.equals(CricketCommonConstants.MANUFACTURER_DISPLAY_NAME)) {
						PropertyMap recordProperties = record.getProperties();
						String productId = (String) recordProperties.get(CricketCommonConstants.PRODUCT_REPOSITORY_ID);
						String brand = dimValName;
						if(brand != null && !brand.isEmpty()) {
							if(!productIdBrandNameMap.containsKey(productId)) {
								productIdBrandNameMap.put(productId, brand);
							}
						}
					}
					if (dimensionName.equals(CricketCommonConstants.PRODUCT_DISPLAY_NAME)) {
						PropertyMap recordProperties = record.getProperties();
						String productId = (String) recordProperties.get(CricketCommonConstants.PRODUCT_REPOSITORY_ID);
						String displayName = dimValName;
						if(displayName != null && !displayName.isEmpty()) {
							if(!productIdDisplayNameMap.containsKey(productId)) {
								productIdDisplayNameMap.put(productId, displayName);
							}
						}
					}
				}
			}
		}
		for (Map.Entry<String, String> entryBrand : productIdBrandNameMap.entrySet()) {
		    String brand = entryBrand.getValue();
			for (Map.Entry<String, String> entryName : productIdDisplayNameMap.entrySet()) {
				if(entryBrand.getKey().equals(entryName.getKey())) {
					if(brand != null && !brand.isEmpty()) {
						if(brandMap.containsKey(brand)) {
							Map<String, String> tempProdMap = brandMap.get(brand);
							if (tempProdMap == null) {
								tempProdMap = new HashMap<String, String>();
							}
							if (!tempProdMap.containsKey(entryBrand.getKey())) {
								tempProdMap.put(entryBrand.getKey(), entryName.getValue());
							}
							brandMap.put(brand, tempProdMap);
						} else if(!brandMap.containsKey(brand)) {
							Map<String, String> tempProdMap2 = new HashMap<String, String>();
							tempProdMap2.put(entryBrand.getKey(), entryName.getValue());
							brandMap.put(brand, tempProdMap2);
						}
					}
				}
			}
			
		}

		
		pReq.setParameter(CricketCommonConstants.BRAND_MAP, brandMap);
		try {
			pReq.serviceParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			logError(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logError(e);
		}
		
		if(isLoggingDebug()) {
			logDebug("exiting PopulatePhoneBrandsList service method");
		}
	}

	public String getEneHost() {
		return eneHost;
	}

	public void setEneHost(String eneHost) {
		this.eneHost = eneHost;
	}
	
	public Integer getEnePort() {
		return enePort;
	}

	public void setEnePort(Integer enePort) {
		this.enePort = enePort;
	}

	public String getWorkBenchPage() {
		return workBenchPage;
	}

	public void setWorkBenchPage(String workBenchPage) {
		this.workBenchPage = workBenchPage;
	}

}