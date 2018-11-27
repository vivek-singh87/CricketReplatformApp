package com.cricket.browse.sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cricket.browse.AccessoryVO;
import com.cricket.browse.PhoneVO;
import com.cricket.browse.PlanVO;
import com.cricket.browse.ProductVO;
import com.cricket.common.constants.CricketCommonConstants;
import com.endeca.infront.cartridge.model.Attribute;
import com.endeca.infront.cartridge.model.Record;

import atg.commerce.pricing.CricketPromotionManager;
import atg.commerce.pricing.PricingModelHolder;
import atg.nucleus.GenericService;
import atg.projects.store.sort.ProductPriceWrapper;
import atg.projects.store.sort.PropertySorter;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.service.util.SortArray;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.ServletUtil;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;


public class PriceSorter extends GenericService implements PropertySorter {
	
	private String priceProperty;
	private Repository productCatalog;
	private CricketPromotionManager cricketPromotionManager;
	private ProfileServices profileServices;

	@Override
	public Object[] sort(List pItems, String pDirection, Locale pLocale) {
		if(pItems == null || pItems.size() == 0){
		      return new Object[0];
		}
		ArrayList productPriceList = new ArrayList();
		for (Object productVOObj : pItems) {
			double thisItemPrice = 0.0;
			thisItemPrice = getListPrice(productVOObj);
			if (productVOObj instanceof PhoneVO) {
				PhoneVO phoneVO = (PhoneVO)productVOObj;
				CricketPriceWrapper currentNode = null;
				currentNode = new CricketPriceWrapper(phoneVO, thisItemPrice);
				productPriceList.add(currentNode);
			} else if (productVOObj instanceof AccessoryVO) {
				AccessoryVO accessoryVO = (AccessoryVO)productVOObj;
				CricketPriceWrapper currentNode = null;
				currentNode = new CricketPriceWrapper(accessoryVO, thisItemPrice);
				productPriceList.add(currentNode);
			} else if (productVOObj instanceof PlanVO) {
				PlanVO planVO = (PlanVO) productVOObj;
				CricketPriceWrapper currentNode = null;
				currentNode = new CricketPriceWrapper(planVO, thisItemPrice);
				productPriceList.add(currentNode);
			} else if (productVOObj instanceof Record) {
				Record record = (Record) productVOObj;
				CricketPriceWrapper currentNode = null;
				currentNode = new CricketPriceWrapper(record, thisItemPrice);
				productPriceList.add(currentNode);
			}
			
		}
		String sortDirection = DESCENDING_SYMBOL;
		if(pDirection != null && pDirection.equals(ASCENDING)) {
			sortDirection = ASCENDING_SYMBOL;
		}
		if(pLocale == null) {
		      pLocale = getNucleus().getDefaultLocale();
		}
		/*
	     * Sorting
	     */
		SortArray sortArray = new SortArray();      
	    sortArray.setSortDirections(sortDirection);
	    sortArray.setSortProperties(new String[] {getPriceProperty()});
	    sortArray.setLocale(pLocale);
	    sortArray.setInputArray(productPriceList.toArray());
	    
	    Object[] sortedArray = sortArray.getOutputArray();
	    if(sortedArray == null){
	    	return pItems.toArray();
	    }
	    else {
	    	for(int i = 0; i < sortedArray.length; i++){
	    		sortedArray[i] = ((CricketPriceWrapper)sortedArray[i]).getProductVO();
	    	}
	    }
	    return sortedArray;
	}

	private double getListPrice(Object productObj) {
		double skuListPrice = 0.0;
		RepositoryItem product = null;
		RepositoryItem sku = null;
		try {
			if(productObj instanceof ProductVO) {
				ProductVO productVO = (ProductVO)productObj;
				String productId = productVO.getProductId();
				product = productCatalog.getItem(productId, CricketCommonConstants.PRODUCT);
				List<RepositoryItem> childSkus = (List<RepositoryItem>)product.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
				if(childSkus != null && childSkus.size() > 0) {
					sku = childSkus.get(0);
				}
			} else if (productObj instanceof Record) {
				Record productRecord = (Record)productObj;
				Map<String, Attribute> recordProps = productRecord.getAttributes();
				Attribute productIdAttr = recordProps.get(CricketCommonConstants.PRODUCT_REPOSITORY_ID);
				if (productIdAttr != null) {
					String productId = (String)productIdAttr.get(0);
					product = productCatalog.getItem(productId, CricketCommonConstants.PRODUCT);
					List<RepositoryItem> childSkus = (List<RepositoryItem>)product.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
					if(childSkus != null && childSkus.size() > 0) {
						sku = childSkus.get(0);
					}
				}
			}
		} catch (RepositoryException e) {
			logError(e);
		}
		skuListPrice = getDiscountedPrice(product, sku);
		return skuListPrice;
	}

	private double getDiscountedPrice(RepositoryItem product, RepositoryItem sku) {
		if(product != null && sku != null) {
			DynamoHttpServletRequest request = ServletUtil.getCurrentRequest();
			PricingModelHolder userPricingHolder = (PricingModelHolder)request.resolveName("/atg/commerce/pricing/UserPricingModels");
			List<RepositoryItem> promotions = getCricketPromotionManager().getPromotions(product, sku, userPricingHolder);
			Profile profile = getProfileServices().getCurrentProfile();
			Double retailPrice = (Double)sku.getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
			Double totalDiscount = 0.0;
			if(promotions != null) {
				for (RepositoryItem promotion : promotions) {			 
					 if (isLoggingDebug()) {
						logDebug("Promotion Id : " + promotion.getRepositoryId() + "promotion Type : " + promotion.getPropertyValue(CricketCommonConstants.ITEM_DISC_TYPE));
						logDebug("Prmotion Type :  "+promotion.getItemDisplayName());
					}
					 String promoType = (String) promotion.getPropertyValue(CricketCommonConstants.ITEM_DISC_TYPE);
					 String template = (String) promotion.getPropertyValue(CricketCommonConstants.TEMPLATE);
					 String discType = null;
					 double discAmt=0.0;
					 Pattern pattern = null;
					 Matcher matcher = null;
					
					 if(null != template && template.equalsIgnoreCase("/advanced/advancedItemDiscount.pmdt")){
						 String qualifierValue = (String)((Map) promotion.getPropertyValue(CricketCommonConstants.TEMPLATE_VALUES)).get(CricketCommonConstants.QUALIFIER);
						 String marketType=null;
						 String marketId=null;
						 pattern = Pattern.compile("(?<=value>).*.(?=</value)");
						 matcher = pattern.matcher(qualifierValue);
				         if(matcher.find()) {
				             if((matcher.group().toString()).contains(CricketCommonConstants.MARKET_TYPE)){
				            	 pattern = Pattern.compile("(?<=string-value>).*.(?=</string-value)");
								 matcher = pattern.matcher(qualifierValue);
								 if(matcher.find()) {
									 marketType=matcher.group().toString();
								 }
				             }
				             else if((matcher.group().toString()).contains(CricketCommonConstants.PROP_MARKET_ID)){
				            	 pattern = Pattern.compile("(?<=string-value>).*.(?=</string-value)");
								 matcher = pattern.matcher(qualifierValue);
								 if(matcher.find()) {
									 marketId=matcher.group().toString();
								 }
				             }
				         }
				         //Check if marketType matches with current profile marketType
				         if(null!=marketType || null!=marketId) {
				        	 String marketCode = null;
				        	 if(null!=marketType){
				        		 marketCode=(String)profile.getPropertyValue(CricketCommonConstants.MARKET_TYPE);
				        	 }
				        	 else if(null!=marketId){
				        		 marketCode=(String)profile.getPropertyValue(CricketCommonConstants.PROP_MARKET_ID);
				        	 }
				        	 if(null!=marketCode && marketCode.equals(marketType)){
				        		 String templateValue = (String)((Map) promotion.getPropertyValue(CricketCommonConstants.TEMPLATE_VALUES)).get("discountStructure");					 
								 templateValue = templateValue.substring(1, templateValue.length() - 1);
								 String[] templateArray = templateValue.split("\\s+");
								
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
				         }
					 }
					 else if(null != template){
						 Map tempValues = (Map) promotion.getPropertyValue(CricketCommonConstants.TEMPLATE_VALUES);
						 discType = (String) tempValues.get(CricketCommonConstants.DISCOUNT_TYPE_VALUE);
						 discAmt = Double.parseDouble((String) tempValues.get(CricketCommonConstants.DISCOUNT_VALUE));
					 }
					 if(null!=discType && discType.equals(CricketCommonConstants.PERCENT_OFF)){
						 discAmt = (retailPrice * (discAmt / 100));					 
					 }
					 
					 if(promoType != null && !(promoType.equalsIgnoreCase(CricketCommonConstants.MAIL_IN_REBATE)) && discAmt >0.0){
						 totalDiscount = totalDiscount + discAmt;			 
					 }else if(promoType != null && (promoType.equalsIgnoreCase(CricketCommonConstants.MAIL_IN_REBATE))){
					 }else if(discAmt > 0.0){
						 totalDiscount = totalDiscount + discAmt;
					 }
				 }
			}
			retailPrice = retailPrice - totalDiscount;
			return retailPrice;
		} else {
			return 0.0;
		}
	}

	public String getPriceProperty() {
		return priceProperty;
	}

	public void setPriceProperty(String priceProperty) {
		this.priceProperty = priceProperty;
	}

	public Repository getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(Repository productCatalog) {
		this.productCatalog = productCatalog;
	}

	public CricketPromotionManager getCricketPromotionManager() {
		return cricketPromotionManager;
	}

	public void setCricketPromotionManager(CricketPromotionManager cricketPromotionManager) {
		this.cricketPromotionManager = cricketPromotionManager;
	}

	public ProfileServices getProfileServices() {
		return profileServices;
	}

	public void setProfileServices(ProfileServices profileServices) {
		this.profileServices = profileServices;
	}

	

}
