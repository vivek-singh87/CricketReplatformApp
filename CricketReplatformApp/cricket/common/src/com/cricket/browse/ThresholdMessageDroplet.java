package com.cricket.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;








import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.inventory.InventoryResponseVO;
import com.cricket.inventory.InventoryUtils;

import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

public class ThresholdMessageDroplet extends DynamoServlet {
	
	private InventoryUtils inventoryUtils;

	public void service(DynamoHttpServletRequest pRequest,DynamoHttpServletResponse pResponse) throws ServletException, IOException {
		CricketOrderImpl order = (CricketOrderImpl) pRequest.getObjectParameter(CricketCommonConstants.ITEM_DESC_ORDER);
		String skuId = pRequest.getParameter(CricketCommonConstants.SKU_ID);
		String mode = pRequest.getParameter(CricketCommonConstants.MODE);
		String flow = pRequest.getParameter(CricketCommonConstants.FLOW);
		InventoryResponseVO invResponseVO = null;
		if(mode != null && mode.equals(CricketCommonConstants.ACCESSORY_QUANTITY_UPDATE)) {
			String quantityString = pRequest.getParameter(CricketCommonConstants.QUANTITY);
			long quantity = Integer.parseInt(quantityString);
			invResponseVO = getInventoryUtils().checkStockLevelForQuantityUpdate(skuId, quantity);
		} else {
			if(order != null && skuId != null) {
				if(flow != null && flow.equals(CricketCommonConstants.CHECKOUT)) {
					invResponseVO = getInventoryUtils().checkStockLevelWithCurrentCart(skuId, order, true);
				} else {
					invResponseVO = getInventoryUtils().checkStockLevelWithCurrentCart(skuId, order, false);
				}
				
			} else if(order == null && skuId != null) {
				invResponseVO = getInventoryUtils().checkStockLevel(skuId);
			}
		}
		if(invResponseVO != null) {
			pRequest.setParameter(CricketCommonConstants.THRESHOLDMESSAGE, invResponseVO.getThresholdMessage());
			pRequest.setParameter(CricketCommonConstants.IS_OUTOFSTOCK, invResponseVO.isOutOfStock());
			pRequest.serviceLocalParameter(CricketCommonConstants.OUTPUT, pRequest, pResponse);
		}
	}

	public InventoryUtils getInventoryUtils() {
		return inventoryUtils;
	}

	public void setInventoryUtils(InventoryUtils inventoryUtils) {
		this.inventoryUtils = inventoryUtils;
	}

}
