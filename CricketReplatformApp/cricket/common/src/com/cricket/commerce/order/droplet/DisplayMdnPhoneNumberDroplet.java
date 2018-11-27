package com.cricket.commerce.order.droplet;

import java.io.IOException;

import javax.servlet.ServletException;

import atg.core.util.StringUtils;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;

public class DisplayMdnPhoneNumberDroplet extends DynamoServlet {
	
	 /** The oparam name rendered once during processing.*/
	  public static final String OPARAM_OUTPUT = "output";
	  
	  /** The oparam name rendered once during processing.*/
	  public static final String OPARAM_EMPTY = "empty";
	  
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		String mdnNumber = (String) pRequest.getParameter("profileMdn");
		if (StringUtils.isEmpty(mdnNumber) || mdnNumber.length() < 10) {
			pRequest.serviceLocalParameter(OPARAM_EMPTY, pRequest, pResponse);
		} else {
			String formatedMdnNumber = CricketCommonConstants.EMPTY_STRING;
			formatedMdnNumber = mdnNumber.substring(0, 3) + "-" + mdnNumber.substring(3, 6)
					+ "-" + mdnNumber.substring(6, (mdnNumber.length()));
			pRequest.setParameter("formatedMdnNumber", formatedMdnNumber);
			pRequest.serviceLocalParameter(OPARAM_OUTPUT, pRequest, pResponse);
		}
	}

	
	
	
}
