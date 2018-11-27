package com.cricket.integration.common.ws;

import atg.nucleus.GenericService;
import atg.service.datacollection.JTSQLTableLogger;
import atg.service.idgen.IdGenerator;
import atg.service.idgen.IdGeneratorException;

import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.integration.common.CricketESPLogEvent;

/**
 * The class holds all the methods that have to be hosted to third party systems from ATG Publishing
 * 
 * @author Tech Mahindra
 * 
 */
public class CricketPublishingWS extends GenericService {

	/**
	 * The Logger component which logs ESP feed to ATG DB
	 */
	private JTSQLTableLogger crickESPLogger;
	/**
	 * The IDGenerator component used to generate unique ID for every ESP feed to be 
	 * stored in the feed logger table
	 */
	private IdGenerator idGenerator;
	/**
	 * The feed description to be stored in the logger table if it is a new feed
	 */
	private final String NEW_FEED = "New Feed";
	/**
	 * Label to display ID Generator exception in log
	 */
	private final String ID_GENERATOR_EXCEPTION = "IDGeneratorException";
	/**
	 * Status to be set for the new feed in the logger table 
	 */
	private final int FEED_STATUS_NEW = 0;

	/**
	 * The method is hosted as a web service. It will be called by ESP to send the PARC rate plan feed to ATG.
	 * It will log the ESP feed to the feed logger table to be picked up later by the CricketFeedScheduler component.
	 * 
	 * @param plansDataXML
	 * @return
	 */
	public String updatePARCDataFeedService(String parcDataXML) {
		
		vlogDebug("Inside updatePARCDataFeedServiceMethod");
		try {
			int columnSizes[] = new int[CricketCommonConstants.NUMBER_7];
			columnSizes[0] = CricketCommonConstants.NUMBER_40;
			columnSizes[1] = parcDataXML.length();
			columnSizes[2] = CricketCommonConstants.NUMBER_1;
			columnSizes[3] = CricketCommonConstants.NUMBER_40;
			columnSizes[4] = CricketCommonConstants.NUMBER_500;
			columnSizes[5] = CricketCommonConstants.NUMBER_100;
			columnSizes[6] = CricketCommonConstants.NUMBER_100;
			getCrickESPLogger().setSQLColumnSizes(columnSizes);
			vlogDebug("PARC Feed Length : " + parcDataXML.length());
			vlogDebug("PARC Feed : " + parcDataXML);
			getCrickESPLogger().logEvent(
					new CricketESPLogEvent(getIdGenerator().generateStringId(),
							parcDataXML, FEED_STATUS_NEW, CricketCommonConstants.PARC,
							NEW_FEED));
		} catch (IdGeneratorException e) {
			return CricketCommonConstants.FAILURE
					+ CricketCommonConstants.BLANK_SPACE
					+ CricketCommonConstants.PIPE
					+ CricketCommonConstants.BLANK_SPACE
					+ ID_GENERATOR_EXCEPTION;
		} catch (Exception e) {
			return CricketCommonConstants.FAILURE
					+ CricketCommonConstants.BLANK_SPACE
					+ CricketCommonConstants.PIPE
					+ CricketCommonConstants.BLANK_SPACE + e.getMessage()
					+ CricketCommonConstants.BLANK_SPACE
					+ CricketCommonConstants.EXCEPTION;
		}
		vlogDebug("Exiting updatePARCDataFeedServiceMethod");
		return CricketCommonConstants.SUCCESS;
	}

	/**
	 * The method is hosted as a web service. It will be called by ESP to send the PIM devices feed to ATG.
	 * It will log the ESP feed to the feed logger table to be picked up later by the CricketFeedScheduler component.
	 * 
	 * @param devicesDataXML
	 * @return
	 */
	public String updatePIMDataFeedService(String devicesDataXML) {
		
		vlogDebug("Inside updatePIMDataFeedServiceMethod");
		try {
			int columnSizes[] = new int[CricketCommonConstants.NUMBER_7];
			columnSizes[0] = CricketCommonConstants.NUMBER_40;
			columnSizes[1] = devicesDataXML.length();
			columnSizes[2] = CricketCommonConstants.NUMBER_1;
			columnSizes[3] = CricketCommonConstants.NUMBER_40;
			columnSizes[4] = CricketCommonConstants.NUMBER_500;
			columnSizes[5] = CricketCommonConstants.NUMBER_100;
			columnSizes[6] = CricketCommonConstants.NUMBER_100;
			getCrickESPLogger().setSQLColumnSizes(columnSizes);
			vlogDebug("PIM Feed Length : " + devicesDataXML.length());
			vlogDebug("PIM Feed : " + devicesDataXML);
			getCrickESPLogger().logEvent(
					new CricketESPLogEvent(getIdGenerator().generateStringId(),
							devicesDataXML, FEED_STATUS_NEW, CricketCommonConstants.PIM,
							NEW_FEED));
		} catch (IdGeneratorException e) {
			return CricketCommonConstants.FAILURE
					+ CricketCommonConstants.BLANK_SPACE
					+ CricketCommonConstants.PIPE
					+ CricketCommonConstants.BLANK_SPACE
					+ ID_GENERATOR_EXCEPTION;
		} catch (Exception e) {
			return CricketCommonConstants.FAILURE
					+ CricketCommonConstants.BLANK_SPACE
					+ CricketCommonConstants.PIPE
					+ CricketCommonConstants.BLANK_SPACE + e.getMessage()
					+ CricketCommonConstants.BLANK_SPACE
					+ CricketCommonConstants.EXCEPTION;
		}
		vlogDebug("Exiting updatePIMDataFeedServiceMethod");
		return CricketCommonConstants.SUCCESS;
	}

	/**
	 * 
	 * @return the crickESPLogger logger component
	 */
	public JTSQLTableLogger getCrickESPLogger() {
		return crickESPLogger;
	}

	/**
	 * 
	 * @param crickESPLogger
	 */
	public void setCrickESPLogger(JTSQLTableLogger crickESPLogger) {
		this.crickESPLogger = crickESPLogger;
	}

	/**
	 * 
	 * @return the idGenerator component
	 */
	public IdGenerator getIdGenerator() {
		return idGenerator;
	}

	/**
	 * 
	 * @param idGenerator
	 */
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
}
