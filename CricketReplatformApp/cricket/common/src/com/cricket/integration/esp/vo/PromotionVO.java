/**
 * 
 */
package com.cricket.integration.esp.vo;

import java.math.BigDecimal;

/**
 * @author MM112358
 *
 */
public class PromotionVO {

	/* Promotional offering code as defined in the billing system,
     * 						sometimes called campaign code */
    private String promotionCode;

    /* Promotional offering code description as defined in the			
     * 			billing system, sometimes called campaign code */
    private String promotionDescription;

    /* Promotional offering code charge if any. */
    private BigDecimal promotionCharge;
    
	/**
	 * 
	 */
	public PromotionVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pPromotionCode
	 * @param pPromotionDescription
	 * @param pPromotionCharge
	 */
	public PromotionVO(String pPromotionCode, String pPromotionDescription,
			BigDecimal pPromotionCharge) {
		super();
		promotionCode = pPromotionCode;
		promotionDescription = pPromotionDescription;
		promotionCharge = pPromotionCharge;
	}

	/**
	 * @return the promotionCode
	 */
	public String getPromotionCode() {
		return promotionCode;
	}

	/**
	 * @param pPromotionCode the promotionCode to set
	 */
	public void setPromotionCode(String pPromotionCode) {
		promotionCode = pPromotionCode;
	}

	/**
	 * @return the promotionDescription
	 */
	public String getPromotionDescription() {
		return promotionDescription;
	}

	/**
	 * @param pPromotionDescription the promotionDescription to set
	 */
	public void setPromotionDescription(String pPromotionDescription) {
		promotionDescription = pPromotionDescription;
	}

	/**
	 * @return the promotionCharge
	 */
	public BigDecimal getPromotionCharge() {
		return promotionCharge;
	}

	/**
	 * @param pPromotionCharge the promotionCharge to set
	 */
	public void setPromotionCharge(BigDecimal pPromotionCharge) {
		promotionCharge = pPromotionCharge;
	}

}
