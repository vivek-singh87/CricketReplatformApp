package com.cricket.search.records;

public class RecordVO {
	private String productId;
	
	private String displayName;
	
	private String skuId;
	
	private String imageUrl;
	
	private Double finalPrice;
	
	private String ratePlanType;
	
	private String parentCategoryName;
	
	private String seoString;
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getRatePlanType() {
		return ratePlanType;
	}

	public void setRatePlanType(String ratePlanType) {
		this.ratePlanType = ratePlanType;
	}

	public String getParentCategoryName() {
		return parentCategoryName;
	}

	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}

	public String getSeoString() {
		return seoString;
	}

	public void setSeoString(String seoString) {
		this.seoString = seoString;
	}

	
}
