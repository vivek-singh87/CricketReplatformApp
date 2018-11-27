package com.cricket.catalog;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import atg.adapter.gsa.ChangeAwareSet;
import atg.commerce.pricing.CricketPromotionManager;
import atg.commerce.pricing.PricingModelHolder;
import atg.commerce.pricing.priceLists.PriceListException;
import atg.commerce.pricing.priceLists.PriceListManager;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.nucleus.naming.ComponentName;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.servlet.ServletUtil;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;

import com.cricket.browse.AccessoryVO;
import com.cricket.browse.AddonVO;
import com.cricket.browse.PhoneVO;
import com.cricket.browse.PlanSpecsVO;
import com.cricket.browse.PlanVO;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.configuration.CricketConfiguration;
import com.cricket.integration.esp.vo.InquireFeaturesResponseVO;
import com.cricket.vo.CricketProfile;

/**
 * @author RM112139
 *
 */
public class CatalogManager extends GenericService {
	
	private Repository catalogRepository;
	private ProfileServices profileService;
	
	private String imagePropertyName;

	private PriceListManager mPriceListManager;
	
	private CricketPromotionManager CktPromotionManager;
	
	private String defaultDataLimit = "2.5GB";
	
	private String defaultDataMessage = "Full-Speed Data";
	
	private String MODEL_NUMBER_QUERY = "modelNumber=?0";
	
	private String PLAN_ID_QUERY = "id=?0";

	private CricketConfiguration cktConfiguration;
	
	private String mCktProfilePath;
	
	private ComponentName mCktProfileComponentName = null;
	
	/**
	 * @return the mPricelistManager
	 */
	public PriceListManager getPriceListManager() {
		return mPriceListManager;
	}

	/**
	 * 
	 * @return the cktConfiguration
	 */
	public CricketConfiguration getCktConfiguration() {
		return cktConfiguration;
	}

	/**
	 * 
	 * @param cktConfiguration
	 */
	public void setCktConfiguration(CricketConfiguration cktConfiguration) {
		this.cktConfiguration = cktConfiguration;
	}
	
	/**
	 * @param pPriceListManager the mPricelistManager to set
	 */
	public void setPriceListManager(PriceListManager pPriceListManager) {
		this.mPriceListManager = pPriceListManager;
	}
	/**
	 * @return profileService
	 */
	public ProfileServices getProfileService() {
		return profileService;
	}

	/**
	 * @param profileService
	 */
	public void setProfileService(ProfileServices profileService) {
		this.profileService = profileService;
	}
	
	public Repository getCatalogRepository() {
		return catalogRepository;
	}

	public void setCatalogRepository(final Repository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}

	/**
	 * Returns the phones whose childSKUs have the model number passed in parameter
	 * 
	 * @return the Phone Products
	 */
	public List<RepositoryItem> getPhonesForModelNumber(String modelNumber) {
		
		List<RepositoryItem> phoneProducts = new ArrayList<RepositoryItem>();
		RepositoryView catalogRepoView;
		try {
			catalogRepoView = getCatalogRepository().getView(CricketCommonConstants.PHONE_SKU);
			Object[] objects = new Object[1];
			objects[0] = modelNumber;
			RqlStatement statement = RqlStatement.parseRqlStatement(MODEL_NUMBER_QUERY);
			RepositoryItem[] phoneSKUs = statement.executeQuery(catalogRepoView, objects);
			if(phoneSKUs != null && phoneSKUs.length > 0){
				for(RepositoryItem phoneSKU : phoneSKUs){
					ChangeAwareSet phoneProductsSet = (ChangeAwareSet)phoneSKU.
											getPropertyValue(CricketCommonConstants.PROP_PARENT_PRODUCTS);
					for (Object phoneProductItem : phoneProductsSet) {
						if(!phoneProducts.contains(phoneProductItem)) {
							phoneProducts.add((RepositoryItem) phoneProductItem);							
						}
					}
				}						
			}
		} catch (RepositoryException e) {
			vlogError("Repository Exception while getting phones for model Number " + modelNumber, e);
		}
		return phoneProducts;
		
	}
	
	
	/**
	 * Method for getting the Phone Products:-
	 * @param categoryId
	 * @param planProductId
	 * @param zipCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PhoneVO> getListOfPhoneProducts(final String categoryId, final String planProductId, final String marketCode, 
														PricingModelHolder userPricingHolder, final String marketType, Boolean isUserLoggedIn, boolean ugradePhoneIntention, String editPhone) {		
		logInfo("Entering into the getListOfPhoneProducts method...");
		if(isLoggingDebug()){
			logDebug("Category Id::::"+categoryId);
			logDebug("Plan Product Id::::"+planProductId);
			logDebug("Zip Code::"+marketCode);
		}
		//final SortedSet<PhoneVO> products = new TreeSet<PhoneVO>();	
		final List<PhoneVO> productsList = new ArrayList<PhoneVO>();
		if(planProductId == null) {
			try {						
						
					RepositoryItem phoneCategoryItem = getCatalogRepository().getItem(categoryId, CricketCommonConstants.CATEGORY);				
					List<RepositoryItem> childProductsItems = (List<RepositoryItem>)phoneCategoryItem.getPropertyValue(CricketCommonConstants.CHILD_PRODUCTS);
					phoneCategoryItem = null;
					if(isLoggingDebug()){
						logDebug("childProductsItems::::"+childProductsItems);						
					}
					if(childProductsItems != null){
						if(marketCode == null || marketCode.isEmpty()) {
							Timestamp startDate;
							Timestamp endDate;
							Date currentDate = new Date();
							PhoneVO phoneVO = null;
							for (RepositoryItem childProduct : childProductsItems) {
								boolean hasStarted = true;
								boolean hasEnded = false;
								startDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
								endDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
								if(startDate != null && startDate.compareTo(currentDate) > 0) {
									hasStarted = false;
								}	
								if(endDate != null && endDate.compareTo(currentDate) < 0) {
									hasEnded = true;
								}	
								if(hasStarted && !hasEnded){
									phoneVO = new PhoneVO();
									phoneVO = populatePhoneListingProduct(childProduct, userPricingHolder);
									if(phoneVO != null && phoneVO.isOOFPhone()) {
										if(isUserLoggedIn && ugradePhoneIntention){
											if(phoneVO.getAvailForCustUppgrades() != null && phoneVO.getAvailForCustUppgrades().equalsIgnoreCase(CricketCommonConstants.Y)) {
													productsList.add(phoneVO);
											}
										} else {
											productsList.add(phoneVO);
										}
									}
								}
							}
						} else {							
								if(null!= marketType && marketType.equalsIgnoreCase(CricketCommonConstants.OOF_CUSTOMER)) {
									boolean hasStarted = true;
									boolean hasEnded = false;
									Timestamp startDate;
									Timestamp endDate;
									Date currentDate = new Date();
									PhoneVO phoneVO = null;
									for (RepositoryItem childProduct : childProductsItems) {
										hasStarted = true;
										hasEnded = false;
										startDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
										endDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
										if(startDate != null && startDate.compareTo(currentDate) > 0) {
											hasStarted = false;
										}	
										if(endDate != null && endDate.compareTo(currentDate) < 0) {
											hasEnded = true;
										}	
										if(hasStarted && !hasEnded){
											phoneVO = new PhoneVO();
											phoneVO = populatePhoneListingProduct(childProduct, userPricingHolder);
											if(phoneVO != null && phoneVO.isOOFPhone()) {
												if(isUserLoggedIn && ugradePhoneIntention){
													if(phoneVO.getAvailForCustUppgrades() != null && phoneVO.getAvailForCustUppgrades().equalsIgnoreCase(CricketCommonConstants.Y)) {
														productsList.add(phoneVO);
													}
												} else {
													productsList.add(phoneVO);
												}
											}
										}
									}
								} else {
									boolean hasStarted = true;
									boolean hasEnded = false;
									Timestamp startDate;
									Timestamp endDate;
									Date currentDate = new Date();
									for (RepositoryItem childProduct : childProductsItems) {
										hasStarted = true;
										hasEnded = false;
										startDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
										endDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
										if(startDate != null && startDate.compareTo(currentDate) > 0) {
											hasStarted = false;
										}	
										if(endDate != null && endDate.compareTo(currentDate) < 0) {
											hasEnded = true;
										}	
										if(hasStarted && !hasEnded){
											final List <RepositoryItem> markets = (List<RepositoryItem>) childProduct.getPropertyValue(CricketCommonConstants.PROP_MARKETS);
											if(isLoggingDebug()){
												logDebug("markets::::" + markets);						
											}
											if (markets != null) {
												PhoneVO phoneVO = null;
												for (RepositoryItem market : markets) {
													String marketCodeRep = (String)market.getPropertyValue(CricketCommonConstants.PROP_MARKET_CODE);
													if(marketCodeRep != null && marketCodeRep.equals(marketCode)) {
														List<RepositoryItem> childSkus = (List<RepositoryItem>)childProduct.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
														phoneVO = new PhoneVO();
														if(childSkus != null && childSkus.size()>0) {
															phoneVO = populatePhoneListingProduct(childProduct, userPricingHolder);
															if(isLoggingDebug()){
																logDebug("Requested marketCode:"+marketCode);	
																logDebug("Market marketCode:"+marketCodeRep);
																logDebug("Market Id:"+market.getRepositoryId());
																logDebug("Phone VO:"+phoneVO);												
															}
															if(isUserLoggedIn && ugradePhoneIntention){
																if(phoneVO.getAvailForCustUppgrades() != null && phoneVO.getAvailForCustUppgrades().equalsIgnoreCase(CricketCommonConstants.Y)) {
																	productsList.add(phoneVO);
																	break;
																}
															} else {
																productsList.add(phoneVO);
																break;
															}
														}
													}
												}
										}
									}
								}
							}
						}
					}
					childProductsItems = null;
				} catch (RepositoryException re) {
					logError("Error while getting the phones for the category "+categoryId + " and zip code "+ marketCode +":", re);
				}
			
		} else {				
				RepositoryItem planProductItem;
				RepositoryItem planGroupItem = null;
				Set<RepositoryItem> planGroupItems = null;
				try {
					planProductItem = getCatalogRepository().getItem(planProductId, CricketCommonConstants.PLAN_PRODUCT);
					if(planProductItem != null){
						planGroupItems = (Set<RepositoryItem>)planProductItem.getPropertyValue(CricketCommonConstants.PLAN_GROUPS);
					}
					planGroupItem = getPlanGroup(planGroupItems);
					List<RepositoryItem> compatiblePhones = null;
					if(planGroupItem != null){
						compatiblePhones = (List<RepositoryItem>)planGroupItem.getPropertyValue(CricketCommonConstants.PROP_GROUP_COMPATIBLE_PHONES);
					}
					if (!StringUtils.isEmpty(editPhone) && editPhone.equalsIgnoreCase(CricketCommonConstants.TRUE)) {
						//compatiblePhones = (List<RepositoryItem>)planProductItem.getPropertyValue(CricketCommonConstants.PROP_COMPATIBLE_PHONES);
					}
					planProductItem = null;
					planGroupItems = null;
					planGroupItem = null;
					if(isLoggingDebug()){
						logDebug("compatiblePhones::::"+compatiblePhones);						
					}
					if (compatiblePhones != null) {
						if(marketCode == null || marketCode.isEmpty()) {
							boolean hasStarted = true;
							boolean hasEnded = false;
							Timestamp startDate;
							Timestamp endDate;
							Date currentDate = new Date();
							PhoneVO phoneVO = null;
							for (RepositoryItem childProduct : compatiblePhones) {
								hasStarted = true;
								hasEnded = false;
								startDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
								endDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
								if(startDate != null && startDate.compareTo(currentDate) > 0) {
									hasStarted = false;
								}	
								if(endDate != null && endDate.compareTo(currentDate) < 0) {
									hasEnded = true;
								}	
								if(hasStarted && !hasEnded){
									phoneVO = new PhoneVO();
									phoneVO = populatePhoneListingProduct(childProduct, userPricingHolder);
									if(phoneVO != null && phoneVO.isOOFPhone()) {
										if(isUserLoggedIn && ugradePhoneIntention){
											if(phoneVO.getAvailForCustUppgrades() != null && phoneVO.getAvailForCustUppgrades().equalsIgnoreCase(CricketCommonConstants.Y)) {
												productsList.add(phoneVO);
											}
										} else {
											productsList.add(phoneVO);
										}
									}
								}
							}
						} else {
							if(null!= marketType && marketType.equalsIgnoreCase(CricketCommonConstants.OOF_CUSTOMER)) {
								boolean hasStarted = true;
								boolean hasEnded = false;
								Timestamp startDate;
								Timestamp endDate;
								Date currentDate = new Date();
								PhoneVO phoneVO = null;
								for (RepositoryItem childProduct : compatiblePhones) {
									hasStarted = true;
									hasEnded = false;
									startDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
									endDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
									if(startDate != null && startDate.compareTo(currentDate) > 0) {
										hasStarted = false;
									}	
									if(endDate != null && endDate.compareTo(currentDate) < 0) {
										hasEnded = true;
									}	
									if(hasStarted && !hasEnded) {
										phoneVO = new PhoneVO();
										phoneVO = populatePhoneListingProduct(childProduct, userPricingHolder);
										if(phoneVO != null && phoneVO.isOOFPhone()) {
											if(isUserLoggedIn && ugradePhoneIntention){
												if(phoneVO.getAvailForCustUppgrades() != null && phoneVO.getAvailForCustUppgrades().equalsIgnoreCase(CricketCommonConstants.Y)) {
													productsList.add(phoneVO);
												}
											} else {
												productsList.add(phoneVO);
											}
										}
									}
								}
							} else {
								boolean hasStarted = true;
								boolean hasEnded = false;
								Timestamp startDate;
								Timestamp endDate;
								Date currentDate = new Date();
								for (RepositoryItem childProduct : compatiblePhones) {
									hasStarted = true;
									hasEnded = false;
									startDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
									endDate = (Timestamp) childProduct.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
									if(startDate != null && startDate.compareTo(currentDate) > 0) {
										hasStarted = false;
									}	
									if(endDate != null && endDate.compareTo(currentDate) < 0) {
										hasEnded = true;
									}	
									if(hasStarted && !hasEnded){
										final List <RepositoryItem> markets = (List<RepositoryItem>) childProduct.getPropertyValue(CricketCommonConstants.PROP_MARKETS);
										if(isLoggingDebug()){
											logDebug("markets::::"+markets);						
										}
										if (markets != null) {
											PhoneVO phoneVO = null;
											for (RepositoryItem market : markets) {
												String marketCodeRep = (String) market.getPropertyValue(CricketCommonConstants.PROP_MARKET_CODE);
												if(isLoggingDebug()){
													logDebug("marketCode::::"+marketCodeRep);						
												}
												if(marketCodeRep != null && marketCodeRep.equals(marketCode)){
													List<RepositoryItem> childSkus = (List<RepositoryItem>)childProduct.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
													phoneVO = new PhoneVO();
													if(childSkus != null && childSkus.size() > 0) {
														phoneVO = populatePhoneListingProduct(childProduct, userPricingHolder);
														if(isLoggingDebug()) {
															logDebug("Requested marketCode:" + marketCode);	
															logDebug("Market marketCode:" + marketCodeRep);
															logDebug("Market Id:" + market.getRepositoryId());
															logDebug("Phone VO:" + phoneVO);												
														}
														if(isUserLoggedIn && ugradePhoneIntention){
															if(phoneVO.getAvailForCustUppgrades() != null && phoneVO.getAvailForCustUppgrades().equalsIgnoreCase(CricketCommonConstants.Y)) {
																productsList.add(phoneVO);
																break;
															}
														} else {
															productsList.add(phoneVO);
															break;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					compatiblePhones = null;
				} catch (RepositoryException re) {					
					logError("Error while getting the compatible phones for the plan - "+planProductId + ":", re);
				}	
			
		}
		if(isLoggingDebug()){
			logDebug("Total No.Of Phone Products::::"+productsList.size());
		}		
		logInfo("Exiting the getListOfPhoneProducts method...");
		//productsList.addAll(products);
		return productsList;
	}
	
	/**
	 * Method for getting the Accessory Products:-
	 * 
	 * @param categoryId
	 * @param phoneProductId
	 * @return
	 */
	public List<AccessoryVO> getListOfAccessoryProducts(final String categoryId, final String phoneProductId) {	
		logInfo("Entering into the getListOfAccessoryProducts method...");		
		if(isLoggingDebug()){
			logDebug("Category Id::::"+categoryId);
			logDebug("Phone Product Id::::"+phoneProductId);
		}
		final List<AccessoryVO> products = new ArrayList<AccessoryVO>();		
		if(phoneProductId == null){			
			RepositoryItem accessoryCategoryItem;
			try {
				accessoryCategoryItem = getCatalogRepository().getItem(categoryId, CricketCommonConstants.CATEGORY);
				List<RepositoryItem> childProducts = (List<RepositoryItem>)accessoryCategoryItem.getPropertyValue(CricketCommonConstants.CHILD_PRODUCTS);
				accessoryCategoryItem = null;
				if (childProducts != null) {
					boolean hasStarted = true;
					boolean hasEnded = false;
					Timestamp startDate;
					Timestamp endDate;
					Date currentDate = new Date();
					AccessoryVO accessoryVO = null;
					for (RepositoryItem repositoryItem : childProducts) {
						hasStarted = true;
						hasEnded = false;
						startDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
						endDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
						if(startDate != null && startDate.compareTo(currentDate) > 0) {
							hasStarted = false;
						}	
						if(endDate != null && endDate.compareTo(currentDate) < 0) {
							hasEnded = true;
						}	
						if(hasStarted && !hasEnded){
							accessoryVO = populateAccessoryVO(repositoryItem);
							products.add(accessoryVO);
						}
					}
				}
				childProducts = null;
			} catch (RepositoryException re) {					
				logError("Error while getting the accessories for category - " + categoryId + ":", re);
			}				
			
		} else {				
				RepositoryItem phoneProductItem;
				try {
					phoneProductItem = getCatalogRepository().getItem(phoneProductId, CricketCommonConstants.PHONE_PRODUCT);
					final List<RepositoryItem> associatedAccessories = (List<RepositoryItem>)phoneProductItem.getPropertyValue(CricketCommonConstants.ASSOCIATED_ACCESSORIES);
					phoneProductItem = null;
					if (associatedAccessories != null) {
						boolean hasStarted = true;
						boolean hasEnded = false;
						Timestamp startDate;
						Timestamp endDate;
						Date currentDate = new Date();
						AccessoryVO accessoryVO = null;
						for (RepositoryItem repositoryItem : associatedAccessories) {
							hasStarted = true;
							hasEnded = false;
							startDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
							endDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
							if(startDate != null && startDate.compareTo(currentDate) > 0) {
								hasStarted = false;
							}	
							if(endDate != null && endDate.compareTo(currentDate) < 0) {
								hasEnded = true;
							}	
							if(hasStarted && !hasEnded){
								accessoryVO = populateAccessoryVO(repositoryItem);
								products.add(accessoryVO);
							}
						}
					}
				} catch (RepositoryException re) {					
					logError("Error while getting the associated accessories for the phone - "+phoneProductId + ":", re);
				}		
			
		}
		if(isLoggingDebug()){
			logDebug("Total No.Of Accessory Products::::"+products.size());
		}
		logInfo("Exiting the getListOfAccessoryProducts method...");
		return products;
	}
	
	/**
	 * Method for getting the Addon Products:-
	 * 
	 * @param categoryId
	 * @param planProductId
	 * @return
	 */
	public List<AddonVO> getListOfAddonProducts(final String categoryId, final String planProductId) {	
		logInfo("Entering into the getListOfAddonProducts method...");		
		if(isLoggingDebug()){
			logDebug("Category Id::::"+categoryId);
			logDebug("Plan Product Id::::"+planProductId);
		}
		final List<AddonVO> products = new ArrayList<AddonVO>();		
		if(planProductId == null){			
			RepositoryItem planCategoryItem;
			try {
				planCategoryItem = getCatalogRepository().getItem(categoryId, CricketCommonConstants.CATEGORY);
				final List<RepositoryItem> childProducts = (List<RepositoryItem>)planCategoryItem.getPropertyValue(CricketCommonConstants.CHILD_PRODUCTS);
				planCategoryItem = null;
				if (childProducts != null) {
					boolean hasStarted = true;
					boolean hasEnded = false;
					Timestamp startDate;
					Timestamp endDate;
					Date currentDate = new Date();
					AddonVO addonVO = null;
					for (RepositoryItem repositoryItem : childProducts) {
						hasStarted = true;
						hasEnded = false;
						startDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
						endDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
						if(startDate != null && startDate.compareTo(currentDate) > 0) {
							hasStarted = false;
						}	
						if(endDate != null && endDate.compareTo(currentDate) < 0) {
							hasEnded = true;
						}	
						if(hasStarted && !hasEnded){
							addonVO = populateAddonVO(repositoryItem);
							if(addonVO != null) {
								products.add(addonVO);
							}
						}
					}
				}
			} catch (RepositoryException re) {					
				logError("Error while getting the addons for category - " + categoryId + ":", re);
			}				
			
		} 
		if(isLoggingDebug()){
			logDebug("Total No.Of addon Products::::"+products.size());
		}
		logInfo("Exiting the getListOfAddonProducts method...");
		return products;
	}
	

	/**
	 * Method for getting the Plan Products:-
	 * 
	 * @param categoryId
	 * @param phoneProductId
	 * @param zipCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PlanVO> getListOfPlanProducts(final String categoryId, final String phoneProductId, final String marketCode, final String marketType) {
		final List<PlanVO> products = new ArrayList<PlanVO>();	
		PlanVO planVO = null;
		boolean hasStarted = true;
		boolean hasEnded = false;
		Timestamp startDate;
		Timestamp endDate;
		Date currentDate = new Date();
		if(phoneProductId == null){
			
			/*
			 * 1 ) Query the "market-info" item descriptor for the given zip code. You will get the list of market info for the given zipCode
			 * 2) Query the product item descriptor with the markets fetched in step1.
			 * 
			 * RepositoryItem[] productItems =
			 * catalogRepository.plan-product.query.....
			 */
			
			try {
				RepositoryItem planCategoryItem = getCatalogRepository().getItem(categoryId, CricketCommonConstants.CATEGORY);		
				vlogDebug("Plan Category ID : " + categoryId);
				vlogDebug("Plan Category Item : " + planCategoryItem);
				final List<RepositoryItem> childPlanItems = (List<RepositoryItem>)planCategoryItem.getPropertyValue(CricketCommonConstants.CHILD_PRODUCTS);
				planCategoryItem = null;
				vlogDebug("Plan products in Category : " + childPlanItems);
				if(childPlanItems != null) {
					if (marketCode == null || marketCode.isEmpty()) {
						vlogDebug("Market code is empty");
						for (RepositoryItem planItem : childPlanItems) {
							vlogDebug("Plan item : " + planItem);
							boolean planAlreadyInList = false;
							String groupCode = (String)planItem.getPropertyValue(CricketCommonConstants.PROP_GROUP_CODE);
							vlogDebug("Group code : " + groupCode);
							if(groupCode != null) {
								for(PlanVO planVOInList : products){
									if(planVOInList.getGroupCode().equalsIgnoreCase(groupCode)){
										planAlreadyInList = true;
										vlogDebug("Plan laready in list : " + planVOInList);
										break;
									}
								}
							}
							if(!planAlreadyInList){
								hasStarted = true;
								hasEnded = false;
								startDate = (Timestamp) planItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
								endDate = (Timestamp) planItem.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
								if(startDate != null && startDate.compareTo(currentDate) > 0) {
									hasStarted = false;
								}	
								if(endDate != null && endDate.compareTo(currentDate) < 0) {
									hasEnded = true;
								}	
								if(hasStarted && !hasEnded){
									vlogDebug("Populating plan VO");
									planVO = populatePlanVO(planItem);
									vlogDebug("Populated plan VO");
									if(planVO != null && planVO.getPlanType() != null && planVO.getPlanType().equalsIgnoreCase(getCktConfiguration().getOOFPlanType())) {
										products.add(planVO);
										vlogDebug("Added plan to VO" + planVO);
									}
								}
							}
						}
					} else {
							vlogDebug("Market Type : " + marketType);
							if(null!= marketType && marketType.equalsIgnoreCase(CricketCommonConstants.OOF_CUSTOMER)) {
								for (RepositoryItem planItem : childPlanItems) {
									vlogDebug("Plan item : " + planItem);
									boolean planAlreadyInList = false;
									String groupCode = (String)planItem.getPropertyValue(CricketCommonConstants.PROP_GROUP_CODE);
									vlogDebug("Group code : " + groupCode);
									if(groupCode != null) {
										for(PlanVO planVOInList : products){
											if(planVOInList.getGroupCode().equalsIgnoreCase(groupCode)){
												planAlreadyInList = true;
												vlogDebug("Plan already in list : " + planVOInList);
												break;
											}
										}
									}
									if(!planAlreadyInList){
										hasStarted = true;
										hasEnded = false;
										startDate = (Timestamp) planItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
										endDate = (Timestamp) planItem.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
										if(startDate != null && startDate.compareTo(currentDate) > 0) {
											hasStarted = false;
										}	
										if(endDate != null && endDate.compareTo(currentDate) < 0) {
											hasEnded = true;
										}	
										if(hasStarted && !hasEnded){
											vlogDebug("Populating plan VO");
											planVO = populatePlanVO(planItem);
											vlogDebug("Populated plan VO");
											if(planVO != null && planVO.getPlanType() != null && planVO.getPlanType().equalsIgnoreCase(getCktConfiguration().getOOFPlanType())) {
												products.add(planVO);
												vlogDebug("Added plan VO to list");
											}
										}
									}
								}
							} else {
								for (RepositoryItem planItem : childPlanItems) {
									vlogDebug("Plan item : " + planItem);
									final List <RepositoryItem> markets = (List<RepositoryItem>) planItem.getPropertyValue(CricketCommonConstants.PROP_MARKETS);
									if (markets != null) {
										for(RepositoryItem market : markets) {											
											String marketCodeRep = (String) market.getPropertyValue(CricketCommonConstants.PROP_MARKET_CODE);
											vlogDebug("Market Type : " + marketCodeRep);
											if(marketCodeRep!=null && marketCode != null && marketCodeRep.equals(marketCode)) {
												boolean planAlreadyInList = false;
												String groupCode = (String)planItem.getPropertyValue(CricketCommonConstants.PROP_GROUP_CODE);
												vlogDebug("Plan Group Code : " + groupCode);
												if(groupCode != null) {
													for(PlanVO planVOInList : products){
														if(planVOInList.getGroupCode().equalsIgnoreCase(groupCode)){
															planAlreadyInList = true;
															vlogDebug("Plan already in list : " + planVOInList);
															break;
														}
													}
												}
												if(!planAlreadyInList){
													hasStarted = true;
													hasEnded = false;
													startDate = (Timestamp) planItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
													endDate = (Timestamp) planItem.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
													if(startDate != null && startDate.compareTo(currentDate) > 0) {
														hasStarted = false;
													}	
													if(endDate != null && endDate.compareTo(currentDate) < 0) {
														hasEnded = true;
													}	
													if(hasStarted && !hasEnded){
														vlogDebug("Populating Plan VO : " + planItem);
														planVO = populatePlanVO(planItem);
														vlogDebug("Populated Plan VO : " + planItem);
														if(isLoggingDebug()){
															logDebug("Requested marketCode:" + marketCode);	
															logDebug("Market marketCodeRep:" + marketCodeRep);
															logDebug("Market Id:"+market.getRepositoryId());
															logDebug("Phone VO:"+planVO);												
														}
														if(planVO != null && planVO.getPlanType() != null && !planVO.getPlanType().equalsIgnoreCase(getCktConfiguration().getOOFPlanType())) {
															products.add(planVO);
															vlogDebug("Added plan to list : " + planVO);
															break;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}				
			} catch (RepositoryException e) {
				logError(e);
			}
		} else {				
				RepositoryItem phoneProductItem;
				try {
					phoneProductItem = getCatalogRepository().getItem(phoneProductId, CricketCommonConstants.PHONE_PRODUCT);
					vlogDebug("Phone Product : " + phoneProductItem);
					if (phoneProductItem != null) {
						final Set<RepositoryItem> compatiblePlans = (Set<RepositoryItem>)phoneProductItem.getPropertyValue(CricketCommonConstants.COMPATIBLE_PLANS);
						phoneProductItem = null;
						vlogDebug("Compatible Plans : " + compatiblePlans);
						if (compatiblePlans != null) {
							if (marketCode == null || marketCode.isEmpty()) {
								vlogDebug("Market Code is empty : " + marketCode);
								for (RepositoryItem repositoryItem : compatiblePlans) {
									ChangeAwareSet parentCategoriesSet = (ChangeAwareSet)repositoryItem.getPropertyValue(CricketCommonConstants.PARENT_CATEGORIES);
									vlogDebug("Parent Categories set : " + parentCategoriesSet);
									List<RepositoryItem> parentCategories = new ArrayList<RepositoryItem>();
									for (Object parentCategoryItem : parentCategoriesSet) {
										parentCategories.add((RepositoryItem) parentCategoryItem);
									}
									vlogDebug("Parent Categories list : " + parentCategories);
									String parentCategoryId = null;
									boolean isPlanCategorised = false;
									if(parentCategories != null) {
										for(RepositoryItem parentCategory : parentCategories){
											parentCategoryId = parentCategory.getRepositoryId();
											if(parentCategoryId != null && parentCategoryId.equalsIgnoreCase(getCktConfiguration().getAllPlansCategoryId())){
												isPlanCategorised = true;
												vlogDebug("Plan is categorised : " + parentCategoryId);
											}
										}
									}
									boolean planAlreadyInList = false;
									String groupCode = (String)repositoryItem.getPropertyValue(CricketCommonConstants.PROP_GROUP_CODE);
									vlogDebug("Plan Group code : " + groupCode);
									if(groupCode != null) {
										for(PlanVO planVOInList : products){
											if(planVOInList.getGroupCode().equalsIgnoreCase(groupCode)){
												planAlreadyInList = true;
												vlogDebug("Plan is already in list : " + planVOInList);
												break;
											}
										}
									}
									if(!planAlreadyInList && isPlanCategorised){
										hasStarted = true;
										hasEnded = false;
										startDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
										endDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
										if(startDate != null && startDate.compareTo(currentDate) > 0) {
											hasStarted = false;
										}	
										if(endDate != null && endDate.compareTo(currentDate) < 0) {
											hasEnded = true;
										}	
										if(hasStarted && !hasEnded){
											planVO = populatePlanVO(repositoryItem);
											if(planVO != null && planVO.getPlanType() != null && planVO.getPlanType().equalsIgnoreCase(getCktConfiguration().getOOFPlanType())) {
												products.add(planVO);
												vlogDebug("Adding plan to VO list : " + planVO);
											}
										}
									}
								}
							} else {
								vlogDebug("Market Type : " + marketType);
								if(null!= marketType && marketType.equalsIgnoreCase(CricketCommonConstants.OOF_CUSTOMER)) {
									for (RepositoryItem repositoryItem : compatiblePlans) {
										vlogDebug("Plan item : " + repositoryItem);
										ChangeAwareSet parentCategoriesSet = (ChangeAwareSet)repositoryItem.getPropertyValue(CricketCommonConstants.PARENT_CATEGORIES);
										vlogDebug("Parent categories in set : " + parentCategoriesSet);
										List<RepositoryItem> parentCategories = new ArrayList<RepositoryItem>();
										for (Object parentCategoryItem : parentCategoriesSet) {
											parentCategories.add((RepositoryItem) parentCategoryItem);
										}
										vlogDebug("Parent categories in list : " + parentCategories);
										String parentCategoryId = null;
										boolean isPlanCategorised = false;
										if(parentCategories != null) {
											for(RepositoryItem parentCategory : parentCategories){
												parentCategoryId = parentCategory.getRepositoryId();
												if(parentCategoryId != null && parentCategoryId.equalsIgnoreCase(getCktConfiguration().getAllPlansCategoryId())){
													isPlanCategorised = true;
													vlogDebug("Plan is categorised : " + parentCategoryId);
												}
											}
										}
										boolean planAlreadyInList = false;
										String groupCode = (String)repositoryItem.getPropertyValue(CricketCommonConstants.PROP_GROUP_CODE);
										vlogDebug("Plan Group code : " + groupCode);
										if(groupCode != null) {
											for(PlanVO planVOInList : products){
												if(planVOInList.getGroupCode().equalsIgnoreCase(groupCode)){
													planAlreadyInList = true;
													vlogDebug("Plan is already in list");
													break;
												}
											}
										}
										if(!planAlreadyInList && isPlanCategorised){
											hasStarted = true;
											hasEnded = false;
											startDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
											endDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
											if(startDate != null && startDate.compareTo(currentDate) > 0) {
												hasStarted = false;
											}	
											if(endDate != null && endDate.compareTo(currentDate) < 0) {
												hasEnded = true;
											}	
											if(hasStarted && !hasEnded){
												planVO = populatePlanVO(repositoryItem);
												if(planVO != null && planVO.getPlanType() != null && planVO.getPlanType().equalsIgnoreCase(getCktConfiguration().getOOFPlanType())) {
													products.add(planVO);
													vlogDebug("Adding plan to VO list" + planVO);
												}
											}
										}
									}
								} else {
									for (RepositoryItem repositoryItem : compatiblePlans) {
										ChangeAwareSet parentCategoriesSet = (ChangeAwareSet)repositoryItem.getPropertyValue(CricketCommonConstants.PARENT_CATEGORIES);
										vlogDebug("Plan categories in set" + parentCategoriesSet);
										List<RepositoryItem> parentCategories = new ArrayList<RepositoryItem>();
										for (Object parentCategoryItem : parentCategoriesSet) {
											parentCategories.add((RepositoryItem) parentCategoryItem);
										}
										vlogDebug("Plan categories in list" + parentCategories);
										String parentCategoryId = null;
										boolean isPlanCategorised = false;
										if(parentCategories != null) {
											for(RepositoryItem parentCategory : parentCategories){
												parentCategoryId = parentCategory.getRepositoryId();
												if(parentCategoryId != null && parentCategoryId.equalsIgnoreCase(getCktConfiguration().getAllPlansCategoryId())){
													isPlanCategorised = true;
													vlogDebug("Plan is categorised");
												}
											}
										}
										final List <RepositoryItem> markets = (List<RepositoryItem>) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_MARKETS);
										if (markets != null) {
											for(RepositoryItem market : markets) {
												String marketCodeRep = (String) market.getPropertyValue(CricketCommonConstants.PROP_MARKET_CODE);
												vlogDebug("Market Code while iterating : " + marketCodeRep);
												if(marketCodeRep!=null && marketCode != null && marketCodeRep.equals(marketCode)) {
													boolean planAlreadyInList = false;
													String groupCode = (String)repositoryItem.getPropertyValue(CricketCommonConstants.PROP_GROUP_CODE);
													vlogDebug("Plan group code : " + groupCode);
													if(groupCode != null) {
														for(PlanVO planVOInList : products){
															if(planVOInList.getGroupCode().equalsIgnoreCase(groupCode)){
																planAlreadyInList = true;
																vlogDebug("Plan already in list : " + planVOInList);
																break;
															}
														}
													}
													if(!planAlreadyInList && isPlanCategorised){
														hasStarted = true;
														hasEnded = false;
														startDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
														endDate = (Timestamp) repositoryItem.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
														if(startDate != null && startDate.compareTo(currentDate) > 0) {
															hasStarted = false;
														}	
														if(endDate != null && endDate.compareTo(currentDate) < 0) {
															hasEnded = true;
														}	
														if(hasStarted && !hasEnded){
															vlogDebug("Populating plan VO");
															planVO = populatePlanVO(repositoryItem);
															if(isLoggingDebug()){
																logDebug("Requested marketCode:" + marketCode);	
																logDebug("Market marketCodeRep:" + marketCodeRep);
																logDebug("Market Id:"+market.getRepositoryId());
																logDebug("Phone VO:"+planVO);												
															}
															vlogDebug("Populated plan VO");
															if(planVO != null && planVO.getPlanType() != null && !planVO.getPlanType().equalsIgnoreCase(getCktConfiguration().getOOFPlanType())) {
																products.add(planVO);
																vlogDebug("Adding plan to plan VO list" + planVO);
																break;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				} catch (RepositoryException re) {					
					logError("Error while getting the compatible plans for the phone - "+phoneProductId + ":", re);
				}		
			
		}

		return products;
	}
	
	
	/**
	 * 
	 * @returns the list of compatible plans in the form of a list of VOs
	 */
	public List<PlanVO> getListOfCompatiblePlans(List<RepositoryItem> planItems) {
		List<PlanVO> planVOsForDisplay = new ArrayList<PlanVO>();
		boolean hasStarted = true;
		boolean hasEnded = false;
		Timestamp startDate;
		Timestamp endDate;
		Date currentDate = new Date();
		PlanVO planVO = null;
		for (RepositoryItem planItem : planItems) {
			boolean planAlreadyInList = false;
			String groupCode = (String)planItem.getPropertyValue(CricketCommonConstants.PROP_GROUP_CODE);
			vlogDebug("Group code" + groupCode);
			if(groupCode != null) {
				for(PlanVO planVOInList : planVOsForDisplay){
					if(planVOInList.getGroupCode().equalsIgnoreCase(groupCode)){
						planAlreadyInList = true;
						vlogDebug("Plan already in list" + planVOInList);
						break;
					}
				}
				if(!planAlreadyInList){
					ChangeAwareSet parentCategoriesSet = (ChangeAwareSet)planItem.getPropertyValue(CricketCommonConstants.PARENT_CATEGORIES);
					vlogDebug("Parent categories in set : " + parentCategoriesSet);
					List<RepositoryItem> parentCategories = new ArrayList<RepositoryItem>();
					for (Object parentCategoryItem : parentCategoriesSet) {
						parentCategories.add((RepositoryItem) parentCategoryItem);
					}
					vlogDebug("Parent categories in list : " + parentCategories);
					String parentCategoryId = null;
					boolean isPlanCategorised = false;
					if(parentCategories != null) {
						for(RepositoryItem parentCategory : parentCategories){
							parentCategoryId = parentCategory.getRepositoryId();
							if(parentCategoryId != null && parentCategoryId.equalsIgnoreCase(getCktConfiguration().getAllPlansCategoryId())){
								isPlanCategorised = true;
								vlogDebug("Plan is catgeorised : " + parentCategoryId);
							}
						}
					}
					if(isPlanCategorised){
						hasStarted = true;
						hasEnded = false;
						startDate = (Timestamp) planItem.getPropertyValue(CricketCommonConstants.PROP_START_DATE);
						endDate = (Timestamp) planItem.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
						if(startDate != null && startDate.compareTo(currentDate) > 0) {
							hasStarted = false;
						}	
						if(endDate != null && endDate.compareTo(currentDate) < 0) {
							hasEnded = true;
						}	
						if(hasStarted && !hasEnded){
							planVO = populatePlanVO(planItem);
							planVOsForDisplay.add(planVO);
							vlogDebug("Addin plan to VO list for display : " + planVO);
						}
					}
				}
			}			
		}
		return planVOsForDisplay;
	}
	
	/**
	 * @param phoneProductId
	 * @param zipCode
	 * @return
	 */
	public PhoneVO getPhoneDetails(final String phoneProductId, final String marketCode){
		PhoneVO phoneVO = null;
		
		RepositoryItem phoneProductItem;
		try {
			phoneProductItem = getCatalogRepository().getItem(phoneProductId, CricketCommonConstants.PHONE_PRODUCT);			
			if (phoneProductItem != null) {
				phoneVO = populatePhoneVO(phoneProductItem);				
			}
		} catch (RepositoryException re) {					
			logError("Error while getting the PhoneDetails for the phone id - "+phoneProductId + ":", re);
		}		
		return phoneVO;
	}
	
	/**
	 * @param productId
	 * @return
	 */
	public AccessoryVO getAccessoryDetails(final String productId){
		AccessoryVO accessoriesVO = null;	
		RepositoryItem accessoryProductItem;
		try {
			accessoryProductItem = getCatalogRepository().getItem(productId, CricketCommonConstants.ACCESSORY_PRODUCT);
			if (accessoryProductItem != null) {
				//populate Product VO
				accessoriesVO = populateAccessoryVO(accessoryProductItem);			
			}
		} catch (RepositoryException re) {
			logError("Error while getting the getAccessoryDetails for the accessory id - "+productId + ":", re);
			
		}		
		return accessoriesVO;
	}



	
	
	/**
	 * @param repositoryItem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PhoneVO populatePhoneVO(RepositoryItem repositoryItem){
		PhoneVO phoneVO = new PhoneVO();
		
		// Setting the Price for all the Skus for the Product selected
		Map <String, Double> setSkuPriceMap = new HashMap<String, Double>();
		double listPrice = 0.0;			
		RepositoryItem listPriceItem;
		try {
				Profile profile = profileService.getCurrentProfile();
				listPriceItem  = (RepositoryItem) profile.getPropertyValue(CricketCommonConstants.PROP_PRICE_LIST_ITEM);
				List<RepositoryItem> childSkus = (List<RepositoryItem>)repositoryItem.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
				String productId = (String) repositoryItem.getRepositoryId();
				for (RepositoryItem childSku : childSkus) {
					String skuId = (String) childSku.getRepositoryId();
					RepositoryItem priceItem = getPriceListManager().getPrice(listPriceItem, productId, skuId);	
					if(priceItem !=null){
						listPrice= (Double) priceItem.getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);											
					}else{
						if(null != childSku.getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE))
						listPrice = (Double)childSku.getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
					}																	
					if((listPrice >= 0) && !(StringUtils.isBlank(skuId))){
						setSkuPriceMap.put(skuId, listPrice);
					}
				}			
			} catch (PriceListException e) {
					logError("Error while getting the list price - "+listPrice + ":", e);
			}
		phoneVO.setPhoneSkuPrice(setSkuPriceMap);
		// End of setting the Price for all the Skus for the Product selected
		
		//Populate the object
		//populate overview Tab
		//populate specification Tab
		//populate Help and Resource Tab
		//populate compatible plans
		//populate key features 
		//populate included accessories
		//populate associated accessories 
		//populate phone specifications
		//populate similar phones
		
		return phoneVO;
	}
	
	/**
	 * populatePlanVO() used to set the details in PlanVO and setting it there 
	 * Operations:- To display the Plan Price
	 * @param childProduct
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PlanVO populatePlanVO(RepositoryItem planProductItem){
		
		if (isLoggingDebug()) {
			logDebug("Starting Method: populatePlanVO() ");
			logDebug("planProductItem:-"+planProductItem);
		}
		double listPrice = 0.0;
		RepositoryItem planGroupItem = null;
		String greenBoxContent = null;
		Set<RepositoryItem> planGroupItems;
		PlanVO planVO = new PlanVO();
		List<RepositoryItem> childSkus = (List<RepositoryItem>)planProductItem.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);	
		planGroupItems = (Set<RepositoryItem>)planProductItem.getPropertyValue(CricketCommonConstants.PLAN_GROUPS);
		if(planGroupItems != null && planGroupItems.size() > 0) {
			for(RepositoryItem planGroupItemInSet : planGroupItems){
				planGroupItem = planGroupItemInSet;	
				break;
			}
		}
		if (childSkus!=null && childSkus.size() > 0) {
			RepositoryItem childSku = childSkus.get(0);
			String skuId = (String)childSku.getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
			planVO.setDefaultSkuId(skuId);
			
		}
		if(childSkus != null && childSkus.size() > 0) {
			if (isLoggingDebug()) {
				logDebug("childSkus:- "+childSkus);		
			}
			if(null != ((RepositoryItem)childSkus.get(0)).getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE)) {
				listPrice = (Double)((RepositoryItem)childSkus.get(0)).getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
			}
			planVO.setFinalPrice(listPrice);
		}
		childSkus = null;
		String groupCode = (String)planProductItem.getPropertyValue(CricketCommonConstants.PROP_GROUP_CODE);
		if(groupCode != null && !groupCode.isEmpty()) {
			planVO.setGroupCode(groupCode);
		}
		String planType = (String)planProductItem.getPropertyValue(CricketCommonConstants.PROP_PLAN_TYPE);
		if(planType != null && !planType.isEmpty()) {
			planVO.setPlanType(planType);
		}
		String ratePlanType = (String)planProductItem.getPropertyValue(CricketCommonConstants.PROP_RATE_PLAN_TYPE);
		if(ratePlanType != null && !ratePlanType.isEmpty()) {
			planVO.setRatePlanType(ratePlanType);
		} else {
			planVO.setRatePlanType(CricketCommonConstants.CHAR_V);
		}
		if (planVO.getRatePlanType().equals(CricketCommonConstants.CHAR_D)) {
			String dataLimit = null;
			String dataMessage = null;
			if(planGroupItem != null){
				dataLimit = (String)planGroupItem.getPropertyValue(CricketCommonConstants.DATA_LIMIT);
				dataMessage = (String)planGroupItem.getPropertyValue(CricketCommonConstants.DATA_MESSAGE);
			}
			if (dataLimit != null && !dataLimit.isEmpty() && dataMessage != null && !dataMessage.isEmpty()) {
				planVO.setDataMessage(dataMessage);
				planVO.setDataLimit(dataLimit);
			} else if(dataLimit != null && !dataLimit.isEmpty() && (dataMessage == null || dataMessage.isEmpty())) {
				planVO.setDataMessage(dataMessage);
				planVO.setDataLimit(getDefaultDataLimit());
			} else {
				planVO.setDataMessage(getDefaultDataMessage());
				planVO.setDataLimit(getDefaultDataLimit());
			}
		}
		if(planGroupItem != null){
			greenBoxContent = (String)planGroupItem.getPropertyValue(CricketCommonConstants.GREEN_BOX_CONTENT);
			planVO.setGreenBoxContent(greenBoxContent);
		}
		
		String productId = (String)planProductItem.getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
		planVO.setProductId(productId);
		Map<String, PlanSpecsVO> planSpecsMap = createPlanSpecsMap(planGroupItem);
		if(planSpecsMap != null) {
			planVO.setPlanSpecs(planSpecsMap);
		}
		List<String> features = new ArrayList<String>();
		if(planGroupItem != null){
			features = (List<String>)planGroupItem.getPropertyValue(CricketCommonConstants.KEY_FEATURES);			
		}
		if (features != null) {
			planVO.setFeatures(features);
		}
		return planVO;
	}
	
	public RepositoryItem getPlanGroup(Set<RepositoryItem> planGroupItems) {
		RepositoryItem planGroupItem = null;
		if(planGroupItems != null && planGroupItems.size() > 0) {
			for(RepositoryItem planGroupItemInSet : planGroupItems){
				planGroupItem = planGroupItemInSet;	
				break;
			}
		}
		return planGroupItem;
	}
	
	/**
	 * @param profile
	 */
		
	private Map<String, PlanSpecsVO> createPlanSpecsMap(
			RepositoryItem planGroupItem) {
		Map<String, PlanSpecsVO> planSpecsMap = new LinkedHashMap<String, PlanSpecsVO>();
		if(planGroupItem != null) {
			List<RepositoryItem> planSpecs = (List<RepositoryItem>)planGroupItem.getPropertyValue(CricketCommonConstants.PLAN_SPECS);
			String specKey;
			String specValue;
			String specBool;
			String specType;
			PlanSpecsVO specsVO;
			for (RepositoryItem planSpec : planSpecs) {
				specsVO = new PlanSpecsVO();
				specKey = (String)planSpec.getPropertyValue(CricketCommonConstants.SPECIFICATION_NAME);
				specValue = (String)planSpec.getPropertyValue(CricketCommonConstants.SPEC_VALUE);
				specType = (String)planSpec.getPropertyValue(CricketCommonConstants.SPEC_TYPE);
				specBool = (String)planSpec.getPropertyValue(CricketCommonConstants.BOOL_VALUE);
				boolean specBoolValue = false;
				if(specBool != null && specBool.equalsIgnoreCase(Boolean.TRUE.toString())) {
					specBoolValue = true;
				}
				if(specKey != null) {
					if(specValue != null) {
						specsVO.setSpecValue(specValue);
					}
					if(specType != null) {
						specsVO.setSpecType(specType);
					}
					if(specBool != null) {
						specsVO.setSpecBoolValue(specBoolValue);
					}
					planSpecsMap.put(specKey, specsVO);
				}
			}
		}
		return planSpecsMap;
	}

	/**
	 * populateAccessoryVO() used to set the details in AccessoryVO and setting it there 
	 * Operations:- To display the Accessory Price
	 * @param childProduct
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private AccessoryVO populateAccessoryVO(final RepositoryItem accessoryProductItem){
		if (isLoggingDebug()) {
			logDebug("Starting Method: populateAccessoryVO() ");
			logDebug("accessoryProductItem:-"+accessoryProductItem);
		}
		final AccessoryVO accessoryVO = new AccessoryVO();
		accessoryVO.setProductId((String)accessoryProductItem.getRepositoryId());
		accessoryVO.setDisplayName((String)accessoryProductItem.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME));
		RepositoryItem largeImage = (RepositoryItem)accessoryProductItem.getPropertyValue(CricketCommonConstants.FULL_IMAGE);
		if (largeImage != null) {
			String largeImageUrl = (String)largeImage.getPropertyValue(CricketCommonConstants.URL);
			List<String> largeImages = new ArrayList<String>();
			largeImages.add(largeImageUrl);
			accessoryVO.setLargeImages(largeImages);
		}
		List<RepositoryItem> childSkus = (List<RepositoryItem>) accessoryProductItem.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
		if(childSkus != null && childSkus.size() > 0) {
			accessoryVO.setDefaultSkuId((String)childSkus.get(0).getPropertyValue(CricketCommonConstants.REPOSITORY_ID));
		}
		RepositoryItem manufacturer = (RepositoryItem)accessoryProductItem.getPropertyValue(CricketCommonConstants.MANUFACTURER);
		if (manufacturer != null) {
			String manufacturerName = (String) manufacturer.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME);
			if(manufacturerName != null) {
				accessoryVO.setManufacturer(manufacturerName);
			}
		}
		if (isLoggingDebug()) {
			logDebug("Ending Method: populateAccessoryVO() ");			
		}
		return accessoryVO;
	}
	
	/**
	 * populateAddonVO() used to set the details in AddonVO and setting it there 
	 * Operations:- To display the Addon Price
	 * @param childProduct
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private AddonVO populateAddonVO(final RepositoryItem addonProductItem){
		if (isLoggingDebug()) {
			logDebug("Starting Method: populateAddonVO() ");
			logDebug("addonProductItem:-"+addonProductItem);
		}
		boolean flag = false;
		Boolean isGenericAddon = (Boolean)addonProductItem.getPropertyValue("isGenericAddOn");
		if(isGenericAddon == null || isGenericAddon == Boolean.FALSE) {
			return null;
		}
		
		double listPrice = 0.0;
		final AddonVO addonVO = new AddonVO();
		addonVO.setProductId((String)addonProductItem.getRepositoryId());
		addonVO.setDisplayName((String)addonProductItem.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME));
		
		//get ancestor categories
		List<RepositoryItem> ancestorCategories = (List<RepositoryItem>)addonProductItem.getPropertyValue(CricketCommonConstants.ANCESTOR_CATEGORIES);
		if(ancestorCategories != null) {
			String seoString = null;
			for (RepositoryItem parentCategory : ancestorCategories) {
				seoString = (String)parentCategory.getPropertyValue(CricketCommonConstants.SEO_STRING);
				if(seoString != null && !seoString.isEmpty()) {
					addonVO.setSeoString(seoString);
					break;
				}
			}
			if(seoString == null || seoString.isEmpty()) {
				seoString = "other";
				addonVO.setSeoString(seoString);
			}
		}
		Map<String, String> ancestorCatsIdNameMap = new HashMap<String, String>();
		for (RepositoryItem ancestorCategory : ancestorCategories) {
			String id = (String)ancestorCategory.getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
			String name = (String)ancestorCategory.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME);
			ancestorCatsIdNameMap.put(id, name);
		}
		addonVO.setAncestorCategories(ancestorCatsIdNameMap);
		//set child SKU id
		List<RepositoryItem> childSKUs = (List<RepositoryItem>) addonProductItem.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
		if(!childSKUs.isEmpty()) {
			addonVO.setSkuId(childSKUs.get(0).getRepositoryId());
		}
		
		flag = setIncludedFlag((String)addonProductItem.getRepositoryId());
		if(flag) {
			addonVO.setIncluded(flag);
		} else {
			addonVO.setIncluded(false);
		}		
		if(childSKUs!=null && childSKUs.size() > 0){
			if (isLoggingDebug()) {
				logDebug("childSkus:- "+childSKUs);			
			}
			if(null != ((RepositoryItem)childSKUs.get(0)).getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE))
				listPrice = (Double)((RepositoryItem)childSKUs.get(0)).getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
			}
		if((listPrice >= 0)){
			addonVO.setFinalPrice(listPrice);
		}
		return addonVO;
	}
	
	private boolean setIncludedFlag(String pRepositoryId) {
		vlogDebug("Set the included flag in addonVo for the product : "+pRepositoryId);
		if(getCktProfilePath() != null) {
			CricketProfile cktProfile = (CricketProfile)  ServletUtil.getCurrentRequest().resolveName(mCktProfileComponentName);
			List<String> userPurchasedOfferingProducts = cktProfile.getUserPurchasedOfferingProducts();
			if(null != userPurchasedOfferingProducts && userPurchasedOfferingProducts.size() > 0) {
				vlogDebug("userPurchasedOfferingProducts :  "+userPurchasedOfferingProducts);
				for (String productId : userPurchasedOfferingProducts) {
					if(productId.equals(pRepositoryId)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	/**
	 * populateAddonVODynamic() used to set the details in AddonVO and setting it there 
	 * Operations:- To display the Addon Price
	 * @param childProduct
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private AddonVO populateAddonVODynamic(final RepositoryItem addonProductItem, boolean isIncluded){
		if (isLoggingDebug()) {
			logDebug("Starting Method: populateAddonVO() ");
			logDebug("addonProductItem:-"+addonProductItem);
		}
		double listPrice = 0.0;
		boolean flag = false;
		String repId = null;
		final AddonVO addonVO = new AddonVO();
		addonVO.setProductId((String)addonProductItem.getRepositoryId());
		addonVO.setDisplayName((String)addonProductItem.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME));
		//addonVO.setIncluded(isIncluded);
		//get ancestor categories
		String id;
		String name;
		List<RepositoryItem> ancestorCategories = (List<RepositoryItem>)addonProductItem.getPropertyValue(CricketCommonConstants.ANCESTOR_CATEGORIES);
		Map<String, String> ancestorCatsIdNameMap = new HashMap<String, String>();
		for (RepositoryItem ancestorCategory : ancestorCategories) {
			id = (String)ancestorCategory.getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
			name = (String)ancestorCategory.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME);
			ancestorCatsIdNameMap.put(id, name);
		}
		addonVO.setAncestorCategories(ancestorCatsIdNameMap);
		//set child SKU id
		List<RepositoryItem> childSKUs = (List<RepositoryItem>) addonProductItem.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
		if(!childSKUs.isEmpty()) {
			repId =childSKUs.get(0).getRepositoryId();
			addonVO.setSkuId(repId);
			addonVO.setIncluded(setIncludedFlag(repId));
		}
		// Setting List price for the default Sku
		if(childSKUs != null && childSKUs.size() > 0) {
			if(null != ((RepositoryItem)childSKUs.get(0)).getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE))
			listPrice = (Double)((RepositoryItem)childSKUs.get(0)).getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
		}
		if((listPrice >= 0)){
			addonVO.setFinalPrice(listPrice);
		}
		return addonVO;
	}
	
	/**
	 * populatePhoneListingProduct() used to set the details in PhoneVO and setting it there 
	 * Operations:- To display the Phone Listing Price
	 * @param childProduct
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PhoneVO populatePhoneListingProduct(final RepositoryItem childProduct, PricingModelHolder userPricingHolder){
		if (isLoggingDebug()) {
			logDebug("Starting Method: populatePhoneListingProduct() ");
		}
		
		double listPrice = 0.0;
		double finalPrice = 0.0;
		Object isOOFPhone = childProduct.getPropertyValue(CricketCommonConstants.IS_OOF_PHONE);
		if (isOOFPhone == null) {
			isOOFPhone = false;
		}
		final PhoneVO phoneVO = new PhoneVO();
		try {
			phoneVO.setOOFPhone((Boolean)isOOFPhone);//changed for the defect #875
			phoneVO.setProductId((String)childProduct.getRepositoryId());
			phoneVO.setDisplayName((String)childProduct.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME));
			String productType = (String)childProduct.getPropertyValue(CricketCommonConstants.TYPE);	
			String availableForCustomerUpgrade = (String)childProduct.getPropertyValue(CricketCommonConstants.AVAIL_FOR_CUST_UPGRADES);
			phoneVO.setAvailForCustUppgrades(availableForCustomerUpgrade);
			if(isLoggingDebug()){
				logDebug("Product Type:"+productType);
			}		
			if(!StringUtils.isEmpty(productType) && CricketCommonConstants.PHONE_PRODUCT.equalsIgnoreCase(productType)){			
				if(childProduct.getPropertyValue(CricketCommonConstants.IS_FINANCING_GAVAILABLE)!=null){
					phoneVO.setFinancingAvailable(((Boolean)childProduct.getPropertyValue(CricketCommonConstants.IS_FINANCING_GAVAILABLE)).booleanValue());
				}			
			}		
			final List<String> images = new ArrayList<String>();		
			final RepositoryItem mediaItem = (RepositoryItem)childProduct.getPropertyValue(imagePropertyName);
			if(mediaItem!=null){
				images.add((String)	mediaItem.getPropertyValue(CricketCommonConstants.URL));
			}									
			phoneVO.setLargeImages(images);
			List<RepositoryItem> childSkus = (List<RepositoryItem>)childProduct.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
			if(childSkus!=null && childSkus.size() > 0) {
				if(null != ((RepositoryItem)childSkus.get(0)).getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE)) {
					listPrice = (Double)((RepositoryItem)childSkus.get(0)).getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
				}
			}
			//Get Promotions based on Product
			finalPrice = listPrice;
			if(childSkus.size() > 0 && listPrice > 0) {
				 List<RepositoryItem> promotions = getCktPromotionManager().getPromotions(childProduct, (RepositoryItem) childSkus.get(0),userPricingHolder);
							 
				 if(null != promotions){
					 for(RepositoryItem promo : promotions){			 
						 if (isLoggingDebug()) {
							logDebug("Promotion Id : " + promo.getRepositoryId() + "promotion Type : " + promo.getPropertyValue(CricketCommonConstants.ITEM_DISC_TYPE));
							logDebug("Prmotion Type :  "+promo.getItemDisplayName());
						}
						 String promoType = (String) promo.getPropertyValue(CricketCommonConstants.ITEM_DISC_TYPE);	
						 //	String type = (String)promo.getPropertyValue("type");
						 if(promoType != null && (promoType.equalsIgnoreCase(CricketCommonConstants.WEB_INSTANT_DISC) || promoType.equalsIgnoreCase(CricketCommonConstants.INSTANT_DISC))){
							 String discType = null;
							 double discAmt=0.0;
							 String template = (String) promo.getPropertyValue(CricketCommonConstants.TEMPLATE);
							 if(null != template && !template.equalsIgnoreCase(CricketCommonConstants.ADV_DISCT_PMDT)){
								 Map tempValues = (Map) promo.getPropertyValue(CricketCommonConstants.TEMPLATE_VALUES);							 
								 discType = (String) tempValues.get(CricketCommonConstants.DISCOUNT_TYPE_VALUE);
								 discAmt = Double.parseDouble((String) tempValues.get(CricketCommonConstants.DISCOUNT_VALUE));
							 }
							 else if(null != template && template.equalsIgnoreCase(CricketCommonConstants.ADV_DISCT_PMDT)){
								 String templateValue = (String)((Map) promo.getPropertyValue(CricketCommonConstants.TEMPLATE_VALUES)).get(CricketCommonConstants.DISC_STRUCT);
								 templateValue = templateValue.substring(1, templateValue.length() - 1);
								 String[] templateArray = templateValue.split("\\s+");
								 Pattern pattern = null;
								 Matcher matcher = null;
								 for(String discStructure : templateArray){
									 if(discStructure.contains(CricketCommonConstants.DISCOUNT_TYPE)){
										 pattern = Pattern.compile("\"(.*?)\"");
										 matcher = pattern.matcher(discStructure);
										 if (matcher.find()){
										     discType=matcher.group(1);
										 }
									 }
									 else if(discStructure.contains(CricketCommonConstants.ADJUSTER)){
										 pattern = Pattern.compile("\"(.*?)\"");
										 matcher = pattern.matcher(discStructure);
										 if (matcher.find()== true){
										     discAmt=Double.parseDouble(matcher.group(1));
										 }
									 }
								 }							 
							 }
							 if(discType.equals(CricketCommonConstants.PERCENT_OFF)){
								 discAmt = (listPrice * (discAmt / 100));					 
							 }
							 finalPrice = finalPrice - discAmt;
						 }
					 }
					//Calculating percentage off based on retail price (listPrice) and final price
					 if(finalPrice != listPrice){
						 int discountPercentage = (int) (((listPrice - finalPrice) /listPrice) * 100);					 
						 phoneVO.setDiscountAmt(discountPercentage);
					 }
				}
			}
			
			if ((finalPrice >= 0)) {
				phoneVO.setFinalPrice(finalPrice);			
			}
			
			//List<RepositoryItem> childSkus = (List<RepositoryItem>) childProduct.getPropertyValue("childSkus");
			if(childSkus != null && childSkus.size() > 0) {
				phoneVO.setDefaultSkuId((String)(childSkus.get(0).getPropertyValue(CricketCommonConstants.REPOSITORY_ID)));
				if (isLoggingDebug()) {
					logDebug("Ending Method: populatePhoneListingProduct() ");
				}
			}
		} catch(Exception e){
			if(isLoggingError())
				logError("Error while fetching Phone Vo : " + e);
		}
		RepositoryItem manufacturer = (RepositoryItem)childProduct.getPropertyValue(CricketCommonConstants.MANUFACTURER);
		if (manufacturer != null) {
			String manufacturerName = (String) manufacturer.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME);
			if(manufacturerName != null) {
				phoneVO.setManufacturer(manufacturerName);
			}
		}
		List<RepositoryItem> parentCategories = (List<RepositoryItem>)childProduct.getPropertyValue(CricketCommonConstants.ANCESTOR_CATEGORIES);
		if(parentCategories != null) {
			String seoString = null;
			for (RepositoryItem parentCategory : parentCategories) {
				seoString = (String)parentCategory.getPropertyValue(CricketCommonConstants.SEO_STRING);
				if(seoString != null && !seoString.isEmpty()) {
					phoneVO.setSeoString(seoString);
				}
			}
		}
		return phoneVO;
	}
	
	/**
	 * 
	 * @param inquireFeaturesRequestVO
	 * @return
	 */
	public List<AddonVO> getAddOns(List<InquireFeaturesResponseVO> optionalAddOns, List<InquireFeaturesResponseVO> allIncludedAddOns) {
		
		List<AddonVO> planOptionalAddOns = new ArrayList<AddonVO>();
		List<AddonVO> planAllIncludedAddOns = new ArrayList<AddonVO>();
		List<AddonVO> planAddOns = new ArrayList<AddonVO>();
		AddonVO addonVO;		
		RepositoryItem [] addOnProducts= null;
		RepositoryItem addOnProduct = null;
		
		try{
			  RepositoryView view = getCatalogRepository().getView(CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT);
				
				RqlStatement statement = RqlStatement.parseRqlStatement(CricketCommonConstants.OFFER_ID_QUERY);
			  for(InquireFeaturesResponseVO optionalAddOn : optionalAddOns) {

	    		  try {
	  				Object params[] = new Object[1];
	  				params[0] = optionalAddOn.getId();
	  				addOnProducts =statement.executeQuery (view, params);
	    		    if(addOnProducts !=null && addOnProducts.length > 0) {
	    		    	addonVO = new AddonVO();
	    		    	addOnProduct = addOnProducts[0];
	    		    	addonVO = populateAddonVODynamic(addOnProduct, false);
	    		    	planOptionalAddOns.add(addonVO);
	    		    }
	    		  }
	    		  catch(RepositoryException exc) {
	    			  vlogError(exc.toString());
	    		  }
	    	}
			  for(InquireFeaturesResponseVO allIncludedAddOn : allIncludedAddOns) {

	    		  try {
	  				Object params[] = new Object[1];
	  				params[0] = allIncludedAddOn.getId();
	  				addOnProducts =statement.executeQuery (view, params);
	    		    if(addOnProducts !=null && addOnProducts.length > 0) {
	    		    	addonVO = new AddonVO();
	    		    	addOnProduct = addOnProducts[0];
	    		    	addonVO = populateAddonVODynamic(addOnProduct, true);
	    		    	planAllIncludedAddOns.add(addonVO);
	    		    }
	    		  }
	    		  catch(RepositoryException exc) {
	    			  vlogError(exc.toString());
	    		  }
	    	}
			 planAddOns.addAll(planAllIncludedAddOns);
			 planAddOns.addAll(planOptionalAddOns);
		} catch (Exception e) {
			vlogError("An Exception Occurred", e);
		}	
		return 	planAddOns;			
	}
	
	public String getImagePropertyName() {
		return imagePropertyName;
	}

	public void setImagePropertyName(final String imagePropertyName) {
		this.imagePropertyName = imagePropertyName;
	}

	/**
	 * @return the cktPromotionManager
	 */
	public CricketPromotionManager getCktPromotionManager() {
		return CktPromotionManager;
	}

	/**
	 * @param cktPromotionManager the cktPromotionManager to set
	 */
	public void setCktPromotionManager(CricketPromotionManager cktPromotionManager) {
		CktPromotionManager = cktPromotionManager;
	}

	public String getDefaultDataLimit() {
		return defaultDataLimit;
	}

	public void setDefaultDataLimit(String defaultDataLimit) {
		this.defaultDataLimit = defaultDataLimit;
	}

	public String getDefaultDataMessage() {
		return defaultDataMessage;
	}

	public void setDefaultDataMessage(String defaultDataMessage) {
		this.defaultDataMessage = defaultDataMessage;
	}

	/**
	 * @return the sessionBeanPath
	 */
	public String getCktProfilePath() {
		return mCktProfilePath;
	}

	/**
	 * @param pSessionBeanPath the sessionBeanPath to set
	 */
	public void setCktProfilePath(String pCktProfilePath) {
		mCktProfilePath = pCktProfilePath;
		if (mCktProfilePath != null) {
			mCktProfileComponentName = ComponentName.getComponentName(mCktProfilePath);
		} else {
			mCktProfileComponentName = null;
		}
	}
	
	 /**
	 * @param planCode
	 * @return
	 */
	public String getParcPlanId(final String planCode){
			String parcPlanId  = null;
			
			RepositoryItem planProductItem;
			try {
				planProductItem = getCatalogRepository().getItem(planCode, CricketCommonConstants.PLAN_PRODUCT);			
				if (planProductItem != null) {
					 parcPlanId = (String)planProductItem.getPropertyValue(CricketCommonConstants.PROP_PARC_PLAN_ID);				
				}
			} catch (RepositoryException re) {					
				logError("Error while getting the parcPlanId for the plan id - "+planCode + ":", re);
			}		
			return parcPlanId;
	}
	 /**
	 * @param productId
	 * @return
	 */
	public String getAddonOfferId(final String productId){
			String offerId  = null;
			
			RepositoryItem planProductItem;
			try {
				planProductItem = getCatalogRepository().getItem(productId, CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT);			
				if (planProductItem != null) {
					offerId = (String)planProductItem.getPropertyValue(CricketCommonConstants.PROP_OFFER_ID);				
				}
			} catch (RepositoryException re) {					
				logError("Error while getting the offerId for the addon Product - "+productId + ":", re);
			}		
			return offerId;
	}

	/**
	 * Returns the plans end date
	 * 
	 * @return the end date is passed or not
	 */
	public String CheckPlanEndDate(String id) {
		
		String planDateEnded= CricketCommonConstants.NO ;
		RepositoryView catalogRepoView;
		try {
			catalogRepoView = getCatalogRepository().getView(CricketCommonConstants.PLAN_PRODUCT);
			Object[] objects = new Object[1];
			objects[0] = id;
			RqlStatement statement = RqlStatement.parseRqlStatement(PLAN_ID_QUERY);
			RepositoryItem[] planSKUs = statement.executeQuery(catalogRepoView, objects);
			if(planSKUs != null && planSKUs.length > 0){
				for(RepositoryItem planSKU : planSKUs){
					Timestamp endDate = (Timestamp) planSKU.getPropertyValue(CricketCommonConstants.PROP_END_DATE);
					Date date= new Date();
					Date currentDate = new Timestamp(date.getTime());
					if(endDate != null && endDate.compareTo(currentDate) < 0) {
						planDateEnded = CricketCommonConstants.YES;
					}
				}						
			}
		} catch (RepositoryException e) {
			vlogError("Repository Exception while getting plan for id " + id, e);
		}
		return planDateEnded;
		
	}
	
}
