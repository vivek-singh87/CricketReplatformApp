package com.cricket.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import atg.core.util.StringUtils;
import atg.nucleus.naming.ComponentName;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.integration.esp.vo.OfferingsVO;
import com.cricket.integration.esp.vo.SubscriberVO;
import com.cricket.user.session.UserAccountInformation;
import com.cricket.vo.CricketProfile;

/**
 * This droplet is used to display the logged-in user's addons section in addon listing page.
 *
 */
public class GetUserAddonsDroplet extends DynamoServlet {

	private ComponentName mSessionBeanComponentName = null;
	
	private String mSessionBeanPath;
	
	private String mCktProfilePath;
	
	private ComponentName mCktProfileComponentName = null;
	
	/**
	 * @return the sessionBeanPath
	 */
	public String getCktProfilePath() {
		return mCktProfilePath;
	}

	/**
	 * @param pSessionBeanPath the sessionBeanPath to set
	 */
	public void setCktProfilePath(String pCktProfilePath) {
		mCktProfilePath = pCktProfilePath;
		if (mCktProfilePath != null) {
			mCktProfileComponentName = ComponentName.getComponentName(mCktProfilePath);
		} else {
			mCktProfileComponentName = null;
		}
	}
	
	/**
	 * @return the sessionBeanPath
	 */
	public String getSessionBeanPath() {
		return mSessionBeanPath;
	}

	/**
	 * @param pSessionBeanPath the sessionBeanPath to set
	 */
	public void setSessionBeanPath(String pSessionBeanPath) {
		mSessionBeanPath = pSessionBeanPath;
		if (mSessionBeanPath != null) {
			mSessionBeanComponentName = ComponentName.getComponentName(mSessionBeanPath);
		} else {
			mSessionBeanComponentName = null;
		}
	}
	
	/**
	 * @return the sessionBeanComponentName
	 */
	public ComponentName getSessionBeanComponentName() {
		return mSessionBeanComponentName;
	}

	/**
	 * @param pSessionBeanComponentName the sessionBeanComponentName to set
	 */
	public void setSessionBeanComponentName(ComponentName pSessionBeanComponentName) {
		mSessionBeanComponentName = pSessionBeanComponentName;
	}
	@Override
	public void service(DynamoHttpServletRequest pReq,
			DynamoHttpServletResponse pRes) throws ServletException,
			IOException {
		vlogDebug("Start of GetUserAddonsDroplet's service method");
		Map<String, List<String>> products = new HashMap<String, List<String>>();
		String billingAccountNumber = pReq.getParameter(CricketCommonConstants.BILLING_ACCT_NUM);
		vlogDebug("The account number is :::::::"+ billingAccountNumber);
		if(!StringUtils.isBlank(billingAccountNumber)) {
			List<String> additionalOfferingProducts = new ArrayList<String>();
			List<String> bundledOfferingProducts = new ArrayList<String>();
			List<String> userPurchasedOfferingProducts = new ArrayList<String>();
			UserAccountInformation userAccountInformation = (UserAccountInformation) pReq.resolveName(mSessionBeanComponentName);
			CricketProfile cktProfile = (CricketProfile) pReq.resolveName(mCktProfileComponentName);
			String mdn = cktProfile.getMdn();
			if(!StringUtils.isEmpty(mdn) && null != userAccountInformation) {
				List<SubscriberVO> subscribers = userAccountInformation.getSubscribers();
				if(subscribers != null && subscribers.size() > 0) {
					for (SubscriberVO subscriberVO : subscribers) {
						if (null != subscriberVO && mdn.equalsIgnoreCase(subscriberVO.getMdn())) {
							List<OfferingsVO> additionalOfferings = subscriberVO.getAdditionalOfferings();
							for (OfferingsVO additionalOffer : additionalOfferings) {
								if(!StringUtils.isBlank(additionalOffer.getOfferingName())) {
									additionalOfferingProducts.add(additionalOffer.getOfferingName());
									if (additionalOffer.getOfferTypeId() != 3
											&& !CricketCommonConstants.REC_OFFERING_NAME.equals(additionalOffer.getOfferingName())) {
										userPurchasedOfferingProducts.add(additionalOffer.getOfferingName());
									}
									
								}
							}
							vlogDebug("For MDN ::::: "+ mdn +", The Additional Offering Products are ::::::" + additionalOfferingProducts);
							
							List<OfferingsVO> bundledOfferings = subscriberVO.getBundledOfferings();
							for (OfferingsVO bundledOffer : bundledOfferings) {
								if(!StringUtils.isBlank(bundledOffer.getOfferingValue())) {
									bundledOfferingProducts.add(bundledOffer.getOfferingValue());
								}
							}
							vlogDebug("For MDN ::::: "+ mdn +", The Bundled Offering Products are ::::::" + bundledOfferingProducts);
						}
						
					}
					products.put(CricketCommonConstants.ADD_OFFER_PRODUCTS, additionalOfferingProducts);
					products.put(CricketCommonConstants.BUNDLE_OFFER_PRDCTS, bundledOfferingProducts);
					cktProfile.setBundledOfferingProducts(bundledOfferingProducts);
					cktProfile.setAdditionalOfferingProducts(additionalOfferingProducts);
					cktProfile.setUserPurchasedOfferingProducts(userPurchasedOfferingProducts);
					pReq.setParameter(CricketCommonConstants.ADDON_PRDCTS, products);
					pReq.serviceParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
				} else {
					vlogDebug("Subscribers are coming empty or null");
				}
			} else {
				logError("MDN or UserAccountInformation is null");
			}
		} else {
			vlogDebug("billingAccountNumber is null");
		}
	}

}
