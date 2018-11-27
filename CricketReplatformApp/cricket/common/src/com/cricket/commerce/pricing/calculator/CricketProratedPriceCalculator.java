package com.cricket.commerce.pricing.calculator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atg.commerce.catalog.CatalogTools;
import atg.commerce.order.CommerceItem;
import atg.commerce.pricing.ItemPriceInfo;
import atg.commerce.pricing.PricingException;
import atg.commerce.pricing.priceLists.ItemPriceCalculator;
import atg.core.util.StringUtils;
import atg.nucleus.naming.ComponentName;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.ServletUtil;

import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.commerce.order.session.UpgradeItemDetailsSessionBean;
import com.cricket.commerce.pricing.CricketItemPriceInfo;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.integration.esp.vo.SubscriberChargesVO;
import com.cricket.util.EspServiceResponseData;


public class CricketProratedPriceCalculator extends ItemPriceCalculator {
	
	private CartConfiguration cartConfiguration;
	private ComponentName mUserAccountInformationComponentName = null;
    private ComponentName mSessionComponentName = null;
	private String mUserAccountInformationPath;
	private String mSessionComponentPath;
	private CatalogTools catalogTools;
	private String upgradeItemDetailsSessionComponentPath;
    private ComponentName upgradeItemDetailsSessionComponentName= null;

	
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
		if (upgradeItemDetailsSessionComponentPath != null) {
			upgradeItemDetailsSessionComponentName = ComponentName.getComponentName(upgradeItemDetailsSessionComponentPath);
		} else {
			upgradeItemDetailsSessionComponentName = null;
		}
	}

	@SuppressWarnings("rawtypes")
	public void priceItem(ItemPriceInfo pPriceQuote, CommerceItem pItem,
			RepositoryItem pPricingModel, Locale pLocale,
			RepositoryItem pProfile, Map pExtraParameters)
			throws PricingException {		
		
		if (null != pItem && pItem instanceof CricketCommerceItemImpl 
		&& !StringUtils.isEmpty(((CricketCommerceItemImpl) pItem).getCricItemTypes())) {
			String cricItemTypes = ((CricketCommerceItemImpl) pItem).getCricItemTypes();
			//Boolean isChangeFlow = ((CricketCommerceItemImpl) pItem).isChangeFlow();
			Boolean isPlanDownGrade = ((CricketCommerceItemImpl) pItem).isDownGrade();
			CricketItemPriceInfo cktPriceInfo = (CricketItemPriceInfo) pPriceQuote;
			if (isLoggingDebug()) {
				logDebug("[CricketProratedPriceCalculator->priceItem()]: Inside CricketProratedPriceCalculator : cricItemTypes:  " + cricItemTypes + ", isPlanDownGrade: =" + isPlanDownGrade);
			}
			UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean = (UpgradeItemDetailsSessionBean) ServletUtil.getCurrentRequest().resolveName(upgradeItemDetailsSessionComponentName);
			if (getCartConfiguration().getChangePlanItemType().equalsIgnoreCase(cricItemTypes)
							|| getCartConfiguration().getChangeAddOnItemType().equalsIgnoreCase(cricItemTypes)
							|| (getCartConfiguration().getAddonProductItemType().equalsIgnoreCase(cricItemTypes) 
									&& null != upgradeItemDetailsSessionBean 
									&& getCartConfiguration().getUpgradePlanIntention().equalsIgnoreCase(upgradeItemDetailsSessionBean.getUserIntention()))) {
				
				//UserAccountInformation userAccountInformation = (UserAccountInformation) ServletUtil.getCurrentRequest().resolveName(mUserAccountInformationComponentName);
				EspServiceResponseData espServiceResponseData = (EspServiceResponseData) ServletUtil.getCurrentRequest().resolveName(mSessionComponentName);
				if (isLoggingDebug()) {
					logDebug("[CricketProratedPriceCalculator->priceItem()]: espServiceResponseData: "+ espServiceResponseData);
				}
				if (null != espServiceResponseData) {
				List<SubscriberChargesVO> updateSubscriberlist = espServiceResponseData.getRecurringSubscriberChargesList();
				BigDecimal planProratedPrice = new BigDecimal(0.0);
				String chargeItemName = "";
				String productId = pItem.getAuxiliaryData().getProductId();
				if (null != updateSubscriberlist && !updateSubscriberlist.isEmpty() && updateSubscriberlist.size()  > 0) {
					if (isLoggingDebug()) {
						logDebug("[CricketProratedPriceCalculator->priceItem()]: Inside updateSubscriberlist");
					}
					for (SubscriberChargesVO updateSubscriber : updateSubscriberlist) {
						if (getCartConfiguration().getChangePlanItemType().equalsIgnoreCase(cricItemTypes)) {
							if (updateSubscriber.getChargeItemTypeId() == 1) {
								planProratedPrice = planProratedPrice.add(updateSubscriber.getChargeAmount());
							} 
						} else {
								chargeItemName  = updateSubscriber.getChargeItemName();
								if (!StringUtils.isEmpty(chargeItemName) && chargeItemName.contains(productId)) {
									if (isLoggingDebug()) {
										logDebug("[CricketProratedPriceCalculator->priceItem()]: addon price: "+updateSubscriber.getChargeAmount().doubleValue());
									}
									cktPriceInfo.setProRatedPrice(updateSubscriber.getChargeAmount().doubleValue());
									cktPriceInfo.setAmount(updateSubscriber.getChargeAmount().doubleValue());
								}
							}
						}
						if (getCartConfiguration().getChangePlanItemType().equalsIgnoreCase(cricItemTypes)) {
							if (isLoggingDebug()) {
								logDebug("[CricketProratedPriceCalculator->priceItem()]: planProratedPrice: "+planProratedPrice);
							}
							cktPriceInfo.setProRatedPrice(planProratedPrice.doubleValue());
							cktPriceInfo.setAmount(planProratedPrice.doubleValue());
						}
					}
				}
			/*	CricketOrder order = (CricketOrder) cart.getCurrent();
				
				if (null != userAccountInformation && null != userAccountInformation.getBillingCycleDate()) {				 
					
					Calendar effectiveDate = new GregorianCalendar();
				    Calendar endDate = new GregorianCalendar();
				    
				    Calendar calendar = Calendar.getInstance();
				    
				    Date currentDate = calendar.getTime();				    
					Date billCycleDate = userAccountInformation.getBillingCycleDate().getTime();
					//billCycleDate.add(Calendar.DAY_OF_MONTH, -1);
					
					effectiveDate.set(currentDate.getYear(), currentDate.getMonth(), currentDate.getDate());
					endDate.set(billCycleDate.getYear(), billCycleDate.getMonth(), billCycleDate.getDate());					
									     
					int noOfDaysInBillingMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					int remainingDays = (int) ((endDate.getTimeInMillis() - effectiveDate.getTimeInMillis()) / (1000 * 60 * 60 * 24));
					
					if(isLoggingDebug()){
						logDebug("[CricketProratedPriceCalculator->priceItem()]: effectiveDate: "+ effectiveDate.getTime() + ", endDate: " + endDate.getTime()+", noOfDaysInBillingMonth: "+ noOfDaysInBillingMonth+", remainingDays: "+ remainingDays);
					}
					
					if (getCartConfiguration().getChangePlanItemType().equalsIgnoreCase(cricItemTypes)) {
						
						String removedPlanSkuID = order.getRemovedPlanSkuId();
						String removedPlanID = order.getRemovedPlanId();
						Double previousPlanPrice = 0.0;
						previousPlanPrice = getPreviousPlanPrice(removedPlanSkuID,
								removedPlanID);								
						Double remainingBalance = 0.0;								
						Double previousPlanAmountForRemainingDays = previousPlanPrice/noOfDaysInBillingMonth * remainingDays;									
						Double currentPlanPrice = (Double) ((RepositoryItem) pItem
								.getAuxiliaryData().getCatalogRef())
								.getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
						Double currentPlanAmountForRemainingDays = currentPlanPrice/noOfDaysInBillingMonth * remainingDays;
						remainingBalance = currentPlanAmountForRemainingDays - previousPlanAmountForRemainingDays;
						remainingBalance = getPricingTools().round(remainingBalance);
						cktPriceInfo.setAmount(remainingBalance);
						cktPriceInfo.setProRatedPrice(remainingBalance);
						
					} else if (getCartConfiguration().getChangeAddOnItemType().equalsIgnoreCase(cricItemTypes) 
							|| CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT.equalsIgnoreCase(cricItemTypes)) {
						Double currentAddonPrice = (Double) ((RepositoryItem) pItem
								.getAuxiliaryData().getCatalogRef())
								.getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
						Double remainingBalance = 0.0;								
						Double currentPlanAmountForRemainingDays = currentAddonPrice/noOfDaysInBillingMonth * remainingDays;
						remainingBalance = getPricingTools().round(currentPlanAmountForRemainingDays);
						cktPriceInfo.setAmount(remainingBalance);
						cktPriceInfo.setProRatedPrice(remainingBalance);								
					}					
				} */
			} else if (getCartConfiguration().getRemovedAddonItemType().equalsIgnoreCase(cricItemTypes)
					|| (getCartConfiguration().getChangePlanItemType().equalsIgnoreCase(cricItemTypes) && isPlanDownGrade)) {
				if (isLoggingDebug()) {
					logDebug("[CricketProratedPriceCalculator->priceItem()]: setting itemprice amount to zero ");
				}
				cktPriceInfo.setAmount(0.0);
				// adding 2 times the amount for logged in user in AAL flow because it must charge for 2 months
			}/*else if (!pProfile.isTransient() && (cricItemTypes.equalsIgnoreCase(CricketCommonConstants.PLAN_PRODUCT) || cricItemTypes.equalsIgnoreCase(getCartConfiguration().getAddonProductItemType()))){
				UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean = (UpgradeItemDetailsSessionBean) ServletUtil.getCurrentRequest()
						.resolveName(getUpgradeItemDetailsSessionComponentPath());
				String intention = upgradeItemDetailsSessionBean.getUserIntention();
				if(intention != null && intention.equalsIgnoreCase(getCartConfiguration().getAddLineIntention())){
					double amount= cktPriceInfo.getAmount() * 2;
					cktPriceInfo.setAmount(amount);
					//pItem.setPriceInfo(cktPriceInfo);
					//cktPriceInfo.setAmountIsFinal(true);
				}
			}*/
		}
		if (isLoggingDebug()) {
			logDebug("Exit with Updated ItemPriceInfo for " + pItem + "="
					+ pPriceQuote);
		}
	}

	@SuppressWarnings("unchecked")
	private Double getPreviousPlanPrice(String removedPlanSkuID,
			String removedPlanID) {
		Double previousPlanPrice = 0.0;
		RepositoryItem removePlanSku = null;
		try {
		if (!StringUtils.isEmpty(removedPlanSkuID)) {
			removePlanSku = getCatalogTools().findSKU(
					removedPlanSkuID);
		} else if (!StringUtils.isEmpty(removedPlanID)) {
			RepositoryItem planProduct = getCatalogTools().findProduct(removedPlanID);
			if (null != planProduct) {
				List<RepositoryItem> childSkus = (List<RepositoryItem>) planProduct.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
				if(childSkus != null && !childSkus.isEmpty() && childSkus.size()>0) {
					removePlanSku = childSkus.get(0);
				}
			}
		}
		if (null != removePlanSku) {
			previousPlanPrice = (Double) removePlanSku.getPropertyValue(CricketCommonConstants.PROP_LIST_PRICE);
		}
		
			
		} catch (RepositoryException e) {
				if (isLoggingError()) {
					logError("RepositoryException" + e);
				}
			}
		return previousPlanPrice;
	}

	/**
	 * @return the cartConfiguration
	 */
	public CartConfiguration getCartConfiguration() {
		return cartConfiguration;
	}

	/**
	 * @param cartConfiguration the cartConfiguration to set
	 */
	public void setCartConfiguration(CartConfiguration cartConfiguration) {
		this.cartConfiguration = cartConfiguration;
	}
	
	/**
	 * @return the sessionBeanPath
	 */
	public String getUserAccountInformationPath() {
		return mUserAccountInformationPath;
	}

	/**
	 * @param pSessionBeanPath the sessionBeanPath to set
	 */
	public void setUserAccountInformationPath(String pUserAccountInformationPath) {
		mUserAccountInformationPath = pUserAccountInformationPath;
		if (mUserAccountInformationPath != null) {
			mUserAccountInformationComponentName = ComponentName.getComponentName(mUserAccountInformationPath);
		} else {
			mUserAccountInformationComponentName = null;
		}
	}

	/**
	 * @return the mShoppingCartComponentName
	 */
	public ComponentName getSessionComponentName() {
		return mSessionComponentName;
	}

	/**
	 * @param mShoppingCartComponentName the mShoppingCartComponentName to set
	 */
	public void setSessionComponentName(
			ComponentName pSessionComponentName) {
		this.mSessionComponentName = pSessionComponentName;
	}

	/**
	 * @return the mShoppingCartPath
	 */
	public String getSessionComponentPath() {
		return mSessionComponentPath;
	}

	/**
	 * @param mShoppingCartPath the mShoppingCartPath to set
	 */
	public void setSessionComponentPath(String pSessionComponentPath) {
		this.mSessionComponentPath = pSessionComponentPath;
		if (mSessionComponentPath != null) {
			mSessionComponentName = ComponentName.getComponentName(mSessionComponentPath);
		} else {
			mSessionComponentName = null;
		}
	}

	/**
	 * @return the catalogTools
	 */
	public CatalogTools getCatalogTools() {
		return catalogTools;
	}

	/**
	 * @param catalogTools the catalogTools to set
	 */
	public void setCatalogTools(CatalogTools catalogTools) {
		this.catalogTools = catalogTools;
	}

	
 }
