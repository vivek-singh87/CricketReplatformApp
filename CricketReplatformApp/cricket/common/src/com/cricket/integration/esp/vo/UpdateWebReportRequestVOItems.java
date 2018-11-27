/**
 * 
 */
package com.cricket.integration.esp.vo;

/**
 * @author MM112358
 *
 */
public class UpdateWebReportRequestVOItems {

	 private java.lang.String mdn;

	 private  UpdateWebReportRequestVOItemsItem[] item;
	    
	    
	/**
	 * 
	 */
	public UpdateWebReportRequestVOItems() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param pMdn
	 * @param pItem
	 */
	public UpdateWebReportRequestVOItems(String pMdn,
			UpdateWebReportRequestVOItemsItem[] pItem) {
		super();
		mdn = pMdn;
		item = pItem;
	}


	/**
	 * @return the mdn
	 */
	public java.lang.String getMdn() {
		return mdn;
	}


	/**
	 * @param pMdn the mdn to set
	 */
	public void setMdn(java.lang.String pMdn) {
		mdn = pMdn;
	}


	/**
	 * @return the item
	 */
	public UpdateWebReportRequestVOItemsItem[] getItem() {
		return item;
	}


	/**
	 * @param pItem the item to set
	 */
	public void setItem(UpdateWebReportRequestVOItemsItem[] pItem) {
		item = pItem;
	}
	
	public UpdateWebReportRequestVOItemsItem getItem(int i) {
	        return this.item[i];
	    }

	public void setItem(int i, UpdateWebReportRequestVOItemsItem value) {
	        this.item[i] = value;
	    }
}
