<dsp:page> 
<dsp:importbean bean="/atg/cricket/util/CricketProfile"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<script>
function submitFormUserIntentionModal(){
	if ($('#userradio4:checked').val() == 'on') {	 
		$('#notification-style-1-a').foundation('reveal', 'close');
		return false;
	} else if ($('#userradio3:checked').val() == 'on') {	
		$('#notification-style-1-a').foundation('reveal', 'close');
		logoutSubmit(document.ATGlogOutForm);
	} else if ($('#userradio2:checked').val() == 'on') {
		var ratePlanCode = '<dsp:valueof bean="CricketProfile.ratePlanCode"/>';	
		if (itemType == 'phone-product') {
			var nooflines = '<dsp:valueof bean="CricketProfile.NumberOfLines"/>';
			if (nooflines != null && nooflines != '') {
				var numoflines = parseInt(nooflines);
				if (numoflines > 1) {
					$('#'+cartFormIdForSubmit).find('#upgradeFlow').val('upgradePhone');
					//window.location.href= '<dsp:valueof bean="CricketConfiguration.homePageLinks.myAccountURL"/>' +  '?selectLOS=true';
					//return false;
				}else{
					$('#'+cartFormIdForSubmit).find('#setUserIntention').val('upgradePhone');
				}
			}
			if (ratePlanCode != null && ratePlanCode != '4P5') {
				$('#notification-style-1-a').foundation('reveal', 'close');
			}
			submitFromUserIntentionModal = 'true';
			submitFormAddToCart(cartFormIdForSubmit);
			submitFromUserIntentionModal = '';
			$('#'+cartFormIdForSubmit).find('#setUserIntention').val('');
			$('#'+cartFormIdForSubmit).find('#upgradeFlow'),val('');
		} else if (itemType == 'plan-product') {
			var nooflines = '<dsp:valueof bean="CricketProfile.NumberOfLines"/>';
			if (nooflines != null && nooflines != '') {
				var numoflines = parseInt(nooflines);
				if (numoflines > 1) {
					$('#'+cartFormIdForSubmit).find('#upgradeFlow').val('upgradePlan');
					//window.location.href= '<dsp:valueof bean="CricketConfiguration.homePageLinks.myAccountURL"/>'+  '?selectLOS=true';
					//return false;
				}else{
					$('#'+cartFormIdForSubmit).find('#setUserIntention').val('upgradePlan');
				}
			} 
			if (ratePlanCode != null && ratePlanCode != '4P5' && $('#productSorryErrorValue').val() == 'true') {
				$('#notification-style-1-a').foundation('reveal', 'close');
			}		
			if($('#productSorryErrorValue').val() == 'false'){
				sorryErrorModal();
			}
			submitFromUserIntentionModal = 'true';
			submitFormAddToCart(cartFormIdForSubmit);
			submitFromUserIntentionModal = '';
			$('#'+cartFormIdForSubmit).find('#setUserIntention').val('');
			$('#'+cartFormIdForSubmit).find('#upgradeFlow'),val('');
		}  else if (itemType == 'addOn-product') {
			var nooflines = '<dsp:valueof bean="CricketProfile.NumberOfLines"/>';
			if (nooflines != null && nooflines != '') {
				var numoflines = parseInt(nooflines);
				if (numoflines > 1) {
					$('#'+cartFormIdForSubmit).find('#upgradeFlow').val('upgradeFeature');
					//window.location.href= '<dsp:valueof bean="CricketConfiguration.homePageLinks.myAccountURL"/>'+  '?selectLOS=true';
					//return false;
				}else{
					$('#'+cartFormIdForSubmit).find('#setUserIntention').val('upgradeFeature');
				}
			}
			
			if (ratePlanCode != null && ratePlanCode != '4P5') {
				$('#notification-style-1-a').foundation('reveal', 'close');
			}
			submitFromUserIntentionModal = 'true';
			submitFormAddToCart(cartFormIdForSubmit);
			submitFromUserIntentionModal = '';
			$('#'+cartFormIdForSubmit).find('#setUserIntention').val('');
			$('#'+cartFormIdForSubmit).find('#upgradeFlow'),val('');
		}
	} else if ($('#userradio1:checked').val() == 'on') {
		$('#'+cartFormIdForSubmit).find('#setUserIntention').val('addLine');
		$('#productSorryErrorValue').val('true');
		var custTypeName = '<dsp:valueof bean="CricketProfile.custTypeName"/>';
		var customerType = '<dsp:valueof bean="CricketProfile.customerType"/>';
		if (custTypeName != '' && custTypeName != 'PAYGo' && customerType != '' && customerType != 'h') {
			$('#notification-style-1-a').foundation('reveal', 'close');
		}
		submitFromUserIntentionModal = 'true';
		submitFormAddToCart(cartFormIdForSubmit);
		submitFromUserIntentionModal = '';
		$('#'+cartFormIdForSubmit).find('#setUserIntention').val('');
	}
	
}
</script>
<div id="notification-style-1-a" class="reveal-modal medium notification">
	<p class="title"><crs:outMessage key="cricket_shoppingcart_errormessage_whattodo"/><!-- What would you like to do? --></p>
	<form class="custom">
		<label for="radio1">
			<input name="radio-group-1-a" type="radio" id="userradio1" style="display:none;">
			<span class="custom radio"></span> <%-- <crs:outMessage key="cricket_shoppingcart_errormessage_addnewline"/> --%><!-- Add a new line of service to your account -->
			<span id="phoneSelectionMessage" style="display:none"><crs:outMessage key="cricket_shoppingcart_upgradephone_addnewline"/></span>
			<span id="planSelectionMessage" style="display:none"><crs:outMessage key="cricket_shoppingcart_upgradeplan_addnewline"/></span>
			<span id="addonSelectionMessage" style="display:none"><crs:outMessage key="cricket_shoppingcart_addon_addnewline"/></span><!-- Add a new line of service to your account -->
		</label>
		<label for="radio2">
			<input name="radio-group-1-a" type="radio" id="userradio2" style="display:none;">
			<span class="custom radio"></span> <%-- <crs:outMessage key="cricket_shoppingcart_errormessage_upgradeexistingaccount"/> --%><!-- Upgrade your existing -->
			<span id="phoneUpgradeExistingLOS" style="display:none"><crs:outMessage key="cricket_shoppingcart_upgradePhone_existingService"/></span>
			<span id="planUpgradeExistingLOS" style="display:none"><crs:outMessage key="cricket_shoppingcart_changePlan_existingService"/></span>
			<span id="addonUpgradeExistingLOS" style="display:none"><crs:outMessage key="cricket_shoppingcart_changeaddon_loggedinmdn"/></span><!-- Add a new line of service to your account -->
			
		</label>
		<label for="radio3">
			<input name="radio-group-1-a" type="radio" id="userradio3" style="display:none;">
			<span class="custom radio"></span> <%-- <crs:outMessage key="cricket_shoppingcart_errormessage_logoutaccount"/> --%><!-- Log out to add this to a separate account -->
			<span id="phoneUpgradelogout" style="display:none"><crs:outMessage key="cricket_shoppingcart_upgradePhone_logout"/></span>
			<span id="planUpgradelogout" style="display:none"><crs:outMessage key="cricket_shoppingcart_changePlan_logout"/></span>
			<span id="addonUpgradelogout" style="display:none"><crs:outMessage key="cricket_shoppingcart_addon_logout"/></span><!-- Add a new line of service to your account -->
			
		</label>	
		<%-- <label for="radio4">
			<input name="radio-group-1-a" type="radio" id="userradio4" style="display:none;">
			<span class="custom radio"></span> <crs:outMessage key="cricket_shoppingcart_errormessage_donothing"/><!-- Do Nothing -->
		</label>		 --%>	
	</form>
	<p class="buttons">
		<a href="#" class="button small orange-button" onclick="$('#notification-style-1-a').foundation('reveal', 'close');return false;">Cancel</a>
		<a href="#" class="button small green-button" onclick="submitFormUserIntentionModal();"><crs:outMessage key="cricket_shoppingcart_errormodal_submit"/><!-- Submit --></a>
	</p>	
</div>
</dsp:page>