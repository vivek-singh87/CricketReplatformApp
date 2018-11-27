package com.cricket.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.search.records.SupportVO;
import com.endeca.navigation.AssocDimLocations;
import com.endeca.navigation.AssocDimLocationsList;
import com.endeca.navigation.DimLocation;
import com.endeca.navigation.DimVal;
import com.endeca.navigation.DimValIdList;
import com.endeca.navigation.ENEConnection;
import com.endeca.navigation.ENEQuery;
import com.endeca.navigation.ENEQueryException;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.ERec;
import com.endeca.navigation.ERecList;
import com.endeca.navigation.ERecSearch;
import com.endeca.navigation.ERecSearchList;
import com.endeca.navigation.HttpENEConnection;
import com.endeca.navigation.Navigation;
import com.endeca.navigation.PropertyMap;
import com.endeca.navigation.UrlENEQueryParseException;

public class GetSupportResultsFromKeyWord extends DynamoServlet {
	
	private String eneHost;
	private Integer enePort;
	
	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		if(isLoggingDebug()) {
			logDebug("entering GetSupportResultsFromKeyWord service method");
		}
		String keyWordQuery = pReq.getParameter("keyWordQuery");
		ENEConnection nec = new HttpENEConnection(eneHost, enePort);
		List<SupportVO> supportVOs = new ArrayList<SupportVO>();
		ENEQueryResults qr = null;
		try {
		qr = nec.query(createKeywordQuery(keyWordQuery));
		} catch (UrlENEQueryParseException e) {
			logError(e);
		} catch (ENEQueryException e) {
			logError(e);
		}
		if(qr != null) {
			Navigation nav = qr.getNavigation();
			ERecList records = nav.getERecs();
			ListIterator i = records.listIterator();
			while (i.hasNext()) {
				ERec record = (ERec)i.next();
				PropertyMap recordProperties = record.getProperties();
				String faqId = (String) recordProperties.get("faq.id");
				if(faqId != null && !faqId.isEmpty()) {
					if(isLoggingDebug()) {
						logDebug("support content is there for this search query");
					}
					AssocDimLocationsList dims = (AssocDimLocationsList)record.getDimValues();
					String faqTitle = null;
					String faqQuestion = null;
					String faqAnswer = null;
					String faqLink = null;
					for (int j=0; j < dims.size(); j++) {
						AssocDimLocations dim = (AssocDimLocations)dims.get(j);
						for (int k=0; k < dim.size(); k++) {
							DimLocation dimLoc = (DimLocation)dim.get(k);
							DimVal dval = dimLoc.getDimValue();
							String dimensionName = dval.getDimensionName();
							String dimValName = dval.getName();
							if(dimensionName.equals("faq.SubCategory")) {
								faqTitle = dimValName;
							}
							if(dimensionName.equals("faq.question")) {
								faqQuestion = dimValName;
							}
						}
					}
					if (faqTitle == null || faqTitle.isEmpty()) {
						faqTitle = (String) recordProperties.get("faq.question");
					}
					if (faqQuestion == null || faqQuestion.isEmpty()) {
						faqQuestion = (String) recordProperties.get("faq.question");
					}
					faqLink = (String) recordProperties.get("faq.seo_name");
					faqAnswer = (String) recordProperties.get("faq.answer");
	 				SupportVO supportVO = new SupportVO();
	 				supportVO.setFaqTitle(faqTitle);
	 				supportVO.setFaqQuestion(faqQuestion);
	 				supportVO.setFaqAnswer(faqAnswer);
	 				supportVO.setFaqLink(faqLink);
	 				supportVOs.add(supportVO);
	 				
				}
			}
			if(isLoggingDebug()) {
				logDebug("entering GetSupportResultsFromKeyWord service method");
			}
		}
		if(isLoggingDebug()) {
			logDebug("support content is there for this search query");
		}
		pReq.setParameter("supportResultVOs", supportVOs);
		try {
			pReq.serviceParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
		} catch (ServletException e) {
			if(isLoggingError()) {
				logError("error occured while trying to service the content"+e);
			}
		} catch (IOException e) {
			if(isLoggingError()) {
				logError("error occured while trying to service the content"+e);
			}
		}
	}
	
	@SuppressWarnings("unchecked") 
    private static ENEQuery createKeywordQuery(final String searchTerm) { 
        final ENEQuery query = new ENEQuery(); 
        final DimValIdList dimValIdList = new DimValIdList("0"); 
        query.setNavDescriptors(dimValIdList); 
        final ERecSearchList searches = new ERecSearchList(); 
        final ERecSearch eRecSearch = new ERecSearch("All", searchTerm); 
        searches.add(eRecSearch); 
        query.setNavERecSearches(searches); 
        query.setNavNumERecs(1000);
        return query; 
        
    } 


	public String getEneHost() {
		return eneHost;
	}

	public void setEneHost(String eneHost) {
		this.eneHost = eneHost;
	}

	public Integer getEnePort() {
		return enePort;
	}

	public void setEnePort(Integer enePort) {
		this.enePort = enePort;
	}

}
