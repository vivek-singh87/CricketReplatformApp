/**
 * InquireOrderDetailsSOAPImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mycricket.webservices.InquireOrderDetails._interface;

import atg.commerce.order.InvalidParameterException;
import atg.nucleus.GenericService;
import atg.nucleus.Nucleus;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;

import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderManager;
import com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsResponseInfo;
import com.mycricket.webservices.InquireOrderDetails.types.ResponseInfo;
import com.webservices.utils.MappingUtils;

public class InquireOrderDetailsSOAPImpl extends GenericService implements com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetails_PortType{
	
	
	private  CricketOrderManager mOrderManager;	
	
	private MappingUtils mMappingUtils;

	private static final String CRICKET_COMPONENT_ORDER = "/atg/commerce/order/OrderManager";
	
	private static final String MAPPING_UTILS ="/webservices/utils/MappingUtils";
	
	
	/**
	 * @return the mordermanager
	 */
	public CricketOrderManager getOrdermanager() {
		 if (mOrderManager == null) {
			 mOrderManager = (CricketOrderManager) Nucleus.getGlobalNucleus().resolveName(CRICKET_COMPONENT_ORDER);
	     }
	     return mOrderManager;
		 
	}
	
	public MappingUtils getMappingUtils(){
		
		 if (mMappingUtils == null) {
			 mMappingUtils = (MappingUtils) Nucleus.getGlobalNucleus().resolveName(MAPPING_UTILS);
	     }
	     return mMappingUtils;
	}
	
    public com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsResponseInfo sendCricketOrderDetails(com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsRequest inquireOrderDetailsRequest) throws java.rmi.RemoteException {
    	CricketOrderImpl cricketOrder = null;
    	RepositoryItem profileItem=null;
    	 
    	if(getMappingUtils().isLoggingDebug())
    		getMappingUtils().logDebug("Starting webservice InquireOrderDetails ----------- "+getClass().getName());
    	
		try {
			//waiting for 5 seconds to ensure order is persisted in ATG before sending email to customer
			//Exact target will call this webservices as soon as BillingOrder is generated in Billing System
			//But in ATG there are few more ESP calls to be executed(as per existing ESP Architecture) after Billing Order ESP call for the ATG Order to get persist
			try {
	            // thread to sleep for 5000 milliseconds
				Thread.sleep(5000); 
	         } catch (InterruptedException ie) {
	        	 getMappingUtils().logError("InterruptedException while executing sleep() method: ", ie);	             
	         }
			
			cricketOrder = (CricketOrderImpl) getOrdermanager().getOrderByBillingOrderNumber(inquireOrderDetailsRequest.getOrderId());
			if(null==cricketOrder)
				throw new InvalidParameterException();
			//cricketOrder = (CricketOrderImpl)getOrdermanager().loadOrder(inquireOrderDetailsRequest.getOrderId());
			 Repository rep = getOrdermanager().getOrderTools().getProfileRepository();
             try {
				profileItem = rep.getItem(cricketOrder.getProfileId(), getOrdermanager().getOrderTools().getDefaultProfileType());
			} catch (RepositoryException e) {
				getMappingUtils().logError("error while getting profie for order "+getClass().getName()  +"   "+e,e);
			}
           
		} 
		catch(InvalidParameterException e){
			getMappingUtils().logError("error while loading order "+e,e);
			InquireOrderDetailsResponseInfo response = new InquireOrderDetailsResponseInfo();
			ResponseInfo responseInfo =  new ResponseInfo();
			responseInfo.setCode("101");
			responseInfo.setDescription("Order details are not found by given Billing Order Id is not found in Repository.");
			response.setResponseInfo(responseInfo);
			return response;
			
		}
		 
		InquireOrderDetailsResponseInfo responseInfo=null;
		try {
		  responseInfo = getMappingUtils().mapOrderToCustomerOrderResponse(cricketOrder,profileItem);
		 
		  if(getMappingUtils().isLoggingDebug()){
			  getMappingUtils().logDebug("atg order response info      "+responseInfo.toString());
			  getMappingUtils().logDebug("Ending webservices InquireOrderDetails-------  "+getClass().getName());
			}
		  return responseInfo;
		}
		catch(Exception e){
			responseInfo = new InquireOrderDetailsResponseInfo();
			getMappingUtils().logError("Error while mapping order details to Inquire order respone "+e,e);
			ResponseInfo response =  new ResponseInfo();
			response.setCode("102");
			response.setDescription(e.getMessage());
			responseInfo.setResponseInfo(response);
			return responseInfo;
		}
    }

}
