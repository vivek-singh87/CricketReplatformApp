<dsp:page>
<script>
function submitFormAddToCart(formId){
	document.getElementById(formId).submit()
}
</script>
<dsp:getvalueof var="addToCartClass" param="addToCartClass"/>
<dsp:getvalueof var="productId" param="productId"/>
<dsp:getvalueof var="skuId" param="skuId"/>
<dsp:getvalueof var="quantity" param="quantity"/>
<dsp:getvalueof var="successURL" param="successURL"/>
<a href="#" class="${addToCartClass}" onclick="submitFormAddToCart('addToCart${productId}');">Add to Cart</a>

<dsp:form name="addToCart${productId}" id="addToCart${productId}" formid="addToCart${productId}">
	<dsp:input type="hidden" bean="/atg/commerce/order/purchase/CartModifierFormHandler.productId" value="${productId}"/>
	<dsp:input type="hidden" bean="/atg/commerce/order/purchase/CartModifierFormHandler.catalogRefIds" value="${skuId}"/>
	<dsp:input type="hidden" bean="/atg/commerce/order/purchase/CartModifierFormHandler.quantity" value="${quantity}"/>
	<dsp:input type="hidden" bean="/atg/commerce/order/purchase/CartModifierFormHandler.addItemToOrderSuccessURL" value="${successURL}"/>
	<dsp:input type="hidden" bean="/atg/commerce/order/purchase/CartModifierFormHandler.addItemToOrder" value="submit"/>
</dsp:form>
</dsp:page>