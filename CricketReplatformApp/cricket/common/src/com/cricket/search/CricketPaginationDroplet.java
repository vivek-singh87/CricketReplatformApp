package com.cricket.search;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.search.records.PaginationVO;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class CricketPaginationDroplet extends DynamoServlet {
	
	private static final String TOTAL_RECORDS = "totalRecords";
	
	private static final String RECORDS_PER_PAGE = "recordsPerPage";
	
	private static final String START = "start";
			
	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		int totalRecords = 0;
		int start = 0;
		int recordsPerPage = 0;
		if(pReq.getObjectParameter(TOTAL_RECORDS) != null && pReq.getObjectParameter(TOTAL_RECORDS) instanceof Long) {
			Long totalRecordsLong = (Long)pReq.getObjectParameter(TOTAL_RECORDS);
			if(totalRecordsLong != null) {
				totalRecords = totalRecordsLong.intValue();
			}
		} else {
			totalRecords = (Integer)pReq.getObjectParameter(TOTAL_RECORDS);
		}
		if(pReq.getObjectParameter(RECORDS_PER_PAGE) != null && pReq.getObjectParameter(RECORDS_PER_PAGE) instanceof Long) {
			Long recordsPerPageLong = (Long)pReq.getObjectParameter(RECORDS_PER_PAGE);
			if(recordsPerPageLong != null) {
				recordsPerPage = recordsPerPageLong.intValue();
			}
		} else {
			recordsPerPage = (Integer)pReq.getObjectParameter(RECORDS_PER_PAGE);
		}
		if(pReq.getObjectParameter(START) != null && pReq.getObjectParameter(START) instanceof Long) {
			Long startLong = (Long)pReq.getObjectParameter(START);
			if(startLong != null) {
				start = startLong.intValue();
			}
		} else if (pReq.getObjectParameter(START) instanceof String) {
			String startString = (String)pReq.getObjectParameter(START);
			start = Integer.parseInt(startString);
		} else {
			start = (Integer)pReq.getObjectParameter(START);
		}
		int noOfPages;
		PaginationVO previousVO = new PaginationVO();
		PaginationVO nextVO = new PaginationVO();
		if (recordsPerPage < totalRecords) {
			if (totalRecords % recordsPerPage != 0) {
				noOfPages = totalRecords / recordsPerPage + 1;
			} else {
				noOfPages = totalRecords / recordsPerPage;
			}
		} else {
			noOfPages = 1;
		}
		PaginationVO[] paginationVOs = new PaginationVO[noOfPages];
		for(int i=0; i < noOfPages; i++) {
			Integer count = i + 1;
			String linkText = count.toString();
			int startValue = recordsPerPage*i;
			PaginationVO pageVO = new PaginationVO();
			pageVO.setLinkText(linkText);
			pageVO.setStartValue(startValue);
			if(start == (recordsPerPage*i + 1)) {
				pageVO.setActive(true);
				if (i != 0) {
					Integer previousCount = count -1 ;
					int previousStartValue = recordsPerPage*(i - 1);
					previousVO.setLinkText(previousCount.toString());
					previousVO.setActive(true);
					previousVO.setStartValue(previousStartValue);
					
				}
				if (i != noOfPages - 1) {
					Integer nextCount = count + 1;
					int nextStartValue = recordsPerPage*(i + 1);
					nextVO.setLinkText(nextCount.toString());
					nextVO.setActive(true);
					nextVO.setStartValue(nextStartValue);
				}
			} else {
				pageVO.setActive(false);
			}
			paginationVOs[i] = pageVO;
		}
		pReq.setParameter(CricketCommonConstants.NEXT_VO, nextVO);
		pReq.setParameter(CricketCommonConstants.PREVIOUS_VO, previousVO);
		pReq.setParameter(CricketCommonConstants.TOTAL_PAGES, noOfPages);
		pReq.setParameter(CricketCommonConstants.PAGINATION_VOS, paginationVOs);
		try {
			pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
		} catch (ServletException e) {
			if(isLoggingError()) {
				logError("An error occurred while serviceing the request");
			}
			logError(e);
		} catch (IOException e) {
			if(isLoggingError()) {
				logError("An error occurred while serviceing the request"+e);
			}
		}
	}

}
