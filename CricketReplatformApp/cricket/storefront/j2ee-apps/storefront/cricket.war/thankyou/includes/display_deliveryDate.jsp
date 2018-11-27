<dsp:page>
  <dsp:importbean bean="/atg/commerce/ShoppingCart"/>
   	<dsp:getvalueof var="workOrderType" bean='ShoppingCart.current.workOrderType' />
   			<c:if test="${workOrderType ne 'RRC'}">
				<span>
				<crs:outMessage key="cricket_checkout_EstimatedDelivery"/><!-- Estimated delivery - --> 
					<span class="bold">
					 <dsp:valueof bean="ShoppingCart.last.estimatedDeliveryDate" date="MMM dd, yyyy"></dsp:valueof>					 
					</span>
				</span>
			 </c:if>  
</dsp:page>