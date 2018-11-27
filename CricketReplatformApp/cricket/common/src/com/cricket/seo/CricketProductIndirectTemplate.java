package com.cricket.seo;

import java.util.List;
import java.util.regex.Matcher;

import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.seo.IndirectUrlTemplate;
import atg.repository.seo.ItemLinkException;
import atg.service.webappregistry.WebApp;
import atg.servlet.DynamoHttpServletRequest;

public class CricketProductIndirectTemplate extends IndirectUrlTemplate {
	
	Repository productCatlog;
	
	public String getForwardUrl(DynamoHttpServletRequest pRequest, String pIndirectUrl, WebApp pDefaultWebApp, Repository pDefaultRepository)
		    throws ItemLinkException {
		if(isLoggingDebug()){
			logDebug("Entering getForwardUrl");
		}
		String forwardUrlOrg = null;
		String forwardUrl = null;
		StringBuffer forwardUrlBuff = new StringBuffer();
		forwardUrlOrg = super.getForwardUrl(pRequest, pIndirectUrl, pDefaultWebApp, pDefaultRepository);
		if(forwardUrlOrg != null) {
			forwardUrlBuff.append(forwardUrlOrg);
			String productId = forwardUrlOrg.substring(forwardUrlOrg.lastIndexOf("productId=") + 10);
			RepositoryItem product = null;
			if(isLoggingDebug()){
				logDebug("default forward url template :: " + forwardUrl);
			}
			try {
				if(forwardUrlOrg != null && productId != null) {
					product = getProductCatlog().getItem(productId, CricketCommonConstants.PRODUCT);
					if(product != null) {
						RepositoryItem category = (RepositoryItem)product.getPropertyValue("parentCategory");
						if (category != null) {
							String categoryId = (String)product.getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
							forwardUrlBuff.append("&productCategoryId=");
							forwardUrlBuff.append(categoryId);
						}
					}
					
				}
				if(pIndirectUrl.contains("cell-phones") || pIndirectUrl.contains("cell-phone-accessories")) {
					if(product != null) {
						List<RepositoryItem> childSKUs = (List<RepositoryItem>)product.getPropertyValue(CricketCommonConstants.PROP_CHILD_SKUS);
						if(childSKUs != null && childSKUs.size() > 0) {
							String skuId = (String)childSKUs.get(0).getPropertyValue(CricketCommonConstants.REPOSITORY_ID);
							forwardUrlBuff.append("&skuId=");
							forwardUrlBuff.append(skuId);
							forwardUrl = forwardUrlBuff.toString();
						}
					}
				}	
				forwardUrl = forwardUrlBuff.toString();
			} catch (RepositoryException re) {
				logError("Error while getting the phone with phone id :: " + productId);
			}
		}
		return forwardUrl;
	}
	
	public Matcher matches(String pUrl) {
	    Matcher matcher = null;

	    matcher = getIndirectRegexPattern().matcher(pUrl);

	    if (!matcher.matches())
	    {
	      matcher = null;
	    }

	    return matcher;
	  }

	public Repository getProductCatlog() {
		return productCatlog;
	}

	public void setProductCatlog(Repository productCatlog) {
		this.productCatlog = productCatlog;
	}

}
