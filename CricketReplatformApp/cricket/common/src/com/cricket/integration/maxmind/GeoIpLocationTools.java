package com.cricket.integration.maxmind;

import iaik.security.provider.IAIK;

import java.io.IOException;
import java.security.Security;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import source.com.maxmind.geoip.Location;
import source.com.maxmind.geoip.LookupService;
import source.com.maxmind.geoip.regionName;
import source.com.maxmind.geoip.timeZone;
import atg.nucleus.GenericService;
import atg.repository.MutableRepository;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.QueryExpression;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryItemDescriptor;
import atg.repository.RepositoryView;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.configuration.CricketConfiguration;
import com.cricket.util.CricketUtils;

import de.ailis.pherialize.Pherialize;

/* 
* GeoIpLocation extends  GenericService, this class uses MaxMind API to 
* get the city info by passing ip address of the user
* @author : techM
* @version 1.0
*/ 
public class GeoIpLocationTools extends GenericService{
	
	private static final String RIJNDAEL_256_ECB_PKCS5_PADDING = "Rijndael-256/ECB/PKCS5Padding";
	private static final String ZIP_CODE = "zipCode";
	private static final String CRICKET_ZIPCODE_ITEM = "CricketzipCodeItem";
	private CricketUtils cricketUtils;
	//property : mContentRepository
	private MutableRepository mContentRepository;	
	CricketConfiguration mCricketConfiguration;
	
	private String defaultIpAddr;
	
	
	/**
	 * @return the cricketConfiguration
	 */
	public CricketConfiguration getCricketConfiguration() {
		return mCricketConfiguration;
	}

	/**
	 * @param pCricketConfiguration the cricketConfiguration to set
	 */
	public void setCricketConfiguration(CricketConfiguration pCricketConfiguration) {
		mCricketConfiguration = pCricketConfiguration;
	}

	/**
	 * @return the contentRepository
	 */
	public MutableRepository getContentRepository() {
		return mContentRepository;
	}

	/**
	 * @param pContentRepository the contentRepository to set
	 */
	public void setContentRepository(MutableRepository pContentRepository) {
		mContentRepository = pContentRepository;
	}

	/**
	 * The method is used to get the city details from Max Mind by using the ip address of the user
	 * @param pIpAddress - String
	 * @return CityVO
	 * */
	CityVO getCityInfoObject(String pIpAddress){
		if(isLoggingDebug()){
			logDebug("Enter getCityInfoObject::"+pIpAddress);
		}
		CityVO cityVO = null;
		LookupService lookupService = null;
		
		try {
			//get the LookupService object from the Max Mind
			 lookupService = new LookupService(getCricketConfiguration().getGeoIPCityFile(),LookupService.GEOIP_STANDARD );			
			 Location location = null;
			 if(lookupService != null){
				 //get location object by passing ip address
				 location = lookupService.getLocation(pIpAddress);
				 if(isLoggingDebug()){
						logDebug("location: " +location);						
				 }
			 }
	         if(location != null){
	        	 cityVO = new CityVO();
	        	 //set city Info from location object to cityVO
            	cityVO.setCountryCode(location.countryCode);
        		cityVO.setCountryName(location.countryName);
        		cityVO.setRegioncode(location.region);
        		cityVO.setState(location.region);
        		cityVO.setRegionName(regionName.regionNameByCode(location.countryCode, location.region));
        		cityVO.setCity(location.city);
        		cityVO.setPostalCode(location.postalCode);
        		cityVO.setLatitude(location.latitude);
        		cityVO.setLongitude(location.longitude);
        		cityVO.setMetroCode(location.metro_code);
        		if(pIpAddress.equals(getDefaultIpAddr())) {
        			cityVO.setDefaultLocation(true);
        		} else {
        			cityVO.setGeoIpDetecred(true);
        		}
        		cityVO.setAreaCode(String.valueOf(location.area_code));
        		cityVO.setTimeZone(timeZone.timeZoneByCountryAndRegion(location.countryCode, location.region));
	         }else{
	        	 if(isLoggingDebug()){
					 logDebug("::::: No Location Detected by the Maxmind for the given IP Address :::::"+pIpAddress);
				 }
	         }
			 if(isLoggingDebug()){
				 logDebug("Exit getCityInfoObject:::"+cityVO);
			 }
		} catch (IOException e) {
			if (isLoggingError()) {
				logError("IOException" + e);
			} 
		} finally {
			if(lookupService != null){
				lookupService.close();
			}
		}
		

		return cityVO;
	}

	CityVO getCityInfoFromRepository(String pLatitude, String pLongitude, String zipCode){
		if(isLoggingDebug()){
			logDebug("Inside getCityInfoFromRepository: lat :"+pLatitude+"::Long::"+pLongitude);
		}
		CityVO cityVO = null;
		RepositoryItem[] repsItem = getCityInfoRepoItem(pLatitude, pLongitude, zipCode);
		if(repsItem != null && repsItem[0] != null){
			cityVO = new CityVO();
			if(repsItem[0].getPropertyValue(CricketCookieConstants.CITY) != null) {
				cityVO.setCity(repsItem[0].getPropertyValue(CricketCookieConstants.CITY).toString());
			}
			if(repsItem[0].getPropertyValue(CricketCookieConstants.STATE) != null) {
				cityVO.setState(repsItem[0].getPropertyValue(CricketCookieConstants.STATE).toString());
			}
			if(repsItem[0].getPropertyValue(CricketCookieConstants.COUNTRY) != null) {
				cityVO.setCountryName(repsItem[0].getPropertyValue(CricketCookieConstants.COUNTRY).toString());
			}
			if(pLongitude != null) {
				cityVO.setLongitude(Double.valueOf(pLongitude));
			}
			if(pLatitude != null) {
				cityVO.setLatitude(Double.valueOf(pLatitude));
			}
			if(repsItem[0].getPropertyValue(CricketCommonConstants.ZIP_CODE) != null) {
				cityVO.setPostalCode(repsItem[0].getPropertyValue(CricketCommonConstants.ZIP_CODE).toString());
			}
			if(repsItem[0].getPropertyValue(CricketCommonConstants.AREA_CODE) != null) {
				cityVO.setAreaCode(repsItem[0].getPropertyValue(CricketCommonConstants.AREA_CODE).toString());
			}
			if(repsItem[0].getPropertyValue(CricketCommonConstants.TIME_ZONE) != null) {
				cityVO.setTimeZone(repsItem[0].getPropertyValue(CricketCommonConstants.TIME_ZONE).toString());
			}
			cityVO.setGeoIpDetecred(true);
		}
		if(isLoggingDebug()){
			 logDebug("Exit getCityInfoFromRepository:::"+cityVO);
		}
		return cityVO;
	}
	
	RepositoryItem[] getCityInfoRepoItem(String pLatitude, String pLongitude, String zipCode){
		RepositoryItem[] repsItem = null;
		//Get Mutable repository 
		MutableRepository mRepos = getContentRepository();
		RepositoryItemDescriptor rItemDesc = null;
		RepositoryView reposView = null;
		try {
			//get ItemDescriptor : CricketzipCodeItem 
			if(mRepos != null){
				rItemDesc = mRepos.getItemDescriptor(CricketCommonConstants.ZIPCODE_ITEMDESCRIPTOR_NAME);
			}
			//the the view of the repository
			if(rItemDesc != null){
				reposView = rItemDesc.getRepositoryView();
			}
			//Query the repository to get city info by using latitude and longitude
			if(reposView != null){
				QueryBuilder qBuilder = reposView.getQueryBuilder();
				
				//Query for latitude
				QueryExpression zipCd = qBuilder.createPropertyQueryExpression(CricketCommonConstants.ZIP_CODE);
				QueryExpression zipCdValue = qBuilder.createConstantQueryExpression(zipCode);
				Query zipCodeQuery = qBuilder.createComparisonQuery(zipCd, zipCdValue, QueryBuilder.EQUALS);
				repsItem = reposView.executeQuery(zipCodeQuery);
			}	
			
		} catch (RepositoryException e) {
			if (isLoggingError()) {
				logError("RepositoryException" + e);
			}
		}		
		return repsItem;
	}
	
	/**
	 * @param zipCode
	 * @return
	 * @throws RepositoryException 
	 */
	public RepositoryItem[] getZipCodeInfo(String zipCode) throws RepositoryException{
		RepositoryView geoView = getContentRepository().getView(CRICKET_ZIPCODE_ITEM);
		QueryBuilder builder = geoView.getQueryBuilder();
		QueryExpression propertyB = builder.createPropertyQueryExpression(ZIP_CODE);
		QueryExpression valueB = builder.createConstantQueryExpression(zipCode);
		Query invQuery = builder.createComparisonQuery(propertyB, valueB, QueryBuilder.EQUALS);
		return geoView.executeQuery(invQuery);
	}

	
	 public String encrypt(Map<String, String> strToEncrypt)
	    {
	        try
	        {
	        	Security.addProvider(new IAIK());
	        	Cipher cipher = Cipher.getInstance(RIJNDAEL_256_ECB_PKCS5_PADDING,CricketCookieConstants.IAIK);
	            final SecretKeySpec secretKey = new SecretKeySpec(getCricketUtils().getKey(), CricketCookieConstants.RIJNDAEL_256);
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	            final String serializedStr = Pherialize.serialize(strToEncrypt);
	            //System.out.println("serializedStr::::"+serializedStr);
	            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(serializedStr.getBytes()));
	            return encryptedString;
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        return null;

	    }

	/**
	 * @return the cricketUtils
	 */
	public CricketUtils getCricketUtils() {
		return cricketUtils;
	}

	/**
	 * @param cricketUtils the cricketUtils to set
	 */
	public void setCricketUtils(CricketUtils cricketUtils) {
		this.cricketUtils = cricketUtils;
	}


	public String getDefaultIpAddr() {
		return defaultIpAddr;
	}

	public void setDefaultIpAddr(String defaultIpAddr) {
		this.defaultIpAddr = defaultIpAddr;
	}
	

}
