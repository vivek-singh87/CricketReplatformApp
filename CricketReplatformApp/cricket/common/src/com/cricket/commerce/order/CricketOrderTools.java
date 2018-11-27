package com.cricket.commerce.order;

import static com.cricket.common.constants.CricketESPConstants.PHONE_PRODUCT;
import static com.cricket.common.constants.CricketESPConstants.PLAN_PRODUCT;
import static com.cricket.common.constants.CricketESPConstants.PROPERTY_DISPLAY_NAME;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;
import javax.transaction.TransactionManager;
import javax.xml.rpc.ServiceException;

import atg.commerce.order.ChangedProperties;
import atg.commerce.order.HardgoodShippingGroup;
import atg.commerce.order.ObjectCreationException;
import atg.commerce.order.Order;
import atg.commerce.pricing.AmountInfo;
import atg.commerce.pricing.ItemPriceInfo;
import atg.commerce.pricing.PricingAdjustment;
import atg.core.util.StringUtils;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.projects.store.order.StoreOrderTools;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.service.pipeline.PipelineResult;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.ServletUtil;

import com.cricket.cart.vo.PackageVO;
import com.cricket.checkout.utils.CricketAddress;
import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.commerce.order.payment.CricketCreditCard;
import com.cricket.commerce.pricing.CricketItemPriceInfo;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ESPApplicationException;
import com.cricket.integration.esp.CricketESPAdapter;

/**
 * @author TechM
 *
 */
public class CricketOrderTools extends StoreOrderTools {
	/**
	 * @param packageId
	 * @return
	 */
	private CartConfiguration cartConfiguration;
	/* holds espAdapter instance to communicate ESP Layer */
	  private CricketESPAdapter espAdapter;
	 
	  public List<CricketCommerceItemImpl> getCommerceItemswithUniquePackageId(String packageId, CricketOrderImpl cricketOrder) {

				List<CricketCommerceItemImpl> commerceItems =  cricketOrder.getCommerceItems();
				List<CricketCommerceItemImpl> packageommerceItems = new ArrayList<CricketCommerceItemImpl>();
				if (null != commerceItems && !commerceItems.isEmpty() && commerceItems.size() > 0) {
				for (CricketCommerceItemImpl CricketCommerceItemImpl : commerceItems) {
					if (!StringUtils.isEmpty(CricketCommerceItemImpl.getPackageId()) && CricketCommerceItemImpl.getPackageId().equalsIgnoreCase(packageId)) {
						packageommerceItems.add(CricketCommerceItemImpl);
					}
				}
				}
				/*RepositoryView reposView = getOrderRepository().getView(CricketCommonConstants.COMMERCEITEM);
				RqlStatement statement = RqlStatement.parseRqlStatement("packageId=?0");
				Object[] inputParams = new Object[1];
				inputParams[0] = packageId;
				commerItemList = statement.executeQuery(reposView, inputParams);*/			
			return packageommerceItems;
		}
	public RepositoryItem findItem(String pCommerceItemId){
		RepositoryItem item = null;
		try {
			item = getOrderRepository().getItem(pCommerceItemId, CricketCommonConstants.COMMERCEITEM);
		} catch (RepositoryException e) {
			if(isLoggingError()){
				logError("Repository Exception in getCommerceItemsForPackage of CricketOrderTools",e);
			}
		}
		return item;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getRemovedAddonCommerceItems(CricketOrderImpl order) {
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Entering into CricketOrderTools class of getRemovedAddonCommerceItems() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		List<CricketCommerceItemImpl> itemList = order.getCommerceItems();
		List<String> removedAddonsList = new ArrayList<String>();
		if (itemList != null && !itemList.isEmpty()) {
			for (CricketCommerceItemImpl cricketCommerceItem: itemList) {
				if (!StringUtils.isEmpty(cricketCommerceItem.getCricItemTypes()) && cricketCommerceItem.getCricItemTypes().equalsIgnoreCase(getCartConfiguration().getRemovedAddonItemType())) {
					removedAddonsList.add(cricketCommerceItem.getAuxiliaryData().getProductId());
				}
			}
		}
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Exiting from CricketOrderTools class of getRemovedAddonCommerceItems() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return removedAddonsList;
	}
	@SuppressWarnings("unchecked")
	public CricketCommerceItemImpl[] getCommerceItemsForPackage(CricketPackage cricketPackage, CricketOrderImpl order) {
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Entering into CricketOrderTools class of getCommerceItemsForPackage() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		CricketCommerceItemImpl[] commerItemList = null;
		
		List<CricketCommerceItemImpl> commerceItemList = (List<CricketCommerceItemImpl>) order.getCommerceItems();
		List<CricketCommerceItemImpl> packageCommerceItemList = new ArrayList<CricketCommerceItemImpl>();

        for (CricketCommerceItemImpl cricketCommerceItemImpl : commerceItemList) {
            if (!StringUtils.isBlank(cricketCommerceItemImpl.getPackageId())
            		&& null != cricketPackage
            		&& !StringUtils.isBlank(cricketPackage.getId())
            		&& cricketPackage.getId().equalsIgnoreCase(cricketCommerceItemImpl.getPackageId())) {
            	
            	packageCommerceItemList.add(cricketCommerceItemImpl);
            }
        }
        
        commerItemList = packageCommerceItemList.toArray(new CricketCommerceItemImpl[packageCommerceItemList.size()]);
        if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Exiting from CricketOrderTools class of getCommerceItemsForPackage() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return commerItemList;
	}
	
	public CricketPackage getCricketPackage(String packageId, CricketOrderImpl order) {
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Entering into CricketOrderTools class of getCricketPackage() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		List<CricketPackage> cricketPackageList = (List<CricketPackage>) order.getCktPackages();
		CricketPackage commerceItemCricketPackage = null;
		for (CricketPackage cricketPackage : cricketPackageList) {
			if (null != cricketPackage && !StringUtils.isBlank(cricketPackage.getId())
					&& !StringUtils.isBlank(packageId) 
					&& packageId.equalsIgnoreCase(cricketPackage.getId())) {
				commerceItemCricketPackage = cricketPackage;
			}
		}
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Exiting from CricketOrderTools class of getCricketPackage() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		if (null == commerceItemCricketPackage) {
			logInfo("CricketOrderTools: getCricketPackage() method: No Cricket Packages returned for Package ID: " +  packageId
					+ " for order ID : " + order.getId());
		}
		return commerceItemCricketPackage;
	}
	 public CricketPackage createCricketPackage() throws ObjectCreationException
	  {
		 CricketPackage cricketPackage = new CricketPackage();
	    if (cricketPackage instanceof ChangedProperties)
	      ((ChangedProperties) cricketPackage).setSaveAllProperties(true);

	    try {
	      MutableRepository mutRep = (MutableRepository) getOrderRepository();
	      MutableRepositoryItem mutItem = mutRep.createItem(CricketCommonConstants.CRICKET_PACKAGE);
	      cricketPackage.setId(mutItem.getRepositoryId());
	      if (cricketPackage instanceof ChangedProperties)
	        ((ChangedProperties) cricketPackage).setRepositoryItem(mutItem);
	    }
	    catch (RepositoryException e) {
	      throw new ObjectCreationException(e);
	    }

	    return cricketPackage;
	  }
	
	public RepositoryItem createPackageItem(){
		
		TransactionManager tm = getTransactionManager();
		TransactionDemarcation td = new TransactionDemarcation();
		MutableRepositoryItem packageItem = null;
		MutableRepository mRepos = (MutableRepository) getOrderRepository();

		try {
			td.begin(tm);
			if (isLoggingDebug()) {
				logDebug("Start createPackageItem Method");
			}
			
			if(mRepos != null){
				packageItem = mRepos.createItem(CricketCommonConstants.CRICKET_PACKAGE);
				packageItem.setPropertyValue(CricketCommonConstants.PACKAGE_NUMBER, 1);
				mRepos.addItem(packageItem);
			}			
		} catch (RepositoryException repositoryException) {
			vlogError("Repository Exception in createPackageItem of CricketOrderTools : ", repositoryException);
			if (isLoggingDebug()) {
				logDebug("There was a RepositoryException while inserting the records in createPackageItem Method of class CricketOrderTools");
			}	
		} catch (TransactionDemarcationException exc) {
			vlogError("TransactionDemarcationException in createPackageItem method of class CricketOrderTools : ",  exc);
			if (isLoggingDebug()) {
				logDebug("There was a TransactionDemarcationException while inserting the createPackageItem Method of class CricketOrderTools");
			}	
		} finally {
			try {
				td.end();
			} catch (TransactionDemarcationException e) {
				vlogError("TransactionDemarcationException in createPackageItem method of class CricketOrderTools: ", e);
				if (isLoggingDebug()) {
					logDebug("There was a TransactionDemarcationException while inserting the createPackageItem Method of class CricketOrderTools");
				}	
			}
		}	
		return packageItem;
	}
	
	public void updateCommerceItem(CricketCommerceItemImpl commerceItem,
			String productType, CricketPackage cricketPackage) {
		commerceItem.setCricItemTypes(productType);
		commerceItem.setPackageId(cricketPackage.getId());
		commerceItem.setLOS(Boolean.TRUE);
	}
	
	/**
	 * This method fetches all commerce items in the order and 
	 * maps each package id to its list of commerce item.
	 * @param order
	 * @return Map<String, List<CricketCommerceItemImpl>>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<CricketCommerceItemImpl>> getPackageWiseCommerceItems(Order order){	
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Entering into CricketOrderTools class of getPackageWiseCommerceItems() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		Map<String, List<CricketCommerceItemImpl>> packageWiseCIMap = new TreeMap<String, List<CricketCommerceItemImpl>>();		
		List<CricketCommerceItemImpl> commerceItemList = (List<CricketCommerceItemImpl>)order.getCommerceItems();		
		for (CricketCommerceItemImpl cricketCommerceItemImpl : commerceItemList) {
			String packageId = cricketCommerceItemImpl.getPackageId();
			List<CricketCommerceItemImpl> pkgCommerceItemList = null;
			 if (!StringUtils.isBlank(packageId)) {
				 if(packageWiseCIMap.containsKey(packageId)){
					 pkgCommerceItemList = packageWiseCIMap.get(packageId);
					 pkgCommerceItemList.add(cricketCommerceItemImpl);					 
				 }
				 else{					 
					 pkgCommerceItemList = new ArrayList<CricketCommerceItemImpl>();
					 pkgCommerceItemList.add(cricketCommerceItemImpl);					
				 }
				 packageWiseCIMap.put(packageId, pkgCommerceItemList);
			 }
		}
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Exiting from CricketOrderTools class of getPackageWiseCommerceItems() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return packageWiseCIMap;
	}
	
	
	/**
	 * This method fetches the the details of the commercerItems based on the type of the Product
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getDisplayItems(CricketOrderImpl order, DynamoHttpServletRequest pRequest){
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Entering into CricketOrderTools class of getDisplayItems() method--- Fetching all items to display:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		Map<String, Object> displyItemsMap = new HashMap<String, Object>();
		//accessories list
		List<CricketCommerceItemImpl> accessoriesItems = new ArrayList<CricketCommerceItemImpl>();
		//upgradePhoneItems list
		List<CricketCommerceItemImpl> upgradePhoneItems = new ArrayList<CricketCommerceItemImpl>();
		//changePlanItems list
		List<CricketCommerceItemImpl> changePlanItems = new ArrayList<CricketCommerceItemImpl>();
		//changeAddonsItems list
		List<CricketCommerceItemImpl> changeAddonsItems = new ArrayList<CricketCommerceItemImpl>();
		//removedAddonCommerceItems list
		List<CricketCommerceItemImpl> removedAddonsItems = new ArrayList<CricketCommerceItemImpl>();
		//map for package with key as package id and value as commerceItem
		Map<String, PackageVO> mapPackageCommerceItems = new TreeMap<String, PackageVO>();
		//check if commerceitem count is zero 
		if(order != null && order.getCommerceItemCount() > 0){
			if(isLoggingDebug()){
				logDebug("CommerceItem Count : " + order.getCommerceItemCount());
			}
			
			//get commerce items frm order
			List<CricketCommerceItemImpl> commerceItemList = order.getCommerceItems();
			for(CricketCommerceItemImpl commerceItem : commerceItemList){
				//product type of commerce item
				String productType = commerceItem.getCricItemTypes();
				//check if prduct type is accessory
				if(productType != null && productType.equalsIgnoreCase(CricketCommonConstants.ACCESSORY_PRODUCT)){
					accessoriesItems.add(commerceItem);
				//check if prduct type is upgradePhone
				}else if(productType != null && productType.equalsIgnoreCase(CricketCommonConstants.CITYPE_UPGRADEPHONE)){
					upgradePhoneItems.add(commerceItem);
				//check if prduct type is changePlan
				}else if(productType != null && productType.equalsIgnoreCase(CricketCommonConstants.CITYPE_CHANGEPLAN)){
					changePlanItems.add(commerceItem);
				//check if prduct type is changeAddOn
				}else if(productType != null && productType.equalsIgnoreCase(CricketCommonConstants.CITYPE_CHANGEADDON)){
					changeAddonsItems.add(commerceItem);
				}else if(productType != null && productType.equalsIgnoreCase(CricketCommonConstants.CITYPE_RMOEVEDADDON)){
					removedAddonsItems.add(commerceItem);
				}
			}
			// NOW CHECKING PACKAGES
			List<CricketPackage> cricketPackages = order.getCktPackages();
			//Get package wise commerceItems
			Map<String, List<CricketCommerceItemImpl>> packageWiseCIMap = getPackageWiseCommerceItems(order);
			
			if(cricketPackages != null && cricketPackages.size() > 0){
				for(CricketPackage cricketPackage : cricketPackages){					
					//get package id
					String packageId = cricketPackage.getId();
					if(isLoggingDebug()){
						logDebug("Setting values in PackageVO for Package : " + packageId);
					}
					//get commerce item for package
					List<CricketCommerceItemImpl> pkgCommerceItems  = packageWiseCIMap.get(cricketPackage.getId());					
					if(pkgCommerceItems != null && pkgCommerceItems.size() > 0){
						PackageVO packageVO = new PackageVO();
						packageVO.setPackageId(packageId);
						packageVO.setPackageTotal(cricketPackage.getPackageTotal());
						List<ItemPriceInfo> addonList = new ArrayList<ItemPriceInfo>();
						for(CricketCommerceItemImpl commerceItem : pkgCommerceItems){
							//CricketCommerceItemImpl commItem = (CricketCommerceItemImpl) order.getCommerceItem(commerceItem.getId());
							//product type of commerce item
							String productType = (String) commerceItem.getPropertyValue(CricketCommonConstants.CRICKET_ITEMTYPES);							
							//check if product type is phone
							if(productType.equalsIgnoreCase(CricketCommonConstants.PHONE_PRODUCT)){
								packageVO.setPhoneCommerceItem(commerceItem.getRepositoryItem());									
								if(null != (CricketItemPriceInfo) commerceItem.getPriceInfo())	{
									packageVO.setPhonePriceInfo(commerceItem.getPriceInfo());
									packageVO.setActivationFee(((CricketItemPriceInfo) commerceItem.getPriceInfo()).getActivationFee());
									// Getting Phone Discounts																				
									packageVO.setPhoneDiscountAdjustments(getDiscounts(commerceItem));
								}
							//check if product type is plan
							}else if(productType.equalsIgnoreCase(CricketCommonConstants.PLAN_PRODUCT)){
								packageVO.setPlanCommerceItem(commerceItem.getRepositoryItem());
								packageVO.setPlanPriceInfo((CricketItemPriceInfo) commerceItem.getPriceInfo());	
								packageVO.setPlanDiscountAdjustments(getDiscounts(commerceItem));
							//check if product type is addOn
							}else if(productType.equalsIgnoreCase(CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT)){
								// add addon to packageVO
								List<RepositoryItem> addOnCommItem = new ArrayList<RepositoryItem>(); 
								addOnCommItem = packageVO.getAddOnsCommerceItems() != null ? packageVO.getAddOnsCommerceItems() : addOnCommItem;
								addOnCommItem.add(commerceItem.getRepositoryItem());
								packageVO.setAddOnsCommerceItems(addOnCommItem);
								addonList.add(commerceItem.getPriceInfo());
								packageVO.setAddOnPriceInfo(addonList);
							}
						}
						mapPackageCommerceItems.put(packageId, packageVO);
					}
				}
			}
		}
		if(isLoggingDebug()){
			logDebug("Package wise Commerce Item set to packageVO : " + mapPackageCommerceItems.size());
		}
		if(accessoriesItems != null && accessoriesItems.size() > 0){
			displyItemsMap.put(CricketCommonConstants.ACCESSORIES, accessoriesItems);
		}
		if(upgradePhoneItems!=null && upgradePhoneItems.size()>0){
			displyItemsMap.put(CricketCommonConstants.UPGRADE_PHONE,upgradePhoneItems);
		}
		if(changePlanItems!=null && changePlanItems.size()>0){
			displyItemsMap.put(CricketCommonConstants.CHANGE_PLAN,changePlanItems);
		}
		if(changeAddonsItems!=null && changeAddonsItems.size()>0){
			displyItemsMap.put(CricketCommonConstants.CHANGE_ADDONS,changeAddonsItems);
		}
		if(mapPackageCommerceItems != null && mapPackageCommerceItems.size() > 0){			
			displyItemsMap.put(CricketCommonConstants.PACKAGES, mapPackageCommerceItems);
		}
		if(removedAddonsItems!=null && removedAddonsItems.size()>0){
			displyItemsMap.put(CricketCommonConstants.REMOVED_ADDONS,removedAddonsItems);
		}
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
					pageURL = pRequest.getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Exiting from into CricketOrderTools class of getDisplayItems() method--- Fetching all items to display:::" + displyItemsMap.size() + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return displyItemsMap;
	}
	
	/**
	 * This method is used for getting the list of discounts applied to the commerceItem
	 * @param CricketCommerceItemImpl
	 * @return List<PricingAdjustment> - list of discounts applied to the item
	 */
	@SuppressWarnings("unchecked")
	private List<PricingAdjustment> getDiscounts(CricketCommerceItemImpl commerceItem){		
		if(isLoggingDebug()){
			logDebug("Start getDiscounts --- Fetch all Discounts applicable : ");
		}
		List<PricingAdjustment> phoneDiscountAdjustments = new ArrayList<PricingAdjustment>();
		if(null !=commerceItem.getPriceInfo()){
			List<AmountInfo> pricingDetails = commerceItem.getPriceInfo().getCurrentPriceDetails();	
			
			for (AmountInfo amountInfo : pricingDetails){										
				List<PricingAdjustment> adjustmentList = amountInfo.getAdjustments();
				for(PricingAdjustment pricingAdjustment : adjustmentList ){
					 if (pricingAdjustment.getAdjustmentDescription().equalsIgnoreCase(CricketCommonConstants.ITEM_DISCOUNT)) {
						 phoneDiscountAdjustments.add(pricingAdjustment);
					}											
				}
			}
		}
		if(isLoggingDebug()){
			logDebug("End getDiscounts --- Fetched all Discounts applicable : " + phoneDiscountAdjustments.size());
		}
		return phoneDiscountAdjustments;
	}
	
	/** this method is used to display the order details i.e commerceItems,payment method,shipping method and if any discount*/
	public String orderDetailsToTrack(CricketOrderImpl order,CricketAddress accountHolderaddress, String processorName){
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
			// Getting the Session Id
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();
			 String sessionId = CricketCommonConstants.EMPTY_STRING;
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}		
	    		logDebug("Entering into into CricketOrderTools class of orderDetailsToTrack() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		// now displaying the commerceItems products
		List<CricketCommerceItemImpl> commerceItems = order.getCommerceItems();
		RepositoryItem productItem = null;
		String productName = null;
		double amount = 0.0;
		double listPrice = 0.0;
		String itemType = null;
		StringBuilder productInfo = null;
		logInfo("################# START .. "+processorName+" displaying details ###########################");
		logInfo("Order Id : "+order.getId());
		logInfo("Order Type : "+order.getWorkOrderType());
		if(accountHolderaddress!=null){
		logInfo("Account Holder Address : "+accountHolderaddress.toString());
		}
		if(null!= order.getWorkOrderType() && order.getWorkOrderType().equalsIgnoreCase("ACT") || order.getWorkOrderType().equalsIgnoreCase("ADD")){
 			logInfo(" *** Number of Packages in this Order: *** "+ order.getCktPackages().size());
			logInfo(" *** ITEMS in Each Package : *** " );
			List<CricketPackage> cktPackages = order.getCktPackages();
			CricketCommerceItemImpl[] commerceItemList = null;
			CricketCommerceItemImpl commItem = null;
			for(CricketPackage cktPackage : cktPackages){
				commerceItemList =  getCommerceItemsForPackage(cktPackage, order);
				if(commerceItemList != null && commerceItemList.length > 0){
					for(int i=0;i<commerceItemList.length;i++){
						productInfo = new StringBuilder();
						commItem = commerceItemList[i];
						 itemType     = commItem.getCricItemTypes();
						 productItem  = (RepositoryItem)commItem.getAuxiliaryData().getProductRef();
						 productName  = (String)productItem.getPropertyValue(PROPERTY_DISPLAY_NAME);
						 amount       = commItem.getPriceInfo().getAmount();
						 listPrice    = commItem.getPriceInfo().getListPrice();
						 productInfo.append("Product Type :"+itemType).append("|| Product Name : "+productName).
						 append("|| List Price of Product: "+listPrice).append("|| Product amount(total) : "+amount);
						 logInfo(productInfo.toString());
					}
 				}
			}
		} else{
		if(commerceItems!=null && commerceItems.size()>0){
 				for(CricketCommerceItemImpl item:commerceItems){
					 productInfo = new StringBuilder();
					 itemType     = item.getCricItemTypes();
					 productItem  = (RepositoryItem)item.getAuxiliaryData().getProductRef();
					 productName  = (String)productItem.getPropertyValue(PROPERTY_DISPLAY_NAME);
					 amount       = item.getPriceInfo().getAmount();
					 listPrice    = item.getPriceInfo().getListPrice();
					 productInfo.append("Product Type :"+itemType).append("|| Product Name : "+productName).
					 append("|| List Price of Product: "+listPrice).append("|| Product amount(total) : "+amount);
					 logInfo(productInfo.toString());
 				}
			}
		}
		// now displaying the shipping information
 		List<HardgoodShippingGroup> shippingGroupsList = order.getShippingGroups();
		if(shippingGroupsList!=null && shippingGroupsList.size()>0){
			for(HardgoodShippingGroup shippingGroup:shippingGroupsList){
 				logInfo("shippingMethod : "+shippingGroup.getShippingMethod());
				logInfo("Shipping Address : "+shippingGroup.getShippingAddress().getFirstName()+" | "+shippingGroup.getShippingAddress().getLastName()+
						 " | "+shippingGroup.getShippingAddress().getAddress1()+" | "+shippingGroup.getShippingAddress().getState() +" | "+shippingGroup.getShippingAddress().getPostalCode()+
						 " | "+shippingGroup.getShippingAddress().getCity()+" | "+shippingGroup.getShippingAddress().getCountry());
			}
		}
		// now displaying the payment information
		List<CricketCreditCard> paymentGroupsList = order.getPaymentGroups();
		if(paymentGroupsList!=null && paymentGroupsList.size()>0){
			for(CricketCreditCard paymentGroup:paymentGroupsList){
				if(null!=paymentGroup.getCreditCardNumber()){
 					logInfo("PaymentMethod : "+paymentGroup.getPaymentMethod()+"|| Card Type : "+paymentGroup.getCreditCardType()+"|| Card Number[last 4 digits] : "+paymentGroup.getCreditCardNumber());
					logInfo("isDifferentCard : "+paymentGroup.isDiffernetCard()+"||Auto bill payment flag : "+paymentGroup.getAutoBillPayment()+"|| Vesta token: "+paymentGroup.getVestaToken());
					logInfo("Billing Address : "+paymentGroup.getBillingAddress().getFirstName()+" | "+paymentGroup.getBillingAddress().getLastName()+
						 " | "+paymentGroup.getBillingAddress().getAddress1()+" | "+paymentGroup.getBillingAddress().getState() +" | "+paymentGroup.getBillingAddress().getPostalCode()+
						 " | "+paymentGroup.getBillingAddress().getCity()+" | "+paymentGroup.getBillingAddress().getCountry());
					}
			}
		}
	
		// now displaying the any adjustments - discounts
				logInfo("marketCode : "+order.getMarketCode());
				logInfo("Is Order - isDownGrade : "+order.isDownGrade());
				logInfo("billingQuoteId : "+order.getBillingQuoteId());
				logInfo("cricCustomerId : "+order.getCricCustomerId());
				logInfo("cricCustmerBillingNumber : "+order.getCricCustmerBillingNumber());
				logInfo("posSaleId : "+order.getPosSaleId());
				logInfo("vestaSystemOrderId	 : "+order.getVestaSystemOrderId());
				logInfo("billingSystemOrderId : "+order.getBillingSystemOrderId());
				logInfo("billSysPaymentApprTransId : "+order.getBillSysPaymentApprTransId());
				logInfo("billSysPaymentRefId : "+order.getBillSysPaymentRefId());
				logInfo("teleSaleCode : "+order.getTeleSaleCode());
				logInfo("estimatedDeliveryDate : "+order.getEstimatedDeliveryDate());
				logInfo("removedPhoneId : "+order.getRemovedPhoneId());
				logInfo("removedPhoneSkuId : "+order.getRemovedPhoneSkuId());
				logInfo("removedPlanId : "+order.getRemovedPlanId());
				logInfo("removedPlanSkuId : "+order.getRemovedPlanSkuId());
				logInfo("upgradeModelNumber : "+order.getUpgradeModelNumber());
				logInfo("upgradeMdn : "+order.getUpgradeMdn());
				logInfo("user Id Address : "+order.getUserIpAddress());
				logInfo("phoneNumber : "+order.getPhoneNumber());
				logInfo("emailId : "+order.getEmailId());
				logInfo("languageIdentifier : "+order.getLanguageIdentifier());
				logInfo("shippingAddressType : "+order.getShippingAddressType());
				logInfo("billingAddressType : "+order.getBillingAddressType());
				logInfo("totalTax : "+order.getTotalTax());
				logInfo("order amount : "+order.getPriceInfo().getAmount());
                logInfo("order RawSubtotal : "+order.getPriceInfo().getRawSubtotal());
                logInfo("order Total : "+order.getPriceInfo().getTotal());
                logInfo("order Adjustments : "+order.getPriceInfo().getAdjustments());
				logInfo("oob order details : "+order.toString());
				logInfo("################# End of "+processorName+" displaying details ###########################");
				if (isLoggingDebug()) {
					// Getting the Page url
						String pageURL = CricketCommonConstants.EMPTY_STRING;
						if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
							pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
						}
					// Getting the Session Id
					 HttpSession session = ServletUtil.getCurrentRequest().getSession();
					 String sessionId = CricketCommonConstants.EMPTY_STRING;
					 if(null != session) {
						 sessionId = session.getId();
					 }
					 // Getting the Order Id
						String orderId = CricketCommonConstants.EMPTY_STRING;
						if(!StringUtils.isBlank(order.getId())){
							orderId = order.getId();
						}		
			    		logDebug("Exiting from CricketOrderTools class of orderDetailsToTrack() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
				}
		return "";
	}
	
	/**
	 * @return the cartConfiguration
	 */
	public CartConfiguration getCartConfiguration() {
		return cartConfiguration;
	}

	/**
	 * @param pCartConfiguration the cartConfiguration to set
	 */
	public void setCartConfiguration(CartConfiguration pCartConfiguration) {
		cartConfiguration = pCartConfiguration;
	}
	
	public String getProfileString(RepositoryItem profile){
		
		StringBuilder strValue = new StringBuilder();
		strValue.append("\n-------------------------------     Profile  Info      -------------------------------");
		strValue.append("userLocationZipCode = "+profile.getPropertyValue("userLocationZipCode"));
		strValue.append(" :manuallyEnteredZipCode = "+profile.getPropertyValue("manuallyEnteredZipCode"));
		strValue.append(" :isDefaultLocation = "+profile.getPropertyValue("isDefaultLocation"));
		strValue.append(" :userLocationCity = "+profile.getPropertyValue("userLocationCity"));
		strValue.append(" :isLOSExist = "+profile.getPropertyValue("isLOSExist"));
		strValue.append(" :networkProvider = "+profile.getPropertyValue("networkProvider"));
		strValue.append(" :isUserLoggedIn = "+profile.getPropertyValue("isUserLoggedIn"));
		strValue.append(" :marketId = "+profile.getPropertyValue("marketId"));
		strValue.append(" :marketType = "+profile.getPropertyValue("marketType"));
		strValue.append(" :marketName = "+profile.getPropertyValue("marketName"));
		strValue.append(" :activationFee = "+profile.getPropertyValue("activationFee"));
		strValue.append(" :marketSubTypes = "+profile.getPropertyValue("marketSubTypes"));
		strValue.append(" :sprintCSA = "+profile.getPropertyValue("sprintCSA"));
		strValue.append(" :jointVenture = "+profile.getPropertyValue("jointVenture"));
		strValue.append(" :rateCenterId = "+profile.getPropertyValue("rateCenterId"));
		strValue.append(" :accountType = "+profile.getPropertyValue("accountType"));
		strValue.append(" :customerType = "+profile.getPropertyValue("customerType"));
		strValue.append(" :customerNumber = "+profile.getPropertyValue("customerNumber"));
		strValue.append(" :login = "+profile.getPropertyValue("login"));
		strValue.append(" :email = "+profile.getPropertyValue("email"));
		strValue.append(" :autoLogin = "+profile.getPropertyValue("autoLogin"));
		
		return strValue.toString();
		
	}
	
	public boolean isValidCricketPackage(CricketCommerceItemImpl[] commerceItemList) {
		boolean isValidCricketPackage = false;
		boolean phoneProductExist = false;
		boolean planProductExist = false;
		String productType = null;
		
		if(commerceItemList != null && commerceItemList.length > 0){			
			for(CricketCommerceItemImpl commerceItem : commerceItemList){
				productType = commerceItem.getCricItemTypes();
				if(PHONE_PRODUCT.equalsIgnoreCase(productType)){
					phoneProductExist = true;
				}else if(PLAN_PRODUCT.equalsIgnoreCase(productType)){
					planProductExist = true;
				}
				if(phoneProductExist && planProductExist){
					isValidCricketPackage = true;
					break;
				}
			}
		}
		
		return isValidCricketPackage;
	}
	
	/**
	 * @return the espAdapter
	 */
	public CricketESPAdapter getEspAdapter() {
		return espAdapter;
	}
	/**
	 * @param espAdapter the espAdapter to set
	 */
	public void setEspAdapter(CricketESPAdapter espAdapter) {
		this.espAdapter = espAdapter;
	}
	
	
}
