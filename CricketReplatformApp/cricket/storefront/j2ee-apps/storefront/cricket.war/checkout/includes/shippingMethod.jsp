<dsp:page> 
<dsp:importbean bean="/com/cricket/commerce/order/CricketShippingAddressData"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>

<dsp:getvalueof var="shippingMethod" bean="CricketShippingAddressData.shippingMethodValue"/>
<c:choose>
<c:when test="${shippingMethod eq 'POBOX' }">
<div id="shipping-method-po-box1" >
		<legend class="shipping-method">
			<h3>
				<crs:outMessage key="cricket_checkout_shippingmethod" />
				<!-- Shipping Method -->
			</h3>
		</legend>
		<label for="method2" class="shipping-method-label method"> 
			<input id="method2" type="radio" name="shipping-method-type2" checked="checked">			
			 <p class="label-text"><crs:outMessage key="cricket_checkout_poboxfree"/></p> <!-- PO Box (5-7 Day Delivery) - FREE  -->
		</label>
		<p class="delivery-info">
			<crs:outMessage key="cricket_checkout_poboxordermessage" />
			<!-- Orders shipping to a PO Box should be delivered within 5-7 business days. -->
		</p>		
</div>
</c:when>
<c:otherwise>
<div id="shipping-method-free1">
		<legend class="shipping-method">
			<h3>
				<crs:outMessage key="cricket_checkout_shippingmethod" />
				<!-- Shipping Method -->
			</h3>
		</legend>
		<label for="method1" class="shipping-method-label method"> <input
			id="method1" type="radio" name="shipping-method-type1"
			checked="checked"> <crs:outMessage
				key="cricket_checkout_shippingovernight" />
			<!-- Free overnight  -->
		</label>
		<p class="delivery-info">
			<span class="italic"><crs:outMessage
					key="cricket_checkout_signature" />
				<!--  Signature is required for delivery. --></span> <br>
			<crs:outMessage key="cricket_checkout_orderplacedafter3pm" />
			<!-- Orders placed after 3PM EST should ship the next business day. -->
		</p>		
	</div>	
</c:otherwise>
</c:choose>
</dsp:page>