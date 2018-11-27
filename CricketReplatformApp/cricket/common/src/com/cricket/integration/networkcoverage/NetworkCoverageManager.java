package com.cricket.integration.networkcoverage;

import atg.nucleus.GenericService;

import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.vo.InquireCoverageResponseVO;
import com.cricket.integration.maxmind.CityVO;

/**
 * @author sp112587
 *
 */
public class NetworkCoverageManager extends GenericService{
	
	//property : mNetworkCoverageTools
	NetworkCoverageTools networkCoverageTools;
	
	/**
	 * @return the networkCoverageTools
	 */
	public NetworkCoverageTools getNetworkCoverageTools() {
		return networkCoverageTools;
	}

	/**
	 * @param networkCoverageTools the networkCoverageTools to set
	 */
	public void setNetworkCoverageTools(NetworkCoverageTools networkCoverageTools) {
		this.networkCoverageTools = networkCoverageTools;
	}

	/**
	 * The method is used to get the coverage details based on the city info of the current user 
	 * @param pRequest - CityVO
	 * @return InquireCoverageResponseVO
	 * */
	public InquireCoverageResponseVO getCoverageDetails(CityVO cityVO) throws CricketException{
		
		InquireCoverageResponseVO responseVO = getNetworkCoverageTools().getCoverageInfo(cityVO);
		return responseVO;
		
	}
}
