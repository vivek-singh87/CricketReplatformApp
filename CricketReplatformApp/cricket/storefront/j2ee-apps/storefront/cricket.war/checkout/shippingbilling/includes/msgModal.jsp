<dsp:page>
<dsp:importbean bean="/atg/commerce/order/purchase/ShippingGroupFormHandler" />
<dsp:importbean bean="/com/cricket/commerce/order/CricketShippingAddressData" />
<script>
$(function() {
$("#businessException").click(function() {
var url = '../../?openCart=true';
$(location).attr('href', url);
$("#cart-img").click();
})
});


$(function() {
$("#systemException").click(function() {
var url = 'https://account.mycricket.com/cricketlocations/';
$(location).attr('href', url);
})
});

$(function() {
$("#timeOutException").click(function() {
var url = '../../?openCart=true';
$(location).attr('href', url);
})
});
function submitFormRemovePOBoxInput(){
$('#address').val('');
$("#shipping-method-free").show();
$("#shipping-method-po-box").hide();
$('#notification-style-shippingMethod').foundation('reveal', 'close');return false;
}
$(function() {
$("#invalidAddress").click(function() {$('#notification-style-4').foundation('reveal', 'close');return false;
})
});
</script>
				<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
				  <dsp:param name="inUrl" value="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp"/>
				  <dsp:oparam name="output">
					<dsp:getvalueof  var="successUrl" param="secureUrl" />
				  </dsp:oparam>
				</dsp:droplet>							

 <dsp:droplet name="/atg/dynamo/droplet/Switch">
			<dsp:param bean="ShippingGroupFormHandler.formError" name="value"/>
			<dsp:oparam name="true">
				<dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
					<dsp:param name="exceptions" bean="ShippingGroupFormHandler.formExceptions" />
					<dsp:oparam name="outputStart">
					<script>
					$(document).ready(function() {
					$("#openErrorShippingModal").click();
					});
															
					</script>
					<div id="notification-style-2" class="reveal-modal small notification"  style="display: block; opacity: 1; top: 100px;">
					</dsp:oparam>
					<dsp:oparam name="output">
					
					<dsp:getvalueof var="errorMessage" param="message"/>
 					 <c:choose>
					 	<c:when test="${fn:contains(errorMessage, 'invalidShippingAddres')}">
						<dsp:form   id="alternativeAddress-info" class="custom" name="alternativeAddress-info" method="get">
								<dsp:getvalueof var="shippingRadioValue" bean="ShippingGroupFormHandler.shippingRadioValue"/>
								<dsp:getvalueof var="billingRadioValue" bean="ShippingGroupFormHandler.billingRadioValue"/>
 								<input type="hidden" name="shipping-address" value="${shippingRadioValue}"/>
								<input type="hidden" name="billing-address" value="${billingRadioValue}"/>
								<dsp:droplet name="/atg/dynamo/droplet/IsEmpty">
								<dsp:param name="value" bean="CricketShippingAddressData.normalizedAddressMap"/>
								<dsp:oparam name="false">
								 <p class="title"> <crs:outMessage key="shipping_address_alternatives"/> </p>
								<dsp:droplet name="/atg/dynamo/droplet/ForEach">
								<dsp:param name="array" bean="CricketShippingAddressData.normalizedAddressMap"/>
								<dsp:setvalue param="address" paramvalue="element" />
									<dsp:oparam name="output">
									<dsp:getvalueof id="option308" param="key"	idtype="java.lang.Integer">
 										<label for="radio14">
											<input name="fromAlternativeAddress" value="${option308}"type="radio" id="radio14" style="display:none;">
											<span class="custom radio"></span> 
											<dsp:valueof param="address.addressLine1"/>,
											<dsp:valueof param="address.city"/>,
											<dsp:valueof param="address.state"/>,
											<dsp:valueof param="address.zipCode"/>,
											<dsp:valueof param="address.country"/> 
										</label>
										 </dsp:getvalueof>
									</dsp:oparam>
								</dsp:droplet> 
									<label for="radio14">
										<input name="fromAlternativeAddress" type="radio" value="NoneOfAbove" id="NoneOfAbove" style="display:none;>
										<span class="custom radio"></span> None of the above match, I'll try again
									</label>	
									<br>	
										<c:if test="${fn:contains(errorMessage,'invalidShippingAddres')}">
											<hr />
											<p> 
											<a href="#" class="button small orange-button" onclick="$('#notification-style-4').foundation('reveal', 'close');return false;">Cancel</a>
											<a href="#" onclick="submitFormPayment('alternativeAddress-info')"class="button small green-button">Submit
											  <dsp:droplet name="/atg/dynamo/droplet/ProtocolChange"> 
									  <dsp:param name="inUrl" value="${contextpath}/checkout/payment/paymentDetails.jsp"/> 
									  <dsp:oparam name="output">
										<dsp:input
											 bean="ShippingGroupFormHandler.shippingBillingAddressSuccessURL"
											 paramvalue="secureUrl" type="hidden"/>
									  </dsp:oparam>
									</dsp:droplet>
									<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
									  <dsp:param name="inUrl" value="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp"/>
									  <dsp:oparam name="output">
									  
										<dsp:input
											 bean="ShippingGroupFormHandler.shippingBillingAddressErrorURL"
											 paramvalue="secureUrl" type="hidden"/>
									  </dsp:oparam>
									</dsp:droplet>									
											<dsp:input type="hidden" bean="ShippingGroupFormHandler.shippingandBillingAddress" value="submit"/>
									</a></p>
									</c:if>	
								</dsp:oparam>				
									 <dsp:oparam name="true">
									 <p class="title"><crs:outMessage key="${errorMessage}"/></p><br>
 											 <a href="#" class="button small green-button" id="invalidAddress" onclick="$('#notification-style-4').foundation('reveal', 'close');return false;">OK</a>

									   </dsp:oparam>	
</dsp:droplet>									   
								</dsp:form> 
						</c:when>
						<c:otherwise>
								 
							  <p><crs:outMessage key="${errorMessage}"/></p>
						 </c:otherwise>
									
							</c:choose>
							</dsp:oparam>
				  <dsp:oparam name="outputEnd">
									<c:choose>
									<c:when test="${errorMessage eq'SYSTEM_EXCEPTIONS'}">
									 
										<p class="buttons">
														<a href="#" class="button small green-button" id="systemException" onclick="$('#notification-style-4').foundation('reveal', 'close');return true;">Return to Store Locator</a>
													</p>
									</c:when>
									<c:when test="${errorMessage eq 'BUSINESS_EXCEPTIONS'}">
									 
										
										<p class="buttons">
														<a href="#" class="button small green-button" id="businessException" onclick="$('#notification-style-4').foundation('reveal', 'close');return true;">Return to Cart</a>
													</p>
									</c:when>
									<c:when test="${errorMessage eq 'espTimeOut'}">
										<p class="buttons">
														<a href="#" class="button small green-button" id="timeOutException" onclick="$('#notification-style-4').foundation('reveal', 'close');return true;">Return to Home</a>
													</p>
									</c:when>
									<c:when test="${fn:contains(errorMessage,'invalidAccountAddress')}">
											 <a href="#" class="button small green-button" id="invalidAddress" onclick="$('#notification-style-4').foundation('reveal', 'close');return false;">OK</a>

									</c:when>
									<c:when test="${fn:contains(errorMessage,'invalidBillingAddress')}">
											 <a href="#" class="button small green-button" id="invalidAddress" onclick="$('#notification-style-4').foundation('reveal', 'close');return false;">OK</a>

									</c:when>
								 
									</c:choose>
									
								 </div>
									 
						 
					</dsp:oparam>
					 
				</dsp:droplet>
			</dsp:oparam>
		</dsp:droplet>

	<div id="notification-style-shippingMethod" class="reveal-modal small notification" style="display: block; opacity: 1; top: 100px;">
		<p>Please note, orders shipping to a PO Box are not eligible for overnight shipping and should be delivered within 5-7 business days</p>
		<p class="buttons">
			<a href="#" class="button small orange-button" onclick="submitFormRemovePOBoxInput();"><crs:outMessage key="cricket_shoppingcart_NO"/><!--No --></a>
			<a href="#" class="button small green-button" onclick="$('#notification-style-shippingMethod').foundation('reveal', 'close');return false;">Yes</a>
			 
		</p>	
	</div>	
		<a data-reveal-id="notification-style-2" href="#" id="openErrorShippingModal" style="display:none;"></a>
		<a data-reveal-id="notification-style-shippingMethod" href="#" id="shippingMethodMsg" style="display:none;"></a>

<script>
function submitFormPayment(formId){ 
 var value = $('input:radio[name=fromAlternativeAddress]:checked').val();
 if(value == 'NoneOfAbove'){
 $('#notification-style-4').foundation('reveal', 'close'); return false;
 }else{
$('#notification-style-4').foundation('reveal', 'close');
	$('a.btn-checkout-step-1').css("display", "none");
	$('a.btn-checkout-step-1').next(".button").css("display", "none");
 	document.getElementById(formId).submit();
	}
}
</script>			 	
</dsp:page>