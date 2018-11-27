package com.cricket.pricing;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import atg.commerce.CommerceException;
import atg.commerce.order.purchase.PurchaseProcessFormHandler;
import atg.core.util.StringUtils;
import atg.droplet.DropletFormException;
import atg.repository.RepositoryException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;

import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderManager;
import com.cricket.commerce.order.CricketPurchaseProcessHelper;
import com.cricket.common.constants.CricketCommonConstants;


public class CricketCouponFormHandler extends PurchaseProcessFormHandler {
	
	private String mCouponCode;

	private String mApplyCouponSuccessURL;

	private String mApplyCouponErrorURL;

	private String mEditCouponSuccessURL;

	private String mEditCouponErrorURL;

	private String mRemoveCouponSuccessURL;

	private String mRemoveCouponErrorURL;
	  

	public CricketOrderImpl getOrder() {
		    return (CricketOrderImpl)super.getOrder();
		  }
	 
	 
	  public String getCurrentCouponCode() {
		    try {
		    	CricketOrderManager orderManager = (CricketOrderManager)getOrderManager();
		    	 return orderManager.getCouponCode(getOrder());
		    } 
		    catch (CommerceException e) {
		    	
		      return null;
		    }
	}
	  
	public void addCouponException() {

		String ErrorMessage = CricketPurchaseProcessHelper.couponErrorMessage;
		if (ErrorMessage != null) {
			if (ErrorMessage.equalsIgnoreCase(CricketCommonConstants.EXPIRE_PROMO_CODE_ERROR)) {
				addFormException(new DropletFormException(CricketCommonConstants.EXPIRE_PROMO_CODE_MESSAGE, CricketCommonConstants.EMPTY_STRING));
				if (isLoggingDebug()) {
					logDebug("Promo Code is Expired");
				}
			} else if (ErrorMessage.equalsIgnoreCase(CricketCommonConstants.COUPON_CODE_EXPIRED_MESSAGE)){
				addFormException(new DropletFormException(CricketCommonConstants.COUPON_CODE_EXPIRED_MESSAGE, CricketCommonConstants.EMPTY_STRING));
				if (isLoggingDebug()) {
					logDebug("Coupon Code is Expired");
				}
			} else if (ErrorMessage.equalsIgnoreCase(CricketCommonConstants.ENTER_PROMO_CODE_ERROR)) {
				addFormException(new DropletFormException(CricketCommonConstants.ENTER_PROMO_CODE_ERROR_MESSAGE,CricketCommonConstants.EMPTY_STRING));
				if (isLoggingDebug()) {
					logDebug("Enter a Promo Code");
				}
			} else {
				addFormException(new DropletFormException(
						CricketCommonConstants.INVALID_PROMO_CODE_MESSAGE, CricketCommonConstants.EMPTY_STRING));
				if (isLoggingDebug()) {
					logDebug("Enter a valid Promo Code");
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * This handle method claims a coupon specified by its code for a specific user and
	 * order and reprices the order.
	 * 	
	 */
	public boolean handleClaimCoupon(DynamoHttpServletRequest pRequest,
			DynamoHttpServletResponse pResponse) throws ServletException,
			IOException, RepositoryException {
		
			// Getting the Page url
			String pageURL = CricketCommonConstants.EMPTY_STRING;
			if(!StringUtils.isBlank(pRequest.getRequestURIWithQueryString())){
				pageURL = pRequest.getRequestURIWithQueryString();
			}
			// Getting the Session Id
			String sessionId = CricketCommonConstants.EMPTY_STRING;
			 HttpSession session = ServletUtil.getCurrentRequest().getSession();			 
			 if(null != session) {
				 sessionId = session.getId();
			 }
			 // Getting the Order Id
			String orderId = CricketCommonConstants.EMPTY_STRING;
			if(null != getOrder()){
				if(!StringUtils.isBlank(getOrder().getId())){
					orderId = getOrder().getId();
				}
			}
		if (isLoggingDebug()) {								
	    		logDebug("Entering into CricketCouponFormHandler class of handleClaimCoupon() method :::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		try {
			/*
			 * Attempt to claim the specified coupon
			 */
			boolean couponTendered = ((CricketPurchaseProcessHelper)getPurchaseProcessHelper()).tenderCoupon(getCouponCode().toUpperCase(),getOrder(),getProfile(),
							getUserPricingModels(), getUserLocale());			
		
		} catch (CommerceException commerceException) {
			processException(commerceException,
					CricketPurchaseProcessHelper.MSG_UNCLAIMABLE_COUPON,
					pRequest, pResponse);
		} finally{
			/* Add Coupon Exception to Form Exception
			 */  
			addCouponException();		
		}
		if (isLoggingDebug()) {								
	    		logDebug("Exiting fromo CricketCouponFormHandler class of handleClaimCoupon() method :::" + CricketCommonConstants.SESSION_ID + sessionId + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return checkFormRedirect(getApplyCouponSuccessURL(),
				getApplyCouponErrorURL(), pRequest, pResponse);
	}
	
	/**
	 * @return mCouponCode.
	 */
	public String getCouponCode() {
		return mCouponCode;
	}
	/**
	 * @param mCouponCode
	 *            - Coupon code to set.
	 */
	public void setCouponCode(String pCouponCode) {
		mCouponCode = pCouponCode;
	}
	/**
	 * @return the mApplyCouponSuccessURL.
	 */
	public String getApplyCouponSuccessURL() {
		return mApplyCouponSuccessURL;
	}
	/**
	 * @param mApplyCouponSuccessURL
	 *            - Coupon success URL to set.
	 */
	public void setApplyCouponSuccessURL(String pApplyCouponSuccessURL) {
		mApplyCouponSuccessURL = pApplyCouponSuccessURL;
	}
	/**
	 * @return the mApplyCouponErrorURL.
	 */
	public String getApplyCouponErrorURL() {
		return mApplyCouponErrorURL;
	}
	/**
	 * @param mApplyCouponErrorURL
	 *            - Coupon Error URL to set.
	 */
	public void setApplyCouponErrorURL(String pApplyCouponErrorURL) {
		mApplyCouponErrorURL = pApplyCouponErrorURL;
	}
	/**
	 * @return the mEditCouponSuccessURL.
	 */
	public String getEditCouponSuccessURL() {
		return mEditCouponSuccessURL;
	}
	/**
	 * @param mEditCouponSuccessURL
	 *            - Edit Coupon Success URL to set.
	 */
	public void setEditCouponSuccessURL(String pEditCouponSuccessURL) {
		mEditCouponSuccessURL = pEditCouponSuccessURL;
	}
	/**
	 * @return the mEditCouponErrorURL.
	 */
	public String getEditCouponErrorURL() {
		return mEditCouponErrorURL;
	}
	/**
	 * @param mEditCouponErrorURL
	 *            - Edit Coupon ERROR URL to set.
	 */
	public void setEditCouponErrorURL(String pEditCouponErrorURL) {
		mEditCouponErrorURL = pEditCouponErrorURL;
	}
	/**
	 * @return the mRemoveCouponSuccessURL.
	 */
	public String getRemoveCouponSuccessURL() {
		return mRemoveCouponSuccessURL;
	}
	/**
	 * @param mRemoveCouponSuccessURL
	 *            - Remove Coupon Success URL to set.
	 */
	public void setRemoveCouponSuccessURL(String pRemoveCouponSuccessURL) {
		mRemoveCouponSuccessURL = pRemoveCouponSuccessURL;
	}
	/**
	 * @return the mRemoveCouponErrorURL.
	 */
	public String getRemoveCouponErrorURL() {
		return mRemoveCouponErrorURL;
	}
	/**
	 * @param mRemoveCouponErrorURL
	 *            - Remove Coupon Error URL to set.
	 */
	public void setRemoveCouponErrorURL(String pRemoveCouponErrorURL) {
		mRemoveCouponErrorURL = pRemoveCouponErrorURL;
	}
}
