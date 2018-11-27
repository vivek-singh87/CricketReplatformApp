package com.cricket.util;

import iaik.security.provider.IAIK;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import atg.commerce.profile.CommerceProfileTools;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.vo.CricketAccountInfo;
import com.cricket.vo.CricketProfile;
import com.cricket.vo.MyCricketCookieLocationInfo;

import de.ailis.pherialize.Mixed;
import de.ailis.pherialize.Pherialize;

/**
 * 
 * This component is used to decrypt the Cookie which is coming from account.mycricket.com
 * 
 * @author AK112151
 *
 */
public class CricketUtils extends GenericService {

	
	/**
	 * property profiletools refers to CommerceProfileTools
	 */
	private CommerceProfileTools profiletools;
	/**
	 * property cricketProfile refers to CricketProfile
	 */
	private CricketProfile cricketProfile;
	/**
	 * property accountInfo refers to CricketAccountInfo
	 */
	private CricketAccountInfo accountInfo;
	/**
	 * property LocationInfo refers to MyCricketCookieLocationInfo
	 */
	private MyCricketCookieLocationInfo locationInfo;
	
	/**
	 * @return the locationInfo
	 */
	public MyCricketCookieLocationInfo getLocationInfo() {
		return locationInfo;
	}

	/**
	 * @param locationInfo the locationInfo to set
	 */
	public void setLocationInfo(MyCricketCookieLocationInfo locationInfo) {
		this.locationInfo = locationInfo;
	}

	
	/**
	 * This method decrypts the cookie information and calls setCookieInfoToVO,setLocationInfoToVO,setCartInfoToVO.
	 * methods to set the values in the vo's.	 * 
	 * 
	 * @param key
	 * @param value
	 * @param algorithm
	 * @param name
	 * @return
	 */

	public Mixed getDecriptedCookieInfo(Cookie cookie,DynamoHttpServletRequest pRequest) {
       
		if(isLoggingDebug()){
			logDebug("Entering to  getDecriptedCookieInfo in CricketUtils");
		}
			Security.addProvider(new IAIK());
        	String val = null;
        	String mapKey = null;
        	Map<String, String> cookieMap=new HashMap<String, String>();
        	//String cricketCookie = cookie.getValue();
			String CricketCookieUrlDecoding = null; 
			String decoderCookieValue = null;
			Mixed unserializedMixed = null; 
			CricketProfile cricketProfile = (CricketProfile) pRequest.resolveName(CricketCookieConstants.ATG_CRICKET_UTIL_CRICKET_PROFILE);
			setCricketProfile(cricketProfile);
			MyCricketCookieLocationInfo locationInfo = (MyCricketCookieLocationInfo) pRequest.resolveName(CricketCookieConstants.COM_CRICKET_UTIL_LOCATION_INFO);
			setLocationInfo(locationInfo);
			CricketAccountInfo accountInfo = (CricketAccountInfo) pRequest.resolveName(CricketCookieConstants.ATG_CRICKET_UTIL_CRICKET_ACCOUNT_INFO);
			setAccountInfo(accountInfo);
            //Cipher cipher;
		try {
			if (!StringUtils.isEmpty(cookie.getValue())) {
				CricketCookieUrlDecoding = URLDecoder.decode(cookie.getValue(),
						CricketCookieConstants.UTF_8);
				decoderCookieValue = CricketCookieUrlDecoding.trim();
				Cipher cipher = Cipher.getInstance(
						CricketCookieConstants.RIJNDAEL_256_ECB_NO_PADDING, CricketCookieConstants.IAIK);
				final SecretKeySpec encryptionkey = new SecretKeySpec(getKey(),
						CricketCookieConstants.RIJNDAEL_256);
				cipher.init(Cipher.DECRYPT_MODE, encryptionkey);
				final String decryptedString = new String(cipher.doFinal(Base64
						.decodeBase64(decoderCookieValue)));
				unserializedMixed = Pherialize.unserialize(decryptedString);
				String arrayVal = null;
				String decodedArray[] = { unserializedMixed.toString() };
				for (int i = 0; i < decodedArray.length; i++) {
					arrayVal = decodedArray[i];
				}
				String withOutBrace = arrayVal.substring(1,
						arrayVal.length() - 1);
				StringTokenizer stoTokenizer = new StringTokenizer(
						withOutBrace, CricketCookieConstants.COMMA);
				if (CricketCookieConstants.LOCATION_INFO.equalsIgnoreCase(cookie.getName())) {
					while (stoTokenizer.hasMoreElements()) {
						String aStr = (String) stoTokenizer.nextElement();
						mapKey = aStr.substring(0, aStr.lastIndexOf(CricketCookieConstants.EQUALS));
						val = aStr.substring(aStr.lastIndexOf(CricketCookieConstants.EQUALS) + 1,
								aStr.length());
						cookieMap.put(mapKey, val);
						if (null != cookieMap) {
								setLocationInfoToVO(cookieMap, mapKey);
							}
					}
					if(isLoggingDebug()){
						if(cookieMap != null) {
							logDebug("Location Info Cookie Value After Decrypting = " + cookieMap.toString());
						} else {
							logDebug("Location Info Cookie map is null");
						}
					}
				} else if (CricketCookieConstants.CRICKET.equalsIgnoreCase(cookie.getName())) {
					Map<String, String> cricketCookieMap = getCookieMap(arrayVal);
					if(isLoggingDebug()){
						logDebug("Cricket Cookie Value After Decrypting = " + cricketCookieMap.toString());
					}
					setCookieInfoToVO(cricketCookieMap);
				} 
			}
		} catch (NoSuchAlgorithmException e) {
				if(isLoggingError()){
					logError("NoSuchAlgorithmException",e);
				}
			} catch (NoSuchProviderException e) {
				if(isLoggingError()){
					logError("NoSuchProviderException",e);
				}
			} catch (NoSuchPaddingException e) {
				if(isLoggingError()){
					logError("NoSuchPaddingException",e);
				}
			} catch (InvalidKeyException e) {
				if(isLoggingError()){
					logError("InvalidKeyException",e);
				}
			} catch (IllegalBlockSizeException e) {
				if(isLoggingError()){
					logError("IllegalBlockSizeException",e);
				}
			} catch (BadPaddingException e) {
				if(isLoggingError()){
					logError("BadPaddingException",e);
				}
			} catch (UnsupportedEncodingException e) {
				if(isLoggingError()){
					logError("UnsupportedEncodingException",e);
				}
			}
           
		if(isLoggingDebug()){
			logDebug("Exiting getDecriptedCookieInfo in CricketUtils");
		}
        
        return unserializedMixed;
    }

	public byte[] getKey(){
		String encryptionkey = "50bc55d6acd622dea04ae46e4653747d36a62a83877edd79d190e5d62e161b93";
			byte[] bytes;
			try {
				bytes = Hex.decodeHex(encryptionkey.toCharArray());
				return bytes;
			} catch (DecoderException e) {
				if(isLoggingError()){
					logError("DecoderException",e);
				}
			} 
			return null;
		
	}

	private void setLocationInfoToVO(Map<String, String> cookieMap,
			String mapKey) {
		String key=mapKey.trim();
		if(CricketCookieConstants.MARKET_CODE.equalsIgnoreCase(key)){
			getLocationInfo().setMarketCode(cookieMap.get(mapKey));
		}
		
		if(CricketCookieConstants.ZIP.equalsIgnoreCase(key)){
			getLocationInfo().setZip(cookieMap.get(mapKey));
		}
		
		if(CricketCookieConstants.CITY.equalsIgnoreCase(key)){
			getLocationInfo().setCity(cookieMap.get(mapKey));
		}
		if(CricketCookieConstants.STATE.equalsIgnoreCase(key)){
			getLocationInfo().setState(cookieMap.get(mapKey));
		}
		
		if(CricketCookieConstants.NETWORK_PROVIDER_ID.equalsIgnoreCase(key)){
			getLocationInfo().setNetworkProviderId(cookieMap.get(mapKey));
		}
		
		if(CricketCookieConstants.NETWORK_PROVIDER_NAME.equalsIgnoreCase(key)){
			getLocationInfo().setNetworkProviderName(cookieMap.get(mapKey));
			getLocationInfo().setDefaultLocation(false);
			getLocationInfo().setGeoIpDetecred(false);
		}
		
		if(CricketCommonConstants.IS_DEFAULT_LOCATION.equalsIgnoreCase(key)){
			if(cookieMap.get(mapKey) != null && cookieMap.get(mapKey).equals(Boolean.TRUE.toString())) {
				getLocationInfo().setDefaultLocation(true);
			} else {
				getLocationInfo().setDefaultLocation(false);
			}
		}
		
		if(CricketCommonConstants.IS_GEO_IP_DETECTED.equalsIgnoreCase(key)){
			if(cookieMap.get(mapKey) != null && cookieMap.get(mapKey).equals(Boolean.TRUE.toString())) {
				getLocationInfo().setGeoIpDetecred(true);
			} else {
				getLocationInfo().setGeoIpDetecred(false);
			}
		}
		
		
		

		/*try {
			atg.core.util.BeanUtils.setPropertyValue(getLocationInfo(), mapKey, cookieMap.get(mapKey));
		} catch (ItemNotFoundException e) {
			if(isLoggingError()){
				logError("ItemNotFoundException:", e);
			}
		}*/
	}

	
	/**
	 * method takes plain text as a parameter and sets in to VO.
	 * @param plainText
	 */
	private void setCookieInfoToVO(Map<String, String> cookieMap) {
		
		if(isLoggingDebug()){
			logDebug("Map Value in setCookieInfoToVO = "+cookieMap.toString());
		}
		
		if(cookieMap != null && cookieMap.size() >0){
			getCricketProfile().setABPFlag(Boolean.valueOf(cookieMap.get(CricketCookieConstants.ABP)));
			getCricketProfile().setCustomerType(cookieMap.get(CricketCookieConstants.CUSTOMER_TYPE));
			getCricketProfile().setFirstName(cookieMap.get(CricketCookieConstants.FIRST_NAME1));
			getCricketProfile().setLastName(cookieMap.get(CricketCookieConstants.LAST_NAME1));
			 getCricketProfile().setMdn(cookieMap.get(CricketCookieConstants.MDN));
			  getCricketProfile().setUserId(cookieMap.get(CricketCookieConstants.MDN));
			  getCricketProfile().setMiddleName(cookieMap.get(CricketCookieConstants.MIDDLE_NAME));
			  getCricketProfile().setPhoneCode(cookieMap.get(CricketCookieConstants.PHONE_CODE));
			  getCricketProfile().setPlanCode(cookieMap.get(CricketCookieConstants.PLAN_CODE));
			  getCricketProfile().setPlaneName(cookieMap.get(CricketCookieConstants.PLANE_NAME));
			  getCricketProfile().setProductType(cookieMap.get(CricketCookieConstants.PRODUCT_TYPE));
			  getCricketProfile().setData(cookieMap.get(CricketCookieConstants.DATA));
			  getCricketProfile().setDeviceModel(cookieMap.get(CricketCookieConstants.DEVICE_MODEL));
			  getCricketProfile().setFamilyplanLOS(cookieMap.get(CricketCookieConstants.FAMILYPLAN_LOS));
			  getCricketProfile().setMarketCode(cookieMap.get(CricketCookieConstants.MARKET_CODE2));	
			  getCricketProfile().setRatePlanGroupCode(cookieMap.get(CricketCookieConstants.RATE_PLAN_GROUP_CODE));
			  getCricketProfile().setVoice(cookieMap.get(CricketCookieConstants.VOICE));
			  getCricketProfile().setAccountNumber(cookieMap.get(CricketCookieConstants.ACCOUNT_NUMBER_ORIGIONAL));
			  getCricketProfile().setCustTypeName(cookieMap.get(CricketCookieConstants.CUST_TYPE_NAME));	
			  getCricketProfile().setAccountName(cookieMap.get(CricketCookieConstants.ACCOUNT_NAME));
			  getCricketProfile().setRatePlanTypeId(cookieMap.get(CricketCookieConstants.RATE_PLAN_TYPE_ID));
			  getCricketProfile().setRatePlanTypeName(cookieMap.get(CricketCookieConstants.RATE_PLAN_TYPE_NAME));
			  getCricketProfile().setIntention(cookieMap.get(CricketCookieConstants.INTENTION));
			  getCricketProfile().setFamilyPlan(cookieMap.get(CricketCookieConstants.FAMILY_PLAN));
			  getCricketProfile().setRatePlanCode(cookieMap.get(CricketCookieConstants.RATE_PLAN_CODE));
			  getCricketProfile().setDate(cookieMap.get(CricketCookieConstants.DATE));
			  getCricketProfile().setNumberOfLines(cookieMap.get(CricketCookieConstants.NUMBER_OF_LINES));
			  getCricketProfile().setPassword("Cricket1");
		}
		if (cricketProfile.getUserId() != null&& cricketProfile.getMdn() != null) {
			createProfileinATG(cricketProfile);
		}
		
	}
	
	/**
	 * Takes the Cricket profile Vo and Creates the profile in ATG or Updated the profile in ATG. 
	 * 
	 * @param cricketProfile
	 */
	private void createProfileinATG(CricketProfile cricketProfile) {
		MutableRepository profileRepository = getProfiletools()
				.getProfileRepository();
		try {
			if (cricketProfile != null) {
				if(isLoggingDebug()){
					logDebug("Entering in to  createProfileinATG of CricketUtil");
				}
				MutableRepositoryItem userItemForUpdate = profileRepository
						.getItemForUpdate(cricketProfile.getUserId(), CricketCookieConstants.USER); 
				if (null != userItemForUpdate && cricketProfile.getUserId().equalsIgnoreCase(userItemForUpdate.getRepositoryId())) {
					if(isLoggingDebug()){
						logDebug("Updating the Existing User in to  createProfileinATG of CricketUtil");
					}
					userItemForUpdate.setPropertyValue(CricketCookieConstants.FIRST_NAME,cricketProfile.getFirstName());
					userItemForUpdate.setPropertyValue(CricketCookieConstants.MIDDLE_NAME,cricketProfile.getMiddleName());
					userItemForUpdate.setPropertyValue(CricketCookieConstants.MDN,cricketProfile.getMdn());
					userItemForUpdate.setPropertyValue(CricketCookieConstants.ABP_FLAG,cricketProfile.isABPFlag());
					userItemForUpdate.setPropertyValue(CricketCookieConstants.CUSTOMER_TYPE1,cricketProfile.getCustomerType());
					userItemForUpdate.setPropertyValue(CricketCookieConstants.PLAN_CODE1,cricketProfile.getPlanCode());
					userItemForUpdate.setPropertyValue(CricketCookieConstants.PHONE_CODE1,cricketProfile.getPhoneCode());
					userItemForUpdate.setPropertyValue(CricketCookieConstants.PLANE_NAME1,cricketProfile.getPlaneName());
					userItemForUpdate.setPropertyValue(CricketCookieConstants.PRODUCT_TYPE1,cricketProfile.getProductType());
					String encryptPassword = getProfiletools()
							.getPropertyManager().getPasswordHasher().hashPasswordForLogin(/*s)generatePassword(*/
									cricketProfile.getPassword());
					userItemForUpdate.setPropertyValue(CricketCookieConstants.PASSWORD,encryptPassword);
					userItemForUpdate.setPropertyValue(CricketCookieConstants.LOGIN,cricketProfile.getUserId());
					userItemForUpdate.setPropertyValue(CricketCookieConstants.LAST_NAME,cricketProfile.getLastName());
					//userItemForUpdate.setPropertyValue(CricketCookieConstants.NUMBER_OF_LINES,cricketProfile.getNumberOfLines());
					if(null != getAccountInfo().getZip()){
						RepositoryItem accountInfo = setAccountInfoInProfile(getAccountInfo());
							userItemForUpdate.setPropertyValue(CricketCookieConstants.HOME_ADDRESS,accountInfo);
					}
					profileRepository.updateItem(userItemForUpdate);
				} else {
					MutableRepositoryItem PhoneProfileItem = profileRepository
							.createItem(cricketProfile.getUserId(), CricketCookieConstants.USER);
					if(isLoggingDebug()){
						logDebug("Creating New User in to  createProfileinATG of CricketUtil");
					}
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.FIRST_NAME,cricketProfile.getFirstName());
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.MIDDLE_NAME,cricketProfile.getMiddleName());
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.MDN,cricketProfile.getMdn());
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.ABP_FLAG,cricketProfile.isABPFlag());
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.CUSTOMER_TYPE1,cricketProfile.getCustomerType());
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.PLAN_CODE1,cricketProfile.getPlanCode());
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.PHONE_CODE1,cricketProfile.getPhoneCode());
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.PLANE_NAME1,cricketProfile.getPlaneName());
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.PRODUCT_TYPE1,cricketProfile.getProductType());
					String encryptPassword = getProfiletools()
							.getPropertyManager().getPasswordHasher().hashPasswordForLogin(/*s)generatePassword(*/
									cricketProfile.getPassword());
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.PASSWORD,encryptPassword);
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.LOGIN,cricketProfile.getUserId());
					PhoneProfileItem.setPropertyValue(CricketCookieConstants.LAST_NAME,cricketProfile.getLastName());
					//PhoneProfileItem.setPropertyValue(CricketCookieConstants.NUMBER_OF_LINES,cricketProfile.getNumberOfLines());
					if(null != getAccountInfo().getZip()){
						RepositoryItem accountInfo = setAccountInfoInProfile(getAccountInfo());
							PhoneProfileItem.setPropertyValue(CricketCookieConstants.HOME_ADDRESS,accountInfo);
					}
					profileRepository.addItem(PhoneProfileItem);
				}
			}
		} catch (RepositoryException e) {
			if(isLoggingError()){
				logError("RepositoryException:", e);
			}
		}

	}

	/**
	 * Takes the account Info VO and Creates the ContactInfor Item In ATG to set it as home address.
	 * 
	 * @param accountInfo
	 * @return
	 */
	private RepositoryItem setAccountInfoInProfile(
			CricketAccountInfo accountInfo) {
		
		MutableRepository profileRepository = getProfiletools()
				.getProfileRepository();
		RepositoryItem accountItem = null;
		try {
			if(null != accountInfo.getZip()){
				MutableRepositoryItem contactInfoItem = profileRepository.createItem(CricketCookieConstants.CONTACT_INFO);
				contactInfoItem.setPropertyValue(CricketCookieConstants.ADDRESS1, accountInfo.getAdderss1());
				contactInfoItem.setPropertyValue(CricketCookieConstants.ADDRESS2, accountInfo.getAdderss2());
				contactInfoItem.setPropertyValue(CricketCookieConstants.CITY, accountInfo.getCity());
				contactInfoItem.setPropertyValue(CricketCookieConstants.COUNTRY, accountInfo.getCountry());
				contactInfoItem.setPropertyValue(CricketCookieConstants.STATE, accountInfo.getState());
				contactInfoItem.setPropertyValue(CricketCookieConstants.POSTAL_CODE, accountInfo.getZip());
				accountItem = profileRepository.addItem(contactInfoItem);
			}
		} catch (RepositoryException e) {
			if(isLoggingError()){
				logError("RepositoryException:", e);
			}
		}
		return accountItem;
	}

	/**
	 * @return the profiletools
	 */
	public CommerceProfileTools getProfiletools() {
		return profiletools;
	}

	/**
	 * @param profiletools
	 *            the profiletools to set
	 */
	public void setProfiletools(CommerceProfileTools profiletools) {
		this.profiletools = profiletools;
	}

	/**
	 * @return the cricketProfile
	 */
	public CricketProfile getCricketProfile() {
		return cricketProfile;
	}

	/**
	 * @param cricketProfile
	 *            the cricketProfile to set
	 */
	public void setCricketProfile(CricketProfile cricketProfile) {
		this.cricketProfile = cricketProfile;
	}

	/**
	 * @return the accountInfo
	 */
	public CricketAccountInfo getAccountInfo() {
		return accountInfo;
	}

	/**
	 * @param accountInfo
	 *            the accountInfo to set
	 */
	public void setAccountInfo(CricketAccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}
	
	public Map<String, String> getCookieMap(String cookie){
		//removing the first and last flower brackets
		cookie = cookie.substring(1,cookie.length()-1);
		if(isLoggingDebug()){
			logDebug("Before Converting Cookie Value to Map"+cookie);
		}
		String str = null;
		String strSplit[] = null;
		StringBuilder sb = null;
		int flowerBracketStartIndex = -1;
		int flowerBracketEndIndex = -1;
		Map<String, String> cookieMap = new HashMap<String, String>();
		String complexKey = null;
		
		StringTokenizer st = new StringTokenizer(cookie,CricketCookieConstants.COMMA);
		while(st.hasMoreTokens()){
			str = st.nextToken();			
			flowerBracketStartIndex = str.indexOf("{");
			if(flowerBracketStartIndex > 0){
				flowerBracketEndIndex = str.indexOf("}");
				if(flowerBracketEndIndex > 0){
					//might be empty string or only one value is available
					if(flowerBracketStartIndex + 1 == flowerBracketEndIndex){
						//empty value for complex key, store key, set value to null						
						strSplit = str.split(CricketCookieConstants.EQUALS);
						cookieMap.put(strSplit[0].trim(), null);
					}else{
						//only one value for the complex key						
						cookieMap.put(str.substring(0, str.indexOf(CricketCookieConstants.EQUALS)).trim(), str.substring(str.indexOf(CricketCookieConstants.EQUALS)+1, str.length()).trim());
					}
				}else{					
					//more than one value for complex key, retrieve complete value	
					complexKey = str.substring(0, str.indexOf(CricketCookieConstants.EQUALS));					
					sb = new StringBuilder();
					sb.append(str.substring(str.indexOf(CricketCookieConstants.EQUALS)+1, str.length()));
					str = st.nextToken();
					flowerBracketEndIndex = str.indexOf("}");
					while(flowerBracketEndIndex < 0){
						sb.append(CricketCookieConstants.COMMA);	
						sb.append(str);	
						str = st.nextToken();
						flowerBracketEndIndex = str.indexOf("}");
					}
					//capturing last part of the complex value
					sb.append(CricketCookieConstants.COMMA);
					sb.append(str);
					cookieMap.put(complexKey.trim(), sb.toString().trim());
				}
			}else{
				strSplit = str.split(CricketCookieConstants.EQUALS);
				//put in map 
				cookieMap.put(strSplit[0].trim(), strSplit[1].trim());
			}
			
		}
		return cookieMap;
	}

}
