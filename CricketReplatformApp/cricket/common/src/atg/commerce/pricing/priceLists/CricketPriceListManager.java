package atg.commerce.pricing.priceLists;

import com.cricket.common.constants.CricketCommonConstants;

import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import atg.service.perfmonitor.PerfStackMismatchException;
import atg.service.perfmonitor.PerformanceMonitor;
import atg.userprofiling.ProfileServices;


/**
 * This class extends the OOTB PriceManager to set the Price List based on the market.
 * @author TechM
 *
 */
public class CricketPriceListManager extends PriceListManager {	
	
	private ProfileServices profileService;
	
	@Override
	public RepositoryItem determinePriceList(RepositoryItem profile,
			RepositoryItem pSite, String pPriceListPropertyName)
			throws RepositoryException {
		
		RepositoryItem priceList = null;
		if (isLoggingDebug()) {
			logDebug("CricketPriceListManager :::: Enter determinePriceList");				
		}		

		if (profile != null) {
			//priceList = getPriceListTools().getPriceListForMarket(city, zipcode, coverage, marketId);			
			RepositoryView priceRepoView = getPriceListRepository().getView(CricketCommonConstants.PROP_PRICE_LIST_ITEM);			
			RqlStatement smt = RqlStatement.parseRqlStatement("ALL");
			RepositoryItem[] plList = smt.executeQuery(priceRepoView, null);
			if(plList != null){
				priceList = plList[0];
			}
			if (isLoggingDebug()) {
				logDebug("priceList : " + priceList );				
			}
			return priceList;
		}else
			return super.determinePriceList(profile,pSite,pPriceListPropertyName);		
	}	
	
	@Override
	public RepositoryItem getPriceList(RepositoryItem pProfile, String pProfilePropertyName, boolean pUseDefaultPriceList)
			throws PriceListException {
		String perfName = "getPriceList(profile,property,priceList)";
	    PerformanceMonitor.startOperation("PriceListManager", perfName);
	    if (isLoggingDebug())
	          logDebug("CricketPriceListManager :::: Enter getPriceList");
	    boolean perfCancelled = false;
	    try
	    {
	      RepositoryItem priceList = null;
	      if(pProfile == null){
	    	  if (isLoggingDebug())
		          logDebug("Profile Item is null, getting Profile from current Profile");
	    	  pProfile = getProfileService().getCurrentProfile();
	      }
	      if (pProfile != null) {
	        try
	        {
	          if (pProfile.getItemDescriptor().hasProperty(pProfilePropertyName))
	            priceList = (RepositoryItem)pProfile.getPropertyValue(pProfilePropertyName);
	        }
	        catch (RepositoryException re) {
	          if (isLoggingError()) {
	            logError(re);
	          }
	        }
	      }
	      if ((priceList == null) && (pUseDefaultPriceList)) {
	        if (isLoggingDebug())
	          logDebug("Profile.priceList is null, use defaultPriceList.");
	        priceList = getDefaultPriceList(pProfilePropertyName);
	      }
	      if (isLoggingDebug())
	          logDebug("Returning PriceList : " + priceList);

	      return priceList;
	    }
	    finally {
	      try {
	        if (!perfCancelled) {
	          PerformanceMonitor.endOperation("PriceListManager", perfName);
	          perfCancelled = true;
	        }
	      } catch (PerfStackMismatchException e) {
	        if (isLoggingWarning())
	          logWarning(e);
	      }
	    }
	}
	
	/**
	 * @return the profileService
	 */
	public ProfileServices getProfileService() {
		return profileService;
	}

	/**
	 * @param profileService the profileService to set
	 */
	public void setProfileService(ProfileServices profileService) {
		this.profileService = profileService;
	}
}
