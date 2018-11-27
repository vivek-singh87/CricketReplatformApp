/**
 * ShippingInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mycricket.webservices.InquireOrderDetails.types;

public class ShippingInfo  implements java.io.Serializable {
    private java.lang.String shippingMethod;

    private java.lang.Double shippingPrice;

    private com.mycricket.webservices.InquireOrderDetails.types.Address address;

    public ShippingInfo() {
    }

    public ShippingInfo(
           java.lang.String shippingMethod,
           java.lang.Double shippingPrice,
           com.mycricket.webservices.InquireOrderDetails.types.Address address) {
           this.shippingMethod = shippingMethod;
           this.shippingPrice = shippingPrice;
           this.address = address;
    }


    /**
     * Gets the shippingMethod value for this ShippingInfo.
     * 
     * @return shippingMethod
     */
    public java.lang.String getShippingMethod() {
        return shippingMethod;
    }


    /**
     * Sets the shippingMethod value for this ShippingInfo.
     * 
     * @param shippingMethod
     */
    public void setShippingMethod(java.lang.String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }


    /**
     * Gets the shippingPrice value for this ShippingInfo.
     * 
     * @return shippingPrice
     */
    public java.lang.Double getShippingPrice() {
        return shippingPrice;
    }


    /**
     * Sets the shippingPrice value for this ShippingInfo.
     * 
     * @param shippingPrice
     */
    public void setShippingPrice(java.lang.Double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }


    /**
     * Gets the address value for this ShippingInfo.
     * 
     * @return address
     */
    public com.mycricket.webservices.InquireOrderDetails.types.Address getAddress() {
        return address;
    }


    /**
     * Sets the address value for this ShippingInfo.
     * 
     * @param address
     */
    public void setAddress(com.mycricket.webservices.InquireOrderDetails.types.Address address) {
        this.address = address;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ShippingInfo)) return false;
        ShippingInfo other = (ShippingInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.shippingMethod==null && other.getShippingMethod()==null) || 
             (this.shippingMethod!=null &&
              this.shippingMethod.equals(other.getShippingMethod()))) &&
            ((this.shippingPrice==null && other.getShippingPrice()==null) || 
             (this.shippingPrice!=null &&
              this.shippingPrice.equals(other.getShippingPrice()))) &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getShippingMethod() != null) {
            _hashCode += getShippingMethod().hashCode();
        }
        if (getShippingPrice() != null) {
            _hashCode += getShippingPrice().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ShippingInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "ShippingInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("", "shippingMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "shippingPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("", "address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "Address"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
