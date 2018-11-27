<dsp:page>
	<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
	<h3><crs:outMessage key="cricket_shoppingcart_automatic_bill_payment"/><!-- Automatic Bill Pay --></h3>
	<p><crs:outMessage key="cricket_shoppingcart_automatic_bill_message"/><!-- Save $5 each month with Automatic Bill Pay! --><br />
	   <crs:outMessage key="cricket_shoppingcart_automatic_bill_message1"/>
		<!-- With Automatic Bill Pay, your balance due will automatically be deducted 2 days prior to your billing due date each month using the credit card of your choice. Plus, get a $5 monthly discount!</p> -->
	<dsp:form id="auto-bill-payment">
		<dsp:input bean="UserSessionBean.autoBillPayment" type="checkbox" class="auto-bill-pay-checkbox" onclick="setAutoBill(this)"/>
		<span class="auto-bill-payment-label"><crs:outMessage key="cricket_shoppingcart_signup_automatic_bill_payment"/><!-- Sign up for automatic bill payments. --></span>
	</dsp:form>

</dsp:page>