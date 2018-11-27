package com.cricket.browse.sort;

import java.util.List;
import java.util.Locale;

import atg.nucleus.GenericService;
import atg.projects.store.sort.PropertySorter;
import atg.service.util.SortArray;


public class BrandSorter extends GenericService implements PropertySorter {
	
	private String brandProperty;

	@Override
	public Object[] sort(List pItems, String pDirection, Locale pLocale) {
		if(pItems == null || pItems.size() == 0){
		      return new Object[0];
		}
		String sortDirection = DESCENDING_SYMBOL;
		if(pDirection != null && pDirection.equals(ASCENDING)) {
			sortDirection = ASCENDING_SYMBOL;
		}
		if(pLocale == null) {
		      pLocale = getNucleus().getDefaultLocale();
		}
		/*
	     * Sorting
	     */
		SortArray sortArray = new SortArray();      
	    sortArray.setSortDirections(sortDirection);
	    sortArray.setSortProperties(new String[] {getBrandProperty()});
	    sortArray.setLocale(pLocale);
	    sortArray.setInputArray(pItems.toArray());
	    return sortArray.getOutputArray();
	}

	public String getBrandProperty() {
		return brandProperty;
	}

	public void setBrandProperty(String brandProperty) {
		this.brandProperty = brandProperty;
	}

}
