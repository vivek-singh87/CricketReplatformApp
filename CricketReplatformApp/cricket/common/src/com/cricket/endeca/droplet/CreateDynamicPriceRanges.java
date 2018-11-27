package com.cricket.endeca.droplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.DynamoServlet;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.endeca.price.PriceRangeVO;
import com.endeca.infront.cartridge.model.Refinement;

public class CreateDynamicPriceRanges extends DynamoServlet {
	
	private int rangeStep;
	
	private String navLinkPrefix = "|BTWN+";
	
	private String navLinkPrefixFinal = "|GT+";
	
	private boolean isPriceRangeStatic = true;
	
	private String staticPriceRangeValues;

	public void service(DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) {
		if(isLoggingDebug()) {
			logDebug("entering into service method of CreateDynamicPriceRanges");
		}
		List<Refinement> refinements = (List<Refinement>)pReq.getObjectParameter("refinements");
		Map<String, PriceRangeVO> priceVOMap = new HashMap<String, PriceRangeVO>();
		List<String> keysList = new ArrayList<String>();
		if (refinements != null) {
			List<Float> priceValues = new ArrayList<Float>();
			String navStateNoPrice = CricketCommonConstants.EMPTY_STRING;
			for (Refinement refinement : refinements) {
				String label = refinement.getLabel();
				float labelFloat = Float.parseFloat(label);
				priceValues.add(labelFloat);
				if (navStateNoPrice.isEmpty()) {
					navStateNoPrice = refinement.getNavigationState();
				}
			}
			Collections.sort(priceValues);
			if (isPriceRangeStatic) {
				String[] ranges = staticPriceRangeValues.split("\\|");
				for (String range : ranges) {
					String[] pair = range.split("-");
					StringBuilder rangeDisplayBuilder = new StringBuilder();
					StringBuilder navLinkBuilder = new StringBuilder();
					if(isValidRange(pair, priceValues)) {
						if(pair.length > 1) {
							rangeDisplayBuilder.append(CricketCommonConstants.SYM_DOLLAR);
							rangeDisplayBuilder.append(pair[0]);
							rangeDisplayBuilder.append(" - ");
							rangeDisplayBuilder.append(CricketCommonConstants.SYM_DOLLAR);
							rangeDisplayBuilder.append(pair[1]);
							navLinkBuilder.append(navLinkPrefix);
							navLinkBuilder.append(pair[0]);
							navLinkBuilder.append(CricketCommonConstants.SYM_PLUS);
							navLinkBuilder.append(pair[1]);
						} else {
							rangeDisplayBuilder.append(CricketCommonConstants.SYM_DOLLAR);
							rangeDisplayBuilder.append(pair[0].replace(CricketCommonConstants.SYM_PLUS, CricketCommonConstants.EMPTY_STRING));
							rangeDisplayBuilder.append(" +");
							navLinkBuilder.append(navLinkPrefixFinal);
							navLinkBuilder.append(pair[0].replace(CricketCommonConstants.SYM_PLUS, CricketCommonConstants.EMPTY_STRING));
						}
						keysList.add(rangeDisplayBuilder.toString());
						PriceRangeVO newVO = new PriceRangeVO();
						newVO.setRangeDisplay(rangeDisplayBuilder.toString());
						newVO.setNavLink(navLinkBuilder.toString());
						priceVOMap.put(rangeDisplayBuilder.toString(), newVO);
					}
				}
			} else {
				String key = null;
				long quotient = 0;
				if(priceValues.get(priceValues.size() - 1) > rangeStep) {
					for(double priceValue : priceValues) {
						quotient = (int)(priceValue/rangeStep);
						long key1 = rangeStep*quotient;
						String key2 = CricketCommonConstants.HYPHEN_SYM;
						long key3 = rangeStep*(quotient+1);
						StringBuilder keyB = new StringBuilder();
						keyB.append(key1);
						keyB.append(key2);
						keyB.append(key3);
						key = keyB.toString();
						if(!priceVOMap.containsKey(key)) {
							PriceRangeVO priceVO = new PriceRangeVO();
							String navLink = createNavLink(quotient, false);
							priceVO.setNavLink(navLink);
							priceVO.setRangeDisplay(key);
							priceVOMap.put(key, priceVO);
							keysList.add(key);
						}
					}
					if(key != null && !key.isEmpty()) {
						PriceRangeVO priceVOFinal = new PriceRangeVO();
						String[] keyPair = key.split(CricketCommonConstants.HYPHEN_SYM);
						String keyFinal = keyPair[0] + " +";
						String navLinkFinal = createNavLink(quotient, true);
						priceVOMap.remove(key);
						keysList.remove(key);
						priceVOFinal.setNavLink(navLinkFinal);
						priceVOFinal.setRangeDisplay(keyFinal);
						priceVOMap.put(keyFinal, priceVOFinal);
						keysList.add(keyFinal);
					}
				}
			}
			if(isLoggingDebug()) {
				logDebug("priceVOMap::::" + priceVOMap);
				logDebug("navStateNoPrice::::" + navStateNoPrice);
				logDebug("keysList::::" + keysList);
			}
			pReq.setParameter("priceVOMap", priceVOMap);
			pReq.setParameter("navStateNoPrice", navStateNoPrice);
			pReq.setParameter("keysList", keysList);
			try {
				pReq.serviceParameter(CricketCommonConstants.OUTPUT, pReq, pRes);
			} catch (ServletException e) {
				if(isLoggingError()) {
					logError("there was a problem servicing content");
				}
			} catch (IOException e) {
				if(isLoggingError()) {
					logError("there was a problem servicing content");
				}
			}
		}
		if(isLoggingDebug()) {
			logDebug("exiting from service method of CreateDynamicPriceRanges");
		}
	}
	
	private boolean isValidRange(String[] pair, List<Float> priceValues) {
		for (Float price : priceValues) {
			if(pair.length > 1) {
				float lowerLimit = Float.parseFloat(pair[0]);
				float upperLimit = Float.parseFloat(pair[1]);
				if(lowerLimit <= price && price <= upperLimit) {
					return true;
				}
			} else {
				String lowerLimitString = pair[0].replace(CricketCommonConstants.SYM_PLUS, CricketCommonConstants.EMPTY_STRING);
				float lowerLimit = Float.parseFloat(lowerLimitString);
				if (price >= lowerLimit) {
					return true;
				}
			}
			
		}
		return false;
	}

	private String createNavLink(long quotient, boolean isFinal) {
		StringBuilder linkB = new StringBuilder();
		if (isFinal) {
			linkB.append(navLinkPrefixFinal);
			linkB.append(rangeStep*quotient);
		} else {
			linkB.append(navLinkPrefix);
			linkB.append(rangeStep*quotient);
			linkB.append(CricketCommonConstants.SYM_PLUS);
			linkB.append(rangeStep*(quotient+1));
		}
		return linkB.toString();
	}
	
	public int getRangeStep() {
		return rangeStep;
	}

	public void setRangeStep(int rangeStep) {
		this.rangeStep = rangeStep;
	}

	public String getNavLinkPrefix() {
		return navLinkPrefix;
	}

	public void setNavLinkPrefix(String navLinkPrefix) {
		this.navLinkPrefix = navLinkPrefix;
	}

	public String getNavLinkPrefixFinal() {
		return navLinkPrefixFinal;
	}

	public void setNavLinkPrefixFinal(String navLinkPrefixFinal) {
		this.navLinkPrefixFinal = navLinkPrefixFinal;
	}

	public String getStaticPriceRangeValues() {
		return staticPriceRangeValues;
	}

	public void setStaticPriceRangeValues(String staticPriceRangeValues) {
		this.staticPriceRangeValues = staticPriceRangeValues;
	}
	
}