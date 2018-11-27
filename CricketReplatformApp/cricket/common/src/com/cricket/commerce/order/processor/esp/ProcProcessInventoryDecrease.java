package com.cricket.commerce.order.processor.esp;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.TransactionManager;

import atg.commerce.order.InvalidParameterException;
import atg.commerce.order.OrderManager;
import atg.commerce.order.PipelineConstants;
import atg.core.util.StringUtils;
import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
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

/**
 * @author TechM
 * Description: This processor is used to decrease the Inventory.
 */
public class ProcProcessInventoryDecrease extends GenericService
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
	@SuppressWarnings("rawtypes")
	public int runProcess(Object pParam, PipelineResult pResult)
			throws InvalidParameterException {
		//logInfo("[ProcProcessInventoryDecrease->runProcess()]: Entering into runProcess()...");
		
		long starttime = 0L;
		long endtime = 0L;
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
			//RepositoryInventoryManager inventoryManager =(RepositoryInventoryManager) paramMap.get(PipelineConstants.INVENTORYMANAGER);
			
			if (order == null) {
				throw new InvalidParameterException("[ProcProcessInventoryDecrease->runProcess()]: order Id is not valid");
			}
		
 			cricketOrder = (CricketOrderImpl) order;
 			if (isLoggingDebug()) {			 		
		    		logDebug("Inside the ProcProcessInventoryDecrease class of runProcess() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId());
			}
			Object accountHolderAddressDataObj = paramMap.get(CricketESPConstants.ACCOUNT_HOLDER_ADDDRESS_DATA);
			CricketAccountHolderAddressData  accountHolderAddressData = (CricketAccountHolderAddressData)accountHolderAddressDataObj;
			logInfo("[ProcProcessInventoryDecrease->runProcess()]: Entering into runProcess()...Order Id:"+cricketOrder.getId()+ " || For the User: "+
					accountHolderAddressData.getAccountAddress().getFirstName()+ " "+accountHolderAddressData.getAccountAddress().getLastName()+" || Phone Number : "+
					accountHolderAddressData.getAccountAddress().getPhoneNumber());
	        	try {
				 if(isLoggingDebug()){
					 CricketOrderTools ordertools =(CricketOrderTools) getOrderManager().getOrderTools();
	     	    	 logDebug("[ProcProcessInventoryDecrease->runProcess()]: Order Details :"+ordertools.orderDetailsToTrack(cricketOrder, null, this.getClass().getName()));
		        }
				starttime = System.currentTimeMillis();
				
				if(null != cricketOrder.getWorkOrderType() && cricketOrder.getWorkOrderType() !=CricketESPConstants.TRANSACTION_TYPE_RRC){
				 synchronized (cricketOrder) {
					@SuppressWarnings("unchecked")
					List<CricketCommerceItemImpl> commerceItemList = cricketOrder.getCommerceItems();
					String itemType = null;
					String skuId = null;
	 				long currentStockLevel;
	 				MutableRepositoryItem mutItem= null;
					for(CricketCommerceItemImpl commerceItem:commerceItemList){
						itemType = commerceItem.getCricItemTypes();
						if(itemType.equalsIgnoreCase(CricketCommonConstants.PHONE_PRODUCT) || itemType.equalsIgnoreCase(CricketCommonConstants.ACC_UPGRADE_PHONE) ||
								itemType.equalsIgnoreCase(CricketCommonConstants.ACCESSORY_PRODUCT)){
							logInfo("ProcProcessInventoryDecrease - inventory check for the Item: "+commerceItem.getAuxiliaryData().getProductId());
	
							skuId = commerceItem.getCatalogRefId();
							RepositoryView priceRepoView = getInventoryRepository().getView(CricketCommonConstants.INVENTORY);			
							RqlStatement smt = RqlStatement.parseRqlStatement("catalogRefId=?0");
							Object params[] = new Object[1];
							params[0]=skuId;
						   RepositoryItem[] inventoryList =   smt.executeQuery(priceRepoView, params);
							if(null!=inventoryList)
							{
								currentStockLevel= (Long)inventoryList[0].getPropertyValue(CricketCommonConstants.INVENTORY_STOCK_LEVEL);
								logInfo("ProcProcessInventoryDecrease - current Stock level for the Item: "+commerceItem.getCatalogRefId() +" is "+currentStockLevel);
								if(currentStockLevel > 0 && currentStockLevel-commerceItem.getQuantity()>=0){
									mutItem = (MutableRepositoryItem) inventoryList[0];
									if(null!= mutItem) {
										synchronized(mutItem){
										mutItem.setPropertyValue(CricketCommonConstants.INVENTORY_STOCK_LEVEL,currentStockLevel-commerceItem.getQuantity());
										getInventoryRepository().updateItem(mutItem);
										logInfo("ProcProcessInventoryDecrease -after decresing -Stock level for the Item: "+commerceItem.getCatalogRefId() +" is "+mutItem.getPropertyValue("stockLevel"));
										// why oob not working i.e below one
										//	inventoryManager.decreaseStockLevel(inventoryList[0].getRepositoryId(),commerceItem.getQuantity(),null);	
										}
									}
								}
							}else{
								logInfo("ProcProcessInventoryDecrease - No Inventory is Availabe for the Item "+commerceItem.getCatalogRefId());
							}
						}
					  }
					} // end of synchronized
				}// end of if
				
				logInfo("[ProcProcessInventoryDecrease->runProcess()]: Exiting runProcess(), with return SUCCESS..." + getCricketESPAdapter().getSessionOrderRequestConversationInfo(cricketOrder));
				return SUCCESS;
	        	} catch (RepositoryException e) {
	        		vlogError("[ProcProcessInventoryDecrease->runProcess()]: " + CricketESPConstants.WHOOP_KEYWORD +  " RepositoryException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e);
					e.printStackTrace();
				}
	        	cricketOrder.setTotalTax(cricketOrder.getTaxPriceInfo().getAmount());
	        	endtime = System.currentTimeMillis();
	        	logInfo("ProcProcessInventoryDecrease - Time taken to execute ProcProcessInventoryDecrease chain:"+(endtime-starttime));
	 } catch (TransactionDemarcationException e1) {
		 	vlogError("[ProcProcessInventoryDecrease->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + "TransactionDemarcationException while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), e1);

		e1.printStackTrace();
	}
        	finally {
    	      try {
    	        td.end();
    	      }
    	      catch (TransactionDemarcationException tde) {
    	        if (isLoggingError()) {
    	        	vlogError("[ProcProcessInventoryDecrease->runProcess()]:" + CricketESPConstants.WHOOP_KEYWORD + "TransactionDemarcationException inside finally block while getting response:" + "ConversationId: " + getCricketESPAdapter().getConversationId()  + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  cricketOrder.getId(), tde);
    	          logError(tde);
    	        }
    	      }
    	    }	 			
		}
 				logInfo("[ProcProcessInventoryDecrease->runProcess()]: Exiting runProcess(), with return  success...");
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
 

	 

}
