package com.cricket.checkout.order;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.transaction.Transaction;

import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketPurchaseProcessHelper;
import com.cricket.common.constants.CricketCommonConstants;

import atg.commerce.CommerceException;
import atg.commerce.order.purchase.PurchaseProcessFormHandler;
import atg.commerce.util.RepeatingRequestMonitor;
import atg.core.util.StringUtils;
import atg.droplet.DropletFormException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;


public class CricketTelesalesFormHandler extends PurchaseProcessFormHandler{
	
	private String telesalesCode;
	
	private boolean removeTelesalesCode;
	

	public void handleApplyTelesalesCode (DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) throws IOException {
		
		RepeatingRequestMonitor rrm = getRepeatingRequestMonitor();
		String myHandleMethod = CricketCommonConstants.TELE_SALES_HANDLE_METHOD;
		if ((rrm == null) || (rrm.isUniqueRequestEntry(myHandleMethod))) {
			Transaction tr = null;
			try {
				tr = ensureTransaction();
				synchronized (getOrder()) {					
					if(!getRemoveTelesalesCode())
					{
						if (isLoggingDebug()) {			 					
					   		logDebug("[CricketTelesalesFormHandler->handleApplyTelesalesCode()] if getRemoveTelesalesCode is true ..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + getOrder().getId());
					       }
						if (!StringUtils.isEmpty(getTelesalesCode())) {
							if (isLoggingDebug()) {			 					
						   		logDebug("[CricketTelesalesFormHandler->handleApplyTelesalesCode()] if TelesalesCode is not empty ..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + getOrder().getId());
						       }
						((CricketOrderImpl) getOrder()).setTeleSaleCode(getTelesalesCode());
							getOrderManager().updateOrder(getOrder());
						} else {
							addFormException(new DropletFormException(CricketPurchaseProcessHelper.MSG_EMPTY_TELESALES_CODE, CricketCommonConstants.EMPTY_STRING));
						}
					} else {
						((CricketOrderImpl) getOrder()).setTeleSaleCode(null);
						getOrderManager().updateOrder(getOrder());
					}
				}
		
			}  catch (CommerceException commerceException) {
				if (isLoggingError()) {
		       		  vlogError("CommerceException Occured while updating order with ID :" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  getOrder().getId(), commerceException);
		            }
			} catch (Exception exception) {
				if (isLoggingError()) {
		       		  vlogError("Exception Occured while updating order with ID :" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  getOrder().getId(), exception);
		            }
			} finally {
				if (tr != null) {
					commitTransaction(tr);
				}
				if (rrm != null) {
					rrm.removeRequestEntry(myHandleMethod);
				}
			}
		
	}
	}
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	public String getTelesalesCode() {
		return telesalesCode;
	}

	public void setTelesalesCode(String pTelesalesCode) {
		telesalesCode = pTelesalesCode;
	}
	public boolean getRemoveTelesalesCode() {
		return removeTelesalesCode;
	}
	public void setRemoveTelesalesCode(boolean pRemoveTelesalesCode) {
		removeTelesalesCode = pRemoveTelesalesCode;
	}

	
}