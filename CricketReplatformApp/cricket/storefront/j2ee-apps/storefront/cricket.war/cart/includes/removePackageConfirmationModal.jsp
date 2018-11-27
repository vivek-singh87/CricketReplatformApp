<dsp:page>
<script>
function submitFormRemovePackageForm(){
	document.getElementById(deletePackageFormId).submit();
}
</script>
<div id="notification-style-3" class="reveal-modal small notification" style="display: block; opacity: 1; top: 100px;">
	<p class="title"><crs:outMessage key="cricket_shoppingcart_youwant_to_continue"/><!--Are you sure you want to continue? --></p>
	<p class="buttons">
		<a href="#" class="button small orange-button" onclick="$('#notification-style-3').foundation('reveal', 'close');return false;"><crs:outMessage key="cricket_shoppingcart_NO"/><!--No --></a>
		<a href="#" class="button small green-button" onclick="submitFormRemovePackageForm();$('#notification-style-3').foundation('reveal', 'close');">
		<crs:outMessage key="cricket_shoppingcart_yes_remove_fromcart"/>
		<!--Yes, Remove From Cart -->
		</a>
	</p>	
</div>
</dsp:page>