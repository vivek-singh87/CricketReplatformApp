package com.cricket.browse.droplet;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.configuration.CricketConfiguration;

import antlr.StringUtils;
import atg.droplet.IsEmpty;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class CreateCategoryLinkSeoUrlDroplet extends DynamoServlet {
	
	private String seoPropertyName = "displayName";
	
	private CricketConfiguration cricketConfiguration;
	
	
	public void service(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		if(isLoggingDebug()) {
			logDebug("entering CreateCategoryLinkSeoUrlDroplet service method");
		}
		///browse?N=10153
		RepositoryItem category = (RepositoryItem)pRequest.getObjectParameter(CricketCommonConstants.CHILD_CATEGORY);
		String dimUrl = pRequest.getParameter(CricketCommonConstants.DIM_URL);
		StringBuffer seoUrl = new StringBuffer();
		String seoPart = CricketCommonConstants.EMPTY_STRING;
		try {
			if(category != null && category.getItemDescriptor().hasProperty(seoPropertyName)) {
				String seoString = (String)category.getPropertyValue(seoPropertyName);
				if(dimUrl != null && !dimUrl.isEmpty()) {
					if (seoString != null && !seoString.isEmpty()) {
						String[] pair = dimUrl.split(CricketCommonConstants.DIM_URL_SPLIT);
						seoUrl.append(pair[0]);
						seoUrl.append(CricketCommonConstants.FORWARD_SLASH);
						seoUrl.append(seoString);
						seoUrl.append(CricketCommonConstants.Q_MARK);
						seoUrl.append(pair[1]);
						seoPart = seoUrl.toString();
						if(isLoggingDebug()) {
							logDebug("seo string formed is:" + seoPart);
						}
						//seoUrl = pair[0] + "/" + seoString + "?" + pair[1];
					} else {
						seoPart = dimUrl;
					}
				}
			} else {
				seoPart = dimUrl;
			}
			pRequest.setParameter(CricketCommonConstants.CATLINK_SEO_URL, seoPart);
			pRequest.serviceParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
		} catch (RepositoryException e) {
			logError(e);
		}
		if(isLoggingDebug()) {
			logDebug("exiting CreateCategoryLinkSeoUrlDroplet service method");
		}
	}


	public String getSeoPropertyName() {
		return seoPropertyName;
	}


	public void setSeoPropertyName(String seoPropertyName) {
		this.seoPropertyName = seoPropertyName;
	}


	public CricketConfiguration getCricketConfiguration() {
		return cricketConfiguration;
	}


	public void setCricketConfiguration(CricketConfiguration cricketConfiguration) {
		this.cricketConfiguration = cricketConfiguration;
	}

}
