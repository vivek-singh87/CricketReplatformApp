<dsp:page>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>
<!-- Similar Phones Mobile -->	
	<section id="similar-phones" class="three-callouts">
		<div class="row">
			<div class="large-8 small-12 columns">
				<h2><crs:outMessage key="cricket_phonedetails_Similar"/><!-- Similar --> <span><crs:outMessage key="cricket_phonedetails_Phones"/><!-- Phones --></span></h2>
			</div>
			<!-- NEW: Added in the div below for see all phones link to right side of similar phones -->
		    <div class="large-4 small-12 hide-for-small columns">
			    <dsp:droplet name="DimensionValueCacheDroplet">
					<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="phonesCategoryIdCacheEntry" param="dimensionValueCacheEntry" />
			      </dsp:oparam>
				</dsp:droplet>
		      <a href="${contextpath}${phonesCategoryIdCacheEntry.url}" class="circle-arrow right"><crs:outMessage key="cricket_phonedetails_SeeAllPhones"/><!-- See All Phones --></a>
		    </div>
		</div>
		<!-- Mobile Version -->
		<a href="#" class="prev show-for-small"><crs:outMessage key="cricket_phonedetails_Prev"/><!-- Prev --></a>
		<a href="#" class="next show-for-small"><crs:outMessage key="cricket_phonedetails_Next"/><!-- Next --></a>	
		<!--This is the included page in phone_details.jsp which displays phones those are similar to the selected phone. -->
		<div class="swiper-container show-for-small">
			<div class="swiper-wrapper">		
					<dsp:droplet name="/atg/dynamo/droplet/ForEach">
						<dsp:param name="array" param="Product.fixedRelatedProducts"/>
						<dsp:param name="elementName" value="fixedRelatedProductsInfo"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="productId" param="fixedRelatedProductsInfo.id"/>																	
								<dsp:getvalueof var="skuId" param="fixedRelatedProductsInfo.childSkus[0].id"/>
								<!-- START This is used for SEO Url droplet. -->
								<dsp:droplet name="GetSeoStringDroplet">
									<dsp:param name="product" param="fixedRelatedProductsInfo"/>
									<dsp:oparam name="output">
										<dsp:getvalueof var="seoString" param="seoString"/>
									</dsp:oparam>
								</dsp:droplet>
								<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
									<dsp:param name="seoString" value="${seoString}"/>
									<dsp:param name="id" value="${productId}"/>
									<dsp:param name="itemDescriptorName" value="phone-product"/>
									<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
									<dsp:oparam name="output">
										<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
									</dsp:oparam>
								</dsp:droplet>	
								<!-- END This is used for SEO Url droplet. -->					
								<!-- First Slide -->
								<div class="swiper-slide text-center"> 
									<dsp:getvalueof var="fullImageUrl" param="fixedRelatedProductsInfo.fullImage.url"></dsp:getvalueof>
									<dsp:getvalueof var="height" bean="ImageConfiguration.height.phoneDetailRecommendedProduct" />
									<a href="${contextpath}${canonicalUrl}">
									<!--	<dsp:img src="${fullImageUrl}" alt="${fixedRelatedProductsInfo.fullImage}"></dsp:img> -->
											<!--START Liquid Pixel Image display droplet-->
												<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
												<dsp:param name="imageLink" value="${fullImageUrl}" />
												<dsp:param name="imageHeight" value="${height}"/>
												<dsp:oparam name="output">
												<dsp:getvalueof var="liquidpixelurl" param="url"/>
												<dsp:img src="${liquidpixelurl}" alt="${fixedRelatedProductsInfo.fullImage}"></dsp:img>
												</dsp:oparam>
										 </dsp:droplet>
										 <!--END Liquid Pixel Image display droplet-->
									</a>
									<!-- <a href="#"><img src="${contextpath}/img/phone-detail/similar-phone-1.png" alt=""/></a> -->
									<h4><dsp:valueof valueishtml="true" param="fixedRelatedProductsInfo.displayName"/></h4>
									<a href="${contextpath}${canonicalUrl}" class="button"><crs:outMessage key="cricket_phonedetails_ShopNow"/><!-- Shop Now --></a>
								</div>	
						</dsp:oparam>
					</dsp:droplet>
				</div>	
			</div>
			<div class="row cta show-for-small">
				<div class="small-12 columns">
					<p><a href="${contextpath}${phonesCategoryIdCacheEntry.url}" class="circle-arrow"><crs:outMessage key="cricket_phonedetails_SeeAllPhones"/><!-- See All Phones --></a></p>
				</div>
			</div>			
			<!-- Desktop Version -->
			<div class="row hide-for-small">
			<!--This is the included page in phone_details.jsp which displays phones those are similar to the selected phone. -->
				<dsp:droplet name="/atg/dynamo/droplet/ForEach">
					<dsp:param name="array" param="Product.fixedRelatedProducts"/>
					<dsp:param name="elementName" value="fixedRelatedProductsInfo"/>
						<dsp:oparam name="output">
						<dsp:getvalueof var="productId" param="fixedRelatedProductsInfo.id"/>
							<dsp:getvalueof var="skuId" param="fixedRelatedProductsInfo.childSkus[0].id"/>
							<!-- START This is used for SEO Url droplet. -->
								<dsp:droplet name="GetSeoStringDroplet">
									<dsp:param name="product" param="fixedRelatedProductsInfo"/>
									<dsp:oparam name="output">
										<dsp:getvalueof var="seoString" param="seoString"/>
									</dsp:oparam>
								</dsp:droplet>
								<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
									<dsp:param name="seoString" value="${seoString}"/>
									<dsp:param name="id" value="${productId}"/>
									<dsp:param name="itemDescriptorName" value="phone-product"/>
									<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
									<dsp:oparam name="output">
										<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
									</dsp:oparam>
								</dsp:droplet>	
							<!-- END This is used for SEO Url droplet. -->					
							<div class="large-3 small-12 columns text-center">
							 <dsp:getvalueof var="fullImageUrl" param="fixedRelatedProductsInfo.fullImage.url"></dsp:getvalueof>
							 <dsp:getvalueof var="height" bean="ImageConfiguration.height.phoneDetailRecommendedProduct" />									         								         
									<a href="${contextpath}${canonicalUrl}">
									<!--START Liquid Pixel Image display droplet-->
											<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
											<dsp:param name="imageLink" value="${fullImageUrl}" />
											<dsp:param name="imageHeight" value="${height}"/>
											<dsp:oparam name="output">
											<dsp:getvalueof var="liquidpixelurl" param="url"/>
											<dsp:img src="${liquidpixelurl}" alt="${fixedRelatedProductsInfo.fullImage}"></dsp:img>
											</dsp:oparam>
									 </dsp:droplet>
									 <!--END Liquid Pixel Image display droplet-->
									</a>										
								<h4><dsp:valueof valueishtml="true" param="fixedRelatedProductsInfo.displayName"/></h4>
								<a href="${contextpath}${canonicalUrl}" class="button"><crs:outMessage key="cricket_phonedetails_ShopNow"/><!-- Shop Now --></a>
							</div>
						</dsp:oparam>
				</dsp:droplet>																	
			</div>		
		</section>	
</dsp:page>