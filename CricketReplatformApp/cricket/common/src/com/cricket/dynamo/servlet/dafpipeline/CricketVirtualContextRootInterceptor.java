package com.cricket.dynamo.servlet.dafpipeline;

import java.net.MalformedURLException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import atg.servlet.ContextRootSwappingService;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.VirtualContextRootInterceptor;

import com.cricket.common.constants.CricketCommonConstants;

public class CricketVirtualContextRootInterceptor extends VirtualContextRootInterceptor {
	
	private String matchContextPathWith;
	private String replaceMatchedWith;
	
	public boolean preInterceptRequest(DynamoHttpServletRequest pRequest, HttpServletResponse pResponse, FilterChain pChain) {
	    ServletContext localServletContext = pRequest.getSession().getServletContext();
	    String fullUrl = getFullUrlForRequest(pRequest);
	    ContextRootSwappingService.ServletContextInfo contextInfo = null;
		try {
			contextInfo = getOtherContext(localServletContext, fullUrl);
		} catch (MalformedURLException e1) {
			logError(e1);
		}
		if (contextInfo != null) {
			if(contextInfo.getOriginalContextRoot().equals(matchContextPathWith)) {
				pRequest.setContextPath(CricketCommonConstants.EMPTY_STRING);
			}
		}
		
		return super.preInterceptRequest(pRequest, pResponse, pChain);
	  }

	public String getMatchContextPathWith() {
		return matchContextPathWith;
	}

	public void setMatchContextPathWith(String matchContextPathWith) {
		this.matchContextPathWith = matchContextPathWith;
	}

	public String getReplaceMatchedWith() {
		return replaceMatchedWith;
	}

	public void setReplaceMatchedWith(String replaceMatchedWith) {
		this.replaceMatchedWith = replaceMatchedWith;
	}

}
