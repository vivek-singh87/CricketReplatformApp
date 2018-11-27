package com.cricket.browse;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class DetermineCategoryTemplate extends DynamoServlet {
	

	private Repository productCatalog;
	
	private String searchPageUrl;
	
	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		
		String categoryId = pReq.getParameter(CricketCommonConstants.CATEGORY_ID);
		String templateUrl = CricketCommonConstants.EMPTY_STRING;
		try {
			if (categoryId != null && !categoryId.isEmpty()) {
				RepositoryItem categoryItem =  getProductCatalog().getItem(categoryId, CricketCommonConstants.CATEGORY);
				RepositoryItem template = null;
				
				if (categoryItem != null) {
					template = (RepositoryItem)categoryItem.getPropertyValue(CricketCommonConstants.TEMPLATE);
					if(template != null) {
						templateUrl = (String)template.getPropertyValue(CricketCommonConstants.URL);
					}
				}
			} else {
					templateUrl = searchPageUrl;
			}
			if (templateUrl == null || templateUrl.isEmpty()) {
				templateUrl = searchPageUrl;
			}
			pReq.setParameter(CricketCommonConstants.TEMPLATE_URL, templateUrl);
			pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
		} catch (RepositoryException e) {
			if (isLoggingError()) {
				logError("there was an error while querying item with repository id :" + categoryId);
			}
		} catch (ServletException e) {
			if (isLoggingError()) {
				logError("there was an error while servicing the request :" + categoryId);
			}
		} catch (IOException e) {
			if (isLoggingError()) {
				logError("there was an error while servicing the request :" + categoryId);
			}
		}
	}

	public Repository getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(Repository productCatalog) {
		this.productCatalog = productCatalog;
	}

	public String getSearchPageUrl() {
		return searchPageUrl;
	}

	public void setSearchPageUrl(String searchPageUrl) {
		this.searchPageUrl = searchPageUrl;
	}

}
