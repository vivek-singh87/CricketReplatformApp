package com.cricket.browse.droplet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.commerce.pricing.PMDLCacheAdapter;
import atg.commerce.pricing.definition.AndElem;
import atg.commerce.pricing.definition.ConstantElem;
import atg.commerce.pricing.definition.ContainsElem;
import atg.commerce.pricing.definition.GreaterThanElem;
import atg.commerce.pricing.definition.GreaterThanOrEqualsElem;
import atg.commerce.pricing.definition.PricingModelElem;
import atg.commerce.pricing.definition.PricingModelExpression;
import atg.commerce.promotion.PromotionException;
import atg.commerce.promotion.PromotionTools;
import atg.nucleus.Nucleus;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class PromotionUpsellAmountDroplet extends DynamoServlet{

	private PromotionTools mPromotionTools;
	private PMDLCacheAdapter mPmdlCacheAdapter;
	
	public void service(DynamoHttpServletRequest pReq,
			DynamoHttpServletResponse pRes) throws ServletException,
			IOException {
			
		Object promotionID = pReq.getParameter("promotionId");
		String upsellAmount[]=null; 
		logDebug("Promotion ID:" + promotionID.toString() );
		try {
		 	RepositoryItem promotion = getPromotionTools().getPromotions().getItem(promotionID.toString(), getPromotionTools().getBasePromotionItemType());
		 	if(promotion != null){
		 	PricingModelElem pricingModel = (PricingModelElem) getPmdlCacheAdapter().getCacheElement(promotion);
		 	String promotionType = ((Integer) promotion.getPropertyValue("type")).toString();
		 	PricingModelExpression[] pmdlExpressionMap = pricingModel.getQualifier().getSubElements();
		 	logDebug("Promotion Type:" + promotionType);
		 	if(promotionType.equalsIgnoreCase(CricketCommonConstants.NINE)||promotionType.equalsIgnoreCase(CricketCommonConstants.TEN)||promotionType.equalsIgnoreCase(CricketCommonConstants.ELEVEN)||promotionType.equalsIgnoreCase(CricketCommonConstants.TWELVE)){
		 	for(PricingModelExpression map:pmdlExpressionMap){
				 	if(map instanceof GreaterThanElem || map instanceof GreaterThanOrEqualsElem){
				 		logDebug("PMDL Expression - Greater Then instance");
				 		PricingModelExpression[] pmdl = pmdlExpressionMap;
				 		if(map instanceof GreaterThanElem){
				 			 pmdl = ((GreaterThanElem) map).getSubElements();
				 		} else if(map instanceof GreaterThanOrEqualsElem){
				 			 pmdl = ((GreaterThanOrEqualsElem) map).getSubElements();
				 		}
				 		for(PricingModelExpression temp:pmdl){
				 			if(temp instanceof ConstantElem){
				 				upsellAmount = ((ConstantElem) temp).getStringValues();
				 				logDebug("Upsell Amount to spent :"+ upsellAmount[0]);
				 			}
				 		}
				 	} else if(map instanceof AndElem){
			 			logDebug("PMDL Expression - AND instance");
			 			PricingModelExpression[] pmdl= ((AndElem)map).getSubElements();
			 			PricingModelExpression[] pmdl2 = pmdlExpressionMap;
			 			for(PricingModelExpression temp:pmdl){
				 			if(temp instanceof GreaterThanElem || temp instanceof GreaterThanOrEqualsElem){
				 				if(temp instanceof GreaterThanElem){
				 					 pmdl2 = ((GreaterThanElem) map).getSubElements();
				 				}else if(temp instanceof GreaterThanOrEqualsElem){
				 					 pmdl2 = ((GreaterThanOrEqualsElem) map).getSubElements();
				 				}
				 				for(PricingModelExpression temp2:pmdl2){
						 			if(temp2 instanceof ConstantElem){
						 				upsellAmount = ((ConstantElem) temp2).getStringValues();
						 				logDebug("Upsell Amount to spent :" + upsellAmount[0]);
						 			}
						 		}
				 			}
				 		}
			 		}
		 		}
		 	}
		 	}
		}   catch (RepositoryException e) {
			 logError(e);
		} catch (Exception e) {
			 logError(e);
		}
		pReq.setParameter("upsellAmount", upsellAmount[0]);
		pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
	}
	public PromotionTools getPromotionTools() {
		return mPromotionTools;
	}
	public void setPromotionTools(PromotionTools mPromotionTools) {
		this.mPromotionTools = mPromotionTools;
	}
	public PMDLCacheAdapter getPmdlCacheAdapter() {
		return mPmdlCacheAdapter;
	}
	public void setPmdlCacheAdapter(PMDLCacheAdapter mPmdlCacheAdapter) {
		this.mPmdlCacheAdapter = mPmdlCacheAdapter;
	}
}
