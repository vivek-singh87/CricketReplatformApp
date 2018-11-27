<dsp:page>
<dsp:importbean bean="/atg/commerce/inventory/InventoryLookup"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/com/cricket/browse/ThresholdMessageDroplet"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<script type="text/javascript">

	function reloadTopPhoneDisplaySection (productId, skuId, skuimage, contextpath) {
		var url = contextpath + "/browse/accessories/details/includes/accessories_details_zipcode.jsp?skuId=" + skuId + "&productId=" + productId;
		$.get(url, function(data) {
			$('#accessoryDisplayTopSection').html(data);
			var image =  skuimage;
			changeProductImageAndPricing(image);
		});
	}
	function changeProductImageAndPricing(url) {            
		var thumbSrc = url;	
		var imageSrc = thumbSrc.replace(/thumb\//g,"");	
		    document.getElementById('product-image').innerHTML = '<img src="'+ imageSrc +'">';               
	}
</script>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsEmpty"/>
<!-- IsEmpty - Check the accessoryid is there if there it will go to false condition if not there use productlook up droplet to get accessoryid  -->
<dsp:droplet name="IsEmpty">
	<dsp:param name="value" param="accessory.id"/>
	<dsp:oparam name="true">
		<dsp:droplet name="ProductLookup">
			<dsp:param name="id" param="productId"/>
			<dsp:param name="filterBySite" value="false"/>
       		<dsp:param name="filterByCatalog" value="false"/>
			<dsp:param name="elementName" value="accessory"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="accessory" param="accessory"/>
			</dsp:oparam>
		</dsp:droplet>
	</dsp:oparam>
	<dsp:oparam name="false">
		<dsp:getvalueof var="accessory" param="accessory"/>
	</dsp:oparam>
</dsp:droplet>
<dsp:getvalueof var="productIdForReload" value="${accessory.id}"/>
<section id="product-details" class="accessory-details-product">			
	
 <!-- Mobile Version - has a swiper gallery of images, and title in a different location -->
  <div class="row">
    <div class="show-for-small columns small-12 text-center">
   <%--  <dsp:getvalueof  var="accDisplayName" param="accessory.displayName"/> --%>
    <dsp:droplet name="/com/cricket/browse/droplet/DisplayNameDroplet">
		<dsp:param name="displayName" value="${accessory.displayName}" />					
		<dsp:oparam name="output">
			<dsp:getvalueof var="displayName" param="displayName"/>
			<dsp:getvalueof var="hasSpecialSymbol" param="hasSpecialSymbol"/>
			<dsp:getvalueof var="firstString" param="firstString"/>
			<dsp:getvalueof var="secondString" param="secondString"/>
			<dsp:getvalueof var="specialSymbol" param="specialSymbol"/>
			<c:choose>
				<c:when test="${hasSpecialSymbol eq true}">	
					<h2 class="show-for-small">${firstString}<sup>${specialSymbol}</sup>${secondString}</h2>								
				</c:when>	
				<c:otherwise>
					<h2 class="show-for-small">${displayName}</h2>
				</c:otherwise>
			</c:choose>
		</dsp:oparam>
	 </dsp:droplet>
    <%--   <h1>${accDisplayName}</h1> --%>
    </div>
  </div>

<dsp:getvalueof var="skuId" param="skuId"></dsp:getvalueof>
<!-- Desktop -->
  <div class="row">
    <div class="columns large-12 small-12 large-centered">
      <div class="row" id="accessory-/cricket/img-gallery">
      	<div id="product-thumbs" class="columns large-2 small-12 large-offset-0 text-center">
			<ul style="padding-top: 60px;" class="hide-for-small">
	      		<dsp:droplet name="/atg/commerce/catalog/SKULookup">				
					<dsp:param name="id" param="skuId"/>
					<dsp:param name="elementName" value="sku"/>	
					<dsp:oparam name="output">				 		
							<dsp:getvalueof var="sku" param="sku" scope="request"/>
							<dsp:droplet name="/atg/dynamo/droplet/ForEach">
								<dsp:param name="array" param="sku.rotatingImages"/>
								<dsp:param name="elementName" value="rotatingImagesInfo"/>                                                                   
									<dsp:oparam name="output">
										<dsp:getvalueof var="rotatingImagesInfo" param="rotatingImagesInfo"/>
										<dsp:getvalueof var="url" param="rotatingImagesInfo.url"></dsp:getvalueof>											
										<dsp:getvalueof var="thumbImageHeight" bean="ImageConfiguration.height.accessoryDetailThumbImage" />
										<dsp:getvalueof var="mainImageHeight" bean="ImageConfiguration.height.phoneDetailMainImage" />																					
											<li>  
											<!-- store LiquidPixel url into liquidPixelMainImage variable  -->		
												<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
													<dsp:param name="imageLink" value="${url}" />
													<dsp:param name="imageHeight" value="${mainImageHeight}" />
													<dsp:oparam name="output">
													<dsp:getvalueof var="liquidPixelMainImage" param="url"/>
														<!-- START Liquid Pixel Image display droplet -->
														<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
															<dsp:param name="imageLink" value="${url}" />
															<dsp:param name="imageHeight" value="${thumbImageHeight}" />
															<dsp:oparam name="output">
															<dsp:getvalueof var="liquidpixelRotatingImage" param="url"/>
																<!-- store LiquidPixelurl url in liquidpixelRotatingImage variable and pass to script to change the main product image when clicked-->
																<a href="javascript:changeProductImageAndPricing('${liquidPixelMainImage}')" class="">
															 		<img src="${liquidpixelRotatingImage}" alt=""></img>
															 	</a>    
															</dsp:oparam>
														</dsp:droplet>
													</dsp:oparam>
												</dsp:droplet>
											</li>
									 		<!--END Liquid Pixel Image display droplet-->
									</dsp:oparam>
								</dsp:droplet>
					</dsp:oparam>
				</dsp:droplet>
           </ul>
  		</div> 
        <!-- This section is to display of full image when onclick of rotating images of a particular sku -->
        <dsp:getvalueof var="largeImageUrl" value="${accessory.fullImage.url}"></dsp:getvalueof>
		<dsp:getvalueof var="height" bean="ImageConfiguration.height.accessoryDetailMainImage" />
			<div id="product-image" class="columns large-5 small-12 text-center">
				 	<!--Start Droplet used to show images from Liquid Pixel Server -->
						<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
							<dsp:param name="imageLink" value="${largeImageUrl}" />
							<dsp:param name="imageHeight" value="${height}"/>
							<dsp:oparam name="output">
							<dsp:getvalueof var="liquidpixelurl" param="url"/>
								<dsp:img src="${liquidpixelurl}" alt=""></dsp:img>
							</dsp:oparam>
						</dsp:droplet>
					<!--End Droplet used to show images from Liquid Pixel Server -->								
					
					<dsp:getvalueof var="largeImageName" param="childSKUsInfo.displayName"></dsp:getvalueof>
					<input type="hidden" id="product-sku" value="${largeImageName}"/>		
			</div>
			
        <div id="product-content" class="columns large-5 small-12 content ">
          <%-- <h1 class="hide-for-small"><dsp:valueof value="${accessory.displayName}"/></h1> --%>
          <c:choose>
				<c:when test="${hasSpecialSymbol eq true}">	
					<h1 class="hide-for-small">${firstString}<sup>${specialSymbol}</sup>${secondString}</h1>								
				</c:when>	
				<c:otherwise>
					<h1 class="hide-for-small">${displayName}</h1>
				</c:otherwise>
			</c:choose>
			<!-- Dynamically load all SKUs for this phone; identify here by a colour but in real use would just be a product sku number-->
				<!-- Showing Colors only when product have more than one sku -->
						<dsp:droplet name="/atg/dynamo/droplet/ForEach">
							<dsp:param name="array" value="${accessory.childSKUs}" />
							<dsp:getvalueof var="size" param="size" />
							<dsp:oparam name="outputStart">
								<c:if test="${size>1}">
									<p class="color-title">
										<crs:outMessage key="cricket_accessoridetails_Swatches"/>
										<!-- Colors: -->
									</p>
								</c:if>
							</dsp:oparam>
						</dsp:droplet>
          		<p class="colors">	
 				<dsp:droplet name="/atg/dynamo/droplet/ForEach">
					<dsp:param name="array" value="${accessory.childSKUs}"/>
					<dsp:param name="elementName" value="childSKUsInfo"/>
						<dsp:oparam name="output">
							<dsp:getvalueof var="childSKUsInfo" param="childSKUsInfo"/>
							<dsp:getvalueof var="skuId" param="childSKUsInfo.id"></dsp:getvalueof>
					 		<dsp:getvalueof var="skuSwatchLargeImage" param="childSKUsInfo.largeImage.url"/>			
					 		<dsp:getvalueof var="swatchName" param="childSKUsInfo.swatch.name"></dsp:getvalueof>
					 	 	<dsp:getvalueof var="swatchImg" param="childSKUsInfo.swatch.url"></dsp:getvalueof>
					 	 	<dsp:getvalueof var="mainImageHeight" bean="ImageConfiguration.height.phoneDetailMainImage" />
					 	 	<!-- user clicks on swatch image it should call the script and change sku large image to main image -->	
					 	 	
					 	 	<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
								<dsp:param name="imageLink" value="${skuSwatchLargeImage}" />
								<dsp:param name="imageHeight" value="${mainImageHeight}" />
								<dsp:oparam name="output">
								<dsp:getvalueof var="skuLargeImage" param="url"/>
								<dsp:getvalueof var="size" param="size"/>
									<c:choose>
										<c:when test="${size eq 1}">
										</c:when>
										<c:otherwise>								
											 <a href="javascript:reloadTopPhoneDisplaySection('${productIdForReload}','${skuId}','${skuLargeImage}','${contextPath}')" id="${swatchName}" class="color-swatch1">
												 <span>
													<!-- START Liquid Pixel Image display droplet -->
													<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
														<dsp:param name="imageLink" value="${swatchImg}" />
														<dsp:oparam name="output">
														<dsp:getvalueof var="liquidpixelurl" param="url"/>
															<img src="${liquidpixelurl}" alt="" style="padding-bottom: 10px;">
														</dsp:oparam>
													</dsp:droplet>
													<!--END Liquid Pixel Image display droplet-->
												 </span>
											</a>
										</c:otherwise>
									</c:choose>
								 </dsp:oparam>
							</dsp:droplet>
						</dsp:oparam>
				</dsp:droplet>
				</p>
				<dsp:include page="/browse/accessories/details/includes/displayAccessoryPricing.jsp">
						<dsp:param name="sku" value="${accessory.childSKUs[0]}" />								
				</dsp:include>
				<%-- <dsp:droplet name="/atg/commerce/catalog/SKULookup">
						<dsp:param name="id" param="skuId"/>
						<dsp:param name="elementName" value="sku"/>	
						<dsp:oparam name="output">											 				
							<dsp:droplet name="InventoryLookup">
							   <dsp:param name="itemId" param="skuId"/>
						  			<dsp:oparam name="output">
									     <dsp:droplet name="ThresholdMessageDroplet">
									     	<dsp:param name="threshold" param="threshold"/>
									     	<dsp:param name="stockLevel" param="inventoryInfo.stockLevel"/>
											<dsp:param name="thresholds" param="sku.thresholds"/> 
									     	<dsp:oparam name="output">
									     	 	<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
								     	 		<dsp:getvalueof var="isOutOfStock" param="isOutOfStock"/>
									     	</dsp:oparam>
								    	 </dsp:droplet>
						       	 	</dsp:oparam>
						    </dsp:droplet>										       
						</dsp:oparam>
				</dsp:droplet> --%>
				<dsp:droplet name="ThresholdMessageDroplet">
					<dsp:param name="skuId" param="skuId"/>
					<dsp:param name="order" bean="/atg/commerce/ShoppingCart.current"/> 
					<dsp:oparam name="output">
						<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
						<dsp:getvalueof var="isOutOfStock" param="isOutOfStock"/>
					</dsp:oparam>
				</dsp:droplet>
				<div class="full-width-btn  mobile-center desktop-right">
					<c:choose>
						<c:when test="${isOutOfStock eq true}">					 
							<a class="button disabled secondary has-tip" data-tooltip="" title="Sorry, this item is out of stock."><crs:outMessage key="cricket_accessoridetails_AddtoCart"/><!-- Add to Cart --></a>
						</c:when>
						<c:otherwise>
							<dsp:include page="/cart/common/addToCart.jsp">
								<dsp:param name="addToCartClass" value="button"/>
								<dsp:param name="productId" param="productId"/>
								<dsp:param name="skuId" value="${skuId}"/>
								<dsp:param name="quantity" value="1"/>
								<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
							</dsp:include>
						</c:otherwise>
					</c:choose>	            	
			        <p class="shipping-info text-right">
			          <!-- Dynamically load all shipping messages based on threshold Quantity present for particualar SKU -->  
						${thresholdMessage}	
					</p>
	          	</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
</dsp:page>
