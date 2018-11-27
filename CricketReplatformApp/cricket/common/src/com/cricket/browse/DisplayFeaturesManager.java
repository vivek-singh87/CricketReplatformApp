package com.cricket.browse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import atg.nucleus.GenericService;

import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.InquireFeaturesRequestVO;
import com.cricket.integration.esp.vo.InquireFeaturesResponseVO;

/**
 * 
 * @author Cricket Communications
 *
 */
public class DisplayFeaturesManager extends GenericService {

	
	private CricketESPAdapter cricketESPAdapter;
	

	public Map<String, List<InquireFeaturesResponseVO>> getCompatibleAddons(
			String modelNumber, String phoneType, String planCode,
			String marketCode, String transactionName, List userAddons, boolean hasEsnHistory,
			boolean isCricketPhone, String saleChannelName, String pOrderId) {
		Map<String,List<InquireFeaturesResponseVO>> planAddOnsMap = new HashMap<String,List<InquireFeaturesResponseVO>>();
		
		InquireFeaturesRequestVO inquireFeaturesRequestVO = new InquireFeaturesRequestVO();
		/*InquireFeaturesResponseVO inquireFeaturesResponseVO = new InquireFeaturesResponseVO();
		inquireFeaturesResponseVO.setGroupName("Data Backup");
		inquireFeaturesResponseVO.setId("482");
		inquireFeaturesResponseVO.setMandatory(true);
		inquireFeaturesResponseVO.setName("Data Backup-CMB");
		inquireFeaturesResponseVO.setPrice(BigDecimal.TEN);
		inquireFeaturesResponseVO.setType(BigInteger.ONE);*/
		inquireFeaturesRequestVO.setHasEsnHistory(hasEsnHistory);
		inquireFeaturesRequestVO.setIsCricketPhone(isCricketPhone);
		inquireFeaturesRequestVO.setMarketId(marketCode);
		inquireFeaturesRequestVO.setPhoneCode(modelNumber);
		inquireFeaturesRequestVO.setPhoneType(phoneType);
		inquireFeaturesRequestVO.setPricePlanCode(planCode);
		inquireFeaturesRequestVO.setSalesChannelName(saleChannelName);
		inquireFeaturesRequestVO.setTransactionName(transactionName);
		String[] accountUser = null;
		if(null != userAddons && userAddons.size() > 0){
			accountUser = new String[userAddons.size()];
			for(int i = 0; i < userAddons.size(); i++){
				accountUser[i] = (String) userAddons.get(i);
			}
		}
		
		inquireFeaturesRequestVO.setFeatureCodes(accountUser);
		planAddOnsMap = getFeatures(inquireFeaturesRequestVO,pOrderId);
		return planAddOnsMap;
	}	
	
	/**
	 * 
	 * @param pOrderId 
	 * @param productId
	 */
	public Map<String,List<InquireFeaturesResponseVO>> getFeatures(InquireFeaturesRequestVO inquireFeaturesRequestVO, String pOrderId) {
		
		Map<String,List<InquireFeaturesResponseVO>> planAddOnsMap = new HashMap<String,List<InquireFeaturesResponseVO>>();
		
		try{								
			planAddOnsMap = getCricketESPAdapter().inquireFeatures(inquireFeaturesRequestVO,pOrderId);	
		} catch (CricketException e) {
			vlogError("An Exception Occurred", e);
		}	
		return 	planAddOnsMap;			
	}	
	
	/**
	 * 
	 * @return
	 */
	public CricketESPAdapter getCricketESPAdapter() {
		return cricketESPAdapter;
	}

	/**
	 * 
	 * @param cricketESPAdapter
	 */
	public void setCricketESPAdapter(CricketESPAdapter cricketESPAdapter) {
		this.cricketESPAdapter = cricketESPAdapter;
	}

	
	
}
