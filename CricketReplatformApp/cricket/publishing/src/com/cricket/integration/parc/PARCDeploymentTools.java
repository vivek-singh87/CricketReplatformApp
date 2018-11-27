package com.cricket.integration.parc;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.transaction.TransactionManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import atg.adapter.gsa.ChangeAwareSet;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.epub.project.Process;
import atg.process.action.ActionException;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;

import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.QueryExpression;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.versionmanager.WorkingContext;
import atg.versionmanager.exceptions.VersionException;
import atg.workflow.WorkflowException;

import com.cricket.configuration.CricketConfiguration;
import com.cricket.integration.common.DeploymentTools;
import com.cricket.integration.parc.request.OfferNotification;
import com.cricket.integration.parc.request.OfferNotification.RatePlan;
import com.cricket.integration.parc.request.OfferNotification.RatePlan.Devices.Device;
import com.cricket.integration.parc.request.OfferNotification.RatePlan.Features.Feature;
import com.cricket.integration.parc.request.OfferNotification.RatePlan.Markets.Market;

/**
 * The class holds the functionality to process the ESP feed (from PARC system) and 
 * place the processed rate plan and addons information within a BCC project 
 * 
 * @author Tech Mahindra
 *
 */
public class PARCDeploymentTools extends DeploymentTools {
	
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
	 * Variable that holds the "Current" value of a rate plan's life cycle phase
	 */
	private final String LIFE_CYCLE_CURRENT = "Current";
	/**
	 * Variable that holds the "Supported" value of a rate plan's life cycle phase
	 */
	private final String LIFE_CYCLE_SUPPORTED = "Supported";
	/**
	 * Variable that holds the "Grandfathered" value of a rate plan's life cycle phase
	 */
	private final String LIFE_CYCLE_GRANDFATHERED = "Grandfathered";
	/**
	 * Variable that holds the "Retired" value of a rate plan's life cycle phase
	 */
	private final String LIFE_CYCLE_RETIRED = "Retired";
	/**
	 * Variable that holds the "Optional" value of a rate plan's life cycle phase
	 */
	private final String OPTIONAL_FEATURE = "Optional";
	/**
	 * Variable that holds the "Optional" value of a rate plan's life cycle phase
	 */
	private final String INCLUDED_FEATURE = "Included";
	/**
	 * Variable that holds the "Optional" value of a rate plan's life cycle phase
	 */
	private final String MANDATORY_FEATURE = "Mandatory";
	/**
	 * Variable that holds the reference to the CricketConfiguration component
	 */
	private CricketConfiguration cricketConfiguration;
	/**
	 * Variable that holds the parcIncludedOfferCodes property of a Plan
	 */
	private final String PARC_INC_OFF_CODES = "parcIncludedOfferCodes";
	/**
	 * Variable that holds the parcMandatoryOfferCodes property of a Plan
	 */
	private final String PARC_MAN_OFF_CODES = "parcMandatoryOfferCodes";

	/**
	 * The method calls the executeImportPARCData() method which unmarshalls the ESP feed XML for rate plans
	 * and calls the method to process the XML feed
	 * 
	 * @param plansFile
	 * @throws VersionException
	 * @throws WorkflowException
	 * @throws CreateException
	 * @throws ActionException
	 * @throws TransactionDemarcationException
	 * @throws RepositoryException 
	 * @throws JAXBException 
	 * @throws Exception
	 */
	public void executeImportPARCData(File parcDataFile) throws VersionException, WorkflowException, CreateException, ActionException,
    TransactionDemarcationException, RepositoryException, JAXBException, Exception
    {
		
		vlogDebug("Start of executeImportPARCData method");
		vlogDebug("Rate plans File : " + parcDataFile);
		TransactionDemarcation td = new TransactionDemarcation();
		boolean rollback = true;

		try {
			td.begin(getTransactionManager(), TransactionDemarcation.REQUIRED);
			Process process = createProject(td, getProjectName());
			importParcData(process, td, parcDataFile);
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
		} catch (JAXBException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}  finally {
			releaseUserIdentity();
			try {
				td.end(rollback);
			} catch (TransactionDemarcationException tde) {
				throw tde;
			}

			WorkingContext.popDevelopmentLine();
		}
		vlogDebug("End of executeImportPARCData method");
	}
	
	/**
	 * The method unmarshalls the ESP feed XML for devices and calls the updatePlanDetails()
	 * method which processes the XML feed
	 * 
	 * @param process
	 * @param td
	 * @param plansDataFile
	 * @throws TransactionDemarcationException 
	 * @throws RepositoryException 
	 * @throws JAXBException 
	 * @throws Exception
	 */
	private void importParcData(Process process, TransactionDemarcation td, File parcDataFile) throws RepositoryException, TransactionDemarcationException, JAXBException
	{ 		
		vlogDebug("Start of importParcData method");
		try {		
				JAXBContext jaxbContext = JAXBContext.newInstance(OfferNotification.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				OfferNotification parcData = (OfferNotification) jaxbUnmarshaller.unmarshal(parcDataFile);
				vlogDebug("Unmarshalled Plans data : " + parcData);
				updatePlanDetails(parcData);
				
		  } catch (JAXBException e) {
			throw e;
		  } 
		vlogDebug("End of importParcData method");
	}	
	
	/**
	 * Calls the updatePhoneDetails() and updateAccessoryDetails() methods depending on whether the
	 * device under processing is a phone or accessory
	 * 
	 * @param offerNotification
	 * @throws RepositoryException
	 * @throws TransactionDemarcationException
	 */
	@SuppressWarnings("unchecked")
	private void updatePlanDetails(OfferNotification offerNotification) throws RepositoryException, TransactionDemarcationException
	{
		vlogDebug("Start of updatePlanDetails method");
		MutableRepositoryItem planProductItem;
		RepositoryView catPlanProdView;
		RepositoryView catSkuView;
		RepositoryView catPhoneProdView;
		RepositoryView catPlanGroupView;
		QueryBuilder qb;
		QueryExpression planIdExpr;
		QueryExpression planIdValExpr;
		QueryExpression planSkuIdExpr;
		QueryExpression planSkuIdValExpr;
		QueryExpression phoneIdExpr;
		QueryExpression phoneIdValExpr;
		QueryExpression planGroupIdExpr;
		QueryExpression planGroupIdValExpr;
		Query query;
		RepositoryItem[] phoneItems;
		RepositoryItem addOnItem;
		RepositoryItem addOnSkuItem;
		MutableRepositoryItem newAddOnItem;
		MutableRepositoryItem newAddOnSkuItem;
		RepositoryItem marketItem;
		MutableRepositoryItem newMarketItem;
		MutableRepositoryItem planSkuItem;
		MutableRepositoryItem planGroupItem;
		/*NamedQueryView nCatPlanProdView;
		NamedQueryView nCatPlanSkuView;
		Query nQuery;
		MutableRepositoryItem planCat;
		MutableRepositoryItem addOnCat;
		boolean isProdAvailInCat = false;
		Object args[] = new Object[1];
		List<RepositoryItem> parcPlanIncAddOns = new ArrayList<RepositoryItem>();
		List<RepositoryItem> parcPlanMandAddOns = new ArrayList<RepositoryItem>();*/
		List<String> parcPlanIncOffCodes = new ArrayList<String>();
		List<String> parcPlanMandOffCodes = new ArrayList<String>();
		boolean isAddOnAvailInCat = false;
		boolean isAddOnSkuAvailInCat = false;
		List<RatePlan> ratePlans = offerNotification.getRatePlan();
		List<Market> markets;
		List<Device> devices;
		List<Feature> features;
		List<RepositoryItem> marketsInfo = new ArrayList<RepositoryItem>();
		List<RepositoryItem> parcPlanOptAddOns = new ArrayList<RepositoryItem>();
		List<String> parcPlanOptOffCodes = new ArrayList<String>();
		List<RepositoryItem> compatiblePhones = new ArrayList<RepositoryItem>();
		Date currentDate = new Date();
		Timestamp startDateCurrent = new Timestamp(currentDate.getTime()); 
		boolean isProductAvailable = true;
		boolean isSKUAvailable = true;
		boolean isPlanGroupAvailable = true;
		boolean phoneAlreadyCompatible = false; 
		TransactionManager tm = getTransactionManager();
		TransactionDemarcation td = new TransactionDemarcation();
		for(RatePlan ratePlan : ratePlans){
			try {
				td.begin(tm);
				catPlanProdView = getCatalogRepository().getView(CricketCommonConstants.PLAN_PRODUCT);	
				qb = catPlanProdView.getQueryBuilder();
				planIdExpr = qb.createPropertyQueryExpression(CricketCommonConstants.REPOSITORY_ID); 
				planIdValExpr = qb.createConstantQueryExpression(ratePlan.getCode());	
				query = qb.createComparisonQuery(planIdExpr, planIdValExpr, QueryBuilder.EQUALS);
				RepositoryItem[] checkedInplans = catPlanProdView.executeQuery(query);
				//nCatPlanProdView = (NamedQueryView)catPlanProdView;
				//nQuery = nCatPlanProdView.getNamedQuery("ProductQuery");
				//args[0] = ratePlan.getCode();
				//RepositoryItem[] checkedInplans = ((ParameterSupportView)catPlanProdView).executeQuery(nQuery, args);
				vlogDebug("Plans in Catalog with this ID : " + checkedInplans);
				if( checkedInplans!= null && checkedInplans.length > 0){
						planProductItem = getCatalogRepository().getItemForUpdate(ratePlan.getCode(), CricketCommonConstants.PLAN_PRODUCT);	
						vlogDebug("Plan product available in catalog : " + planProductItem.getRepositoryId());
						if(planProductItem != null && planProductItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE) == null) {
							planProductItem.setPropertyValue(CricketCommonConstants.PROP_START_DATE, startDateCurrent);
						}
				} else {
					planProductItem = getCatalogRepository().createItem(ratePlan.getCode(), 
																		CricketCommonConstants.PLAN_PRODUCT);
					vlogDebug("Plan product not available in catalog : " + planProductItem.getRepositoryId());
					if(ratePlan.getName() != null) { 
						planProductItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, ratePlan.getName());
						vlogDebug("Plan name for new Plan: " + ratePlan.getName());
					} else if(ratePlan.getPrice() != null) { 
						planProductItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, 
													CricketCommonConstants.SYM_DOLLAR + ratePlan.getPrice());
					}
					planProductItem.setPropertyValue(CricketCommonConstants.PROP_START_DATE, startDateCurrent);
					isProductAvailable = false;
				}
				if(ratePlan.getName() != null) { 
					planProductItem.setPropertyValue(CricketCommonConstants.PROP_PARC_PLAN_NAME, ratePlan.getName());
					vlogDebug("PARC plan name : " + ratePlan.getName());
				}
				if(ratePlan.getActivateDtm() != null) { 
					planProductItem.setPropertyValue(CricketCommonConstants.PROP_ACT_DATE, 
									new Timestamp((ratePlan.getActivateDtm()).toGregorianCalendar().getTime().getTime()));
					vlogDebug("Activation Date name : " + ratePlan.getActivateDtm());
				}
				if(ratePlan.getDeactivateDtm() != null) { 
					planProductItem.setPropertyValue(CricketCommonConstants.PROP_DEACT_DATE, 
									new Timestamp((ratePlan.getDeactivateDtm()).toGregorianCalendar().getTime().getTime()));
					vlogDebug("De-activation Date name : " + ratePlan.getDeactivateDtm());
				}
				if(ratePlan.getCategory() != null) { 
					planProductItem.setPropertyValue(CricketCommonConstants.PROP_RATE_PLAN_TYPE, ratePlan.getCategory());
					vlogDebug("Plan Category : " + ratePlan.getCategory());
				}
				if(ratePlan.getLifecyclePhase() != CricketCommonConstants.INTEGER_ZERO) { 
					if(ratePlan.getLifecyclePhase() == CricketCommonConstants.INTEGER_ONE){
						planProductItem.setPropertyValue(CricketCommonConstants.PROP_LIFECYCLE_PHASE, LIFE_CYCLE_CURRENT);						
					} else if(ratePlan.getLifecyclePhase() == CricketCommonConstants.INTEGER_TWO){
						planProductItem.setPropertyValue(CricketCommonConstants.PROP_LIFECYCLE_PHASE, LIFE_CYCLE_SUPPORTED);						
					} else if(ratePlan.getLifecyclePhase() == CricketCommonConstants.INTEGER_THREE){
						planProductItem.setPropertyValue(CricketCommonConstants.PROP_LIFECYCLE_PHASE, LIFE_CYCLE_GRANDFATHERED);						
					} else {
						planProductItem.setPropertyValue(CricketCommonConstants.PROP_LIFECYCLE_PHASE, LIFE_CYCLE_RETIRED);						
					} 
					vlogDebug("Plan Life cycle phase : " + ratePlan.getLifecyclePhase());
				} 
				if(ratePlan.getType() != null) { 
					planProductItem.setPropertyValue(CricketCommonConstants.PROP_PLAN_TYPE, ratePlan.getType());
					vlogDebug("Plan type : " + ratePlan.getType());
				} 
				if(ratePlan.getId() != null) { 
					planProductItem.setPropertyValue(CricketCommonConstants.PROP_PARC_PLAN_ID, ratePlan.getId());
					vlogDebug("PARC Plan ID : " + ratePlan.getId());
				}
				if(ratePlan.getWebGroupCode() != null) { 
					planProductItem.setPropertyValue(CricketCommonConstants.PROP_GROUP_CODE, ratePlan.getWebGroupCode());
					vlogDebug("PARC Group Code : " + ratePlan.getWebGroupCode());
				}				
				markets = ratePlan.getMarkets().getMarket();
				if(markets.size() > 0){
					for(Market market : markets){
						marketItem = getCatalogRepository().getItem(((Integer)market.getId()).toString(),
																	CricketCommonConstants.ITEM_DESC_MARKET_INFO);
						if(marketItem != null){
							newMarketItem = (MutableRepositoryItem)marketItem;
							vlogDebug("Market already available in catalog : " + market.getId());
						}else{
							newMarketItem = getCatalogRepository().createItem(String.valueOf(market.getId()), 
																	CricketCommonConstants.ITEM_DESC_MARKET_INFO);				
							vlogDebug("Market not available in catalog, creating new " + market.getId());
							newMarketItem.setPropertyValue(CricketCommonConstants.PROP_MARKET_ID, (market.getId()));
							newMarketItem.setPropertyValue(CricketCommonConstants.PROP_CITY, market.getName());
							vlogDebug("Market Name : " + market.getName());
							newMarketItem.setPropertyValue(CricketCommonConstants.PROP_MARKET_CODE, market.getHoCode());
							vlogDebug("Market Code : " + market.getHoCode());
							getCatalogRepository().addItem(newMarketItem);		
						}
						if(!marketsInfo.contains(newMarketItem)) {
							marketsInfo.add(newMarketItem);
						}
					}
					if (marketsInfo != null && marketsInfo.size() > 0) {
						planProductItem.setPropertyValue(CricketCommonConstants.PROP_MARKETS, marketsInfo);
					}
				}
				if (null != ratePlan.getDevices()) {
				devices = ratePlan.getDevices().getDevice();
				if(devices != null && devices.size() > 0){
					catPhoneProdView = getCatalogRepository().getView(CricketCommonConstants.PHONE_SKU);				
					qb = catPhoneProdView.getQueryBuilder();
					phoneIdExpr = qb.createPropertyQueryExpression(CricketCommonConstants.PROP_MODEL_NUMBER); 
					for(Device device : devices){
						phoneIdValExpr = qb.createConstantQueryExpression(device.getPhoneCode());
						query = qb.createComparisonQuery(phoneIdExpr, phoneIdValExpr, QueryBuilder.EQUALS);
						phoneItems = catPhoneProdView.executeQuery(query);
						vlogDebug("Compatible phone model number : " + device.getPhoneCode());
						vlogDebug("Phone SKUs with same model number : " + phoneItems);
						if(phoneItems != null && phoneItems.length > 0){
							for(RepositoryItem phoneProduct : phoneItems){
								ChangeAwareSet phoneProductsSet = (ChangeAwareSet)phoneProduct.
														getPropertyValue(CricketCommonConstants.PROP_PARENT_PRODUCTS);
								vlogDebug("Parent products for SKU " + phoneProduct.getRepositoryId() + " : " + phoneProductsSet);
								List<RepositoryItem>  phoneProductItems = new ArrayList<RepositoryItem>();
								for (Object phoneProductItemFromSet : phoneProductsSet) {
									RepositoryItem phoneProductItem = getCatalogRepository().getItem((
													(RepositoryItem)phoneProductItemFromSet).getRepositoryId(),CricketCommonConstants.PHONE_PRODUCT);
									vlogDebug("Compatible Phone Product From Set: " + phoneProductItemFromSet);
									if(phoneProductItem != null){
										phoneProductItems.add(phoneProductItem);
									}
								}
								for(RepositoryItem phoneProductItem : phoneProductItems){
									vlogDebug("Compatible Phone Product : " + phoneProductItem);
									if(!compatiblePhones.contains(phoneProductItem)) {
										compatiblePhones.add(phoneProductItem);
									}
									/*if(compatiblePhones.size() > 0) {
										for(RepositoryItem compatiblePhone : compatiblePhones){
											if((phoneProductItem.getRepositoryId()).
														equalsIgnoreCase(compatiblePhone.getRepositoryId())) {
												phoneAlreadyCompatible = true;
												break;
											}											
										}
									}
									if(!phoneAlreadyCompatible) {
										if(!compatiblePhones.contains(phoneProductItem) 
												&& (phoneProductItem.getItemDescriptor().getItemDescriptorName()).equals(CricketCommonConstants.PHONE_PRODUCT)) {
											compatiblePhones.add(phoneProductItem);	
										}
										phoneAlreadyCompatible = false;
									}*/
								}
							}						
						}
					}
					if(compatiblePhones != null && compatiblePhones.size() > 0) {
						planProductItem.setPropertyValue(CricketCommonConstants.PROP_COMPATIBLE_PHONES, compatiblePhones);
					}
					vlogDebug("Compatible Phones : " + compatiblePhones);
				}	
			} else {
				logInfo("Devices is null for rate plan :" + ratePlan.getId());
			}
				if (null != ratePlan.getFeatures()) {
				features = ratePlan.getFeatures().getFeature();
				if(features.size() > 0){
					for(Feature feature : features){
						vlogDebug("Feature Type : " + feature.getType());
						if((feature.getType()).equalsIgnoreCase(OPTIONAL_FEATURE)) {
								addOnSkuItem = getCatalogRepository().getItem(feature.getCode(), CricketCommonConstants.ITEM_DESC_ADDON_SKU);
								if(addOnSkuItem != null){
									vlogDebug("AddOn SKU already available in catalog : " + feature.getCode());
									newAddOnSkuItem = (MutableRepositoryItem)addOnSkuItem;
									newAddOnSkuItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, feature.getName());
									newAddOnSkuItem.setPropertyValue(CricketCommonConstants.PROP_LIST_PRICE, (feature.getPrice()).doubleValue());
									getCatalogRepository().updateItem(newAddOnSkuItem);
									isAddOnSkuAvailInCat = true;
								}else{
									vlogDebug("AddOn SKU not available in catalog : " + feature.getCode());
									newAddOnSkuItem = getCatalogRepository().createItem(feature.getCode(),CricketCommonConstants.ITEM_DESC_ADDON_SKU);
									newAddOnSkuItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, feature.getName());	
									newAddOnSkuItem.setPropertyValue(CricketCommonConstants.PROP_LIST_PRICE, (feature.getPrice()).doubleValue());
									getCatalogRepository().addItem(newAddOnSkuItem);
									isAddOnSkuAvailInCat = false;
								}
								vlogDebug("Feature Name : " + feature.getName());
								vlogDebug("Feature Code : " + feature.getCode());
								vlogDebug("Feature ID : " + feature.getId());
								vlogDebug("Feature Price : " + feature.getPrice());
								addOnItem = getCatalogRepository().getItem(feature.getCode(), 
																			CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT);
								if(addOnItem != null){
									vlogDebug("AddOn Product already available in catalog : " + feature.getCode());
									newAddOnItem = (MutableRepositoryItem)addOnItem;
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_PARC_GROUP_NAME, feature.getGroupName());
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_PARC_ADDON_NAME, feature.getName());
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_CKT_CODE, feature.getCode());	
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_OFFER_ID, ((Integer)feature.getId()).toString());
									if(newAddOnItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE) == null) {
										newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_START_DATE , startDateCurrent);
									}
									if(!isAddOnSkuAvailInCat){
										List<RepositoryItem> addOnChildSkus = (List<RepositoryItem>) newAddOnItem.getPropertyValue(
																								CricketCommonConstants.PROP_CHILD_SKUS);
										addOnChildSkus.add(newAddOnSkuItem);
										newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS, addOnChildSkus);
										vlogDebug("AddON Child SKUs : " + addOnChildSkus);
									}
									isAddOnAvailInCat = true;
								} else {
									vlogDebug("AddOn Product not available in catalog : " + feature.getCode());
									newAddOnItem = getCatalogRepository().createItem(feature.getCode(), CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT);
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, feature.getGroupName());
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_PARC_GROUP_NAME, feature.getGroupName());
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_PARC_GROUP_ID, feature.getGroupId());	
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_PARC_ADDON_NAME, feature.getName());
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_CKT_CODE, feature.getCode());	
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_OFFER_ID, ((Integer)feature.getId()).toString());
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_START_DATE,startDateCurrent);
									List<RepositoryItem> addOnChildSkus = (List<RepositoryItem>) newAddOnItem.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
									addOnChildSkus.add(newAddOnSkuItem);
									newAddOnItem.setPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS, addOnChildSkus);
									vlogDebug("AddON Child SKUs : " + addOnChildSkus);
									isAddOnAvailInCat = false;
								}
								vlogDebug("Feature Group Name : " + feature.getGroupName());
								vlogDebug("Feature Group ID : " + feature.getGroupId());
								//if((feature.getType()).equalsIgnoreCase("Optional")) {
									if(!parcPlanOptAddOns.contains(newAddOnItem)){
										parcPlanOptAddOns.add(newAddOnItem);
									}
									if(!parcPlanOptOffCodes.contains(feature.getCode())){
										parcPlanOptOffCodes.add(feature.getCode());
									}
								/*} else if((feature.getType()).equalsIgnoreCase("Included")) {
									if(!parcPlanIncAddOns.contains(newAddOnItem)){
										parcPlanIncAddOns.add(newAddOnItem);
									}
									if(!parcPlanIncOffCodes.contains(feature.getCode())){
										parcPlanIncOffCodes.add(feature.getCode());
									}
								} else {
									if(!parcPlanMandAddOns.contains(newAddOnItem)){
										parcPlanMandAddOns.add(newAddOnItem);									
									}
									if(!parcPlanMandOffCodes.contains(feature.getCode())){
										parcPlanMandOffCodes.add(feature.getCode());	
									}
								}*/
								if(isAddOnAvailInCat){
									getCatalogRepository().updateItem(newAddOnItem);
								} else {
									getCatalogRepository().addItem(newAddOnItem);
								}
		
								//addOnCat = getCatalogRepository().getItemForUpdate(getCricketConfiguration().getOtherAddonsCategoryId(), CricketCommonConstants.CATEGORY);
								/*addOnCat = getCatalogRepository().getItemForUpdate("cat10022", CricketCommonConstants.CATEGORY);
								List<RepositoryItem> fixedChildProducts = (List<RepositoryItem>) addOnCat.getPropertyValue("fixedChildProducts");
								fixedChildProducts.add(newAddOnItem);
								getCatalogRepository().updateItem(addOnCat);*/
						}
						if((feature.getType()).equalsIgnoreCase(INCLUDED_FEATURE)) {
							vlogDebug("Included Offer Id : " + feature.getId());
							if(!parcPlanIncOffCodes.contains(((Integer)feature.getId()).toString())){
								parcPlanIncOffCodes.add(((Integer)feature.getId()).toString());
							}
						} if ((feature.getType()).equalsIgnoreCase(MANDATORY_FEATURE)){
							vlogDebug("Mandatory Offer Id : " + feature.getId());
							if(!parcPlanMandOffCodes.contains(((Integer)feature.getId()).toString())){
								parcPlanMandOffCodes.add(((Integer)feature.getId()).toString());	
							}
						}
					}		
					if (parcPlanOptAddOns != null && parcPlanOptAddOns.size() > 0) {
						planProductItem.setPropertyValue(CricketCommonConstants.PROP_PARC_OPTIONAL_ADDONS, parcPlanOptAddOns);						
					}
					//planProductItem.setPropertyValue("parcIncludedAddOns", parcPlanIncAddOns);
					//planProductItem.setPropertyValue("parcMandatoryAddOns", parcPlanMandAddOns);
					if(parcPlanOptOffCodes != null && parcPlanOptOffCodes.size() > 0) {
						planProductItem.setPropertyValue(CricketCommonConstants.PROP_PARC_OPTIONAL_OFFER_CODES, parcPlanOptOffCodes);
					}
					vlogDebug("Plan Optional Features : " + parcPlanOptAddOns);
					vlogDebug("Plan Optional Feature Codes : " + parcPlanOptOffCodes);
					vlogDebug("Plan Included Feature Codes : " + parcPlanIncOffCodes);
					vlogDebug("Plan Mandatory Feature Codes : " + parcPlanMandOffCodes);
					if(parcPlanIncOffCodes !=null && parcPlanIncOffCodes.size() > 0) {
						planProductItem.setPropertyValue(PARC_INC_OFF_CODES, parcPlanIncOffCodes);
					}
					if(parcPlanMandOffCodes != null && parcPlanMandOffCodes.size() > 0) {
						planProductItem.setPropertyValue(PARC_MAN_OFF_CODES, parcPlanMandOffCodes);
					}
				}
			} else {
				logInfo("Feature is null for rate plan :" + ratePlan.getId());
			}
				/*catSkuView = getCatalogRepository().getView("sku");
				nCatPlanSkuView = (NamedQueryView)catSkuView;
				nQuery = nCatPlanSkuView.getNamedQuery("SKUQuery");
				args[0] = ratePlan.getCode();
				RepositoryItem[] checkedInplanSKUs = ((ParameterSupportView)catPlanProdView).executeQuery(nQuery, args);*/

				catSkuView = getCatalogRepository().getView(CricketCommonConstants.ITEM_DESC_PLAN_SKU);	
				qb = catSkuView.getQueryBuilder();
				planSkuIdExpr = qb.createPropertyQueryExpression(CricketCommonConstants.REPOSITORY_ID); 
				planSkuIdValExpr = qb.createConstantQueryExpression(ratePlan.getCode());	
				query = qb.createComparisonQuery(planSkuIdExpr, planSkuIdValExpr, QueryBuilder.EQUALS);
				RepositoryItem[] checkedInplanSKUs = catSkuView.executeQuery(query);
				vlogDebug("Plan SKUs for ID available in catalog : " + checkedInplanSKUs);
				if(checkedInplanSKUs != null && checkedInplanSKUs.length > 0){
					vlogDebug("Plan SKU already available in catalog : " + ratePlan.getCode());
					planSkuItem = getCatalogRepository().getItemForUpdate(ratePlan.getCode(), CricketCommonConstants.ITEM_DESC_PLAN_SKU);
				} else {
					vlogDebug("Plan SKU not available in catalog : " + ratePlan.getCode());
					planSkuItem = getCatalogRepository().createItem(ratePlan.getCode(), CricketCommonConstants.ITEM_DESC_PLAN_SKU);
					isSKUAvailable = false;
				}
				if(ratePlan.getName() != null) { 
					planSkuItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, ratePlan.getName());
					vlogDebug("Plan Name : " + ratePlan.getName());
				}
				if(ratePlan.getPrice() != null) { 
					planSkuItem.setPropertyValue(CricketCommonConstants.PROP_LIST_PRICE, (ratePlan.getPrice()).doubleValue());
					vlogDebug("Plan price : " + ratePlan.getPrice());
				}
				if(isSKUAvailable){
					getCatalogRepository().updateItem(planSkuItem);
				} else {
					getCatalogRepository().addItem(planSkuItem);
				}
				List<RepositoryItem> childSkus = (List<RepositoryItem>) planProductItem.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
				if(!childSkus.contains(planSkuItem)) {
					childSkus.add(planSkuItem);
				}
				if(childSkus != null && childSkus.size() > 0) {
					planProductItem.setPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS, childSkus);
				}
				if(isProductAvailable){
					getCatalogRepository().updateItem(planProductItem);
				} else {
					getCatalogRepository().addItem(planProductItem);
				}
				
				catPlanGroupView = getCatalogRepository().getView(CricketCommonConstants.ITEM_DESC_PLAN_GROUP);	
				qb = catPlanGroupView.getQueryBuilder();
				planGroupIdExpr = qb.createPropertyQueryExpression(CricketCommonConstants.REPOSITORY_ID); 
				if(ratePlan.getWebGroupCode().equalsIgnoreCase(ratePlan.getCode())) {
					planGroupIdValExpr = qb.createConstantQueryExpression(ratePlan.getWebGroupCode() + CricketCommonConstants.GROUP_CODE_SUFFIX);						
				} else {
					planGroupIdValExpr = qb.createConstantQueryExpression(ratePlan.getWebGroupCode());						
				}
				query = qb.createComparisonQuery(planGroupIdExpr, planGroupIdValExpr, QueryBuilder.EQUALS);
				RepositoryItem[] checkedInplanGroups = catPlanGroupView.executeQuery(query);
				vlogDebug("Plan Groups for ID available in catalog : " + checkedInplanGroups);
				if(checkedInplanGroups != null && checkedInplanGroups.length > 0){
					vlogDebug("Plan Group already available in catalog : " + ratePlan.getWebGroupCode());
					if(ratePlan.getWebGroupCode().equalsIgnoreCase(ratePlan.getCode())) {
						planGroupItem = getCatalogRepository().getItemForUpdate(ratePlan.getWebGroupCode() + CricketCommonConstants.GROUP_CODE_SUFFIX, CricketCommonConstants.ITEM_DESC_PLAN_GROUP);					
					} else {
						planGroupItem = getCatalogRepository().getItemForUpdate(ratePlan.getWebGroupCode(), CricketCommonConstants.ITEM_DESC_PLAN_GROUP);			
					}
				} else {
					vlogDebug("Plan Group not available in catalog : " + ratePlan.getWebGroupCode());
					if(ratePlan.getWebGroupCode().equalsIgnoreCase(ratePlan.getCode())) {
						planGroupItem = getCatalogRepository().createItem(ratePlan.getWebGroupCode() + CricketCommonConstants.GROUP_CODE_SUFFIX, CricketCommonConstants.ITEM_DESC_PLAN_GROUP);						
					} else {
						planGroupItem = getCatalogRepository().createItem(ratePlan.getWebGroupCode(), CricketCommonConstants.ITEM_DESC_PLAN_GROUP);					
					}
					isPlanGroupAvailable = false;
				}
				
				planGroupItem.setPropertyValue(CricketCommonConstants.PROP_DISP_NAME, ratePlan.getWebGroupCode());				
				List<RepositoryItem> groupPlans = (List<RepositoryItem>)(planGroupItem.getPropertyValue(CricketCommonConstants.PROP_GROUP_PLANS));
				List<RepositoryItem> groupCompatiblePhones = new ArrayList<RepositoryItem>();
				List<RepositoryItem> planCompatiblePhones = null;
				if(groupPlans == null) {
					groupPlans = new ArrayList<RepositoryItem>();
				}
				if(!groupPlans.contains(planProductItem)) {
					groupPlans.add(planProductItem);
				}
				planGroupItem.setPropertyValue(CricketCommonConstants.PROP_GROUP_PLANS, groupPlans);
				for(RepositoryItem groupPlan : groupPlans) {
					planCompatiblePhones = (List<RepositoryItem>)groupPlan.getPropertyValue(CricketCommonConstants.PROP_COMPATIBLE_PHONES);
					if(planCompatiblePhones != null) {
						for(RepositoryItem planCompatiblePhone : planCompatiblePhones) {
							if(!groupCompatiblePhones.contains(planCompatiblePhone)){
								groupCompatiblePhones.add(planCompatiblePhone);
							}
						}
					}
				}
				planGroupItem.setPropertyValue(CricketCommonConstants.PROP_GROUP_COMPATIBLE_PHONES, groupCompatiblePhones);
				if(isPlanGroupAvailable){
					getCatalogRepository().updateItem(planGroupItem);
				} else {
					getCatalogRepository().addItem(planGroupItem);
				}
				//planCat = (MutableRepositoryItem) getCatalogRepository().getItem(getCricketConfiguration().getAllPlansCategoryId(), CricketCommonConstants.CATEGORY);
				/*planCat = getCatalogRepository().getItemForUpdate("cat10022", CricketCommonConstants.CATEGORY);
				List<RepositoryItem> fixedChildProducts = (List<RepositoryItem>) planCat.getPropertyValue("fixedChildProducts");
				for(RepositoryItem fixedChildProduct : fixedChildProducts){
					if(fixedChildProduct.getPropertyValue("id").equals(planProductItem.getPropertyValue("id"))){
						isProdAvailInCat = true;
						break;
					}
				}
				if(!isProdAvailInCat){
					fixedChildProducts.add(planProductItem);
					getCatalogRepository().updateItem(planCat);
				}	*/							
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
		}
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
