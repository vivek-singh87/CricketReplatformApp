/**
 * 
 */
package com.cricket.order.abandoned;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import atg.commerce.CommerceException;
import atg.commerce.order.abandoned.AbandonedOrderService;
import atg.dtm.TransactionDemarcationException;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.service.scheduler.ScheduledJob;
import atg.service.scheduler.Scheduler;

import com.cricket.commerce.order.CricketOrderImpl;

/**
 * @author Cricket
 * 
 */
public class CricketAbandonedOrderService extends AbandonedOrderService {

	private CricketReportGeneration mCricketReportGen;

	private String currentDateFormat;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * atg.commerce.order.abandoned.AbandonedOrderService#processAbandonedOrders
	 * ()
	 */
	@Override
	public void processAbandonedOrders() throws CommerceException {

		List<CricketOrderImpl> abandonedOrders = findCricketAbandonedOrders();
		if(abandonedOrders.isEmpty()){
			logDebug("There are no Abandoned Orders to generate the CSV file  "+ new java.util.Date());
			return;
		}
		else{
		generateCSVFileTransferToFTP(abandonedOrders);
		deleteGeneratedFile();
		super.processAbandonedOrders();
		}
	}

	
	
	/**
	 * @param pAbandonedOrders
	 */
	private void generateCSVFileTransferToFTP(
			List<CricketOrderImpl> pAbandonedOrders) {
		
		getCricketReportGen().generateCSVfileforAbandonedOrders(
				pAbandonedOrders);
		
	}



	private void deleteGeneratedFile() {
		File csvFile = getCricketReportGen().getFileToSave();
		boolean status = csvFile.delete();
		if(status && isLoggingDebug()){
			 logDebug("Generated csv file deleted from sever  ");
		}
		
	}



	/* (non-Javadoc)
	 * @see atg.service.scheduler.SingletonSchedulableService#performScheduledTask(atg.service.scheduler.Scheduler, atg.service.scheduler.ScheduledJob)
	 */
	@Override
	public void performScheduledTask(Scheduler pScheduler, ScheduledJob pJob) {
		
		 if (isLoggingDebug()) {
	            logDebug("CricketAbandonedOrderService.performTask: Performing scheduler for abandoned orders started ");
	        }
		 
		 try {
			processAbandonedOrders();
		} catch (CommerceException e) {
			logError("Error while performing aboandorder orders "+e);
		}
		 
		 if (isLoggingDebug()) {
	            logDebug("CricketAbandonedOrderService.performTask: Performing scheduler for abandoned orders ended ");
	        }
	}



	 
	/**
	 * @return 
	 * 
	 */
	private List<CricketOrderImpl> findCricketAbandonedOrders() {
		
		List<CricketOrderImpl> abandonedOrders;
		RepositoryItem[] abandoedOrderItems = null;
		abandonedOrders = new ArrayList<CricketOrderImpl>();
		try {
		 
			abandoedOrderItems = getAbandonedOrdersBatch(null, null, true);
			if (abandoedOrderItems != null) {
				for (RepositoryItem ri : abandoedOrderItems) {
					// add each abandoed order to List<OrderImpl>
					// abandonedOrders
					abandonedOrders.add((CricketOrderImpl) getAbandonedOrderTools()
							.getOrderManager().loadOrder(ri.getRepositoryId()));

					if (isLoggingDebug()) {
						StringBuilder riB = new StringBuilder();
						riB.append("Order found: ");
						riB.append(ri.getRepositoryId());
						logDebug(riB.toString());
					}

				}
				
			}
			
			
		} catch (TransactionDemarcationException e) {
			logError("An error occoured while fetching abandoed orders " + e, e);
		} catch (CommerceException e) {
			logError("An error occoured while fetching abandoed orders " + e, e);
		} catch (RepositoryException e) {
			logError("An error occoured while fetching abandoed orders " + e, e);
		}
		
		return abandonedOrders;

	}

	/**
	 * Gets the current date format.
	 * 
	 * @return the current date format
	 */
	public String getCurrentDateFormat() {
		return currentDateFormat == null ? "yyyyMMdd" : currentDateFormat;
	}

	/**
	 * Sets the current date format.
	 * 
	 * @param currentDateFormat
	 *            the new current date format
	 */
	public void setCurrentDateFormat(String currentDateFormat) {
		this.currentDateFormat = currentDateFormat;
	}

	/**
	 * @return the cricketReportGen
	 */
	public CricketReportGeneration getCricketReportGen() {
		return mCricketReportGen;
	}

	/**
	 * @param pCricketReportGen
	 *            the cricketReportGen to set
	 */
	public void setCricketReportGen(CricketReportGeneration pCricketReportGen) {
		mCricketReportGen = pCricketReportGen;
	}
}
