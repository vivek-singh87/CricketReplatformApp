<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayMdnPhoneNumberDroplet"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="upgradePhone" param="upgradePhone"/>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="allphonesCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>			

<div class="row">
	<div id="order-list" class="large-12 small-12 columns hide-for-small">
		<div id="phone-upgrade" class="row">
			<div class="columns large-12 small-12">
			  <p class="title" data-section-title><crs:outMessage key="cricket_checkout_PHONE_UPGRADEFOR"/><!-- PHONE UPGRADE FOR: --> 
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
			<div class="row">
			  <div class="columns large-12 small-12"><h4><crs:outMessage key="cricket_checkout_new_phone"/><!-- New Phone --></h4></div>
			</div>
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
				<div class="row items">	
					<div class="package-item phone columns large-2 small-12 text-center">
						<dsp:getvalueof var="imageURL"  value="${phone.fullImage.url}"/>
							<!--Start ImageURLLookupDroplet used to show images from Liquid Pixel Server -->
							<dsp:getvalueof var="height" bean="ImageConfiguration.height.checkoutThumbnailImage" />		
							<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
								<dsp:param name="imageLink" value="${imageURL}" />
								<dsp:param name="imageHeight" value="${height}" />
								<dsp:param name="protocalType" value="secure"/>
								<dsp:oparam name="output">
								<dsp:getvalueof var="liquidpixelurl" param="url"/>
									<dsp:img src="${liquidpixelurl}" alt="${phone.displayName}" />
								</dsp:oparam>
							</dsp:droplet>
							<!--End ImageURLLookupDroplet used to show images from Liquid Pixel Server -->
					</div>	
					
					<div class="package-item columns large-6 small-12">
					<p class="bold">${phone.displayName}</p>					
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
					</div>
				
				 <div class="package-item columns large-4 small-12">
						<p class="bold">
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
						</p>
						<p class="small">
						  
							<c:if test="${not empty rebateAmt or not empty totalPromoDiscountAmt}">
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${totalPromoDiscountAmt}" var="totalPromoDiscountAmt" />	
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="0" var="zeroCompare" />		
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
				</div>												
				</div>
			</dsp:oparam>
			</dsp:droplet>	
			</dsp:oparam>
		</dsp:droplet>
		</div>
	</div>
</div>	
</dsp:page>