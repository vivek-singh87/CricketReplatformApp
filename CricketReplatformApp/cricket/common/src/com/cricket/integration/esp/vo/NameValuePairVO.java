/**
 * 
 */
package com.cricket.integration.esp.vo;

/**
 * @author MM112358
 *
 */
public class NameValuePairVO {

	
	 private String name;

	    private String value;

	    public NameValuePairVO() {
	    }

	    public NameValuePairVO(
	           String name,
	           String value) {
	           this.name = name;
	           this.value = value;
	    }

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param pName the name to set
		 */
		public void setName(String pName) {
			name = pName;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param pValue the value to set
		 */
		public void setValue(String pValue) {
			value = pValue;
		}
	    
}
