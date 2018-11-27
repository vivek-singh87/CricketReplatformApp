package com.cricket.product.comparision.droplet;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

/**
 * CompareFeatureDisplayDroplet extends  DynamoServlet
 * @author ma112576
 * 
 */
public class CompareFeatureDisplayDroplet extends DynamoServlet {
	/**
	 * Property PRODUCT of type String
	 */
	private static final String PRODUCT = "product";
	/**
	 * Property SIZE of type String
	 */
	private static final String SIZE = "size";
	/**
	 * Property COUNT of type String
	 */
	private static final String COUNT = "count";
	/**
	 * Property SPECIFICATION_MAP of type String
	 */
	private static final String SPECIFICATION_MAP = "SpecificationsMap";
	/**
	 * Property SPECIFICATION_OUTPUT of type String
	 */
	private static final String SPECIFICATION_OUTPUT = "SpecificationsOutput";
	/*
	 * property : specificationMap
	 */
	Map<String, Set<Object>> specificationMap = new HashMap<String, Set<Object>>();
	
	/**
	 * .The method is used to compare product specification and add it to Map
	 * 
	 * @param pRequest
	 *            - DynamoHttpServletRequest
	 * @param pResponse
	 *            - DynamoHttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 * */
	@Override
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		
		if(isLoggingDebug()){
			logDebug("Inside CompareFeatureDisplayDroplet : service method");
		}
		RepositoryItem productItem = (RepositoryItem) pRequest
				.getObjectParameter(PRODUCT);
		String size = pRequest.getParameter(SIZE);
		String count = pRequest.getParameter(COUNT);

		if(isLoggingDebug()){
			logDebug("Product Item :::"+productItem);
		}
		Map<String, Set<Object>> specificationMap = getSpecifications(productItem,pRequest);
		if (!specificationMap.isEmpty()) {
			pRequest.setParameter(SPECIFICATION_MAP, specificationMap);
		}
		if(size.equals(count)){
			pRequest.serviceLocalParameter(SPECIFICATION_OUTPUT, pRequest, pResponse);
		}
		
		if(isLoggingDebug()){
			logDebug("Exit CompareFeatureDisplayDroplet : service method");
		}
	}
	
	/**
	 * .The method is used to compare product specification and add it to Map
	 * 
	 * @param pProductItem
	 *            - RepositoryItem
	 * @param pRequest
	 *            - DynamoHttpServletRequest
	 * @return Map
	 * */
	private Map<String, Set<Object>> getSpecifications(RepositoryItem pProductItem, DynamoHttpServletRequest pRequest) {
		
		if(isLoggingDebug()){
			logDebug("Inside GetSpecifications method::"+pProductItem);
		}
		
		Map<String, Set<Object>> specificationMap = getSpecificationMap();
		List phoneSpecifications = (List) pProductItem
				.getPropertyValue(CricketCommonConstants.PHONE_SPECIFICATIONS);
		for (Object object : phoneSpecifications) {
			RepositoryItem reposItem = (RepositoryItem) object;
			String gropName = (String) reposItem.getPropertyValue(CricketCommonConstants.SPEC_GROUP_NAME);
			List specifications = (List) reposItem.getPropertyValue(CricketCommonConstants.SPECIFICATIONS);
			Set<Object> phoneSpec = new HashSet<Object>();
			for(Object specif : specifications){
				RepositoryItem specItem = (RepositoryItem) specif;
				String spc = (String) specItem.getPropertyValue(CricketCommonConstants.SPEC_NAME);
				phoneSpec.add(spc);
			}
			if(!specificationMap.isEmpty() && specificationMap.containsKey(gropName)){
				Set<Object> specValues = specificationMap.get(gropName);
				phoneSpec.addAll(specValues);
			}
			specificationMap.put(gropName, phoneSpec);
		}
		
		if(isLoggingDebug()){
			logDebug("Exit GetSpecifications method::"+pProductItem);
		}
		return specificationMap;
	}


	/**
	 * @return the specificationMap
	 */
	public Map<String, Set<Object>> getSpecificationMap() {
		return specificationMap;
	}

	/**
	 * @param specificationMap the specificationMap to set
	 */
	public void setSpecificationMap(Map<String, Set<Object>> specificationMap) {
		this.specificationMap = specificationMap;
	}

	

}
