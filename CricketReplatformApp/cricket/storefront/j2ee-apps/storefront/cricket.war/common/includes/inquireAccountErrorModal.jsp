<dsp:page>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="storeLocatorURL" bean="CricketConfiguration.homePageLinks.findStore" />
<input type="hidden" name="storeLocatorURL" id="storeLocatorURL" value="${storeLocatorURL}" />
<script>
function submitFormInquireAccountFailModal(){
	var storeLocatorURLVal= $("#storeLocatorURL").val();
	$("#cricketInquireAccountlogout").show();
	$("#cricketInquireAccountUpgradeLOS").show();
	if ($('#userradio1:checked').val() == 'on') {	
		$('#notification-style-error-2004').foundation('reveal', 'close');
		logoutSubmit(document.ATGlogOutForm);
	} else if ($('#userradio2:checked').val() == 'on') {	
		$('#notification-style-error-2004').foundation('reveal', 'close');
		window.location= storeLocatorURLVal;
	} 
}
	</script>
<div id="notification-style-error-2004" class="reveal-modal small notification">
	<p class="title"><crs:outMessage key="cricket_shoppingcart_sorry_error"/><!--Sorry, but we are unable to process this request online. What would you like to do?--></p>
	<form class="custom">
		<label for="radio1">
			<input name="radio-group-1-a" type="radio" id="userradio1" style="display:none;">
			<span class="custom radio"></span> <crs:outMessage key="cricket_logout_InquireAccountFail"/> 
			<span id="cricketInquireAccountlogout" style="display:none"> <crs:outMessage key="cricket_logout_InquireAccountFail"/> <%--. Log out to create a new line of service --%> </span>
		</label>
		<label for="radio2">
			<input name="radio-group-1-a" type="radio" id="userradio2" style="display:none;">
			<span class="custom radio"></span>  <crs:outMessage key="cricket_errormessage_AccountLogin"/> <!-- Upgrade your existing -->
			<span id="cricketInquireAccountUpgradeLOS" style="display:none"> <crs:outMessage key="cricket_errormessage_AccountLogin"/> 
			<%--Upgrade your existing line of service or account by calling 1-800-CRICKET or visiting your nearest full-service Cricket store  --%>
			</span>
		</label>
	</form>
	<p class="buttons">
		<a href="#" class="button small orange-button" onclick="$('#notification-style-error-2004').foundation('reveal', 'close');return false;">Cancel</a>
		<a href="#" class="button small green-button" onclick="submitFormInquireAccountFailModal();"><crs:outMessage key="cricket_shoppingcart_errormodal_submit"/><!-- Submit --></a>
	</p>
</div>
</dsp:page>