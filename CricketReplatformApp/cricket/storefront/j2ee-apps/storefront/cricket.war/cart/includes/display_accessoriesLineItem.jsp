<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayShoppingCartDroplet"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/com/cricket/browse/ThresholdMessageDroplet"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler" />
<dsp:getvalueof var="originatingRequestURL" bean="/OriginatingRequest.requestURI"/>
<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>	
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<input type="hidden" id="contextPath" value="${contextPath}"/>
<c:set var="retailPrice" value="0"/>
	<div class="large-8 small-12 columns details-container upgrade">

		<dsp:getvalueof var="accessory" param="accessory"/>
		<!--Start ForEach Droplet -->
		<dsp:droplet name="ForEach">
			<dsp:param name="array" param="accessory"/>
			<dsp:oparam name="output">
				<div class="row">
				<dsp:getvalueof var="size" param="size"/>
				<dsp:getvalueof var="count" param="count"/>		
				<!--Start ProductLookup Droplet -->
				<dsp:droplet name="ProductLookup">
					<dsp:param name="id" param="element.auxiliaryData.productId"/>
					<dsp:param name="filterBySite" value="false"/>
					<dsp:param name="filterByCatalog" value="false"/>
					<dsp:param name="elementName" value="productAccessory"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="productAccessory" param="productAccessory"/>
						<div class="large-8 small-12 columns">												
						<div class="row">
							<div class="large-2 small-2 columns">
								<dsp:getvalueof var="imageURL" value="${productAccessory.fullImage.url}"/>
								<c:if test="${fn:length(imageURL) > 0}">				
									<!--Start ImageURLLookupDroplet used to show images from Liquid Pixel Server -->
									<dsp:getvalueof var="height" bean="ImageConfiguration.height.cartThumbnailImage" />	
									<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
										<dsp:param name="imageHeight" value="${height}" />
										<dsp:param name="imageLink" value="${imageURL}" />
										<dsp:oparam name="output">
										<dsp:getvalueof var="liquidpixelurl" param="url"/>
											<dsp:img src="${liquidpixelurl}" alt="" />
										</dsp:oparam>
									</dsp:droplet>
									<!--End ImageURLLookupDroplet used to show images from Liquid Pixel Server -->
								</c:if>									
							</div>	
							<div class="large-1 small-1 columns"></div>
							<div class="large-9 small-9 columns">	
								<div class="item-details">													
									<h4 class="item">${productAccessory.displayName}</h4>									
									<%-- <dsp:droplet name="/atg/commerce/catalog/SKULookup">
										<dsp:param name="id" param="element.catalogRefId"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:droplet name="/atg/commerce/inventory/InventoryLookup">
											   <dsp:param name="itemId" param="sku.id"/>
												<dsp:oparam name="output">
													<input type="hidden" id="stockLevelCurrentSku" param="inventoryInfo.stockLevel"/>
													<dsp:droplet name="/com/cricket/browse/ThresholdMessageDroplet">
														<dsp:param name="threshold" param="threshold"/>
														<dsp:param name="stockLevel" param="inventoryInfo.stockLevel"/>
														<dsp:param name="thresholds" param="sku.thresholds"/> 
														<dsp:oparam name="output">
															<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
															<c:if test="${!empty thresholdMessage}">
																<p class="disclaimer">*${thresholdMessage}</p>
															</c:if>	
														</dsp:oparam>
													 </dsp:droplet>
												</dsp:oparam>
											</dsp:droplet>
										</dsp:oparam>
									</dsp:droplet> --%>
									<dsp:getvalueof var="lineItemSkuId" param="element.catalogRefId"/>
									<input type="hidden" value="${lineItemSkuId}" id="lineItemSkuId"/>
									<dsp:droplet name="ThresholdMessageDroplet">
										<dsp:param name="skuId" param="element.catalogRefId"/>
										<dsp:param name="order" bean="/atg/commerce/ShoppingCart.current"/> 
										<dsp:param name="flow" value="checkout"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
											<dsp:getvalueof var="isOutOfStock" param="isOutOfStock"/>
											<c:if test="${!empty thresholdMessage}">
												<p class="disclaimer">*${thresholdMessage}</p>
											</c:if>
										</dsp:oparam>
									</dsp:droplet>
									<p>
									<dsp:getvalueof var="CommerceItemId" param="element.id">
									<a href="#" class="edit" onclick="submitFormTODeleteAccessoryInCart('deleteAccessoryInCart${CommerceItemId}');"><crs:outMessage key="cricket_shoppingcart_delete"/><!-- Delete --></a>
									<dsp:form name="deleteAccessoryInCart${CommerceItemId}" id="deleteAccessoryInCart${CommerceItemId}" formid="deleteAccessoryInCart${CommerceItemId}">
										<dsp:input type="hidden" bean="CartModifierFormHandler.removalCommerceIds" value="${CommerceItemId}"/>
										<dsp:input type="hidden" bean="CartModifierFormHandler.deletePackageSuccessURL" id="deleteSuccessUrl"/>
										<dsp:input type="hidden" bean="CartModifierFormHandler.deletePackageFailureURL" id="deleteErrorUrl"/> 
										<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemFromOrder" value="submit"/>
									</dsp:form>
									</dsp:getvalueof>
									</p>
								</div>
							</div>
						</div>														
					</div>
					</dsp:oparam>
				</dsp:droplet>
					<div class="large-2 small-4 columns">
						<div class="item-details centered">
							<p class="item-title"><crs:outMessage key="cricket_shoppingcart_price"/><!-- Price --></p>
							<h4 class="price">
			 					<dsp:include page="/browse/includes/priceLookup.jsp">
			 						<dsp:param name="productId" param="element.auxiliaryData.productId"/>
									<dsp:param name="skuId" param="element.catalogRefId"/>
								</dsp:include>
									<dsp:droplet name="/atg/commerce/catalog/SKULookup">
									<dsp:param name="filterBySite" value="false"/>
									<dsp:param name="filterByCatalog" value="false"/>
										<dsp:param name="id" param="element.catalogRefId"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
										</dsp:oparam>
									</dsp:droplet>
								$${retailPrice}
							</h4>												
						</div>
					</div>
					<div class="large-1 small-4 columns">
						<div class="item-details centered">
							<p class="item-title"><crs:outMessage key="cricket_shoppingcart_quantity"/><!-- Quantity --></p>
							<c:set var="updateAccessoryId">
								<dsp:valueof param="element.id"/>
							</c:set>
							<c:set var="quantity">
								<dsp:valueof param="element.quantity"/>
							</c:set>
							<input type="hidden" id="updatedAccessoryId" param="element.catalogRefId"/>
							<dsp:form onsubmit="submitForm('updateCart${quantity}${updateAccessoryId}');return false;" name="updateCart${quantity}${updateAccessoryId}" id="updateCart${quantity}${updateAccessoryId}" formid="updateCart${quantity}${updateAccessoryId}">
								<dsp:input class="quantity" type="text" value="${quantity}" bean="CartModifierFormHandler.quantity"/>
								<dsp:input type="hidden" bean="CartModifierFormHandler.updateAccessoryId" value="${updateAccessoryId}"/>
								<dsp:input type="hidden" bean="CartModifierFormHandler.updateAccessoryFailureURL" id="updateSuccessUrl"/>
								<dsp:input type="hidden" bean="CartModifierFormHandler.updateAccessorySuccessURL" id="updateErrorUrl"/> 
								<dsp:input type="hidden" bean="CartModifierFormHandler.setOrderByCommerceId" value="submit"/>
							</dsp:form>
							<p>
							<a href='javascript:;' class="quantity-button btn-update" onclick="submitForm('updateCart${quantity}${updateAccessoryId}');"><crs:outMessage key="cricket_shoppingcart_update"/><!-- Update --></a>
							<%-- <a href="#" class="quantity-button">Update</a>--%></p>												
							
						</div>				
				</div>
				<!--End ProductLookup Droplet -->
					<div class="large-1 small-1 columns"></div>										
				</div>
				<c:if test="${count < size}">
					<hr/>
				</c:if>			
			</dsp:oparam>
		</dsp:droplet>
		<!--End ForEach Droplet -->
		<!-- Add More Accessories -->	
		<div class="row">
			<div class="large-12 small-12 columns">	
			<dsp:getvalueof var="accessoriesURL" param="accessoriesURL"/>
				<p><a href="${accessoriesURL}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_add_more_accessories"/><!-- Add More Accessories --></a></p>
			</div>
		</div>										
<input type="hidden" id="doProceedUpdate" value="true"/>								
	</div> <!--/.details-container-->
<script>
function submitForm(formId){
	validateQuantityWithInventory(formId);
}

function validateQuantityWithInventory(formId) {
	var accessorySkuId = $("#lineItemSkuId").val();
	var quantity = $(".quantity").val();
	var contextPath = $('#contextPath').val();
    $.ajax({
        url: contextPath + "/common/includes/inventoryInfoJson.jsp?accessorySkuId=" + accessorySkuId + "&quantity=" + quantity,
        type: "post",
        dataType: "json",
        error:function(){
        },
        success:function(data){
			var isOutOfStock = data.outOfStock;
			if(isOutOfStock == 'true') {
				$("#updatingAccessoryToExceedInventoryModal").click();
			} else {
				$("#"+formId).find ('#updateSuccessUrl').val(window.location);
			    $("#"+formId).find('#updateErrorUrl').val(window.location);
			    var quantity = $(".quantity").val();
			    if(quantity > 0){
			    	document.getElementById(formId).submit();
			    }else{
			    	updateAccessoryQuatityFormId = formId;
			    	$("#updatingAccessoryToZeroModal").click();
			    }
			}
        }
     });
}

function submitFormTODeleteAccessoryInCart(formId){
    $("#"+formId).find ('#deleteSuccessUrl').val(window.location);
    $("#"+formId).find('#deleteErrorUrl').val(window.location);
	document.getElementById(formId).submit();
}

</script>
</dsp:page>