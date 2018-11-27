<dsp:page>
<dsp:importbean bean="/com/cricket/browse/GetPlanGroupDroplet"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayMdnPhoneNumberDroplet"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayNonPackageAddonItemInCart"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/browse/DisplayFeaturesDroplet"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="changePlanList" param="changePlan"/>
<dsp:getvalueof var="removedAddonsList" bean="UpgradeItemDetailsSessionBean.removedAddons"/>
<dsp:getvalueof var="removedAddon" param="removedAddon"/>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.allPlansCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="allplansCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.otherAddonsCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="addonsCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>

<div class="row">
	<div id="order-list" class="large-12 small-12 columns hide-for-small">
		<div id="plan-upgrade" class="row">
			<div class="columns large-12 small-12">
			  <p class="title" data-section-title><crs:outMessage key="cricket_checkout_PLAN_UPGRADEFOR"/>
			  
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
			
			<div class="row items">
			  <div class="columns large-5 small-12"><h4 class="first"><crs:outMessage key="cricket_checkout_new_plan"/><!-- New Plan --></h4></div>
			  <div class="columns large-6 small-12"><h4><crs:outMessage key="cricket_checkout_new_addons"/><!-- New Addons --></h4></div>
			</div>			
			<div class="row items">	
				<div class="package-item plan columns large-5 small-12">
				<dsp:droplet name="ForEach">
				<dsp:param name="array" value="${changePlanList}"/>
				<dsp:oparam name="output">
				<dsp:getvalueof var="changePlan" param="element"/>
				<dsp:getvalueof var="changePlanProductId" value="${changePlan.auxiliaryData.productId}"/>
						<dsp:droplet name="ProductLookup">
							<dsp:param name="id" value="${changePlan.auxiliaryData.productId}"/>
							<dsp:param name="filterBySite" value="false"/>
							<dsp:param name="filterByCatalog" value="false"/>
							<dsp:oparam name="output">
									<dsp:getvalueof var="plan" param="element"/>
								<p class="green-price">
									<c:set var="retailPrice" value="${changePlan.priceInfo.listPrice}"/>
									<dsp:include page="/browse/includes/priceLookup.jsp">
										<dsp:param name="productId" value="${changePlan.auxiliaryData.productId}"/>
											<dsp:param name="skuId" value="${changePlan.catalogRefId}"/>
										</dsp:include>
										<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>
											<sup>$</sup>${splitPrice[0]}<span>/mo</span>					
								</p>
								<p class="plan-title">${plan.planCategory}</p>
								<p class="item">${plan.displayName}</p>
								<p class="small">
									<dsp:droplet name="GetPlanGroupDroplet">
										<dsp:param name="planGroupItems" param="element.planGroups"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="planGroup" param="planGroup"></dsp:getvalueof>
										</dsp:oparam>
									</dsp:droplet>
									<dsp:getvalueof var="includesTextCart" value="${planGroup.includesTextCart}"/>
									<c:if test="${not empty includesTextCart}">
										Includes: ${includesTextCart}
									</c:if>
								</p>				
							</dsp:oparam>
						</dsp:droplet>
				</dsp:oparam>
				</dsp:droplet>				
					<dsp:getvalueof var="removedPlanId" bean="ShoppingCart.last.removedPlanId"/>			
						<c:if test="${!empty removedPlanId}">						
								<h4 class="grey"><crs:outMessage key="cricket_checkout_removed_plan"/><!-- Removed Plan --></h4>
							<dsp:droplet name="ProductLookup">
							<dsp:param name="id" value="${removedPlanId}"/>
							<dsp:param name="filterBySite" value="false"/>
							<dsp:param name="filterByCatalog" value="false"/>
							<dsp:param name="elementName" value="removedPhoneProduct"/>
							<dsp:oparam name="output">
							<c:set var="retailPrice" value="0"/>
							 <dsp:include page="/browse/includes/priceLookup.jsp">
										<dsp:param name="productId"  value="${removedPlanId}" />
										<dsp:param name="skuId" param="removedPhoneProduct.childSkus[0].id"/>
									 </dsp:include>								
									 <c:if test="${empty retailPrice || retailPrice eq 0}">									
												<dsp:droplet name="/atg/commerce/catalog/SKULookup">
													<dsp:param name="id" param="removedSkuId"/>
													<dsp:param name="elementName" value="sku"/>	
													<dsp:oparam name="output">
														<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
													</dsp:oparam>
												</dsp:droplet>
											</c:if>
									 <p>$${retailPrice}/mo <dsp:valueof param="removedPhoneProduct.displayName"/></p>	
							</dsp:oparam>
							</dsp:droplet>				
						</c:if>								
				</div>			
				<div class="row items">									
						
				<div class="package-item plan columns large-6 small-12" style="padding-left: 50px;">
					<dsp:getvalueof bean="ShoppingCart.last.removedPhoneSkuId" var="removedPhoneSkuId"/>
					<dsp:droplet name="/atg/commerce/catalog/SKULookup">
							<dsp:param name="id" value="${removedPhoneSkuId}"/>
							<dsp:param name="elementName" value="sku"/>	
							<dsp:oparam name="output">
								<dsp:getvalueof var="modelNumber" param="sku.modelNumber"/>	
							</dsp:oparam>
						</dsp:droplet>
				<dsp:droplet name="DisplayNonPackageAddonItemInCart">
					<dsp:param name="order" bean="ShoppingCart.last"/>
					<dsp:oparam name="output">
					<dsp:droplet name="ForEach">
					<dsp:param name="array" param="addons"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="size" param="size"/>
						<dsp:getvalueof var="count" param="count"/>
						<c:set var="changeAddonId">
							<dsp:valueof param="element.id"/>
						</c:set>
						<dsp:droplet name="ProductLookup">
						<dsp:param name="id" param="element.auxiliaryData.productId"/>
						<dsp:param name="filterBySite" value="false"/>
       					<dsp:param name="filterByCatalog" value="false"/>
						<dsp:param name="elementName" value="addOns"/>
						<dsp:oparam name="output">	
						<div class="item-details addon">
						<c:set var="retailPrice" value="0"/>
				            <dsp:include page="/browse/includes/priceLookup.jsp">
				            	<dsp:param name="productId" param="addOns.id" />
								<dsp:param name="skuId" param="addOns.childSKUs[0].id" />
				            </dsp:include>
							<c:if test="${empty retailPrice || retailPrice eq 0}">									
								<dsp:getvalueof var="retailPrice" param="addOns.childSKUs[0].listPrice"/>								
							</c:if>
							<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>	
				             <p>$${splitPrice[0]}/mo <dsp:valueof param="addOns.displayName"/></p>	
				         </div>
				             <c:if test="${count eq size}">
				             	<hr class="hide-for-small"/>
				             	<dsp:getvalueof bean="ShoppingCart.last.removedPhoneSkuId" var="removedPhoneSkuId"/>
								<dsp:droplet name="/atg/commerce/catalog/SKULookup">
										<dsp:param name="id" value="${removedPhoneSkuId}"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:getvalueof var="modelNumber" param="sku.modelNumber"/>	
										</dsp:oparam>
									</dsp:droplet>			             	
				             </c:if>
						</dsp:oparam>
						</dsp:droplet>
						</dsp:oparam>
						</dsp:droplet>
					</dsp:oparam>
					<dsp:oparam name="empty">
						<div class="item-details addon">
							<p><crs:outMessage key="cricket_shoppingcart_noaddons_selected"/><!-- No Add-ons Selected. --></p>					
						</div>					
					</dsp:oparam>
				</dsp:droplet>
				
			<%-- <dsp:getvalueof var="removedAddonData" bean="UpgradeItemDetailsSessionBean.removedAddons"/> --%>
				<c:if test="${!empty removedAddon}">				
						<div class="removed-items">
								<h3 class="removed"><crs:outMessage key="cricket_shoppingcart_removed_addons"/><!-- Removed Add-ons --></h3>
								<div class="item-details addon">
							<dsp:droplet name="ForEach">
							<dsp:param name="array"  value="${removedAddon}"/>
							<dsp:param name="elementName" value="removedAddonCommerceItem"/>
							<dsp:oparam name="output">
							<dsp:getvalueof var="removedAddonProductId" param="removedAddonCommerceItem.auxiliaryData.productId"/>							
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

		</div>
	</div>

</dsp:page>