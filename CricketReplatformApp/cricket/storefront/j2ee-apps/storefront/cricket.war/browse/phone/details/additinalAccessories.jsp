<dsp:page>
<!-- Accessories -->
<dsp:importbean bean="/atg/commerce/inventory/InventoryLookup"/>
<dsp:importbean bean="/com/cricket/browse/ThresholdMessageDroplet"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>

	<section id="phone-accessories" class="three-callouts">
			<div class="row">
				<div class="large-8 small-12 columns">
					<h2><crs:outMessage key="cricket_phonedetails_Accessories"/><span><crs:outMessage key="cricket_phonedetails_ForYourPhone"/></span></h2>
				</div>
				<div class="large-4 small-12 hide-for-small columns">
					<dsp:droplet name="DimensionValueCacheDroplet">
						<dsp:param name="repositoryId" bean="CricketConfiguration.accessoriesCategoryId"/>
						<dsp:oparam name="output">
							<dsp:getvalueof var="accessoriesCategoryCacheEntry" param="dimensionValueCacheEntry" />
					  </dsp:oparam>
					</dsp:droplet>
					<a href="${contextpath}${accessoriesCategoryCacheEntry.url}" class="circle-arrow right"><crs:outMessage key="cricket_phonedetails_SeeAllAccessories"/></a>
				</div>
			</div>
			<!-- Mobile Version -->
			<a href="#" class="prev show-for-small"><crs:outMessage key="cricket_phonedetails_Prev"/></a>
			<a href="#" class="next show-for-small"><crs:outMessage key="cricket_phonedetails_Next"/></a>
				<div class="swiper-container show-for-small">
					<div class="swiper-wrapper text-center">
						<dsp:droplet name="/atg/dynamo/droplet/ForEach">
							<dsp:param name="array" param="Product.associatedAccessories"/>
							<dsp:param name="elementName" value="associatedAccessoriesInfo"/>
							<dsp:param name="index" value="index"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="index" param="index"/>
									<c:if test="${index lt 4}">
										<dsp:getvalueof var="productId" param="associatedAccessoriesInfo.id"/>
										<dsp:getvalueof var="skuId" param="associatedAccessoriesInfo.childSkus[0].id"/>
										<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
											<dsp:param name="id" value="${productId}"/>
											<dsp:param name="itemDescriptorName" value="accessory-product"/>
											<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
											<dsp:oparam name="output">
												<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
											</dsp:oparam>
										</dsp:droplet>
										<!-- Third Slide -->
											<div class="swiper-slide"> 
												<a href="${contextpath}${canonicalUrl}">
													<dsp:getvalueof var="accesoriesImgs" param="associatedAccessoriesInfo.fullImage.url"/>
													<%-- <img src="${accesoriesImgs}" alt=""/> --%>
													<!-- START Liquid Pixel Image display droplet -->
													<dsp:getvalueof var="height" bean="ImageConfiguration.height.phoneDetailRecommendedProduct" />
													<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
															<dsp:param name="imageLink" value="${accesoriesImgs}" />
															<dsp:param name="imageHeight" value="${height}"/>
															<dsp:oparam name="output">
															<dsp:getvalueof var="liquidpixelurl" param="url"/>															  
																<img src="${liquidpixelurl}" alt=""/>
															</dsp:oparam>
													 </dsp:droplet>
													 <!--END Liquid Pixel Image display droplet-->
												</a>
												<h4><dsp:valueof param="associatedAccessoriesInfo.displayName"></dsp:valueof></h4>
													<div>
														<p class="green-price">
															<dsp:include page="/browse/includes/priceLookup.jsp">
																<dsp:param name="skuId" param="associatedAccessoriesInfo.childSKUs[0].repositoryId"/>
																<dsp:param name="productId" param="associatedAccessoriesInfo.repositoryId"/>
															</dsp:include>
															<c:if test="${empty retailPrice || retailPrice eq 0}">
																<dsp:getvalueof var="retailPrice" param="associatedAccessoriesInfo.childSKUs[0].listPrice"/>
															</c:if>
															<c:set var="finalPrice" value="${retailPrice}"/>			
															<c:set var="splitPrice" value="${fn:split(finalPrice, '.')}"/>			
															<sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup>
																
														</p>
														<%-- <dsp:droplet name="/atg/commerce/catalog/SKULookup">
															<dsp:param name="id"param="associatedAccessoriesInfo.childSKUs[0].repositoryId"/>
															<dsp:param name="elementName" value="sku"/>	
															<dsp:oparam name="output">											 				
																<dsp:droplet name="InventoryLookup">
																   <dsp:param name="itemId" param="associatedAccessoriesInfo.childSKUs[0].repositoryId"/>
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
														<dsp:param name="skuId" param="associatedAccessoriesInfo.childSKUs[0].repositoryId"/>
														<dsp:param name="order" bean="/atg/commerce/ShoppingCart.current"/> 
														<dsp:oparam name="output">
															<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
															<dsp:getvalueof var="isOutOfStock" param="isOutOfStock"/>
														</dsp:oparam>
													</dsp:droplet>
														<!-- <a class="button" href="#">Add to Cart</a> -->
														<c:choose>
															<c:when test="${isOutOfStock eq true}">
																<a class="button disabled secondary has-tip" data-tooltip="" title="Sorry, this item is out of stock."><crs:outMessage key="cricket_accessoridetails_AddtoCart"/><!-- Add to Cart --></a>
															</c:when>
															<c:otherwise>
															<dsp:include page="/cart/common/addToCart.jsp">
																<dsp:param name="addToCartClass" value="button"/>
																<dsp:param name="productId" param="associatedAccessoriesInfo.repositoryId"/>
																<dsp:param name="skuId" param="associatedAccessoriesInfo.childSKUs[0].repositoryId"/>
																<dsp:param name="quantity" value="1"/>
																<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
															</dsp:include>															
															</c:otherwise>
														</c:choose>

									          		</div>
												<p><a href="${contextpath}${canonicalUrl}" class="circle-arrow"><crs:outMessage key="cricket_phonedetails_ViewDetails"/></a></p>
											</div>
									</c:if>
								</dsp:oparam>
						</dsp:droplet>
					</div>
				</div>	
				<div class="row cta show-for-small">
					<div class="small-12 columns">
						<p><a href="${contextpath}${accessoriesCategoryCacheEntry.url}" class="circle-arrow"><crs:outMessage key="cricket_phonedetails_SeeAllAccessories"/></a></p>
					</div>
				</div>
				<!--  Desktop Version -->
				<div class="row hide-for-small">
					<dsp:droplet name="/atg/dynamo/droplet/ForEach">
						<dsp:param name="array" param="Product.associatedAccessories"/>
						<dsp:param name="elementName" value="associatedAccessoriesInfo"/>
						<dsp:param name="index" value="index"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="index" param="index"/>
									<c:if test="${index lt 4}">
										<dsp:getvalueof var="productId" param="associatedAccessoriesInfo.id"/>
										<dsp:getvalueof var="skuId" param="associatedAccessoriesInfo.childSkus[0].id"/>
										<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
											<dsp:param name="id" value="${productId}"/>
											<dsp:param name="itemDescriptorName" value="accessory-product"/>
											<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
											<dsp:oparam name="output">
												<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
											</dsp:oparam>
										</dsp:droplet>
										<div class="large-3 small-12 columns text-center">					
											<a href="${contextpath}${canonicalUrl}">
												<dsp:getvalueof var="accesoriesImgs" param="associatedAccessoriesInfo.fullImage.url"/>
													<%-- <img src="${accesoriesImgs}" alt=""/> --%>
												<!-- START Liquid Pixel Image display droplet -->
												
												 <dsp:getvalueof var="height" bean="ImageConfiguration.height.phoneDetailRecommendedProduct" />
												<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
														<dsp:param name="imageLink" value="${accesoriesImgs}" />
														<dsp:param name="imageHeight" value="${height}" />
														<dsp:oparam name="output">
														<dsp:getvalueof var="liquidpixelurl" param="url"/>															  
															<img src="${liquidpixelurl}" alt=""/>
														</dsp:oparam>
												 </dsp:droplet>
												 <!--END Liquid Pixel Image display droplet-->
											</a>
											<h4><dsp:valueof param="associatedAccessoriesInfo.displayName"></dsp:valueof></h4>										
												<div>
													<p class="green-price">
													<c:set var="retailPrice" value='0'/>
													<dsp:include page="/browse/includes/priceLookup.jsp">
														<dsp:param name="skuId" param="associatedAccessoriesInfo.childSKUs[0].repositoryId"/>
														<dsp:param name="productId" param="associatedAccessoriesInfo.repositoryId"/>
													</dsp:include>
													<c:if test="${empty retailPrice || retailPrice eq 0}">
														<dsp:getvalueof var="retailPrice" param="associatedAccessoriesInfo.childSKUs[0].listPrice"/>
													</c:if>
													<c:set var="finalPrice" value="${retailPrice}"/>			
													<c:set var="splitPrice" value="${fn:split(finalPrice, '.')}"/>			
													<sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup>
														
													</p>
													<%-- <dsp:droplet name="/atg/commerce/catalog/SKULookup">
															<dsp:param name="id"param="associatedAccessoriesInfo.childSKUs[0].repositoryId"/>
															<dsp:param name="elementName" value="sku"/>	
															<dsp:oparam name="output">											 				
																<dsp:droplet name="InventoryLookup">
																   <dsp:param name="itemId" param="associatedAccessoriesInfo.childSKUs[0].repositoryId"/>
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
														<dsp:param name="skuId" param="associatedAccessoriesInfo.childSKUs[0].repositoryId"/>
														<dsp:param name="order" bean="/atg/commerce/ShoppingCart.current"/> 
														<dsp:oparam name="output">
															<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
															<dsp:getvalueof var="isOutOfStock" param="isOutOfStock"/>
														</dsp:oparam>
													</dsp:droplet>
													<%-- <a class="button" href="#"><crs:outMessage key="cricket_phonedetails_AddtoCart"/></a> --%>
													<c:choose>
														<c:when test="${isOutOfStock eq true}">
															<a class="button disabled secondary has-tip" data-tooltip="" title="Sorry, this item is out of stock."><crs:outMessage key="cricket_accessoridetails_AddtoCart"/><!-- Add to Cart --></a>
														</c:when>
														<c:otherwise>
															<dsp:include page="/cart/common/addToCart.jsp">
																<dsp:param name="addToCartClass" value="button"/>
																<dsp:param name="productId" param="associatedAccessoriesInfo.repositoryId"/>
																<dsp:param name="skuId" param="associatedAccessoriesInfo.childSKUs[0].repositoryId"/>
																<dsp:param name="quantity" value="1"/>
																<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
															</dsp:include>														
														</c:otherwise>
													</c:choose>
								          		</div>
											<p>
												<a href="${contextpath}${canonicalUrl}"  class="circle-arrow">
													<crs:outMessage key="cricket_phonedetails_ViewDetails"/>
												</a>
											</p>
										</div>
									</c:if>
							</dsp:oparam>
					</dsp:droplet>
				</div>	
		</section>
</dsp:page>