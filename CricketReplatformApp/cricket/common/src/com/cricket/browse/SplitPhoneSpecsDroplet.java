package com.cricket.browse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;











import atg.core.util.StringUtils;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;










import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.InquireFeaturesRequestVO;
import com.cricket.integration.esp.vo.InquireFeaturesResponseVO;

 
public class SplitPhoneSpecsDroplet extends DynamoServlet{
	
	
	@SuppressWarnings("unchecked")
	public void service(DynamoHttpServletRequest pRequest,DynamoHttpServletResponse pResponse) 
			throws ServletException,IOException {
				
			List<RepositoryItem> phoneSpecsLeft = new ArrayList<RepositoryItem>();
			List<RepositoryItem> phoneSpecsRight = new ArrayList<RepositoryItem>();
			List<RepositoryItem> phoneSpecs = (List<RepositoryItem>)pRequest.getObjectParameter(CricketCommonConstants.PHONE_SPECS);
			int leftHalfSize = 0;
			if(phoneSpecs != null && phoneSpecs.size() > 0) {
				leftHalfSize = ((Double)Math.ceil((double)phoneSpecs.size() / 2)).intValue();
				for(int index = 0; index < phoneSpecs.size() ; index++) {
					if(index < leftHalfSize) {
						phoneSpecsLeft.add(phoneSpecs.get(index));
					} else {
						phoneSpecsRight.add(phoneSpecs.get(index));
					}
				} 
			}
    	   	pRequest.setParameter(CricketCommonConstants.PHONE_SPECS_LEFT, phoneSpecsLeft);
	    	pRequest.setParameter(CricketCommonConstants.PHONE_SPECS_RIGHT, phoneSpecsRight);	    	
			pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
			
			
	}

}
