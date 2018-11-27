<dsp:page>
	<script type="text/javascript">
	var product = '<dsp:valueof param="producttype"/>';
	if (product.indexOf('phone-product') != -1){
		var parentCategoryId = "HANDSETS";
	}
	else {
		if(product.indexOf('accessory-product') != -1){
			var parentCategoryId = "ACCESSORIES";
		}
		else{
			if(product.indexOf('plan-product') != -1){
				var parentCategoryId = "VOICE PLANS";
			}
			else{
				var parentCategoryId = "PLAN ADD ONS";
			}			
		}
	}
	var productId = '<dsp:valueof param="productId"/>';
	var productName = '<dsp:valueof param="productName"/>';
	var quantity = '<dsp:valueof param="quantity"/>';
	var unitPrice = '<dsp:valueof param="unitPrice"/>';
	var userIntention = '<dsp:valueof param="userIntention"/>';
	var customerType = '<dsp:valueof param="customerType"/>';
	var network = '<dsp:valueof param="network"/>';
	
	var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
	var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
	
	var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
	cmCreateShopAction5Tag(productId, productName, quantity, unitPrice, parentCategoryId, pvAttrs);
	</script>
</dsp:page>
