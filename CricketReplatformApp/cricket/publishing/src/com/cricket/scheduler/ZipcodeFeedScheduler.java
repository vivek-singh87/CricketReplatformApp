package com.cricket.scheduler;

import com.cricket.integration.zipcode.ZIPCodePublishingManager;

import atg.process.action.ActionException;
import atg.service.scheduler.SchedulableService;
import atg.service.scheduler.Schedule;
import atg.service.scheduler.ScheduledJob;
import atg.service.scheduler.Scheduler;
import atg.dtm.TransactionDemarcationException;
import atg.versionmanager.exceptions.VersionException;
import atg.workflow.WorkflowException;

public class ZipcodeFeedScheduler extends SchedulableService {

	int jobId;
	Scheduler scheduler;
	Schedule schedule;
	boolean enabled;

	private ZIPCodePublishingManager mImportService;

	/**
	 * @return the importService
	 */
	public ZIPCodePublishingManager getImportService() {
		return mImportService;
	}

	/**
	 * @param pImportService
	 *            the importService to set
	 */
	public void setImportService(ZIPCodePublishingManager pImportService) {
		mImportService = pImportService;
	}
	
	/**
	 * 
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 
	 * @param pEnabled
	 */
	public void setEnabled(boolean pEnabled) {
		enabled = pEnabled;
	}

	@Override
	public void performScheduledTask(Scheduler pArg0, ScheduledJob pArg1) {
		// TODO Auto-generated method stub		
		if (isLoggingDebug()) {
			logDebug("START Inside performScheduledTask Method");
		}
		vlogDebug("Scheduler Enabled flag is " + isEnabled());
		try {
			if(isEnabled()){
				getImportService().executeImportZipcodeFeed();				
			}
		} catch (WorkflowException workflowException) {
			vlogError("WorkflowException  in class ZipcodeFeedScheduler : " + workflowException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a WorkflowException while calling the ZIPCodePublishingManager class  of performScheduledTask method");
			}
		} catch (TransactionDemarcationException transactionDemarcationException) {
			vlogError("TransactionDemarcationException  in class ZipcodeFeedScheduler : " + transactionDemarcationException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a TransactionDemarcationException while calling the ZIPCodePublishingManager class  of performScheduledTask method");
			}
		} catch (VersionException versionException) {
			vlogError("VersionException  in class ZipcodeFeedScheduler : " + versionException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a VersionException while calling the ZIPCodePublishingManager class  of performScheduledTask method");
			}
		} catch (ActionException actionException) {
			vlogError("ActionException  in class ZipcodeFeedScheduler : " + actionException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a ActionException while calling the ZIPCodePublishingManager class  of performScheduledTask method");
			}
		} catch (Exception exception) {
			vlogError("Exception  in class ZipcodeFeedScheduler : " + exception.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a Exception while calling the ZIPCodePublishingManager class  of performScheduledTask method");
			}
		}
		if (isLoggingDebug()) {
			logDebug("END Inside performScheduledTask Method");
		}
	}

}
