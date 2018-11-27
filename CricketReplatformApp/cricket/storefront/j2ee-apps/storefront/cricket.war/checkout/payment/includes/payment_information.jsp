<dsp:page> 
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
<dsp:getvalueof var="vestaTokenJspURL" bean="CricketConfiguration.vestaTokenJspURL"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<script>document.domain = "mycricket.com";</script>
<div style="display: none;"> 
<iframe name="TokenService" id="TokenService" src="${vestaTokenJspURL}"></iframe>
</div>
<script>
	function validateFormAndGenerateToken2() {
		//vesta token service needs this set
 		var response;
		var el = document.getElementById('TokenService');
 		var creditCardNumber = document.getElementById("PaymentDataCardNumber").value;
		var visaRegex = new RegExp("^4[0-9]{12}(?:[0-9]{3})?$");
 		var masterRegex = new RegExp("^5[1-5][0-9]{14}$");
 		var amexRegex = new RegExp("^3[47][0-9]{13}$");
 		var discoverRegex = new RegExp("^6(?:011|5[0-9]{2})[0-9]{12}$");
 		if(visaRegex.test(creditCardNumber))
 			document.getElementById("CreditCardType").value = 'VISA';
 		if(masterRegex.test(creditCardNumber))
 			document.getElementById("CreditCardType").value = 'MC';
 		if(amexRegex.test(creditCardNumber))
 			document.getElementById("CreditCardType").value = 'AE';
 		if(discoverRegex.test(creditCardNumber))
 			document.getElementById("CreditCardType").value = 'DISC';
 		var result;
 		var errorMsg;
		try {
		 result = getIframeWindow2(el).vesta.token.creditcard_get(creditCardNumber);
		}catch(e){
	     document.getElementById("creditCardError").innerHTML = '<label for="creditcard" generated="true" class="error" style=""><p>' + "Sorry we are unable to generate secure token for now, please try after some time." + '</p></label>';
		}
 		var vestaToken = result['Token'];
 		var responseCode = result['ResponseCode'];
 		document.getElementById("vesta-token").value = vestaToken;
		var valLength = creditCardNumber.length;
		var stars= '****************'.substring(0,valLength-4);
		var res = creditCardNumber.substring((valLength-4),valLength); 
		document.getElementById("PaymentDataVO").value = res;
		document.getElementById("PaymentDataCardNumber").value = stars+res;
		
		if (responseCode != 0)
		{
			var errorMsg;
			if(responseCode == 1003){
				errorMsg = '<label for="creditcard" generated="true" class="error" style=""><p>Credit Card Number is not valid. Please verify.</p></label>';			
			}else{			
				errorMsg = '<label for="creditcard" generated="true" class="error" style=""><p>' + result['ResponseText'] + '</p></label>';
			}
			document.getElementById("creditCardError").innerHTML = errorMsg;
			$('.error-container').css("display", "block");
			$('.error-container').css("visibility", "visible");
		}
		else
		{
			document.getElementById("creditCardError").innerHTML = '';
			$('.error-container').css("display", "none");
			$('.error-container').css("visibility", "none");			
		}
		return result;
	}
	
	function getIframeWindow2(iframe_object) {
		  var doc;
		  if (iframe_object.contentWindow) {
		    return iframe_object.contentWindow;
		    
		  }

		  if (iframe_object.window) {
		    return iframe_object.window;
		  } 

		  if (!doc && iframe_object.contentDocument) {
		    doc = iframe_object.contentDocument;
		  } 

		  if (!doc && iframe_object.document) {
		    doc = iframe_object.document;
		  }

		  if (doc && doc.defaultView) {
		   return doc.defaultView;
		  }

		  if (doc && doc.parentWindow) {
		    return doc.parentWindow;
		  }

		  return undefined;
		}
</script>
 <dsp:importbean bean="/atg/commerce/order/purchase/PaymentGroupFormHandler"/>
		<div class="payment-info">  
				<div class="row">
				  <div class="large-8 small-12 columns">
				    <h2><crs:outMessage key="cricket_checkout_payment_information"/><!-- Payment Information --></h2>
				    <p><crs:outMessage key="cricket_checkout_accept_all_cards"/><!-- Cricket accepts all major credit and debit cards. --></p>
				  </div>
				  <div class="large-4 small-12 columns pay-icons">&nbsp;</div>
				</div>
				<div class="row">
					<div class="large-12 small-12 columns">
						<div class="error-container"><crs:outMessage key="cricket_checkout_correct_errors"/><!-- Please correct the error(s) indicated below --><div id="creditCardError"></div></div>										
					</div>
				</div>
				<div class="row">
				  <div class="large-6 small-12 columns left">
			    	<label for="first-name"><crs:outMessage key="cricket_checkout_first_name"/><!-- First Name --></label>
				  		<%--  <input id="first-name" class="form-element" name="first-name" type="text" required> --%>
						<dsp:input type="text" tabIndex="1" bean="PaymentGroupFormHandler.paymentData.fristName" id="first-name" class="form-element" name="first-name" autocomplete="off"  required="true" maxlength="19"/>
				  </div>
				  <div class="large-6 small-12 columns right">
			    	<label for="last-name"><crs:outMessage key="cricket_checkout_last_name"/><!-- Last Name --></label>
			   			 <%--  <input id="last-name" class="form-element" name="last-name" type="text" required> --%>
						<dsp:input type="text" tabIndex="2" bean="PaymentGroupFormHandler.paymentData.lastName"  id="last-name" class="form-element" name="last-name" autocomplete="off"  required="true" maxlength="20"/>
				  </div>
				</div>
				<div class="row">
				  <div class="large-6 small-12 columns single">
				    <label for="PaymentDataVO"><crs:outMessage key="cricket_checkout_card_number"/><!-- Card Number --></label>
				 	<%--<input id="creditcard" class="form-element" name="creditcard" type="text" required> --%>
					<input type="text" tabIndex="3" maxlength="16" id="PaymentDataCardNumber" class="form-element" name="PaymentDataCardNumber" autocomplete="off"  onchange="validateFormAndGenerateToken2();"/>
					<dsp:input type="hidden" bean="PaymentGroupFormHandler.paymentData.cardNumber" id="PaymentDataVO" name="PaymentDataVO" />
					<dsp:input type="hidden" bean="PaymentGroupFormHandler.paymentData.vestaToken" id="vesta-token" name="vesta-token" required="false"/>
					<dsp:input type="hidden" bean="PaymentGroupFormHandler.paymentData.creditCardType" id="CreditCardType" name="CreditCardType" required="false"/>
				  </div>
				  <div class="large-3 small-4 columns month"> 
				    <label for="month"> <crs:outMessage key="cricket_checkout_expiration_date"/> <!-- Expiration Date --></label>    
				    <dsp:select data-customforms="disabled" tabIndex="4" id="month" autocomplete="off" class="form-element" name="month" value="Month" bean="PaymentGroupFormHandler.paymentData.expirationMonth"  required="true">
				      <dsp:option value=""><crs:outMessage key="cricket_checkout_month"/> <!-- Month --></dsp:option>
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
					<dsp:select data-customforms="disabled" tabIndex="5" autocomplete="off" id="year" class="form-element" name="year" value="Year" bean="PaymentGroupFormHandler.paymentData.expirationYear"  required="true">
						<dsp:option label="year" value=""><crs:outMessage key="cricket_checkout_year"/><!-- Year --></dsp:option>											
						<c:forEach var="year" begin="${currentYear}" end="${currentYear+20}">
						<dsp:option label="${year}" value="${year}">${year}</dsp:option>
						</c:forEach>	   
					</dsp:select>
				  </div>
				</div>
				<!-- desktop form layout cvc number row -->          
				<div class="row" style="padding-bottom: 0.3em;">
				  <div class="large-3 small-6 columns left">
				    <label for="cvcnumber"><crs:outMessage key="cricket_checkout_cvc_number"/> </label>
				   <%-- <input id="cvcnumber" class="form-element" name="cvcnumber" type="text" required maxlength="3"> --%>
	 				<dsp:input type="text" tabIndex="6" bean="PaymentGroupFormHandler.paymentData.cvcNumber" id="cvcnumber" class="form-element" name="cvcnumber" autocomplete="off"  required="true" maxlength="4" />
				  </div>
				  <div class="large-3 small-6 columns left qtip has-tip">
				    <span data-tooltip class="has-tip tip-top" title="<crs:outMessage key="cricket_checkout_what_this_tooltip_message"/><!-- The Card Verification Code, or CVC, is an extra code printed<br/> on your debit or credit card. With most cards (Visa, MasterCard,<br/> bank cards, etc.) it is the final three digits of the number printed<br /> on the signature strip on the reverse of your card. On American<br/> Express cards, it is usually a four-digit code on the front. -->"> <crs:outMessage key="cricket_checkout_what_this"/><!-- What's this? --></span>
				  </div>
				  	<!-- Include Promotion code apply -->    
					<dsp:include page="/checkout/payment/includes/promotionApply.jsp" flush="true"></dsp:include>
				</div>
				<div class="row">
					 <div class="columns large-8 small-12 large-offset-6">
					   <div class="promo-error"><p><crs:outMessage key="cricket_checkout_enter_valid_promotioncode"/><!-- Please enter a valid promotion code --></p></div>
					 </div>
				</div>
			</div> 
		<div class="divider hide-for-small"></div>
<script>
$("#PaymentDataCardNumber").change(function(){ 
	var el = document.getElementById('TokenService');
		var creditCardNumber = document.getElementById("PaymentDataCardNumber").value;
		var valLength = creditCardNumber.length;
		var res = creditCardNumber.substring((valLength-4),valLength); 
		$('#lastFourDugitsofCard').val(res);
		document.getElementById("creditCardlastFourDigits").innerHTML = res;
});	
</script>
	<dsp:input type="hidden" bean="UserSessionBean.lastFourDugitsofCard" id="lastFourDugitsofCard" name="lastFourDugitsofCard"/>
</dsp:page>