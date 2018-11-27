package com.cricket.configuration;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;

public class TimeMonitoring extends DynamoServlet {
	
	private int debugLevel = 1;

	 

	/* (non-Javadoc)
	 * @see atg.servlet.DynamoServlet#service(atg.servlet.DynamoHttpServletRequest, atg.servlet.DynamoHttpServletResponse)
	 */
	@Override
	public void service(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		String pageType = pRequest.getParameter(CricketCommonConstants.CATEGORY_TYPE);
		String isEndTime = pRequest.getParameter(CricketCommonConstants.IS_END_TIME);
		String contentItemType = pRequest.getParameter(CricketCommonConstants.CONTENT_ITEM_TYPE);
		Date  requestTime = Calendar.getInstance().getTime();
		if(isEndTime.equalsIgnoreCase("false")) {
			if(isLoggingDebug() && debugLevel > 1) {
				logDebug("Entering the " + contentItemType + " section of " + pageType + " listing page request "  + pRequest.getRequestURL() + " with ID " + pRequest.getSession().getId() + " Time of entry : " + requestTime);
			}
			pRequest.setParameter("startTime", requestTime);
		}
		else {
			Date startTime = (Date)pRequest.getObjectParameter("startTime");
			long diffinMilleSeconds = requestTime.getTime() - startTime.getTime() ;
			if(isLoggingDebug()) {
				logDebug("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				logDebug("Exiting the " + contentItemType + " section of " + pageType + " listing page request "  + pRequest.getRequestURL() + " with ID " + pRequest.getSession().getId() + " Time of exit : " + requestTime);
				logDebug("Time taken for loading " + contentItemType + " section of " + pageType + " listing page request is::::::: " + diffinMilleSeconds + " MS"   );
				logDebug("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			}
		}
		pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
	}



	public int getDebugLevel() {
		return debugLevel;
	}



	public void setDebugLevel(int debugLevel) {
		this.debugLevel = debugLevel;
	}
}
