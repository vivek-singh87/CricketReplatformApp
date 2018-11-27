<dsp:page>
<dsp:importbean bean="/atg/commerce/order/purchase/PaymentGroupFormHandler" />
<dsp:importbean bean="/com/cricket/pricing/CricketCouponFormHandler" />
<dsp:importbean bean="/atg/dynamo/droplet/ForEach" />
<dsp:importbean bean="/atg/commerce/order/purchase/CommitOrderFormHandler" />
<script>

 
$(function() {
$("#businessException").click(function() {
var url = '/?openCart=true';
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
var url = '../../';
$(location).attr('href', url);
})
});

$(function() {
	var openPopUp = $("#isFormErrorPresent").val();
	if(openPopUp=="yes"){
 		$("#openErrorPaymentModal").click();
	}
 });
$(function() {
	var openPopUp = $("#isFormErrorPresentCoupon").val();
	if(openPopUp=="yes"){
 		$("#openErrorPaymentModal").click();
	}
});
</script>
										

 			<dsp:droplet name="/atg/dynamo/droplet/Switch">
			<dsp:param bean="PaymentGroupFormHandler.formError" name="value"/>
			<dsp:oparam name="true">
					<dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
					<dsp:param name="exceptions" bean="PaymentGroupFormHandler.formExceptions" />
					<dsp:oparam name="outputStart">
			 
				<div id="notification-style-2" class="reveal-modal small notification" >
					</dsp:oparam>
					<dsp:oparam name="output">
					<input type="hidden" id="isFormErrorPresent" value="yes" name="isFormErrorPresentName" />
						 <dsp:getvalueof var="errorMessage" param="message"/>
						  <c:choose>
								 <c:when test="${fn:contains(errorMessage, 'outOfStock')}">
								 <c:set var="lengthOfMsg" value="${fn:length(errorMessage)}"/>
								 <p>Sorry, the quantity requested for the Item's <b>${fn:substring(errorMessage, 10, lengthOfMsg)} </b> not available now. Please remove it from cart.</p>
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
											<a href="#" class="button small green-button" id="systemException" onclick="$('#notification-style-2').foundation('reveal', 'close');return true;">Return to Store Locator</a>
										</p>
								</c:when>
								<c:when test="${errorMessage eq 'BUSINESS_EXCEPTIONS'}">
										<p class="buttons">
											<a href="#" class="button small green-button" id="businessException" onclick="$('#notification-style-2').foundation('reveal', 'close');return true;">Return to Cart</a>
										</p>
								</c:when>
									<c:when test="${errorMessage eq 'espTimeOut'}">
										<p class="buttons">
														<a href="#" class="button small green-button" id="timeOutException" onclick="$('#notification-style-2').foundation('reveal', 'close');return true;">Return to Home</a>
													</p>
									</c:when>
								   <c:when test="${fn:contains(errorMessage, 'outOfStock')}">
										<p class="buttons">
											<a href="#" class="button small green-button" id="businessException" onclick="$('#notification-style-2').foundation('reveal', 'close');return true;">Return to Cart</a>
										</p>
								  </c:when>
									<c:otherwise>
										<p class="buttons">
											<a href="#" class="button small green-button" onclick="$('#notification-style-2').foundation('reveal', 'close');return true;">Ok</a>
										</p>
									</c:otherwise>
								</c:choose>
									
					</div>
						</dsp:oparam>
						</dsp:droplet>
				</dsp:oparam>
				</dsp:droplet>
				
				
			<dsp:droplet name="/atg/dynamo/droplet/Switch">
			<dsp:param bean="CommitOrderFormHandler.formError" name="value"/>
			<dsp:oparam name="true">
					<dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
					<dsp:param name="exceptions" bean="CommitOrderFormHandler.formExceptions" />
					<dsp:oparam name="outputStart">
			 
				<div id="notification-style-2" class="reveal-modal small notification" >
					</dsp:oparam>
					<dsp:oparam name="output">
					<input type="hidden" id="isFormErrorPresent" value="yes" name="isFormErrorPresentName" />
						 <dsp:getvalueof var="errorMessage" param="message"/>
						  <c:choose>
								 <c:when test="${fn:contains(errorMessage, 'outOfStock')}">
								 <c:set var="lengthOfMsg" value="${fn:length(errorMessage)}"/>
								 <p>Sorry, the quantity requested for the Item's <b>${fn:substring(errorMessage, 10, lengthOfMsg)} </b> not available now. Please remove it from cart.</p>
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
											<a href="#" class="button small green-button" id="systemException" onclick="$('#notification-style-2').foundation('reveal', 'close');return true;">Return to Store Locator</a>
										</p>
								</c:when>
								<c:when test="${errorMessage eq 'BUSINESS_EXCEPTIONS'}">
										<p class="buttons">
											<a href="#" class="button small green-button" id="businessException" onclick="$('#notification-style-2').foundation('reveal', 'close');return true;">Return to Cart</a>
										</p>
								</c:when>
									<c:when test="${errorMessage eq 'espTimeOut'}">
										<p class="buttons">
														<a href="#" class="button small green-button" id="timeOutException" onclick="$('#notification-style-2').foundation('reveal', 'close');return true;">Return to Home</a>
													</p>
									</c:when>
								   <c:when test="${fn:contains(errorMessage, 'outOfStock')}">
										<p class="buttons">
											<a href="#" class="button small green-button" id="businessException" onclick="$('#notification-style-2').foundation('reveal', 'close');return true;">Return to Cart</a>
										</p>
								  </c:when>
									<c:otherwise>
										<p class="buttons">
											<a href="#" class="button small green-button" onclick="$('#notification-style-2').foundation('reveal', 'close');return true;">Ok</a>
										</p>
									</c:otherwise>
								</c:choose>
									
					</div>
						</dsp:oparam>
						</dsp:droplet>
				</dsp:oparam>
				</dsp:droplet>
				
				
				
		<!-- Droplet to show Form expections for Coupon Applied on Payment page -->
	<dsp:droplet name="/atg/dynamo/droplet/Switch">
		<dsp:param bean="CricketCouponFormHandler.formError" name="value" />
		<dsp:oparam name="true">
			<dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
				<dsp:param name="exceptions"
					bean="CricketCouponFormHandler.formExceptions" />
				<dsp:oparam name="outputStart">
					 
					<div id="notification-style-2"
						class="reveal-modal small notification">
				</dsp:oparam>
				<dsp:oparam name="output">
					<input type="hidden" id="isFormErrorPresentCoupon" value="yes" name="isFormErrorPresentNameCoupon" />
					<dsp:getvalueof var="errorMessage" param="message" />
					<p>
						<crs:outMessage key="${errorMessage}" />
					</p>
				</dsp:oparam>
				<dsp:oparam name="outputEnd">
					<p class="buttons">
						<a href="#" class="button small green-button"
							onclick="$('#notification-style-2').foundation('reveal', 'close'); return false;">Ok</a>
					</p>
					</div>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
	</dsp:droplet>

	<a data-reveal-id="notification-style-2" href="#" id="openErrorPaymentModal" style="display:none;"></a>
						 
						 	
</dsp:page>