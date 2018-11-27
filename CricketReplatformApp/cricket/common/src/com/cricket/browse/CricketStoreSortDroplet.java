package com.cricket.browse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.droplet.Range;
import atg.nucleus.ServiceMap;
import atg.projects.store.sort.PropertySorter;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.RequestLocale;

	public class CricketStoreSortDroplet extends Range {
	
	 /* Input Parameters */
		private ServiceMap mSorters;
		public ServiceMap getSorters() {
			return mSorters;
		}
  
		public void setSorters(ServiceMap pSorters) {
			mSorters = pSorters;
		}
	
		public void service(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse) throws ServletException, IOException {
			  
		    /*
		     * Input validation
		     */
		    
		    // Input Parameter: array
		    Object items = pRequest.getObjectParameter(Range.ARRAY);
		    if(!(items instanceof List)){
		      super.service(pRequest, pResponse);
		      return;
		    }
		    
		    // Input Parameter: sortSelection
		    String sortSelection = pRequest.getParameter(CricketCommonConstants.SORT_SELECTION_PARAM);
		    if(!(sortSelection instanceof String)){
		      //defaultSortOrder(pRequest, pResponse);
		      super.service(pRequest, pResponse);
		      return;
		    }
		        
		    /*
		     * Parse input
		     */
		    String sortProperty = null;
		    String sortDirection = PropertySorter.DESCENDING; 
		    
		    String[] sortParams = sortSelection.split(CricketCommonConstants.SORT_PARAM_DELIMITER);
		    if(sortParams.length == 1){
		      sortProperty = sortParams[0];
		    }
		    else if(sortParams.length == 2){
		      sortProperty = sortParams[0];
		      sortDirection = sortParams[1];
		    }
		    else{
		      super.service(pRequest, pResponse);
		      return;
		    }
		        
		    /*
		     * Perform Sorting
		     */
		    Object sortedItems = items;
		    if(getSorters().containsKey(sortProperty)){
		    	Object sorter = getSorters().get(sortProperty);
		    	if(sorter instanceof PropertySorter) {
		    		Locale locale = null;
		    		if(pRequest != null){
		    			RequestLocale requestLocale = pRequest.getRequestLocale();
		    			if(requestLocale != null){
		    				locale = requestLocale.getLocale();
		    			} else {
		    				locale = pRequest.getLocale();
		    			}
		    		}
		    		sortedItems = ((PropertySorter) sorter).sort((List)items, sortDirection, locale);
		    	}
		    }
		    pRequest.setParameter(Range.ARRAY, sortedItems);
		    super.service(pRequest, pResponse);
		}
	}