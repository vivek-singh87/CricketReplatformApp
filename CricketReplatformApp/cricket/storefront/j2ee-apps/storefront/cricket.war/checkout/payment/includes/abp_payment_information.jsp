<dsp:page>  
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
<dsp:getvalueof var="vestaTokenJspURL" bean="CricketConfiguration.vestaTokenJspURL"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<script>document.domain = "mycricket.com";</script>
<div style="display: none;"> 
<iframe name="TokenService" id="TokenService" src="${vestaTokenJspURL}"></iframe>
</div>
<script src="${contextpath}/js/customcricketstore.js"></script>
 <dsp:importbean bean="/atg/commerce/order/purchase/PaymentGroupFormHandler"/>
 		<div class="payment-info">  
			<div class="row">
			  <div class="large-8 small-12 columns">
			    <h2><crs:outMessage key="cricket_checkout_payment_information"/><!-- Payment Information --></h2>
			    <p><crs:outMessage key="cricket_checkout_accept_all_cards"/><!-- Cricket accepts all major credit cards and debit cards. --></p>
			  </div>
			  <div class="large-4 small-12 columns pay-icons">&nbsp;</div>
			</div>
			<div class="row">
			  <div class="large-6 small-12 columns left">
			    <label for="first-name"><crs:outMessage key="cricket_checkout_first_name"/><!-- First Name --></label>
			  		<%--  <input id="first-name" class="form-element" name="first-name" type="text" required> --%>
					<dsp:input type="text" tabIndex="8" bean="PaymentGroupFormHandler.abpPaymentData.fristName" id="first-nameAbp" class="form-element" name="first-nameAbp" autocomplete="off"  required="false" maxlength="19"/>
			  </div>
			  <div class="large-6 small-12 columns right">
			    <label for="last-name"><crs:outMessage key="cricket_checkout_last_name"/><!-- Last Name --></label>
			    <%--  <input id="last-name" class="form-element" name="last-name" type="text" required> --%>
					<dsp:input type="text" tabIndex="9" bean="PaymentGroupFormHandler.abpPaymentData.lastName"  id="last-nameAbp" class="form-element" name="last-nameAbp" autocomplete="off"  required="false" maxlength="20"/>

			  </div>
			</div>
			<div class="row">
			  <div class="large-6 small-12 columns single">
			    <label for="creditcard"><crs:outMessage key="cricket_checkout_card_number"/><!-- Card Number --></label>
			 		<%-- <input id="creditcard" class="form-element" name="creditcard" type="text" required> --%>
					<input type="text" tabIndex="10" maxlength="16" id="PaymentDataCardNumberAbp" class="form-element" name="PaymentDataCardNumberAbp" autocomplete="off"  onchange="validateFormAndGenerateToken3();"/>
					<dsp:input type="hidden" bean="PaymentGroupFormHandler.abpPaymentData.cardNumber" id="cardNumberAbp" name="cardNumberAbp" />
					<dsp:input type="hidden" bean="PaymentGroupFormHandler.abpPaymentData.vestaToken" id="vesta-token-abp" name="vesta-token-abp" required="false"/>
					<dsp:input type="hidden" bean="PaymentGroupFormHandler.abpPaymentData.creditCardType" id="CreditCardTypeAbp" name="CreditCardTypeAbp" required="false"/>
			  </div>
			  <div class="large-3 small-4 columns month"> 
			    <label for="month"><crs:outMessage key="cricket_checkout_expiration_date"/><!-- Expiration Date --></label>    
			    <dsp:select data-customforms="disabled" tabIndex="11" id="monthAbp" class="form-element" name="monthAbp" value="Month" bean="PaymentGroupFormHandler.abpPaymentData.expirationMonth"  required="false">
			      <dsp:option value=""><crs:outMessage key="cricket_checkout_month"/><!-- Month --></dsp:option>
           			<dsp:option value="1">January</dsp:option>
					<dsp:option value="2">February</dsp:option>
					<dsp:option value="3">March</dsp:option>
					<dsp:option value="4">April</dsp:option>
					<dsp:option value="5">May</dsp:option>
					<dsp:option value="6">June</dsp:option>
					<dsp:option value="7">July</dsp:option>
					<dsp:option value="8">August</dsp:option>
					<dsp:option value="9">September</dsp:option>
					<dsp:option value="10">October</dsp:option>
					<dsp:option value="11">November</dsp:option>
					<dsp:option value="12">December</dsp:option>
			    </dsp:select>
			  </div>
			  <div class="large-3 small-4 columns year">
			   <dsp:getvalueof var="currentYear" bean="/atg/dynamo/service/CurrentDate.year" vartype="java.lang.Integer"/>
					<dsp:select data-customforms="disabled" tabIndex="12" id="yearAbp" class="form-element" name="yearAbp" value="Year" bean="PaymentGroupFormHandler.abpPaymentData.expirationYear"  required="false">									
						<dsp:option label="year" value=""> <crs:outMessage key="cricket_checkout_year"/><!-- Year --></dsp:option>											
						<c:forEach var="year" begin="${currentYear}" end="${currentYear+20}">
						<dsp:option label="${year}" value="${year}">${year}</dsp:option>
						</c:forEach>	   
					</dsp:select>
			  </div>
			</div>
			<!-- desktop form layout cvc number row -->          
			<div class="row" style="padding-bottom: 0.3em;">
			  <div class="large-3 small-6 columns left">
			    <label for="cvcnumber"><crs:outMessage key="cricket_checkout_cvc_number"/><!--  CVC Number --></label>
			   <%-- <input id="cvcnumber" class="form-element" name="cvcnumber" type="text" required maxlength="3"> --%>
 				<dsp:input type="text" tabIndex="13" bean="PaymentGroupFormHandler.abpPaymentData.cvcNumber" maxlength="4" id="cvcnumberAbp" class="form-element" name="cvcnumberAbp" autocomplete="off"  required="false" />
			  </div>
			  <div class="large-3 small-6 columns left qtip has-tip">
			    <span data-tooltip class="has-tip tip-top" title="<crs:outMessage key="cricket_checkout_what_this_tooltip_message"/><!-- The Card Verification Code, or CVC, is an extra code printed<br /> on your debit or credit card. With most cards (Visa, MasterCard,<br /> bank cards, etc.) it is the final three digits of the number printed<br /> on the signature strip on the reverse of your card. On American<br/> Express cards, it is usually a four digit code on the front. -->"><crs:outMessage key="cricket_checkout_what_this"/><!-- What's this? --></span>
			  </div>
			  	<!-- Include Promotion code apply -->    
			</div>
		</div> 
		<div class="divider hide-for-small"></div>
		
<script>
$("#PaymentDataCardNumberAbp").change(function(){ 
	var el = document.getElementById('TokenService');
		var creditCardNumber = document.getElementById("PaymentDataCardNumberAbp").value;
		var valLength = creditCardNumber.length;
		var res = creditCardNumber.substring((valLength-4),valLength); 
		$('#lastFourDugitsofCard').val(res);
		document.getElementById("creditCardlastFourDigits").innerHTML = res;
});	
</script>
<dsp:input type="hidden" bean="UserSessionBean.lastFourDugitsofCard" id="lastFourDugitsofCard" name="lastFourDugitsofCard"/>
 </dsp:page>