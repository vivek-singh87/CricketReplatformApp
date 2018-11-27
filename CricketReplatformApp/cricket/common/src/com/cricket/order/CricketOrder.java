
package com.cricket.order;
import java.util.Date;
import java.util.List;
import java.util.Map;

import atg.commerce.order.Order;

import com.cricket.commerce.order.CricketPackage;
 


public interface CricketOrder extends Order {
 
	/**
	 * @return the workOrderType
	 */
	public String getWorkOrderType();  
	/**
	 * @param pWorkOrderType the workOrderType to set
	 */
	public void setWorkOrderType(String pWorkOrderType);
	/**
	 * @return the packageType
	 */
	public String getPackageType();
	/**
	 * @param pPackageType the packageType to set
	 */
	public void setPackageType(String pPackageType);
	/**
	 * @return the refundAmount
	 */
	public Double getRefundAmount();
	/**
	 * @param pRefundAmount the refundAmount to set
	 */
	public void setRefundAmount(Double pRefundAmount);
	/**
	 * @return the marketCode
	 */
	public String getMarketCode();
	/**
	 * @param pMarketCode the marketCode to set
	 */
	public void setMarketCode(String pMarketCode);
	
	/**
	 * @return the AccountAddressId
	 */
	public String getAccountAddressId();
	/**
	 * @param pAccountAddressId the AccountAddressId to set
	 */
	public void setAccountAddressId(String pAccountAddressId);
	/**
	 * @return the accountBalance
	 */
	public Double getAccountBalance();
	/**
	 * @param pAccountBalance the accountBalance to set
	 */
	public void setAccountBalance(Double pAccountBalance);
	/**
	 * @return the e91desc
	 */
	public String getE91desc();
	/**
	 * @param pE91desc the e91desc to set
	 */
	public void setE91desc(String pE91desc);
	/**
	 * @return the e911amount
	 */
	public Double getE911amount();
	/**
	 * @param pE911amount the e911amount to set
	 */
	public void setE911amount(Double pE911amount);
	/**
	 * @return the boamount
	 */
	public Double getBoamount();
	/**
	 * @param pBoamount the boamount to set
	 */
	public void setBoamount(Double pBoamount);
	/**
	 * @return the totalTax
	 */
	public Double getTotalTax();
	/**
	 * @param totalTax the totalTax to set
	 */
	public void setTotalTax(Double pTotalTax);
	/**
	 * @return the billingSystemOrderId
	 */
	public String getBillingSystemOrderId();
	/**
	 * @param pBillingSystemOrderId the billingSystemOrderId to set
	 */
	public void setBillingSystemOrderId(String pBillingSystemOrderId);
	/**
	 * @return the vestaSystemOrderId
	 */
	public String getVestaSystemOrderId();
	/**
	 * @param pVestaSystemOrderId the vestaSystemOrderId to set
	 */
	public void setVestaSystemOrderId(String pVestaSystemOrderId);
	/**
	 * @return the billSysPaymentRefId
	 */
	public String getBillSysPaymentRefId();
	/**
	 * @param pBillSysPaymentRefId the billSysPaymentRefId to set
	 */
	public void setBillSysPaymentRefId(String pBillSysPaymentRefId);
	/**
	 * @return the billSysPaymentApprTransId
	 */
	public String getBillSysPaymentApprTransId();
	/**
	 * @param pBillSysPaymentApprTransId the billSysPaymentApprTransId to set
	 */
	public void setBillSysPaymentApprTransId(String pBillSysPaymentApprTransId);
	/**
	 * @return the teleSaleCode
	 */
	public String getTeleSaleCode();
	/**
	 * @param pTeleSaleCode the teleSaleCode to set
	 */
	public void setTeleSaleCode(String pTeleSaleCode);
		

 
	 
	/**
	 * @param pCktPackages
	 */
	public void setCktPackages(List<CricketPackage> pCktPackages);
	
	/**
	 * @return
	 */
	public List<CricketPackage> getCktPackages();
	
	/**
	 * @param pBoTaxName
	 */
	public void setBoTaxName(String pBoTaxName);
	
	/**
	 * @return
	 */
	public String getBoTaxName();
	
	/**
	 * @param pBoTaxValue
	 */
	public void setBoTaxValue(String pBoTaxValue);
	
	/**
	 * @return
	 */
	public String getBoTaxValue();
	 
	/**
	 * @return
	 */
	public String getTelecomTaxName();
	
	/**
	 * @param pTelecomTaxName
	 */
	public void setTelecomTaxName(String pTelecomTaxName);
	
	/**
	 * @return
	 */
	public String getTelecomTaxValue();
	
	/**
	 * @param pTelecomTaxValue
	 */
	public void setTelecomTaxValue(String pTelecomTaxValue);
	
	/**
	 * @return
	 */
	public String getRemovedPhoneId();
	
	/**
	 * @param pRemovedPhoneId
	 */
	public void setRemovedPhoneId(String pRemovedPhoneId);
	
	/**
	 * @return
	 */
	public String getRemovedPhoneSkuId();
	
	/**
	 * @param pRemovedPhoneSkuId
	 */
	public void setRemovedPhoneSkuId(String pRemovedPhoneSkuId);
	
	/**
	 * @return
	 */
	public String getRemovedPlanId();
	
	/**
	 * @param pRemovedPlanId
	 */
	public void setRemovedPlanId(String pRemovedPlanId); 	
	
	/**
	 * @return
	 */
	public String getRemovedPlanSkuId();
	
	/**
	 * @param pRemovedPlanSkuId
	 */
	public void setRemovedPlanSkuId(String pRemovedPlanSkuId); 
	
	/**
	 * @return
	 */ 										
	public Map getRemovedAddons();

	/**
	 * @param pRemovedAddons
	 */
	public void setRemovedAddons(Map<String,String> pRemovedAddons);	
	
	
	 
	/**
	 * @return
	 */
	public Date  getEstimatedDeliveryDate();
	
	/**
	 * @param pEstimatedDeliveryDate
	 */
	public void setEstimatedDeliveryDate(Date pEstimatedDeliveryDate);
	
	
	
	/**
	 * @return
	 */
	public String getBillingQuoteId ();
	
	/**
	 * @param pPosSaleId
	 */
	public void setBillingQuoteId (String pBillingQuoteId );
	

	/**
	 * @return
	 */
	public String getCricCustomerId();
	
	/**
	 * @param pCricCustomerId
	 */
	public void setCricCustomerId(String pCricCustomerId);
	
	
	 
	/**
	 * @return
	 */
	public String getCricCustmerBillingNumber();
	
	/**
	 * @param pCricCustmerBillingNumber
	 */
	public void setCricCustmerBillingNumber(String pCricCustmerBillingNumber);
	
	
	/**
	 * @return
	 */
	public String getVestaTransactionId();
	
	/**
	 * @param pVestaTransactionId
	 */
	public void setVestaTransactionId(String pVestaTransactionId);
	
	/**
	 * @return
	 */
	public String getPosSaleId ();
	
	/**
	 * @param pPosSaleId
	 */
	public void setPosSaleId (String pPosSaleId );
	
	/**
	 * @return
	 */
	public String getUpgradeModelNumber ();
	
	/**
	 * @param pPosSaleId
	 */
	public void setUpgradeModelNumber(String pUpgradeModelNumber);
	
	/**
	 * @return
	 */
	public String getUpgradeMdn();
	
	/**
	 * @param pPosSaleId
	 */
	public void setUpgradeMdn(String pUpgradeMdn);
	
	/**
	 * @return
	 */
	public String getShippingMethod();
	
	/**
	 * @param pPosSaleId
	 */
	public void setShippingMethod(String pShippingMethod);
	
	/**
	 * @return
	 */
	public boolean isDownGrade();
	
	/**
	 * @param downGrade
	 */
	public void setDownGrade(boolean pIsDownGrade);
	
	/**
	 * @return
	 */
	public boolean isAnanymousUser();
	
	/**
	 * @param downGrade
	 */
	public void setAnanymousUser(boolean pIsAnanymousUser);
	
	public void setShippingAddressType(String shippingAddressType);
	
	public String getShippingAddressType();
	
	public void setBillingAddressType(String billingAddressType);
	
	public String getBillingAddressType();
	
	public void setPhoneNumber(String phoneNumber);
	
	public String getPhoneNumber();
	
	public void setEmailId(String emailId);
	
	public String getEmailId();	
	public void setLanguageIdentifier(String languageIdentifier);
	
	public String getLanguageIdentifier();	
	
	public void setUserIpAddress(String userIpAddress);
	
	public String getUserIpAddress();
 
}
