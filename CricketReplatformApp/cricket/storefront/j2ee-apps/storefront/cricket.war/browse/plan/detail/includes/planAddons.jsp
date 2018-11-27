<dsp:page>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/browse/GetPlanGroupDroplet"/>
<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>

<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>

	<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="otherAddonsCategoryId" bean="CricketConfiguration.otherAddonsCategoryId"/>
	<dsp:droplet name="DimensionValueCacheDroplet">
		<dsp:param name="repositoryId" value="${otherAddonsCategoryId}"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="addonsCategoryCacheEntry" param="dimensionValueCacheEntry" />
	    </dsp:oparam>
	</dsp:droplet>
<c:choose>
		<c:when test="${marketType eq 'OOF'}">
		</c:when>
		<c:otherwise>
			<section id="section-additional-features">
		<div class="row">
			<div class="large-9 small-12 columns">
				<h3><%-- <crs:outMessage key="cricket_planAddons_Customize"/> --%>Customize <span><%-- <crs:outMessage key="cricket_planAddons_with_more_option"/> --%>With More Options</span></h3>
			</div>
			<div class="large-3 small-12 columns see-all">
				<a href="${contextpath}${addonsCategoryCacheEntry.url}" class="circle-arrow"><%-- <crs:outMessage key="cricket_planAddons_see_all_addons"/> --%>See all add-ons</a>
			</div>
		</div>

<!-- Mobile View -->		
	
       	<div class="row show-for-small">
            <div class="columns small-12 text-center three-callouts">    
              <a href="#" class="prev show-for-small">Prev</a>
              <a href="#" class="next show-for-small">Next</a>
              			  
	              	<div class="swiper-container show-for-small"> 
					 	<div class="swiper-wrapper">		
							<dsp:droplet name="GetPlanGroupDroplet">
								<dsp:param name="planGroupItems" param="Product.planGroups"/>
								<dsp:oparam name="output">	                
								  <dsp:droplet name="/atg/dynamo/droplet/ForEach">
									<dsp:param name="array" param="planGroup.defaultPlanAddOns"/>			
									<dsp:param name="elementName" value="planAddOns"/>    
										<dsp:oparam name="output"> 
											<div class="swiper-slide plan-item">
												<div class="additional-feature-container">
													<span class="additional-feature-title">
														<dsp:valueof param="planAddOns.briefDescription"/>
													</span>
													<span class="additional-feature-description">
														<dsp:valueof param="planAddOns.displayName"/>
													</span>
													<span class="additional-feature-price">	
														<dsp:include page="/browse/includes/priceLookup.jsp">
															<dsp:param name="skuId" param="planAddOns.childSKUs[0].id" />
															<dsp:param name="productId" param="planAddOns.id" />
														</dsp:include>
															<c:if test="${empty retailPrice || retailPrice eq 0}">
																<dsp:getvalueof var="retailPrice" param="planAddOns.childSKUs[0].listPrice"></dsp:getvalueof>
															</c:if>
															<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>
															<sup>$</sup><c:out value="${splitPrice[0]}"/><span>/mo</span>									
													</span>
													<div class="clearFix"></div>
													<dsp:getvalueof var="productId" param="planAddOns.id"></dsp:getvalueof>
													<!-- START This is used for SEO Url droplet. -->
													<dsp:droplet name="GetSeoStringDroplet">
														<dsp:param name="product" param="planAddOns"/>
														<dsp:oparam name="output">
															<dsp:getvalueof var="seoString" param="seoString"/>
														</dsp:oparam>
													</dsp:droplet>
													<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
														<dsp:param name="seoString" value="${seoString}"/>
														<dsp:param name="id" value="${productId}"/>
														<dsp:param name="itemDescriptorName" value="addOn-product"/>
														<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
														<dsp:oparam name="output">
															<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
														</dsp:oparam>
													</dsp:droplet>	
													<!-- END This is used for SEO Url droplet. -->	
													<dsp:getvalueof var="zipcodevalue" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
													<div>
														<c:choose>
															<c:when test="${zipcodevalue eq false}">
																<a href="#" class="button medium disabled" data-tooltip="" title="Please enter your zip code to see pricing and continue shopping."><%-- <crs:outMessage key="cricket_plandetails_Add_to_Cart"/> --%>Add to Cart</a>
															</c:when>
															<c:otherwise>
																<%-- <crs:outMessage key="cricket_plandetails_Add_to_Cart"/> --%>
																<dsp:include page="/cart/common/addToCart.jsp">
																	<dsp:param name="addToCartClass" value="button medium"/>
																	<dsp:param name="productId" value="${productId}"/>
																	<dsp:param name="quantity" value="1"/>
																	<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
																</dsp:include>													
															</c:otherwise>
													  	</c:choose>
													  </div>
													<div><a class="circle-arrow" href="${contextpath}${canonicalUrl}">LEARN MORE</a></div>						
												</div>
											</div>
										</dsp:oparam>
								  </dsp:droplet>
								</dsp:oparam>
								</dsp:droplet>
						 </div>
			         </div>
				</div>
           </div>
		<!--Desktop View -->		
			<div class="row hide-for-small">
				<dsp:droplet name="GetPlanGroupDroplet">
					<dsp:param name="planGroupItems" param="Product.planGroups"/>
					<dsp:oparam name="output">	  
					<dsp:droplet name="/atg/dynamo/droplet/ForEach">
						<dsp:param name="array" param="planGroup.defaultPlanAddOns"/>				
						<dsp:param name="elementName" value="planAddOns"/>    
								<dsp:oparam name="output">  
									<div class="large-3 small-12 columns">
										<div class="additional-feature-container">
											<span class="additional-feature-title">
												<dsp:valueof param="planAddOns.briefDescription"/>	<!--Plan Addon parent Category display-->	
											</span>
											<span class="additional-feature-description">
												<dsp:valueof param="planAddOns.displayName"/>
											</span>
											<span class="additional-feature-price">						
												<dsp:include page="/browse/includes/priceLookup.jsp">
													<dsp:param name="skuId" param="planAddOns.childSKUs[0].id" />
													<dsp:param name="productId" param="planAddOns.id" />
												</dsp:include>
												<dsp:getvalueof var="retailPrice" param="planAddOns.childSKUs[0].listPrice"></dsp:getvalueof>
												<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>	
												<sup>$</sup><c:out value="${splitPrice[0]}"/><span>/mo</span>
											</span>
											<div class="clearFix"></div>
											<dsp:getvalueof var="productId" param="planAddOns.id"></dsp:getvalueof>
											<!-- START This is used for SEO Url droplet. -->
												<dsp:droplet name="GetSeoStringDroplet">
													<dsp:param name="product" param="planAddOns"/>
													<dsp:oparam name="output">
														<dsp:getvalueof var="seoString" param="seoString"/>
													</dsp:oparam>
												</dsp:droplet>
												<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
													<dsp:param name="seoString" value="${seoString}"/>
													<dsp:param name="id" value="${productId}"/>
													<dsp:param name="itemDescriptorName" value="addOn-product"/>
													<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
													<dsp:oparam name="output">
														<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
													</dsp:oparam>
												</dsp:droplet>	
											<!-- END This is used for SEO Url droplet. -->
											<dsp:getvalueof var="zipcodevalue" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
											 <div>
												<c:choose>
													<c:when test="${zipcodevalue eq false}">
														<a href="#" class="button medium disabled" data-tooltip="" title="Please enter your zip code to see pricing and continue shopping."><%-- <crs:outMessage key="cricket_plandetails_Add_to_Cart"/> --%>Add to Cart</a>
													</c:when>
													<c:otherwise>
														<%-- <crs:outMessage key="cricket_plandetails_Add_to_Cart"/> --%>
														<dsp:include page="/cart/common/addToCart.jsp">
															<dsp:param name="addToCartClass" value="button medium"/>
															<dsp:param name="productId" value="${productId}"/>
															<dsp:param name="quantity" value="1"/>
															<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
														</dsp:include>													
													</c:otherwise>
											  	 </c:choose>
											  </div>
											<div><a class="circle-arrow" href="${contextpath}${canonicalUrl}">LEARN MORE</a></div>						
										</div>
									</div>
								</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
				</dsp:droplet>
			</div> 
			<div class="row">
				<div class="large-12 small-12 columns">
					<div id="additional-features-container-divider"></div>
				</div>
			</div>
	</section>
		</c:otherwise>
	</c:choose>			

</dsp:page>