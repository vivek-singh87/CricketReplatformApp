package com.cricket.commerce.order.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderManager;
import com.cricket.commerce.order.CricketPackage;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.integration.esp.CricketESPAdapter;

import atg.beans.PropertyNotFoundException;
import atg.commerce.CommerceException;
import atg.commerce.order.ChangedProperties;
import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.OrderTools;
import atg.commerce.order.PipelineConstants;
import atg.commerce.order.processor.OrderRepositoryUtils;
import atg.commerce.order.processor.SavedProperties;
import atg.core.util.ResourceUtils;
import atg.core.util.StringUtils;
import atg.repository.ConcurrentUpdateException;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Repository;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;


public class ProcSaveCricketPackageObject extends SavedProperties implements PipelineProcessor {

	// These are the two valid return codes for this pipeline processor
		private static final int SUCCESS = 1;
		private static final int FAILURE = 2;
		private boolean changed;
		
		/** holding CricketESPAdapter component. */
		private CricketESPAdapter cricketESPAdapter;

	/**
		 * @return the cricketESPAdapter
		 */
		public CricketESPAdapter getCricketESPAdapter() {
			return cricketESPAdapter;
		}

		/**
		 * @param pCricketESPAdapter the cricketESPAdapter to set
		 */
		public void setCricketESPAdapter(CricketESPAdapter pCricketESPAdapter) {
			cricketESPAdapter = pCricketESPAdapter;
		}

		  static final String MY_RESOURCE_NAME = "atg.commerce.order.OrderResources";

		  /** Resource Bundle **/
		  private static ResourceBundle sResourceBundle = atg.core.i18n.LayeredResourceBundle.getBundle(MY_RESOURCE_NAME, atg.service.dynamo.LangLicense.getLicensedDefault());
		 
		/**
		 * This method returns the return codes for the processor.
		 * 
		 * @return the int array of the return codes
		 */
		public int[] getRetCodes() {
			int[] retCodes = {SUCCESS, FAILURE};

			return retCodes;
		}
		
		public int runProcess(Object param, PipelineResult pipelineResult) throws Exception {
			Map map = (Map) param;
			CricketOrderImpl cricketOrder = (CricketOrderImpl) map
					.get(PipelineConstants.ORDER);
			CricketOrderManager cricketOrderManager = (CricketOrderManager) map
					.get(PipelineConstants.ORDERMANAGER);
			Repository repository = (Repository) map
					.get(PipelineConstants.ORDERREPOSITORY);
	        if (isLoggingDebug()) {				 					
		    		logDebug("Entering into ProcSaveCricketPackageObject class of runProcess() method :::");
			}
			String mappedPropName;
			Object value;
			if (cricketOrder == null)
			      throw new InvalidParameterException(ResourceUtils.getMsgResource("InvalidOrderParameter",
			                                      MY_RESOURCE_NAME, sResourceBundle));
			    if (repository == null)
			      throw new InvalidParameterException(ResourceUtils.getMsgResource("InvalidRepositoryParameter",
			                                      MY_RESOURCE_NAME, sResourceBundle));
			    if (cricketOrderManager == null)
			      throw new InvalidParameterException(ResourceUtils.getMsgResource("InvalidOrderManagerParameter",
			                                      MY_RESOURCE_NAME, sResourceBundle));
			    
			OrderTools orderTools = (OrderTools) cricketOrderManager.getOrderTools();

			MutableRepository mutableRepository = null;
			MutableRepositoryItem mutableRepositoryItem = null;		
			Object[] savedPropertiesObject;		

			mutableRepository = (MutableRepository) repository;

			CricketPackage cricketPackage = null;
			List cricketPackages = cricketOrder.getCktPackages();
			Iterator iterator = cricketPackages.iterator();

			List<MutableRepositoryItem> cricketPackagesList = new ArrayList<MutableRepositoryItem>();
			while (iterator.hasNext()) {
				cricketPackage = (CricketPackage) iterator.next();

				if (isLoggingDebug()) {
					logDebug("CricketPackage id is :" + cricketPackage.getId());
				}

				if (((cricketPackage) instanceof ChangedProperties)
						&& (!((ChangedProperties) cricketPackage)
								.getSaveAllProperties())) {
					savedPropertiesObject = ((ChangedProperties) cricketPackage)
							.getChangedProperties().toArray();
				} else {
					savedPropertiesObject = getSavedProperties();
				}
				
				mutableRepositoryItem = null;

				if (cricketPackage instanceof ChangedProperties) {
					mutableRepositoryItem = ((ChangedProperties) cricketPackage)
							.getRepositoryItem();

				}
				
				 if (mutableRepositoryItem == null) {
					 mutableRepositoryItem = mutableRepository.getItemForUpdate(cricketPackage.getId(),
			              orderTools.getMappedItemDescriptorName(cricketPackage.getClass().getName()));
			          if (cricketPackage instanceof ChangedProperties)
			            ((ChangedProperties) cricketPackage).setRepositoryItem(mutableRepositoryItem);
			        }
				 
				 for (int i = 0; i < savedPropertiesObject.length; i++) {
			          mappedPropName = getMappedPropertyName((String) savedPropertiesObject[i]);

			          if (! OrderRepositoryUtils.hasProperty(cricketOrder, cricketPackage, mappedPropName))
			            continue;

			          try {
			            value = OrderRepositoryUtils.getPropertyValue(cricketOrder, cricketPackage, mappedPropName);
			          }
			          catch (PropertyNotFoundException e) {
			        	  if (isLoggingError()) {
			        		  vlogError("[ProcSaveCricketPackageObject->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " PropertyNotFoundException :", e);
	                       }
			            continue; // should never happen because we already checked for existence
			          }

			          if (isLoggingDebug())
			            logDebug("save property[" + (String) savedPropertiesObject[i] + ":" + value + ":" +
			            		cricketPackage.getClass().getName() + ":" + cricketPackage.getId() + "]");
			          OrderRepositoryUtils.saveRepositoryItem(mutableRepository, mutableRepositoryItem, (String) savedPropertiesObject[i], value, orderTools);
			          changed = true;
			        }
				 
				 if ((! cricketOrder.isTransient()) && mutableRepositoryItem.isTransient()) {
			          if (isLoggingDebug())
			            logDebug("Adding CricketPackage to Repository: " + mutableRepositoryItem.getItemDescriptor().getItemDescriptorName());
			          mutableRepository.addItem(mutableRepositoryItem);
			        }
				 
				  try {
					  mutableRepository.updateItem(mutableRepositoryItem);


			        }
			        catch (ConcurrentUpdateException e) {
			        	if (isLoggingError()) {
			        		  vlogError("[ProcSaveCricketPackageObject->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ConcurrentUpdateException :", e);
	                      }
			          String[] msgArgs = { cricketOrder.getId(), mutableRepositoryItem.getItemDescriptor().getItemDescriptorName() };
			          throw new CommerceException(ResourceUtils.getMsgResource("ConcurrentUpdateAttempt",
			                                          MY_RESOURCE_NAME, sResourceBundle, msgArgs), e);
			        }
			        
				if (cricketPackage instanceof ChangedProperties) {
					ChangedProperties cp = (ChangedProperties) cricketPackage;

					if (cp.isChanged()) {
						changed = true;
					}

					cp.clearChangedProperties();
					cp.setSaveAllProperties(false);
					cp.setChanged(false);
				}
			}

			if (changed) {
				map.put("Changed", Boolean.TRUE);
				 if (isLoggingDebug()) {
				        logDebug("Set changed flag to true in ProcSaveCricketPackageObject");
				    }
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
		
}
