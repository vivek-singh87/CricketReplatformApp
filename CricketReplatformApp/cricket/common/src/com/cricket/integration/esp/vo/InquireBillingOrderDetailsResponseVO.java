/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.util.Calendar;
import java.util.Date;

import atg.nucleus.GenericService;

/**
 * @author Cricket
 *
 */
public class InquireBillingOrderDetailsResponseVO {

	/**
	 * 
	 */
	public InquireBillingOrderDetailsResponseVO() {
	
	}
		
		private String billingOrderNumber;
		
		  private String externalId; 
		  
		  private String mdn;
		  
		  private String min;
		  
		  private String  orderTypeCode; 
		  
		  private String  orderStatus; 
		  
		  private String billingQuoteNumber; 
		  
		  private String  purchaseOrderNumber; 
		  
		  private Calendar createdDate; 
		  
		  private Calendar executedDate; 
		  
		  private Calendar submittedDate; 
		  
		  private String salesRepresentativeInfo; 
		  
		  private Calendar lastModifiedDate;

		  private ESPResponseVO[] response;
		  
		  private String trackingNumber;
		  
		  private boolean ifOrderExist=true;
		  
		 

		/**
		 * @param pBillingOrderNumber
		 * @param pExternalId
		 * @param pMdn
		 * @param pMin
		 * @param pOrderTypeCode
		 * @param pOrderStatus
		 * @param pBillingQuoteNumber
		 * @param pPurchaseOrderNumber
		 * @param pCreatedDate
		 * @param pExecutedDate
		 * @param pSubmittedDate
		 * @param pSalesRepresentativeInfo
		 * @param pLastModifiedDate
		 * @param pResponse
		 * @param pTrackingNumber
		 */
		public InquireBillingOrderDetailsResponseVO(String pBillingOrderNumber,
				String pExternalId, String pMdn, String pMin,
				String pOrderTypeCode, String pOrderStatus,
				String pBillingQuoteNumber, String pPurchaseOrderNumber,
				Calendar pCreatedDate, Calendar pExecutedDate,
				Calendar pSubmittedDate, String pSalesRepresentativeInfo,
				Calendar pLastModifiedDate, ESPResponseVO[] pResponse,String pTrackingNumber,boolean pIfOrderExist) {
			super();
			billingOrderNumber = pBillingOrderNumber;
			externalId = pExternalId;
			mdn = pMdn;
			min = pMin;
			orderTypeCode = pOrderTypeCode;
			orderStatus = pOrderStatus;
			billingQuoteNumber = pBillingQuoteNumber;
			purchaseOrderNumber = pPurchaseOrderNumber;
			createdDate = pCreatedDate;
			executedDate = pExecutedDate;
			submittedDate = pSubmittedDate;
			salesRepresentativeInfo = pSalesRepresentativeInfo;
			lastModifiedDate = pLastModifiedDate;
			response = pResponse;
			trackingNumber=pTrackingNumber;
			ifOrderExist=pIfOrderExist;
		}

		/**
		 * @return the billingOrderNumber
		 */
		public String getBillingOrderNumber() {
			return billingOrderNumber;
		}

		/**
		 * @param pBillingOrderNumber the billingOrderNumber to set
		 */
		public void setBillingOrderNumber(String pBillingOrderNumber) {
			billingOrderNumber = pBillingOrderNumber;
		}

		/**
		 * @return the externalId
		 */
		public String getExternalId() {
			return externalId;
		}

		/**
		 * @param pExternalId the externalId to set
		 */
		public void setExternalId(String pExternalId) {
			externalId = pExternalId;
		}

		/**
		 * @return the mdn
		 */
		public String getMdn() {
			return mdn;
		}

		/**
		 * @param pMdn the mdn to set
		 */
		public void setMdn(String pMdn) {
			mdn = pMdn;
		}

		/**
		 * @return the min
		 */
		public String getMin() {
			return min;
		}

		/**
		 * @param pMin the min to set
		 */
		public void setMin(String pMin) {
			min = pMin;
		}

		/**
		 * @return the orderTypeCode
		 */
		public String getOrderTypeCode() {
			return orderTypeCode;
		}

		/**
		 * @param pOrderTypeCode the orderTypeCode to set
		 */
		public void setOrderTypeCode(String pOrderTypeCode) {
			orderTypeCode = pOrderTypeCode;
		}

		/**
		 * @return the orderStatus
		 */
		public String getOrderStatus() {
			return orderStatus;
		}

		/**
		 * @param pOrderStatus the orderStatus to set
		 */
		public void setOrderStatus(String pOrderStatus) {
			orderStatus = pOrderStatus;
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
		 * @return the purchaseOrderNumber
		 */
		public String getPurchaseOrderNumber() {
			return purchaseOrderNumber;
		}

		/**
		 * @param pPurchaseOrderNumber the purchaseOrderNumber to set
		 */
		public void setPurchaseOrderNumber(String pPurchaseOrderNumber) {
			purchaseOrderNumber = pPurchaseOrderNumber;
		}

		/**
		 * @return the createdDate
		 */
		public Date getCreatedDate() {
			return createdDate.getTime();
		}

		/**
		 * @param pCalendar the createdDate to set
		 */
		public void setCreatedDate(Calendar pCalendar) {
			createdDate = pCalendar;
		}

		/**
		 * @return the executedDate
		 */
		public Date getExecutedDate() {
			return executedDate.getTime();
		}

		/**
		 * @param pCalendar the executedDate to set
		 */
		public void setExecutedDate(Calendar pCalendar) {
			executedDate = pCalendar;
		}

		/**
		 * @return the submittedDate
		 */
		public Date getSubmittedDate() {
			return submittedDate.getTime();
		}

		/**
		 * @param pCalendar the submittedDate to set
		 */
		public void setSubmittedDate(Calendar pCalendar) {
			submittedDate = pCalendar;
		}

		/**
		 * @return the salesRepresentativeInfo
		 */
		public String getSalesRepresentativeInfo() {
			return salesRepresentativeInfo;
		}

		/**
		 * @param pSalesRepresentativeInfo the salesRepresentativeInfo to set
		 */
		public void setSalesRepresentativeInfo(String pSalesRepresentativeInfo) {
			salesRepresentativeInfo = pSalesRepresentativeInfo;
		}

		/**
		 * @return the lastModifiedDate
		 */
		public Date getLastModifiedDate() {
			return lastModifiedDate.getTime();
		}

		/**
		 * @param pCalendar the lastModifiedDate to set
		 */
		public void setLastModifiedDate(Calendar pCalendar) {
			lastModifiedDate = pCalendar;
		}

		/**
		 * @return the response
		 */
		public ESPResponseVO[] getResponse() {
			return response;
		}

		/**
		 * @param pResponse the response to set
		 */
		public void setResponse(ESPResponseVO[] pResponse) {
			response = pResponse;
		}

		public String getTrackingNumber() {
			return trackingNumber;
		}

		public void setTrackingNumber(String trackingNumber) {
			this.trackingNumber = trackingNumber;
		}

		public boolean isIfOrderExist() {
			return ifOrderExist;
		}

		public void setIfOrderExist(boolean ifOrderExist) {
			this.ifOrderExist = ifOrderExist;
		} 

	

}
