<dsp:page>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/atg/targeting/TargetingRange"/>
<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>
<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
<dsp:getvalueof var="manuallyEnteredZipCode" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
<dsp:getvalueof var="mobileSuffix" bean="CricketConfiguration.mobileSuffix"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>

	<section id="section-recommended-phones">
			<div class="row row-container">
				<div class="row">
					<div class="large-10 small-12 columns">
						<h3><crs:outMessage key="cricket_planrecommendphone_GetThePhone"/><!-- Get The Phone --> <span><crs:outMessage key="cricket_planrecommendphone_YouReallyWant"/><!-- You Really Want --></span></h3>
					</div>
					<div class="large-2 small-12 columns see-all hide-for-small">
						<dsp:droplet name="DimensionValueCacheDroplet">
							<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="phonesCategoryIdCacheEntry" param="dimensionValueCacheEntry" />
					      	</dsp:oparam>
						</dsp:droplet>
						<a href="${contextPath}${phonesCategoryIdCacheEntry.url}" class="circle-arrow"><crs:outMessage key="cricket_planrecommendphone_SeeAllPhones"/><!-- See all phones --></a>
					</div>
				</div>
			
				<!-- Recommended Phones Mobile View -->		
				<div class="row show-for-small">
					<div class="columns small-12 text-center three-callouts">    
						<a href="#" class="prev show-for-small"><crs:outMessage key="cricket_planrecommendphone_Prev"/><!-- Prev --></a>
						<a href="#" class="next show-for-small"><crs:outMessage key="cricket_planrecommendphone_Next"/><!-- Next --></a>
						<div class="swiper-container show-for-small"> 
							<div class="swiper-wrapper">
								<dsp:droplet name="TargetingRange">
									<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/PlanListingFeaturedPhones${mobileSuffix}" name="targeter"/>
									<dsp:param name="start" value="1"/>
			  						<dsp:param name="howMany" value="4"/>
									<dsp:oparam name="output">
										<div class="swiper-slide plan-item">
											<div class="recommended-phone-container">
												<dsp:getvalueof var="productId" param="element.id"/>
												<dsp:getvalueof var="skuId" param="element.childSkus[0].id"/>
												<!-- START This is used for SEO Url droplet. -->
												<dsp:droplet name="GetSeoStringDroplet">
													<dsp:param name="product" param="element"/>
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
												<dsp:getvalueof param="element.fullImage.url" var="imageURL"/>												
												<a href="${contextPath}${canonicalUrl}">
												<!--Start Droplet used to show images from Liquid Pixel Server -->
												<dsp:getvalueof var="height" bean="ImageConfiguration.height.phoneDetailRecommendedProduct" />
													<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
														<dsp:param name="imageLink" value="${imageURL}" />
														<dsp:param name="imageHeight" value="${height}"/>
														<dsp:oparam name="output">
														<dsp:getvalueof var="liquidpixelurl" param="url"/>
															<dsp:img src="${liquidpixelurl}" class="recommended-phone-image"/>
														</dsp:oparam>
													</dsp:droplet>
												<!--End Droplet used to show images from Liquid Pixel Server -->	
												</a>
												<span class="recommended-phone-name"><dsp:valueof valueishtml="true" param="element.displayName"/></span>
												<!-- Changes done as part of defect 5819 - updates from TPN -->
												<a href="${contextpath}${canonicalUrl}" class="button medium">Shop Now</a>
												<!-- <c:choose>
													<c:when test="${manuallyEnteredZipCode eq true}">
														 <dsp:include page="/cart/common/addToCart.jsp">
															<dsp:param name="addToCartClass" value="button medium"/>
															<dsp:param name="productId" value="${productId}"/>
															<dsp:param name="skuId" value="${skuId}"/>
															<dsp:param name="quantity" value="1"/>
															<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
														</dsp:include>
													</c:when>
													<c:otherwise>
														<a href="#" class="button disabled secondary grey-add-cart"><crs:outMessage key="cricket_planrecommendphone_AddtoCart"/>-->														
														<!-- Add To Cart --><!--</a>
													</c:otherwise>
												</c:choose> 
												<a href="${contextPath}${canonicalUrl}" class="circle-arrow"><crs:outMessage key="cricket_planrecommendphone_LearnMore"/>-->
												<!-- Learn More --><!--</a>-->
											</div>
										</div>
									</dsp:oparam>
								</dsp:droplet>
							</div>
						</div> <!--/.swiper-container-->
					</div>				
				</div> <!--/ END Recommended Phones Mobile View  -->	
				
				<!-- Recommended Phones Desktop View -->		
				<div class="row hide-for-small">
					<dsp:droplet name="TargetingRange">
						<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/PlanListingFeaturedPhones" name="targeter"/>
						<dsp:param name="start" value="1"/>
			  			<dsp:param name="howMany" value="4"/>
						<dsp:oparam name="output">
							<div class="large-3 small-12 columns">
								<div class="recommended-phone-container">
									<dsp:getvalueof var="productId" param="element.id"/>
									<dsp:getvalueof var="skuId" param="element.childSkus[0].id"/>
									<!-- START This is used for SEO Url droplet. -->
									<dsp:droplet name="GetSeoStringDroplet">
										<dsp:param name="product" param="element"/>
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
									<dsp:getvalueof param="element.fullImage.url" var="imageURL"/>
									<a href="${contextPath}${canonicalUrl}">
									<!--Start Droplet used to show images from Liquid Pixel Server -->
									<dsp:getvalueof var="height" bean="ImageConfiguration.height.phoneDetailRecommendedProduct" />
										<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
											<dsp:param name="imageLink" value="${imageURL}" />
											<dsp:param name="imageHeight" value="${height}"/>
											<dsp:oparam name="output">
											<dsp:getvalueof var="liquidpixelurl" param="url"/>
												<dsp:img src="${liquidpixelurl}" class="recommended-phone-image"/>
											</dsp:oparam>
										</dsp:droplet>
									<!--End Droplet used to show images from Liquid Pixel Server -->	
									</a>
									<span class="recommended-phone-name"><dsp:valueof valueishtml="true" param="element.displayName"/></span>
									<c:choose>
										<c:when test="${manuallyEnteredZipCode eq true}">
											 <dsp:include page="/cart/common/addToCart.jsp">
												<dsp:param name="addToCartClass" value="button medium"/>
												<dsp:param name="productId" value="${productId}"/>
												<dsp:param name="skuId" value="${skuId}"/>
												<dsp:param name="quantity" value="1"/>
												<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
											</dsp:include>
										</c:when>
										<c:otherwise>
											<a href="#" class="button disabled secondary grey-add-cart has-tip" data-tooltip="" title="Please enter your zip code to see pricing and continue shopping."><crs:outMessage key="cricket_planrecommendphone_AddtoCart"/><!-- Add To Cart --></a>
										</c:otherwise>
									</c:choose>
									<a href="${contextPath}${canonicalUrl}" class="circle-arrow"><crs:outMessage key="cricket_planrecommendphone_LearnMore"/><!-- Learn More --></a>
								</div>
							</div>
						</dsp:oparam>
					</dsp:droplet>
				</div> <!--/ END Recommended Phones Desktop View  -->
		
		</div> <!--/.row-container -->
		
		<div id="foot-see-all" class="row show-for-small">
			<div class="small-centered columns">
				<a href="${contextPath}${phonesCategoryIdCacheEntry.url}" class="circle-arrow"><crs:outMessage key="cricket_planrecommendphone_SeeAllPhones"/><!-- See all phones --></a>
			</div>
		</div>			
		
	</section>
</dsp:page>