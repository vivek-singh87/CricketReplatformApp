/**
 * 
 */
package com.cricket.inventory;

import java.io.File;

import javax.xml.bind.JAXBException;

import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;
import atg.repository.RepositoryException;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.inventory.response.UpdateWebProductResponseInfo;

public class InventoryPublishingManager extends GenericService{
	
	/**
	 * Property DeployTools holding the reference of InventoryDeploymentTools
	 */
	private InventoryDeploymentTools mDeployTools;
	
	/**
	 * @return the mDeployTools
	 */
	public InventoryDeploymentTools getDeployTools() {
		return mDeployTools;
	}

	/**
	 * @param pDeployTools the mDeployTools to set
	 */
	public void setDeployTools(final InventoryDeploymentTools pDeployTools) {
		this.mDeployTools = pDeployTools;
	}

	/**
	 * Method executeImportFeed getting the UpdateWebProductResponseInfo as parameter.
	 * Doing the Operations: Getting the response by calling the deployment tools class.
	 * @param invInfo - request parameter of type UpdateWebProductResponseInfo
	 * @param inventoryDataFile - request parameter of type File
	 * @return invInfo - return of type  UpdateWebProductResponseInfo
	 */
	public String executeImportFeed(final File inventoryDataFile,
			final UpdateWebProductResponseInfo invInfo){
		String returnValue=CricketCommonConstants.SUCCESS;
		if (isLoggingDebug()) {
			logDebug("Starting Method: executeImportFeed() ");
		}
		
		try {
			getDeployTools().executeImportFeed(inventoryDataFile);
			
		} catch (TransactionDemarcationException e) {
			returnValue = CricketCommonConstants.FAILURE + CricketCommonConstants.BLANK_SPACE
							+ CricketCommonConstants.PIPE + CricketCommonConstants.BLANK_SPACE + e.getMessage()
							+ CricketCommonConstants.BLANK_SPACE + "TransactionDemarcationException";
			if (isLoggingError()) {
				logDebug("Error in Method: executeImportFeed() " + returnValue);
			}
		} catch (RepositoryException e) {
			returnValue = CricketCommonConstants.FAILURE + CricketCommonConstants.BLANK_SPACE
					+ CricketCommonConstants.PIPE + CricketCommonConstants.BLANK_SPACE + e.getMessage()
					+ CricketCommonConstants.BLANK_SPACE + "RepositoryException";
			if (isLoggingError()) {
				logDebug("Error in Method: executeImportFeed() " + returnValue);
			}
		} catch (JAXBException e) {
			returnValue = CricketCommonConstants.FAILURE + CricketCommonConstants.BLANK_SPACE
					+ CricketCommonConstants.PIPE + CricketCommonConstants.BLANK_SPACE + e.getMessage()
					+ CricketCommonConstants.BLANK_SPACE + "JAXBException";
			if (isLoggingError()) {
				logDebug("Error in Method: executeImportFeed() " + returnValue);
			}
		} catch (Exception e) {
			returnValue = CricketCommonConstants.FAILURE + CricketCommonConstants.BLANK_SPACE
					+ CricketCommonConstants.PIPE + CricketCommonConstants.BLANK_SPACE + e.getMessage()
					+ CricketCommonConstants.BLANK_SPACE + "Exception";
			if (isLoggingError()) {
				logDebug("Error in Method: executeImportFeed() " + returnValue);
			}
		}
		
		if (isLoggingDebug()) {
			logDebug("Ending Method: executeImportFeed() " + returnValue);
		}
		return returnValue;
	}
}
