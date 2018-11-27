<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="removedAddonList" param="removedAddon"/>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.otherAddonsCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="addonsCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>
<div class="large-8 small-12 columns details-container">
	<div class="row">
		<div class="large-4 small-12 columns">
			<h3>Removed Addons</h3>
			<dsp:droplet name="ForEach">
			<dsp:param name="array" value="${removedAddonList}"/>
			<dsp:oparam name="output">
			<dsp:getvalueof var="removedAddonItem" param="element"/>
				<dsp:droplet name="ProductLookup">
				<dsp:param name="id" value="${removedAddonItem.auxiliaryData.productId}"/>
				<dsp:param name="filterBySite" value="false"/>
       			<dsp:param name="filterByCatalog" value="false"/>
				<dsp:param name="elementName" value="addOns"/>
				<dsp:oparam name="output">
					<div class="item-details addon">
						<c:set var="retailPrice" value="0"/>
			            <dsp:include page="/browse/includes/priceLookup.jsp">
			            	<dsp:param name="productId"  value="${removedAddonItem.auxiliaryData.productId}" />
			               	<dsp:param name="skuId"  value="${removedAddonItem.catalogRefId}"/>
			             </dsp:include>
						 <c:if test="${empty retailPrice || retailPrice eq 0}">									
									<dsp:droplet name="/atg/commerce/catalog/SKULookup">
										<dsp:param name="id" value="${removedAddonItem.catalogRefId}"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
										</dsp:oparam>
									</dsp:droplet>
								</c:if>
			             <p>$${retailPrice}/mo <dsp:valueof param="addOns.displayName"/></p>	
						<p><a href="#" class="remove" onclick="submitFormUpgrade('RemoveCart${removedAddonItem.id}');"><crs:outMessage key="cricket_shoppingcart_remove"/><!-- Remove --></a></p>
						<dsp:form name="RemoveCart${removedAddonItem.id}" id="RemoveCart${removedAddonItem.id}" formid="RemoveCart${removedAddonItem.id}">
									<dsp:input type="hidden" bean="CartModifierFormHandler.removalCommerceIds" value="${removedAddonItem.id}"/>
									<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemFromOrder" value="submit"/>
								</dsp:form>
					</div>	
					
					
				</dsp:oparam>
				</dsp:droplet>
			</dsp:oparam>
			</dsp:droplet>	
		</div>
		
		
		<div class="large-2 small-12 columns">																															
		</div>
	</div>		
</div> <!--/.details-container-->


</dsp:page>