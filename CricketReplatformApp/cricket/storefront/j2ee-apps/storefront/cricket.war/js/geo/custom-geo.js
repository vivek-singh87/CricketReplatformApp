$(function(){
	var width = $(document).width();
	var mobileLoc=$("#location").val();
	var locationInRepository =$("#locationInRepository").val();
	var mobileLocationTried =$("#isMobileLocTried").val();
	var testingOn =$("#testingOn").val();
	var isLocationDefault = $("#isLocationDefault").val();
	var url = document.URL;
	var userAgent = navigator.userAgent;
	var isiPad = false;
	if(userAgent.indexOf("ipad") != -1 || userAgent.indexOf("iPad") != -1 || userAgent.indexOf("IPAD") != -1) {
		isiPad = true;
	}
	if((width < 770 || isiPad == true || isiPad) && (mobileLocationTried == 'false' || mobileLocationTried == false) && url.indexOf("latitude") == -1 && (isLocationDefault == 'true' || isLocationDefault == true)){
		if(geo_position_js.init()){
			geo_position_js.getCurrentPosition(success_callback,error_callback,{enableHighAccuracy:true});
		}
		
	}
});
function success_callback(p){
	//alert("position::::"+p);
	
	var lat ;
	var lon ;
	if(p != null){
		lat = p.coords.latitude.toFixed(2);
		lon = p.coords.longitude.toFixed(2);
	}

	if(lat == null){
		lat = 0;
	}
	if(lon == null){
		lon = 0;
	}
 	if(locationInRepository != "NoLocation" && lat != null && lon != null){
 		var gmapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&sensor=true&callback=zipmap";
 		var zipCode = "";
 		$.ajax({
 		      type : "GET",
 		      dataType : "json",
 		      url : gmapUrl,
 		      success : function(data) {
 		    	  for (var i=0;i<data.results[0].address_components.length;i++) {
 		    		  var component = data.results[0].address_components[i];
 		    		  if(component.types[0] == 'postal_code') {
 		    			  zipCode = component.short_name;
 		    			  var url = window.location.href;
 		    			  url += url.indexOf("?") != -1 ? "&" : "?";
 		    			  url += "latitude="+lat+"&longitude="+lon+"&userDevice=mobile&zipCode="+zipCode;
 		    			  window.location.href = url;
 		    		  }
 		    	  }
 		      }
 		});
	} 
}


function error_callback(p){
	//alert('error='+p.message);
}

//manual change zipcode functionality starts here
var stopExecution = 'false';
var selectedCity = '';
$(function(){
	var options = {
	        beforeSubmit:  showRequest,  // pre-submit callback 
	        success:       showResponse  // post-submit callback
	 };
	$("#changeLocationFormDeskTop").ajaxForm(options);
});
	var geoData;
	function callFnToGetCity(inputElement, event) {  
		if (event.keyCode == 13) {
			getCityForZipCode();							
		}  
    } 
	
function showRequest() {
}

function showResponse() {
	var currentPageUrl = document.URL;
	if(currentPageUrl.indexOf("checkout") != -1){
		window.location.href = $('#rootcontextpath').val();
	} else if(currentPageUrl.indexOf("_DARGS") != -1) {
		var urlParts = currentPageUrl.split('?');
		var queryStringFinal = "";
		if(urlParts.length > 1) {
			var paramPairs = urlParts[1].split('&');
			for(i=0; i<paramPairs.length; i++) {
				var keyValue = paramPairs[i].split("=");
				if(keyValue[0] != '_DARGS'){
					if(i == paramPairs.length - 1) {
						queryStringFinal += keyValue[0] + "=" + keyValue[1];
					} else {
						queryStringFinal += keyValue[0] + "=" + keyValue[1] + "&";
					}
				}
			}
			var finalUrl = urlParts[0] + "?" + queryStringFinal;
			window.location.href = finalUrl;
		} else {
			location.reload();
		}
	} else if (currentPageUrl.indexOf("&Ntt=") != -1) {
		var genSearchQuery = $('#generalSearchQuery').val();
		var contextPath = $('#contextPathSearchQuery').val();
		var searchTerm = $('#searchTerm').val();
		var searchQuery = contextPath + genSearchQuery + "&Ntt=" + searchTerm + "&Nrpp=10000";
		document.location.href = searchQuery;
	} else {		
		location.reload();
	}
}

function modalAlert(value, cityName){
	  var dialogHTML = '<div class="modal"><div class="dialog-content"><h2>Are you sure you want to change your location?</h2><p>If yes, your cart will be emptied and you will have to start again.</p><a href="#" class="button small orange" data-confirm="no">No</a><a href="#" class="button small green" data-confirm="yes">Yes, empty my cart</a></div></div>';
	  $("#outer-wrap").before(dialogHTML);
	  var overlayHTML = '<div class="md-overlay"></div>';
	  $("#outer-wrap").after(overlayHTML);
	  var $this = value;

	  $('.dialog-content a.button').on("click", function(e){
	    e.preventDefault();

	    var confirm = $(this).attr('data-confirm');
	    if(confirm == "yes"){		
		//	$("#clears-cart").submit();
			$('#clearCartItems').val('true');
			$(".modal, .md-overlay").hide();	
			submitChangeLocationForm(cityName);	
		//	$(".modal, .md-overlay").remove();		
	    } else {	
	    document.getElementById('zip').value = ""; 
		$(".zip-results").hide();
		document.getElementById('selectcity2').innerHTML =  "";
		openLocationDrawer();
	    $(".modal, .md-overlay").remove();	 
	    }
	  });
	}
			
function checkCartIsEmpty(cityName){
	var cartItemCount = $("#shoppingCartCount").val();
	if(cartItemCount > 0){
		modalAlert(this,cityName);
		$(this).blur();
	} else{
		submitChangeLocationForm(cityName);
		}
}	
	
function getCityForZipCode() {		
	
	var zipCode = $.trim($('#zip').val());
	var cartItemCount = $("#shoppingCartCount").val();
	 if(zipCode!=null && zipCode!="" && (zipCode.match(/^\d{5}([- ]?\d{4})?$/))) {	
		 $(".zip-results").hide();
		var contextPath = $('#contextPathValue').val();
		var html = "";
		$.ajax({
            url: contextPath + "/common/includes/marketInfoJson.jsp?zipCode=" + zipCode,
            type: "post",
            dataType: "json",
            error:function(){
            	document.getElementById('cityList').innerHTML = "<li>Sorry, we could not determine your City</li>";
            },
            success:function(data){
            	geoData = data;
            	if (data.cityInfos.length == 1) {
            		$(".zip-results").hide();
            		var cityName = data.cityInfos[0].cityName;
					var zipCode = data.cityInfos[0].zipCode;
					var NoCoverage = data.cityInfos[0].NoCoverage;
					$('#noNetworkCoverageDesk').val(false);
					if(NoCoverage!= undefined && (NoCoverage!=null && NoCoverage!="")){							
						$('#noNetworkCoverageDesk').val(true);
						//$(".errors-location").show();
						//$(".errors-location").html("<p>We currently do not activate new Cricket accounts in your area.  <a href=\"#\">Sign up</a> and be the first to know when this changes.</p>");						
					}							
					checkCartIsEmpty(cityName);																	            		
            	} else if(data.cityInfos.length > 1){					
            		$('#noNetworkCoverageDesk').val(false);
            		$(".zip-results").show();
            		document.getElementById('selectcity2').innerHTML = 'Please Select Your City:';							
					html += "<select id='cityListResults2' class='inline-list' name='cityListResults2' onchange='setCity(this.value)'>";						
					html += "<option >" + "Choose a city / state..." + "</option>";
            		for(i=0;i<data.cityInfos.length;i++) {
	                	var city = data.cityInfos[i].cityName;
	                	var state = data.cityInfos[i].state;
	                	html += "<option value=\""+city+"\">" + city + ", " + state +"</option>";
	                }						
		            document.getElementById('cityList').innerHTML = html;						
            	}else{            			
            		//$('#changeLoctionRedirectURL').val(document.URL);
        			$('#changeLocationSubmitDesk').trigger('click');	            		
            	}
            }
            
         });
	  }else{
		$(".errors-location").show();
		$(".errors-location").html("<p>Please enter a valid five-digit zip code.</p>");	
	  }
	}
	
	function submitChangeLocationForm(cityName) {	
		for(i=0;i<geoData.cityInfos.length;i++) {
			if(cityName == geoData.cityInfos[i].cityName) {
				$('#changeLocCityDesk').val(geoData.cityInfos[i].cityName);
				$('#changeLocStateDesk').val(geoData.cityInfos[i].state);
				$('#changeLocCountryDesk').val(geoData.cityInfos[i].country);
				$('#changeLocAreaCodeDesk').val(geoData.cityInfos[i].areaCode);
				$('#changeLocPostalCodeDesk').val(geoData.cityInfos[i].zipCode);
				$('#changeLocLatitudeDesk').val(geoData.cityInfos[i].latitude);
				$('#changeLocLongitudeDesk').val(geoData.cityInfos[i].longitude);
				$('#changeLoctimezoneDesk').val(geoData.cityInfos[i].timezone);
			}
		}
		//$('#changeLoctionRedirectURL').val(document.URL);
		$('#changeLocationSubmitDesk').trigger('click');
				
	}		
	function setCity(city){		
		selectedCity = city;	
	}
	function updateCity(){		
	 checkCartIsEmpty(selectedCity);
	}
	function logoutSubmitDesk(form) {
		    form.submit();
  	}
//manual change zipcode functionality ends here
	
//manual change zipcode functionality for mobile starts here
	function switchLanguage(lang) {
		MP.SrcUrl=decodeURIComponent('mp_js_orgin_url');
		MP.UrlLang='mp_js_current_lang';
		MP.init();
		MP.switchLanguage(MP.UrlLang==lang?'en':lang);
		return false;
	}
	$(function(){
		var options = {
		        beforeSubmit:  showRequestMobile,  // pre-submit callback 
		        success:       showResponseMobile  // post-submit callback
		 };
		$("#changeLocationForm").ajaxForm(options);
	});
		var geoData;
		var selectedCityMobile='';
		function callFnCityForZipCodeMobile(inputElement, event) {  
			if (event.keyCode == 13) {						
					getCityForZipCodeMobile();								
			}  
	    } 
		function callFnMyAccountLoginMobile(inputElement, event) {  
			if (event.keyCode == 13) {						
				document.getElementById('mobileLoginForm').submit();							
			}  
	    } 
		function showRequestMobile() {
		}
		
		function showResponseMobile() {
			var currentPageUrl = document.URL;
			if(currentPageUrl.indexOf("checkout") != -1){
				window.location.href = $('#rootcontextpath').val();
			} else if(currentPageUrl.indexOf("_DARGS") != -1) {
				var urlParts = currentPageUrl.split('?');
				var queryStringFinal = "";
				if(urlParts.length > 1) {
					var paramPairs = urlParts[1].split('&');
					for(i=0; i<paramPairs.length; i++) {
						var keyValue = paramPairs[i].split("=");
						if(keyValue[0] != '_DARGS'){
							if(i == paramPairs.length - 1) {
								queryStringFinal += keyValue[0] + "=" + keyValue[1];
							} else {
								queryStringFinal += keyValue[0] + "=" + keyValue[1] + "&";
							}
						}
					}
					var finalUrl = urlParts[0] + "?" + queryStringFinal;
					window.location.href = finalUrl;
				} else {
					location.reload();
				}
			} else if (currentPageUrl.indexOf("&Ntt=") != -1) {
				var genSearchQuery = $('#generalSearchQuery').val();
				var contextPath = $('#contextPathSearchQuery').val();
				var searchTerm = $('#searchTerm').val();
				var searchQuery = contextPath + genSearchQuery + "&Ntt=" + searchTerm + "&Nrpp=10000";
				document.location.href = searchQuery;
			} else {		
				location.reload();
			}
		}
			
		function modalAlertMobile(value, cityName){
			  var dialogHTML = '<div class="modal"><div class="dialog-content"><h4>Are you sure you want to change your location?</h4><p>If yes, your cart will be emptied and you will have to start again.</p><a href="#" class="button small orange" data-confirm="no">No</a><a href="#" class="button small greenmobile" data-confirm="yes">Yes, empty my cart</a></div></div>';
			  $("#outer-wrap").before(dialogHTML);
			  var overlayHTML = '<div class="md-overlay"></div>';
			  $("#outer-wrap").after(overlayHTML);
			  var $this = value;

			  $('.dialog-content a.button').on("click", function(e){
			    e.preventDefault();

			    var confirm = $(this).attr('data-confirm');
			    if(confirm == "yes"){		
				//	$("#clears-cart").submit();
					$('#clearCartItemsMobile').val('true');
					submitChangeMobileLocationForm(cityName);	
				//	$(".modal, .md-overlay").remove();		
			    } else {	
			    document.getElementById('zipchange').value = ""; 
			    $(".mobile-zip-results").hide();
				document.getElementById('selectcity1').innerHTML = "";
				document.getElementById('cityListResults1').innerHTML = "";
				openLocationDrawer();
			    $(".modal, .md-overlay").remove();	 
			    }
			  });
			}
					
		function checkCartIsEmptyMobile(cityName){
			var cartItemCount = $("#shoppingCartCount").val();
			if(cartItemCount > 0){
				modalAlertMobile(this,cityName);
				$(this).blur();
			} else{
				submitChangeMobileLocationForm(cityName);
				}
		}	
		
		function getCityForZipCodeMobile() {	
		var zipCode = $.trim($('#zipchange').val());
		if(zipCode!=null && zipCode!="" && (zipCode.match(/^\d{5}([- ]?\d{4})?$/))) {	
			$(".mobile-zip-results").hide();
			var contextPath = $('#contextPathValue').val();
			var html = "";
			$.ajax({
	            url: contextPath + "/common/includes/marketInfoJson.jsp?zipCode=" + zipCode,
	            type: "post",
	            dataType: "json",
	            error:function(){
	            	document.getElementById('cityListMobile').innerHTML = "<li>Sorry, we could not determine your City</li>";
	            },
	            success:function(data){
	            	geoData = data;				
	            	if (data.cityInfos.length == 1) {
						$(".mobile-zip-results").hide();
	            		var cityName = data.cityInfos[0].cityName;				
						var NoCoverage = data.cityInfos[0].NoCoverage;
						$('#noNetworkCoverageMob').val(false);
						if(NoCoverage!= undefined && (NoCoverage!=null && NoCoverage!="")){
							$('#noNetworkCoverageMob').val(true);
							//$(".errors-mobile-location").show();
							//$(".errors-mobile-location").html("<p>We currently do not activate new Cricket accounts in your area.  <a>Sign up</a> and be the first to know when this changes.</p>");						
						}					
						checkCartIsEmptyMobile(cityName);
						
	            	} else if(data.cityInfos.length > 1){
	            		$('#noNetworkCoverageMob').val(false);
						$(".mobile-zip-results").show();
						document.getElementById('selectcity1').innerHTML = 'Please Select Your City:';
						html += "<select id='cityListResults1' class='form-element' name='cityListResults1' onchange='setCityMobile(this.value)'>";
						html += "<option value=''>" + "Choose a city / state..." + "</option>";
	            		for(i=0;i<data.cityInfos.length;i++) {
		                	var city = data.cityInfos[i].cityName;
		                	var state = data.cityInfos[i].state;
							html += "<option value=\""+city+"\">" + city + ", " + state +"</option>";	                	
		                }
			            document.getElementById('cityListMobile').innerHTML = html;	
	            	}else{            			
	            		//$('#changeLoctionRedirectURL').val(document.URL);
	        			$('#changeLocationSubmit').trigger('click');	            		
	            	}
	            }
	            
	         });
		  }else{
			$(".errors-mobile-location").show();
			$(".errors-mobile-location").html("<p>Please enter a valid five-digit zip code.</p>");	
		  }
		}
		function submitChangeMobileLocationForm(cityName) {
			for(i=0;i<geoData.cityInfos.length;i++) {
				if(cityName == geoData.cityInfos[i].cityName) {
					$('#changeLocCity').val(geoData.cityInfos[i].cityName);
					$('#changeLocState').val(geoData.cityInfos[i].state);
					$('#changeLocCountry').val(geoData.cityInfos[i].country);
					$('#changeLocAreaCode').val(geoData.cityInfos[i].areaCode);
					$('#changeLocPostalCode').val(geoData.cityInfos[i].zipCode);
					$('#changeLocLatitude').val(geoData.cityInfos[i].latitude);
					$('#changeLocLongitude').val(geoData.cityInfos[i].longitude);
					$('#changeLoctimezone').val(geoData.cityInfos[i].timezone);
				}
			}
			//$('#changeLoctionRedirectURL').val(document.URL);
			$('#changeLocationSubmit').trigger('click');
		}
		function setCityMobile(city){		
				selectedCityMobile = city;	
		}
		function updateCityMobile(){
			checkCartIsEmptyMobile(selectedCityMobile);
		}
		function logoutSubmit(form) {
		    form.submit();
		} 
//manual change zipcode functionality for mobile ends here
//remember me functionality starts here

$(function() {
	var encryptedPassword = $.cookie("userPassword");
	var userPhoneNumber = $.cookie("userPhoneNumber");
	if(userPhoneNumber) {
		$("#uidDesktop").val(userPhoneNumber);
		$("#uidMobile").val(userPhoneNumber);
		try {
			if(encryptedPassword) {
				var decryptedUnParsed = CryptoJS.RC4.decrypt(encryptedPassword, "Secret Passphrase");
				var decrypted = decryptedUnParsed.toString( CryptoJS.enc.Utf8 );
				$("#pwdDesktop").val(decrypted.toString( CryptoJS.enc.Utf8 ));
				$("#pwdMobile").val(decrypted.toString( CryptoJS.enc.Utf8 ));
				//alert(decrypted.toString( CryptoJS.enc.Utf8 ));
			}
		}
		catch(err) {
			console.error("malformed url exception"); 
		}
	}
	
});
var rememberMeClickCount = 0;
	
function rememberMe(spanElement, channel) {
	rememberMeClickCount = rememberMeClickCount + 1;
	if(rememberMeClickCount%2 == 1) {
		createCookieWithUserId(channel);
		createCookieWithPassword(channel);
	}
}

function createCookieWithUserId(channel) {
	var cname="userPhoneNumber";
	if(channel == 'mobile') {
		var cvalue=$("#uidMobile").val();
	} else {
		var cvalue=$("#uidDesktop").val();
	}
	var d = new Date();
	d.setTime(d.getTime()+(365*24*60*60*1000));
	var expires = "expires="+d.toGMTString();
	createCookie(cname, cvalue, expires);
}

function createCookieWithPassword(channel) {
	var cname="userPassword";
	if(channel == 'mobile') {
		var cvalue=$("#pwdMobile").val();
	} else {
		var cvalue=$("#pwdDesktop").val();
	}
	var encryptedPassword = CryptoJS.RC4.encrypt(cvalue, "Secret Passphrase");
	var d = new Date();
	d.setTime(d.getTime()+(365*24*60*60*1000));
	var expires = "expires="+d.toGMTString();
	createCookie(cname, encryptedPassword, expires);
}

function createCookie(name, value, expires) {
	document.cookie = name + "=" + value + "; " + expires;
}
//remember me functionality ends here
