package com.cricket.imageurl;

import java.util.HashMap;
import java.util.Map;
/*
 * This configuration file used to set height of images in imageURLLookupDroplet in JSP
 */

public class ImageConfiguration {
	
	private Map<String, Integer> height = new HashMap<String,Integer>();

	public Map<String, Integer> getHeight() {
		return height;
	}

	public void setHeight(Map<String, Integer> height) {
		this.height = height;
	}
	

}
