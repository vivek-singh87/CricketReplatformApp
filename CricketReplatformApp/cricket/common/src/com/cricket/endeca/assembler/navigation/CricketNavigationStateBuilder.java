package com.cricket.endeca.assembler.navigation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.endeca.infront.navigation.NavigationException;
import com.endeca.soleng.urlformatter.UrlState;

import atg.core.util.StringUtils;
import atg.endeca.assembler.AssemblerTools;
import atg.endeca.assembler.navigation.ExtendedNavigationStateBuilder;
import atg.endeca.assembler.navigation.filter.RecordFilterBuilder;
import atg.endeca.assembler.navigation.filter.UrlStateAwareRecordFilterBuilder;
import atg.nucleus.logging.VariableArgumentApplicationLogging;

public class CricketNavigationStateBuilder extends ExtendedNavigationStateBuilder {
	
	protected List<String> parseRecordFilters(UrlState pUrlState) throws NavigationException {
		Set resultSet = new HashSet();
	    if (getNonSecurityFilterBuilders() != null) {
	    	for (RecordFilterBuilder recordFilterBuilder : getNonSecurityFilterBuilders()) {
	    		String recordFilter = null;
	    		if ((recordFilterBuilder instanceof UrlStateAwareRecordFilterBuilder)) {
	    			recordFilter = ((UrlStateAwareRecordFilterBuilder)recordFilterBuilder).buildRecordFilter(pUrlState);
	    		}
	    		else {
	    			recordFilter = recordFilterBuilder.buildRecordFilter();
	    		}
	    		if (!StringUtils.isBlank(recordFilter)) {
	    			resultSet.add(recordFilter);
	    		}
	    	}
	    }
	    VariableArgumentApplicationLogging logger = AssemblerTools.getApplicationLogging();
	    if (logger.isLoggingDebug()) {
	    	logger.logDebug("Record filters: " + resultSet);
	    }
	    return new ArrayList(resultSet);
	}
}
