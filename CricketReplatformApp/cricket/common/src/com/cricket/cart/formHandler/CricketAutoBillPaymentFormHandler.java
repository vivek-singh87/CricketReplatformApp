package com.cricket.cart.formHandler;

import java.io.IOException;

import javax.servlet.ServletException;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.user.session.UserSessionBean;

import atg.droplet.GenericFormHandler;
import atg.nucleus.naming.ComponentName;
import atg.repository.RepositoryException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

public class CricketAutoBillPaymentFormHandler extends GenericFormHandler {

	private String mSessionBeanPath;
	private ComponentName mSessionBeanComponentName = null;
	public String autoBillCheckBox;

	public void handleSubmitAutoBillPayment(final DynamoHttpServletRequest pRequest,
			final DynamoHttpServletResponse pResponse) throws ServletException,
			IOException, RepositoryException {

		UserSessionBean userSessionBean = null;
		userSessionBean = (UserSessionBean) pRequest.resolveName(mSessionBeanComponentName);
		autoBillCheckBox = pRequest.getParameter(CricketCommonConstants.AUTO_BILL_PAYMENT);

		if (autoBillCheckBox != null) {
			if (autoBillCheckBox.equalsIgnoreCase(CricketCommonConstants.TRUE)) {
				userSessionBean.setAutoBillPayment(true);
				if (isLoggingDebug()) {
					logDebug("User selected Auto Bill Payment option");
				}
			} else {
				userSessionBean.setAutoBillPayment(false);
				if (isLoggingDebug()) {
					logDebug("User not selected Auto Bill Payment option");
				}
			}
		}

	}

	public String getSessionBeanPath() {
		return mSessionBeanPath;
	}

	public void setSessionBeanPath(final String pSessionBeanPath) {
		mSessionBeanPath = pSessionBeanPath;
		
		if (mSessionBeanPath == null) {
			mSessionBeanComponentName = null;
		}else{
			mSessionBeanComponentName = ComponentName
					.getComponentName(mSessionBeanPath);
		} 
	}

	public ComponentName getSessionBeanComponentName() {
		return mSessionBeanComponentName;
	}

	public void setSessionBeanComponentName(final ComponentName pSessionBeanComponentName) {
		mSessionBeanComponentName = pSessionBeanComponentName;
	}

}
