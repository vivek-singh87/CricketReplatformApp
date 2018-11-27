package com.cricket.browse.droplet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.configuration.CricketConfiguration;

import atg.core.util.StringUtils;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class IsMandatoryAddonDroplet extends DynamoServlet {

	private CricketConfiguration cricketConfiguration;
	private boolean isMandatory = false;

	public void service(DynamoHttpServletRequest pReq,
			DynamoHttpServletResponse pRes) throws ServletException,
			IOException {

		String addonId = pReq.getParameter(CricketCommonConstants.ADDON_ID);
		vlogDebug("AddonID:" + addonId);
		List<String> mandatoryAddons = getCricketConfiguration().getHppAddon();

		if (!StringUtils.isBlank(addonId) && null != mandatoryAddons && mandatoryAddons.size() > 0 && mandatoryAddons.contains(addonId)) {
			isMandatory = true;
			vlogDebug("Mandatory Addon:" + addonId);
		} else {
			isMandatory = false;
		}
		pReq.setParameter(CricketCommonConstants.IS_MANDATORY, isMandatory);
		pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
	}

	public CricketConfiguration getCricketConfiguration() {
		return cricketConfiguration;
	}

	public void setCricketConfiguration(
			CricketConfiguration cricketConfiguration) {
		this.cricketConfiguration = cricketConfiguration;
	}
}
