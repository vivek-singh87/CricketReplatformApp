<dsp:page>
	<dsp:importbean bean="/com/cricket/commerce/order/CricketAccountHolderAddressData"/>
					<div class="row">
								<div class="large-12 small-12 columns">
									<h2><crs:outMessage key="cricket_checkout_accountaddress"/><!-- Account Address --></h2>
									<p><crs:outMessage key="cricket_checkout_address_onfile"/><!-- Address on file: --></p>
									<div class="error-container"><crs:outMessage key="cricket_checkout_correct_error_below"/><!-- Please correct the error(s) indicated below --></div>
								</div>
							</div>
							<div class="row">
										  <!-- Current Customer Info, no need to show firstname/last name general form fields -->
							<div class="columns customer-info-item">
								<p>
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.firstName"/>
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.middleName"/>
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.lastName"/><br/>
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.address1"/>
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.companyName"/>
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.address2"/><br/>
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.city"/>			
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.stateAddress"/>		
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.country"/>		
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.postalCode"/><br/>
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.email"/><br/>
									<dsp:valueof bean="CricketAccountHolderAddressData.accountAddress.phoneNumber"/>
								</p>
							</div>
			        </div>
					<div class="divider"></div>
</dsp:page>