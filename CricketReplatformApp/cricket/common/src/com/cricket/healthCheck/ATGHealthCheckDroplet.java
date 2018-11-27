package com.cricket.healthCheck;

import java.io.IOException;

import javax.servlet.ServletException;

import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;

public class ATGHealthCheckDroplet extends DynamoServlet {

	private Repository sdsRepository;
	
	private String itemId;
	
	private static final String LIVE_DATA_SOURCE = "LIVE_DATA_SOURCE";
	
	private static final String SDS = "sds";
	
	private static final String CURRENT_DATA_SOURCE_NAME = "currentDataSourceName";

	public Repository getSdsRepository() {
		return sdsRepository;
	}

	public void setSdsRepository(Repository sdsRepository) {
		this.sdsRepository = sdsRepository;
	}
	
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		try {
		RepositoryItem sdsItem = getSdsRepository().getItem(getItemId(),SDS);
		if (null != sdsItem) {
			String currentDataSourceName = (String) sdsItem.getPropertyValue(CURRENT_DATA_SOURCE_NAME);
			pRequest.setParameter(LIVE_DATA_SOURCE, currentDataSourceName);
		}
			} catch (RepositoryException e) {      
				if(isLoggingError()){
					logError("Repository Exception in getCommerceItemsForPackage of UserIntentionServlet",e);
				}
			}
		pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
	}
}
