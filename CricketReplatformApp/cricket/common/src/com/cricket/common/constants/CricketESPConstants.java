package com.cricket.common.constants;

/**
 * @author TechM
 * Details:-  Class CricketESPAdapterConstants will hold all the hard code constants for ESP calls.
 *
 */

public class CricketESPConstants {
	
	public static final String NAMESPACE = "http://esp.cricket.com/ESP/Namespaces/Container/Public/MessageHeader.xsd";
	public static final String PART_NAME = "MessageHeader";
	
	public static final String ESP_NAME_INQUIRECOVERAGE = "InquireCoverage";	
	public static final String ESP_NAME_MANAGEPAYMENT = "ManagePayment";
	public static final String ESP_NAME_MANAGEABP = "ManageABP";
	public static final String ESP_NAME_SENDMESSAGE = "SendMessage";
	public static final String ESP_NAME_MANAGESALE = "ManageSale";
	public static final String ESP_NAME_MANAGESALEITEM = "ManageSaleItem";
	public static final String ESP_NAME_FINALIZESALE = "FinalizeSale";
	public static final String ESP_NAME_COMPLETESALE = "CompleteSale";
	public static final String ESP_NAME_INQUIREFEATURES = "InquireFeatures";
	public static final String ESP_NAME_VALIDATEADDRESS = "ValidateAddress";
	public static final String ESP_NAME_INQUIREDELIVERYESTIMATE = "InquireDeliveryEstimate";
	public static final String ESP_NAME_CREATEACTIVATIONQUOTE = "CreateActivationQuote";
	public static final String ESP_NAME_INQUIREACCOUNT = "InquireAccount";
	public static final String ESP_NAME_UPDATEBILLINGQUOTESTATUS = "UpdateBillingQuoteStatus";
	public static final String ESP_NAME_UPDATEBILLINGQUOTE = "UpdateBillingQuote";
	public static final String ESP_NAME_INQUIREBILLINGORDERDETAILS = "InquireBillingOrderDetails";
	public static final String ESP_NAME_UPDATESUBSCRIBER = "UpdateSubscriber";
	public static final String ESP_NAME_CREATESHIPPINGQUOTE = "CreateShippingQuote";
	public static final String ESP_NAME_UPDATEWEBREPORT = "UpdateWebReport";
	
	
   
	public static final String ESP_RESPONSE_CODE_SUCCESS = "0";
	public static final int INT_ZERO = 0;
	
	public static final String PSA_CREATE_ORDER = "CREATE_ORDER";
	public static final String PSA_CREATE_EQUIPMENT_ORDER = "CREATE_EQUIPMENT_ORDER";
	public static final String PSA_COMPLETE_ORDER = "COMPLETE_ORDER";
	
	public static final String TRANSACTION_TYPE_RRC = "RRC";
	public static final String TRANSACTION_TYPE_OXC = "OXC";
	public static final String TRANSACTION_TYPE_OUP = "OUP";
	public static final String TRANSACTION_TYPE_ADD = "ADD";
	public static final String TRANSACTION_TYPE_ACT = "ACT";
	
	public static final int FEATURE_TYPE_INCLUDED = -1;
	public static final int FEATURE_TYPE_MANDATORY = 3;
	public static final int FEATURE_TYPE_OPTIONAL = 2;
	
	public static final String RESOURCE_BUNDLE_NAME = "com.cricket.integration.esp.ESPAdapterMessages";

	public static final String CUSTOMER_TYPE = "CustomerType";	
	public static final String ACCOUNT_TYPE_INDICATOR_PREPAID = "P";
	public static final String ACCOUNT_TYPE_INDICATOR_PAYGO = "O";
	public static final String PAYMENT_TYPE_SERVICE = "S";
	public static final String PAYMENT_ORIGINATION_WEB = "W";
	public static final String LANGUAGE_PREFERENCE_ENGLISH = "E";
	public static final String CONTACT_PREFERENCE_SMS_EMAIL = "SMS_EMAIL";
	
	public static final String NETWORK_PROVIDER = "networkProvider";
	public static final String NETWORK_PROVIDER_NAME_CRICKET = "Cricket";
	public static final String NETWORK_PROVIDER_NAME_SPRINT = "Sprint";
	public static final String SPRINT_CSA = "sprintCSA";
		
	public static final String ENROLL_ABP = "ENROLL_ABP";
	
	public static final String PHONE_PRODUCT = "phone-product";
	public static final String PLAN_PRODUCT = "plan-product";
	public static final String ADD_ON_PRODUCT = "addOn-product";
	public static final String ACCESSORY_PRODUCT = "accessory-product";
	public static final String UPGRADE_PHONE = "upgradePhone";
	public static final String CHANGE_PLAN = "changePlan";
	public static final String CHANGE_ADD_ON = "changeAddOn";
	public static final String ITEM_DISCOUNT = "Item Discount";
	public static final String SERVICE_CHARGES = "Service Charges";	
	
	public static final String DELIMITER_PIPE = "|";
	public static final String DELIMITER_UNDERSCORE = "_";
	public static final String MINUS = "-";
	public static final String SINGLE_SPACE = " ";
	public static final String EMPTY_STRING = "";
	
	public static final String UNIT_OF_MEASURE_EA = "ea";
	public static final String MANUFACTURER_CODE = "manufacturerCode";
	public static final String MODEL_NUMBER = "modelNumber";
	public static final String SERVICE_TYPE = "serviceType";
	public static final String PHONE_TYPE_VOICE = "Voice";
	
	public static final String PROPERTY_ITEM_DISCOUNT_TYPE = "itemDiscountType";
	public static final String PROPERTY_PARC_PLAN_ID = "parcPlanId";
	public static final String PROPERTY_OFFER_ID = "offerId";
	public static final String PROPERTY_DISPLAY_NAME = "displayName";
	public static final String PROPERTY_DESCRIPTION = "description";
	public static final String PROPERTY_JOINT_VENTURE = "jointVenture";
	public static final String PROPERTY_MARKET_ID = "marketId";
	public static final String PROPERTY_RATE_CENTER_ID = "rateCenterId";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIMEZONE_PST = "PST";
	
	public static final String COUNTRY_USA_CODE_THREE_LETTER = "USA";
	public static final String COUNTRY_USA_CODE_TWO_LETTER = "US";
	
	public static final String CONVERSATIONID_REGEX = "<ns1:conversationId .*>(.+?)</ns1:conversationId>";
	
	public static final String MESSAGE_ID_PREFIX = "atg-esp";
	public static final String ORDER_ID = "orderId";
	public static final String NOORDER = "NOORDER";
	
	public static final String CREDIT_CARD_STATE_AUTHORIZED = "AUTHORIZED";
	
	public static final String ESP_SYSTEM_EXCEPTIONS = "SYSTEM_EXCEPTIONS";
	public static final String ESP_BUSINESS_EXCEPTIONS = "BUSINESS_EXCEPTIONS";
	public static final String ESP_OFFEREXISTSFORCUST_EXCEPTION = "OFFEREXISTSFORCUST_EXCEPTION";
	public static final String ESP_GNVOFFER_EXISTS_FOR_CUST ="gnvOfferExistsForCust";
	public static final String ESP_TIMEOUT = "espTimeOut";
	public static final String READ_TIMED_OUT = "Read timed out";
	
	public static final String ACCOUNT_HOLDER_ADDDRESS_DATA = "acountHolderAddressData";
	public static final String SHIPPING_ADDRESS_DATA = "shippingAddressData";
	public static final String BILLING_ADDRESS_DATA = "billingAddressData";
	public static final String PROFILE_ITEM = "profileItem";
	public static final String LOCATION_INFO = "locationInfo";
	public static final String BUSINESS_ADDRESS_DATA = "businessAddressData";
	public static final String CRICKET_PROFILE = "cricketProfile";
	public static final String ESP_SERVICE_RESPONSE_DATA = "espServiceResponseData";
	public static final String USER_SESSION_BEAN = "userSessionBean";
	public static final String USER_COOKIE_INFO = "cookieInfo";
	public static final String USER_ACCOUNT_INFORMATION = "userAccountInformation";
	
	public static final String SERVICE_TAX = "ServiceTax";
	public static final String SALES_TAX = "salesTax";
	public static final String IP_ADDRESS = "ipAddress";
	public static final String IS_ABP_DIFF_CREDIT_CARD = "isAbpDiffCreditCard";
	
	public static final String UPS_ONE_DAY_PM_RES_SIG  ="UPS_ONE_DAY_PM_RES_SIG";
	public static final String UPS_SUREPOST="UPS_SUREPOST";
	
	public static final String INVALID_BILLING_ADDRESS = "invalidBillingAddress";
	public static final String INVALID_SHIPPING_ADDRESS = "invalidShippingAddres";
	public static final String INVALID_ACCOUNT_ADDRESS = "invalidAccountAddress";
	public static final String ADDRES = "Addres";
	public static final String FAILED_TO_GEOCODE = "FAILED_TO_GEOCODE!";
	public static final String GEOCODE_API_TIMEOUT = "Error_2003";
	
	public static final String CURRENT_RETRY_COUNT = "currentRetryCount";
	public static final String IS_MAX_RETRY_COUNT_REACHED = "isMaxRetryCountReached";	
	
	public static final String SHIPPING_ADDRESS_RADIO_VALUE = "shipping-address";
	public static final String BILLING_ADDRESS_RADIO_VALUE = "billing-address";
	public static final String DIFFERENT_ADDRESS = "Different address";
	public static final String SAME_AS_ACCOUNT_HOLDER_ADDRESS = "Same as account holder address";
	public static final String SAME_AS_SHIPPING_ADDRESS = "Same as shipping address";
	public static final String HOME_ADDRESS = "homeAddress";	
	
	/* this regex is used to replace all special characters with empty string, while setting phone number in bean */
 	public static final String PHONE_REGEX = "[^0-9]+";
 	
 	public static final String ACCESSRORY = "accessory";
 	public static final String PHONE = "phone";
 	public static final String PLAN = "plan";
 	public static final String FEATURE = "feature";
 	
 	public static final String ADD_A_LINE = "Add A Line";
 	public static final String ACCESSORY_ONLY = "Accessory Only";
 	public static final String NEW_ACTIVATION = "New Activation";
 	public static final String FEATURE_CHANGE = "Feature Change";
 	public static final String RATE_PLAN_MIGRATION = "Rate Plan Migration";
 	public static final String UPGRADEPHONE = "Upgrade Phone";
 	public static final String UPGRADEPLAN = "Upgrade Plan";
 	
	public static final String VESTA_RESPONSE_CODE_1001 = "1001"; 	
	public static final String VESTA_RESPONSE_CODE_1001_ABP = "1001_ABP";
 	public static final String UNDEFINED = "undefined";
 	public static final String CKT_SALES_CHANNEL_NAME = "MyCricket";
 	
 	//added to fix QC 8166
 	public static final String PAY_AS_YOU_GO_OFFER_ID = "981";
 	
 	public static final String UPDATE_BILLING_QUOTE_STATUS_CANCEL = "C";
 	public static final String GIVEN_OFFER_EXISTS_FOR_CUST = "gnvOfferExistsForCust";
 	public static final String WHOOP_KEYWORD = "Whoops -  ";
 	
 	public static final String ORDER_DATE="orderDate";
 	public static final String ORDER_NUMBER="orderNumber";
}
