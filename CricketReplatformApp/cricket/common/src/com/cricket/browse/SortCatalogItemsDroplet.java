package com.cricket.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;
import com.endeca.infront.shaded.org.apache.commons.lang.StringUtils;

import atg.adapter.gsa.ChangeAwareSet;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

/**
 * @author sm111226
 * 
 */
public class SortCatalogItemsDroplet extends DynamoServlet {

	/**
	 * @param pRequest
	 * @param pResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings(CricketCommonConstants.UNCHECKED)
	public void service(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException {
		Object catItems = pRequest.getObjectParameter(CricketCommonConstants.CATALOG_ITEMS);
		ChangeAwareSet catalogItemsSet;
		List<RepositoryItem> catalogItems = new ArrayList<RepositoryItem>();
		if (catItems != null && catItems.getClass() == ChangeAwareSet.class) {
			catalogItemsSet = (ChangeAwareSet) pRequest
					.getObjectParameter(CricketCommonConstants.CATALOG_ITEMS);
			for (Object catItem : catalogItemsSet) {
				catalogItems.add((RepositoryItem) catItem);
			}
		} else {
			catalogItems = (List<RepositoryItem>) pRequest
					.getObjectParameter(CricketCommonConstants.CATALOG_ITEMS);
		}
		List<RepositoryItem> sortedCatalogItems = new ArrayList<RepositoryItem>();
		List<RepositoryItem> sortedCatalogItemsWithoutPriority = new ArrayList<RepositoryItem>();
		String itemType = null;
		Integer itemDispPriority = null;
		List<Integer> sortedDisplayPriorities = new ArrayList<Integer>();
		try {
			if(catalogItems != null && catalogItems.size() > 0){
				for (RepositoryItem catalogItem : catalogItems) {
					itemType = catalogItem.getItemDescriptor()
							.getItemDescriptorName();
					if ((StringUtils.equalsIgnoreCase(itemType, CricketCommonConstants.CATEGORY)
							|| StringUtils
									.equalsIgnoreCase(itemType, CricketCommonConstants.PHONE_PRODUCT) || StringUtils
								.equalsIgnoreCase(itemType, CricketCommonConstants.PLAN_PRODUCT))
							&& catalogItem.getPropertyValue(CricketCommonConstants.DISPLAY_PRIORITY) != null) {
						sortedDisplayPriorities.add((Integer) catalogItem
								.getPropertyValue(CricketCommonConstants.DISPLAY_PRIORITY));
					} else if ((StringUtils.equalsIgnoreCase(itemType, CricketCommonConstants.CATEGORY)
							|| StringUtils
									.equalsIgnoreCase(itemType, CricketCommonConstants.PHONE_PRODUCT) || StringUtils
								.equalsIgnoreCase(itemType, CricketCommonConstants.PLAN_PRODUCT))
							&& catalogItem.getPropertyValue(CricketCommonConstants.DISPLAY_PRIORITY) == null) {
						sortedCatalogItemsWithoutPriority.add(catalogItem);
					}
				}
			}
			if (sortedDisplayPriorities.size() > 0) {
				Collections.sort(sortedDisplayPriorities);
				for (Integer displayPriority : sortedDisplayPriorities) {
					if(catalogItems != null && catalogItems.size() > 0){
						for (RepositoryItem catalogItem : catalogItems) {
							itemDispPriority = (Integer) catalogItem
									.getPropertyValue(CricketCommonConstants.DISPLAY_PRIORITY);
							if ((itemDispPriority != null)
									&& (displayPriority == itemDispPriority)) {
								sortedCatalogItems.add(catalogItem);
							}
						}
					}
				}
			}
			if (sortedCatalogItemsWithoutPriority.size() > 0) {
				sortedCatalogItems.addAll(sortedCatalogItemsWithoutPriority);
			}

		} catch (RepositoryException e) {
			vlogError("Repository Exception occurred in SortCatalogItemsDroplet", e);
		}

		pRequest.setParameter(CricketCommonConstants.SORTED_CATALOG_ITEMS, sortedCatalogItems);
		pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
	}

}
