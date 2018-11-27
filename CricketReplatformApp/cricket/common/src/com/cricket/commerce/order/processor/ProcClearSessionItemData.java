package com.cricket.commerce.order.processor;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import atg.commerce.order.PaymentGroupRelationship;
import atg.commerce.order.PipelineConstants;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.nucleus.naming.ComponentName;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;

import com.cricket.browse.StorePackageFeaturesDroplet;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderManager;
import com.cricket.commerce.order.session.UpgradeItemDetailsSessionBean;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.util.EspServiceResponseData;
import com.cricket.vo.CricketProfile;

public class ProcClearSessionItemData extends GenericService implements PipelineProcessor {

    /** Value SUCCESS return. */
    private static final int SUCCESS = 1;
    
    private ComponentName mSessionBeanComponentName = null;
    private ComponentName storePackageFeaturesDropletComponentName = null;
    private ComponentName espServiceResponseDataComponentName = null;
	private String mSessionBeanPath;
	private String storePackageFeaturesDroplet;
	private String espServiceResponseData;
	private String mCktProfilePath;
	private CricketOrderManager orderManager;
	private ComponentName mCktProfileComponentName = null;

	@Override
	 public int[] getRetCodes() {
	    int[] ret = {SUCCESS};
	    return ret;
	  }  

	@Override
	public int runProcess(Object pParam, PipelineResult pResult) throws Exception {
		
		 if(isLoggingDebug()){
		    	logDebug("Entering ClearSessionItemData processor");
		    }
		 HashMap paramMap = (HashMap) pParam;
	     Object order = paramMap.get(PipelineConstants.ORDER);
	     CricketOrderImpl cricketOrder = (CricketOrderImpl) order;
	     if (isLoggingDebug()) {		 				
	    		logDebug("Inside the ProcClearSessionItemData class of runProcess() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
		}
	    /* CricketOrderImpl cricketOrder = (CricketOrderImpl)order;
	     if(cricketOrder.isDownGrade()){
	    	 cricketOrder.getPaymentGroups();
	    	 PaymentGroupRelationship pPaymentGroupRelationship = null;
	    	 
	    	List payRel =  cricketOrder.getPaymentGroupRelationships();
	    	if(payRel!=null){
	    		pPaymentGroupRelationship = (PaymentGroupRelationship)payRel.get(0);
	    	}
	    	 cricketOrder.addPaymentGroupRelationship(pPaymentGroupRelationship);
	    	 
	     }*/
		UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean = (UpgradeItemDetailsSessionBean) ServletUtil
				.getCurrentRequest().resolveName(mSessionBeanComponentName);
 		clearUpgradeItemSessionData(upgradeItemDetailsSessionBean);
		
		StorePackageFeaturesDroplet storePackageFeaturesDroplet = (StorePackageFeaturesDroplet) ServletUtil
				.getCurrentRequest().resolveName(storePackageFeaturesDropletComponentName);
		storePackageFeaturesDroplet.setUserSessionBean(null);
		EspServiceResponseData espServiceResponseData = (EspServiceResponseData) ServletUtil
				.getCurrentRequest().resolveName(espServiceResponseDataComponentName);
		espServiceResponseData.setServiceAmountList(null);
		espServiceResponseData.setRecurringSubscriberChargesList(null);
		CricketProfile cricketProfile = (CricketProfile) ServletUtil
		.getCurrentRequest().resolveName(mCktProfileComponentName);
		cricketProfile.setMdn(null);
 		 if(isLoggingDebug()){
		    	logDebug("Exiting ClearSessionItemData processor");
		    }
		return SUCCESS;
	}

	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	/**
	 * Clears Session data stored from Cookie related to upgrade Phone/
	 * Change addon/Change plan scenarios.
	 * 
	 * @param upgradeItemDetailsSessionBean
	 */
	private void clearUpgradeItemSessionData(UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean) {
		
		upgradeItemDetailsSessionBean.setRemovedAddons(null);
		upgradeItemDetailsSessionBean.setRemovedPhoneId(null);
		upgradeItemDetailsSessionBean.setRemovedPhoneSkuId(null);
		upgradeItemDetailsSessionBean.setRemovedPlanId(null);
		upgradeItemDetailsSessionBean.setRemovedPlanSkuId(null);
		upgradeItemDetailsSessionBean.setUserIntention(null);
		
		if(isLoggingDebug()){
	    	logDebug("ClearSessionItemData processor : Session Item Data Cleared");
	    }
	}
	
	/**
	 * @return the sessionBeanPath
	 */
	public String getSessionBeanPath() {
		return mSessionBeanPath;
	}

	/**
	 * @param pSessionBeanPath the sessionBeanPath to set
	 */
	public void setSessionBeanPath(String pSessionBeanPath) {
		mSessionBeanPath = pSessionBeanPath;
		if (mSessionBeanPath != null) {
			mSessionBeanComponentName = ComponentName.getComponentName(mSessionBeanPath);
		} else {
			mSessionBeanComponentName = null;
		}
	}
	
	/**
	 * @return the sessionBeanComponentName
	 */
	public ComponentName getSessionBeanComponentName() {
		return mSessionBeanComponentName;
	}

	/**
	 * @param pSessionBeanComponentName the sessionBeanComponentName to set
	 */
	public void setSessionBeanComponentName(ComponentName pSessionBeanComponentName) {
		mSessionBeanComponentName = pSessionBeanComponentName;
	}

	/**
	 * @return the storePackageFeaturesDropletComponentName
	 */
	public ComponentName getStorePackageFeaturesDropletComponentName() {
		return storePackageFeaturesDropletComponentName;
	}

	/**
	 * @param storePackageFeaturesDropletComponentName the storePackageFeaturesDropletComponentName to set
	 */
	public void setStorePackageFeaturesDropletComponentName(
			ComponentName storePackageFeaturesDropletComponentName) {
		this.storePackageFeaturesDropletComponentName = storePackageFeaturesDropletComponentName;
	}

	/**
	 * @return the espServiceResponseDataComponentName
	 */
	public ComponentName getEspServiceResponseDataComponentName() {
		return espServiceResponseDataComponentName;
	}

	/**
	 * @param espServiceResponseDataComponentName the espServiceResponseDataComponentName to set
	 */
	public void setEspServiceResponseDataComponentName(
			ComponentName espServiceResponseDataComponentName) {
		this.espServiceResponseDataComponentName = espServiceResponseDataComponentName;
	}

	/**
	 * @return the storePackageFeaturesDroplet
	 */
	public String getStorePackageFeaturesDroplet() {
		return storePackageFeaturesDroplet;
	}

	/**
	 * @param storePackageFeaturesDroplet the storePackageFeaturesDroplet to set
	 */
	public void setStorePackageFeaturesDroplet(String storePackageFeaturesDroplet) {
		this.storePackageFeaturesDroplet = storePackageFeaturesDroplet;
		
 		if (this.storePackageFeaturesDroplet != null) {
 			storePackageFeaturesDropletComponentName = ComponentName.getComponentName(this.storePackageFeaturesDroplet);
		} else {
			storePackageFeaturesDropletComponentName = null;
		}
	}

	/**
	 * @return the espServiceResponseData
	 */
	public String getEspServiceResponseData() {
		return espServiceResponseData;
	}

	/**
	 * @param espServiceResponseData the espServiceResponseData to set
	 */
	public void setEspServiceResponseData(String espServiceResponseData) {
		this.espServiceResponseData = espServiceResponseData;
		if (this.espServiceResponseData != null) {
			espServiceResponseDataComponentName = ComponentName.getComponentName(this.espServiceResponseData);
		} else {
			espServiceResponseDataComponentName = null;
		}
	}

	 
	/**
	 * @return the sessionBeanPath
	 */
	public String getCktProfilePath() {
		return mCktProfilePath;
	}

	/**
	 * @param pSessionBeanPath the sessionBeanPath to set
	 */
	public void setCktProfilePath(String pCktProfilePath) {
		mCktProfilePath = pCktProfilePath;
		if (mCktProfilePath != null) {
			mCktProfileComponentName = ComponentName.getComponentName(mCktProfilePath);
		} else {
			mCktProfileComponentName = null;
		}
	}

	/**
	 * @return the orderManager
	 */
	public CricketOrderManager getOrderManager() {
		return orderManager;
	}

	/**
	 * @param orderManager the orderManager to set
	 */
	public void setOrderManager(CricketOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	
	

}
