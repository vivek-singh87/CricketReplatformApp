package com.cricket.browse.droplet;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.seo.CanonicalItemLink;
import atg.repository.seo.ItemLinkException;
import atg.repository.seo.UrlParameter;
import atg.repository.seo.UrlParameterLookup;
import atg.repository.seo.UrlTemplate;
import atg.repository.seo.UrlTemplateMapper;
import atg.repository.seo.UserResourceLookup;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

public class CricketCanonicalItemLinkDroplet extends CanonicalItemLink {

	protected void populateParams(DynamoHttpServletRequest pRequest, UrlParameter[] pParams, UrlParameterLookup[] pLookups) {
		for (UrlParameterLookup lookup : pLookups) {
			String lookupName = null;
			String itemDesriptorName = pRequest.getParameter(CricketCommonConstants.ITEM_DESC_NAME);
			String seoString = pRequest.getParameter(CricketCommonConstants.SEO_STRING);
			if (lookup != null) {
				lookupName = lookup.getName();
			}
			for (int i = 0; i < pParams.length; i++) {
				if ((lookupName != null) && (pParams[i].getItemName().equals(lookupName))) {
					pParams[i].setLookup(lookup);
				}
				try {
					pParams[i].lookupValue(pRequest, null);
					if(itemDesriptorName != null) {
						if(pParams[i].getPropertyName() != null) {
							if(pParams[i].getPropertyName().equals(CricketCommonConstants.PARENT_CAT_DISPLAY_NAME) && (itemDesriptorName.equals(CricketCommonConstants.PHONE_PRODUCT) || itemDesriptorName.equals(CricketCommonConstants.ITEM_DESC_ADDON_PRODUCT))) {
								pParams[i].setValue(seoString);
							}
							if(pParams[i].getValue() != null) {
								if(pParams[i].getPropertyName().equals(CricketCommonConstants.PROP_DISP_NAME)) {
									pParams[i].setValue(pParams[i].getValue().replace(CricketCommonConstants.FORWARD_SLASH, CricketCommonConstants.HYPHEN_SYM));
								}
								if(!pParams[i].getPropertyName().equals(CricketCommonConstants.REPOSITORY_ID)) {
									pParams[i].setValue(pParams[i].getValue().replace(CricketCommonConstants.SYM_PLUS, CricketCommonConstants.HYPHEN_SYM).toLowerCase());
								}
							}
						}
					}
				} catch (ItemLinkException ile) {
					if (isLoggingError()) {
						logError(ile);
					}
				}
			}
		}
	}
}
