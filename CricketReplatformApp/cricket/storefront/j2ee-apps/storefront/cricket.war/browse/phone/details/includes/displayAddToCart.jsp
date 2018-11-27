<dsp:page>
	<dsp:importbean
		bean="/atg/commerce/order/purchase/CartModifierFormHandler" />
	<dsp:importbean bean="/atg/commerce/inventory/InventoryLookup" />
	<dsp:importbean bean="/atg/userprofiling/Profile" />
	<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration"
		var="ImageConfiguration" />
	<dsp:importbean bean="/com/cricket/browse/ThresholdMessageDroplet" />
	<dsp:importbean bean="/atg/userprofiling/Profile" />
	<dsp:importbean bean="/atg/dynamo/droplet/Switch" />
	<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath" />
	<dsp:getvalueof var="CartButton" param="CartButton" />
	<dsp:getvalueof var="ProfilemarketType" param="ProfilemarketType" />
	<dsp:getvalueof var="isOOFPhone" param="isOOFPhone" />	
	<dsp:getvalueof var="skuId" param="skuId" />
	<dsp:getvalueof var="productId" param="productId" />
	<%-- <dsp:droplet name="/atg/commerce/catalog/SKULookup">
		<dsp:param name="id" value="${skuId}" />
		<dsp:param name="elementName" value="sku" />
		<dsp:oparam name="output">
			<dsp:droplet name="InventoryLookup">
				<dsp:param name="itemId" param="sku.id" />
				<dsp:oparam name="output">
					<dsp:droplet name="ThresholdMessageDroplet">
						<dsp:param name="threshold" param="threshold" />
						<dsp:param name="stockLevel" param="inventoryInfo.stockLevel" />
						<dsp:param name="thresholds" param="sku.thresholds" />
						<dsp:oparam name="output">
							<dsp:getvalueof var="thresholdMessage" param="thresholdMessage" />
							<dsp:getvalueof var="isOutOfStock" param="isOutOfStock" />
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
	</dsp:droplet> --%>
	<dsp:droplet name="ThresholdMessageDroplet">
		<dsp:param name="skuId" param="skuId"/>
		<dsp:param name="order" bean="/atg/commerce/ShoppingCart.current"/> 
		<dsp:oparam name="output">
			<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
			<dsp:getvalueof var="isOutOfStock" param="isOutOfStock"/>
		</dsp:oparam>
	</dsp:droplet>
	<c:choose>
		<c:when test="${isOutOfStock eq true or CartButton eq 'disable'}">
			<c:choose>
				<c:when test="${(ProfilemarketType eq 'OOF') and (isOOFPhone eq false or isOOFPhone eq null)}">				
					<a class="button text-right disabled secondary has-tip" data-tooltip="" title="Not available in your area.">
						<crs:outMessage key="cricket_phonedetails_AddtoCart" />
					</a> 
				</c:when>
				<c:otherwise>
					<a class="button text-right disabled secondary has-tip" data-tooltip="" title="Sorry, this item is out of stock.">
						<crs:outMessage key="cricket_phonedetails_AddtoCart" />
					</a> 
				</c:otherwise>				
				</c:choose>	
			
		</c:when>
		<c:otherwise>
			<dsp:include page="/cart/common/addToCart.jsp">
				<dsp:param name="addToCartClass" value="button text-right" />
				<dsp:param name="productId" value="${productId}" />
				<dsp:param name="skuId" value="${skuId}" />
				<dsp:param name="quantity" value="1" />
				<dsp:param name="url"
					bean="/OriginatingRequest.requestURIWithQueryString" />
			</dsp:include>
		</c:otherwise>
	</c:choose>
	<p class="shipping-info text-right">${thresholdMessage}</p>
		<c:choose>
				<c:when test="${(ProfilemarketType eq 'OOF') and (isOOFPhone eq false or isOOFPhone eq null)}">				
					<p class="shipping-info text-right">Not available in your area.</p>
				</c:when>				
		</c:choose>	
</dsp:page>