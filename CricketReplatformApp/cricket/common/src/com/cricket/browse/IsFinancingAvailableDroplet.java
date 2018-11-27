package com.cricket.browse;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import atg.core.util.StringUtils;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;

public class IsFinancingAvailableDroplet extends DynamoServlet {
	
	private List<String> excludedInFootPrintCities;
	
	@Override
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		vlogDebug("Start of IsFinancingAvailableDroplet's service method");
		
		String userLocationCity = pRequest.getParameter(CricketCommonConstants.USER_LOC_CITY);
		String marketType = pRequest.getParameter(CricketCommonConstants.MARKET_TYPE); 
		String OOFMarketType = pRequest.getParameter(CricketCommonConstants.OOF_MARKET_TYPE);
		if(!StringUtils.isBlank(marketType) && !StringUtils.isBlank(OOFMarketType) && marketType.equalsIgnoreCase(OOFMarketType)) {
			vlogDebug("This is an OOF market type");
			pRequest.setParameter(CricketCommonConstants.SHOW_FINANCE, CricketCommonConstants.FALSE);
			pRequest.serviceParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
		} else if(!StringUtils.isBlank(userLocationCity) && getExcludedInFootPrintCities().size() > 0 && getExcludedInFootPrintCities().contains(userLocationCity)) {
			vlogDebug("This is a Excluded InFootPrint City");
			pRequest.setParameter(CricketCommonConstants.SHOW_FINANCE, CricketCommonConstants.FALSE);
			pRequest.serviceParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
		} else {
			vlogDebug("This is other option");
			pRequest.setParameter(CricketCommonConstants.SHOW_FINANCE, CricketCommonConstants.TRUE);
			pRequest.serviceParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
		}
		vlogDebug("End of IsFinancingAvailableDroplet's service method");
	}

	public List<String> getExcludedInFootPrintCities() {
		return excludedInFootPrintCities;
	}

	public void setExcludedInFootPrintCities(List<String> pExcludedInFootPrintCities) {
		excludedInFootPrintCities = pExcludedInFootPrintCities;
	}
}
