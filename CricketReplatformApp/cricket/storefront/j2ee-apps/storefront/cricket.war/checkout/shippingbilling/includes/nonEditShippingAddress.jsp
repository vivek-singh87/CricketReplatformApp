	<dsp:page>
	<dsp:importbean bean="/com/cricket/commerce/order/CricketShippingAddressData" />
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<div class="columns customer-info-item">
	<dsp:getvalueof var="accountHolderAddress" bean="CricketShippingAddressData.shippingAddressAsAccountHolderAddress"/>
	<h3>
		<crs:outMessage key="cricket_checkout_shipping_address"/><!-- Shipping Address -->
	</h3>
	
	<c:choose>
    <c:when test="${accountHolderAddress eq true}">
      <p><crs:outMessage key="cricket_checkout_same_account_address"/><!-- Same as account address --></p>
			<a class="edit"  href="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp">
				 <crs:outMessage key="cricket_checkout_edit"/><!-- Edit -->
			</a>
    </c:when>   
    <c:otherwise>
    	<p>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.firstName"/>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.middleName"/>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.lastName"/><br/>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.address1"/>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.companyName"/>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.address2"/><br/>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.city"/>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.stateAddress"/>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.country"/>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.postalCode"/><br/>
			<dsp:valueof bean="CricketShippingAddressData.shippingAddress.phoneNumber"/>
		</p>		
			<a class="edit"  href="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp?pageSection=DifferentShippingAddress">
				<crs:outMessage key="cricket_checkout_edit"/> <!-- Edit -->
			</a>
    </c:otherwise>
</c:choose>

</div>
</dsp:page>