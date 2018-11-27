package com.cricket.util;

import java.util.List;

import com.cricket.integration.esp.vo.SubscriberChargesVO;

import atg.nucleus.GenericService;

public class EspServiceResponseData extends GenericService{

	 private List<CAQServiceAmount> serviceAmountList;
	 /* This list will be set from ProcProcessUpdateSubscriber and
	  * used in CricketProratedPriceCalculator logic while doing Change Plan/Feature 
	  */
	 private List<SubscriberChargesVO> recurringSubscriberChargesList;

	/**
	 * @return the serviceAmountList
	 */
	public List<CAQServiceAmount> getServiceAmountList() {
		return serviceAmountList;
	}

	/**
	 * @param serviceAmountList the serviceAmountList to set
	 */
	public void setServiceAmountList(List<CAQServiceAmount> serviceAmountList) {
		this.serviceAmountList = serviceAmountList;
	}
	
	/**
	 * @return the recurringSubscriberChargesList
	 */
	public List<SubscriberChargesVO> getRecurringSubscriberChargesList() {
		return recurringSubscriberChargesList;
	}

	/**
	 * @param recurringSubscriberChargesList the recurringSubscriberChargesList to set
	 */
	public void setRecurringSubscriberChargesList(
			List<SubscriberChargesVO> recurringSubscriberChargesList) {
		this.recurringSubscriberChargesList = recurringSubscriberChargesList;
	}

	// get the total service amounts for updateBillingQuoteStatus
	public double getTotalServiceAmount(){
		double totalServiceAmount =0.0;
  		  if(null!=serviceAmountList && serviceAmountList.size()>0){				
			   for(CAQServiceAmount caqServiceAmount:serviceAmountList){
				   totalServiceAmount+= caqServiceAmount.getServiceAmount().doubleValue()+caqServiceAmount.getServiceTax().doubleValue();
			  }
 		   }
  		return totalServiceAmount;
		}
	
	@Override
	public String toString() {
	
		StringBuilder strValue = new StringBuilder();
		strValue.append("\n-------------------------------     EspServiceResponseData  Info      -------------------------------");
		
		if(serviceAmountList != null){
			strValue.append("serviceAmountList = < ");
			for(CAQServiceAmount serviceAmount : serviceAmountList){
				strValue.append(serviceAmount.toString());
			}
			strValue.append(" >");
		}
		
		if(recurringSubscriberChargesList != null){
			strValue.append("serviceAmountList = < ");
			for(SubscriberChargesVO charges : recurringSubscriberChargesList){
				strValue.append(charges.toString());
			}
			strValue.append(" >");
		}
		
		return strValue.toString();
	}
	
}
