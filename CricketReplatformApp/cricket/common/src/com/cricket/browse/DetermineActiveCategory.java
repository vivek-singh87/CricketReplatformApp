package com.cricket.browse;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class DetermineActiveCategory extends DynamoServlet {
	
	private String noshowCatIds;

	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		String navParam = pReq.getParameter(CricketCommonConstants.NAV_PARAM);
		String cacheEntry = pReq.getParameter(CricketCommonConstants.CACHE_ENTRY);
		String categoryId = pReq.getParameter(CricketCommonConstants.CATEGORY_ID);
		boolean isActiveCategory = false;
		try {
			if (categoryId != null && noshowCatIds.contains(categoryId)) {
				pReq.serviceParameter(CricketCommonConstants.NO_SHOW, pReq, pRes);
			} else if (navParam != null && cacheEntry != null) {
				String[] navParamArray = navParam.split(" ");
				for (int i=0; i<navParamArray.length;i++) {
					if (cacheEntry.contains(navParamArray[i])) {
						isActiveCategory = true;
						break;
					}
				}
				if (isActiveCategory) {
					pReq.serviceParameter(CricketCommonConstants.ACTIVE, pReq, pRes);
				} else {
					pReq.serviceParameter(CricketCommonConstants.INACTIVE, pReq, pRes);
				}
				
			} else {
				pReq.serviceParameter(CricketCommonConstants.INACTIVE, pReq, pRes);
			}
		} catch (ServletException e) {
			if (isLoggingError()) {
				logError("there was an error while servicing the request :");
			}
		} catch (IOException e) {
			if (isLoggingError()) {
				logError("there was an error while servicing the request :");
			}
		}
	}
	
	public String getNoshowCatIds() {
		return noshowCatIds;
	}

	public void setNoshowCatIds(String noshowCatIds) {
		this.noshowCatIds = noshowCatIds;
	}
}