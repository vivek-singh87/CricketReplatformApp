package com.cricket.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import atg.commerce.pricing.priceLists.PriceListManager;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.configuration.CricketConfiguration;
import com.cricket.search.records.CatLinkVO;
import com.cricket.search.records.RecordVO;
import com.cricket.search.session.UserSearchSessionInfo;
import com.endeca.infront.cartridge.model.Attribute;
import com.endeca.infront.cartridge.model.Record;
import com.endeca.infront.cartridge.model.Refinement;

public class SegregateSearchResultsBasedOnCategory extends DynamoServlet {
	
	private String typeDistinguishProperty;
	
	private String imageProperty;
	
	private String activePriceProperty = "sku.activePrice";
	
	private UserSearchSessionInfo searchSessionInfo;
	
	private Repository productCatalog;
	
	private ProfileServices profileService;
	
	private PriceListManager priceListManager;
	
	private CricketConfiguration cricketConfiguration;
	
	private static final String PHONE_PRODUCT = "phone-product";
	
	private static final String PLAN_PRODUCT = "plan-product";
	
	private static final String ACCESSORY_PRODUCT = "accessory-product";
	
	private static final String ADDON_PRODUCT = "addOn-product";
	
	private static final String PHONES = "Phones";
	private static final String PLANS = "Plans";
	private static final String ACCESSORIES = "Accessories";
	private static final String ADDONS = "AddOns";
	
	private static final String CATEGORY_REFINEMENTS = "categoryTypeRefinements";
	private static final String RECORDS = "records";
	private static final String TOTAL_RECORDS = "totalNumRecs";
	private static final String RECORDS_PER_PAGE = "recordsPerPage";
	private static final String PRODUCT_DISPLAY_NAME = "product.displayName";
	private static final String SKU_REPOSITORY_ID = "sku.repositoryId";
	private static final String FULL_IMAGE = "fullImage";
	private static final String BRIEF_DESCRIPTION = "briefDescription";
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		
		if(isLoggingDebug()) {
			logDebug("entering service method of SegregateSearchResultsBasedOnCategory");
		}
		Map<String, List<RecordVO>> segregatedMap = new HashMap<String, List<RecordVO>>();
		List<Refinement> catTypeRefinements = (List<Refinement>)pReq.getObjectParameter(CATEGORY_REFINEMENTS);
		List<Record> records = (List<Record>)pReq.getObjectParameter(RECORDS);
		String searchQuery = pReq.getParameter("searchQuery");
		String allResultsQuery = searchSessionInfo.getGenTextBoxSearchQuery() + "&Ntt=" + searchQuery;
		Long totalCount = (Long)pReq.getObjectParameter(TOTAL_RECORDS);
		Long recordsPerPageLong = (Long)pReq.getObjectParameter(RECORDS_PER_PAGE);
		int recordsPerPage = 0;
		if(recordsPerPageLong != null) {
			recordsPerPage = recordsPerPageLong.intValue();
		}
		for (Record record : records) {
			RecordVO recordVO = new RecordVO();
			Map<String, Attribute> recordProps = record.getAttributes();
			Attribute productIdAttr = recordProps.get(CricketCommonConstants.PRODUCT_REPOSITORY_ID);
			Attribute displayNameAttr = recordProps.get(PRODUCT_DISPLAY_NAME);
			Attribute skuIdAttr = recordProps.get(SKU_REPOSITORY_ID);
			Attribute imageUrlAttr = recordProps.get(imageProperty);
			Attribute productTypeAttr = recordProps.get(typeDistinguishProperty);
			Attribute finalPriceAttr = recordProps.get(activePriceProperty);
			if (productIdAttr != null) {
				String productId = (String)productIdAttr.get(0);
				recordVO.setProductId(productId);
			}
			if (displayNameAttr != null) {
				String displayName = (String)displayNameAttr.get(0);
				recordVO.setDisplayName(displayName);
			}
			if (skuIdAttr != null) {
				String skuId = (String)skuIdAttr.get(0);
				recordVO.setSkuId(skuId);
			}
			if (imageUrlAttr != null) {
				String imageUrl = (String)imageUrlAttr.get(0);
				recordVO.setImageUrl(imageUrl);
			}
			if (finalPriceAttr != null) {
				Double finalPrice = (Double)finalPriceAttr.get(0);
				recordVO.setFinalPrice(finalPrice);
			}
			if (productTypeAttr != null) {
				String commonId = (String)productTypeAttr.get(0);
				String productType = CricketCommonConstants.EMPTY_STRING;
				if (commonId.contains(PHONE_PRODUCT)) {
					productType = PHONES;
				}
				if (commonId.contains(PLAN_PRODUCT)) {
					productType = PLANS;
				}
				if (commonId.contains(ACCESSORY_PRODUCT)) {
					productType = ACCESSORIES;
				}
				if (commonId.contains(ADDON_PRODUCT)) {
					productType = ADDONS;
				}
				if(isLoggingDebug()) {
					logDebug("recordVO before ensuring :: " + recordVO);
				}
				recordVO = ensureRequiredData(recordVO);
				if(isLoggingDebug()) {
					logDebug("recordVO after ensuring :: " + recordVO);
				}
				if (segregatedMap.containsKey(productType)) {
					List<RecordVO> recordVosTemp = segregatedMap.get(productType);
					recordVosTemp.add(recordVO);
					segregatedMap.put(productType, recordVosTemp);
				} else {
					List<RecordVO> recordVosTemp2 = new ArrayList<RecordVO>();
					recordVosTemp2.add(recordVO);
					segregatedMap.put(productType, recordVosTemp2);
				}
			}
		}
		
		populateUserSearchSessionInfo(catTypeRefinements, searchQuery, totalCount.intValue(), allResultsQuery, recordsPerPage);
		
		if(isLoggingDebug()) {
			logDebug("segregated map prepared :: " + segregatedMap);
		}
		pReq.setParameter("segregatedMap", segregatedMap);
		try {
			pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
		} catch (ServletException e) {
			if(isLoggingError()) {
				logError("error occured while trying to service the content"+e);
			}
		} catch (IOException e) {
			if(isLoggingError()) {
				logError("error occured while trying to service the content"+e);
			}
		}
		if(isLoggingDebug()) {
			logDebug("exiting service method of SegregateSearchResultsBasedOnCategory");
		}
	}

	@SuppressWarnings("unchecked")
	private RecordVO ensureRequiredData(RecordVO recordVO) {
		if(isLoggingDebug()) {
			logDebug("entering ensureRequiredData");
		}
		String productId = recordVO.getProductId();
		Profile profile = profileService.getCurrentProfile();
		RepositoryItem listPriceItem = null;
		double listPrice = 0.0;
		if (profile != null) {
			listPriceItem  = (RepositoryItem) profile.getPropertyValue(CricketCommonConstants.PROP_PRICE_LIST_ITEM);
		}
		if (recordVO  != null) {
			try {
				RepositoryItem repositoryItem = (RepositoryItem)getProductCatalog().getItem(productId, CricketCommonConstants.PRODUCT);
				if(repositoryItem != null) {
					List<RepositoryItem> parentCategories = (List<RepositoryItem>)repositoryItem.getPropertyValue(CricketCommonConstants.ANCESTOR_CATEGORIES);
					if(parentCategories != null) {
						for (RepositoryItem parentCategory : parentCategories) {
							String seoString = (String)parentCategory.getPropertyValue(CricketCommonConstants.SEO_STRING);
							if(seoString != null && !seoString.isEmpty()) {
								recordVO.setSeoString(seoString);
							}
						}
					}
					String productType = (String)repositoryItem.getPropertyValue(CricketCommonConstants.TYPE);
					if(recordVO.getImageUrl() == null || recordVO.getImageUrl().isEmpty()) {
						RepositoryItem fullImage = (RepositoryItem)repositoryItem.getPropertyValue(FULL_IMAGE);
						if(fullImage != null) {
							String imageUrl = (String)fullImage.getPropertyValue(CricketCommonConstants.URL);
							recordVO.setImageUrl(imageUrl);
						}
					}
					if(recordVO.getFinalPrice() == null) {
						List<RepositoryItem> childSkus = (List<RepositoryItem>)repositoryItem.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
						if(childSkus != null && childSkus.size() > 0) {
							Double skuPrice = (Double)childSkus.get(0).getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
							recordVO.setFinalPrice(skuPrice);
						}
					}
					if(productType != null && productType.equals(CricketCommonConstants.PLAN_PRODUCT) && recordVO.getRatePlanType() == null) {
						String ratePlanType = (String)repositoryItem.getPropertyValue(CricketCommonConstants.PROP_RATE_PLAN_TYPE);
						if(ratePlanType == null) {
							ratePlanType = "Feature Plan";
						}
						if(ratePlanType.equals(CricketCommonConstants.CHAR_V)) {
							ratePlanType = "Feature Plan";
						}
						if(ratePlanType.equals(CricketCommonConstants.CHAR_D)) {
							ratePlanType = "Smart Plan";
						}
						recordVO.setRatePlanType(ratePlanType);
					}
					if(productType != null && productType.equals(CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT) && recordVO.getParentCategoryName() == null) {
						String parentCategoryName = (String)repositoryItem.getPropertyValue(BRIEF_DESCRIPTION);
						if (parentCategoryName == null) {
							parentCategoryName = "Others";
							List<RepositoryItem> ancestorCategories = (List<RepositoryItem>)repositoryItem.getPropertyValue(CricketCommonConstants.ANCESTOR_CATEGORIES);
							List<String> addonRelatedCategories = getCricketConfiguration().getAddonRelatedCategories();
							boolean catNameFound = false;
							for (String addOnRelCatId : addonRelatedCategories) {
								for (RepositoryItem ancestorCategory : ancestorCategories) {
									String categoryId = (String)ancestorCategory.getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
									if(addonRelatedCategories.contains(categoryId)) {
										parentCategoryName = (String)ancestorCategory.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME);
										catNameFound = true;
										break;
									}
								}
								if(catNameFound) {
									break;
								}
							}
						}
						if(parentCategoryName != null){
							recordVO.setParentCategoryName(parentCategoryName);
						}
					}
				}
			} catch (RepositoryException e) {
				if (isLoggingError()) {
					logError("an exception accured while trying to get repository from repository : " + getProductCatalog() + " :: " + productId);
					logError(e);
				}
			}
			
		}
		if(isLoggingDebug()) {
			logDebug("exiting ensureRequiredData");
		}
		return recordVO;
	}

	private void populateUserSearchSessionInfo(List<Refinement> catTypeRefinements, String searchQuery, int totalCount, String allResultsQuery, int recordsPerPage) {
		if(isLoggingDebug()) {
			logDebug("entering populateUserSearchSessionInfo");
		}
		String queryTemp = CricketCommonConstants.EMPTY_STRING;
		if(searchSessionInfo != null) {
			queryTemp = searchSessionInfo.getSearchQuery();
		}
		if (queryTemp == null) {
			queryTemp = CricketCommonConstants.EMPTY_STRING;
		}
		if(searchQuery != null && !queryTemp.equals(searchQuery)) {
			searchSessionInfo.setSearchQuery(searchQuery);
			searchSessionInfo.setTotalCount(totalCount);
			searchSessionInfo.setItemsPerPage(recordsPerPage);
			Map<String, CatLinkVO> catLinkMap = new HashMap<String, CatLinkVO>();
			if(catTypeRefinements != null) {
				for (Refinement refinement : catTypeRefinements) {
					CatLinkVO catlinkVO = new CatLinkVO();
					String categoryNavLink = refinement.getNavigationState();
					if(categoryNavLink != null && !categoryNavLink.isEmpty()) {
						categoryNavLink = categoryNavLink.replace("&Nrpp=10000", CricketCommonConstants.EMPTY_STRING);
					}
					int resultCount = refinement.getCount();
					String label = refinement.getLabel();
					String catKey = CricketCommonConstants.EMPTY_STRING;
					catlinkVO.setCategoryCount(resultCount);
					catlinkVO.setCategoryNavLink(categoryNavLink + "&searchPage=searchPage");
					if (label != null && label.equals(CricketCommonConstants.PHONE_PRODUCT)) {
						catKey = PHONES;
					}
					if (label != null && label.equals(CricketCommonConstants.PLAN_PRODUCT)) {
						catKey = PLANS;
					}
					if (label != null && label.equals(CricketCommonConstants.ACCESSORY_PRODUCT)) {
						catKey = ACCESSORIES;
					}
					if (label != null && label.equals(CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT)) {
						catKey = ADDONS;
					}
					if (!catKey.isEmpty()) {
						catLinkMap.put(catKey, catlinkVO);
					}
				}
			}
			CatLinkVO catlinkVO2 = new CatLinkVO();
			catlinkVO2.setCategoryCount(totalCount);
			catlinkVO2.setCategoryNavLink(allResultsQuery);
			catLinkMap.put("All-Results", catlinkVO2);
			searchSessionInfo.setCatLinkMap(catLinkMap);
		}
		if(isLoggingDebug()) {
			logDebug("exiting populateUserSearchSessionInfo");
		}
		
	}

	public String getTypeDistinguishProperty() {
		return typeDistinguishProperty;
	}

	public void setTypeDistinguishProperty(String typeDistinguishProperty) {
		this.typeDistinguishProperty = typeDistinguishProperty;
	}

	public String getImageProperty() {
		return imageProperty;
	}

	public void setImageProperty(String imageProperty) {
		this.imageProperty = imageProperty;
	}

	public UserSearchSessionInfo getSearchSessionInfo() {
		return searchSessionInfo;
	}

	public void setSearchSessionInfo(UserSearchSessionInfo searchSessionInfo) {
		this.searchSessionInfo = searchSessionInfo;
	}

	public String getActivePriceProperty() {
		return activePriceProperty;
	}

	public void setActivePriceProperty(String activePriceProperty) {
		this.activePriceProperty = activePriceProperty;
	}

	public Repository getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(Repository productCatalog) {
		this.productCatalog = productCatalog;
	}

	public ProfileServices getProfileService() {
		return profileService;
	}

	public void setProfileService(ProfileServices profileService) {
		this.profileService = profileService;
	}

	public PriceListManager getPriceListManager() {
		return priceListManager;
	}

	public void setPriceListManager(PriceListManager priceListManager) {
		this.priceListManager = priceListManager;
	}

	public CricketConfiguration getCricketConfiguration() {
		return cricketConfiguration;
	}

	public void setCricketConfiguration(CricketConfiguration cricketConfiguration) {
		this.cricketConfiguration = cricketConfiguration;
	}

}
