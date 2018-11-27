/**
 * 
 */
package com.cricket.myaccount;

import atg.commerce.order.OrderHolder;
import atg.projects.store.profile.StoreProfileTools;

/**
 * @author ma112576
 *
 */
public class CricketProfileTools extends StoreProfileTools {

	private String domainName;
	@Override
	public boolean shouldLoadShoppingCarts(OrderHolder pShoppingCart) {
	    return Boolean.FALSE;
	  }
	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return domainName;
	}
	
	/**
	 * @param domainName the domainName to set
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
}
