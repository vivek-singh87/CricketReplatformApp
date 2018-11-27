<dsp:page>
<dsp:importbean bean="/com/cricket/commerce/order/CricketShippingAddressData"/>
<dsp:importbean bean="/com/cricket/commerce/order/CricketBillingAddressData"/>
<dsp:importbean bean="/com/cricket/commerce/order/util/CricketPaymentData"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>

<div class="columns customer-info-item">
	<h3><crs:outMessage key="cricket_checkout_billing_address"/><!-- Billing Address --></h3>
		<dsp:getvalueof var="accountHolderAddress" bean="CricketShippingAddressData.billingAddressAsAccountHolderAddress"/>
		<dsp:getvalueof var="shippingAddress" bean="CricketShippingAddressData.billingAddressAsShippingAddress"/>
 		
 	<c:choose>
    <c:when test="${accountHolderAddress eq true}">
    <p> 
   <dsp:valueof bean="CricketPaymentData.fristName"/>
			<dsp:valueof bean="CricketPaymentData.lastName"/><br>
     <crs:outMessage key="cricket_checkout_same_account_address"/><!-- Same as account address --></p>
			<a class="edit"  href="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp">
				<crs:outMessage key="cricket_checkout_edit"/><!-- Edit -->
			</a>
    </c:when>
    <c:when test="${shippingAddress eq true}">
     <p>
    		<dsp:valueof bean="CricketPaymentData.fristName"/>
			<dsp:valueof bean="CricketPaymentData.lastName"/><br>
      <crs:outMessage key="cricket_checkout_same_shipping_address"/><!-- Same as Shipping Address --></p>
			<a class="edit"  href="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp">
				<crs:outMessage key="cricket_checkout_edit"/><!-- Edit -->
			</a>
      </c:when>
    <c:otherwise>
    	<p>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.firstName"/>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.middleName"/>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.lastName"/><br/>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.address1"/>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.companyName"/>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.address2"/><br/>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.city"/>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.stateAddress"/>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.country"/>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.postalCode"/><br/>
			<dsp:valueof bean="CricketBillingAddressData.billingAddress.phoneNumber"/>			
			<a class="edit"  href="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp?pageSection=DifferentBillingAddress">
				<crs:outMessage key="cricket_checkout_edit"/><!-- Edit -->
			</a>
		</p>
    </c:otherwise>
</c:choose>
</div>
</dsp:page>