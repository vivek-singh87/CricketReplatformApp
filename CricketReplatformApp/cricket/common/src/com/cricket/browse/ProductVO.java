package com.cricket.browse;

import java.util.List;

/**
 * @author RM112139
 *
 */
public class ProductVO {//implements Comparable<ProductVO>{ 
	
	/**
	 * displayName
	 */
	private String displayName;
	
	/**
	 * productId
	 */
	private String productId;
	
	/**
	 * longDescription
	 */
	private String longDescription;
	
	/**
	 * shortDescription
	 */
	private String shortDescription;
	
	/**
	 * childSkus
	 */
	private List<SKUVO> childSkus;
	
	/**
	 * thumbNailImages
	 */
	private List<String> thumbNailImages;
	
	/**
	 * thumbNailImages
	 */
	private List<String> smallImages;
	
	/**
	 * largeImages
	 */
	private List<String> largeImages;
	
	/**
	 * defaultSku Id
	 */
	private String defaultSkuId;

	/**
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * @param longDescription
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * @return
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return
	 */
	public List<SKUVO> getChildSkus() {
		return childSkus;
	}

	/**
	 * @param childSkus
	 */
	public void setChildSkus(List<SKUVO> childSkus) {
		this.childSkus = childSkus;
	}

	/**
	 * @return
	 */
	public List<String> getThumbNailImages() {
		return thumbNailImages;
	}

	/**
	 * @param thumbNailImages
	 */
	public void setThumbNailImages(List<String> thumbNailImages) {
		this.thumbNailImages = thumbNailImages;
	}

	/**
	 * @return
	 */
	public List<String> getLargeImages() {
		return largeImages;
	}

	/**
	 * @param largeImages
	 */
	public void setLargeImages(List<String> largeImages) {
		this.largeImages = largeImages;
	}

	public List<String> getSmallImages() {
		return smallImages;
	}

	public void setSmallImages(List<String> smallImages) {
		this.smallImages = smallImages;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductVO other = (ProductVO) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}

	public String getDefaultSkuId() {
		return defaultSkuId;
	}

	public void setDefaultSkuId(String defaultSkuId) {
		this.defaultSkuId = defaultSkuId;
	}
	
	/*@Override
	public int compareTo(ProductVO productVO) {
		
		String displayName1 = productVO.getDisplayName().toUpperCase();
		String displayName2 = this.getDisplayName().toUpperCase();		
		//ascending order
		return displayName2.compareTo(displayName1);		
	}
	*/

}
