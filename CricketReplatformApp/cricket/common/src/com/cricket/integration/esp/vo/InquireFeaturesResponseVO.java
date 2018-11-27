package com.cricket.integration.esp.vo;

import java.math.BigDecimal;
import java.math.BigInteger;



public class InquireFeaturesResponseVO {
	
	
	private String id ;
	private String name ;
	private BigInteger type ;
	private BigDecimal price ;
	private boolean isMandatory ;
	private String groupName ;

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public BigInteger getType() {
		return type;
	}
	
	/**
	 * 
	 * @param bigInteger
	 */
	public void setType(BigInteger bigInteger) {
		this.type = bigInteger;
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getPrice() {
		return price;
	}
	
	/**
	 * 
	 * @param price
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMandatory() {
		return isMandatory;
	}
	
	/**
	 * 
	 * @param isMandatory
	 */
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getGroupName() {
		return groupName;
	}
	
	/**
	 * 
	 * @param groupName
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}	
	
	@Override
	public String toString() {
		
		StringBuilder strValue = new StringBuilder();
		strValue.append("InquireFeaturesResponseVO : [ ");
		strValue.append("id = "+id);
		strValue.append(" : name = "+name);
		strValue.append(" : type = "+type);
		strValue.append(" : price = "+price);
		strValue.append(" : isMandatory = "+isMandatory);
		strValue.append(" : groupName = "+groupName);
		strValue.append(" ]");
		return strValue.toString();
	}
	
}
