/**
 * 
 */
package com.cricket.order.abandoned;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import atg.commerce.order.CommerceItemImpl;
import atg.commerce.order.HardgoodShippingGroup;
import atg.commerce.order.OrderManager;
import atg.commerce.order.ShippingGroup;
import atg.commerce.pricing.PricingAdjustment;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;

import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.configuration.CricketConfiguration;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * @author Cricket
 * 
 */
public class CricketReportGeneration extends GenericService {

	/** The field separator. */
	private String fieldSeparator;

	/** The string enclosure. */
	private String stringEnclosure;

	/** The include header. */
	private boolean includeHeader;

	// value to pass in the constructor
	/** The type of exportation. */
	private String typeOfExportation; // default: CSV

	private String mCurrentDateFormat;

	private String mCurrentTimeFormat;

	private File mFileToSave;

	private OrderManager mOrderManager;
	
	private String mCsvFileName;

	private CricketConfiguration mCricketConfiguration;

	private final static String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss aaa";

	private final static String PROFILE_EMAIL = "email";

	private final static String PROFILE_POSTAL_CODE = "userLocationZipCode";

	private final static String PRODUCT_ITEM_TYPE = "cricItemTypes";

	private final static String DISPLAY_NAME = "displayName";
	
	private final static String FTP_OPEN_CHANNEL = "sftp";

	private final static String FTP_HOST_KEY_CHECKING="StrictHostKeyChecking";

	private final static String FTP_HOST_KEY_CHECKING_VALUE="no";
	/**
	 * Generate csv report.
	 * 
	 * @param pAbandonedOrders
	 */
	public void generateCSVfileforAbandonedOrders(
			List<CricketOrderImpl> pAbandonedOrders) {
		
		String fileName = generateCSVFileName();

		setFileToSave(new File(fileName));
		String[] orderArray = fromOrderListToStringArray(pAbandonedOrders);

		try {
			// saving abandoned orders to csv file
			generatingCSVFileWithAbandonedOrders(orderArray);
			//Uploading CSV file to FTP
			uploadCSVFileTOFTP();
		 
		} catch (IOException e) {
			logError(
					"An error occoured while generating csv report for abandoed orders "
							+ e, e);
		}

	}

	/**
	 * @return
	 */
	private String generateCSVFileName() {
		String fileName = CricketCommonConstants.EMPTY_STRING;
		String todayFormat = getCurrentDateFormat();

		// build filename
		try {
			fileName = getCsvFileName()+ getToday(todayFormat) + "."
					+ "csv";
		} catch (ParseException e) {
			logError(
					"An error occoured while generating csv file name for abandoed orders "
							+ e, e);
		}
		return fileName;
	}

	/**
	 * Convert a List of OrderImpl list to a string array.
	 * 
	 * @param listOfOrders
	 *            the list of orders
	 * @return the string array
	 */
	@SuppressWarnings("unchecked")
	public String[] fromOrderListToStringArray(
			List<CricketOrderImpl> listOfOrders) {
		// The container of all CSV rows
		ArrayList<String> stringList = new ArrayList<String>();
		// The single line
		StringBuffer orderRow = this.initializeOrderRow();
		stringList.add(orderRow.toString());
		// read all orders selectec
		if (isLoggingDebug()) {
			logDebug("Getting abandoned orders to generate csv file");
		}
		for (CricketOrderImpl cricketOrder : listOfOrders) {

			try {
				// get the list of all Commerce Items of order
				List<CommerceItemImpl> lci1 = cricketOrder.getCommerceItems();
				if (isLoggingDebug()) {
					logDebug("Handle order " + cricketOrder.getId());
				}

				orderRow = new StringBuffer();

				String postalCode = CricketCommonConstants.EMPTY_STRING;
				String email = CricketCommonConstants.EMPTY_STRING;
				String transactionItemType = CricketCommonConstants.EMPTY_STRING;
				StringBuffer promoNames = new StringBuffer();
				try {

					Repository rep = getOrderManager().getOrderTools()
							.getProfileRepository();
					RepositoryItem profileItem = rep.getItem(cricketOrder
							.getProfileId(), getOrderManager().getOrderTools()
							.getDefaultProfileType());
					if (null == profileItem) {

						HardgoodShippingGroup shippingGroup = null;
						List<ShippingGroup> shippingGroups = (List<ShippingGroup>) cricketOrder
								.getShippingGroups();

						for (ShippingGroup sg : shippingGroups) {
							if (sg instanceof HardgoodShippingGroup) {
								shippingGroup = (HardgoodShippingGroup) sg;
							}
						}
						if (null != shippingGroup) {
							postalCode = shippingGroup.getShippingAddress()
									.getPostalCode();
							if (StringUtils.isBlank(postalCode)) {
								postalCode = CricketCommonConstants.EMPTY_STRING;
							}
							RepositoryItem shippingItem = shippingGroup.getRepositoryItem();
							if(null!=shippingItem)
								email = (String)shippingItem.getPropertyValue(PROFILE_EMAIL);
						}

					} else {
						email = (String) profileItem
								.getPropertyValue(PROFILE_EMAIL);
						postalCode = (String) profileItem
								.getPropertyValue(PROFILE_POSTAL_CODE);
					}
				} catch (RepositoryException e) {
					logError("error while getting profie for order " + e, e);
				}

				// adding email
				if(!StringUtils.isBlank(email))
					orderRow.append(convertToCSVString(email));
				else
					orderRow.append(convertToCSVString(""));
				orderRow.append(getFieldSeparator());

				// adding abandoned phones
				List<CricketCommerceItemImpl> cItems = cricketOrder
						.getCommerceItems();
				StringBuffer phonesNames = new StringBuffer();
				for (CricketCommerceItemImpl cItem : cItems) {

					transactionItemType = (String) cItem
							.getPropertyValue(PRODUCT_ITEM_TYPE);
					/*
					 * String phoneName = cItem.getItemDisplayName(); if
					 * (isLoggingDebug()) { logDebug("\tCommerce Item " +
					 * commerceItemDetail.getItemDisplayName()); }
					 */
					RepositoryItem skuItem = (RepositoryItem) cItem
							.getAuxiliaryData().getCatalogRef();
					phonesNames.append(skuItem.getPropertyValue(DISPLAY_NAME)
							+ "/");

				}
				// adding abandonedphones
				orderRow.append(convertToCSVString(phonesNames));
				orderRow.append(getFieldSeparator());

				// adding transaction types
				if (StringUtils.isBlank(transactionItemType))
					orderRow.append(convertToCSVString(CricketCommonConstants.EMPTY_STRING));
				else
					orderRow.append(convertToCSVString(transactionItemType));
				orderRow.append(getFieldSeparator());

				// DATE_OF_ORDER
				Timestamp orderTimeStamp = (Timestamp) cricketOrder
						.getLastModifiedDate();
				java.util.Date orderDate;
				if (null != orderTimeStamp) {
					orderDate = new java.util.Date(orderTimeStamp.getTime());
				} else {
					orderDate = new java.util.Date();
				}

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						DATE_FORMAT);
				String dateFormated = simpleDateFormat.format(orderDate);

				// adding date
				orderRow.append(convertToCSVString(dateFormated));
				orderRow.append(getFieldSeparator());

				// adding abandoned optin
				orderRow.append(convertToCSVString(CricketCommonConstants.EMPTY_STRING));
				orderRow.append(getFieldSeparator());

				if (null != cricketOrder.getPriceInfo()) {

					List<PricingAdjustment> adjustment = cricketOrder
							.getPriceInfo().getAdjustments();
					if (!adjustment.isEmpty()) {

						for (PricingAdjustment adj : adjustment) {
							String promName = (String) adj.getPricingModel()
									.getPropertyValue(DISPLAY_NAME);
							if (!StringUtils.isBlank(promName))
								promoNames.append(promName);
						}
					}
				}

				// adding promo code
				orderRow.append(convertToCSVString(promoNames));
				orderRow.append(getFieldSeparator());

				// adding zip code
				if(!StringUtils.isBlank(postalCode))
					orderRow.append(convertToCSVString(postalCode));
				else
					orderRow.append("");
				orderRow.append(getFieldSeparator());
				
				orderRow.append(convertToCSVString("E"));
				orderRow.append(getFieldSeparator());
				
				
			 
				stringList.add(orderRow.toString());

				if (isLoggingDebug()) {
					logDebug("Added abandoed order to csv file: "
							+ orderRow.toString());
				}

			} catch (Exception e) {
				// orderRow = new StringBuffer();
				if (isLoggingError()) {
					logError(e.getMessage());
					logError("Order " + cricketOrder.getId()
							+ " has inconsistent data. Not exported.");
				}
			}

		}

		// conversion to string array
		String[] toReturn = new String[stringList.size()];
		toReturn = stringList.toArray(toReturn);
		return toReturn;
	}

	/**
	 * Initialize order row.
	 * 
	 * @return the string buffer
	 */
	private StringBuffer initializeOrderRow() {
		StringBuffer orderRow = new StringBuffer();
		orderRow.append(CricketAbandonedOrderFields.EMAIL_ADDRESS);
		orderRow.append(getFieldSeparator());

		orderRow.append(CricketAbandonedOrderFields.ABANDONED_PHONES);
		orderRow.append(getFieldSeparator());

		orderRow.append(CricketAbandonedOrderFields.ABANDONED_TRANSACTION_TYPE);
		orderRow.append(getFieldSeparator());

		orderRow.append(CricketAbandonedOrderFields.ABANDONED_DATE);
		orderRow.append(getFieldSeparator());

		orderRow.append(CricketAbandonedOrderFields.ABANDONED_OPTIN);
		orderRow.append(getFieldSeparator());

		orderRow.append(CricketAbandonedOrderFields.ABANDONED_PROMO_CODE);
		orderRow.append(getFieldSeparator());

		orderRow.append(CricketAbandonedOrderFields.ZIPCODE);
		orderRow.append(getFieldSeparator());

		orderRow.append(CricketAbandonedOrderFields.LANGAUAGE_IDENTIFER);
		
		return orderRow;
	}

	 

	/**
	 * @param pBufferToWrite
	 * @throws IOException
	 */
	private void generatingCSVFileWithAbandonedOrders(String[] pBufferToWrite) throws IOException {

		FileOutputStream fileOutputStream = null;
		PrintStream printStream = null;
		try {
			if (isLoggingDebug()) {
				logDebug("Adding abonded orders to csv file ");
			}
			fileOutputStream = new FileOutputStream(getFileToSave());
			if (fileOutputStream != null) {
				printStream = new PrintStream(fileOutputStream);
				if (printStream != null) {
					int i = 0;
					if (isLoggingDebug()) {
						logDebug("Writing abandoned orders to csv file "
								+ getFileToSave());
					}
					for (String str : pBufferToWrite) {
						// if we don't want header, haven't to write the first
						// line
						if (!includeHeader && i == 0) {
							if (isLoggingDebug()) {
								logDebug("First line (header) jumped");
							}
						} else {
							printStream.println(str);
						}
						i++;
					}
				}
			}

		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
			if (printStream != null) {
				printStream.close();
			}
		}

		if (isLoggingDebug()) {
			logDebug("Adding abonded orders to csv file " + getFileToSave()
					+ "at location" + getFileToSave().getAbsolutePath());
		}

	}

	/**
	 * @param dateFormat
	 * @return
	 * @throws ParseException
	 */
	private static String getToday(String dateFormat) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date date = new Date();
		return simpleDateFormat.format(date);
	}

	/**
	 * 
	 */
	private void uploadCSVFileTOFTP() {

		if (isLoggingDebug()) {
			logDebug("Uploading Abandoned Order CSV file to ftp location");
		}

		Session ftpSession = null;
		Channel ftpChannel = null;
		ChannelSftp channelSftp = null;

		String ftpHostName = getCricketConfiguration().getFtpHostName();
		int ftpHostPort = Integer.parseInt(getCricketConfiguration()
				.getFtpPort());
		String ftpHostUserName = getCricketConfiguration().getFtpUserName();
		String ftpHostUserPwd = getCricketConfiguration().getFtpUserPassword();
		String ftpHostFileLocation = getCricketConfiguration()
				.getFtpFileLocation();

		try {
			JSch jsch = new JSch();

			ftpSession = jsch.getSession(ftpHostUserName, ftpHostName,
					ftpHostPort);
			if(null!=ftpSession){
				ftpSession.setPassword(ftpHostUserPwd);
				java.util.Properties config = new java.util.Properties();
				config.put(FTP_HOST_KEY_CHECKING, FTP_HOST_KEY_CHECKING_VALUE);
				ftpSession.setConfig(config);
				ftpSession.connect();
				ftpChannel = ftpSession.openChannel(FTP_OPEN_CHANNEL);
				if(null!=ftpChannel){
					ftpChannel.connect();
					channelSftp = (ChannelSftp) ftpChannel;
					channelSftp.cd(channelSftp.getHome() + "/" + ftpHostFileLocation);
					if(isLoggingDebug()){
						logDebug("Transfering Abandoned Order CSV file "+getFileToSave().getPath() +"to FTP");
					}
					channelSftp.put(new FileInputStream(getFileToSave()),
							getFileToSave().getName());
					
						if (isLoggingDebug()) {
							logDebug("Abandoned Order CSV file" + getFileToSave().getName()
									+ " transfererd to ftp location "
									+ channelSftp.getHome() + "/" + ftpHostFileLocation);
						}
				}
				else {
					if(isLoggingDebug())
						logDebug("Transfering Abandoned order to FTP is not sucessfull becaause FTP is not accessiable right now" );
				}
			}
			else {
				if(isLoggingDebug())
					logDebug("Transfering Abandoned order to FTP is not sucessfull becaause FTP is not accessiable right now" );
			}
			

		} catch (JSchException e) {
			logError(
					"Error while transferring  abandoned order csv file to FTP: "
							+ e, e);
		} catch (SftpException e) {
			logError(
					"Error while transferring  abandoned order csv file to FTP: "
							+ e, e);
		} catch (FileNotFoundException e) {
			logError(
					"Error while transferring  abandoned order csv file "+getFileToSave().getPath() + " to FTP: " 
							+ e, e);
		}

		finally {
			if (null != channelSftp)
				channelSftp.disconnect();
			if (null != ftpChannel)
				ftpChannel.disconnect();
			if (null != ftpSession)
				ftpSession.disconnect();

			
		}

	}

	 
	/**
	 * adding quotations to Object/String so that it can display correct format
	 * in csv.
	 * 
	 * @param pValue
	 *            the value
	 * @return the object
	 */
	private Object convertToCSVString(Object pValue) {
		pValue = "\"" + pValue + "\"";
		return pValue;

	}

	/**
	 * @return the fileToSave
	 */
	public File getFileToSave() {
		return mFileToSave;
	}

	/**
	 * @param pFileToSave
	 *            the fileToSave to set
	 */
	public void setFileToSave(File pFileToSave) {
		mFileToSave = pFileToSave;
	}

	/**
	 * @return the currentTimeFormat
	 */
	public String getCurrentTimeFormat() {
		return mCurrentTimeFormat;
	}

	/**
	 * @param pCurrentTimeFormat
	 *            the currentTimeFormat to set
	 */
	public void setCurrentTimeFormat(String pCurrentTimeFormat) {
		mCurrentTimeFormat = pCurrentTimeFormat;
	}

	/**
	 * @return the fieldSeparator
	 */
	public String getFieldSeparator() {
		return fieldSeparator;
	}

	/**
	 * @return the currentDateFormat
	 */
	public String getCurrentDateFormat() {
		return mCurrentDateFormat;
	}

	/**
	 * @param pCurrentDateFormat
	 *            the currentDateFormat to set
	 */
	public void setCurrentDateFormat(String pCurrentDateFormat) {
		mCurrentDateFormat = pCurrentDateFormat;
	}

	/**
	 * @param pFieldSeparator
	 *            the fieldSeparator to set
	 */
	public void setFieldSeparator(String pFieldSeparator) {
		fieldSeparator = pFieldSeparator;
	}

	/**
	 * @return the stringEnclosure
	 */
	public String getStringEnclosure() {
		return stringEnclosure;
	}

	/**
	 * @param pStringEnclosure
	 *            the stringEnclosure to set
	 */
	public void setStringEnclosure(String pStringEnclosure) {
		stringEnclosure = pStringEnclosure;
	}

	/**
	 * @return the includeHeader
	 */
	public boolean isIncludeHeader() {
		return includeHeader;
	}

	/**
	 * @param pIncludeHeader
	 *            the includeHeader to set
	 */
	public void setIncludeHeader(boolean pIncludeHeader) {
		includeHeader = pIncludeHeader;
	}

	/**
	 * @return the typeOfExportation
	 */
	public String getTypeOfExportation() {
		return typeOfExportation;
	}

	/**
	 * @param pTypeOfExportation
	 *            the typeOfExportation to set
	 */
	public void setTypeOfExportation(String pTypeOfExportation) {
		typeOfExportation = pTypeOfExportation;
	}

	/**
	 * @return the orderManager
	 */
	public OrderManager getOrderManager() {
		return mOrderManager;
	}

	/**
	 * @param pOrderManager
	 *            the orderManager to set
	 */
	public void setOrderManager(OrderManager pOrderManager) {
		mOrderManager = pOrderManager;
	}

	/**
	 * @return the cricketConfiguration
	 */
	public CricketConfiguration getCricketConfiguration() {
		return mCricketConfiguration;
	}

	/**
	 * @param pCricketConfiguration
	 *            the cricketConfiguration to set
	 */
	public void setCricketConfiguration(
			CricketConfiguration pCricketConfiguration) {
		mCricketConfiguration = pCricketConfiguration;
	}

	 

	/**
	 * @return the csvFileName
	 */
	public String getCsvFileName() {
		return mCsvFileName;
	}

	/**
	 * @param pCsvFileName the csvFileName to set
	 */
	public void setCsvFileName(String pCsvFileName) {
		mCsvFileName = pCsvFileName;
	}

}
