package com.cricket.common.util.formhandler;

import java.io.IOException;

import atg.commerce.CommerceException;
import atg.commerce.order.Order;
import atg.commerce.order.OrderManager;
import atg.core.util.StringUtils;
import atg.droplet.DropletException;
import atg.droplet.GenericFormHandler;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

import com.cricket.exceptions.CricketException;
import com.cricket.integration.esp.CricketESPAdapter;
import com.cricket.integration.esp.vo.InquireBillingOrderDetailsRequestVO;
import com.cricket.integration.esp.vo.InquireBillingOrderDetailsResponseVO;

public class CustSrvcOrderDetailsFormHandler extends GenericFormHandler {

	private CricketESPAdapter cricketESPAdapter;
	private InquireBillingOrderDetailsResponseVO responseVO;
	String redirectURL;
	String billingOrderID;
	String successURL;
	String errorURL;
	String custPassword;
	String storedPassword;
	private OrderManager orderManager;
	private Repository orderRepository;

	public boolean handleRetrieveOrder(DynamoHttpServletRequest pReq,
			DynamoHttpServletResponse pRes) throws IOException {

		if (isLoggingDebug()) {
			logDebug("billing order ID submitted : " + getBillingOrderID());
		}
		if (getFormError()) {
			if (getErrorURL() != null) {
				pRes.sendLocalRedirect(getErrorURL(), pReq);
				return false;
			}
			return true;
		}
		if (getCustPassword() == null
				|| !getCustPassword().equalsIgnoreCase(getStoredPassword())) {
			addFormException(new DropletException(
					"RetrieveBillingOrderDetailsFormHandler:Incorrect Password Please try again:"));
			return false;

		}

		InquireBillingOrderDetailsRequestVO requestVO = new InquireBillingOrderDetailsRequestVO();

		try {
			requestVO.setBillingOrderNumber(getBillingOrderID());
			setResponseVO(callInquireOrderService(requestVO));
			if (getResponseVO() != null
					&& getResponseVO().getBillingOrderNumber() != null
					&& !(getResponseVO().getBillingOrderNumber().isEmpty())
					&& getSuccessURL() != null) {
				pRes.sendLocalRedirect(getSuccessURL(), pReq);
				return false;
			} else if (getErrorURL() != null) {
				responseVO = new InquireBillingOrderDetailsResponseVO();
				responseVO.setIfOrderExist(false);
				pRes.sendLocalRedirect(getErrorURL(), pReq);
				return false;
			}

			return true;
		} catch (Exception e) {
			addFormException(new DropletException(
					"RetrieveBillingOrderDetailsFormHandler:Error occured while getting order details:",
					e));
			logError(e);
		}
		// pRes.sendRedirect(getRedirectURL(), false);
		return true;
	}

	private Order getOrderByBillingSystemID(String billingOrderID) {
		Object lParams[] = null;
		String rql = null;
		RepositoryItem[] lItems = null;
		Order order = null;
		if (!StringUtils.isBlank(billingOrderID)) {
			rql = "billingSystemOrderId =? 0";
			lParams = new Object[1];
			lParams[0] = billingOrderID;
		}
		if (!StringUtils.isBlank(rql) && lParams != null) {
			try {
				RepositoryView lRepositoryView = getOrderRepository().getView(
						"order");
				RqlStatement lRqlStatement = RqlStatement
						.parseRqlStatement(rql);
				lItems = lRqlStatement.executeQuery(lRepositoryView, lParams);

				if (lItems != null && lItems.length > 0) {
					String lOrderNumber = lItems[0].getRepositoryId();
					if (getOrderManager().orderExists(lOrderNumber)) {
						order = getOrderManager().loadOrder(lOrderNumber);
					}
				} else {
				}
			} catch (RepositoryException e) {
				if (isLoggingError()) {
					logError(e);
				}

			} catch (CommerceException e) {
				if (isLoggingError()) {
					logError(e);
				}
			}

		}
		return order;
	}

	/**
	 * The method is used to get the order details based on the order id
	 * 
	 * @param requestVO
	 *            - InquireBillingOrderDetailsRequestVO
	 * */
	private InquireBillingOrderDetailsResponseVO callInquireOrderService(
			InquireBillingOrderDetailsRequestVO requestVO) {
		if (isLoggingDebug()) {
			logDebug("inside callInquireOrderService");
		}

		InquireBillingOrderDetailsResponseVO responseVO = null;
		if (requestVO != null) {
			try {
				responseVO = getCricketESPAdapter()
						.getInquireBillingOrderDetails(requestVO);
			} catch (CricketException e) {
				if (isLoggingError()) {
					logError("callInquireOrderService:Error while fetching order details from ESP : "
							+ e.getMessage());
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

	public String getCustPassword() {
		return custPassword;
	}

	public void setCustPassword(String custPassword) {
		this.custPassword = custPassword;
	}

	public String getStoredPassword() {
		return storedPassword;
	}

	public void setStoredPassword(String storedPassword) {
		this.storedPassword = storedPassword;
	}

	public OrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public Repository getOrderRepository() {
		return orderRepository;
	}

	public void setOrderRepository(Repository orderRepository) {
		this.orderRepository = orderRepository;
	}

}
