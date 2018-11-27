package com.cricket.product.comparision.droplet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;

/**
 * CompareSpecificationDroplet extends  DynamoServlet
 * @author ma112576
 * 
 */
public class CompareSpecificationDroplet extends DynamoServlet {

	/**
	 * Property PRODUCT of type String
	 */
	private static final String PRODUCT = "product";
	/**
	 * Property GROUP_NAME of type String
	 */
	/*private static final String GROUP_NAME = "groupName";*/
	/**
	 * Property SPECIFICATION of type String
	 */
	private static final String SPECIFICATION = "specification";
	/**
	 * Property SPEC_VALUE of type String
	 */
	private static final String SPEC_VALUE = "SpecValue";
	/**
	 * Property OUTPUT of type String
	 */
	private static final String OUTPUT = "Output";
	/**
	 * Property PHONE_SPECS_FOR_COMPARE of type String
	 */
	private static final String PHONE_SPECS_FOR_COMPARE = "phoneCompareSpecs";
	/**
	 * Property COMPARE_SPEC_NAME of type String
	 */
	private static final String COMPARE_SPEC_NAME = "compareSpecName";
	/**
	 * Property COMPARE_SPEC_NAME of type String
	 */
	private static final String COMPARE_SPEC_VALUE = "compareSpecValue";
	
	/**
	 * .The method is used to compare product specification 
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
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {

		if (isLoggingDebug()) {
			logDebug("Inside CompareFeatureDroplet : Service method");
		}
		RepositoryItem productItem = (RepositoryItem) pRequest
				.getObjectParameter(PRODUCT);
		/*String groupName = pRequest.getParameter(GROUP_NAME);*/
		String specification = pRequest.getParameter(SPECIFICATION);

		if(isLoggingDebug()){
			logDebug("Product Item :::"+productItem);
		}
		
		List phoneSpecifications = (List) productItem
				.getPropertyValue(PHONE_SPECS_FOR_COMPARE);
		/*for (Object object : phoneSpecifications) {
			RepositoryItem reposItem = (RepositoryItem) object;
			String productGropName = (String) reposItem
					.getPropertyValue(CricketCommonConstants.SPEC_GROUP_NAME);
			if (productGropName.equals(groupName)) {
				List specifications = (List) reposItem
						.getPropertyValue(CricketCommonConstants.SPECIFICATIONS);*/
				String spcValue = null;
				for (Object specif : phoneSpecifications) {
					RepositoryItem specItem = (RepositoryItem) specif;
					String spc = (String) specItem.getPropertyValue(COMPARE_SPEC_NAME);
					if (specification.equalsIgnoreCase(spc)) {
						spcValue = (String) specItem.getPropertyValue(COMPARE_SPEC_VALUE);
						spcValue = spcValue != null ? spcValue : CricketCommonConstants.YES; 
						pRequest.setParameter(SPEC_VALUE, spcValue);
						break;
					} else {
						pRequest.setParameter(SPEC_VALUE, CricketCommonConstants.NO);
					}
				}
				/*} else {
				pRequest.setParameter(SPECIFICATIONS, CricketCommonConstants.NO);

			}

		}*/

		pRequest.serviceLocalParameter(OUTPUT, pRequest, pResponse);

		if (isLoggingDebug()) {
			logDebug("Exit CompareFeatureDroplet : Service method");
		}
	}

}
