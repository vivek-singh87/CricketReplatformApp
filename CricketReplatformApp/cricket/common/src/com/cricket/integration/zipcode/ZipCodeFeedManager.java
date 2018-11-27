package com.cricket.integration.zipcode;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import atg.nucleus.GenericService;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import com.cricket.configuration.CricketConfiguration;

public class ZipCodeFeedManager extends GenericService {

	CricketConfiguration mCricketConfiguration;
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

	private ZipcodeFeedTools mZipcodeFeedTools;
	private String mCsvpathtoWrite;

	/**
	 * @return the csvpathtoWrite
	 */
	public String getCsvpathtoWrite() {
		return mCsvpathtoWrite;
	}

	/**
	 * @param pCsvpathtoWrite
	 *            the csvpathtoWrite to set
	 */
	public void setCsvpathtoWrite(String pCsvpathtoWrite) {
		mCsvpathtoWrite = pCsvpathtoWrite;
	}

	/**
	 * @return the zipcodeFeedTools
	 */
	public ZipcodeFeedTools getZipcodeFeedTools() {
		return mZipcodeFeedTools;
	}

	/**
	 * @param pZipcodeFeedTools
	 *            the zipcodeFeedTools to set
	 */
	public void setZipcodeFeedTools(ZipcodeFeedTools pZipcodeFeedTools) {
		mZipcodeFeedTools = pZipcodeFeedTools;
	}

	public void removeRecord() {

		getZipcodeFeedTools().removeRecord();
	}

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
	 * This method is used to read Zipcode data from CSV file and filter the duplicate data of zipcode.
	 */
	public void readMapBeanCSVFile() throws IOException {
		if (isLoggingDebug()) {
			logDebug("Start Inside readMapBeanCSVFile Method");
		}
		//get the Path of the CSV file
		//String csvFilename = getCsvpath();
		String zipcodesCSVPath = getCricketConfiguration().getZipcodesCSVPath();
		ZipcodeBean zipcodeBean;
		
		CSVReader csvReader = new CSVReader(new FileReader(zipcodesCSVPath));

		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(ZipcodeBean.class);
		
		//To make a strategy of CSV file coloums and database coloumns
		String[] columns = getColumnsNames();
		/*new String[] { "zipCode", "city", "state",
				"country", "citytype", "areacode", "latitude", "longitude",
				"classificationcode", "timezone" };*/
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
					getZipcodeFeedTools().insertRecord(zipcodeBean);
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
}
