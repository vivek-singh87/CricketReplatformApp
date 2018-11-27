/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.util.Calendar;

/**
 * @author MM112358
 *
 */
public class UpdateWebReportRequestVO {

	 private String billingQuoteNumber;

	    private Calendar quoteCreatedDate;

	    private String billingOrderId;

	    private Calendar orderCreatedDate;

	    private String accountNumber;

	    private String transactionType;

	    private String marketId;

	    private String salesRepresentative;

	    private  PromotionVO promotionInfo;

	    private String language;

	    /* Customer information. */
	    private UpdateWebReportRequestVOCustomer customer;

	    private UpdateWebReportRequestVOItems[] items;

	    
	/**
	 * 
	 */
	public UpdateWebReportRequestVO() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param pBillingQuoteNumber
	 * @param pQuoteCreatedDate
	 * @param pBillingOrderId
	 * @param pOrderCreatedDate
	 * @param pAccountNumber
	 * @param pTransactionType
	 * @param pMarketId
	 * @param pSalesRepresentative
	 * @param pPromotionInfo
	 * @param pLanguage
	 * @param pCustomer
	 * @param pItems
	 */
	public UpdateWebReportRequestVO(String pBillingQuoteNumber,
			Calendar pQuoteCreatedDate, String pBillingOrderId,
			Calendar pOrderCreatedDate, String pAccountNumber,
			String pTransactionType, String pMarketId,
			String pSalesRepresentative, PromotionVO pPromotionInfo,
			String pLanguage, UpdateWebReportRequestVOCustomer pCustomer,
			UpdateWebReportRequestVOItems[] pItems) {
		super();
		billingQuoteNumber = pBillingQuoteNumber;
		quoteCreatedDate = pQuoteCreatedDate;
		billingOrderId = pBillingOrderId;
		orderCreatedDate = pOrderCreatedDate;
		accountNumber = pAccountNumber;
		transactionType = pTransactionType;
		marketId = pMarketId;
		salesRepresentative = pSalesRepresentative;
		promotionInfo = pPromotionInfo;
		language = pLanguage;
		customer = pCustomer;
		items = pItems;
	}


	/**
	 * @return the billingQuoteNumber
	 */
	public String getBillingQuoteNumber() {
		return billingQuoteNumber;
	}


	/**
	 * @param pBillingQuoteNumber the billingQuoteNumber to set
	 */
	public void setBillingQuoteNumber(String pBillingQuoteNumber) {
		billingQuoteNumber = pBillingQuoteNumber;
	}


	/**
	 * @return the quoteCreatedDate
	 */
	public Calendar getQuoteCreatedDate() {
		return quoteCreatedDate;
	}


	/**
	 * @param pQuoteCreatedDate the quoteCreatedDate to set
	 */
	public void setQuoteCreatedDate(Calendar pQuoteCreatedDate) {
		quoteCreatedDate = pQuoteCreatedDate;
	}


	/**
	 * @return the billingOrderId
	 */
	public String getBillingOrderId() {
		return billingOrderId;
	}


	/**
	 * @param pBillingOrderId the billingOrderId to set
	 */
	public void setBillingOrderId(String pBillingOrderId) {
		billingOrderId = pBillingOrderId;
	}


	/**
	 * @return the orderCreatedDate
	 */
	public Calendar getOrderCreatedDate() {
		return orderCreatedDate;
	}


	/**
	 * @param pOrderCreatedDate the orderCreatedDate to set
	 */
	public void setOrderCreatedDate(Calendar pOrderCreatedDate) {
		orderCreatedDate = pOrderCreatedDate;
	}


	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}


	/**
	 * @param pAccountNumber the accountNumber to set
	 */
	public void setAccountNumber(String pAccountNumber) {
		accountNumber = pAccountNumber;
	}


	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}


	/**
	 * @param pTransactionType the transactionType to set
	 */
	public void setTransactionType(String pTransactionType) {
		transactionType = pTransactionType;
	}


	/**
	 * @return the marketId
	 */
	public String getMarketId() {
		return marketId;
	}


	/**
	 * @param pMarketId the marketId to set
	 */
	public void setMarketId(String pMarketId) {
		marketId = pMarketId;
	}


	/**
	 * @return the salesRepresentative
	 */
	public String getSalesRepresentative() {
		return salesRepresentative;
	}


	/**
	 * @param pSalesRepresentative the salesRepresentative to set
	 */
	public void setSalesRepresentative(String pSalesRepresentative) {
		salesRepresentative = pSalesRepresentative;
	}


	/**
	 * @return the promotionInfo
	 */
	public PromotionVO getPromotionInfo() {
		return promotionInfo;
	}


	/**
	 * @param pPromotionInfo the promotionInfo to set
	 */
	public void setPromotionInfo(PromotionVO pPromotionInfo) {
		promotionInfo = pPromotionInfo;
	}


	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}


	/**
	 * @param pLanguage the language to set
	 */
	public void setLanguage(String pLanguage) {
		language = pLanguage;
	}


	/**
	 * @return the customer
	 */
	public UpdateWebReportRequestVOCustomer getCustomer() {
		return customer;
	}


	/**
	 * @param pCustomer the customer to set
	 */
	public void setCustomer(UpdateWebReportRequestVOCustomer pCustomer) {
		customer = pCustomer;
	}


	/**
	 * @return the items
	 */
	public UpdateWebReportRequestVOItems[] getItems() {
		return items;
	}


	/**
	 * @param pItems the items to set
	 */
	public void setItems(UpdateWebReportRequestVOItems[] pItems) {
		items = pItems;
	}
	 

    /**
     * @param i
     * @return
     */
    public  UpdateWebReportRequestVOItems getItems(int i) {
        return this.items[i];
    }

    /**
     * @param i
     * @param value
     */
    public void setItems(int i,  UpdateWebReportRequestVOItems value) {
        this.items[i] = value;
    }
}
