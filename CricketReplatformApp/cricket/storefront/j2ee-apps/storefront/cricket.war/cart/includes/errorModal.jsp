<dsp:page>
<script>
function submitFormCartErrorModal(){
	if ($('#radio11:checked').val() == 'on') {	 
		$('#notification-style-1-b').foundation('reveal', 'close');
		return false;
	} else if ($('#radio21:checked').val() == 'on') {	
		if (clearUserIntention == 'true') {
			$('#notification-style-1-b').foundation('reveal', 'close');
			$('#'+cartFormIdForSubmit).find('#clearUserIntention').val('true');
		}
		if (removeItemsInCart == 'true') {
			$('#notification-style-1-b').foundation('reveal', 'close');
			$('#'+cartFormIdForSubmit).find('#removeItemBeforeAdding').val('true');
		}
		submitFormAddToCart(cartFormIdForSubmit);
		$('#'+cartFormIdForSubmit).find('#clearUserIntention').val('false');
		$('#'+cartFormIdForSubmit).find('#removeItemBeforeAdding').val('false');
	}

}
</script>
<div id="notification-style-1-b" class="reveal-modal medium notification" style="display: block; opacity: 1; top: 100px;">
	<p class="title" id="promptId"><crs:outMessage key="cricket_shoppingcart_errormodal_what_would_youlike"/><!-- What would you like to do? --></p>
	<form class="custom" id="cartErrorModal">
		<label for="radio11">
			<input name="radio-group-1-b" type="radio" id="radio11" style="display:none;">
			<span class="custom radio"></span> <span id="option1Display"><crs:outMessage key="cricket_shoppingcart_errormodal_do_nothing"/><!-- Do Nothing --></span>
		</label>
		<label for="radio21">
			<input name="radio-group-1-b" type="radio" id="radio21" style="display:none;">
			<span class="custom radio"></span> <span id="option2Display"><crs:outMessage key="cricket_shoppingcart_errormodal_remove_item_incart"/><!-- Remove Items in cart and add new item. --></span>
		</label>				
	</form>
	<p class="buttons">
		<a href="#" class="button small orange-button" onclick="$('#notification-style-1-b').foundation('reveal', 'close');return false;"><crs:outMessage key="cricket_shoppingcart_errormodal_cancel"/><!-- Cancel --></a>
		<a href="#" class="button small green-button" onclick="submitFormCartErrorModal();"><crs:outMessage key="cricket_shoppingcart_errormodal_submit"/><!-- Submit --></a>
	</p>	
</div>
</dsp:page>