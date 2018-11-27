package atg.commerce.pricing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atg.commerce.catalog.CatalogTools;
import atg.commerce.order.Order;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.ServletUtil;

import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.commerce.order.session.UpgradeItemDetailsSessionBean;
import com.cricket.common.constants.CricketCommonConstants;

public class CricketItemDiscountCalculator extends ItemDiscountCalculator {

	private CatalogTools catalogTools;
 	private CartConfiguration cartConfiguration;
	/*
	 * This method overrides the adjuster value to 0.0 if the discount 
	 * to be applied is of type Mail In Rebate Multi line discount
	 */
	/**
	 * @return the cartConfiguration
	 */
	public CartConfiguration getCartConfiguration() {
		return cartConfiguration;
	}

	/**
	 * @param cartConfiguration the cartConfiguration to set
	 */
	public void setCartConfiguration(CartConfiguration cartConfiguration) {
		this.cartConfiguration = cartConfiguration;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Collection findQualifyingItems(final List pPriceQuotes, final  List pItems, final RepositoryItem pPricingModel, 
			final RepositoryItem pProfile, final Locale pLocale, final Order pOrder, final Map pExtraParameters) throws PricingException {
		Collection qualifiedItems = null;
		String promoType = null;
		
		promoType = (String) pPricingModel.getPropertyValue(CricketCommonConstants.ITEM_DISC_TYPE);
	    
	    //Get Qualified Items only if PromoType is not mail in rebate
		if(null == promoType){
			qualifiedItems = super.findQualifyingItems(pPriceQuotes, pItems,
					pPricingModel, pProfile, pLocale, pOrder, pExtraParameters);
		}
		else if (null != promoType && !promoType.equalsIgnoreCase("mail in Rebate")) {
			qualifiedItems = super.findQualifyingItems(pPriceQuotes, pItems,
					pPricingModel, pProfile, pLocale, pOrder, pExtraParameters);

			// Checks if the promotype is Multi Line Discount
			// Remove from the qualified items list all Items that are not Plan product
			// Remove the first plan item from the list

			if (null != promoType && null != qualifiedItems && promoType.equalsIgnoreCase("Multi Line Discount")) {
			
				
				// get list of qualified items
					List qualifiedList = null;
					qualifiedList = (ArrayList<?>) qualifiedItems;
					List sortedArrayLit = null;
					sortedArrayLit = getSortedItems(qualifiedList); 				
					String cItem = null;
					cItem = (! (sortedArrayLit == null) && ! sortedArrayLit.isEmpty()) ? ((String) sortedArrayLit.get(0)) : null;
					QualifiedItem qualifiedItem = null;
					if(cItem != null){
						UpgradeItemDetailsSessionBean upgradeItemDetailsSessionBean = (UpgradeItemDetailsSessionBean) ServletUtil.getCurrentRequest()
								.resolveName("/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean");
						// apply multipline discount in case of logged in user and trying to add Add a Line
						if(null!= upgradeItemDetailsSessionBean && 
								(null==upgradeItemDetailsSessionBean.getUserIntention()|| !upgradeItemDetailsSessionBean.getUserIntention().
								equalsIgnoreCase(getCartConfiguration().getAddLineIntention()))){
		 				for (int i = 0; i < sortedArrayLit.size(); i++) { 					
		 					qualifiedItem = (QualifiedItem) qualifiedList.get(i);
		 					if(qualifiedItem != null && qualifiedItem.getItem() != null && cItem.equals(qualifiedItem.getItem().getId())){
		 						qualifiedItems.remove(qualifiedItem);
		 						break; 						
		 					}
						}
					}
				}// end of loggedin user
			}
		}
		
	    return qualifiedItems;
	}

	/**
	 * @param qualifiedList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List getSortedItems(final List qualifiedList) {
		List sortedArrayLit = null;
		sortedArrayLit = new ArrayList();
		QualifiedItem qualifiedItem = null;
		for (int i = 0; i < qualifiedList.size(); i++) {
			qualifiedItem = (QualifiedItem) qualifiedList.get(i);
			if(qualifiedItem != null){
				String productType = findProductType(qualifiedItem.getItem().getAuxiliaryData().getProductId());					
				if (null != productType && productType.equalsIgnoreCase(CricketCommonConstants.PLAN_PRODUCT)) {				
					sortedArrayLit.add(qualifiedItem.getItem().getId());
				}
			}
		}
		Collections.sort(sortedArrayLit);
		return sortedArrayLit;
	}
	
	private String findProductType(final String productId) {
		String productType = null;
		try {
			productType = (String) ((getCatalogTools().findProduct(productId))
					.getPropertyValue(CricketCommonConstants.TYPE));

		} catch (RepositoryException e) {
			if (isLoggingError()) {
				logError("RepositoryException" + e);
			}
		}
		return productType;
	}

	/**
	 * @return the catalogTools
	 */
	public CatalogTools getCatalogTools() {
		return catalogTools;
	}

	/**
	 * @param catalogTools the catalogTools to set
	 */
	public void setCatalogTools(final CatalogTools catalogTools) {
		this.catalogTools = catalogTools;
	}
}
