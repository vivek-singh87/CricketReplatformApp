package com.cricket.user.session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.PositiveInteger;

import atg.nucleus.GenericService;

import com.cricket.integration.esp.vo.AddressVO;
import com.cricket.integration.esp.vo.OfferingsVO;
import com.cricket.integration.esp.vo.ResponseVO;
import com.cricket.integration.esp.vo.SubscriberVO;

public class UserAccountInformation extends GenericService {
	private String billingAccountNumber;
	private String accountType;
	private String accountStatus;
	private String marketId;
	private String jointVentureCode;
	private PositiveInteger rateCenterId;
	private Calendar billingCycleDate;
	private String billingPreferencesLanguage;
	private boolean billingPreferencesABP;
	private String solicitationContactPreference;
	private String customerId;
	private String customerType;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String phone;
	private Date dob;
	private List<SubscriberVO> subscribers;
	private ResponseVO response;
	private AddressVO marketAddress;
	private AddressVO billingAddress;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public PositiveInteger getRateCenterId() {
		return rateCenterId;
	}
	public void setRateCenterId(PositiveInteger rateCenterId) {
		this.rateCenterId = rateCenterId;
	}
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public String getJointVentureCode() {
		return jointVentureCode;
	}
	public void setJointVentureCode(String jointVentureCode) {
		this.jointVentureCode = jointVentureCode;
	}
	public String getBillingAccountNumber() {
		return billingAccountNumber;
	}
	public void setBillingAccountNumber(String billingAccountNumber) {
		this.billingAccountNumber = billingAccountNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Calendar getBillingCycleDate() {
		return billingCycleDate;
	}
	public void setBillingCycleDate(Calendar billingCycleDate) {
		this.billingCycleDate = billingCycleDate;
	}
	public String getBillingPreferencesLanguage() {
		return billingPreferencesLanguage;
	}
	public void setBillingPreferencesLanguage(String billingPreferencesLanguage) {
		this.billingPreferencesLanguage = billingPreferencesLanguage;
	}
	
	public boolean isBillingPreferencesABP() {
		return billingPreferencesABP;
	}
	public void setBillingPreferencesABP(boolean billingPreferencesABP) {
		this.billingPreferencesABP = billingPreferencesABP;
	}
	public String getSolicitationContactPreference() {
		return solicitationContactPreference;
	}
	public void setSolicitationContactPreference(
			String solicitationContactPreference) {
		this.solicitationContactPreference = solicitationContactPreference;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public List<SubscriberVO> getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(List<SubscriberVO> subscribers) {
		this.subscribers = subscribers;
	}
	
	public ResponseVO getResponse() {
		return response;
	}
	public void setResponse(ResponseVO response) {
		this.response = response;
	}
	public AddressVO getMarketAddress() {
		return marketAddress;
	}
	public void setMarketAddress(AddressVO marketAddress) {
		this.marketAddress = marketAddress;
	}
	public AddressVO getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(AddressVO billingAddress) {
		this.billingAddress = billingAddress;
	}
	
	/**
	 * @return
	 */
	public Map<String,List<OfferingsVO>> getAllOfferingsPerMdn(){
		 List<SubscriberVO> subcribers = getSubscribers();
		 List<OfferingsVO> offerings = null;
		 String mdn = null;
		 Map<String,List<OfferingsVO>> oferingsPerMdn = new HashMap<String,List<OfferingsVO>>();
				  
		 if(null!=subcribers && subcribers.size()>0){
			 for(SubscriberVO subscriber:subcribers){
				 offerings = new ArrayList<OfferingsVO>();
				 mdn = subscriber.getMdn();
				if(subscriber.getBundledOfferings()!=null && subscriber.getBundledOfferings().size()>0){				
				    offerings.addAll(subscriber.getBundledOfferings());
				}
				if(subscriber.getAdditionalOfferings()!=null && subscriber.getAdditionalOfferings().size()>0){				
					 offerings.addAll(subscriber.getAdditionalOfferings());
				}
				oferingsPerMdn.put(mdn,offerings);
			 }
		 }
		 
		 return oferingsPerMdn;
	}
	
	
	/**
	 * @param mdn
	 * @return
	 */
	public List<String> getOfferingCodesPerMdn(String mdn){
		Map<String,List<OfferingsVO>> allOfferings = getAllOfferingsPerMdn();
		List<String> offeringCodes = null;
		List<OfferingsVO> offeringVos = allOfferings.get(mdn);
		if(offeringVos != null && offeringVos.size() > 0){
			offeringCodes = new ArrayList<String>();
			for(OfferingsVO offeringVO : offeringVos){
				if(!offeringCodes.contains(offeringVO.getOfferingCode())){
				offeringCodes.add(offeringVO.getOfferingCode());
				}
			}
			
		}		
		return offeringCodes;
		
	}
}
