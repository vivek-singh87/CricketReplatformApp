package atg.commerce.pricing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import atg.commerce.CommerceException;
import atg.commerce.order.CommerceItem;
import atg.commerce.order.OrderManager;
import atg.commerce.order.OrderTools;
import atg.commerce.pricing.definition.DiscountStructure;
import atg.commerce.pricing.definition.MatchingObject;
import atg.commerce.pricing.definition.PricingModelElem;
import atg.core.util.ResourceUtils;
import atg.nucleus.GenericService;
import atg.repository.RepositoryItem;
import atg.service.dynamo.LangLicense;
import atg.service.perfmonitor.PerformanceMonitor;

/**
 * This class is to retrive all the applicable Promotion for a given product and sku.
 * @author TechM
 *
 */

public class CricketPromotionManager extends GenericService{
	
    private static ResourceBundle sResourceBundle = ResourceBundle.getBundle("atg.commerce.pricing.Resources", LangLicense.getLicensedDefault());
    private Qualifier mPricingQualifierService;
    private OrderTools mOrderTools;
    private PricingTools mPricingTools;
    
    private ItemPricingEngine mItemPricingEngine;
    
    
    public Qualifier getPricingQualifierService()
    {
        return mPricingQualifierService;
    }

    public void setPricingQualifierService(final Qualifier pPricingQualifierService)
    {
        mPricingQualifierService = pPricingQualifierService;
    }

    /**
	 * @return the mOrderTools
	 */
	public OrderTools getOrderTools() {
		return mOrderTools;
	}

	/**
	 * @param mOrderTools the mOrderTools to set
	 */
	public void setOrderTools(OrderTools mOrderTools) {
		this.mOrderTools = mOrderTools;
	}

    public void setPricingTools(final PricingTools pPricingTools)
    {
        mPricingTools = pPricingTools;
    }

    public PricingTools getPricingTools()
    {
        return mPricingTools;
    }

	public void setItemPricingEngine(final ItemPricingEngine pItemPricingEngine)
    {
        mItemPricingEngine = pItemPricingEngine;
    }

    public ItemPricingEngine getItemPricingEngine()
    {
        return mItemPricingEngine;
    }
	
   
	/**
	 * Based on the Product and sku, this method retrives all the promotions applicable and returns to the calling class.
	 * @param product
	 * @param sku
	 * @param userPricingHolder
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<RepositoryItem> getPromotions(final RepositoryItem product,
			final RepositoryItem sku, final PricingModelHolder userPricingHolder)

	{
		List<RepositoryItem> promotions = null;
		promotions = new ArrayList<RepositoryItem>();
		CommerceItem item = null;
		
		try {
			item = getItem(product, sku);
			if (isLoggingDebug()){
				logDebug((new StringBuilder()).append("item = ").append(item)
						.toString());
			}
			ItemPricingEngine itemPricingEngine = null;
			itemPricingEngine = getItemPricingEngine();
			
			if (itemPricingEngine instanceof ItemPricingEngineImpl) {
				ItemPricingEngineImpl itemPricingEngineImpl = null;
				itemPricingEngineImpl = (ItemPricingEngineImpl) itemPricingEngine;
				Collection pricingModels = userPricingHolder
						.getItemPricingModels();
				List<CommerceItem> items = null;
				items = new ArrayList<CommerceItem>(1);
				List<AmountInfo> priceQuotes = null;
				priceQuotes = new ArrayList<AmountInfo>(1);
				items.add(item);
				priceQuotes.add((itemPricingEngineImpl).createPriceInfo());
				List wrappedItems = null;
				wrappedItems= getWrappCommerceItems(items,priceQuotes);
				PricingContext pricingContext = null;
				pricingContext = getPricingTools()
						.getPricingContextFactory().createPricingContext(
								wrappedItems, null, null, null, null, null);
				HashMap<String, PricingContext> extraParameters = null;
				extraParameters = new HashMap<String, PricingContext>();
				extraParameters.put("pricingContext", pricingContext);
				
				if (itemPricingEngineImpl.getPreCalculators() != null) {
					int num ;
					num = itemPricingEngineImpl.getPreCalculators().length;
					ItemPricingCalculator calc = null;
					for (int c = 0; c < num; c++) {
						calc = itemPricingEngineImpl.getPreCalculators()[c];
						itemPricingEngineImpl.applyCalculator(calc, 0,priceQuotes, items, pricingContext,extraParameters);
					}
				}
				PromotionProcessingComponent prePromotionProcessing[] ;
				prePromotionProcessing = itemPricingEngineImpl.getPrePromotionProcessing();
				if (prePromotionProcessing != null){
					itemPricingEngineImpl.applyPromotionProcessing(prePromotionProcessing, pricingModels,pricingContext, extraParameters);
				}
				pricingModels = itemPricingEngineImpl
						.vetoPromotionsForEvaluation(pricingContext, null,pricingModels);
				Iterator<RepositoryItem> itr ;
				itr = pricingModels.iterator();
				
				do {
					if (!itr.hasNext()){
						break;
					}
					RepositoryItem pricingModel ;
					pricingModel = (RepositoryItem) itr.next();
					pricingContext.setPricingModel(pricingModel);
					Collection qualifyingItems ;
					qualifyingItems  = getPricingQualifierService().findQualifyingItems(pricingContext, null);
					
					if (qualifyingItems != null && !qualifyingItems.isEmpty()){
						promotions.add(pricingModel);
						
					}
				} while (true);
				
				 
			} else if (isLoggingError()){
				logError(ResourceUtils.getMsgResource(
						"itemPricingEngineIncorrectType",
						"atg.commerce.pricing.Resources", sResourceBundle));
			}
		} catch (CommerceException exception) {
			if (isLoggingError()){
				logError(exception);
			}
		}
		if (! promotions.isEmpty()) {
			if (isLoggingDebug()){
				logDebug("Total promotions:::" + promotions.size());
			}

		} else if (isLoggingDebug()){
			logDebug("No targeting promotions");
		}
		return promotions;

	}

	
	/**
	 * @param items
	 * @param priceQuotes
	 * @return
	 */
	private List getWrappCommerceItems(List<CommerceItem> items, List<AmountInfo> priceQuotes){
		List wrappedItems = null;
		wrappedItems = getPricingQualifierService()
						.wrapCommerceItems(items, priceQuotes);
				
		return wrappedItems;
		
		
	}
    /**
     * @param product
     * @param sku
     * @return
     * @throws CommerceException
     * @throws ServletException
     * @throws IOException
     */
    private CommerceItem getItem(final RepositoryItem product, final  RepositoryItem sku)
			throws CommerceException {

		PerformanceMonitor.startOperation("GetApplicableItemPromotions","getItemToPrice");
		String skuId;
		String productId;
		CommerceItem commerceitem;
		skuId = getCatalogRefId(sku);
		productId = getProductId(product);		
		commerceitem = createCommerceItem(skuId, sku, productId, product,1l);
		return commerceitem;
	}


	/**
	 * @param pProduct
	 * @return
	 */
	private String getProductId(final Object pProduct) {
		String productId = null;
		if (pProduct instanceof RepositoryItem) {
			productId = ((RepositoryItem) pProduct).getRepositoryId();
			if (isLoggingDebug()){
				logDebug((new StringBuilder()).append("getProductId for ")
						.append(pProduct).append("=").append(productId)
						.toString());
			}
		}
		if (isLoggingDebug() && pProduct != null){
			logDebug((new StringBuilder())
					.append("Cannot get productId for object of class ")
					.append(pProduct.getClass()).toString());
		}
		return productId;
	}

	/**
	 * @param pCatalogRef
	 * @return
	 */
	private String getCatalogRefId(final Object pCatalogRef) {
		String id = null;
		if (pCatalogRef instanceof RepositoryItem){
			id =  ((RepositoryItem) pCatalogRef).getRepositoryId();
		}
		return id;
	}

	
	/**
	 * @param productItem
	 * @param skuItem
	 * @param profileItem
	 * @param locale
	 * @param userPricingHolder
	 * @param pricingHolderItem
	 * @return
	 */
	public MatchingObject getPromotionInfo(final RepositoryItem productItem,
			final RepositoryItem skuItem,RepositoryItem profileItem, Locale locale, final PricingModelHolder userPricingHolder,RepositoryItem pricingHolderItem){
		CommerceItem item = null;
		Object isPromotionQualified=null;
		ItemPricingEngine itemPricingEngine = null;
		itemPricingEngine = getItemPricingEngine();
		try {
			
			itemPricingEngine = getItemPricingEngine();
			item = getItem(productItem, skuItem);
			if(null==item)
				return null;
			
			if (itemPricingEngine instanceof ItemPricingEngineImpl) {
				ItemPricingEngineImpl itemPricingEngineImpl = null;
				itemPricingEngineImpl = (ItemPricingEngineImpl) itemPricingEngine;
				List<CommerceItem> items = null;
				items = new ArrayList<CommerceItem>(1);
				List<AmountInfo> priceQuotes = null;
				priceQuotes = new ArrayList<AmountInfo>(1);
				items.add(item);
				priceQuotes.add((itemPricingEngineImpl).createPriceInfo());
				
				List wrappedItems = null;
				wrappedItems= getWrappCommerceItems(items,priceQuotes);	
				
				PricingModelElem model = (PricingModelElem)getPricingQualifierService().getPMDLCache().get(pricingHolderItem);
				PricingContext pricingContext = null;
				pricingContext = getPricingTools().getPricingContextFactory().createPricingContext(
							wrappedItems, pricingHolderItem, profileItem, locale, null, null);
				HashMap<String, PricingContext> extraParameters = null;
				extraParameters = new HashMap<String, PricingContext>();
				extraParameters.put("pricingContext", pricingContext);
				Map pmdlBindings = getPricingQualifierService().createPMDLBindings(pricingContext, extraParameters, items);
				
				isPromotionQualified = model.getQualifier().evaluate(this,  this, pmdlBindings);
				Object matchList =  getPricingQualifierService().evaluateTarget(pricingContext, extraParameters, pricingContext.getItems());
				
				if(null!= isPromotionQualified && isPromotionQualified instanceof Boolean && (Boolean)isPromotionQualified==true){
				    if(matchList instanceof List){
					     if(!((List) matchList).isEmpty() &&  ((List)matchList).size()>0){
					    	 MatchingObject matchObject =(MatchingObject)((List) matchList).get(0);
					    	 return matchObject;		    	 
					     }
				    }
				}
			 
		}
		} catch (Exception e) {
			logError("Error while checking promotion is valid or not"+e,e);
		}
	 
		
		 return null;
		
	}
	 

	private CommerceItem createCommerceItem(String pCatalogRefId,
			Object pCatalogRef, String pProductId, Object pProductRef,
			long pQuantity) throws CommerceException {
		return getPricingTools().createPricingCommerceItem(pCatalogRefId,
				pProductId, pQuantity);
	}

	

}
