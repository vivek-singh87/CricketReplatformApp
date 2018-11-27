<dsp:page>

	<dsp:getvalueof var="billingAddressType" bean="/atg/commerce/ShoppingCart.last.billingAddressType"/>

<div class="columns customer-info-item">
	<h3><crs:outMessage key="cricket_checkout_billing_address"/><!-- Billing Address --></h3>
   <dsp:tomap var="billingAddress" bean="/atg/commerce/ShoppingCart.last.paymentGroups[0].billingAddress"/> 
 	<c:choose>
    <c:when test="${billingAddressType eq 'AccountAddress'}">
   			 <dsp:valueof value="${billingAddress.firstName}"/>
			<dsp:valueof value="${billingAddress.lastName}"/>
      <p><crs:outMessage key="cricket_checkout_same_account_address"/><!-- Same as account address --></p>
	
    </c:when>
    <c:when test="${billingAddressType eq 'ShippingAddress'}">
    	    <dsp:valueof value="${billingAddress.firstName}"/>
			<dsp:valueof value="${billingAddress.lastName}"/>
      <p><crs:outMessage key="cricket_checkout_same_shipping_address"/><!-- Same as Shipping Address --></p>
      </c:when>
    <c:otherwise>
    		<p>
			<dsp:valueof value="${billingAddress.firstName}"/>
			<dsp:valueof value="${billingAddress.middleName}"/>
			<dsp:valueof value="${billingAddress.lastName}"/><br/>
			<dsp:valueof value="${billingAddress.address1}"/>
			<dsp:valueof value="${billingAddress.companyName}"/>
			<dsp:valueof value="${billingAddress.address2}"/><br/>
			<dsp:valueof value="${billingAddress.city}"/>
			<dsp:valueof value="${billingAddress.state}"/>
			<dsp:valueof value="${billingAddress.country}"/>
			<dsp:valueof value="${billingAddress.postalCode}"/><br/>
			<dsp:valueof value="${billingAddress.phoneNumber}"/>
			</p>		
    </c:otherwise>
</c:choose>
</div>
</dsp:page>