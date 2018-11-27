package com.cricket.common.ws;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import atg.core.util.StringUtils;
import atg.nucleus.GenericService;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.inventory.InventoryPublishingManager;
import com.cricket.inventory.response.UpdateWebProductResponseInfo;


/**
 * 
 * @author Cricket Communications
 *
 */
public class CricketPublishingWS extends GenericService{
	
	/**
	 * Property holding reference of InventoryPublishingManager
	 */
	private InventoryPublishingManager mInvPubManager;
	/**
	 * Variable that holds the path where the XML file created 
	 */
	private String feedFilePath;
	
	/**
	 * Method updateWebProducts to update the Web products and getting the Response status
	 * @param inventoryFeedXML - request parameter of type String
	 * @return invInfo - UpdateWebProductResponseInfo
	 */
	public String updateWebProduct(final String inventoryFeedXML)
	{
		if (isLoggingDebug()) {
			logDebug("Start updateWebProduct");
			logDebug("inventoryFeedXML     : " + inventoryFeedXML);
		}
		UpdateWebProductResponseInfo invInfo = new UpdateWebProductResponseInfo();
		String result = CricketCommonConstants.EMPTY_STRING;
		// Checking if passing String parameter is empty or not
		if(!(StringUtils.isEmpty(inventoryFeedXML))){
			File inventoryDataFile = null;
			try {
				inventoryDataFile = new File(getFeedFilePath());
				if (!inventoryDataFile.exists()) {
					inventoryDataFile.createNewFile();
				}
				if (isLoggingDebug()) {
					logDebug("Inventory Feed Feed file :  :   "+inventoryDataFile);
				}
				BufferedWriter bw = new BufferedWriter(new FileWriter(inventoryDataFile));
				bw.write(inventoryFeedXML);
				bw.close();
			} catch (IOException e) {
				logError("IOException : ", e);
			}
		    result =  getInvPubManager().executeImportFeed(inventoryDataFile, invInfo);
		}	
		else 
			result= "Input parameter 'inventoryFeedXML' for updateWebProduct is empty";
		
		if (isLoggingDebug()) {
			logDebug("Exit updateWebProduct");
			logDebug("Response      : " + result);
		}
		return result;
	}
	/**
	 * @return the mInvPubManager
	 */
	public InventoryPublishingManager getInvPubManager() {
		return mInvPubManager;
	}

	/**
	 * @param pInvPubManager the mInvPubManager to set
	 */
	public void setInvPubManager(final InventoryPublishingManager pInvPubManager) {
		this.mInvPubManager = pInvPubManager;
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
	
	
	
}
