/**
 * InquireOrderDetailsResponseInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mycricket.webservices.InquireOrderDetails.types;

public class InquireOrderDetailsResponseInfo  implements java.io.Serializable {
    private com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsResponse inquireOrderDetailsResponse;

    private com.mycricket.webservices.InquireOrderDetails.types.ResponseInfo responseInfo;

    public InquireOrderDetailsResponseInfo() {
    }

    public InquireOrderDetailsResponseInfo(
           com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsResponse inquireOrderDetailsResponse,
           com.mycricket.webservices.InquireOrderDetails.types.ResponseInfo responseInfo) {
           this.inquireOrderDetailsResponse = inquireOrderDetailsResponse;
           this.responseInfo = responseInfo;
    }


    /**
     * Gets the inquireOrderDetailsResponse value for this InquireOrderDetailsResponseInfo.
     * 
     * @return inquireOrderDetailsResponse
     */
    public com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsResponse getInquireOrderDetailsResponse() {
        return inquireOrderDetailsResponse;
    }


    /**
     * Sets the inquireOrderDetailsResponse value for this InquireOrderDetailsResponseInfo.
     * 
     * @param inquireOrderDetailsResponse
     */
    public void setInquireOrderDetailsResponse(com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsResponse inquireOrderDetailsResponse) {
        this.inquireOrderDetailsResponse = inquireOrderDetailsResponse;
    }


    /**
     * Gets the responseInfo value for this InquireOrderDetailsResponseInfo.
     * 
     * @return responseInfo
     */
    public com.mycricket.webservices.InquireOrderDetails.types.ResponseInfo getResponseInfo() {
        return responseInfo;
    }


    /**
     * Sets the responseInfo value for this InquireOrderDetailsResponseInfo.
     * 
     * @param responseInfo
     */
    public void setResponseInfo(com.mycricket.webservices.InquireOrderDetails.types.ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InquireOrderDetailsResponseInfo)) return false;
        InquireOrderDetailsResponseInfo other = (InquireOrderDetailsResponseInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.inquireOrderDetailsResponse==null && other.getInquireOrderDetailsResponse()==null) || 
             (this.inquireOrderDetailsResponse!=null &&
              this.inquireOrderDetailsResponse.equals(other.getInquireOrderDetailsResponse()))) &&
            ((this.responseInfo==null && other.getResponseInfo()==null) || 
             (this.responseInfo!=null &&
              this.responseInfo.equals(other.getResponseInfo())));
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
        if (getInquireOrderDetailsResponse() != null) {
            _hashCode += getInquireOrderDetailsResponse().hashCode();
        }
        if (getResponseInfo() != null) {
            _hashCode += getResponseInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InquireOrderDetailsResponseInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", ">InquireOrderDetailsResponseInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inquireOrderDetailsResponse");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inquireOrderDetailsResponse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "InquireOrderDetailsResponse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "responseInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "ResponseInfo"));
        elemField.setNillable(false);
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
