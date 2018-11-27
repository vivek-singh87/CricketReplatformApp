package com.cricket.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import atg.nucleus.GenericService;

public class CricketConfiguration extends GenericService {
	
	private String mGeoIPCityFile;
	
	private String zipcodesCSVPath;

	private String allPlansCategoryId;
	
	private String smartPhonesCategoryId;
	
	private String basicPhonesCategoryId;
	
	private String featurePhonesCategoryId;
	
	private String allPhonesCategoryId;
	
	private String phonesCategoryId;
	
	private String plansCategoryId;
	
	private String accessoriesCategoryId;
	
	private String otherAddonsCategoryId;
	
	private String payGoCategoryId;
	
	private String addonsCategoryId;
	
	private List<String> addonRelatedCategories;
	
	private String mMobileSuffix;
	
	private String seoKeyword;
	
	private String bannerProfileProperty;
	
	private List<String> phoneSpecsToCompare;
	
	private Map<String, String> homePageLinks = new HashMap<String,String>();
	
	private Map<String, String> shopPagesLinks = new HashMap<String,String>();
	
	private Map<String, String> socialMediaLinks = new HashMap<String,String>();
	
	private String vestaTokenJspURL;

	//Coremetrics 
	private String mCoremetricsClientID;
	
	private boolean mCoremetricsDataCollectionMethod;
	
	private String mCoremetricsDataCollectionDomain;
	
	private String mCoremetricsCookieDomain;
	
	private String supportLink;	
	
	private String mPimFeedFilePath;	
	
	private String mParcFeedFilePath;
	
	// ESP
	private String espServiceUrl;
	
	private String ftpHostName;

	private String ftpPort;

	private String ftpUserName;

	private String ftpUserPassword;

	private String ftpFileLocation;
	
	private int maxNoOfPackageForPIA;
	
	private int maxNoOfPackageForPayGo;

	private String OOFPlanType;

	private String defaultSortParam;
	
	private String inventoryFeedFilePath;	
 
	private String mEspCallLogFileLocation;	
	
	private String hostName;
	
	private String secureHostName;
	
	private String rootcontextpath;
	
	private String mailRebatePromotionLink; 
	
	private String nonSecureHost;
	
	private String secureHost;
	
	private int securePort;
	
	private int nonSecurePort;
	
	private String secureProtocol;
	
	private String nonSecureProtocol;
	
	//ESP order status msg for various codes
	private String orderStatusMsg_D;
	private String orderStatusMsg_X;
	private String orderStatusMsg_R;
	private String orderStatusMsg_C;
	private String orderStatusMsg_E;
	private String orderStatusMsg_P;
	
	private Boolean timeMonitoring;
	
	private List<String> hppAddon;
	
	private boolean espXMLLoggingEnabled;
	
	private String sendMessageTemplateIdentifier;
	
	private String sendMessagedateFormat;
	
	
	/**
	 * @return the timeMonitoring
	 */
	public Boolean getTimeMonitoring() {
		return timeMonitoring;
	}

	/**
	 * @param timeMonitoring the timeMonitoring to set
	 */
	public void setTimeMonitoring(Boolean timeMonitoring) {
		this.timeMonitoring = timeMonitoring;
	}

	public String getNonSecureHost() {
		return nonSecureHost;
	}

	public void setNonSecureHost(String nonSecureHost) {
		this.nonSecureHost = nonSecureHost;
	}

	public String getSecureHost() {
		return secureHost;
	}

	public void setSecureHost(String secureHost) {
		this.secureHost = secureHost;
	}

	public int getSecurePort() {
		return securePort;
	}

	public void setSecurePort(int securePort) {
		this.securePort = securePort;
	}

	public int getNonSecurePort() {
		return nonSecurePort;
	}

	public void setNonSecurePort(int nonSecurePort) {
		this.nonSecurePort = nonSecurePort;
	}

	public String getSecureProtocol() {
		return secureProtocol;
	}

	public void setSecureProtocol(String secureProtocol) {
		this.secureProtocol = secureProtocol;
	}

	public String getNonSecureProtocol() {
		return nonSecureProtocol;
	}

	public void setNonSecureProtocol(String nonSecureProtocol) {
		this.nonSecureProtocol = nonSecureProtocol;
	}
	

	public String getMailRebatePromotionLink() {
		return mailRebatePromotionLink;
	}

	public void setMailRebatePromotionLink(String pMailRebatePromotionLink) {
		mailRebatePromotionLink = pMailRebatePromotionLink;
	}

	public String getRootcontextpath() {
		return rootcontextpath;
	}

	public void setRootcontextpath(String pRootcontextpath) {
		rootcontextpath = pRootcontextpath;
	}

	private Map<String, String> whyCricketLinks = new HashMap<String,String>();

	public Map<String, String> getWhyCricketLinks() {
		return whyCricketLinks;
	}

	public void setWhyCricketLinks(Map<String, String> whyCricketLinks) {
		this.whyCricketLinks = whyCricketLinks;
	}
	
 
	public String getDefaultSortParam() {
		return defaultSortParam;
	}

	public void setDefaultSortParam(String defaultSortParam) {
		this.defaultSortParam = defaultSortParam;
	}	 


	/**
	 * 
	 * @return the vestaTokenJspURL
	 */
	public String getVestaTokenJspURL() {
		return vestaTokenJspURL;
	}

	/**
	 * 
	 * @param vestaTokenJspURL
	 */
	public void setVestaTokenJspURL(String vestaTokenJspURL) {
		this.vestaTokenJspURL = vestaTokenJspURL;
	}
	
	/**
	 * 
	 * @return the OOFPlanType
	 */
	public String getOOFPlanType() {
		return OOFPlanType;
	}

	/**
	 * 
	 * @param OOFPlanType
	 */
	public void setOOFPlanType(String OOFPlanType) {
		this.OOFPlanType = OOFPlanType;
	}
	
	/**
	 * @return the espServiceUrl
	 */
	public String getEspServiceUrl() {
		return espServiceUrl;
	}

	/**
	 * @param espServiceUrl the espServiceUrl to set
	 */
	public void setEspServiceUrl(String espServiceUrl) {
		this.espServiceUrl = espServiceUrl;
	}

	/**
	 * 
	 * @return the phoneSpecsToCompare
	 */
	public List<String> getPhoneSpecsToCompare() {
		return phoneSpecsToCompare;
	}

	/**
	 * 
	 * @param phoneSpecsToCompare
	 */
	public void setPhoneSpecsToCompare(List<String> phoneSpecsToCompare) {
		this.phoneSpecsToCompare = phoneSpecsToCompare;
	}

	/**
	 * @return the coremetricsClientID
	 */
	public String getCoremetricsClientID() {
		return mCoremetricsClientID;
	}

	/**
	 * @param pCoremetricsClientID the coremetricsClientID to set
	 */
	public void setCoremetricsClientID(String pCoremetricsClientID) {
		mCoremetricsClientID = pCoremetricsClientID;
	}

	/**
	 * @return the coremetricsDataCollectionMethod
	 */
	public boolean isCoremetricsDataCollectionMethod() {
		return mCoremetricsDataCollectionMethod;
	}

	/**
	 * @param pCoremetricsDataCollectionMethod the coremetricsDataCollectionMethod to set
	 */
	public void setCoremetricsDataCollectionMethod(
			boolean pCoremetricsDataCollectionMethod) {
		mCoremetricsDataCollectionMethod = pCoremetricsDataCollectionMethod;
	}

	/**
	 * @return the coremetricsDataCollectionDomain
	 */
	public String getCoremetricsDataCollectionDomain() {
		return mCoremetricsDataCollectionDomain;
	}

	/**
	 * @param pCoremetricsDataCollectionDomain the coremetricsDataCollectionDomain to set
	 */
	public void setCoremetricsDataCollectionDomain(
			String pCoremetricsDataCollectionDomain) {
		mCoremetricsDataCollectionDomain = pCoremetricsDataCollectionDomain;
	}

	/**
	 * @return the coremetricsCookieDomain
	 */
	public String getCoremetricsCookieDomain() {
		return mCoremetricsCookieDomain;
	}

	/**
	 * 
	 * @return the seoKeyword
	 */
	public String getSeoKeyword() {
		return seoKeyword;
	}

	/**
	 * 
	 * @param pSeoKeyword
	 */
	public void setSeoKeyword(String pSeoKeyword) {
		seoKeyword = pSeoKeyword;
	}
	
	/**
	 * @param pCoremetricsCookieDomain the coremetricsCookieDomain to set
	 */
	public void setCoremetricsCookieDomain(String pCoremetricsCookieDomain) {
		mCoremetricsCookieDomain = pCoremetricsCookieDomain;
	}

	/**
	 * @return the zipcodesCSVPath
	 */
	public String getZipcodesCSVPath() {
		return zipcodesCSVPath;
	}

	/**
	 * @param pZipcodesCSVPath the zipcodesCSVPath to set
	 */
	public void setZipcodesCSVPath(String pZipcodesCSVPath) {
		zipcodesCSVPath = pZipcodesCSVPath;
	}
	
	public List<String> getAddonRelatedCategories() {
		return addonRelatedCategories;
	}

	public void setAddonRelatedCategories(List<String> pAddonRelatedCategories) {
		addonRelatedCategories = pAddonRelatedCategories;
	}
	
	public String getMobileSuffix() {
		return mMobileSuffix;
	}

	public void setMobileSuffix(String pMobileSuffix) {
		mMobileSuffix = pMobileSuffix;
	}

	public String getFeaturePhonesCategoryId() {
		return featurePhonesCategoryId;
	}

	public void setFeaturePhonesCategoryId(String pFeaturePhonesCategoryId) {
		featurePhonesCategoryId = pFeaturePhonesCategoryId;
	}

	public String getAddonsCategoryId() {
		return addonsCategoryId;
	}

	public void setAddonsCategoryId(String pAddonsCategoryId) {
		addonsCategoryId = pAddonsCategoryId;
	}

	public String getOtherAddonsCategoryId() {
		return otherAddonsCategoryId;
	}

	public void setOtherAddonsCategoryId(String pOtherAddonsCategoryId) {
		otherAddonsCategoryId = pOtherAddonsCategoryId;
	}

	/**
	 * @return the accessoriesCategoryId
	 */
	public String getAccessoriesCategoryId() {
		return accessoriesCategoryId;
	}

	/**
	 * @param pAccessoriesCategoryId the accessoriesCategoryId to set
	 */
	public void setAccessoriesCategoryId(String pAccessoriesCategoryId) {
		accessoriesCategoryId = pAccessoriesCategoryId;
	}
	
	public String getAllPlansCategoryId() {
		return allPlansCategoryId;
	}

	public void setAllPlansCategoryId(String allPlansCategoryId) {
		this.allPlansCategoryId = allPlansCategoryId;
	}

	public String getAllPhonesCategoryId() {
		return allPhonesCategoryId;
	}

	public void setAllPhonesCategoryId(String allPhonesCategoryId) {
		this.allPhonesCategoryId = allPhonesCategoryId;
	}

	public Map<String, String> getHomePageLinks() {
		return homePageLinks;
	}

	public void setHomePageLinks(Map<String, String> homePageLinks) {
		this.homePageLinks = homePageLinks;
	}	
	

	public Map<String, String> getShopPagesLinks() {
		return shopPagesLinks;
	}

	public void setShopPagesLinks(Map<String, String> shopPagesLinks) {
		this.shopPagesLinks = shopPagesLinks;
	}

	public Map<String, String> getSocialMediaLinks() {
		return socialMediaLinks;
	}

	public void setSocialMediaLinks(Map<String, String> socialMediaLinks) {
		this.socialMediaLinks = socialMediaLinks;
	}

	public String getPlansCategoryId() {
		return plansCategoryId;
	}

	public void setPlansCategoryId(String plansCategoryId) {
		this.plansCategoryId = plansCategoryId;
	}

	public String getPhonesCategoryId() {
		return phonesCategoryId;
	}

	public void setPhonesCategoryId(String phonesCategoryId) {
		this.phonesCategoryId = phonesCategoryId;
	}

	/**
	 * @return the geoIPCityFile
	 */
	public String getGeoIPCityFile() {
		return mGeoIPCityFile;
	}

	/**
	 * @param pGeoIPCityFile the geoIPCityFile to set
	 */
	public void setGeoIPCityFile(String pGeoIPCityFile) {
		mGeoIPCityFile = pGeoIPCityFile;
	}

	public String getSupportLink() {
		return supportLink;
	}

	public void setSupportLink(String supportLink) {
		this.supportLink = supportLink;
	}
	
	/**
	 * 
	 * @return the mFeedFilePath
	 */
	public String getPimFeedFilePath() {
		return mPimFeedFilePath;
	}

	/**
	 * 
	 * @param mFeedFilePath
	 */
	public void setPimFeedFilePath(String mPimFeedFilePath) {
		this.mPimFeedFilePath = mPimFeedFilePath;
	}
	

	/**
	 * 
	 * @return the mParcFeedFilePath
	 */
	public String getParcFeedFilePath() {
		return mParcFeedFilePath;
	}


	/**
	 * 
	 * @param mFeedFilePath
	 */
	public void setParcFeedFilePath(String mParcFeedFilePath) {
		this.mParcFeedFilePath = mParcFeedFilePath;
	}

	/**
	 * @return the maxNoOfPackageForPIA
	 */
	public int getMaxNoOfPackageForPIA() {
		return maxNoOfPackageForPIA;
	}

	/**
	 * @param maxNoOfPackageForPIA the maxNoOfPackageForPIA to set
	 */
	public void setMaxNoOfPackageForPIA(int maxNoOfPackageForPIA) {
		this.maxNoOfPackageForPIA = maxNoOfPackageForPIA;
	}

	/**
	 * @return the maxNoOfPackageForPayGo
	 */
	public int getMaxNoOfPackageForPayGo() {
		return maxNoOfPackageForPayGo;
	}

	/**
	 * @param maxNoOfPackageForPayGo the maxNoOfPackageForPayGo to set
	 */
	public void setMaxNoOfPackageForPayGo(int maxNoOfPackageForPayGo) {
		this.maxNoOfPackageForPayGo = maxNoOfPackageForPayGo;
	}

	/**
	 * @return the ftpHostName
	 */
	public String getFtpHostName() {
		return ftpHostName;
	}

	/**
	 * @param pFtpHostName the ftpHostName to set
	 */
	public void setFtpHostName(String pFtpHostName) {
		ftpHostName = pFtpHostName;
	}

	/**
	 * @return the ftpPort
	 */
	public String getFtpPort() {
		return ftpPort;
	}

	/**
	 * @param pFtpPort the ftpPort to set
	 */
	public void setFtpPort(String pFtpPort) {
		ftpPort = pFtpPort;
	}

	/**
	 * @return the ftpUserName
	 */
	public String getFtpUserName() {
		return ftpUserName;
	}

	/**
	 * @param pFtpUserName the ftpUserName to set
	 */
	public void setFtpUserName(String pFtpUserName) {
		ftpUserName = pFtpUserName;
	}

	/**
	 * @return the ftpUserPassword
	 */
	public String getFtpUserPassword() {
		return ftpUserPassword;
	}

	/**
	 * @param pFtpUserPassword the ftpUserPassword to set
	 */
	public void setFtpUserPassword(String pFtpUserPassword) {
		ftpUserPassword = pFtpUserPassword;
	}

	 

	/**
	 * @return the ftpFileLocation
	 */
	public String getFtpFileLocation() {
		return ftpFileLocation;
	}

	/**
	 * @param pFtpFileLocation the ftpFileLocation to set
	 */
	public void setFtpFileLocation(String pFtpFileLocation) {
		ftpFileLocation = pFtpFileLocation;
	}

	/**
	 * @return the inventoryFeedFilePath
	 */
	public String getInventoryFeedFilePath() {
		return inventoryFeedFilePath;
	}

	/**
	 * @param inventoryFeedFilePath the inventoryFeedFilePath to set
	 */
	public void setInventoryFeedFilePath(String inventoryFeedFilePath) {
		this.inventoryFeedFilePath = inventoryFeedFilePath;
	}

	public String getPayGoCategoryId() {
		return payGoCategoryId;
	}

	public void setPayGoCategoryId(String payGoCategoryId) {
		this.payGoCategoryId = payGoCategoryId;
	}

	/**
	 * @return the espCallLogFileLocation
	 */
	public String getEspCallLogFileLocation() {
		return mEspCallLogFileLocation;
	}

	/**
	 * @param pEspCallLogFileLocation the espCallLogFileLocation to set
	 */
	public void setEspCallLogFileLocation(String pEspCallLogFileLocation) {
		mEspCallLogFileLocation = pEspCallLogFileLocation;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the secureHostName
	 */
	public String getSecureHostName() {
		return secureHostName;
	}

	/**
	 * @param secureHostName the secureHostName to set
	 */
	public void setSecureHostName(String secureHostName) {
		this.secureHostName = secureHostName;
	}

	public String getSmartPhonesCategoryId() {
		return smartPhonesCategoryId;
	}

	public void setSmartPhonesCategoryId(String smartPhonesCategoryId) {
		this.smartPhonesCategoryId = smartPhonesCategoryId;
	}

	public String getBasicPhonesCategoryId() {
		return basicPhonesCategoryId;
	}

	public void setBasicPhonesCategoryId(String basicPhonesCategoryId) {
		this.basicPhonesCategoryId = basicPhonesCategoryId;
	}

	public String getOrderStatusMsg_D() {
		return orderStatusMsg_D;
	}

	public void setOrderStatusMsg_D(String orderStatusMsg_D) {
		this.orderStatusMsg_D = orderStatusMsg_D;
	}

	public String getOrderStatusMsg_X() {
		return orderStatusMsg_X;
	}

	public void setOrderStatusMsg_X(String orderStatusMsg_X) {
		this.orderStatusMsg_X = orderStatusMsg_X;
	}

	public String getOrderStatusMsg_R() {
		return orderStatusMsg_R;
	}

	public void setOrderStatusMsg_R(String orderStatusMsg_R) {
		this.orderStatusMsg_R = orderStatusMsg_R;
	}

	public String getOrderStatusMsg_C() {
		return orderStatusMsg_C;
	}

	public void setOrderStatusMsg_C(String orderStatusMsg_C) {
		this.orderStatusMsg_C = orderStatusMsg_C;
	}

	public String getOrderStatusMsg_E() {
		return orderStatusMsg_E;
	}

	public void setOrderStatusMsg_E(String orderStatusMsg_E) {
		this.orderStatusMsg_E = orderStatusMsg_E;
	}

	public String getOrderStatusMsg_P() {
		return orderStatusMsg_P;
	}

	public void setOrderStatusMsg_P(String orderStatusMsg_P) {
		this.orderStatusMsg_P = orderStatusMsg_P;
	}

	public String getBannerProfileProperty() {
		return bannerProfileProperty;
	}

	public void setBannerProfileProperty(String bannerProfileProperty) {
		this.bannerProfileProperty = bannerProfileProperty;
	}

	public List<String> getHppAddon() {
		return hppAddon;
	}

	public void setHppAddon(List<String> hppAddon) {
		this.hppAddon = hppAddon;
	}

	/**
	 * @return the espXMLLoggingEnabled
	 */
	public boolean isEspXMLLoggingEnabled() {
		return espXMLLoggingEnabled;
	}

	/**
	 * @param espXMLLoggingEnabled the espXMLLoggingEnabled to set
	 */
	public void setEspXMLLoggingEnabled(boolean espXMLLoggingEnabled) {
		this.espXMLLoggingEnabled = espXMLLoggingEnabled;
	}

	/**
	 * @return the sendMessageTemplateIdentifier
	 */
	public String getSendMessageTemplateIdentifier() {
		return sendMessageTemplateIdentifier;
	}

	/**
	 * @param sendMessageTemplateIdentifier the sendMessageTemplateIdentifier to set
	 */
	public void setSendMessageTemplateIdentifier(
			String sendMessageTemplateIdentifier) {
		this.sendMessageTemplateIdentifier = sendMessageTemplateIdentifier;
	}

	/**
	 * @return the sendMessagedateFormat
	 */
	public String getSendMessagedateFormat() {
		return sendMessagedateFormat;
	}

	/**
	 * @param sendMessagedateFormat the sendMessagedateFormat to set
	 */
	public void setSendMessagedateFormat(String sendMessagedateFormat) {
		this.sendMessagedateFormat = sendMessagedateFormat;
	}	 
	
}