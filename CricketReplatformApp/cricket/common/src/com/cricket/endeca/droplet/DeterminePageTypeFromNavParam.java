package com.cricket.endeca.droplet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;

import atg.commerce.endeca.cache.DimensionValueCacheObject;
import atg.commerce.endeca.cache.DimensionValueCacheTools;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class DeterminePageTypeFromNavParam extends DynamoServlet{
	
	private DimensionValueCacheTools dimensionValueCacheTools = null;
	
	private Repository productCatalog;
	
	private Map <String, String> catePageToSearchPageMap;
	
	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		String navParam = pReq.getParameter(CricketCommonConstants.NAV_STATE);
		String searchPage = pReq.getParameter(CricketCommonConstants.SEARCH_PAGE);
		String navigationFilterId = null;
		String[] navigationFilterArray = null;
		String categoryId = null;
		boolean isCategoryPage = false;
		if (navParam != null) {
			navigationFilterArray = navParam.split(" ");
			DimensionValueCacheObject cacheObject = null;
			for(int i=0;i<navigationFilterArray.length;i++) {
				navigationFilterId = navigationFilterArray[i];
				cacheObject = getDimensionValueCacheTools().getCachedObjectForDimval(navigationFilterId);
				if (cacheObject != null) {
					categoryId = cacheObject.getRepositoryId();
					isCategoryPage = true;
					break;
				}
			}
		}
		try {
			if (isCategoryPage) {
				String templateUrl = CricketCommonConstants.EMPTY_STRING;
				String correspondingSearchPageUrl = CricketCommonConstants.EMPTY_STRING;
				if (categoryId != null && !categoryId.isEmpty()) {
					RepositoryItem categoryItem =  getProductCatalog().getItem(categoryId, CricketCommonConstants.CATEGORY);
					RepositoryItem template = null;
					
					if (categoryItem != null) {
						template = (RepositoryItem)categoryItem.getPropertyValue(CricketCommonConstants.TEMPLATE);
						if(template != null) {
							templateUrl = (String)template.getPropertyValue(CricketCommonConstants.URL);
							correspondingSearchPageUrl = catePageToSearchPageMap.get(templateUrl);
						}
					}
				}
				if (searchPage != null && searchPage.equals(CricketCommonConstants.SEARCH_PAGE)) {
					if (isLoggingDebug()) {
						logDebug("category type search page");
					}
					pReq.setParameter(CricketCommonConstants.CATEGORY_TYPE, CricketCommonConstants.CATEGORY_TYPE);
					pReq.serviceParameter(CricketCommonConstants.SEARCH_LIST, pReq, pRes);
				} else {
					if (isLoggingDebug()) {
						logDebug("category page");
					}
					pReq.setParameter(CricketCommonConstants.CORRESPONDING_SEARCH_PAGE_URL, correspondingSearchPageUrl);
					pReq.setParameter(CricketCommonConstants.TEMPLATE_URL, templateUrl);
					pReq.serviceParameter(CricketCommonConstants.CATEGORY_LIST, pReq, pRes);
				}
			} else {
				if (isLoggingDebug()) {
					logDebug("all search results search page");
				}
				pReq.setParameter(CricketCommonConstants.CATEGORY_TYPE, "allSearch");
				pReq.serviceParameter("searchList", pReq, pRes);
			}
		} catch (IOException e) {
			if (isLoggingError()) {
				logError("There was an IOException while servicing the parameter");
			}
		} catch (ServletException e) {
			if (isLoggingError()) {
				logError("There was an ServletException while servicing the parameter");
			}
		} catch (RepositoryException e) {
			if (isLoggingError()) {
				logError("There was an RepositoryException while getting category for id: " + categoryId );
			}
		}
		
	}

	public DimensionValueCacheTools getDimensionValueCacheTools() {
		return dimensionValueCacheTools;
	}

	public void setDimensionValueCacheTools(DimensionValueCacheTools dimensionValueCacheTools) {
		this.dimensionValueCacheTools = dimensionValueCacheTools;
	}

	public Repository getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(Repository productCatalog) {
		this.productCatalog = productCatalog;
	}

	public Map <String, String> getCatePageToSearchPageMap() {
		return catePageToSearchPageMap;
	}

	public void setCatePageToSearchPageMap(Map <String, String> catePageToSearchPageMap) {
		this.catePageToSearchPageMap = catePageToSearchPageMap;
	}
}
