<dsp:page>

<dsp:importbean bean="/com/cricket/util/CartCookieInfo"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="openCart" bean="CartCookieInfo.openCart" />
<dsp:getvalueof var="cricketMyAccountHostURL" bean="CricketConfiguration.homePageLinks.myAccount" />
<dsp:getvalueof var="openLocationDrawer" bean="CitySessionInfoObject.openLocationDrawer"/>
<dsp:getvalueof var="inquireAccountTimeOut" bean="CitySessionInfoObject.inquireAccountTimeOut"/>
<dsp:getvalueof var="inquireCoverageTimeOut" bean="CitySessionInfoObject.inquireCoverageTimeOut"/>
<dsp:getvalueof var="redirectTOAccount" bean="CitySessionInfoObject.loggedIn"/>
<input type="hidden" name="openCart" id="openCartDrawer" value="${openCart}" />
<input type="hidden" name="openLocationDrawer" id="openLocationDrawer" value="${openLocationDrawer}" />
<input type="hidden" name="inquireCoverageTimeOut" id="inquireCoverageTimeOut" value="${inquireCoverageTimeOut}" />
<input type="hidden" name="inquireAccountTimeOut" id="inquireAccountTimeOut" value="${inquireAccountTimeOut}" />
<input type="hidden" name="cricketMyAccountHostURL" id="cricketMyAccountHostURL" value="${cricketMyAccountHostURL}" />
<input type="hidden" name="redirectTOAccount" id="redirectTOAccount" value="${redirectTOAccount}" />
<input type="hidden" name="DPSLogout" id="DPSLogout" value="${param.DPSLogout}" />
<script> 
 $(document).ready( function() {
	var DPSLogoutVal=$("#DPSLogout").val();
	var loggedIn=$("#redirectTOAccount").val();
	var drawerCart=$("#openCartDrawer").val();
	var openCartParameter = '<dsp:valueof param="openCart"/>';
	var openLocationDrawerVal=$("#openLocationDrawer").val();
	var inquireCoverageTimeOutVal=$("#inquireCoverageTimeOut").val();	
	var inquireAccountTimeOutVal=$("#inquireAccountTimeOut").val();	
	var cricketMyAccountHostURLVal= $("#cricketMyAccountHostURL").val()
	//alert('loggedIn:::'+loggedIn+"::"+DPSLogoutVal);
	if((loggedIn!=undefined && loggedIn!=null && loggedIn=='false') || DPSLogoutVal=='true'){		
		window.location= cricketMyAccountHostURLVal+"?action=Logout";
	}
	//alert('drawerCart::'+drawerCart)
	//Code to open the Cart Drawer
	if(drawerCart!=undefined && drawerCart!=null && drawerCart=='openCart' && openCartParameter != 'true'){		
		openCartDrawer();
	}	
	
	if(inquireAccountTimeOutVal!=undefined && inquireAccountTimeOutVal!=null && inquireAccountTimeOutVal == 'true'){		
		$("#inquireAccountTimeOutModel").click();
	}	
	
	//Code to open the Location Drawer when there is no network coverage found for the entered zip code.
	if(openLocationDrawerVal!=undefined && openLocationDrawerVal!=null && openLocationDrawerVal=='true' || inquireCoverageTimeOutVal!=undefined && inquireCoverageTimeOutVal!=null && inquireCoverageTimeOutVal=='true'){	
		var w = $(document).width();
		if(inquireCoverageTimeOutVal == 'true'){
			if(w<769)
			{		
				$("#drawer-mobile-location input[id=zipchange]").focus();
				$(".errors-mobile-location").show();
				$(".errors-mobile-location").html("<p>Sorry, but we are temporarily unable to process online requests at this time. Please call 1-800-CRICKET, visit your nearest Cricket store, or try again online later.</p>");
				mobileLocator();
			}else{
				$("#drawer-location input[id=zip]").focus();
				document.getElementById('zip').value = "";
				$(".errors-location").show();
				$(".errors-location").html("<p>Sorry, but we are temporarily unable to process online requests at this time. Please call 1-800-CRICKET, visit your nearest Cricket store, or try again online later.</p>");
				openLocationDrawer();
			}
		}else {
			if(w<769)
			{		
				$("#drawer-mobile-location input[id=zipchange]").focus();
				$(".errors-mobile-location").show();
				$(".errors-mobile-location").html("<p>We currently do not activate new Cricket accounts in your area.  <a href="+cricketMyAccountHostURLVal+">Sign up</a> and be the first to know when this changes.</p>");
				mobileLocator();
			}else{
				$("#drawer-location input[id=zip]").focus();
				document.getElementById('zip').value = "";
				$(".errors-location").show();
				$(".errors-location").html("<p>We currently do not activate new Cricket accounts in your area.  <a href="+cricketMyAccountHostURLVal+">Sign up</a> and be the first to know when this changes.</p>");
				openLocationDrawer();
			}
		}
	}
	
});
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
</script>
	<dsp:setvalue bean="CitySessionInfoObject.openLocationDrawer" value="false"/>
	<dsp:setvalue bean="CitySessionInfoObject.loggedIn" value="true"/>
	<dsp:setvalue bean="CartCookieInfo.openCart" value="null"/>
</dsp:page>