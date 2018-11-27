package com.cricket.common.util.droplet;

import java.io.IOException;

import javax.servlet.ServletException;

import atg.commerce.order.OrderManager;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.QueryExpression;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.commerce.order.OrderConstants;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.order.CricketOrder;

public class GetBillingOrderDetailsDroplet extends DynamoServlet {

	private Repository orderRepository;
	private OrderManager orderManager;
	
	private static final String BILLING_ORDERID = "billingOrderId";
	
		/** The oparam name rendered once during processing.*/
	  public static final String OPARAM_OUTPUT = "output";
	  
	  /** The oparam name rendered once during processing.*/
	  public static final String EMPTY = "empty";
	  
	  /** The oparam name rendered once during processing.*/
	  public static final String PHONES_OUTPUT = "Phones";
	  
	  /** The oparam name rendered once during processing.*/
	  public static final String SHIPPING_METHOD_OUTPUT = "shippingmethod";
	  
	  /** The oparam name rendered once during processing.*/
	  public static final String ORDER_OUTPUT = "order";
	
	  CricketOrderTools mOrderTools;
	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) throws ServletException, IOException {
		String billingOrderId = pReq.getParameter(BILLING_ORDERID);
		if(isLoggingDebug()){
			logDebug("Start Fetching the atg Order and shipping method for billing order id : " + billingOrderId);
		}
		
		try {
				
			if(billingOrderId!= null){
				RepositoryView orderView = getOrderRepository().getView(CricketCommonConstants.ITEM_DESC_ORDER);
				QueryBuilder builder = orderView.getQueryBuilder();
				QueryExpression propertyB = builder.createPropertyQueryExpression(OrderConstants.BILLING_SYSTEM_ORDER_ID);
				QueryExpression valueB = builder.createConstantQueryExpression(billingOrderId);
				Query orderQuery = builder.createComparisonQuery(propertyB, valueB, QueryBuilder.EQUALS);
			
				//order=(CricketOrderImpl) getOrderManager().getCricketOrderTools().getOrderRepository().getItem(billingOrderId, "order");
				RepositoryItem[] orders=orderView.executeQuery(orderQuery);
				
				 if(orders != null && orders.length > 0 ){
					 CricketOrder order=(CricketOrder) getOrderManager().loadOrder(orders[0].getRepositoryId());
					 	//get shipping method from order 
						getShippingMethod((CricketOrder) getOrderManager().loadOrder(orders[0].getRepositoryId()),pReq,pRes);
						pReq.setParameter(ORDER_OUTPUT,order);
						
				 }
							
				 pReq.serviceLocalParameter(OPARAM_OUTPUT, pReq,pRes);
			}
			
				
			
		} catch (RepositoryException e) {
			pReq.serviceLocalParameter(EMPTY, pReq,pRes);
			if(isLoggingError()) {
				logError("Error in getting  order from Order Repository for billing order ID="+billingOrderId);
			}
		}
		catch (Exception e) {
			pReq.serviceLocalParameter(EMPTY, pReq,pRes);
			if(isLoggingError()) {
				logError("Error in getting  order from Order Repository for billing order ID="+billingOrderId);
			}
		}
			
		}
		
	
	public void getShippingMethod(CricketOrder order,DynamoHttpServletRequest pReq,DynamoHttpServletResponse pRes) throws ServletException, IOException{
		
		try {
			String shippingMethod=order.getShippingMethod();
			if(shippingMethod.equalsIgnoreCase(CricketCommonConstants.SHIPPING_METHOD_OVERNIGHT))
				pReq.setParameter(SHIPPING_METHOD_OUTPUT, CricketCommonConstants.DISPLAY_SHIPPING_OVERNIGHT );
			else
				pReq.setParameter(SHIPPING_METHOD_OUTPUT, CricketCommonConstants.DISPLAY_SHIPPING_PO_BOX);
		} catch (Exception e) {
			if(isLoggingError()) {
				logError("Error in getting shipping group for the order ="+order.getId());
			}
		}
		
		
		
	}
	
	
	public Repository getOrderRepository() {
		return orderRepository;
	}


	public void setOrderRepository(Repository orderRepository) {
		this.orderRepository = orderRepository;
	}


	public OrderManager getOrderManager() {
		return orderManager;
	}


	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}
	
}
