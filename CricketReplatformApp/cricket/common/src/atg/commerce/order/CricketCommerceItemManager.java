package atg.commerce.order;

import java.util.ArrayList;
import java.util.List;

import atg.core.util.StringUtils;

import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.configuration.CartConfiguration;
import com.cricket.common.constants.CricketCommonConstants;

public class CricketCommerceItemManager extends CommerceItemManager {
	
	private CartConfiguration cartConfiguration;
	private String productType;
	@Override
	protected boolean shouldMergeItems(final CommerceItem pExistingItem, final CommerceItem pNewItem) {
		boolean shouldMerge=false;
		if(CricketCommonConstants.ACCESSORY_PRODUCT.equalsIgnoreCase(getProductType())){
			shouldMerge = super.shouldMergeItems(pExistingItem, pNewItem);
		}
		return shouldMerge;
 	}	
	
	
	@SuppressWarnings("unchecked")
	public List<String> getRelatedItems(final CricketOrderImpl order, final String[] commerceItemIds) throws CommerceItemNotFoundException, InvalidParameterException {
		List<String> commerceItemIdList = null;
		CricketCommerceItemImpl commerceItem = null;
		List<CricketCommerceItemImpl> cricketCommerceItemImplList= null;
		commerceItemIdList = new ArrayList<String>();
		String commerceItemId = null;
		if (null != order) {
			for (int i = 0; i < commerceItemIds.length; i++) {
				commerceItemId = commerceItemIds[i];
				if (!StringUtils.isEmpty(commerceItemId)) {
					commerceItem = (CricketCommerceItemImpl) order
							.getCommerceItem(commerceItemId);
					if (null != commerceItem
							&& !StringUtils.isEmpty(commerceItem.getCricItemTypes())
							&& (commerceItem.getCricItemTypes()
									.equalsIgnoreCase(getCartConfiguration().getChangePlanItemType())
									|| commerceItem.getCricItemTypes()
									.equalsIgnoreCase(getCartConfiguration().getChangeAddOnItemType()))) {
						cricketCommerceItemImplList = order.getCommerceItems();
						for (CricketCommerceItemImpl cricketCommerceItem : cricketCommerceItemImplList) {
							/*if (null != cricketCommerceItem
									&& !StringUtils.isEmpty(cricketCommerceItem.getCricItemTypes())
									&& ((cricketCommerceItem.getCricItemTypes()
											.equalsIgnoreCase(getCartConfiguration().getChangeAddOnItemType()))
									|| (cricketCommerceItem.getCricItemTypes()
											.equalsIgnoreCase(getCartConfiguration().getRemovedAddonItemType())))) {*/
								commerceItemIdList.add(cricketCommerceItem.getId());
							//}
						}
					}
				}
			}
		}
		return commerceItemIdList;
	}


	/**
	 * @return the cartConfiguration
	 */
	public CartConfiguration getCartConfiguration() {
		return cartConfiguration;
	}


	/**
	 * @param pCartConfiguration the cartConfiguration to set
	 */
	public void setCartConfiguration(final CartConfiguration pCartConfiguration) {
		cartConfiguration = pCartConfiguration;
	}


	public String getProductType() {
		return productType;
	}


	public void setProductType(final  String productType) {
		this.productType = productType;
	}
	
	
}
