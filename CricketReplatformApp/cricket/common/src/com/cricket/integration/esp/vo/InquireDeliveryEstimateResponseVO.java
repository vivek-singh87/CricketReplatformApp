package com.cricket.integration.esp.vo;

import java.util.List;

public class InquireDeliveryEstimateResponseVO extends ESPResponseVO {
	
	private List<DeliveryServiceVO> deliveryService; 
	
	private ResponseVO response;
	
	public List<DeliveryServiceVO> getDeliveryService() {
		return deliveryService;
	}
	public void setDeliveryService(List<DeliveryServiceVO> deliveryService) {
		this.deliveryService = deliveryService;
	}
	
	public ResponseVO getResponse() {
		return response;
	}
	public void setResponse(ResponseVO response) {
		this.response = response;
	}		
	
}
