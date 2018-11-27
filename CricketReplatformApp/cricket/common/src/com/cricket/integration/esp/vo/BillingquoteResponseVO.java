/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.util.List;

/**
 * @author Cricket
 *
 */
public class BillingquoteResponseVO extends ESPResponseVO {
	
	 private  String billingQuoteId;

	    private java.lang.String billingAccountNumber;
	    
	   private DeviceVO deviceVO;
	   
	   private SubscriberBillingQuoteDetailVO billingQuoteDetail;
	   
	   private List<HotBillChargeVO> hotBillCharge ;
	   
	   
	   

	/**
	 * @param pBillingQuoteId
	 * @param pCustomerId
	 * @param pDeviceVO
	 * @param pBillingQuoteDetail
	 * @param pHotBillCharge
	 */
	public BillingquoteResponseVO(String pBillingQuoteId, String pBillingAccountNumber,
			DeviceVO pDeviceVO,
			SubscriberBillingQuoteDetailVO pBillingQuoteDetail,
			List<HotBillChargeVO> pHotBillCharge) {
		super();
		billingQuoteId = pBillingQuoteId;
		billingAccountNumber = pBillingAccountNumber;
		deviceVO = pDeviceVO;
		billingQuoteDetail = pBillingQuoteDetail;
		hotBillCharge = pHotBillCharge;
	}




	/**
	 * 
	 */
	public BillingquoteResponseVO() {
		// TODO Auto-generated constructor stub
	}




	/**
	 * @return the billingQuoteId
	 */
	public java.lang.String getBillingQuoteId() {
		return billingQuoteId;
	}




	/**
	 * @param pBillingQuoteId the billingQuoteId to set
	 */
	public void setBillingQuoteId(java.lang.String pBillingQuoteId) {
		billingQuoteId = pBillingQuoteId;
	}

	/**
	 * @return the deviceVO
	 */
	public DeviceVO getDeviceVO() {
		return deviceVO;
	}




	/**
	 * @param pDeviceVO the deviceVO to set
	 */
	public void setDeviceVO(DeviceVO pDeviceVO) {
		deviceVO = pDeviceVO;
	}




	/**
	 * @return the billingQuoteDetail
	 */
	public SubscriberBillingQuoteDetailVO getBillingQuoteDetail() {
		return billingQuoteDetail;
	}




	/**
	 * @param pBillingQuoteDetail the billingQuoteDetail to set
	 */
	public void setBillingQuoteDetail(
			SubscriberBillingQuoteDetailVO pBillingQuoteDetail) {
		billingQuoteDetail = pBillingQuoteDetail;
	}




	/**
	 * @return the hotBillCharge
	 */
	public List<HotBillChargeVO> getHotBillCharge() {
		return hotBillCharge;
	}




	/**
	 * @param pHotBillCharge the hotBillCharge to set
	 */
	public void setHotBillCharge(List<HotBillChargeVO> pHotBillCharge) {
		hotBillCharge = pHotBillCharge;
	}




	/**
	 * @return the billingAccountNumber
	 */
	public java.lang.String getBillingAccountNumber() {
		return billingAccountNumber;
	}




	/**
	 * @param pBillingAccountNumber the billingAccountNumber to set
	 */
	public void setBillingAccountNumber(java.lang.String pBillingAccountNumber) {
		billingAccountNumber = pBillingAccountNumber;
	}

}
