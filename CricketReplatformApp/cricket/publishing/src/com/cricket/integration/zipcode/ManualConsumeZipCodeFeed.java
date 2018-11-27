package com.cricket.integration.zipcode;

import atg.droplet.GenericFormHandler;
import atg.dtm.TransactionDemarcationException;
import atg.process.action.ActionException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.versionmanager.exceptions.VersionException;
import atg.workflow.WorkflowException;

public class ManualConsumeZipCodeFeed extends GenericFormHandler {

	private ZIPCodePublishingManager feedReader;

	/**
	 * @return the feedReader
	 */
	public ZIPCodePublishingManager getFeedReader() {
		return feedReader;
	}

	/**
	 * @param pFeedReader
	 *            the feedReader to set
	 */
	public void setFeedReader(ZIPCodePublishingManager pFeedReader) {
		feedReader = pFeedReader;
	}

	public void handleManualConsume(DynamoHttpServletRequest pReq,
			DynamoHttpServletResponse pRes) {
		try {
			feedReader.executeImportZipcodeFeed();
		} catch (WorkflowException workflowException) {
			vlogError("WorkflowException  in class ManualConsumeZipCodeFeed : " + workflowException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a WorkflowException while calling the ZIPCodePublishingManager class  of inhandleManualConsume method");
			}
		} catch (TransactionDemarcationException transactionDemarcationException) {
			vlogError("TransactionDemarcationException in class ManualConsumeZipCodeFeed : " + transactionDemarcationException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a TransactionDemarcationException while calling the ZIPCodePublishingManager class of inhandleManualConsume method" + transactionDemarcationException);
			}	
		} catch (VersionException versionException) {
			vlogError("VersionException  in class ManualConsumeZipCodeFeed : " + versionException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a VersionException while calling the ZIPCodePublishingManager class  of inhandleManualConsume method");
			}			
		} catch (ActionException actionException) {
			vlogError("ActionException  in class ManualConsumeZipCodeFeed : " + actionException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a ActionException while calling the ZIPCodePublishingManager class  of inhandleManualConsume method");
			}	
		} catch (Exception exception) {
			vlogError("Exception  in class ManualConsumeZipCodeFeed : " + exception.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a Exception while calling the ZIPCodePublishingManager class  of inhandleManualConsume method");
			}	
		}

	}

}
