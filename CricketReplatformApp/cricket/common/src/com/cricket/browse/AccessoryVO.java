package com.cricket.browse;

import java.util.List;

/**
 * @author RM112139
 *
 */
public class AccessoryVO extends ProductVO {
	
	/**
	 * colour
	 */
	private String colour;
	
	/**
	 * modelNumber
	 */
	private String modelNumber;
	
	/**
	 * manufacturer
	 */
	private String manufacturer;
	
	/**
	 * compatiblePhones
	 */
	private List<PhoneVO> compatiblePhones;
	/**
	 * finalPrice
	 */
	private double finalPrice;
	
	/**
	 * @return
	 */
	public double getFinalPrice() {
		return finalPrice;
	}
	/**
	 * @param finalPrice
	 */
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	/**
	 * @return
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * @param colour
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}

	/**
	 * @return
	 */
	public String getModelNumber() {
		return modelNumber;
	}

	/**
	 * @param modelNumber
	 */
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	/**
	 * @return
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return
	 */
	public List<PhoneVO> getCompatiblePhones() {
		return compatiblePhones;
	}

	/**
	 * @param compatiblePhones
	 */
	public void setCompatiblePhones(List<PhoneVO> compatiblePhones) {
		this.compatiblePhones = compatiblePhones;
	}

	@Override
	public String toString() {
		return "AccessoryVO [colour=" + colour + ", modelNumber=" + modelNumber
				+ ", manufacturer=" + manufacturer + ", compatiblePhones="
				+ compatiblePhones + "]";
	}	
	

}
