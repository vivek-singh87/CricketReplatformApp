<dsp:page>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
	<dsp:importbean bean="/com/cricket/browse/ThresholdMessageDroplet"/>
	<dsp:importbean bean="/atg/commerce/inventory/InventoryLookup"/>
	<dsp:getvalueof var="throughSearch" param="throughSearch"/>
	<c:choose>
		<c:when test="${throughSearch eq true}">
			<dsp:getvalueof var="productId" param="product.id"/>
			<dsp:getvalueof var="skuId" param="product.childSkus[0].id"/>
			<dsp:getvalueof var="imageURL" param="product.fullImage.url"/>
		</c:when>
		<c:otherwise>
			<dsp:getvalueof var="productId" param="product.productId"/>
			<dsp:getvalueof var="skuId" param="product.childSkus[0].skuId"/>
			<c:set var="imageURL" value="" />
			<dsp:getvalueof var="largeImagesList" param="product.largeImages"/>
			<c:if test="${fn:length(largeImagesList) > 0}">				
				<dsp:getvalueof var="imageURL" value="${largeImagesList[0]}"/>
			</c:if>
		</c:otherwise>
	</c:choose>
	
	<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
		<dsp:param name="id" value="${productId}"/>
		<dsp:param name="itemDescriptorName" value="accessory-product"/>
		<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
		</dsp:oparam>
	</dsp:droplet>
	
	
	
	<dsp:getvalueof var="size" param="size"/>
	<dsp:getvalueof var="count" param="count"/>
	<div class="swiper-slide result-content">
		<h4>
			<dsp:a href="${contextpath}${canonicalUrl}" id="${productId}">
				<dsp:valueof param="product.displayName"/>
			</dsp:a>
		</h4>	
		<a href="${contextpath}${canonicalUrl}">
		<!--	<dsp:img style="height: 260px; width: 126px" src="${contextpath}/${imageURL}" alt=""/> -->
			<!--START Liquid Pixel Image display droplet-->
			<dsp:getvalueof var="height" bean="ImageConfiguration.height.accessoryListingThumb" />
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
					<dsp:param name="imageLink" value="${imageURL}" />
					<dsp:param name="imageHeight" value="${height}"/>
					<dsp:oparam name="output">
					<dsp:getvalueof var="liquidpixelurl" param="url"/>
						  
					<dsp:img src="${liquidpixelurl}" alt=""/>
					</dsp:oparam>
			 </dsp:droplet>
			 <!--END Liquid Pixel Image display droplet-->
		</a>
		<!-- Display Price -->
		<p class="green-price">				
			<dsp:include page="/browse/includes/discountTag_price.jsp">
				<dsp:param name="productId" value="${productId}"/>
				<dsp:param name="skuId" value="${skuId}" />
			</dsp:include>
			<dsp:getvalueof var="finalPrice" value="${finalPrice}"/> 
			<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${finalPrice}" var="finalPrice" />
			<c:set var="splitPrice" value="${fn:split(finalPrice, '.')}"/>	
			<sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup>				
		</p>
		<!-- Changes done as part of defect 5819 - updates from TPN -->
		<a href="${contextpath}${canonicalUrl}" class="button medium">Shop Now</a>
		<!--
		<dsp:droplet name="/atg/commerce/catalog/SKULookup">
			<dsp:param name="id" value="${skuId}"/>
			<dsp:param name="elementName" value="sku"/>	
				<dsp:oparam name="output">											 				
					<dsp:droplet name="InventoryLookup">
					   <dsp:param name="itemId" value="${skuId}"/>
				  			<dsp:oparam name="output">					
						     	<dsp:droplet name="ThresholdMessageDroplet">
							     	<dsp:param name="threshold" param="threshold"/>
							     	<dsp:param name="stockLevel" param="inventoryInfo.stockLevel"/>
									<dsp:param name="thresholds" param="sku.thresholds"/> 
							     	<dsp:oparam name="output">
							     	 	<dsp:getvalueof var="isOutOfStock" param="isOutOfStock"/>
							     	</dsp:oparam>
						    	 </dsp:droplet>
				       	 	</dsp:oparam>
				     </dsp:droplet>										       
				</dsp:oparam>
		</dsp:droplet>
		<c:choose>
			<c:when test="${isOutOfStock}">
				<a  class="grey-add-cart has-tip" style="background-color: #E63924; color: #FFFFFF;" data-tooltip="" title="Sorry, this item is out of stock.">Add to Cart</a>
			</c:when>
			<c:otherwise>
				<dsp:include page="/cart/common/addToCart.jsp">
					<dsp:param name="addToCartClass" value="green-add-cart"/>
					<dsp:param name="productId" value="${productId}"/>
					<dsp:param name="skuId" value="${skuId}"/>
					<dsp:param name="quantity" value="1"/>
					<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
				</dsp:include>
			</c:otherwise>
		</c:choose>			
		<a href="${contextpath}${canonicalUrl}" class="circle-arrow">Learn More</a>
		-->
	</div>
</dsp:page>