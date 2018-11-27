	<dsp:page>
	<div class="columns customer-info-item">
	<dsp:getvalueof var="shippingAddrType" bean="/atg/commerce/ShoppingCart.last.shippingAddressType"/>
	<h3>
		 <crs:outMessage key="cricket_checkout_shipping_address"/> <!-- Shipping Address -->
	</h3>
	
	<c:choose>
    <c:when test="${shippingAddrType eq 'AccountAddress'}">
      <p><crs:outMessage key="cricket_checkout_same_account_address"/><!-- Same as account address --></p>		
    </c:when>   
    <c:otherwise>
    	<dsp:tomap var="shippingAddress" bean="/atg/commerce/ShoppingCart.last.shippingGroups[0].shippingAddress"/>
    
    	<p>
			<dsp:valueof value="${shippingAddress.firstName}"/>
			<dsp:valueof value="${shippingAddress.middleName}"/>
			<dsp:valueof value="${shippingAddress.lastName}"/><br/>
			<dsp:valueof value="${shippingAddress.address1}"/>
			<dsp:valueof value="${shippingAddress.companyName}"/>
			<dsp:valueof value="${shippingAddress.address2}"/><br/>
			<dsp:valueof value="${shippingAddress.city}"/>
			<dsp:valueof value="${shippingAddress.state}"/>
			<dsp:valueof value="${shippingAddress.country}"/>
			<dsp:valueof value="${shippingAddress.postalCode}"/><br/>
			<dsp:valueof value="${shippingAddress.phoneNumber}"/>
		</p>				
    </c:otherwise>
</c:choose>

</div>
</dsp:page>