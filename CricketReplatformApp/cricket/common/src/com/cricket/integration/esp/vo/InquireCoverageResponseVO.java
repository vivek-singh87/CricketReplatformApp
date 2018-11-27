package com.cricket.integration.esp.vo;

import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.CoverageMarketInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ResponseInfo;

public class InquireCoverageResponseVO extends ESPRequestVO{

	/**
	 * Reference to the CoverageMarketInfo
	 */
	private CoverageMarketInfo mCoverageMarket;
	private ResponseInfo mResponse;
	/**
	 * @return the mResponse
	 */
	public ResponseInfo getResponse() {
		return mResponse;
	}
	/**
	 * @param pResponse the mResponse to set
	 */
	public void setResponse(ResponseInfo pResponse) {
		this.mResponse = pResponse;
	}
	/**
	 * @return the mCoverageMarket
	 */
	public CoverageMarketInfo getCoverageMarket() {
		return mCoverageMarket;
	}
	/**
	 * @param mCoverageMarket the mCoverageMarket to set
	 */
	public void setCoverageMarket(CoverageMarketInfo pCoverageMarket) {
		this.mCoverageMarket = pCoverageMarket;
	}
	
}
