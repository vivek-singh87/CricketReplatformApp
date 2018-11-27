<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/atg/dynamo/droplet/Range"/>
<dsp:importbean bean="/com/cricket/search/session/UserSearchSessionInfo"/>
<dsp:importbean bean="/com/cricket/search/SegregateSearchResultsBasedOnCategory"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="productDetailsURL" value="/browse/phone/phone_details.jsp"/>
<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
<dsp:getvalueof var="imageHeight" bean="ImageConfiguration.height.searchPhoneResult" />
<dsp:getvalueof var="zipCodeKnown" bean="CitySessionInfoObject.cityVO.manulaEntry" vartype="java.lang.Boolean"/>
	<div id="search-all-results">
		<dsp:droplet name="SegregateSearchResultsBasedOnCategory">
			<dsp:param name="records" value="${contentItem.records}"/>
			<dsp:param name="categoryTypeRefinements" value="${categoryTypeRefinements}"/>
			<dsp:param name="searchQuery" param="Ntt"/>
			<dsp:param name="totalNumRecs" value="${totalNumRecs}"/>
			<dsp:param name="recordsPerPage" value="${contentItem.recsPerPage}"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="segregatedMapBasedOnCategory" param="segregatedMap"/>
			</dsp:oparam>
		</dsp:droplet>
		<!-- Never ever remove this comment as it is required for identifying the ajax reload section in case of type ahead search -->
		<!-- IdentifierForTypeAheadSearchAjaxReload -->
		<dsp:droplet name="ForEach">
			<dsp:param name="array" bean="UserSearchSessionInfo.keyOrder"/>
			<dsp:param name="elementName" value="currentKey3"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="sizeSegregated" param="size"/>
				<dsp:getvalueof var="countSegregated" param="count"/>
				<dsp:getvalueof var="currentKey3" param="currentKey3"/>
				<dsp:getvalueof var="eachList" value="${segregatedMapBasedOnCategory[currentKey3]}"/>
				<c:choose>
					<c:when test="${empty eachList || fn:length(eachList) eq 0}">
					</c:when>
					<c:otherwise>
						<div class="row">
							<div class="columns large-10 small-8">
								<h3>
									<c:choose>
										<c:when test="${currentKey3 eq 'AddOns'}">
											Plan Add-Ons
										</c:when>
										<c:otherwise>
											<dsp:valueof param="currentKey3"/>
										</c:otherwise>
									</c:choose>
									
									<span class="number">(${fn:length(eachList)})</span>
								</h3>
							</div>
							<c:choose>
								<c:when test="${currentKey3 eq 'Phones'}">
									<dsp:getvalueof var="allItemsUrl" value="${allPhonesLink}"/>
									<dsp:getvalueof var="productDetailsURL" value="/browse/phone/phone_details.jsp"/>
								</c:when>
								<c:when test="${currentKey3 eq 'Plans'}">
									<dsp:getvalueof var="allItemsUrl" value="${allPlansLink}"/>
									<dsp:getvalueof var="productDetailsURL" value="/browse/plan/detail/planDetails.jsp"/>
								</c:when>
								<c:when test="${currentKey3 eq 'Accessories'}">
									<dsp:getvalueof var="allItemsUrl" value="${allAccessoriesLink}"/>
									<dsp:getvalueof var="productDetailsURL" value="/browse/accessories/details/accessories_details.jsp"/>
								</c:when>
								<c:when test="${currentKey3 eq 'AddOns'}">
									<dsp:getvalueof var="allItemsUrl" value="${allAddonsLink}"/>
									<dsp:getvalueof var="productDetailsURL" value="/browse/plan/addon/plan_addon_details.jsp"/>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
							<div class="columns large-2 small-4 text-right">
								<a href="${contextPath}/browse/${allItemsUrl}" class="circle-arrow">
									Show All
								</a>
							</div>
						</div>
						<c:choose>
							<c:when test="${currentKey3 eq 'Phones'}">
								<div class="row search-section">
									<dsp:droplet name="Range">
										<dsp:param name="array" value="${eachList}"/>
										<dsp:param name="howMany" value="3"/>
										<dsp:param name="elementName" value="recordVO"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="productId" param="recordVO.productId"/>
											<dsp:getvalueof var="skuId" param="recordVO.skuId"/>
											<dsp:getvalueof var="imageUrl" param="recordVO.imageUrl"/>
											<dsp:getvalueof var="displayName" param="recordVO.displayName"/>
											<div class="columns result">
												<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
													<dsp:param name="imageLink" value="${imageUrl}" />
													<dsp:param name="imageHeight" value="${imageHeight}"/>
													<dsp:oparam name="output">
														<dsp:getvalueof var="lpImageUrl" param="url"/>
													</dsp:oparam>
											 	</dsp:droplet>
												<div class="image left">
													<img src="${lpImageUrl}" alt=""/>
												</div>
												<div class="copy left">
													<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
														<dsp:param name="id" value="${productId}"/>
														<dsp:param name="seoString" param="recordVO.seoString"/>
														<dsp:param name="itemDescriptorName" value="phone-product"/>
														<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
														<dsp:oparam name="output">
															<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
														</dsp:oparam>
													</dsp:droplet>
													<h4>
														<a href="${contextPath}${canonicalUrl}">
															<dsp:getvalueof var="displayName" value="${displayName}"/>									  					
															<dsp:droplet name="/com/cricket/browse/droplet/DisplayNameDroplet">
																<dsp:param name="displayName" value="${displayName}" />					
																<dsp:oparam name="output">
																	<dsp:getvalueof var="productDisplayName" param="displayName"/>				
																	<dsp:getvalueof var="hasSpecialSymbol" param="hasSpecialSymbol"/>
																	<dsp:getvalueof var="firstString" param="firstString"/>
																	<dsp:getvalueof var="secondString" param="secondString"/>
																	<dsp:getvalueof var="specialSymbol" param="specialSymbol"/>
																	<c:choose>
																		<c:when test="${hasSpecialSymbol eq true}">	
																			${firstString}<sup>${specialSymbol}</sup>${secondString}							
																		</c:when>	
																		<c:otherwise>
																			${productDisplayName}
																		</c:otherwise>
																	</c:choose>
																</dsp:oparam>
															</dsp:droplet>
														</a>
													</h4>
													<c:if test="${zipCodeKnown}">
														<dsp:include page="/browse/includes/discountTag_price.jsp">
															<dsp:param name="productId" param="recordVO.productId"/>
															<dsp:param name="skuId" param="recordVO.skuId" />
														</dsp:include>
														<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${finalPrice}" var="fPrice" />
														<c:set var="splitPrice" value="${fn:split(fPrice, '.')}"/>
														<p class="green-price"><sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup></p>
													</c:if>													
													<!-- Changes done as part of defect 5819 - updates from TPN -->
													<a href="${contextpath}${canonicalUrl}" class="button orange-shop-now">Shop Now</a>
													<!--  <a class="green-add-cart button">Add to Cart</a>
													<a href="${contextPath}${productDetailsURL}?productId=${productId}&skuId=${skuId}" class="circle-arrow">Learn More</a>
													-->
							 					</div>
											</div>
										</dsp:oparam>
									</dsp:droplet>
								</div>
							</c:when>
							<c:when test="${currentKey3 eq 'Accessories'}">
								<div class="row search-section">
									<dsp:droplet name="Range">
										<dsp:param name="array" value="${eachList}"/>
										<dsp:param name="howMany" value="3"/>
										<dsp:param name="elementName" value="recordVO"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="productId" param="recordVO.productId"/>
											<dsp:getvalueof var="skuId" param="recordVO.skuId"/>
											<dsp:getvalueof var="imageUrl" param="recordVO.imageUrl"/>
											<dsp:getvalueof var="displayName" param="recordVO.displayName"/>
											<div class="columns result">
												<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
													<dsp:param name="imageLink" value="${imageUrl}" />
													<dsp:param name="imageHeight" value="${imageHeight}"/>
													<dsp:oparam name="output">
														<dsp:getvalueof var="lpImageUrl" param="url"/>
													</dsp:oparam>
											 	</dsp:droplet>
												<div class="image left">
													<img src="${lpImageUrl}" alt=""/>
												</div>
												<div class="copy left">
													<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
														<dsp:param name="id" value="${productId}"/>
														<dsp:param name="itemDescriptorName" value="accessory-product"/>
														<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
														<dsp:oparam name="output">
															<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
														</dsp:oparam>
													</dsp:droplet>
													<h4>
														<a href="${contextPath}${canonicalUrl}">
															<dsp:getvalueof var="displayName" value="${displayName}"/>									  					
															<dsp:droplet name="/com/cricket/browse/droplet/DisplayNameDroplet">
																<dsp:param name="displayName" value="${displayName}" />					
																<dsp:oparam name="output">
																	<dsp:getvalueof var="productDisplayName" param="displayName"/>				
																	<dsp:getvalueof var="hasSpecialSymbol" param="hasSpecialSymbol"/>
																	<dsp:getvalueof var="firstString" param="firstString"/>
																	<dsp:getvalueof var="secondString" param="secondString"/>
																	<dsp:getvalueof var="specialSymbol" param="specialSymbol"/>
																	<c:choose>
																		<c:when test="${hasSpecialSymbol eq true}">	
																			${firstString}<sup>${specialSymbol}</sup>${secondString}							
																		</c:when>	
																		<c:otherwise>
																			${productDisplayName}
																		</c:otherwise>
																	</c:choose>
																</dsp:oparam>
															</dsp:droplet>
														</a>
													</h4>
													<c:if test="${zipCodeKnown}">
														<dsp:include page="/browse/includes/discountTag_price.jsp">
															<dsp:param name="productId" param="recordVO.productId"/>
															<dsp:param name="skuId" param="recordVO.skuId" />
														</dsp:include>
														<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${finalPrice}" var="fPrice" />
														<c:set var="splitPrice" value="${fn:split(fPrice, '.')}"/>
														<p class="green-price"><sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup></p>
														<a href="${contextpath}${canonicalUrl}" class="button orange-shop-now">Shop Now</a>
													</c:if>
													<a href="${contextPath}${canonicalUrl}" class="circle-arrow">Learn More</a>
							 					</div>
											</div>
										</dsp:oparam>
									</dsp:droplet>
								</div>
							</c:when>
							<c:when test="${currentKey3 eq 'Plans'}">
								<div class="row search-section plans">
									<dsp:droplet name="Range">
										<dsp:param name="array" value="${eachList}"/>
										<dsp:param name="howMany" value="3"/>
										<dsp:param name="elementName" value="recordVO"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="productId" param="recordVO.productId"/>
											<dsp:getvalueof var="skuId" param="recordVO.skuId"/>
											<dsp:getvalueof var="imageUrl" param="recordVO.imageUrl"/>
											<dsp:getvalueof var="displayName" param="recordVO.displayName"/>
											<dsp:getvalueof var="ratePlanType" param="recordVO.ratePlanType"/>
											<div class="columns result">
												<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
													<dsp:param name="imageLink" value="${imageUrl}" />
													<dsp:param name="imageHeight" value="${imageHeight}"/>
													<dsp:oparam name="output">
														<dsp:getvalueof var="lpImageUrl" param="url"/>
													</dsp:oparam>
											 	</dsp:droplet>
												<div class="image left">
													<img src="${lpImageUrl}" alt=""/>
												</div>
												<div class="copy left">
													<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
														<dsp:param name="id" value="${productId}"/>
														<dsp:param name="itemDescriptorName" value="plan-product"/>
														<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
														<dsp:oparam name="output">
															<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
														</dsp:oparam>
													</dsp:droplet>
													<h4>
														<a href="${contextPath}${canonicalUrl}">
															${ratePlanType}
														</a>
													</h4>
													<c:if test="${zipCodeKnown}">
														<a class="green-add-cart button">Add to Cart</a>
													</c:if>
													<a href="${contextPath}${canonicalUrl}" class="circle-arrow">Learn More</a>
							 					</div>
											</div>
										</dsp:oparam>
									</dsp:droplet>
								</div>
							</c:when>
							<c:when test="${currentKey3 eq 'AddOns'}">
								<div class="row search-section plan-addons">
									<dsp:droplet name="Range">
										<dsp:param name="array" value="${eachList}"/>
										<dsp:param name="howMany" value="3"/>
										<dsp:param name="elementName" value="recordVO"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="productId" param="recordVO.productId"/>
											<dsp:getvalueof var="skuId" param="recordVO.skuId"/>
											<dsp:getvalueof var="imageUrl" param="recordVO.imageUrl"/>
											<dsp:getvalueof var="displayName" param="recordVO.displayName"/>
											<dsp:getvalueof var="parentCategoryName" param="recordVO.parentCategoryName"/>
											<dsp:getvalueof var="finalPrice" param="recordVO.finalPrice"/>
											<div class="columns result">
												<!-- Price logic start -->
												<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${finalPrice}" var="retailPrice" />
								                <c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>
								                <c:set var="fPrice" value="${splitPrice[0]}" />
								                <c:set var="imageUrl" value="/img/search/listing/${fPrice}.png"></c:set>
												<!-- Price logic end -->
												<div class="image left">
												<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
													<dsp:param name="imageLink" value="${imageUrl}" />
													<dsp:param name="imageHeight" value="${imageHeight}"/>
													<dsp:oparam name="output">
														<dsp:getvalueof var="liquidpixelurl" param="url" />
														<img src="${liquidpixelurl}" alt="" height="90" width="90"/>															
													</dsp:oparam>
												</dsp:droplet>
												</div>
												<div class="copy left">
													<h3>${parentCategoryName}</h3>
													<h4>
														<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
															<dsp:param name="id" value="${productId}"/>
															<dsp:param name="seoString" param="recordVO.seoString"/>
															<dsp:param name="itemDescriptorName" value="addOn-product"/>
															<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
															<dsp:oparam name="output">
																<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
															</dsp:oparam>
														</dsp:droplet>
														<a href="${contextPath}${canonicalUrl}">
															<dsp:getvalueof var="displayName" value="${displayName}"/>									  					
															<dsp:droplet name="/com/cricket/browse/droplet/DisplayNameDroplet">
																<dsp:param name="displayName" value="${displayName}" />					
																<dsp:oparam name="output">
																	<dsp:getvalueof var="productDisplayName" param="displayName"/>				
																	<dsp:getvalueof var="hasSpecialSymbol" param="hasSpecialSymbol"/>
																	<dsp:getvalueof var="firstString" param="firstString"/>
																	<dsp:getvalueof var="secondString" param="secondString"/>
																	<dsp:getvalueof var="specialSymbol" param="specialSymbol"/>
																	<c:choose>
																		<c:when test="${hasSpecialSymbol eq true}">	
																			${firstString}<sup>${specialSymbol}</sup>${secondString}							
																		</c:when>	
																		<c:otherwise>
																			${productDisplayName}
																		</c:otherwise>
																	</c:choose>
																</dsp:oparam>
															</dsp:droplet>
														</a>
													</h4>
													<c:if test="${zipCodeKnown}">
														<a class="green-add-cart button">Add to Cart</a>
													</c:if>
													<a href="${contextPath}${canonicalUrl}" class="circle-arrow">Learn More</a>
							 					</div>
											</div>
										</dsp:oparam>
									</dsp:droplet>
								</div>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
						<c:if test="${countSegregated ne sizeSegregated }">
							<hr/>
						</c:if>
					</c:otherwise>
				</c:choose>
			</dsp:oparam>
		</dsp:droplet>
		
          <!-- ALL Search Results (shows in Header Predictive Search as well)-->
        <c:if test="${fn:length(supportResultVOs) gt 0}">
        	<dsp:getvalueof var="Ntt" param="Ntt"/>
			<div class="row">
				<div class="columns large-10 small-8"><h3>Support <span class="number">(${fn:length(supportResultVOs)})</span></h3></div>
				<div class="columns large-2 small-4 text-right"><a href="${contextPath}${generalSearchQuery}&Ntt=${Ntt}&searchPage=searchPage&activeLink=support" class="circle-arrow">Show All</a></div>
			</div>
			<div class="row search-section support">
				<dsp:droplet name="Range">
					<dsp:param name="array" value="${supportResultVOs}"/>
					<dsp:param name="howMany" value="3"/>
		          	<dsp:param name="elementName" value="supportVO"/>
		          	<dsp:oparam name="output">
		          		<dsp:getvalueof var="supportVO" param="supportVO"/>
						<div class="columns result">
							<div class="copy left">
								<h3>${supportVO.faqTitle}</h3>
								<h4>${supportVO.faqQuestion}</h4>
								<dsp:getvalueof var="supportTerm" value="${supportVO.faqLink}"/>
								<dsp:getvalueof var="supportLink" bean="CricketConfiguration.supportLink"/>
								<a href="${supportLink}/${supportTerm}" class="circle-arrow">View</a>
							</div>
			            </div>
		          	</dsp:oparam>
				</dsp:droplet>
			</div>
		</c:if>
		<!-- Never ever remove this comment as it is required for identifying the ajax reload section in case of type ahead search -->
		<!-- IdentifierForTypeAheadSearchAjaxReload -->
     </div>   
</dsp:page>