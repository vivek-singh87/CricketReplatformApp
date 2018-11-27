<dsp:page>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:importbean bean="/com/cricket/browse/CompatibleProductListingDroplet"/>
<dsp:importbean bean="/atg/store/sort/CricketStoreSortDroplet"/>
<!-- Plans -->

	<section id="phone-plans" class="three-callouts">
			<div class="row">
				<div class="large-8 small-12 columns">
					<h2><crs:outMessage key="cricket_phonedetails_Plans"/><!-- Plans --> <span><crs:outMessage key="cricket_phonedetails_withthisPhone"/><!-- with this Phone --></span></h2>
				</div>
				<div class="large-4 small-12 hide-for-small columns">
					<dsp:droplet name="DimensionValueCacheDroplet">
						<dsp:param name="repositoryId" bean="CricketConfiguration.allPlansCategoryId"/>
						<dsp:oparam name="output">
							<dsp:getvalueof var="plansCategoryCacheEntry" param="dimensionValueCacheEntry" />
					  </dsp:oparam>
					</dsp:droplet>
					<a href="${contextpath}${plansCategoryCacheEntry.url}" class="circle-arrow right"><crs:outMessage key="cricket_phonedetails_SeeAllPlans"/><!-- See All Plans --></a>
					<!-- <a href="#" class="circle-arrow right">See All Plans</a> -->
				</div>
			</div>			
				<dsp:droplet name="CompatibleProductListingDroplet">
					<dsp:param name="planItems" param="Product.compatiblePlans"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="planVOList" param="planVOList"/>	
					</dsp:oparam>
				</dsp:droplet>	
				<dsp:droplet name="CricketStoreSortDroplet">
					<dsp:param name="array" value="${planVOList}"/>
			        <dsp:param name="howMany" value="10"/>
			        <dsp:param name="sortSelection" value="price:ascending"/>			      
			        <dsp:oparam name="output">
						<dsp:getvalueof var="sortedPlanVO" param="array"/>
			        </dsp:oparam>
				</dsp:droplet>	
				<dsp:getvalueof var="manuallyEnteredZipCode" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
				<dsp:getvalueof var="profileMarketType" bean="Profile.marketType"/>
				<dsp:getvalueof var="OOFPlanType" bean="CricketConfiguration.OOFPlanType"/>						
			<!-- Mobile Version -->
			<a href="#" class="prev show-for-small"><crs:outMessage key="cricket_phonedetails_Prev"/><!-- Prev --></a>
			<a href="#" class="next show-for-small"><crs:outMessage key="cricket_phonedetails_Next"/><!-- Next --></a>							
			<div class="swiper-container show-for-small">
				<div class="swiper-wrapper text-center">
					<dsp:droplet name="/atg/dynamo/droplet/ForEach">
						<dsp:param name="array" value="${sortedPlanVO}"/>
						<dsp:param name="elementName" value="sortedVOInfo"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="sortedVOProductId" param="sortedVOInfo.productId"/>
								<dsp:droplet name="/atg/dynamo/droplet/ForEach">
									<dsp:param name="array" param="Product.compatiblePlans"/>
									<dsp:param name="elementName" value="compatiblePlansInfo"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="productId" param="compatiblePlansInfo.id"/>
											<c:if test="${productId eq sortedVOProductId}">
												<!-- START This is used for SEO Url droplet. -->
												<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
													<dsp:param name="id" value="${productId}"/>
													<dsp:param name="itemDescriptorName" value="plan-product"/>
													<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
													<dsp:oparam name="output">
														<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
													</dsp:oparam>
												</dsp:droplet>
												<!-- END This is used for SEO Url droplet. -->
												<dsp:getvalueof var="compatiblePlanType" param="compatiblePlansInfo.planType"/>
												<c:choose>
													<c:when test="${profileMarketType != null && profileMarketType eq 'OOF'}">
														<c:if test="${compatiblePlanType != null && compatiblePlanType eq OOFPlanType}">
														 	<div class="swiper-slide"> 
																<div class="large-4 small-12 columns text-center">
								                                     <div class="plan-item">
							                                      		<dsp:getvalueof var="compatiblePlansImages" param="compatiblePlansInfo.fullImage.url"/>
							                                            <a href="${contextpath}${canonicalUrl}">
								                                            <dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
																			<dsp:param name="imageLink" value="${compatiblePlansImages}" />
																			<dsp:oparam name="output">
																				<dsp:getvalueof var="liquidpixelurl" param="url" />
																				<img src="${liquidpixelurl}" alt=""/> 
																			</dsp:oparam>
																			</dsp:droplet>
							                                            </a>
								                                        <dsp:getvalueof var="zipcodevalue" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
																          <c:choose>
																 			<c:when test="${zipcodevalue eq false}">
																 			 	<a class="button disabled secondary has-tip"  data-tooltip="" title="Please enter your zip code to see pricing and continue shopping."><crs:outMessage key="cricket_phonedetails_AddtoCart"/><!-- Add to Cart --></a> 
																			</c:when>
																			<c:otherwise>
																			<dsp:include page="/cart/common/addToCart.jsp">
																				<dsp:param name="addToCartClass" value="button"/>
																				<dsp:param name="productId" value="${productId}"/>
																				<dsp:param name="skuId" param="compatiblePlansInfo.childSKUs[0].repositoryId"/>
																				<dsp:param name="quantity" value="1"/>
																				<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
																			</dsp:include>
																			<%-- <a class="button" href="#"><crs:outMessage key="cricket_phonedetails_AddtoCart"/><!-- Add to Cart --></a> --%>
																			</c:otherwise>
																		  </c:choose>
								                                      </div>
								                                      <a href="${contextpath}${canonicalUrl}" class="circle-arrow"><crs:outMessage key="cricket_phonedetails_ViewDetails"/><!-- View Details --></a>
								                                </div>
							                               	</div>
							                               </c:if>
						                               </c:when>
						                               <c:otherwise>
															<c:if test="${compatiblePlanType != null && !(compatiblePlanType eq OOFPlanType)}">
								                               	<div class="swiper-slide"> 
																	<div class="large-4 small-12 columns text-center">
									                                     <div class="plan-item">
								                                      		<dsp:getvalueof var="compatiblePlansImages" param="compatiblePlansInfo.fullImage.url"/>
								                                            <a href="${contextpath}${canonicalUrl}">
								                                            <dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
																				<dsp:param name="imageLink" value="${compatiblePlansImages}" />
																				<dsp:oparam name="output">
																					<dsp:getvalueof var="liquidpixelurl" param="url" />
																					 <img src="${liquidpixelurl}" alt=""/>
																				</dsp:oparam>
																				</dsp:droplet>
								                                             </a>
									                                        <dsp:getvalueof var="zipcodevalue" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
																	          <c:choose>
																	 			<c:when test="${zipcodevalue eq false}">
																	 			 	<a class="button disabled secondary"><crs:outMessage key="cricket_phonedetails_AddtoCart"/><!-- Add to Cart --></a> 
																				</c:when>
																				<c:otherwise>
																					<dsp:include page="/cart/common/addToCart.jsp">
																						<dsp:param name="addToCartClass" value="button"/>
																						<dsp:param name="productId" value="${productId}"/>
																						<dsp:param name="skuId" param="compatiblePlansInfo.childSKUs[0].repositoryId"/>
																						<dsp:param name="quantity" value="1"/>
																						<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
																					</dsp:include>
																					<%-- <a class="button" href="#"><crs:outMessage key="cricket_phonedetails_AddtoCart"/><!-- Add to Cart --></a> --%>
																				</c:otherwise>
																			  </c:choose>
									                                      </div>
									                                      <a href="${contextpath}${canonicalUrl}" class="circle-arrow"><crs:outMessage key="cricket_phonedetails_ViewDetails"/><!-- View Details --></a>
									                                </div>
								                               	</div>
								                               </c:if>
						                               </c:otherwise>
					                               </c:choose>
					                             </c:if>
										</dsp:oparam>				
								</dsp:droplet>
							</dsp:oparam>
						</dsp:droplet>
				</div>	
			</div>
			<div class="row cta show-for-small">
				<div class="small-12 columns text-center">
					<p><a href="${contextpath}${plansCategoryCacheEntry.url}" class="circle-arrow"><crs:outMessage key="cricket_phonedetails_SeeAllPlans"/><!-- See All Plans --></a></p>
				</div>
			</div>
			<!-- Desktop Version -->
				<a href="#" class="prev swiper-plans-prev hide-for-small"><crs:outMessage key="cricket_phonedetails_Prev"/><!-- Prev --></a>
                <a href="#" class="next swiper-plans-next hide-for-small"><crs:outMessage key="cricket_phonedetails_Next"/><!-- Next --></a>  
                <div id="swiper-plans" class="row hide-for-small">              
                    <div class="swiper-wrapper"> 
					<dsp:droplet name="/atg/dynamo/droplet/ForEach">
					<dsp:param name="array" value="${sortedPlanVO}"/>
					<dsp:param name="elementName" value="sortedVOInfo"/>
						<dsp:oparam name="output">
							<dsp:getvalueof var="sortedVOProductId" param="sortedVOInfo.productId"/>
		                   <dsp:droplet name="/atg/dynamo/droplet/ForEach">
								<dsp:param name="array" param="Product.compatiblePlans"/>
								<dsp:param name="elementName" value="compatiblePlansInfo"/>
								<dsp:getvalueof var="productId" param="compatiblePlansInfo.id"/>
									<dsp:oparam name="output"> 
										<dsp:getvalueof var="productId" param="compatiblePlansInfo.id"/>
										<!-- START This is used for SEO Url droplet. -->
										<c:if test="${productId eq sortedVOProductId}">
											<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
												<dsp:param name="id" value="${productId}"/>
												<dsp:param name="itemDescriptorName" value="plan-product"/>
												<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
												<dsp:oparam name="output">
													<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
												</dsp:oparam>
											</dsp:droplet>
											<!-- END This is used for SEO Url droplet. -->
											<dsp:getvalueof var="compatiblePlanType" param="compatiblePlansInfo.planType"/>
											<c:choose>
												<c:when test="${profileMarketType != null && profileMarketType eq 'OOF'}">
													<c:if test="${compatiblePlanType != null && compatiblePlanType eq OOFPlanType}">
													 	<div class="large-4 small-12 columns text-center swiper-slide">                                                
							                                  <div class="plan-item">
								                                  <dsp:getvalueof var="compatiblePlansImages" param="compatiblePlansInfo.fullImage.url"/>
								                                   <a href="${contextpath}${canonicalUrl}">
								                                   <dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
																		<dsp:param name="imageLink" value="${compatiblePlansImages}" />
																		<dsp:oparam name="output">
																			<dsp:getvalueof var="liquidpixelurl" param="url" />
																					 <img src="${liquidpixelurl}" alt=""/> 
																		</dsp:oparam>
																		</dsp:droplet>
								                                   </a>
							                                    	<dsp:getvalueof var="zipcodevalue" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
															          <c:choose>
															 			<c:when test="${zipcodevalue eq false}">
																 			 <a class="button disabled secondary has-tip" data-tooltip="" title="Please enter your zip code to see pricing and continue shopping."><crs:outMessage key="cricket_phonedetails_AddtoCart"/><!-- Add to Cart --></a> 
																		</c:when>
																		<c:otherwise>
																			<dsp:include page="/cart/common/addToCart.jsp">
																				<dsp:param name="addToCartClass" value="button"/>
																				<dsp:param name="productId" value="${productId}"/>
																				<dsp:param name="skuId" param="compatiblePlansInfo.childSKUs[0].repositoryId"/>
																				<dsp:param name="quantity" value="1"/>
																				<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
																			</dsp:include>
																			<%-- <a class="button" href="#"><crs:outMessage key="cricket_phonedetails_AddtoCart"/><!-- Add to Cart --></a> --%>
																		</c:otherwise>
																	  </c:choose>
							                                  </div>
							                                  <a href="${contextpath}${canonicalUrl}" class="circle-arrow"><crs:outMessage key="cricket_phonedetails_ViewDetails"/><!-- View Details --></a>
							                           </div>
						                           </c:if>
						                         </c:when>
						                         <c:otherwise>
													<c:if test="${compatiblePlanType != null && !(compatiblePlanType eq OOFPlanType)}">
													 	<div class="large-4 small-12 columns text-center swiper-slide">                                                
							                                  <div class="plan-item">
								                                  <dsp:getvalueof var="compatiblePlansImages" param="compatiblePlansInfo.fullImage.url"/>
								                                   <a href="${contextpath}${canonicalUrl}">
								                                   <dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
																		<dsp:param name="imageLink" value="${compatiblePlansImages}" />
																		<dsp:oparam name="output">
																			<dsp:getvalueof var="liquidpixelurl" param="url" />
																			<img src="${liquidpixelurl}" alt=""/>
																		</dsp:oparam>
																		</dsp:droplet>
								                                    </a>
							                                    	<dsp:getvalueof var="zipcodevalue" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
															          <c:choose>
															 			<c:when test="${zipcodevalue eq false}">
																 			 <a class="button disabled secondary has-tip" data-tooltip="" title="Please enter your zip code to see pricing and continue shopping."><crs:outMessage key="cricket_phonedetails_AddtoCart"/><!-- Add to Cart --></a> 
																		</c:when>
																		<c:otherwise>
																			<dsp:include page="/cart/common/addToCart.jsp">
																				<dsp:param name="addToCartClass" value="button"/>
																				<dsp:param name="productId" value="${productId}"/>
																				<dsp:param name="skuId" param="compatiblePlansInfo.childSKUs[0].repositoryId"/>
																				<dsp:param name="quantity" value="1"/>
																				<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
																			</dsp:include>
																			<%-- <a class="button" href="#"><crs:outMessage key="cricket_phonedetails_AddtoCart"/><!-- Add to Cart --></a> --%>
																		</c:otherwise>
																	  </c:choose>
							                                  </div>
							                                  <a href="${contextpath}${canonicalUrl}" class="circle-arrow"><crs:outMessage key="cricket_phonedetails_ViewDetails"/><!-- View Details --></a>
							                           </div>
							                        </c:if>
						                         </c:otherwise>
						                     </c:choose>
						                   </c:if>
								</dsp:oparam>				
						</dsp:droplet>
					</dsp:oparam>
				</dsp:droplet>
			 </div> <!--/.swiper-wrapper-->
           </div>           
	</section>				
</dsp:page>