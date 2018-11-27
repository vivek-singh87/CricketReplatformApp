package com.cricket.browse.droplet;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class DisplayNameDroplet extends DynamoServlet {
	
	private String copyRight;
	
	private String registeredMark;
	
	private String firstString = null;

	private String secondString = null;
	
	private String specialSymbol = null;
	
	private boolean hasSpecialSymbol = false; 
	

	public void service(DynamoHttpServletRequest pRequest,DynamoHttpServletResponse pResponse) {
		
		String productName = pRequest.getParameter(CricketCommonConstants.PROP_DISP_NAME);
		
		try {
			if(productName.contains(getRegisteredMark())){
				
				checkSpecialSymbol(getRegisteredMark(),productName);
			
				pRequest.setParameter(CricketCommonConstants.HAS_SPC_SYM, true);
				pRequest.setParameter(CricketCommonConstants.FIRST_STRING, getFirstString());
				pRequest.setParameter(CricketCommonConstants.SECOND_STRING, getSecondString());
				pRequest.setParameter(CricketCommonConstants.SPC_SYMBOL, getSpecialSymbol());
				pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
				
			} else {				
				pRequest.setParameter("hasSpecialSymbol", false);
				pRequest.setParameter(CricketCommonConstants.PROP_DISP_NAME, productName);
				pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
			}
		} catch (ServletException servletException) {
			logError("Error:" + servletException);
			logDebug("DisplayNameDroplet for ServeltException" + servletException);
		} catch (IOException ioException) {
			logError("Error:" + ioException);
			logDebug("DisplayNameDroplet for ServeltException" + ioException);
		} catch (Exception exception) {
			logError("Error:" + exception);
			logDebug("DisplayNameDroplet for ServeltException" + exception);
		}
		
	}
	
	
	public void checkSpecialSymbol(String symbol, String productName){
		
		String[] result = productName.split(symbol);
		try {
			if(result.length>1 && result.length<3){			
				setFirstString(result[0]);	
				setSecondString(result[1]);	
				setSpecialSymbol(symbol);
			}
		} catch (Exception exception) {
			logError("checkSpecialSymbol in DisplayNameDroplet Error:" + exception);
			logDebug("checkSpecialSymbol in DisplayNameDroplet for Exception:" + exception);
		} 
	}
	
	
	public boolean isHasSpecialSymbol() {
		return hasSpecialSymbol;
	}


	public void setHasSpecialSymbol(boolean pHasSpecialSymbol) {
		hasSpecialSymbol = pHasSpecialSymbol;
	}


	public String getSpecialSymbol() {
		return specialSymbol;
	}


	public void setSpecialSymbol(String pSpecialSymbol) {
		specialSymbol = pSpecialSymbol;
	}

	public String getFirstString() {
		return firstString;
	}


	public void setFirstString(String pFirstString) {
		firstString = pFirstString;
	}


	public String getSecondString() {
		return secondString;
	}


	public void setSecondString(String pSecondString) {
		secondString = pSecondString;
	}


	public String getCopyRight() {
		return copyRight;
	}

	public void setCopyRight(String pCopyRight) {
		copyRight = pCopyRight;
	}

	public String getRegisteredMark() {
		return registeredMark;
	}

	public void setRegisteredMark(String pRegisteredMark) {
		registeredMark = pRegisteredMark;
	}

}
