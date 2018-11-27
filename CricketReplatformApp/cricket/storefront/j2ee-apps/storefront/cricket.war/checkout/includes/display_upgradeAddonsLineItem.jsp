<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayMdnPhoneNumberDroplet"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:getvalueof var="changeAddonsList" param="changeAddons"/>
<dsp:getvalueof var="removedAddonList" param="removedAddon"/>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.otherAddonsCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="addonsCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>
<div class="row">
	<div id="order-list" class="large-12 small-12 columns">
		<div id="addon-change" class="row">
			<div class="columns large-12 small-12">
				<p class="title" data-section-title><crs:outMessage key="cricket_checkout_ADDON_CHANGEFOR"/><!-- ADD-ON CHANGE FOR: --> 
				<dsp:droplet name="DisplayMdnPhoneNumberDroplet">
					<dsp:param name="profileMdn" bean="ShoppingCart.last.upgradeMdn"/>
					<dsp:oparam name="empty">
					</dsp:oparam>
					<dsp:oparam name="output">
						<dsp:valueof param="formatedMdnNumber"/>
					</dsp:oparam>
				</dsp:droplet>
				</p>
			</div>
		<c:if test="${!empty changeAddonsList}">
		<div class="row items">
			  <div class="columns large-5 small-12"><h4 class="first"><crs:outMessage key="cricket_checkout_new_addons"/></h4></div>			
		</div>
		</c:if>
		<div class="row items">	
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
				</dsp:oparam>
				</dsp:droplet>	
			</dsp:oparam>
			</dsp:droplet>	
			<dsp:getvalueof bean="ShoppingCart.current.removedPhoneSkuId" var="removedPhoneSkuId"/>
				<dsp:droplet name="/atg/commerce/catalog/SKULookup">
					<dsp:param name="id" value="${removedPhoneSkuId}"/>
					<dsp:param name="elementName" value="sku"/>	
					<dsp:oparam name="output">
						<dsp:getvalueof var="modelNumber" param="sku.modelNumber"/>	
					</dsp:oparam>
				</dsp:droplet>			
				<%-- <dsp:getvalueof var="removedAddonsData" bean="ShoppingCart.last.removedAddons"/> --%>
				
				<c:if test="${!empty removedAddonList}">
					<div class="row items">
					<div class="package-item phone columns large-12 small-12">
							<h4><crs:outMessage key="cricket_checkout_removed_addons"/><!-- Removed Addons --></h4>		
														
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
												<dsp:param name="skuId"   param="removedAddonProduct.childSkus[0].id"/>
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
									</dsp:oparam>
									</dsp:droplet>
								</dsp:oparam>
						</dsp:droplet>							
					</div>
					</div>
				</c:if>	
		</div>
		</div>
	</div>
</div>
</dsp:page>