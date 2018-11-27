package com.cricket.commerce.pricing;

import java.util.Locale;
import java.util.Map;

import atg.commerce.order.CommerceItem;
import atg.commerce.pricing.ItemPriceInfo;
import atg.commerce.pricing.PricingException;
import atg.commerce.pricing.priceLists.ItemPriceCalculator;
import atg.core.util.StringUtils;
import atg.repository.MutableRepository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.ServletUtil;

import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.session.UpgradeItemDetailsSessionBean;
import com.cricket.common.constants.CricketCommonConstants;

/**
 * This class is to add administration fee to plans
 * 
 * @author TechM
 * 
 */
public class CricketAdminFeeCalculator extends ItemPriceCalculator {

	MutableRepository catalogRepository;

	private double administrationFee;
	private String upgradeItemDetailsSessionComponentPath;

	
	/**
	 * @return the upgradeItemDetailsSessionComponentPath
	 */
	public String getUpgradeItemDetailsSessionComponentPath() {
		return upgradeItemDetailsSessionComponentPath;
	}

	/**
	 * @param mUpgradeItemDetailsSessionComponentPath the upgradeItemDetailsSessionComponentPath to set
	 */
	public void setUpgradeItemDetailsSessionComponentPath(
			String mUpgradeItemDetailsSessionComponentPath) {
		upgradeItemDetailsSessionComponentPath = mUpgradeItemDetailsSessionComponentPath;
	}
	/**
	 * @return the administrationFee
	 */
	public double getAdministrationFee() {
		return administrationFee;
	}

	/**
	 * @param administrationFee the administrationFee to set
	 */
	public void setAdministrationFee(double administrationFee) {
		this.administrationFee = administrationFee;
	}

	/**
	 * @return the catalogRepository
	 */
	public MutableRepository getCatalogRepository() {
		return catalogRepository;
	}

	/**
	 * @param catalogRepository
	 *            the catalogRepository to set
	 */
	public void setCatalogRepository(MutableRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}

	/*
	 * This method is to add administration fee to each plan in the cart
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public void priceItem(ItemPriceInfo pPriceQuote, CommerceItem pItem,
			RepositoryItem pPricingModel, Locale pLocale,
			RepositoryItem pProfile, Map pExtraParameters)
			throws PricingException {

		if (isLoggingDebug())
			logDebug("ItemPriceInfo for " + pItem + "=" + pPriceQuote);

		// Calling super method
		/*super.priceItem(pPriceQuote, pItem, pPricingModel, pLocale, pProfile,
				pExtraParameters);*/

		// updating the admin fee for plans.
		double totalPrice = 0.0;
		boolean isPlanDownGrade = false;
		String itemType = null;
		boolean changePlan= false;
		try {
			if (null != pItem && null != pItem.getAuxiliaryData() 
					&& !StringUtils.isEmpty(pItem.getAuxiliaryData().getProductId())) {
				RepositoryItem prdtItem = getCatalogRepository().getItem(
						pItem.getAuxiliaryData().getProductId(), CricketCommonConstants.PRODUCT);
				CricketItemPriceInfo cktPriceInfo = (CricketItemPriceInfo) pPriceQuote;
				
				if (prdtItem != null) {
					String prdtItemType = (String) prdtItem.getPropertyValue(CricketCommonConstants.TYPE);
					if (pItem instanceof CricketCommerceItemImpl) {
						isPlanDownGrade = ((CricketCommerceItemImpl) pItem).isDownGrade();
						itemType = (String) ((CricketCommerceItemImpl) pItem).getPropertyValue(CricketCommonConstants.CRICKET_ITEMTYPES);
					}
					if(null != itemType && !StringUtils.isEmpty(itemType) && itemType.equalsIgnoreCase(CricketCommonConstants.CITYPE_CHANGEPLAN)){
						changePlan = true;
					}
					if(!changePlan){
						//fix for defect 8666(for OOF market Admin fee must not be showing up)
						String marketType = 
								(String) pProfile.getPropertyValue(CricketCommonConstants.MARKET_TYPE);
						if (null != prdtItemType && prdtItemType.equalsIgnoreCase(CricketCommonConstants.PLAN_PRODUCT)
								&& !isPlanDownGrade && !marketType.equalsIgnoreCase(CricketCommonConstants.OOF_CUSTOMER)) {
							cktPriceInfo.setAdminFee(getAdministrationFee());
							totalPrice = cktPriceInfo.getAmount()
									+ cktPriceInfo.getAdminFee();
							totalPrice = getPricingTools().round(totalPrice);
							cktPriceInfo.setAmount(totalPrice);
						}
					}
					// adding one more time Admin fee for logged in user in AAL flow because it must charge for 2 months
					if (!pProfile.isTransient() && null != prdtItemType && !StringUtils.isEmpty(prdtItemType) && (prdtItemType.equalsIgnoreCase(CricketCommonConstants.PLAN_PRODUCT) || prdtItemType.equalsIgnoreCase(CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT))){ 
						UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean = (UpgradeItemDetailsSessionBean) ServletUtil.getCurrentRequest()
								.resolveName(getUpgradeItemDetailsSessionComponentPath());
						String intention = upgradeItemDetailsSessionBean.getUserIntention();
						if(intention != null && intention.equalsIgnoreCase(CricketCommonConstants.ADD_LINE)){
							double amount= cktPriceInfo.getAmount() + cktPriceInfo.getAdminFee()+ cktPriceInfo.getListPrice();
							cktPriceInfo.setAmount(amount);
						}
	
					}
				}
			}
		} catch (RepositoryException e) {
			if (isLoggingError()) {
				logError("RepositoryException Exception in priceItem of CricketAdminFeeCalculator",e);
			}
		}
		if (isLoggingDebug())
			logDebug("Exit with Updated ItemPriceInfo for " + pItem + "="
					+ pPriceQuote);
	}
}
