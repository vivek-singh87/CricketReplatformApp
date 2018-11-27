/**
 * 
 */
package com.cricket.vo;

import atg.nucleus.GenericService;

/**
 * Component created as a VO.
 * 
 * @author AK112151
 *
 */
public class MyCricketCookieCartInfo extends GenericService {
	private String packageId;
	private String productType;
	private boolean editActionFromCart=false;
	private boolean addAddonWithoutPackageError=false;
	private String openCart;

	/**
	 * @return the packageId
	 */
	public String getPackageId() {
		return packageId;
	}

	/**
	 * @param packageId the packageId to set
	 */
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	/**
	 * @return the editActionFromCart
	 */
	public boolean isEditActionFromCart() {
		return editActionFromCart;
	}

	/**
	 * @param editActionFromCart the editActionFromCart to set
	 */
	public void setEditActionFromCart(boolean editActionFromCart) {
		this.editActionFromCart = editActionFromCart;
	}

	/**
	 * @return the openCart
	 */
	public String getOpenCart() {
		return openCart;
	}

	/**
	 * @param openCart the openCart to set
	 */
	public void setOpenCart(String openCart) {
		this.openCart = openCart;
	}

	/**
	 * @return the addAddonWithoutPackageError
	 */
	public boolean isAddAddonWithoutPackageError() {
		return addAddonWithoutPackageError;
	}

	/**
	 * @param addAddonWithoutPackageError the addAddonWithoutPackageError to set
	 */
	public void setAddAddonWithoutPackageError(boolean addAddonWithoutPackageError) {
		this.addAddonWithoutPackageError = addAddonWithoutPackageError;
	}

}
