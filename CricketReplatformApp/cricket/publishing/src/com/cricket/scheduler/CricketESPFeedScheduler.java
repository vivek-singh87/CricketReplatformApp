package com.cricket.scheduler;

import java.util.Date;

import atg.repository.ItemDescriptorImpl;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.service.scheduler.SchedulableService;
import atg.service.scheduler.Schedule;
import atg.service.scheduler.ScheduledJob;
import atg.service.scheduler.Scheduler;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.integration.parc.PARCPublishingManager;
import com.cricket.integration.pim.PIMPublishingManager;

/**
 * The scheduler component which fetches the ESP feeds logged in the CKT_FEED_LOGGER table
 * periodically and processes them
 *  
 * @author Tech Mahindra
 *
 */
public class CricketESPFeedScheduler extends SchedulableService {

	/**
	 * Variable to hold jobId
	 */
	private int jobId;
	/**
	 * Variable to hold scheduler
	 */
	private Scheduler scheduler;
	/**
	 * Variable to hold schedule
	 */
	private Schedule schedule;
	/**
	 * Variable to hold mParcPubManager
	 */
	private PARCPublishingManager mParcPubManager;
	/**
	 * Variable to hold mPimPubManager
	 */
	private PIMPublishingManager mPimPubManager;
	/**
	 * Variable to hold mEspFeedLogRepository
	 */
	private MutableRepository mEspFeedLogRepository;

	/**
	 * The method which is invoked out of the box for the scheduled time specified in the 
	 * Schedule property. Checks the source system which sent the feed and calls corresponding 
	 * method for processing the feed.
	 */
	@Override
	public void performScheduledTask(Scheduler scheduler, ScheduledJob job) {
		RepositoryView repoView;
		RepositoryItem newFeedsList[] = null;
		String sourceSystem = null;
		MutableRepositoryItem feedItemForUpdate = null;
		boolean feedUpdateStatus = false;
		ItemDescriptorImpl espFeedItemDesc = null;
		Date date = new Date();
		vlogDebug("START Inside performScheduledTask Method");
		try {
			espFeedItemDesc = (ItemDescriptorImpl)getEspFeedLogRepository().
								getItemDescriptor(CricketCommonConstants.ESP_FEED_ITEM_DESC);
			espFeedItemDesc.invalidateCaches();
			repoView = getEspFeedLogRepository().getView(CricketCommonConstants.ESP_FEED_ITEM_DESC);
			RqlStatement statement = RqlStatement.parseRqlStatement(CricketCommonConstants.FEED_STATUS_QUERY);
			Object[] inputParams = new Object[1];
			inputParams[0] = CricketCommonConstants.STATUS_NEW;			
			newFeedsList = statement.executeQuery(repoView, inputParams);
			if(newFeedsList != null && newFeedsList.length > 0) {
				for(RepositoryItem newFeed : newFeedsList) {
					sourceSystem = (String)(newFeed).getPropertyValue(CricketCommonConstants.PROPERTY_SOURCE_SYSTEM);
					vlogDebug("Source System : " + sourceSystem);
					vlogDebug("Feed ID : " + newFeed.getRepositoryId());
					vlogDebug("Feed status before processing : " + feedUpdateStatus);
					if(sourceSystem.equalsIgnoreCase(CricketCommonConstants.PARC)) {
						feedUpdateStatus = getParcPubManager().executeImportPlans(newFeed);					
					} else if(sourceSystem.equalsIgnoreCase(CricketCommonConstants.PIM)) {
						feedUpdateStatus = getPimPubManager().executeImportDevices(newFeed);
					}
					feedItemForUpdate = (MutableRepositoryItem)newFeed;
					if(feedUpdateStatus) {
						feedItemForUpdate.setPropertyValue(CricketCommonConstants.
												PROPERTY_STATUS, CricketCommonConstants.STATUS_PROCESSED);
						feedItemForUpdate.setPropertyValue(CricketCommonConstants.
												PROPERTY_DESCRIPTION, CricketCommonConstants.DESC_FEED_SUCCESS);
						feedItemForUpdate.setPropertyValue(CricketCommonConstants.
												PROPERTY_UPDATION_DATE, date.toString());
						vlogDebug("Feed status after processing : " + CricketCommonConstants.STATUS_PROCESSED);
						getEspFeedLogRepository().updateItem(feedItemForUpdate);
					} else {
						feedItemForUpdate.setPropertyValue(CricketCommonConstants.
												PROPERTY_STATUS, CricketCommonConstants.STATUS_FAILED);
						feedItemForUpdate.setPropertyValue(CricketCommonConstants.
												PROPERTY_DESCRIPTION, CricketCommonConstants.DESC_FEED_FAIL);
						feedItemForUpdate.setPropertyValue(CricketCommonConstants.
												PROPERTY_UPDATION_DATE, date.toString());
						vlogDebug("Feed status after processing : " + CricketCommonConstants.STATUS_FAILED);
						getEspFeedLogRepository().updateItem(feedItemForUpdate);					
					}
				}
			} else {
				vlogDebug("No Feeds to Process");
			}
		} catch (RepositoryException e) {  
				vlogError("Repository Exception in performScheduledTask of CricketESPFeedScheduler : Message",e.getMessage());
				vlogError("Repository Exception in performScheduledTask of CricketESPFeedScheduler : Cause",e.getCause());
		} catch (Exception exception) {
			vlogError("Exception  in class CricketESPFeedScheduler : Message : " + exception.getMessage());
			vlogError("Exception  in class CricketESPFeedScheduler : Cause : " + exception.getCause());
		}
		vlogDebug("END Inside performScheduledTask Method");
	}
	
	/**
	 * Returns the reference to PARCPublishingManager component
	 * 
	 * @return the mParcPubManager
	 */
	public PARCPublishingManager getParcPubManager() {
		return mParcPubManager;
	}

	/**
	 * Sets the reference to PARCPublishingManager component
	 * 
	 * @param mParcPubManager
	 */
	public void setParcPubManager(PARCPublishingManager mParcPubManager) {
		this.mParcPubManager = mParcPubManager;
	}

	/**
	 * Returns the reference to PIMPublishingManager component
	 * 
	 * @return the mPimPubManager
	 */
	public PIMPublishingManager getPimPubManager() {
		return mPimPubManager;
	}

	/**
	 * Sets the reference to PIMPublishingManager component
	 * 
	 * @param mPimPubManager
	 */
	public void setPimPubManager(PIMPublishingManager mPimPubManager) {
		this.mPimPubManager = mPimPubManager;
	}

	/**
	 * Returns the reference to EspFeedLogRepository component
	 * @return the mEspFeedLogRepository
	 */ 
	public MutableRepository getEspFeedLogRepository() {
		return mEspFeedLogRepository;
	}

	/**
	 * Sets the reference to EspFeedLogRepository component
	 * 
	 * @param espFeedLogRepository
	 */
	public void setEspFeedLogRepository(MutableRepository mEspFeedLogRepository) {
		this.mEspFeedLogRepository = mEspFeedLogRepository;
	}

}
