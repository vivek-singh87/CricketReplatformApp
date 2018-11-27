package com.cricket.commerce.endeca.index;

import atg.commerce.endeca.alias.ActivePriceNameGenerator;
import atg.commerce.pricing.priceLists.PriceListException;
import atg.commerce.pricing.priceLists.PriceListManager;
import atg.nucleus.GenericService;
import atg.repository.RepositoryItem;
import atg.repository.search.indexing.Context;
import atg.repository.search.indexing.IndexingOutputConfig;
import atg.repository.search.indexing.specifier.OutputProperty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CricketActivePriceCalculator extends atg.commerce.endeca.index.ActivePriceCalculator
{
  public static String CLASS_VERSION = "$Id: //product/DCS/version/10.2/Java/atg/commerce/endeca/index/ActivePriceCalculator.java#2 $$Change: 768796 $";

  protected static String LIST_PRICE_PROPERTY = "listPrice";
  private PriceListManager mPriceListManager;
  private ActivePriceNameGenerator mActivePriceNameGenerator;
  private boolean mDynamicActivePriceName = false;

  private String mDefaultActivePriceName = "record.activePrice";

  public PriceListManager getPriceListManager()
  {
    return this.mPriceListManager;
  }

  public void setPriceListManager(PriceListManager pPriceListManager)
  {
    this.mPriceListManager = pPriceListManager;
  }

  public ActivePriceNameGenerator getActivePriceNameGenerator()
  {
    return this.mActivePriceNameGenerator;
  }

  public void setActivePriceNameGenerator(ActivePriceNameGenerator pActivePriceNameGenerator)
  {
    this.mActivePriceNameGenerator = pActivePriceNameGenerator;
  }

  public boolean isDynamicActivePriceName()
  {
    return this.mDynamicActivePriceName;
  }

  public void setDynamicActivePriceName(boolean pDynamicActivePriceName)
  {
    this.mDynamicActivePriceName = pDynamicActivePriceName;
  }

  public String getDefaultActivePriceName()
  {
    return this.mDefaultActivePriceName;
  }

  public void setDefaultActivePriceName(String pDefaultActivePriceName)
  {
    this.mDefaultActivePriceName = pDefaultActivePriceName;
  }

  public Map<String, Object> calculateActivePrice(RepositoryItem pSku, Context pContext)
  {
    Map activePriceMap = new HashMap();

    String[] allowablePriceLists = (String[])pContext.getAttribute("atg.priceListVariantProdcer.priceListIds");

    if (allowablePriceLists == null) {
      vlogDebug("{0} is not set on the indexing context, active price for SKU {1} will not be added to the index.", new Object[] { "atg.priceListVariantProdcer.priceListIds", pSku });

      return activePriceMap;
    }

    Object activePrice = null;
    for (String priceListId : allowablePriceLists) {
      try {
        RepositoryItem priceList = getPriceListManager().getPriceList(priceListId);

        if (priceList != null) {
          RepositoryItem price = getPriceListManager().getPrice(priceList, null, pSku);

          if (price != null) {
            Object p = price.getPropertyValue(LIST_PRICE_PROPERTY);

            if (activePrice == null) {
              activePrice = p;
              break;
            }
          }
        }
      }
      catch (PriceListException e) {
        vlogError(e, "An exception has occured determining the active price for SKU {0}", new Object[] { pSku });
      }

    }

    if (activePrice != null) {
      if (isDynamicActivePriceName()) {
        activePriceMap.put(getActivePriceNameGenerator().generateActivePriceName(Arrays.asList(allowablePriceLists)), activePrice);
      }
      else
      {
        vlogDebug("Adding an active price for SKU {0}, dynamic price name calculation is disabled. If more than one price should exist in this record it must be enabled.", new Object[] { pSku.getRepositoryId() });

        activePriceMap.put(getDefaultActivePriceName(), activePrice);
      }
    }

    return activePriceMap;
  }

  public boolean ownsDynamicPropertyName(String pOutputPropertyName, IndexingOutputConfig pConfig, OutputProperty pOutputProperty)
  {
    return getActivePriceNameGenerator().ownsDynamicPropertyName(pOutputPropertyName, pConfig, pOutputProperty);
  }
}