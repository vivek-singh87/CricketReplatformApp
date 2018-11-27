<dsp:page>

     
		          <div class="row nopad">
								<div class="large-12 small-12 columns">
									<h2><crs:outMessage key="cricket_checkout_billing_address"/><!-- Billing Address --></h2>
									<p><crs:outMessage key="cricket_checkout_billing_address_message"/><!-- Please provide your information as it appears on the credit/debit card you will be using for your purchase. --></p>
								</div>
							</div>
		          <div class="row">
		          	<div class="large-12 small-12 columns">
		          		<label class="address-label">
		          			<input id="billing-address1" type="radio" value="1" name="billing-address" checked="checked">
		          			<crs:outMessage key="cricket_checkout_same_account_address"/><!-- Same as account address  -->
		          		</label>
		          		<label class="address-label">
		          			<input id="billing-address2" type="radio" value="2" name="billing-address">
		          			<crs:outMessage key="cricket_checkout_same_shipping_address"/><!-- Same as shipping address -->
		          		</label>
		          		<label class="address-label" value="3">
		          			<input id="billing-address3" type="radio" name="billing-address">
		          		<crs:outMessage key="cricket_checkout_different_address"/>	<!-- Different address -->
		          		</label>
		          	</div>
		          </div>
		          
		          <!-- If they select a different address for billing, show the fields for them to submit-->
          		<div id="different-billing-address">
          		  <div class="divider"></div>
          		  <div class="row">
                  <div class="large-6 small-12 columns left">
                    <label for="first-name-billing-address"><crs:outMessage key="cricket_checkout_first_name"/><!-- First Name --></label>
                    <input id="first-name-billing-address" name="first-name-billing-address" type="text" required>
                  </div>
                  <div class="large-6 small-12 columns right">
                    <label for="last-name-billing-address"><crs:outMessage key="cricket_checkout_last_name"/><!-- Last Name --></label>
                    <input id="last-name-billing-address" name="last-name-billing-address" type="text" required>
                  </div>
                </div>
                <div class="row">
                <div class="large-12 small-12 columns single">
                    <label for="company-name-billing-address"><crs:outMessage key="cricket_checkout_company_name"/><crs:outMessage key="cricket_checkout_optional"/><!-- Company Name (optional) --></label>
                    <input id="company-name-billing-address" name="company-name-billing-address" type="text">
                  </div>
                </div>             
                <div class="row">
                  <div class="large-8 small-12 columns left">
                    <label for="address-billing-address"><crs:outMessage key="cricket_checkout_address_pobox"/> <!-- Address / PO Box --></label>
                    <input id="address-billing-address" name="address-billing-address" type="text" required>
                  </div>
                  <div class="large-4 small-12 columns right">
                    <label for="apt-suite-billing-address"><crs:outMessage key="cricket_checkout_apt_suite"/><crs:outMessage key="cricket_checkout_optional"/><!--  Apt. / Suite (optional) --></label>
                    <input id="apt-suite-billing-address" name="apt-suite-billing-address" type="text">
                  </div>
                </div>

                <div class="row">
                  <div class="large-6 small-12 columns left"> 
                    <label for="city-billing-address"><crs:outMessage key="cricket_checkout_city"/> <!-- City --> </label>
                    <input id="city-billing-address" name="city-billing-address" type="text" tabIndex="-1" value="Atlanta" required>
                  </div>
                  <div class="large-4 small-12 columns middle">
                    <label for="state-billing-address"><crs:outMessage key="cricket_checkout_state"/><!-- State --></label>
                    <select id="state-billing-address" name="state-billing-address" tabIndex="-1" required>
                        <option value="">State</option>
                        <option value="AL">Alabama</option>
                        <option value="AK">Alaska</option>
                        <option value="AZ">Arizona</option>
                        <option value="AR">Arkansas</option>
                        <option value="CA">California</option>
                        <option value="CO">Colorado</option>
                        <option value="CT">Connecticut</option>
                        <option value="DE">Delaware</option>
                        <option value="DC">District Of Columbia</option>
                        <option value="FL">Florida</option>
                        <option value="GA" selected="selected">Georgia</option>
                        <option value="HI">Hawaii</option>
                        <option value="ID">Idaho</option>
                        <option value="IL">Illinois</option>
                        <option value="IN">Indiana</option>
                        <option value="IA">Iowa</option>
                        <option value="KS">Kansas</option>
                        <option value="KY">Kentucky</option>
                        <option value="LA">Louisiana</option>
                        <option value="ME">Maine</option>
                        <option value="MD">Maryland</option>
                        <option value="MA">Massachusetts</option>
                        <option value="MI">Michigan</option>
                        <option value="MN">Minnesota</option>
                        <option value="MS">Mississippi</option>
                        <option value="MO">Missouri</option>
                        <option value="MT">Montana</option>
                        <option value="NE">Nebraska</option>
                        <option value="NV">Nevada</option>
                        <option value="NH">New Hampshire</option>
                        <option value="NJ">New Jersey</option>
                        <option value="NM">New Mexico</option>
                        <option value="NY">New York</option>
                        <option value="NC">North Carolina</option>
                        <option value="ND">North Dakota</option>
                        <option value="OH">Ohio</option>
                        <option value="OK">Oklahoma</option>
                        <option value="OR">Oregon</option>
                        <option value="PA">Pennsylvania</option>
                        <option value="RI">Rhode Island</option>
                        <option value="SC">South Carolina</option>
                        <option value="SD">South Dakota</option>
                        <option value="TN">Tennessee</option>
                        <option value="TX">Texas</option>
                        <option value="UT">Utah</option>
                        <option value="VT">Vermont</option>
                        <option value="VA">Virginia</option>
                        <option value="WA">Washington</option>
                        <option value="WV">West Virginia</option>
                        <option value="WI">Wisconsin</option>
                        <option value="WY">Wyoming</option>
                    </select> 
                  </div>
                  <div class="large-2 small-12 columns right">
                    <label for="zipcode-billing-address"><crs:outMessage key="cricket_checkout_zipcode"/><!--  Zipcode  --></label>
                    <input id="zipcode-billing-address" name="zipcode-billing-address" type="text" tabIndex="-1" value="30301" required>
                  </div>
                </div>
          		</div>
</dsp:page>