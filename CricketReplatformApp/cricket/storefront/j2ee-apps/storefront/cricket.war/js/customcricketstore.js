

/* START Checkout Javascript*/
function shippingMethodMsg()
{
if ($('#shipping-method-po-box').is(":visible")) 
	$("#shippingMethodMsg").click();
}
/* END Checkout Javascript*/

/* START ManageABP Javascript*/
function validateFormAndGenerateToken3() {
	var response;
	var el = document.getElementById('TokenService');
	var creditCardNumber = document.getElementById("PaymentDataCardNumberAbp").value;
	var visaRegex = new RegExp("^4[0-9]{12}(?:[0-9]{3})?$");
	var masterRegex = new RegExp("^5[1-5][0-9]{14}$");
	var amexRegex = new RegExp("^3[47][0-9]{13}$");
	var discoverRegex = new RegExp("^6(?:011|5[0-9]{2})[0-9]{12}$");
	if(visaRegex.test(creditCardNumber))
		document.getElementById("CreditCardTypeAbp").value = 'VISA';
	if(masterRegex.test(creditCardNumber))
		document.getElementById("CreditCardTypeAbp").value = 'MC';
	if(amexRegex.test(creditCardNumber))
		document.getElementById("CreditCardTypeAbp").value = 'AE';
	if(discoverRegex.test(creditCardNumber))
		document.getElementById("CreditCardTypeAbp").value = 'DISC';
	var result;
	try {
	 result = getIframeWindow2(el).vesta.token.creditcard_get(creditCardNumber);
	}catch(e){
	     document.getElementById("creditCardErrorAbp").innerHTML = '<label for="creditcard" generated="true" class="error" style=""><p>' + "Sorry we are unable to generate secure token for now, please try after some time." + '</p></label>';
	}
	  
		var vestaToken = result['Token'];
		var responseCode = result['ResponseCode'];
		document.getElementById("vesta-token-abp").value = vestaToken;
		var valLength = creditCardNumber.length;
		var res = creditCardNumber.substring((valLength-4),valLength); 
		var stars= '****************'.substring(0,valLength-4);
		document.getElementById("cardNumberAbp").value = res;
		document.getElementById("PaymentDataCardNumberAbp").value = stars+res;
	if (responseCode != 0)
	{
		document.getElementById("creditCardErrorAbp").innerHTML = '<label for="creditcard" generated="true" class="error" style=""><p>' + result['ResponseText'] + '</p></label>';
	}
	else
	{
		document.getElementById("creditCardErrorAbp").innerHTML = '';
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

function cartOpen(){
	var URL = window.location.href;
	if(URL.indexOf("?") != -1) {
		URL = URL + "&openCart=true";
		window.location = URL;
	} else {
		URL = URL + "?openCart=true";
		window.location = URL;
	}
}

/* END ManageABP Javascript*/
$('a#btn-checkout-step-3-top-custom, a#btn-checkout-step-3-bottom-custom').click(function(e) { 
	if (!window.location.search.indexOf('whoopsError=true') > -1) {
	    e.preventDefault();
	    $(this).css("display", "none");
		$(this).next().css("display", "inline-block");
		$("#btn-checkout-step-3-top-custom").css("display", "none");
		$("#btn-checkout-step-3-bottom-custom").css("display", "none");
		$("#btn-checkout-step3-processing-top").css("display", "inline-block");
		$("#btn-checkout-step3-processing-bottom").css("display", "inline-block");
		document.confirmOrder.submit();  
	}
  }); 

function submitPNFSearchForm() {
	var searchText = $("#search-full").val();
	if(searchText.length > 0) {
		$("#search-newPNF").val(searchText);
		$("#atg_store_searchSubmitPNF").trigger("click");
	}
}

function detectEnterPNFSearchForm(event) {
	if(event.keyCode == 13) {
      submitPNFSearchForm();
    }
}














