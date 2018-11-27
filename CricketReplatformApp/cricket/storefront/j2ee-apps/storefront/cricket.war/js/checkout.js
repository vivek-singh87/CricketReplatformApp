var _birthdateVerify; //set global so it can be checked in date form verification
var _creditExpiryVerify=false;
var _creditExpiryVerifyAutoBill=false;
// Checkout Specific JS
$(function(){
//sets true if user selects to make auto pyaments with a different credit card
var diffCardAutoBill = false;

// We need to disable the Cart Toggle in the header if they are on checkout Pages
// For both Desktop & Mobile
if($("#constructor").hasClass("section-checkout")) {
  $("#li-cart a, li.cart-icon").unbind("click");
  $('#li-cart a, li.cart-icon').addClass('no-toggle');
} else  {
  $('#li-cart a, li.cart-icon').removeClass('no-toggle');
}

$('#li-cart a, li.cart-icon').click(function(){
  if ($(this).hasClass('no-toggle')) {
    // change this to wherever the static cart page lives
    //window.location = '/cart/cart-static.php';
  }
});

// All Promo code fields need to display different error message in different location outside of
// validator plugin stuff, also don't show anything on success
$('#promo-value').click(function(e) {
  e.preventDefault();
  if( $("#promo-code").val() ) {
    $("#promo-code").removeClass().addClass("error");
    $(".promo-error").css("visibility" , "visible");
  } else {
    $("#promo-code").removeClass();
    $(".promo-error").css("visibility" , "hidden");
  }
});

/* -=-=-=-=-=-=-=-=-=-=-=- START VALIDATION =-=-=-=-=-=-=-=-=-=-=-=-= */
//NOTES: Javascript Validation (using validate.js) for form elements
function checkoutValidation() {
  $('.error-container').css("visibility" , "visible");
  $("#shipping-method-po-box").hide();

  if( $('.section-checkout').hasClass('new')){
    termsState(true);
  }

  //by default on page load, ensure submit button is set to enabled & processing is disabled
  //in case a user hits the back button or an edit button to return to this page
  //(could be overkill but adding as a just incase)

  if( $('a.btn-checkout-step-1').length > 0 ){
    $('a.btn-checkout-step-1').css("display", "inline-block");
    $('a.btn-checkout-step-1').next(".button").css("display", "none");
  }
  if( $('a.btn-checkout-step-2').length > 0 ){
    $('a.btn-checkout-step-2').css("display", "inline-block");
    $('a.btn-checkout-step-2').next(".button").css("display", "none");
  }
  if( $('a#btn-checkout-step-3-top').length > 0 ){
    $('a#btn-checkout-step-3-top').css("display", "inline-block");
    $('a#btn-checkout-step-3-top').next(".button").css("display", "none");
  }

  // Client Side Validation for Step 1 of Checkout
  $('a.btn-checkout-step-1').click(function(e) {
    e.preventDefault();

    //must check all 3 fields to confirm valid date before submit
    var month = $("#month").children("option:selected").val();
    var day = $("#day").children("option:selected").val()
    var year = $("#year").children("option:selected").val();

    birthdateVerification(month, day, year);

    //ADDED FOR DEFECT 6775 - user able to click submit btn multiple times
    var validated = $("#customer-info").valid();
    if(validated){
      //turn off button click
      $(this).css("display", "none");
      $(this).next(".button").css("display", "inline-block").removeAttr("href");
    }
    $("#customer-info").submit();

  }); // END OF checkoutValidation 1

  // Client Side Validation for Step 2 of Checkout
  $('a.btn-checkout-step-2').click(function(e) {
    e.preventDefault();
    //check if credit card month/year has expired
    creditExpiryVerification();

    //ADDED FOR DEFECT 6775 - user able to click submit btn multiple times
    var validated = $("#payment-info").valid();
    if(validated){
      //turn off button click
      $(this).css("display", "none");
      $(this).next(".button").css("display", "inline-block").removeAttr("href");
    }
    // usual click validation stuff....
    $("#payment-info").submit();

  }); // END OF checkoutValidation 2

  // Client Side Validation for Step 3 of Checkout
  $('a#btn-checkout-step-3-top, a#btn-checkout-step-3-bottom').click(function(e) {
    e.preventDefault();

      //turn off button click
      $(this).css("display", "none");
      $(this).next(".button").css("display", "inline-block").removeAttr("href");

    // usual click validation stuff....
    $("#customer-info").submit();

  }); // END OF checkoutValidation 3

  //STEP 1 VALIDATION ACCOUNT HOLDER INFO/BILLING & SHIPPING INFO
  $('#customer-info').validate({
    onkeyup: false,
    onfocusout: false,
    rules: {
     "first-name": {
        required: true
      },
      "first-name-shipping-address": {
        required: true
      },
      "first-name-billing-address": {
        required: true
      },
      "last-name": {
        required: true
      },
      "last-name-shipping-address": {
        required: true
      },
      "last-name-billing-address": {
        required: true
      },
      "address" : {
        required: true,
        poBox: true
      },
      "address-shipping-address" : {
        required: true,
        poBox: true
      },
      "address-billing-address" : {
        required: true,
        poBox: true
      },
      "phone" : {
        required: true,
        phoneUS: true
      },
      "email" : {
        required: true,
        email: true
      },
      "month" : {
        required: true,
        birthdate: true
      },
      "day" : {
        required: true,
        birthdate: true
      },
      "year" : {
        required: true,
        birthdate: true
      }
    },
    messages: {
      "first-name": '<p>Please enter a first name.</p>',
      "first-name-shipping-address": '<p>Please enter a first name under shipping address.</p>',
      "first-name-billing-address": '<p>Please enter a first name under billing address.</p>',
      "last-name": '<p>Please enter a last name.</p>',
      "last-name-shipping-address": '<p>Please enter a last name under shipping address.</p>',
      "last-name-billing-address": '<p>Please enter a last name under billing address.</p>',
      "address" : '<p>Please check your address or PO Box.</p>',
      "address-shipping-address" : '<p>Please check your address or PO Box under shipping address.</p>',
      "address-billing-address" : '<p>Please check your address or PO Box under billing address.</p>',
      "phone" : '<p>Please check your phone number.</p>',
      "email" : '<p>Please check your email address.</p>',
      "month" : '<p>Please check your birthday month.</p>',
      "day" : '<p>Please check your birthday.</p>',
      "year" : '<p>Please check your birthday year.</p>'
    },
    success: "valid",
    errorContainer: ".error-container"
  });
  //STEP 2  VALIDATION CREDIT CARD INFO -- NEW CUSTOMER
  $('#payment-info').validate({
    onkeyup: false,
    onfocusout: false,
    onclick: false,
    rules: {
      "first-name": {
        required: true
      },
      "first-nameAbp": {
        required: true
      },
      "last-name": {
        required: true
      },
      "last-nameAbp": {
        required: true
      },
      "PaymentDataVO": {
        nowhitespace: true,
        validcreditcard: true,
        required: true,
        digits: true
      },
      "abpPaymentDataVO": {
        nowhitespace: true,
        validcreditcard: true,
        required: true,
        digits: true
      },
      "month":{
        required: true,
        validexpiry: true
      },
      "monthAbp":{
        required: true,
        validexpiryautobill: true
      },
      "different-payment-info":{
        required: true
      },
      "day-auto-bill":{
        required: true
      },
      "year":{
        required: true,
        validexpiry: true
      },
      "yearAbp":{
        required: true,
        validexpiryautobill: true
      },
      "cvcnumber": {
        required: true,
        digits: true,
        minlength: 3,
        maxlength: 4
      },
      "cvcnumberAbp": {
        required: true,
        digits: true,
        minlength: 3,
        maxlength: 4
      },
      "terms": {
        required: true
      }
    },
    success: "valid",
    messages: {
      "first-name": '<p>Please enter a first name.</p>',
      "first-nameAbp": '<p>Please enter a first name under auto bill.</p>',
      "last-name": '<p>Please enter a last name.</p>',
      "last-nameAbp": '<p>Please enter a last name under auto bill.</p>',
      "PaymentDataVO": {
        required: "<p>Please enter your credit card number.</p>"
        // If you need super specific error messages for each thing add them here & uncomment
        //minlength: "Min Length Error: Please enter at least 15 digits",
        //maxlength: "Max Length Error: Your credit card number is too long",
        //digits: "Digits Error: Must contain numbers, not letters",
        //nowhitespace: "No whitespace please"
      },
      "abpPaymentDataVO" : {
        // If you need super specific error messages for each thing add them here & uncomment
        //required: "You can't leave this field empty."
        //minlength: "Min Length Error: Please enter at least 15 digits",
        //maxlength: "Max Length Error: Your credit card number is too long",
        //digits: "Digits Error: Must contain numbers, not letters",
        //nowhitespace: "No whitespace please"
      },
      "month" : '<p>Please check your credit card expiry month.</p>',
      "year" : '<p>Please check your credit card expiry year.</p>',
      "monthAbp" : '<p>Please check your credit card expiry month.</p>',
      "yearAbp" : '<p>Please check your credit card expiry year.</p>',
      "cvcnumber" :  '<p>Your cvc number should be 3 to 4 digits.</p>',
      "cvcnumberAbp" :  '<p>Your cvc number should be 3 to 4 digits under auto bill.</p>',
      "terms" : '<p>Please accept the Automatic Bill Pay terms & conditions.</p>'
    },
     highlight: function(element, errorClass, validclass) {
      // if the element is a checkbox, highlight the entire group
      if(element.type == "checkbox" ){
        $(element).addClass("error").removeClass("valid");
        $(element).parent('#terms-wrapper').removeClass("valid").addClass("error-border");
        //$(element.form).find("label[for=" + element.id + "]").addClass("error");
      } else {
        $(element).addClass("error").removeClass("valid");
      }
     },
     unhighlight: function(element, errorClass, validClass) {
       if(element.type == "checkbox" ){
         $(element).removeClass("error").addClass("valid");
         $(element).parent('#terms-wrapper').removeClass("error-border").addClass("valid");
         //$(element.form).find("label[for=" + element.id + "]").removeClass("error");
       } else {
         $(element).removeClass("error").addClass("valid");
       }
     },
      errorPlacement: function(error, element) {
        if( element.attr("name") == "terms" ){
          error.insertBefore(element);
        } else {
          error.insertAfter(element);
        }
      },
    errorContainer: ".error-container"
  });
}


function creditExpiryVerification(){
  var month = $("#month").children("option:selected").val();
  var year = $("#year").children("option:selected").val();
  dateExpiryCheck(month, year, false);

  var monthAutoBill = $("#monthAbp").children("option:selected").val();
  var yearAutoBill = $("#yearAbp").children("option:selected").val();

  if( $("#different-payment-info").css('display') != 'none'){
    dateExpiryCheck(monthAutoBill, yearAutoBill, true);
  }


}

function dateExpiryCheck(month, year, autobill){
  var month = month;
  var year = year;
  var autobill = autobill;

  if(month != undefined && year != undefined){//if both have a date value set
    var todayDate = new Date();
    var todayMonth = todayDate.getMonth() + 1;
    var todayYear = todayDate.getFullYear();

    var checkVerify;

    //if users year matches current year
    if(todayYear == year ){
      //remember credit cards expire on the LAST day of the specified month
      //users card has not yet expired but will next month
      if(todayMonth == month){
        checkVerify = true;
      // todays date is < so users credit card month is valid
      } else if(todayMonth < month) {
        checkVerify = true;
      //todays date is > so users credit card has expired
      } else if(todayMonth > month) {
        checkVerify = false;
      }

    } else if( todayYear > year){ //if the year is before todays year the credit card has expired.
       checkVerify = false;
    } else if( todayYear < year ){
      checkVerify = true;
    }

    if ( autobill ){
      _creditExpiryVerifyAutoBill = checkVerify;
    } else {
      _creditExpiryVerify = checkVerify;
    }
  }
}

function poboxValidation(){ //on the fly typing validation will set shipping method type below
  $('#address').on('input', function(e) {
    var diffShipAddress = $('different-shipping-address');
    if(diffShipAddress.length > 0) return;
    poboxValidationMatch(e);
  });
  $('#address-shipping-address').on('input', function(e) {
    poboxValidationMatch(e);
  });

  /* IE 8 solution */
  $('#address').on('propertychange', function(e) {
    var diffShipAddress = $('different-shipping-address');
    if(diffShipAddress.length > 0) return;
    poboxValidationMatch(e);
  });
  $('#address-shipping-address').on('propertychange', function(e) {
    poboxValidationMatch(e);
  });
}

function poboxValidationMatch (e, value){
  var inputValue;
  if( value ){
    inputValue = value;
  }else{
     inputValue = e.target.value;
  }
    var pattern = /(^p\.?\s?o\.?\s?b\.?(ox|in)?(\s|[0-9])|post\soffice\s?b\.?(ox|in))/i;
    if( inputValue.match(pattern) ){ //it is a po box
      $("#shipping-method-free").hide();
      $("#shipping-method-po-box").show();
    } else {
      $("#shipping-method-free").show();
      $("#shipping-method-po-box").hide();
    }
}

function birthdateValidation(){ //confirming legit date is set, ie: feb 31 can never exist
  _birthdateVerify = false;
  var month = $("#month").children("option:selected").val();
  var day = $("#day").children("option:selected").val()
  var year = $("#year").children("option:selected").val();
  birthdateVerification(month, day, year);
}

function birthdateVerification(month, day, year){
  // console.log('month ', month);
  var month = Number(month);
  month = (month-1);// incoming value needs to be between 0-11 for javascript
  month = month.toString();
  var day = day;
  var year = year;

  if(month != undefined && day != undefined && year != undefined){//if all 3 have a date value
    var date = new Date(year, month, day); // Use the proper constructor
    var todayDate = new Date();
    var todayMonth = todayDate.getMonth();
    var todayMonthFormtd = monthFormated( Number(todayMonth) );  //requires 2 digits for parsing
    var todayDay = todayDate.getDate();
    var todayYear = todayDate.getFullYear();
    var todayDateText = todayYear + "/" + todayMonthFormtd + "/" + todayDay;
    var todayToDate = Date.parse(todayDateText);
    var monthFormtd = monthFormated( Number(month) ); //requires 2 digits for parsing
    var userDate = year.toString() + "/" + monthFormtd + "/" + day.toString();
    var DOB = Date.parse( userDate );
    _birthdateVerify = date.getFullYear() == year && date.getMonth() == month && date.getDate() == day;

    if (DOB >= todayToDate) { //if the users date of birth is in the future (or is today) set verify to false
      _birthdateVerify = false;
    }
  }
}

function monthFormated(month){
  return month < 10 ? "0" + (month+1) : month+1;
}

//set the terms on/off depending on the values passed
function termsState(value){ //state -- disabled/enabled, value-- true or false
  if(!value){
    $("#payment-terms").removeClass("disabled");
    //$("span.checkbox, label.terms").addClass("error");
  } else {
    $("#payment-terms").addClass("disabled");
    $("span.cc-end-number").html('');
    //$("span.checkbox, label.terms").removeClass("error");
  }
  if( !$('#constructor').hasClass("existing") ){
	 $('terms').disabled = value;
  }

}

function termsToggle(){
  $('#first-name, #last-name, #cardNumber, #cvcnumber, .form-element').on('blur', function(e){
    if( $('.section-checkout').hasClass('step2') ){
      termsToggleActions();
    }
  });
  $('#month, #year').on('click change', function(e){
    if( $('.section-checkout').hasClass('step2') ){
      termsToggleActions();
    }
  });
  var self = this;
  $('input[type="radio"]').on('click change', function(e){
    if( this.id == "autopay-payment-2" ){ //if 2nd form, grey out form again, reset.
      diffCardAutoBill = true;
      termsState(true);
    } else if( this.id == ("autopay-payment-1") ) {//if 1st form again, check if good to go and it will choose to set form on/off.
      diffCardAutoBill = false;
      termsToggleActions();
    }
  });
}

function termsToggleActions(){
  var checkSelected;
  var allFormFields = [];
  $('#payment-info input.form-element').each( function(n,element){
    if(diffCardAutoBill){
      if(n < 8 && n > 4){// push into array --checking between to skip mobile specific code
        allFormFields.push(element.value.length);
      }
    } else {
      if(n < 4){// push into array
        allFormFields.push(element.value.length);
      }
    }
  });

  function inputSet(){
    for(var i = 0; i < allFormFields.length; i++){
      var isOk;
      if(allFormFields[i] > 0){
        isOk = true;
      } else {
        isOk = false;
        return false; //stop looping still an empty field.
      }
    }

    if(isOk){
      return true;
    } else {
      return false;
    }
  }

  function selectSet(){
    var monthVal = $('#month').children("option:selected").val();
    var yearVal = $('#year').children("option:selected").val();
    var monthValAutoBill = $('#monthAbp').children("option:selected").val();
    var yearValAutoBill = $('#yearAbp').children("option:selected").val();

    if( monthVal && yearVal && !diffCardAutoBill || monthValAutoBill && yearValAutoBill && diffCardAutoBill ){
      return true;
    } else {
      return false;
    }
  }

  if( inputSet() && selectSet() ){
    //turn on form && add last 4 digits of user credit card number
    var tempVal;
    if(diffCardAutoBill){
      //tempVal = $("#creditcard-auto-bill").val();
      tempVal = $("#abpPaymentDataVO").val();
    } else {
      tempVal = $("#PaymentDataVO").val();
    }
    var valLength = tempVal.length;
    var finalVal = tempVal.slice( (valLength-4), valLength);
    $("span.cc-end-number").html(finalVal);
    termsState(false);
  }
}

/* -=-=-=-=-=-=-=-=-=-=-=- END VALIDATION =-=-=-=-=-=-=-=-=-=-=-=-= */

// Might not be needed now? Originally was used to open/close header drawers
function checkoutDrawers() {
  var activeDrawer;
  $("a.open-drawer").on("click", function(e){
      e.preventDefault();
      var drawerSelected = ".drawer-" + $(this).data("drawerId");
      //check if this is active drawer..if yes close existing drawer
      if( $(drawerSelected).hasClass("active") ){
        $(activeDrawer).removeClass("active").removeClass("open").addClass("closed");
        activeDrawer = null;
        return;
      }

      //check if there is an active drawer
      if(activeDrawer){
        if( $(drawerSelected).hasClass("closed")){
          //remove old active drawer
          $(activeDrawer).removeClass("active").removeClass("open").addClass("closed");
          //set this as new active drawer
          activeDrawer = drawerSelected;
          $(drawerSelected).removeClass("closed").addClass("open").addClass("active");
        }
      } else {
        activeDrawer = drawerSelected;
        $(drawerSelected).removeClass("closed").addClass("open").addClass("active");
      }
    });
}


// In some steps of the checkout process you can toggle your cart to see all your items
// TO DO - change + sign to a - sign when open
function toggleSummary() {
  $("a.open-summary-drawer").on("click", function(e){
    e.preventDefault();
    if( $(".summary-drawer").hasClass("closed")){
      $(".summary-drawer").removeClass("closed").addClass("open");
      $(".open-summary-drawer").parent(".title").addClass("active");
    }else{
      $(".summary-drawer").removeClass("open").addClass("closed");
      $(".open-summary-drawer").parent(".title").removeClass("active");
    }
  });
}

// More toggling stuff, this time with radio buttons
function toggleShippingAddress() {
  $("#different-shipping-address").hide();
  $("#address1").next(".radio").on("click", function(e){
    $("#different-shipping-address").hide();
    //because this is closing -- check if field above is po box and set shipping method correctly
    if ( $('#address').val() ){
      poboxValidationMatch( null, $('#address').val() );
    }
  });
   $("#address2").next(".radio").on("click", function(e){
    $("#different-shipping-address").show();
  });
}

function toggleBillingAddress() {
  $("#different-billing-address").hide();
  $("#billing-address1, #billing-address2").next(".radio").on("click", function(e){
    $("#different-billing-address").hide();
  });
  $("#billing-address3").next(".radio").on("click", function(e){
    $("#different-billing-address").show();
  });
}

function togglePaymentInformation() {
  $("#different-payment-info").hide();
  $("#autopay-payment-1, #autopay-payment-3").next(".radio").on("click", function(e){
    $("#different-payment-info").hide();
    $("#payment-terms").show();
    $('.autopay p').show();
  });
  $("#autopay-payment-3").next(".radio").on("click", function(e){
    $("#payment-terms").hide();
    $('.autopay p').hide();
  });
  $("#autopay-payment-2").next(".radio").on("click", function(e){
    $("#different-payment-info").show();
    $("#payment-terms").show();
    $('.autopay p').show();
  });
}

function locationWarningModal(){

  var defaultStateValue = $('#state').children("option:selected").val();

  $("#city, #zipcode").focus(function(){
    modalAlert(this);
    $(this).blur();
  });
  $('#state').on('click change', function() {
    var hasValue = $('#state').children("option:selected").val();
    if(hasValue){
       var newSelection = $('#state').children("option:selected").val();
        $('.state').val(defaultStateValue);
        Foundation.libs.forms.refresh_custom_select( $('.state'), true );
    }
    modalAlert(this, hasValue);
  })
}

// Whoops Error - disable button & add tooltip defect #8481
// whoopsError param will either be "true" or not show at all
function disableButtonError() {
  if (window.location.search.indexOf('whoopsError=true') > -1) {
    var buttons = $("#btn-checkout-step-3-top-custom, #btn-checkout-step-3-bottom-custom");
    buttons.removeAttr("href");
    buttons.addClass("disabled");
    // Add Tooltip
    buttons.addClass("has-tip")
      .attr('data-tooltip', '')
      .attr('title', 'We are having a system issue, please call our<br /> Sales Department 1-800-975-3708.');
    // Shouldn't have anything happen when you click them
    buttons.on("click", function(e){
      e.stopImmediatePropagation();
    });
  }
}

function modalAlert(value, hasValue){

  var dialogHTML = '<div class="modal"><div class="dialog-content"><h2>Are you sure you want to change your location?</h2><p>If yes, your cart will be emptied and you will have to start again.</p><a href="#" class="button small orange" data-confirm="no">No</a><a href="#" class="button small green" data-confirm="yes">Yes, empty my cart</a></div></div>';
  var overlayHTML = '<div class="md-overlay"></div>';

  // only add modal bg once
  if (!$(".md-overlay").length > 0) {
    $("#outer-wrap").after(overlayHTML);
  }
  // only add modal once in markup
  if (!$(".modal").length > 0) {
    $("#outer-wrap").before(dialogHTML);
  }

 var $this = value;

  $('.dialog-content a.button').on("click", function(e){
    e.preventDefault();

    var confirm = $(this).attr('data-confirm');
    if(confirm == "yes"){

		$("#clear-cart").submit();
		$(".modal, .md-overlay").remove();
    } else {
      //either way close the modal
      $(".modal, .md-overlay").remove();
    }
  });
}

function checkPOBoxSyntax(value)
{
	var pattern1 = /(^p\.?\s?o\.?\s?b\.?(ox|in)?(\s|[0-9])|post\soffice\s?b\.?(ox|in))/i; //check if po box
	var pattern2 = /(^p\.?\s?o\.?\s?b\.?(ox|in)?(\s[0-9])|^post\soffice\s?b\.?(ox|in)?(\s[0-9]))/i;//check if po box with # (for legit po box)

	if( value.match(pattern1) )
	{ //check for po box
		if( value.match(pattern2) )
		{ // check for po box w/a number for legit po box
			$('#notification-po-box').foundation('reveal', 'open');
			return true;
		}
		return false;
	}
	else
	{ //if no po box match assume its a regular address
		if(value.length > 6)
		{
			return true;
		}
	}
}

if($("#constructor").hasClass("section-checkout")) {
  //  Init
  $(".error-container").hide();
  disableButtonError();
  checkoutValidation();
  toggleSummary();
  toggleShippingAddress();
  toggleBillingAddress();
  togglePaymentInformation();
  locationWarningModal();
  poboxValidation();
  birthdateValidation();
  termsToggle();
  termsState(true);


  $("#address").change(function(){
	  checkPOBoxSyntax($(this).val());
  });

}
});
