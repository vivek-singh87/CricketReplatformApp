<dsp:page> 
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<%-- <dsp:valueof var="shippingMethod" bean="ShoppingCart.last.shippingGroups[0].shippingMethod"/>--%>
		         
                  <div id="shipping-method-free">
  		          		<legend class="shipping-method">
							<h3><crs:outMessage key="cricket_shoppingcart_shippingmethod"/><!-- Shipping Method --></h3>
						</legend>
  		          		<label for="method1" class="shipping-method-label method">
  		          			<input id="method1" type="radio" name="shipping-method-type1" checked="checked">
  		          			<crs:outMessage key="cricket_shoppingcart_free_overnight"/><!-- Free overnight --> 
  		          		</label>
  		          		<p class="delivery-info">
							<span class="italic">
								
								<crs:outMessage key="cricket_shoppingcart_signature_required"/><!-- Signature is required for delivery. -->
							</span>
							<br>
							<%-- <crs:outMessage key="cricket_checkout_orderplacedafter3pm"/> --%>
						<crs:outMessage key="cricket_shoppingcart_order_message"/>	<!-- Orders placed after 3PM EST should ship the next business day. -->
						</p>		          		
		         </div> 
 </dsp:page>