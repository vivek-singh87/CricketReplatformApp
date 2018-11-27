package com.cricket.integration.zipcode;


import javax.ejb.CreateException;

import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;
import atg.process.action.ActionException;
import atg.versionmanager.exceptions.VersionException;
import atg.workflow.WorkflowException;


public class ZIPCodePublishingManager extends GenericService{
	
	private ZIPCodeDeploymentTools mDeployTools;

	/**
	 * @return the deployTools
	 */
	public ZIPCodeDeploymentTools getDeployTools() {
		return mDeployTools;
	}

	/**
	 * @param pDeployTools the deployTools to set
	 */
	public void setDeployTools(ZIPCodeDeploymentTools pDeployTools) {
		mDeployTools = pDeployTools;
	}

	
	public void executeImportZipcodeFeed() throws VersionException, WorkflowException, CreateException, ActionException,
    TransactionDemarcationException, Exception{
		try {
			getDeployTools().executeImportZipcodeFeed();
		} catch (WorkflowException workflowException) {
			vlogError("WorkflowException of executeImportZipcodeFeed method of class ZIPCodePublishingManager : " + workflowException.getMessage());
			throw workflowException;
		} catch (TransactionDemarcationException transactionDemarcationException) {
			vlogError("TransactionDemarcationException of executeImportZipcodeFeed method of class ZIPCodePublishingManager : " + transactionDemarcationException.getMessage());
			throw transactionDemarcationException;
		} catch (VersionException versionException) {
			vlogError("VersionException of executeImportZipcodeFeed method of class ZIPCodePublishingManager : " + versionException.getMessage());
			throw versionException;
		} catch (CreateException createException) {
			vlogError("CreateException of executeImportZipcodeFeed method of class ZIPCodePublishingManager : " + createException.getMessage());
			throw createException;
		} catch (ActionException actionException) {
			vlogError("ActionException of executeImportZipcodeFeed method of class ZIPCodePublishingManager : " + actionException.getMessage());
			throw actionException;
		} catch (Exception exception) {
			vlogError("Exception of executeImportZipcodeFeed method of class ZIPCodePublishingManager : " + exception.getMessage());
			throw exception;
		}
	}
	
}
