<dsp:page>
<!-- Section to display the Plan Compatible phones recommended-->
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:importbean bean="/com/cricket/browse/GetPlanGroupDroplet"/>

<section id="section-recommended-phones">
<!-- START Display Plan Compatible phones-->	
	 <dsp:getvalueof var="zipcodevalue" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
	 <dsp:getvalueof var="profileMarketType" bean="Profile.marketType"/>
	 <!--Desktop View -->
	<div class="row hide-for-small">			
		<dsp:droplet name="GetPlanGroupDroplet">
			<dsp:param name="planGroupItems" param="Product.planGroups"/>
			<dsp:oparam name="output">
			<dsp:droplet name="/atg/dynamo/droplet/ForEach">
				<dsp:param name="array" param="planGroup.groupCompatiblePhones"/>
				<dsp:param name="elementName" value="compatiblePhonesInfo"/>
					<dsp:oparam name="outputStart">
						<div class="row">
							<div class="large-9 small-12 columns">
								<h3><%-- <crs:outMessage key="cricket_plandetails_get_the_phone"/> --%>Get the phone <span><%-- <crs:outMessage key="cricket_plandetails_you_really_want"/> --%>you really want</span></h3>
							</div>
							<div class="large-3 small-12 columns see-all">
								<dsp:droplet name="DimensionValueCacheDroplet">
									<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
									<dsp:oparam name="output">
										<dsp:getvalueof var="phonesCategoryIdCacheEntry" param="dimensionValueCacheEntry" />
							      </dsp:oparam>
								</dsp:droplet>
								<a href="${contextpath}${phonesCategoryIdCacheEntry.url}" class="circle-arrow"><%-- <crs:outMessage key="cricket_plandetails_see_all_phones"/> --%>See all phones</a>
							</div>
						</div>
					</dsp:oparam>				
					<dsp:oparam name="output">
					<!-- Start Fixed for the defect id 6087-->
					<dsp:getvalueof var="index" param="index"/>
					<c:if test="${index lt 4}">
					<!-- End Fixed for the defect id 6087-->
						<dsp:getvalueof var="productId" param="compatiblePhonesInfo.id"/>	
						<dsp:getvalueof var="skuId" param="compatiblePhonesInfo.childSkus[0].id"/>	
							<!-- START This is used for SEO Url droplet. -->
							<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
								<dsp:param name="id" value="${productId}"/>
								<dsp:param name="itemDescriptorName" value="phone-product"/>
								<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
								</dsp:oparam>
							</dsp:droplet>	
							<!-- END This is used for SEO Url droplet. -->
							<dsp:getvalueof var="isOOFPhone" param="compatiblePhonesInfo.isOOFPhone"/>		
								<c:choose>
									<c:when test="${profileMarketType != null && profileMarketType eq 'OOF'}">
										<c:if test="${isOOFPhone != null && isOOFPhone}">
											<div class="large-3 small-12 columns">
												<div class="recommended-phone-container">
												<!-- get compatible phone image url-->
													<dsp:getvalueof var="fullImageUrl" param="compatiblePhonesInfo.fullImage.url"></dsp:getvalueof>
													<dsp:getvalueof var="height" bean="ImageConfiguration.height.planDetailCompatiblePhone" />
													<%-- <a href="${contextpath}<c:out value='${fullImageUrl}'/>"> --%>
													<!-- START display image from Liquid pixel server-->
														<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
															<dsp:param name="imageLink" value="${fullImageUrl}" />
															<dsp:param name="imageHeight" value="${height}"/>
															<dsp:oparam name="output">
																<dsp:getvalueof var="liquidpixelurl" param="url"/>
																<a href="${contextpath}${canonicalUrl}"><img src="${liquidpixelurl}" class="recommended-phone-image" alt="${compatiblePhonesInfo.fullImage}"/></a>
															</dsp:oparam>
														 </dsp:droplet>
													<!-- END display image from Liquid pixel server-->
															
													</a>
													<!-- Display Compatible Phone Names-->												
													<span class="recommended-phone-name">
													<dsp:valueof param="compatiblePhonesInfo.displayName"/></span>
													 <!-- Changes done as part of defect 5819 - updates from TPN -->
													<a href="${contextpath}${canonicalUrl}" class="button medium">Shop Now</a>
													 <!-- 
													 <c:choose>
														<c:when test="${zipcodevalue eq false}">
															<a href="#" class="recommended-phone-shop-button button medium disabled"><%-- <crs:outMessage key="cricket_plandetails_Add_to_Cart"/> --%>Add to Cart</a>
														</c:when>
														<c:otherwise>
															<%-- <crs:outMessage key="cricket_plandetails_Add_to_Cart"/> --%>
															<dsp:include page="/cart/common/addToCart.jsp">
																<dsp:param name="addToCartClass" value="recommended-phone-shop-button button medium"/>
																<dsp:param name="productId" param="productId"/>
																<dsp:param name="quantity" value="1"/>
																<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
															</dsp:include>													
														</c:otherwise>
													  </c:choose>
													<div><a href="${contextpath}${canonicalUrl}" class="circle-arrow"><%-- <crs:outMessage key="cricket_plandetails_learn_more"/> --%>LEARN MORE</a></div>
													-->
												</div>
											</div>	
										</c:if>
									</c:when>
									<c:otherwise>
											<div class="large-3 small-12 columns">
												<div class="recommended-phone-container">
												<!-- get compatible phone image url-->
													<dsp:getvalueof var="fullImageUrl" param="compatiblePhonesInfo.fullImage.url"></dsp:getvalueof>
													<dsp:getvalueof var="height" bean="ImageConfiguration.height.planDetailCompatiblePhone" />
													<%-- <a href="${contextpath}<c:out value='${fullImageUrl}'/>"> --%>
													<!-- START display image from Liquid pixel server-->
														<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
															<dsp:param name="imageLink" value="${fullImageUrl}" />
															<dsp:param name="imageHeight" value="${height}"/>
															<dsp:oparam name="output">
																<dsp:getvalueof var="liquidpixelurl" param="url"/>
																<a href="${contextpath}${canonicalUrl}"><img src="${liquidpixelurl}" class="recommended-phone-image" alt="${compatiblePhonesInfo.fullImage}"/></a>
															</dsp:oparam>
														 </dsp:droplet>
													<!-- END display image from Liquid pixel server-->
															
													</a>
													<!-- Display Compatible Phone Names-->												
													<span class="recommended-phone-name">
													<dsp:valueof param="compatiblePhonesInfo.displayName"/></span>
													<a href="${contextpath}${canonicalUrl}" class="button medium">Shop Now</a>
													 <%-- <c:choose>
														<c:when test="${zipcodevalue eq false}">
															<a href="#" class="recommended-phone-shop-button button medium disabled has-tip" data-tooltip="" title="Please enter your zip code to see pricing and continue shopping."><crs:outMessage key="cricket_plandetails_Add_to_Cart"/>Add to Cart</a>
														</c:when>
														<c:otherwise>
															<crs:outMessage key="cricket_plandetails_Add_to_Cart"/>
															<dsp:include page="/cart/common/addToCart.jsp">
																<dsp:param name="addToCartClass" value="recommended-phone-shop-button button medium"/>
																<dsp:param name="productId" param="productId"/>
																<dsp:param name="quantity" value="1"/>
																<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
															</dsp:include>													
														</c:otherwise>
													  </c:choose>
													<div><a href="${contextpath}${canonicalUrl}" class="circle-arrow"><crs:outMessage key="cricket_plandetails_learn_more"/>LEARN MORE</a></div> --%>
												</div>
											</div>	
									</c:otherwise>
							</c:choose>
							<!-- Start Fixed for the defect id 6087-->
						</c:if>
						<!-- End Fixed for the defect id 6087-->
					</dsp:oparam>				
			</dsp:droplet>
		</dsp:oparam>
		</dsp:droplet>
	</div>
	<!-- END Display Plan Compatible phones-->			

		
	<!--START Mobile Version -->
	
		<div class="row show-for-small">
			<div class="row">
				<div class="large-9 small-12 columns">
					<h3><%-- <crs:outMessage key="cricket_plandetails_get_the_phone"/> --%>Get the phone <span><%-- <crs:outMessage key="cricket_plandetails_you_really_want"/> --%>you really want</span></h3>
				</div>
			</div>
            <div class="columns small-12 text-center three-callouts">    
              <a href="#" class="prev show-for-small">Prev</a>
              <a href="#" class="next show-for-small">Next</a>
              <div class="swiper-container show-for-small"> 
                <div class="swiper-wrapper">		
					<dsp:droplet name="GetPlanGroupDroplet">
						<dsp:param name="planGroupItems" param="Product.planGroups"/>
						<dsp:oparam name="output">
						<dsp:droplet name="/atg/dynamo/droplet/ForEach">
							<dsp:param name="array" param="planGroup.groupCompatiblePhones"/>
							<dsp:param name="elementName" value="compatiblePhonesInfo"/>
							<dsp:oparam name="output">							
									<dsp:getvalueof var="productId" param="compatiblePhonesInfo.id"/>
									<dsp:getvalueof var="skuId" param="compatiblePhonesInfo.childSkus[0].id"/>
										<!-- START This is used for SEO Url droplet. -->
										<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
											<dsp:param name="id" value="${productId}"/>
											<dsp:param name="itemDescriptorName" value="phone-product"/>
											<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
											<dsp:oparam name="output">
												<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
											</dsp:oparam>
										</dsp:droplet>	
										<!-- END This is used for SEO Url droplet. -->	
										<dsp:getvalueof var="isOOFPhone" param="compatiblePhonesInfo.isOOFPhone"/>	
											<c:choose>
												<c:when test="${profileMarketType != null && profileMarketType eq 'OOF'}">
													<c:if test="${isOOFPhone != null && isOOFPhone}">
													  <div class="swiper-slide plan-item">
														<div class="recommended-phone-container">
														<!-- get compatible phone image url-->
														<dsp:getvalueof var="fullImageUrl" param="compatiblePhonesInfo.fullImage.url"></dsp:getvalueof>	
														<dsp:getvalueof var="height" bean="ImageConfiguration.height.planDetailCompatiblePhone" />		
														<!-- START display image from Liquid pixel server-->
															<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
																	<dsp:param name="imageLink" value="${fullImageUrl}" />
																	<dsp:param name="imageHeight" value="${height}"/>
																	<dsp:oparam name="output">
																	<dsp:getvalueof var="liquidpixelurl" param="url"/>	
																	<a href="${contextpath}${canonicalUrl}"><img src="${liquidpixelurl}" class="recommended-phone-image" /></a>
																	</dsp:oparam>
															 </dsp:droplet>
															<!-- END display image from Liquid pixel server-->
															<span class="recommended-phone-name">
																<dsp:valueof param="compatiblePhonesInfo.displayName"/>
															</span>	
															<a href="${contextpath}${canonicalUrl}" class="button medium">Shop Now</a>
															<%--  <c:choose>
													 			<c:when test="${zipcodevalue eq false}">
														 			<a href="#" class="recommended-phone-shop-button button medium disabled has-tip" data-tooltip="" title="Please enter your zip code to see pricing and continue shopping."><crs:outMessage key="cricket_plandetails_Add_to_Cart"/>Add to Cart</a>
																</c:when>
																<c:otherwise>
																	<a href="#" class="recommended-phone-shop-button button medium"><crs:outMessage key="cricket_plandetails_Add_to_Cart"/>Add to Cart</a>
																</c:otherwise>
															  </c:choose>										
															<div><a href="${contextpath}${canonicalUrl}" class="circle-arrow">LEARN MORE</a></div> --%>
														</div>
													  </div> 
													  </c:if>
												</c:when>
												<c:otherwise>									
														<div class="swiper-slide plan-item">
														<div class="recommended-phone-container">
														<!-- get compatible phone image url-->
														<dsp:getvalueof var="fullImageUrl" param="compatiblePhonesInfo.fullImage.url"></dsp:getvalueof>	
														<dsp:getvalueof var="height" bean="ImageConfiguration.height.planDetailCompatiblePhone" />		
														<!-- START display image from Liquid pixel server-->
															<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
																	<dsp:param name="imageLink" value="${fullImageUrl}" />
																	<dsp:param name="imageHeight" value="${height}"/>
																	<dsp:oparam name="output">
																	<dsp:getvalueof var="liquidpixelurl" param="url"/>	
																	<a href="${contextpath}${canonicalUrl}"><img src="${liquidpixelurl}" class="recommended-phone-image" /></a>
																	</dsp:oparam>
															 </dsp:droplet>
															<!-- END display image from Liquid pixel server-->
															<span class="recommended-phone-name">
																<dsp:valueof param="compatiblePhonesInfo.displayName"/>
															</span>	
															<a href="${contextpath}${canonicalUrl}" class="button medium">Shop Now</a>
															 <%-- <c:choose>
													 			<c:when test="${zipcodevalue eq false}">
														 			<a href="#" class="recommended-phone-shop-button button medium disabled has-tip" data-tooltip="" title="Please enter your zip code to see pricing and continue shopping."><crs:outMessage key="cricket_plandetails_Add_to_Cart"/>Add to Cart</a>
																</c:when>
																<c:otherwise>
																	<a href="#" class="recommended-phone-shop-button button medium"><crs:outMessage key="cricket_plandetails_Add_to_Cart"/>Add to Cart</a>
																</c:otherwise>
															  </c:choose>										
															<div><a href="${contextpath}${canonicalUrl}" class="circle-arrow">LEARN MORE</a></div> --%>
														</div>
													  </div> 
												</c:otherwise> 
											</c:choose>											
									</dsp:oparam>
								</dsp:droplet>
							</dsp:oparam>
						</dsp:droplet>
                	</div>
              	</div>
            </div>				
		</div> 
			<!--END Mobile Version -->	
		<div id="foot-see-all" class="row show-for-small">
			<div class="small-centered columns">
				<a class="circle-arrow" href="${contextpath}${phonesCategoryIdCacheEntry.url}">See all phones</a>				
			</div>
		</div>	 
					
	</section>
</dsp:page>