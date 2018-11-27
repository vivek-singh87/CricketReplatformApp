package com.cricket.endeca.assembler;

import java.io.IOException;
import java.net.SocketException;

import javax.servlet.ServletException;

import com.endeca.infront.assembler.ContentItem;

import atg.endeca.assembler.AssemblerPipelineServlet;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

public class CricketAssemblerPipelineServlet extends AssemblerPipelineServlet {
	
	private String matchStringForSeoUrls = "jump";
	
	protected boolean shouldIgnoreRequest(DynamoHttpServletRequest pRequest) {
		if(pRequest.getRequestURI().contains(getMatchStringForSeoUrls())) {
			if (isLoggingDebug()) {
				logDebug("ignoring request as it is an seo url for details pages");
			}
			return true;
		} else {
			return super.shouldIgnoreRequest(pRequest);
		}
	}
	
	protected void forwardRequest(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse, ContentItem pContentItem) throws ServletException, IOException {
		try {
			super.forwardRequest(pRequest, pResponse, pContentItem);
		} catch (SocketException e) {
			if(isLoggingError()) {
				logError("Broken pipe:: Assembler took too much time to respond, socket was closed at one end before the request was completed");
			}
		}
	}

	public String getMatchStringForSeoUrls() {
		return matchStringForSeoUrls;
	}

	public void setMatchStringForSeoUrls(String matchStringForSeoUrls) {
		this.matchStringForSeoUrls = matchStringForSeoUrls;
	}

}
