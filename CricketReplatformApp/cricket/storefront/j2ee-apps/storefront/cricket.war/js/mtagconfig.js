// Date last modified =  20091202
// Modified by =  DS

var lpMTagConfig = {
	'lpServer' : 'sales.liveperson.net',
	'lpNumber' : '4968591',
	'lpProtocol' : (document.location.toString().indexOf('https:')==0) ? 'https' : 'http',
	'lpTagLoaded' : false,
	'lpTagSrv' : 'sales.liveperson.net',
	'pageStartTime' : (new Date()).getTime(), //pageStartTime is set with a timestamp as soon as the page starts loading
	'deploymentID' : '2'
	
};

lpMTagConfig.deploymentConfigPath = lpMTagConfig.lpServer+'/visitor/addons/deploy.asp';

lpMTagConfig.lpLoadScripts = function(){
	lpAddMonitorTag(lpMTagConfig.lpProtocol + '://' + lpMTagConfig.deploymentConfigPath + '?site=' + lpMTagConfig.lpNumber + '&d_id=' + lpMTagConfig.deploymentID);
}

function lpAddMonitorTag(src) { 
	if (!lpMTagConfig.lpTagLoaded) {if (typeof(src) == 'undefined' || typeof(src) == 'object') {if (lpMTagConfig.lpMTagSrc) {src = lpMTagConfig.lpMTagSrc;}else {if (lpMTagConfig.lpTagSrv) {src = lpMTagConfig.lpProtocol + '://' +lpMTagConfig.lpTagSrv + '/hcp/html/mTag.js';}else {src = '/hcp/html/mTag.js';};};};if (src.indexOf('http') != 0) {src = lpMTagConfig.lpProtocol + '://' + lpMTagConfig.lpServer + src + '?site=' + lpMTagConfig.lpNumber;} else {if (src.indexOf('site=') < 0) {if (src.indexOf('?') < 0) {src = src + '?';} else{src = src + '&';} src = src + 'site=' + lpMTagConfig.lpNumber;  };};var s = document.createElement('script');s.setAttribute('type', 'text/javascript');s.setAttribute('charset', 'iso-8859-1');s.setAttribute('src', src);document.getElementsByTagName('head').item(0).appendChild(s);}
}
		
//The code below send a PAGEVAR to LP with the time [in seconds] it took the page to load. Code is executed in the onload event
lpMTagConfig.calculateSentPageTime = function () {
	var t = (new Date()).getTime() - lpMTagConfig.pageStartTime;
	lpAddVars('page','pageLoadTime', Math.round(t/1000)+' sec');
};

//Variables Arrays - By Scope
if (typeof(lpMTagConfig.pageVar)=='undefined') { lpMTagConfig.pageVar = []; }
if (typeof(lpMTagConfig.sessionVar)=='undefined') { lpMTagConfig.sessionVar = []; }
if (typeof(lpMTagConfig.visitorVar)=='undefined') { lpMTagConfig.visitorVar = []; }
//Extra actions to be taken once the code executes
if (typeof(lpMTagConfig.onLoadCode)=='undefined') { lpMTagConfig.onLoadCode = []; }
//Dynamic Buttons Array
if(typeof(lpMTagConfig.dynButton)=='undefined') { lpMTagConfig.dynButton = []; }
// This need to be add to afterStartPage will work
if(typeof(lpMTagConfig.ifVisitorCode)=='undefined') {lpMTagConfig.ifVisitorCode = []; }


// Function that sends variables to LP - By Scope
function lpAddVars(scope,name,value) {
	if (name.indexOf('OrderTotal')!=-1 || name.indexOf('OrderNumber')!=-1){
		if  (value=='' || value==0) return; // pass 0 value to all but OrderTotal
		else lpMTagConfig.sendCookies = false
	}	
	value=lpTrimSpaces(value.toString());
//Remove cut long variables names and values. Trims suffix of the variable name above the 25th character onwards
	if (name.length>50) { 
		name=name.substr(0,50);
	}
    if (value.length>50) { // Trims suffix of the variable value above the 50th character onwards
		value=value.substr(0,50);
	}
	switch (scope){
		case 'page': lpMTagConfig.pageVar[lpMTagConfig.pageVar.length] = escape(name)+'='+escape(value); break;
		case 'session': lpMTagConfig.sessionVar[lpMTagConfig.sessionVar.length] = escape(name)+'='+escape(value); break;
		case 'visitor': lpMTagConfig.visitorVar[lpMTagConfig.visitorVar.length] = escape(name)+'='+escape(value); break;
	}	
}

// Preventing long cookie transfer for IE based browsers.
function onloadEMT() { 
	var LPcookieLengthTest=document.cookie;
	if (lpMTag.lpBrowser == 'IE' && LPcookieLengthTest.length>1000){
		lpMTagConfig.sendCookies=false;
	}
}

//The Trim function returns a text value with the leading and trailing spaces removed
function lpTrimSpaces(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,'');
}

// Immediate Data submission function
function lpSendData(varscope,varname,varvalue) {
	if(typeof(lpMTag)!='undefined' && typeof(lpMTag.lpSendData)!='undefined')
		lpMTag.lpSendData(varscope.toUpperCase() +'VAR!'+ varname + '=' + varvalue, true);
}

// The unit variable purpose is to route the chat or call to the designated skill. <LOB> should be replaced with the skill name, i.e. : sales
try {
	if (typeof(lpUnit)=="undefined") {var lpUnit="sales";}
	if (typeof(lpLanguage)=="undefined") {var lpLanguage="english";}
	if(typeof(lpAddVars)!="undefined") {
		lpAddVars("page","unit",lpUnit);
		lpAddVars("session","language",lpLanguage);
	}
	lpMTagConfig.defaultInvite = "chat-" + lpUnit + "-" + lpLanguage;
}
catch(e){}

lpMTagConfig.onLoadCode[lpMTagConfig.onLoadCode.length] = onloadEMT;

//Scan dynButton and removes buttons which doesnt have Div on the page
lpMTagConfig.onLoadCode[lpMTagConfig.onLoadCode.length] = function () {
	if(typeof(lpMTagConfig.dynButton)!='undefined') {
		for (i=0;i<lpMTagConfig.dynButton.length;i++){
			if (typeof(lpMTagConfig.dynButton[i].pid)!='undefined' && document.getElementById(lpMTagConfig.dynButton[i].pid) == null) {
					lpMTagConfig.dynButton.splice(i,1);
					i--;
			}
		}
	}
};


/*
 * RS - Logic to swap out button based on cricekt market or mobile being detected
 */
lpMTagConfig.buttonContents = {};
lpMTagConfig.swappedButtons = {};

lpMTagConfig.buttonSwap = function() {
	if ( typeof(window.rdiLocationInfo) != 'undefined' ) {
		var mobileTag = '';
		var cricketMarketTag = '';
		if (window.rdiLocationInfo.isMobileDetected) mobileTag = '-mobile';
		if (!window.rdiLocationInfo.isCricketMarket) cricketMarketTag = '-paygo';
		if ( mobileTag != '' || cricketMarketTag != '') { 
			for (i=0;i<lpMTagConfig.dynButton.length;i++){
				if (typeof(lpMTagConfig.dynButton[i].pid)!='undefined' && !lpMTagConfig.swappedButtons[lpMTagConfig.dynButton[i].pid] && document.getElementById(lpMTagConfig.dynButton[i].pid) != null ) {
					var pid = lpMTagConfig.dynButton[i].pid;
					var divid = (pid + mobileTag + cricketMarketTag);
					//carry over style and class attributes
					var style = $('#'+pid).attr('style');
					var clss = $('#'+pid).attr('class');
					$('#'+pid).replaceWith('<div id="' + divid + '"></div>');
					$('#'+divid).attr({'style': style, 'class': clss});
					lpMTagConfig.buttonContents[ divid ] = $('#'+divid).html();
					lpMTagConfig.swappedButtons[ divid ] = true;
				}
			}
			
			if ( window.rdiLocationInfo.isMobileDetected ) {
				var phone = '800-309-0154';
				//set a timer to listen for changes the button divs. every 3 seconds
				setInterval(function(){ 
					for(var key in lpMTagConfig.buttonContents) {
						//listen for 
						if($('#'+key).html() != lpMTagConfig.buttonContents[ key ]){
							var imgcontent = $('#'+key+' a').html();
							var mobilecontent = '<a href="tel:'+phone+'">'+imgcontent+'</a>';
							$('#'+key).html( mobilecontent );
							lpMTagConfig.buttonContents[ key ] = $('#'+key).html();
						}
					}
				}, 3000);
			}
	
		}
	}
};


//The folowing functions will be load after the page will finish loading
lpMTagConfig.onLoadAll = function () {
	if ( typeof(window.rdiLocationInfo) != 'undefined' ) {
		if ( window.rdiLocationInfo.isAcKillEnabled ) {
			return;
		}
	}
	lpMTagConfig.buttonSwap();
	lpMTagConfig.calculateSentPageTime();
	lpMTagConfig.lpLoadScripts();
};

if (window.attachEvent) { 
	window.attachEvent('onload',lpMTagConfig.onLoadAll); 
} else {
	window.addEventListener('load',lpMTagConfig.onLoadAll,false);
}

// LP Button Code
if(typeof(lpMTagConfig.dynButton)!="undefined"){
	lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-top","pid":"Cricket-button-top","afterStartPage":true};
	lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-bottom","pid":"Cricket-button-bottom","afterStartPage":true};
	lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-small","pid":"Cricket-button-small","afterStartPage":true};
	lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-navigation","pid":"Cricket-button-navigation","afterStartPage":true};
	lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-home-top","pid":"Cricket-button-home-top","afterStartPage":true};
	lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-home-bottom","pid":"Cricket-button-home-bottom","afterStartPage":true};
	lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-modal","pid":"cricket-modal-chat","afterStartPage":true};
	
	//PAYGo Buttons
	lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-top-paygo","pid":"Cricket-button-top-paygo","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-bottom-paygo","pid":"Cricket-button-bottom-paygo","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-small-paygo","pid":"Cricket-button-small-paygo","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-navigation-paygo","pid":"Cricket-button-navigation-paygo","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-home-top-paygo","pid":"Cricket-button-home-top-paygo","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-home-bottom-paygo","pid":"Cricket-button-home-bottom-paygo","afterStartPage":true};

    //Mobile Buttons
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-top-mobile","pid":"Cricket-button-top-mobile","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-bottom-mobile","pid":"Cricket-button-bottom-mobile","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-small-mobile","pid":"Cricket-button-small-mobile","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-navigation-mobile","pid":"Cricket-button-navigation-mobile","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-home-top-mobile","pid":"Cricket-button-home-top-mobile","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-home-bottom-mobile","pid":"Cricket-button-home-bottom-mobile","afterStartPage":true};

    //Mobile PAYGo Buttons
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-top-mobile-paygo","pid":"Cricket-button-top-mobile-paygo","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-bottom-mobile-paygo","pid":"Cricket-button-bottom-mobile-paygo","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-small-mobile-paygo","pid":"Cricket-button-small-mobile-paygo","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-navigation-mobile-paygo","pid":"Cricket-button-navigation-mobile-paygo","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-home-top-mobile-paygo","pid":"Cricket-button-home-top-mobile-paygo","afterStartPage":true};
    lpMTagConfig.dynButton[lpMTagConfig.dynButton.length] = {"name":"chat-"+lpUnit+"-"+lpLanguage+"-home-bottom-mobile-paygo","pid":"Cricket-button-home-bottom-mobile-paygo","afterStartPage":true};
}

// Dynamc switchof static order-by-phone image to paygo phone number for
// non Cricket networks.
$(document).ready(function() {
	if ( typeof(window.rdiLocationInfo) != 'undefined' ) {
		if ( window.rdiLocationInfo.isAcKillEnabled ) {
			$('img[src="/images/misc/order-by-phone-pia.png"]').remove();
		} else {
			if ( !window.rdiLocationInfo.isCricketMarket ) {
				$('img[src="/images/misc/order-by-phone-pia.png"]').attr( {'src':'//'+window.rdiLocationInfo.srcHost+'/images/misc/order-by-phone-paygo.png','alt':'Order by Phone: 800 922 5159'} );
			} else {
				$('img[src="/images/misc/order-by-phone-pia.png"]').attr( {'alt':'Order by Phone: 800 975 3708'} );
			}
		}
	}
});
