<dsp:page>
<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
<dsp:importbean bean="/com/cricket/cart/formHandler/CricketAutoBillPaymentFormHandler"/>
<dsp:getvalueof var="isAutoBillPayment" bean="UserSessionBean.autoBillPayment"/>
				 <div class="row no-pad autopay">
		          	<div class="large-12 small-12 columns">
		          		<h2	><%-- <crs:outMessage key="cricket_checkout_automatic_billpay"/> --%>AUTOMATIC BILL PAY</h2>
		          		<p><%-- <crs:outMessage key="cricket_checkout_signup_automatic_billpay"/> --%>You have signed up for Automatic Bill Pay.</p>
						<c:choose>
							<c:when test="${isAutoBillPayment eq true}" >
							<label class="autopay-label first-label" onclick="setAtutoBillPayment(true);">
							<input type="radio" name="autopay-payment" id="autopay-payment-1" checked="checked" value="useSameCardForAutoBillPay">
								<%-- <crs:outMessage key="cricket_checkout_usecreditcard_automatic_billpay"/> --%>
								Use the credit/debit card entered above for Automatic Bill Pay
								</label>
							</c:when>
							<c:otherwise>
							<label class="autopay-label first-label" onclick="setAtutoBillPayment(true);">
								<input type="radio" name="autopay-payment" id="autopay-payment-1"  value="useSameCardForAutoBillPay">
								<%-- <crs:outMessage key="cricket_checkout_usecreditcard_automatic_billpay"/> --%>
								Use the credit/debit card entered above for Automatic Bill Pay
								</label>
							</c:otherwise>
						</c:choose>
		          		<label class="autopay-label" onclick="setAtutoBillPayment(true);">
		          			<input type="radio" name="autopay-payment" id="autopay-payment-2" value="userDifferentCardForAutoBillPay">
		          			<%-- <crs:outMessage key="cricket_checkout_different_creditcard_automatic_billpay"/> --%>
		          			Use a different credit/debit card for Automatic Bill Pay
		          		</label>
		          		<label class="autopay-label" onclick="setAtutoBillPayment(false);">
		          			<input type="radio" name="autopay-payment" id="autopay-payment-3" value="noAutoBillPay">
		          				<%-- <crs:outMessage key="cricket_checkout_nosignup_automatic_billpay"/> --%>
		          			Do not sign up for Automatic Bill Pay
		          		</label>          		
		          	</div>
		          </div> 
				    
		          <!-- If they select a different payment option for automatic bill pay, show the fields for them to submit-->
		          <div id="different-payment-info">
						<dsp:include page="/checkout/payment/includes/abp_payment_information.jsp">	</dsp:include>	
					</div> 
							<div id="payment-terms">
			          <div class="row nopad terms">
									<div class="large-12 small-12 columns">
										<div><h3>	<%-- <crs:outMessage key="cricket_checkout_automatic_billpay_termcondition"/> --%>Automatic Bill Pay Terms &amp; Conditions</h3></div>
										<div class="scroll-area">
											<p>
											<%-- <crs:outMessage key="cricket_checkout_automatic_billpay_termcondition_message1"/>--%>	
											You must accept the following terms and conditions to enroll in Automatic Bill Pay for your Cricket account:
											</p>
											
											<p><%-- <crs:outMessage key="cricket_checkout_automatic_billpay_termcondition_message2"/>--%>
											Your automatic bill payment will begin once you receive and activate your Cricket handset. After activation, your credit card ending in <span class="cc-end-number" id="creditCardlastFourDigits"></span> will be charged automatically each month for the total amount due on your Cricket bill. Your monthly bill includes all phone numbers, rate plans, services and features associated with your Cricket account, plus any applicable taxes and surcharges. If you make any changes to your Cricket account, including adding or removing a new phone number, changing rate plans, or adding or removing features, this may increase or decrease the current amount due for your monthly bill. If you make additional payments towards your bill during the month, these will be deducted from your current amount due. Your automatic monthly payment will be adjusted up or down to reflect any account changes or additional payments without further action by you. Please visit mycricket.com to obtain your current account and bill details.
											</p>

											<p><%-- <crs:outMessage key="cricket_checkout_automatic_billpay_termcondition_message3"/>--%>
											Your automatic bill payment will be charged to your registered payment device two days before your Cricket bill due date. The date of your Cricket bill due date will be based on the shipment date of your handset. Once your equipment ships, you will be notified of the date of your first payment via text message. If you make additional payments toward your bill at least two days before the bill due date, those payments will be subtracted from the amount of your automatic bill payment for that month. Payments within two days of your bill due date will be credited towards the next months bill.
											</p>

											<p><%-- <crs:outMessage key="cricket_checkout_automatic_billpay_termcondition_message4"/>--%>
											All notifications regarding your automatic bill payment will be delivered by text message to the oldest active Cricket phone number associated with your Cricket account. Some text messages may include a website link for more information.
											</p>

											<p><%-- <crs:outMessage key="cricket_checkout_automatic_billpay_termcondition_message5"/>--%>
											Ten days prior to the date your automatic bill payment will be charged to your registered payment device, you will receive a text message with the current amount due as of that date. Any additional one-time payments or changes to your Cricket account after the message is sent may change the actual amount you are charged for that month. Your registered payment device must maintain sufficient funds or credit to cover the actual amount due.
											</p>

											<p><%-- <crs:outMessage key="cricket_checkout_automatic_billpay_termcondition_message5"/>--%>
											Automatic Bill Payment will continue until you unenroll by dialing 611 from your Cricket handset.
											</p>											
										</div>
									</div>
								</div>
								<div class="row nopad terms-accept">
									<div class="large-7 small-6 columns">
										<h3><%-- <crs:outMessage key="cricket_checkout_automatic_billpay_termcondition_message6"/>--%>
										You must enter your credit card information before you can accept the terms &amp; conditions</h3>									
									</div>
									<div class="large-5 small-6 columns">
										 <label id="terms-wrapper" class="terms">
										 <input type="checkbox" name="terms" tabindex="14" id="terms" data-customforms="disabled"  />
										 <span>Accept <span class="hide-for-small">Terms &amp; Conditions</span></span>				            			  
				            			</label>						
									</div>									
									<dsp:getvalueof var="" value="123"/>
								</div>
							</div>
							
				</dsp:page>
				