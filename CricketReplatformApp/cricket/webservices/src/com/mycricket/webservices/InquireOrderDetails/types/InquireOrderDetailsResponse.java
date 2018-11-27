/**
 * InquireOrderDetailsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mycricket.webservices.InquireOrderDetails.types;

public class InquireOrderDetailsResponse  implements java.io.Serializable {
    private com.mycricket.webservices.InquireOrderDetails.types.PersonalInfo personalInfo;

    private com.mycricket.webservices.InquireOrderDetails.types.ShippingInfo shippingInfo;

    private com.mycricket.webservices.InquireOrderDetails.types.Address billingAddress;

    private java.lang.String mdn;

    private java.lang.String new_mdn;

    private java.lang.String[] cricketPhoneNumbers;

    private java.lang.String eid;

    private java.lang.String market_code;

    private java.lang.String plan_code;

    private java.lang.String plan_description;

    private com.mycricket.webservices.InquireOrderDetails.types.Address accountHolderAddress;

    private java.lang.String packageType;

    private java.lang.String taxTotal;

    private java.lang.String total;

    private java.lang.Double refundAmount;

    private java.lang.String account_balance;

    private java.lang.String paymentType;

    private java.lang.String workOrderType;

    private com.mycricket.webservices.InquireOrderDetails.types.Packages[] orderSummary;

    private com.mycricket.webservices.InquireOrderDetails.types.TaxSummary taxSummary;

    private java.lang.String e911Desc;

    private java.lang.Double e911Amount;

    private java.lang.String bodesc;

    private java.lang.Double boAmount;

    private java.lang.String removedPlan;

    private java.lang.String removedAddOn;

    private com.mycricket.webservices.InquireOrderDetails.types.Attribute[] additionalAttributes;

    public InquireOrderDetailsResponse() {
    }

    public InquireOrderDetailsResponse(
           com.mycricket.webservices.InquireOrderDetails.types.PersonalInfo personalInfo,
           com.mycricket.webservices.InquireOrderDetails.types.ShippingInfo shippingInfo,
           com.mycricket.webservices.InquireOrderDetails.types.Address billingAddress,
           java.lang.String mdn,
           java.lang.String new_mdn,
           java.lang.String[] cricketPhoneNumbers,
           java.lang.String eid,
           java.lang.String market_code,
           java.lang.String plan_code,
           java.lang.String plan_description,
           com.mycricket.webservices.InquireOrderDetails.types.Address accountHolderAddress,
           java.lang.String packageType,
           java.lang.String taxTotal,
           java.lang.String total,
           java.lang.Double refundAmount,
           java.lang.String account_balance,
           java.lang.String paymentType,
           java.lang.String workOrderType,
           com.mycricket.webservices.InquireOrderDetails.types.Packages[] orderSummary,
           com.mycricket.webservices.InquireOrderDetails.types.TaxSummary taxSummary,
           java.lang.String e911Desc,
           java.lang.Double e911Amount,
           java.lang.String bodesc,
           java.lang.Double boAmount,
           java.lang.String removedPlan,
           java.lang.String removedAddOn,
           com.mycricket.webservices.InquireOrderDetails.types.Attribute[] additionalAttributes) {
           this.personalInfo = personalInfo;
           this.shippingInfo = shippingInfo;
           this.billingAddress = billingAddress;
           this.mdn = mdn;
           this.new_mdn = new_mdn;
           this.cricketPhoneNumbers = cricketPhoneNumbers;
           this.eid = eid;
           this.market_code = market_code;
           this.plan_code = plan_code;
           this.plan_description = plan_description;
           this.accountHolderAddress = accountHolderAddress;
           this.packageType = packageType;
           this.taxTotal = taxTotal;
           this.total = total;
           this.refundAmount = refundAmount;
           this.account_balance = account_balance;
           this.paymentType = paymentType;
           this.workOrderType = workOrderType;
           this.orderSummary = orderSummary;
           this.taxSummary = taxSummary;
           this.e911Desc = e911Desc;
           this.e911Amount = e911Amount;
           this.bodesc = bodesc;
           this.boAmount = boAmount;
           this.removedPlan = removedPlan;
           this.removedAddOn = removedAddOn;
           this.additionalAttributes = additionalAttributes;
    }


    /**
     * Gets the personalInfo value for this InquireOrderDetailsResponse.
     * 
     * @return personalInfo
     */
    public com.mycricket.webservices.InquireOrderDetails.types.PersonalInfo getPersonalInfo() {
        return personalInfo;
    }


    /**
     * Sets the personalInfo value for this InquireOrderDetailsResponse.
     * 
     * @param personalInfo
     */
    public void setPersonalInfo(com.mycricket.webservices.InquireOrderDetails.types.PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }


    /**
     * Gets the shippingInfo value for this InquireOrderDetailsResponse.
     * 
     * @return shippingInfo
     */
    public com.mycricket.webservices.InquireOrderDetails.types.ShippingInfo getShippingInfo() {
        return shippingInfo;
    }


    /**
     * Sets the shippingInfo value for this InquireOrderDetailsResponse.
     * 
     * @param shippingInfo
     */
    public void setShippingInfo(com.mycricket.webservices.InquireOrderDetails.types.ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
    }


    /**
     * Gets the billingAddress value for this InquireOrderDetailsResponse.
     * 
     * @return billingAddress
     */
    public com.mycricket.webservices.InquireOrderDetails.types.Address getBillingAddress() {
        return billingAddress;
    }


    /**
     * Sets the billingAddress value for this InquireOrderDetailsResponse.
     * 
     * @param billingAddress
     */
    public void setBillingAddress(com.mycricket.webservices.InquireOrderDetails.types.Address billingAddress) {
        this.billingAddress = billingAddress;
    }


    /**
     * Gets the mdn value for this InquireOrderDetailsResponse.
     * 
     * @return mdn
     */
    public java.lang.String getMdn() {
        return mdn;
    }


    /**
     * Sets the mdn value for this InquireOrderDetailsResponse.
     * 
     * @param mdn
     */
    public void setMdn(java.lang.String mdn) {
        this.mdn = mdn;
    }


    /**
     * Gets the new_mdn value for this InquireOrderDetailsResponse.
     * 
     * @return new_mdn
     */
    public java.lang.String getNew_mdn() {
        return new_mdn;
    }


    /**
     * Sets the new_mdn value for this InquireOrderDetailsResponse.
     * 
     * @param new_mdn
     */
    public void setNew_mdn(java.lang.String new_mdn) {
        this.new_mdn = new_mdn;
    }


    /**
     * Gets the cricketPhoneNumbers value for this InquireOrderDetailsResponse.
     * 
     * @return cricketPhoneNumbers
     */
    public java.lang.String[] getCricketPhoneNumbers() {
        return cricketPhoneNumbers;
    }


    /**
     * Sets the cricketPhoneNumbers value for this InquireOrderDetailsResponse.
     * 
     * @param cricketPhoneNumbers
     */
    public void setCricketPhoneNumbers(java.lang.String[] cricketPhoneNumbers) {
        this.cricketPhoneNumbers = cricketPhoneNumbers;
    }


    /**
     * Gets the eid value for this InquireOrderDetailsResponse.
     * 
     * @return eid
     */
    public java.lang.String getEid() {
        return eid;
    }


    /**
     * Sets the eid value for this InquireOrderDetailsResponse.
     * 
     * @param eid
     */
    public void setEid(java.lang.String eid) {
        this.eid = eid;
    }


    /**
     * Gets the market_code value for this InquireOrderDetailsResponse.
     * 
     * @return market_code
     */
    public java.lang.String getMarket_code() {
        return market_code;
    }


    /**
     * Sets the market_code value for this InquireOrderDetailsResponse.
     * 
     * @param market_code
     */
    public void setMarket_code(java.lang.String market_code) {
        this.market_code = market_code;
    }


    /**
     * Gets the plan_code value for this InquireOrderDetailsResponse.
     * 
     * @return plan_code
     */
    public java.lang.String getPlan_code() {
        return plan_code;
    }


    /**
     * Sets the plan_code value for this InquireOrderDetailsResponse.
     * 
     * @param plan_code
     */
    public void setPlan_code(java.lang.String plan_code) {
        this.plan_code = plan_code;
    }


    /**
     * Gets the plan_description value for this InquireOrderDetailsResponse.
     * 
     * @return plan_description
     */
    public java.lang.String getPlan_description() {
        return plan_description;
    }


    /**
     * Sets the plan_description value for this InquireOrderDetailsResponse.
     * 
     * @param plan_description
     */
    public void setPlan_description(java.lang.String plan_description) {
        this.plan_description = plan_description;
    }


    /**
     * Gets the accountHolderAddress value for this InquireOrderDetailsResponse.
     * 
     * @return accountHolderAddress
     */
    public com.mycricket.webservices.InquireOrderDetails.types.Address getAccountHolderAddress() {
        return accountHolderAddress;
    }


    /**
     * Sets the accountHolderAddress value for this InquireOrderDetailsResponse.
     * 
     * @param accountHolderAddress
     */
    public void setAccountHolderAddress(com.mycricket.webservices.InquireOrderDetails.types.Address accountHolderAddress) {
        this.accountHolderAddress = accountHolderAddress;
    }


    /**
     * Gets the packageType value for this InquireOrderDetailsResponse.
     * 
     * @return packageType
     */
    public java.lang.String getPackageType() {
        return packageType;
    }


    /**
     * Sets the packageType value for this InquireOrderDetailsResponse.
     * 
     * @param packageType
     */
    public void setPackageType(java.lang.String packageType) {
        this.packageType = packageType;
    }


    /**
     * Gets the taxTotal value for this InquireOrderDetailsResponse.
     * 
     * @return taxTotal
     */
    public java.lang.String getTaxTotal() {
        return taxTotal;
    }


    /**
     * Sets the taxTotal value for this InquireOrderDetailsResponse.
     * 
     * @param taxTotal
     */
    public void setTaxTotal(java.lang.String taxTotal) {
        this.taxTotal = taxTotal;
    }


    /**
     * Gets the total value for this InquireOrderDetailsResponse.
     * 
     * @return total
     */
    public java.lang.String getTotal() {
        return total;
    }


    /**
     * Sets the total value for this InquireOrderDetailsResponse.
     * 
     * @param total
     */
    public void setTotal(java.lang.String total) {
        this.total = total;
    }


    /**
     * Gets the refundAmount value for this InquireOrderDetailsResponse.
     * 
     * @return refundAmount
     */
    public java.lang.Double getRefundAmount() {
        return refundAmount;
    }


    /**
     * Sets the refundAmount value for this InquireOrderDetailsResponse.
     * 
     * @param refundAmount
     */
    public void setRefundAmount(java.lang.Double refundAmount) {
        this.refundAmount = refundAmount;
    }


    /**
     * Gets the account_balance value for this InquireOrderDetailsResponse.
     * 
     * @return account_balance
     */
    public java.lang.String getAccount_balance() {
        return account_balance;
    }


    /**
     * Sets the account_balance value for this InquireOrderDetailsResponse.
     * 
     * @param account_balance
     */
    public void setAccount_balance(java.lang.String account_balance) {
        this.account_balance = account_balance;
    }


    /**
     * Gets the paymentType value for this InquireOrderDetailsResponse.
     * 
     * @return paymentType
     */
    public java.lang.String getPaymentType() {
        return paymentType;
    }


    /**
     * Sets the paymentType value for this InquireOrderDetailsResponse.
     * 
     * @param paymentType
     */
    public void setPaymentType(java.lang.String paymentType) {
        this.paymentType = paymentType;
    }


    /**
     * Gets the workOrderType value for this InquireOrderDetailsResponse.
     * 
     * @return workOrderType
     */
    public java.lang.String getWorkOrderType() {
        return workOrderType;
    }


    /**
     * Sets the workOrderType value for this InquireOrderDetailsResponse.
     * 
     * @param workOrderType
     */
    public void setWorkOrderType(java.lang.String workOrderType) {
        this.workOrderType = workOrderType;
    }


    /**
     * Gets the orderSummary value for this InquireOrderDetailsResponse.
     * 
     * @return orderSummary
     */
    public com.mycricket.webservices.InquireOrderDetails.types.Packages[] getOrderSummary() {
        return orderSummary;
    }


    /**
     * Sets the orderSummary value for this InquireOrderDetailsResponse.
     * 
     * @param orderSummary
     */
    public void setOrderSummary(com.mycricket.webservices.InquireOrderDetails.types.Packages[] orderSummary) {
        this.orderSummary = orderSummary;
    }


    /**
     * Gets the taxSummary value for this InquireOrderDetailsResponse.
     * 
     * @return taxSummary
     */
    public com.mycricket.webservices.InquireOrderDetails.types.TaxSummary getTaxSummary() {
        return taxSummary;
    }


    /**
     * Sets the taxSummary value for this InquireOrderDetailsResponse.
     * 
     * @param taxSummary
     */
    public void setTaxSummary(com.mycricket.webservices.InquireOrderDetails.types.TaxSummary taxSummary) {
        this.taxSummary = taxSummary;
    }


    /**
     * Gets the e911Desc value for this InquireOrderDetailsResponse.
     * 
     * @return e911Desc
     */
    public java.lang.String getE911Desc() {
        return e911Desc;
    }


    /**
     * Sets the e911Desc value for this InquireOrderDetailsResponse.
     * 
     * @param e911Desc
     */
    public void setE911Desc(java.lang.String e911Desc) {
        this.e911Desc = e911Desc;
    }


    /**
     * Gets the e911Amount value for this InquireOrderDetailsResponse.
     * 
     * @return e911Amount
     */
    public java.lang.Double getE911Amount() {
        return e911Amount;
    }


    /**
     * Sets the e911Amount value for this InquireOrderDetailsResponse.
     * 
     * @param e911Amount
     */
    public void setE911Amount(java.lang.Double e911Amount) {
        this.e911Amount = e911Amount;
    }


    /**
     * Gets the bodesc value for this InquireOrderDetailsResponse.
     * 
     * @return bodesc
     */
    public java.lang.String getBodesc() {
        return bodesc;
    }


    /**
     * Sets the bodesc value for this InquireOrderDetailsResponse.
     * 
     * @param bodesc
     */
    public void setBodesc(java.lang.String bodesc) {
        this.bodesc = bodesc;
    }


    /**
     * Gets the boAmount value for this InquireOrderDetailsResponse.
     * 
     * @return boAmount
     */
    public java.lang.Double getBoAmount() {
        return boAmount;
    }


    /**
     * Sets the boAmount value for this InquireOrderDetailsResponse.
     * 
     * @param boAmount
     */
    public void setBoAmount(java.lang.Double boAmount) {
        this.boAmount = boAmount;
    }


    /**
     * Gets the removedPlan value for this InquireOrderDetailsResponse.
     * 
     * @return removedPlan
     */
    public java.lang.String getRemovedPlan() {
        return removedPlan;
    }


    /**
     * Sets the removedPlan value for this InquireOrderDetailsResponse.
     * 
     * @param removedPlan
     */
    public void setRemovedPlan(java.lang.String removedPlan) {
        this.removedPlan = removedPlan;
    }


    /**
     * Gets the removedAddOn value for this InquireOrderDetailsResponse.
     * 
     * @return removedAddOn
     */
    public java.lang.String getRemovedAddOn() {
        return removedAddOn;
    }


    /**
     * Sets the removedAddOn value for this InquireOrderDetailsResponse.
     * 
     * @param removedAddOn
     */
    public void setRemovedAddOn(java.lang.String removedAddOn) {
        this.removedAddOn = removedAddOn;
    }


    /**
     * Gets the additionalAttributes value for this InquireOrderDetailsResponse.
     * 
     * @return additionalAttributes
     */
    public com.mycricket.webservices.InquireOrderDetails.types.Attribute[] getAdditionalAttributes() {
        return additionalAttributes;
    }


    /**
     * Sets the additionalAttributes value for this InquireOrderDetailsResponse.
     * 
     * @param additionalAttributes
     */
    public void setAdditionalAttributes(com.mycricket.webservices.InquireOrderDetails.types.Attribute[] additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public com.mycricket.webservices.InquireOrderDetails.types.Attribute getAdditionalAttributes(int i) {
        return this.additionalAttributes[i];
    }

    public void setAdditionalAttributes(int i, com.mycricket.webservices.InquireOrderDetails.types.Attribute _value) {
        this.additionalAttributes[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InquireOrderDetailsResponse)) return false;
        InquireOrderDetailsResponse other = (InquireOrderDetailsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.personalInfo==null && other.getPersonalInfo()==null) || 
             (this.personalInfo!=null &&
              this.personalInfo.equals(other.getPersonalInfo()))) &&
            ((this.shippingInfo==null && other.getShippingInfo()==null) || 
             (this.shippingInfo!=null &&
              this.shippingInfo.equals(other.getShippingInfo()))) &&
            ((this.billingAddress==null && other.getBillingAddress()==null) || 
             (this.billingAddress!=null &&
              this.billingAddress.equals(other.getBillingAddress()))) &&
            ((this.mdn==null && other.getMdn()==null) || 
             (this.mdn!=null &&
              this.mdn.equals(other.getMdn()))) &&
            ((this.new_mdn==null && other.getNew_mdn()==null) || 
             (this.new_mdn!=null &&
              this.new_mdn.equals(other.getNew_mdn()))) &&
            ((this.cricketPhoneNumbers==null && other.getCricketPhoneNumbers()==null) || 
             (this.cricketPhoneNumbers!=null &&
              java.util.Arrays.equals(this.cricketPhoneNumbers, other.getCricketPhoneNumbers()))) &&
            ((this.eid==null && other.getEid()==null) || 
             (this.eid!=null &&
              this.eid.equals(other.getEid()))) &&
            ((this.market_code==null && other.getMarket_code()==null) || 
             (this.market_code!=null &&
              this.market_code.equals(other.getMarket_code()))) &&
            ((this.plan_code==null && other.getPlan_code()==null) || 
             (this.plan_code!=null &&
              this.plan_code.equals(other.getPlan_code()))) &&
            ((this.plan_description==null && other.getPlan_description()==null) || 
             (this.plan_description!=null &&
              this.plan_description.equals(other.getPlan_description()))) &&
            ((this.accountHolderAddress==null && other.getAccountHolderAddress()==null) || 
             (this.accountHolderAddress!=null &&
              this.accountHolderAddress.equals(other.getAccountHolderAddress()))) &&
            ((this.packageType==null && other.getPackageType()==null) || 
             (this.packageType!=null &&
              this.packageType.equals(other.getPackageType()))) &&
            ((this.taxTotal==null && other.getTaxTotal()==null) || 
             (this.taxTotal!=null &&
              this.taxTotal.equals(other.getTaxTotal()))) &&
            ((this.total==null && other.getTotal()==null) || 
             (this.total!=null &&
              this.total.equals(other.getTotal()))) &&
            ((this.refundAmount==null && other.getRefundAmount()==null) || 
             (this.refundAmount!=null &&
              this.refundAmount.equals(other.getRefundAmount()))) &&
            ((this.account_balance==null && other.getAccount_balance()==null) || 
             (this.account_balance!=null &&
              this.account_balance.equals(other.getAccount_balance()))) &&
            ((this.paymentType==null && other.getPaymentType()==null) || 
             (this.paymentType!=null &&
              this.paymentType.equals(other.getPaymentType()))) &&
            ((this.workOrderType==null && other.getWorkOrderType()==null) || 
             (this.workOrderType!=null &&
              this.workOrderType.equals(other.getWorkOrderType()))) &&
            ((this.orderSummary==null && other.getOrderSummary()==null) || 
             (this.orderSummary!=null &&
              java.util.Arrays.equals(this.orderSummary, other.getOrderSummary()))) &&
            ((this.taxSummary==null && other.getTaxSummary()==null) || 
             (this.taxSummary!=null &&
              this.taxSummary.equals(other.getTaxSummary()))) &&
            ((this.e911Desc==null && other.getE911Desc()==null) || 
             (this.e911Desc!=null &&
              this.e911Desc.equals(other.getE911Desc()))) &&
            ((this.e911Amount==null && other.getE911Amount()==null) || 
             (this.e911Amount!=null &&
              this.e911Amount.equals(other.getE911Amount()))) &&
            ((this.bodesc==null && other.getBodesc()==null) || 
             (this.bodesc!=null &&
              this.bodesc.equals(other.getBodesc()))) &&
            ((this.boAmount==null && other.getBoAmount()==null) || 
             (this.boAmount!=null &&
              this.boAmount.equals(other.getBoAmount()))) &&
            ((this.removedPlan==null && other.getRemovedPlan()==null) || 
             (this.removedPlan!=null &&
              this.removedPlan.equals(other.getRemovedPlan()))) &&
            ((this.removedAddOn==null && other.getRemovedAddOn()==null) || 
             (this.removedAddOn!=null &&
              this.removedAddOn.equals(other.getRemovedAddOn()))) &&
            ((this.additionalAttributes==null && other.getAdditionalAttributes()==null) || 
             (this.additionalAttributes!=null &&
              java.util.Arrays.equals(this.additionalAttributes, other.getAdditionalAttributes())));
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
        if (getPersonalInfo() != null) {
            _hashCode += getPersonalInfo().hashCode();
        }
        if (getShippingInfo() != null) {
            _hashCode += getShippingInfo().hashCode();
        }
        if (getBillingAddress() != null) {
            _hashCode += getBillingAddress().hashCode();
        }
        if (getMdn() != null) {
            _hashCode += getMdn().hashCode();
        }
        if (getNew_mdn() != null) {
            _hashCode += getNew_mdn().hashCode();
        }
        if (getCricketPhoneNumbers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCricketPhoneNumbers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCricketPhoneNumbers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEid() != null) {
            _hashCode += getEid().hashCode();
        }
        if (getMarket_code() != null) {
            _hashCode += getMarket_code().hashCode();
        }
        if (getPlan_code() != null) {
            _hashCode += getPlan_code().hashCode();
        }
        if (getPlan_description() != null) {
            _hashCode += getPlan_description().hashCode();
        }
        if (getAccountHolderAddress() != null) {
            _hashCode += getAccountHolderAddress().hashCode();
        }
        if (getPackageType() != null) {
            _hashCode += getPackageType().hashCode();
        }
        if (getTaxTotal() != null) {
            _hashCode += getTaxTotal().hashCode();
        }
        if (getTotal() != null) {
            _hashCode += getTotal().hashCode();
        }
        if (getRefundAmount() != null) {
            _hashCode += getRefundAmount().hashCode();
        }
        if (getAccount_balance() != null) {
            _hashCode += getAccount_balance().hashCode();
        }
        if (getPaymentType() != null) {
            _hashCode += getPaymentType().hashCode();
        }
        if (getWorkOrderType() != null) {
            _hashCode += getWorkOrderType().hashCode();
        }
        if (getOrderSummary() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOrderSummary());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOrderSummary(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTaxSummary() != null) {
            _hashCode += getTaxSummary().hashCode();
        }
        if (getE911Desc() != null) {
            _hashCode += getE911Desc().hashCode();
        }
        if (getE911Amount() != null) {
            _hashCode += getE911Amount().hashCode();
        }
        if (getBodesc() != null) {
            _hashCode += getBodesc().hashCode();
        }
        if (getBoAmount() != null) {
            _hashCode += getBoAmount().hashCode();
        }
        if (getRemovedPlan() != null) {
            _hashCode += getRemovedPlan().hashCode();
        }
        if (getRemovedAddOn() != null) {
            _hashCode += getRemovedAddOn().hashCode();
        }
        if (getAdditionalAttributes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAdditionalAttributes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAdditionalAttributes(), i);
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
        new org.apache.axis.description.TypeDesc(InquireOrderDetailsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "InquireOrderDetailsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "personalInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "PersonalInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ShippingInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "ShippingInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("billingAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "billingAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "Address"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mdn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mdn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("new_mdn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "new_mdn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cricketPhoneNumbers");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cricketPhoneNumbers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("", "mdn"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "eid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("market_code");
        elemField.setXmlName(new javax.xml.namespace.QName("", "market_code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plan_code");
        elemField.setXmlName(new javax.xml.namespace.QName("", "plan_code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plan_description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "plan_description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountHolderAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "accountHolderAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "Address"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("packageType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "packageType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxTotal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "taxTotal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("total");
        elemField.setXmlName(new javax.xml.namespace.QName("", "total"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refundAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "refundAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("account_balance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "account_balance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "paymentType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workOrderType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "workOrderType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderSummary");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderSummary"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "Packages"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("", "packages"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxSummary");
        elemField.setXmlName(new javax.xml.namespace.QName("", "taxSummary"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "TaxSummary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e911Desc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "e911Desc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e911Amount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "e911Amount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bodesc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bodesc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "boAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("removedPlan");
        elemField.setXmlName(new javax.xml.namespace.QName("", "removedPlan"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("removedAddOn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "removedAddOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalAttributes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "additionalAttributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mycricket.com/webservices/InquireOrderDetails/types", "Attribute"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
