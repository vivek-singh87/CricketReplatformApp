/**
 * InquireOrderDetails_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mycricket.webservices.InquireOrderDetails._interface;

public class InquireOrderDetails_ServiceLocator extends org.apache.axis.client.Service implements com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetails_Service {

    public InquireOrderDetails_ServiceLocator() {
    }


    public InquireOrderDetails_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public InquireOrderDetails_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for InquireOrderDetailsSOAP
    private java.lang.String InquireOrderDetailsSOAP_address = "http://mycricket.com/webservices/services/InquireOrderDetailsSOAP";

    public java.lang.String getInquireOrderDetailsSOAPAddress() {
        return InquireOrderDetailsSOAP_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String InquireOrderDetailsSOAPWSDDServiceName = "InquireOrderDetailsSOAP";

    public java.lang.String getInquireOrderDetailsSOAPWSDDServiceName() {
        return InquireOrderDetailsSOAPWSDDServiceName;
    }

    public void setInquireOrderDetailsSOAPWSDDServiceName(java.lang.String name) {
        InquireOrderDetailsSOAPWSDDServiceName = name;
    }

    public com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetails_PortType getInquireOrderDetailsSOAP() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(InquireOrderDetailsSOAP_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getInquireOrderDetailsSOAP(endpoint);
    }

    public com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetails_PortType getInquireOrderDetailsSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetailsSOAPStub _stub = new com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetailsSOAPStub(portAddress, this);
            _stub.setPortName(getInquireOrderDetailsSOAPWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setInquireOrderDetailsSOAPEndpointAddress(java.lang.String address) {
        InquireOrderDetailsSOAP_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetails_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetailsSOAPStub _stub = new com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetailsSOAPStub(new java.net.URL(InquireOrderDetailsSOAP_address), this);
                _stub.setPortName(getInquireOrderDetailsSOAPWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("InquireOrderDetailsSOAP".equals(inputPortName)) {
            return getInquireOrderDetailsSOAP();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/interface", "InquireOrderDetails");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/interface", "InquireOrderDetailsSOAP"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("InquireOrderDetailsSOAP".equals(portName)) {
            setInquireOrderDetailsSOAPEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
