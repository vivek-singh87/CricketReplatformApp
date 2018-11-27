package com.cricket.seo;

import atg.core.util.StringUtils;
import atg.repository.Repository;
import atg.repository.seo.IndirectUrlTemplate;
import atg.repository.seo.ItemLinkException;
import atg.service.webappregistry.WebApp;
import atg.servlet.DynamoHttpServletRequest;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.configuration.CricketConfiguration;
import com.cricket.vo.CricketProfile;

/**
 * 
 * @author 
 *
 */
public class StaticIndirectUrlTemplate extends IndirectUrlTemplate {

	private CricketConfiguration cricketConfiguration;
	

	/**
	 * @return
	 */
	public CricketConfiguration getCricketConfiguration() {
		return cricketConfiguration;
	}

	/**
	 * @param cricketConfiguration
	 */
	public void setCricketConfiguration(final CricketConfiguration cricketConfiguration) {
		this.cricketConfiguration = cricketConfiguration;
	}

	
	/**
	 *  
	 */
	public String getForwardUrl(DynamoHttpServletRequest pRequest, String pIndirectUrl, WebApp pDefaultWebApp, Repository pDefaultRepository)
		    throws ItemLinkException {
		
		String forwardUrl = null;
		forwardUrl = super.getForwardUrl(pRequest, pIndirectUrl, pDefaultWebApp, pDefaultRepository);
		
		CricketProfile cricketProfile = (CricketProfile) pRequest.resolveName("/atg/cricket/util/CricketProfile");
		
		if(isLoggingDebug()){			
			logDebug("pIndirectUrl = "+ pIndirectUrl);			
			logDebug("MDN ="+cricketProfile.getMdn()+" PhoneCode = "+cricketProfile.getPhoneCode() + " DeviceModel = "+cricketProfile.getDeviceModel()+" PlanCode = "+cricketProfile.getPlanCode()
					 +" ProductType = " + cricketProfile.getProductType() + " MarketCode = " + cricketProfile.getMarketCode() + " FirstName = " + cricketProfile.getFirstName()
					 +" userId = " + cricketProfile.getUserId() +" User Name = "+cricketProfile.getUserName() );
			logDebug(" forwardUrl = "+forwardUrl);
		}
	   StringBuffer forwardUrlSB = new StringBuffer();	
      
        if(pIndirectUrl.contains(CricketCommonConstants.ADD_A_LINE) && !StringUtils.isBlank(forwardUrl)) {
        	
        	//forwardUrlSB.append(forwardUrl);
        	forwardUrl = forwardUrl+"?"+"intention=addLine";
        	forwardUrlSB.append(forwardUrl);
        } else if(pIndirectUrl.contains(CricketCommonConstants.WHY_CRICKET) && !StringUtils.isBlank(forwardUrl)) {
        	
        	//forwardUrl = forwardUrl+"?"+"intention=addLine";
        	forwardUrlSB.append(forwardUrl);
        } else if(pIndirectUrl.contains("billing-ordersummary/enter-billing-orderid") && !StringUtils.isBlank(forwardUrl)) {
        	
        	//forwardUrl = forwardUrl+"?"+"intention=addLine";
        	forwardUrlSB.append(forwardUrl);
        }
        
		return forwardUrl;
		
	}
		    
		    
}