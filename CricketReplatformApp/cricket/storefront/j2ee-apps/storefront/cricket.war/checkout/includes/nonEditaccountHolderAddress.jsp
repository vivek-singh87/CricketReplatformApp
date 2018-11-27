<dsp:page>
	<dsp:importbean bean="/com/cricket/commerce/order/CricketAccountHolderAddressData"/>			
 <div class="columns customer-info-item">
                    <h3><crs:outMessage key="cricket_checkout_account_address"/> <!-- Account Address --></h3>
                    <p>
                    
            <dsp:tomap var="accountAddress" bean="/atg/userprofiling/Profile.homeAddress"/>
            <dsp:getvalueof var="email" bean="/atg/userprofiling/Profile.email"/>
 			<dsp:valueof value="${accountAddress.firstName}"/>
			<dsp:valueof value="${accountAddress.middleName}"/>
			<dsp:valueof value="${accountAddress.lastName}"/><br/>
			<dsp:valueof value="${accountAddress.address1}"/>
			<dsp:valueof value="${accountAddress.companyName}"/>
			<dsp:valueof value="${accountAddress.address2}"/><br/>
			<dsp:valueof value="${accountAddress.city}"/>			
			<dsp:valueof value="${accountAddress.state}"/>		
			<dsp:valueof value="${accountAddress.country}"/>		
			<dsp:valueof value="${accountAddress.postalCode}"/><br/>
			<dsp:valueof value="${email}"/><br/>
			<dsp:valueof value="${accountAddress.phoneNumber}"/>

		</p>
                  </div>
</dsp:page>