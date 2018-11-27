package com.cricket.util;

import atg.nucleus.GenericService;

public class SOAPMessageHeaderAttribute extends GenericService {
	/*holds conversationId value from the ESP response,
	 * store this value in this variable and use it in subsequent ESP calls in the session*/
	private String conversationId;

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	
}
