/**
 * 
 */
package com.cricket.promotion.droplet;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import atg.commerce.pricing.CricketPromotionManager;
import atg.commerce.pricing.PricingModelHolder;
import atg.commerce.pricing.definition.MatchingObject;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.userprofiling.ProfileServices;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.promotion.vo.PromotionVO;

/**
 * @author TechM
 *
 */
public class DisplayPromotionDroplet extends DynamoServlet{

	CricketPromotionManager cricketPromotionManager;
	ProfileServices profileServices;
	
	/**
	 * This method sets the output for the droplet.
	 * Retrieves all applicable promotions.
	 * Filters based on the profile market type and sets the output.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void service(DynamoHttpServletRequest request,
			DynamoHttpServletResponse response) throws ServletException, IOException {
		
		RepositoryItem product = (RepositoryItem)request.getObjectParameter(CricketCommonConstants.PRODUCT);
		RepositoryItem sku = (RepositoryItem)request.getObjectParameter(CricketCommonConstants.ITEM);
		
		
		double retailPrice = 0.0;
		if(request.getParameter(CricketCommonConstants.RETAIL_PRICE) != null){
			retailPrice = Double.parseDouble(request.getParameter(CricketCommonConstants.RETAIL_PRICE));
		}
		DecimalFormat decimalFormat = new DecimalFormat(".00");
		decimalFormat.setRoundingMode(RoundingMode.DOWN);	
		
		if(isLoggingDebug()){
			logDebug("Start GetPromotionDroplet for product : " + product + " and Sku  : " + sku);
		}
		
		PricingModelHolder userPricingHolder = (PricingModelHolder)request.resolveName("/atg/commerce/pricing/UserPricingModels");
		List<RepositoryItem> promotions =getCricketPromotionManager().getPromotions(product, sku, userPricingHolder); 		 
		PromotionVO promotionVO = new PromotionVO();
		if(null != promotions && promotions.size()>0 && !promotions.isEmpty()){
			
			
			double totalDiscount=0.0;
		 
			Map<String,Double> promotionMap = new HashMap<String,Double>();
			for(RepositoryItem promotion : promotions){		
				 if (isLoggingDebug()) {
					logDebug("Promotion Id : " + promotion.getRepositoryId() + "," + "promotion Type : " + promotion.getPropertyValue(CricketCommonConstants.ITEM_DISC_TYPE));
					logDebug("Prmotion Type :  "+promotion.getItemDisplayName());
				}
				 String promoType = (String) promotion.getPropertyValue(CricketCommonConstants.ITEM_DISC_TYPE);
				 String discType = null;
				 double discAmt=0.0;
				  
				 MatchingObject promotionInfo = getCricketPromotionManager().getPromotionInfo(product, sku, getProfileServices().getCurrentProfile().getDataSource(), request.getLocale(), userPricingHolder, promotion);
				 
				 if(null!= promotionInfo && null!=promotionInfo.getDiscounts() && 	 promotionInfo.getDiscounts().size()>0){
					 if(null!=promotionInfo.getDiscounts().get(0)){
						 discAmt=promotionInfo.getDiscounts().get(0).getAdjuster();
						 discType = promotionInfo.getDiscounts().get(0).getDiscountType();
					 }
				 }
				  
				 if(null!=discType && discType.equals(CricketCommonConstants.PERCENT_OFF)){
					 discAmt = (retailPrice * (discAmt / 100));	
				 }
				 //Amount in two decimal place and restrict to get round off
				 discAmt = Double.parseDouble(decimalFormat.format(discAmt));				 
				 if(promoType != null && !(promoType.equalsIgnoreCase(CricketCommonConstants.MAIL_IN_REBATE)) && discAmt >0.0){
					 if(promoType.equals(CricketCommonConstants.WEB_INSTANT_DISC)){
						 promotionVO.setWebInstantDiscount(discAmt);
					 } else if(promoType.equals(CricketCommonConstants.INSTANT_DISC)){
						 promotionVO.setInstantDiscount(discAmt);
					 }
					 totalDiscount = totalDiscount + discAmt;
					 promotionMap.put(promoType, discAmt);		
				 }else if(promoType != null && (promoType.equalsIgnoreCase(CricketCommonConstants.MAIL_IN_REBATE))){
					 promotionVO.setAmtMIR(discAmt);
				 }else if(discAmt >0.0){
						if(isLoggingDebug()) {
							logDebug((String) promotion.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME) + ":" + discAmt);
						}
					 totalDiscount = totalDiscount + discAmt;
					 promotionMap.put((String) promotion.getPropertyValue(CricketCommonConstants.PROP_DISP_NAME), discAmt);
				 }
			 }
			promotionVO.setPromtions(promotionMap);
			//Amount in two decimal place and restrict to get round off
			totalDiscount = Double.parseDouble(decimalFormat.format(totalDiscount));
			if(isLoggingDebug()) {
				logDebug("Total Promotion Amount:"+totalDiscount);
			}
			promotionVO.setTotalDiscount(totalDiscount);
			if(!promotionMap.isEmpty() && promotionMap.size() >0){
				request.setParameter("promotionInfo", promotionVO);
				request.serviceLocalParameter("Output", request, response);
			}
			else{
				request.setParameter(CricketCommonConstants.PROMOTION, "No Promotion to Display");
				request.serviceLocalParameter("Empty", request, response);
			}
					
		}
		else{
			request.setParameter(CricketCommonConstants.PROMOTION, "No Promotion to Display");
			request.serviceLocalParameter("Empty", request, response);
		}
		
		if(isLoggingDebug()){
			logDebug("End GetPromotionDroplet for product : " + product + " and Sku  : " + sku);
		}		
	}
	
	/**
	 * @return the cricketPromotionManager
	 */
	public CricketPromotionManager getCricketPromotionManager() {
		return cricketPromotionManager;
	}
	/**
	 * @param cricketPromotionManager the cricketPromotionManager to set
	 */
	public void setCricketPromotionManager(
			CricketPromotionManager cricketPromotionManager) {
		this.cricketPromotionManager = cricketPromotionManager;
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
	

}
