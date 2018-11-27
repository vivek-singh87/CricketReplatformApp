package com.cricket.vo;

import atg.nucleus.GenericService;

/**
 * Component created as a VO.
 * 
 * @author AK112151
 *
 */
public class CricketAccountInfo extends GenericService{
	/**
	 * member variable adderss1 
	 */
	private String adderss1;
	/**
	 * member variable adderss2 
	 */
	private String adderss2;
	/**
	 * member variable city 
	 */
	private String city;
	/**
	 * member variable state 
	 */
	private String state;
	/**
	 * member variable country 
	 */
	private String country;
	/**
	 * member variable zip 
	 */
	private String zip;
	/**
	 * @return the adderss1
	 */
	public String getAdderss1() {
		return adderss1;
	}
	/**
	 * @param adderss1 the adderss1 to set
	 */
	public void setAdderss1(String adderss1) {
		this.adderss1 = adderss1;
	}
	/**
	 * @return the adderss2
	 */
	public String getAdderss2() {
		return adderss2;
	}
	/**
	 * @param adderss2 the adderss2 to set
	 */
	public void setAdderss2(String adderss2) {
		this.adderss2 = adderss2;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

}
