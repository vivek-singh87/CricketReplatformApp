/**
 * InquireOrderDetailsSOAPSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mycricket.webservices.InquireOrderDetails._interface;

public class InquireOrderDetailsSOAPSkeleton implements com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetails_PortType, org.apache.axis.wsdl.Skeleton {
    private com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetails_PortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "InquireOrderDetailsRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", ">InquireOrderDetailsRequest"), com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("sendCricketOrderDetails", _params, new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "InquireOrderDetailsResponseInfo"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", ">InquireOrderDetailsResponseInfo"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "sendCricketOrderDetails"));
        _oper.setSoapAction("http://mycricket.com/webservices/InquireOrderDetails/sendCricketOrderDetails");
        _myOperationsList.add(_oper);
        if (_myOperations.get("sendCricketOrderDetails") == null) {
            _myOperations.put("sendCricketOrderDetails", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("sendCricketOrderDetails")).add(_oper);
    }

    public InquireOrderDetailsSOAPSkeleton() {
        this.impl = new com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetailsSOAPImpl();
    }

    public InquireOrderDetailsSOAPSkeleton(com.mycricket.webservices.InquireOrderDetails._interface.InquireOrderDetails_PortType impl) {
        this.impl = impl;
    }
    public com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsResponseInfo sendCricketOrderDetails(com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsRequest inquireOrderDetailsRequest) throws java.rmi.RemoteException
    {
        com.mycricket.webservices.InquireOrderDetails.types.InquireOrderDetailsResponseInfo ret = impl.sendCricketOrderDetails(inquireOrderDetailsRequest);
        return ret;
    }

}
