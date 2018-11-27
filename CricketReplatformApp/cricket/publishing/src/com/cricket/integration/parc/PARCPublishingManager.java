package com.cricket.integration.parc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.ejb.CreateException;
import javax.xml.bind.JAXBException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;
import atg.process.action.ActionException;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.versionmanager.exceptions.VersionException;
import atg.workflow.WorkflowException;


/**
 * The component creates an XML file from the ESP feed and passes it on to executeImportDevices()
 * method of PARCDeploymentTools for further processing of the PARC rate plan feed
 * 
 * @author Cricket Communications
 *
 */
public class PARCPublishingManager extends GenericService{
	
	/**
	 * Variable that holds the reference to PARCDeploymentTools component
	 */
	private PARCDeploymentTools mDeployTools;		
	/**
	 * Variable that holds the path where the XML file created using the ESP feed should be placed
	 */
	private String mFeedFilePath;

	/**
	 * The method creates an XML file out of the ESP feed for rate plans received and calls the 
	 * executeImportDevices() method of PARCDeploymentTools for further processing
	 *  
	 * @param plansDataFile
	 */
	public boolean executeImportPlans(RepositoryItem  feedItem){
		
		if(feedItem != null) {
			
			vlogDebug("Start of executeImportPlans method");
			vlogDebug("Feed Item Id : " + feedItem.getRepositoryId());
			try {
				File parcDataFile = new File(getFeedFilePath());
				if(!parcDataFile.exists()) {
					parcDataFile.createNewFile();
				} 
				vlogDebug("Plans Feed file : " + parcDataFile);
				BufferedWriter bw = new BufferedWriter(new FileWriter(parcDataFile));
	    	    bw.write((String)feedItem.getPropertyValue(CricketCommonConstants.PROP_FEED));
	    	    bw.close();
				getDeployTools().executeImportPARCData(parcDataFile);
			} catch (WorkflowException e) {
				vlogError("WorkflowException : " + e.getCause());
				vlogError("WorkflowException Message : " + e.getMessage());
				return false;
			} catch (TransactionDemarcationException e) {
				vlogError("TransactionDemarcationException : " + e.getCause());
				vlogError("TransactionDemarcationException Message : " + e.getMessage());
				return false;
			} catch (VersionException e) {
				vlogError("VersionException : " + e.getCause());
				vlogError("VersionException Message : " + e.getMessage());
				return false;
			} catch (CreateException e) {
				vlogError("CreateException : " + e.getCause());
				vlogError("CreateException Message : " + e.getMessage());
				return false;
			} catch (ActionException e) {
				vlogError("ActionException : " + e.getCause());
				vlogError("ActionException Message : " + e.getMessage());
				return false;
			} catch (RepositoryException e) {
				vlogError("RepositoryException : " + e.getCause());
				vlogError("RepositoryException Message : " + e.getMessage());
				return false;
			} catch (JAXBException e) {
				vlogError("JAXBException : " + e.getCause());
				vlogError("JAXBException Message : " + e.getMessage());
				return false;
			} catch (Exception e) {
				vlogError("Exception : " + e.getCause());
				vlogError("Exception Message : " + e.getMessage());
				return false;
			}	
			vlogDebug("End of executeImportDevices method");
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Returns the feed file path with reference to CricketConfiguration component
	 * 
	 * @return the mFeedFilePath
	 */
	public String getFeedFilePath() {
		return mFeedFilePath;
	}


	/**
	 * Sets the feed file path with reference to CricketConfiguration component
	 * 
	 * @param mFeedFilePath
	 */
	public void setFeedFilePath(String mFeedFilePath) {
		this.mFeedFilePath = mFeedFilePath;
	}
	
	/**
	 * Returns the reference to the PARCDeploymentTools component
	 * 
	 * @return the mDeployTools
	 */
	public PARCDeploymentTools getDeployTools() {
		return mDeployTools;
	}

	/**
	 * Sets the reference to the PARCDeploymentTools component
	 * 
	 * @param pDeployTools
	 */
	public void setDeployTools(PARCDeploymentTools pDeployTools) {
		this.mDeployTools = pDeployTools;
	}
}
