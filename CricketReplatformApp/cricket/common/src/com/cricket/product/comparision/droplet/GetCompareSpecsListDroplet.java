package com.cricket.product.comparision.droplet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;

import atg.commerce.catalog.comparison.ProductComparisonList;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;

/**
 * CompareFeatureDisplayDroplet extends  DynamoServlet
 * @author ma112576
 * 
 */
public class GetCompareSpecsListDroplet extends DynamoServlet {
	/**
	 * Property PRODUCT of type String
	 */
	private static final String PRODUCT_LIST = "productList";
	private static final String COMPARE_SPEC_LIST = "compareSpecList";
	private static final String PHONE_COMPARE_SPECS = "phoneCompareSpecs";
	private static final String COMPARE_SPEC_NAME = "compareSpecName";

	List<String> compareSpecList = new LinkedList<String>();
	
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
	@SuppressWarnings("unchecked")
	@Override
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		
		if(isLoggingDebug()){
			logDebug("Inside GetCompareSpecsListDroplet : service method");
		}
		getCompareSpecList().clear();
		List productList = (List) pRequest
				.getObjectParameter(PRODUCT_LIST);
		

		if(productList != null) {
			for(int index=0; index<productList.size(); index++) {
				RepositoryItem productItem = ((ProductComparisonList.Entry)productList.get(index)).getProduct();
				vlogDebug("Product Id : " + productItem.getRepositoryId());
				List<RepositoryItem> phoneCompareSpecs = (List<RepositoryItem>) productItem.getPropertyValue(PHONE_COMPARE_SPECS);
				if(phoneCompareSpecs != null) {
					for(RepositoryItem phoneCompareSpec : phoneCompareSpecs) {
						String compareSpecName = (String) phoneCompareSpec.getPropertyValue(COMPARE_SPEC_NAME);
						if(!getCompareSpecList().contains(compareSpecName)) {
							vlogDebug("Spec Name : " + compareSpecName);
							getCompareSpecList().add(compareSpecName);
						}
					}
				}				
			}
		}
		if (!getCompareSpecList().isEmpty()) {
			pRequest.setParameter(COMPARE_SPEC_LIST, getCompareSpecList());
		}
			pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
		
		if(isLoggingDebug()){
			logDebug("Exit GetCompareSpecsListDroplet : service method");
		}
	}

	/**
	 * 
	 * @return the specificationMap
	 */
	public List<String> getCompareSpecList() {
		return compareSpecList;
	}

	/**
	 * 
	 * @param pSpecificationMap
	 */
	public void setCompareSpecList(List<String> pCompareSpecList) {
		compareSpecList = pCompareSpecList;
	}	

}
