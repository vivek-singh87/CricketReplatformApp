package com.webservices.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import atg.commerce.catalog.CatalogTools;
import atg.commerce.order.CreditCard;
import atg.commerce.order.HardgoodShippingGroup;
import atg.commerce.order.Order;
import atg.commerce.order.PaymentGroup;
import atg.commerce.order.ShippingGroup;
import atg.commerce.pricing.ItemPriceInfo;
import atg.commerce.pricing.OrderPriceInfo;
import atg.commerce.pricing.PricingAdjustment;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;

import com.cricket.cart.vo.PackageVO;
import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.commerce.order.CricketPackage;
import com.cricket.commerce.pricing.CricketItemPriceInfo;
import com.cricket.common.constants.CricketCommonConstants;
import com.mycricket.webservices.InquireOrderDetails.types.Address;
import com.mycricket.webservices.InquireOrderDetails.types.Attribute;
import com.mycricket.webservices.InquireOrderDetails.types.BOTaxImposition;
import com.mycricket.webservices.InquireOrderDetails.types.Discount;
import com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsResponse;
import com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsResponseInfo;
import com.mycricket.webservices.InquireOrderDetails.types.Items;
import com.mycricket.webservices.InquireOrderDetails.types.Packages;
import com.mycricket.webservices.InquireOrderDetails.types.PersonalInfo;
import com.mycricket.webservices.InquireOrderDetails.types.ResponseInfo;
import com.mycricket.webservices.InquireOrderDetails.types.ShippingInfo;
import com.mycricket.webservices.InquireOrderDetails.types.TaxSummary;
import com.mycricket.webservices.InquireOrderDetails.types.TelecomTaxImposition;

public class MappingUtils extends GenericService {
	
	
	private static final String EMAIL="email";
	
	private static final String FIRST_NAME="firstName";
	
	private static final String LAST_NAME="lastName";
	
	private static final String SHIPPING_METHOD="shippingMethod";
	
	private static final String PLAN_CODE="PlanCode";
	
	private static final String PLAN_DESCRIPTION="PlaneName";
	
	private static final String DISPLAY_NAME="displayName";
	
	 private static final String RESPONE_CODE_SUCCESS="100";
	
	private static final String RESPONSE_MSG_SUCCESS="success";
	
	private static final String HOME_ADDRESS="homeAddress";
	
	private static final String ADDRESS_1="address1";
	
	private static final String ADDRESS_2="address2";
	
	private static final String CITY="city";
	
	private static final String STATE ="state";
	
	private static final String POSTAL_CODE="postalCode";
	
	private static final String PACKAGES="PACKAGES";
	
	private static final String ACCESSORIES="ACCESSORIES";
	
	private static final String PACKAGE_TYPE_LOS="LOS";
	
	private static final String PACKAGE_TYPE_ACCESSORY="Accessory";
	
	private static final String ITEM_TYPE_ONE_TIME_FEE="One Time Fee";
	
	private static final String ITEM_TYPE_ONE_TIME_ACT_FEE="One-Time Activation Fee";
	
	private static final String ITEM_TYPE_ADMIN_FEE="Administration Fee";
	
	private static final String PRODUCT_ID="productId";
	
	private static final String ITEM_TYPE_PHONE="Phone";
 
	private static final String ITEM_TYPE_PLAN="Plan";
	
	private static final String ITEM_TYPE_OPTIONAL_FEATURES="Optional Features";
	
	private static final String ITEM_TYPE_ACCESSORY="Accessory";	
	
	private static final String ORDER_DISCOUNTS_PACKAGE="Order Discounts";
	
	private static final String ORDER_DISCOUNT="Order Discount";
	
	private static final String CONTACT_INFO_ITEM="contactInfo";
		
	private CricketOrderTools mOrderTools;
	
	private CatalogTools mCatalogTools;
	
	 
	 
	/**
	 * 
	 * @param pCricketOrder
	 * @param pProfileItem 
	 * @return
	 */
	
    public  InquireOrderDetailsResponseInfo mapOrderToCustomerOrderResponse(CricketOrderImpl pCricketOrder, RepositoryItem pProfileItem) {

    	InquireOrderDetailsResponseInfo orderResponseInfo = new InquireOrderDetailsResponseInfo();
    	InquireOrderDetailsResponse orderDetails = new InquireOrderDetailsResponse();
    	HashMap<String,String> aditonalAttributes= new HashMap<String, String>();    	
    	 if(isLoggingDebug()){
    		 logDebug("Mapping order details to responseInfo for InquireOrderDetails "+getClass().getName());
    	 }
    	com.mycricket.webservices.InquireOrderDetails.types.PersonalInfo personalInfo = new PersonalInfo();
    	String customerPhoneNumber=null;
	    	if(null!= pProfileItem){
	    		
	    		if(isLoggingDebug()){
	       		 logDebug("Mapping order profile details to responseInfo for InquireOrderDetails "+getClass().getName());
	       	 }
	    
	    
		    	if(null!=pProfileItem.getPropertyValue(EMAIL) || null!=pProfileItem.getPropertyValue(FIRST_NAME) || null!=pProfileItem.getPropertyValue(LAST_NAME)){
		    		if(isLoggingDebug()){
		    			logDebug("Adding Profile info to  order webservice "+getClass().getName());
		    		}
		    		
		    		if(null!=pProfileItem.getPropertyValue(EMAIL))
		    			personalInfo.setEmail((String) pProfileItem.getPropertyValue(EMAIL));
		    		if(null!=pProfileItem.getPropertyValue(FIRST_NAME))
		    			personalInfo.setFirstName((String) pProfileItem.getPropertyValue(FIRST_NAME));
		    		if(null!=pProfileItem.getPropertyValue(LAST_NAME))
		    			personalInfo.setLastName((String) pProfileItem.getPropertyValue(LAST_NAME));    	
			    	orderDetails.setPersonalInfo(personalInfo);
			    	
			    	String planCode = (String) pProfileItem.getPropertyValue(PLAN_CODE);
			    	String planDescription = (String) pProfileItem.getPropertyValue(PLAN_DESCRIPTION); 
			    	if(!StringUtils.isBlank(planCode))
			    		orderDetails.setPlan_code(planCode);
			    	if(!StringUtils.isBlank(planDescription))
			    		orderDetails.setPlan_description(planDescription);
		    	}   	
	    		
	    	}
    	      
	        RepositoryItem accountHolderAddress =null;// (RepositoryItem) pProfileItem.getPropertyValue(HOME_ADDRESS);
	        try {
	        	if(isLoggingDebug()){
	        	logDebug("Account Address Id "+pCricketOrder.getAccountAddressId());
	        	}
	        	if(!StringUtils.isBlank(pCricketOrder.getAccountAddressId()))
				 accountHolderAddress= (MutableRepositoryItem) getOrderTools().getProfileRepository().getItem(pCricketOrder.getAccountAddressId(), CONTACT_INFO_ITEM);
	        	if(isLoggingDebug()){
	        	logDebug("Conctat_info of Account Holder Address   "+accountHolderAddress);
	        	}
			} catch (RepositoryException e) {
				logError(getClass().getName() + "  Error while fetching the Account Holder address for order "+pCricketOrder.getBillingSystemOrderId());
			}
	        
	        
	        if(null!=accountHolderAddress){ 
	        	if(isLoggingDebug()){
	       		 logDebug("Mapping order account address details  to responseInfo for InquireOrderDetails "+getClass().getName());
	       	 }
	        		customerPhoneNumber = (String) accountHolderAddress.getPropertyValue("phoneNumber");
			        Address accountAddress = new Address();
			        accountAddress.setAddressLine1((String) accountHolderAddress.getPropertyValue(ADDRESS_1));
			        accountAddress.setAddressLine2((String) accountHolderAddress.getPropertyValue(ADDRESS_2));
			        accountAddress.setCity((String) accountHolderAddress.getPropertyValue(CITY));
			        accountAddress.setFirstName((String) accountHolderAddress.getPropertyValue(FIRST_NAME));
			        accountAddress.setLastName((String) accountHolderAddress.getPropertyValue(LAST_NAME));
			        accountAddress.setState((String) accountHolderAddress.getPropertyValue(STATE));
			        accountAddress.setZip((String) accountHolderAddress.getPropertyValue(POSTAL_CODE));
			        accountAddress.setContactNumber(customerPhoneNumber);
			        orderDetails.setAccountHolderAddress(accountAddress);			        
		        	if(StringUtils.isBlank(personalInfo.getFirstName()) || StringUtils.isEmpty(personalInfo.getFirstName()) 
		        			|| StringUtils.isBlank(personalInfo.getLastName()) || StringUtils.isEmpty(personalInfo.getLastName())){
		        		personalInfo.setFirstName((String) accountHolderAddress.getPropertyValue(FIRST_NAME));
		        		personalInfo.setLastName((String) accountHolderAddress.getPropertyValue(LAST_NAME)); 
		        		orderDetails.setPersonalInfo(personalInfo);
		        	}			    	
			   
		        }
	        else{
	        	getBillingInfo(pCricketOrder, orderDetails,customerPhoneNumber,true,aditonalAttributes);
	        }
        
    	
    	 
    	getShippingInfo(pCricketOrder, orderDetails,customerPhoneNumber);    	
    	getBillingInfo(pCricketOrder, orderDetails,customerPhoneNumber,false,aditonalAttributes);
    	
    	 
        List<CricketPackage> cktPackages = pCricketOrder.getCktPackages();
   		
        String[] cricketPhoneNumbers= null;
        int phoneNoIndex=0;
        if(!cktPackages.isEmpty()){
        	 
 	        	if(isLoggingDebug()){
 	       		 logDebug("Mapping order package details  to responseInfo for InquireOrderDetails "+getClass().getName());
 	       	 }
 	       	cricketPhoneNumbers= new String[cktPackages.size()];
 	       	for (CricketPackage existingpackage : cktPackages) {
	    		if(null!=existingpackage.getNewMdn()){
	    			cricketPhoneNumbers[phoneNoIndex]=new String();
	        		orderDetails.setNew_mdn((String) existingpackage.getNewMdn());
	        		cricketPhoneNumbers[phoneNoIndex]=(String)existingpackage.getNewMdn();
	        		phoneNoIndex++;
	        	}
	        	if(null!=existingpackage.getMdn())
	        	  orderDetails.setMdn((String) existingpackage.getMdn());   
	    		
	    	}
	        
        }
        else if(!StringUtils.isBlank(pCricketOrder.getUpgradeMdn())){
         	cricketPhoneNumbers= new String[1];
         	String mdn = pCricketOrder.getUpgradeMdn();
         	cricketPhoneNumbers[phoneNoIndex]=new String();    		 
    		cricketPhoneNumbers[phoneNoIndex]=mdn;
        }
        
        
        
        
        if(null!=cricketPhoneNumbers)
        orderDetails.setCricketPhoneNumbers(cricketPhoneNumbers);
                
        if(null!= pCricketOrder.getMarketCode())
        	orderDetails.setMarket_code(pCricketOrder.getMarketCode());   

        if(null!=pCricketOrder.getPackageType())
        	orderDetails.setPackageType(pCricketOrder.getPackageType());      
		
		orderDetails.setTotal(new DecimalFormat("###.##").format(pCricketOrder.getPriceInfo().getTotal()));
		 
		if(null!=pCricketOrder.getTaxPriceInfo())
			orderDetails.setTaxTotal(new DecimalFormat("###.##").format(pCricketOrder.getTaxPriceInfo().getAmount()));
		
       if(null!=pCricketOrder.getAccountBalance())
    	   orderDetails.setAccount_balance(pCricketOrder.getAccountBalance().toString());
       
       if(!StringUtils.isBlank(pCricketOrder.getWorkOrderType()))
    	   orderDetails.setWorkOrderType(pCricketOrder.getWorkOrderType());
       
       if(null!=pCricketOrder.getRefundAmount())
    	   orderDetails.setRefundAmount(pCricketOrder.getRefundAmount());

       
       if(!pCricketOrder.getRemovedAddons().isEmpty()) {
       Map<String, String> removedAddons = pCricketOrder.getRemovedAddons();
       StringBuffer removedAddonInfo = new StringBuffer();
       for (Map.Entry<String, String> entry : removedAddons.entrySet()){
    	   try {
   			RepositoryItem removedaddOnItem = getCatalogTools().findProduct(entry.getValue());
   			if(null!=removedaddOnItem) {
   				String removedaddOnName = (String) removedaddOnItem.getPropertyValue(DISPLAY_NAME);
   				removedAddonInfo.append(removedaddOnName+",");
   			}
   			 
   		} catch (RepositoryException e) {
   			logError("Error while fetching removed plan item name for  InquireOrderDetails response "+e,e);
   		}
    	   
    	   
       }
       
       orderDetails.setRemovedAddOn(removedAddonInfo.toString()); 
       }
       if(null!=pCricketOrder.getRemovedPlanId()) {
    	   
       
    	   String removedPlanId = pCricketOrder.getRemovedPlanId();
    	   
    	   try {
			RepositoryItem removedPlanItem = getCatalogTools().findProduct(removedPlanId);
			if(null!=removedPlanItem) {
				String removedPlanName = (String) removedPlanItem.getPropertyValue(DISPLAY_NAME);
				orderDetails.setRemovedPlan(removedPlanName);
			}
			 
		} catch (RepositoryException e) {
			logError("Error while fetching removed plan item name for  InquireOrderDetails response "+e,e);
		}
       
       }
               
       if(isLoggingDebug()){
    	  logDebug("getting tax summary to order response ");
       }
       TaxSummary taxSummary = new TaxSummary();
       BOTaxImposition boTaxImposition[] = new BOTaxImposition[1];
       TelecomTaxImposition telecomTaxImposition[] = new TelecomTaxImposition[1];
      
       if(null!=pCricketOrder.getTelecomTaxValue())
       {
    	   if(isLoggingDebug()){
        	   logDebug("Adding Telcom Tax info to order response  of order id  "+pCricketOrder.getId());
           }
    	   
    	   telecomTaxImposition[0] = new TelecomTaxImposition();
    	   telecomTaxImposition[0].setValue(pCricketOrder.getTelecomTaxValue());
    	   if(null!= pCricketOrder.getTelecomTaxName())
        	   telecomTaxImposition[0].setName(pCricketOrder.getTelecomTaxName());
    	   
    	   taxSummary.setTelecomTaxImposition(telecomTaxImposition);
       }
       
       if(null!=pCricketOrder.getBoTaxValue()) {
    	   if(isLoggingDebug()){
        	   logDebug("Adding Bo Tax info to order response  of order id  "+pCricketOrder.getId());
           }
    	   
    	   boTaxImposition[0] = new BOTaxImposition();
    	   boTaxImposition[0].setValue(pCricketOrder.getBoTaxValue());
    	   if(null!=pCricketOrder.getBoTaxName())
        	   boTaxImposition[0].setName(pCricketOrder.getBoTaxName());
    	   
    	   taxSummary.setBOTaxImposition(boTaxImposition);
       }
       
       if(!StringUtils.isBlank(pCricketOrder.getE91desc()))
    	   orderDetails.setE911Desc(pCricketOrder.getE91desc()); 
       
       if(null!=(pCricketOrder.getE911amount()))
    	   orderDetails.setE911Amount(pCricketOrder.getE911amount());
       int orderPromotionSize=0;
       OrderPriceInfo orderPriceInfo = pCricketOrder.getPriceInfo();
       if(null!=orderPriceInfo && null!=orderPriceInfo.getAdjustments()){
		for(Iterator<PricingAdjustment> i$ = orderPriceInfo.getAdjustments().iterator(); i$.hasNext();)
       {
	    	PricingAdjustment pt = i$.next();
	    	if(pt.getTotalAdjustment()<0){
	    	    	orderPromotionSize++;    		     	    		
	    	}
		 
       }
       }
       
       //ADDING COMMERCE ITEMS TO RESPONSE
        Map<String, Object> packageItems = getOrderTools().getDisplayItems(pCricketOrder, null);
        
	        if(null!=packageItems && packageItems.size()>0){
	        	 
	        Map<String, PackageVO> mapPackageCommerceItems = (Map<String, PackageVO>) packageItems.get(PACKAGES);
	        List<CricketCommerceItemImpl> accessoriesItems  =   (List<CricketCommerceItemImpl>) packageItems.get(ACCESSORIES);
	        List<CricketCommerceItemImpl> upgradePhoneItems =  (List<CricketCommerceItemImpl>) packageItems.get(CricketCommonConstants.UPGRADE_PHONE);
	        List<CricketCommerceItemImpl> changePlanItems = (List<CricketCommerceItemImpl>) packageItems.get(CricketCommonConstants.CHANGE_PLAN);
	        List<CricketCommerceItemImpl> changeAddonsItems =(List<CricketCommerceItemImpl>) packageItems.get(CricketCommonConstants.CHANGE_ADDONS);
	        Packages cktOrderPackages[]=null;
	        int packageSize=0;
	        if(null!=mapPackageCommerceItems && null!=accessoriesItems) {
	        	packageSize=packageSize+mapPackageCommerceItems.size()+1;
	        }
	        else  if(null!=mapPackageCommerceItems){
	        	packageSize=packageSize+mapPackageCommerceItems.size();
	        }
	        else if(null!=accessoriesItems){
	        	packageSize=packageSize+1;
	        }
	        
	        if(null!=upgradePhoneItems){
	        	packageSize=packageSize+1;
	        }
	        if(null!=changePlanItems){
	        	packageSize=packageSize+1;
	        }
	        
	        if(null!=changeAddonsItems){
	        	packageSize=packageSize+1;
	        }
	        if(orderPromotionSize>0){
	        	packageSize=packageSize+1;
	        }
	   
	        int packageIndex=0;
	        cktOrderPackages =  new Packages[packageSize];
	        if(null!=mapPackageCommerceItems && mapPackageCommerceItems.size()>0) {
	        	
	        for(Entry<String, PackageVO> packageInfo:mapPackageCommerceItems.entrySet()){
	        	 
	        	PackageVO packagesVO = packageInfo.getValue();
	        	Double adminFee = 0.0;
	        	cktOrderPackages[packageIndex]= new Packages();
	        	cktOrderPackages[packageIndex].setType(PACKAGE_TYPE_LOS);
	        	cktOrderPackages[packageIndex].setSubTotal(new DecimalFormat("###.##").format(Double.valueOf(packagesVO.getPackageTotal())));
	        	 
	        	int commItemSize=0;
	        	List addonList = packagesVO.getAddOnPriceInfo();
	        	int addOnSize=0;
	        	if(null!=addonList && !addonList.isEmpty())
	        		 addOnSize = addonList.size();
	        	int defaultSize=4;
	        	Items commerceItems[]  = new Items[defaultSize+addOnSize];
	        	
	        	//Adding phone Item
	        	commerceItems[commItemSize] = new Items();
	        	RepositoryItem phoneItem = packagesVO.getPhoneCommerceItem();
	        	List<PricingAdjustment> adjustment = packagesVO.getPhoneDiscountAdjustments();
	        	ItemPriceInfo priceInfo = packagesVO.getPhonePriceInfo();
	        	if(isLoggingDebug()){
	        		logDebug("Adding Phone details to InquireOrderDetails response ");
	        	}
	        	addingCItemToReponse(phoneItem,adjustment,commerceItems[commItemSize],priceInfo,null);
	        	
	        	//Adding plan Item
	        	commItemSize++;
	        	RepositoryItem planItem = packagesVO.getPlanCommerceItem();
	        	List<PricingAdjustment> planAdjustment = packagesVO.getPlanDiscountAdjustments();
	        	ItemPriceInfo planPriceInfo = packagesVO.getPlanPriceInfo();
	        	adminFee = ((CricketItemPriceInfo)planPriceInfo).getAdminFee();
	        	commerceItems[commItemSize]=new Items();
	        	if(isLoggingDebug()){
	        		logDebug("Adding Plan details to InquireOrderDetails response ");
	        	}
	        	addingCItemToReponse(planItem,planAdjustment,commerceItems[commItemSize],planPriceInfo,null);
	        	

				// Adding Admin Fee
	        	
 	        	 
	        	commItemSize++;				     	
	        	commerceItems[commItemSize]=new Items();
	        	if(isLoggingDebug()){
	        		logDebug("Adding Admin fee details to InquireOrderDetails response ");
	        	}
	        	addingAdditionalFeeInfo(commerceItems[commItemSize],adminFee,false);
	        		
 	        	
	        	//Adding Add on
	        	List<RepositoryItem> addOnCommItem = packagesVO.getAddOnsCommerceItems();  
	        	
	        	 for(int addOnIndex=0; addOnIndex<addOnSize; addOnIndex++){
	        		 commItemSize++;
     				 ItemPriceInfo addOnPriceInfo = (ItemPriceInfo) addonList.get(addOnIndex);
     				 RepositoryItem addOnItem = addOnCommItem.get(addOnIndex);
     				List<PricingAdjustment> addOnAdjustment = addOnPriceInfo.getAdjustments() ;
     	        	commerceItems[commItemSize]=new Items();
     	        	if(isLoggingDebug()){
    	        		logDebug("Adding Add-on details to InquireOrderDetails response ");
    	        	}
     	        	addingCItemToReponse(addOnItem,addOnAdjustment,commerceItems[commItemSize],addOnPriceInfo,null);
	        		 
	        		 
	        	 }
	        	 
	        	 
	        	
	        	//Adding Activation Fee
	        	
	        	commItemSize++;
				Double activationFee = packagesVO.getActivationFee();        	
	        	commerceItems[commItemSize]=new Items();
	        	if(isLoggingDebug()){
	        		logDebug("Adding activation fee details to InquireOrderDetails response ");
	        	}
	        	addingAdditionalFeeInfo(commerceItems[commItemSize],activationFee,true);	        	        	
	        	cktOrderPackages[packageIndex].setItem(commerceItems);
	        	packageIndex++;        	  		
	    		 
	        }
	    }
	       
	        //adding upgade phones 
	        if(null!=upgradePhoneItems){
	        	Double subtotal=0.0;
	        	int upgradeItems=0;
	        	if(isLoggingDebug()){
	        		logDebug("Adding Upgrade phone fee details to InquireOrderDetails response ");
	        	}
	        	Items upgradePhoneCommItems[] = new Items[upgradePhoneItems.size()];
	        	cktOrderPackages[packageIndex]= new Packages();
        		cktOrderPackages[packageIndex].setType(PACKAGE_TYPE_LOS);
        		 for(CricketCommerceItemImpl upgradePhoneItem: upgradePhoneItems){
        			 upgradePhoneCommItems[upgradeItems] = new Items();
 	        		if(null!=upgradePhoneItem.getPriceInfo()){
 	        		ItemPriceInfo upgradePhonePriceInfo = upgradePhoneItem.getPriceInfo();
 	        		subtotal=upgradePhonePriceInfo.getAmount();
 	        		List<PricingAdjustment> upgradePhoneAdjustments = upgradePhoneItem.getPriceInfo().getAdjustments();
 	        		addingCItemToReponse(upgradePhoneItem.getRepositoryItem(),upgradePhoneAdjustments,upgradePhoneCommItems[upgradeItems],upgradePhonePriceInfo,null);
 	        		
        		 }
 	        		else{
 	        			addingCItemToReponse(upgradePhoneItem.getRepositoryItem(),null,upgradePhoneCommItems[upgradeItems],null,null);
 	        		}
 	        		upgradeItems++;
        		 
	        }        		 
        		 cktOrderPackages[packageIndex].setItem(upgradePhoneCommItems);        		
        		 cktOrderPackages[packageIndex].setSubTotal(new DecimalFormat("###.##").format(subtotal));
        		 packageIndex++;
	        }
	        
	        // adding accessoreis
	        if(null!=accessoriesItems && accessoriesItems.size()>0){
	        	if(isLoggingDebug()){
	        		logDebug("Adding Accessory details to InquireOrderDetails response ");
	        	}
	        	int accItemSize=0;
	        	cktOrderPackages[packageIndex]= new Packages();
        		cktOrderPackages[packageIndex].setType(PACKAGE_TYPE_ACCESSORY);
        		Items accryCommerceItems[]=  new Items[accessoriesItems.size()];        		 
        		Double accSubTotal = 0.0;
	        	for(CricketCommerceItemImpl accessoriesItem:accessoriesItems){
	        		accryCommerceItems[accItemSize] = new Items();
	        		if(null!=accessoriesItem.getPriceInfo()){
	        		ItemPriceInfo accsryPriceInfo = accessoriesItem.getPriceInfo();
	        		List<PricingAdjustment> accAdjustments = accessoriesItem.getPriceInfo().getAdjustments();
	        		addingCItemToReponse(accessoriesItem.getRepositoryItem(),accAdjustments,accryCommerceItems[accItemSize],accsryPriceInfo,null);
	        		}
	        		else{
	        			addingCItemToReponse(accessoriesItem.getRepositoryItem(),null,accryCommerceItems[accItemSize],null,null);
	        		}
	        		accSubTotal=accSubTotal+accryCommerceItems[accItemSize].getPrice();
	        		accItemSize++;	        
	        		
	        	}	        	
	        	cktOrderPackages[packageIndex].setItem(accryCommerceItems);	     
	        	cktOrderPackages[packageIndex].setSubTotal(new DecimalFormat("###.##").format(accSubTotal));
	        	packageIndex++;
	        }
	        
	        
	       //adding change plan
	        
	        if(null!=changePlanItems){
	        	int changePlanItemSize=0;
	        	if(isLoggingDebug()){
	        		logDebug("Adding Upgrade phone fee details to InquireOrderDetails response ");
	        	}
	        	Items changePlanCommItems[] = new Items[changePlanItems.size()];
	        	cktOrderPackages[packageIndex]= new Packages();
        		cktOrderPackages[packageIndex].setType(PACKAGE_TYPE_LOS);
        		 for(CricketCommerceItemImpl changePlanItem: changePlanItems){
        			 changePlanCommItems[changePlanItemSize] = new Items();
 	        		if(null!=changePlanItem.getPriceInfo()){
 	        		ItemPriceInfo changePlanPriceInfo = changePlanItem.getPriceInfo();
 	        		List<PricingAdjustment> changePlanAdjustments = changePlanItem.getPriceInfo().getAdjustments();
 	        		addingCItemToReponse(changePlanItem.getRepositoryItem(),changePlanAdjustments,changePlanCommItems[changePlanItemSize],changePlanPriceInfo,null);
 	        		
        		 }
 	        		else{
 	        			addingCItemToReponse(changePlanItem.getRepositoryItem(),null,changePlanCommItems[changePlanItemSize],null,null);
 	        		}
 	        		changePlanItemSize++;
        		 
	        }
        		 cktOrderPackages[packageIndex].setItem(changePlanCommItems);
        		 packageIndex++;
	        }
	        
	            
	     // adding change add-ons
	        if(null!=changeAddonsItems){
	        	int changeAddOnItems=0;
	        	if(isLoggingDebug()){
	        		logDebug("Adding Upgrade phone fee details to InquireOrderDetails response ");
	        	}
	        	Items changeAddOnCommItems[] = new Items[changeAddonsItems.size()];
	        	cktOrderPackages[packageIndex]= new Packages();
        		cktOrderPackages[packageIndex].setType(PACKAGE_TYPE_LOS);
        		 for(CricketCommerceItemImpl changeAddonsItem: changeAddonsItems){
        			 changeAddOnCommItems[changeAddOnItems] = new Items();
 	        		if(null!=changeAddonsItem.getPriceInfo()){
 	        		ItemPriceInfo changeAddonsItemPriceInfo = changeAddonsItem.getPriceInfo();
 	        		List<PricingAdjustment> changeAddonAdjustments = changeAddonsItem.getPriceInfo().getAdjustments();
 	        		addingCItemToReponse(changeAddonsItem.getRepositoryItem(),changeAddonAdjustments,changeAddOnCommItems[changeAddOnItems],changeAddonsItemPriceInfo,null);
 	        		
        		 }
 	        		else {
 	        			addingCItemToReponse(changeAddonsItem.getRepositoryItem(),null,changeAddOnCommItems[changeAddOnItems],null,null);
 	        		}
 	        		changeAddOnItems++;
	        }
        		 cktOrderPackages[packageIndex].setItem(changeAddOnCommItems);
        		 packageIndex++;
	        }
	        
	        //Adding Order Discounts
	        if(orderPromotionSize>0 && null!=orderPriceInfo && null!=orderPriceInfo.getAdjustments()){
	        	int promotionItems=0;
	        	if(isLoggingDebug()){
	        		logDebug("Adding Upgrade phone fee details to InquireOrderDetails response ");
	        	}
	        	Items orderPromotions[] = new Items[orderPromotionSize];	        	
	        	cktOrderPackages[packageIndex]= new Packages();
        		cktOrderPackages[packageIndex].setType(ORDER_DISCOUNTS_PACKAGE);
        		
        		for(Iterator<PricingAdjustment> i$ = orderPriceInfo.getAdjustments().iterator(); i$.hasNext();)
 	            {
 	    	    	PricingAdjustment pt = i$.next();
 	    	    	if(pt.getTotalAdjustment()<0){	 
 	    	    		String promotionName;
 	    	    		if(null!=pt.getPricingModel())
 	    	    		 promotionName= (String) pt.getPricingModel().getPropertyValue(DISPLAY_NAME);
 	    	    		else
 	    	    			promotionName=pt.getAdjustmentDescription();
 	    	    		orderPromotions[promotionItems]=new Items();
 	    	    		addingPromotions(orderPromotions[promotionItems],pt.getTotalAdjustment(),promotionName);
 	    	    		promotionItems++;
 	    	    	}
 	    		 
 	            }
        		 cktOrderPackages[packageIndex].setItem(orderPromotions);
        		 packageIndex++;
	        }
	        
	        //Adding commerceItems as package in order response
	        orderDetails.setOrderSummary(cktOrderPackages);
	    }
        
	    String languageIdentifer =  pCricketOrder.getLanguageIdentifier();
	    if(!StringUtils.isBlank(languageIdentifer))
	    	aditonalAttributes.put("LanguageIdentifier", languageIdentifer);
	    
	    addAditionalAttributes(orderDetails,aditonalAttributes);    
        
        ResponseInfo responseStatus = new ResponseInfo();
        responseStatus.setCode(RESPONE_CODE_SUCCESS);
        responseStatus.setDescription(RESPONSE_MSG_SUCCESS);
        
        orderResponseInfo.setInquireOrderDetailsResponse(orderDetails);
        orderResponseInfo.setResponseInfo(responseStatus);

        if(isLoggingDebug()){
       		 logDebug("Mapped order details  for InquireOrderDetails "+orderResponseInfo);
       	 }
        
        return orderResponseInfo;
    }

	

	/**
	 * @param pOrderItems
	 * @param totalAdjustment
	 * @param adjustmentDescription
	 */
	private void addingPromotions(Items pOrderItems, double totalAdjustment,
			String adjustmentDescription) {
		 
				pOrderItems.setType(ORDER_DISCOUNT);
				pOrderItems.setName(adjustmentDescription);	
				pOrderItems.setPrice(totalAdjustment);
		
		
	}



	/**
	 * @param pOrderItem
	 * @param pAdditionalFee
	 * @param isActivationFee
	 */
	private void addingAdditionalFeeInfo(Items pOrderItem, Double pAdditionalFee, boolean isActivationFee) {
		if(null!=pAdditionalFee){
			
			if(isActivationFee){
			pOrderItem.setType(ITEM_TYPE_ONE_TIME_FEE);
			pOrderItem.setName(ITEM_TYPE_ONE_TIME_ACT_FEE);
			}
			else{
				pOrderItem.setType(ITEM_TYPE_ADMIN_FEE);
				pOrderItem.setName(ITEM_TYPE_ADMIN_FEE);				
			}
			pOrderItem.setPrice(pAdditionalFee);
		}
		
	}



	/**
	 * @param orderDetails
	 * @param aditonalAttributes 
	 */
	private void addAditionalAttributes(InquireOrderDetailsResponse orderDetails, HashMap<String, String> aditonalAttributes) {

		if(aditonalAttributes.isEmpty())
			return;
		
		Attribute additionalAttr[] = new Attribute[aditonalAttributes.size()];
		int index=0;
		for (Map.Entry<String, String> entry : aditonalAttributes.entrySet()) {
			
			additionalAttr[index]=new Attribute();
			additionalAttr[index].setName(entry.getKey());
			additionalAttr[index].setValue(entry.getValue());
			index++;
		}
		
		orderDetails.setAdditionalAttributes(additionalAttr);
		
		
	}



	/**
	 * @param pCommerceItem
	 * @param pAdjustmentInfo
	 * @param pOrderItem
	 * @param pPriceInfo
	 * @param pActivationFee
	 */
	private void addingCItemToReponse(RepositoryItem pCommerceItem,
			List<PricingAdjustment> pAdjustmentInfo, Items pOrderItem, ItemPriceInfo pPriceInfo, Double pActivationFee) {
		// Adding phone Item
			if(null!=pActivationFee){
				pOrderItem.setType(ITEM_TYPE_ONE_TIME_FEE);
				pOrderItem.setName(ITEM_TYPE_ONE_TIME_ACT_FEE);
				pOrderItem.setPrice(pActivationFee);
			}
			else {
								
    		String productType = (String) pCommerceItem.getPropertyValue(CricketCommonConstants.CRICKET_ITEMTYPES);	
    		RepositoryItem productItem=null;
    		try {
				productItem = getCatalogTools().findProduct((String) pCommerceItem.getPropertyValue(PRODUCT_ID));
			} catch (RepositoryException e) {
				logError("Error while fetching the product details in InquireOrderDetails webservice "+e,e);
			}
    		if(productType.equalsIgnoreCase(CricketCommonConstants.PHONE_PRODUCT) || productType.equalsIgnoreCase(CricketCommonConstants.CITYPE_UPGRADEPHONE)){
    			pOrderItem.setType(ITEM_TYPE_PHONE);
    		}
    			else if(productType.equalsIgnoreCase(CricketCommonConstants.PLAN_PRODUCT) || productType.equalsIgnoreCase(CricketCommonConstants.CITYPE_CHANGEPLAN)){    				 
    				pOrderItem.setType(ITEM_TYPE_PLAN);
    			}
    			else if(productType.equalsIgnoreCase(CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT) || productType.equalsIgnoreCase(CricketCommonConstants.CITYPE_CHANGEADDON)){
    				pOrderItem.setType(ITEM_TYPE_OPTIONAL_FEATURES);
    			}
    			else if(productType.equalsIgnoreCase(CricketCommonConstants.ACCESSORY_PRODUCT))
    			{
    				pOrderItem.setType(ITEM_TYPE_ACCESSORY);
    			}
    			 
    			 
    		
    		if(null!=productItem){
	    		String itemName = (String) productItem.getPropertyValue(DISPLAY_NAME);
	    		pOrderItem.setName(itemName);
    		}
    		
    		if(productType.equalsIgnoreCase(CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT)){
    			 
    			 if(null!=pAdjustmentInfo && pAdjustmentInfo.size()>0)
    			 pOrderItem.setPrice(pAdjustmentInfo.get(0).getTotalAdjustment());  
    		}
    		else 
    		{	
    			if(null!=pPriceInfo){
    				if(productType.equalsIgnoreCase(CricketCommonConstants.PHONE_PRODUCT) || productType.equalsIgnoreCase(CricketCommonConstants.PLAN_PRODUCT)|| productType.equalsIgnoreCase(CricketCommonConstants.CITYPE_UPGRADEPHONE)){
    					pOrderItem.setPrice(pPriceInfo.getListPrice());
    	    		}else{
    	    			pOrderItem.setPrice(pPriceInfo.getAmount());
    	    		}
    				}
    	
            
            if(null!=pAdjustmentInfo && !pAdjustmentInfo.isEmpty() && pAdjustmentInfo.size() > 0){
            	if(isLoggingDebug()){
            		logDebug("gettiing pricing adjustments to order response  "+pAdjustmentInfo);
            	}
        	   int index=0;
        	   Discount[] itemDiscount =null;
        	    
        	   for(PricingAdjustment adjustmentAmount: pAdjustmentInfo){
        		   if (adjustmentAmount.getAdjustmentDescription().equalsIgnoreCase(CricketCommonConstants.ITEM_DISCOUNT)) {
        			   if(null==itemDiscount){
        				    itemDiscount = new Discount[pAdjustmentInfo.size()];
        			   }
        		   	  itemDiscount[index] = new Discount();
	            	  itemDiscount[index].setAmount(adjustmentAmount.getTotalAdjustment());
	            	  if(null!=adjustmentAmount.getPricingModel())
	            		  itemDiscount[index].setDescription((String) adjustmentAmount.getPricingModel().getPropertyValue(DISPLAY_NAME));
	            	  else
	            		  itemDiscount[index].setDescription(adjustmentAmount.getAdjustmentDescription());
	            	  if(isLoggingDebug()){
	            	  logDebug("Added Adjustment total price of item "+itemDiscount[index].toString());
	            	  }
	            	  index++;
        		   }
        	   }
        	   
        	   if(null!=itemDiscount && itemDiscount.length>0) {
        		   pOrderItem.setDiscount(itemDiscount);
	            	  if(isLoggingDebug()){
	            	  logDebug("Adding itemdiscounts ---------> "+itemDiscount);
	            	  }
	              }
        	    
           } 
		}
            
	}
}
		
	

	/**
	 * @param pCricketOrder
	 * @param orderDetails
	 * @param pCustomerPhoneNumber 
	 * @param aditonalAttributes 
	 * @param pB 
	 * @return 
	 */
	private  void getBillingInfo(CricketOrderImpl pCricketOrder,
			InquireOrderDetailsResponse orderDetails, String pCustomerPhoneNumber, boolean pIsAccountAddress, HashMap<String, String> aditonalAttributes) {
		
	 
	  if(isLoggingDebug()){
	       		 logDebug("Mapping order billing info details  to responseInfo for InquireOrderDetails "+getClass().getName());
	   }
	        	
		List<PaymentGroup> pgList = pCricketOrder.getPaymentGroups();
		CreditCard paymentType = (CreditCard) pgList.get(0);
		atg.core.util.Address billingInfo = paymentType.getBillingAddress();
		if (billingInfo != null) {        	
        	 
            Address billingAddress = new Address();
            
            if(null!=billingInfo.getFirstName())
            	billingAddress.setFirstName(billingInfo.getFirstName());
            
            if(null!=billingInfo.getLastName())
            	billingAddress.setLastName(billingInfo.getLastName());
            
            if(null!=billingInfo.getAddress1())
            	billingAddress.setAddressLine1(billingInfo.getAddress1());
            if(null!=billingInfo.getAddress2())
            	billingAddress.setAddressLine2(billingInfo.getAddress2());
            if(null!=billingInfo.getCity())
            	billingAddress.setCity(billingInfo.getCity());
            if(null!=billingInfo.getState())
            	billingAddress.setState(billingInfo.getState());
             
            String postalCode = billingInfo.getPostalCode();

            if (!StringUtils.isEmpty(postalCode)) {
            	billingAddress.setZip(postalCode);
            }
            
            if(!StringUtils.isBlank(pCustomerPhoneNumber)){
            	billingAddress.setContactNumber(pCustomerPhoneNumber);
            }
            
           
            
            if(pIsAccountAddress) {
            	  orderDetails.setAccountHolderAddress(billingAddress);
            	  com.mycricket.webservices.InquireOrderDetails.types.PersonalInfo personalInfo = new PersonalInfo();
		        	personalInfo.setFirstName((String) billingInfo.getFirstName());
		        	personalInfo.setLastName((String) billingInfo.getLastName());    	
			    	orderDetails.setPersonalInfo(personalInfo);
            }
            else{ 
            orderDetails.setBillingAddress(billingAddress);            
            }
            String creditCardType = paymentType.getCreditCardType();
            if(!StringUtils.isBlank(creditCardType)){            	
        		orderDetails.setPaymentType(creditCardType);
        	}
            
            String creditCardNumber =  paymentType.getCreditCardNumber();
            if(!StringUtils.isBlank(creditCardNumber)){
            	aditonalAttributes.put("creditCardNumber", creditCardNumber);
            }
 
        }
	}
	
	
	/**
	 * @param pCricketOrder
	 * @param orderDetails
	 * @param pCustomerPhoneNumber 
	 */
	private  void getShippingInfo(CricketOrderImpl pCricketOrder,
			InquireOrderDetailsResponse orderDetails, String pCustomerPhoneNumber) {
		 
	        	if(isLoggingDebug()){
	       		 logDebug("Mapping order shipping address details  to responseInfo for InquireOrderDetails "+getClass().getName());
	       	 }
		HardgoodShippingGroup shippingInfo = (HardgoodShippingGroup) getHardgoodShippingGroup(pCricketOrder);
		 
        if (shippingInfo != null) {

            ShippingInfo shippingDetailsBean = new ShippingInfo();
            Address shippingAddress = new Address();//shippingDetailsBean.getAddress();
            
            if(null!=shippingInfo.getShippingAddress().getAddress1())
            	shippingAddress.setAddressLine1(shippingInfo.getShippingAddress().getAddress1());
            if(null!=shippingInfo.getShippingAddress().getAddress2())
            	shippingAddress.setAddressLine2(shippingInfo.getShippingAddress().getAddress2());
            if(null!=shippingInfo.getShippingAddress().getCity())
            	shippingAddress.setCity(shippingInfo.getShippingAddress().getCity());
            if(null!=shippingInfo.getShippingAddress().getState())
            	shippingAddress.setState(shippingInfo.getShippingAddress().getState());

            if(null!=shippingInfo.getShippingAddress().getFirstName())
            	shippingAddress.setFirstName(shippingInfo.getShippingAddress().getFirstName());
            
            if(null!=shippingInfo.getShippingAddress().getLastName())
            	shippingAddress.setLastName(shippingInfo.getShippingAddress().getLastName());
            
            
            String postalCode = shippingInfo.getShippingAddress().getPostalCode();

            if (!StringUtils.isBlank(postalCode)) {            	
            	shippingAddress.setZip(postalCode);
            }
            
            if(!StringUtils.isBlank(pCustomerPhoneNumber)){
            	shippingAddress.setContactNumber(pCustomerPhoneNumber);
            }
            	
            shippingDetailsBean.setAddress(shippingAddress);
            if(null!=shippingInfo.getShippingMethod())
            	shippingDetailsBean.setShippingMethod(shippingInfo.getShippingMethod());
            else
            	shippingDetailsBean.setShippingMethod("");
            if(null!=shippingInfo.getPriceInfo())
            	shippingDetailsBean.setShippingPrice(shippingInfo.getPriceInfo().getAmount());
            else
            	shippingDetailsBean.setShippingPrice(0.0);
            orderDetails.setShippingInfo(shippingDetailsBean);

        }
	}
    /**
     * 
     * @param order
     * @return
     */
    private static HardgoodShippingGroup getHardgoodShippingGroup(Order order) {
        List<ShippingGroup> shippingGroups = (List<ShippingGroup>) order.getShippingGroups();

        for (ShippingGroup sg : shippingGroups) {
            if (sg instanceof HardgoodShippingGroup) {
                return (HardgoodShippingGroup) sg;
            }
        }
        return null;
    }

	/**
	 * @return the orderTools
	 */
	public CricketOrderTools getOrderTools() {
		return mOrderTools;
	}

	/**
	 * @param pOrderTools the orderTools to set
	 */
	public void setOrderTools(CricketOrderTools pOrderTools) {
		mOrderTools = pOrderTools;
	}


	/**
	 * @return the catalogTools
	 */
	public CatalogTools getCatalogTools() {
		return mCatalogTools;
	}

	/**
	 * @param pCatalogTools the catalogTools to set
	 */
	public void setCatalogTools(CatalogTools pCatalogTools) {
		mCatalogTools = pCatalogTools;
	}

}
