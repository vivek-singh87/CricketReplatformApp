package com.cricket.integration.zipcode;

import javax.transaction.TransactionManager;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;

import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;

public class ZipcodeFeedTools extends GenericService {

	private MutableRepository mGeoRepository;

	/**
	 * @return the geoRepository
	 */
	public MutableRepository getGeoRepository() {
		return mGeoRepository;
	}

	/**
	 * @param pGeoRepository
	 *            the geoRepository to set
	 */
	public void setGeoRepository(MutableRepository pGeoRepository) {
		mGeoRepository = pGeoRepository;
	}

	/**
	 * @return the zipcodeBean
	 */
	public ZipcodeBean getZipcodeBean() {
		return mZipcodeBean;
	}

	/**
	 * @param pZipcodeBean
	 *            the zipcodeBean to set
	 */
	public void setZipcodeBean(ZipcodeBean pZipcodeBean) {
		mZipcodeBean = pZipcodeBean;
	}

	/**
	 * @return the transactionManager
	 */
	public TransactionManager getTransactionManager() {
		return mTransactionManager;
	}

	/**
	 * @param pTransactionManager
	 *            the transactionManager to set
	 */
	public void setTransactionManager(TransactionManager pTransactionManager) {
		mTransactionManager = pTransactionManager;
	}

	private ZipcodeBean mZipcodeBean;
	private TransactionManager mTransactionManager;

	public void insertRecord(ZipcodeBean zipcodeBean) {

		TransactionManager tm = getTransactionManager();
		TransactionDemarcation td = new TransactionDemarcation();

		try {
			td.begin(tm);
			if (isLoggingDebug()) {
				logDebug("Start Inside insertRecord Method");
			}
			MutableRepositoryItem geoItem = getGeoRepository().createItem(
					CricketCommonConstants.ZIPCODE_ITEMDESCRIPTOR_NAME);
			geoItem.setPropertyValue(CricketCommonConstants.ZIP_CODE, zipcodeBean.getZipcode());
			geoItem.setPropertyValue(CricketCookieConstants.CITY, zipcodeBean.getCity());
			geoItem.setPropertyValue(CricketCookieConstants.STATE, zipcodeBean.getState());
			geoItem.setPropertyValue(CricketCookieConstants.COUNTRY, zipcodeBean.getCountry());
			geoItem.setPropertyValue(CricketCommonConstants.CITY_TYPE, zipcodeBean.getCitytype());
			geoItem.setPropertyValue(CricketCommonConstants.AREA_CODE, zipcodeBean.getAreacode());
			geoItem.setPropertyValue(CricketCommonConstants.GEO_LATITUDE, zipcodeBean.getLatitude());
			geoItem.setPropertyValue(CricketCommonConstants.GEO_LONGITUDE, zipcodeBean.getLongitude());
			geoItem.setPropertyValue(CricketCommonConstants.CLASSIFICATION_CODE,
					zipcodeBean.getClassificationcode());
			geoItem.setPropertyValue(CricketCommonConstants.TIME_ZONE, zipcodeBean.getTimezone());
			geoItem.setPropertyValue(CricketCommonConstants.CITY_ALIAS_MIXED_CASE, zipcodeBean.getCityAliasMixedCase());
			getGeoRepository().addItem(geoItem);
			if (isLoggingDebug()) {
				logDebug("End Inside insertRecord Method");
			}
		} catch (RepositoryException repositoryException) {
			vlogError("RepositoryException in insertRecord method of class ZipcodeFeedTools : ", repositoryException);
			if (isLoggingDebug()) {
				logDebug("There was a RepositoryException while inserting the records in insertRecord Method of class ZipcodeFeedTools");
			}	
		} catch (TransactionDemarcationException exc) {
			vlogError("TransactionDemarcationException in insertRecord method of class ZipcodeFeedTools : ", exc);
			if (isLoggingDebug()) {
				logDebug("There was a TransactionDemarcationException while inserting the insertRecord Method of class ZipcodeFeedTools");
			}	
		} finally {
			try {
				td.end();
			} catch (TransactionDemarcationException e) {
				vlogError("TransactionDemarcationException in insertRecord method of class ZipcodeFeedTools: ", e);
				if (isLoggingDebug()) {
					logDebug("There was a TransactionDemarcationException while inserting the insertRecord Method of class ZipcodeFeedTools");
				}	
			}
		}

	}

	public void removeRecord() {

		MutableRepository reposiotryview = (MutableRepository) getGeoRepository();
		RepositoryView view = null;

		TransactionManager tm = getTransactionManager();
		TransactionDemarcation td = new TransactionDemarcation();

		try {
			td.begin(tm);
			if (isLoggingDebug()) {
				logDebug("Start Inside removeRecord Method");
			}
			view = getGeoRepository().getView(CricketCommonConstants.ZIPCODE_ITEMDESCRIPTOR_NAME);
			RepositoryItem[] items = null;
			if (view != null) {
				QueryBuilder qb = view.getQueryBuilder();
				Query getFeedsQuery = qb.createUnconstrainedQuery();
				items = view.executeQuery(getFeedsQuery);
			}
			if (items != null && items.length > 0) {
				// remove all items in the repository
				for (RepositoryItem item : items) {
					((MutableRepository) reposiotryview).removeItem(
							item.getRepositoryId(), CricketCommonConstants.ZIPCODE_ITEMDESCRIPTOR_NAME);
				}
			}
			if (isLoggingDebug()) {
				logDebug("End Inside removeRecord Method");
			}
		} catch (RepositoryException repositoryException) {
			vlogError("RepositoryException in insertRecord method of class ZipcodeFeedTools: ", repositoryException);
			if (isLoggingDebug()) {
				logDebug("There was a RepositoryException while removing the records in zipcode feed Data in class ZipcodeFeedTools");
			}			
		} catch (TransactionDemarcationException transactionDemarcationException) {
			vlogError("TransactionDemarcationException in insertRecord method of class ZipcodeFeedTools: ", transactionDemarcationException);
			if (isLoggingDebug()) {
				logDebug("There was a TransactionDemarcationException while removing the records in zipcode feed Data in class ZipcodeFeedTools");
			}	
		} finally {
			try {
				td.end();
			} catch (TransactionDemarcationException e) {
				vlogError("TransactionDemarcationException in insertRecord method of class ZipcodeFeedTools: ", e);
				if (isLoggingDebug()) {
					logDebug("There was a TransactionDemarcationException while removing the records in zipcode feed Data in class ZipcodeFeedTools");
				}	
			}
		}
	}

}
