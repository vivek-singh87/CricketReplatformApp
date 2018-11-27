/**
 * 
 */
package com.cricket.inventory;

import java.io.File;
import java.util.List;

import javax.transaction.TransactionManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import atg.core.util.StringUtils;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.QueryExpression;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.versionmanager.WorkingContext;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.inventory.request.UpdateWebProductRequestInfo;
import com.cricket.inventory.request.UpdateWebProductRequestInfo.Request.Batch.Inventory;



/**
 * Class InventoryDeploymentTools extends Generic service
 * Operations done in this method:-  Updation the inventory Repository, Creation and add the item in inventory Repository
 */
public class InventoryDeploymentTools extends GenericService{
	
	/**
	 * Property TransactionManager to hold reference of TransactionManager
	 */
	private TransactionManager mTransMgr;
	/**
	 * Property InventoryRepository to hold reference of MutableRepository
	 */
	private MutableRepository mInvRepo;
	/**
	 * Property catalogRepository to hold reference of MutableRepository
	 */
	private MutableRepository mCatRepo;
	
	
	/**
	 * @return the mTransMgr
	 */
	public TransactionManager getTransMgr() {
		return mTransMgr;
	}

	/**
	 * @param pTransMgr the mTransMgr to set
	 */
	public void setTransMgr(final TransactionManager pTransMgr) {
		this.mTransMgr = pTransMgr;
	}

	
	/**
	 * @return the mInvRepo
	 */
	public MutableRepository getInvRepo() {
		return mInvRepo;
	}

	/**
	 * @param pInvRepo the mInvRepo to set
	 */
	public void setInvRepo(final MutableRepository pInvRepo) {
		this.mInvRepo = pInvRepo;
	}

	
	
	/**
	 * @return the mCatRepo
	 */
	public MutableRepository getCatRepo() {
		return mCatRepo;
	}

	/**
	 * @param pCatRepo the mCatRepo to set
	 */
	public void setCatRepo(final MutableRepository pCatRepo) {
		this.mCatRepo = pCatRepo;
	}
	
	/**
	 * Method executeImportFeed getting the inventoryDataFile as parameter.
	 * Doing the Operations: Importing the feed data.
	 * @param inventoryDataFile - request parameter of type File
	 */
	public void executeImportFeed(final File inventoryDataFile) throws TransactionDemarcationException, RepositoryException, JAXBException, Exception
    {
		if (isLoggingDebug()) {
			logDebug("Starting Method: executeImportFeed() ");
		}
		final TransactionDemarcation transDemarc = new TransactionDemarcation();
		boolean rollback = true;

		try {
			transDemarc.begin(getTransMgr(), TransactionDemarcation.REQUIRED);
			importFeedData(transDemarc, inventoryDataFile);
			rollback = false;
		}
		finally {			
			transDemarc.end(rollback);
			 
			WorkingContext.popDevelopmentLine();
		}
		if (isLoggingDebug()) {
			logDebug("Ending Method: executeImportFeed() ");
		}
	}
	
	
	/**
	 * Method importFeedData getting the inventoryDataFile as parameter.
	 * Doing the Operations: Unmarshalling the file into the Object.
	 * @param inventoryDataFile - request parameter of type File
	 * @throws JAXBException 
	 */
	public void importFeedData(final TransactionDemarcation transDemarc, final File inventoryDataFile)
			throws RepositoryException, TransactionDemarcationException, JAXBException, Exception { 
		if (isLoggingDebug()) {
			logDebug("Starting Method: importFeedData() changes ");
		}
		final JAXBContext jaxbContext = JAXBContext.newInstance(UpdateWebProductRequestInfo.class);				
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		// Unmarshal the file into the object
		final UpdateWebProductRequestInfo inventoryFeedData = (UpdateWebProductRequestInfo) jaxbUnmarshaller.
						unmarshal(inventoryDataFile);
		updateInventoryFeed(inventoryFeedData);		
				 
		if (isLoggingDebug()) {
			logDebug("Ending Method: importFeedData() ");
		}
	}
	
	/**
	 * Method updateInventoryFeed getting the inventoryFeedData as parameter.
	 * Doing the Operations: Creating the inventory Item if it is not there, Updating the Items if present in the repository
	 * @param inventoryFeedData - request parameter of type UpdateWebProductRequestInfo
	 */
	public void updateInventoryFeed(final UpdateWebProductRequestInfo inventoryFeedData)
			throws RepositoryException, TransactionDemarcationException, Exception
 {
		if (isLoggingDebug()) {
			logDebug("Starting Method: updateInventoryFeed() ");
			logDebug("inventoryFeedData" + inventoryFeedData);
		}
		MutableRepositoryItem repInvConsiderate = null;
		RepositoryItem proCatItem = null;
		RepositoryItem[] repInvs = null;
		if (isLoggingDebug()) {
			logDebug("inventoryFeedData" + inventoryFeedData);
		}
		final TransactionManager transMangr = getTransMgr();
		final TransactionDemarcation transDemarc = new TransactionDemarcation();
		// Getting the list of Inventory Items
		final List<Inventory> inventoryItems = inventoryFeedData.getRequest()
				.getBatch().getInventory();
		for (final Inventory inventoryItem : inventoryItems) {

			try {
				transDemarc.begin(transMangr);
				// Making Query on Inventory Repository viewing to Inventory
				// Item Descriptor
				final RepositoryView invView = getInvRepo().getView(
						CricketCommonConstants.ITEM_DESC_INV);
				final QueryBuilder builder = invView.getQueryBuilder();
				final QueryExpression propertyB = builder
						.createPropertyQueryExpression(CricketCommonConstants.PROPERTY_SKU_ID);
				final QueryExpression valueB = builder.createConstantQueryExpression(inventoryItem.getProductSKU());
				final Query invQuery = builder.createComparisonQuery(propertyB, valueB, QueryBuilder.EQUALS);
				repInvs = invView.executeQuery(invQuery);
				// Checking the Inventory item if null
				if (repInvs == null|| repInvs.length == CricketCommonConstants.INTEGER_ZERO) {
					if (isLoggingDebug()) {
						logDebug("Create InventoryItem for" + inventoryItem.getProductSKU());
					}
					// Creating the Inventory Item
					repInvConsiderate = getInvRepo().createItem(CricketCommonConstants.ITEM_DESC_INV);
					proCatItem = getCatRepo().getItem(inventoryItem.getProductSKU(),CricketCommonConstants.ITEM_DESC_SKU);					
					if (proCatItem != null) {
						// Get the sku item Inventory property
						String isOverriddenInventoryFlag = (String) proCatItem.getPropertyValue(CricketCommonConstants.OVERRIDE_INVENTORY_FLAG);
						// Null check and if it true then it has to consume feed
						if(!StringUtils.isBlank(isOverriddenInventoryFlag) && isOverriddenInventoryFlag.equalsIgnoreCase(Boolean.FALSE.toString())){													
								if (inventoryItem.getProductSKU() != null) {
									repInvConsiderate.setPropertyValue(CricketCommonConstants.PROPERTY_SKU_ID,inventoryItem.getProductSKU());
								}
								if (inventoryItem.getProductType() != null) {
									repInvConsiderate.setPropertyValue(CricketCommonConstants.PROP_PROD_TYPE_FEED,inventoryItem.getProductType());
								}
								final String invQty = Integer.toString(inventoryItem.getInventoryQuantity());
								if (!(StringUtils.isEmpty(invQty))) {
									repInvConsiderate.setPropertyValue(CricketCommonConstants.PROP_STK_LVL,Long.valueOf(invQty));
									logDebug("invQty:-" + Long.getLong(invQty));
								}
								repInvConsiderate.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME,proCatItem.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME));
								if (isLoggingDebug()) {
									logDebug("InventoryItem:-" + repInvConsiderate);
								}
								getInvRepo().addItem(repInvConsiderate);
						}
					}
				}
				if (repInvs != null) {
					if (isLoggingDebug()) {
						logDebug("Updating InventoryItem for" + inventoryItem.getProductSKU());
					}
					proCatItem = getCatRepo().getItem(inventoryItem.getProductSKU(),CricketCommonConstants.ITEM_DESC_SKU);
					if (proCatItem != null) {
						// Get the sku item Inventory property
						String isOverriddenInventoryFlag = (String) proCatItem.getPropertyValue(CricketCommonConstants.OVERRIDE_INVENTORY_FLAG);
						// Null check and if it true then it has to consume feed
						if(isOverriddenInventoryFlag == null || (!StringUtils.isBlank(isOverriddenInventoryFlag) && isOverriddenInventoryFlag.equalsIgnoreCase(Boolean.FALSE.toString()))){
								repInvConsiderate = (MutableRepositoryItem) repInvs[0];
								if (inventoryItem.getProductSKU() != null) {
									repInvConsiderate.setPropertyValue(CricketCommonConstants.PROPERTY_SKU_ID,inventoryItem.getProductSKU());
								}
								if (inventoryItem.getProductType() != null) {
									repInvConsiderate.setPropertyValue(CricketCommonConstants.PROP_PROD_TYPE_FEED,inventoryItem.getProductType());
								}
								final String invQty = Integer.toString(inventoryItem.getInventoryQuantity());
								if (!(StringUtils.isEmpty(invQty))) {
									repInvConsiderate.setPropertyValue(CricketCommonConstants.PROP_STK_LVL,Long.valueOf(invQty));
								}
		
								if (isLoggingDebug()) {
									logDebug("repInvConsiderate" + repInvConsiderate);
								}
								// Updating the Inventory Item
								getInvRepo().updateItem(repInvConsiderate);
						}
					}
				}
			}

			finally {
				transDemarc.end();
			}
		}
		if (isLoggingDebug()) {
			logDebug("Ending Method: updateInventoryFeed() ");
		}
	}
		
}
