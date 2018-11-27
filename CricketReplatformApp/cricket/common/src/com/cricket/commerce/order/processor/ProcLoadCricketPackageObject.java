package com.cricket.commerce.order.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketPackage;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.integration.esp.CricketESPAdapter;

import atg.beans.DynamicBeans;
import atg.commerce.order.ChangedProperties;
import atg.commerce.order.CommerceIdentifier;
import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.Order;
import atg.commerce.order.OrderManager;
import atg.commerce.order.OrderTools;
import atg.commerce.order.PipelineConstants;
import atg.commerce.order.processor.LoadProperties;
import atg.commerce.order.processor.OrderRepositoryUtils;
import atg.core.util.ResourceUtils;
import atg.core.util.StringUtils;
import atg.repository.ItemDescriptorImpl;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItemDescriptor;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;


public class ProcLoadCricketPackageObject extends LoadProperties implements PipelineProcessor {
	
	 static final String MY_RESOURCE_NAME = "atg.commerce.order.OrderResources";

	  /** Resource Bundle **/
	  private static java.util.ResourceBundle sResourceBundle = atg.core.i18n.LayeredResourceBundle.getBundle(MY_RESOURCE_NAME, atg.service.dynamo.LangLicense.getLicensedDefault());
	  
	  private final int SUCCESS = 1;
	 	  
	  public int[] getRetCodes()
	  {
	    int[] ret = {SUCCESS};
	    return ret;
	  } 
	  
	  public int runProcess(Object pParam, PipelineResult pResult) throws Exception
	  {
	    HashMap map = (HashMap) pParam;
	    CricketOrderImpl order = (CricketOrderImpl) map.get(PipelineConstants.ORDER);
	    MutableRepositoryItem orderItem = (MutableRepositoryItem) map.get(PipelineConstants.ORDERREPOSITORYITEM);
	    OrderManager orderManager = (OrderManager) map.get(PipelineConstants.ORDERMANAGER);
	    Boolean invalidateCache = (Boolean) map.get(PipelineConstants.INVALIDATECACHE);
	    if (isLoggingDebug()) {			 					
	    		logDebug("Inside the ProcLoadCricketPackageObject class of runProcess() method :::");
		}   
	    // check for null parameters
	    if (order == null)
	      throw new InvalidParameterException(ResourceUtils.getMsgResource("InvalidOrderParameter",
	                                      MY_RESOURCE_NAME, sResourceBundle));
	    if (orderItem == null)
	      throw new InvalidParameterException(ResourceUtils.getMsgResource("InvalidRepositoryItemParameter",
	                                      MY_RESOURCE_NAME, sResourceBundle));
	    if (orderManager == null)
	      throw new InvalidParameterException(ResourceUtils.getMsgResource("InvalidOrderManagerParameter",
	                                      MY_RESOURCE_NAME, sResourceBundle));
	    if (invalidateCache == null)
	      invalidateCache = Boolean.FALSE;
	        
	    OrderTools orderTools = orderManager.getOrderTools();

	    CommerceIdentifier ci;
	    MutableRepositoryItem sgMutItem, mutItem;
	    RepositoryItemDescriptor desc;
	    
	    String className, mappedPropName;
	    String[] loadProperties = getLoadProperties();
	    Object value;
	    
	    List cricketPackages = (List) orderItem.getPropertyValue(CricketPipleLineConstants.CRICKET_PACKAGE_ITEM_PROP);
	    
	    Iterator iter = cricketPackages.iterator();
	    while (iter.hasNext()) {
	    	 mutItem = (MutableRepositoryItem) iter.next();
	         
	         if(mutItem == null) {
	           if(isLoggingError()) {
	             logError("Null cricketPackage in order: " + order.getId());
	           }
	           continue;
	         }
	         desc = mutItem.getItemDescriptor();
	         if (invalidateCache.booleanValue()) {
	             invalidateCache((ItemDescriptorImpl) desc, mutItem);
	         }
	         
	         className = orderTools.getMappedBeanName(desc.getItemDescriptorName());
	         ci = (CommerceIdentifier) Class.forName(className).newInstance();

	         DynamicBeans.setPropertyValue(ci, CricketCommonConstants.REPOSITORY_ID, mutItem.getRepositoryId());
	         if (ci instanceof ChangedProperties) {
	           ((ChangedProperties) ci).setRepositoryItem(mutItem);
	         }
	         
	         for (int i = 0; i < loadProperties.length; i++) {
	             mappedPropName = getMappedPropertyName(loadProperties[i]);
	             if (desc.hasProperty(loadProperties[i])) {
	               value = mutItem.getPropertyValue(loadProperties[i]);
	               if (isLoggingDebug())
	                 logDebug("load property[" + loadProperties[i] + ":" + value + ":"
	                             + ci.getClass().getName() + ":" + ci.getId() + "]");
	               OrderRepositoryUtils.setPropertyValue(order, ci, mappedPropName, value);
	             }
	           }
	         
	         if (ci instanceof ChangedProperties) {
	             ((ChangedProperties) ci).clearChangedProperties();
	         }
	             
	           if(isLoggingDebug()) {
	             logDebug("loading cricketPackage item " + ci.getClass().getName() + ":" + ci.getId() + "class of container is " + order.getClass().getName());
	           }
	             
	           order.addCricketPackage((CricketPackage) ci);
	                     
	    }
	 /*       if (ci instanceof ChangedProperties)
	          ((ChangedProperties) ci).clearChangedProperties();

	        sg.addHandlingInstruction((HandlingInstruction) ci);
	      } // for
	      
	      if (sg instanceof ChangedProperties)
	        ((ChangedProperties) sg).clearChangedProperties();
	    } // for
*/		
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
	   * This method invalidates the item from the cache if invalidateCache is true
	   */
	  protected void invalidateCache(ItemDescriptorImpl desc, MutableRepositoryItem mutItem) {
	    try {
	      desc.removeItemFromCache(mutItem.getRepositoryId());
	    }
	    catch (RepositoryException e) {
	      if (isLoggingWarning())
	        logWarning("Unable to invalidate item descriptor " + desc.getItemDescriptorName() + ":" + mutItem.getRepositoryId());
	    }
	  }
}
