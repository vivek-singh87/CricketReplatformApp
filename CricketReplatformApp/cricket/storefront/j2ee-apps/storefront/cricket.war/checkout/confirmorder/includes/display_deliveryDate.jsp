<dsp:page>
  <dsp:importbean bean="/atg/commerce/ShoppingCart"/>
			<span>
		<dsp:getvalueof var="workOrderType" bean='ShoppingCart.current.workOrderType' />
			 <c:if test="${workOrderType ne 'RRC'}">
				<crs:outMessage key="cricket_checkout_EstimatedDelivery"/><!-- Estimated delivery - --> 
				<span class="bold">
				 <dsp:valueof bean="ShoppingCart.current.estimatedDeliveryDate" date="MMM dd, yyyy"></dsp:valueof>					 
				</span>
			 </c:if>  
			</span>
			
</dsp:page>