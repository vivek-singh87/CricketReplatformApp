package com.cricket.integration.networkcoverage;

import atg.nucleus.GenericService;

import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.AddressInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.AddressZipInfo;
import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.InquireCoverageResponseVO;
import com.cricket.integration.maxmind.CityVO;
import com.cricket.integration.esp.vo.*;

public class NetworkCoverageTools extends GenericService{

	
	private CricketESPAdapter cricketESPAdapter;

	/**
	 * @return the cricketESPAdapter
	 */
	public CricketESPAdapter getCricketESPAdapter() {
		return cricketESPAdapter;
	}

	/**
	 * @param cricketESPAdapter the cricketESPAdapter to set
	 */
	public void setCricketESPAdapter(CricketESPAdapter cricketESPAdapter) {
		this.cricketESPAdapter = cricketESPAdapter;
	}
	
	/**
	 * The method is used to get the coverage details based on the city info of the current user 
	 * @param pRequest - CityVO
	 * @return InquireCoverageResponseVO
	 * */
	public InquireCoverageResponseVO getCoverageInfo(CityVO cityVO) throws CricketException{
			InquireCoverageResponseVO responseVO;
		
			InquireCoverageRequestVO requestVO;
			AddressInfo address = new AddressInfo();
			// Getting the country code
			address.setCountry(cityVO.getCountryCode());

			AddressZipInfo zip = new AddressZipInfo();
			// Getting the zip code
			zip.setZipCode(cityVO.getPostalCode());

			address.setZip(zip);
			requestVO = new InquireCoverageRequestVO();
			requestVO.setAddress(address);

			// Make ESP call to get Network details.
			responseVO = getCricketESPAdapter().inquireCoverage(requestVO);
		
		return responseVO;
	}	
}
