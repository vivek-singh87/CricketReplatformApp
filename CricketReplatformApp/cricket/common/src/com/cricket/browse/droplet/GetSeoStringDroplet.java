package com.cricket.browse.droplet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class GetSeoStringDroplet extends DynamoServlet {
	
	public void service(DynamoHttpServletRequest pRequest,DynamoHttpServletResponse pResponse) {
		
		RepositoryItem product = (RepositoryItem)pRequest.getObjectParameter(CricketCommonConstants.PRODUCT);
		String seoString = null;
		if(product != null) {
			List<RepositoryItem> parentCategories = (List<RepositoryItem>)product.getPropertyValue(CricketCommonConstants.ANCESTOR_CATEGORIES);
			if(parentCategories != null) {
				for (RepositoryItem parentCategory : parentCategories) {
					seoString = (String)parentCategory.getPropertyValue(CricketCommonConstants.SEO_STRING);
					if(seoString != null && !seoString.isEmpty()) {
						break;
					}
				}
			}
 		}
		pRequest.setParameter(CricketCommonConstants.SEO_STRING, seoString);
		try {
			pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
		} catch (ServletException e) {
			logError(e);
		} catch (IOException e) {
			logError(e);
		}
	}

}
