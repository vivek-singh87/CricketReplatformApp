package com.cricket.commerce.order.configuration;

import atg.nucleus.GenericService;

public class CartConfiguration extends GenericService {
	
	private String mIntentionalURLParameterName;
	
	private String mPhoneIdCookieName;
	
	private String mPhoneSkuIdCookieName;
	
	private String mPlanIdCookieName;
	
	private String mPlanSkuIdCookieName;
	
	private String mAddonProductIdOneCookieName;
	
	private String mAddonProductIdTwoCookieName;
	
	private String mAddonProductIdThreeCookieName;
	
	private String mAddonSkuIdOneCookieName;
	
	private String mAddonSkuIdTwoCookieName;
	
	private String mAddonSkuIdThreeCookieName;
	
	private String mUpgradePhoneIntention;
	
	private String mUpgradePlanIntention;
	
	private String mUpgradeAddonIntention;
	
	private String mAddLineIntention;
	
	private String mUpgradePhoneItemType;
	
	private String mChangePlanItemType;
	
	private String mChangeAddOnItemType;
	
	private String mRrcWorkOrderType;
	
	private String mOupWorkOrderType;
	
	private String mAddonIdCookieName;
	
	private String mAddonSkuIdCookieName;
	
	private String mAddonProductItemType;
	/*holds the work order type for new activation */
	private String actWorkOrderType;
	/* holds the work order type for add a line */
	private String addWorkOrderType;
	/*holds the work order type for accessories */
	private String mAccessoriesWorkOrderType;
	
	private int mOrderPersistentCookieMaxAge;
	
	private String mRemovedAddonItemType;
	
	private String mOutOfFootPrintMarketType;
	
	private String mHppParcGroupName;

	/**
	 * @return the mHppParcGroupName
	 */
	public String getHppParcGroupName() {
		return mHppParcGroupName;
	}

	/**
	 * @param mHppParcGroupName the mHppParcGroupName to set
	 */
	public void setHppParcGroupName(String mHppParcGroupName) {
		this.mHppParcGroupName = mHppParcGroupName;
	}

	/**
	 * @return the addWorkOrderType
	 */
	public String getAddWorkOrderType() {
		return addWorkOrderType;
	}

	/**
	 * @param addWorkOrderType the addWorkOrderType to set
	 */
	public void setAddWorkOrderType(String addWorkOrderType) {
		this.addWorkOrderType = addWorkOrderType;
	}

	/**
	 * @return the mActWorkOrderType
	 */
	public String getActWorkOrderType() {
		return actWorkOrderType;
	}

	/**
	 * @param mActWorkOrderType the mActWorkOrderType to set
	 */
	public void setActWorkOrderType(String mActWorkOrderType) {
		this.actWorkOrderType = mActWorkOrderType;
	}

	/**
	 * @return the intentionalURLParameterName
	 */
	public String getIntentionalURLParameterName() {
		return mIntentionalURLParameterName;
	}

	/**
	 * @param pIntentionalURLParameterName the intentionalURLParameterName to set
	 */
	public void setIntentionalURLParameterName(String pIntentionalURLParameterName) {
		mIntentionalURLParameterName = pIntentionalURLParameterName;
	}

	/**
	 * @return the phoneIdCookieName
	 */
	public String getPhoneIdCookieName() {
		return mPhoneIdCookieName;
	}

	/**
	 * @param pPhoneIdCookieName the phoneIdCookieName to set
	 */
	public void setPhoneIdCookieName(String pPhoneIdCookieName) {
		mPhoneIdCookieName = pPhoneIdCookieName;
	}

	/**
	 * @return the phoneSkuIdCookieName
	 */
	public String getPhoneSkuIdCookieName() {
		return mPhoneSkuIdCookieName;
	}

	/**
	 * @param pPhoneSkuIdCookieName the phoneSkuIdCookieName to set
	 */
	public void setPhoneSkuIdCookieName(String pPhoneSkuIdCookieName) {
		mPhoneSkuIdCookieName = pPhoneSkuIdCookieName;
	}

	/**
	 * @return the planIdCookieName
	 */
	public String getPlanIdCookieName() {
		return mPlanIdCookieName;
	}

	/**
	 * @param pPlanIdCookieName the planIdCookieName to set
	 */
	public void setPlanIdCookieName(String pPlanIdCookieName) {
		mPlanIdCookieName = pPlanIdCookieName;
	}

	/**
	 * @return the planSkuIdCookieName
	 */
	public String getPlanSkuIdCookieName() {
		return mPlanSkuIdCookieName;
	}

	/**
	 * @param pPlanSkuIdCookieName the planSkuIdCookieName to set
	 */
	public void setPlanSkuIdCookieName(String pPlanSkuIdCookieName) {
		mPlanSkuIdCookieName = pPlanSkuIdCookieName;
	}

	/**
	 * @return the addonProductIdOneCookieName
	 */
	public String getAddonProductIdOneCookieName() {
		return mAddonProductIdOneCookieName;
	}

	/**
	 * @param pAddonProductIdOneCookieName the addonProductIdOneCookieName to set
	 */
	public void setAddonProductIdOneCookieName(String pAddonProductIdOneCookieName) {
		mAddonProductIdOneCookieName = pAddonProductIdOneCookieName;
	}

	/**
	 * @return the addonProductIdTwoCookieName
	 */
	public String getAddonProductIdTwoCookieName() {
		return mAddonProductIdTwoCookieName;
	}

	/**
	 * @param pAddonProductIdTwoCookieName the addonProductIdTwoCookieName to set
	 */
	public void setAddonProductIdTwoCookieName(String pAddonProductIdTwoCookieName) {
		mAddonProductIdTwoCookieName = pAddonProductIdTwoCookieName;
	}

	/**
	 * @return the addonProductIdThreeCookieName
	 */
	public String getAddonProductIdThreeCookieName() {
		return mAddonProductIdThreeCookieName;
	}

	/**
	 * @param pAddonProductIdThreeCookieName the addonProductIdThreeCookieName to set
	 */
	public void setAddonProductIdThreeCookieName(
			String pAddonProductIdThreeCookieName) {
		mAddonProductIdThreeCookieName = pAddonProductIdThreeCookieName;
	}

	/**
	 * @return the addonSkuIdOneCookieName
	 */
	public String getAddonSkuIdOneCookieName() {
		return mAddonSkuIdOneCookieName;
	}

	/**
	 * @param pAddonSkuIdOneCookieName the addonSkuIdOneCookieName to set
	 */
	public void setAddonSkuIdOneCookieName(String pAddonSkuIdOneCookieName) {
		mAddonSkuIdOneCookieName = pAddonSkuIdOneCookieName;
	}

	/**
	 * @return the addonSkuIdTwoCookieName
	 */
	public String getAddonSkuIdTwoCookieName() {
		return mAddonSkuIdTwoCookieName;
	}

	/**
	 * @param pAddonSkuIdTwoCookieName the addonSkuIdTwoCookieName to set
	 */
	public void setAddonSkuIdTwoCookieName(String pAddonSkuIdTwoCookieName) {
		mAddonSkuIdTwoCookieName = pAddonSkuIdTwoCookieName;
	}

	/**
	 * @return the addonSkuIdThreeCookieName
	 */
	public String getAddonSkuIdThreeCookieName() {
		return mAddonSkuIdThreeCookieName;
	}

	/**
	 * @param pAddonSkuIdThreeCookieName the addonSkuIdThreeCookieName to set
	 */
	public void setAddonSkuIdThreeCookieName(String pAddonSkuIdThreeCookieName) {
		mAddonSkuIdThreeCookieName = pAddonSkuIdThreeCookieName;
	}

	/**
	 * @return the upgradePhoneIntention
	 */
	public String getUpgradePhoneIntention() {
		return mUpgradePhoneIntention;
	}

	/**
	 * @param pUpgradePhoneIntention the upgradePhoneIntention to set
	 */
	public void setUpgradePhoneIntention(String pUpgradePhoneIntention) {
		mUpgradePhoneIntention = pUpgradePhoneIntention;
	}

	/**
	 * @return the upgradePlanIntention
	 */
	public String getUpgradePlanIntention() {
		return mUpgradePlanIntention;
	}

	/**
	 * @param pUpgradePlanIntention the upgradePlanIntention to set
	 */
	public void setUpgradePlanIntention(String pUpgradePlanIntention) {
		mUpgradePlanIntention = pUpgradePlanIntention;
	}

	/**
	 * @return the upgradeAddonIntention
	 */
	public String getUpgradeAddonIntention() {
		return mUpgradeAddonIntention;
	}

	/**
	 * @param pUpgradeAddonIntention the upgradeAddonIntention to set
	 */
	public void setUpgradeAddonIntention(String pUpgradeAddonIntention) {
		mUpgradeAddonIntention = pUpgradeAddonIntention;
	}

	/**
	 * @return the addLineIntention
	 */
	public String getAddLineIntention() {
		return mAddLineIntention;
	}

	/**
	 * @param pAddLineIntention the addLineIntention to set
	 */
	public void setAddLineIntention(String pAddLineIntention) {
		mAddLineIntention = pAddLineIntention;
	}

	/**
	 * @return the upgradePhoneItemType
	 */
	public String getUpgradePhoneItemType() {
		return mUpgradePhoneItemType;
	}

	/**
	 * @param pUpgradePhoneItemType the upgradePhoneItemType to set
	 */
	public void setUpgradePhoneItemType(String pUpgradePhoneItemType) {
		mUpgradePhoneItemType = pUpgradePhoneItemType;
	}

	/**
	 * @return the changePlanItemType
	 */
	public String getChangePlanItemType() {
		return mChangePlanItemType;
	}

	/**
	 * @param pChangePlanItemType the changePlanItemType to set
	 */
	public void setChangePlanItemType(String pChangePlanItemType) {
		mChangePlanItemType = pChangePlanItemType;
	}

	/**
	 * @return the changeAddOnItemType
	 */
	public String getChangeAddOnItemType() {
		return mChangeAddOnItemType;
	}

	/**
	 * @param pChangeAddOnItemType the changeAddOnItemType to set
	 */
	public void setChangeAddOnItemType(String pChangeAddOnItemType) {
		mChangeAddOnItemType = pChangeAddOnItemType;
	}

	/**
	 * @return the rrcWorkOrderType
	 */
	public String getRrcWorkOrderType() {
		return mRrcWorkOrderType;
	}

	/**
	 * @param pRrcWorkOrderType the rrcWorkOrderType to set
	 */
	public void setRrcWorkOrderType(String pRrcWorkOrderType) {
		mRrcWorkOrderType = pRrcWorkOrderType;
	}

	/**
	 * @return the oupWorkOrderType
	 */
	public String getOupWorkOrderType() {
		return mOupWorkOrderType;
	}

	/**
	 * @param pOupWorkOrderType the oupWorkOrderType to set
	 */
	public void setOupWorkOrderType(String pOupWorkOrderType) {
		mOupWorkOrderType = pOupWorkOrderType;
	}

	/**
	 * @return the addonIdCookieName
	 */
	public String getAddonIdCookieName() {
		return mAddonIdCookieName;
	}

	/**
	 * @param pAddonIdCookieName the addonIdCookieName to set
	 */
	public void setAddonIdCookieName(String pAddonIdCookieName) {
		mAddonIdCookieName = pAddonIdCookieName;
	}

	/**
	 * @return the addonSkuIdCookieName
	 */
	public String getAddonSkuIdCookieName() {
		return mAddonSkuIdCookieName;
	}

	/**
	 * @param pAddonSkuIdCookieName the addonSkuIdCookieName to set
	 */
	public void setAddonSkuIdCookieName(String pAddonSkuIdCookieName) {
		mAddonSkuIdCookieName = pAddonSkuIdCookieName;
	}

	/**
	 * @return the addonProductItemType
	 */
	public String getAddonProductItemType() {
		return mAddonProductItemType;
	}

	/**
	 * @param pAddonProductItemType the addonProductItemType to set
	 */
	public void setAddonProductItemType(String pAddonProductItemType) {
		mAddonProductItemType = pAddonProductItemType;
	}

	/**
	 * @return the mAccessoriesWorkOrderType
	 */
	public String getAccessoriesWorkOrderType() {
		return mAccessoriesWorkOrderType;
	}

	/**
	 * @param mAccessoriesWorkOrderType the mAccessoriesWorkOrderType to set
	 */
	public void setAccessoriesWorkOrderType(String pAccessoriesWorkOrderType) {
		this.mAccessoriesWorkOrderType = pAccessoriesWorkOrderType;
	}

	/**
	 * @return the mOrderPersistentCookieMaxAge
	 */
	public int getOrderPersistentCookieMaxAge() {
		return mOrderPersistentCookieMaxAge;
	}

	/**
	 * @param mOrderPersistentCookieMaxAge the mOrderPersistentCookieMaxAge to set
	 */
	public void setOrderPersistentCookieMaxAge(int pOrderPersistentCookieMaxAge) {
		this.mOrderPersistentCookieMaxAge = pOrderPersistentCookieMaxAge;
	}

	/**
	 * @return the mRemovedAddonItemType
	 */
	public String getRemovedAddonItemType() {
		return mRemovedAddonItemType;
	}

	/**
	 * @param mRemovedAddonItemType the mRemovedAddonItemType to set
	 */
	public void setRemovedAddonItemType(String mRemovedAddonItemType) {
		this.mRemovedAddonItemType = mRemovedAddonItemType;
	}

	/**
	 * @return the mOoutOfFootPrintMarketType
	 */
	public String getOutOfFootPrintMarketType() {
		return mOutOfFootPrintMarketType;
	}

	/**
	 * @param mOoutOfFootPrintMarketType the mOoutOfFootPrintMarketType to set
	 */
	public void setOutOfFootPrintMarketType(String mOutOfFootPrintMarketType) {
		this.mOutOfFootPrintMarketType = mOutOfFootPrintMarketType;
	}

	
}
