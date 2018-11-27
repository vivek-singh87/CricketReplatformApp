package com.cricket.browse.formhandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.user.session.UserSessionBean;

import atg.droplet.GenericFormHandler;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

public class AddCompareProdToSession extends GenericFormHandler {
	
	private UserSessionBean userSessionBean;

	private String productId;
	
	private String action;
	
	private String liquidPixelUrl;
	
	private String successUrl;
	
	private String failureURL;
	
	public void handleAddToSession (DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) throws IOException {
		if (isLoggingDebug()) {
			logDebug("Entering handleAddToSession method");
		}
		if(userSessionBean != null) {
			if(action != null && action.equals(CricketCommonConstants.REMOVE_ALL)) {
				List<String> emptyList = new ArrayList<String>();
				userSessionBean.setComapreImageUrls(emptyList);
				userSessionBean.setCompareProductIDs(CricketCommonConstants.EMPTY_STRING);
				userSessionBean.setNumberOfItemsInCompare(0);
			} else {
				List<String> liquidPixelUrls = userSessionBean.getComapreImageUrls();
				String comaSepProdIds = userSessionBean.getCompareProductIDs();
				int productCount = userSessionBean.getNumberOfItemsInCompare();
				String comaSepProdIdsNew = CricketCommonConstants.EMPTY_STRING;
				List<String> liquidPixelUrlsNew = new ArrayList<String>();
				if (action != null && action.equals(CricketCommonConstants.REMOVE)) {
					productCount--;
					boolean idIsThere = false;
					boolean entryRemoved = false;
					if(comaSepProdIds != null && comaSepProdIds.contains(productId)) {
						idIsThere = true;
						String[] prodIdArray = comaSepProdIds.split(",");
						for (String prodId : prodIdArray) {
							if(!prodId.equals(productId)) {
								if (comaSepProdIdsNew.equals(CricketCommonConstants.EMPTY_STRING)) {
									comaSepProdIdsNew = prodId;
								} else {
									comaSepProdIdsNew = comaSepProdIdsNew + "," + prodId;
								}
							}
						}
					}
					if (liquidPixelUrls != null) {
						for(String lpUrl : liquidPixelUrls) {
							if(!lpUrl.contains(productId)) {
								liquidPixelUrlsNew.add(lpUrl);
							} else if (idIsThere) {
								entryRemoved = true;
							}
						}
					}
					if (entryRemoved) {
						userSessionBean.setComapreImageUrls(liquidPixelUrlsNew);
						userSessionBean.setCompareProductIDs(comaSepProdIdsNew);
						userSessionBean.setNumberOfItemsInCompare(productCount);
					}
				} else {
					
					productCount++;
					if(comaSepProdIds == null) {
						comaSepProdIds = CricketCommonConstants.EMPTY_STRING;
					}
					if(liquidPixelUrls == null) {
						liquidPixelUrls = new ArrayList<String>();
					}
					liquidPixelUrls.add(liquidPixelUrl + CricketCommonConstants.PIPE + productId);
					if(comaSepProdIds.isEmpty()) {
						comaSepProdIds = productId;
					} else {
						comaSepProdIds = comaSepProdIds + CricketCommonConstants.COMMA_SEPARATOR + productId;
					}
					if(liquidPixelUrl != null && !liquidPixelUrl.isEmpty() && productId != null && !productId.isEmpty()) {
						userSessionBean.setComapreImageUrls(liquidPixelUrls);
						userSessionBean.setCompareProductIDs(comaSepProdIds);
						userSessionBean.setNumberOfItemsInCompare(productCount);
					}
				}
			}
		}
		
		if (isLoggingDebug()) {
			logDebug("Exiting handleAddToSession method");
		}
		try {
			checkFormRedirect(successUrl, failureURL, pReq, pRes);
		} catch (ServletException e) {
			logError(e);
		}
	}
	
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}

	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getLiquidPixelUrl() {
		return liquidPixelUrl;
	}

	public void setLiquidPixelUrl(String liquidPixelUrl) {
		this.liquidPixelUrl = liquidPixelUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getFailureURL() {
		return failureURL;
	}

	public void setFailureURL(String failureURL) {
		this.failureURL = failureURL;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
