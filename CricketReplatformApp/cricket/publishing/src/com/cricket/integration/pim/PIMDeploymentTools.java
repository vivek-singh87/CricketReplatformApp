package com.cricket.integration.pim;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.transaction.TransactionManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.epub.project.Process;
import atg.process.action.ActionException;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.QueryExpression;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.versionmanager.WorkingContext;
import atg.versionmanager.exceptions.VersionException;
import atg.workflow.WorkflowException;
import com.cricket.common.constants.CricketCommonConstants;

import com.cricket.configuration.CricketConfiguration;
import com.cricket.integration.common.DeploymentTools;
import com.cricket.integration.pim.request.AdapterInfo;
import com.cricket.integration.pim.request.BatteryInfo;
import com.cricket.integration.pim.request.BluetoothInfo;
import com.cricket.integration.pim.request.BundleContentInfo;
import com.cricket.integration.pim.request.CableInfo;
import com.cricket.integration.pim.request.CaseInfo;
import com.cricket.integration.pim.request.ChargerInfo;
import com.cricket.integration.pim.request.HandsetInfo;
import com.cricket.integration.pim.request.HeadsetInfo;
import com.cricket.integration.pim.request.MemoryInfo;
import com.cricket.integration.pim.request.ModemInfo;
import com.cricket.integration.pim.request.OtherInfo;
import com.cricket.integration.pim.request.PhoneGiftPackInfo;
import com.cricket.integration.pim.request.ProductInfo;
import com.cricket.integration.pim.request.ProductListInfo;
import com.cricket.integration.pim.request.ProductMasterNotificationRequestInfo;
import com.cricket.integration.pim.request.SpeakerInfo;

/**
 * The class holds the functionality to process the ESP feed (from PIM system) and 
 * place the processed device,ie, phone or accessory information within a BCC project 
 * 
 * @author Tech Mahindra
 *
 */
public class PIMDeploymentTools extends DeploymentTools {
	
	/**
	 * Variable that holds reference to TransactionManager component
	 */
	private TransactionManager mTransactionManager;
	/**
	 * Variable that holds ProjectName property which will be suffixed to the BCC project created
	 * This will help business user identify the source of the content within the project
	 * 
	 */
	private String mProjectName;
	/**
	 * Variable that holds reference to  ProductCatlog Repository
	 */
	private MutableRepository catalogRepository;
	/**
	 * Variable that holds reference to CricketConfiguration component
	 */
	private CricketConfiguration cricketConfiguration;


	/**
	 * The method calls the importDeviceData() method which unmarshalls the ESP feed XML for device
	 * and calls the method to process the XML feed
	 * 
	 * @param devicesFile
	 * @throws VersionException
	 * @throws WorkflowException
	 * @throws CreateException
	 * @throws ActionException
	 * @throws TransactionDemarcationException
	 * @throws RepositoryException 
	 * @throws JAXBException, Exception 
	 * @throws Exception
	 */
	public void executeImportDevices(File devicesFile) throws VersionException, WorkflowException, CreateException, ActionException,
    TransactionDemarcationException, RepositoryException, JAXBException, Exception
    {
		vlogDebug("Start of executeImportDevices method");
		vlogDebug("Devices File : " + devicesFile);
		TransactionDemarcation td = new TransactionDemarcation();
		boolean rollback = true;

		try {
			td.begin(getTransactionManager(), TransactionDemarcation.REQUIRED);
			Process process = createProject(td, getProjectName());
			if(devicesFile != null) {
				importDeviceData(process, td, devicesFile);
			}
			advanceWorkflow(process);
			rollback = false;
		} catch (VersionException e) {
			throw e;
		} catch (TransactionDemarcationException e) {
			throw e;
		} catch (CreateException e) {
			throw e;
		} catch (WorkflowException e) {
			throw e;
		} catch (ActionException e) {
			throw e;
		} catch (RepositoryException e) {
			throw e;
		}  catch (JAXBException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			releaseUserIdentity();
			try {
				td.end(rollback);
			} catch (TransactionDemarcationException tde) {
				throw tde;
			}
			WorkingContext.popDevelopmentLine();
		}
		vlogDebug("End of executeImportDevices method");
	}
	
	/**
	 * The method unmarshalls the ESP feed XML for devices and calls the updateDeviceDetails()
	 * method which processes the XML feed
	 * 
	 * @param process
	 * @param td
	 * @param devicesDataFile
	 * @throws JAXBException 
	 * @throws Exception
	 */
	private void importDeviceData(Process process, TransactionDemarcation td, File devicesDataFile) throws RepositoryException, TransactionDemarcationException, JAXBException
	{				
		vlogDebug("Start of importDeviceData method");
		try {		
				JAXBContext jaxbContext = JAXBContext.newInstance(ProductMasterNotificationRequestInfo.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				ProductMasterNotificationRequestInfo pimData = (ProductMasterNotificationRequestInfo) jaxbUnmarshaller.unmarshal(devicesDataFile);
				vlogDebug("Unmarshalled devices : " + pimData.getProductList());
				updateDeviceDetails(pimData);
				
		  } catch (JAXBException e) {
			throw e;
		  }
		vlogDebug("End of importDeviceData method");
	}
	
	/**
	 * Calls the updatePhoneDetails() and updateAccessoryDetails() methods depending on whether the
	 * device under processing is a phone or accessory
	 *  
	 * @param pmNotificationReqInfo
	 * @throws RepositoryException
	 * @throws TransactionDemarcationException
	 */
	private void updateDeviceDetails(ProductMasterNotificationRequestInfo pmNotificationReqInfo) throws RepositoryException, TransactionDemarcationException
	{		
		vlogDebug("Start of updateDeviceDetails method");
		ProductListInfo productListInfo = pmNotificationReqInfo.getProductList();
		List<ProductInfo> productInfos = productListInfo.getProduct();	
		for(ProductInfo productInfo : productInfos){				
			if(productInfo.getPhoneGiftPack() != null){
				vlogDebug("Device is a phone");
				updatePhoneDetails(productInfo);					
			} else {
				vlogDebug("Device is an accessory");
				updateAccessoryDetails(productInfo);
			}						
		}
		vlogDebug("End of updateDeviceDetails method");
	}

	/**
	 * Processes the phone data sent in the ESP device feed and stores the data in the BCC project
	 * 
	 * @param productInfo
	 * @throws RepositoryException 
	 * @throws TransactionDemarcationException 
	 */
	private void updatePhoneDetails(ProductInfo productInfo) throws RepositoryException, TransactionDemarcationException
	{		
		vlogDebug("Start of updatePhoneDetails method");
		MutableRepositoryItem phoneSkuItem;
		/*MutableRepositoryItem phoneProductItem;
		RepositoryView catPhoneView;
		MutableRepositoryItem phoneCat;
		boolean isProdAvailInCat = false;
		boolean isProductAvailable = true;
		NamedQueryView nCatPhoneSkuView;
		Object args[] = new Object[1]; 
		RqlStatement statement;
		Query nQuery;*/
		RepositoryView catPhoneSkuView;
		QueryBuilder qb;
		QueryExpression phoneIdExpr;
		QueryExpression phoneIdValExpr;
		Query query;
		RepositoryItem bundleContentItem;
		List<RepositoryItem> bomContents = new ArrayList<RepositoryItem>();
		boolean isSKUAvailable = true;
		TransactionManager tm = getTransactionManager();
		TransactionDemarcation td = new TransactionDemarcation();
		try {
			td.begin(tm);
			PhoneGiftPackInfo pgpInfo = productInfo.getPhoneGiftPack();	
			/*catPhoneSkuView = getCatalogRepository().getView("phone");
			nCatPhoneSkuView = (NamedQueryView)catPhoneSkuView;
			nQuery = nCatPhoneSkuView.getNamedQuery("SKUQuery");
			args[0] = pgpInfo.getInventoryItem();
			RepositoryItem[] phoneSkus = ((ParameterSupportView)catPhoneSkuView).executeQuery(nQuery, args);*/
			catPhoneSkuView = getCatalogRepository().getView(CricketCommonConstants.PHONE_SKU);	
			qb = catPhoneSkuView.getQueryBuilder();
			phoneIdExpr = qb.createPropertyQueryExpression(CricketCommonConstants.REPOSITORY_ID); 
			phoneIdValExpr = qb.createConstantQueryExpression(pgpInfo.getInventoryItem());	
			query = qb.createComparisonQuery(phoneIdExpr, phoneIdValExpr, QueryBuilder.EQUALS);
			RepositoryItem[] phoneSkus = catPhoneSkuView.executeQuery(query);
			vlogDebug("Phone SKUs : " + phoneSkus);
			if(phoneSkus != null && phoneSkus.length > 0){
				phoneSkuItem = getCatalogRepository().getItemForUpdate(pgpInfo.getInventoryItem(), CricketCommonConstants.PHONE_SKU);
				vlogDebug("Phone SKU available in catalog : " + phoneSkuItem.getRepositoryId());
			} else {
				phoneSkuItem = getCatalogRepository().createItem(pgpInfo.getInventoryItem(), CricketCommonConstants.PHONE_SKU);
				vlogDebug("Phone SKU not available in catalog, creating new : " + phoneSkuItem.getRepositoryId());
				isSKUAvailable = false;
			}
			if(pgpInfo.getShortDescription() != null) { 
				phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, pgpInfo.getShortDescription());
				vlogDebug("Phone Giftpack short description : " + pgpInfo.getShortDescription());
			}
			if(pgpInfo.getShortDescription() == null) { 
				vlogDebug("Phone Giftpack short description is null");
				if(pgpInfo.getDescription() != null) { 	
					phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, pgpInfo.getDescription());
					vlogDebug("Phone Giftpack description : " + pgpInfo.getDescription());
				}
				else {
					phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, CricketCommonConstants.NO_SHORT_DESC_PIM);	
					vlogDebug("Phone Giftpack description is also null");				
				}
			}
			if(pgpInfo.getMsrp() != null) { 
				phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_LIST_PRICE, (pgpInfo.getMsrp()).doubleValue());
				vlogDebug("Phone Giftpack MSRP price : " + pgpInfo.getMsrp());
			}
			if(pgpInfo.getPhoneGiftPackColor() != null) { 
				phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_PIM_COLOR, (pgpInfo.getPhoneGiftPackColor()));
				vlogDebug("Phone Giftpack Color : " + pgpInfo.getPhoneGiftPackColor());
			}
			if(pgpInfo.getDeviceCategory()!= null) { 
				phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_DEVICE_CATEGORY, pgpInfo.getDeviceCategory());
				vlogDebug("Phone Giftpack deviceCategory : " + pgpInfo.getDeviceCategory());
			}	
			if(pgpInfo.getHoModelNumber()!= null) { 
				phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_MODEL_NUMBER, pgpInfo.getHoModelNumber());
				vlogDebug("Phone Giftpack HoModelNumber : " + pgpInfo.getHoModelNumber());
			}	
			if(pgpInfo.getInventoryItemStatusCode()!= null) { 
				phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_LIFECYCLE_PHASE, pgpInfo.getInventoryItemStatusCode());
				vlogDebug("Phone Giftpack Status Code : " + pgpInfo.getInventoryItemStatusCode());
			}
			if(pgpInfo.getPhoneGiftPackManufacturer()!= null) { 
				phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_MANUFACTURER_CODE, pgpInfo.getPhoneGiftPackManufacturer());
				vlogDebug("Phone Giftpack Manufacturer Code : " + pgpInfo.getPhoneGiftPackManufacturer());
			}
			if(pgpInfo.getServiceType()!= null) { 
				phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_SERVICE_TYPE, pgpInfo.getServiceType());
				vlogDebug("Phone Giftpack Service Type : " + pgpInfo.getServiceType());
			}
			if(pgpInfo.isSellable()!= null) { 
				vlogDebug("Phone Giftpack Sellable Flag : " + pgpInfo.isSellable());
				if(pgpInfo.isSellable()){
					phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_PIM_SELLABLE_FLAG, CricketCommonConstants.Y);					
				} else {
					phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_PIM_SELLABLE_FLAG, CricketCommonConstants.N);						
				}
			}
			if(pgpInfo.isOrderableOnTheWebFlag()!= null) { 
				phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_WEB_SELLABLE_FLAG, pgpInfo.isOrderableOnTheWebFlag());
				vlogDebug("Phone Giftpack Orderable on the Web Flag : " + pgpInfo.isOrderableOnTheWebFlag());
			}
			if(pgpInfo.getContents() != null) {
				if(pgpInfo.getContents().getBundleContent() != null) {
					if(pgpInfo.getContents().getBundleContent().size() > 0) {
						for(BundleContentInfo bundleInfo : pgpInfo.getContents().getBundleContent()) {
							if(!bundleInfo.getItemNumber().equalsIgnoreCase(pgpInfo.getInventoryItem())){
								vlogDebug("Phone Giftpack Bundled Content Item Id : " + pgpInfo.getInventoryItem());
								bundleContentItem = getCatalogRepository().getItem(bundleInfo.getItemNumber(), CricketCommonConstants.ACCESSORY_SKU);
								if(bundleContentItem != null) {
									if(!((String)bundleContentItem.getPropertyValue(CricketCommonConstants.PROP_ACC_TYPE)).
																				equals(CricketCommonConstants.DEVICE_TYPE_HANDSET)) {
										bomContents.add(bundleContentItem);
									}
								}
							}
						}
					}					
				}				
			}
			phoneSkuItem.setPropertyValue(CricketCommonConstants.PROP_BOM_CONTENTS, bomContents);
			if(isSKUAvailable){
				getCatalogRepository().updateItem(phoneSkuItem);
			} else {
				getCatalogRepository().addItem(phoneSkuItem);
			}
		} catch (RepositoryException e) {
			throw e;
		} catch (TransactionDemarcationException exc) {
			throw exc;
		} finally {
			try {
				td.end();
			} catch (TransactionDemarcationException e) {
				throw e;
			}
		}
		vlogDebug("End of updatePhoneDetails method");
	}
	
	/**
	 * Processes the accessory data sent in the ESP device feed and stores the data in the BCC project
	 * 
	 * @param productInfo
	 * @throws TransactionDemarcationException 
	 * @throws RepositoryException 
	 */
	private void updateAccessoryDetails(ProductInfo productInfo) throws TransactionDemarcationException, RepositoryException
	{
		vlogDebug("Start of updateAccessoryDetails method");
		
		MutableRepositoryItem accessorySkuItem;
		RepositoryView catAccSkuView;
		/*MutableRepositoryItem accessoryProductItem;
		RepositoryView catAccView;
		NamedQueryView nCatAccSkuView;
		Query nQuery;
		MutableRepositoryItem accCat;
		boolean isProdAvailInCat = false;
		boolean isProductAvailable = true;
		Object args[] = new Object[1]; */
		QueryBuilder qb;
		QueryExpression accIdExpr;
		QueryExpression accIdValExpr;
		Query query;
		String pimInventoryItem = null;
		String pimDesc = null;
		double msrp = 0.0;
		String accessoryType = null;
		String manufacturer = null;
		String lifeCyclePhase = null;
		String hoModelNumber = null;
		boolean isWebSellable = false;
		boolean isPIMSellable = false;
		boolean isSKUAvailable = true;
		TransactionManager tm = getTransactionManager();
		TransactionDemarcation td = new TransactionDemarcation();
		try {
			td.begin(tm);
			AdapterInfo adapterInfo = productInfo.getAdapter();
			BatteryInfo batterInfo = productInfo.getBattery();
			BluetoothInfo bluetoothInfo = productInfo.getBluetooth();
			CableInfo cableInfo = productInfo.getCable();
			CaseInfo caseInfo = productInfo.getCase();
			ChargerInfo chargerInfo = productInfo.getCharger();
			HandsetInfo handsetInfo = productInfo.getHandsetDevice();
			HeadsetInfo headsetInfo = productInfo.getHeadset();
			MemoryInfo memoryInfo = productInfo.getMemory();
			ModemInfo modemInfo = productInfo.getModem();
			SpeakerInfo speakerInfo = productInfo.getSpeaker();
			OtherInfo otherInfo = productInfo.getOther();
			if(adapterInfo != null){
				pimInventoryItem = adapterInfo.getInventoryItem();
				pimDesc = adapterInfo.getShortDescription();
				msrp = (adapterInfo.getMsrp()).doubleValue();
				manufacturer = adapterInfo.getAdapterManufacturer();
				if(adapterInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = adapterInfo.isOrderableOnTheWebFlag();
				}
				if(adapterInfo.isSellable() != null) {
					isPIMSellable = adapterInfo.isSellable();
				}
				lifeCyclePhase = adapterInfo.getInventoryItemStatusCode();
				hoModelNumber = adapterInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_CABLE_ADAP;
				
			} else if(batterInfo != null) {
				pimInventoryItem = batterInfo.getInventoryItem();
				pimDesc = batterInfo.getShortDescription();
				msrp = (batterInfo.getMsrp()).doubleValue();
				manufacturer = batterInfo.getBatteryManufacturer();
				if(batterInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = batterInfo.isOrderableOnTheWebFlag();
				}
				if(batterInfo.isSellable() != null) {
					isPIMSellable = batterInfo.isSellable();
				}
				lifeCyclePhase = batterInfo.getInventoryItemStatusCode();
				hoModelNumber = batterInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_BATTERIES;
				
			} else if(bluetoothInfo != null) {
				pimInventoryItem = bluetoothInfo.getInventoryItem();
				pimDesc = bluetoothInfo.getShortDescription();
				msrp = (bluetoothInfo.getMsrp()).doubleValue();
				manufacturer = bluetoothInfo.getBluetoothManufacturer();
				if(bluetoothInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = bluetoothInfo.isOrderableOnTheWebFlag();
				}
				if(bluetoothInfo.isSellable() != null) {
					isPIMSellable = bluetoothInfo.isSellable();					
				}
				lifeCyclePhase = bluetoothInfo.getInventoryItemStatusCode();
				hoModelNumber = bluetoothInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_BLUETOOTH;
				
			} else if(cableInfo != null) {
				pimInventoryItem = cableInfo.getInventoryItem();
				pimDesc = cableInfo.getShortDescription();
				msrp = (cableInfo.getMsrp()).doubleValue();
				manufacturer = cableInfo.getCableManufacturer();
				if(cableInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = cableInfo.isOrderableOnTheWebFlag();
				}
				if(cableInfo.isSellable() != null) {
					isPIMSellable = cableInfo.isSellable();
				}
				lifeCyclePhase = cableInfo.getInventoryItemStatusCode();
				hoModelNumber = cableInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_CABLE_ADAP;
				
			} else if(caseInfo != null) {
				pimInventoryItem = caseInfo.getInventoryItem();
				pimDesc = caseInfo.getShortDescription();
				msrp = (caseInfo.getMsrp()).doubleValue();
				manufacturer = caseInfo.getManufacturer();
				if(caseInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = caseInfo.isOrderableOnTheWebFlag();
				}
				if(caseInfo.isSellable() != null) {
					isPIMSellable = caseInfo.isSellable();
				}
				lifeCyclePhase = caseInfo.getInventoryItemStatusCode();
				hoModelNumber = caseInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_CASES;
				
			} else if(chargerInfo != null) {
				pimInventoryItem = chargerInfo.getInventoryItem();
				pimDesc = chargerInfo.getShortDescription();
				msrp = (chargerInfo.getMsrp()).doubleValue();
				manufacturer = chargerInfo.getChargerManufacturer();
				if(chargerInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = chargerInfo.isOrderableOnTheWebFlag();
				}
				if(chargerInfo.isSellable() != null) {
					isPIMSellable = chargerInfo.isSellable();
				}
				lifeCyclePhase = chargerInfo.getInventoryItemStatusCode();
				hoModelNumber = chargerInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_CHARGERS;
				
			} else if(handsetInfo != null) {
				pimInventoryItem = handsetInfo.getInventoryItem();
				pimDesc = handsetInfo.getShortDescription();
				msrp = (handsetInfo.getMsrp()).doubleValue();
				manufacturer = handsetInfo.getHandsetManufacturer();
				if(handsetInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = handsetInfo.isOrderableOnTheWebFlag();
				}
				if(handsetInfo.isSellable() != null) {
					isPIMSellable = handsetInfo.isSellable();
				}
				lifeCyclePhase = handsetInfo.getInventoryItemStatusCode();
				hoModelNumber = handsetInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.DEVICE_TYPE_HANDSET;
				
			} else if(headsetInfo != null) {
				pimInventoryItem = headsetInfo.getInventoryItem();
				pimDesc = headsetInfo.getShortDescription();
				msrp = (headsetInfo.getMsrp()).doubleValue();
				manufacturer = headsetInfo.getHeadsetManufacturer();
				if(headsetInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = headsetInfo.isOrderableOnTheWebFlag();
				}
				if(headsetInfo.isSellable() != null) {
					isPIMSellable = headsetInfo.isSellable();
				}
				lifeCyclePhase = headsetInfo.getInventoryItemStatusCode();
				hoModelNumber = headsetInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_HEADPHONES;
				
			} else if(memoryInfo != null) {
				pimInventoryItem = memoryInfo.getInventoryItem();
				pimDesc = memoryInfo.getShortDescription();
				msrp = (memoryInfo.getMsrp()).doubleValue();
				manufacturer = memoryInfo.getMemoryManufacturer();
				if(memoryInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = memoryInfo.isOrderableOnTheWebFlag();
				}
				if(memoryInfo.isSellable() != null) {
					isPIMSellable = memoryInfo.isSellable();
				}
				lifeCyclePhase = memoryInfo.getInventoryItemStatusCode();
				hoModelNumber = memoryInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_DATA_MEMORY;
				
			} else if(modemInfo != null) {
				pimInventoryItem = modemInfo.getInventoryItem();
				pimDesc = modemInfo.getShortDescription();
				msrp = (modemInfo.getMsrp()).doubleValue();
				manufacturer = modemInfo.getModemManufacturer();
				if(modemInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = modemInfo.isOrderableOnTheWebFlag();
				}
				if(modemInfo.isSellable() != null) {
					isPIMSellable = modemInfo.isSellable();
				}
				lifeCyclePhase = modemInfo.getInventoryItemStatusCode();
				hoModelNumber = modemInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_MODEM;
				
			} else if(speakerInfo != null) {
				pimInventoryItem = speakerInfo.getInventoryItem();
				pimDesc = speakerInfo.getShortDescription();
				msrp = (speakerInfo.getMsrp()).doubleValue();
				manufacturer = speakerInfo.getManufacturer();
				if(speakerInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = speakerInfo.isOrderableOnTheWebFlag();
				}
				if(speakerInfo.isSellable() != null) {
					isPIMSellable = speakerInfo.isSellable();
				}
				lifeCyclePhase = speakerInfo.getInventoryItemStatusCode();
				hoModelNumber = speakerInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_SPEAKER;
				
			}  else if(otherInfo != null) {
				pimInventoryItem = otherInfo.getInventoryItem();
				pimDesc = otherInfo.getShortDescription();
				msrp = (otherInfo.getMsrp()).doubleValue();
				manufacturer = otherInfo.getManufacturer();
				if(otherInfo.isOrderableOnTheWebFlag() != null) {
					isWebSellable = otherInfo.isOrderableOnTheWebFlag();
				}
				if(otherInfo.isSellable() != null) {
					isPIMSellable = otherInfo.isSellable();
				}
				lifeCyclePhase = otherInfo.getInventoryItemStatusCode();
				hoModelNumber = otherInfo.getHoModelNumber();
				accessoryType = CricketCommonConstants.ACC_TYPE_OTHER;
				
			} 
			/*catAccSkuView = getCatalogRepository().getView("sku");
			nCatAccSkuView = (NamedQueryView)catAccSkuView;
			nQuery = nCatAccSkuView.getNamedQuery("SKUQuery");
			args[0] = pimInventoryItem;
			RepositoryItem[] accSkus = ((ParameterSupportView)catAccSkuView).executeQuery(nQuery, args);*/
			catAccSkuView = getCatalogRepository().getView(CricketCommonConstants.ACCESSORY_SKU);	
			qb = catAccSkuView.getQueryBuilder();
			accIdExpr = qb.createPropertyQueryExpression(CricketCommonConstants.REPOSITORY_ID); 
			accIdValExpr = qb.createConstantQueryExpression(pimInventoryItem);	
			query = qb.createComparisonQuery(accIdExpr, accIdValExpr, QueryBuilder.EQUALS);
			RepositoryItem[] accSkus = catAccSkuView.executeQuery(query);
			vlogDebug("Accessory SKUs list : " + accSkus);
			if(accSkus !=null && accSkus.length > 0){
				accessorySkuItem = getCatalogRepository().getItemForUpdate(pimInventoryItem, CricketCommonConstants.ACCESSORY_SKU);
				vlogDebug("Accessory SKU already available in catalog : " + accessorySkuItem.getRepositoryId());
			} else {
				accessorySkuItem = getCatalogRepository().createItem(pimInventoryItem, CricketCommonConstants.ACCESSORY_SKU);
				vlogDebug("Accessory SKU not available in catalog, creating new " + accessorySkuItem.getRepositoryId());
				isSKUAvailable = false;
			}
			if(pimDesc != null) { 
				accessorySkuItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, pimDesc);
				vlogDebug("Accessory Short Description : " + pimDesc);
			}
			if(pimDesc == null) { 
				accessorySkuItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, "No Short Description sent from PIM");
				vlogDebug("Accessory Short Description is null ");
			}
			if(msrp != 0.0) { 
				accessorySkuItem.setPropertyValue(CricketCommonConstants.PROP_LIST_PRICE, msrp);
				vlogDebug("Accessory MSRP Price : " + msrp);
			}
			if(manufacturer != null) { 
				accessorySkuItem.setPropertyValue(CricketCommonConstants.PROP_MANUFACTURER_CODE, manufacturer);
				vlogDebug("Accessory manufacturer code : " + manufacturer);
			}
			if(isWebSellable) { 
				accessorySkuItem.setPropertyValue(CricketCommonConstants.PROP_WEB_SELLABLE_FLAG, isWebSellable);
				vlogDebug("Accessory Orderable on the Web Flag : " + isWebSellable);
			}
			vlogDebug("Accessory PIM Sellable Flag : " + isPIMSellable);
			if(isPIMSellable){
				accessorySkuItem.setPropertyValue(CricketCommonConstants.PROP_PIM_SELLABLE_FLAG, CricketCommonConstants.Y);					
			} else {
				accessorySkuItem.setPropertyValue(CricketCommonConstants.PROP_PIM_SELLABLE_FLAG, CricketCommonConstants.N);						
			}
			if(lifeCyclePhase != null) { 
				accessorySkuItem.setPropertyValue(CricketCommonConstants.PROP_LIFECYCLE_PHASE, lifeCyclePhase);
				vlogDebug("Accessory Inventory status code : " + lifeCyclePhase);
			}
			if(hoModelNumber != null) { 
				accessorySkuItem.setPropertyValue(CricketCommonConstants.PROP_MODEL_NUMBER, hoModelNumber);
				vlogDebug("Accessory HO model number : " + hoModelNumber);
			}
			if(accessoryType != null) { 
				accessorySkuItem.setPropertyValue(CricketCommonConstants.PROP_ACC_TYPE, accessoryType);
				vlogDebug("Accessory Type : " + accessoryType);
			}
			if(isSKUAvailable){
				getCatalogRepository().updateItem(accessorySkuItem);
			} else {
				getCatalogRepository().addItem(accessorySkuItem);
			}
		} catch (RepositoryException e) {
			throw e;
		}catch (TransactionDemarcationException exc) {
			throw exc;
		} finally {
			try {
				td.end();
			} catch (TransactionDemarcationException e) {
				throw e;
			}
		}
		vlogDebug("End of updateAccessoryDetails method");
	}
	
	/**
	 * 
	 * @return the catalogRepository
	 */
	public MutableRepository getCatalogRepository() {
		return catalogRepository;
	}

	/**
	 * 
	 * @param catalogRepository
	 */
	public void setCatalogRepository(MutableRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}
	
	/**
	 * 
	 * @return the mTransactionManager
	 */
	public TransactionManager getTransactionManager() {
	    return mTransactionManager;
	}
	
	/**
	 * 
	 * @param pTransactionManager
	 */
	public void setTransactionManager(TransactionManager pTransactionManager) {
	    mTransactionManager = pTransactionManager;
	}
	
	/**
	 * 
	 * @return the mProjectName
	 */
	public String getProjectName() {
	    return mProjectName;
	}

	/**
	 * 
	 * @param pProjectName
	 */
	public void setProjectName(String pProjectName) {
		mProjectName = pProjectName;
	}	

	/**
	 * 
	 * @return the cricketConfiguration
	 */
	public CricketConfiguration getCricketConfiguration() {
		return cricketConfiguration;
	}

	/**
	 * 
	 * @param cricketConfiguration
	 */
	public void setCricketConfiguration(CricketConfiguration cricketConfiguration) {
		this.cricketConfiguration = cricketConfiguration;
	}
}
