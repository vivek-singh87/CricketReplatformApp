package com.cricket.integration.common;

import java.util.Date;

import atg.nucleus.logging.LogEvent;


/**
 * This is an Event class which holds the properties mapped to the table columns which store the 
 * ESP feed.It is invoked by the CricketESPFeedLogger component every time an ESP feed is recieved
 * @author Tech Mahindra
 *
 */
public class CricketESPLogEvent extends LogEvent {

	/**
	 * id Property which will be mapped to the primary key of CKT_FEED_LOGGER table 
	 */
	private String id;
	/**
	 * feed Property which will be mapped to the feed column of CKT_FEED_LOGGER table
	 * which holds the entire ESP feed
	 */
	private String feed;
	/**
	 * status Property which will be mapped to the status column of CKT_FEED_LOGGER table
	 * which denotes whether the feed is new or failed or processed successfully
	 */
	private int status;
	/**
	 * sourceSystem Property which will be mapped to the sourceSystem column of CKT_FEED_LOGGER
	 * table which denotes the system which sent the ESP feed, for example, PIM or PARC
	 */
	private String sourceSystem;
	/**
	 * description Property which will be mapped to the description column of CKT_FEED_LOGGER table
	 * which holds the feed description whether it was processed successfully or failed
	 */
	private String description;
	/**
	 * creationDate Property which will be mapped to the creationDate column of CKT_FEED_LOGGER table
	 * which denotes the timestamp when ATG recieved the feed 
	 */
	private String creationDate;
	/**
	 * updationDate Property which will be mapped to the updationDate column of CKT_FEED_LOGGER table
	 * which denotes the timestamp when the feed row was last updated in the table
	 */
	private String updationDate;
	
	/**
	 * The constructor method which is invoked by CricketESPFeedSchedular component for setting 
	 * property values to the event object
	 *   
	 * @param feed
	 * @param status
	 * @param sourceSystem
	 * @param description
	 * @param creationDate
	 * @param updationDate
	 */
	public CricketESPLogEvent(String id, String feed, int status, String sourceSystem,
			String description) {
		super(description);
		Date date = new Date();
		setId(id);
		setFeed(feed);
		setStatus(status);
		setSourceSystem(sourceSystem);
		setDescription(description);
		setCreationDate(date.toString());
		setUpdationDate(date.toString());		
	}

	
	/**
	 * Returns id Property which is mapped to the primary key of CKT_FEED_LOGGER table 
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	
	/**
	 * Sets id Property which is mapped to the primary key of CKT_FEED_LOGGER table 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * Returns feed Property which is mapped to the feed column of CKT_FEED_LOGGER table
	 * which holds the entire ESP feed
	 * 
	 * @return the feed
	 */
	public String getFeed() {
		return feed;
	}
	
	/**
	 * Sets feed Property which is mapped to the feed column of CKT_FEED_LOGGER table
	 * which holds the entire ESP feed
	 * 
	 * @param feed
	 */
	public void setFeed(String feed) {
		this.feed = feed;
	}
	
	/**
	 * Returns status Property which is mapped to the status column of CKT_FEED_LOGGER table
	 * which denotes whether the feed is new or failed or processed successfully
	 * 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Sets status Property which is mapped to the status column of CKT_FEED_LOGGER table
	 * which denotes whether the feed is new or failed or processed successfully
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * Returns sourceSystem Property which is mapped to the sourceSystem column of CKT_FEED_LOGGER
	 * table which denotes the system which sent the ESP feed, for example, PIM or PARC
	 * 
	 * @return the sourceSystem
	 */
	public String getSourceSystem() {
		return sourceSystem;
	}
	
	/**
	 * Sets sourceSystem Property which is mapped to the sourceSystem column of CKT_FEED_LOGGER
	 * table which denotes the system which sent the ESP feed, for example, PIM or PARC
	 * 
	 * @param sourceSystem
	 */
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	
	/**
	 * Returns description Property which is mapped to the description column of CKT_FEED_LOGGER table
	 * which holds the feed description whether it was processed successfully or failed
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets description Property which is mapped to the description column of CKT_FEED_LOGGER table
	 * which holds the feed description whether it was processed successfully or failed
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns creationDate Property which is mapped to the creationDate column of CKT_FEED_LOGGER table
	 * which denotes the timestamp when ATG recieved the feed 
	 * 
	 * @return the creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}
	
	/**
	 * Sets creationDate Property which is mapped to the creationDate column of CKT_FEED_LOGGER table
	 * which denotes the timestamp when ATG recieved the feed 
	 * 
	 * @param creationDate
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * Returns updationDate Property which is mapped to the updationDate column of CKT_FEED_LOGGER table
	 * which denotes the timestamp when the feed row was last updated in the table
	 * 
	 * @return the updationDate
	 */
	public String getUpdationDate() {
		return updationDate;
	}
	
	/**
	 * Sets updationDate Property which is mapped to the updationDate column of CKT_FEED_LOGGER table
	 * which denotes the timestamp when the feed row was last updated in the table
	 * 
	 * @param updationDate
	 */
	public void setUpdationDate(String updationDate) {
		this.updationDate = updationDate;
	}

}
