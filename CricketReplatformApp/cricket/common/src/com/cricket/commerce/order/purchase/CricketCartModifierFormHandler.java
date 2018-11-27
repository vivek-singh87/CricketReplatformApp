package com.cricket.commerce.order.purchase;

import static com.cricket.common.constants.CricketCommonConstants.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import atg.commerce.CommerceException;
import atg.commerce.catalog.CatalogTools;
import atg.commerce.inventory.InventoryException;
import atg.commerce.order.CommerceItem;
import atg.commerce.order.CommerceItemNotFoundException;
import atg.commerce.order.CricketCommerceItemManager;
import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.Order;
import atg.commerce.order.ShippingGroup;
import atg.commerce.order.ShippingGroupCommerceItemRelationship;
import atg.commerce.order.purchase.CartModifierFormHandler;
import atg.commerce.promotion.PromotionTools;
import atg.commerce.util.RepeatingRequestMonitor;
import atg.core.util.StringUtils;
import atg.droplet.DropletException;
import atg.droplet.DropletFormException;
import atg.nucleus.naming.ParameterName;
import atg.projects.store.inventory.StoreInventoryManager;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.service.pipeline.RunProcessException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileServices;

import com.cricket.browse.DisplayFeaturesManager;
import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketBillingAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderManager;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.commerce.order.CricketPackage;
import com.cricket.commerce.order.CricketPurchaseProcessHelper;
import com.cricket.commerce.order.OrderConstants;
import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.commerce.order.session.UpgradeItemDetailsSessionBean;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.configuration.CricketConfiguration;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.CricketESPAdapterHelper;
import com.cricket.integration.esp.vo.InquireFeaturesResponseVO;
import com.cricket.integration.esp.vo.OfferingsVO;
import com.cricket.integration.esp.vo.SubscriberVO;
import com.cricket.integration.maxmind.CitySessionInfoObject;
import com.cricket.integration.maxmind.GeoIpLocationTools;
import com.cricket.user.session.UserAccountInformation;
import com.cricket.user.session.UserSessionBean;
import com.cricket.util.CricketUtils;
import com.cricket.vo.CricketProfile;
import com.cricket.vo.MyCricketCookieCartInfo;

import de.ailis.pherialize.Mixed;

/**
 * @author TechM
 * 
 */
public class CricketCartModifierFormHandler extends CartModifierFormHandler {
	
	private CricketShippingAddressData shippingAddressData;
	private CricketBillingAddressData billingAddressData;
	private CitySessionInfoObject citySessionInfoObjectPath;

	private CatalogTools catalogTools;
	private String duplicatePackageId;
	private String deletePacakgeId;
	private String addonProductType;
	private String addonCommerceItemId;
	private CricketOrderManager cricketOrderManager;
	private String duplicatePackageErrorUrl;
	private String duplicatePackageSuccessUrl;
	private String deletePackageSuccessURL;
	private String deletePackageFailureURL;
	private String updateAccessoryFailureURL;
	private String updateAccessorySuccessURL;
	private List<RepositoryItem> loosingPromotions;
	private String deleteCommerceItemId;
	private PromotionTools promotionTools;
	private String skuId;
	public static final ParameterName COOKIE_NAME = ParameterName.getParameterName(ORDER_PERSISTENT_COOKIE);
	private CricketProfile cricketProfile;
	private CricketESPAdapter cricketEspAdapter;
	private CricketAccountHolderAddressData accountHolderAddressData;
	private UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean;
	private MyCricketCookieCartInfo cartCookie;
	private CartConfiguration cartConfiguration;
	private String editPackageId;
	private String editProductType;
	private String editSuccessUrl;
	private String removeItemType;
	private String upgradeItemFlow;
	private String formPackageId;
	private int maxNoOfPackageForPIA;
	private int maxNoOfPackageForPayGo;
	//private String openCart;
	private String updateAccessoryId;
	private String allPhoneUrl;
	private String allPlanUrl;
	private DisplayFeaturesManager displayFeaturesManager;
	private boolean isDynamicAddOn;

	private String removalAddonId;
	private boolean modalError;
	private boolean reloadDrawerCartDiv;
	private String redirectURL;
	private String currentURL;
	private Repository catalogRepository;
	private boolean cartOpen;
	private boolean addAsRemovedCommerceItem;
	private Repository storeTextRepository;
	private boolean onlyAccessories;
	private StoreInventoryManager inventoryManager;
	private UserAccountInformation userAccountInformation;
	/* holds CricketESPAdapterHelper */
	private CricketESPAdapterHelper cricketESPAdapterHelper;
	/* variable holds the payment data for normal credit card */ 
	private ProfileServices profileServices;
	private CricketConfiguration cricketConfiguration;
	private String geoIPLocationTools;
	private String cricketUtils;
	private String hppProductId;
	private String hppSKUId;
	private String hppProductFromESP;
	private String hppSKUFromESP;
 	public String getHppSKUFromESP() {
		return hppSKUFromESP;
	}
	public void setHppSKUFromESP(String hppSKUFromESP) {
		this.hppSKUFromESP = hppSKUFromESP;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * atg.commerce.order.purchase.CartModifierFormHandler#preAddItemToOrder
	 * (atg.servlet.DynamoHttpServletRequest,
	 * atg.servlet.DynamoHttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void preAddItemToOrder(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		businessRuleValidator();
		boolean flag = false;
		
		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}		
		 // Getting the Order Id
		 CricketOrderImpl order = (CricketOrderImpl) getOrder();
		 String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank( order.getId())){
	    			orderId = order.getId();
				}
			}
		if (isLoggingDebug()) {			
	    		logDebug("Entering into CricketCartModifierFormHandler class of preAddItemToOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		Profile profile = getProfileServices().getCurrentProfile();
		
		if(getProfile().isTransient()){
			setAddressFieldstoEmpty();
		}
		
		CricketCommerceItemImpl nonEditableCommerceItem = null;
		String productType = findProductType(getProductId());
		
		StringBuffer OrderPersistCookie = new StringBuffer();
		OrderPersistCookie.append(COOKIE_NAME);
		// Check if cookie contains OrderPersistentCookie cookies
		Cookie[] cookies = pRequest.getCookies();
		Cookie cookie1 = null;
		for(int i = 0; i < cookies.length; i++) { 
              cookie1 = cookies[i];
            if(cookie1.getName().contentEquals(OrderPersistCookie)){
            	flag = true;
            	break;
            }
        }

		if(flag == false){
			Cookie cookieName = new Cookie(ORDER_PERSISTENT_COOKIE,ORDER_PERSISTENT_COOKIE);
    		cookieName.setMaxAge(getCartConfiguration().getOrderPersistentCookieMaxAge());
    		order = (CricketOrderImpl) getOrder();
    		order.getCktPackages();
    		if(order != null){	
    			orderId = order.getId();
    		}
    		if(!StringUtils.isEmpty(orderId)){
    			cookieName.setValue(orderId);
    			cookieName.setPath(SEPARTOR);
    			pResponse.addCookie(cookieName);
    		}
		}
		addPayGoCustomerErrors(pRequest, pResponse);
		if (getFormError()) {
			return;
		}
		String userIntention = getUpgradeItemDetailsSessionBean().getUserIntention();
		if(!getProfile().isTransient() && !StringUtils.isEmpty(userIntention) && userIntention.equalsIgnoreCase(ACC_UPGRADE_PHONE) && !StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getRemovedPlanId()) && productType.equalsIgnoreCase(PHONE_PRODUCT) ){
			try {
				boolean phoneCompatibleWithPlan = isPhoneCompatibleWithPlan(getProductId(), getUpgradeItemDetailsSessionBean().getRemovedPlanId());
				if(!phoneCompatibleWithPlan){
					/*String msg = formatUserMessage(INCOMPATIBLE_PHONE_WITH_ACCOUNT, pRequest,pResponse);
					String propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
					setModalError(Boolean.FALSE);
					addFormException(new DropletFormException(msg,null, propertyPath, INCOMPATIBLE_PHONE_WITH_ACCOUNT));*/
				}
			} catch (RepositoryException e) {
				if (isLoggingError()) {
					vlogError("[RepositoryException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			}
		}else if(!getProfile().isTransient() && !StringUtils.isEmpty(userIntention) 
				&& userIntention.equalsIgnoreCase(ACC_UPGRADE_PLAN) 
				&& (getUpgradeItemDetailsSessionBean().isPlan4p5() 
						|| !StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getRemovedPhoneId())) 
						&& productType.equalsIgnoreCase(PLAN_PRODUCT)){
			try {
				boolean planCompatibleWithPhone = isPlanCompatibleWithPhone(getUpgradeItemDetailsSessionBean().getRemovedPhoneId(), getProductId());
				if(getUpgradeItemDetailsSessionBean().isPlan4p5()){
					String msg = formatUserMessage(FOUR_P_FIVE_PLAN_RATEPLAN_CHANGE_ERROR, pRequest,pResponse);
					String propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
					setModalError(Boolean.TRUE);
					addFormException(new DropletFormException(msg,null, propertyPath, FOUR_P_FIVE_PLAN_RATEPLAN_CHANGE_ERROR));
				} else if(!planCompatibleWithPhone){
					String msg = formatUserMessage(INCOMPATIBLE_PLAN_WITH_ACCOUNT, pRequest,pResponse);
					String propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
					setModalError(Boolean.FALSE);
					addFormException(new DropletFormException(msg,null, propertyPath, INCOMPATIBLE_PLAN_WITH_ACCOUNT));
				} else if(getProductId().equalsIgnoreCase(getUpgradeItemDetailsSessionBean().getRemovedPlanId())){
					String msg = formatUserMessage(UPGRADE_PLAN_SAME_AS_EXISTING_PLAN, pRequest,pResponse);
					String propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
					setModalError(Boolean.FALSE);
					addFormException(new DropletFormException(msg,null, propertyPath, UPGRADE_PLAN_SAME_AS_EXISTING_PLAN));
				}
			} catch (RepositoryException e) {
				if (isLoggingError()) {
					vlogError("[RepositoryException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			}
		}
		
		CricketOrderTools cricketOrderTools = (CricketOrderTools) getOrderManager().getOrderTools();
		if(getCartCookie().isEditActionFromCart() && (!StringUtils.isEmpty(pRequest.getParameter(EDIT_ITEM)) && pRequest.getParameter(EDIT_ITEM).equalsIgnoreCase(TRUE))){
			if(isLoggingDebug()){
				logDebug("Editing the plan or phone in a package");
			}
			this.getFormExceptions().clear();
			
			List<CricketCommerceItemImpl> commerceItemswithUniquePackageId = cricketOrderTools.getCommerceItemswithUniquePackageId(getCartCookie().getPackageId(), (CricketOrderImpl) getOrder());
			if(null != commerceItemswithUniquePackageId){
				//try {
					if (productType.equalsIgnoreCase(PHONE_PRODUCT)) {
						clearPackageCommerceItems(getCartCookie().getPackageId(), (CricketOrderImpl) getOrder());
						deleteItems(pRequest, pResponse);
					} else {
								
						for (CricketCommerceItemImpl cricketCommerceItem : commerceItemswithUniquePackageId) {
							 if (!getCartCookie().getProductType().equalsIgnoreCase(cricketCommerceItem.getCricItemTypes()) && !cricketCommerceItem.getCricItemTypes().equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)) {
								 nonEditableCommerceItem= cricketCommerceItem;
							 }
						}
						CricketCommerceItemImpl commerceItem = null;
						String[] remivalList = null;
						String msg = null;
						String propertyPath = null;
							for (CricketCommerceItemImpl cricketCommerceItem : commerceItemswithUniquePackageId) {
								if(getCartCookie().getProductType().equalsIgnoreCase(cricketCommerceItem.getCricItemTypes()) && nonEditableCommerceItem != null){
									if (isCompatiblePhoneAndPlan(productType,(RepositoryItem) nonEditableCommerceItem.getAuxiliaryData().getProductRef())) {
											remivalList = new String[1];
											remivalList[0] = cricketCommerceItem.getId();
											setRemovalCommerceIds(remivalList);
											deleteItems(pRequest, pResponse);
										}
									else {
										getCartCookie().setOpenCart(OPEN_CART);
										if (getCartCookie().isEditActionFromCart()) {
											getCartCookie().setPackageId(null);
											getCartCookie().setProductType(null);
											getCartCookie().setEditActionFromCart(false);
										}
										if (productType.equalsIgnoreCase(PHONE_PRODUCT)) {
											  msg = formatUserMessage(INCOMPATIBLE_PHONE, pRequest,pResponse);
											  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
											addFormException(new DropletFormException(msg,null, propertyPath, INCOMPATIBLE_PHONE));
										} else if (productType.equalsIgnoreCase(PLAN_PRODUCT)) {
											  msg = formatUserMessage(INCOMPATIBLE_PLAN, pRequest,pResponse);
											  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
											addFormException(new DropletFormException(msg,null, propertyPath, INCOMPATIBLE_PLAN));
										}
									}
								}
							}
							if(commerceItemswithUniquePackageId.size() == 1){
								  commerceItem = commerceItemswithUniquePackageId.get(0);
								if(getCartCookie().getProductType().equalsIgnoreCase(commerceItem.getCricItemTypes())){
									 remivalList = new String[1];
									remivalList[0] = commerceItem.getId();
									setRemovalCommerceIds(remivalList);
									deleteItems(pRequest, pResponse);
								}
							}
			}
				/*}catch (CommerceItemNotFoundException e) {
						if (isLoggingError()) {
							vlogError("[CommerceItemNotFoundException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
						}
					} catch (InvalidParameterException e) {
						if (isLoggingError()) {
							vlogError("[InvalidParameterException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
						}
					} */
				 }				
		} else {
			if(productType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)) {
				
				RepositoryItem addonProduct = null ;
				try {
					addonProduct = getCatalogTools().findProduct(getProductId());
				} catch (RepositoryException e) {
					vlogDebug("RepositoryException : ", e);
				}
				
				// Start check for addon rules before adding to cart
				if(addonProduct != null) {
					if (!isAddAsRemovedCommerceItem()) {
					List<RepositoryItem> dependantAddOns = (List<RepositoryItem>) addonProduct.getPropertyValue(DEPENDENT_ADD_ON);
					List<RepositoryItem> otherAddonsInGroup = (List<RepositoryItem>) addonProduct.getPropertyValue(OTHER_ADD_ON_GROUP);
					boolean isDependantAddonsPresent = true;
					CricketOrderTools tools = (CricketOrderTools) getOrderManager().getOrderTools();
					order = (CricketOrderImpl) getOrder();
					CricketPackage cktPackage = tools.getCricketPackage(getFormPackageId(), order);
					CricketCommerceItemImpl[] commerceItems = (CricketCommerceItemImpl[]) tools.getCommerceItemsForPackage(cktPackage, order);
					if(dependantAddOns != null || otherAddonsInGroup != null) {
						RepositoryItem pkgProduct = null;
						String pkgProductType = null;
						StringBuilder otherAddonErrorMessage = new StringBuilder();
						if (commerceItems !=null && commerceItems.length > 1) {
							for (CricketCommerceItemImpl pkgCommerceItem : commerceItems) {
								  pkgProduct = (RepositoryItem) pkgCommerceItem.getAuxiliaryData().getProductRef();
								  pkgProductType = (String) pkgProduct.getPropertyValue(TYPE);
								if (pkgProductType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)) {
									if(otherAddonsInGroup != null && otherAddonsInGroup.size() > 0 && otherAddonsInGroup.contains(pkgProduct)) {
										   otherAddonErrorMessage.append(OTHER_ERROR_MESSAGE);
										for(RepositoryItem otherAddonInGroup : otherAddonsInGroup) {
											otherAddonErrorMessage = otherAddonErrorMessage.append(" ").append(otherAddonInGroup.getPropertyValue(DISPLAY_NAME));
										}
										otherAddonErrorMessage = otherAddonErrorMessage.append(" and ").append(pkgProduct.getPropertyValue(DISPLAY_NAME));
										addFormException(new DropletException(otherAddonErrorMessage.toString()));
										setReloadDrawerCartDiv(Boolean.TRUE);
										return;
									}
								}
							}	
						}
						
						if(dependantAddOns != null && dependantAddOns.size() > 0) {
							List<RepositoryItem> addOnsInPackage = new ArrayList<RepositoryItem>();
							if(commerceItems != null && commerceItems.length > 0){
								for(CricketCommerceItemImpl pkgCommerceItem : commerceItems) {
									  pkgProduct = (RepositoryItem) pkgCommerceItem.getAuxiliaryData().getProductRef();
									  pkgProductType = (String) pkgProduct.getPropertyValue(TYPE);
									if (pkgProductType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)) {
										addOnsInPackage.add(pkgProduct);
									}
								}
							}
							StringBuilder dependantAddonErrorMessage = new StringBuilder();
							dependantAddonErrorMessage.append(DEPENDANT_ADDONS_ERROR_MESSAGE);
							
								for(RepositoryItem dependantAddOn : dependantAddOns) {	
									if(addOnsInPackage != null && addOnsInPackage.isEmpty()){
										dependantAddonErrorMessage = dependantAddonErrorMessage.append(" ").append(dependantAddOn.getPropertyValue(DISPLAY_NAME));
										isDependantAddonsPresent = false;
									} else if (addOnsInPackage != null && addOnsInPackage.size() > 0 ){
										if(!addOnsInPackage.contains(dependantAddOn)){
											dependantAddonErrorMessage = dependantAddonErrorMessage.append(" ").append(dependantAddOn.getPropertyValue(DISPLAY_NAME));
											isDependantAddonsPresent = false;
										}
									}
									
								}
							if(!isDependantAddonsPresent) {
								addFormException(new DropletException(dependantAddonErrorMessage.toString()));
								setReloadDrawerCartDiv(Boolean.TRUE);
								return;
							}
						}
					}
				}
				}
				// End check for addon rules before adding to cart
				
				if(pRequest.getParameter(DYNAMIC_ADDON) != null) {
					setDynamicAddOn(Boolean.parseBoolean(pRequest.getParameter(DYNAMIC_ADDON)));
				} else {
					setDynamicAddOn(false);
				}
				if(!isDynamicAddOn()){
					order = (CricketOrderImpl) getOrder();
					CricketOrderTools tools = (CricketOrderTools) getOrderManager().getOrderTools();
					List<CricketPackage> existingPackagesForAddOn = order.getCktPackages();
					if(existingPackagesForAddOn != null && existingPackagesForAddOn.size() > 0){
						boolean compatiblePackageFound = false;
						for (CricketPackage existingpackageForAddOn : existingPackagesForAddOn) {
							String planCode = null;
							String modelNumber = null;
							RepositoryItem pkgProduct = null;
							String pkgProductType = null;
							RepositoryItem pkgPhoneSKU = null;
							CricketCommerceItemImpl[] commerceItems = (CricketCommerceItemImpl[]) tools.getCommerceItemsForPackage(existingpackageForAddOn, order);
							if (commerceItems !=null && commerceItems.length > 1) {
								for (CricketCommerceItemImpl pkgCommerceItem : commerceItems) {
									  pkgProduct = (RepositoryItem) pkgCommerceItem.getAuxiliaryData().getProductRef();
									  pkgProductType = (String) pkgProduct.getPropertyValue(TYPE);
									if (pkgProductType.equalsIgnoreCase(PLAN_PRODUCT)) {
										planCode = pkgProduct.getRepositoryId();
									}
									if (pkgProductType.equalsIgnoreCase(PHONE_PRODUCT)) {
										  pkgPhoneSKU = (RepositoryItem) pkgCommerceItem.getAuxiliaryData().getCatalogRef();
										if(pkgPhoneSKU != null) {
											modelNumber = (String)pkgPhoneSKU.getPropertyValue(PROP_MODEL_NUMBER);
										}
									}
								}
							}
							String marketCode = (String)getProfile().getPropertyValue(PROP_MARKET_ID);
							if(planCode != null && modelNumber != null && marketCode != null) {
								Map<String, List<InquireFeaturesResponseVO>> planAddOnsMap = getDisplayFeaturesManager().getCompatibleAddons(
																								modelNumber, PHONE_TYPE, planCode, marketCode,
																									ADD_ADDON_TRANS_NAME, (ArrayList) getCricketProfile().getUserPurchasedOfferingProducts(),false, true, CricketESPConstants.CKT_SALES_CHANNEL_NAME,getOrder().getId());
								if (null != planAddOnsMap) {
								List<InquireFeaturesResponseVO> optionalAddOns= planAddOnsMap.get(OPTIONAL_ADDONS);
								
								InquireFeaturesResponseVO activationFeeFeature = getCricketESPAdapterHelper().getActivationFeeAddOn(optionalAddOns);
								if(activationFeeFeature != null && activationFeeFeature.getPrice() != null) {
						    		profile.setPropertyValue(ACTIVATION_FEE, activationFeeFeature.getPrice().doubleValue());
						    	}
								
								
								InquireFeaturesResponseVO administrationFeeFeature = getCricketESPAdapterHelper().getAdministrationFeeAddOn(optionalAddOns);					    									
								if(optionalAddOns != null && optionalAddOns.size() > 0){
									for(InquireFeaturesResponseVO optionalAddOn : optionalAddOns) {
										
										String currentAddOnOfferId = null;
										if(addonProduct != null) {
											currentAddOnOfferId = (String)(addonProduct.getPropertyValue(PROP_OFFER_ID));
										}								
										if(optionalAddOn.getId() != null && currentAddOnOfferId != null && optionalAddOn.getId().equalsIgnoreCase(currentAddOnOfferId)) {
											setFormPackageId(existingpackageForAddOn.getId());
									    	Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersMap = null;
									    	//List<InquireFeaturesResponseVO> mandatoryAddOns = planAddOnsMap.get("allIncludedAddOns");
									    	 List<InquireFeaturesResponseVO> mandatoryAddOns = planAddOnsMap.get(MANDATORY_ADDONS);
	
									    	mandatoryOffersMap = getUserSessionBean().getMandatoryOffersMap();
									    	if (mandatoryOffersMap == null) {
									    		mandatoryOffersMap = new HashMap<String, List<InquireFeaturesResponseVO>>();
									    	}
									    	if(existingpackageForAddOn.getId() != null && mandatoryAddOns != null && mandatoryAddOns.size() > 0) {
									    		if(administrationFeeFeature != null){
									    			//Administration Fee addOn needs to be sent to billing system for charging the user on monthly basis
									    			//Hence adding to mandatoryAddOns list which will be used in CreateActivationQuote request for building CricketOfferingCodes
									    			mandatoryAddOns.add(administrationFeeFeature);
									    		}
										    	mandatoryOffersMap.put(existingpackageForAddOn.getId(), mandatoryAddOns);	    		
									    	}
									    	getUserSessionBean().setMandatoryOffersMap(mandatoryOffersMap);
											compatiblePackageFound = true;
											break;												
										}
									}
								}
								List<InquireFeaturesResponseVO>  hppAddons = planAddOnsMap.get(HPP_ADDONS);
						    	if (null != hppAddons && !hppAddons.isEmpty()) {
						    		getUserSessionBean().setHppAddOns(hppAddons);
						    	}
								if (compatiblePackageFound) {
									break;
								} 
							}
							}
							
						}
						if(!compatiblePackageFound) {
								setReloadDrawerCartDiv(Boolean.TRUE);
								addFormException(new DropletException("This plan add on is not compatible with the phone and plan added to cart, please change the phone or plan to add the selected plan addon to cart"));
						}
					}							
				}
			}
		}
		//check for duplicate add on to same package
		if(productType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT) && !StringUtils.isEmpty(getFormPackageId())){
			List<CricketCommerceItemImpl> commerceItemswithUniquePackageId = cricketOrderTools.getCommerceItemswithUniquePackageId(getFormPackageId(), (CricketOrderImpl) getOrder());
			//CommerceItem commItem = null;
		if(commerceItemswithUniquePackageId != null){
			for(CricketCommerceItemImpl commItemInPackage : commerceItemswithUniquePackageId){
				//try {
					if(commItemInPackage != null && commItemInPackage.getCatalogRefId().equalsIgnoreCase(getCatalogRefIds()[0])){
						setReloadDrawerCartDiv(Boolean.TRUE);
						addFormException(new DropletException("This recurring monthly plan add-on can only be added once per phone package."));
					}
				/*} catch (CommerceItemNotFoundException e) {
					if(isLoggingError()){
						vlogError("[CommerceItemNotFoundException in preAddItemToOrder:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
					}
				} catch (InvalidParameterException e) {
					if(isLoggingError()){
						vlogError("[InvalidParameterException in preAddItemToOrder:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
					}
				}*/
			}
		}
		}
		String prdType = findProductType(getProductId()); 
		if((!prdType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT) && !prdType.equalsIgnoreCase(ACCESSORY_PRODUCT)) && !getCartCookie().isEditActionFromCart() && null == getFormPackageId()
				&& !checkCompatiblePackagesInOrder()){
				//check no.of package for PIA and PayGo customer
			boolean pkgSizeForCust = numberOfPackage((String)getProfile().getPropertyValue(NETWORK_PROVIDER),pRequest, pResponse);			
		}
		
		if (isLoggingDebug()) {				
	    		logDebug("EXITING from CricketCartModifierFormHandler class of preAddItemToOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	/**
	 * @param pRequest
	 * @param pResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addPayGoCustomerErrors(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		String custTypeName = getCricketProfile().getCustTypeName();
		String customerType = getCricketProfile().getCustomerType();
		String userIntention = getUpgradeItemDetailsSessionBean().getUserIntention();		
		
		if ((!StringUtils.isEmpty(customerType) && PAYGO_CUSTOMER_TYPE.equalsIgnoreCase(customerType)) 
				|| (!StringUtils.isEmpty(custTypeName) && custTypeName.equalsIgnoreCase(PAYGO_CUST_NAME))) {
			if (userIntention != null && userIntention.equalsIgnoreCase(getCartConfiguration().getUpgradePhoneIntention())) {
				String msg = formatUserMessage(PAYGO_UPGRADE_PHONE_ERROR, pRequest,pResponse);
				String propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
				setModalError(Boolean.TRUE);
				addFormException(new DropletFormException(msg,null, propertyPath, PAYGO_UPGRADE_PHONE_ERROR));
			} else if (userIntention != null && userIntention.equalsIgnoreCase(getCartConfiguration().getUpgradePlanIntention())) {
				String msg = formatUserMessage(PAYGO_UPGRADE_PLAN_ERROR, pRequest,pResponse);
				String propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
				setModalError(Boolean.TRUE);
				addFormException(new DropletFormException(msg,null, propertyPath, PAYGO_UPGRADE_PLAN_ERROR));
			} else if (userIntention != null && userIntention.equalsIgnoreCase(getCartConfiguration().getAddLineIntention())) {
				String msg = formatUserMessage(PAYGO_ADD_LINE_ERROR, pRequest,pResponse);
				String propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
				setModalError(Boolean.TRUE);
				addFormException(new DropletFormException(msg,null, propertyPath, PAYGO_ADD_LINE_ERROR));
			} 
		}
		if(!StringUtils.isEmpty(customerType) && customerType.equalsIgnoreCase(HARGRAY_CUSTOMER_TYPE) && !StringUtils.isEmpty(userIntention) 
				&& !userIntention.equalsIgnoreCase(getCartConfiguration().getAddLineIntention())){
            String msg = formatUserMessage(HARGRAY_ADD_LINE_ERROR, pRequest,pResponse);
            String propertyPath = generatePropertyPath(ORDER_PAYGO);
            setModalError(Boolean.TRUE);
            addFormException(new DropletFormException(msg,null, propertyPath, HARGRAY_ADD_LINE_ERROR));           
		}
		 return;
	}
	
	/**
	 * 
	 */
	private void setAddressFieldstoEmpty() {
		
		getAccountHolderAddressData().getAccountAddress().setAddress1(EMPTY_STRING);
		getAccountHolderAddressData().getAccountAddress().setFirstName(null);
		getAccountHolderAddressData().getAccountAddress().setMiddleName(EMPTY_STRING);
		getAccountHolderAddressData().getAccountAddress().setLastName(null);
		getAccountHolderAddressData().getAccountAddress().setCompanyName(EMPTY_STRING);
		getAccountHolderAddressData().getAccountAddress().setAddress2(EMPTY_STRING);
		getAccountHolderAddressData().getAccountAddress().setEmail(EMPTY_STRING);
		getAccountHolderAddressData().getAccountAddress().setPhoneNumber(null);
		getAccountHolderAddressData().setMonth(EMPTY_STRING);
		getAccountHolderAddressData().setDay(EMPTY_STRING);
		getAccountHolderAddressData().setYear(EMPTY_STRING);
		
		getShippingAddressData().getShippingAddress().setAddress1(EMPTY_STRING);
		getShippingAddressData().getShippingAddress().setAddress2(EMPTY_STRING);
		getShippingAddressData().getShippingAddress().setCompanyName(EMPTY_STRING);
		getShippingAddressData().getShippingAddress().setFirstName(EMPTY_STRING);
		getShippingAddressData().getShippingAddress().setMiddleName(EMPTY_STRING);;
		getShippingAddressData().getShippingAddress().setLastName(EMPTY_STRING);;
		getShippingAddressData().getShippingAddress().setPhoneNumber(EMPTY_STRING);;
		getShippingAddressData().setBillingAddressAsAccountHolderAddress(false);
		getShippingAddressData().setBillingAddressAsShippingAddress(false);
		getShippingAddressData().setShippingAddressAsAccountHolderAddress(false);
		
		getBillingAddressData().getBillingAddress().setAddress1(EMPTY_STRING);
		getBillingAddressData().getBillingAddress().setAddress2(EMPTY_STRING);
		getBillingAddressData().getBillingAddress().setCompanyName(EMPTY_STRING);
		getBillingAddressData().getBillingAddress().setFirstName(EMPTY_STRING);;
		getBillingAddressData().getBillingAddress().setMiddleName(EMPTY_STRING);;
		getBillingAddressData().getBillingAddress().setLastName(EMPTY_STRING);;
		getBillingAddressData().getBillingAddress().setPhoneNumber(EMPTY_STRING);;
	}

	/**
	 * @param pRequest
	 * @param upgradeItemDetailsSessionBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setSessionDataFromCookie(DynamoHttpServletRequest pRequest,
			UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean) {
		
		// Getting the Page url
		String hppAddonKey = null;
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		// Getting the Order Id
		 CricketOrderImpl order = (CricketOrderImpl) getOrder();
		String orderId = CricketCommonConstants.EMPTY_STRING;
   		if(null != order){
   			if(!StringUtils.isBlank( order.getId())){
	    			orderId = order.getId();
				}
   		}
		if (isLoggingDebug()) {
			
			 			
	    		logDebug("Entering into CricketCartModifierFormHandler class of setSessionDataFromCookie() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		String addonProductId = null;
		String addonSkuId = null;
		String phone_skuId = null;
		Set<RepositoryItem> parentProducts = new HashSet<RepositoryItem>();
		String parentProduct = null;
		String planSkuId=null;
		List<String> addonSkuIds = new ArrayList<String>();
		List<String> addonProductIds = new ArrayList<String>();
		Map<String, String> removedAddons = new HashMap<String, String>();
		UserAccountInformation userAccountInfo = getUserAccountInformation();
			if (!StringUtils.isEmpty(cricketProfile.getDeviceModel())) {
					try {
						RepositoryView reposView = getCatalogRepository().getView(PHONE_SKU);
						RqlStatement statement = RqlStatement.parseRqlStatement("modelNumber=?0");
						Object[] inputParams = new Object[1];
						inputParams[0] = cricketProfile.getDeviceModel();
						RepositoryItem[] phone_skus = statement.executeQuery(reposView, inputParams);
						if (null != phone_skus && phone_skus.length > 0) {
							RepositoryItem repositoryItem = null;
							RepositoryItem rItem = null;
						for (int j = 0; j < phone_skus.length; j++) {
							repositoryItem = phone_skus[j];
							phone_skuId=(String)repositoryItem.getPropertyValue(REPOSITORY_ID);
							parentProducts=(Set)repositoryItem.getPropertyValue(PROP_PARENT_PRODUCTS);
							for (Iterator iterator = parentProducts.iterator(); iterator.hasNext();) {
								rItem = (RepositoryItem) iterator.next();
								parentProduct = rItem.getRepositoryId();
								break;
							}
						}
						}
					} catch (RepositoryException e) {      
						if(isLoggingError()){
							vlogError("Repository Exception in setSessionDataFromCookie of CricketOrderTools:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
						}
					}
					upgradeItemDetailsSessionBean.setRemovedPhoneId(parentProduct);
					upgradeItemDetailsSessionBean.setRemovedPhoneSkuId(phone_skuId);
					upgradeItemDetailsSessionBean.setModelNumber(cricketProfile.getDeviceModel());
				}

				if (!StringUtils.isEmpty(cricketProfile.getRatePlanCode())) {
					try { 
						RepositoryItem planProductItem = getCatalogRepository().getItem(cricketProfile.getRatePlanCode(),PLAN_PRODUCT);
						if (null != planProductItem) {
							List<RepositoryItem> plan_skus = (List<RepositoryItem>) planProductItem.getPropertyValue(PROP_CHILD_SKUS);
						if (null != plan_skus && !plan_skus.isEmpty()) {
							for (RepositoryItem repositoryItem : plan_skus) {
								planSkuId = repositoryItem.getRepositoryId();
							}
						 }
						}
					} catch (RepositoryException e) {      
						if(isLoggingError()){
							vlogError("Repository Exception in getCommerceItemsForPackage of UserIntentionServlet::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
						}
					}
					if(cricketProfile.getRatePlanCode().equalsIgnoreCase(CricketCommonConstants._4P5)){
						upgradeItemDetailsSessionBean.setPlan4p5(true);
					}
					upgradeItemDetailsSessionBean.setRemovedPlanId(cricketProfile.getRatePlanCode());
					upgradeItemDetailsSessionBean.setRemovedPlanSkuId(planSkuId);
				}
				
				if (!StringUtils.isEmpty(cricketProfile.getMdn())) {
					//Fixing Null Pointer issue 
					if(isLoggingDebug()){
						logDebug("UserAccountInfo:::"+userAccountInfo);
						logDebug("Subscribers:::"+userAccountInfo.getSubscribers());
					}
					if(userAccountInfo!=null && userAccountInfo.getSubscribers()!=null) {
						
					List<SubscriberVO> subscribers = userAccountInfo.getSubscribers();
					if (null != subscribers && !subscribers.isEmpty()) {
						List<OfferingsVO> additionalOfferings = new ArrayList<OfferingsVO>();
						RepositoryItem addOnProductItem = null;
						List<RepositoryItem> addon_skus = new ArrayList<RepositoryItem>();
					for (SubscriberVO subscriberVO : subscribers) {
						if(cricketProfile.getMdn().equalsIgnoreCase(subscriberVO.getMdn())) {
							  additionalOfferings = subscriberVO.getAdditionalOfferings();
							for (OfferingsVO offeringsVO : additionalOfferings) {
								if (null != offeringsVO && offeringsVO.getOfferTypeId() != 3 
										&& !StringUtils.isEmpty(offeringsVO.getOfferingName())
										&& !REC_OFFERING_NAME.equals(offeringsVO.getOfferingName())) {
										addonProductId = offeringsVO.getOfferingName();
										addonProductIds.add(addonProductId);
										try {
											 addOnProductItem = getCatalogRepository().getItem(addonProductId,ITEM_DESC_ADDON_PRODUCT);
											if (null != addOnProductItem) {
												if (null != addOnProductItem.getPropertyValue(CricketCommonConstants.PROP_PARC_GROUP_NAME) 
														&& addOnProductItem.getPropertyValue(CricketCommonConstants.PROP_PARC_GROUP_NAME).equals(CricketCommonConstants.HANDSET_PROTECTION_GROUP_NAME)) {
													hppAddonKey = addOnProductItem.getRepositoryId();
												}
												 addon_skus =  (List<RepositoryItem>) addOnProductItem.getPropertyValue(PROP_CHILD_SKUS);
											if (null != addon_skus && !addon_skus.isEmpty()) {
												for (RepositoryItem repositoryItem : addon_skus) {
													addonSkuId = repositoryItem.getRepositoryId();
												}
											}
											addonSkuIds.add(addonSkuId);
											removedAddons.put(addonProductId, addonSkuId);
											}
										} catch (RepositoryException e) {      
											if(isLoggingError()){
												vlogError("Repository Exception in getCommerceItemsForPackage of UserIntentionServlet::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
											}
										}
								}
							}
						}
					}
				}
			if (!addonProductIds.isEmpty()) {
				upgradeItemDetailsSessionBean.setAddonProductIds(addonProductIds);;
			}
			if (!addonSkuIds.isEmpty()) {
				upgradeItemDetailsSessionBean.setAddonSkuIds(addonSkuIds);
			}
			if (!removedAddons.isEmpty()) {
				upgradeItemDetailsSessionBean.setRemovedAddons(removedAddons);
			}
			}
			upgradeItemDetailsSessionBean.setMdn(cricketProfile.getMdn());
		}
				
			if (isLoggingDebug()) {							
		    		logDebug("Exiting from CricketCartModifierFormHandler class of setSessionDataFromCookie() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
	}
	//	returns the no of packages currently available in cart.
	/**
	 * @param networkType
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean numberOfPackage(String networkType, DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException{
		
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
			 CricketOrderImpl order = (CricketOrderImpl) getOrder();
			 String orderId = CricketCommonConstants.EMPTY_STRING;
	    		if(null != order){
	    			if(!StringUtils.isBlank( order.getId())){
		    			orderId = order.getId();
					}
	    		}			
	    		logDebug("Entering into CricketCartModifierFormHandler class of numberOfPackage() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		boolean returnValue=false;
		String msg = null;
		String propertyPath = null;
		if(isLoggingDebug()){
			logDebug("Number id packages in Order"+((CricketOrderImpl) getOrder()).getId()+"   is    "+ ((CricketOrderImpl) getOrder()).getCktPackages().size());
		}
		if(null != networkType){
			int pkgSize = ((CricketOrderImpl) getOrder()).getCktPackages().size();
			if(pkgSize >= getMaxNoOfPackageForPayGo() && null != networkType && networkType.equalsIgnoreCase(NETWORKTYPE_SPRINT)){
//				returnValue = CUSTTYPE_PAYGO;
				returnValue=true;
				msg = formatUserMessage(PAYGO_PACKAGE_LIMIT, pRequest,pResponse);
				propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
				addFormException(new DropletFormException(msg, null, propertyPath,PAYGO_PACKAGE_LIMIT)); 
			}
			else if(pkgSize >= getMaxNoOfPackageForPIA() && null != networkType && networkType.equalsIgnoreCase(NETWORKTYPE_CRICKET)){
//				returnValue = CUSTTYPE_PIA;
				returnValue=true;
				msg = formatUserMessage(PIA_PACKAGE_LIMIT, pRequest,pResponse);
				propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
				addFormException(new DropletFormException(msg, null, propertyPath,PIA_PACKAGE_LIMIT));
			}			
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
			 CricketOrderImpl order = (CricketOrderImpl) getOrder();
			 String orderId = CricketCommonConstants.EMPTY_STRING;
	    		if(null != order){
	    			if(!StringUtils.isBlank( order.getId())){
		    			orderId = order.getId();
					}
	    		}			
	    		logDebug("Exiting from CricketCartModifierFormHandler class of numberOfPackage() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return returnValue;		
	}

	public boolean businessRuleValidator() {
		

	/*	String profile_acc_type = "PayGo"; // getProfile.getAccountTupe();
		boolean profile_upg_phone = true; // getProfile.getUpgradePhone();
		boolean profile_upg_plan = true; // getProfile.getUpgradePlan();
		String profile_plan_type = "PIA AIM"; // getProfile.getPlanType();
		String profile_plan_code = "4P5"; // getProfile.getAccountTupe();
		if (profile_acc_type.equalsIgnoreCase("PayGo")
				&& (profile_upg_phone || profile_upg_plan))
			return true;
		else if (profile_plan_type.equalsIgnoreCase("PIA AIM"))
			return true;
		else if (profile_plan_code.equalsIgnoreCase("4P5") && !profile_upg_plan)
			return true;
		else if (profile_acc_type.equalsIgnoreCase("Hargray")) {
			String msg = "Cant purchase"; // getOrderManager().getOrderTools().getStoreTextMessage(id);
			addFormException(new DropletFormException(msg, null, null,
					"MSG_VALUE"));
			return false;
		} else {
			String msg = "Cant purchase"; // getOrderManager().getOrderTools().getStoreTextMessage(id);
			addFormException(new DropletFormException(msg, null, null,
					"MSG_VALUE"));
			return false;
		}*/
		return true;
	}

	/**
	 * @param productId
	 * @return
	 */
	private String findProductType(String productId) {
		String productType = null;
		try {
			// get product type from repository whether it is phone, plan and
			// accessories
			productType = (String) ((getCatalogTools().findProduct(productId))
					.getPropertyValue(TYPE));

		} catch (RepositoryException e) {
			if (isLoggingError()) {
				logError("RepositoryException" + e);
			}
		}

		return productType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * atg.commerce.order.purchase.CartModifierFormHandler#handleAddItemToOrder
	 * (atg.servlet.DynamoHttpServletRequest,
	 * atg.servlet.DynamoHttpServletResponse)
	 */
	@Override
	public boolean handleAddItemToOrder(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		
		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}

	 // Getting the Order Id
		String orderId = CricketCommonConstants.EMPTY_STRING;
		if(null != getOrder()){
			if(!StringUtils.isBlank(getOrder().getId())){
				orderId = getOrder().getId();
			}
		}
		if (isLoggingDebug()) {								
	    		logDebug("Entering into CricketCartModifierFormHandler class of handleAddItemToOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		if(!getProfile().isTransient() && Integer.valueOf(getCricketProfile().getNumberOfLines()) > 1 
				&& !StringUtils.isEmpty(pRequest.getParameter(UPGRADE_FLOW)) && StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getUserIntention())){
			setValueInCookie(pRequest, pResponse);
			return checkFormRedirect(getAddItemToOrderSuccessURL(),
					getAddItemToOrderErrorURL(), pRequest, pResponse);
		}
		setFormPackageId(pRequest.getParameter(FORM_PACKAGE_ID));
		setCurrentURL(pRequest.getParameter(CURRENT_URL));		
		RepeatingRequestMonitor rrm = getRepeatingRequestMonitor();
		this.getFormExceptions().clear();
		if (isAddAsRemovedCommerceItem()) {
			if (StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getUserIntention())) {
				getUpgradeItemDetailsSessionBean().setUserIntention(getCartConfiguration().getUpgradeAddonIntention());
			} 			
			setSessionDataFromCookie(pRequest, getUpgradeItemDetailsSessionBean());
		}
		
		if(null != getFormPackageId()){
			String packageValue = PACKAGE_ID_PARAM+getFormPackageId();
			if(null!=getCurrentURL() && getCurrentURL().contains(packageValue) && !findProductType(getProductId()).equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)){
				String url = getCurrentURL().replace(packageValue, EMPTY_STRING);
				setRedirectURL(url);
				setCartOpen(true);
			}
		}
		if ((rrm == null) || (rrm.isUniqueRequestEntry(MY_HANDLER_METHOD))) {
			Transaction tr = null;
			try {
				tr = ensureTransaction();
				if (tr == null ) {
					tr = getTransactionManager().getTransaction();
				}
				setModalError(Boolean.FALSE);
				setReloadDrawerCartDiv(Boolean.FALSE);
				synchronized (getOrder()) {					
					String setUserIntention = pRequest.getParameter(SET_USERINTENTION);
					if(setUserIntention == null || setUserIntention.length() ==0){
						 if(getUpgradeItemDetailsSessionBean() != null && getUpgradeItemDetailsSessionBean().getUserIntention() != null){						
							 setUserIntention = getUpgradeItemDetailsSessionBean().getUserIntention();
						 }
					}
					if (null != setUserIntention && !StringUtils.isEmpty(setUserIntention) && !setUserIntention.equalsIgnoreCase(DEFAULT)) {
						getUpgradeItemDetailsSessionBean().setUserIntention(setUserIntention);
						setSessionDataFromCookie(pRequest, getUpgradeItemDetailsSessionBean());
					}
					String removeItemBeforeAdding = pRequest.getParameter(REMOVE_ITEM_BEFORE_ADDING);
					if (!StringUtils.isEmpty(removeItemBeforeAdding) && removeItemBeforeAdding.equalsIgnoreCase(TRUE)) {
							clearShoppingCart();
						}
					
					String clearUserIntention = pRequest.getParameter(CLEAR_USER_INTENTION);
					if (!StringUtils.isEmpty(clearUserIntention) && clearUserIntention.equalsIgnoreCase(TRUE)) {
						clearUpgradeItemSessionData();
					}
					
					if (!StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getUserIntention())) {
						checkUserIntention(pRequest, pResponse);
					}
					if (!StringUtils.isEmpty(getAddItemToOrderErrorURL()) && getAddItemToOrderErrorURL().contains(_REQUESTID)) {
						String[] requestURL = getAddItemToOrderErrorURL().split(_REQUESTID);
						if (null != requestURL && requestURL.length > 0 
								&& !StringUtils.isEmpty(requestURL[0])) {
							setAddItemToOrderErrorURL(requestURL[0]);
						}
						
					}
					//Check if add to cart of add on applicable
					if(null == getFormPackageId() && findProductType(getProductId()).equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)
							&& !getCartConfiguration().getUpgradeAddonIntention().equalsIgnoreCase(getUpgradeItemDetailsSessionBean().getUserIntention())
							&& !getCartConfiguration().getUpgradePlanIntention().equalsIgnoreCase(getUpgradeItemDetailsSessionBean().getUserIntention())
							&& !isAddAsRemovedCommerceItem()){						
						checkAddOnToCart(pRequest, pResponse);
					}
					
					if (getFormError()) {
						return	checkFormRedirect(getAddItemToOrderSuccessURL(),
								getAddItemToOrderErrorURL(), pRequest,
								pResponse);
					 }
					
					preAddItemToOrder(pRequest, pResponse);
					if (getFormError()) {
						pResponse.sendRedirect(getAddItemToOrderErrorURL());
						return checkFormRedirect(getAddItemToOrderSuccessURL(),
								getAddItemToOrderErrorURL(), pRequest, pResponse);
					}
					/*if (!StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getUserIntention())
						&& StringUtils.isEmpty(getUpgradeItemFlow())) {
						clearShoppingCart();
					} */
					if(!getFormError()){
						addItemToOrder(pRequest, pResponse);
					}
					if (!checkFormRedirect(null, getAddItemToOrderErrorURL(),
							pRequest, pResponse)) {
						if (getOrder().isTransient()) {
							getOrderManager().addOrder(getOrder());
						} else {
							getOrderManager().updateOrder(getOrder());
						}
						return checkFormRedirect(getAddItemToOrderSuccessURL(),
								getAddItemToOrderErrorURL(), pRequest, pResponse);
					}
					postAddItemToOrder(pRequest, pResponse);
					if (getOrder().isTransient()) {
						getOrderManager().addOrder(getOrder());
					} else {
						getOrderManager().updateOrder(getOrder());
					}
				}
				runProcessRepriceOrder(getOrder(), getUserPricingModels(), getUserLocale(), getProfile(), createRepriceParameterMap());
			} catch (CommerceException commerceException) {
				vlogError("CommerceException Occured while updating order with ID :" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, commerceException);
			} catch (Exception exception) {
				vlogError("Exception Occured while updating order with ID :" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, exception);
			} finally {
				if (tr != null) {
					commitTransaction(tr);
				}
				if (rrm != null) {
					rrm.removeRequestEntry(MY_HANDLER_METHOD);
				}
			}
		}
		if (isLoggingDebug()) {								
	    		logDebug("Exitinf from CricketCartModifierFormHandler class of handleAddItemToOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return checkFormRedirect(getAddItemToOrderSuccessURL(),
				getAddItemToOrderErrorURL(), pRequest, pResponse);
	}

	/**
	 * Updating the cricket cookie
	 * @param pRequest
	 * @param pResponse
	 * @param productId
	 */
	private void setValueInCookie(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse) throws UnsupportedEncodingException{
		Cookie[] cookies = pRequest.getCookies();
		Cookie cookie1 = null;
		for(int i = 0; i < cookies.length; i++) { 
              cookie1 = cookies[i];
              if(cookie1.getName().contentEquals(CricketCookieConstants.CRICKET)){
    			String numOfLOS = getCricketProfile().getNumberOfLines();
    			String upgradeFlow = pRequest.getParameter(UPGRADE_FLOW);
    			if(Integer.valueOf(numOfLOS) > 1 && StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getUserIntention())
    					){
    				CricketUtils cricketUtils = (CricketUtils) pRequest.resolveName(getCricketUtils());
    				Mixed decriptedLocationInfo = cricketUtils.getDecriptedCookieInfo(cookie1, pRequest);
    				String arrayVal = null;
    				String decodedArray[] = { decriptedLocationInfo.toString() };
    				for (int j = 0; j < decodedArray.length; j++) {
    					arrayVal = decodedArray[j];
    				}
    				if(arrayVal != null){
    					Map<String, String> cricketCookieMap = cricketUtils.getCookieMap(arrayVal);
    					cricketCookieMap.put("selectLOS", TRUE);
    					cricketCookieMap.put(INTENTION, upgradeFlow);
    					GeoIpLocationTools geoIpTools = (GeoIpLocationTools) pRequest.resolveName(getGeoIPLocationTools());
    					String encodedCookieValue = URLEncoder.encode(geoIpTools.encrypt(cricketCookieMap), CricketCookieConstants.UTF_8);
    					cookie1.setValue(encodedCookieValue);
    					cookie1.setPath(SEPARTOR);
    					cookie1.setMaxAge(-1);
    					cookie1.setDomain(".mycricket.com");
    					pResponse.addCookie(cookie1);       
    					Map<String, String> homePageLinks = getCricketConfiguration().getHomePageLinks();
    					setRedirectURL(homePageLinks.get(MYACCOUNT_URL));
    				}
    			}
    		
            	break;
            }
        }
	}
	
	
	/**
	 * @param pRequest
	 * @param pResponse
	 * @param productId
	 * @param skuId
	 * @param quantityRequired
	 */
	private void checkSkuStockLevel(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse, String productId,
			String skuId, long quantityRequired) {
		
		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
		if (isLoggingDebug()) {									
	    		logDebug("Entering into CricketCartModifierFormHandler class of checkSkuStockLevel() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		if (productId == null) {
			if (isLoggingDebug()) {
				logDebug("Cannot determine if sku " + skuId
						+ " is available as productId is null");
			}
			return;
		}

		if ((skuId == null) || (StringUtils.isEmpty(skuId.trim()))) {
			if (isLoggingDebug()) {
				logDebug("The skuId is null or an empty string");
			}
			return;
		}
		try {
			StoreInventoryManager invManager = getInventoryManager();

			Object productParam = invManager.getCatalogRefRepository().getItem(
					productId, PRODUCT);

			if ((productParam == null)
					|| (!(productParam instanceof RepositoryItem))) {
				if (isLoggingError()) {
					logError("Cannot get the product from the repository. Product is "
							+ productParam);
				}

				return;
			}

			int availability = invManager.queryAvailabilityStatus(
					(RepositoryItem) productParam, skuId);

			long stockLevel = invManager.queryStockLevel(skuId);
			long backOrderLevel = invManager.queryBackorderLevel(skuId);

			if (isLoggingDebug()) {
				logDebug("Quantity =" + quantityRequired + ";Stock Level ="
						+ stockLevel + "; backOrderLevel=" + backOrderLevel);
			}
			if (availability == invManager
					.getAvailabilityStatusBackorderableValue()) {
				if ((backOrderLevel != -1L)
						&& (quantityRequired > backOrderLevel)) {
					String productDisplayName = (String) ((RepositoryItem) productParam)
							.getPropertyValue(PROP_DISP_NAME);

					addFormException(new DropletFormException(
							formatUserMessage(ITEM_QUANTITY_MORE_THAN_MAX,
									productDisplayName,
									Long.valueOf(backOrderLevel), pRequest,
									pResponse), (String) null,
									ITEM_QUANTITY_MORE_THAN_MAX));
				}

			}

			if (availability == invManager.getAvailabilityStatusInStockValue()) {
				if ((stockLevel != -1L)
						&& (quantityRequired > stockLevel)) {
					String productDisplayName = (String) ((RepositoryItem) productParam)
							.getPropertyValue(PROP_DISP_NAME);

					addFormException(new DropletFormException(
							formatUserMessage(ITEM_QUANTITY_MORE_THAN_MAX,
									productDisplayName,
									Long.valueOf(stockLevel), pRequest,
									pResponse), (String) null,
									ITEM_QUANTITY_MORE_THAN_MAX));
				}
			}
		} catch (InventoryException ex) {
			vlogError("InventoryException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, ex);
			getInventoryManager().logInventoryException(ex);
		} catch (Exception e) {
			if (isLoggingError())
				vlogError("Error determining if sku was in stock.:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
		}
		if (isLoggingDebug()) {									
	    		logDebug("Exiting from CricketCartModifierFormHandler class of checkSkuStockLevel() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
	/**
	 * @param pRequest
	 * @param pResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	
	private void checkAddOnToCart(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		setAllPlanUrl(pRequest.getParameter(ALL_PLAN_URL));
		setAllPhoneUrl(pRequest.getParameter(ALL_PHONE_URL));
		CricketOrderImpl order = (CricketOrderImpl) getOrder();

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}
			}
		if (isLoggingDebug()) {
								
	    		logDebug("Entering into CricketCartModifierFormHandler class of checkAddOnToCart() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		List<CricketPackage> existingPackages = order.getCktPackages();		
		String msg = null;
		String propertyPath = null;
		RepositoryItem pkgProduct = null;
		String pkgProductType = null;
		
		if ((null != existingPackages && existingPackages.size()>0)) {
			CricketOrderTools tools = (CricketOrderTools) getOrderManager().getOrderTools();
			
			for (CricketPackage existingpackage : existingPackages) {				
				CricketCommerceItemImpl[] commerceItems = (CricketCommerceItemImpl[]) tools.getCommerceItemsForPackage(existingpackage, order);
				
				if(commerceItems.length == 1){
					  pkgProduct = (RepositoryItem) commerceItems[0].getAuxiliaryData().getProductRef();
					  pkgProductType = (String) pkgProduct.getPropertyValue(TYPE);
					
					if (pkgProductType.equalsIgnoreCase(PLAN_PRODUCT)){						
						msg = formatUserMessage(NO_PHONE_INPACKAGE, pRequest,pResponse);
						propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
						addFormException(new DropletFormException(msg, null, propertyPath,NO_PHONE_INPACKAGE));
						setRedirectURL(getAllPhoneUrl());
						getCartCookie().setAddAddonWithoutPackageError(Boolean.TRUE);
						break;						
					}						
					else if (pkgProductType.equalsIgnoreCase(PHONE_PRODUCT)){						
						msg = formatUserMessage(NO_PLAN_INPACKAGE, pRequest,pResponse);
						propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
						addFormException(new DropletFormException(msg, null, propertyPath,NO_PLAN_INPACKAGE));
						setRedirectURL(getAllPlanUrl());
						getCartCookie().setAddAddonWithoutPackageError(Boolean.TRUE);
						break;						
					}						
				}
			}
		}
		else{
			msg = formatUserMessage("noPhoneInPackage", pRequest,pResponse);
			propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
			addFormException(new DropletFormException(msg, null, propertyPath,"noPhoneInPackage"));
			setRedirectURL(getAllPhoneUrl());
			getCartCookie().setAddAddonWithoutPackageError(Boolean.TRUE);
		}
		if (isLoggingDebug()) {					
	    		logDebug("Exiting from CricketCartModifierFormHandler class of checkAddOnToCart() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}

	/**
	 * @param pRequest
	 * @param pResponse
	 * @throws ServletException
	 * @throws IOException
	 * @throws CommerceException
	 * @throws RepositoryException
	 */
	private void checkUserIntention(
			DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException, IOException, CommerceException, RepositoryException {

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
			
		if (isLoggingDebug()) {				
	    		logDebug("Entering into CricketCartModifierFormHandler class of checkUserIntention() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		String productType = findProductType(getProductId());
		Profile profile = getProfileServices().getCurrentProfile();
		if (!StringUtils.isEmpty(productType)) {
			String msg = null;
			String propertyPath = null;
			setUpgradeItemFlow(pRequest.getParameter(UPGRADE_ITEM_FLOW));
			if (getUpgradeItemDetailsSessionBean().getUserIntention().equalsIgnoreCase(getCartConfiguration().getUpgradePhoneIntention())) {
				//Upgrade Phone User Intention
				if (!(productType.equalsIgnoreCase(PHONE_PRODUCT) || productType.equalsIgnoreCase(ACCESSORY_PRODUCT))) {
					setModalError(Boolean.TRUE);
					  msg = formatUserMessage(ADD_OTHER_PRODUCTS_IN_UPGRADE_PHONE_FLOW, pRequest,pResponse);
					  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
					addFormException(new DropletFormException(msg, null, propertyPath,ADD_OTHER_PRODUCTS_IN_UPGRADE_PHONE_FLOW));
				} else if ((productType.equalsIgnoreCase(PHONE_PRODUCT))) {
					List<CricketPackage> cricketPackages = ((CricketOrderImpl) getOrder()).getCktPackages();
					if (null != cricketPackages
							&& !cricketPackages.isEmpty() 
							&& cricketPackages.size() > 0) {
						  msg = formatUserMessage(COMPLETE_PACKAGE_TRANSACTION_FIRST, pRequest,pResponse);
						  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
						addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_PACKAGE_TRANSACTION_FIRST));
						setModalError(Boolean.TRUE);
					} else if (getOrder().getCommerceItemCount() > 0) {
						
						if (isUpgradePhoneItemInCart() && StringUtils.isEmpty(getUpgradeItemFlow())) {
							  msg = formatUserMessage(COMPLETE_UPGRADE_PHONE_TRANSACTION, pRequest,pResponse);
							  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
							addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_UPGRADE_PHONE_TRANSACTION));
							setModalError(Boolean.TRUE);
						}
						 else if (isChangePlanItemInCart() || isChangeAddonItemInCart()) {
							  msg = formatUserMessage(COMPLETE_PACKAGE_TRANSACTION_FIRST, pRequest,pResponse);
							  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
							addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_PACKAGE_TRANSACTION_FIRST));
							setModalError(Boolean.TRUE);
						}/* else if (isChangeAddonItemInCart()) {
							String msg = formatUserMessage(COMPLETE_PACKAGE_TRANSACTION_FIRST, pRequest,pResponse);
							String propertyPath = generatePropertyPath("order");
							addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_PACKAGE_TRANSACTION_FIRST));
						} */else if(isUpgradePhoneItemInCart() && !StringUtils.isEmpty(getUpgradeItemFlow())){
							replaceUpgradeItem(getCartConfiguration().getUpgradePhoneItemType());
						} else {
							if (!StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getRemovedPlanId()) && !getUpgradeItemDetailsSessionBean().isPlan4p5()
									&& !isPhoneCompatibleWithPlan(getProductId(), getUpgradeItemDetailsSessionBean().getRemovedPlanId())) {
								  msg = formatUserMessage(PHONE_NOTCOMPATIBLE_WITH_PLAN, pRequest,pResponse);
								  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
								addFormException(new DropletFormException(msg, null, propertyPath,PHONE_NOTCOMPATIBLE_WITH_PLAN));
								setModalError(Boolean.FALSE);
							}
							//check compatiblity with plan
						}
					}
				}
				
			} else if (getUpgradeItemDetailsSessionBean().getUserIntention().equalsIgnoreCase(getCartConfiguration().getUpgradePlanIntention())) {
				//Change Plan User Intention
				List<CricketPackage> cricketPackages = ((CricketOrderImpl) getOrder()).getCktPackages();
				if (null != cricketPackages
						&& !cricketPackages.isEmpty() 
						&& cricketPackages.size() > 0) {
					  msg = formatUserMessage(COMPLETE_PACKAGE_TRANSACTION_FIRST, pRequest,pResponse);
					  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
					addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_PACKAGE_TRANSACTION_FIRST));
					setModalError(Boolean.TRUE);
				} else {
				if (productType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT) || productType.equalsIgnoreCase(PLAN_PRODUCT)) {
					if (productType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)) {
						if (!isChangePlanItemInCart()) {
							  msg = formatUserMessage(COMPLETE_CHANGEPLAN_TRANSACTION, pRequest,pResponse);
							  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
							addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_CHANGEPLAN_TRANSACTION));
							setModalError(Boolean.TRUE);
						} else if (isChangePlanItemInCart() && isUpgradeAddonProductAlreadyInCart()) {
							  msg = formatUserMessage(ADDON_ALREADY_PRESENT_IN_UPGRADEITEM, pRequest,pResponse);
							  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
							addFormException(new DropletFormException(msg, null, propertyPath,ADDON_ALREADY_PRESENT_IN_UPGRADEITEM));
							setModalError(Boolean.FALSE);
						}
						//check if cart has change-plan commerce item
						//if so check addon compatibity with it.
						//if not throw exception telling user can't add item.
					} else {
						if (isChangePlanItemInCart() && StringUtils.isEmpty(getUpgradeItemFlow())) {
							  msg = formatUserMessage(COMPLETE_CHANGEPLAN_TRANSACTION, pRequest,pResponse);
							  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
							addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_CHANGEPLAN_TRANSACTION));
							setModalError(Boolean.TRUE);
						} else if (isChangePlanItemInCart() && !StringUtils.isEmpty(getUpgradeItemFlow())) {
							replaceUpgradeItem(getCartConfiguration().getChangePlanItemType());
						} else if (isAccessoriesPresentInCart() || isUpgradePhoneItemInCart() || isUpgradeAddonProductInCart()) {
							  msg = formatUserMessage(COMPLETE_PACKAGE_TRANSACTION_FIRST, pRequest,pResponse);
							  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
							addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_PACKAGE_TRANSACTION_FIRST));
							setModalError(Boolean.TRUE);
						} /*else if (isUpgradePhoneItemInCart()) {
							String msg = formatUserMessage(COMPLETE_PACKAGE_TRANSACTION_FIRST, pRequest,pResponse);
							String propertyPath = generatePropertyPath("order");
							addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_PACKAGE_TRANSACTION_FIRST));
							setModalError(Boolean.TRUE);
						} else if (isUpgradeAddonProductInCart()) {
							String msg = formatUserMessage(COMPLETE_PACKAGE_TRANSACTION_FIRST, pRequest,pResponse);
							String propertyPath = generatePropertyPath("order");
							addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_PACKAGE_TRANSACTION_FIRST));
							setModalError(Boolean.TRUE);
						} */else {
							if (!StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getRemovedPhoneId())
									&& !isPlanCompatibleWithPhone(getUpgradeItemDetailsSessionBean().getRemovedPhoneId(), getProductId())) {
								  msg = formatUserMessage(PLAN_NOT_COMPATIBLE_WITH_PHONE, pRequest,pResponse);
								  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
								addFormException(new DropletFormException(msg, null, propertyPath,PLAN_NOT_COMPATIBLE_WITH_PHONE));
								setModalError(Boolean.FALSE);
							}
							//check compatibility with phone.
						}
						
					}
				} else {
					  msg = formatUserMessage(ADD_OTHER_PRODUCTS_INCHANGEPLANF_LOW, pRequest,pResponse);
					  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
					addFormException(new DropletFormException(msg, null, propertyPath,ADD_OTHER_PRODUCTS_INCHANGEPLANF_LOW));
					setModalError(Boolean.TRUE);
				}
			}
			} else if (getUpgradeItemDetailsSessionBean().getUserIntention().equalsIgnoreCase(getCartConfiguration().getUpgradeAddonIntention())) {
				List<CricketPackage> cricketPackages = ((CricketOrderImpl) getOrder()).getCktPackages();
				if (null != cricketPackages
						&& !cricketPackages.isEmpty() 
						&& cricketPackages.size() > 0) {
					  msg = formatUserMessage(COMPLETE_PACKAGE_TRANSACTION_FIRST, pRequest,pResponse);
					  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
					addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_PACKAGE_TRANSACTION_FIRST));
					setModalError(Boolean.TRUE);
				} else if (isAccessoriesPresentInCart() || isUpgradePhoneItemInCart() || isChangePlanItemInCart()) {
					  msg = formatUserMessage(COMPLETE_PACKAGE_TRANSACTION_FIRST, pRequest,pResponse);
					  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
					addFormException(new DropletFormException(msg, null, propertyPath,COMPLETE_PACKAGE_TRANSACTION_FIRST));
					setModalError(Boolean.TRUE);
				} else {
				if (productType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)) {
					//check addon compatibility with plan and phone.
					if (isUpgradeAddonProductAlreadyInCart()) {
						  msg = formatUserMessage(ADDON_ALREADY_PRESENT_IN_UPGRADEITEM, pRequest,pResponse);
						  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
						addFormException(new DropletFormException(msg, null, propertyPath,ADDON_ALREADY_PRESENT_IN_UPGRADEITEM));
						setModalError(Boolean.FALSE);
					} else {
						
						//check addon compatibility with plan and phone.
						boolean compatibleAddon = false;
						String modelNumber = getUpgradeItemDetailsSessionBean().getModelNumber();
						String planCode = getUpgradeItemDetailsSessionBean().getRemovedPlanId();
						String marketCode = (String)getProfile().getPropertyValue(PROP_MARKET_ID);
						RepositoryItem addonProduct = getCatalogTools().findProduct(getProductId());
						String currentAddOnOfferId = null;
						if(addonProduct != null) {
							currentAddOnOfferId = (String)(addonProduct.getPropertyValue(PROP_OFFER_ID));
						}
						if(planCode != null && modelNumber != null && marketCode != null) {
							Map<String, List<InquireFeaturesResponseVO>> planAddOnsMap = getDisplayFeaturesManager().getCompatibleAddons(
																							modelNumber, PHONE_TYPE, planCode, marketCode,
																							CricketESPConstants.TRANSACTION_TYPE_RRC, (ArrayList) getCricketProfile().getUserPurchasedOfferingProducts(),false, true, CricketESPConstants.CKT_SALES_CHANNEL_NAME,getOrder().getId());
							if (null != planAddOnsMap) {
							List<InquireFeaturesResponseVO> optionalAddOns= planAddOnsMap.get(OPTIONAL_ADDONS);
							
							InquireFeaturesResponseVO activationFeeFeature = getCricketESPAdapterHelper().getActivationFeeAddOn(optionalAddOns);
							if(activationFeeFeature != null && activationFeeFeature.getPrice() != null) {
					    		profile.setPropertyValue(ACTIVATION_FEE, activationFeeFeature.getPrice().doubleValue());
					    	}
							if (null != optionalAddOns && !optionalAddOns.isEmpty() && optionalAddOns.size() > 0) {
							InquireFeaturesResponseVO administrationFeeFeature = getCricketESPAdapterHelper().getAdministrationFeeAddOn(optionalAddOns);					    									
							
							for(InquireFeaturesResponseVO optionalAddOn : optionalAddOns) {
								if(optionalAddOn.getId() != null && currentAddOnOfferId != null && optionalAddOn.getId().equalsIgnoreCase(currentAddOnOfferId)) {
							    	Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersMap = null;
							    	//List<InquireFeaturesResponseVO> mandatoryAddOns = planAddOnsMap.get("allIncludedAddOns");
							    	 List<InquireFeaturesResponseVO> mandatoryAddOns = planAddOnsMap.get(MANDATORY_ADDONS);

							    	mandatoryOffersMap = getUserSessionBean().getMandatoryOffersMap();
							    	if (mandatoryOffersMap == null) {
							    		mandatoryOffersMap = new HashMap<String, List<InquireFeaturesResponseVO>>();
							    	}
							    	if(mandatoryAddOns != null && mandatoryAddOns.size() > 0) {
							    		if(administrationFeeFeature != null){
							    			//Administration Fee addOn needs to be sent to billing system for charging the user on monthly basis
							    			//Hence adding to mandatoryAddOns list which will be used in CreateActivationQuote request for building CricketOfferingCodes
							    			mandatoryAddOns.add(administrationFeeFeature);
							    		}
							    	}
							    	if(planCode != null && mandatoryAddOns != null && mandatoryAddOns.size() > 0){
							    		mandatoryOffersMap.put(planCode, mandatoryAddOns);
							    		getUserSessionBean().setMandatoryOffersMap(mandatoryOffersMap);
							    	}
							    	compatibleAddon = true;
									break;												
								}
							}
							}
							List<InquireFeaturesResponseVO>  hppAddons = planAddOnsMap.get(HPP_ADDONS);
					    	if (null != hppAddons && !hppAddons.isEmpty()) {
					    		getUserSessionBean().setHppAddOns(hppAddons);
					    	}
						}
						}
						if(!compatibleAddon) {
							setReloadDrawerCartDiv(Boolean.TRUE);
							msg = formatUserMessage(ADDON_NOT_COMPATIBLE_WITH_PHONE_AND_PLAN, pRequest,pResponse);
							propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
							addFormException(new DropletFormException(msg, null, propertyPath,ADDON_NOT_COMPATIBLE_WITH_PHONE_AND_PLAN));
						}
					}
				} else {
					  msg = formatUserMessage(ADD_OTHERPRODUCTS_IN_CHANGEFEATUREF_LOW, pRequest,pResponse);
					  propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
					addFormException(new DropletFormException(msg, null, propertyPath,ADD_OTHERPRODUCTS_IN_CHANGEFEATUREF_LOW));
					setModalError(Boolean.TRUE);
				}
			}
			}
		}
		//if ()
		if (isLoggingDebug()) {				
	    		logDebug("Exiting from CricketCartModifierFormHandler class of checkUserIntention() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
	
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isAccessoriesPresentInCart() {
		boolean isAccessoriesPresentInCart = Boolean.FALSE;
		List<CricketCommerceItemImpl> cricketCommerceItemList = getOrder().getCommerceItems();
		if (null != cricketCommerceItemList && !cricketCommerceItemList.isEmpty()) {
			for(CricketCommerceItemImpl cricketCommerceItem :  cricketCommerceItemList){
				if (ACCESSORY_PRODUCT.equalsIgnoreCase(cricketCommerceItem.getCricItemTypes())) {
					isAccessoriesPresentInCart = Boolean.TRUE;
					break;
				}
			}
		}
		return isAccessoriesPresentInCart;
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isUpgradeAddonProductAlreadyInCart() {
		boolean isUpgradeAddonProductAlreadyInCart = Boolean.FALSE;
		String addonProductId = getProductId();
		List<CricketCommerceItemImpl> cricketCommerceItemList = getOrder().getCommerceItems();
		if (null != cricketCommerceItemList && !cricketCommerceItemList.isEmpty() 
				&& !StringUtils.isEmpty(addonProductId)) {
			for(CricketCommerceItemImpl cricketCommerceItem :  cricketCommerceItemList){
				if (addonProductId.equalsIgnoreCase(cricketCommerceItem.getAuxiliaryData().getProductId())) {
					isUpgradeAddonProductAlreadyInCart = Boolean.TRUE;
				}
			}
		}
		return isUpgradeAddonProductAlreadyInCart;
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isChangePlanProductAlreadyInCart() {
		boolean isChangePlanProductAlreadyInCart = Boolean.FALSE;
		List<CricketCommerceItemImpl> cricketCommerceItemList = getOrder().getCommerceItems();
		if (null != cricketCommerceItemList && !cricketCommerceItemList.isEmpty()) {
			for(CricketCommerceItemImpl cricketCommerceItem :  cricketCommerceItemList){
				if (getCartConfiguration().getChangePlanItemType().equalsIgnoreCase(cricketCommerceItem.getCricItemTypes())) {
					isChangePlanProductAlreadyInCart = Boolean.TRUE;
				}
			}
		}
		return isChangePlanProductAlreadyInCart;
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isUpgradeAddonProductInCart() {
		boolean isUpgradeAddonProductInCart = Boolean.FALSE;
		List<CricketCommerceItemImpl> cricketCommerceItemList = getOrder().getCommerceItems();
		if (null != cricketCommerceItemList && !cricketCommerceItemList.isEmpty()) {
			for(CricketCommerceItemImpl cricketCommerceItem :  cricketCommerceItemList){
				if (getCartConfiguration().getChangeAddOnItemType().equalsIgnoreCase(cricketCommerceItem.getCricItemTypes())) {
					isUpgradeAddonProductInCart = Boolean.TRUE;
				}
			}
		}
		return isUpgradeAddonProductInCart;
	}
	
	/**
	 * @param phoneId
	 * @param planId
	 * @return
	 * @throws RepositoryException
	 */
	@SuppressWarnings("unchecked")
	private boolean isPhoneCompatibleWithPlan(String phoneId, String planId) throws RepositoryException {
		boolean isPhoneCompatibleWithPlan = Boolean.FALSE;
		RepositoryItem planItem = getCatalogTools().findProduct(planId);
		List<RepositoryItem> compaliblePhones = (List<RepositoryItem>) planItem.getPropertyValue(COMPATIBLE_PHONE);
		for(RepositoryItem cItem :  compaliblePhones){
			String compatiablePhonId = cItem.getRepositoryId();
			if (compatiablePhonId.equals(phoneId)) {
				isPhoneCompatibleWithPlan = Boolean.TRUE;
				break;
			}
		}
	
		return isPhoneCompatibleWithPlan;
	}
	
	/**
	 * @param phoneId
	 * @param planId
	 * @return
	 * @throws RepositoryException
	 */
	@SuppressWarnings("unchecked")
	private boolean isPlanCompatibleWithPhone(String phoneId, String planId) throws RepositoryException {
		boolean isPlanCompatibleWithPhone = Boolean.FALSE;
		RepositoryItem phoneItem = getCatalogTools().findProduct(phoneId);
		if (null != phoneItem) {
		Set<RepositoryItem> compatiblePlans = (Set<RepositoryItem>) phoneItem.getPropertyValue(COMPATIBLE_PLANS);
		if (null != compatiblePlans && !compatiblePlans.isEmpty()) {
			String compatiblePlanId = null;
		for(RepositoryItem cItem :  compatiblePlans){
			 compatiblePlanId = cItem.getRepositoryId();
			if (compatiblePlanId.equals(planId)) {
				isPlanCompatibleWithPhone = Boolean.TRUE;
				break;
			}
		}
		}
		}
		return isPlanCompatibleWithPhone;
	}
	
	/**
	 * @param itemType
	 * @throws CommerceException
	 */
	@SuppressWarnings("unchecked")
	private void replaceUpgradeItem(String itemType) throws CommerceException {
		List<CricketCommerceItemImpl> cricketCommerceItemList = getOrder().getCommerceItems();
		 if (!StringUtils.isEmpty(itemType)) {
		 List<String> removalIds = new ArrayList<String>();
		for (CricketCommerceItemImpl cricketCommerceItemImpl : cricketCommerceItemList) {
			if (itemType.equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())) {
				removalIds.add(cricketCommerceItemImpl.getId());
			}
		}
		for (String cricketCommerceItemId : removalIds) {
			getCommerceItemManager().removeItemFromOrder(getOrder(),cricketCommerceItemId);
		}
		}
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isUpgradePhoneItemInCart() {
		boolean isUpgradePhoneItemInCart = Boolean.FALSE;
		//check if only accessories items in cart
		//else error message
		List<CricketCommerceItemImpl> cricketCommerceItemList = getOrder().getCommerceItems();
		for (CricketCommerceItemImpl cricketCommerceItemImpl : cricketCommerceItemList) {
			if (getCartConfiguration().getUpgradePhoneItemType().equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())) {
				isUpgradePhoneItemInCart = Boolean.TRUE;
				break;
			}
		}
		return isUpgradePhoneItemInCart;
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isChangePlanItemInCart() {
		boolean isUpgradePhoneItemInCart = Boolean.FALSE;
		//check if only accessories items in cart
		//else error message
		List<CricketCommerceItemImpl> cricketCommerceItemList = getOrder().getCommerceItems();
		for (CricketCommerceItemImpl cricketCommerceItemImpl : cricketCommerceItemList) {
			if (getCartConfiguration().getChangePlanItemType().equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())) {
				isUpgradePhoneItemInCart = Boolean.TRUE;
				break;
			}
		}
		return isUpgradePhoneItemInCart;
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isChangeAddonItemInCart() {
		boolean isUpgradePhoneItemInCart = Boolean.FALSE;
		//check if only accessories items in cart
		//else error message
		List<CricketCommerceItemImpl> cricketCommerceItemList = getOrder().getCommerceItems();
		for (CricketCommerceItemImpl cricketCommerceItemImpl : cricketCommerceItemList) {
			if (getCartConfiguration().getChangeAddOnItemType().equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())) {
				isUpgradePhoneItemInCart = Boolean.TRUE;
				break;
			}
		}
		return isUpgradePhoneItemInCart;
	}
	/**
	 * @throws CommerceException
	 */
	protected void clearShoppingCart() throws CommerceException {
		getCommerceItemManager().removeAllCommerceItemsFromOrder(getOrder());
		((CricketOrderImpl) getOrder()).removeAllCricketPackages();
	}

	/* (non-Javadoc)
	 * @see atg.commerce.order.purchase.CartModifierFormHandler#postAddItemToOrder(atg.servlet.DynamoHttpServletRequest, atg.servlet.DynamoHttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void postAddItemToOrder(
			DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse)
			throws ServletException, IOException {
		
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
				if(null != getOrder()){
					if(!StringUtils.isBlank(getOrder().getId())){
						orderId = getOrder().getId();
					}
				}
						
	    		logDebug("Entering into CricketCartModifierFormHandler class of postAddItemToOrder() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		//changes for defect 6313 start 
		Profile profile = getProfileServices().getCurrentProfile();
		List<CricketPackage> existingPackagesForActFee = ((CricketOrderImpl)getOrder()).getCktPackages();
		if(existingPackagesForActFee != null && existingPackagesForActFee.size() > 0){
			boolean activationFeeSet = false;
			for (CricketPackage existingPackageForActFee : existingPackagesForActFee) {
				String planCode = null;
				String modelNumber = null;
				RepositoryItem pkgProduct = null;
				String pkgProductType = null;
				RepositoryItem pkgPhoneSKU = null;
				CricketCommerceItemImpl[] commerceItems = (CricketCommerceItemImpl[]) ((CricketOrderTools) getOrderManager().getOrderTools()).getCommerceItemsForPackage(existingPackageForActFee, (CricketOrderImpl)getOrder());
				if (commerceItems !=null && commerceItems.length > 1) {
					for (CricketCommerceItemImpl pkgCommerceItem : commerceItems) {
						  pkgProduct = (RepositoryItem) pkgCommerceItem.getAuxiliaryData().getProductRef();
						  pkgProductType = (String) pkgProduct.getPropertyValue(TYPE);
						if (pkgProductType.equalsIgnoreCase(PLAN_PRODUCT)) {
							planCode = pkgProduct.getRepositoryId();
						}
						if (pkgProductType.equalsIgnoreCase(PHONE_PRODUCT)) {
							  pkgPhoneSKU = (RepositoryItem) pkgCommerceItem.getAuxiliaryData().getCatalogRef();
							if(pkgPhoneSKU != null) {
								modelNumber = (String)pkgPhoneSKU.getPropertyValue(PROP_MODEL_NUMBER);
							}
						}
					}
				}
				String marketCode = (String)getProfile().getPropertyValue(PROP_MARKET_ID);
				if(planCode != null && modelNumber != null && marketCode != null) {
					Map<String, List<InquireFeaturesResponseVO>> planAddOnsMap = getDisplayFeaturesManager().getCompatibleAddons(
																					modelNumber, PHONE_TYPE, planCode, marketCode,
																						ADD_ADDON_TRANS_NAME, (ArrayList) getCricketProfile().getUserPurchasedOfferingProducts(),false, true, CricketESPConstants.CKT_SALES_CHANNEL_NAME,getOrder().getId());
					if (null != planAddOnsMap) {
					List<InquireFeaturesResponseVO> optionalAddOns= planAddOnsMap.get(OPTIONAL_ADDONS);
					
					InquireFeaturesResponseVO activationFeeFeature = getCricketESPAdapterHelper().getActivationFeeAddOn(optionalAddOns);
					if(activationFeeFeature != null && activationFeeFeature.getPrice() != null) {
			    		profile.setPropertyValue(ACTIVATION_FEE, activationFeeFeature.getPrice().doubleValue());
			    		activationFeeSet = true;
			    	}
					List<InquireFeaturesResponseVO>  hppAddons = planAddOnsMap.get(HPP_ADDONS);
			    	if (null != hppAddons && !hppAddons.isEmpty()) {
			    		getUserSessionBean().setHppAddOns(hppAddons);
			    	}			    									
				}
				}
				if(activationFeeSet){
					//changes for defect 8318 start 
					double activationFee = 0;
					Cookie cookieName = new Cookie(ACTIVATION_FEE_COOKIE, ACTIVATION_FEE_COOKIE);
		    		cookieName.setMaxAge(getCartConfiguration().getOrderPersistentCookieMaxAge());
		    		if(profile != null){	
		    			activationFee = (Double) profile.getPropertyValue(ACTIVATION_FEE);
		    		}
		    		if(activationFee > 0){
		    			cookieName.setValue(Double.toString(activationFee));
		    			cookieName.setPath(SEPARTOR);
		    			pResponse.addCookie(cookieName);
		    		}
		    		//changes for defect 8318 ends 
					break;
				}
				
			}
		}		
		//changes for defect 6313 end
		
		if (getCartCookie().isEditActionFromCart()) {
			getCartCookie().setPackageId(null);
			getCartCookie().setProductType(null);
			getCartCookie().setEditActionFromCart(false);
		}
		if (!StringUtils.isEmpty(getUserSessionBean().getSpecialURLCouponCode())) {
			addCouponToOrder();
		}
		boolean isCartHasOnlyAccessories = isCartHasOnlyAccessories(getOrder().getCommerceItems());
		setOnlyAccessories(isCartHasOnlyAccessories);
		super.postAddItemToOrder(pRequest,
				pResponse);
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
				if(null != getOrder()){
					if(!StringUtils.isBlank(getOrder().getId())){
						orderId = getOrder().getId();
					}
				}
						
	    		logDebug("Exiting from CricketCartModifierFormHandler class of postAddItemToOrder() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
	
	/**
	 * 
	 */
	private void addCouponToOrder() {
		try {
			((CricketPurchaseProcessHelper) getPurchaseProcessHelper())
			.tenderCoupon(getUserSessionBean().getSpecialURLCouponCode(),
					((CricketOrderImpl) getOrder()), getProfile(),
					getUserPricingModels(), getUserLocale());
			addCouponException();	
		} catch (CommerceException e) {
			if (isLoggingError())
				logError(e);
		} catch (IllegalArgumentException e) {
			if (isLoggingError())
				logError(e);
		}
	}
	
	public void addCouponException() {
		String message = null;
		String ErrorMessage = CricketPurchaseProcessHelper.couponErrorMessage;
		if (ErrorMessage != null) {
			if (ErrorMessage
					.equalsIgnoreCase(EXPIRE_PROMO_CODE_ERROR)) {
				  message = getStoreTextMessage(EXPIRE_PROMO_CODE_MESSAGE);
				if (!StringUtils.isEmpty(message)) {
					addFormException(new DropletFormException(
							message, EMPTY_STRING));
					setReloadDrawerCartDiv(Boolean.TRUE);
				}
				if (isLoggingDebug()) {
					logDebug("Promo Code is Expired");
				}
			} else if (ErrorMessage
					.equalsIgnoreCase(ENTER_PROMO_CODE_ERROR)) {
				  message = getStoreTextMessage(ENTER_PROMO_CODE_ERROR_MESSAGE);
				if (!StringUtils.isEmpty(message)) {
					addFormException(new DropletFormException(
							message, EMPTY_STRING));
					setReloadDrawerCartDiv(Boolean.TRUE);
				}
				if (isLoggingDebug()) {
					logDebug("Enter a Promo Code");
				}
			} else {
				  message = getStoreTextMessage(INVALID_PROMO_CODE_MESSAGE);
				if (!StringUtils.isEmpty(message)) {
					addFormException(new DropletFormException(
							message, EMPTY_STRING));
					setReloadDrawerCartDiv(Boolean.TRUE);
				}
				if (isLoggingDebug()) {
					logDebug("Enter a valid Promo Code");
				}
			}
		} else {
			getUserSessionBean().setSpecialURLCouponCode(null);
		}
	}
	
	
	private String getStoreTextMessage(String Key) {
		String message = null;
		RepositoryItem repositoryItem = null;
		RepositoryView reposView;
		try {
			reposView = getStoreTextRepository().getView(STORE_TEXT);
			RqlStatement statement = RqlStatement.parseRqlStatement("key=?0");
			Object[] inputParams = new Object[1];
			inputParams[0] = Key;
			RepositoryItem[] store_txt_item = statement.executeQuery(reposView,
					inputParams);
			if (null != store_txt_item && store_txt_item.length > 0) {
				for (int j = 0; j < store_txt_item.length; j++) {
					repositoryItem = store_txt_item[j];
					message= (String) repositoryItem.getPropertyValue(TEXT);
				}
				}

		} catch (RepositoryException e) {
			if (isLoggingError()) {
				logError("RepositoryException in getStoreTextMessage of CricketCartModifierFormhandler", e);
			}
		}
		return message;
	}
	
	/**
	 * @param commerceItem
	 * @return
	 */
	public boolean isPackageExist(CricketCommerceItemImpl commerceItem) {
		boolean isExist = false;
		if (commerceItem.isLOS()) {
			isExist = true;
		}
		return isExist;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void addItemToOrder(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {		
		super.addItemToOrder(pRequest, pResponse);

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
		 CricketOrderImpl order = (CricketOrderImpl) getOrder();
		 String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank(order.getId())){
	    			orderId = order.getId();
				}
			}
		if (isLoggingDebug()) {						
	    		logDebug("Enter into CricketCartModifierFormHandler class of addItemToOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		CricketOrderTools tools = (CricketOrderTools) getOrderManager().getOrderTools();
		String productType = findProductType(getProductId());
		CricketCommerceItemManager ciItemManager =(CricketCommerceItemManager) getCommerceItemManager();
		ciItemManager.setProductType(productType);
		// Order order = getShoppingCart().getCurret();
		order = (CricketOrderImpl) getOrder();

		int cItemCnt = order.getCommerceItemCount();
		CricketPackage cricketPackage = null;

		// To get the value from Profile
		boolean profile_upg_phone = false; // getProfile.getUpgradePhone();
		boolean profile_upg_plan = false; // getProfile.getUpgradePlan();
		boolean profile_upg_addon = false;
		
		if (!StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getUserIntention())) {
			if (getUpgradeItemDetailsSessionBean().getUserIntention().equalsIgnoreCase(getCartConfiguration().getUpgradePhoneIntention())) {
				profile_upg_phone = Boolean.TRUE;
			} else if (getUpgradeItemDetailsSessionBean().getUserIntention().equalsIgnoreCase(getCartConfiguration().getUpgradePlanIntention())) {
				profile_upg_plan = Boolean.TRUE;
			} else if (getUpgradeItemDetailsSessionBean().getUserIntention().equalsIgnoreCase(getCartConfiguration().getUpgradeAddonIntention())) {
				profile_upg_addon = Boolean.TRUE;
			} 
		}
		CricketCommerceItemImpl commerceItem = null;
		try {
			ArrayList<CricketCommerceItemImpl> commItems = (ArrayList<CricketCommerceItemImpl>) order.getCommerceItemsByCatalogRefId(getCatalogRefIds()[0]);
			
			commerceItem = (CricketCommerceItemImpl) order.getCommerceItem(((CommerceItem) commItems.get(commItems.size()-1)).getId());
			if(getCartCookie().isEditActionFromCart() && getCartCookie().getProductType().equalsIgnoreCase(productType) 
					&& (!StringUtils.isEmpty(pRequest.getParameter(EDIT_ITEM)) && pRequest.getParameter(EDIT_ITEM).equalsIgnoreCase(TRUE))){
				/*RepositoryItem cktPackageItem = getOrderManager().getOrderTools().getOrderRepository().getItem(getCartCookie().getPackageId(), "cktPackage");
				CricketPackage cricketPackageEdit = new CricketPackage();
				cricketPackageEdit.setId((String) cktPackageItem.getPropertyValue("id"));*/
				CricketPackage cktPackage = tools.getCricketPackage(getCartCookie().getPackageId(), order);
				tools.updateCommerceItem(commerceItem, productType, cktPackage);
				List<CricketPackage> cricketPackages = order.getCktPackages();
				if(!order.getCktPackages().contains(cktPackage)){
					cricketPackages.add(cktPackage);
					order.setCktPackages(cricketPackages);
				}
			}else {
			
			if (productType.equalsIgnoreCase(ACCESSORY_PRODUCT)
						|| profile_upg_phone || profile_upg_plan || profile_upg_addon) {
				if (productType.equalsIgnoreCase(ACCESSORY_PRODUCT)) {
					commerceItem.setCricItemTypes(productType);
				} else {
					if (profile_upg_phone) {
						if (productType.equalsIgnoreCase(PHONE_PRODUCT)) {
							commerceItem.setCricItemTypes(getCartConfiguration().getUpgradePhoneItemType());
						} else {
							commerceItem.setCricItemTypes(productType);
						}
					} 
					if (profile_upg_plan) {
						if (productType.equalsIgnoreCase(PLAN_PRODUCT)) {
							if (StringUtils.isEmpty(getUpgradeItemFlow())) {
								order.setRemovedAddonCompatibility(setRemovedAddonsCompatibility(false));
								if (null != order.getRemovedAddonCompatibility() && !order.getRemovedAddonCompatibility().isEmpty()) {
									CricketCommerceItemImpl removedCommerceItem = null;
									for (Iterator<Map.Entry<String, Boolean>> it = order.getRemovedAddonCompatibility().entrySet().iterator(); it.hasNext();) {
										 Map.Entry<String, Boolean> entry = it.next();
										if (!entry.getValue()) {
											removedCommerceItem = (CricketCommerceItemImpl) getCommerceItemManager().createCommerceItem(entry.getKey(), getUpgradeItemDetailsSessionBean().getRemovedAddons().get(entry.getKey()), 1);
											removedCommerceItem.setCricItemTypes(getCartConfiguration().getRemovedAddonItemType());
											getCommerceItemManager().addItemToOrder(order, removedCommerceItem);
											removedCommerceItem.setCompatibleAddon(Boolean.FALSE);
											it.remove();
										}
									}
								}
							}
							commerceItem.setCricItemTypes(getCartConfiguration().getChangePlanItemType());
							commerceItem.setDownGrade(isPlanDownGrade(commerceItem));
						} else if (productType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)) {
							if (isAddAsRemovedCommerceItem()) {
								commerceItem.setCricItemTypes(getCartConfiguration().getRemovedAddonItemType());
								//commerceItem. Set compatibility true or false based on map.
								for (Iterator<Map.Entry<String, Boolean>> it = order.getRemovedAddonCompatibility().entrySet().iterator(); it.hasNext();) {
									 Map.Entry<String, Boolean> entry = it.next();
									 	if (!StringUtils.isEmpty(entry.getKey()) && entry.getKey().equalsIgnoreCase(getProductId())) {
									 		commerceItem.setCompatibleAddon(entry.getValue());
									 		it.remove();
										}
									}
							} else {
								commerceItem.setCricItemTypes(productType);
								commerceItem.setChangeFlow(Boolean.TRUE);
							}
						}else {
							commerceItem.setCricItemTypes(productType);
						}
					} 
					if (profile_upg_addon) {
						if (productType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)) {
							//order.setRemovedAddonCompatibility(setRemovedAddonsCompatibility(true));
							if (isAddAsRemovedCommerceItem()) {
								commerceItem.setCricItemTypes(getCartConfiguration().getRemovedAddonItemType());
								order.setRemovedAddonCompatibility(setRemovedAddonsCompatibility(true));
								//commerceItem. Set compatibility true or false based on map.
								for (Iterator<Map.Entry<String, Boolean>> it = order.getRemovedAddonCompatibility().entrySet().iterator(); it.hasNext();) {
									 Map.Entry<String, Boolean> entry = it.next();
									 	if (!StringUtils.isEmpty(entry.getKey()) && entry.getKey().equalsIgnoreCase(getProductId())) {
									 		commerceItem.setCompatibleAddon(entry.getValue());
									 		it.remove();
										}
									}
							} else {
								commerceItem.setCricItemTypes(getCartConfiguration().getChangeAddOnItemType());
								order.setRemovedAddonCompatibility(setRemovedAddonsCompatibility(true));
								addHPPAddonToCart();
							}
						} else {
							commerceItem.setCricItemTypes(productType);
						}
					}
					setRemovedItemDetailsInOrder();
				}
			} else if (productType.equalsIgnoreCase(PHONE_PRODUCT)
					|| productType.equalsIgnoreCase(PLAN_PRODUCT)
						|| productType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)) {

					if (cItemCnt == 1) {

						cricketPackage = tools.createCricketPackage();
						tools.updateCommerceItem(commerceItem, productType,
								cricketPackage);
						List<CricketPackage> cricketPackages = new ArrayList<CricketPackage>();
						cricketPackages.add(cricketPackage);
						order.setCktPackages(cricketPackages);
					} else {
						// Package ID known, update the new item to that package.						
						if (!StringUtils.isEmpty(getFormPackageId()) && null != tools.getCricketPackage(getFormPackageId(), order)) {
							CricketPackage cktPackage = tools.getCricketPackage(getFormPackageId(), order);
							tools.updateCommerceItem(((CricketCommerceItemImpl) commItems.get(commItems.size()-1)),productType, cktPackage);
							
							// Start Check if the phone being added already has the plan exactly compatible to it in the package and not the other plan in the group
							if (productType.equalsIgnoreCase(PHONE_PRODUCT)) {
								CricketCommerceItemImpl[] commerceItems = (CricketCommerceItemImpl[]) tools.getCommerceItemsForPackage(cktPackage, order);
								if (commerceItems.length > 1) {
									RepositoryItem pkgProduct = null;
									String pkgProductType = null;
									for (CricketCommerceItemImpl pkgCommerceItem : commerceItems) {
										  pkgProduct = (RepositoryItem) pkgCommerceItem.getAuxiliaryData().getProductRef();
										  pkgProductType = (String) pkgProduct.getPropertyValue(TYPE);
										if (pkgProductType.equalsIgnoreCase(PLAN_PRODUCT)) {
											RepositoryItem phoneInPackage = getCatalogTools().findProduct(getProductId());
											Set<RepositoryItem> phoneCompatiblePlans = (Set<RepositoryItem>) phoneInPackage.getPropertyValue(COMPATIBLE_PLANS);
											if(!phoneCompatiblePlans.contains(pkgProduct)) {
												Set<RepositoryItem> planGroupsSet = (Set<RepositoryItem>) pkgProduct.getPropertyValue(PLAN_GROUPS);
												List<RepositoryItem> planGroupsList = new ArrayList(planGroupsSet);
												RepositoryItem planGroup = planGroupsList.get(0);
												List<RepositoryItem> plansInGroup = (List<RepositoryItem>) planGroup.getPropertyValue(GROUP_PLANS);
												for(RepositoryItem planInGroup : plansInGroup){
													if(phoneCompatiblePlans.contains(planInGroup)) {		
														getCommerceItemManager().setCommerceItemProductAndSku(pkgCommerceItem, 
																	planInGroup.getRepositoryId(), ((List<RepositoryItem>)planInGroup.getPropertyValue(CHILD_SKUS)).get(0).getRepositoryId());
														getOrderManager().updateOrder(getOrder());														
														break;
													}
												}
											}
											break;
										}
									}
								}
							}
							// End Check if the phone being added already has the plan exactly compatible to it in the package and not the other plan in the group
						} else {
							boolean isCompatiable = false;
							boolean isCompatibleMsg = false;
							boolean isSameProduct = false;
							List<CricketPackage> existingPackages = order.getCktPackages();
							if (null != existingPackages && existingPackages.size() > 0) {
								for (CricketPackage existingpackage : existingPackages) {
									
									CricketCommerceItemImpl[] commerceItems = (CricketCommerceItemImpl[]) tools.getCommerceItemsForPackage(existingpackage, order);
									boolean isPhone = false;
									boolean isPlan = false;
									
									if (commerceItems.length > 1) {
										RepositoryItem pkgProduct = null;
										String pkgProductType = null;
										for (CricketCommerceItemImpl pkgCommerceItem : commerceItems) {
											  pkgProduct = (RepositoryItem) pkgCommerceItem.getAuxiliaryData().getProductRef();
											  pkgProductType = (String) pkgProduct.getPropertyValue(TYPE);
											if (pkgProductType.equalsIgnoreCase(PLAN_PRODUCT))
												isPlan = true;
											if (pkgProductType.equalsIgnoreCase(PHONE_PRODUCT))
												isPhone = true;
										}
										if(isPhone && isPlan)
											isCompatibleMsg = false;
									}
									if (!isPhone || !isPlan || productType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT)) {
										RepositoryItem pkgProduct = null;
										String pkgProductType = null;
										for (CricketCommerceItemImpl pkgCommerceItem : commerceItems) {
											  pkgProduct = (RepositoryItem) pkgCommerceItem.getAuxiliaryData().getProductRef();
											  pkgProductType = findProductType(pkgProduct.getRepositoryId());
											if (pkgProductType.equalsIgnoreCase(productType)) {
												isSameProduct = true;
												break;
											}
											else if (pkgProductType.equalsIgnoreCase(PLAN_PRODUCT)&& productType.equalsIgnoreCase(PHONE_PRODUCT)) {
												if (isCompatiblePhoneAndPlan(productType, pkgProduct)) {
													tools.updateCommerceItem(((CricketCommerceItemImpl) commItems.get(commItems.size()-1)),productType,existingpackage);
													isCompatiable = true;
													
													// Start Check if the phone being added already has the plan exactly compatible to it in the package and not the other plan in the group
													if (productType.equalsIgnoreCase(PHONE_PRODUCT)) {
														CricketCommerceItemImpl[] existPackcommerceItems = (CricketCommerceItemImpl[]) tools.getCommerceItemsForPackage(existingpackage, order);
														if (existPackcommerceItems.length > 1) {
															RepositoryItem exisPkgProduct = null;
															String exisPkgProductType = null;
															for (CricketCommerceItemImpl existPackcommerceItem : existPackcommerceItems) {
																  exisPkgProduct = (RepositoryItem) existPackcommerceItem.getAuxiliaryData().getProductRef();
																  exisPkgProductType = (String) exisPkgProduct.getPropertyValue(TYPE);
																if (exisPkgProductType.equalsIgnoreCase(PLAN_PRODUCT)) {
																	RepositoryItem phoneInPackage = getCatalogTools().findProduct(getProductId());
																	Set<RepositoryItem> phoneCompatiblePlans = (Set<RepositoryItem>) phoneInPackage.getPropertyValue(COMPATIBLE_PLANS);
																	if(!phoneCompatiblePlans.contains(exisPkgProduct)) {
																		Set<RepositoryItem> planGroupsSet = (Set<RepositoryItem>) exisPkgProduct.getPropertyValue(PLAN_GROUPS);
																		List<RepositoryItem> planGroupsList = new ArrayList(planGroupsSet);
																		RepositoryItem planGroup = planGroupsList.get(0);
																		List<RepositoryItem> plansInGroup = (List<RepositoryItem>) planGroup.getPropertyValue(GROUP_PLANS);
																		for(RepositoryItem planInGroup : plansInGroup){
																			if(phoneCompatiblePlans.contains(planInGroup)) {		
																				getCommerceItemManager().setCommerceItemProductAndSku(existPackcommerceItem, 
																							planInGroup.getRepositoryId(), ((List<RepositoryItem>)planInGroup.getPropertyValue(CHILD_SKUS)).get(0).getRepositoryId());
																				getOrderManager().updateOrder(getOrder());														
																				break;
																			}
																		}
																	}
																	break;
																}
															}
														}
													}
													// End Check if the phone being added already has the plan exactly compatible to it in the package and not the other plan in the group
 												}else{
 													isCompatibleMsg = true;
 													
 												}
											}
											else if (pkgProductType.equalsIgnoreCase(PHONE_PRODUCT) && productType.equalsIgnoreCase(PLAN_PRODUCT)) {
												if (isCompatiblePhoneAndPlan(productType, pkgProduct)) {
													tools.updateCommerceItem(((CricketCommerceItemImpl) commItems.get(commItems.size()-1)),productType,existingpackage);
													isCompatiable = true;
 												}else {
 													// to show incompatible msg
 													isCompatibleMsg = true;
 													
 												}											
											} 
											if (isCompatiable)
												break;
										}
										if (isCompatiable)
											break;
									}
								}
							}
							if (!isCompatiable) {
								cricketPackage = tools.createCricketPackage();
								tools.updateCommerceItem(commerceItem, productType,cricketPackage);
								List<CricketPackage> cricketPackages = order.getCktPackages();
								cricketPackages.add(cricketPackage);
								order.setCktPackages(cricketPackages);
								
								if(!isSameProduct && !pRequest.getRequestURIWithQueryString().contains(ADD_ANOTHER_PACKAGE)){
									if (productType.equalsIgnoreCase(PLAN_PRODUCT) && isCompatibleMsg) {
										String msg = formatUserMessage("newPackageForPlan", pRequest,pResponse);
										String propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
										setReloadDrawerCartDiv(Boolean.TRUE);
										 addFormException(new DropletFormException(msg,null, propertyPath, "newPackageForPlan"));
										setCartOpen(true);
									}else if(productType.equalsIgnoreCase(PHONE_PRODUCT) && isCompatibleMsg){
										String msg = formatUserMessage("newPackageForPhone", pRequest,pResponse);
										String propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
										setReloadDrawerCartDiv(Boolean.TRUE);
										addFormException(new DropletFormException(msg,null, propertyPath, "newPackageForPhone"));
										setCartOpen(true);
									}
							   }
							} 
						}						
					}
				}
			}
			
			runProcessRepriceOrder(getOrder(), getUserPricingModels(), getUserLocale(), getProfile(), createRepriceParameterMap());
 		} catch (CommerceItemNotFoundException e) {
			if (isLoggingError()) {
				vlogError("CommerceItemNotFound Exception in addItemToOrder of CricketCartModifierFormhandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		} catch (InvalidParameterException e) {
			if (isLoggingError()) {
				vlogError("InvalidParameter Exception in addItemToOrder of CricketCartModifierFormhandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		} catch (CommerceException e) {
			if (isLoggingError()) {
				vlogError("Commerce Exception Exception in addItemToOrder of CricketCartModifierFormhandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		} catch (RepositoryException e) {
			if (isLoggingError()) {
				vlogError("RepositoryException Exception in addItemToOrder of CricketCartModifierFormhandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);;
			}
		} catch (RunProcessException e) {
			if (isLoggingError()) {
				vlogError("RunProcessException Exception in addItemToOrder of CricketCartModifierFormhandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
			}
		}
		if (isLoggingDebug()) {						
	    		logDebug("Exit from CricketCartModifierFormHandler class of addItemToOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
	
	private void addHPPAddonToCart() {
		if (null != getUserSessionBean().getHppAddOns() && !getUserSessionBean().getHppAddOns().isEmpty() 
				&& isHPPPresentInAccount() && !validESPAndAccountHPP()) {
		try {
			CricketCommerceItemImpl ci = (CricketCommerceItemImpl) getCommerceItemManager()
			.createCommerceItem(getHppSKUFromESP(), getHppProductFromESP(),
					getQuantity());
			if (ci != null) {
				ci.setCricItemTypes(getCartConfiguration().getChangeAddOnItemType());
				getCommerceItemManager().addItemToOrder(getOrder(), ci);
				Map<String, Boolean> removedAddonCompatibility = ((CricketOrderImpl) getOrder()).getRemovedAddonCompatibility();
				removedAddonCompatibility.put(getHppProductId(), Boolean.FALSE);
				((CricketOrderImpl) getOrder()).setRemovedAddonCompatibility(removedAddonCompatibility);
			}
		} catch (CommerceException e) {
			logError(
					"CommerceException Occured while addHPPAddonToCart for order with ID :"
							+ getOrder().getId(), e);
		}
		}
	}
	
	private boolean isHPPPresentInAccount() {
		boolean isHPPPresentInAccount = Boolean.FALSE;
		UserAccountInformation userAccountInfo = getUserAccountInformation();
		if(userAccountInfo!=null && userAccountInfo.getSubscribers()!=null) {
			
			List<SubscriberVO> subscribers = userAccountInfo.getSubscribers();
			if (null != subscribers && !subscribers.isEmpty()) {
				for (SubscriberVO subscriberVO : subscribers) {
					List<OfferingsVO> additionalOfferings = new ArrayList<OfferingsVO>();
					RepositoryItem addOnProductItem = null;
					List<RepositoryItem> addon_skus = new ArrayList<RepositoryItem>();
					if(cricketProfile.getMdn().equalsIgnoreCase(subscriberVO.getMdn())) {
						  additionalOfferings = subscriberVO.getAdditionalOfferings();
						for (OfferingsVO offeringsVO : additionalOfferings) {
							if (null != offeringsVO && offeringsVO.getOfferTypeId() != 3 
									&& !StringUtils.isEmpty(offeringsVO.getOfferingName())
									&& !REC_OFFERING_NAME.equals(offeringsVO.getOfferingName())) {
								try {
									 addOnProductItem = getCatalogRepository().getItem(offeringsVO.getOfferingName(),ITEM_DESC_ADDON_PRODUCT);
									if (null != addOnProductItem) {
										if (null != addOnProductItem.getPropertyValue(CricketCommonConstants.PROP_PARC_GROUP_NAME) 
												&& addOnProductItem.getPropertyValue(CricketCommonConstants.PROP_PARC_GROUP_NAME).equals(CricketCommonConstants.HANDSET_PROTECTION_GROUP_NAME)) {
											setHppProductId(addOnProductItem.getRepositoryId());
											isHPPPresentInAccount = Boolean.TRUE;
											addon_skus =  (List<RepositoryItem>) addOnProductItem.getPropertyValue(PROP_CHILD_SKUS);
											if (null != addon_skus && !addon_skus.isEmpty()) {
												for (RepositoryItem repositoryItem : addon_skus) {
													setHppSKUId(repositoryItem.getRepositoryId());
												}
											}
										}
									
									}
									} catch (RepositoryException e) {      
										if(isLoggingError()){
											vlogError("Repository Exception in isHPPPresentInAccount of CricketCartModifierFormhandler::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  getOrder().getId(), e);
										}
									}
							}
							}
						}
							}
			}
		}
		return isHPPPresentInAccount;
	}
	
	private boolean validESPAndAccountHPP() {
		boolean isAccountHPPSameAsESPHPP = Boolean.FALSE;
		if (null != getUserSessionBean().getHppAddOns() 
				&& !getUserSessionBean().getHppAddOns().isEmpty()) {
			for (InquireFeaturesResponseVO inquireFeaturesResponseVO : getUserSessionBean().getHppAddOns()) {
				RepositoryItem [] addOnProducts= null;
				RepositoryItem addOnProduct = null;
				try {
					 RepositoryView view = getCatalogRepository().getView(CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT);
						
						RqlStatement statement = RqlStatement.parseRqlStatement(CricketCommonConstants.OFFER_ID_QUERY);
						
			  				Object params[] = new Object[1];
			  				params[0] = inquireFeaturesResponseVO.getId();
			  				addOnProducts =statement.executeQuery (view, params);
			    		    if(addOnProducts !=null && addOnProducts.length > 0) {
			    		    	addOnProduct = addOnProducts[0];
			    		    	if (null != addOnProduct) {
			    		    		setHppProductFromESP(addOnProduct.getRepositoryId());
			    		    		List<RepositoryItem> addon_skus = new ArrayList<RepositoryItem>();
			    		    		addon_skus =  (List<RepositoryItem>) addOnProduct.getPropertyValue(PROP_CHILD_SKUS);
									if (null != addon_skus && !addon_skus.isEmpty()) {
										for (RepositoryItem repositoryItem : addon_skus) {
											setHppSKUFromESP(repositoryItem.getRepositoryId());
										}
									}
			    		    	if (addOnProduct.getRepositoryId().equals(getHppProductId())) {
			    		    		isAccountHPPSameAsESPHPP = Boolean.TRUE;
			    		    	}
			    		    	}
			    		    }
			    		  }
			    		  catch(RepositoryException exc) {
			    			  vlogError("Repository Exception in validESPAndAccountHPP of CricketCartModifierFormhandler::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  getOrder().getId(), exc);
			    		  }
			}
		}
		return isAccountHPPSameAsESPHPP;
	}
	/**
	 * @param commerceItem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isPlanDownGrade(CricketCommerceItemImpl commerceItem) {
		boolean isPlanDownGrade = Boolean.FALSE;
		String removedPlanSkuID = getUpgradeItemDetailsSessionBean().getRemovedPlanSkuId();
		RepositoryItem removePlanSku = null;
		Double removedItemPrice = 0.0;
		Double changeItemPrice = 0.0;
		try {
			if (!StringUtils.isEmpty(removedPlanSkuID)) {
				removePlanSku = getCatalogTools().findSKU(
						removedPlanSkuID);
			} else if (!StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getRemovedPlanId())) {
				RepositoryItem planProduct = getCatalogTools().findProduct(
						getUpgradeItemDetailsSessionBean().getRemovedPlanId());
				if (null != planProduct) {
					List<RepositoryItem> childSkus = (List<RepositoryItem>) planProduct.getPropertyValue(PROP_CHILD_SKUS);
					if(childSkus != null && !childSkus.isEmpty() && childSkus.size()>0) {
						removePlanSku = childSkus.get(0);
					}
				}
			}
			if (null != commerceItem &&  null != commerceItem.getAuxiliaryData().getCatalogRef()) {
				changeItemPrice = (Double) ((RepositoryItem) commerceItem.getAuxiliaryData().getCatalogRef()).getPropertyValue(PROP_LIST_PRICE);
			}
			if (null != removePlanSku) {
				removedItemPrice = (Double) removePlanSku.getPropertyValue(PROP_LIST_PRICE);
				if (removedItemPrice > changeItemPrice) {
					isPlanDownGrade = Boolean.TRUE;
				}
			}
			
		} catch (RepositoryException e) {
			if (isLoggingError()) {
				logDebug("Repository Exception in isPlanDownGrade of CricketCartModifierFormHandler", e);
			}
		}
		//((CricketOrderImpl)getOrder()).setDownGrade(isPlanDownGrade);
		return isPlanDownGrade;
	}
	/**
	 * @return
	 */
	private boolean checkCompatiblePackagesInOrder() {
		boolean checkCompatiblePackagesInOrder = Boolean.FALSE;
		List<CricketPackage> cktPackages = ((CricketOrderImpl) getOrder()).getCktPackages();
		String productType = findProductType(getProductId());
		for (CricketPackage cricketPackage : cktPackages) {
			CricketCommerceItemImpl[] commerceItems = (CricketCommerceItemImpl[]) ((CricketOrderTools) getOrderManager().getOrderTools()).getCommerceItemsForPackage(cricketPackage,(CricketOrderImpl) getOrder());
			boolean isPhone = false;
			boolean isPlan = false;
			RepositoryItem planProduct = null;
			RepositoryItem phoneProduct = null;
			RepositoryItem pkgProduct = null;
			String pkgProductType = null;
				for (CricketCommerceItemImpl pkgCommerceItem : commerceItems) {
					  pkgProduct = (RepositoryItem) pkgCommerceItem.getAuxiliaryData().getProductRef();
					  pkgProductType = (String) pkgProduct.getPropertyValue(TYPE);
					if (pkgProductType.equalsIgnoreCase(PLAN_PRODUCT)) {
						isPlan = true;
						planProduct = pkgProduct;
					}
					if (pkgProductType.equalsIgnoreCase(PHONE_PRODUCT)) {
						isPhone = true;
						phoneProduct = pkgProduct;
					}
				}
				if (isPlan && isPhone) {
					continue;
				}
				if (isPhone && !isPlan && !StringUtils.isEmpty(productType) && productType.equalsIgnoreCase(PLAN_PRODUCT) && null != phoneProduct) {
					checkCompatiblePackagesInOrder = isCompatiblePhoneAndPlan(productType, phoneProduct);
					break;
				}
				if (!isPhone && isPlan && !StringUtils.isEmpty(productType) && productType.equalsIgnoreCase(PHONE_PRODUCT) && null != planProduct) {
					checkCompatiblePackagesInOrder = isCompatiblePhoneAndPlan(productType, planProduct);
					break;
				}
		}
		return checkCompatiblePackagesInOrder;
	}
	
	/**
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean handleClearRemovedAddonFromMap(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		String l_sMethod = "CricketCartModifierOrderFormHandler.handleClearRemovedAddonFromMap";
		RepeatingRequestMonitor l_oRepeatingRequestMonitor = getRepeatingRequestMonitor();
		if ((l_oRepeatingRequestMonitor == null)
				|| (l_oRepeatingRequestMonitor.isUniqueRequestEntry(l_sMethod))) {
			Transaction l_oTransaction = null;
			try {
				synchronized (getOrder()) {
					l_oTransaction = ensureTransaction();
					if (!StringUtils.isEmpty(getRemovalAddonId())) {
						for(Iterator<Map.Entry<String, String>> it = ((CricketOrderImpl) getOrder()).getRemovedAddons().entrySet().iterator(); it.hasNext();) {
						      Map.Entry<String, String> entry = it.next();
						      if(entry.getKey().equals(getRemovalAddonId())) {
						        it.remove();
						      }
						    }
						for(Iterator<Map.Entry<String, Boolean>> iter = ((CricketOrderImpl) getOrder()).getRemovedAddonCompatibility().entrySet().iterator(); iter.hasNext();) {
						      Map.Entry<String, Boolean> entry = iter.next();
						      if(entry.getKey().equals(getRemovalAddonId())) {
						    	  iter.remove();
						      }
						    }
					}
					getOrderManager().updateOrder(getOrder());
				}
			} catch (CommerceException commerceException) {
				logError(
						"CommerceException Occured while updating order with ID :"
								+ getOrder().getId(), commerceException);
			} catch (Exception exception) {
				logError("Exception Occured while updating order with ID :"
						+ getOrder().getId(), exception);
			} finally {
				if (l_oTransaction != null) {
					commitTransaction(l_oTransaction);
				}
				if (l_oRepeatingRequestMonitor != null) {
					l_oRepeatingRequestMonitor.removeRequestEntry(l_sMethod);
				}
			}
		}
		return false;
	}
	
	/**
	 * @return
	 * @throws RepositoryException
	 * @throws CommerceException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Boolean> setRemovedAddonsCompatibility(boolean isChangeFeature) throws RepositoryException, CommerceException {
		//getUserSessionBean().getMandatoryOffersUpgradePlanMap().;
		String phoneId = getUpgradeItemDetailsSessionBean().getRemovedPhoneId();
		Map<String, String> addons = getUpgradeItemDetailsSessionBean().getRemovedAddons();
		Map<String, Boolean> addonCompatibility = new HashMap<String, Boolean> ();
		Profile profile = getProfileServices().getCurrentProfile();
		if (isChangeFeature && null != addons) {
			for (String removedAddonId: addons.keySet()) {
				addonCompatibility.put(removedAddonId, Boolean.TRUE);
			}
			List<CricketCommerceItemImpl> cricketCommerceItemList = (List<CricketCommerceItemImpl>) getOrder().getCommerceItems();
			for (CricketCommerceItemImpl cricketCommerceItemImpl : cricketCommerceItemList) {
			if(CITYPE_RMOEVEDADDON.equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())){
				if(addonCompatibility !=null && addonCompatibility.size() > 0){
					addonCompatibility.remove(cricketCommerceItemImpl.getAuxiliaryData().getProductId());				    					
				}
			}
			}
		} else {
			String modelNumber = getCricketProfile().getDeviceModel();
		if (!StringUtils.isEmpty(modelNumber) && null != addons && !addons.isEmpty() && addons.size() > 0) {
				String planCode = null;
				String marketCode = (String) getProfile().getPropertyValue(PROP_MARKET_ID);
				boolean hasEsnHistory = Boolean.FALSE;
		    	boolean isCricketPhone = Boolean.TRUE;
		    	
		    	if(isChangeFeature){
		    		 planCode = getUpgradeItemDetailsSessionBean().getRemovedPlanId();
		    	}else{
		    		 planCode = getProductId();
		    	}
		    	
		    	if (!StringUtils.isEmpty(modelNumber) && !StringUtils.isEmpty(planCode) && !StringUtils.isEmpty(marketCode)) {
			    	Map<String, List<InquireFeaturesResponseVO>> planAddOnsMap = getDisplayFeaturesManager().getCompatibleAddons(
																	modelNumber, CricketESPConstants.PHONE_TYPE_VOICE, planCode, marketCode,
																	CricketESPConstants.TRANSACTION_TYPE_RRC, (ArrayList) getCricketProfile().getUserPurchasedOfferingProducts(), hasEsnHistory, isCricketPhone, CricketESPConstants.CKT_SALES_CHANNEL_NAME,getOrder().getId());
			    	if  (null != planAddOnsMap && !planAddOnsMap.isEmpty() && planAddOnsMap.size() > 0) {
				    	List<InquireFeaturesResponseVO>  optionalAddonList = planAddOnsMap.get(OPTIONAL_ADDONS);
				    	if  (null != optionalAddonList && !optionalAddonList.isEmpty() && optionalAddonList.size() > 0) {
				    		
				    		//storing manadatory Addon + Admin Fee add on in the session bean to use in ESP
					    		InquireFeaturesResponseVO administrationFeeFeature = getCricketESPAdapterHelper().getAdministrationFeeAddOn(optionalAddonList);
	
					    		InquireFeaturesResponseVO activationFeeFeature = getCricketESPAdapterHelper().getActivationFeeAddOn(optionalAddonList);
								if(activationFeeFeature != null && activationFeeFeature.getPrice() != null) {
						    		profile.setPropertyValue(ACTIVATION_FEE, activationFeeFeature.getPrice().doubleValue());
						    	}
								
					    		Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersMap = null;
					    		List<InquireFeaturesResponseVO> mandatoryAddOns = planAddOnsMap.get(MANDATORY_ADDONS);
	
					    		mandatoryOffersMap = getUserSessionBean().getMandatoryOffersMap();
					    		if (mandatoryOffersMap == null) {
					    			mandatoryOffersMap = new HashMap<String, List<InquireFeaturesResponseVO>>();
					    		}
					    		if(mandatoryAddOns != null && mandatoryAddOns.size() > 0) {
					    			if(administrationFeeFeature != null){
					    				//Administration Fee addOn needs to be sent to billing system for charging the user on monthly basis
					    				//Hence adding to mandatoryAddOns list which will be used in CreateActivationQuote request for building CricketOfferingCodes
					    				mandatoryAddOns.add(administrationFeeFeature);
					    			}
					    		}
					    		if(planCode != null && mandatoryAddOns != null && mandatoryAddOns.size() > 0){
					    			mandatoryOffersMap.put(planCode, mandatoryAddOns);
					    			getUserSessionBean().setMandatoryOffersMap(mandatoryOffersMap);
					    		}				    		
				    		// end of add-on storing code
				    		
				    		RepositoryItem removedAddonProduct = null;
				    		String offerId = null;
				    		for (String removedAddonId: addons.keySet()) {
				    			boolean isCompatible = Boolean.FALSE;
					    		if (!StringUtils.isEmpty(removedAddonId)) {
					    			  removedAddonProduct = getCatalogTools().findProduct(removedAddonId);
					    			  offerId = (String) removedAddonProduct.getPropertyValue(PROP_OFFER_ID);
					    			for (InquireFeaturesResponseVO optionalAddon : optionalAddonList) {
					    				optionalAddon.getId();
					    				if (!StringUtils.isEmpty(offerId) && offerId.equalsIgnoreCase(optionalAddon.getId())) {
					    					isCompatible = Boolean.TRUE;
					    				}
						    		}
					    		}
					    		addonCompatibility.put(removedAddonId, isCompatible);
				    		}
				    		List<CricketCommerceItemImpl> cricketCommerceItemList = (List<CricketCommerceItemImpl>) getOrder().getCommerceItems();
				    		 List<String> removalIds = new ArrayList<String>();
				    		 List<String> userSelectedRemovableAddOns = new ArrayList<String>();
				    			for (CricketCommerceItemImpl cricketCommerceItemImpl : cricketCommerceItemList) {
				    				if (ITEM_DESC_ADDON_PRODUCT.equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())
				    						|| "changeAddOn".equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())) {
				    				boolean compatibleAddon = Boolean.FALSE;
				    				 offerId = (String) ((RepositoryItem) cricketCommerceItemImpl.getAuxiliaryData().getProductRef()).getPropertyValue(PROP_OFFER_ID);
				    				if (!StringUtils.isEmpty(offerId)) {
				    					for (InquireFeaturesResponseVO optionalAddon : optionalAddonList) {
				    						if (!StringUtils.isEmpty(offerId) && offerId.equalsIgnoreCase(optionalAddon.getId())) {
				    							compatibleAddon = Boolean.TRUE;
				    						}
				    					}
				    					if (!compatibleAddon) {
				    						removalIds.add(cricketCommerceItemImpl.getId());
				    					}
				    				}
				    			}else if(CITYPE_RMOEVEDADDON.equalsIgnoreCase(cricketCommerceItemImpl.getCricItemTypes())){
				    				if(addonCompatibility !=null && addonCompatibility.size() > 0){
				    					addonCompatibility.remove(cricketCommerceItemImpl.getAuxiliaryData().getProductId());				    					
				    				}
				    			}
				    			}				    		
				    			
				    			for (String cricketCommerceItemId : removalIds) {
				    				getCommerceItemManager().removeItemFromOrder(getOrder(),cricketCommerceItemId);
				    			}
			    	}
				    	List<InquireFeaturesResponseVO>  hppAddons = planAddOnsMap.get(HPP_ADDONS);
				    	if (null != hppAddons && !hppAddons.isEmpty()) {
				    		getUserSessionBean().setHppAddOns(hppAddons);
				    	}
		    	}
			}
	    	
		}
	}
		return addonCompatibility;
	}
	
	/**
	 * 
	 */
	private void setRemovedItemDetailsInOrder() {
		CricketOrderImpl order = (CricketOrderImpl) getOrder();
		order.setRemovedPhoneId(getUpgradeItemDetailsSessionBean().getRemovedPhoneId());
		order.setRemovedPhoneSkuId(getUpgradeItemDetailsSessionBean().getRemovedPhoneSkuId());
		order.setRemovedPlanId(getUpgradeItemDetailsSessionBean().getRemovedPlanId());
		order.setRemovedPlanSkuId(getUpgradeItemDetailsSessionBean().getRemovedPlanSkuId());
		order.setRemovedAddons(getUpgradeItemDetailsSessionBean().getRemovedAddons());
		order.setUpgradeMdn(getUpgradeItemDetailsSessionBean().getMdn());
		order.setUpgradeModelNumber(getUpgradeItemDetailsSessionBean().getModelNumber());
	}
	
	/**
	 * 
	 */
	private void clearRemovedItemDetailsInOrder() {
		CricketOrderImpl order = (CricketOrderImpl) getOrder();
		order.setRemovedPhoneId(null);
		order.setRemovedPhoneSkuId(null);
		order.setRemovedPlanId(null);
		order.setRemovedPlanSkuId(null);
		order.setRemovedAddons(null);
		order.setUpgradeMdn(null);
		order.setUpgradeModelNumber(null);
		order.setRemovedAddonCompatibility(null);
	}
	
	/**
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean handleClearRemovedAddonData(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		//Need to check the intention of this handle method
		RepeatingRequestMonitor rrm = getRepeatingRequestMonitor();
		String myHandleMethod = "CricketCartModifierFormHandler.handleAddItemToOrder";

		if ((rrm == null) || (rrm.isUniqueRequestEntry(myHandleMethod))) {
			Transaction tr = null;
			try {
					tr = ensureTransaction();
					if (tr == null ) {
						tr = getTransactionManager().getTransaction();
					} 
					CricketOrderImpl order = (CricketOrderImpl) getOrder();
					order.setRemovedAddons(null);
					getOrderManager().updateOrder(getOrder());
				} catch (CommerceException commerceException) {
					logError("CommerceException Occured while updating order with ID :"
							+ getOrder().getId(), commerceException);
				} catch (Exception exception) {
					logError("Exception Occured while updating order with ID :"
							+ getOrder().getId(), exception);
				} finally {
					if (tr != null) {
						commitTransaction(tr);
					}
					if (rrm != null) {
						rrm.removeRequestEntry(myHandleMethod);
					}
				}
			}
		if (StringUtils.isEmpty(getDeletePackageSuccessURL())) {
			setDeletePackageSuccessURL("/");
		}
		return checkFormRedirect(
				getDeletePackageSuccessURL(), getDeletePackageFailureURL(), pRequest,
				pResponse);
	}
	
	/**
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean handleDuplicatePackage(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		
		RepeatingRequestMonitor rrm = getRepeatingRequestMonitor();
		String myHandleMethod = "CricketCartModifierFormHandler.handleDuplicatePackage";
		Transaction tr = null;
		boolean packageItemflag = false;
		CricketPackage packageItem=null;
		boolean checkFormRedirect = false;
		CricketPackage packageInCitem=null;
		CricketOrderTools tools = (CricketOrderTools) getOrderManager().getOrderTools();
		CricketOrderImpl order = (CricketOrderImpl) getOrder();

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}
			}
				
		if (isLoggingDebug()) {				
	    		logDebug("Entering into CricketCartModifierFormHndler class of handleDuplicatePackage() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		CricketCommerceItemManager ciItemManager =(CricketCommerceItemManager) getCommerceItemManager();
		ciItemManager.setProductType(null);
		if ((rrm == null) || (rrm.isUniqueRequestEntry(myHandleMethod))) {
			try {
				tr = ensureTransaction();
				//String packageId = getDuplicatePackageId(); 
				List<String> listOfProductIds = new ArrayList<String>();
				List<String> listOfSkuIds = new ArrayList<String>();
				synchronized (getOrder()) {
				List<CricketCommerceItemImpl> commerceItemswithUniquePackages = ((CricketOrderTools) getOrderManager()
						.getOrderTools())
						.getCommerceItemswithUniquePackageId(getDuplicatePackageId(), (CricketOrderImpl) getOrder());
				String[] productIds = null;
				String[] skuIds = null;
				if (null != commerceItemswithUniquePackages && commerceItemswithUniquePackages.size() > 0) {					
					productIds = new String[commerceItemswithUniquePackages.size()];
					skuIds = new String[commerceItemswithUniquePackages.size()];					
						//CommerceItem commerceItem = null;
						for (CricketCommerceItemImpl commerceItem : commerceItemswithUniquePackages) {
							listOfProductIds.add(commerceItem.getAuxiliaryData()
									.getProductId());
							listOfSkuIds.add((String) commerceItem.getCatalogRefId());
						}
				}
				for (int i = 0; i < listOfProductIds.size(); i++) {
					productIds[i] = listOfProductIds.get(i);
				}
				for (int i = 0; i < listOfSkuIds.size(); i++) {
					skuIds[i] = listOfSkuIds.get(i);
				}
				setProductIds(productIds);
				setCatalogRefIds(skuIds);
				setQuantity(1);
				 
				//super.handleAddMultipleItemsToOrder(pRequest, pResponse);
				preDuplicatePackage(pRequest, pResponse);
				if (getFormError()) {
					checkFormRedirect(getDuplicatePackageSuccessUrl(),
							getDuplicatePackageErrorUrl(), pRequest,
							pResponse);
				}
				/*if (!StringUtils.isEmpty(getUpgradeItemDetailsSessionBean().getUserIntention())
					&& StringUtils.isEmpty(getUpgradeItemFlow())) {
					clearShoppingCart();
				} */
				if(!getFormError()){
					 addMultipleItemsToOrder(pRequest, pResponse);
					
					 @SuppressWarnings("unchecked")
					List<CricketCommerceItemImpl> commerceItems = getOrder().getCommerceItems();
					 String productType = null;
					 for (CricketCommerceItemImpl commerceItem : commerceItems) {
						 packageInCitem = tools.getCricketPackage(commerceItem.getPackageId(), order);
							if (packageInCitem == null && !packageItemflag) {
								packageItem = tools.createCricketPackage();
								packageItemflag = true;
								List<CricketPackage> packItems = order.getCktPackages();
								if (!order.getCktPackages().contains(packageInCitem)) {
									packItems.add(packageItem);
									order.setCktPackages(packItems);
								}
							}
						 productType = findProductType((String)commerceItem.getAuxiliaryData().getProductId());
						 if (packageInCitem==null && (productType.equalsIgnoreCase(PHONE_PRODUCT)
									|| productType.equalsIgnoreCase(PLAN_PRODUCT)
									|| productType.equalsIgnoreCase(ITEM_DESC_ADDON_PRODUCT))) {
							tools.updateCommerceItem(commerceItem, productType, packageItem);
						 }
	
					}
				}
				 postDuplicatePackage(pRequest,pResponse);
				 getCartCookie().setOpenCart(OPEN_CART);
				if (getFormError()) {
					return checkFormRedirect(null,
							"../index.jsp", pRequest, pResponse);
				} else {
					String redirectURL = addOpenCartURLParam(getDuplicatePackageSuccessUrl());
					pResponse.sendRedirect(redirectURL);

					checkFormRedirect = checkFormRedirect(
							getDuplicatePackageSuccessUrl(),  getDuplicatePackageErrorUrl(), pRequest,
							pResponse);
				}
				getOrderManager().updateOrder(getOrder());
				}
				runProcessRepriceOrder(getOrder(), getUserPricingModels(), getUserLocale(), getProfile(), createRepriceParameterMap());
				getUserSessionBean().setAutoBillPayment(getUserSessionBean().isAutoBillPayment());
			} catch (CommerceItemNotFoundException e) {
				if (isLoggingError()) {
					vlogError("CommerceItemNotFoundException  in CricketCartModifierFormHandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			} catch (InvalidParameterException e) {
				if (isLoggingError()) {
					vlogError("InvalidParameterException  in CricketCartModifierFormHandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			} catch (CommerceException e) {
				if (isLoggingError()) {
					vlogError("CommerceException  in CricketCartModifierFormHandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			} catch (RunProcessException e) {
				if (isLoggingError()) {
					vlogError("RunProcessException  while repricing the duplication of CricketCartModifierFormHandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			}finally {
				if (tr != null) {
					commitTransaction(tr);
				}

				if (rrm != null) {
					rrm.removeRequestEntry(myHandleMethod);
				}
			}
		}
		if (isLoggingDebug()) {				
	    		logDebug("Exiting form CricketCartModifierFormHndler class of handleDuplicatePackage() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return checkFormRedirect;

	}

	/**
	 * @param pRequest
	 * @param pResponse
	 */
	private void postDuplicatePackage(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) {
		getCartCookie().setOpenCart(OPEN_CART);
		
	}

	/**
	 * @param pRequest
	 * @param pResponse
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void preDuplicatePackage(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		boolean pkgSizeForCust = numberOfPackage((String)getProfile().getPropertyValue(NETWORK_PROVIDER),pRequest, pResponse);
	}

	/**
	 * @param commerceItem
	 * @return
	 */
	private List<RepositoryItem> getLoosingPronotionsFromCItem(CommerceItem commerceItem) {
		Collection<RepositoryItem> pPromotions = new ArrayList<RepositoryItem>();
		String productId = commerceItem.getAuxiliaryData().getProductId();
		List<RepositoryItem> loosingPromoItems = new ArrayList<RepositoryItem>();
		try {
			RepositoryItem productItem = getCatalogTools().findProduct(
					productId);
			String productType = (String) productItem.getPropertyValue(TYPE);
			if (ACCESSORY_PRODUCT.equalsIgnoreCase(productType)) {
				getPromotionTools().getOrderPromotions(getOrder(), pPromotions);
				getPromotionTools()
						.getItemPromotions(commerceItem, pPromotions);
			} else {
				getPromotionTools().getOrderPromotions(getOrder(), pPromotions);
				getPromotionTools().getItemPromotions(
						(CommerceItem) commerceItem, pPromotions);
				getPromotionTools().getShippingPromotions(
						(ShippingGroup) getOrder().getShippingGroups().get(0),
						pPromotions);
				getPromotionTools().getTaxPromotions(getOrder(), pPromotions);
			}
			for (RepositoryItem promotionItem : pPromotions) {
				loosingPromoItems.add(promotionItem);
			}

		} catch (RepositoryException e) {
			if (isLoggingError()) {
				logDebug(
						"Repository Exception in getLoosingPronotions of CricketCartModifierFormHandler",
						e);
			}
		}

		return loosingPromoItems;
	}
	
	/**
	 * 
	 * @param packageId
	 */
	protected void clearPackageCommerceItems(String cricketPackageId, CricketOrderImpl cricketOrder) {
		List<CricketCommerceItemImpl> commerceItems = ((CricketOrderTools) getOrderManager().getOrderTools()).getCommerceItemswithUniquePackageId(cricketPackageId, cricketOrder);
		if (null != commerceItems && !commerceItems.isEmpty() && commerceItems.size() > 0) {	
			String [] commerceItemIds = new String[commerceItems.size()];
			for (int i=0 ; i < commerceItems.size() ; i++) {
				if (null != commerceItems.get(i)) {
					commerceItemIds[i] = commerceItems.get(i).getId();
				}
			}
			setRemovalCommerceIds(commerceItemIds);
		}
	}
	/**
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean handleDeletePackage(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		boolean checkFormRedirect = false;
		String l_sMethod = "CricketCartModifierOrderFormHandler.handleDeletePackage";
		RepeatingRequestMonitor l_oRepeatingRequestMonitor = getRepeatingRequestMonitor();
		CricketOrderImpl order = (CricketOrderImpl) getOrder(); 
		String addonProductType=null;
		 String[] remivalList; 
		 String commerceItemId = null;
		if ((l_oRepeatingRequestMonitor == null)
				|| (l_oRepeatingRequestMonitor.isUniqueRequestEntry(l_sMethod))) {
			Transaction l_oTransaction = null;
			try {
				l_oTransaction = ensureTransaction();
				
				 List<CricketCommerceItemImpl> commerceItemswithUniquePackages = ((CricketOrderTools) getOrderManager()
							.getOrderTools())
							.getCommerceItemswithUniquePackageId(getDeletePacakgeId(), (CricketOrderImpl) getOrder()); 
				 String addonToRemove = getAddonCommerceItemId();
				 RepositoryItem parentAddonItem=null;
				 if(!StringUtils.isBlank(addonToRemove)) {				 
				 CommerceItem addonItem = getOrder().getCommerceItem(addonToRemove);
				 String productId = addonItem.getCatalogRefId();
				 parentAddonItem = getCatalogTools().findProduct(productId);
				 }
				 if(null!=commerceItemswithUniquePackages){
					 if(getAddonProductType()== null){
					 remivalList = new String[commerceItemswithUniquePackages.size()];
					 } else {
						 remivalList = new String[commerceItemswithUniquePackages.size()];
					 }
					int removeIndex=0;
						for (CricketCommerceItemImpl commerceItem : commerceItemswithUniquePackages) {
							addonProductType = (String) commerceItem.getCricItemTypes();
							commerceItemId = commerceItem.getId();
							if(null != getAddonProductType() && getAddonProductType().equalsIgnoreCase(addonProductType)){
								removeIndex = removeAddOns(remivalList, commerceItemId, addonToRemove, parentAddonItem, removeIndex);
							}  
							else if(getAddonProductType()==null){
								remivalList[removeIndex] = commerceItemId;
								removeIndex++;
							}
						}
						String[] finalAddonRemovList = new String[removeIndex];
						int ind=0;
						for(String removeList:remivalList){
							if(!StringUtils.isBlank(removeList)){
								finalAddonRemovList[ind]=removeList;
								ind++;
							}
				
						}
						setRemovalCommerceIds(finalAddonRemovList);
				 }
				synchronized (order) {
					deleteItems(pRequest, pResponse);
					/* Iterator<CricketPackage> iter = ((CricketOrderImpl)getOrder()).getCktPackages().listIterator();
					while(iter.hasNext()){
						CricketPackage cricketPackage = (CricketPackage) iter.next();*/
						if(getAddonProductType()==null && !StringUtils.isEmpty(getDeletePacakgeId())){
							try {
								((CricketOrderImpl)getOrder()).removeCricketPackage(getDeletePacakgeId());
							} catch (InvalidParameterException e) {
								if (isLoggingError()) {
									logDebug(
											"InvalidParameterException in handleDeletePackage of CricketCartModifierFormHandler",
											e);
							}
							}
						}
					//}
						if(getCartCookie()!=null){
							getCartCookie().setPackageId(null);
							getCartCookie().setProductType(null); 
							getCartCookie().setEditActionFromCart(false);
						}
						getOrderManager().updateOrder(getOrder());
						getUserSessionBean().setAutoBillPayment(false);
				}
				//start code for spanish lang 
				 String locale = getCricketProfile().getLanguageIdentifier();
				 if(locale != null && locale.equalsIgnoreCase("es")){
					 String deleteSuccessURL = "http://espanol.mycricket.com/sdtest/";//getDeletePackageSuccessURL();
						int successURLlength = deleteSuccessURL.length();
						String[] successURLSplit = deleteSuccessURL.split(".com");
						String siteUrl = successURLSplit[0].concat(".com");
						int successURLSplitLength = siteUrl.length();
						String  successUrl = deleteSuccessURL.substring(successURLSplitLength, successURLlength);
						setDeletePackageSuccessURL(successUrl);
				 }
				// end code for spanish lang 
				
				getCartCookie().setOpenCart(OPEN_CART);
				if(getOrder().getCommerceItems().isEmpty()){
					getCartCookie().setOpenCart(null);	
					return checkFormRedirect(getDeletePackageSuccessURL(),
							null, pRequest, pResponse);
				}
				if (getFormError()) {
					return checkFormRedirect(null,
							getDeletePackageFailureURL(), pRequest, pResponse);
				}else{
				// TODO Change the URLS.
				String successURL =	addOpenCartURLParam(getDeletePackageSuccessURL());
				setDeletePackageSuccessURL(successURL);
				checkFormRedirect = checkFormRedirect(getDeletePackageSuccessURL(),						
						null, pRequest, pResponse); 
				}
			} catch (CommerceException e) {
				if (isLoggingError()) {
					logDebug(
							"InvalidParameterException  in handleDeletePackage of CricketCartModifierFormHandler",
							e);
				}
			} catch (RepositoryException e) {
				if (isLoggingError()) {
					logDebug(
							"RepositoryException:",
							e);
				}
			} finally {
				if (getFormError()) {
					try {
						setTransactionToRollbackOnly();
					} catch (SystemException pSystemException) {
						if (isLoggingError()) {
							logError("SystemException", pSystemException);
						}
					}
				}
				// Commit or rolback the transaction depending on the status.
				if (l_oTransaction != null) {
					commitTransaction(l_oTransaction);
				}
				if (l_oRepeatingRequestMonitor != null) {
					l_oRepeatingRequestMonitor.removeRequestEntry(l_sMethod);
				}
			}
		}
		return checkFormRedirect;
	}
	
	/**
	 * @param remivalList
	 * @param commerceItemId
	 * @param addonToRemove
	 * @param parentAddonItem
	 * @return removeIndex
	 */
	private int removeAddOns(String[] remivalList, String commerceItemId,
			String addonToRemove, RepositoryItem parentAddonItem,
			 int removeIndex)
			throws CommerceItemNotFoundException, InvalidParameterException,
			RepositoryException {
		if(addonToRemove.equalsIgnoreCase(commerceItemId)){									
			remivalList[removeIndex] = commerceItemId;
			removeIndex++;
			if(isLoggingDebug()){
				logDebug("Removing Addon:");
			}
		}
		else{
			CommerceItem addonItem = getOrder().getCommerceItem(commerceItemId);	
			String productId = addonItem.getCatalogRefId();
			RepositoryItem prodctRepItem = getCatalogTools().findProduct(productId);
			List dependAddon = (List) prodctRepItem.getPropertyValue("dependantAddons");
			if(!dependAddon.isEmpty()){
				for(Object depend: dependAddon){
					String id = ((RepositoryItem)depend).getRepositoryId();
					if(id.equalsIgnoreCase(parentAddonItem.getRepositoryId())){
						remivalList[removeIndex] = commerceItemId;
						removeIndex++;
						if(isLoggingDebug()){
							logDebug("Removing Dependent Addon:" + productId);
						}
					}
					
				}
			}
		}
		return removeIndex;
	}

	/**
	 * @param inputURL
	 * @return
	 */
	private String addOpenCartURLParam(String inputURL) {
		String url = inputURL;
		String[] urlWithoutRequestID = null;
		if (url.contains("&_requestid=")) {
			urlWithoutRequestID = url.split("&_requestid=");
		} else if (url.contains("?_requestid=")) {
			urlWithoutRequestID = url.split("\\?_requestid=");
		}
		if (null != urlWithoutRequestID && urlWithoutRequestID.length > 0) {
			url = urlWithoutRequestID[0];
		}
		if (!StringUtils.isEmpty(url) && !url.contains(OPEN_CART)) {
			if (url.contains("?")) {
				if (url.contains(HASH)) {
					url = url.replace(HASH, EMPTY_STRING);
				}
				url = url.concat("&openCart=true");
			} else {
				if (url.contains(HASH)) {
					url = url.replace(HASH, EMPTY_STRING);
				}
				url = url.concat("?openCart=true");
			}
		}
		return url;
	}
	
	/**
	 * @param req
	 * @param res
	 */
	public void preDeletePackage(DynamoHttpServletRequest req,
			DynamoHttpServletResponse res) {
		

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(req.getRequestURIWithQueryString())){
			pageURL = req.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
		if (isLoggingDebug()) {							
	    		logDebug("Entering into CricketCartModifierFormHandler class of preDeletePackage() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		String[] removalCatalogRefIds = getRemovalCommerceIds();
		String packageId = null;
		CommerceItem commerceItem = null;
		if(null != removalCatalogRefIds && removalCatalogRefIds.length > 0){
		for (int i = 0; i < removalCatalogRefIds.length; i++) {
			packageId = removalCatalogRefIds[i];
			try {
				commerceItem = getOrder().getCommerceItem(packageId);
				setLoosingPromotions(getLoosingPronotionsFromCItem(commerceItem));
			} catch (CommerceItemNotFoundException e) {
				if (isLoggingError()) {
					vlogError("CommerceItemNotFoundException in preDeletePackage of CricketCartModifierFormHandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			} catch (InvalidParameterException e) {
				if (isLoggingError()) {
					vlogError("InvalidParameterException in preDeletePackage of CricketCartModifierFormHandler" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			}
		}
		}
		if (isLoggingDebug()) {					
	    		logDebug("Exiting from CricketCartModifierFormHandler class of preDeletePackage() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}

	/* (non-Javadoc)
	 * @see atg.commerce.order.purchase.CartModifierFormHandler#preRemoveItemFromOrder(atg.servlet.DynamoHttpServletRequest, atg.servlet.DynamoHttpServletResponse)
	 */
	@Override
	public void preRemoveItemFromOrder(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		String[] removalCatalogRefIds = getRemovalCommerceIds();
		String[] removeIds = null;
	try {
		if (!StringUtils.isEmpty(getRemoveItemType()) && 
				(getRemoveItemType().equalsIgnoreCase(getCartConfiguration().getUpgradePlanIntention())
						|| getRemoveItemType().equalsIgnoreCase(getCartConfiguration().getUpgradeAddonIntention()))) {
				List<String> itemIds = ((CricketCommerceItemManager) getCommerceItemManager())
						.getRelatedItems((CricketOrderImpl) getOrder(), removalCatalogRefIds);
				if (null != itemIds && !itemIds.isEmpty()) {
					removeIds = new String[itemIds.size()];
					//int i = removalCatalogRefIds.length;
					for (int j=0 ; j < itemIds.size(); j++) {
						removeIds[j] = itemIds.get(j);
						//removalCatalogRefIds[i + j] = itemIds.get(j);
					}
				}
				if(getOrder().getCommerceItemCount() != removalCatalogRefIds.length){ 
					setRemovalCommerceIds(removeIds);
				}
		}
		String packageId = null;
		CricketCommerceItemImpl commerceItem = null;
		for (int i = 0; i < removalCatalogRefIds.length; i++) {
			packageId = removalCatalogRefIds[i];
			
				commerceItem = (CricketCommerceItemImpl) getOrder().getCommerceItem(packageId);
				setLoosingPromotions(getLoosingPronotionsFromCItem(commerceItem));
				}
			} catch (CommerceItemNotFoundException e) {
				if (isLoggingError()) {
					logDebug(
							"CommerceItemNotFoundException in preRemoveItemFromOrder of CricketCartModifierFormHandler",
							e);
				}
			} catch (InvalidParameterException e) {
				if (isLoggingError()) {
					logDebug(
							"InvalidParameterException in preRemoveItemFromOrder of CricketCartModifierFormHandler",
							e);
			}
		}
	}

	@Override
	public boolean handleRemoveItemFromOrder(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws IOException,
			ServletException {
		String l_sMethod = "CricketCartModifierOrderFormHandler.handleSetOrderByCommerceId";

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
				
		if (isLoggingDebug()) {				
	    		logDebug("Entering into CricketCartModiferFormHandler class of handleRemoveItemFromOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		RepeatingRequestMonitor l_oRepeatingRequestMonitor = getRepeatingRequestMonitor();
		if ((l_oRepeatingRequestMonitor == null)
				|| (l_oRepeatingRequestMonitor.isUniqueRequestEntry(l_sMethod))) {
			Transaction l_oTransaction = null;
			try {
				synchronized (getOrder()) {
					l_oTransaction = ensureTransaction();
					preRemoveItemFromOrder(pRequest, pResponse);
					String delpackageId = null;
					String[] removalCommerceIds = getRemovalCommerceIds();
					CricketCommerceItemImpl commerceItem = null;
					for (int i = 0; i < removalCommerceIds.length; i++) {
						delpackageId = removalCommerceIds[i];
						commerceItem = (CricketCommerceItemImpl) getOrder().getCommerceItem(delpackageId);
						if (!StringUtils.isEmpty(commerceItem.getPackageId())) {
							try {
								((CricketOrderImpl)getOrder()).removeCricketPackage(getAddonProductType());
							} catch (InvalidParameterException e) {
								if (isLoggingError()) {
									vlogError("InvalidParameterException in handleRemoveItemFromOrder of CricketCartModifierFormHandler:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
							 }
							} 
					}
					}
					super.handleRemoveItemFromOrder(pRequest, pResponse);
					getCartCookie().setOpenCart(OPEN_CART);
				}
				if (StringUtils.isEmpty(getDeletePackageSuccessURL())) {
					setDeletePackageSuccessURL("/");
				}
				if(getOrder().getCommerceItems().isEmpty()){
					getCartCookie().setOpenCart(null);	
					return checkFormRedirect(getDeletePackageSuccessURL(),
							null, pRequest, pResponse);
				}
				return checkFormRedirect(getDeletePackageSuccessURL(),
						getDeletePackageFailureURL(), pRequest, pResponse);
			} catch (CommerceItemNotFoundException e) {
				if (isLoggingError()) {
					vlogError("CommerceItemNotFoundException in handleRemoveItemFromOrder of CricketCartModifierFormHandler:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			} catch (InvalidParameterException e) {
				if (isLoggingError()) {
					vlogError("InvalidParameterException in handleRemoveItemFromOrder of CricketCartModifierFormHandler:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
				}
			} finally {
				if (getFormError()) {
					try {
						setTransactionToRollbackOnly();
					} catch (SystemException pSystemException) {
						if (isLoggingError()) {
							vlogError("SystemException in handleRemoveItemFromOrder of CricketCartModifierFormHandler:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, pSystemException);
						}
					}
				}
				// Commit or rolback the transaction depending on the status.
				if (l_oTransaction != null) {
					commitTransaction(l_oTransaction);
				}
				if (l_oRepeatingRequestMonitor != null) {
					l_oRepeatingRequestMonitor.removeRequestEntry(l_sMethod);
				}
			}
		}
		if (isLoggingDebug()) {				
	    		logDebug("Exiting from CricketCartModiferFormHandler class of handleRemoveItemFromOrder() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return false;

	}
	
 	@Override
 public void postRemoveItemFromOrder(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		
		super.postRemoveItemFromOrder(pRequest, pResponse);
		if (!StringUtils.isEmpty(getRemoveItemType()) && 
				(getRemoveItemType().equalsIgnoreCase(getCartConfiguration().getUpgradePhoneIntention())
						|| getRemoveItemType().equalsIgnoreCase(getCartConfiguration().getUpgradePlanIntention())
						|| getRemoveItemType().equalsIgnoreCase(getCartConfiguration().getUpgradeAddonIntention()))) {
			clearRemovedItemDetailsInOrder();
			clearUpgradeItemSessionData();
		} else if (!StringUtils.isEmpty(getRemoveItemType()) && 
				getRemoveItemType().equalsIgnoreCase(getCartConfiguration().getRemovedAddonItemType())) {
				String[] removalCommerceIds = getRemovalCommerceIds();
				for (int i = 0; i < removalCommerceIds.length; i++) {
					((CricketOrderImpl) getOrder()).getRemovedAddonCompatibility().put(getRemovalAddonId(), Boolean.TRUE);
				}
		}
 }
 	
	/**
	 * 
	 */
	protected void clearUpgradeItemSessionData() {
		getUpgradeItemDetailsSessionBean().setRemovedAddons(null);
		getUpgradeItemDetailsSessionBean().setRemovedPhoneId(null);
		getUpgradeItemDetailsSessionBean().setRemovedPhoneSkuId(null);
		getUpgradeItemDetailsSessionBean().setRemovedPlanId(null);
		getUpgradeItemDetailsSessionBean().setRemovedPlanSkuId(null);
		getUpgradeItemDetailsSessionBean().setUserIntention(null);
		getUpgradeItemDetailsSessionBean().setMdn(null);
		getUpgradeItemDetailsSessionBean().setModelNumber(null);
	}
	
 @Override
public boolean handleMoveToPurchaseInfo(
		DynamoHttpServletRequest arg0, DynamoHttpServletResponse arg1)
		throws ServletException, IOException {
	// TODO Auto-generated method stub
	return super.handleMoveToPurchaseInfoByRelId(arg0, arg1);
}
 
 @Override
public void preMoveToPurchaseInfo(DynamoHttpServletRequest pRequest,
		DynamoHttpServletResponse pResponse) throws ServletException,
		IOException {
	// TODO Auto-generated method stub
	super.preMoveToPurchaseInfo(pRequest, pResponse);
}
 
 @SuppressWarnings("unchecked")
@Override
public void postMoveToPurchaseInfo(DynamoHttpServletRequest pRequest,
		DynamoHttpServletResponse pResponse) throws ServletException,
		IOException {
		 //try {
		 CricketOrderImpl order = (CricketOrderImpl) getOrder();

		// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
				pageURL = pRequest.getRequestURIWithQueryString();
			}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != order){
				if(!StringUtils.isBlank(order.getId())){
					orderId = order.getId();
				}
			}
				
		 if (isLoggingDebug()) {					
		    		logDebug("Entering into CricketCartModifierFormHandler class of postMoveToPurchaseInfo() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
		 if(getProfile().isTransient()){
				order.setAnanymousUser(true);
			}
		 String userIntentionInSessionBean = getUpgradeItemDetailsSessionBean().getUserIntention();
		 
		 if (!StringUtils.isEmpty(userIntentionInSessionBean)) {
			 if (userIntentionInSessionBean.equalsIgnoreCase(getCartConfiguration().getUpgradePlanIntention())
					 || userIntentionInSessionBean.equalsIgnoreCase(getCartConfiguration().getUpgradeAddonIntention())) {
				 order.setWorkOrderType(getCartConfiguration().getRrcWorkOrderType());
			 } else if (userIntentionInSessionBean.equalsIgnoreCase(getCartConfiguration().getUpgradePhoneIntention())) {
				 order.setWorkOrderType(getCartConfiguration().getOupWorkOrderType());
				 order.setPackageType(CricketESPConstants.UPGRADEPHONE);
			 }else if (userIntentionInSessionBean.equalsIgnoreCase(getCartConfiguration().getAddLineIntention())) {
				 order.setWorkOrderType(getCartConfiguration().getAddWorkOrderType());
				 order.setPackageType(ADD_A_LINE_ON_CART);
			 }
	 	} else if(getProfile().isTransient()){
	 		order.setWorkOrderType(getCartConfiguration().getActWorkOrderType());
	 		order.setPackageType(PAYGO_PURCHASE);
	 	}else{
	 		order.setWorkOrderType(getCartConfiguration().getAddWorkOrderType());
	 		order.setPackageType(NEW_ACTIVATION);
	 	}
		 
		 List<CricketCommerceItemImpl> commerceItems = order.getCommerceItems();
		 if (isCartHasOnlyAccessories(commerceItems)) {
			 order.setWorkOrderType(getCartConfiguration().getAccessoriesWorkOrderType());
			 order.setPackageType(PURCHASE_ACCESSORIES);
		 }
		 
		 if (!StringUtils.isEmpty(userIntentionInSessionBean)) {
			 if (userIntentionInSessionBean.equalsIgnoreCase(getCartConfiguration().getUpgradePlanIntention())){
				 order.setPackageType(CHANGE_PLAN_ON_CART);
				 
			 }
			 if (userIntentionInSessionBean.equalsIgnoreCase(getCartConfiguration().getUpgradeAddonIntention())){
				 order.setPackageType(CHANGE_FEATURES);
	 		 }
		 }
		 
		 //set order -> donwGrade during change feature remove only add on scenario
		 if (!StringUtils.isEmpty(userIntentionInSessionBean)) {
			 if (userIntentionInSessionBean.equalsIgnoreCase(getCartConfiguration().getUpgradeAddonIntention())) {				
				 if(isCartHasOnlyRemovedAddOn(commerceItems)){
					 order.setDownGrade(Boolean.TRUE);
				 }else{
					 order.setDownGrade(Boolean.FALSE);
				 }			
			 }
		 }	
			 
		 String locale = getCricketProfile().getLanguageIdentifier();
		 
			if(locale != null && locale.equalsIgnoreCase(LANGUAGE_IDENTIFIER_SPANISH)){
				order.setLanguageIdentifier(LANGUAGE_IDENTIFER_S);				 
			}
			else {
				order.setLanguageIdentifier(LANGUAGE_IDENTIFER_E);				 
			}
			// now setting network provider id
			if(!getProfile().isTransient()){
 		 	 ((CricketOrderImpl)getOrder()).setMarketCode(getCricketProfile().getMarketCode());
			}else{
				((CricketOrderImpl)getOrder()).setMarketCode((String)getProfile().getPropertyValue(CricketCommonConstants.PROP_MARKET_ID));
			}
			
			//some times there is empty package getting created which is causing issues in ESP's
			//removal of empty packages
			
			List<CricketPackage> cktPackages = order.getCktPackages();
						
			if(cktPackages != null && cktPackages.size() > 0){
				Iterator<CricketPackage> cktPackageItr  = cktPackages.iterator();				
				if(cktPackageItr != null){
					CricketCommerceItemImpl[] commerceItemList = null;
					CricketPackage cricketPackage =  null;
					CricketOrderTools cricketOrderTools = (CricketOrderTools) getOrderManager().getOrderTools();
					List<String> removePackageIds = new ArrayList<String>();
					while(cktPackageItr.hasNext()){
						cricketPackage = cktPackageItr.next();
						if(cricketPackage != null){
							commerceItemList = cricketOrderTools.getCommerceItemsForPackage(cricketPackage, order);
							if(commerceItemList == null || commerceItemList.length == 0 || commerceItemList.length == 1){
								//cktPackageItr.remove();
								removePackageIds.add(cricketPackage.getId());
							}								
						}
					}
					if(removePackageIds.size() > 0){
						for(String removePackageId : removePackageIds){
							try {
								logInfo("[CricketCartModifierFormHandler->postMoveToPurchaseInfo()]: extra packageId: "+ removePackageId +" for orderId: "+ order.getId());
								order.removeCricketPackage(removePackageId);
							} catch (InvalidParameterException e) {
								vlogError("InvalidParameterException while removing package in postMoveToPurchaseInfo of CricketCartModifierFormHandler::::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
							}
						}
					}
				}					
				
			}		
			if(null==getShippingAddressData().getShippingAddress().getCity() || getShippingAddressData().getShippingAddress().getCity().trim().length()==0){
				//CitySessionInfoObject citySessionInfoObject = (CitySessionInfoObject) pRequest.resolveName(getCitySessionInfoObjectPath());
				getShippingAddressData().getShippingAddress().setCity(getCitySessionInfoObjectPath().getCityVO().getCity());
				getShippingAddressData().getShippingAddress().setStateAddress(getCitySessionInfoObjectPath().getCityVO().getState());
				getShippingAddressData().getShippingAddress().setPostalCode(getCitySessionInfoObjectPath().getCityVO().getPostalCode());
			}
			if(null==getBillingAddressData().getBillingAddress().getCity() || getBillingAddressData().getBillingAddress().getCity().trim().length()==0){
				getBillingAddressData().getBillingAddress().setCity(getCitySessionInfoObjectPath().getCityVO().getCity());
				getBillingAddressData().getBillingAddress().setStateAddress(getCitySessionInfoObjectPath().getCityVO().getState());
				getBillingAddressData().getBillingAddress().setPostalCode(getCitySessionInfoObjectPath().getCityVO().getPostalCode());
			}
			super.postMoveToPurchaseInfo(pRequest, pResponse);
			if (isLoggingDebug()) {					
		    		logDebug("Exiting from CricketCartModifierFormHandler class of postMoveToPurchaseInfo() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
			}
}
 
 /**
 * @param commerceItems
 * @return
 */
private boolean isCartHasOnlyAccessories(List<CricketCommerceItemImpl> commerceItems) {
	 boolean isCartHasOnlyAccessories = Boolean.FALSE;
	 if (null != commerceItems && !commerceItems.isEmpty() && commerceItems.size() > 0) {
		 isCartHasOnlyAccessories = Boolean.TRUE;
		 for (CricketCommerceItemImpl cricketCommerceItem : commerceItems) {
			 if (null != cricketCommerceItem 
					 && !OrderConstants.ACCESSORIES.equalsIgnoreCase(cricketCommerceItem.getCricItemTypes())) {
				 isCartHasOnlyAccessories = Boolean.FALSE;
				 break;
			 }
		 }
	 }
	 return isCartHasOnlyAccessories;
	 
 }


/**
 * @param commerceItems
 * @return
 */
private boolean isCartHasOnlyRemovedAddOn(List<CricketCommerceItemImpl> commerceItems) {
	 boolean isCartHasOnlyRemovedAddOn = Boolean.FALSE;
	 if (null != commerceItems && !commerceItems.isEmpty() && commerceItems.size() > 0) {
		 isCartHasOnlyRemovedAddOn = Boolean.TRUE;
		 for (CricketCommerceItemImpl cricketCommerceItem : commerceItems) {
			 if (null != cricketCommerceItem 
					 && !CITYPE_RMOEVEDADDON.equalsIgnoreCase(cricketCommerceItem.getCricItemTypes())) {
				 isCartHasOnlyRemovedAddOn = Boolean.FALSE;
				 break;
			 }
		 }
	 }
	 return isCartHasOnlyRemovedAddOn;
	 
 }
	/**
	 * @return the catalogTools
	 */
	public CatalogTools getCatalogTools() {
		return catalogTools;
	}

	/**
	 * @param pCatalogTools
	 *            the catalogTools to set
	 */
	public void setCatalogTools(CatalogTools pCatalogTools) {
		catalogTools = pCatalogTools;
	}
	
	/**
	 * @return the duplicatePackageId
	 */
	public String getDuplicatePackageId() {
		return duplicatePackageId;
	}

	/**
	 * @param duplicatePackageId
	 *            the duplicatePackageId to set
	 */
	public void setDuplicatePackageId(String duplicatePackageId) {
		this.duplicatePackageId = duplicatePackageId;
	}

	/**
	 * @return the cricketOrderManager
	 */
	public CricketOrderManager getCricketOrderManager() {
		return cricketOrderManager;
	}

	/**
	 * @param cricketOrderManager
	 *            the cricketOrderManager to set
	 */
	public void setCricketOrderManager(CricketOrderManager cricketOrderManager) {
		this.cricketOrderManager = cricketOrderManager;
	}

	/**
	 * @return the duplicatePackageErrorUrl
	 */
	public String getDuplicatePackageErrorUrl() {
		return duplicatePackageErrorUrl;
	}

	/**
	 * @param duplicatePackageErrorUrl
	 *            the duplicatePackageErrorUrl to set
	 */
	public void setDuplicatePackageErrorUrl(String duplicatePackageErrorUrl) {
		this.duplicatePackageErrorUrl = duplicatePackageErrorUrl;
	}

	/**
	 * @return the duplicatePackageSuccessUrl
	 */
	public String getDuplicatePackageSuccessUrl() {
		return duplicatePackageSuccessUrl;
	}

	/**
	 * @param duplicatePackageSuccessUrl
	 *            the duplicatePackageSuccessUrl to set
	 */
	public void setDuplicatePackageSuccessUrl(String duplicatePackageSuccessUrl) {
		this.duplicatePackageSuccessUrl = duplicatePackageSuccessUrl;
	}

	/**
	 * @return the deletePackageSuccessURL
	 */
	public String getDeletePackageSuccessURL() {
		return deletePackageSuccessURL;
	}

	/**
	 * @param deletePackageSuccessURL
	 *            the deletePackageSuccessURL to set
	 */
	public void setDeletePackageSuccessURL(String deletePackageSuccessURL) {
		this.deletePackageSuccessURL = deletePackageSuccessURL;
	}

	/**
	 * @return the deletePackageFailureURL
	 */
	public String getDeletePackageFailureURL() {
		return deletePackageFailureURL;
	}

	/**
	 * @param deletePackageFailureURL
	 *            the deletePackageFailureURL to set
	 */
	public void setDeletePackageFailureURL(String deletePackageFailureURL) {
		this.deletePackageFailureURL = deletePackageFailureURL;
	}

	/**
	 * @return the deletePacakgeId
	 */
	public String getDeletePacakgeId() {
		return deletePacakgeId;
	}

	/**
	 * @param deletePacakgeId
	 *            the deletePacakgeId to set
	 */
	public void setDeletePacakgeId(String deletePacakgeId) {
		this.deletePacakgeId = deletePacakgeId;
	}

	/**
	 * @return the deleteCommerceItemId
	 */
	public String getDeleteCommerceItemId() {
		return deleteCommerceItemId;
	}

	/**
	 * @param deleteCommerceItemId
	 *            the deleteCommerceItemId to set
	 */
	public void setDeleteCommerceItemId(String deleteCommerceItemId) {
		this.deleteCommerceItemId = deleteCommerceItemId;
	}

	/**
	 * @return the promotionTools
	 */
	public PromotionTools getPromotionTools() {
		return promotionTools;
	}

	/**
	 * @param promotionTools
	 *            the promotionTools to set
	 */
	public void setPromotionTools(PromotionTools promotionTools) {
		this.promotionTools = promotionTools;
	}

	/**
	 * @return the skuId
	 */
	public String getSkuId() {
		return skuId;
	}

	/**
	 * @param skuId
	 *            the skuId to set
	 */
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	/**
	 * @return the loosingPromotions
	 *//*
	public List<RepositoryItem> getLoosingPromotions() {
		return loosingPromotions;
	}

	*//**
	 * @param loosingPromotions the loosingPromotions to set
	 *//*
	public void setLoosingPromotions(List<RepositoryItem> loosingPromotions) {
		this.loosingPromotions = loosingPromotions;
	}*/

	public boolean handleClearCartItems(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws IOException,
			ServletException {
		String l_sMethod = "CricketCartModifierOrderFormHandler.handleClearCartItems";
		boolean checkFormRedirect = false;
		RepeatingRequestMonitor l_oRepeatingRequestMonitor = getRepeatingRequestMonitor();
		if ((l_oRepeatingRequestMonitor == null)
				|| (l_oRepeatingRequestMonitor.isUniqueRequestEntry(l_sMethod))) {
			Transaction l_oTransaction = null;
			try {
				l_oTransaction = ensureTransaction();
				@SuppressWarnings("unchecked")
				List<CommerceItem> commerceItems = getOrder()
						.getCommerceItems();
				String[] remivalList = new String[commerceItems.size()];
				for (int i = 0; i < commerceItems.size(); i++) {
					remivalList[i] = commerceItems.get(i).getId();
				}
				setRemovalCommerceIds(remivalList);
				((CricketOrderImpl)getOrder()).removeAllCricketPackages();
				deleteItems(pRequest, pResponse);
				getCartCookie().setOpenCart(null);
				getCartCookie().setEditActionFromCart(Boolean.FALSE);
				getCartCookie().setProductType(null);
				getCartCookie().setPackageId(null);
				getUserSessionBean().setAutoBillPayment(false);
				checkFormRedirect = checkFormRedirect(getClearFormSuccessURl(),
						getClearFormErrorURl(), pRequest, pResponse);
			} finally {
				if (getFormError()) {
					try {
						setTransactionToRollbackOnly();
					} catch (SystemException pSystemException) {
						if (isLoggingError()) {
							logError("SystemException", pSystemException);
						}
					}
				}
				// Commit or rolback the transaction depending on the status.
				if (l_oTransaction != null) {
					commitTransaction(l_oTransaction);
				}
				if (l_oRepeatingRequestMonitor != null) {
					l_oRepeatingRequestMonitor.removeRequestEntry(l_sMethod);
				}
			}
		}
		return checkFormRedirect;

	}
	/**
	 * @return the clearFormSuccessUrl
	 */
	public String getClearFormSuccessURl() {
		return clearFormSuccessURl;
	}

	/**
	 * @param clearFormSuccessURl the clearFormSuccessUrl to set
	 */
	public void setClearFormSuccessURl(String clearFormSuccessURl) {
		this.clearFormSuccessURl = clearFormSuccessURl;
	}
	/**
	 * @return the clearFormErrorUrl
	 */
	public String getClearFormErrorURl() {
		return clearFormErrorURl;
	}

	/**
	 * @param clearFormErrorURl the clearFormErrorUrl to set
	 */
	public void setClearFormErrorURl(String clearFormErrorURl) {
		this.clearFormErrorURl = clearFormErrorURl;
	}
	private String clearFormErrorURl;
	private String clearFormSuccessURl;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean handleSetOrderByCommerceId(
			DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		String l_sMethod = "CricketCartModifierOrderFormHandler.handleSetOrderByCommerceId";

		// Getting the Page url
		String pageURL = CricketCommonConstants.EMPTY_STRING;
		if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
			pageURL = pRequest.getRequestURIWithQueryString();
		}
		 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
				
		if (isLoggingDebug()) {				
	    		logDebug("Entering into CricketCartModifierOrderFormHandler class of handleSetOrderByCommerceId() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		RepeatingRequestMonitor l_oRepeatingRequestMonitor = getRepeatingRequestMonitor();
		if ((l_oRepeatingRequestMonitor == null)
				|| (l_oRepeatingRequestMonitor.isUniqueRequestEntry(l_sMethod))) {
			Transaction l_oTransaction = null;
			try {
				l_oTransaction = ensureTransaction();
				synchronized (getOrder()) {
					String cricItemTypes = null;
					String redirectURL =  null;
					List<CricketCommerceItemImpl> commerceItems = getOrder()
							.getCommerceItems();
					if (null != commerceItems) {
						for (CricketCommerceItemImpl commerceItem : commerceItems) {
							  cricItemTypes = commerceItem
									.getCricItemTypes();
							if (getUpdateAccessoryId().equalsIgnoreCase(
									commerceItem.getId())
									&& ACCESSORY_PRODUCT
											.equalsIgnoreCase(cricItemTypes)) {
								checkSkuStockLevel(pRequest, pResponse, commerceItem.getAuxiliaryData().getProductId(), commerceItem.getCatalogRefId(), getQuantity());
								if (getFormError()) {
									//setOpenCart(null);
									/*return checkFormRedirect(null,
											getUpdateAccessoryFailureURL(), pRequest,
											pResponse);*/
									redirectURL = addOpenCartURLParam(getUpdateAccessoryFailureURL());
									pResponse.sendRedirect(redirectURL);
									return checkFormRedirect(redirectURL,redirectURL, pRequest,pResponse);
								}
								((CricketCommerceItemManager) getCommerceItemManager()).setProductType(cricItemTypes);
								super.handleSetOrderByCommerceId(pRequest,
										pResponse);
								getCartCookie().setOpenCart(OPEN_CART);
								/*if(getOrder().getCommerceItems().isEmpty()){
									setOpenCart(null);	
									return checkFormRedirect(pRequest.getContextPath(),
											null, pRequest, pResponse);
								}*/
								if (getFormError()) {
									//setOpenCart(null);
									return checkFormRedirect(null,
											getUpdateAccessoryFailureURL(), pRequest,
											pResponse);
								} else {
									  redirectURL = addOpenCartURLParam(getUpdateAccessorySuccessURL());
									pResponse.sendRedirect(redirectURL);
									return false;
									/*return checkFormRedirect(
											getUpdateAccessorySuccessURL(), null, pRequest,
											pResponse);*/
								}
							}
						}
					}
					
				} 
				

			} finally {
				if (getFormError()) {
					try {
						setTransactionToRollbackOnly();
					} catch (SystemException pSystemException) {
						if (isLoggingError()) {
							vlogError("SystemException:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, pSystemException);
						}
					}
				}
				// Commit or rolback the transaction depending on the status.
				if (l_oTransaction != null) {
					commitTransaction(l_oTransaction);
				}
				if (l_oRepeatingRequestMonitor != null) {
					l_oRepeatingRequestMonitor.removeRequestEntry(l_sMethod);
				}
			}
		}
		if (isLoggingDebug()) {				
	    		logDebug("Exiting from CricketCartModifierOrderFormHandler class of handleSetOrderByCommerceId() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return false;
	}

	/**
	 * @return the loosingPromotions
	 */
	public List<RepositoryItem> getLoosingPromotions() {
		return loosingPromotions;
	}

	/**
	 * @param loosingPromotions the loosingPromotions to set
	 */
	public void setLoosingPromotions(List<RepositoryItem> loosingPromotions) {
		this.loosingPromotions = loosingPromotions;
	}

	/**
	 * @return the cricketProfile
	 */
	public CricketProfile getCricketProfile() {
		return cricketProfile;
	}

	/**
	 * @param cricketProfile the cricketProfile to set
	 */
	public void setCricketProfile(CricketProfile cricketProfile) {
		this.cricketProfile = cricketProfile;
	}

	/**
	 * @return the cricketEspAdapter
	 */
	public CricketESPAdapter getCricketEspAdapter() {
		return cricketEspAdapter;
	}

	/**
	 * @param cricketEspAdapter the cricketEspAdapter to set
	 */
	public void setCricketEspAdapter(CricketESPAdapter cricketEspAdapter) {
		this.cricketEspAdapter = cricketEspAdapter;
	}

	/**
	 * @return the accountHolderAddressData
	 */
	public CricketAccountHolderAddressData getAccountHolderAddressData() {
		return accountHolderAddressData;
	}

	/**
	 * @param accountHolderAddressData the accountHolderAddressData to set
	 */
	public void setAccountHolderAddressData(CricketAccountHolderAddressData accountHolderAddressData) {
		this.accountHolderAddressData = accountHolderAddressData;
	}

	/**
	 * @return the cartCookie
	 */
	public MyCricketCookieCartInfo getCartCookie() {
		return cartCookie;
	}

	/**
	 * @param cartCookie the cartCookie to set
	 */
	public void setCartCookie(MyCricketCookieCartInfo cartCookie) {
		this.cartCookie = cartCookie;
	}
	
	
	public boolean handleEditCommerceItem(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		this.getFormExceptions().clear();
		getCartCookie().setPackageId(getEditPackageId());
		getCartCookie().setProductType(getEditProductType()); 
		getCartCookie().setEditActionFromCart(true);
		//String successUrl="../../index.jsp";
		return checkFormRedirect(getEditSuccessUrl()/*successUrl*/, null, pRequest, pResponse);
		
	}

	/**
	 * @return the editPackageId
	 */
	public String getEditPackageId() {
		return editPackageId;
	}

	/**
	 * @param editPackageId the editPackageId to set
	 */
	public void setEditPackageId(String editPackageId) {
		this.editPackageId = editPackageId;
	}

	/**
	 * @return the editProductType
	 */
	public String getEditProductType() {
		return editProductType;
	}

	/**
	 * @param editProductType the editProductType to set
	 */
	public void setEditProductType(String editProductType) {
		this.editProductType = editProductType;
	}
	@Override
	protected void modifyOrderByCommerceId(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException, CommerceException, RunProcessException {
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
				 CricketOrderImpl order = (CricketOrderImpl) getOrder();
				 String orderId = CricketCommonConstants.EMPTY_STRING;
		    		if(null != order){
		    			if(!StringUtils.isBlank( order.getId())){
			    			orderId = order.getId();
						}
		    		}
				/*OrderHolder cart = (OrderHolder) pRequest.resolveName("/atg/commerce/ShoppingCart");
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(cart.getCurrent().getId())){
					orderId = cart.getCurrent().getId();
				}*/				
			logDebug("Entering into CricketCartModifierFormHandler class of modifyOrderByCommerceId() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
				
		Order order = getOrder();
		Map<CricketCommerceItemImpl, Long> removedItemMap = new HashMap<CricketCommerceItemImpl, Long>();
		Map<CricketCommerceItemImpl, Long> changedItemMap = new HashMap<CricketCommerceItemImpl, Long>();
		if (order == null) {
			String msg = formatUserMessage("noOrderToModify", pRequest,
					pResponse);
			throw new ServletException(msg);
		}
		try {
			synchronized (order) {
				@SuppressWarnings("unchecked")
				List<CricketCommerceItemImpl> items = order.getCommerceItems();
				CricketCommerceItemImpl item = null;
				String cricItemTypes = null;
				String commerceItemId = null;
				long oldQuantity = 0;
				long qty = 0;
				if (items != null) {
					for (int i = 0; i < items.size(); i++) {
						  item = (CricketCommerceItemImpl) items.get(i);
						  cricItemTypes = item.getCricItemTypes();
						if (getUpdateAccessoryId().equalsIgnoreCase(item.getId()) && ACCESSORY_PRODUCT.equalsIgnoreCase(cricItemTypes)) {
							  commerceItemId = item.getId();
							long quantity;
							if (isCheckForChangedQuantity())
								quantity = getQuantityByCommerceId(commerceItemId, pRequest, pResponse);
							else
								quantity = item.getQuantity();
							if (haveIds(commerceItemId, getRemovalCommerceIds()))
								quantity = 0L;
							if (quantity > 0L) {
								if (item.getQuantity() != quantity) {
									oldQuantity = item.getQuantity();
									getPurchaseProcessHelper().adjustItemRelationshipsForQuantityChange(order, item, quantity);
									item.setQuantity(quantity);
									changedItemMap.put(item,Long.valueOf(oldQuantity));
								}
								continue;
							}
							ShippingGroupCommerceItemRelationship sgrel = null;
							ShippingGroup sg;
							for (Iterator<?> iter = item
									.getShippingGroupRelationships().iterator(); iter
									.hasNext(); getHandlingInstructionManager()
									.removeHandlingInstructionsFromShippingGroup(
											order, sg.getId(), item.getId())) {
								sgrel = (ShippingGroupCommerceItemRelationship) iter.next();
								sg = sgrel.getShippingGroup();
							}
							  qty = item.getQuantity();
							getCommerceItemManager().removeAllRelationshipsFromCommerceItem(order, item.getId());
							getCommerceItemManager().removeItemFromOrder(order,item.getId());
							removedItemMap.put(item, Long.valueOf(qty));
							i--;
						}
					}

					modifyCommerceItemsProperties(pRequest, pResponse,changedItemMap, removedItemMap);
					Map<?, ?> extraParams = createRepriceParameterMap();
					runProcessRepriceOrder(getModifyOrderPricingOp(), order,getUserPricingModels(), getUserLocale(),getProfile(), extraParams);
					modifyOrderPostReprice(pRequest, pResponse, changedItemMap,removedItemMap);
					removeInStorePickupShippingGroups(order);
					CommerceItem commerceItem;
					Long longQuantity;
					for (Iterator<?> itemIterator = removedItemMap.entrySet()
							.iterator(); itemIterator.hasNext(); runProcessSendScenarioEvent(order, commerceItem, longQuantity.longValue(),
							"atg.commerce.order.ItemRemovedFromOrder")) {
						@SuppressWarnings("rawtypes")
						java.util.Map.Entry entry = (java.util.Map.Entry) itemIterator.next();
						commerceItem = (CommerceItem) entry.getKey();
						longQuantity = (Long) entry.getValue();
					}
					CommerceItem commerceItem1;
					Long longQuantity1;
					for (Iterator<?> itemIterator = changedItemMap.entrySet()
							.iterator(); itemIterator.hasNext(); runProcessSendScenarioEvent(order, commerceItem1, longQuantity1.longValue(),
							"atg.commerce.order.ItemQuantityChanged")) {
						@SuppressWarnings("rawtypes")
						java.util.Map.Entry entry = (java.util.Map.Entry) itemIterator.next();
						commerceItem1 = (CommerceItem) entry.getKey();
						longQuantity1 = (Long) entry.getValue();
					}

				}
			}
		} catch (NumberFormatException nfe) {
			String msg = formatUserMessage("invalidQuantity", pRequest,pResponse);
			String propertyPath = generatePropertyPath(ITEM_DESC_ORDER);
			addFormException(new DropletFormException(msg, nfe, propertyPath,"invalidQuantity"));
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
			 CricketOrderImpl orderID = (CricketOrderImpl) getOrder();
			 String orderId = CricketCommonConstants.EMPTY_STRING;
	    		if(null != orderID){
	    			if(!StringUtils.isBlank( orderID.getId())){
		    			orderId = orderID.getId();
					}
	    		}
			/*OrderHolder cart = (OrderHolder) pRequest.resolveName("/atg/commerce/ShoppingCart");
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(cart.getCurrent().getId())){
				orderId = cart.getCurrent().getId();
			}*/				
			logDebug("Exiting from CricketCartModifierFormHandler class of modifyOrderByCommerceId() method:::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
	}
	/**
	 * @param pId
	 * @param pIds
	 * @return
	 */
	final boolean haveIds(String pId, String pIds[]) {
		if (pIds != null && pId != null) {
			int length = pIds.length;
			for (int c = 0; c < length; c++)
				if (pId.equals(pIds[c]))
					return true;

		}
		return false;
	}
	
	
	/**
	 * @param productType
	 * @param pkgProduct
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean isCompatiblePhoneAndPlan(String productType, RepositoryItem pkgProduct){		
		
		List<RepositoryItem> compalibleList = null;
		List<RepositoryItem> compalibleListInPlan = null;
		List<RepositoryItem> compalibleListInPhone = null;
		if (productType.equalsIgnoreCase(PHONE_PRODUCT)) {
			//compalibleList = new ArrayList<RepositoryItem>((List<RepositoryItem>) pkgProduct.getPropertyValue("planGroups"));
			Set planGroupSet = (Set<RepositoryItem>) pkgProduct.getPropertyValue(PLAN_GROUPS);
			compalibleListInPhone = new ArrayList<RepositoryItem>(planGroupSet);
			if (null != compalibleListInPhone) {
				List<RepositoryItem> groupCompatiblePhones = null;
				String compatiablePrdtId = null;
				for (RepositoryItem planGItem : compalibleListInPhone) {
					 groupCompatiblePhones = (List<RepositoryItem>) planGItem.getPropertyValue(PROP_GROUP_COMPATIBLE_PHONES);
					if(null != groupCompatiblePhones && groupCompatiblePhones.size() > 0){
						for (RepositoryItem repositoryItem : groupCompatiblePhones) {
							  compatiablePrdtId = repositoryItem.getRepositoryId();
							if (compatiablePrdtId.equals(getProductId())) {
								return true;
							}
						}
					}
				}
			}
		}
		else if(productType.equalsIgnoreCase(PLAN_PRODUCT)){
			 Set planSet = (Set<RepositoryItem>) pkgProduct.getPropertyValue(COMPATIBLE_PLANS);
			 compalibleList = new ArrayList<RepositoryItem>(planSet) ;
			 String compatiablePrdtId = null;
			 List<RepositoryItem> groupPlans = null;
			 Set planGroupSet = null;
			 if (null != compalibleList) {
					for (RepositoryItem cItem : compalibleList) {
						planGroupSet = (Set<RepositoryItem>) cItem.getPropertyValue(PLAN_GROUPS);
						compalibleListInPlan = new ArrayList<RepositoryItem>(planGroupSet);
						if (null != compalibleList) {
							for (RepositoryItem planGItem : compalibleListInPlan) {
							  groupPlans = (List<RepositoryItem>) planGItem.getPropertyValue(PROP_GROUP_PLANS);
								if(null != groupPlans && groupPlans.size() > 0){
									for (RepositoryItem repositoryItem : groupPlans) {
										  compatiablePrdtId = repositoryItem.getRepositoryId();
										if (compatiablePrdtId.equals(getProductId())) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		return false;
	}

	/**
	 * @return the editSuccessUrl
	 */
	public String getEditSuccessUrl() {
		return editSuccessUrl;
	}
	
	/**
	 * @param editSuccessUrl the editSuccessUrl to set
	 */
	public void setEditSuccessUrl(String editSuccessUrl) {
		this.editSuccessUrl = editSuccessUrl;
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

	/**
	 * @return the removeItemType
	 */
	public String getRemoveItemType() {
		return removeItemType;
	}

	/**
	 * @param pRemoveItemType the removeItemType to set
	 */
	public void setRemoveItemType(String pRemoveItemType) {
		removeItemType = pRemoveItemType;
	}

	/**
	 * @return the upgradeItemFlow
	 */
	public String getUpgradeItemFlow() {
		return upgradeItemFlow;
	}

	/**
	 * @param pUpgradeItemFlow the upgradeItemFlow to set
	 */
	public void setUpgradeItemFlow(String pUpgradeItemFlow) {
		upgradeItemFlow = pUpgradeItemFlow;
	}

	/**
	 * @return the upgradeItemDetailsSessionBean
	 */
	public UpgradeItemDetailsSessionBean getUpgradeItemDetailsSessionBean() {
		return upgradeItemDetailsSessionBean;
	}

	/**
	 * @param pUpgradeItemDetailsSessionBean the upgradeItemDetailsSessionBean to set
	 */
	public void setUpgradeItemDetailsSessionBean(
			UpgradeItemDetailsSessionBean pUpgradeItemDetailsSessionBean) {
		upgradeItemDetailsSessionBean = pUpgradeItemDetailsSessionBean;
	}

	/**
	 * @return the productType
	 */
	public String getAddonProductType() {
		return addonProductType;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setAddonProductType(String addonProductType) {
		this.addonProductType = addonProductType;
	}


	/**
	 * @return the formPackageId
	 */
	public String getFormPackageId() {
		return formPackageId;
	}

	/**
	 * @param formPackageId the formPackageId to set
	 */
	public void setFormPackageId(String formPackageId) {
		this.formPackageId = formPackageId;
	}

	/**
	 * @return the maxNoOfPackageForPIA
	 */
	public int getMaxNoOfPackageForPIA() {
		return maxNoOfPackageForPIA;
	}

	/**
	 * @param maxNoOfPackageForPIA the maxNoOfPackageForPIA to set
	 */
	public void setMaxNoOfPackageForPIA(int maxNoOfPackageForPIA) {
		this.maxNoOfPackageForPIA = maxNoOfPackageForPIA;
	}

	/**
	 * @return the maxNoOfPackageForPayGo
	 */
	public int getMaxNoOfPackageForPayGo() {
		return maxNoOfPackageForPayGo;
	}

	/**
	 * @param maxNoOfPackageForPayGo the maxNoOfPackageForPayGo to set
	 */
	public void setMaxNoOfPackageForPayGo(int maxNoOfPackageForPayGo) {
		this.maxNoOfPackageForPayGo = maxNoOfPackageForPayGo;
	}


	/**
	 * @return the removalAddonId
	 */
	public String getRemovalAddonId() {
		return removalAddonId;
	}

	/**
	 * @param removalAddonId the removalAddonId to set
	 */
	public void setRemovalAddonId(String removalAddonId) {
		this.removalAddonId = removalAddonId;
	}
	
	/**
	 * @return the updateAccessoryFailureURL
	 */
	public String getUpdateAccessoryFailureURL() {
		return updateAccessoryFailureURL;
	}

	/**
	 * @param updateAccessoryFailureURL the updateAccessoryFailureURL to set
	 */
	public void setUpdateAccessoryFailureURL(String updateAccessoryFailureURL) {
		this.updateAccessoryFailureURL = updateAccessoryFailureURL;
	}

	/**
	 * @return the updateAccessorySuccessURL
	 */
	public String getUpdateAccessorySuccessURL() {
		return updateAccessorySuccessURL;
	}

	/**
	 * @param updateAccessorySuccessURL the updateAccessorySuccessURL to set
	 */
	public void setUpdateAccessorySuccessURL(String updateAccessorySuccessURL) {
		this.updateAccessorySuccessURL = updateAccessorySuccessURL;
	}

	/**
	 * @return the updateAccessoryId
	 */
	public String getUpdateAccessoryId() {
		return updateAccessoryId;
	}

	/**
	 * @param updateAccessoryId the updateAccessoryId to set
	 */
	public void setUpdateAccessoryId(String updateAccessoryId) {
		this.updateAccessoryId = updateAccessoryId;
	}

	/**
	 * @return the allPhoneUrl
	 */
	public String getAllPhoneUrl() {
		return allPhoneUrl;
	}

	/**
	 * @param allPhoneUrl the allPhoneUrl to set
	 */
	public void setAllPhoneUrl(String allPhoneUrl) {
		this.allPhoneUrl = allPhoneUrl;
	}

	/**
	 * @return the allPlanUrl
	 */
	public String getAllPlanUrl() {
		return allPlanUrl;
	}

	/**
	 * @param allPlanUrl the allPlanUrl to set
	 */
	public void setAllPlanUrl(String allPlanUrl) {
		this.allPlanUrl = allPlanUrl;
	}

	/**
	 * 
	 * @return
	 */
	public DisplayFeaturesManager getDisplayFeaturesManager() {
		return displayFeaturesManager;
	}

	/**
	 * 
	 * @param displayFeaturesManager
	 */
	public void setDisplayFeaturesManager(
			DisplayFeaturesManager displayFeaturesManager) {
		this.displayFeaturesManager = displayFeaturesManager;
	}
	

	private UserSessionBean userSessionBean;
	

	/**
	 * 
	 * @return the userSessionBean
	 */
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}


	/**
	 * 
	 * @param userSessionBean
	 */
	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

	/**
	 * 
	 * @return the isDynamicAddOn
	 */ 
	public boolean isDynamicAddOn() {
		return isDynamicAddOn;
	}

	/**
	 * 
	 * @param isDynamicAddOn
	 */
	public void setDynamicAddOn(boolean isDynamicAddOn) {
		this.isDynamicAddOn = isDynamicAddOn;
	}
	/**
	 * @return the isModalError
	 */
	public boolean isModalError() {
		return modalError;
	}

	/**
	 * @param isModalError the isModalError to set
	 */
	public void setModalError(boolean modalError) {
		this.modalError = modalError;
	}

	/**
	 * @return the reloadDrawerCartDiv
	 */
	public boolean isReloadDrawerCartDiv() {
		return reloadDrawerCartDiv;
	}

	/**
	 * @param reloadDrawerCartDiv the reloadDrawerCartDiv to set
	 */
	public void setReloadDrawerCartDiv(boolean reloadDrawerCartDiv) {
		this.reloadDrawerCartDiv = reloadDrawerCartDiv;
	}

	/**
	 * @return the redirectURL
	 */
	public String getRedirectURL() {
		return redirectURL;
	}

	/**
	 * @param redirectURL the redirectURL to set
	 */
	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	/**
	 * @return the currentURL
	 */
	public String getCurrentURL() {
		return currentURL;
	}

	/**
	 * @param currentURL the currentURL to set
	 */
	public void setCurrentURL(String currentURL) {
		this.currentURL = currentURL;
	}

	/**
	 * @return the catalogRepository
	 */
	public Repository getCatalogRepository() {
		return catalogRepository;
	}

	/**
	 * @param catalogRepository the catalogRepository to set
	 */
	public void setCatalogRepository(Repository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}

	/**
	 * @return the cartOpen
	 */
	public boolean isCartOpen() {
		return cartOpen;
	}

	/**
	 * @param cartOpen the cartOpen to set
	 */
	public void setCartOpen(boolean cartOpen) {
		this.cartOpen = cartOpen;
	}

	/**
	 * @return the addAsRemovedCommerceItem
	 */
	public boolean isAddAsRemovedCommerceItem() {
		return addAsRemovedCommerceItem;
	}

	/**
	 * @param addAsRemovedCommerceItem the addAsRemovedCommerceItem to set
	 */
	public void setAddAsRemovedCommerceItem(boolean addAsRemovedCommerceItem) {
		this.addAsRemovedCommerceItem = addAsRemovedCommerceItem;
	}

	/**
	 * @return the storeTextRepository
	 */
	public Repository getStoreTextRepository() {
		return storeTextRepository;
	}

	/**
	 * @param storeTextRepository the storeTextRepository to set
	 */
	public void setStoreTextRepository(Repository storeTextRepository) {
		this.storeTextRepository = storeTextRepository;
	}

	/**
	 * @return the onlyAccessories
	 */
	public boolean isOnlyAccessories() {
		return onlyAccessories;
	}

	/**
	 * @param onlyAccessories the onlyAccessories to set
	 */
	public void setOnlyAccessories(boolean onlyAccessories) {
		this.onlyAccessories = onlyAccessories;
	}

	/**
	 * @return the addonCommerceItemId
	 */
	public String getAddonCommerceItemId() {
		return addonCommerceItemId;
	}

	/**
	 * @param addonCommerceItemId the addonCommerceItemId to set
	 */
	public void setAddonCommerceItemId(String addonCommerceItemId) {
		this.addonCommerceItemId = addonCommerceItemId;
	}

	/**
	 * @return the shippingAddressData
	 */
	public CricketShippingAddressData getShippingAddressData() {
		return shippingAddressData;
	}

	/**
	 * @param shippingAddressData the shippingAddressData to set
	 */
	public void setShippingAddressData(CricketShippingAddressData shippingAddressData) {
		this.shippingAddressData = shippingAddressData;
	}

	/**
	 * @return the billingAddressData
	 */
	public CricketBillingAddressData getBillingAddressData() {
		return billingAddressData;
	}

	/**
	 * @param billingAddressData the billingAddressData to set
	 */
	public void setBillingAddressData(CricketBillingAddressData billingAddressData) {
		this.billingAddressData = billingAddressData;
	}

	/**
	 * @return the inventoryManager
	 */
	public StoreInventoryManager getInventoryManager() {
		return inventoryManager;
	}

	/**
	 * @param inventoryManager the inventoryManager to set
	 */
	public void setInventoryManager(StoreInventoryManager inventoryManager) {
		this.inventoryManager = inventoryManager;
	}

	/**
	 * @return the userAccountInformation
	 */
	public UserAccountInformation getUserAccountInformation() {
		return userAccountInformation;
	}

	/**
	 * @param userAccountInformation the userAccountInformation to set
	 */
	public void setUserAccountInformation(
			UserAccountInformation userAccountInformation) {
		this.userAccountInformation = userAccountInformation;
	}

	/**
	 * @return the cricketESPAdapterHelper
	 */
	public CricketESPAdapterHelper getCricketESPAdapterHelper() {
		return cricketESPAdapterHelper;
	}

	/**
	 * @param cricketESPAdapterHelper the cricketESPAdapterHelper to set
	 */
	public void setCricketESPAdapterHelper(
			CricketESPAdapterHelper cricketESPAdapterHelper) {
		this.cricketESPAdapterHelper = cricketESPAdapterHelper;
	}

	/**
	 * @return the profileServices
	 */
	public ProfileServices getProfileServices() {
		return profileServices;
	}

	/**
	 * @param profileServices the profileServices to set
	 */
	public void setProfileServices(ProfileServices profileServices) {
		this.profileServices = profileServices;
	}
	/**
	 * @return the citySessionInfoObjectPath
	 */
	public CitySessionInfoObject getCitySessionInfoObjectPath() {
		return citySessionInfoObjectPath;
	}
	/**
	 * @param citySessionInfoObjectPath the citySessionInfoObjectPath to set
	 */
	public void setCitySessionInfoObjectPath(CitySessionInfoObject citySessionInfoObjectPath) {
		this.citySessionInfoObjectPath = citySessionInfoObjectPath;
	}
	/**
	 * @return the cricketConfiguration
	 */
	public CricketConfiguration getCricketConfiguration() {
		return cricketConfiguration;
	}
	/**
	 * @param pCricketConfiguration the cricketConfiguration to set
	 */
	public void setCricketConfiguration(CricketConfiguration pCricketConfiguration) {
		cricketConfiguration = pCricketConfiguration;
	}
	/**
	 * @return the geoIPLocationTools
	 */
	public String getGeoIPLocationTools() {
		return geoIPLocationTools;
	}
	/**
	 * @param pGeoIPLocationTools the geoIPLocationTools to set
	 */
	public void setGeoIPLocationTools(String pGeoIPLocationTools) {
		geoIPLocationTools = pGeoIPLocationTools;
	}
	/**
	 * @return the cricketUtils
	 */
	public String getCricketUtils() {
		return cricketUtils;
	}
	/**
	 * @param pCricketUtils the cricketUtils to set
	 */
	public void setCricketUtils(String pCricketUtils) {
		cricketUtils = pCricketUtils;
	}
	public String getHppProductId() {
		return hppProductId;
	}
	public void setHppProductId(String hppProductId) {
		this.hppProductId = hppProductId;
	}
	public String getHppSKUId() {
		return hppSKUId;
	}
	public void setHppSKUId(String hppSKUId) {
		this.hppSKUId = hppSKUId;
	}
	public String getHppProductFromESP() {
		return hppProductFromESP;
	}
	public void setHppProductFromESP(String hppProductFromESP) {
		this.hppProductFromESP = hppProductFromESP;
	}

	
}
