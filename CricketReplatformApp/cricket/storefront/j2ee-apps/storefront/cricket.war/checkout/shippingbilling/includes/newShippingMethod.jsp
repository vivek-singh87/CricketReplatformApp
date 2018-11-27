<dsp:page>

<div class="row special-pad">
<div class="large-12 small-12 columns">
	<div id="shipping-method-free">
		<legend class="shipping-method">
			<h2>
				<crs:outMessage key="cricket_checkout_shippingmethod" />
				<!-- Shipping Method -->
			</h2>
		</legend>
		<label for="method1" class="shipping-method-label method">
			 <input	id="method1" type="radio" name="shipping-method-type1"	checked="checked" value="Free overnight"> 
			 <crs:outMessage key="cricket_checkout_shippingovernight" /> <%-- Free overnight  --%>
		</label>
		<p class="delivery-info">
			<span class="italic"><crs:outMessage
					key="cricket_checkout_signature" /> <!-- Signature is required for delivery. --></span>
			<br>
			<crs:outMessage key="cricket_checkout_orderplacedafter3pm" />
			<!-- Orders placed after 3PM EST should ship the next business day. -->
		</p>
	</div>
	<div id="shipping-method-po-box">
		<legend class="shipping-method">
			<h2>
				<crs:outMessage key="cricket_checkout_shippingmethod" />
				<%-- Shipping Method --%>
			</h2>
		</legend>
		<label for="method2" class="shipping-method-label method">
		 <input id="method2" type="radio" name="shipping-method-type2"	checked="checked" value="PO Box (5-7 Day Delivery) - FREE"> 
		 <crs:outMessage key="cricket_checkout_poboxfree" /> <!-- PO Box (5-7 Day Delivery) - FREE  -->
		</label>
		<p class="delivery-info">
			<crs:outMessage key="cricket_checkout_poboxordermessage" />
			<!-- Orders shipping to a PO Box should be delivered within 5-7 business days. -->
		</p>
	</div>
</div>
</div>
	<div class="divider"></div>
</dsp:page>