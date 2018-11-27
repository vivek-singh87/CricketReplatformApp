package com.cricket.integration.zipcode;

import java.io.FileNotFoundException;
import java.io.IOException;

import atg.nucleus.GenericService;


public class CricketZipcodeFeedReader extends GenericService {

	private ZipCodeFeedManager mZipCodeFeedManager;

	/**
	 * @return the zipCodeFeedManager
	 */
	public ZipCodeFeedManager getZipCodeFeedManager() {
		return mZipCodeFeedManager;
	}

	/**
	 * @param pZipCodeFeedManager
	 *            the zipCodeFeedManager to set
	 */
	public void setZipCodeFeedManager(ZipCodeFeedManager pZipCodeFeedManager) {
		mZipCodeFeedManager = pZipCodeFeedManager;
	}

	public void readZipcodeFeed() {
		if (isLoggingDebug()) {
			logDebug("Start Inside readZipcodeFeed Method");
		}
		try {
			// Remove the records from database
			getZipCodeFeedManager().removeRecord();
			//	insert the records into database
			getZipCodeFeedManager().readMapBeanCSVFile();

		} catch (FileNotFoundException fileNotFoundException) {	
			vlogError("FileNotFoundException : ", fileNotFoundException);
			if (isLoggingDebug()) {
				logDebug("There was an FileNotFoundException while servicing the zipcode feed Data in class CricketZipcodeFeedReader");
			}
		} catch (IOException ioException) {
			vlogError("IOException : ", ioException);
			if (isLoggingError()) {
				logError("There was an IOException while servicing the zipcode feed Data in class CricketZipcodeFeedReader");
			}			
		}
		if (isLoggingDebug()) {
			logDebug("End Inside readZipcodeFeed Method");
		}
	}

}
