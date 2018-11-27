<dsp:page>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:getvalueof var="fromPage" param="fromPage"/>
 
 <div class="large-12 small-12 columns">
	<h3><crs:outMessage key="cricket_shoppingcart_automatic_bill_payment"/><!-- Automatic Bill Payment --></h3>
	<p><crs:outMessage key="cricket_shoppingcart_automatic_bill_message2"/><!-- You have signed up for Automatic Bill Pay.--></p>
	<label class="autopay-label first-label">
    			<input type="radio" name="autopay-payment" checked="checked"><crs:outMessage key="cricket_shoppingcart_automatic_bill_message3"/><!-- Use the credit/debit card entered above for Automatic Bill Pay. -->
    		</label>  
			<c:if test="${fromPage eq null || fromPage ne 'thankyou'}">
    		<a class="edit"  href="${contextpath}/checkout/payment/paymentDetails.jsp"><crs:outMessage key="cricket_shoppingcart_edit"/><!-- Edit --></a>
			</c:if>
			<br><br><br><br> 
</div>
</dsp:page>