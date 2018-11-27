package com.cricket.test;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import atg.repository.RepositoryException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

import com.cricket.common.ws.CricketPublishingWS;
import com.cricket.inventory.response.UpdateWebProductResponseInfo;


public class TestFormHandler {

	private CricketPublishingWS cricketPublishingWS;
	/**
	 * Variable that holds the path where the XML file created 
	 */
	private String feedFilePath;

	public CricketPublishingWS getCricketPublishingWS() {
		return cricketPublishingWS;
	}

	public void setCricketPublishingWS(CricketPublishingWS cricketPublishingWS) {
		this.cricketPublishingWS = cricketPublishingWS;
	}
	
	/**
	 * @return the feedFilePath
	 */
	public String getFeedFilePath() {
		return feedFilePath;
	}
	/**
	 * @param feedFilePath the feedFilePath to set
	 */
	public void setFeedFilePath(String feedFilePath) {
		this.feedFilePath = feedFilePath;
	}
	
	public void handleSubmitInventory(final DynamoHttpServletRequest pRequest,
			final DynamoHttpServletResponse pResponse) throws ServletException,
			IOException, RepositoryException {
		UpdateWebProductResponseInfo invInfo = new UpdateWebProductResponseInfo();
		File inventoryDataFile = null;
		inventoryDataFile = new File(getFeedFilePath());
		getCricketPublishingWS().getInvPubManager().executeImportFeed(inventoryDataFile, invInfo);		

	} 
}
