package com.cricket.inventory;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.common.constants.CricketCommonConstants;

import atg.commerce.order.CommerceItem;
import atg.commerce.order.CommerceItemNotFoundException;
import atg.commerce.order.InvalidParameterException;
import atg.nucleus.GenericService;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.QueryExpression;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;

public class InventoryUtils extends GenericService {
	
	private Map<Integer, String> defaultThresholds = null;
	
	List<String> defaultThresholdquantityList = null;
	
	private Repository inventoryRepository;
	
	private Repository productCatalog;
	
	public InventoryResponseVO checkStockLevel(String skuId) {
		long stockLevel = getStockLevelForSkuId(skuId);
		RepositoryItem sku = null;
		try {
			sku = productCatalog.getItem(skuId, CricketCommonConstants.ITEM_DESC_SKU);
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		InventoryResponseVO responseVO = getThresholdMessaging(sku, stockLevel);
		return responseVO;
	}
	
	public InventoryResponseVO checkStockLevelWithCurrentCart(String skuId, CricketOrderImpl order, boolean balanceStock) {
		long balanceStockVar = 0;
		if(balanceStock) {
			balanceStockVar = 1;
		}
		long stockLevel = getStockLevelForSkuId(skuId);
		List<CommerceItem> commerceItems = order.getCommerceItems();
		long effectiveStockLevel = stockLevel;
		String skuIdCommItem = null;
		RepositoryItem sku = null;
		String skuType = null;
		try {
			sku = productCatalog.getItem(skuId, CricketCommonConstants.ITEM_DESC_SKU);
			if(sku != null) {
				skuType = (String)sku.getPropertyValue(CricketCommonConstants.TYPE);
			}
			if(skuType != null && skuType.equalsIgnoreCase(CricketCommonConstants.ACCESSORY_SKU)) {
				for(CommerceItem commerceItem: commerceItems) {
					skuIdCommItem = commerceItem.getCatalogRefId();
					if(skuId != null && skuIdCommItem != null && skuIdCommItem.equalsIgnoreCase(skuId)) {
						long commItemCount = commerceItem.getQuantity();
						effectiveStockLevel = (stockLevel - commItemCount) + balanceStockVar;
						break;
					}
				}
			} else if(skuType != null && skuType.equalsIgnoreCase(CricketCommonConstants.PHONE_SKU)) {
				commerceItems = order.getCommerceItemsByCatalogRefId(skuId);
				if(commerceItems != null) {
					long samePhoneCount = commerceItems.size();
					effectiveStockLevel = (stockLevel - samePhoneCount) + balanceStockVar;
				}
			}
		} catch (RepositoryException e) {
			if(isLoggingError()) {
				logError("cant get item for skuId");
			}
			e.printStackTrace();
		} catch (CommerceItemNotFoundException e) {
			if(isLoggingError()) {
				//logError("commerce item with passed catalogRefId is not in container");
			}
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
		InventoryResponseVO responseVO = getThresholdMessaging(sku, effectiveStockLevel);
		return responseVO;
	}
	
	private InventoryResponseVO getThresholdMessaging(RepositoryItem sku, long stockLevel) {
		InventoryResponseVO responseVO = new InventoryResponseVO();
		int thresholdQty = 0;
        boolean isOutOfStock = false;
        int thresholdCount = 0;
        List<Integer> thresholdquantityList = new ArrayList<Integer>();
		Map<Integer, String> quantityMsgMap = new HashMap<Integer, String>();
		boolean isMsgSet = false;
		String massgr = null;
			if(sku != null) {
				List<RepositoryItem> thresholds = (List<RepositoryItem>)sku.getPropertyValue(CricketCommonConstants.THRESHOLDS);
				if(thresholds != null && thresholds.size() >= 3) {
					//Loop the threshold Reposiotry Items to get the thresholdquantity, thresholdtext and store it in quantityMessageMap. add quantity to thresholdquantitylist for sorting
					for(RepositoryItem threshold : thresholds) {
						if(threshold.getPropertyValue(CricketCommonConstants.THRESHOLDQUANTITY) != null){
							thresholdQty = ((Integer) threshold.getPropertyValue(CricketCommonConstants.THRESHOLDQUANTITY)).intValue();
						}
						String thresholdMsg = (String) threshold.getPropertyValue(CricketCommonConstants.THRESHOLDTEXT);
						if (isLoggingDebug()) {
							logDebug("thresholdMessage" + thresholdMsg);
							logDebug("thresholdQuantity" + thresholdQty);
						}
						quantityMsgMap.put(thresholdQty, thresholdMsg);
						thresholdquantityList.add(thresholdQty);					
					}
			
					if (thresholdquantityList != null) {
						//Sort the thresholdquantityList in ascending order
						Collections.sort(thresholdquantityList);
						//assign the shipping message to a quantity range present in the particular sku  
						for (Integer quantity : thresholdquantityList) {
                            thresholdCount++;
                            isOutOfStock = false;
							if (stockLevel <= quantity) {
								massgr = quantityMsgMap.get(quantity);
								//check the out of stock for a product
                                if (thresholdCount == 1) {
                                    isOutOfStock = true;
                                }
								isMsgSet = true;
								break;
							}
						}
						//Default message to be assigne for a stocklevel is greater then the quantity present in thresholds.
						if(!isMsgSet) {
							if(thresholdquantityList.size() != 0){	
								massgr = quantityMsgMap.get(thresholdquantityList.get(thresholdquantityList.size()-1));
							}
						}
					}
				} else {
					for (String quantityString : getDefaultThresholdquantityList()) {
						Integer quantity = Integer.parseInt(quantityString);
                        thresholdCount++;
                        isOutOfStock = false;
						if (stockLevel <= quantity) {
							massgr = defaultThresholds.get(quantityString);
							//check the out of stock for a product
                            if (thresholdCount == 1) {
                                isOutOfStock = true;
                            }
							isMsgSet = true;
							break;
						}
					}
					if(!isMsgSet) {
						if(thresholdquantityList.size() != 0){	
							massgr = quantityMsgMap.get(thresholdquantityList.get(thresholdquantityList.size()-1));
						}
					}
				}
			}
		responseVO.setOutOfStock(isOutOfStock);
		responseVO.setThresholdMessage(massgr);
		return responseVO;
	}

	private long getStockLevelForSkuId(String skuId) {
		long stockLevel = 0;
		try {
			RepositoryView inventoryView = getInventoryRepository().getView(CricketCommonConstants.ITEM_DESC_INV);
			QueryBuilder qBuilder = inventoryView.getQueryBuilder();
			QueryExpression zipCd = qBuilder.createPropertyQueryExpression(CricketCommonConstants.PROPERTY_SKU_ID);
			QueryExpression zipCdValue = qBuilder.createConstantQueryExpression(skuId);
			Query inventoryQuery = qBuilder.createComparisonQuery(zipCd, zipCdValue, QueryBuilder.EQUALS);
			RepositoryItem[] repsItem = inventoryView.executeQuery(inventoryQuery);
			if(repsItem != null && repsItem.length > 0) {
				stockLevel = (Long)repsItem[0].getPropertyValue(CricketCommonConstants.PROP_STK_LVL);
			}
		} catch (RepositoryException e) {
			if(isLoggingError()) {
				logError("cant get view for repository item");
			}
			e.printStackTrace();
		}
		return stockLevel;
	}
	
	public InventoryResponseVO checkStockLevelForQuantityUpdate(String skuId, long quantity) {
		InventoryResponseVO invResponseVO = null;
		long effectiveStockLevel = 0;
		long stockLevel = getStockLevelForSkuId(skuId);
		RepositoryItem sku = null;
		if(quantity > stockLevel) {
			invResponseVO = new InventoryResponseVO();
			invResponseVO.setOutOfStock(true);
		} else {
			try {
				sku = productCatalog.getItem(skuId, CricketCommonConstants.ITEM_DESC_SKU);
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
			effectiveStockLevel = (stockLevel - quantity) + 1;
			invResponseVO = getThresholdMessaging(sku, effectiveStockLevel);
		}
		return invResponseVO;
	}

	public Map<Integer, String> getDefaultThresholds() {
		return defaultThresholds;
	}

	public void setDefaultThresholds(Map<Integer, String> defaultThresholds) {
		this.defaultThresholds = defaultThresholds;
	}

	public Repository getInventoryRepository() {
		return inventoryRepository;
	}

	public void setInventoryRepository(Repository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	public Repository getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(Repository productCatalog) {
		this.productCatalog = productCatalog;
	}

	public List<String> getDefaultThresholdquantityList() {
		return defaultThresholdquantityList;
	}

	public void setDefaultThresholdquantityList(
			List<String> defaultThresholdquantityList) {
		this.defaultThresholdquantityList = defaultThresholdquantityList;
	}

}
