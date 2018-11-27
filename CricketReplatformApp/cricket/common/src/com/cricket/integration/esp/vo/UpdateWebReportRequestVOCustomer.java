/**
 * 
 */
package com.cricket.integration.esp.vo;

/**
 * @author MM112358
 *
 */
public class UpdateWebReportRequestVOCustomer {

	private String customerId;

    private String customerType;

    /* Customer Name. */
    private NameVO name;

    /* Customer business name. */
    private NameBusinessVO businessName;

    /* This is the mailing address for the customer where the bill
     * will be sent */
    private AddressVO billingAddress;

    /* This is the shipping address for the customer where the items
     * will be shipped */
    private AddressVO shippingAddress;

    /* Customer phone information. */
    private PhoneVO phone;

    /* Customer email information.  If this structure is provided,
     * then one and only one email address must be explicitly marked as the
     * primary address. */
    private EmailVO email;
    
	/**
	 * 
	 */
	public UpdateWebReportRequestVOCustomer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pCustomerId
	 * @param pCustomerType
	 * @param pName
	 * @param pBusinessName
	 * @param pBillingAddress
	 * @param pShippingAddress
	 * @param pPhone
	 * @param pEmail
	 */
	public UpdateWebReportRequestVOCustomer(String pCustomerId,
			String pCustomerType, NameVO pName, NameBusinessVO pBusinessName,
			AddressVO pBillingAddress, AddressVO pShippingAddress,
			PhoneVO pPhone, EmailVO pEmail) {
		super();
		customerId = pCustomerId;
		customerType = pCustomerType;
		name = pName;
		businessName = pBusinessName;
		billingAddress = pBillingAddress;
		shippingAddress = pShippingAddress;
		phone = pPhone;
		email = pEmail;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param pCustomerId the customerId to set
	 */
	public void setCustomerId(String pCustomerId) {
		customerId = pCustomerId;
	}

	/**
	 * @return the customerType
	 */
	public String getCustomerType() {
		return customerType;
	}

	/**
	 * @param pCustomerType the customerType to set
	 */
	public void setCustomerType(String pCustomerType) {
		customerType = pCustomerType;
	}

	/**
	 * @return the name
	 */
	public NameVO getName() {
		return name;
	}

	/**
	 * @param pName the name to set
	 */
	public void setName(NameVO pName) {
		name = pName;
	}

	/**
	 * @return the businessName
	 */
	public NameBusinessVO getBusinessName() {
		return businessName;
	}

	/**
	 * @param pBusinessName the businessName to set
	 */
	public void setBusinessName(NameBusinessVO pBusinessName) {
		businessName = pBusinessName;
	}

	/**
	 * @return the billingAddress
	 */
	public AddressVO getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param pBillingAddress the billingAddress to set
	 */
	public void setBillingAddress(AddressVO pBillingAddress) {
		billingAddress = pBillingAddress;
	}

	/**
	 * @return the shippingAddress
	 */
	public AddressVO getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * @param pShippingAddress the shippingAddress to set
	 */
	public void setShippingAddress(AddressVO pShippingAddress) {
		shippingAddress = pShippingAddress;
	}

	/**
	 * @return the phone
	 */
	public PhoneVO getPhone() {
		return phone;
	}

	/**
	 * @param pPhone the phone to set
	 */
	public void setPhone(PhoneVO pPhone) {
		phone = pPhone;
	}

	/**
	 * @return the email
	 */
	public EmailVO getEmail() {
		return email;
	}

	/**
	 * @param pEmail the email to set
	 */
	public void setEmail(EmailVO pEmail) {
		email = pEmail;
	}

}
