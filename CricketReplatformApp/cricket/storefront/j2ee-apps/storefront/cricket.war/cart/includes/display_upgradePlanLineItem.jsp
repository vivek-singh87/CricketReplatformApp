<dsp:page>
<dsp:importbean bean="/com/cricket/browse/GetPlanGroupDroplet"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
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
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<script>
function submitFormClearRemovedAddon(formId){
	document.getElementById(formId).submit();
}
</script>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="changePlanList" param="changePlan"/>
<dsp:getvalueof var="removedAddon" param="removedAddon"/>
<dsp:getvalueof bean="ShoppingCart.current.removedPhoneSkuId" var="removedPhoneSkuId"/>
<dsp:getvalueof bean="ShoppingCart.current.removedPhoneId" var="removedPhoneId"/>
<dsp:getvalueof var="removedAddonsList" bean="UpgradeItemDetailsSessionBean.removedAddons"/>
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
<div class="large-8 small-12 columns details-container">
	<div class="row">
		<div class="large-4 small-12 columns">
			<h3><crs:outMessage key="cricket_shoppingcart_new_plan"/><!-- New Plan --></h3>
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
				<div class="item-details">
					<p class="plan-price">
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
					<p class="notes">
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
					<p><a href="${contextpath}${allplansCategoryCacheEntry.url}&phoneId=${removedPhoneId}&upgradeItemFlow=true" class="edit"><crs:outMessage key="cricket_shoppingcart_edit"/><!-- Edit --></a></p>
				</div>
			</dsp:oparam>
			</dsp:droplet>		
			</dsp:oparam>
			</dsp:droplet>	
				<dsp:getvalueof var="removedPlanId" bean="ShoppingCart.current.removedPlanId"/>
			<c:if test="${!empty removedPlanId}">
			<div class="removed-items">
				<h3 class="removed"><crs:outMessage key="cricket_shoppingcart_removed_plan"/><!-- Removed Plan --></h3>
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
										<dsp:param name="id" param="removedPhoneProduct.childSkus[0].id"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
										</dsp:oparam>
									</dsp:droplet>
								</c:if>
						<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>	
			             <p>$${splitPrice[0]}/mo <dsp:valueof param="removedPhoneProduct.displayName"/></p>	
				</dsp:oparam>
				</dsp:droplet>
			</div>
			</c:if>
		</div>
		<hr class="show-for-small"/>
		<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>
		<dsp:getvalueof var="OOFMarketType" bean="CartConfiguration.outOfFootPrintMarketType"></dsp:getvalueof>
		<c:if test="${OOFMarketType ne marketType}">
			<div class="large-6 small-12 columns">
				<h3><crs:outMessage key="cricket_shoppingcart_new_addons"/><!-- New Add-ons --></h3>
									<dsp:droplet name="/atg/commerce/catalog/SKULookup">
											<dsp:param name="id" value="${removedPhoneSkuId}"/>
											<dsp:param name="elementName" value="sku"/>	
											<dsp:oparam name="output">
												<dsp:getvalueof var="modelNumber" param="sku.modelNumber"/>	
											</dsp:oparam>
										</dsp:droplet>
				<dsp:droplet name="DisplayNonPackageAddonItemInCart">
						<dsp:param name="order" bean="ShoppingCart.current"/>
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
					             <p><a href="#" class="remove" onclick="submitFormUpgrade('RemoveCart${changeAddonId}');"><crs:outMessage key="cricket_shoppingcart_remove"/><!-- Remove --></a> </p>
					             <%-- <a href="#" class="delete-label" onclick="submitForm('deleteCart${packageId}');">Delete</a> --%>
					             <dsp:form name="RemoveCart${changeAddonId}" id="RemoveCart${changeAddonId}" formid="RemoveCart${changeAddonId}" style="display:none">
										<dsp:input type="hidden" bean="CartModifierFormHandler.removalCommerceIds" value="${changeAddonId}"/>
									<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemFromOrder" value="submit"/>
								</dsp:form>
					            
					             </div>
					             <c:if test="${count eq size}">
					             	<hr class="hide-for-small"/>
					             	<dsp:getvalueof bean="ShoppingCart.current.removedPhoneSkuId" var="removedPhoneSkuId"/>
					             	<c:if test="${empty removedPhoneSkuId}">
										<dsp:getvalueof bean="ShoppingCart.current.upgradeModelNumber" var="modelNumber"/>
									</c:if>
									<dsp:droplet name="/atg/commerce/catalog/SKULookup">
											<dsp:param name="id" value="${removedPhoneSkuId}"/>
											<dsp:param name="elementName" value="sku"/>	
											<dsp:oparam name="output">
												<dsp:getvalueof var="modelNumber" param="sku.modelNumber"/>	
											</dsp:oparam>
										</dsp:droplet>
					             	<a href="${contextpath}${addonsCategoryCacheEntry.url}&planId=${changePlan.auxiliaryData.productId}&modelNumber=${modelNumber}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_more_addons"/><!-- More Add-Ons --></a>
					             </c:if>
							</dsp:oparam>
							</dsp:droplet>
							</dsp:oparam>
							</dsp:droplet>
						</dsp:oparam>
						<dsp:oparam name="empty">
							<div class="item-details addon">
								<p><crs:outMessage key="cricket_shoppingcart_noaddons_selected"/><!-- No Add-ons Selected. --></p>
								<c:choose>
								<c:when test="${empty changePlan}">
									<p><a href="${contextpath}${addonsCategoryCacheEntry.url}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_select_addons"/><!-- Select Add-Ons --></a></p>
								</c:when>
								<c:otherwise>
									<p><a href="${contextpath}${addonsCategoryCacheEntry.url}&planId=${changePlan.auxiliaryData.productId}&modelNumber=${modelNumber}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_select_addons"/><!-- Select Add-Ons --></a></p>
								</c:otherwise>
								</c:choose>	
							</div>	
						</dsp:oparam>
						</dsp:droplet>
					
				<c:if test="${!empty removedAddon}">
			
				<div class="removed-items">
						<h3 class="removed"><crs:outMessage key="cricket_shoppingcart_removed_addons"/><!-- Removed Add-ons --></h3>
						<div class="item-details addon">
				<dsp:droplet name="ForEach">
				<dsp:param name="array"  value="${removedAddon}"/>
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
			</div>
		</c:if>
		<div class="large-2 small-12 columns"></div>
	</div>		
</div> <!--/.details-container-->

</dsp:page>