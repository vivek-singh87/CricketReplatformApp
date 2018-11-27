<dsp:page>
<dsp:importbean bean="/atg/commerce/order/purchase/ShippingGroupFormHandler" />	
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/com/cricket/commerce/order/CricketShippingAddressData"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:getvalueof var="shippingAddressType" bean="ShoppingCart.current.shippingAddressType"/>	
<div class="row special-pad">
		          	<div class="large-12 small-12 columns">
		          	<legend class="shipping-address"><h2><crs:outMessage key="cricket_checkout_shipping_address"/><!-- Shipping Address --></h2></legend>
		          	<c:choose>
		          	<c:when test="${shippingAddressType eq 'DifferentAddress'}">
		          		<label class="address-label">
		          			 <input id="address1" type="radio" name="shipping-address"  value="Same as account holder address"> 
		          		<crs:outMessage key="cricket_checkout_same_account_address"/>	<!-- Same as account address -->
		          		</label>
		          		<label class="address-label">
		          			<input id="address2" type="radio" name="shipping-address" value="Different address" checked="checked"> 
				          	 <crs:outMessage key="cricket_checkout_different_address"/> 	<!-- Different address -->
		          		</label>
		          	</c:when>
		          	<c:otherwise>
		          		<label class="address-label">
		          			 <input id="address1" type="radio" name="shipping-address" checked="checked" value="Same as account holder address"> 
		          			<crs:outMessage key="cricket_checkout_same_account_address"/>	<!-- Same as account address -->
		          		</label>
		          		<label class="address-label">
		          			<input id="address2" type="radio" name="shipping-address" value="Different address"> 
				          	 <crs:outMessage key="cricket_checkout_different_address"/> 	<!-- Different address -->
		          		</label>
		          	</c:otherwise>
		          	</c:choose>
		          		<%-- If they select a different address, show the fields for them to submit--%>
		          		<div id="different-shipping-address">
		          		  <div class="divider"></div>
		          		    <div class="row">
                        <div class="large-6 small-12 columns left">
                          <label for="first-name-shipping-address"><crs:outMessage key="cricket_checkout_first_name"/><!-- First Name --></label>
                          <dsp:input id="first-name-shipping-address" name="first-name-shipping-address" tabIndex="12" type="text" maxlength="19" bean="ShippingGroupFormHandler.shippingAddressData.shippingAddress.firstName" />
                          <%-- <input id="first-name-shipping-address" name="first-name-shipping-address" type="text" required> --%>
                        </div>
                        <div class="large-6 small-12 columns right">
                          <label for="last-name"> <crs:outMessage key="cricket_checkout_last_name"/> <!-- Last Name --></label>
                          <dsp:input id="last-name-shipping-address" name="last-name-shipping-address" tabIndex="13" type="text" maxlength="20" bean="ShippingGroupFormHandler.shippingAddressData.shippingAddress.lastName" />
                          <%--<input id="last-name-shipping-address" name="last-name-shipping-address" type="text" required> --%>
                        </div>
                      </div>
                      <div class="row">
                      <div class="large-12 small-12 columns single">
                          <label for="company-name-shipping-address"><crs:outMessage key="cricket_checkout_company_name"/><!-- Company Name --> <crs:outMessage key="cricket_checkout_optional"/> <!-- (optional) --></label>
                          <dsp:input id="company-name-shipping-address" name="company-name-shipping-address" tabIndex="14" type="text" maxlength="30" bean="ShippingGroupFormHandler.shippingAddressData.shippingAddress.companyName" />
                          <%-- <input id="company-name-shipping-address" name="company-name-shipping-address" type="text"> --%>
                        </div>
                      </div>             
                      <div class="row">
                        <div class="large-8 small-12 columns left">
                          <label for="addres-shipping-addresss"><crs:outMessage key="cricket_checkout_address_pobox"/> <!-- Address / PO Box --></label>
                          <dsp:input id="address-shipping-address" name="address-shipping-address" tabIndex="15" type="text" maxlength="30" bean="ShippingGroupFormHandler.shippingAddressData.shippingAddress.address1" onChange="shippingMethodMsg()" />
                          <%-- <input id="address-shipping-address" name="address-shipping-address" type="text" required> --%>
                        </div>
                        <div class="large-4 small-12 columns right">
                          <label for="apt-suite-shipping-address"> <crs:outMessage key="cricket_checkout_apt_suite"/> <crs:outMessage key="cricket_checkout_optional"/> <!-- Apt. / Suite (optional) --></label>
                          <dsp:input id="apt-suite-shipping-address" name="apt-suite-shipping-address" tabIndex="16" type="text" maxlength="30" bean="ShippingGroupFormHandler.shippingAddressData.shippingAddress.address2"/>
                          <%-- <input id="apt-suite-shipping-address" name="apt-suite-shipping-address" type="text"> --%>
                        </div>
                      </div>

                      <div class="row">
                        <div class="large-6 small-12 columns left"> 
                          <label for="city-shipping-address">  <crs:outMessage key="cricket_checkout_city"/><!-- City --> </label>
                          <dsp:input id="city-shipping-address" name="city-shipping-address" type="text" tabIndex="17" maxlength="30" bean="ShippingGroupFormHandler.shippingAddressData.shippingAddress.city" beanvalue="CricketShippingAddressData.shippingAddress.city" />
                          <%-- <input id="city-shipping-address" name="city-shipping-address" type="text" tabIndex="-1" value="Atlanta" required> --%>
                        </div>
                        <div class="large-4 small-12 columns middle">
                          <label for="state-shipping-address"><crs:outMessage key="cricket_checkout_state"/><!-- State --></label>
						<dsp:select data-customforms="disabled" id="state-shipping-address" name="state-shipping-address" tabIndex="18" bean="ShippingGroupFormHandler.shippingAddressData.shippingAddress.stateAddress" >
                          <dsp:droplet name="/atg/dynamo/droplet/ForEach">
						  <dsp:param name="array" bean="/com/cricket/commerce/order/util/StateList.places"/>
						  <dsp:param name="elementName" value="state"/>
						   <dsp:oparam name="output">
						   <c:set var="stateCode">
						   <dsp:valueof param="state.code"/>
						   </c:set>			   
						   <c:set var="userLocationStateCode">
								<dsp:valueof bean="CricketShippingAddressData.shippingAddress.stateAddress"/>
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
                          <label for="zipcode-shipping-address"><crs:outMessage key="cricket_checkout_zipcode"/> <!-- Zip Code --> </label>
                          <dsp:input id="zipcode-shipping-address" name="zipcode-shipping-address" type="text" tabIndex="19" maxlength="10" bean="ShippingGroupFormHandler.shippingAddressData.shippingAddress.postalCode" beanvalue="CricketShippingAddressData.shippingAddress.postalCode" />
                          <%-- <input id="zipcode-shipping-address" name="zipcode-shipping-address" type="text" tabIndex="-1" value="30301" required> --%>
                        </div>
                      </div>
		          		
                </div>

		          </div>
				  </div>
</dsp:page>