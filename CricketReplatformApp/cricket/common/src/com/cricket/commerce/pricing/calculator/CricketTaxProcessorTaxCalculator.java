package com.cricket.commerce.pricing.calculator;

import java.util.Locale;
import java.util.Map;

import com.cricket.commerce.order.CricketOrderImpl;

import atg.commerce.order.Order;
import atg.commerce.pricing.PricingException;
import atg.commerce.pricing.TaxPriceInfo;
import atg.commerce.pricing.TaxProcessorTaxCalculator;
import atg.repository.RepositoryItem;

public class CricketTaxProcessorTaxCalculator extends TaxProcessorTaxCalculator {
	
	@SuppressWarnings("rawtypes")
	@Override
	public void priceTax(TaxPriceInfo priceQuote, Order order,
			RepositoryItem pricingModel, Locale locale,
			RepositoryItem profile, Map extraParameters)
			throws PricingException {
		super.priceTax(priceQuote, order, pricingModel, locale, profile, extraParameters);
		
		CricketOrderImpl cricketOrder = (CricketOrderImpl) order;
		if(null!= cricketOrder){
			if(null != cricketOrder.getTotalTax()){
				priceQuote.setAmount(cricketOrder.getTotalTax());
				//cricketOrder.setTotalTax(0.0);
			}
		}
	}
}