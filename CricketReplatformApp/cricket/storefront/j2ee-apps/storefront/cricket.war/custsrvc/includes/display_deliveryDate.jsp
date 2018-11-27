<dsp:page>
<dsp:getvalueof var="order" param="order"/>
<span>
<crs:outMessage key="cricket_checkout_EstimatedDelivery"/><!-- Estimated delivery - --> 
	<span class="bold">
	 <dsp:valueof bean="order.estimatedDeliveryDate" date="MMM dd, yyyy"></dsp:valueof>					 
	</span>
</span>
</dsp:page>