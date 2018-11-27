package com.cricket.common.util.formhandler;

import java.io.IOException;

import atg.droplet.DropletException;
import atg.droplet.GenericFormHandler;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.InquireBillingOrderDetailsRequestVO;
import com.cricket.integration.esp.vo.InquireBillingOrderDetailsResponseVO;


	public class RetrieveBillingOrderDetailsFormHandler extends GenericFormHandler {
		
		
		
		private CricketESPAdapter cricketESPAdapter;
		private InquireBillingOrderDetailsResponseVO responseVO;
		String redirectURL;
		String billingOrderID;
		String successURL;
		String errorURL;
		
		public boolean handleRetrieveOrder (DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) throws IOException {
			
			if(isLoggingDebug()) {
				logDebug("billing order ID submitted : "+getBillingOrderID());
			}
			 if (getFormError()) {
			      if (getErrorURL() != null) {
			    	  pRes.sendLocalRedirect(getErrorURL(),pReq);
			        return false;
			      }
			      return true;
			    }
			InquireBillingOrderDetailsRequestVO requestVO=new InquireBillingOrderDetailsRequestVO();
			
			try {
				requestVO.setBillingOrderNumber(getBillingOrderID());
				setResponseVO(callInquireOrderService(requestVO));
				if (getResponseVO() != null && getResponseVO().getBillingOrderNumber()!= null && !(getResponseVO().getBillingOrderNumber().isEmpty()) && getSuccessURL() != null) {
					 pRes.sendLocalRedirect(getSuccessURL(),pReq);
				     return false;
				 }
				else if(getErrorURL() !=null){
					responseVO=new InquireBillingOrderDetailsResponseVO();
					responseVO.setIfOrderExist(false);
					pRes.sendLocalRedirect(getErrorURL(),pReq);
					return false;
					}
				
				    return true;
			} catch (Exception e) {
				addFormException(new DropletException("RetrieveBillingOrderDetailsFormHandler:Error occured while getting order details:",e));
				logError(e);
			}	
			//pRes.sendRedirect(getRedirectURL(), false); 
			 return true;
		}
		
		/**
		 * The method is used to get the order details based on the order id 
		 * @param requestVO - InquireBillingOrderDetailsRequestVO
		 * */
		private InquireBillingOrderDetailsResponseVO callInquireOrderService(InquireBillingOrderDetailsRequestVO requestVO) {
			if (isLoggingDebug()) {
				logDebug("inside callInquireOrderService");
			}

			InquireBillingOrderDetailsResponseVO responseVO=null;
			if (requestVO != null) {						
				try {
					responseVO=getCricketESPAdapter().getInquireBillingOrderDetails(requestVO);
					} catch (CricketException e) {
					if (isLoggingError()) {
						logError("callInquireOrderService:Error while fetching order details from ESP : ", e);
					}
				}
			}
			return responseVO;
			
		}

		public String getRedirectURL() {
			return redirectURL;
		}

		public void setRedirectURL(String redirectURL) {
			this.redirectURL = redirectURL;
		}

		public String getSuccessURL() {
			return successURL;
		}

		public void setSuccessURL(String successURL) {
			this.successURL = successURL;
		}

		public String getErrorURL() {
			return errorURL;
		}

		public void setErrorURL(String errorURL) {
			this.errorURL = errorURL;
		}

		public String getBillingOrderID() {
			return billingOrderID;
		}

		public void setBillingOrderID(String billingOrderID) {
			this.billingOrderID = billingOrderID;
		}

		public CricketESPAdapter getCricketESPAdapter() {
			return cricketESPAdapter;
		}

		public void setCricketESPAdapter(CricketESPAdapter cricketESPAdapter) {
			this.cricketESPAdapter = cricketESPAdapter;
		}

		public InquireBillingOrderDetailsResponseVO getResponseVO() {
			return responseVO;
		}

		public void setResponseVO(InquireBillingOrderDetailsResponseVO responseVO) {
			this.responseVO = responseVO;
		}
		
		
	}
