package com.cricket.commerce.endeca.index.accessor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cricket.common.constants.CricketCommonConstants;

import atg.commerce.pricing.CricketPromotionManager;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.repository.search.indexing.Context;
import atg.repository.search.indexing.PropertyAccessorImpl;
import atg.repository.search.indexing.specifier.PropertyTypeEnum;

public class AppliedPromotionPriceAccessor extends PropertyAccessorImpl {
	
	CricketPromotionManager cricketPromotionManager;
	Repository productCatalog;
	String applicablePromoQuery;
	
	protected Object getTextOrMetaPropertyValue(Context pContext, RepositoryItem pItem, String pPropertyName, PropertyTypeEnum pType) {
		String marketType = (String)pContext.getAttribute("atg.AppliedPromotionPriceVariantProducer.marketType");
		Double price = preparePriceVariant(marketType, pItem);
		return price;
	}
	
	private Double preparePriceVariant(String marketTypeProfile, RepositoryItem product) {
		Double finalPrice = 0.0;
		Double listPrice = 0.0;
		Double totalDiscount = 0.0;
		String skuId = CricketCommonConstants.EMPTY_STRING;
		String productId = (String)product.getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
		List<RepositoryItem> skus = (List<RepositoryItem>)product.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
		RepositoryItem sku = null;
		if(skus != null && skus.size() > 0) {
			sku = skus.get(0);
			skuId = (String)sku.getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
			listPrice = (Double)sku.getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
			if(listPrice == null) {
				listPrice = 0.0;
			}
			finalPrice = listPrice;
		}
		if(sku != null) {
			List<RepositoryItem> promotions = getApplicablePromotions(productId, skuId);
			if(promotions != null) {
				for (RepositoryItem promotion : promotions) {
					String promoType = (String) promotion.getPropertyValue(CricketCommonConstants.ITEM_DISC_TYPE);
					String template = (String) promotion.getPropertyValue(CricketCommonConstants.TEMPLATE);
					Pattern pattern = null;
					Matcher matcher = null;
					if(null != template && template.equalsIgnoreCase("/advanced/advancedItemDiscount.pmdt")) {
						String qualifierValue = (String)((Map) promotion.getPropertyValue(CricketCommonConstants.TEMPLATE_VALUES)).get(CricketCommonConstants.QUALIFIER);
						String marketTypePromo = null;
						String marketId=null;
						pattern = Pattern.compile("(?<=value>).*.(?=</value)");
						matcher = pattern.matcher(qualifierValue);
						if(matcher.find()) {
							if((matcher.group().toString()).contains(CricketCommonConstants.MARKET_TYPE)) {
								pattern = Pattern.compile("(?<=string-value>).*.(?=</string-value)");
								matcher = pattern.matcher(qualifierValue);
								if(matcher.find()) {
									marketTypePromo = matcher.group().toString();
								}
							}
						}
						if(marketTypeProfile != null && marketTypeProfile.equalsIgnoreCase(marketTypePromo)) {
							if(isLoggingDebug()) {
								logDebug("promtion:: " + promotion + "is a market level promotion for:: " + productId + " :: " + skuId);
							}
							String templateValue = (String)((Map) promotion.getPropertyValue(CricketCommonConstants.TEMPLATE_VALUES)).get("discountStructure");					 
							templateValue = templateValue.substring(1, templateValue.length() - 1);
							String[] templateArray = templateValue.split("\\s+");
							for(String discStructure : templateArray){
								if(discStructure.contains(CricketCommonConstants.ADJUSTER)) {
									pattern = Pattern.compile("\"(.*?)\"");
									matcher = pattern.matcher(discStructure);
									if (matcher.find() == true){
										totalDiscount = totalDiscount + Double.parseDouble(matcher.group(1));
									}
								}
							}
						}
					} else if(null != template){
						Map tempValues = (Map) promotion.getPropertyValue(CricketCommonConstants.TEMPLATE_VALUES);
						String discType = (String) tempValues.get(CricketCommonConstants.DISCOUNT_TYPE_VALUE);
						Double discAmt = Double.parseDouble((String) tempValues.get(CricketCommonConstants.DISCOUNT_VALUE));
						if(null != discType && discType.equals(CricketCommonConstants.PERCENT_OFF)){
							 discAmt = (listPrice * (discAmt / 100));
							 totalDiscount = totalDiscount + discAmt;
						} else {
							totalDiscount = totalDiscount + discAmt;
						}
					}
				}
			}
		}
		if(isLoggingDebug()) {
			logDebug("total discount calculated for:: " + productId + " :: " + skuId + "is:: " + totalDiscount + "for market:: " + marketTypeProfile);
			logDebug("actual price was:: " + listPrice);
		}
		finalPrice = listPrice - totalDiscount;
		return finalPrice;
	}

	private List<RepositoryItem> getApplicablePromotions(String productId, String skuId) {
		List<RepositoryItem> itemsList = null;
		RepositoryItem [] items = null;
		try {
			RepositoryView view = productCatalog.getView(CricketCommonConstants.PROMOTION);
			RqlStatement statement = RqlStatement.parseRqlStatement(getApplicablePromoQuery());
			Object params[] = new Object[2];
			params[0] = productId;
			params[1] = skuId;
			items = statement.executeQuery (view, params);
		} catch (RepositoryException e) {
			logError(e);
		}
		if(items != null) {
			itemsList = Arrays.asList(items);
		}
		if(isLoggingDebug()) {
			logDebug("promtions:: " + itemsList + "found applicable for products:: " + productId + " :: " + skuId);
		}
		return itemsList;
	}

	public CricketPromotionManager getCricketPromotionManager() {
		return cricketPromotionManager;
	}

	public void setCricketPromotionManager(
			CricketPromotionManager cricketPromotionManager) {
		this.cricketPromotionManager = cricketPromotionManager;
	}

	public Repository getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(Repository productCatalog) {
		this.productCatalog = productCatalog;
	}

	public String getApplicablePromoQuery() {
		return applicablePromoQuery;
	}

	public void setApplicablePromoQuery(String applicablePromoQuery) {
		this.applicablePromoQuery = applicablePromoQuery;
	}
	
}
