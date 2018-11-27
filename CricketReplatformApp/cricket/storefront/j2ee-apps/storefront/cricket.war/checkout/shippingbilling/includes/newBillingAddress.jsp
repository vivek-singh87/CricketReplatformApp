<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/commerce/order/purchase/ShippingGroupFormHandler" />
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/com/cricket/commerce/order/CricketBillingAddressData"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:getvalueof var="billingAddressType" bean="ShoppingCart.current.billingAddressType"/>	
<dsp:getvalueof var="workOrderType" bean="ShoppingCart.current.workOrderType"/>
		          <div class="row nopad">
								<div class="large-12 small-12 columns">
									<h2><crs:outMessage key="cricket_checkout_billing_address"/><!-- Billing Address --></h2>
									<p><crs:outMessage key="cricket_checkout_billing_address_message"/><!-- Please provide your information as it appears on the credit/debit card you will be using for your purchase. --></p>
								</div>
							</div>
		          <div class="row">
		          	<div class="large-12 small-12 columns">
		          	<c:choose>
		          	<c:when test="${billingAddressType eq 'DifferentAddress'}">
			          	<label class="address-label">
		          			 <input id="billing-address1" type="radio" value="Same as account holder address" name="billing-address" > 
				          	<crs:outMessage key="cricket_checkout_same_account_address"/><!-- Same as account address --> 
		          		</label>
		          		<c:choose>
							<c:when test="${workOrderType ne null && workOrderType eq 'RRC' }" />
							<c:otherwise>
							<label class="address-label">
								 <input id="billing-address2" type="radio" value="Same as shipping address" name="billing-address"> 
          						<crs:outMessage key="cricket_checkout_same_shipping_address"/><!-- Same as shipping address -->
							</label>
							</c:otherwise>
						</c:choose>
		          			
		          		<label class="address-label" value="3">
							<input id="billing-address3" type="radio" name="billing-address" checked="checked" value="Different address"> 
			    	  		<crs:outMessage key="cricket_checkout_different_address"/> <!-- Different address -->
		          		</label>
		          	</c:when>
		          	<c:when test="${billingAddressType eq 'ShippingAddress'}">
			          	<label class="address-label">
		          			 <input id="billing-address1" type="radio" value="Same as account holder address" name="billing-address" > 
				          	<crs:outMessage key="cricket_checkout_same_account_address"/><!-- Same as account address --> 
		          		</label>
		          		<c:choose>
							<c:when test="${workOrderType ne null && workOrderType eq 'RRC' }" />
							<c:otherwise>
							<label class="address-label">
								 <input id="billing-address2" type="radio" value="Same as shipping address" name="billing-address" checked="checked"> 
          						<crs:outMessage key="cricket_checkout_same_shipping_address"/><!-- Same as shipping address -->
							</label>
							</c:otherwise>
						</c:choose>
		          			
		          		<label class="address-label" value="3">
							<input id="billing-address3" type="radio" name="billing-address" value="Different address"> 
					  		<crs:outMessage key="cricket_checkout_different_address"/> <!-- Different address -->
		          		</label>
		          	</c:when>
		          	<c:otherwise>
		          		<label class="address-label">
		          			 <input id="billing-address1" type="radio" value="Same as account holder address" checked="checked" name="billing-address" > 
				          	<crs:outMessage key="cricket_checkout_same_account_address"/><!-- Same as account address --> 
		          		</label>
		          		<c:choose>
							<c:when test="${workOrderType ne null && workOrderType eq 'RRC' }" />
							<c:otherwise>
							<label class="address-label">
								 <input id="billing-address2" type="radio" value="Same as shipping address" name="billing-address"> 
          						<crs:outMessage key="cricket_checkout_same_shipping_address"/><!-- Same as shipping address -->
							</label>
							</c:otherwise>
						</c:choose>
		          		<label class="address-label" value="3">
							<input id="billing-address3" type="radio" name="billing-address" value="Different address"> 
				      		<crs:outMessage key="cricket_checkout_different_address"/> <!-- Different address -->
		          		</label>
		          	</c:otherwise>
		          	</c:choose>
		          		
		          	</div>
		          </div>
		          
		          <%-- If they select a different address for billing, show the fields for them to submit--%>
          		<div id="different-billing-address">
          		  <div class="divider"></div>
          		  <div class="row">
                  <div class="large-6 small-12 columns left">
                    <label for="first-name-billing-address"><crs:outMessage key="cricket_checkout_first_name"/><!--  First Name --></label>
                    <dsp:input id="first-name-billing-address" name="first-name-billing-address" tabIndex="20" type="text" maxlength="19" bean="ShippingGroupFormHandler.billingAddressData.billingAddress.firstName" />
                    <%-- <input id="first-name-billing-address" name="first-name-billing-address" type="text" required> --%>
                  </div>
                  <div class="large-6 small-12 columns right">
                    <label for="last-name-billing-address"><crs:outMessage key="cricket_checkout_last_name"/><!-- Last Name --></label>
                    <dsp:input id="last-name-billing-address" name="last-name-billing-address" tabIndex="21" type="text" maxlength="20" bean="ShippingGroupFormHandler.billingAddressData.billingAddress.lastName"  />
                    <%-- <input id="last-name-billing-address" name="last-name-billing-address" type="text" required> --%>
                  </div>
                </div>
                <div class="row">
                <div class="large-12 small-12 columns single">
                    <label for="company-name-billing-address"><crs:outMessage key="cricket_checkout_company_name"/><crs:outMessage key="cricket_checkout_optional"/><!-- Company Name (optional) --></label>
                    <dsp:input id="company-name-billing-address" name="company-name-billing-address" tabIndex="22" type="text" maxlength="30" bean="ShippingGroupFormHandler.billingAddressData.billingAddress.companyName"/>
                    <%-- <input id="company-name-billing-address" name="company-name-billing-address" type="text"> --%>
                  </div>
                </div>             
                <div class="row">
                  <div class="large-8 small-12 columns left">
                    <label for="address-billing-address"> <crs:outMessage key="cricket_checkout_address_pobox"/><!-- Address / PO Box --></label>
                    <dsp:input id="address-billing-address" name="address-billing-address" tabIndex="23" type="text" maxlength="30" bean="ShippingGroupFormHandler.billingAddressData.billingAddress.address1"  />
                    <%-- <input id="address-billing-address" name="address-billing-address" type="text" required> --%>
                  </div>
                  <div class="large-4 small-12 columns right">
                    <label for="apt-suite-billing-address"><crs:outMessage key="cricket_checkout_apt_suite"/><crs:outMessage key="cricket_checkout_optional"/><!--  Apt. / Suite(optional) --></label>
                    <dsp:input id="apt-suite-billing-address" name="apt-suite-billing-address" tabIndex="24" type="text" maxlength="30" bean="ShippingGroupFormHandler.billingAddressData.billingAddress.address2"/>
                    <%-- <input id="apt-suite-billing-address" name="apt-suite-billing-address" type="text"> --%>
                  </div>
                </div>

                <div class="row">
                  <div class="large-6 small-12 columns left"> 
                    <label for="city-billing-address"> <crs:outMessage key="cricket_checkout_city"/> <!-- City --> </label>
                    <dsp:input id="city-billing-address" name="city-billing-address" tabIndex="25" type="text" maxlength="30" bean="ShippingGroupFormHandler.billingAddressData.billingAddress.city" beanvalue="CricketBillingAddressData.billingAddress.city" />
                    <%-- <input id="city-billing-address" name="city-billing-address" type="text" tabIndex="-1" value="Atlanta" required> --%>
                  </div>
                  <div class="large-4 small-12 columns middle">
                    <label for="state-billing-address"><crs:outMessage key="cricket_checkout_state"/><!-- State --></label>
                    <dsp:select data-customforms="disabled" id="state-billing-address" name="state-billing-address" tabIndex="26" bean="ShippingGroupFormHandler.billingAddressData.billingAddress.stateAddress"  >
                          <dsp:droplet name="/atg/dynamo/droplet/ForEach">
						  <dsp:param name="array" bean="/com/cricket/commerce/order/util/StateList.places"/>
						  <dsp:param name="elementName" value="state"/>
						   <dsp:oparam name="output">
						 <c:set var="stateCode">
						   <dsp:valueof param="state.code"/>
						   </c:set>			   
						   <c:set var="userLocationStateCode">
								<dsp:valueof bean="CricketBillingAddressData.billingAddress.stateAddress"/>
						   </c:set>	
						   	<c:choose>
								<c:when test="${userLocationStateCode eq stateCode }">
									<dsp:option value="${stateCode}" selected="true">
										<dsp:valueof param="state.displayName"/>
									</dsp:option>					
								</c:when>
								<c:otherwise>
									<dsp:option value="${stateCode}">
										<dsp:valueof param="state.displayName"/>
									</dsp:option>								
								</c:otherwise>
						   </c:choose>	
						  </dsp:oparam>
						</dsp:droplet>
                    </dsp:select>
                  </div>
                  <div class="large-2 small-12 columns right">
                    <label for="zipcode-billing-address"><crs:outMessage key="cricket_checkout_zipcode"/> <!-- Zip Code --> </label>
                    <dsp:input id="zipcode-billing-address" name="zipcode-billing-address" type="text" tabIndex="27" maxlength="10" bean="ShippingGroupFormHandler.billingAddressData.billingAddress.postalCode" beanvalue="CricketBillingAddressData.billingAddress.postalCode"  />
                    <%-- <input id="zipcode-billing-address" name="zipcode-billing-address" type="text" tabIndex="-1" value="30301" required> --%>
                  </div>
                </div>
          		</div>
</dsp:page>