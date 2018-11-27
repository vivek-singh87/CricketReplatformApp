package com.cricket.commerce.order.payment;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import atg.commerce.order.PaymentGroup;
import atg.core.util.StringUtils;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;
import atg.servlet.ServletUtil;

import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.common.constants.CricketCommonConstants;

public class GetCreditCardDetailsDroplet extends DynamoServlet{
	/**
	 * Input Parameter : Order
	 */
	private String Order = "order";
	/**
	 * Output Parameter : EXPIREMONTH
	 */
	private String EXPIREMONTH = "expireMonth";
	/**
	 * Output Parameter : EXPIREYEAR
	 */
	private String EXPIREYEAR = "expireYear";
	/**
	 * Output Parameter : credit cart type
	 */
	private String CARDTYPE = "creditCardType";
	/**
	 * Output Parameter : credit cart number
	 */
	private String CARDNUMBER = "creditCardNumber";
	
	
	@SuppressWarnings("unchecked")
	public void service(DynamoHttpServletRequest pReq,
			DynamoHttpServletResponse pRes) throws ServletException,
			IOException {
					
				String cardExpireMonth = null;
				String cardExpireYear = null;
				String cardType = null;
				String cardNumber = null;
				
				CricketOrderImpl submittedOrder = (CricketOrderImpl) pReq
						.getObjectParameter(getOrder());
				// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(pReq.getRequestURIWithQueryString())){
					pageURL = pReq.getRequestURIWithQueryString();
				}
				// Getting the Order Id
				String orderId = CricketCommonConstants.EMPTY_STRING;
				if(null != submittedOrder){
					if(!StringUtils.isBlank(submittedOrder.getId())){
						orderId = submittedOrder.getId();
					}
				}
				if (isLoggingDebug()) {								
		    		logDebug("Entering into ProcProcessManagePayment class of runProcess() method :::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId + CricketCommonConstants.PAGE_URL + pageURL);
				}
		try {		
			if (submittedOrder != null) {
				List<PaymentGroup> PaymentGroups = (List<PaymentGroup>) submittedOrder
						.getPaymentGroups();
				CricketCreditCard creditCard = null;
				for (Object paymentGroup : PaymentGroups) {
					if (paymentGroup instanceof CricketCreditCard) {
						creditCard = (CricketCreditCard) paymentGroup;
						if (!creditCard.isDiffernetCard()) {
							cardExpireMonth = creditCard.getExpirationMonth();
							cardExpireYear = creditCard.getExpirationYear();
							cardType = creditCard.getCreditCardType();
							cardNumber = creditCard.getCreditCardNumber();
						}
					}
				}
			}
			pReq.setParameter(getEXPIREMONTH(), cardExpireMonth);
			pReq.setParameter(getEXPIREYEAR(), cardExpireYear);
			pReq.setParameter(getCARDTYPE(), cardType);
			pReq.setParameter(getCARDNUMBER(), cardNumber);
			pReq.serviceLocalParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
		} catch (Exception e) {
			if (isLoggingError()) {
	       		  vlogError("[GetCreditCardDetailsDroplet->service()]: Exception :" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID +  orderId, e);
	             }
		}
		if(isLoggingDebug()){
			logDebug("[GetCreditCardDetailsDroplet->service()]: Exiting service()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + orderId );
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
	
	/**
	 * @return the cARDTYPE
	 */
	public String getCARDTYPE() {
		return CARDTYPE;
	}
	/**
	 * @param cARDTYPE the cARDTYPE to set
	 */
	public void setCARDTYPE(String cARDTYPE) {
		CARDTYPE = cARDTYPE;
	}
	/**
	 * @return the Order
	 */
	public String getOrder() {
		return Order;
	}
	/**
	 * @paramOrder
	 */
	public void setOrder(String pOrder) {
		Order = pOrder;
	}
	/**
	 * @return EXPIREMONTH
	 */
	public String getEXPIREMONTH() {
		return EXPIREMONTH;
	}
	/**
	 * @param EXPIREMONTH
	 */
	public void setEXPIREMONTH(String pEXPIREMONTH) {
		EXPIREMONTH = pEXPIREMONTH;
	}
	/**
	 *  @return EXPIREYEAR
	 */
	public String getEXPIREYEAR() {
		return EXPIREYEAR;
	}
	/**
	 *  @param EXPIREYEAR
	 */
	public void setEXPIREYEAR(String pEXPIREYEAR) {
		EXPIREYEAR = pEXPIREYEAR;
	}
	/**
	 *  @return CARDNUMBER
	 */
	public String getCARDNUMBER() {
		return CARDNUMBER;
	}
	/**
	 *  @param CARDNUMBER
	 */
	public void setCARDNUMBER(String cARDNUMBER) {
		CARDNUMBER = cARDNUMBER;
	}
	
	

}
