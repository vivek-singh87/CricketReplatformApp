package com.cricket.commerce.order;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import atg.commerce.order.ChangedProperties;
import atg.commerce.order.CommerceIdentifierImpl;
import atg.repository.MutableRepositoryItem;

import com.cricket.common.constants.CricketCommonConstants;

public class CricketPackage extends CommerceIdentifierImpl
implements ChangedProperties {

	private static final long serialVersionUID = 1L;

	public CricketPackage() {
		    super();
		  }
/*
	 // property: miscInformation
	  public String getMiscInformation() {
	    return (String) getPropertyValue("miscInformation");
	  }

	  public void setMiscInformation (String pMiscInformation) {
	    setPropertyValue("miscInformation", pMiscInformation);
	  }*/
	 
	 
	 
	 /**
	  * 
	  * @return
	  */
	 public String getPackageTotal(){
		 return (String) getPropertyValue(CricketCommonConstants.PACKAGE_TOTAL);
		 
	 }
	 
	 /**
	  * 
	  * @param packageTotal
	  */
	 public void setPackageTotal(String packageTotal){
		 setPropertyValue(CricketCommonConstants.PACKAGE_TOTAL, packageTotal);
	 }
	 
	 
	 // Package Number
	 public int getPackageNumber() {
		 return (Integer) getPropertyValue(CricketCommonConstants.PACKAGE_NUMBER);
	 
	 }
	 
	 public void setPackageNumber(int pPackageNumber) {
		 setPropertyValue(CricketCommonConstants.PACKAGE_NUMBER, pPackageNumber);
	 
	 }
	 // Activation Fee
	 public double getActivationFee() {
		 return (Double) getPropertyValue(CricketCommonConstants.ACTIVATION_FEE);
	 
	 }
	 
	 public void setActivationFee(double pActivationFee) {
		 setPropertyValue(CricketCommonConstants.ACTIVATION_FEE, pActivationFee);
	 
	 }
	/*// packageType
	 public String getPackageType() {
		 return (String) getPropertyValue("packageType");
	 
	 }
	 
	 public void setPackageType(String pPackageType) {
		 setPropertyValue("packageType", pPackageType);
	 
	 }*/
	// newMdn
	 public String getNewMdn() {
		 return (String) getPropertyValue(CricketCommonConstants.NEW_MDN);
	 
	 }
	 
	 public void setNewMdn(String pNewMdn) {
		 setPropertyValue(CricketCommonConstants.NEW_MDN, pNewMdn);
	 
	 }
	// mdn
	 public String getMdn() {
		 return (String) getPropertyValue(CricketCommonConstants.MDN);
	 
	 }
	 
	 public void setMdn(String pMdn) {
		 setPropertyValue(CricketCommonConstants.MDN, pMdn);
	 
	 }
	 
	  //
	  // Observer implementation
	  //
	  public void update(Observable o, Object arg) {
	    if (arg instanceof String) {
	      addChangedProperty((String) arg);
	    }
	    else {
	      throw new RuntimeException("Observable update for " +
	         getClass().getName() + " was received with arg type " +
	         arg.getClass().getName() + ":" + arg);
	    }
	  }

	  //
	  // ChangedProperties implementation
	  //

	  // property: saveAllProperties
	  private boolean mSaveAllProperties = false;

	  public boolean getSaveAllProperties() {
	    return mSaveAllProperties;
	  }

	  public void setSaveAllProperties(boolean pSaveAllProperties) {
	    mSaveAllProperties = pSaveAllProperties;
	  }

	  // property: changed
	  private boolean mChanged = false;

	  public boolean isChanged() {
	    return (mChanged || (mChangedProperties != null
	                   && ! getChangedProperties().isEmpty()));
	  }

	  public void setChanged(boolean pChanged) {
	    mChanged = pChanged;
	  }

	  // property: changedProperties
	  @SuppressWarnings("rawtypes")
	private HashSet mChangedProperties = new HashSet(7);

	  @SuppressWarnings("rawtypes")
	public Set getChangedProperties() {
	    return mChangedProperties;
	  }

	  @SuppressWarnings("unchecked")
	public void addChangedProperty(String pPropertyName) {
	    mChangedProperties.add(pPropertyName);
	  }

	  public void clearChangedProperties() {
	    mChangedProperties.clear();
	  }

	  // property: repositoryItem
	  private MutableRepositoryItem mRepositoryItem = null;

	  public MutableRepositoryItem getRepositoryItem() {
	    return mRepositoryItem;
	  }

	  public void setRepositoryItem(MutableRepositoryItem pRepositoryItem) {
	    mRepositoryItem = pRepositoryItem;
	  }

	  // setPropertyValue/getPropertyValue methods
	  public Object getPropertyValue(String pPropertyName) {
	    MutableRepositoryItem mutItem = getRepositoryItem();
	    if (mutItem == null)
	      throw new RuntimeException("Null repository item: " + getId());

	    return mutItem.getPropertyValue(pPropertyName);
	  }

	  public void setPropertyValue(String pPropertyName,
	                     Object pPropertyValue)
	  {
	    MutableRepositoryItem mutItem = getRepositoryItem();
	    if (mutItem == null)
	      throw new RuntimeException("Null repository item: " + getId());

	    mutItem.setPropertyValue(pPropertyName, pPropertyValue);
	    setChanged(true);
	  }
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		if (arg0 != null && arg0 instanceof CricketPackage) {
			CricketPackage cp = (CricketPackage) arg0;
			
			if (getId() == null) {
                return (cp.getId() == null);
            }
            else {
                return getId().equals(cp.getId());
            }	
			
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int hash = 7;
		hash = PRIME * hash + getPackageNumber();
		hash = PRIME * hash + (null == getId() ? 0 : getId().hashCode());
		return hash;
	}


}
