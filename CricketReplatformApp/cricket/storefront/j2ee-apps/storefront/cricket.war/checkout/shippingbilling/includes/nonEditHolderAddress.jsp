<dsp:page>
<dsp:importbean bean="/com/cricket/commerce/order/CricketAccountHolderAddressData"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>

	<div class="columns customer-info-item">
		<h3><crs:outMessage key="cricket_checkout_accountaddress"/><!-- Account Address --></h3>
		<p>
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.firstName" />
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.middleName" />
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.lastName" /><br/>
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.address1" />
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.companyName" />
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.address2" /><br/>
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.city" />
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.stateAddress" />
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.country" />
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.postalCode" /><br/>
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.email" /><br/>
			<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.phoneNumber" />

		</p>
		<a class="edit" href="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp">
		<crs:outMessage key="cricket_checkout_edit"/> <!-- Edit -->
		</a>
	</div>
								
</dsp:page>