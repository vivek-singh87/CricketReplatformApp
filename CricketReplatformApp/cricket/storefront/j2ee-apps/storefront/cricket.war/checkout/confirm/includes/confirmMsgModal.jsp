<dsp:page>
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

</script>
										

 			<dsp:droplet name="/atg/dynamo/droplet/Switch">
			<dsp:param bean="CommitOrderFormHandler.formError" name="value"/>
			<dsp:oparam name="true">
					<dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
					<dsp:param name="exceptions" bean="CommitOrderFormHandler.formExceptions" />
					<dsp:oparam name="outputStart">
					<script>
							$(document).ready(function() {
							$("#openErrorPaymentModal").click();
							});
																			
					</script>
				<div id="notification-style-2" class="reveal-modal small notification" >
					</dsp:oparam>
					<dsp:oparam name="output">
							<dsp:getvalueof var="errorMessage" param="message"/>
							<c:choose>
								 <c:when test="${fn:contains(errorMessage, 'outOfStock')}">
								 <c:set var="lengthOfMsg" value="${fn:length(errorMessage)}"/>
								  <p>Sorry, the quantity requested for the Item's <b>${fn:substring(errorMessage, 10, lengthOfMsg)} </b> not available now. Please remove it from cart.</p>  
								<%-- <p><crs:outMessage key="${errorMessage}"/></p>  --%>
								
								 </c:when>
								 <c:when test="${fn:contains(errorMessage , 'commit')}">
								  <c:set var="errorMessageATG" value="SYSTEM_EXCEPTIONS"/>
 									<p><crs:outMessage key="${errorMessageATG}"/></p>
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
						
				<a data-reveal-id="notification-style-2" href="#" id="openErrorPaymentModal" style="display:none;"></a>
						 
						 	
</dsp:page>