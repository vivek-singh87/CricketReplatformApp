<dsp:page>
 <dsp:importbean bean="/atg/commerce/order/purchase/GetCreditCardDetailsDroplet"/>

<dsp:droplet name="GetCreditCardDetailsDroplet">
<dsp:param name="order" bean="/atg/commerce/ShoppingCart.current" />
	<dsp:oparam name="output">
		<dsp:getvalueof var="month" param="expireMonth"/>
		<dsp:getvalueof var="year" param="expireYear"/>
		<dsp:getvalueof var="cardType" param="creditCardType"/>
		<dsp:getvalueof var="cardNumber" param="creditCardNumber"/>
	</dsp:oparam>
</dsp:droplet>
 
<h3><crs:outMessage key="cricket_checkout_payment_information"/><!--  Payment Information --></h3>
		
 			
			<c:choose>
				<c:when test="${cardType eq 'MC' || cardType eq 'MASTERCARD' ||cardType eq 'masterCard'}">
					<p class="icon mastercard">
					<crs:outMessage key="cricket_checkout_ending_in"/><!-- Ending In --> 					
						${cardNumber}					
					</p>
				</c:when>
				<c:when test="${cardType eq 'VISA' || cardType eq 'visa'}">
					<p class="icon visa">
						<crs:outMessage key="cricket_checkout_ending_in"/><!-- Ending In  -->
						${cardNumber} 
					</p>
				</c:when>
				<c:when test="${cardType eq 'AE' || cardType eq 'americanExpress' || cardType eq 'AMERICANEXPRESS'}">
					<p class="icon americanexpress">
						<crs:outMessage key="cricket_checkout_ending_in"/><!-- Ending In --> 
						${cardNumber} 
					</p>
				</c:when>
				<c:when test="${cardType eq 'DISC' || cardType eq 'discover' || cardType eq 'DISCOVER'}">
					<p class="icon discover">
					<crs:outMessage key="cricket_checkout_ending_in"/><!-- 	Ending In --> 
						${cardNumber} 	
					</p>
				</c:when>
			</c:choose>	
	<a class="edit"  href="${contextpath}/checkout/payment/paymentDetails.jsp">
		<crs:outMessage key="cricket_checkout_edit"/><!-- Edit -->
	</a>
	
</dsp:page>