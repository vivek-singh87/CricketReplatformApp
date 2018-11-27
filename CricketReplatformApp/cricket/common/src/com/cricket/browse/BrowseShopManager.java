package com.cricket.browse;

import java.util.List;

import atg.commerce.pricing.PricingModelHolder;
import atg.nucleus.GenericService;
import atg.repository.RepositoryItem;

import com.cricket.catalog.CatalogManager;

/**
 * @author RM112139
 * 
 */
public class BrowseShopManager extends GenericService {

	/**
	 * 
	 */
	private CatalogManager catalogManager;

	/**
	 * @return
	 */
	public CatalogManager getCatalogManager() {
		return catalogManager;
	}

	/**
	 * @param catalogManager
	 */
	public void setCatalogManager(CatalogManager catalogManager) {
		this.catalogManager = catalogManager;
	}

	/**
	 * @param categoryId
	 * @param planProductId
	 * @param marketCode
	 * @return
	 */
	public List<PhoneVO> getListOfPhoneProducts(String categoryId,
			String planProductId, String marketCode,PricingModelHolder userPricingHolder, String marketType, Boolean isUserLoggedIn, boolean ugradePhoneIntention, String editPhone) {
		return getCatalogManager().getListOfPhoneProducts(categoryId, planProductId, marketCode,userPricingHolder, marketType, isUserLoggedIn, ugradePhoneIntention, editPhone);
	}

	/**
	 * @param categoryId
	 * @param phoneProductId
	 * @param marketCode
	 * @return
	 */
	public List<PlanVO> getListOfPlanProducts(String categoryId,
			String phoneProductId, String marketCode, String marketType) {
		return getCatalogManager().getListOfPlanProducts(categoryId, phoneProductId, marketCode, marketType);
	}
	
	/**
	 * 
	 * @param planItems
	 * @return
	 */
	public List<PlanVO> getListOfCompatiblePlans(List<RepositoryItem> planItems){
		return getCatalogManager().getListOfCompatiblePlans(planItems);
	}

	/**
	 * @param categoryId
	 * @param phoneProductId
	 * @return
	 */
	public List<AccessoryVO> getListOfAccessoryProducts(String categoryId, String phoneProductId) {
		return getCatalogManager().getListOfAccessoryProducts(categoryId, phoneProductId);
	}
	
	/**
	 * @param categoryId
	 * @param planProductId
	 * @return
	 */
	public List<AddonVO> getListOfAddonProducts(String categoryId, String planProductId) {
		return getCatalogManager().getListOfAddonProducts(categoryId, planProductId);
	}
	
	/**
	 * @param productId
	 * @param marketCode
	 * @return
	 */
	public PhoneVO getPhoneDetails(String productId, String marketCode){
		PhoneVO phoneVO = null;
		phoneVO = getCatalogManager().getPhoneDetails(productId, marketCode);
		return phoneVO;
	}

	public AccessoryVO getAccessoryDetails (String productId){
		AccessoryVO accessoriesVO = null;
		accessoriesVO = getCatalogManager().getAccessoryDetails(productId);
		return accessoriesVO;
	}

	

}
