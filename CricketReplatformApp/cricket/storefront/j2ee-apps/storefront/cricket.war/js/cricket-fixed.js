//
// Specific Cricket website application JavaScript
// NOTE: December 19, 2013 - This file includes a reduced cricket.js file and cart.js and search.js files, eliminating any js functionality specific to mobile screens
//	

// Function for email placeholder fix
//
// If browser doesn't support placeholder attribute, add/remove value attribute instead
function removePlaceholderText() {
  
  var placeholderSupport = "placeholder" in document.createElement("input");
 
  //if(!placeholderSupport){
 
  	$('[placeholder]').focus(function() {
  	  console.log("focus");
  	  var input = $(this);
  	  if (input.val() == input.attr('placeholder')) {
  		input.val('');
  		input.removeClass('placeholder');
  	  }
  	}).blur(function() {
  	  console.log("blur");
  	  var input = $(this);
  	  if (input.val() == '' || input.val() == input.attr('placeholder')) {
  		input.addClass('placeholder');
  		input.val(input.attr('placeholder'));
  	  }
  	}).blur();
  //}
 
}

// Functions for error handling library - jquery validate
//
// Notes: Test US zip codes
jQuery.validator.addMethod("zipcode", function(zip) {
// matches US ZIP code
// allow either five digits or nine digits with an optional '-' between
zip = zip.replace(/^\s+/, "");
zip = zip.replace(/\s+$/, "");

if(zip.length == 0) {
return true;
}

if(zip.match(/^\d{5}([- ]?\d{4})?$/)) {
return true;
}
return false;
}, "Please specify a valid US ZIP code");

// Notes: Test for US phone number
jQuery.validator.addMethod("phoneUS", function(phone_number, element) {
    phone_number = phone_number.replace(/\s+/g, ""); 
	return this.optional(element) || phone_number.length > 9 &&
		phone_number.match(/^(1-?)?(\([2-9]\d{2}\)|[2-9]\d{2})-?[2-9]\d{2}-?\d{4}$/);
}, "Please specify a valid phone number");

//Notes: Test for post office box in address bar, or just address
jQuery.validator.addMethod("poBox", function(value, element) {
  var pattern1 = /(^p\.?\s?o\.?\s?b\.?(ox|in)?(\s|[0-9])|post\soffice\s?b\.?(ox|in))/i; //check if po box
  var pattern2 = /(^p\.?\s?o\.?\s?b\.?(ox|in)?(\s[0-9])|^post\soffice\s?b\.?(ox|in)?(\s[0-9]))/i;//check if po box with # (for legit po box)
  
  if( value.match(pattern1) ){ //check for po box 
    if( value.match(pattern2) ){ // check for po box w/a number for legit po box
    return true;	 
    }
    return false;
  } else { //if no po box match assume its a regular address
    if(value.length > 6){
      return true;
    }
  }
  return false;
}, "Please specify a valid address or P.O. Box");

//Notes: Test for post office box in address bar, or just address
jQuery.validator.addMethod("birthdate", function(value, element) {
  if(_birthdateVerify) return true;
  return false;
}, "Please specify a valid birthdate");


//checking credit card field for whitespace, this can be used for any fields to
jQuery.validator.addMethod("nowhitespace", function(value, element) {
    return this.optional(element) || /^\S+$/i.test(value);
}, "<p>No white space please</p>");


jQuery.validator.addMethod("validcreditcard", function(value, element) {
  var pattern1 = /(^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\d{3})\d{11})$)/i;
  if( value.match(pattern1) ) return true;
  return false;
}, "<p>Your Credit Card Number is not valid. Please check it and try again.</p>");

jQuery.validator.addMethod("validexpiry", function(value, element) {
  if(_creditExpiryVerify) return true;
  return false;
}, "<p>Your Credit Card Expiry Date is not valid. Please check it and try again.</p>");

jQuery.validator.addMethod("validexpiryautobill", function(value, element) {
  if(_creditExpiryVerifyAutoBill) return true;
  return false;
}, "<p>Your Credit Card Expiry Date is not valid. Please check it and try again.</p>");

// FUNCTION: To support testing when window is ready resizing
var waitForFinalEvent = (function () {
  var timers = {};
  return function (callback, ms, uniqueId) {
    if (!uniqueId) {
      uniqueId = "Don't call this twice without a uniqueId";
    }
    if (timers[uniqueId]) {
      clearTimeout (timers[uniqueId]);
    }
    timers[uniqueId] = setTimeout(callback, ms);
  };
})();

// Function to fix placeholder value in inputs not working in IE9 and lower
function placeHolderFix(element){
        var originalValue;
        var is_chrome = window.chrome;
        if(is_chrome){
                $(element).val("");
        }

    if(navigator.appVersion.indexOf("MSIE") !== -1){
            originalValue = $(element).val();
      }else{
            originalValue = $(element).attr('placeholder');
    }


    var placeAttr = $(element).attr('placeholder');
    $(element).focus(function() {
                    $(element).attr('placeholder', '');

                    if(navigator.appVersion.indexOf("MSIE") !== -1 && $(element).val() == originalValue){
                            $(element).val(""); 
                    }
            });
    $(element).blur(function(){
            $(element).attr('placeholder', placeAttr);
            if(navigator.appVersion.indexOf("MSIE") !== -1 && $(element).val() == ""){
                    $(element).attr('value', originalValue);
            }
    });
} 

// FUNCTION: toggleDrawer()
// NOTES: Main functionality to open and close drawers
//
function toggleDrawer(val) {
	// if dropdown is open, close it
	$(".results-dropdown").hide();
	// Set Globals first
	var locationDrawer = document.getElementById('drawer-location');
	var loginDrawer = document.getElementById('drawer-log-in');
	var cartDrawer = document.getElementById('drawer-cart');
	var searchDrawer = document.getElementById('drawer-search');
		
	if(val=="location")
	{
		loginDrawer.style.height = 0;
		cartDrawer.style.height = 0;
		searchDrawer.style.height = 0;
		if (locationDrawer.clientHeight) {
			locationDrawer.style.height = 0;
		} else {
			var wrapper = $("#drawer-location .form-container").height();
			wrapper = 200;
			locationDrawer.style.height = wrapper + "px";
		}
	}
	else if(val=="login")
	{
		locationDrawer.style.height = 0;
		cartDrawer.style.height = 0;
		searchDrawer.style.height = 0;		
		if (loginDrawer.clientHeight) {
			loginDrawer.style.height = 0;
		} else {
			var wrapper = $("#drawer-log-in .form-container").height();
			wrapper = 300;
			loginDrawer.style.height = wrapper + "px";
		}		
	}	
	else if(val=="cart")
	{
		locationDrawer.style.height = 0;
		loginDrawer.style.height = 0;
		searchDrawer.style.height = 0;		
		if (cartDrawer.clientHeight) {
			$("#drawer-cart .drawer-wrapper").slideUp(400);
			cartDrawer.style.height = 0;			
		} else {
			$("#drawer-cart .drawer-wrapper").show();
			//var wrapper = $("#drawer-cart .drawer-wrapper").height() + 40; // add some extra padding for now
			//alert(wrapper);
			//cartDrawer.style.height = wrapper + "px";
			cartDrawer.style.height = "auto";
			$("#drawer-cart .drawer-wrapper").hide();
			$("#drawer-cart .accordion-footer .columns").css({"height":"auto"});
			$("#drawer-cart .drawer-wrapper").slideDown(400);
		}		
	}
	else if(val=="search")
	{
		locationDrawer.style.height = 0;
		loginDrawer.style.height = 0;
		cartDrawer.style.height = 0;
		if (searchDrawer.clientHeight) {
			searchDrawer.style.height = 0;
		} else {
			var wrapper = $("#drawer-search .drawer-wrapper").height();
			$("#drawer-search").css("height", wrapper);
		}		
	}		
	else
	{
		locationDrawer.style.height = 0;
		loginDrawer.style.height = 0;
		cartDrawer.style.height = 0;
		searchDrawer.style.height = 0;		
	}
}

// FUNCTION: initDrawers()
// NOTES: This function opens/closes/clears all top navigation drawers for desktop version of site	
//
function openLocationDrawer()
{
	if($("body").hasClass("mobile"))
	{
		mobileLocator();
	}
	else 
	{
		toggleDrawer("location");
		$("#drawer-location").toggleClass("open");
		$("#drawer-location").toggleClass("closed");
		$("#drawer-location input:not([type=hidden]):first").focus();
		$("#li-log-in a, #li-cart a#cart-img, #li-search a").removeClass("open");
		$("#li-location a").toggleClass("open");
		$("#drawer-indicator").removeClass();
		$("#drawer-utility-indicator").removeClass("dui-log-in").toggleClass("dui-location");
		$(".drawer-utility").css("border-top","0");
		$("#drawer-location").css("border-top","1px solid #484848");	
	}
}

function openMyAccountDrawer()
{
	toggleDrawer("login");
	$("#drawer-log-in").toggleClass("open");
	$("#drawer-log-in").toggleClass("closed");
	$("#drawer-log-in input:not([type=hidden]):first").focus();
	$("#li-location a, #li-cart a#cart-img, #li-search a").removeClass("open");
	$("#li-log-in a").toggleClass("open");
	$("#drawer-indicator").removeClass();
	$("#drawer-utility-indicator").removeClass("dui-location").toggleClass("dui-log-in");
	$(".drawer-utility").css("border-top","0");
	$("#drawer-log-in").css("border-top","1px solid #484848");		
}

function openCartDrawer()
{
	toggleDrawer("cart");
	$("#drawer-cart").toggleClass("open");
	$("#drawer-cart").toggleClass("closed");
	$("#li-location a, #li-log-in a, #li-search a").removeClass("open");
	$("#li-cart a#cart-img").toggleClass("open");
	$("#drawer-utility-indicator").removeClass();			
	$("#drawer-indicator").removeClass("di-search").toggleClass("di-cart");	
}

function openSearchDrawer()
{
	toggleDrawer("search");
	$("#drawer-search").toggleClass("open");
	$("#drawer-search").toggleClass("closed");
	$("#drawer-search input:not([type=hidden]):first").focus();
	$("#li-location a, #li-log-in a, #li-cart a#cart-img").removeClass("open");
	$("#li-search a").toggleClass("open");
	$("#drawer-utility-indicator").removeClass();
	$("#drawer-indicator").removeClass("di-cart").toggleClass("di-search");	
}

function initDrawers()
{
	// Utility Nav Drawers
	$("#li-location a, .close-location, .alert-box a:not(.no-drawer), .open-zip-drawer").on('click', function(){
		openLocationDrawer();
	});

	$("#li-log-in a, .close-log-in, #li-log-out a").on('click', function(){
		openMyAccountDrawer();
	});
		
	// Main Nav Drawers 
	$("#li-cart a, .close-cart").on('click', function(){
		openCartDrawer();
	});
	
	$("#li-search a, .close-search").on('click', function(){		
		openSearchDrawer();
	});	
	
	$("li.cart-icon").on('click', function(){
		$("#drawer-cart").show();
		toggleDrawer("cart");
		$("#drawer-cart").toggleClass("open");
		$("#drawer-cart").toggleClass("closed");
	});		
	
	// Function added specific to ipad showing cursor focus after drawer closes
	$(".close-drawer").on('click', function(){
		$("#drawer-log-in input:not([type=hidden]):first").focus();
		$("#drawer-log-in input:not([type=hidden]):first").blur();		
	});	
	
	// Function for ie9 and lower to make placeholder attribute work
	var emailSignup = $("#email-sign-up");
    placeHolderFix(emailSignup);
	
}	

// FUNCTION: swiperOptions()
// NOTES: Used for Phones and Plans in small screen view
//
function swiperOptions() {
  // On small screens, the Phones section turns into a 1 phone touch slider
	$('.swiper-container').each(function(){

  // Accessories Swiper Shows Pagination for items
	if($(this).attr("id")=="accessories-listing-swiper") {
    $(this).swiper({
      mode:'horizontal',
      slidesPerView: 1,
      centeredSlides: true,
      watchActiveIndex: true,
      initialSlide: 0,
      paginationClickable: true,
      pagination: '.pagination',
      onSlideChangeEnd : function(e) {
        addClasses(e);
      }
    });		
	}	else {
    $(this).swiper({
      mode:'horizontal',
      //loop: true, // This was messing up on Chrome iOS6
      slidesPerView: 1,
      centeredSlides: true,
      watchActiveIndex: true,
      initialSlide: 0,
       onSlideChangeEnd : function(e) {
         addClasses(e);
       }
    });		
	}

      
    var $swipers = $(this);

    $swipers.siblings('.prev').click(function(e){
     e.preventDefault();
     $swipers.data('swiper').swipePrev();
     addClasses(e);
    });
    $swipers.siblings('.next').click(function(e){
     e.preventDefault();
     $swipers.data('swiper').swipeNext();
     addClasses(e);
    });
    
    
    function addClasses(e) {
      // Count # of slides
      var totalSlides = $swipers.data('swiper').slides.length;
      var activeIndex = e.activeIndex;
      
      // Add Disabled class to first & last slides when you're on them
      if (activeIndex == 0) {
        $swipers.siblings('.prev').addClass("disabled");
      }
      
      else if (activeIndex == totalSlides - 1) {
        //console.log("last slide!");
        $swipers.siblings('.next').addClass("disabled");
      }
      else {
        //console.log("in the middle");
        $swipers.siblings('.next').removeClass("disabled");
        $swipers.siblings('.prev').removeClass("disabled");
      }
    }
    
    
  });
}

// FUNCTION: mobileLocator()
// NOTES: When in mobile view, click on top Zip code will open drawer to change zip code
//
function mobileLocator()
{
	var mobileLocationDrawer = document.getElementById('drawer-mobile-location');	
	if (mobileLocationDrawer.clientHeight) {
		mobileLocationDrawer.style.height = 0;
	} else {
		var wrapper = $("#drawer-mobile-location .form-container").height();
		wrapper = 420;
		mobileLocationDrawer.style.height = wrapper + "px";
	}
}

// FUNCTION: showZipResults()
// NOTES: Slides down zip results in Location Drawer
//
function showZipResults()
{
	// reset dropdown first
	$("#zip-results").css({"height":"0"});
	$("#drawer-location").css({"padding-bottom": "40px"});
	
	var zipResultsDropdown = document.getElementById('zip-results');	
	var wrapper = $("#zip-results table").height();
	var currentPadding = $("#drawer-location").height();
	var newHeight = currentPadding + wrapper;
	$("#drawer-location").css({"padding-bottom": newHeight})
	zipResultsDropdown.style.height = wrapper + "px";
}

// Mobile accordion for expand/collapse items where we can't use "section"
// TO DO - make this more general so it can be used elsewhere
function mobileAccordion() {  
  $('.mobile-accordion .mobile-trigger').click(function(){
    var fieldsetWrapper = $(this).parent('fieldset');
    if( !fieldsetWrapper.hasClass('current') ){
     $(this).closest('.mobile-accordion').find('.current').removeClass('current');
    }
    fieldsetWrapper.toggleClass('current');
    return false;
  });
}

// FUNCTION: initLayout
// NOTES: This function accounts for resizing of page to run all pertinent function both ONLOAD and ONRESIZE
//	
function initLayout()
{		
	// capture width	
	var w = $(document).width();
	var url = location.pathname;

	if(w<769)
	{
		// Set up Swiper options for small screen
		//swiperOptions();
		
		//$("body").addClass("mobile");
			
		// This changes footer sitemap of 5 columns to an accordion on small screen
		//$("#footer-sitemap").addClass("section-container accordion");
		
		// Make Filter Dropdown into an accordion as well - couldn't get forms & sections to play nice together
		//$('#phone-filters-dropdown').addClass('mobile-accordion');
		
		// Phone Listing Page - add expanded class to topbar filters
		//$("#section-filters .top-bar").addClass("expanded");
		
		// Hide Phone Compare Bar on Mobile
		//$("#compare-bar").css("visibility", "hidden");
		
		// Hide Swiper "Previous" Arrows since you'll be on first slide on page load
		//$('.three-callouts a.prev').addClass("disabled");
			
		// Swap out text of My Account button to Log In on smaller screens
		//$("#li-log-in a").text("Log In");
		
		// Close all drawers if open
		//$(".drawer, .drawer-utility").hide();
		//$("#drawer-indicator").removeClass();
		//$("#drawer-utility-indicator").removeClass();
			
		// $(".open-zip, .alert-box a:not(.no-drawer)").on('click', function(){
		// 	mobileLocator();
		// });	
		
	}	
	else
	{									
	  	$("body").removeClass("mobile");
	  
		// Closing side menu and removing styles that express them							
		$("#nav-utility").removeClass("mobile-utility");
		$("#drawer-mobile-location").css({"height":"0"});
		$('#inner-wrap').css({'right': 'auto', 'left': 'auto'});
		
		// If present, swap out text of Login button to My Account on larger screens
		$("#li-log-in a").text("My Account");	
		
		$('#phone-filters-dropdown').removeClass('mobile-accordion'); 
		
			
		// This changes back to footer sitemap
		$("#footer-sitemap").removeClass("section-container accordion");	
	}			
}

function initClickToChatModal()
{
	$('#chat-button #chat-close').click(function() {
		$('#cricket-modal-chat').hide();
		$('#chat-button').hide();
		$('#chat-button').css('visibility', 'hidden');
	});
}

// This is specific for mobile devices when change orientation
$(window).bind('orientationchange', function(event){
	//if(  $("#drawer-location").hasClass("open") || $("#header .mobile-location").css('display') == "block" )
	//{
		window.location.reload();
	//}	
});

// Load these items on load of the page	
$(function(){

	// Init Drawers for location, login, cart, and search
	initDrawers();
		
	// Load selected functions on page load
	initLayout();
	
    //Placeholder fix for email input field in footer
    removePlaceholderText();
    
	// Click to chat modal functions
	initClickToChatModal();

	// Load show/hide functionality for Cart drawer "accordions"
	$('#drawer-cart .accordion section p.title').click(function(e) {
		e.preventDefault();
		//document.getElementById('drawer-cart').style.height = 'auto';
		if($(this).parent().hasClass('active')) {
			$(this).parent().removeClass('active');
		} else {
			$(this).parent().addClass('active');
		}
	});	
		
  	mobileAccordion();
					
	// Reload selected function on resize of page
	// $(window).resize(function(){
	// 	initLayout();			
	// });

	$('#chat-button .close-chat-button').click(function() {
		$('#chat-button').hide();
		
	});
	
	$('#chat-button #chat-button-image').click(function() {
		$('#chat-button').hide();
		$('#chat-container').show();
		chatG.startChat();
	});
	
	
	$('#chat-container-header-close').click(function() {
		$('#chat-container').hide();
	});
	
	$('#chat-container-header-title').click(function() {
		if ($('#chat-container').css('bottom') != '0px') {
			$('#chat-container-header-minimize').click();
		}
	});
	
	$('#chat-container-header-minimize').click(function() {
		if ($('#chat-container').css('bottom') == '0px') {
			$('#chat-container').css('bottom', '-235px');
		} else {
			$('#chat-container').css('bottom', '0px');
		}
	})
	
	// Show footer disclaimer only on homepage (hidden in CSS)
	if ($("#constructor").hasClass("homepage")) {
	  $("#disclaimer-container p").show();
	}

  // IE7 Hacks for Drawers, add Closed class so we can style both states
  $("#drawer-cart").addClass("closed");
  $("#drawer-search").addClass("closed");
  $("#drawer-log-in").addClass("closed");
  $("#drawer-location").addClass("closed");
	
	
	// IE8 & Lower Specific Hacks - Force IE8 to use Tabs instead of Accordions when Section plugin is set to "auto"
  if ($('html').hasClass('lt-ie9')) { 
    if($('.section-container').hasClass("auto")) {
      $(".section-container").removeClass('auto').addClass('tabs');
      $(".section-container").attr("data-section", "tabs");
      // Reflow so tabs in IE8 look like they are supposed to
      $('.section-container').foundation('section','reflow');
    }
  }
	
	
	// Toggle 'side menu' in mobile view	
	$('#open-side-menu a').on('click', function(e) {
		e.preventDefault();
	    $('#inner-wrap').animate({ right: '90%' }, 300);
		$("#nav-utility").addClass("mobile-utility");
		$(".indicator-black-west").fadeIn();
		$(".account-icon a").toggleClass("open");
	});
	$('.side-menu-close a').on('click', function(e) {
		e.preventDefault();
	    $('#inner-wrap').animate({ right: '0' }, 300);
		$(".indicator-black-west").hide();
		$(".account-icon a").toggleClass("open");
	});

	// Toggle Cart in mobile view	
	// $('.cart-icon a').on('click', function(e) {
	// 	e.preventDefault();
	//     $('#inner-wrap').animate({ right: '90%' }, 300);
	// 	$("#nav-utility").addClass("mobile-utility");
	// 	$(".indicator-black-west").fadeIn();
	// 	$(".account-icon a").toggleClass("open");
	// });
	// $('.side-menu-close a').on('click', function(e) {
	// 	e.preventDefault();
	//     $('#inner-wrap').animate({ right: '0' }, 300);
	// 	$(".indicator-black-west").hide();
	// 	$(".account-icon a").toggleClass("open");
	// });

// CLIENT SIDE VALIATIONS FUNCTIONS //

	// Client Side Validation for Location drawer - Zip Code lookup	
	$('#btn-check-zip').click(function() {		
		//$("#btn-check-zip").addClass('disabled-button');
		$("#checkZipForm").submit();
	});		
	$('#checkZipForm').validate({			
   		rules: { 																					
			"zip" : {
				required: true,
				rangelength: [5, 5]
			}																						
   		},
   		messages: {						
			"zip" : {
				required: '<p>Please enter a valid five-digit zip code.</p>',
				rangelength: '<p>Please enter a valid five-digit zip code.</p>'
			}									
   		},
		errorPlacement: function(error, element) 
		{	
			//$("#btn-check-zip").removeClass('disabled-button');
			$(".zip-results").hide();
			$(".errors-location").html("");	
			error.appendTo(".errors-location");	
			$(".errors-location").show();	
			
		},
		//onfocusout: false,
      	onkeyup: false,			
		submitHandler: function(form) {
			//$("#btn-check-zip").removeClass('disabled-button');
			$(".errors-location").html("");
			$(".zip-results").show();
			
		 }			
	});	

	// Client Side Validation for Mobile Location drawer - Zip Code lookup
	$('#btn-mobile-check-zip').click(function() {			
		$("#checkMobileZipForm").submit();
	});		
	$('#checkMobileZipForm').validate({			
   		rules: { 																					
			"zipchange" : {
				required: true,
				zipcode: true
			}																						
   		},
   		messages: {						
			"zipchange" : '<p>We currently do not activate new Cricket accounts in your area. <a href="#">Sign up</a> to get notifications on availability.</p>'										
   		},
		errorPlacement: function(error, element) 
		{	
			$(".mobile-zip-results").hide();
			$(".errors-mobile-location").html("");	
			error.appendTo(".errors-mobile-location");	
			$(".errors-mobile-location").show();	
		},
		//onfocusout: false,
      	onkeyup: false,			
		submitHandler: function(form) {
			$(".errors-mobile-location").html("");
			$(".mobile-zip-results").show();
		 }			
	});			

	// Client Side Validation for Login drawer - My Account 
	$('#btn-login').on('click', function() {			
		$("#checkLoginForm").submit();
	});		
	$('#checkLoginForm').validate({			
   		rules: { 																					
			"phone" : {
				required: true,
				phoneUS: true
			},
			"password" : {
				required: true
			}																					
   		},
   		messages: {						
			"phone" : '<p>You could not be authenticated. Please check your phone number and password.</p>',
			"password" : '<p>You could not be authenticated. Please check your phone number and password.</p>'										
   		},
		errorPlacement: function(error, element) 
		{	
			$(".mobile-zip-results").hide();
			$(".errors-login").html("");	
			error.appendTo(".errors-login");	
			$(".errors-login").show();	
		},
		//onfocusout: false,
      	onkeyup: false,				
		submitHandler: function(form) {
			$(".errors-login").html("");
			toggleDrawer("login");
			$("#drawer-log-in input:not([type=hidden]):first").focus();
			$("#li-location a, #li-cart a#cart-img, #li-search a").removeClass("open");
			$("#li-log-in a").toggleClass("open");
			$("#drawer-indicator").removeClass();
			$("#drawer-utility-indicator").removeClass("dui-location").toggleClass("dui-log-in");
			$(".drawer-utility").css("border-top","0");
			$("#drawer-log-in").css("border-top","1px solid #484848");	

			$("#mobileLoginForm").hide();
			$("#li-log-in").hide();
			$("#li-log-out, #li-username").css({"display":"inline-block"});
		 }			
	});	
	
	// Client Side Validation for Mobile View login with phone and password
	$('#btn-mobile-login').on('click', function() {			
		$("#mobileLoginForm").submit();
	});		
	$('#mobileLoginForm').validate({			
   		rules: { 																					
			"mobile-phone" : {
				required: true,
				phoneUS: true
			},
			"mobile-password" : {
				required: true
			}																					
   		},
   		messages: {						
			"mobile-phone" : '<p>You could not be authenticated. Please check your phone number and password.</p>',
			"mobile-password" : '<p>You could not be authenticated. Please check your phone number and password.</p>'										
   		},
		errorPlacement: function(error, element) 
		{	
			$(".errors-mobile-login").html("");	
			error.appendTo(".errors-mobile-login");	
			$(".errors-mobile-login").show();	
		},
		//onfocusout: false,
      	onkeyup: false,				
		submitHandler: function(form) {
			$(".errors-mobile-login").html("");
			$("#mobileLoginForm").hide();
			//$("#li-location").hide();
			$("#li-log-in").hide();
			$("#li-log-out, #li-username").css({"display":"inline-block"});
			$(".mobile-utility h3").css("padding-bottom","50px");
		 }			
	});	

	// Client Side Validation for Email Newsletter field in Footer
	$('#btn-email-signup').on('click', function(e) {			
		e.preventDefault();
		$("#emailSignupForm").submit();
	});		
	$('#emailSignupForm').validate({			
   		rules: { 																					
			"email-sign-up" : {
				email: true,
				required: true
			}																						
   		},
   		messages: {						
			"email-sign-up" : '<p>Please enter a valid email address.</p>'										
   		},
		errorPlacement: function(error, element) 
		{	
			$(".errors-email-signup").html("");	
			error.appendTo(".errors-email-signup");	
			$(".errors-email-signup").show();	
		},
		//onfocusout: false,
      	onkeyup: false,		
		submitHandler: function(form) {
			//$(".errors-email-signup").html("");
			$("<p>You haved signed up for hot deals, thank you.</p>").appendTo(".errors-email-signup");
			$(".errors-email-signup").show();	
		 }			
	});	

	// Hide swiper IE8 and lower
	if( $("html").hasClass("lt-ie9") ) { 
		$(".carousel-prev, .carousel-next, .plans-prev, .plans-next").hide();
	} 
			
	// Home page desktop slider for Phones
	var homePagePhones = $('#homepage-phones').swiper({
		mode:'horizontal',
		slidesPerView: 3,
		visibilityFullFit: true,
		watchActiveIndex: true,
		initialSlide: 0,
		onImagesReady : function(e)
		{
			addClassesPhones(e)
		},		
		onSlideChangeEnd : function(e) {
			addClassesPhones(e)
		}		
	});	
	$('.carousel-prev').on('click', function(e){
		e.preventDefault();
		homePagePhones.swipePrev();
		addClassesPhones(e);		
	});
	$('.carousel-next').on('click', function(e){
		e.preventDefault();
		homePagePhones.swipeNext();
		addClassesPhones(e)
	}); 	

	function addClassesPhones(e) {
		// Count # of slides
		var totalSlides = $('#homepage-phones .swiper-slide').length;
		var activeIndex = e.activeIndex;

		if(totalSlides > 3)
		{
			// Add Disabled class to first & last slides when you're on them
			if (activeIndex == 0) 
			{
				//console.log("first slide!");
				$('.carousel-prev').addClass("disabled");
			}
			else if (activeIndex == totalSlides - 3) 
			{
				//console.log("last slide!");
				$('.carousel-next').addClass("disabled");
			}
			else 
			{
				//console.log("in the middle");
				$('.carousel-next').removeClass("disabled");
				$('.carousel-prev').removeClass("disabled");
			}
		} 
		else 
		{
			$('.carousel-next').addClass("disabled");
			$('.carousel-prev').addClass("disabled");
		}
	}


	// Home page desktop slider for Plans
	var homePagePlans = $('#homepage-plans').swiper({
		mode:'horizontal',
		slidesPerView: 3,
		visibilityFullFit: true,
		watchActiveIndex: true,
		initialSlide: 0,
		onImagesReady : function(e)
		{
			addClassesPlans(e)
		},		
		onSlideChangeEnd : function(e) {
			addClassesPlans(e)
		}		
	});	
	$('.plans-prev').on('click', function(e){
		e.preventDefault();
		homePagePlans.swipePrev();
		addClassesPlans(e);		
	});
	$('.plans-next').on('click', function(e){
		e.preventDefault();
		homePagePlans.swipeNext();
		addClassesPlans(e)
	}); 	

	function addClassesPlans(e) {
		// Count # of slides
		var totalSlides = $('#homepage-plans .swiper-slide').length;
		var activeIndex = e.activeIndex;

		if(totalSlides > 3)
		{
			// Add Disabled class to first & last slides when you're on them
			if (activeIndex == 0) 
			{
				//console.log("first slide!");
				$('.plans-prev').addClass("disabled");
			}
			else if (activeIndex == totalSlides - 3) 
			{
				//console.log("last slide!");
				$('.plans-next').addClass("disabled");
			}
			else 
			{
				//console.log("in the middle");
				$('.plans-next').removeClass("disabled");
				$('.plans-prev').removeClass("disabled");
			}
		} 
		else 
		{
			$('.plans-next').addClass("disabled");
			$('.plans-prev').addClass("disabled");
		}
	}
		
}); 



//live chat js....

var lpChatConfig = {
	lpProtocol : 'http',
	lpNumber : '4968591',
	apiKey : '8f4e6cb33f0a4dd19c263d33f260235f',
	lpServer : 'dev.liveperson.net',
	jsApiSrcDomain : 'dev.liveperson.net',
	skill: 'sales-english',
	onLoad : function() {
		//after the button is created the requestConfig is sent.
		window.lpc = new lpChat();
	},
	onLine : 'myLineHandler',  
	onError : 'myErrorHandler',  
	onAvailability: 'myOnAvailability'  
};

$(function() {
   //chatGui is a JavaScript object which wraps the connection with the chat API
    window.chatG = new chatGui({
        divID : 'chat-container-wrapper' //the div which contains the chat
        ,width: 311 //chat window default width
        ,height: 231 //chat window default height
       //visitor name can be changed dynamically according to information obtained
       //about the current visitor
        ,visitor: 'Me'
        ,autoResumechat : true //resumes chat automatically
        ,resizable : false //option to resize chat window
        ,draggable: false //option to drag chat window
        ,showTime : false //show the time of each message
       //remembers the position where the chat was last opened
        ,rememberPosition : true

       
    },
    lpChatConfig
	);
    chatG.init();	
});


lpChatConfig.lpAddScript = function(src, ignore) {var c = lpChatConfig;if(typeof(c.lpProtocol)=='undefined'){c.lpProtocol = (document.location.toString().indexOf("https:")==0) ? "https" : "http";}if (typeof(src) == 'undefined' || typeof(src) == 'object') {src = c.lpChatSrc ? c.lpChatSrc : '/hcp/html/lpChatAPI.js';};if (src.indexOf('http') != 0) {src = c.lpProtocol + "://" + c.lpServer + src + '?site=' + c.lpNumber;} else {if (src.indexOf('site=') < 0) {if (src.indexOf('?') < 0)src = src + '?'; else src = src + '&';src = src + 'site=' + c.lpNumber;}};var s = document.createElement('script');s.setAttribute('type', 'text/javascript');s.setAttribute('charset', 'iso-8859-1');s.setAttribute('src', src);document.getElementsByTagName('head').item(0).appendChild(s);}
lpChatConfig.lpAddScript();


// END of document.ready load


// Cart Specific JS
$(function(){

	if($("#constructor").hasClass("cart-static"))
	{
		$("#li-cart a, .close-cart").unbind("click");
		$("#drawer-cart").css({"height":"auto"}).toggleClass("open");
		$("#drawer-cart").toggleClass("open");
		$("#li-location a, #li-log-in a, #li-search a").removeClass("open");
		$("#li-cart a#cart-img").toggleClass("open").css({"pointer":"crosshair"});
		$("#drawer-utility-indicator").removeClass();			
		$("#drawer-indicator").removeClass("di-search").toggleClass("di-cart");		
		$(".close-cart").css("visibility","hidden");
	}

	if($(document).width() > 767)
	{
		// This function tests the height of the details and summary columns and sets both heights to the highest so middle grey pixel stretches
		$("#drawer-cart section .content > .row").each(function(){  

			// Since some accordions are closed, need to open and get height of those as well
			if($(this).parent().parent().hasClass("active"))
			{
				var detailsHeight = $(".details-container", this).height();
				var summaryHeight = $(".summary-container", this).height();		
			}
			else
			{
				$(this).parent().parent().addClass("active");
				var detailsHeight = $(".details-container", this).height();
				var summaryHeight = $(".summary-container", this).height();
				$(this).parent().parent().removeClass("active");
				// alert("details height is " + detailsHeight);
				// alert("sumamry height is " + summaryHeight);						
			}
		
			// Now test to see if details is higher, and set summary height accordingly
			if(detailsHeight > summaryHeight)
			{
				$(".summary-container", this).height(detailsHeight);
			} 
		
		}); 
	}

	// For Quanity input field, test to see what value and apply text to the A href title "quantity-button"
	// And also add a class to the button for javascript use
	$('#drawer-cart .quantity').keyup(function() {		
		var buttonText = $(this).parent().find("a.quantity-button");		
 		if($(this).val()=="0")
		{
			buttonText.removeClass("btn-remove btn-update").addClass("btn-remove").text("Remove");
		}		
 		else if($(this).val() > "1")
		{
			buttonText.removeClass("btn-remove btn-update").addClass("btn-update").text("Update");
		}
		else {
			buttonText.removeClass("btn-remove btn-update").text("");
		}			
	});

	// Also need to test initial document ready to apply proper text
	$('#drawer-cart .quantity').each(function() {		
		var buttonText = $(this).parent().find("a.quantity-button");
 		if($(this).val()=="0")
		{
			buttonText.removeClass("btn-remove btn-update").addClass("btn-remove").text("Remove");
		}		
 		else if($(this).val() > "1")
		{
			buttonText.removeClass("btn-remove btn-update").addClass("btn-update").text("Update");
		}
		else {
			buttonText.removeClass("btn-remove btn-update").text("");
		}			
	});
	
});	


// Search Specific JS
$(function(){
  
  // Add active state when we're in the search section - probably only for our prototype, won't
  // be needed in production version.
  if(window.location.href.indexOf("results") > -1) {
      $("#li-search a").addClass("open");
   }
   
   if(window.location.href.indexOf("invalid") > -1) {
       $("#li-search a").addClass("open");
    }

// FUNCTION: searchInit()
//NOTES: Used for autocomplete in global search box, might not be used in production version depending on implementation
function searchInit() {
  
  // Adding some classes to wrapper elements for prototype only, once design has been decided on this will no
  // longer be needed (only used to show different options to client)
  var url = location.pathname;
  // 3 & 4 are no-zip versions of search results
  if (url.indexOf('predictive-search-3') > -1) {
      $("#outer-wrap").addClass("predictive-search-nozip");
      $("#li-mobile-search").parent("#nav-main").addClass("predictive-search-nozip");
  // 2 shows a loader and a zip version
  }else if (url.indexOf('predictive-search-2') > -1) {
      $("#outer-wrap").addClass("predictive-search-zip predictive-search-loading");
      $("#li-mobile-search").parent("#nav-main").addClass("predictive-search-zip predictive-search-loading");
  // 4 shows a loader and a no-zip version
  }else if (url.indexOf('predictive-search-4') > -1) {
      $("#outer-wrap").addClass("predictive-search-nozip predictive-search-loading");
      $("#li-mobile-search").parent("#nav-main").addClass("predictive-search-nozip predictive-search-loading");
  // otherwise just show normal zip version without loader everywhere else
  } else {
    $("#outer-wrap").addClass("predictive-search-zip");
    $("#li-mobile-search").parent("#nav-main").addClass("predictive-search-zip");
  }
  
  // We're only showing the loading icon in only 2 of the templates, remove it from all others for now
  if ($("#outer-wrap, #li-mobile-search").hasClass("predictive-search-loading")) {
    $(".acLoading").hide();
  } else {
    $(".acLoading").remove();
  }
  
  

  
}

  //  Init
  //searchInit();
  
	$(".acLoading").hide();
	
	// $(".search-new").keyup(function(){
	// 	
	// 	// Note to TechM: Capture what is typed in search box in this variable if you need it.
	// 	var searchTerm = $(this).val();
	// 				
	// 	// After three keystrokes, show the results dropdown
	// 	if($(this).val().length > 2)
	// 	{
	// 		$(".results-dropdown").show();
	// 	}
	// 	else {
	// 		$(".results-dropdown").hide();
	// 		$(".acLoading").hide();
	// 	}		
	// });

	$(".search-new").keyup(function(){// For global search box
             
              // Note to TechM: Capture what is typed in search box in this variable if you need it.
              var searchTerm = $(this).val();
                                 
              // After three keystrokes, show the results dropdown
              if($(this).val().length > 2)
              {
                     populateTypeAheadResults();
                     //$(".results-dropdown").show();
              }
              else {
                     $(".results-dropdown").hide();
                     $(".acLoading").hide();
                     document.getElementById('typeAheadSearchResultsUL').innerHTML="";
              }            
       });
      
       $("#search-full").keyup(function(){// For search again bar on search results page
             
              // Note to TechM: Capture what is typed in search box in this variable if you need it.
              var searchTerm = $(this).val();
                                 
              // After three keystrokes, show the results dropdown
              if($(this).val().length > 2)
              {
                     populateTypeAheadResultsAgain();
                     //$(".results-dropdown").show();
              }
              else {
                     $(".results-dropdown").hide();
                     $(".acLoading").hide();
                     document.getElementById('typeAheadSearchResultsUL').innerHTML="";
              }            
       });
});
