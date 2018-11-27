/**
 * TaxSummary.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mycricket.webservices.InquireOrderDetails.types;

public class TaxSummary  implements java.io.Serializable {
    private com.mycricket.webservices.InquireOrderDetails.types.TelecomTaxImposition[] telecomTaxImposition;

    private com.mycricket.webservices.InquireOrderDetails.types.BOTaxImposition[] BOTaxImposition;

    public TaxSummary() {
    }

    public TaxSummary(
           com.mycricket.webservices.InquireOrderDetails.types.TelecomTaxImposition[] telecomTaxImposition,
           com.mycricket.webservices.InquireOrderDetails.types.BOTaxImposition[] BOTaxImposition) {
           this.telecomTaxImposition = telecomTaxImposition;
           this.BOTaxImposition = BOTaxImposition;
    }


    /**
     * Gets the telecomTaxImposition value for this TaxSummary.
     * 
     * @return telecomTaxImposition
     */
    public com.mycricket.webservices.InquireOrderDetails.types.TelecomTaxImposition[] getTelecomTaxImposition() {
        return telecomTaxImposition;
    }


    /**
     * Sets the telecomTaxImposition value for this TaxSummary.
     * 
     * @param telecomTaxImposition
     */
    public void setTelecomTaxImposition(com.mycricket.webservices.InquireOrderDetails.types.TelecomTaxImposition[] telecomTaxImposition) {
        this.telecomTaxImposition = telecomTaxImposition;
    }

    public com.mycricket.webservices.InquireOrderDetails.types.TelecomTaxImposition getTelecomTaxImposition(int i) {
        return this.telecomTaxImposition[i];
    }

    public void setTelecomTaxImposition(int i, com.mycricket.webservices.InquireOrderDetails.types.TelecomTaxImposition _value) {
        this.telecomTaxImposition[i] = _value;
    }


    /**
     * Gets the BOTaxImposition value for this TaxSummary.
     * 
     * @return BOTaxImposition
     */
    public com.mycricket.webservices.InquireOrderDetails.types.BOTaxImposition[] getBOTaxImposition() {
        return BOTaxImposition;
    }


    /**
     * Sets the BOTaxImposition value for this TaxSummary.
     * 
     * @param BOTaxImposition
     */
    public void setBOTaxImposition(com.mycricket.webservices.InquireOrderDetails.types.BOTaxImposition[] BOTaxImposition) {
        this.BOTaxImposition = BOTaxImposition;
    }

    public com.mycricket.webservices.InquireOrderDetails.types.BOTaxImposition getBOTaxImposition(int i) {
        return this.BOTaxImposition[i];
    }

    public void setBOTaxImposition(int i, com.mycricket.webservices.InquireOrderDetails.types.BOTaxImposition _value) {
        this.BOTaxImposition[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TaxSummary)) return false;
        TaxSummary other = (TaxSummary) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.telecomTaxImposition==null && other.getTelecomTaxImposition()==null) || 
             (this.telecomTaxImposition!=null &&
              java.util.Arrays.equals(this.telecomTaxImposition, other.getTelecomTaxImposition()))) &&
            ((this.BOTaxImposition==null && other.getBOTaxImposition()==null) || 
             (this.BOTaxImposition!=null &&
              java.util.Arrays.equals(this.BOTaxImposition, other.getBOTaxImposition())));
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
        if (getTelecomTaxImposition() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTelecomTaxImposition());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTelecomTaxImposition(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBOTaxImposition() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBOTaxImposition());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBOTaxImposition(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TaxSummary.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "TaxSummary"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telecomTaxImposition");
        elemField.setXmlName(new javax.xml.namespace.QName("", "telecomTaxImposition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "TelecomTaxImposition"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BOTaxImposition");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BOTaxImposition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "BOTaxImposition"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
