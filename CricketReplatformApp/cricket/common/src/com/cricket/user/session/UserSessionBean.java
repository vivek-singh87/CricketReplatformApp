package com.cricket.user.session;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import atg.nucleus.GenericService;

import com.cricket.integration.esp.vo.InquireFeaturesResponseVO;

/**
 * @author TechMahindra 
 *
 */
public class UserSessionBean extends GenericService {
	
	private String compareProductIDs;
	
	private List<String> comapreImageUrls;
	
	private int numberOfItemsInCompare;
	
	private boolean autoBillPayment;
	
	private String couponCode;
	
	private String specialURLCouponCode;
	
	private String lastFourDugitsofCard;
	
	private Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersMap;
	
	private Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersUpgradePlanMap;
	
	private List<InquireFeaturesResponseVO> hppAddOns;

	public Map<String, List<InquireFeaturesResponseVO>> getMandatoryOffersMap() {
		return mandatoryOffersMap;
	}

	public void setMandatoryOffersMap(
			Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersMap) {
		this.mandatoryOffersMap = mandatoryOffersMap;
	}

	public Map<String, List<InquireFeaturesResponseVO>> getMandatoryOffersUpgradePlanMap() {
		return mandatoryOffersUpgradePlanMap;
	}

	public void setMandatoryOffersUpgradePlanMap(
			Map<String, List<InquireFeaturesResponseVO>> mandatoryOffersUpgradePlanMap) {
		this.mandatoryOffersUpgradePlanMap = mandatoryOffersUpgradePlanMap;
	}
	
	public String getCompareProductIDs() {
		return compareProductIDs;
	}

	public void setCompareProductIDs(String compareProductIDs) {
		this.compareProductIDs = compareProductIDs;
	}

	public List<String> getComapreImageUrls() {
		return comapreImageUrls;
	}

	public void setComapreImageUrls(List<String> comapreImageUrls) {
		this.comapreImageUrls = comapreImageUrls;
	}

	public int getNumberOfItemsInCompare() {
		return numberOfItemsInCompare;
	}

	public void setNumberOfItemsInCompare(int numberOfItemsInCompare) {
		this.numberOfItemsInCompare = numberOfItemsInCompare;
	}

	/**
	 * @return the autoBillPayment
	 */
	public boolean isAutoBillPayment() {
		return autoBillPayment;
	}

	/**
	 * @param autoBillPayment the autoBillPayment to set
	 */
	public void setAutoBillPayment(boolean autoBillPayment) {
		this.autoBillPayment = autoBillPayment;
	}

	/**
	 * @return the couponCode
	 */
	public String getCouponCode() {
		return couponCode;
	}

	/**
	 * @param couponCode the couponCode to set
	 */
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	/**
	 * @return the lastFourDugitsofCard
	 */
	public String getLastFourDugitsofCard() {
		return lastFourDugitsofCard;
	}

	/**
	 * @param lastFourDugitsofCard the lastFourDugitsofCard to set
	 */
	public void setLastFourDugitsofCard(String lastFourDugitsofCard) {
		this.lastFourDugitsofCard = lastFourDugitsofCard;
	}
	
	/**
	 * @return the specialURLCouponCode
	 */
	public String getSpecialURLCouponCode() {
		return specialURLCouponCode;
	}
	/**
	 * @param specialURLCouponCode to set
	 */
	public void setSpecialURLCouponCode(String specialURLCouponCode) {
		this.specialURLCouponCode = specialURLCouponCode;
	}

	@Override
	public String toString() {
		StringBuilder strValue = new StringBuilder();
		strValue.append("\n-------------------------------     UserSessionBean  Info      -------------------------------");
		strValue.append("compareProductIDs = "+compareProductIDs);
		strValue.append(" : numberOfItemsInCompare = "+numberOfItemsInCompare);
		strValue.append(" : autoBillPayment = "+autoBillPayment);
		strValue.append(" : couponCode = "+couponCode);
		strValue.append(" : Special URL couponCode = "+ specialURLCouponCode);
		strValue.append(" : lastFourDugitsofCard = "+lastFourDugitsofCard);
		if(comapreImageUrls != null){
			strValue.append(" : comapreImageUrls = "+comapreImageUrls);
		}
		
		if(mandatoryOffersMap != null){
			
			strValue.append(" : mandatoryOffersMap = { ");
			
			Set<Entry<String, List<InquireFeaturesResponseVO>>> entrySet = mandatoryOffersMap.entrySet();
			
			for(Entry<String, List<InquireFeaturesResponseVO>> entry : entrySet){
				
				strValue.append(entry.getKey()+" = < ");
				
				for(InquireFeaturesResponseVO responseVo : entry.getValue()){
					strValue.append(responseVo.toString()+",");
				}
				
				strValue.append(" > ");
				
			}
			
			strValue.append(" } ");
			
		}
		
		if(mandatoryOffersUpgradePlanMap != null){
			
			strValue.append(" : mandatoryOffersUpgradePlanMap = { ");
			
			Set<Entry<String, List<InquireFeaturesResponseVO>>> entrySet = mandatoryOffersUpgradePlanMap.entrySet();
			
			for(Entry<String, List<InquireFeaturesResponseVO>> entry : entrySet){
				
				strValue.append(entry.getKey()+" = < ");
				
				for(InquireFeaturesResponseVO responseVo : entry.getValue()){
					strValue.append(responseVo.toString()+",");
				}
				
				strValue.append(" > ");
				
			}
			
			strValue.append(" } ");
			
		}
		
		
		return strValue.toString();
	}

	/**
	 * @return the hppAddOns
	 */
	public List<InquireFeaturesResponseVO> getHppAddOns() {
		return hppAddOns;
	}

	/**
	 * @param hppAddOns the hppAddOns to set
	 */
	public void setHppAddOns(List<InquireFeaturesResponseVO> hppAddOns) {
		this.hppAddOns = hppAddOns;
	}
	

}
