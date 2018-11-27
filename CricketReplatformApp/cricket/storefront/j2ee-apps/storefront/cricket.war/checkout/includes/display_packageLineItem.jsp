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
<div class="row items">		
			<div class="package-item phone columns large-4 small-12">	  
				<h4>Phone</h4>
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
									<dsp:param name="protocalType" value="secure"/>
									<dsp:oparam name="output">
									<dsp:getvalueof var="liquidpixelurl" param="url"/>
										<img src="${liquidpixelurl}" alt="<dsp:valueof param='element.displayName'/>" />
									</dsp:oparam>
								</dsp:droplet>
								<!--End ImageURLLookupDroplet used to show images from Liquid Pixel Server -->								
							</dsp:oparam>
							</dsp:droplet>
							<%-- <dsp:droplet name="/atg/commerce/inventory/InventoryLookup">
							   <dsp:param name="itemId" value="${phones.catalogRefId}"/>
								<dsp:oparam name="output">
									<dsp:droplet name="/com/cricket/browse/ThresholdMessageDroplet">
										<dsp:param name="threshold" param="threshold"/>
										<dsp:param name="stockLevel" param="inventoryInfo.stockLevel"/>
										<dsp:param name="thresholds" param="element.childSKUs[0].thresholds"/> 
										<dsp:oparam name="output">
											<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
											<c:if test="${!empty thresholdMessage}">
												<p class="disclaimer">*${thresholdMessage}</p>
											</c:if>	
										</dsp:oparam>
									 </dsp:droplet>
								</dsp:oparam>
							</dsp:droplet>	 --%>	
							<dsp:droplet name="ThresholdMessageDroplet">
								<dsp:param name="skuId" param="skuId"/>
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
						<p class="small">
							
								<c:if test="${not empty rebateAmt or not empty totalPromoDiscountAmt}">
									<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${totalPromoDiscountAmt}" var="totalPromoDiscountAmt" />
									<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${instantPromoDiscountAmt}" var="instantPromoDiscountAmt" />	
									<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${webInstantPromoDiscountAmt}" var="webInstantPromoDiscountAmt" />						
									<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${rebateAmt}" var="rebateAmt" />														
							
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
									<c:if test="${not empty rebateAmt && rebateAmt ne '0.00'}">
										$${rebateAmt} <crs:outMessage key="cricket_shoppingcart_mail_inoffer"/><!-- Mail-in offer. -->
									</c:if>
												
								</c:if>
						</p>					
					</dsp:oparam>
					<dsp:oparam name="empty">
							<p><crs:outMessage key="cricket_checkout_no_phone_selected"/><!-- No Phones Selected. --></p>				
					</dsp:oparam>
				</dsp:droplet>
			</div>	
			<!--END PHONES content -->	
			<!--START PLANS content -->	
			<c:set var="retailPrice" value="0"/>
			<div class="package-item plan columns large-4 small-12">
				 <h4><crs:outMessage key="cricket_shoppingcart_plan"/><!--Plans --></h4>
					<dsp:droplet name="ProductLookup">
					<dsp:param name="id" value="${plans.productId}"/>
					<dsp:param name="filterBySite" value="false"/>
					<dsp:param name="filterByCatalog" value="false"/>
					<dsp:oparam name="output">
							<p class="green-price">						
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
					<dsp:oparam name="empty">
						<p><crs:outMessage key="cricket_checkout_no_plan_selected"/><!-- No Plan Selected. --></p>
					</dsp:oparam>
					</dsp:droplet>		
			</div>
			<!--END PLANS content -->	
			<!--START ADD-ONS content -->	
			<c:set var="retailPrice" value="0"/>
			<div class="package-item add-ons columns large-4 small-12">
				<h4><crs:outMessage key="cricket_checkout_addons"/><!-- Add-ons --></h4>
					<dsp:droplet name="ForEach">
						<dsp:param name="array" value="${addons}"/>
						<dsp:oparam name="output">
							<dsp:getvalueof var="size" param="size"/>
							<dsp:getvalueof var="count" param="count"/>
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
								</dsp:oparam>
							</dsp:droplet>		
						</dsp:oparam>
						<dsp:oparam name="empty">
							<p><crs:outMessage key="cricket_checkout_no_addons_selected"/><!-- No Add-ons Selected. --></p>
						</dsp:oparam>
					</dsp:droplet>
			</div>
		<!--END ADD-ONS content -->	
	</div>
</dsp:page>