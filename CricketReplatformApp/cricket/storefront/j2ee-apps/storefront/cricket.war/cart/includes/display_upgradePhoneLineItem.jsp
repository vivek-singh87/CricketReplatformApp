<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="upgradePhone" param="upgradePhone"/>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="allphonesCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>
<div class="large-8 small-12 columns details-container upgrade">
	<div class="row">
		<h3><crs:outMessage key="cricket_shoppingcart_new_phone"/><!-- New Phone --></h3>
		<dsp:droplet name="ForEach">
			<dsp:param name="array" param="upgradePhone"/>
			<dsp:oparam name="output">
			<dsp:getvalueof var="upgradePhone" param="element"/>
		<dsp:droplet name="ProductLookup">
			<dsp:param name="id" param="element.auxiliaryData.productId"/>
			<dsp:param name="filterBySite" value="false"/>
       		<dsp:param name="filterByCatalog" value="false"/>
			<dsp:param name="elementName" value="phone"/>
			<dsp:oparam name="output">
			<dsp:getvalueof var="phone" value="${upgradePhone.auxiliaryData.productRef}"/>
			<div class="large-8 small-12 columns">												
				<div class="row">
					<div class="large-2 small-2 columns">
						<div class="item-details">
						<dsp:getvalueof var="imageURL"  value="${phone.fullImage.url}"/>
						<c:if test="${fn:length(imageURL) > 0}">				
							<!--Start ImageURLLookupDroplet used to show images from Liquid Pixel Server -->
							<dsp:getvalueof var="height" bean="ImageConfiguration.height.cartThumbnailImage" />		
							<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
								<dsp:param name="imageHeight" value="${height}" />
								<dsp:param name="imageLink" value="${imageURL}" />
								<dsp:oparam name="output">
								<dsp:getvalueof var="liquidpixelurl" param="url"/>
									<dsp:img src="${liquidpixelurl}" alt="${phone.displayName}" />
								</dsp:oparam>
							</dsp:droplet>
							<!--End ImageURLLookupDroplet used to show images from Liquid Pixel Server -->
						</c:if>
						</div>
					</div>	
					<div class="large-1 small-1 columns"></div>
					<div class="large-9 small-9 columns">	
						<div class="item-details">													
							<h4 class="item">${phone.displayName}</h4>
							<p class="disclaimer">
								<dsp:droplet name="/atg/commerce/inventory/InventoryLookup">
								<dsp:param name="itemId" value="${upgradePhone.catalogRefId}" />
								<dsp:param name="useCache" value="true" />
								<dsp:oparam name="output">
									<dsp:getvalueof var="inventoryMsg" param="inventoryInfo.availabilityStatusMsg"/>
									<c:if test="${inventoryMsg eq 'Out of Stock' }">
										<p class="disclaimer"><crs:outMessage key="cricket_shoppingcart_outof_stock"/><!-- *Out of Stock: will ship within 3 days. --></p>	
									</c:if>
								</dsp:oparam>
								</dsp:droplet> 
							</p>
							<dsp:getvalueof var="planId" bean="UpgradeItemDetailsSessionBean.removedPlanId">
							<p><a href="${contextpath}${allphonesCategoryCacheEntry.url}&planId=${planId}&upgradeItemFlow=true" class="edit"><crs:outMessage key="cricket_shoppingcart_edit"/><!-- Edit --></a></p>
							</dsp:getvalueof>
						</div>
					</div>
				</div>														
			</div>
			<div class="large-4 small-12 columns">
				<div class="item-details">
					<h4 class="price">
						<dsp:include page="/browse/includes/priceLookup.jsp">
	 						<dsp:param name="productId" value="${upgradePhone.auxiliaryData.productId}"/>
							<dsp:param name="skuId" value="${upgradePhone.catalogRefId}"/>
						</dsp:include>
						<dsp:include page="/browse/includes/promotionsLookUp.jsp">
								<dsp:param name="product" param="phone" />
								<dsp:param name="sku" param="phone.childSKUs[0]" />
								<dsp:param name="pageValue" value="cart" />
								<dsp:param name="retailPrice" value="${retailPrice}"/>
							</dsp:include>
						<c:if test="${empty retailPrice || retailPrice eq 0}">									
									<dsp:droplet name="/atg/commerce/catalog/SKULookup">
										<dsp:param name="id" value="${upgradePhone.catalogRefId}"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
										</dsp:oparam>
									</dsp:droplet>
								</c:if>
						<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice-webInstantPromoDiscountAmt-instantPromoDiscountAmt-rebateAmt}" var="productRetailPrice" />
								$${productRetailPrice}
					</h4>
					  
							<c:if test="${not empty rebateAmt or not empty totalPromoDiscountAmt}">
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${totalPromoDiscountAmt}" var="totalPromoDiscountAmt" />	
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${instantPromoDiscountAmt}" var="instantPromoDiscountAmt" />	
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${webInstantPromoDiscountAmt}" var="webInstantPromoDiscountAmt" />	
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="0" var="zeroCompare" />						
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
				</div>												
			</div>
		</dsp:oparam>
		</dsp:droplet>		
		</dsp:oparam>
		</dsp:droplet>	
	</div>	
		
</div> <!--/.details-container-->



</dsp:page>