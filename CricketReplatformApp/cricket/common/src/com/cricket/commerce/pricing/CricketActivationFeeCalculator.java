package com.cricket.commerce.pricing;

import java.util.Locale;
import java.util.Map;

import atg.commerce.order.CommerceItem;
import atg.commerce.pricing.ItemPriceInfo;
import atg.commerce.pricing.PricingCommerceItem;
import atg.commerce.pricing.PricingException;
import atg.commerce.pricing.priceLists.ItemPriceCalculator;
import atg.core.util.StringUtils;
import atg.repository.RepositoryItem;

import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.common.constants.CricketCommonConstants;

/**
 * This class is to add activation fee to phones
 * for packages with phone and plan
 * 
 * @author TechM
 * 
 */
public class CricketActivationFeeCalculator extends ItemPriceCalculator {
	
	//RepositoryItem catalogRepository;

	private double activationFee;

	/**
	 * @return the catalogRepository
	 */
	/*public RepositoryItem getCatalogRepository() {
		return catalogRepository;
	}*/

	/**
	 * @param catalogRepository the catalogRepository to set
	 */
	/*public void setCatalogRepository(RepositoryItem catalogRepository) {
		this.catalogRepository = catalogRepository;
	}*/

	/**
	 * @return the activationFee
	 */
	public double getActivationFee() {
		return activationFee;
	}

	/**
	 * @param activationFee the activationFee to set
	 */
	public void setActivationFee(double activationFee) {
		this.activationFee = activationFee;
	}

	/* 
	 * This method is to add activation fee to each phone in the package
	 * if the package contains both plan and phone.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void priceItem(ItemPriceInfo pPriceQuote, CommerceItem pItem,
			RepositoryItem pPricingModel, Locale pLocale,
			RepositoryItem pProfile, Map pExtraParameters)
			throws PricingException {
		if (isLoggingDebug())
			logDebug("ItemPriceInfo for " + pItem + "=" + pPriceQuote);

		// Calling super method
		/*super.priceItem(pPriceQuote, pItem, pPricingModel, pLocale, pProfile,
				pExtraParameters);
*/
		// updating the activation fee for plans.
		double totalPrice = 0.0;
		if(!(pItem instanceof PricingCommerceItem)){
			if (null != pItem && null != pItem.getAuxiliaryData() 
					&& !StringUtils.isEmpty(pItem.getAuxiliaryData().getProductId())) {
				
				CricketCommerceItemImpl cktCommItem = (CricketCommerceItemImpl)pItem;
				CricketItemPriceInfo cktPriceInfo = (CricketItemPriceInfo) pPriceQuote;			
				if(CricketCommonConstants.PHONE_PRODUCT.equalsIgnoreCase(cktCommItem.getCricItemTypes())){
					double activationFee = 0.0;
					if ((Double)pProfile.getPropertyValue(CricketCommonConstants.ACTIVATION_FEE) != null) {
						activationFee = (Double)pProfile.getPropertyValue(CricketCommonConstants.ACTIVATION_FEE);
					}
					cktPriceInfo.setActivationFee(activationFee);
					totalPrice = cktPriceInfo.getAmount() + cktPriceInfo.getActivationFee();
					totalPrice = getPricingTools().round(totalPrice);
					cktPriceInfo.setAmount(totalPrice);
				}
			}
		}
		if (isLoggingDebug())
			logDebug("Exit with Updated ItemPriceInfo for " + pItem + "="
					+ pPriceQuote);
	}	
}
