package com.cricket.integration.esp;

import static com.cricket.common.constants.CricketESPConstants.ACCESSORY_PRODUCT;
import static com.cricket.common.constants.CricketESPConstants.COUNTRY_USA_CODE_THREE_LETTER;
import static com.cricket.common.constants.CricketESPConstants.COUNTRY_USA_CODE_TWO_LETTER;
import static com.cricket.common.constants.CricketESPConstants.CREDIT_CARD_STATE_AUTHORIZED;
import static com.cricket.common.constants.CricketESPConstants.DATE_FORMAT;
import static com.cricket.common.constants.CricketESPConstants.EMPTY_STRING;
import static com.cricket.common.constants.CricketESPConstants.ESP_RESPONSE_CODE_SUCCESS;
import static com.cricket.common.constants.CricketESPConstants.INT_ZERO;
import static com.cricket.common.constants.CricketESPConstants.MANUFACTURER_CODE;
import static com.cricket.common.constants.CricketESPConstants.MODEL_NUMBER;
import static com.cricket.common.constants.CricketESPConstants.NETWORK_PROVIDER_NAME_SPRINT;
import static com.cricket.common.constants.CricketESPConstants.PROPERTY_DISPLAY_NAME;
import static com.cricket.common.constants.CricketESPConstants.SERVICE_TYPE;
import static com.cricket.common.constants.CricketESPConstants.SINGLE_SPACE;
import static com.cricket.common.constants.CricketESPConstants.TIMEZONE_PST;
import static com.cricket.common.constants.CricketESPConstants.TRANSACTION_TYPE_OUP;
import static com.cricket.common.constants.CricketESPConstants.TRANSACTION_TYPE_OXC;
import static com.cricket.common.constants.CricketESPConstants.TRANSACTION_TYPE_ACT;
import static com.cricket.common.constants.CricketESPConstants.TRANSACTION_TYPE_RRC;
import static com.cricket.common.constants.CricketESPConstants.UNIT_OF_MEASURE_EA;
import static com.cricket.common.constants.CricketESPConstants.UPGRADE_PHONE;
import static com.cricket.common.constants.CricketESPConstants.UPS_ONE_DAY_PM_RES_SIG;
import static com.cricket.common.constants.CricketESPConstants.UPS_SUREPOST;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import atg.commerce.order.HardgoodShippingGroup;
import atg.commerce.order.PaymentGroup;
import atg.commerce.order.ShippingGroup;
import atg.core.util.Address;
import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.repository.RepositoryItem;
import atg.servlet.ServletUtil;

import com.cricket.checkout.order.CricketAccountHolderAddressData;
import com.cricket.checkout.order.CricketShippingAddressData;
import com.cricket.commerce.order.CricketCommerceItemImpl;
import com.cricket.commerce.order.CricketOrderImpl;
import com.cricket.commerce.order.CricketOrderTools;
import com.cricket.commerce.order.CricketPackage;
import com.cricket.commerce.order.payment.CricketCreditCard;
import com.cricket.common.constants.CricketCommonConstants;
import com.cricket.common.constants.CricketESPConstants;
import com.cricket.configuration.CricketConfiguration;
import com.cricket.esp.ESP.Namespaces.Container.Public.CompleteSaleResponse_xsd.CompleteSaleResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfoAccount;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfoAccountEquipmentShipmentInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfoAccountEquipmentShipmentInfoShipmentOrderSummary;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteRequest_xsd.CreateActivationQuoteRequestInfoAccountEquipmentShipmentInfoShippingHeader;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateActivationQuoteResponse_xsd.CreateActivationQuoteResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateShippingQuoteRequest_xsd.CreateShippingQuoteRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateShippingQuoteRequest_xsd.CreateShippingQuoteRequestInfoAccount;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateShippingQuoteRequest_xsd.CreateShippingQuoteRequestInfoAccountEquipmentShipmentInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateShippingQuoteRequest_xsd.CreateShippingQuoteRequestInfoAccountEquipmentShipmentInfoShipmentOrderSummary;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateShippingQuoteRequest_xsd.CreateShippingQuoteRequestInfoAccountEquipmentShipmentInfoShippingHeader;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateShippingQuoteRequest_xsd.CreateShippingQuoteRequestInfoAccountSubscriber;
import com.cricket.esp.ESP.Namespaces.Container.Public.CreateShippingQuoteResponse_xsd.CreateShippingQuoteResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.FinalizeSaleResponse_xsd.FinalizeSaleResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireAccountResponse_xsd.InquireAccountResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireAccountResponse_xsd.InquireAccountResponseInfoAccount;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireAccountResponse_xsd.InquireAccountResponseInfoAccountCustomer;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireAccountResponse_xsd.InquireAccountResponseInfoAccountSubscriber;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireBillingOrderDetailsResponse_xsd.InquireBillingOrderDetailsResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireBillingOrderDetailsResponse_xsd.InquireBillingOrderDetailsResponseInfoBillingOrderDetailsBillingOrderInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireCoverageResponse_xsd.InquireCoverageResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireDeliveryEstimateResponse_xsd.InquireDeliveryEstimateResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.InquireDeliveryEstimateResponse_xsd.InquireDeliveryEstimateResponseInfoInquireDeliveryEstimateResponseDeliveryService;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManagePaymentResponse_xsd.ManagePaymentResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManagePaymentResponse_xsd.ManagePaymentResponseInfoPaymentManagementStatus;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageSaleItemResponse_xsd.ManageSaleItemResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ManageSaleResponse_xsd.ManageSaleResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateBillingQuoteRequest_xsd.UpdateBillingQuoteRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateBillingQuoteRequest_xsd.UpdateBillingQuoteRequestInfoServiceAmounts;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateBillingQuoteStatusRequest_xsd.UpdateBillingQuoteStatusRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateBillingQuoteStatusRequest_xsd.UpdateBillingQuoteStatusRequestInfoPaymentInformation;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateBillingQuoteStatusResponse_xsd.UpdateBillingQuoteStatusResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateBillingQuoteStatusResponse_xsd.UpdateBillingQuoteStatusResponseInfoOrderInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateBillingQuoteStatusResponse_xsd.UpdateBillingQuoteStatusResponseInfoOrderInfoPaymentResult;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateSubscriberResponse_xsd.UpdateSubscriberResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateSubscriberResponse_xsd.UpdateSubscriberResponseInfoSubscriberResponseInfoSubscriberProductsInfoSubscriberProducts;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateWebReportRequest_xsd.UpdateWebReportRequestInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateWebReportRequest_xsd.UpdateWebReportRequestInfoCustomer;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateWebReportRequest_xsd.UpdateWebReportRequestInfoItems;
import com.cricket.esp.ESP.Namespaces.Container.Public.UpdateWebReportRequest_xsd.UpdateWebReportRequestInfoItemsItem;
import com.cricket.esp.ESP.Namespaces.Container.Public.ValidateAddressResponse_xsd.ValidateAddressResponseInfo;
import com.cricket.esp.ESP.Namespaces.Container.Public.ValidateAddressResponse_xsd.ValidateAddressResponseInfoValidateAddressResponse;
import com.cricket.esp.ESP.Namespaces.Container.Public.ValidateAddressResponse_xsd.ValidateAddressResponseInfoValidateAddressResponseAddress;
import com.cricket.esp.ESP.Namespaces.Container.Public.ValidateAddressResponse_xsd.ValidateAddressResponseInfoValidateAddressResponseGeoCode;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.AccountQuoteChargesInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.AddressInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.AddressStateInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.AddressZipInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.AdjustmentInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.AdjustmentInfoPriceAdjustment;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.BillingMarketInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.BillingPreferencesInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.BillingQuoteDetailsInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.BillingQuoteResponseInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.BillingQuoteStatusInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.BundledOfferingsInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ChargeCardInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ChargeInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ContractTermInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.CoverageMarketInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.CreditCardExpirationDateInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.CreditCardTypeInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.CustomerContactInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.CustomerTypeInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.DealerCommissionInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.DeviceInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.EffectiveDatesInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.EmailInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.GenericRecordLocatorInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.GenericRecordLocatorInfoAccountSelector;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.GenericRecordLocatorInfoAccountSelectorCustomerInformation;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.HotBillChargesInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.LanguagePreferenceInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ManufacturerInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.NameBusinessInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.NameInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.NetworkProviderInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.PaymentInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.PaymentSystemResponseInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.PhoneInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.PostRegularPaymentInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.PricePlanDetailInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.PricePlanInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.PricePlanInfoDataMeterInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.RecurringChargeInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ResponseInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.SalesLineItemInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ShipmentLineInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ShoppingCartResponseInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.ShoppingCartResponseInfoCustomer;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.SubscriberQuoteChargesInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.TenderTypeInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.UnitOfMeasureInfo;
import com.cricket.esp.ESP.Namespaces.Types.Public.CricketDataModel_xsd.WebCustomerAddressInfo;
import com.cricket.integration.esp.vo.ActivationQuoteResponseVO;
import com.cricket.integration.esp.vo.AddressVO;
import com.cricket.integration.esp.vo.BillingQuoteDetailVO;
import com.cricket.integration.esp.vo.BillingQuoteDetailsVO;
import com.cricket.integration.esp.vo.BillingquoteResponseVO;
import com.cricket.integration.esp.vo.CompleteSaleResponseVO;
import com.cricket.integration.esp.vo.CreateShippingQuoteResponseVO;
import com.cricket.integration.esp.vo.DeliveryServiceVO;
import com.cricket.integration.esp.vo.DeviceVO;
import com.cricket.integration.esp.vo.ESPResponseVO;
import com.cricket.integration.esp.vo.FinalizeSaleResponseVO;
import com.cricket.integration.esp.vo.HotBillChargeVO;
import com.cricket.integration.esp.vo.InquireAccountResponseVO;
import com.cricket.integration.esp.vo.InquireBillingOrderDetailsResponseVO;
import com.cricket.integration.esp.vo.InquireCoverageResponseVO;
import com.cricket.integration.esp.vo.InquireDeliveryEstimateResponseVO;
import com.cricket.integration.esp.vo.InquireFeaturesResponseVO;
import com.cricket.integration.esp.vo.LineItemVO;
import com.cricket.integration.esp.vo.ManagePaymentResponseVO;
import com.cricket.integration.esp.vo.ManageSaleItemResponseVO;
import com.cricket.integration.esp.vo.ManageSaleResponseVO;
import com.cricket.integration.esp.vo.ManufacturerVO;
import com.cricket.integration.esp.vo.OfferingsVO;
import com.cricket.integration.esp.vo.RecurringChargesVO;
import com.cricket.integration.esp.vo.ResponseVO;
import com.cricket.integration.esp.vo.ShoppingCartVO;
import com.cricket.integration.esp.vo.SubscriberBillingQuoteDetailVO;
import com.cricket.integration.esp.vo.SubscriberChargesVO;
import com.cricket.integration.esp.vo.SubscriberProductsVO;
import com.cricket.integration.esp.vo.SubscriberQuoteChargesVO;
import com.cricket.integration.esp.vo.SubscriberVO;
import com.cricket.integration.esp.vo.UpdateBillingQuoteStatusResponseOrderVO;
import com.cricket.integration.esp.vo.UpdateBillingQuoteStatusResponseVO;
import com.cricket.integration.esp.vo.UpdateSubscriberResponseVO;
import com.cricket.integration.esp.vo.UpdateWebReportRequestVO;
import com.cricket.integration.esp.vo.UpdateWebReportRequestVOItems;
import com.cricket.integration.esp.vo.UpdateWebReportRequestVOItemsItem;
import com.cricket.integration.esp.vo.ValidateAddressResponseVO;
import com.cricket.user.session.UserAccountInformation;
import com.cricket.util.CAQServiceAmount;
import com.cricket.util.EspServiceResponseData;
import com.cricket.vo.CricketProfile;
import com.cricket.vo.MyCricketCookieLocationInfo;

public class CricketESPAdapterHelper extends GenericService {
	
	/**
	 * Variable to hold cktConfiguration
	 */
	private CricketConfiguration cktConfiguration;

	/*holds drawerId for manageSale ESP */
	private String drawerId;	
	/*holds locationId for manageSale ESP POSContext */
	private	String locationId;
	/*holds salesRepresentative for manageSale ESP POSContext */
	private	String salesRepresentative;	
	/*holds salesChannel for CreateActivationQuote ESP  */
	private	String	salesChannel;
	/*holds shippingOrderType for CreateActivationQuote ESP */
	private	String	shippingOrderType;
	/*holds quoteStatus for UpdateBillingSystemStatus ESP */
	private String quoteStatus;
	private String vestaTokenStub;	
	/* holds newUserBillingNumber for CrateShippingQuoteESP */
	private String newUserBillingNumber;
	/* holds newUserCustomerId for CrateShippingQuoteESP */
	private String newUserCustomerId;
	/*holds contract term for CreateActivationQuote ESP */
	private int contractTerm;
	/*enable or disable of payment token */
	private boolean devEnvironmentEnabled;
	/* cricketOrderTools */
    private CricketOrderTools cricketOrderTools;
	
	/** holding CricketESPAdapter component. */
	private CricketESPAdapter cricketESPAdapter;

/**
	 * @return the cricketESPAdapter
	 */
	public CricketESPAdapter getCricketESPAdapter() {
		return cricketESPAdapter;
	}

	/**
	 * @param pCricketESPAdapter the cricketESPAdapter to set
	 */
	public void setCricketESPAdapter(CricketESPAdapter pCricketESPAdapter) {
		cricketESPAdapter = pCricketESPAdapter;
	}
	/**
	 * Getting the SessionId
	 */
	private String getSessionId() {
		 HttpSession session = ServletUtil.getCurrentRequest().getSession();
		 String sessionId = CricketCommonConstants.EMPTY_STRING;
		 if(null != session) {
			 sessionId = session.getId();
		 }
		 return sessionId;
	}
	/**
	 * This method takes the InquireCoverageResponseInfo coming from ESP response as input and fills the application specific InquireCoverageResponseVO 
	 * @param inquireCoverageResponseInfo
	 * @return InquireCoverageResponseVO
	 */
	public InquireCoverageResponseVO getInquireCoverageResponseVO(InquireCoverageResponseInfo inquireCoverageResponseInfo) {
		InquireCoverageResponseVO responseVO=null;
		if(inquireCoverageResponseInfo!=null && inquireCoverageResponseInfo.getCoverageMarket()!=null){
			if(isLoggingDebug()){			
				logDebug("[CricketESPAdapterHelper->getInquireCoverageResponseVO()]:  Entering into getInquireCoverageResponseVO()...");
			}	
			CoverageMarketInfo coverageMarketInformation = new CoverageMarketInfo();
			// getting the MarketID
			coverageMarketInformation.setMarketID(inquireCoverageResponseInfo.getCoverageMarket().getMarketID());
			
			NetworkProviderInfo networkProviderInformation = new NetworkProviderInfo();
			// getting the MarketID			
			networkProviderInformation.setId(inquireCoverageResponseInfo.getCoverageMarket().getNetworkProvider().getId());
			// getting the NetworkProvider Name
			networkProviderInformation.setName(inquireCoverageResponseInfo.getCoverageMarket().getNetworkProvider().getName());
							
			// Setting the NetworkProvider Info in the Coverage market Info
			coverageMarketInformation.setNetworkProvider(networkProviderInformation);
			
			ResponseInfo responseInformation = new ResponseInfo();
			// getting the Description
			responseInformation.setDescription(inquireCoverageResponseInfo.getResponse().getDescription());
			// getting the sprintCSA based on condition if the network area is Sprint
			if ( inquireCoverageResponseInfo.getCoverageMarket().getNetworkProvider().getName().getValue().equalsIgnoreCase(NETWORK_PROVIDER_NAME_SPRINT))
				coverageMarketInformation.setSprintCsa(inquireCoverageResponseInfo.getCoverageMarket().getSprintCsa());
						
			responseVO = new InquireCoverageResponseVO();
			
			// Setting the coverageMarketInformation Info in the responseVO
			responseVO.setCoverageMarket(coverageMarketInformation);
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapterHelper->getInquireCoverageResponseVO()]: MarketID : "+ coverageMarketInformation.getMarketID());
				logDebug("[CricketESPAdapterHelper->getInquireCoverageResponseVO()]: NetworkProviderID : "+ networkProviderInformation.getId());
				logDebug("[CricketESPAdapterHelper->getInquireCoverageResponseVO()]: NetworkProviderName : "+ networkProviderInformation.getName());
				logDebug("[CricketESPAdapterHelper->getInquireCoverageResponseVO()]: SprintCsa : "+ coverageMarketInformation.getSprintCsa());
			}
			// Setting the responseInformation Info in the responseVO
			responseVO.setResponse(responseInformation);
		} else if(inquireCoverageResponseInfo != null && inquireCoverageResponseInfo.getCoverageMarket() == null) {
			responseVO = new InquireCoverageResponseVO();
			responseVO.setCoverageMarket(null);
		}
		if(isLoggingDebug()){			
			logDebug("[CricketESPAdapterHelper->getInquireCoverageResponseVO()]: Exiting getInquireCoverageResponseVO()...");
		}	
		return responseVO;
	}
	
	/**
	 * This method takes the ManagePaymentResponseInfo coming from ESP response as input and fills the application specific ManagePaymentResponseVO
	 * @param managePaymentResponseVO
	 * @param creditCard
	 * @param managePaymentResponseInfo
	 * @return
	 */
	public ManagePaymentResponseVO getManagePaymentResponseVO(ManagePaymentResponseInfo managePaymentResponseInfo, CricketCreditCard creditCard) {
		if(isLoggingDebug()){		
			logDebug("[CricketESPAdapterHelper->getManagePaymentResponseVO()]:  Entering into getManagePaymentResponseVO()...");
		}	
		ManagePaymentResponseVO managePaymentResponseVO  = null;
		if(managePaymentResponseInfo != null && managePaymentResponseInfo.getResponse() != null){
			ResponseInfo responseInfo = managePaymentResponseInfo.getResponse();			
			managePaymentResponseVO = new ManagePaymentResponseVO();
			ResponseVO responseVO = new ResponseVO();
			
			responseVO.setCode(responseInfo.getCode());
			responseVO.setDescription(responseInfo.getDescription());
			managePaymentResponseVO.setResponse(responseVO);		
		
			ManagePaymentResponseInfoPaymentManagementStatus paymentManagementStatus = managePaymentResponseInfo.getPaymentManagementStatus();
		
			if(paymentManagementStatus != null && ESP_RESPONSE_CODE_SUCCESS.equals(responseInfo.getCode())){
				if(creditCard != null){
					creditCard.setStateAsString(CREDIT_CARD_STATE_AUTHORIZED);
					creditCard.setTransactionId(paymentManagementStatus.getTransactionId());
				}
				//creditCard.setAmountAuthorized(creditCard.getAmount());
				managePaymentResponseVO.setOrderId(paymentManagementStatus.getOrderId());
				managePaymentResponseVO.setTransactionId(paymentManagementStatus.getTransactionId());				
				PaymentSystemResponseInfo paymentSystemResponseInfo = paymentManagementStatus.getVestaResponse();
				if(paymentSystemResponseInfo != null){
					managePaymentResponseVO.setVestaResponse_responseCode(paymentSystemResponseInfo.getResponseCode());
					managePaymentResponseVO.setVestaResponse_responseText(paymentSystemResponseInfo.getResponseText());
					if(ESP_RESPONSE_CODE_SUCCESS.equals(paymentSystemResponseInfo.getResponseCode()) && creditCard != null){
						creditCard.setStateAsString(CREDIT_CARD_STATE_AUTHORIZED);
					}
				}
			}			
		}
		if(isLoggingDebug()){			
			logDebug("[CricketESPAdapterHelper->getManagePaymentResponseVO()]:  Exiting getManagePaymentResponseVO()...");
		}	
		return managePaymentResponseVO;
	}
	
	
	/**
	 * This method takes the ManageSaleResponseInfo coming from ESP response as input and fills the application specific ManageSaleResponseVO
	 * @param manageSaleResponseInfo
	 * @return
	 */
	public ManageSaleResponseVO getManageSaleResponseVO(
			ManageSaleResponseInfo manageSaleResponseInfo) {
		if(isLoggingDebug()){			
			logDebug("[CricketESPAdapterHelper->getManageSaleResponseVO()]:  Entering into getManageSaleResponseVO()...");
		}
		ManageSaleResponseVO manageSaleResponseVO = null;
		if(manageSaleResponseInfo != null && manageSaleResponseInfo.getResponse() != null){			
			manageSaleResponseVO = new ManageSaleResponseVO();
			//commenting as this is not required in success scenario
			ResponseVO responseVO = new ResponseVO();
			
			responseVO.setCode(manageSaleResponseInfo.getResponse().getCode());
			responseVO.setDescription(manageSaleResponseInfo.getResponse().getDescription());
			manageSaleResponseVO.setResponse(responseVO);
			
			ShoppingCartResponseInfo shoppingCartResponse  = manageSaleResponseInfo.getShoppingCartResponse();
			if(shoppingCartResponse != null){
				ShoppingCartVO shoppingCartVO = new ShoppingCartVO();
				shoppingCartVO.setSaleId(shoppingCartResponse.getSaleId());
				shoppingCartVO.setNote(shoppingCartResponse.getNote());
				if(shoppingCartResponse.getSalesTransactionType() != null){
					shoppingCartVO.setSalesTransactionType(shoppingCartResponse.getSalesTransactionType().toString());
				}
				
				WebCustomerAddressInfo shippAddress = shoppingCartResponse.getShippingAddress();
				if(shippAddress != null){
					AddressVO shippAddressVO = new AddressVO();
					shippAddressVO.setAddressLine1(shippAddress.getAddressLine1());
					shippAddressVO.setCity(shippAddress.getCity());
					if(shippAddress.getState() != null){
						shippAddressVO.setState(shippAddress.getState().toString());
					}
					shippAddressVO.setZipCode(shippAddress.getPostalCode());
					shippAddressVO.setCountry(getCountry(shippAddress.getCountry()));
					shoppingCartVO.setShippingAddress(shippAddressVO);
										
					shoppingCartVO.setCustomerFirstName(shippAddress.getFirstName());
					shoppingCartVO.setCustomerLastName(shippAddress.getLastName());
					
				}				
				manageSaleResponseVO.setShoppingCartResponse(shoppingCartVO);
			}
		}
		if(isLoggingDebug()){			
			logDebug("[CricketESPAdapterHelper->getManageSaleResponseVO()]:  Exiting getManageSaleResponseVO()...");
		}	
		return manageSaleResponseVO;
	}	
	
	
	/**
	 * This method takes the ManageSaleItemResponseInfo coming from ESP response as input and fills the application specific ManageSaleItemResponseVO
	 * @param response
	 * @return
	 */
	public ManageSaleItemResponseVO getManageSaleItemResponseVO(
			ManageSaleItemResponseInfo response) {
		if(isLoggingDebug()){			
			logDebug("[CricketESPAdapterHelper->getManageSaleItemResponseVO()]:  Entering into getManageSaleItemResponseVO()...");
		}
		ManageSaleItemResponseVO manageSaleItemResponseVO = null;
		if(response != null && response.getResponse() != null){			
			double totalTax = 0.0;
			manageSaleItemResponseVO = new ManageSaleItemResponseVO();
			
			ResponseVO responseVO = new ResponseVO();
			responseVO.setCode(response.getResponse().getCode());
			responseVO.setDescription(response.getResponse().getDescription());
			manageSaleItemResponseVO.setResponse(responseVO); 
							
			ShoppingCartResponseInfo[] shoppingCartResponseInfos = response.getShoppingCartResponse();
			if(shoppingCartResponseInfos != null && shoppingCartResponseInfos.length > 0){
				ShoppingCartVO shoppingCartVO = null;					
				ShoppingCartResponseInfoCustomer customer = null;
				LineItemVO lineItem = null;
				List<ShoppingCartVO> shoppingCartVOs = new ArrayList<ShoppingCartVO>();;
				List<LineItemVO> lineItemVOs = null;
				AddressVO shippingAddressVO = null;
				for(ShoppingCartResponseInfo shoppingCartResponseInfo : shoppingCartResponseInfos){
					shoppingCartVO = new ShoppingCartVO();
					shoppingCartVO.setSaleId(shoppingCartResponseInfo.getSaleId());
					customer = shoppingCartResponseInfo.getCustomer();
					if(customer != null){
						shoppingCartVO.setBillingAccountNumber(customer.getBillingAccountNumber());
						if(customer.getName() != null){								
							shoppingCartVO.setCustomerFirstName(customer.getName().getFirstName());
							shoppingCartVO.setCustomerLastName(customer.getName().getLastName());								
						}
					}
					SalesLineItemInfo[] lineItemInfos = shoppingCartResponseInfo.getLineItem();
					if(lineItemInfos != null && lineItemInfos.length >0 ){
					    lineItemVOs = new ArrayList<LineItemVO>();
						for(SalesLineItemInfo lineItemInfo : lineItemInfos){
							lineItem = new LineItemVO();
							lineItem.setSaleId(lineItemInfo.getSaleId());
							lineItem.setLineNumber(lineItemInfo.getLineNumber());
							lineItem.setQuantity(lineItemInfo.getQuantity());
							lineItem.setBarCode(lineItemInfo.getBarCode());
							if(lineItemInfo.getPricing() != null){
								lineItem.setPrice(lineItemInfo.getPricing().getPrice());
							}
							lineItem.setSalesTax(lineItemInfo.getSalesTax());
							lineItem.setTaxAmount(lineItemInfo.getTaxAmount());
							totalTax=(lineItemInfo.getTaxAmount()).doubleValue()+totalTax;
							lineItem.setNote(lineItemInfo.getNote());
							if(lineItemInfo.getType() != null){
								lineItem.setType(lineItemInfo.getType().toString());
							}
							lineItem.setName(lineItemInfo.getName());
							lineItem.setManufacturer(lineItemInfo.getManufacturer());
							lineItem.setDescription(lineItemInfo.getDescription());
							if(lineItemInfo.getStatus() != null){
								lineItem.setStatus(lineItemInfo.getStatus().toString());
							}
							lineItem.setDefaultPrice(lineItemInfo.getDefaultPrice());
							lineItem.setCategory(lineItemInfo.getCategory());
							lineItemVOs.add(lineItem);
						}
						shoppingCartVO.setLineItems(lineItemVOs);
					}
					shoppingCartVO.setNote(shoppingCartResponseInfo.getNote());
					if(shoppingCartResponseInfo.getSalesTransactionType() != null){
						shoppingCartVO.setSalesTransactionType(shoppingCartResponseInfo.getSalesTransactionType().toString());
					}
					WebCustomerAddressInfo customerAddressInfo = shoppingCartResponseInfo.getShippingAddress();
					if(customerAddressInfo != null){
						shippingAddressVO = new AddressVO();
						shippingAddressVO.setAddressLine1(customerAddressInfo.getAddressLine1());
						shippingAddressVO.setCity(customerAddressInfo.getCity());
						if(customerAddressInfo.getState() != null){
							shippingAddressVO.setState(customerAddressInfo.getState().toString());
						}
						shippingAddressVO.setZipCode(customerAddressInfo.getPostalCode());
						shippingAddressVO.setCountry(getCountry(customerAddressInfo.getCountry()));
						shippingAddressVO.setFirstName(customerAddressInfo.getFirstName());
						shippingAddressVO.setLastName(customerAddressInfo.getLastName());
						shoppingCartVO.setShippingAddress(shippingAddressVO);
					}
					shoppingCartVOs.add(shoppingCartVO);
				}
			 
				manageSaleItemResponseVO.setTotalTax(totalTax);
				manageSaleItemResponseVO.setShoppingCarResponse(shoppingCartVOs);
			}
		}
		if(isLoggingDebug()){			
			logDebug("[CricketESPAdapterHelper->getManageSaleItemResponseVO()]:  Exiting getManageSaleItemResponseVO()...");
		}
		return manageSaleItemResponseVO;
	}
	
	
	/**
	 * This method takes the FinalizeSaleResponseInfo coming from ESP response as input and fills the application specific FinalizeSaleResponseVO
	 * @param finalizeSaleResponseInfo
	 * @return
	 */
	public FinalizeSaleResponseVO getFinalizeResponseVO(FinalizeSaleResponseInfo finalizeSaleResponseInfo) {
		FinalizeSaleResponseVO finalizeSaleResponseVO = null;
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->getFinalizeResponseVO()]: Entering into getFinalizeResponseVO()...");
		}
		if(finalizeSaleResponseInfo != null && finalizeSaleResponseInfo.getResponse() != null){
			finalizeSaleResponseVO = new FinalizeSaleResponseVO();
			//commenting as this is not required in success scenario
			 ResponseVO responseVO = new ResponseVO();
			
			responseVO.setCode(finalizeSaleResponseInfo.getResponse().getCode());
			responseVO.setDescription(finalizeSaleResponseInfo.getResponse().getDescription());
			finalizeSaleResponseVO.setResponse(responseVO);
			
			ShoppingCartResponseInfo shoppingCartResponse  = finalizeSaleResponseInfo.getShoppingCartResponse();
			if(shoppingCartResponse != null){
				ShoppingCartVO shoppingCartVO = new ShoppingCartVO();
				shoppingCartVO.setSaleId(shoppingCartResponse.getSaleId());					
				ShoppingCartResponseInfoCustomer customer = shoppingCartResponse.getCustomer();
				if(customer != null){
					shoppingCartVO.setBillingAccountNumber(customer.getBillingAccountNumber());
					if(customer.getName() != null){							
						shoppingCartVO.setCustomerFirstName(customer.getName().getFirstName());
						shoppingCartVO.setCustomerLastName(customer.getName().getLastName());							
					}						
				}
				shoppingCartVO.setNote(shoppingCartResponse.getNote());
				if(shoppingCartResponse.getSalesTransactionType() != null){
					shoppingCartVO.setSalesTransactionType(shoppingCartResponse.getSalesTransactionType().toString());
				}
				
				WebCustomerAddressInfo shippAddress = shoppingCartResponse.getShippingAddress();
				if(shippAddress != null){
					AddressVO shippAddressVO = new AddressVO();
					shippAddressVO.setAddressLine1(shippAddress.getAddressLine1());
					shippAddressVO.setCity(shippAddress.getCity());
					if(shippAddress.getState() != null){
						shippAddressVO.setState(shippAddress.getState().toString());
					}
					shippAddressVO.setZipCode(shippAddress.getPostalCode());
					shippAddressVO.setCountry(getCountry(shippAddress.getCountry()));
					shoppingCartVO.setShippingAddress(shippAddressVO);
										
					shoppingCartVO.setCustomerFirstName(shippAddress.getFirstName());
					shoppingCartVO.setCustomerLastName(shippAddress.getLastName());						
				}				
				finalizeSaleResponseVO.setShoppingCartResponse(shoppingCartVO);
			}
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->getFinalizeResponseVO()]: Exiting getFinalizeResponseVO()...");
		}
		return finalizeSaleResponseVO;
	}
	
	
	/**
	 * This method takes the CompleteSaleResponseInfo coming from ESP response as input and fills the application specific CompleteSaleResponseVO
	 * @param completeSaleResponseInfo
	 * @return
	 */
	public CompleteSaleResponseVO getCompleteSaleResponseVO(CompleteSaleResponseInfo completeSaleResponseInfo) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->getCompleteSaleResponseVO()]: Entering into completeSale()...");
		}
		CompleteSaleResponseVO completeSaleResponseVO = null;
		if(completeSaleResponseInfo != null && completeSaleResponseInfo.getResponse() != null){
			completeSaleResponseVO = new CompleteSaleResponseVO();
			
			ResponseVO responseVO = new ResponseVO();
			responseVO.setCode(completeSaleResponseInfo.getResponse().getCode());
			responseVO.setDescription(completeSaleResponseInfo.getResponse().getDescription());
			completeSaleResponseVO.setResponse(responseVO); 
			
			ShoppingCartResponseInfo shoppingCartResponse  = completeSaleResponseInfo.getShoppingCartResponse();
			if(shoppingCartResponse != null){
				ShoppingCartVO shoppingCartVO = new ShoppingCartVO();
				shoppingCartVO.setSaleId(shoppingCartResponse.getSaleId());
				PaymentInfo[] paymentInfos = shoppingCartResponse.getPayment();
				if(paymentInfos != null && paymentInfos.length > 0){
					List<String> sourceSystemPaymentTransactionIds = new ArrayList<String>();						
					for(PaymentInfo paymentInfo : paymentInfos){
						if((paymentInfo.getSourceSystemPaymentTransactionId() != null) && (paymentInfo.getSourceSystemPaymentTransactionId().length() > 0)){
							sourceSystemPaymentTransactionIds.add(paymentInfo.getSourceSystemPaymentTransactionId());
						}
					}
					shoppingCartVO.setSourceSystemPaymentTransactionId(sourceSystemPaymentTransactionIds);						
				}
				completeSaleResponseVO.setShoppingCartResponse(shoppingCartVO);
			}
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->getCompleteSaleResponseVO()]: Exiting completeSale()...");
		}
		return completeSaleResponseVO;
	}
	
	
	/**
	 * This method takes the ValidateAddressResponseInfo coming from ESP response as input and fills the application specific ValidateAddressResponseVO
	 * @param validateAddressResponseInfo
	 * @return
	 */
	public ValidateAddressResponseVO getValidateAddressResponseVO(ValidateAddressResponseInfo validateAddressResponseInfo) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->getValidateAddressResponseVO()]: Entering into getValidateAddressResponseVO()...");
		}
		ValidateAddressResponseVO validateAddressResponseVO = null;				
		if(validateAddressResponseInfo != null && validateAddressResponseInfo.getResponse(INT_ZERO) != null){
			
			ResponseInfo responseInfo = validateAddressResponseInfo.getResponse(INT_ZERO);
			ResponseVO responseVO = new ResponseVO();
			responseVO.setCode(responseInfo.getCode());
			responseVO.setDescription(responseInfo.getDescription());
			validateAddressResponseVO = new ValidateAddressResponseVO();
			validateAddressResponseVO.setResponse(responseVO);
			ValidateAddressResponseInfoValidateAddressResponse validateAddressResponses = validateAddressResponseInfo.getValidateAddressResponse();
			if(validateAddressResponses != null){
				ValidateAddressResponseInfoValidateAddressResponseGeoCode[] geoCodes = validateAddressResponses.getGeoCode();
				//QC 7789 - Business confirmed to check the existance of Geocode to decide
				//account & billing addresses are valid or not
				if(geoCodes != null && geoCodes.length > 0){
					validateAddressResponseVO.setGeoCodeValue(geoCodes[0].getGeoCodeValue());
				}
				ValidateAddressResponseInfoValidateAddressResponseAddress[] validateAddressResponseAddresses = validateAddressResponses.getAddress();				
				if(validateAddressResponseAddresses != null && validateAddressResponseAddresses.length > 0){
					AddressVO addressVO1 = null;
					AddressInfo addressInfo = null;
					List<AddressVO> normalizedAddresses = new ArrayList<AddressVO>();
					for(ValidateAddressResponseInfoValidateAddressResponseAddress validateAddressResponseAddresse : validateAddressResponseAddresses){
						addressInfo = validateAddressResponseAddresse.getNormalizedAddress();
						if(addressInfo != null){
							addressVO1 = getAddressVO(addressInfo);								
							normalizedAddresses.add(addressVO1);
						}
					}
					validateAddressResponseVO.setNormalizedAddresses(normalizedAddresses);
				}
			}
			
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->getValidateAddressResponseVO()]: Exiting getValidateAddressResponseVO()...");
		}
		return validateAddressResponseVO;
	}
	
	
	/**
	 * This method takes the InquireDeliveryEstimateResponseInfo coming from ESP response as input and fills the application specific InquireDeliveryEstimateResponseVO
	 * @param inquireDeliveryEstimateResponseInfo
	 * @return
	 */
	public InquireDeliveryEstimateResponseVO getInquireDeliveryEstimateResponseVO(InquireDeliveryEstimateResponseInfo inquireDeliveryEstimateResponseInfo) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->getInquireDeliveryEstimateResponseVO()]: Entering into getInquireDeliveryEstimateResponseVO()...");
		}
		InquireDeliveryEstimateResponseVO inquireDeliveryEstimateResponseVO = null;
		if(inquireDeliveryEstimateResponseInfo != null){
			ResponseInfo responseInfo = inquireDeliveryEstimateResponseInfo.getResponse(INT_ZERO);
			if(responseInfo != null){
				inquireDeliveryEstimateResponseVO = new InquireDeliveryEstimateResponseVO();
				
				ResponseVO response = new ResponseVO();
				response.setCode(responseInfo.getCode());
				response.setDescription(responseInfo.getDescription());
				inquireDeliveryEstimateResponseVO.setResponse(response); 
				
				InquireDeliveryEstimateResponseInfoInquireDeliveryEstimateResponseDeliveryService[] deliveryServices = inquireDeliveryEstimateResponseInfo.getInquireDeliveryEstimateResponse();
				if(deliveryServices != null && deliveryServices.length > 1){
					DeliveryServiceVO deliveryServiceVO = null;
					List<DeliveryServiceVO> deliveryServiceVOs = new ArrayList<DeliveryServiceVO>();
					for(InquireDeliveryEstimateResponseInfoInquireDeliveryEstimateResponseDeliveryService ds : deliveryServices){
						deliveryServiceVO = new DeliveryServiceVO();
						if(ds.getService() != null){
							deliveryServiceVO.setServiceCode(ds.getService().getCode());
							deliveryServiceVO.setServiceDescription(ds.getService().getDescription());
						}
						if(ds.getEstimatedDeliveryDate() != null){
							deliveryServiceVO.setEstimatedDeliveryDate(ds.getEstimatedDeliveryDate());
						}
						deliveryServiceVOs.add(deliveryServiceVO);
					}
					inquireDeliveryEstimateResponseVO.setDeliveryService(deliveryServiceVOs);
				}				
			}
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->getInquireDeliveryEstimateResponseVO()]: Exiting getInquireDeliveryEstimateResponseVO()...");
		}
		return inquireDeliveryEstimateResponseVO;
	}
	
	/**
	 * This method sets the createActivationQuote response from ESP to ATG VO 
	 * createActivationQuoteRespose
	 * @param activationQuoteResponseVO
	 * @param activationQuoteResponseInfo
	 * @return
	 */
	public ActivationQuoteResponseVO createActivationQuoteResponse(CreateActivationQuoteResponseInfo activationQuoteResponseInfo) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->createActivationQuoteResponse()]: Entering into createActivationQuoteResponse()...");
		}
		ActivationQuoteResponseVO activationQuoteResponseVO = null;
		BillingQuoteResponseInfo billingQuoteResponseInfo = activationQuoteResponseInfo.getActivationQuoteInfo();
		if(billingQuoteResponseInfo != null){
			activationQuoteResponseVO = new ActivationQuoteResponseVO();
			activationQuoteResponseVO.setBillingAccountNumber(billingQuoteResponseInfo.getBillingAccountNumber());
			activationQuoteResponseVO.setBillingQuoteId(billingQuoteResponseInfo.getBillingQuoteId());
			activationQuoteResponseVO.setCustomerId(billingQuoteResponseInfo.getCustomerId());
			
			BillingQuoteDetailsInfo billingQuoteDetailsInfo = billingQuoteResponseInfo.getBillingQuoteDetails();
			if(billingQuoteDetailsInfo != null){
				BillingQuoteDetailVO billingQuoteDetailVO = new BillingQuoteDetailVO();
				billingQuoteDetailVO.setNextBillDate(billingQuoteDetailsInfo.getNextBillDate());
				billingQuoteDetailVO.setOneTimeCharge(billingQuoteDetailsInfo.getOneTimeCharge());
				RecurringChargeInfo recurringChargeInfo = billingQuoteDetailsInfo.getTotalRecurringCharges();
				if(recurringChargeInfo != null){
					billingQuoteDetailVO.setTotalRecurringCharges_amount(recurringChargeInfo.getAmount());
					billingQuoteDetailVO.setTotalRecurringCharges_chargePeriod(recurringChargeInfo.getChargePeriod());
					if(recurringChargeInfo.getChargePeriodUom() != null){
						billingQuoteDetailVO.setTotalRecurringCharges_chargePeriodUom(recurringChargeInfo.getChargePeriodUom().toString());
					}
				}
				RecurringChargeInfo savingsChargeInfo = billingQuoteDetailsInfo.getTotalRecurringSavings();
				if(savingsChargeInfo != null){
					billingQuoteDetailVO.setTotalRecurringSavings_amount(savingsChargeInfo.getAmount());
					billingQuoteDetailVO.setTotalRecurringSavings_chargePeriod(savingsChargeInfo.getChargePeriod());
					if(savingsChargeInfo.getChargePeriodUom() != null){
						billingQuoteDetailVO.setTotalRecurringSavings_chargePeriodUom(savingsChargeInfo.getChargePeriodUom().toString());
					}
				}						
				activationQuoteResponseVO.setBillingQuoteDetail(billingQuoteDetailVO);
			}
			HotBillChargesInfo[] hotBillChargesInfos = billingQuoteResponseInfo.getHotBillCharges();
			if(hotBillChargesInfos != null && hotBillChargesInfos.length > 0 ){
				List<HotBillChargeVO> hotBillChargeVOs = new ArrayList<HotBillChargeVO>();
				HotBillChargeVO hotBillChargeVO = null;
				for(HotBillChargesInfo hotBillChargesInfo :hotBillChargesInfos){
					hotBillChargeVO = new HotBillChargeVO();
					
					hotBillChargeVO.setFuturePeriodBoo(hotBillChargesInfo.isFuturePeriodBoo());
					EffectiveDatesInfo effDatesInfo = hotBillChargesInfo.getEffectiveDate();
					if(effDatesInfo != null){
						hotBillChargeVO.setEffectiveDate(effDatesInfo.getEffectiveDate());
						hotBillChargeVO.setExpirationDate(effDatesInfo.getExpirationDate());
					}
					hotBillChargeVO.setChargeTotal(hotBillChargesInfo.getChargeTotal());
					hotBillChargeVO.setTaxTotal(hotBillChargesInfo.getTaxTotal());
					
					AccountQuoteChargesInfo[] accountQuoteChargesInfo = hotBillChargesInfo.getAccountQuoteCharges();
					if(accountQuoteChargesInfo != null && accountQuoteChargesInfo.length > 0){
						AccountQuoteChargesInfo accQuoteChargesInfo = accountQuoteChargesInfo[INT_ZERO];
						if(accQuoteChargesInfo != null){
							hotBillChargeVO.setAccountQuoteCharges_chargeTotal(accQuoteChargesInfo.getChargeTotal());
							EffectiveDatesInfo effectiveDatesInfo1 = accQuoteChargesInfo.getEffectiveDate();
							if(effectiveDatesInfo1 != null){
								hotBillChargeVO.setAccountQuoteCharges_effectiveDate(effectiveDatesInfo1.getEffectiveDate());
								hotBillChargeVO.setAccountQuoteCharges_expirationDate(effectiveDatesInfo1.getExpirationDate());
							}
							hotBillChargeVO.setAccountQuoteCharges_futurePeriodBoo(accQuoteChargesInfo.isFuturePeriodBoo());
							hotBillChargeVO.setAccountQuoteCharges_taxTotal(accQuoteChargesInfo.getTaxTotal());
						}
					}
					SubscriberQuoteChargesInfo[] subQuoteChargesInfos = hotBillChargesInfo.getSubscriberQuoteCharges();//scriberQuoteCharges(INT_ZERO);
					if(subQuoteChargesInfos != null && subQuoteChargesInfos.length > 0){
						List<SubscriberQuoteChargesVO> subscriberQuoteChargeVOs = new ArrayList<SubscriberQuoteChargesVO>();
						List<SubscriberChargesVO> subscriberChargesVOs = null;
						SubscriberQuoteChargesVO subQuoteChargesVO = null;
						for(SubscriberQuoteChargesInfo subQuoteChargesInfo : subQuoteChargesInfos){	
							subQuoteChargesVO =  new SubscriberQuoteChargesVO();
							subQuoteChargesVO.setChargeTotal(subQuoteChargesInfo.getChargeTotal());
							subQuoteChargesVO.setTaxTotal(subQuoteChargesInfo.getTaxTotal());
							subQuoteChargesVO.setMdn(subQuoteChargesInfo.getMdn());
							subQuoteChargesVO.setMin(subQuoteChargesInfo.getMin());
							
							ChargeInfo[] chargeInfos = subQuoteChargesInfo.getSubscriberCharges();
							if(chargeInfos != null && chargeInfos.length > 0){
								  subscriberChargesVOs = new ArrayList<SubscriberChargesVO>();
								SubscriberChargesVO subscriberChargesVO = null;
								for(ChargeInfo chargeInfo : chargeInfos){
									subscriberChargesVO = new SubscriberChargesVO();
									subscriberChargesVO.setChargeAmount(chargeInfo.getChargeAmount());
									subscriberChargesVO.setChargeItemName(chargeInfo.getChargeItemName());
									subscriberChargesVO.setChargeItemTypeId(chargeInfo.getChargeItemTypeId());										
									subscriberChargesVO.setRecurringCharge(chargeInfo.isRecurringCharge());										
									subscriberChargesVO.setTaxAmount(chargeInfo.getTaxAmount());
									
									AdjustmentInfo[] adjustmentInfos = chargeInfo.getAdjustmentInfo();
									if(adjustmentInfos != null && adjustmentInfos.length > 0){
										AdjustmentInfo adjustmentInfo = chargeInfo.getAdjustmentInfo(INT_ZERO);
										if(adjustmentInfo != null){	
											if(adjustmentInfo.getAction() != null){
												subscriberChargesVO.setAction(adjustmentInfo.getAction().toString());
											}
											subscriberChargesVO.setCode(adjustmentInfo.getCode());
											subscriberChargesVO.setDescription(adjustmentInfo.getDescription());
											AdjustmentInfoPriceAdjustment priceAdjustment = adjustmentInfo.getPriceAdjustment();
											if(priceAdjustment != null){
												subscriberChargesVO.setDollarAdjustment(priceAdjustment.getDollarAdjustment());
											}										
										}
									}
									
									subscriberChargesVOs.add(subscriberChargesVO);
								}
								subQuoteChargesVO.setSubscriberCharges(subscriberChargesVOs);
							}
							
							subscriberQuoteChargeVOs.add(subQuoteChargesVO);
						}
						hotBillChargeVO.setSubscriberQuoteCharges(subscriberQuoteChargeVOs);
					}
					hotBillChargeVOs.add(hotBillChargeVO);
				}
				activationQuoteResponseVO.setHotBillCharge(hotBillChargeVOs);
			}
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->createActivationQuoteResponse()]: Exiting createActivationQuoteResponse()...");
		}
		return activationQuoteResponseVO;
	}
	
	
	/**
	 * @param shippingAddressData
	 * @param emailInfo
	 * @param account
	 * @return
	 */
	public CreateActivationQuoteRequestInfoAccount[] createCAQRequestInfoAccounts(
			CricketShippingAddressData shippingAddressData,
			EmailInfo emailInfo, CreateActivationQuoteRequestInfoAccount account,CricketOrderImpl cricketOrder) {

		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			
	    							
	    		logDebug("[CricketESPAdapterHelper->createCAQRequestInfoAccounts()]: Entering into createCAQRequestInfoAccounts()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		NameInfo name;
		CreateActivationQuoteRequestInfoAccountEquipmentShipmentInfo equipmentShipmentInfo = new CreateActivationQuoteRequestInfoAccountEquipmentShipmentInfo();
		CustomerContactInfo recipientShippingInfo = new CustomerContactInfo();
		name = new NameInfo();
		name.setFirstName(shippingAddressData.getShippingAddress().getFirstName());
		name.setLastName(shippingAddressData.getShippingAddress().getLastName());
		recipientShippingInfo.setName(name);						
		AddressInfo recipientShippingAddress = new AddressInfo();
		recipientShippingAddress.setAddressLine1(shippingAddressData.getShippingAddress().getAddress1());
		recipientShippingAddress.setCity(shippingAddressData.getShippingAddress().getCity());
		recipientShippingAddress.setState(AddressStateInfo.fromValue(shippingAddressData.getShippingAddress().getStateAddress()));
		AddressZipInfo zipInfo1 = new AddressZipInfo();
		zipInfo1.setZipCode(shippingAddressData.getShippingAddress().getPostalCode());
		recipientShippingAddress.setZip(zipInfo1);
		recipientShippingAddress.setCountry(getCountry(shippingAddressData.getShippingAddress().getCountry()));
		recipientShippingInfo.setAddress(recipientShippingAddress);
		recipientShippingInfo.setEmail(emailInfo);
		
		equipmentShipmentInfo.setRecipientShippingInfo(recipientShippingInfo);

		CreateActivationQuoteRequestInfoAccountEquipmentShipmentInfoShippingHeader shippingHeader = new CreateActivationQuoteRequestInfoAccountEquipmentShipmentInfoShippingHeader();
		// Response from inventory estimate delivery 
		shippingHeader.setShipVia( getSelectedShippingMethod(cricketOrder)); 
		shippingHeader.setShipRequestDate(new Date());
		
		shippingHeader.setShipRequestFromState(AddressStateInfo.fromValue(shippingAddressData.getShippingAddress().getStateAddress()));
		equipmentShipmentInfo.setShippingHeader(shippingHeader);
		
		CreateActivationQuoteRequestInfoAccountEquipmentShipmentInfoShipmentOrderSummary shipmentOrderSummary = new CreateActivationQuoteRequestInfoAccountEquipmentShipmentInfoShipmentOrderSummary();
		
		shipmentOrderSummary.setOrderShipmentTaxAmount(new BigDecimal(cricketOrder.getPriceInfo().getShipping()));
		shipmentOrderSummary.setOrderShipmentChargeAmount(new BigDecimal(cricketOrder.getPriceInfo().getShipping()));
		shipmentOrderSummary.setOrderShipmentNetTotal(new BigDecimal(cricketOrder.getPriceInfo().getShipping()));
		shipmentOrderSummary.setShippingOrderType(getShippingOrderType());
		equipmentShipmentInfo.setShipmentOrderSummary(shipmentOrderSummary);
		
		account.setEquipmentShipmentInfo(equipmentShipmentInfo);
		
		CreateActivationQuoteRequestInfoAccount[] accounts = new CreateActivationQuoteRequestInfoAccount[1];
		accounts[0] = account;

		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 	    							
	    		logDebug("[CricketESPAdapterHelper->createCAQRequestInfoAccounts()]: Exiting createCAQRequestInfoAccounts()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return accounts;
	}
	
	
	/**
	 * @param inquireAccountResponseInfo
	 * @return
	 */
	public InquireAccountResponseVO inquireAccountResponse(
			InquireAccountResponseInfo inquireAccountResponseInfo) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->inquireAccountResponse()]: Entering into inquireAccountResponse()...");
		}
		InquireAccountResponseVO inquireAccountResponseVO = null;
		if(inquireAccountResponseInfo != null && inquireAccountResponseInfo.getResponse() != null){
			ResponseVO response = new ResponseVO();
			response.setCode(inquireAccountResponseInfo.getResponse().getCode());
			response.setDescription(inquireAccountResponseInfo.getResponse().getDescription());
			inquireAccountResponseVO = new InquireAccountResponseVO();
			inquireAccountResponseVO.setResponse(response);
			
			InquireAccountResponseInfoAccount account = inquireAccountResponseInfo.getAccount();
			if(account != null){
				inquireAccountResponseVO.setBillingAccountNumber(account.getBillingAccountNumber());
				if(account.getAccountType() != null){
					inquireAccountResponseVO.setAccountType(account.getAccountType().toString());
				}
				if(account.getAccountStatus() != null){
					inquireAccountResponseVO.setAccountStatus(account.getAccountStatus().toString());
				}
				BillingMarketInfo marketInfo = account.getBillingMarket();
				if(marketInfo != null){
					inquireAccountResponseVO.setMarketId(marketInfo.getMarketId());
					inquireAccountResponseVO.setJointVentureCode(marketInfo.getJointVentureCode());
					inquireAccountResponseVO.setRateCenterId(marketInfo.getRateCenterId());
				}
				AddressInfo marketAddressInfo = account.getMarketAddress();
				AddressVO addressVO = null;
					if(marketAddressInfo != null){
						addressVO = getAddressVO(marketAddressInfo);					
						inquireAccountResponseVO.setMarketAddress(addressVO);
				}
				inquireAccountResponseVO.setBillingCycleDate(account.getBillingCycleDate());
				BillingPreferencesInfo billingPreferencesInfo = account.getBillingPreferences();
				if(billingPreferencesInfo != null){
					if(billingPreferencesInfo.getLanguage() != null){
						inquireAccountResponseVO.setBillingPreferencesLanguage(billingPreferencesInfo.getLanguage().toString());
					}
					inquireAccountResponseVO.setBillingPreferencesABP(billingPreferencesInfo.getAutoBillPayment());
				}
				if(account.getSolicitationContactPreference() != null){
					inquireAccountResponseVO.setSolicitationContactPreference(account.getSolicitationContactPreference().toString());
				}
				
				InquireAccountResponseInfoAccountCustomer customer = account.getCustomer();
				if(customer != null){
					inquireAccountResponseVO.setCustomerId(customer.getCustomerId());
					if(customer.getCustomerType() != null){
						inquireAccountResponseVO.setCustomerType(customer.getCustomerType().toString());
					}
					NameInfo customerName = customer.getName();
					//fix for QC 8060, for some special accounts we get BusinessName element
					//instead of Name element in customer element
					if(customerName == null){
						NameBusinessInfo nameBusinessInfo = customer.getBusinessName();
						if(nameBusinessInfo != null){
							customerName = nameBusinessInfo.getContact();							
						}
					}
					//QC 8060 - fix end
					if(customerName != null){
						inquireAccountResponseVO.setFirstName(customerName.getFirstName());
						inquireAccountResponseVO.setLastName(customerName.getLastName());
					}
					AddressInfo billAddressInfo = customer.getBillingAddress();
					if(billAddressInfo != null){
						addressVO = getAddressVO(billAddressInfo);								
						inquireAccountResponseVO.setBillingAddress(addressVO);
					}
					if(customer.getEmail() != null){
						inquireAccountResponseVO.setEmailAddress(customer.getEmail().getEmailAddress());
					}
					if(customer.getPhone() != null){
						inquireAccountResponseVO.setPhone(customer.getPhone().getHomePhone());
					}
					if(customer.getIdentity() != null && customer.getIdentity().getBirth() != null){
						inquireAccountResponseVO.setDob(customer.getIdentity().getBirth().getDateOfBirth());
					}
					if(customer.getIdentity() != null && customer.getIdentity().getSocialSecurityNumber() != null){
						inquireAccountResponseVO.setSocialSecurityNumber(customer.getIdentity().getSocialSecurityNumber());
					}
				}
				
				InquireAccountResponseInfoAccountSubscriber[] subscribers = account.getSubscriber();
				if(subscribers != null && subscribers.length > 0){
					List<SubscriberVO> subscriberVOs = new ArrayList<SubscriberVO>();
					SubscriberVO subscriberVO = null;
					DeviceVO deviceVO = null;
					ManufacturerVO manufacturerVO = null;
					List<OfferingsVO> bundledOfferingVOs = null;
					List<OfferingsVO> additionalOfferingVOs = null;
					OfferingsVO offeringVO = null;
					for(InquireAccountResponseInfoAccountSubscriber subscriber : subscribers){
						subscriberVO = new SubscriberVO();
						subscriberVO.setMdn(subscriber.getMdn());
						if(subscriber.getSubscriberStatus() != null){
							subscriberVO.setSubscriberStatus(subscriber.getSubscriberStatus().toString());
						}
						if(subscriber.getSubscriptionType() != null){
							subscriberVO.setSubscriptionType(subscriber.getSubscriptionType().toString());
						}
						subscriberVO.setBillingResponsibility(subscriber.getBillingResponsibility());
						DeviceInfo deviceInfo = subscriber.getDevice();
						if(deviceInfo != null){
							deviceVO = new DeviceVO();
							if(deviceInfo.getEquipmentIdentifier() != null){
								deviceVO.setEsn(deviceInfo.getEquipmentIdentifier().getEsn());
								deviceVO.setImei(deviceInfo.getEquipmentIdentifier().getImei());
								deviceVO.setMeid(deviceInfo.getEquipmentIdentifier().getMeid());
							}
							ManufacturerInfo manufacturerInfo = deviceInfo.getManufacturer();
							if(manufacturerInfo != null){
								manufacturerVO = new ManufacturerVO();
								manufacturerVO.setMake(manufacturerInfo.getMake());
								manufacturerVO.setModel(manufacturerInfo.getModel());
								manufacturerVO.setPhoneCode(manufacturerInfo.getPhoneCode());
								manufacturerVO.setPhoneType(manufacturerInfo.getPhoneType());
								deviceVO.setManufacturerVO(manufacturerVO);
							}
						}
						EffectiveDatesInfo effectiveDatesInfo = subscriber.getEffectiveDate();
						if(effectiveDatesInfo != null){
							subscriberVO.setEffectiveDate(effectiveDatesInfo.getEffectiveDate());
						}
						ContractTermInfo contractTermInfo = subscriber.getContract();
						if(contractTermInfo != null){
							subscriberVO.setTerm(contractTermInfo.getTerm());
							if(contractTermInfo.getCommission() != null){
								subscriberVO.setLocationId(contractTermInfo.getCommission().getLocationId());
								subscriberVO.setSalesRepresentative(contractTermInfo.getCommission().getSalesRepresentative());
								subscriberVO.setSalesChannel(contractTermInfo.getCommission().getSalesChannel());
							}
						}
						PricePlanInfo pricePlanInfo = subscriber.getPricePlan();
						if(pricePlanInfo != null){
							subscriberVO.setPlanCode(pricePlanInfo.getPlanCode());
							if(pricePlanInfo.getPlanType() != null){
								subscriberVO.setPlanType(pricePlanInfo.getPlanType().toString());
							}
							PricePlanDetailInfo priceDetailInfo = pricePlanInfo.getPrimaryPricePlan();
							if(priceDetailInfo != null){
								subscriberVO.setPrimaryPricePlanName(priceDetailInfo.getPlanName());
								subscriberVO.setPrimaryPricePlanDescription(priceDetailInfo.getPlanDescription());
							}
							BundledOfferingsInfo[] bundledOfferingsInfo = pricePlanInfo.getBundledOfferings();
							if(bundledOfferingsInfo != null && bundledOfferingsInfo.length >0 ){
								bundledOfferingVOs = new ArrayList<OfferingsVO>();
								for(BundledOfferingsInfo bundledOffering : bundledOfferingsInfo){
									offeringVO = new OfferingsVO();
									offeringVO.setOfferingCode(bundledOffering.getOfferingCode());
									offeringVO.setOfferingName(bundledOffering.getOfferingName());
									offeringVO.setOfferingValue(bundledOffering.getOfferingValue());
									if(bundledOffering.getEffectiveDate() != null){
										offeringVO.setEffectiveDate(bundledOffering.getEffectiveDate().getEffectiveDate());
									}
									bundledOfferingVOs.add(offeringVO);
								}
								subscriberVO.setBundledOfferings(bundledOfferingVOs);
							}
							//added to fix QC 8166
							PricePlanInfoDataMeterInfo dataMeterInfo = pricePlanInfo.getDataMeterInfo();
							if(dataMeterInfo != null){
								if(dataMeterInfo.isIsDataAutoMetered()){
									subscriberVO.setDataAutoMetered(Boolean.valueOf(true));
								}
							}
							//end QC 8166
						}
						BundledOfferingsInfo[] additionalOfferingsInfo = subscriber.getAdditionalOfferings();
						if(additionalOfferingsInfo != null && additionalOfferingsInfo.length >0 ){
							additionalOfferingVOs = new ArrayList<OfferingsVO>();
							for(BundledOfferingsInfo additionalOffering : additionalOfferingsInfo){
								offeringVO = new OfferingsVO();
								offeringVO.setOfferingCode(additionalOffering.getOfferingCode());
								offeringVO.setOfferingName(additionalOffering.getOfferingName());
								offeringVO.setOfferingDescription(additionalOffering.getOfferingDescription());
								if(additionalOffering.getOfferTypeId() != null){
									offeringVO.setOfferTypeId(additionalOffering.getOfferTypeId().intValue());		
								}
								if(additionalOffering.getEffectiveDate() != null){
									offeringVO.setEffectiveDate(additionalOffering.getEffectiveDate().getEffectiveDate());
								}
								additionalOfferingVOs.add(offeringVO);
							}
							subscriberVO.setAdditionalOfferings(additionalOfferingVOs);
						}
						subscriberVOs.add(subscriberVO);
					}
					inquireAccountResponseVO.setSubscribers(subscriberVOs);
				}
			}
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapterHelper->inquireAccountResponse()]: Exiting inquireAccountResponse()...");
			}
		}
		return inquireAccountResponseVO;
	}	
	
	/**
	 * @param pBillingQuoteStatusVO
	 * @return
	 */
	public UpdateBillingQuoteStatusRequestInfo mapToBillingQuoteStatusRequest(
			CricketOrderImpl order,EspServiceResponseData espServiceResponseData) {

		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}	    							
	    		logDebug("[CricketESPAdapterHelper->mapToBillingQuoteStatusRequest()]: Entering into mapToBillingQuoteStatusRequest()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		UpdateBillingQuoteStatusRequestInfo billingQuoteStatusRequestInfo = new UpdateBillingQuoteStatusRequestInfo();
 		billingQuoteStatusRequestInfo.setQuoteId(order.getBillingQuoteId());
		billingQuoteStatusRequestInfo.setQuoteStatus(BillingQuoteStatusInfo.fromValue(getQuoteStatus())); 				
		billingQuoteStatusRequestInfo.setCommission(getDealerCommission());
		
		// add PaymentInformation for multiple LOS/UpgradePhone/chanageFeature/changePlan scenarios
		/*if(((TRANSACTION_TYPE_ADD.equalsIgnoreCase(order.getWorkOrderType()) && null == order.getCktPackages())  || 
			(null != order.getCktPackages() && order.getCktPackages().size()>1) || 
			TRANSACTION_TYPE_RRC.equalsIgnoreCase(order.getWorkOrderType())) && espServiceResponseData.getTotalServiceAmount() > 0.0)*/
		if(espServiceResponseData.getTotalServiceAmount() > 0.0 && (TRANSACTION_TYPE_ACT.equalsIgnoreCase(order.getWorkOrderType()) || TRANSACTION_TYPE_RRC.equalsIgnoreCase(order.getWorkOrderType())))
		{
			UpdateBillingQuoteStatusRequestInfoPaymentInformation paymentInfo = new UpdateBillingQuoteStatusRequestInfoPaymentInformation();
			GenericRecordLocatorInfo genericRecordLocatorInfo = new GenericRecordLocatorInfo();
			GenericRecordLocatorInfoAccountSelector accountSelectorInfo = new GenericRecordLocatorInfoAccountSelector();
			GenericRecordLocatorInfoAccountSelectorCustomerInformation customerInformation = new GenericRecordLocatorInfoAccountSelectorCustomerInformation();
			
			accountSelectorInfo.setBillingAccountNumber(order.getCricCustmerBillingNumber());
			customerInformation.setCustomerId(order.getCricCustomerId());
			
			accountSelectorInfo.setCustomerInformation(customerInformation);
			genericRecordLocatorInfo.setAccountSelector(accountSelectorInfo);
			
			paymentInfo.setGenericRecordLocator(genericRecordLocatorInfo);
			
			PostRegularPaymentInfo[] accountPayments = new PostRegularPaymentInfo[1];
			PostRegularPaymentInfo accountPayment = new PostRegularPaymentInfo();
			CricketCreditCard creditCard = getPaymentGroup(order);			 		 
			BigDecimal serviceAmount = BigDecimal.valueOf(espServiceResponseData.getTotalServiceAmount());			
			accountPayment.setPaymentAmount(serviceAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
  			accountPayment.setCreditCardType(CreditCardTypeInfo.fromValue(creditCard.getCreditCardType()));
			accountPayment.setTenderType(TenderTypeInfo.CREDIT);
			accountPayment.setPaymentApprovalTransactionId(order.getVestaSystemOrderId());
			
			accountPayments[0]=accountPayment;
			
			paymentInfo.setAccountPayments(accountPayments);
			billingQuoteStatusRequestInfo.setPaymentInformation(paymentInfo);
			
 		}

		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 					
	    		logDebug("[CricketESPAdapterHelper->mapToBillingQuoteStatusRequest()]: Exiting mapToBillingQuoteStatusRequest()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
 		return billingQuoteStatusRequestInfo;
	}
	
	/**
	 * @param pResponseInfo
	 * @return
	 */
	public UpdateBillingQuoteStatusResponseVO mapToBillingQuoteStatusResponse(
			UpdateBillingQuoteStatusResponseInfo pResponseInfo) {

		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 				
	    		logDebug("[CricketESPAdapterHelper->mapToBillingQuoteStatusResponse()]: Entering into mapToBillingQuoteStatusResponse()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pResponseInfo.getOrderInfo().getOrderId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		UpdateBillingQuoteStatusResponseVO responseBQSVO = new UpdateBillingQuoteStatusResponseVO();
		ESPResponseVO responseVO[] = new ESPResponseVO[pResponseInfo.getResponse().length];
		ResponseInfo responseInfo[] = pResponseInfo.getResponse();

		if(null!=pResponseInfo.getResponse()){
			int index=0;
			for(ResponseInfo response: responseInfo){
				responseVO[index]=new ESPResponseVO();
				responseVO[index].setCode(response.getCode());
				responseVO[index].setDescription(response.getDescription());
				index++;
			}
			responseBQSVO.setResponse(responseVO);
		}
		responseBQSVO.setOrderInfo(mapTOBQSResponseOrderVO(pResponseInfo.getOrderInfo()));
		if(null != pResponseInfo.getOrderInfo() && null!=pResponseInfo.getOrderInfo().getPaymentResult()){
		UpdateBillingQuoteStatusResponseInfoOrderInfoPaymentResult[] paymentResult = pResponseInfo.getOrderInfo().getPaymentResult();
			if(null != paymentResult && paymentResult.length>0){
				responseBQSVO.setPaymentApprovalTransactionId(paymentResult[0].getPaymentApprovalTransactionId());
			} 
		}
		//paymentResult[0]=responseBQSVO.getOrderInfo().
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 				
	    		logDebug("[CricketESPAdapterHelper->mapToBillingQuoteStatusResponse()]: Exiting mapToBillingQuoteStatusResponse()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pResponseInfo.getOrderInfo().getOrderId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return responseBQSVO;
	}
	
	/**
	 * @param pOrderInfo
	 * @return
	 */
	private UpdateBillingQuoteStatusResponseOrderVO mapTOBQSResponseOrderVO(
			UpdateBillingQuoteStatusResponseInfoOrderInfo pOrderInfo) {

		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 			
	    		logDebug("[CricketESPAdapterHelper->mapTOBQSResponseOrderVO()]: Entering into mapTOBQSResponseOrderVO()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderInfo.getOrderId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		UpdateBillingQuoteStatusResponseOrderVO orderResponseVO = new UpdateBillingQuoteStatusResponseOrderVO();
		orderResponseVO.setOrderId(pOrderInfo.getOrderId());
 		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 				
	    		logDebug("[CricketESPAdapterHelper->mapTOBQSResponseOrderVO()]: Exiting mapTOBQSResponseOrderVO()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + pOrderInfo.getOrderId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return orderResponseVO;
	}	
	
	/**
	 * @param pResponseInfo
	 * @return
	 */
	public ESPResponseVO[] mapToBillingQuoteResponse(
			ResponseInfo[] pResponseInfo) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->mapToBillingQuoteResponse()]: Entering into mapToBillingQuoteResponse()...");
		}
		
		ESPResponseVO billingQuoteResponseVO[] = new ESPResponseVO[pResponseInfo.length];		
		int index=0;
		for(ResponseInfo response:pResponseInfo){
			billingQuoteResponseVO[index]=new ESPResponseVO();
			billingQuoteResponseVO[index].setCode(response.getCode());
			billingQuoteResponseVO[index].setDescription(response.getDescription());
			
			index++;
		}
		if(isLoggingDebug()){		
			logDebug("[CricketESPAdapterHelper->mapToBillingQuoteResponse()]: Exiting mapToBillingQuoteResponse()...");
		}
		return billingQuoteResponseVO;
	}
	

	/**
	 * @param pBillingQuoteRequest
	 * @return
	 */
	public UpdateBillingQuoteRequestInfo mapTOBillingQuoteRequest(
			CricketOrderImpl crkOrder,EspServiceResponseData espServiceResponseData) {
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}						
	    		logDebug("[CricketESPAdapterHelper->mapTOBillingQuoteRequest()]: Entering into mapTOBillingQuoteRequest()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + crkOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		UpdateBillingQuoteRequestInfo billingQuoteRequestInfo =  new UpdateBillingQuoteRequestInfo();
		
 		billingQuoteRequestInfo.setBillingQuoteNumber(crkOrder.getBillingQuoteId());
 		
 		billingQuoteRequestInfo.setMyCricketHeldPaymentAmount(BigDecimal.valueOf((Double.valueOf(new DecimalFormat("###.##").format(crkOrder.getPriceInfo().getTotal())))));
		billingQuoteRequestInfo.setMyCricketHeldPaymentId(crkOrder.getVestaSystemOrderId());
 		billingQuoteRequestInfo.setPosTotalAmount(BigDecimal.valueOf((Double.valueOf(new DecimalFormat("###.##").format(crkOrder.getPriceInfo().getTotal())))));
		billingQuoteRequestInfo.setPosTransactionId(crkOrder.getPosSaleId());
		
		if(crkOrder.getWorkOrderType() !=null && !TRANSACTION_TYPE_OXC.equals(crkOrder.getWorkOrderType())){
			if(null!=espServiceResponseData){
			 List<CAQServiceAmount> serviceAmountList=  espServiceResponseData.getServiceAmountList();
			  if(null!=serviceAmountList && serviceAmountList.size()>0){
	 				UpdateBillingQuoteRequestInfoServiceAmounts[] serviceAmountsInfo = new 
	 						UpdateBillingQuoteRequestInfoServiceAmounts[serviceAmountList.size()];
					int index=0;
				   for(CAQServiceAmount caqServiceAmount:serviceAmountList){
						serviceAmountsInfo[index]=new UpdateBillingQuoteRequestInfoServiceAmounts();
						serviceAmountsInfo[index].setMdn(caqServiceAmount.getMdn());
						serviceAmountsInfo[index].setServiceAmount(caqServiceAmount.getServiceAmount().add(caqServiceAmount.getServiceTax()));			
						index++;
				  }
					billingQuoteRequestInfo.setServiceAmounts(serviceAmountsInfo);
			   }
			}
		}
 		GenericRecordLocatorInfo genericLocatorInfo = new GenericRecordLocatorInfo();
		
 		GenericRecordLocatorInfoAccountSelector accountSelectorInfo = new GenericRecordLocatorInfoAccountSelector();
		accountSelectorInfo.setBillingAccountNumber(crkOrder.getCricCustmerBillingNumber());

		genericLocatorInfo.setAccountSelector(accountSelectorInfo);		
		billingQuoteRequestInfo.setGenericRecordLocator(genericLocatorInfo); 
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}							
	    		logDebug("[CricketESPAdapterHelper->mapTOBillingQuoteRequest()]: Exiting mapTOBillingQuoteRequest()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + crkOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return billingQuoteRequestInfo;
	}  
	
	/**
	 * @param pResponseInfo
	 * @return
	 */
	public InquireBillingOrderDetailsResponseVO mapTOBillingOrderDetailsResponse(
			InquireBillingOrderDetailsResponseInfo pResponseInfo) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->mapTOBillingOrderDetailsResponse()]: Entering into mapTOBillingOrderDetailsResponse()...");
		}
		InquireBillingOrderDetailsResponseVO responseVO = new InquireBillingOrderDetailsResponseVO();
		if(pResponseInfo!= null){
			InquireBillingOrderDetailsResponseInfoBillingOrderDetailsBillingOrderInfo billingOrderInfo = pResponseInfo.getBillingOrderDetails().getBillingOrderInfo();
			responseVO.setBillingOrderNumber(billingOrderInfo.getBillingOrderNumber());
			responseVO.setBillingQuoteNumber(billingOrderInfo.getBillingQuoteNumber());
			responseVO.setCreatedDate(billingOrderInfo.getCreatedDate());
			responseVO.setExecutedDate(billingOrderInfo.getExecutedDate());
			responseVO.setExternalId(billingOrderInfo.getExternalId());
			responseVO.setLastModifiedDate(billingOrderInfo.getLastModifiedDate());
			responseVO.setMdn(billingOrderInfo.getMdn());
			responseVO.setMin(billingOrderInfo.getMin());
			
			responseVO.setOrderTypeCode(billingOrderInfo.getOrderTypeCode().getValue());
			responseVO.setPurchaseOrderNumber(billingOrderInfo.getPurchaseOrderNumber());
			responseVO.setSubmittedDate(billingOrderInfo.getSubmittedDate());
			responseVO.setSalesRepresentativeInfo(billingOrderInfo.getSalesRepresentative());
			responseVO.setTrackingNumber(billingOrderInfo.getEquipmentShipmentInfo().getTrackingNumber());
			if(billingOrderInfo.getOrderStatus().getValue().equals(CricketCommonConstants.CHAR_D))
				responseVO.setOrderStatus(getCktConfiguration().getOrderStatusMsg_D());
			else if(billingOrderInfo.getOrderStatus().getValue().equals(CricketCommonConstants.CHAR_X))
				responseVO.setOrderStatus(getCktConfiguration().getOrderStatusMsg_X());
			else if(billingOrderInfo.getOrderStatus().getValue().equals(CricketCommonConstants.CHAR_R))
				responseVO.setOrderStatus(getCktConfiguration().getOrderStatusMsg_R());
			else if(billingOrderInfo.getOrderStatus().getValue().equals(CricketCommonConstants.CHAR_C))
				responseVO.setOrderStatus(getCktConfiguration().getOrderStatusMsg_C());
			else if(billingOrderInfo.getOrderStatus().getValue().equals(CricketCommonConstants.CHAR_E))
				responseVO.setOrderStatus(getCktConfiguration().getOrderStatusMsg_E());
			else if(billingOrderInfo.getOrderStatus().getValue().equals(CricketCommonConstants.CHAR_P))
				responseVO.setOrderStatus(getCktConfiguration().getOrderStatusMsg_P());
			else 
				responseVO.setOrderStatus(CricketCommonConstants.UNKNOWN);
			
					
						
			
			responseVO.setIfOrderExist(true);
		}		
		else{
			responseVO.setIfOrderExist(false);
		}
		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->mapTOBillingOrderDetailsResponse()]: Exiting mapTOBillingOrderDetailsResponse()...");
		}
		
		return responseVO;
	}

	
	/**
	 * @param pResposneInfo
	 * @return
	 */
	public UpdateSubscriberResponseVO mapTOUpdateSubscriberResponseVO(
			UpdateSubscriberResponseInfo pResposneInfo) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->mapTOUpdateSubscriberResponseVO()]: Entering into mapTOUpdateSubscriberResponseVO()...");
		}
		
		UpdateSubscriberResponseVO responseVO = new UpdateSubscriberResponseVO();
		BillingquoteResponseVO billingQuote = new BillingquoteResponseVO();
		BillingQuoteResponseInfo billingQuoteInfo = pResposneInfo.getSubscriberResponseInfo().getBillingQuoteResponse();
		billingQuote.setBillingQuoteId(billingQuoteInfo.getBillingQuoteId());
		billingQuote.setBillingAccountNumber(billingQuoteInfo.getBillingAccountNumber());
		DeviceVO deviceVO = new DeviceVO();
		ManufacturerVO manufacturerVO = new ManufacturerVO();
		
		manufacturerVO.setMake(billingQuoteInfo.getNewDeviceInfo().getManufacturer().getMake());
		deviceVO.setManufacturerVO(manufacturerVO); 
		billingQuote.setDeviceVO(deviceVO);
		
		SubscriberBillingQuoteDetailVO billingQuoteDetailVO = new SubscriberBillingQuoteDetailVO();
		RecurringChargesVO recurringChargesVO = new RecurringChargesVO();
		RecurringChargesVO recurringSavingsVO = new RecurringChargesVO();
		
		recurringChargesVO.setAmount(billingQuoteInfo.getBillingQuoteDetails().getTotalRecurringCharges().getAmount());
		recurringChargesVO.setChargePeriod(billingQuoteInfo.getBillingQuoteDetails().getTotalRecurringCharges().getChargePeriod());
		recurringChargesVO.setChargePeriodUom(billingQuoteInfo.getBillingQuoteDetails().getTotalRecurringCharges().getChargePeriodUom().getValue());
		
		billingQuoteDetailVO.setTotalRecurringCharges(recurringChargesVO);
		
		recurringSavingsVO.setAmount(billingQuoteInfo.getBillingQuoteDetails().getTotalRecurringSavings().getAmount());
		recurringSavingsVO.setChargePeriod(billingQuoteInfo.getBillingQuoteDetails().getTotalRecurringSavings().getChargePeriod());
			
		billingQuoteDetailVO.setTotalRecurringSavings(recurringSavingsVO);
		billingQuoteDetailVO.setNextBillDate(billingQuoteInfo.getBillingQuoteDetails().getNextBillDate());
		billingQuoteDetailVO.setOneTimeCharge(billingQuoteInfo.getBillingQuoteDetails().getOneTimeCharge());
		billingQuote.setBillingQuoteDetail(billingQuoteDetailVO);
		
		List<HotBillChargeVO> billChargeVOs = new ArrayList<HotBillChargeVO>();
		
		HotBillChargesInfo[] hotBillChargesInfo = billingQuoteInfo.getHotBillCharges();
		
		HotBillChargeVO billCharge = null;
		if(hotBillChargesInfo != null){
			for(HotBillChargesInfo billChargesInfo: hotBillChargesInfo){
				billCharge = new HotBillChargeVO();
				billCharge.setFuturePeriodBoo(billChargesInfo.isFuturePeriodBoo());
				billCharge.setAccountQuoteCharges_effectiveDate(billChargesInfo.getEffectiveDate().getEffectiveDate());
				billCharge.setAccountQuoteCharges_expirationDate(billChargesInfo.getEffectiveDate().getExpirationDate());
				billCharge.setChargeTotal(billChargesInfo.getChargeTotal());
				billCharge.setTaxTotal(billChargesInfo.getTaxTotal());
				
				SubscriberQuoteChargesInfo[] quoteChargesInfos = billChargesInfo.getSubscriberQuoteCharges();
				if(quoteChargesInfos != null && quoteChargesInfos.length > 0){
					List<SubscriberQuoteChargesVO> subscriberQuoteChargesVOs = new ArrayList<SubscriberQuoteChargesVO>();
					SubscriberQuoteChargesVO subscriberQuoteChargesVO = null;
					for(SubscriberQuoteChargesInfo quoteChargesInfo : quoteChargesInfos){
						subscriberQuoteChargesVO = new SubscriberQuoteChargesVO();
						subscriberQuoteChargesVO.setChargeTotal(quoteChargesInfo.getChargeTotal());
						subscriberQuoteChargesVO.setTaxTotal(quoteChargesInfo.getTaxTotal());
						subscriberQuoteChargesVO.setEquipmentIdentifer(quoteChargesInfo.getEquipmentIdentifier().getEsn());
						
						ChargeInfo subscribeChargeInfo[] = quoteChargesInfo.getSubscriberCharges();					
						if(subscribeChargeInfo != null && subscribeChargeInfo.length > 0){
							List<SubscriberChargesVO> subscriberChargesVOs = new ArrayList<SubscriberChargesVO>();
							SubscriberChargesVO chargesVO = null;
							for(ChargeInfo chargeInfo:subscribeChargeInfo){
								chargesVO = new SubscriberChargesVO();
								chargesVO.setChargeItemName(chargeInfo.getChargeItemName());
								chargesVO.setChargeItemTypeId(chargeInfo.getChargeItemTypeId());
								chargesVO.setChargeAmount(chargeInfo.getChargeAmount());
								chargesVO.setTaxAmount(chargeInfo.getTaxAmount());
								chargesVO.setRecurringCharge(chargeInfo.isRecurringCharge());
								if(chargeInfo.getAdjustmentInfo()!=null && chargeInfo.getAdjustmentInfo().length>0){
									AdjustmentInfo adjustmentInfo = chargeInfo.getAdjustmentInfo(0);
									chargesVO.setAction(adjustmentInfo.getAction().getValue());
									chargesVO.setCode(adjustmentInfo.getCode());
									chargesVO.setDescription(adjustmentInfo.getCode());
									chargesVO.setDollarAdjustment(adjustmentInfo.getPriceAdjustment().getDollarAdjustment());
								}
								
								subscriberChargesVOs.add(chargesVO);
							}
							subscriberQuoteChargesVO.setSubscriberCharges(subscriberChargesVOs);
						}
						subscriberQuoteChargesVOs.add(subscriberQuoteChargesVO);
						
					}
					billCharge.setSubscriberQuoteCharges(subscriberQuoteChargesVOs);
				}			
				billChargeVOs.add(billCharge);			
			}		
			billingQuote.setHotBillCharge(billChargeVOs);
		}
		
		responseVO.setBillingQuote(billingQuote);
		
		UpdateSubscriberResponseInfoSubscriberResponseInfoSubscriberProductsInfoSubscriberProducts[] subscriberProductsInfo = pResposneInfo.getSubscriberResponseInfo().getSubscriberProductsInfo();
		SubscriberProductsVO[] subscriberProductsVO = new SubscriberProductsVO[subscriberProductsInfo.length];
		int index=0;
		for(UpdateSubscriberResponseInfoSubscriberResponseInfoSubscriberProductsInfoSubscriberProducts subscriberProducts:subscriberProductsInfo){
			
			subscriberProductsVO[index]=new SubscriberProductsVO();
			subscriberProductsVO[index].setAction(subscriberProducts.getAction().getValue());
			subscriberProductsVO[index].setProductId(subscriberProducts.getProductId());
			subscriberProductsVO[index].setProductName(subscriberProducts.getProductName());
			subscriberProductsVO[index].setProductTypeId(subscriberProducts.getProductTypeId());
			
			index++;
		}
		
		responseVO.setSubscriberProducts(subscriberProductsVO);
		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->mapTOUpdateSubscriberResponseVO()]: Exiting mapTOUpdateSubscriberResponseVO()...");
		}
		return responseVO;
	}
	
	
	/**
	 * @param pResponseInfo
	 * @return
	 */
	public CreateShippingQuoteResponseVO mapTOCreateShippingResponseVO(
			CreateShippingQuoteResponseInfo pResponseInfo) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->mapTOCreateShippingResponseVO()]: Entering into mapTOCreateShippingResponseVO()...");
		}
		
		CreateShippingQuoteResponseVO createShippingresponseVO = new CreateShippingQuoteResponseVO();
		BillingQuoteResponseInfo billingQuoteResponse = pResponseInfo.getShippingQuoteInfo();
		
		createShippingresponseVO.setBillingAccountNumber(billingQuoteResponse.getBillingAccountNumber());
		createShippingresponseVO.setBillingQuoteId(billingQuoteResponse.getBillingQuoteId());
		
		BillingQuoteDetailsVO billingQuoteDetailsVO = new BillingQuoteDetailsVO();
		BillingQuoteDetailsInfo billingQuoteDetailsInfo = billingQuoteResponse.getBillingQuoteDetails();
		
		
		RecurringChargesVO totalRecurringCharges = new RecurringChargesVO();		
		totalRecurringCharges.setAmount(billingQuoteDetailsInfo.getTotalRecurringCharges().getAmount());
		totalRecurringCharges.setChargePeriod(billingQuoteDetailsInfo.getTotalRecurringCharges().getChargePeriod());
		totalRecurringCharges.setChargePeriodUom(billingQuoteDetailsInfo.getTotalRecurringCharges().getChargePeriodUom().getValue());		
		billingQuoteDetailsVO.setTotalRecurringCharges(totalRecurringCharges);
		
		
		RecurringChargesVO totalRecurringSavings = new RecurringChargesVO();		
		totalRecurringSavings.setAmount(billingQuoteDetailsInfo.getTotalRecurringSavings().getAmount());
		totalRecurringSavings.setChargePeriod(billingQuoteDetailsInfo.getTotalRecurringSavings().getChargePeriod());
		billingQuoteDetailsVO.setTotalRecurringSavings(totalRecurringSavings);
		
		billingQuoteDetailsVO.setOneTimeCharge(billingQuoteDetailsInfo.getOneTimeCharge());
		
		createShippingresponseVO.setBillingQuoteDetails(billingQuoteDetailsVO);
		
		ResponseInfo[] responseInfo = pResponseInfo.getResponse();
		ESPResponseVO[] responseVO = new ESPResponseVO[responseInfo.length];
		int i=0;
		for(ResponseInfo response: responseInfo){
			responseVO[i]=new ESPResponseVO();
			responseVO[i].setCode(response.getCode());
			responseVO[i].setDescription(response.getDescription());
		}
		
		createShippingresponseVO.setResponseStatus(responseVO);
		 
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->mapTOCreateShippingResponseVO()]: Exiting mapTOCreateShippingResponseVO()...");
		}
		return createShippingresponseVO;
	}
	
	/**
	 * @param pResponseInfo
	 * @return
	 */
	public ESPResponseVO[] mapTOUpdateWebReportResponse(
			ResponseInfo[] pResponseInfo) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->mapTOUpdateWebReportResponse()]: Entering into mapTOUpdateWebReportResponse()...");
		}
		ESPResponseVO[] responseVO = new ESPResponseVO[pResponseInfo.length];
		int index=0;		
		for(ResponseInfo response: pResponseInfo){
			responseVO[index]=new ESPResponseVO();
			responseVO[index].setCode(response.getCode());
			responseVO[index].setDescription(response.getDescription());
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapterHelper->mapTOUpdateWebReportResponse()]: Exiting mapTOUpdateWebReportResponse()...");
		}
		return responseVO;
	}
	
	/**
	 * @param creditCard
	 * @return
	 */
	public ChargeCardInfo createChargeCardInfo(CricketCreditCard creditCard, boolean isManageABPMethodCall) {
		ChargeCardInfo chargeCardInfo = null;
		if(creditCard != null){
			chargeCardInfo = new ChargeCardInfo();
			if(null != creditCard.getVestaToken() && creditCard.getVestaToken().length() > 0){
				if(isLoggingDebug()){
					if(isManageABPMethodCall){
						logDebug("[CricketESPAdapterHelper->createChargeCardInfo()]: invoked from ManageABP() - Taking Dynamic Vesta Token");
					}else{
						logDebug("[CricketESPAdapterHelper->createChargeCardInfo()]: invoked from ManagePayment() -  Taking Dynamic Vesta Token");
					}
				}
				chargeCardInfo.setChargeCardToken(creditCard.getVestaToken());
			} else {
				if(isLoggingDebug()){
					if(isManageABPMethodCall){
						logDebug("[CricketESPAdapterHelper->createChargeCardInfo()]: invoked from ManageABP() -  Taking Default Vesta Token");
					}else{
						logDebug("[CricketESPAdapterHelper->createChargeCardInfo()]: invoked from ManagePayment()  Taking Default Vesta Token");
					}
					
				}
				chargeCardInfo.setChargeCardToken(getVestaTokenStub());  
			}
			
			CreditCardExpirationDateInfo creditCardExpirationDateInfo = new CreditCardExpirationDateInfo();
			creditCardExpirationDateInfo.setExpirationMonth(creditCard.getExpirationMonth());
			String twoDigitYear = creditCard.getExpirationYear().substring(2,creditCard.getExpirationYear().length());
			creditCardExpirationDateInfo.setExpirationYear(twoDigitYear);
			chargeCardInfo.setChargeCardExpirationDate(creditCardExpirationDateInfo);
			
			chargeCardInfo.setChargeCardCustomerName(creditCard.getCcFirstName()+ SINGLE_SPACE + creditCard.getCcLastName());
			chargeCardInfo.setCVN(creditCard.getCardVerificationNumber());
			Address creditCardBillingAddress = creditCard.getBillingAddress();
			if(creditCardBillingAddress != null){				
				chargeCardInfo.setChargeCardBillingAddress(getAddressInfo(creditCardBillingAddress));						
			}			
		}
		return chargeCardInfo;
	}
	
	/**
	 * @param pShippingQuoteRequest
	 * @return
	 */
	public CreateShippingQuoteRequestInfo mapTOShippingQuoteRequest(
														CricketShippingAddressData shippingAddressData,
														CricketAccountHolderAddressData accountHolderAddressData,
														CricketProfile cricketProfile,
														RepositoryItem profile, 
														MyCricketCookieLocationInfo locationInfo,
														CricketOrderImpl order) {
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			
	    		logDebug("[CricketESPAdapter->mapTOShippingQuoteRequest()]: Entering into mapTOShippingQuoteRequest()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		CreateShippingQuoteRequestInfo createShippingRequest = new CreateShippingQuoteRequestInfo();
		
		GenericRecordLocatorInfo recordLocatorInfo = new GenericRecordLocatorInfo();
		GenericRecordLocatorInfoAccountSelector accountSelectorInfo = new GenericRecordLocatorInfoAccountSelector();
		CreateShippingQuoteRequestInfoAccount[] accountInfo = new CreateShippingQuoteRequestInfoAccount[1];
 		CreateShippingQuoteRequestInfoAccount account = new  CreateShippingQuoteRequestInfoAccount();
		GenericRecordLocatorInfoAccountSelectorCustomerInformation customerInfo = new GenericRecordLocatorInfoAccountSelectorCustomerInformation();

 		// in case of non login user
		if(profile.isTransient()){
			accountSelectorInfo.setBillingAccountNumber(getNewUserBillingNumber());		 
			customerInfo.setCustomerId(getNewUserCustomerId());
			account.setBillingAccountNumber(getNewUserBillingNumber());			
		} else {
			accountSelectorInfo.setBillingAccountNumber(cricketProfile.getAccountNumber());	
			// get it from inqurireaccount response
			customerInfo.setCustomerId(cricketProfile.getCustomerId());
			account.setBillingAccountNumber(cricketProfile.getAccountNumber());		
		}
		accountSelectorInfo.setCustomerInformation(customerInfo);
		recordLocatorInfo.setAccountSelector(accountSelectorInfo);
		createShippingRequest.setGenericRecordLocator(recordLocatorInfo);		
		
		CreateShippingQuoteRequestInfoAccountSubscriber subscriberInfo =  null;
		CreateShippingQuoteRequestInfoAccountSubscriber[] subscriberInfoList = null;
		EffectiveDatesInfo effectiveDatesInfo = new EffectiveDatesInfo();
		int orderLinenumber = 0;
		String productName=null;
		RepositoryItem product=null;
		String productType = null;
 		double taxAmount = 0.0;	
		int subcriberIndex=0;
		ShipmentLineInfo shipmentLineInfo = null;
 		RepositoryItem skuItem =null;	
 		String manufacturerCode = null;
		String modelNumber = null;
		String serviceType=null;
		DeviceInfo deviceInfo = null;
		ManufacturerInfo manufacturerInfo = null;
		effectiveDatesInfo.setEffectiveDate(getPacificDateTime(new Date())); 
		
		ContractTermInfo contractTermInfo = new ContractTermInfo();
		contractTermInfo.setTerm(new BigInteger(String.valueOf(getContractTerm())));		
		contractTermInfo.setCommission(getDealerCommission());
		
 		account.setEffectiveDates(effectiveDatesInfo);
	 
		subscriberInfoList = new CreateShippingQuoteRequestInfoAccountSubscriber[1];
		subscriberInfo = new CreateShippingQuoteRequestInfoAccountSubscriber();
		subscriberInfo.setContract(contractTermInfo);				
			
		List<CricketCommerceItemImpl> commerceItemList = order.getCommerceItems();
		int commerceItemCount = commerceItemList.size();
		
		if(commerceItemList != null && commerceItemCount >0){
				ShipmentLineInfo[] shipmentLineInfos = new ShipmentLineInfo[commerceItemCount];
			for(CricketCommerceItemImpl commerceItem:commerceItemList)
			{
				shipmentLineInfo = new ShipmentLineInfo();
				productType = commerceItem.getCricItemTypes();
				product = (RepositoryItem)commerceItem.getAuxiliaryData().getProductRef();
				productName=(String)product.getPropertyValue(PROPERTY_DISPLAY_NAME);	
				shipmentLineInfo.setOrderLineNumber(String.valueOf(orderLinenumber));
				if(isLoggingDebug()){
					logDebug("[CricketESPAdapter->mapTOShippingQuoteRequest()]: CommerceItem info - ProductName: "+ productName + " ProductId: "+ commerceItem.getCatalogRefId() + " ProductType: "+productType);
				}
				shipmentLineInfo.setItemId(commerceItem.getCatalogRefId());							
				shipmentLineInfo.setQuantity((int)commerceItem.getQuantity());
				shipmentLineInfo.setUnitOfMeasure(UnitOfMeasureInfo.fromValue(UNIT_OF_MEASURE_EA));
				shipmentLineInfo.setProductName(productName);																
				shipmentLineInfo.setBasePrice(BigDecimal.valueOf(commerceItem.getPriceInfo().getAmount()));
				shipmentLineInfo.setLineTax1(BigDecimal.valueOf(taxAmount));
				
				if ((null!=productType &&  UPGRADE_PHONE.equalsIgnoreCase(productType))){
								deviceInfo = new DeviceInfo();
								manufacturerInfo = new ManufacturerInfo();
								skuItem =(RepositoryItem)(commerceItem.getAuxiliaryData().getCatalogRef());
								manufacturerCode =((String)skuItem.getPropertyValue(MANUFACTURER_CODE)) == null ? EMPTY_STRING :
													((String)skuItem.getPropertyValue(MANUFACTURER_CODE)).trim();
								modelNumber=((String)skuItem.getPropertyValue(MODEL_NUMBER)) == null ? EMPTY_STRING :
									((String)skuItem.getPropertyValue(MODEL_NUMBER)).trim();
								serviceType=((String)skuItem.getPropertyValue(SERVICE_TYPE)) == null ? EMPTY_STRING :
									((String)skuItem.getPropertyValue(SERVICE_TYPE)).trim();
								manufacturerInfo.setModel(manufacturerCode); 
								manufacturerInfo.setPhoneCode(modelNumber);
								manufacturerInfo.setPhoneType(serviceType);
							 	deviceInfo.setManufacturer(manufacturerInfo);
								shipmentLineInfo.setDeviceInfo(deviceInfo);										 
								 
				} 
							
				shipmentLineInfos[orderLinenumber] = shipmentLineInfo;	
				orderLinenumber++;	
			} // end of for loop
			subscriberInfo.setShippingOrderLines(shipmentLineInfos);
			subscriberInfoList[subcriberIndex] =subscriberInfo;
		}
					
		 // now setting subscription to account
		account.setSubscriber(subscriberInfoList);

		NameInfo name;
		CreateShippingQuoteRequestInfoAccountEquipmentShipmentInfo equipmentShipmentInfo = new CreateShippingQuoteRequestInfoAccountEquipmentShipmentInfo();
		CustomerContactInfo recipientShippingInfo = new CustomerContactInfo();
		name = new NameInfo();
		name.setFirstName(shippingAddressData.getShippingAddress().getFirstName());
		name.setLastName(shippingAddressData.getShippingAddress().getLastName());
		recipientShippingInfo.setName(name);	
	
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setEmailAddress(accountHolderAddressData.getAccountAddress().getEmail());
		
		AddressInfo recipientShippingAddress = new AddressInfo();
		recipientShippingAddress.setAddressLine1(shippingAddressData.getShippingAddress().getAddress1());
		recipientShippingAddress.setCity(shippingAddressData.getShippingAddress().getCity());
		recipientShippingAddress.setState(AddressStateInfo.fromValue(shippingAddressData.getShippingAddress().getStateAddress()));
		AddressZipInfo zipInfo1 = new AddressZipInfo();
		zipInfo1.setZipCode(shippingAddressData.getShippingAddress().getPostalCode());
		recipientShippingAddress.setZip(zipInfo1);
		recipientShippingAddress.setCountry(getCountry(shippingAddressData.getShippingAddress().getCountry()));
		recipientShippingInfo.setAddress(recipientShippingAddress);
		recipientShippingInfo.setEmail(emailInfo);
		
		equipmentShipmentInfo.setRecipientShippingInfo(recipientShippingInfo);

		CreateShippingQuoteRequestInfoAccountEquipmentShipmentInfoShippingHeader shippingHeader = new CreateShippingQuoteRequestInfoAccountEquipmentShipmentInfoShippingHeader();
		// Response from inventory estamate delivery 
		shippingHeader.setShipVia(getSelectedShippingMethod(order)); 
		// get it from inquireDeliveryEstimate
		shippingHeader.setShipRequestDate(new Date());
	
		shippingHeader.setShipRequestFromState(AddressStateInfo.fromValue(shippingAddressData.getShippingAddress().getStateAddress()));
		equipmentShipmentInfo.setShippingHeader(shippingHeader);
			
		CreateShippingQuoteRequestInfoAccountEquipmentShipmentInfoShipmentOrderSummary shipmentOrderSummary = new CreateShippingQuoteRequestInfoAccountEquipmentShipmentInfoShipmentOrderSummary();
			
		// check with priya to get shipping tax and amount - as shipping is free, so sending zero amount
		
		shipmentOrderSummary.setOrderShipmentTaxAmount(new BigDecimal(0));
		shipmentOrderSummary.setOrderShipmentChargeAmount(new BigDecimal(0));
		shipmentOrderSummary.setOrderShipmentNetTotal(new BigDecimal(0));
		shipmentOrderSummary.setShippingOrderType(getShippingOrderType());
		equipmentShipmentInfo.setShipmentOrderSummary(shipmentOrderSummary);
		
		account.setEquipmentShipmentInfo(equipmentShipmentInfo);
	
		accountInfo[0]=account;
		createShippingRequest.setAccount(accountInfo);

		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}
			 			
	    		logDebug("[CricketESPAdapter->mapTOShippingQuoteRequest()]: Exiting mapTOShippingQuoteRequest()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return createShippingRequest;
	}
 	
	/**
	 * @param pWebReportRequest
	 * @return
	 */
	public UpdateWebReportRequestInfo mapToUpdateWebReportRequest(
			UpdateWebReportRequestVO pWebReportRequest,CricketOrderImpl pOrder) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->mapToUpdateWebReportRequest()]: Entering into mapToUpdateWebReportRequest()...");
		}
		
		UpdateWebReportRequestInfo requestInfo = new UpdateWebReportRequestInfo();
		
		requestInfo.setBillingQuoteNumber(pWebReportRequest.getBillingQuoteNumber());
		requestInfo.setQuoteCreatedDate(pWebReportRequest.getQuoteCreatedDate());
		requestInfo.setBillingOrderId(pWebReportRequest.getBillingOrderId());
		requestInfo.setOrderCreatedDate(pWebReportRequest.getOrderCreatedDate());
		requestInfo.setAccountNumber(pWebReportRequest.getAccountNumber());
		requestInfo.setTransactionType(pWebReportRequest.getTransactionType());
		requestInfo.setMarketId(pWebReportRequest.getMarketId());
		// defect 8710
		if(null!= pOrder && (null!=pOrder.getTeleSaleCode() && !StringUtils.isEmpty(pOrder.getTeleSaleCode()))){
			requestInfo.setSalesRepresentative(pOrder.getTeleSaleCode());
		}else{
			requestInfo.setSalesRepresentative(pWebReportRequest.getSalesRepresentative());
		}
		LanguagePreferenceInfo language = LanguagePreferenceInfo.fromValue(pWebReportRequest.getLanguage());
		requestInfo.setLanguage(language);		
		
		if(null!=pWebReportRequest.getCustomer()){			
			UpdateWebReportRequestInfoCustomer customerInfo = new UpdateWebReportRequestInfoCustomer();
				
			customerInfo.setCustomerId(pWebReportRequest.getCustomer().getCustomerId());
			//CustomerTypeInfo customerType = CustomerTypeInfo.fromValue(pWebReportRequest.getCustomer().getCustomerType());
			customerInfo.setCustomerType(CustomerTypeInfo.S);
			
			if(null!=pWebReportRequest.getCustomer().getName()){
			NameInfo nameInfo = new NameInfo();
			nameInfo.setFirstName(pWebReportRequest.getCustomer().getName().getFirstName());
			nameInfo.setLastName(pWebReportRequest.getCustomer().getName().getLastName());
			customerInfo.setName(nameInfo);		
			}
			
			if(null!=pWebReportRequest.getCustomer().getBillingAddress()){ 
			AddressInfo bussinessAddress = mapTOcustomerAddress(pWebReportRequest.getCustomer().getBillingAddress());
			customerInfo.setBillingAddress(bussinessAddress);
			}
			
			if(null!=pWebReportRequest.getCustomer().getShippingAddress()){
			AddressInfo shippingAddress = mapTOcustomerAddress(pWebReportRequest.getCustomer().getShippingAddress());
			customerInfo.setShippingAddress(shippingAddress);
			}
			
			if(null!=pWebReportRequest.getCustomer().getPhone()){
			PhoneInfo customerPhone = new PhoneInfo();
			customerPhone.setHomePhone(pWebReportRequest.getCustomer().getPhone().getHomePhone());		
			customerInfo.setPhone(customerPhone);
			}
			
			if(null!=pWebReportRequest.getCustomer().getEmail()){
			EmailInfo customerEmail = new EmailInfo();
			customerEmail.setEmailAddress(pWebReportRequest.getCustomer().getEmail().getEmailAddress());
			customerInfo.setEmail(customerEmail);
			}			
			requestInfo.setCustomer(customerInfo);
		}	
		
		UpdateWebReportRequestInfoItems[] requestInfoItems = mapTOWebReportItems(pWebReportRequest);
		requestInfo.setItems(requestInfoItems);
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->mapToUpdateWebReportRequest()]: Exiting mapToUpdateWebReportRequest()...");
		}
		return requestInfo;
	}
	
	/**
	 * @param pAddressVO
	 * @param bussinessAddress
	 * @return 
	 */
	private AddressInfo mapTOcustomerAddress(AddressVO pAddressVO) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->mapTOcustomerAddress()]: Entering into mapTOcustomerAddress()...");
		}
		
		AddressInfo bussinessAddress = new AddressInfo();
		AddressZipInfo zipCodeInfo = new AddressZipInfo();
		bussinessAddress.setAddressLine1(pAddressVO.getAddressLine1());
		bussinessAddress.setCity(pAddressVO.getCity());
		AddressStateInfo state = AddressStateInfo.fromValue(pAddressVO.getState());
		bussinessAddress.setState(state);
		
		zipCodeInfo.setZipCode(pAddressVO.getZipCode());
		zipCodeInfo.setZipCodeExtension(pAddressVO.getZipCodeExtension());
		bussinessAddress.setZip(zipCodeInfo);
		
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->mapTOcustomerAddress()]: Exiting mapTOcustomerAddress()...");
		}
		return bussinessAddress;		
	}	
	
	/**
	 * @param pWebReportRequest
	 * @return
	 */
	private UpdateWebReportRequestInfoItems[] mapTOWebReportItems(UpdateWebReportRequestVO pWebReportRequest) {
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->mapTOWebReportItems()]: Entering into mapTOWebReportItems()...");
		}
		
		UpdateWebReportRequestVOItems[] requestItemsVO = pWebReportRequest.getItems();
		UpdateWebReportRequestInfoItems[] itemsInfo = new UpdateWebReportRequestInfoItems[pWebReportRequest.getItems().length];
		
		if(requestItemsVO.length>0){
			int item=0;
			for(UpdateWebReportRequestVOItems voItems :requestItemsVO){
				UpdateWebReportRequestVOItemsItem[] itemsVO = voItems.getItem();
				UpdateWebReportRequestInfoItemsItem[] itemsItem = new UpdateWebReportRequestInfoItemsItem[itemsVO.length];
				if(itemsVO.length>0)
				{	
					int index=0;
					for(UpdateWebReportRequestVOItemsItem itemDetails: itemsVO){												
						//UpdateWebReportRequestInfoItemsItem itemsItem = new UpdateWebReportRequestInfoItemsItem();
						if(null!=itemDetails){
						itemsItem[index]=new UpdateWebReportRequestInfoItemsItem();
						itemsItem[index].setItemCode(itemDetails.getItemCode());
						itemsItem[index].setItemDescription(itemDetails.getItemDescription());
						itemsItem[index].setItemType(itemDetails.getItemType());
						itemsItem[index].setQuantity(itemDetails.getQuantity());
						itemsItem[index].setSku(itemDetails.getSku());
						if(null!=itemDetails.getUnitActualPrice())
							itemsItem[index].setUnitActualPrice(itemDetails.getUnitActualPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
						else
							itemsItem[index].setUnitActualPrice(BigDecimal.ZERO);
						if(null!=itemDetails.getUnitMonthlyPrice())
							itemsItem[index].setUnitMonthlyPrice(itemDetails.getUnitMonthlyPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
						else
							itemsItem[index].setUnitMonthlyPrice(BigDecimal.ZERO);
						if(null!=itemDetails.getUnitPrice())
							itemsItem[index].setUnitPrice(itemDetails.getUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP));		
						else
							itemsItem[index].setUnitPrice(BigDecimal.ZERO);	
						index++;
						}
					}
				}
				itemsInfo[item]=new UpdateWebReportRequestInfoItems();
				itemsInfo[item].setItem(itemsItem);
				itemsInfo[item].setMdn(voItems.getMdn());
				item++;
			}
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->mapTOWebReportItems()]: Exiting mapTOWebReportItems()...");
		}
		return itemsInfo;
	}	
	
	/**
	 * @param order
	 * @param saleLineItems
	 * @return
	 */
	public SalesLineItemInfo[] createSaleLineItemInfo(CricketOrderImpl order,
			List<CricketCommerceItemImpl> saleLineItems) {
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}						
	    		logDebug("[CricketESPAdapterHelper->createSaleLineItemInfo()]: Entering into createSaleLineItemInfo()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		
		SalesLineItemInfo[] salesLineItemInfos;
		String workOrderType = order.getWorkOrderType();
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->manageSaleItem()]: workOrderType: "+ workOrderType);
		}
		if(TRANSACTION_TYPE_RRC.equalsIgnoreCase(workOrderType)) {
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->manageSaleItem()]: Initializing SalesLineItemInfos with size: 1");
			}
			salesLineItemInfos = new SalesLineItemInfo[1];	
		}else if(TRANSACTION_TYPE_OXC.equalsIgnoreCase(workOrderType)) {
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->manageSaleItem()]: Initializing SalesLineItemInfos with size: "+(saleLineItems.size() + 2));
			}
			salesLineItemInfos = new SalesLineItemInfo[saleLineItems.size() + 2];
		} else if(TRANSACTION_TYPE_OUP.equalsIgnoreCase(workOrderType)) {
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->manageSaleItem()]: Initializing SalesLineItemInfos with size: "+(order.getCommerceItemCount() + 2));
			}
			salesLineItemInfos = new SalesLineItemInfo[order.getCommerceItemCount() + 2];
		} else {
			// add LOS or Multi LOS
			int accessoriesCount = getAccessoriesItemCount(order.getCommerceItems());
			//List<CricketPackage> packageItem = order.getCktPackages(); 
			int packageCount = getCricketPackageCount(order);
			if(isLoggingDebug()){
				logDebug("[CricketESPAdapter->manageSaleItem()]: Initializing SalesLineItemInfos with size: "+(packageCount + accessoriesCount + 2)+" PackageCount: "+ packageCount+" AccessoriesCount " + accessoriesCount);
			}
			salesLineItemInfos = new SalesLineItemInfo[packageCount + accessoriesCount + 2];
		}
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 		
	    		logDebug("[CricketESPAdapterHelper->createSaleLineItemInfo()]: EXITING FROM createSaleLineItemInfo()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return salesLineItemInfos;
	}
	
	/**
	 * Get the shipping address from shipping group.
	 * @param cricketOrder
	 * @return
	 */
	public Address getShippingAddressfromShippingGroup(CricketOrderImpl cricketOrder){
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 		
	    		logDebug("[CricketESPAdapterHelper->getShippingAddressfromShippingGroup()]: Entering into getShippingAddressfromShippingGroup()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		List<ShippingGroup> sgList =cricketOrder.getShippingGroups();
		HardgoodShippingGroup shippingType = null;
		for (ShippingGroup sg : sgList) {
			if (sg instanceof HardgoodShippingGroup) {
				shippingType = (HardgoodShippingGroup) sg;
				if(isLoggingDebug()){
					logDebug("[CricketESPAdapterHelper->getShippingAddressfromShippingGroup()]: shippingType: "+shippingType);
					logDebug("[CricketESPAdapterHelper->getShippingAddressfromShippingGroup()]: Exiting getShippingAddressfromShippingGroup()...");
				}
				
				return shippingType.getShippingAddress();
			} 
		}
		
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 			
	    		logDebug("[CricketESPAdapterHelper->getShippingAddressfromShippingGroup()]: Exiting getShippingAddressfromShippingGroup()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return null;
 	}	
	
	/**
	 * @param cricketOrder
	 * @return
	 */
	public Address getBillingAddressfromPaymentGroup(CricketOrderImpl cricketOrder){
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 		
	    		logDebug("Entering into the class CricketESPAdapterHelper->getBillingAddressfromPaymentGroup()]:..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		List<PaymentGroup> pgs = cricketOrder.getPaymentGroups();
		CricketCreditCard creditCard = null;
		for(PaymentGroup pg:pgs){
			 if(pg instanceof CricketCreditCard){
				 creditCard  =(CricketCreditCard)pg;
				 if(!creditCard.isDiffernetCard()){
					 return creditCard.getBillingAddress();
				 }  
			 }
		}
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 			
	    		logDebug("Exiting form the class CricketESPAdapterHelper->getBillingAddressfromPaymentGroup()]:..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return null;
	}
	/**
	 * @param order
	 * @return
	 */
	public CricketCreditCard getPaymentGroup(CricketOrderImpl  order){
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 			
	    		logDebug("[CricketESPAdapterHelper->getPaymentGroup()]: Entering into getPaymentGroup()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		List<PaymentGroup> pgs = order.getPaymentGroups();
		CricketCreditCard creditCard = null;
		for(PaymentGroup pg:pgs){
			 if(pg instanceof CricketCreditCard){
				 creditCard  =(CricketCreditCard)pg;
				 if(creditCard.isDiffernetCard()){
					 if(isLoggingDebug()){
						 logDebug("[CricketESPAdapterHelper->getPaymentGroup()]: creditCard: "+creditCard);
						 logDebug("[CricketESPAdapterHelper->getPaymentGroup()]: Exiting getPaymentGroup()...");					 
					}					 
					 return creditCard;
				 }  
			 }
		}
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 
	    		logDebug("[CricketESPAdapterHelper->getPaymentGroup()]: creditCard: "+creditCard);
	    		logDebug("[CricketESPAdapterHelper->getPaymentGroup()]: Exiting getPaymentGroup()..." + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + order.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return creditCard;
	}		
	
	/**
	 * @param cricketOrder
	 * @return
	 */
	public String getSelectedShippingMethod(CricketOrderImpl cricketOrder) {
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			 			
	    		logDebug("Entering into CricketESPAdapterHelper class of getSelectedShippingMethod() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		String shipVia = null;
		String shippingMethod = cricketOrder.getShippingMethod();
		if(shippingMethod != null && CricketCommonConstants.POBOX.equals(shippingMethod)){					
			shipVia = UPS_SUREPOST;
		} else {
			shipVia = UPS_ONE_DAY_PM_RES_SIG;
		}
		if (isLoggingDebug()) {
			// Getting the Page url
				String pageURL = CricketCommonConstants.EMPTY_STRING;
				if(!StringUtils.isBlank(ServletUtil.getCurrentRequest().getRequestURIWithQueryString())){
					pageURL = ServletUtil.getCurrentRequest().getRequestURIWithQueryString();
				}			
	    		logDebug("Exiting from CricketESPAdapterHelper class of getSelectedShippingMethod() method:::" + CricketCommonConstants.SESSION_ID + getSessionId() + CricketCommonConstants.ORDER_ID + cricketOrder.getId() + CricketCommonConstants.PAGE_URL + pageURL);
		}
		return shipVia;
	}
	
	/**
	 * @return
	 */
	public DealerCommissionInfo getDealerCommission() {
		DealerCommissionInfo dealerCommissionInfo = new DealerCommissionInfo();
		dealerCommissionInfo.setLocationId(getLocationId()); 
		dealerCommissionInfo.setSalesRepresentative(getSalesRepresentative()); 
		dealerCommissionInfo.setSalesChannel(getSalesChannel());
		return dealerCommissionInfo;
	}
	
	/**
	 * @param address
	 * @return
	 */
	public AddressInfo getAddressInfo(Address address) {
		if(address == null){
			return null;
		}
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setAddressLine1(address.getAddress1());
		addressInfo.setCity(address.getCity());
		addressInfo.setState(AddressStateInfo.fromValue(address.getState()));
		AddressZipInfo zip = new AddressZipInfo();
		zip.setZipCode(address.getPostalCode());
		// check with client
		//zip.setZipCodeExtension(address.getZipCodeExtension());
		addressInfo.setZip(zip);
		return addressInfo;
	}	
	
	/**
	 * @param addressInfo
	 * @return
	 */
	public AddressVO getAddressVO(AddressInfo addressInfo) {
		if(addressInfo == null){
			return null;
		}
		AddressVO addressVO = new AddressVO();
		addressVO.setAddressLine1(addressInfo.getAddressLine1());
		addressVO.setCity(addressInfo.getCity());
		addressVO.setCountry(getCountry(addressInfo.getCountry()));
		if(addressInfo.getState() != null){
			addressVO.setState(addressInfo.getState().toString());
		}
		if(addressInfo.getZip() != null){
			addressVO.setZipCode(addressInfo.getZip().getZipCode());
		}						
		
		return addressVO;
	}
	
	/**
	 * @param saleLineItems
	 * @return
	 */
	public int getAccessoriesItemCount(List<CricketCommerceItemImpl> saleLineItems){
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->getAccessoriesItemCount()]: Entering into getAccessoriesItemCount()...");
		}
		String productType = null;
		int accessoriesCount = 0;
		for(CricketCommerceItemImpl commerceItem : saleLineItems){
			productType = (String) commerceItem.getCricItemTypes();			
			if(ACCESSORY_PRODUCT.equalsIgnoreCase(productType)){
				accessoriesCount++;
			}
		}
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->getAccessoriesItemCount()]: accessoriesCount "+ accessoriesCount);	
			logDebug("[CricketESPAdapter->getAccessoriesItemCount()]: Exiting getAccessoriesItemCount()...");
		}
		
		return accessoriesCount;
	}
	
	/**
	 * @param optionalAddOns
	 * @return
	 */
	public InquireFeaturesResponseVO getAdministrationFeeAddOn(
			List<InquireFeaturesResponseVO> optionalAddOns) {
		InquireFeaturesResponseVO administrationFeeFeature = null;
		if(optionalAddOns != null && optionalAddOns.size() >0){		    			
			for(InquireFeaturesResponseVO optionalAddOn : optionalAddOns){
				if(optionalAddOn != null && CricketCommonConstants.ADMINISTRATION_FEE.equals(optionalAddOn.getGroupName())){
					administrationFeeFeature = optionalAddOn;
					break;
				}
			}
		}
		return administrationFeeFeature;
	}
	
	/**
	 * @param optionalAddOns
	 * @return
	 */
	public InquireFeaturesResponseVO getActivationFeeAddOn(
			List<InquireFeaturesResponseVO> optionalAddOns) {
		InquireFeaturesResponseVO activationFeeFeature = null;
		if(optionalAddOns != null && optionalAddOns.size() >0){		    			
			for(InquireFeaturesResponseVO optionalAddOn : optionalAddOns){
				if(optionalAddOn != null && CricketCommonConstants.ACTIVATION_FEE_FEATURE.equals(optionalAddOn.getGroupName())){
					activationFeeFeature = optionalAddOn;
					break;
				}
			}
		}
		return activationFeeFeature;
	}
	
	
	/**
	 * 
	 * @param userAccountInformation
	 * @param mdn
	 * @return
	 */
	public boolean isDataAutoMetered(UserAccountInformation userAccountInformation, String mdn) {
		boolean isDataAutoMetered = Boolean.valueOf(false);
		if(userAccountInformation != null && mdn != null){
			List<SubscriberVO> subscribers = userAccountInformation.getSubscribers();
			if(subscribers != null && subscribers.size() > 0){
				for(SubscriberVO subscriber : subscribers){
					if(subscriber.getMdn() != null && subscriber.getMdn().equals(mdn)){
						if(subscriber.isDataAutoMetered()){
							isDataAutoMetered = Boolean.valueOf(true);
						}
					}
				}
			}
		}
		return isDataAutoMetered;
	}
	
	
	/**
	 * @param currentDate
	 * @return
	 */
	public Date getPacificDateTime(Date currentDate){ 
		if(isLoggingDebug()){
			logDebug("[CricketESPAdapter->getPacificDateTime()]: Entering into getPacificDateTime()...");
		}
		
        Date usPacificDate = null;
        SimpleDateFormat dateFormatUSPacific = new SimpleDateFormat(DATE_FORMAT);
        dateFormatUSPacific.setTimeZone(TimeZone.getTimeZone(TIMEZONE_PST));

        //Local time zone   
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat(DATE_FORMAT);

        //Time in USMountain
        try {
        	usPacificDate = dateFormatLocal.parse( dateFormatUSPacific.format(currentDate));
        	if(isLoggingDebug()){
        		logDebug("[CricketESPAdapter->getPacificDateTime()]: US PacificDate" + usPacificDate);
        	}
        } catch (ParseException e) {
              vlogError("[CricketESPAdapter->getPacificDateTime()]:" + CricketESPConstants.WHOOP_KEYWORD +  " ParseException - cannot convert date to PST timezone, returning the same date as input"  + "ConversationId: " + getCricketESPAdapter().getConversationId(), e);
              return currentDate;                 
        }
        
        if(isLoggingDebug()){
        	logDebug("[CricketESPAdapter->getPacificDateTime()]: Exiting getPacificDateTime()...");
        }
        return usPacificDate;       
   
 	}
	
	/**
	 * @return the drawerId
	 */
	public String getDrawerId() {
		return drawerId;
	}

	/**
	 * @param drawerId the drawerId to set
	 */
	public void setDrawerId(String drawerId) {
		this.drawerId = drawerId;
	}

	/**
	 * @return the locationId
	 */
	public String getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the salesRepresentative
	 */
	public String getSalesRepresentative() {
		return salesRepresentative;
	}

	/**
	 * @param salesRepresentative the salesRepresentative to set
	 */
	public void setSalesRepresentative(String salesRepresentative) {
		this.salesRepresentative = salesRepresentative;
	}

	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}

	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	/**
	 * @return the shippingOrderType
	 */
	public String getShippingOrderType() {
		return shippingOrderType;
	}

	/**
	 * @param shippingOrderType the shippingOrderType to set
	 */
	public void setShippingOrderType(String shippingOrderType) {
		this.shippingOrderType = shippingOrderType;
	}

	/**
	 * @return the quoteStatus
	 */
	public String getQuoteStatus() {
		return quoteStatus;
	}

	/**
	 * @param quoteStatus the quoteStatus to set
	 */
	public void setQuoteStatus(String quoteStatus) {
		this.quoteStatus = quoteStatus;
	}	

	/**
	 * @return the vestaTokenStub
	 */
	public String getVestaTokenStub() {
		return vestaTokenStub;
	}

	/**
	 * @param vestaTokenStub the vestaTokenStub to set
	 */
	public void setVestaTokenStub(String vestaTokenStub) {
		this.vestaTokenStub = vestaTokenStub;
	}	

	/**
	 * @return the newUserBillingNumber
	 */
	public String getNewUserBillingNumber() {
		return newUserBillingNumber;
	}

	/**
	 * @param newUserBillingNumber the newUserBillingNumber to set
	 */
	public void setNewUserBillingNumber(String newUserBillingNumber) {
		this.newUserBillingNumber = newUserBillingNumber;
	}

	/**
	 * @return the newUserCustomerId
	 */
	public String getNewUserCustomerId() {
		return newUserCustomerId;
	}

	/**
	 * @param newUserCustomerId the newUserCustomerId to set
	 */
	public void setNewUserCustomerId(String newUserCustomerId) {
		this.newUserCustomerId = newUserCustomerId;
	}

	/**
	 * @return the contractTerm
	 */
	public int getContractTerm() {
		return contractTerm;
	}

	/**
	 * @param contractTerm the contractTerm to set
	 */
	public void setContractTerm(int contractTerm) {
		this.contractTerm = contractTerm;
	}

	/**
	 * @param country
	 * @return
	 */
	public String getCountry(String country){
		return 	country == null ? COUNTRY_USA_CODE_THREE_LETTER : (COUNTRY_USA_CODE_TWO_LETTER.equals(country) ? COUNTRY_USA_CODE_THREE_LETTER : country);
	}

	public CricketConfiguration getCktConfiguration() {
		return cktConfiguration;
	}

	public void setCktConfiguration(CricketConfiguration cktConfiguration) {
		this.cktConfiguration = cktConfiguration;
	}

	/**
	 * @return the devEnvironmentEnabled
	 */
	public boolean isDevEnvironmentEnabled() {
		return devEnvironmentEnabled;
	}

	/**
	 * @param devEnvironmentEnabled the devEnvironmentEnabled to set
	 */
	public void setDevEnvironmentEnabled(boolean devEnvironmentEnabled) {
		this.devEnvironmentEnabled = devEnvironmentEnabled;
	}
	
	/**
	 * 
	 * @param cktOrder
	 * @return
	 */
	public int getCricketPackageCount(CricketOrderImpl cktOrder){
		List<CricketPackage> cktPackages = cktOrder.getCktPackages();
		CricketCommerceItemImpl[] commerceItemList = null;
		int cricPackageCount = 0;
		if(cktPackages != null && cktPackages.size() > 0){
			for(CricketPackage cktPackage : cktPackages){
				commerceItemList = getCricketOrderTools().getCommerceItemsForPackage(cktPackage, cktOrder);
				if(commerceItemList != null && commerceItemList.length > 0 && getCricketOrderTools().isValidCricketPackage(commerceItemList)){
					cricPackageCount++;
				}
			}
		}
		
		return cricPackageCount;
	}
	/**
	 * @return the cricketOrderTools
	 */
	public CricketOrderTools getCricketOrderTools() {
		return cricketOrderTools;
	}

	/**
	 * @param cricketOrderTools the cricketOrderTools to set
	 */
	public void setCricketOrderTools(CricketOrderTools cricketOrderTools) {
		this.cricketOrderTools = cricketOrderTools;
	}
	
}
