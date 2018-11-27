package com.cricket.integration.zipcode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;
import javax.transaction.TransactionManager;

import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.epub.project.Process;
import atg.process.action.ActionException;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Query;
import atg.repository.QueryBuilder;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.versionmanager.WorkingContext;
import atg.versionmanager.exceptions.VersionException;
import atg.workflow.WorkflowException;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketCookieConstants;
import com.cricket.configuration.CricketConfiguration;
import com.cricket.integration.common.DeploymentTools;

public class ZIPCodeDeploymentTools extends DeploymentTools {

	private TransactionManager mTransactionManager;
	private String mProjectName;
	private MutableRepository mGeoRepository;
	private ZipcodeBean mZipcodeBean;
	//private ZIPCodeDeploymentTools mDeployTools;
	private String mCsvpath;
	CricketConfiguration mCricketConfiguration;
	String[] columnsNames;

	
	/**
	 * @return the columnsNames
	 */
	public String[] getColumnsNames() {
		return columnsNames;
	}

	/**
	 * @param pColumnsNames the columnsNames to set
	 */
	public void setColumnsNames(String[] pColumnsNames) {
		columnsNames = pColumnsNames;
	}

	/**
	 * @return the cricketConfiguration
	 */
	public CricketConfiguration getCricketConfiguration() {
		return mCricketConfiguration;
	}

	/**
	 * @param pCricketConfiguration the cricketConfiguration to set
	 */
	public void setCricketConfiguration(CricketConfiguration pCricketConfiguration) {
		mCricketConfiguration = pCricketConfiguration;
	}

	/**
	 * @return the csvpath
	 */
	public String getCsvpath() {
		return mCsvpath;
	}

	/**
	 * @param pCsvpath the csvpath to set
	 */
	public void setCsvpath(String pCsvpath) {
		mCsvpath = pCsvpath;
	}

	/**
	 * This abstract method is meant to be overridden in the user's subclass.
	 * This is where all the logic for importing the user's data is to be done.
	 */
	public void importUserData(Process pProcess, TransactionDemarcation pTD)
			throws Exception {
		readZipcodeFeed();
	}

	/**
	 * @return the deployTools
	 *//*
	public ZIPCodeDeploymentTools getDeployTools() {
		return mDeployTools;
	}

	*//**
	 * @param pDeployTools
	 *            the deployTools to set
	 *//*
	public void setDeployTools(ZIPCodeDeploymentTools pDeployTools) {
		mDeployTools = pDeployTools;
	}
*/
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

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return mProjectName;
	}

	/**
	 * @param pProjectName
	 *            the projectName to set
	 */
	public void setProjectName(String pProjectName) {
		mProjectName = pProjectName;
	}

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
	   * This is the starting point for the service. In order to start it, the executeImportZipcodeFeed() method needs to
	   * be called by another service. This method begins a transaction and sets the security
	   * context on the thread for the user specified in the userName property. Next, it creates a project
	   * and then calls importUserData(). Next, it attempts to advance the project's workflow. Finally, it
	   * unsets the security context and commits the transaction.
	   *
	   * <b>NOTE!!! - This code only creates a single transaction and is suitable for imports which can fit
	   * into the context of a single transaction. If you are doing large imports, then you must handle batching
	   * the transaction in the importUserData() method.</b>
	   */
	public void executeImportZipcodeFeed() throws VersionException,
			WorkflowException, CreateException, ActionException,
			TransactionDemarcationException, Exception {
		TransactionDemarcation td = new TransactionDemarcation();
		boolean rollback = true;

		try {
			td.begin(getTransactionManager(), TransactionDemarcation.REQUIRED);
			Process process = createProject(td, getProjectName());
			importUserData(process, td);
			advanceWorkflow(process);
			rollback = false;
		} catch (VersionException versionException) {
			throw versionException;
		} catch (TransactionDemarcationException transactionDemarcationException) {
			throw transactionDemarcationException;
		} catch (CreateException createException) {
			throw createException;
		} catch (WorkflowException workflowException) {
			throw workflowException;
		} catch (ActionException actionException) {
			throw actionException;
		} catch (Exception exception) {
			throw exception;
		} finally {
			releaseUserIdentity();
			try {
				td.end(rollback);
			} catch (TransactionDemarcationException tde) {
				throw tde;
			}
			WorkingContext.popDevelopmentLine();
		}
	}
	
	/**
	 * This method is used to call remove and insert method.
	 */
	
	public void readZipcodeFeed() {
		if (isLoggingDebug()) {
			logDebug("Start Inside readZipcodeFeed Method");
		}
		try {
			// call for remove the data from database
			removeRecord();
			// call for insert data from csv to atg database
			readMapBeanCSVFile();

		} catch (FileNotFoundException fileNotFoundException) {
			vlogError("FileNotFoundException  in class ZIPCodeDeploymentTools  : " + fileNotFoundException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a FileNotFoundException in ZIPCodeDeploymentTools class  of readZipcodeFeed method");
			}	
		} catch (IOException ioException) {
			vlogError("IOException  in class ZIPCodeDeploymentTools  : " + ioException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a IOException in ZIPCodeDeploymentTools class  of readZipcodeFeed method");
			}	
		}
		if (isLoggingDebug()) {
			logDebug("End Inside readZipcodeFeed Method");
		}
	}

	/**
	 * This method is used to read Zipcode data from csv file and filter the duplicate data of zipcode.
	 */
	public void readMapBeanCSVFile() throws IOException {
		if (isLoggingDebug()) {
			logDebug("Start Inside readMapBeanCSVFile Method");
		}
		//get the Path of the CSV file
		String csvFilename = getCricketConfiguration().getZipcodesCSVPath();
		ZipcodeBean zipcodeBean;
		CSVReader csvReader = new CSVReader(new FileReader(csvFilename));

		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(ZipcodeBean.class);
		//To make a strategy of CSV file coloums and database coloumns
		String[] columns = getColumnsNames();
		
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();
		int count = 0;
		List list = csv.parse(strat, csvReader);
		//Set<String> zipCodeSet = new HashSet<String>();
		//Start reading the data from CSV file
		for (Object csvrecord : list) {
			zipcodeBean = (ZipcodeBean) csvrecord;
			//Ignore the first record in csv file
			if (count > 0) {
				//check one by one zipcode duplicate data from CSV file
				//if (zipCodeSet.add(zipcodeBean.getZipcode())) {
					insertRecord(zipcodeBean);
				//}
			}
			count++;
		}
		// close the CSV file after reading the data form it
		csvReader.close();
		if (isLoggingDebug()) {
			logDebug("End Inside readMapBeanCSVFile Method");
		}
	}

	/**
	 * This method is used to insert Zipcode data from csv file to ATG Database
	 */
	public void insertRecord(ZipcodeBean zipcodeBean) {

		TransactionManager tm = getTransactionManager();
		TransactionDemarcation td = new TransactionDemarcation();

		try {
			td.begin(tm, TransactionDemarcation.REQUIRED);
			if (isLoggingDebug()) {
				logDebug("Start Inside insertRecord Method");
			}
			MutableRepositoryItem geoItem = getGeoRepository().createItem(CricketCommonConstants.ZIPCODE_ITEMDESCRIPTOR_NAME);
			vlogDebug("Zip Code : " + zipcodeBean.getZipCode());
			vlogDebug("City : " + zipcodeBean.getCity());
			vlogDebug("State : " + zipcodeBean.getState());
			vlogDebug("County : " + zipcodeBean.getCounty());
			vlogDebug("City Type : " + zipcodeBean.getCityType());
			vlogDebug("Area Code : " + zipcodeBean.getAreaCode());
			vlogDebug("Latitude : " + zipcodeBean.getLatitude());
			vlogDebug("Longitude : " + zipcodeBean.getLongitude());
			vlogDebug("Classification Code : " + zipcodeBean.getClassificationCode());
			vlogDebug("Timezone : " + zipcodeBean.getTimeZone());
			vlogDebug("City Alias Mixed Code : " + zipcodeBean.getCityAliasMixedCase());
			vlogDebug("City Alias Code : " + zipcodeBean.getCityAliasCode());
			vlogDebug("Primary Record : " + zipcodeBean.getPrimaryRecord());
			geoItem.setPropertyValue(CricketCommonConstants.ZIP_CODE, zipcodeBean.getZipCode());
			geoItem.setPropertyValue(CricketCookieConstants.CITY, zipcodeBean.getCity());
			geoItem.setPropertyValue(CricketCookieConstants.STATE, zipcodeBean.getState());
			geoItem.setPropertyValue(CricketCookieConstants.COUNTRY, zipcodeBean.getCounty());
			geoItem.setPropertyValue(CricketCommonConstants.CITY_TYPE, zipcodeBean.getCityType());
			geoItem.setPropertyValue(CricketCommonConstants.AREA_CODE, zipcodeBean.getAreaCode());
			geoItem.setPropertyValue(CricketCommonConstants.GEO_LATITUDE, zipcodeBean.getLatitude());
			geoItem.setPropertyValue(CricketCommonConstants.GEO_LONGITUDE, zipcodeBean.getLongitude());
			geoItem.setPropertyValue(CricketCommonConstants.CLASSIFICATION_CODE,zipcodeBean.getClassificationCode());
			geoItem.setPropertyValue(CricketCommonConstants.TIME_ZONE, zipcodeBean.getTimeZone());
			geoItem.setPropertyValue(CricketCommonConstants.CITY_ALIAS_MIXED_CASE, zipcodeBean.getCityAliasMixedCase());
			geoItem.setPropertyValue("cityAliasCode", zipcodeBean.getCityAliasCode());
			geoItem.setPropertyValue("primaryRecord", zipcodeBean.getPrimaryRecord());
			
			getGeoRepository().addItem(geoItem);
			
			if (isLoggingDebug()) {
				logDebug("End Inside insertRecord Method");
			}
		} catch (RepositoryException repositoryException) {
			vlogError("RepositoryException  in class ZIPCodeDeploymentTools  : " + repositoryException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a RepositoryException in ZIPCodeDeploymentTools class  of insertRecord method");
			}	
		} catch (TransactionDemarcationException exc) {
			vlogError("TransactionDemarcationException  in class ZIPCodeDeploymentTools  : " + exc.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a TransactionDemarcationException in ZIPCodeDeploymentTools class  of insertRecord method");
			}	
		} finally {
			try {
				td.end();
			} catch (TransactionDemarcationException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This method is used to remove Zipcode data from ATG Database
	 */
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
			vlogError("RepositoryException  in class ZIPCodeDeploymentTools  : " + repositoryException.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a RepositoryException in ZIPCodeDeploymentTools class  of removeRecord method");
			}	
		} catch (TransactionDemarcationException exc) {
			vlogError("TransactionDemarcationException  in class ZIPCodeDeploymentTools  : " + exc.getMessage());
			if (isLoggingDebug()) {
				logDebug("There was a TransactionDemarcationException in ZIPCodeDeploymentTools class  of removeRecord method");
			}	
		} finally {
			try {
				td.end();
			} catch (TransactionDemarcationException e) {
				vlogError("TransactionDemarcationException  in class ZIPCodeDeploymentTools  : " + e.getMessage());
				if (isLoggingDebug()) {
					logDebug("There was a TransactionDemarcationException in ZIPCodeDeploymentTools class  of removeRecord method");
				}
			}
		}
	}
}
