<dsp:page>
					<dsp:droplet name="/atg/dynamo/droplet/Switch">
						<dsp:param name="value" param="activeProgressBar"/>
						<dsp:oparam name="1">
							<dsp:getvalueof var="step1Class" value="step-1 current"/>
							<dsp:getvalueof var="step2Class" value="step-2"/>
							<dsp:getvalueof var="step3Class" value="step-3"/>
							<dsp:getvalueof var="desktopProgressbarClass" value="large-12 small-12 columns progress-bar step1"/>
							<dsp:getvalueof var="mobileProgressbarClass" value="large-12 small-12 columns progress-bar mobile step1"/>
							<dsp:getvalueof var="mobileShippingText" value="1 Shipping and Billing"/>
							<dsp:getvalueof var="mobilePaymentText" value="2"/>
							<dsp:getvalueof var="mobileConfirmText" value="3"/>
						</dsp:oparam>
						<dsp:oparam name="2">
							<dsp:getvalueof var="step1Class" value="step-1"/>
							<dsp:getvalueof var="step2Class" value="step-2 current"/>
							<dsp:getvalueof var="step3Class" value="step-3"/>
							<dsp:getvalueof var="desktopProgressbarClass" value="large-12 small-12 columns progress-bar step2"/>
							<dsp:getvalueof var="mobileProgressbarClass" value="large-12 small-12 columns progress-bar mobile step2"/>
							<dsp:getvalueof var="mobileShippingText" value="1"/>
							<dsp:getvalueof var="mobilePaymentText" value="2 Payment Details"/>
							<dsp:getvalueof var="mobileConfirmText" value="3"/>
						</dsp:oparam>
						<dsp:oparam name="3">
							<dsp:getvalueof var="step1Class" value="step-1"/>
							<dsp:getvalueof var="step2Class" value="step-2"/>
							<dsp:getvalueof var="step3Class" value="step-3 current"/>
							<dsp:getvalueof var="desktopProgressbarClass" value="large-12 small-12 columns progress-bar step3"/>
							<dsp:getvalueof var="mobileProgressbarClass" value="large-12 small-12 columns progress-bar mobile step3"/>
							<dsp:getvalueof var="mobileShippingText" value="1"/>
							<dsp:getvalueof var="mobilePaymentText" value="2"/>
							<dsp:getvalueof var="mobileConfirmText" value="3 Confirm Order"/>
							
						</dsp:oparam>
						<dsp:oparam name="default">
						</dsp:oparam>
					</dsp:droplet>

<div class="section-progress-content hide-for-small">
			<div class="row">
				<div class="large-12 small-12 columns">
				    <h1><crs:outMessage key="cricket_checkout_Checkout"/><!-- Checkout --></h1>
				</div>		
			</div>	
			<div class="row">			
		
					
				<div class="${desktopProgressbarClass}">
				<ul>
					<li id="step1" class="${step1Class}">
						<crs:outMessage key="cricket_checkout_one"/>
						<crs:outMessage key="cricket_checkout_shippingbillingdetails"/>
						<!-- 1 Shipping and Billing Details -->
					</li>
					<li id="step2" class="${step2Class}">
					 <crs:outMessage key="cricket_checkout_two"/>
					<crs:outMessage key="cricket_checkout_paymentdetails"/>
					<!-- 	2 Payment Details -->
					</li>
					<li id="step3" class="${step3Class}">
					 <crs:outMessage key="cricket_checkout_three"/> 
					 <crs:outMessage key="cricket_checkout_confirmorder"/>
						<!-- 3 Confirm Order -->
					</li>
				</ul>
				</div>		
			</div>
		</div> <!--/.section-progress-content-->		

		<!-- mobile view -->
	<div class="section-progress-content show-for-small">
			<div class="row">
				<div class="large-12 small-12 columns">
				    <h1><crs:outMessage key="cricket_checkout_Checkout"/><!-- Checkout --></h1>
				</div>		
			</div>	
			<div class="row">
				<div class="${mobileProgressbarClass}">
				<ul>
					<li class="${step1Class}"><c:out value="${mobileShippingText}"/></li>
					<li class="${step2Class}"><c:out value="${mobilePaymentText}"/></li>
					<li class="${step3Class}"><c:out value="${mobileConfirmText}"/></li>
				</ul>
				</div>		
			</div>
		</div> <!--/.section-progress-content-->	
</dsp:page>