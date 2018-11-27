package com.cricket.commerce.order.processor.esp;

import static com.cricket.common.constants.CricketESPConstants.PROPERTY_DISPLAY_NAME;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.TransactionManager;

import atg.commerce.inventory.RepositoryInventoryManager;
import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.OrderManager;
import atg.commerce.order.PipelineConstants;
import atg.core.util.StringUtils;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;
import atg.repository.MutableRepository;
import atg.repository.RepositoryItem;
import atg.service.pipeline.PipelineProcessor;
import atg.service.pipeline.PipelineResult;
import atg.servlet.ServletUtil;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.inventory.InventoryResponseVO;
import com.cricket.inventory.InventoryUtils;

/**
 * @author TechM
 * Description: This processor is used to check the Inventory.
 */
public class ProcProcessInventoryCheck extends GenericService
		implements PipelineProcessor {
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
 
	 /** holding order manager component */
    OrderManager orderManager;
    MutableRepository inventoryRepository;
    TransactionManager transactionManager;
    boolean enabled;
    InventoryUtils inventoryUtils;
  /**
	 * @return the orderManager
	 */
	public OrderManager getOrderManager() {
		return orderManager;
	}

	/**
	 * @param orderManager the orderManager to set
	 */
	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}
	 private static final int SUCCESS=1;
	/**
	 * 0 - call stop chain.
	 * 1 - call createActivationQuote ESP
	 * 2 - call UpdateSubscriber ESP
	 * 3 - call createShippingQuote ESP
	 * @return an integer array of the valid return codes.
	 */
	@Override
	 public int[] getRetCodes() {
	    int[] ret = {SUCCESS};
	    return ret;
	  }  

	/**
	 * @author TechM
	 * Description: This processor is used to decrease the Inventory level.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int runProcess(Object pParam, PipelineResult pResult)
			throws InvalidParameterException {
	//	logInfo("[ProcProcessInventoryCheck->runProcess()]: Entering into runProcess()...");
		/*long starttime = 0L;
		long endtime = 0L;
		String orderType = null;*/
		if(enabled){
		 TransactionDemarcation td = new TransactionDemarcation();
		 CricketOrderImpl cricketOrder = null;
		 HashMap paramMapGetOrder = (HashMap) pParam;
		 Object getOrderObject = paramMapGetOrder.get(PipelineConstants.ORDER);
		 cricketOrder = (CricketOrderImpl) getOrderObject;
		 
	 try {
		    td.begin(getTransactionManager(), 3);
			HashMap paramMap = (HashMap) pParam;
			
			Object order = paramMap.get(PipelineConstants.ORDER);
			RepositoryInventoryManager inventoryManager =(RepositoryInventoryManager) paramMap.get(PipelineConstants.INVENTORYMANAGER);
			
			if (order == null) {
				throw new InvalidParameterException("[ProcProcessInventoryCheck->runProcess()]: order Id is not valid");
			}
 			cricketOrder = (CricketOrderImpl) order;
 			if (isLoggingDebug()) {		
 		    		logDebug("Inside the ProcProcessInventoryCheck class of runProcess() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
 			}
			Object accountHolderAddressDataObj = paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
			CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData)accountHolderAddressDataObj;
			logInfo("[ProcProcessInventoryCheck->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
					accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
					accountHolderAddressData.getAccountAddress().getPhoneNumber());
				//starttime = System.currentTimeMillis();
				if(null != cricketOrder.getWorkOrderType() && cricketOrder.getWorkOrderType() != "RRC"){
					List<CricketCommerceItemImpl> commerceItemList = cricketOrder.getCommerceItems();
					String itemType = null;
					String skuId = null;
					StringBuilder outOfStockItems = new StringBuilder();
  					for(CricketCommerceItemImpl commerceItem:commerceItemList){
						itemType = commerceItem.getCricItemTypes();
						if(itemType.equalsIgnoreCase(CricketCommonConstants.PHONE_PRODUCT) || itemType.equalsIgnoreCase(CricketCommonConstants.ACC_UPGRADE_PHONE) ||
								itemType.equalsIgnoreCase(CricketCommonConstants.ACCESSORY_PRODUCT)){
							logInfo("ProcProcessInventoryCheck - inventory check for the Item: "+commerceItem.getCatalogRefId());
							skuId = commerceItem.getCatalogRefId();
							// call the inventoryTool to check inventory status
							InventoryResponseVO inventoryResponseVO	=getInventoryUtils().checkStockLevelWithCurrentCart(skuId, cricketOrder, true);
							if(null!=inventoryResponseVO && inventoryResponseVO.isOutOfStock()){
								logInfo("ProcProcessInventoryCheck - Oops inventory for the Item: "+commerceItem.getCatalogRefId()+" is Not Avaible So Processing the order stops here..... ");
								outOfStockItems.append((String)((RepositoryItem)commerceItem.getAuxiliaryData().getProductRef()).getPropertyValue(PROPERTY_DISPLAY_NAME)).append(" ");
								pResult.addError(CricketCommonConstants.OUT_OF_STOCK+outOfStockItems.toString(), outOfStockItems.toString());
								cricketOrder.setTotalTax(0.0);
								//pResult.addError(CricketCommonConstants.OUT_OF_STOCK, CricketCommonConstants.OUT_OF_STOCK);
								return PipelineProcessor.STOP_CHAIN_EXECUTION;
							} else{
								logInfo("ProcProcessInventoryCheck - inventory for the Item: "+commerceItem.getCatalogRefId()+" is Avaible So Processing the order.. ");
							}
						}
					}
				}
				logInfo("[ProcProcessInventoryCheck->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
				return SUCCESS;
	        	 
	 } catch (TransactionDemarcationException e1) {
		 vlogError("[ProcProcessInventoryCheck->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + " TransactionDemarcationException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e1);
		e1.printStackTrace();
	}
        	finally {
    	      try {
    	        td.end();
    	      }
    	      catch (TransactionDemarcationException tde) {
    	    	  vlogError("[ProcProcessInventoryCheck->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + " TransactionDemarcationException inside finally block while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), tde);
    	      }
    	    }
		}
 				logInfo("[ProcProcessInventoryCheck->runProcess()]: Exiting runProcess(), with return  success...");
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
	 * @return the inventoryRepository
	 */
	public MutableRepository getInventoryRepository() {
		return inventoryRepository;
	}

	/**
	 * @param inventoryRepository the inventoryRepository to set
	 */
	public void setInventoryRepository(MutableRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	/**
	 * @return the transactionManager
	 */
	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

	/**
	 * @param transactionManager the transactionManager to set
	 */
	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	 
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the inventoryUtils
	 */
	public InventoryUtils getInventoryUtils() {
		return inventoryUtils;
	}

	/**
	 * @param inventoryUtils the inventoryUtils to set
	 */
	public void setInventoryUtils(InventoryUtils inventoryUtils) {
		this.inventoryUtils = inventoryUtils;
	}

	 

}
