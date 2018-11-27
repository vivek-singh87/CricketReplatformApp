package com.cricket.commerce.order;

import static com.cricket.common.constants.CricketESPConstants.PROPERTY_DISPLAY_NAME;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import atg.commerce.order.HardgoodShippingGroup;
import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.OrderImpl;
import atg.core.i18n.LayeredResourceBundle;
import atg.core.util.ResourceUtils;
import atg.core.util.StringUtils;
import atg.nucleus.logging.ApplicationLogging;
import atg.repository.RepositoryItem;
import atg.service.dynamo.LangLicense;

import com.cricket.checkout.utils.CricketAddress;
import com.cricket.commerce.order.payment.CricketCreditCard;
import com.cricket.order.CricketOrder;

public class CricketOrderImpl extends OrderImpl implements CricketOrder{
	
	private static final String ACCOUNT_ADDRESS_ID = "accountAddressId";
	private static final long serialVersionUID = 1L;
	private static final String CRICKET_PACKAGES = "packages";
	private List<CricketPackage> cktPackages = new ArrayList<CricketPackage>();
	private static ResourceBundle sResourceBundle = LayeredResourceBundle.getBundle("atg.commerce.order.OrderResources", LangLicense.getLicensedDefault());
	
	 
	public Map<String, Boolean> removedAddonCompatibility;
	/**
	 * @return the packageId
	 */
	@Override
	public List<CricketPackage> getCktPackages() {
		return cktPackages;
	}

	/**
	 * @param packItems the packageId to set
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setCktPackages(List<CricketPackage> packItems) {
		if (packItems == null || packItems.isEmpty()) {
			List repositoryItemList = (List) getRepositoryItem()
					.getPropertyValue(CRICKET_PACKAGES);
			repositoryItemList.clear();
			cktPackages.clear();
		} else {
			synchronized (this) {
				// packageId.addAll(packItems);
				List repositoryItemList = (List) getRepositoryItem()
						.getPropertyValue(CRICKET_PACKAGES);
				for (CricketPackage cricketPackage : packItems) {
					if (null != cricketPackage) {
						RepositoryItem repItem = cricketPackage
								.getRepositoryItem();
						if (repItem != null
								&& !repositoryItemList.contains(repItem)) {
							repositoryItemList.add(repItem);
							
						}
					}
				}
				if (cktPackages != packItems) {
				cktPackages.addAll(packItems);
				// setPropertyValue(COUPONS, coupons);
				}
			}

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addCricketPackage(CricketPackage pCricketPackage) throws InvalidParameterException {
		if (pCricketPackage == null) {
			throw new InvalidParameterException(ResourceUtils.getMsgResource(
					"InvalidCommerceItem", "atg.commerce.order.OrderResources",
					sResourceBundle));
		}
		synchronized (this) {
			List repositoryItemList = (List) getRepositoryItem()
					.getPropertyValue(CRICKET_PACKAGES);
			RepositoryItem repItem = pCricketPackage.getRepositoryItem();
			if (repItem != null && !repositoryItemList.contains(repItem)) {
				repositoryItemList.add(repItem);
			}
			if (pCricketPackage != null
					&& !cktPackages.contains(pCricketPackage)) {
				cktPackages.add(pCricketPackage);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void removeAllCricketPackages() {
		List repositoryItemList = (List) getRepositoryItem()
				.getPropertyValue(CRICKET_PACKAGES);
		repositoryItemList.clear();

		synchronized (this) {
			cktPackages.clear();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void removeCricketPackage(String pCricketPackageId) throws InvalidParameterException {
		if (pCricketPackageId == null) {
			throw new InvalidParameterException(ResourceUtils.getMsgResource(
					"InvalidCommerceItem", "atg.commerce.order.OrderResources",
					sResourceBundle));
		}
		synchronized (this) {
			List repositoryItemList = (List) getRepositoryItem()
					.getPropertyValue(CRICKET_PACKAGES);
			
			Iterator iter = repositoryItemList.iterator();
		    while (iter.hasNext()) {
		      RepositoryItem item = (RepositoryItem)iter.next();
		      if (pCricketPackageId.equals(item.getRepositoryId())) {
		        iter.remove();
		        break;
		      }
		    }
		    Iterator packageIterator = cktPackages.iterator();
		    while (packageIterator.hasNext()) {
		    	CricketPackage cricketPackage = (CricketPackage) packageIterator.next();
			      if (pCricketPackageId.equals(cricketPackage.getId())) {
			    	  packageIterator.remove();
			    	  break;
			      }
			    }
		}
		
	}
	
	/** this method is used to display the order details i.e commerceItems,payment method,shipping method and if any discount*/
	public String orderDetailsToTrack(CricketOrderImpl order,CricketAddress accountHolderaddress, String processorName){
		/*// now displaying the commerceItems products
		List<CricketCommerceItemImpl> commerceItems = order.getCommerceItems();
		RepositoryItem productItem = null;
		String productName = null;
		double amount = 0.0;
		double listPrice = 0.0;
		String itemType = null;
		StringBuilder productInfo = null;
		logInfo("################# Start .. "+processorName+" displaying details ###########################");
		logInfo("Order Id : "+order.getId());
		logInfo("Order Type : "+order.getWorkOrderType());
		if(accountHolderaddress!=null){
		logInfo("Account Holder Address : "+accountHolderaddress.toString());
		}
		if(null!= order.getWorkOrderType() && order.getWorkOrderType().equalsIgnoreCase("ACT") && order.getWorkOrderType().equalsIgnoreCase("ADD")){
			logInfo(" 			###***### 	PACKAGE INFORMATION:  ###***### 		" );
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
				logInfo(" 		###***### 	 PRODUCT INFORMATOIN: ###***### 	 " );
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
				logInfo(" 			###***### 	 SHIPPING INFORMATION: ###***### 	* " );
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
					logInfo(" 			###***### 	 PAYMENT INFORMATION: ###***### 	 " );
					logInfo("PaymentMethod : "+paymentGroup.getPaymentMethod()+"|| Card Type : "+paymentGroup.getCreditCardType()+"|| Card Number[last 4 digits] : "+paymentGroup.getCreditCardNumber());
					logInfo("isDifferentCard : "+paymentGroup.isDiffernetCard()+"||Auto bill payment flag : "+paymentGroup.getAutoBillPayment()+"|| Vesta token: "+paymentGroup.getVestaToken());
					logInfo("Billing Address : "+paymentGroup.getBillingAddress().getFirstName()+" | "+paymentGroup.getBillingAddress().getLastName()+
						 " | "+paymentGroup.getBillingAddress().getAddress1()+" | "+paymentGroup.getBillingAddress().getState() +" | "+paymentGroup.getBillingAddress().getPostalCode()+
						 " | "+paymentGroup.getBillingAddress().getCity()+" | "+paymentGroup.getBillingAddress().getCountry());
					}
			}
		}
	
		// now displaying the any adjustments - discounts
		logInfo(" 		###***### 	 REMAING ORDER INFORMATION : ###***### 	 ");
				logInfo("marketCode : "+order.getMarketCode());
				logInfo("totalTax : "+order.getTotalTax());
				logInfo("billingSystemOrderId : "+order.getBillingSystemOrderId());
				logInfo("vestaSystemOrderId	 : "+order.getVestaSystemOrderId());
				logInfo("billSysPaymentRefId : "+order.getBillSysPaymentRefId());
				logInfo("billSysPaymentApprTransId : "+order.getBillSysPaymentApprTransId());
				logInfo("teleSaleCode : "+order.getTeleSaleCode());
				logInfo("estimatedDeliveryDate : "+order.getEstimatedDeliveryDate());
				logInfo("billingQuoteId : "+order.getBillingQuoteId());
				logInfo("posSaleId : "+order.getPosSaleId());
				logInfo("cricCustomerId : "+order.getCricCustomerId());
				logInfo("cricCustmerBillingNumber : "+order.getCricCustmerBillingNumber());
				logInfo("removedPhoneId : "+order.getRemovedPhoneId());
				logInfo("removedPhoneSkuId : "+order.getRemovedPhoneSkuId());
				logInfo("removedPlanId : "+order.getRemovedPlanId());
				logInfo("removedPlanSkuId : "+order.getRemovedPlanSkuId());
				logInfo("upgradeModelNumber : "+order.getUpgradeModelNumber());
				logInfo("upgradeMdn : "+order.getUpgradeMdn());
				logInfo("isDownGrade : "+order.isDownGrade());
				logInfo("user Id Address : "+order.getUserIpAddress());
				logInfo("phoneNumber : "+order.getPhoneNumber());
				logInfo("emailId : "+order.getEmailId());
				logInfo("languageIdentifier : "+order.getLanguageIdentifier());
				logInfo("shippingAddressType : "+order.getShippingAddressType());
				logInfo("billingAddressType : "+order.getBillingAddressType());
				logInfo("oob order details : "+order.toString());*/
		return "";
	}
	 
	
	@Override
	public String getWorkOrderType() {
		 return (String) getPropertyValue(OrderConstants.WORK_ORDER_TYPE);
	}

	@Override
	public void setWorkOrderType(String pWorkOrderType) {
		 setPropertyValue(OrderConstants.WORK_ORDER_TYPE, pWorkOrderType);
		
	}

	@Override
	public String getPackageType() {
		return (String) getPropertyValue(OrderConstants.PACKATE_TYPE);
	}

	@Override
	public void setPackageType(String pPackageType) {
		 
		 setPropertyValue(OrderConstants.PACKATE_TYPE, pPackageType);
	}

	@Override
	public Double getRefundAmount() {
		 
		return (Double) getPropertyValue(OrderConstants.REFUND_AMOUNT);
	}

	@Override
	public void setRefundAmount(Double pRefundAmount) {
		 
		 setPropertyValue(OrderConstants.REFUND_AMOUNT, pRefundAmount);
	}

	@Override
	public String getMarketCode() {
		 
		return (String) getPropertyValue(OrderConstants.MARKET_CODE);
	}

	@Override
	public void setMarketCode(String pMarketCode) {
		 
		 setPropertyValue(OrderConstants.MARKET_CODE, pMarketCode);
	}
	
	public String getAccountAddressId() {
		 
		return (String) getPropertyValue(ACCOUNT_ADDRESS_ID);
	}

	@Override
	public void setAccountAddressId(String pAccountAddressId) {
		 
		 setPropertyValue(ACCOUNT_ADDRESS_ID, pAccountAddressId);
	}

	@Override
	public Double getAccountBalance() {
		 
		return (Double) getPropertyValue(OrderConstants.ACCOUNT_BALANCE);
	}

	@Override
	public void setAccountBalance(Double pAccountBalance) {
		 
		 setPropertyValue(OrderConstants.ACCOUNT_BALANCE, pAccountBalance);
	}

	@Override
	public String getE91desc() {
		 
		return (String) getPropertyValue(OrderConstants.E91_DESC);
	}

	@Override
	public void setE91desc(String pE91desc) {
		 
		 setPropertyValue(OrderConstants.E91_DESC, pE91desc);
	}

	@Override
	public Double getE911amount() {
		 
		return (Double) getPropertyValue(OrderConstants.E911_AMOUNT);
	}

	@Override
	public void setE911amount(Double pE911amount) {
		 setPropertyValue(OrderConstants.E911_AMOUNT, pE911amount);
		
	}

	@Override
	public Double getBoamount() {
		 
		return (Double) getPropertyValue(OrderConstants.BO_AMOUNT);
	}

	@Override
	public void setBoamount(Double pBoamount) {
		 setPropertyValue(OrderConstants.BO_AMOUNT, pBoamount);
		
	}

	@Override
	public Double getTotalTax() {
		 
		return (Double) getPropertyValue(OrderConstants.TOTAL_TAX);
	}

	@Override
	public void setTotalTax(Double pTotalTax) {
		 
		 setPropertyValue(OrderConstants.TOTAL_TAX, pTotalTax);
	}

	@Override
	public String getBillingSystemOrderId() {
		 
		return (String) getPropertyValue(OrderConstants.BILLING_SYSTEM_ORDER_ID);
	}

	@Override
	public void setBillingSystemOrderId(String pBillingSystemOrderId) {
		 
		 setPropertyValue(OrderConstants.BILLING_SYSTEM_ORDER_ID, pBillingSystemOrderId);
	}

	@Override
	public String getVestaSystemOrderId() {
		 
		return (String) getPropertyValue(OrderConstants.VESTA_SYSTEM_ORDER_ID);
	}

	@Override
	public void setVestaSystemOrderId(String pVestaSystemOrderId) {
		 
		 setPropertyValue(OrderConstants.VESTA_SYSTEM_ORDER_ID, pVestaSystemOrderId);
	}

	@Override
	public String getBillSysPaymentRefId() {
		 
		return (String) getPropertyValue(OrderConstants.BILL_SYS_PAYMENT_REF_ID);
	}

	@Override
	public void setBillSysPaymentRefId(String pBillSysPaymentRefId) {
		 setPropertyValue(OrderConstants.BILL_SYS_PAYMENT_REF_ID, pBillSysPaymentRefId);
		
	}

	@Override
	public String getBillSysPaymentApprTransId() {
		 
		return (String) getPropertyValue(OrderConstants.BILLS_PAYMENT_APPR_TRANS_ID);
	}

	@Override
	public void setBillSysPaymentApprTransId(String pBillSysPaymentApprTransId) {
		 setPropertyValue(OrderConstants.BILLS_PAYMENT_APPR_TRANS_ID, pBillSysPaymentApprTransId);
		
	}

	@Override
	public String getTeleSaleCode() {
		 
		return (String) getPropertyValue(OrderConstants.TELE_SALE_CODE);
	}

	@Override
	public void setTeleSaleCode(String pTeleSaleCode) {
		 
		 setPropertyValue(OrderConstants.TELE_SALE_CODE, pTeleSaleCode);
	}

	@Override
	public void setBoTaxName(String pBoTaxName) {
		setPropertyValue(OrderConstants.BO_TAX_NAME, pBoTaxName);
		
	}

	@Override
	public String getBoTaxName() {
		return (String) getPropertyValue(OrderConstants.BO_TAX_NAME);
	}

	@Override
	public void setBoTaxValue(String pBoTaxValue) {
		setPropertyValue(OrderConstants.BO_TAX_VALUE, pBoTaxValue);
		
	}

	@Override
	public String getBoTaxValue() {
		return (String) getPropertyValue(OrderConstants.BO_TAX_VALUE);
	}

	@Override
	public String getTelecomTaxName() {
		return (String) getPropertyValue(OrderConstants.TELECOM_TAX_NAME);
	}

	@Override
	public void setTelecomTaxName(String pTelecomTaxName) {
		setPropertyValue(OrderConstants.TELECOM_TAX_NAME, pTelecomTaxName);
		
	}

	@Override
	public String getTelecomTaxValue() {
		return (String) getPropertyValue(OrderConstants.TELECOM_TAX_VLAUE);
	}

	@Override
	public void setTelecomTaxValue(String pTelecomTaxValue) {
		setPropertyValue(OrderConstants.TELECOM_TAX_VLAUE, pTelecomTaxValue);
		
	}

	 
		
	@Override
	public String getRemovedPhoneId() {
		return (String) getPropertyValue(OrderConstants.REMOVED_PHONE_ID);
	}

	@Override
	public void setRemovedPhoneId(String pRemovedPhoneId) {
		setPropertyValue(OrderConstants.REMOVED_PHONE_ID, pRemovedPhoneId);
		
	}

	@Override
	public String getRemovedPhoneSkuId() {
		return (String) getPropertyValue(OrderConstants.REMOVED_PHONE_SKU_ID);
	}

	@Override
	public void setRemovedPhoneSkuId(String pRemovedPhoneSkuId) {
		setPropertyValue(OrderConstants.REMOVED_PHONE_SKU_ID, pRemovedPhoneSkuId);
		
	}

	@Override
	public String getRemovedPlanId() {
		return (String) getPropertyValue(OrderConstants.REMOVED_PLAN_ID);
	}

	@Override
	public void setRemovedPlanId(String pRemovedPlanId) {
		setPropertyValue(OrderConstants.REMOVED_PLAN_ID, pRemovedPlanId);
		
	}

	@Override
	public String getRemovedPlanSkuId() {
		return (String) getPropertyValue(OrderConstants.REMOVED_PLAN_SKU_ID);
	}

	@Override
	public void setRemovedPlanSkuId(String pRemovedPlanSkuId) {
		setPropertyValue(OrderConstants.REMOVED_PLAN_SKU_ID, pRemovedPlanSkuId);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,String> getRemovedAddons() {
		return (Map<String,String>)getPropertyValue(OrderConstants.REMOVED_ADDONS);
	}

	@Override
	public void setRemovedAddons(Map<String,String> pRemovedAddons) {
		setPropertyValue(OrderConstants.REMOVED_ADDONS, pRemovedAddons);		
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#getEstimatedDeliveryDate()
	 */
	@Override
	public Date getEstimatedDeliveryDate() {
		 
		return (Date) getPropertyValue(OrderConstants.ESTIMATED_DELIVERY_DATE);
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#setEstimatedDeliveryDate(java.lang.String)
	 */
	@Override
	public void setEstimatedDeliveryDate(Date pEstimatedDeliveryDate) {
		setPropertyValue(OrderConstants.ESTIMATED_DELIVERY_DATE, pEstimatedDeliveryDate);
		
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#getPosSaleId()
	 */
	@Override
	public String getBillingQuoteId () {
		 
		return (String) getPropertyValue(OrderConstants.BILLING_QUOTE_ID);
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#setPosSaleId(java.lang.String)
	 */
	@Override
	public void setBillingQuoteId (String pBillingQuoteId ) {
		setPropertyValue(OrderConstants.BILLING_QUOTE_ID, pBillingQuoteId);
		
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#getCricCustomerId()
	 */
	@Override
	public String getCricCustomerId() {
		 
		return (String) getPropertyValue(OrderConstants.CRIC_CUSTOMER_ID);
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#setCricCustomerId(java.lang.String)
	 */
	@Override
	public void setCricCustomerId(String pCricCustomerId) {
		setPropertyValue(OrderConstants.CRIC_CUSTOMER_ID, pCricCustomerId);
		
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#getCricCustmerBillingNumber()
	 */
	@Override
	public String getCricCustmerBillingNumber() {
		 
		return (String) getPropertyValue(OrderConstants.CRIC_CUST_BILLING_NUMBER);
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#SetCricCustmerBillingNumber(java.lang.String)
	 */
	@Override
	public void setCricCustmerBillingNumber(String pCricCustmerBillingNumber) {
		setPropertyValue(OrderConstants.CRIC_CUST_BILLING_NUMBER, pCricCustmerBillingNumber);
		
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#getvestaTransactionId()
	 */
	@Override
	public String getVestaTransactionId() {
		return (String) getPropertyValue(OrderConstants.VESTA_TRANSACTION_ID);
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#SetvestaTransactionId(java.lang.String)
	 */
	@Override
	public void setVestaTransactionId(String pVestaTransactionId) {
		setPropertyValue(OrderConstants.VESTA_TRANSACTION_ID, pVestaTransactionId);
		
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#getPosSaleId()
	 */
	@Override
	public String getPosSaleId() {
		return (String) getPropertyValue(OrderConstants.POS_SALE_ID);
	}

	/* (non-Javadoc)
	 * @see com.cricket.order.CricketOrder#SetPosSaleId(java.lang.String)
	 */
	@Override
	public void setPosSaleId(String pPosSaleId) {
		setPropertyValue(OrderConstants.POS_SALE_ID, pPosSaleId);
		
	}
	 
	/**
	 * @return the removedAddonCompatibility
	 */
	public Map<String, Boolean> getRemovedAddonCompatibility() {
		return removedAddonCompatibility;
	}

	/**
	 * @param removedAddonCompatibility the removedAddonCompatibility to set
	 */
	public void setRemovedAddonCompatibility(
			Map<String, Boolean> removedAddonCompatibility) {
		this.removedAddonCompatibility = removedAddonCompatibility;
	}

	@Override
	public String getUpgradeModelNumber() {
		return (String) getPropertyValue(OrderConstants.UPGRADE_MODEL_NUMBER);
	}

	@Override
	public void setUpgradeModelNumber(String pUpgradeModelNumber) {
		setPropertyValue(OrderConstants.UPGRADE_MODEL_NUMBER, pUpgradeModelNumber);
	}

	@Override
	public String getUpgradeMdn() {
		return (String) getPropertyValue(OrderConstants.UPGRADE_MDN);
	}

	@Override
	public void setUpgradeMdn(String pUpgradeMdn) {
		setPropertyValue(OrderConstants.UPGRADE_MDN, pUpgradeMdn);
	}
	@Override
	public String getShippingMethod() {
		return (String) getPropertyValue(OrderConstants.SHIPPING_METHOD);
	}

	@Override
	public void setShippingMethod(String pShippingMethod) {
		setPropertyValue(OrderConstants.SHIPPING_METHOD, pShippingMethod);
	}
	public void setDownGrade(boolean pIsDownGrade) {
		setPropertyValue(OrderConstants.ISDONW_GRADE, Boolean.valueOf(pIsDownGrade));
	}
	
	public boolean isDownGrade() {
		Boolean returnValue = (Boolean) getPropertyValue(OrderConstants.ISDONW_GRADE);
    	if (returnValue == null) {
            return false;
        } else {
            return returnValue.booleanValue();
        }
	}
	
	@Override
	public void setAnanymousUser(boolean pIsDownGrade) {
		setPropertyValue(OrderConstants.ISANANYMOUS_USER, Boolean.valueOf(pIsDownGrade));
	}
	
	public boolean isAnanymousUser() {
		Boolean returnValue = (Boolean) getPropertyValue(OrderConstants.ISANANYMOUS_USER);
    	if (returnValue == null) {
            return false;
        } else {
            return returnValue.booleanValue();
        }
	}

	@Override
	public void setShippingAddressType(String shippingAddressType) {
		setPropertyValue(OrderConstants.SHIPPING_ADDR_TYPE, shippingAddressType);
	}
	
	@Override
	public String getShippingAddressType() {
		return (String)getPropertyValue(OrderConstants.SHIPPING_ADDR_TYPE);
	}
	
	@Override
	public void setBillingAddressType(String billingAddressType) {
		setPropertyValue(OrderConstants.BILLING_ADDR_TYPE, billingAddressType);
	}
	
	@Override
	public String getBillingAddressType() {
		return (String)getPropertyValue(OrderConstants.BILLING_ADDR_TYPE);
	}
	
	@Override
	public void setPhoneNumber(String phoneNumber) {
		setPropertyValue(OrderConstants.PHONE_NUMBER, phoneNumber);
	}
	
	@Override
	public String getPhoneNumber(){
		return (String)getPropertyValue(OrderConstants.PHONE_NUMBER);
	}
	
	@Override
	public void setEmailId(String emailId) {
		setPropertyValue(OrderConstants.EMAIL_ID, emailId);
	}
	
	@Override
	public String getEmailId(){
		return (String)getPropertyValue(OrderConstants.EMAIL_ID);
	}
	
	@Override
	public void setLanguageIdentifier(String LanguageIdentifier) {
		setPropertyValue(OrderConstants.LANGUAGE_IDENTIFIER, LanguageIdentifier);
	}
	
	@Override
	public String getLanguageIdentifier(){
		return (String)getPropertyValue(OrderConstants.LANGUAGE_IDENTIFIER);
	}

	@Override
	public void setUserIpAddress(String userIpAddress) {
		setPropertyValue(OrderConstants.USER_IP_ADDRESS, userIpAddress);
	}
	
	@Override
	public String getUserIpAddress(){
		return (String)getPropertyValue(OrderConstants.USER_IP_ADDRESS);
	}
	 
}
