package com.cricket.commerce.order;

import atg.commerce.order.CommerceItemImpl;
import atg.commerce.order.PropertyNameConstants;

public class CricketCommerceItemImpl extends CommerceItemImpl{
	           
	private static final long serialVersionUID = -7372248157108321956L;
	private static final String PACKAGE_ID = "packageId";
	private static final String IS_LOS = "isLOS";
	private static final String IS_CHANGE_FLOW = "isChangeFlow";
	private static final String IS_DOWN_GRADE = "isDownGrade";
	private static final String IS_COMPATIBLE_ADDON = "isCompatibleAddon";
	//private String mPackageId;
	//private boolean isLOS;
	private static final String MARKET_CODE = "marketCode";
	private static final String E_ID = "eid";
	private static final String CRIC_ITEM_TYPES = "cricItemTypes";
	//private String mMarketCode;   
	//private String mEeid;

	/**
	 * @return the packageId
	 */
	public String getPackageId() {
		return (String) getPropertyValue(PACKAGE_ID);
	}

	/**
	 * @param pPackageId the packageId to set
	 */
	public void setPackageId(String pPackageId) {
		 setPropertyValue(PACKAGE_ID, pPackageId);
	}

	/**
	 * @return the isLOS
	 */
	public boolean isLOS() {
		Boolean returnValue = (Boolean) getPropertyValue(IS_LOS);

    	if (returnValue == null) {
            return false;
        } else {
            return returnValue.booleanValue();
        }
	}

	/**
	 * @param pIsLOS the isLOS to set
	 */
	public void setLOS(boolean pIsLOS) {
		 setPropertyValue(IS_LOS, Boolean.valueOf(pIsLOS));
	}
	

	/**
	 * @param pIsChangeFlow the isChangeFlow to set
	 */
	public void setChangeFlow(boolean pIsChangeFlow) {
		 setPropertyValue(IS_CHANGE_FLOW, Boolean.valueOf(pIsChangeFlow));
	}
	
	/**
	 * @return the isLOS
	 */
	public boolean isChangeFlow() {
		Boolean returnValue = (Boolean) getPropertyValue(IS_CHANGE_FLOW);

    	if (returnValue == null) {
            return false;
        } else {
            return returnValue.booleanValue();
        }
	}
	/**
	 * @return the cItemTypes
	 */
	public String getCricItemTypes() {
		return (String) getPropertyValue(CRIC_ITEM_TYPES);
	}

	/**
	 * @param pCItemTypes the cItemTypes to set
	 */
	public void setCricItemTypes(String pCItemTypes) {
		setPropertyValue(CRIC_ITEM_TYPES, pCItemTypes);
	}

	/**
	 * @return the marketCode
	 */
	public String getMarketCode() {
		return (String) getPropertyValue(MARKET_CODE);
	}

	/**
	 * @param pMarketCode the marketCode to set
	 */
	public void setMarketCode(String pMarketCode) {
		setPropertyValue(MARKET_CODE, pMarketCode);
	}

	/**
	 * @return the eeid
	 */
	public String getEid() {
		return (String) getPropertyValue(E_ID);
	}

	/**
	 * @param pEeid the eeid to set
	 */
	public void setEid(String pEid) {
		setPropertyValue(E_ID, pEid);
	}

	public String getCommerceItemClassType() {
	     return (String)getPropertyValue(PropertyNameConstants.COMMERCEITEMCLASSTYPE);
  }
	
  public void setCommerceItemClassType(String pCommerceItemClassType) {
    setPropertyValue(PropertyNameConstants.COMMERCEITEMCLASSTYPE, pCommerceItemClassType);
	  }

	public void setDownGrade(boolean pIsDownGrade) {
		setPropertyValue(IS_DOWN_GRADE, Boolean.valueOf(pIsDownGrade));
	}
	
	public boolean isDownGrade() {
		Boolean returnValue = (Boolean) getPropertyValue(IS_DOWN_GRADE);

    	if (returnValue == null) {
            return false;
        } else {
            return returnValue.booleanValue();
        }
	}
	
	public void setCompatibleAddon(boolean pIsCompatibleAddon) {
		setPropertyValue(IS_COMPATIBLE_ADDON, Boolean.valueOf(pIsCompatibleAddon));
	}
	
	public boolean isCompatibleAddon() {
		Boolean returnValue = (Boolean) getPropertyValue(IS_COMPATIBLE_ADDON);

    	if (returnValue == null) {
            return false;
        } else {
            return returnValue.booleanValue();
        }
	}
}
