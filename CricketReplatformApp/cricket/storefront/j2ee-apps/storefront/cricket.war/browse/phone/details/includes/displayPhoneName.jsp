<dsp:page>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<script type="text/javascript">

	function reloadTopPhoneDisplaySection (productId, skuId, skuimage, contextpath) {
		var url = contextpath + "/browse/phone/details/includes/displayPhoneName.jsp?skuId=" + skuId + "&productId=" + productId;
		$.get(url, function(data) {
			$('#phoneDisplayTopSection').html(data);
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

<dsp:droplet name="IsEmpty">
	<dsp:param name="value" param="Product.id"/>
	<dsp:oparam name="true">
		<dsp:droplet name="ProductLookup">
			<dsp:param name="id" param="productId"/>
			<dsp:param name="filterBySite" value="false"/>
       		<dsp:param name="filterByCatalog" value="false"/>
			<dsp:param name="elementName" value="Product"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="product" param="Product"/>
			</dsp:oparam>
		</dsp:droplet>
	</dsp:oparam>
	<dsp:oparam name="false">
		<dsp:getvalueof var="product" param="Product"/>
	</dsp:oparam>
</dsp:droplet>
<dsp:getvalueof var="productIdForReload" value="${product.id}"/>
<section id="product-details" class="phone-details-product">
	
  
   <!-- Mobile Version - has a swiper gallery of images, and title in a different location -->
  <div class="row">
    <div class="show-for-small columns small-12 text-center">
	    <dsp:droplet name="/com/cricket/browse/droplet/DisplayNameDroplet">
			<dsp:param name="displayName" value="${product.displayName}" />					
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
    </div>
  </div>
  
  <!-- Desktop Version 3 columns - thumbs / gallery / details -->
  <div class="row">
    <div class="columns large-12 small-12 large-centered">
      <div class="row" style="position:relative;">
		<!-- START displaying SKU thumbnail images -->
		<div id="product-thumbs" class="columns large-2 small-12 large-offset-0 text-center">
			<ul>
	   			<dsp:droplet name="/atg/commerce/catalog/SKULookup">
					<dsp:param name="id" param="skuId"/>
					<dsp:param name="elementName" value="sku"/>	
					<dsp:param name="filterBySite" value="false"/>
					<dsp:param name="filterByCatalog" value="false"/>
						<dsp:oparam name="output">		
							<dsp:getvalueof var="sku" param="sku" scope="request"/>
								<dsp:droplet name="/atg/dynamo/droplet/ForEach">
									<dsp:param name="array" param="sku.rotatingImages"/>
									<dsp:param name="elementName" value="rotatingImagesInfo"/>								                                                              
										<dsp:oparam name="output"> 
										<dsp:getvalueof var="rotatingImagesInfo" param="rotatingImagesInfo"/>
										<dsp:getvalueof var="thumbImageHeight" bean="ImageConfiguration.height.phoneDetailThumbImage" />
										<dsp:getvalueof var="mainImageHeight" bean="ImageConfiguration.height.phoneDetailMainImage" />
											<dsp:getvalueof var="url" param="rotatingImagesInfo.url"></dsp:getvalueof>
											<li>  
												<!-- store LiquidPixel url into liquidPixelMainImage variable  -->	
												<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
													<dsp:param name="imageLink" value="${url}" />
													<dsp:param name="imageHeight" value="${mainImageHeight}" />
													<dsp:oparam name="output">
													<dsp:getvalueof var="liquidPixelMainImage" param="url"/>
														<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
															<dsp:param name="imageLink" value="${url}" />
															<dsp:param name="imageHeight" value="${thumbImageHeight}" />
															<dsp:oparam name="output">
															<dsp:getvalueof var="liquidpixelRotatingImage" param="url"/>
																<!-- store LiquidPixelurl url in liquidpixelRotatingImage variable and pass to script to change the main product image when clicked-->
																<span onclick="javascript:changeProductImageAndPricing('${liquidPixelMainImage}')">
																 <a href="javascript:void()" class="">
																 	<img src="${liquidpixelRotatingImage}" alt=""></img>
																 </a>
																 </span>    
															</dsp:oparam>
														</dsp:droplet>
													</dsp:oparam>
												</dsp:droplet>
											</li>			
									</dsp:oparam>
								</dsp:droplet>
						</dsp:oparam>
					</dsp:droplet>	
				</ul>
			</div>	
	   <!-- END displaying SKU thumbnail images -->	
		 							
		<dsp:getvalueof var="productlargeImageUrl" value="${product.fullImage.url}"></dsp:getvalueof>
		<dsp:getvalueof var="height" bean="ImageConfiguration.height.phoneDetailMainImage" />
		<div id="product-image" class="columns large-5 small-12 text-center">
			<!-- START Liquid Pixel Image display droplet -->
				<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
					<dsp:param name="imageLink" value="${productlargeImageUrl}" />
					<dsp:param name="imageHeight" value="${height}"/>
					<dsp:oparam name="output">
					<dsp:getvalueof var="productliquidpixelImage" param="url"/>
						<dsp:img src="${productliquidpixelImage}" alt=""></dsp:img>
					</dsp:oparam>
				</dsp:droplet>
			 <!--END Liquid Pixel Image display droplet-->
	
			<dsp:getvalueof var="largeImageName" param="childSKUsInfo.displayName"></dsp:getvalueof>
			<input type="hidden" id="product-sku" value="${largeImageName}"/>
		</div>
		
        <!-- Show this section for both Desktop & Mobile -->
        <div id="product-content" class="columns large-5 small-12 content">         	
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
							<dsp:param name="array" value="${product.childSKUs}" />
							<dsp:getvalueof var="size" param="size" />
							<dsp:oparam name="outputStart">
								<c:if test="${size>1}">
									<p class="color-title">
										<crs:outMessage key="cricket_phonedetails_colors"/>
									</p>
								</c:if>
							</dsp:oparam>
						</dsp:droplet>
          	<p class="colors">	
			<dsp:droplet name="/atg/dynamo/droplet/ForEach">
				<dsp:param name="array" value="${product.childSKUs}"/>
					<dsp:param name="elementName" value="childSKUsInfo"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="childSKUsInfo" param="childSKUsInfo"/>							
						<dsp:getvalueof var="skuId" param="childSKUsInfo.id"></dsp:getvalueof>
					 	<dsp:getvalueof var="swatchName" param="childSKUsInfo.swatch.name"></dsp:getvalueof>
					 	<dsp:getvalueof var="skuSwatchLargeImage" param="childSKUsInfo.largeImage.url"/>			
					  	<dsp:getvalueof var="swatchImg" param="childSKUsInfo.swatch.url"></dsp:getvalueof>
					  	<dsp:getvalueof var="skuId" param="childSKUsInfo.id"></dsp:getvalueof>
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
											<a href="javascript:reloadTopPhoneDisplaySection('${productIdForReload}','${skuId}','${skuLargeImage}','${contextpath}')" id="${swatchName}" class="color-swatch1">
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
		  	<!-- This section is to show Price, promotion and final price based on the color / Sku selected --><!-- Defect id Fix for 5865-->
			<dsp:droplet name="/atg/commerce/catalog/SKULookup">
				<dsp:param name="id" param="skuId"/>
				<dsp:param name="elementName" value="sku"/>	
				<dsp:param name="filterBySite" value="false"/>
				<dsp:param name="filterByCatalog" value="false"/>
				<dsp:oparam name="output">
					<dsp:include page="/browse/phone/details/includes/displaySkuPricing.jsp">
						<dsp:param name="product" value="${product}"/>
						<dsp:param name="sku" param="sku"/>
						<c:if test="${product.type!=null && product.type eq 'phone-product'}">
							<dsp:param name="isFinanceAvailable" value="${product.isFinancingAvailable}"/>
						</c:if>
					</dsp:include>
				</dsp:oparam>
			</dsp:droplet> 
			<!-- End Defect id Fix for 5865-->        
          <div class="prices">   
            <!-- Desktop Zip Code Message -->
            <div data-alert class="alert-box text-left hide-for-small">
              <crs:outMessage key="cricket_phonedetails_pleaseEnterzipcode"/><br />
              <a href="#"  onClick="" class="circle-arrow"><crs:outMessage key="cricket_phonedetails_EnterZipcode"/></a>
            </div>
            
            <!-- Mobile Zip Code Message, slightly different from Desktop version -->
            <div data-alert class="alert-box show-for-small">
           		<crs:outMessage key="cricket_phonedetails_Pleaseenteryour"/>
           		<a href="#" onClick="#"><crs:outMessage key="cricket_phonedetails_zipcode"/></a><crs:outMessage key="cricket_phonedetails_toseepricingandaddtoyourcart"/>
            </div>
            
          </div>
          <div class="text-right right">
            <a class="button text-right disabled secondary has-tip" data-tooltip=""  title="Please enter your zip code to continue shopping."><crs:outMessage key="cricket_phonedetails_AddtoCart"/></a>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
</dsp:page>