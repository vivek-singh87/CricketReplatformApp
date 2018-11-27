<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="changeAddonsList" param="changeAddons"/>
<dsp:getvalueof var="removedAddonList" param="removedAddon"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<script>
function submitFormClearRemovedAddon(formId){
	document.getElementById(formId).submit();
}
</script>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.otherAddonsCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="addonsCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>
<div class="large-8 small-12 columns details-container">
	<div class="row">
		<div class="large-4 small-12 columns">
		<h3><crs:outMessage key="cricket_shoppingcart_new_addons"/><!-- New Add-ons --></h3>
			<dsp:droplet name="ForEach">
			<dsp:param name="array" value="${changeAddonsList}"/>
			<dsp:oparam name="output">
			<dsp:getvalueof var="changeAddon" param="element"/>
				<dsp:droplet name="ProductLookup">
				<dsp:param name="id" value="${changeAddon.auxiliaryData.productId}"/>
				<dsp:param name="filterBySite" value="false"/>
       			<dsp:param name="filterByCatalog" value="false"/>
				<dsp:param name="elementName" value="addOns"/>
				<dsp:oparam name="output">
					<div class="item-details addon">
						<c:set var="retailPrice" value="0"/>
			            <dsp:include page="/browse/includes/priceLookup.jsp">
			            	<dsp:param name="productId"  value="${changeAddon.auxiliaryData.productId}" />
			               	<dsp:param name="skuId"  value="${changeAddon.catalogRefId}"/>
			             </dsp:include>
						 <c:if test="${empty retailPrice || retailPrice eq 0}">									
							<dsp:droplet name="/atg/commerce/catalog/SKULookup">
								<dsp:param name="id" value="${changeAddon.catalogRefId}"/>
								<dsp:param name="elementName" value="sku"/>	
								<dsp:oparam name="output">
									<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
								</dsp:oparam>
							</dsp:droplet>
						</c:if>
						 <c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>							
			             <p>$${splitPrice[0]}/mo <dsp:valueof param="addOns.displayName"/></p>	
			             <dsp:getvalueof var="parcGroupName" param="addOns.parcGroupName"/>
			             <dsp:getvalueof var="hppParcGroupName" bean="CartConfiguration.hppParcGroupName"/>
			             <c:if test="${parcGroupName ne hppParcGroupName}">
						<p><a href="#" class="remove" onclick="submitFormUpgrade('RemoveAddonCart${changeAddon.id}');"><crs:outMessage key="cricket_shoppingcart_remove"/><!-- Remove --></a></p>
						<dsp:form name="RemoveAddonCart${changeAddon.id}" id="RemoveAddonCart${changeAddon.id}" formid="RemoveAddonCart${changeAddon.id}">
									<dsp:input type="hidden" bean="CartModifierFormHandler.removalCommerceIds" value="${changeAddon.id}"/>
									<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemFromOrder" value="submit"/>
								</dsp:form>
						</c:if>
					</div>	
					<hr class="hide-for-small"/>	
					
				</dsp:oparam>
				</dsp:droplet>
			</dsp:oparam>
			</dsp:droplet>	
			<dsp:getvalueof bean="ShoppingCart.current.removedPhoneSkuId" var="removedPhoneSkuId"/>
			<c:if test="${empty removedPhoneSkuId}">
				<dsp:getvalueof bean="ShoppingCart.current.upgradeModelNumber" var="modelNumber"/>
			</c:if>
			<dsp:getvalueof bean="ShoppingCart.current.upgradeModelNumber" var="upgradeModelNumber"/>
				<dsp:droplet name="/atg/commerce/catalog/SKULookup">
					<dsp:param name="id" value="${removedPhoneSkuId}"/>
					<dsp:param name="elementName" value="sku"/>	
					<dsp:oparam name="output">
						<dsp:getvalueof var="modelNumber" param="sku.modelNumber"/>	
					</dsp:oparam>
				</dsp:droplet>
				<dsp:getvalueof bean="ShoppingCart.current.removedPlanId" var="removedPlanId"/>
				<a href="${contextpath}${addonsCategoryCacheEntry.url}&planId=${removedPlanId}&modelNumber=${modelNumber}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_more_addons"/><!-- More Add-Ons --></a>
		</div>
		
		<dsp:getvalueof var="removedAddonsData" bean="ShoppingCart.current.removedAddons"/>
		<c:if test="${!empty removedAddonList}">
		<div class="large-6 small-12 columns">
			<h3 class="removed"><crs:outMessage key="cricket_shoppingcart_removed_addons"/><!-- Removed Add-ons --></h3>
			
			<div class="item-details addon">
			<dsp:droplet name="ForEach">
			<dsp:param name="array"  value="${removedAddonList}"/>
			<dsp:param name="elementName" value="removedAddonCommerceItem"/>
			<dsp:oparam name="output">
			<dsp:getvalueof var="removedAddonProductId" param="removedAddonCommerceItem.auxiliaryData.productId"/>
			<dsp:getvalueof var="removedAddonItemId" param="removedAddonCommerceItem.id"/>
					<dsp:droplet name="ProductLookup">
					<dsp:param name="id" param="removedAddonCommerceItem.auxiliaryData.productId"/>
					<dsp:param name="filterBySite" value="false"/>
	       			<dsp:param name="filterByCatalog" value="false"/>
					<dsp:param name="elementName" value="removedAddonProduct"/>
					<dsp:oparam name="output">
					<c:set var="retailPrice" value="0"/>
					 <dsp:include page="/browse/includes/priceLookup.jsp">
				            	<dsp:param name="productId"  param="key" />
				               	<dsp:param name="skuId"  param="removedAddonProduct.childSkus[0].id"/>
				             </dsp:include>
						
							 <c:if test="${empty retailPrice || retailPrice eq 0}">									
										<dsp:droplet name="/atg/commerce/catalog/SKULookup">
											<dsp:param name="id" param="removedAddonProduct.childSkus[0].id"/>
											<dsp:param name="elementName" value="sku"/>	
											<dsp:oparam name="output">
												<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
											</dsp:oparam>
										</dsp:droplet>
									</c:if>
									<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>
				             <p>$${splitPrice[0]}/mo <dsp:valueof param="removedAddonProduct.displayName"/></p>	
			
			<dsp:droplet name="Switch">
	    		  <dsp:param name="value" param="removedAddonCommerceItem.compatibleAddon"/>
	     		 <dsp:oparam name="true">
	     		 <p><a href="#" class="remove" onclick="submitFormClearRemovedAddon('ClearRemovedAddon${removedAddonProductId}');"><crs:outMessage key="cricket_shoppingcart_remove"/><!-- Remove --></a></p>
	     		 <dsp:form name="ClearRemovedAddon${removedAddonProductId}" id="ClearRemovedAddon${removedAddonProductId}" formid="ClearRemovedAddon${removedAddonProductId}">
						<dsp:input type="hidden" bean="CartModifierFormHandler.removalAddonId" value="${removedAddonProductId}"/>
						<dsp:input type="hidden" bean="CartModifierFormHandler.removalCommerceIds" value="${removedAddonItemId}"/>
						<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemFromOrder" value="submit"/>
					</dsp:form>
	     		 </dsp:oparam>
	     		  <dsp:oparam name="false">
	    		   <p class="disclaimer"><crs:outMessage key="cricket_shoppingcart_notcompatible_plan_message"/><!-- *This is not compatible with your selected plan. --></p>
	    		  </dsp:oparam>
	   			   </dsp:droplet>
							
				</dsp:oparam>
				</dsp:droplet>	
				</dsp:oparam>
			</dsp:droplet>
		
			</div>	
		</div>
		</c:if>
		<div class="large-2 small-12 columns">																															
		</div>
	</div>		
</div> <!--/.details-container-->


</dsp:page>