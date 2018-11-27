<dsp:page>
<dsp:importbean bean="/com/cricket/browse/GetPlanGroupDroplet"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<dsp:importbean bean="/atg/commerce/inventory/InventoryLookup" />
<dsp:importbean bean="/com/cricket/browse/ThresholdMessageDroplet" />
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="originatingRequestURL" bean="/OriginatingRequest.requestURI"/>
<dsp:getvalueof var="defaultSortParam" bean="CricketConfiguration.defaultSortParam"/>
<dsp:getvalueof var="packages" param="packages"/>
<dsp:getvalueof var="phones" value="${packages.phoneCommerceItem}"/>
<dsp:getvalueof var="plans" value="${packages.planCommerceItem}"/>
<dsp:getvalueof var="addons" value="${packages.addOnsCommerceItems}"/>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="allphonesCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>
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
<c:set var="retailPrice" value="0"/>
	<div class="large-8 small-12 columns details-container">
		<div class="row">
			<div class="large-4 small-12 columns">
				<h3>Phone</h3>
				<div class="item-details">
					<dsp:droplet name="ProductLookup">
					<dsp:param name="id" value="${phones.productId}"/>
					<dsp:param name="filterBySite" value="false"/>
					<dsp:param name="filterByCatalog" value="false"/>
					<dsp:oparam name="output">
					<dsp:droplet name="/atg/commerce/catalog/SKULookup">
								<dsp:param name="id" value="${phones.catalogRefId}" />
								<dsp:param name="elementName" value="sku" />
								<dsp:oparam name="output">
								<dsp:getvalueof var="imageURL" param="sku.largeImage.url"/>								
								<!--Start ImageURLLookupDroplet used to show images from Liquid Pixel Server -->
								<dsp:getvalueof var="height" bean="ImageConfiguration.height.cartThumbnailImage" />		
								<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
									<dsp:param name="imageLink" value="${imageURL}" />
									<dsp:param name="imageHeight" value="${height}" />
									<dsp:oparam name="output">
									<dsp:getvalueof var="liquidpixelurl" param="url"/>
										<img src="${liquidpixelurl}" alt="<dsp:valueof param='element.displayName'/>" />
									</dsp:oparam>
								</dsp:droplet>
								<!--End ImageURLLookupDroplet used to show images from Liquid Pixel Server -->								
							</dsp:oparam>
							</dsp:droplet>							
							<%-- <dsp:droplet name="/atg/commerce/catalog/SKULookup">
								<dsp:param name="id" value="${phones.catalogRefId}" />
								<dsp:param name="elementName" value="sku" />
								<dsp:oparam name="output">
									<dsp:droplet name="InventoryLookup">
										<dsp:param name="itemId" param="sku.id" />
										<dsp:oparam name="output">
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
							<dsp:droplet name="ThresholdMessageDroplet">
								<dsp:param name="skuId" value="${phones.catalogRefId}"/>
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
							<p class="item"><dsp:valueof param="element.displayName" valueishtml="true" /></p>
							<p class="price">
								<dsp:include page="/browse/includes/priceLookup.jsp">
			 						<dsp:param name="productId" value="${phones.productId}"/>
									<dsp:param name="skuId" value="${phones.catalogRefId}"/>
								</dsp:include>
								<dsp:include page="/browse/includes/promotionsLookUp.jsp">
									<dsp:param name="product" param="element" />
									<dsp:param name="sku" param="element.childSKUs[0]" />
									<dsp:param name="pageValue" value="cart" />
									<dsp:param name="retailPrice" value="${retailPrice}"/>
								</dsp:include>
								<c:if test="${empty retailPrice || retailPrice eq 0}">									
									<dsp:droplet name="/atg/commerce/catalog/SKULookup">
										<dsp:param name="id" value="${phones.catalogRefId}"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>
											<dsp:getvalueof var="modelNumber" param="sku.modelNumber"/>									
										</dsp:oparam>
									</dsp:droplet>
								</c:if>
								<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice-webInstantPromoDiscountAmt-instantPromoDiscountAmt-rebateAmt}" var="productRetailPrice" />
								$${productRetailPrice}
							</p>
							  
							<c:if test="${not empty rebateAmt or not empty totalPromoDiscountAmt}">
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${totalPromoDiscountAmt}" var="totalPromoDiscountAmt" />	
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${instantPromoDiscountAmt}" var="instantPromoDiscountAmt" />	
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${webInstantPromoDiscountAmt}" var="webInstantPromoDiscountAmt" />						
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${rebateAmt}" var="rebateAmt" />	
																					
						<p class="notes">
								<c:if test="${((not empty instantPromoDiscountAmt) && (instantPromoDiscountAmt gt '0.00')) || ((not empty webInstantPromoDiscountAmt) && (webInstantPromoDiscountAmt gt '0.00')) || ((not empty rebateAmt) && (rebateAmt gt '0.00'))}">
									<crs:outMessage key="cricket_shoppingcart_after"/><br>	<!-- After --> 
								</c:if>								
								<c:if test="${not empty instantPromoDiscountAmt && instantPromoDiscountAmt gt '0.00'}">
									$${instantPromoDiscountAmt} Instant Discount
									<br>								
								</c:if>
								<c:if test="${not empty webInstantPromoDiscountAmt && webInstantPromoDiscountAmt gt '0.00'}">
									$${webInstantPromoDiscountAmt} Web Instant Discount	
									<br>						
								</c:if>
								<%-- <c:if test="${not empty rebateAmt && not empty totalPromoDiscountAmt && totalPromoDiscountAmt ne '0.00' && rebateAmt ne '0.00'}">
								<crs:outMessage key="cricket_shoppingcart_and"/><!-- and -->
								</c:if> --%>
								<c:if test="${not empty rebateAmt && rebateAmt ne '0.00'}">
									$${rebateAmt} <crs:outMessage key="cricket_shoppingcart_mail_inoffer"/><!-- Mail-in offer. -->
								</c:if>
						</p>					
							</c:if>
							<%-- ${contextpath}${allphonesCategoryCacheEntry.url} --%>
						<p><a href="#" class="edit" onclick="submitForm('phoneEditCart${phones.packageId}')";><crs:outMessage key="cricket_shoppingcart_edit"/><!-- Edit --></a>
						 <dsp:form name="phoneEditCart${phones.packageId}" id="phoneEditCart${phones.packageId}" formid="phoneEditCart${phones.packageId}" >
								<dsp:input type="hidden" bean="CartModifierFormHandler.editPackageId" value="${phones.packageId}"/>
								<dsp:input type="hidden" bean="CartModifierFormHandler.editProductType" value="${phones.cricItemTypes}"/>
								<c:choose>
									<c:when test="${not empty plans.productId && plans.productId ne ''}">
										<dsp:input type="hidden" bean="CartModifierFormHandler.editSuccessUrl" value="${contextpath}${allphonesCategoryCacheEntry.url}&editPhone=true"/>
									</c:when>
									<c:otherwise>
										<dsp:input type="hidden" bean="CartModifierFormHandler.editSuccessUrl" value="${contextpath}${allphonesCategoryCacheEntry.url}&editPhone=true"/>
									</c:otherwise>
								</c:choose>
								<dsp:input type="hidden" bean="CartModifierFormHandler.editCommerceItem" value="submit"/>
							</dsp:form>
						</p>						
					</dsp:oparam>
					<dsp:oparam name="empty">
						<p><crs:outMessage key="cricket_shoppingcart_nophone_selected"/><!-- No Phone Selected. --></p>
						<c:choose>
						<c:when test="${empty plans}">
							<c:choose>
								<c:when test="${not empty defaultSortParam}">
									<p><a href="${contextpath}${allphonesCategoryCacheEntry.url}&sort=${defaultSortParam}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_add_phone"/><!-- Add Phone --></a></p>
								</c:when>
								<c:otherwise>
									<p><a href="${contextpath}${allphonesCategoryCacheEntry.url}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_add_phone"/><!-- Add Phone --></a></p>
								</c:otherwise>
							</c:choose>						
						</c:when>
						<c:otherwise>
							<p><a href="${contextpath}${allphonesCategoryCacheEntry.url}&planId=${plans.productId}&packageId=${plans.packageId}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_add_phone"/><!-- Add Phone --></a></p>
						</c:otherwise>
						</c:choose>							
					</dsp:oparam>
					</dsp:droplet>
				</div>
			</div>
			<c:set var="retailPrice" value="0"/>
			<div class="large-4 small-12 columns">
				<h3><crs:outMessage key="cricket_shoppingcart_plan"/><!-- Plan --></h3>
				<div class="item-details">
					<dsp:droplet name="ProductLookup">
					<dsp:param name="id" value="${plans.productId}"/>
					<dsp:param name="filterBySite" value="false"/>
					<dsp:param name="filterByCatalog" value="false"/>
					<dsp:oparam name="output">
							<p class="plan-price">
								<dsp:include page="/browse/includes/priceLookup.jsp">
			 						<dsp:param name="productId" value="${plans.productId}"/>
									<dsp:param name="skuId" value="${plans.catalogRefId}"/>
								</dsp:include>
								<c:if test="${empty retailPrice || retailPrice eq 0}">									
									<dsp:droplet name="/atg/commerce/catalog/SKULookup">
										<dsp:param name="id" value="${plans.catalogRefId}"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
										</dsp:oparam>
									</dsp:droplet>
								</c:if>
								<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>
								<sup>$</sup>${splitPrice[0]}<span>/mo</span>
							</p>
							<p class="plan-title"><dsp:valueof param="element.planCategory"/></p>
							<p class="item"><dsp:valueof param="element.displayName" /></p>
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
							<p><%-- <a href="${contextpath}${allplansCategoryCacheEntry.url}" class="edit">Edit</a> --%>
							<a href="#" class="edit" onclick="submitForm('planEditCart${plans.packageId}')";><crs:outMessage key="cricket_shoppingcart_edit"/><!-- Edit --></a>
						 <dsp:form name="planEditCart${plans.packageId}" id="planEditCart${plans.packageId}" formid="planEditCart${plans.packageId}" >
								<dsp:input type="hidden" bean="CartModifierFormHandler.editPackageId" value="${plans.packageId}"/>
								<dsp:input type="hidden" bean="CartModifierFormHandler.editProductType" value="${plans.cricItemTypes}"/>
								<c:choose>
									<c:when test="${not empty phones.productId && phones.productId ne ''}">
										<dsp:input type="hidden" bean="CartModifierFormHandler.editSuccessUrl" value="${contextpath}${allplansCategoryCacheEntry.url}&phoneId=${phones.productId}&editPlan=true"/>
									</c:when>
									<c:otherwise>
										<dsp:input type="hidden" bean="CartModifierFormHandler.editSuccessUrl" value="${contextpath}${allplansCategoryCacheEntry.url}&editPlan=true"/>
									</c:otherwise>
								</c:choose>
								<dsp:input type="hidden" bean="CartModifierFormHandler.editCommerceItem" value="submit"/>
							</dsp:form>
							</p>					
					</dsp:oparam>
					<dsp:oparam name="empty">
						<p><crs:outMessage key="cricket_shoppingcart_noplan_selected"/><!-- No Plan Selected. --></p>
						<c:choose>
						<c:when test="${empty phones}">
							<p><a href="${contextpath}${allplansCategoryCacheEntry.url}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_add_plan"/><!-- Add Plan --></a></p>
						</c:when>
						<c:otherwise>
							<p><a href="${contextpath}${allplansCategoryCacheEntry.url}&phoneId=${phones.productId}&packageId=${phones.packageId}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_add_plan"/><!-- Add Plan --></a></p>
						</c:otherwise>
						</c:choose>						
					</dsp:oparam>
					</dsp:droplet>				
				</div>
			</div>
			<c:set var="retailPrice" value="0"/>
			<div class="large-4 small-12 columns">
				<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>
				<dsp:getvalueof var="OOFMarketType" bean="CartConfiguration.outOfFootPrintMarketType"></dsp:getvalueof>
				<c:if test="${OOFMarketType ne marketType}"><!-- Condition added for defect 748 -->
					<h3><crs:outMessage key="cricket_shoppingcart_addons"/><!-- Add-ons --></h3>
					<div class="item-details">
					<dsp:droplet name="ForEach">
					<dsp:param name="array" value="${addons}"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="size" param="size"/>
						<dsp:getvalueof var="count" param="count"/>
						<dsp:getvalueof var="addonCommerceItemId" param="element.repositoryId"/>
						<c:set var="packageId">
							<dsp:valueof param="element.packageId"/>
						</c:set>
						<c:set var="cricItemTypes">
							<dsp:valueof param="element.cricItemTypes"/>
						</c:set>
						<dsp:droplet name="ProductLookup">
						<dsp:param name="id" param="element.productId"/>
						<dsp:param name="elementName" value="addOns"/>
						<dsp:param name="filterBySite" value="false"/>
					<dsp:param name="filterByCatalog" value="false"/>
						<dsp:oparam name="output">	
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
				             <p><a href="#" class="remove" onclick="submitAddOnDeletePackage('deleteAddonFromCart${addonCommerceItemId}');"><crs:outMessage key="cricket_shoppingcart_remove"/><!-- Remove --></a>
				             <dsp:form name="deleteAddonFromCart${addonCommerceItemId}" id="deleteAddonFromCart${addonCommerceItemId}" formid="deleteAddonFromCart${addonCommerceItemId}">
								<dsp:input type="hidden" bean="CartModifierFormHandler.deletePacakgeId" value="${packageId}"/>
								 <dsp:input type="hidden" bean="CartModifierFormHandler.addonProductType" value="${cricItemTypes}"/>
								 <dsp:input type="hidden" bean="CartModifierFormHandler.addonCommerceItemId" value="${addonCommerceItemId}"/>
 								<dsp:input type="hidden" bean="CartModifierFormHandler.deletePackageSuccessURL" id="deleteSuccessUrl"/>
								<dsp:input type="hidden" bean="CartModifierFormHandler.deletePackageFailureURL" id="deleteErrorUrl"/> 
								<dsp:input type="hidden" bean="CartModifierFormHandler.deletePackage" value="submit"/>
							</dsp:form>
				             </p>
				             <c:if test="${count eq size}">
				             	<hr class="hide-for-small"/>
				             	<a href="${contextpath}${addonsCategoryCacheEntry.url}&planId=${plans.productId}&modelNumber=${modelNumber}&packageId=${phones.packageId}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_more_addons"/><!-- More Add-Ons --></a>
				             </c:if>
						</dsp:oparam>
						</dsp:droplet>		
					</dsp:oparam>
					<dsp:oparam name="empty">
						<p><crs:outMessage key="cricket_shoppingcart_noaddons_selected"/><!-- No Add-ons Selected. --></p>
						<c:choose>
						<c:when test="${!empty plans && !empty phones}">
							<p><a href="${contextpath}${addonsCategoryCacheEntry.url}&planId=${plans.productId}&modelNumber=${modelNumber}&packageId=${phones.packageId}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_select_addons"/><!-- Select Add-Ons --></a></p>
						</c:when>
						<c:otherwise>
							<p><a href="${contextpath}${addonsCategoryCacheEntry.url}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_select_addons"/><!-- Select Add-Ons --></a></p>	
						</c:otherwise>
						</c:choose>						
					</dsp:oparam>
					</dsp:droplet>
				</div>
				</c:if>
					
			</div>
		</div>		
	</div> <!--/.details-container-->
	 <script>
function submitForm(formId){
	document.getElementById(formId).submit();
}
function submitAddOnDeletePackage(formId){
    $("#"+formId).find ('#deleteSuccessUrl').val(window.location);
    $("#"+formId).find('#deleteErrorUrl').val(window.location);
	document.getElementById(formId).submit();
}
</script>
</dsp:page>