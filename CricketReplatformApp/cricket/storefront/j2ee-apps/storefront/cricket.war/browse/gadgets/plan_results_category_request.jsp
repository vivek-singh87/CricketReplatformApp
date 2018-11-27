<dsp:page>
<dsp:importbean bean="/com/cricket/browse/SegregatePlansBasedOnType"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/dynamo/droplet/Range"/>
<dsp:importbean bean="/atg/store/sort/CricketStoreSortDroplet"/>
<dsp:getvalueof var="manuallyEnteredZipCode" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<!-- Plans Listing Desktop View -->
	<section id="section-plans-list" class="hide-for-small">
		<div class="row">					
			<table class="table-plans-list">
				<dsp:droplet name="SegregatePlansBasedOnType">
					<dsp:param name="planVOsList" param="productList"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="numberOfFeaturePlans" param="numberOfFeaturePlans"/>
						<dsp:getvalueof var="numberOfSmartPlans" param="numberOfSmartPlans"/>
						<dsp:getvalueof var="featurePlanVOList" param="featurePlanVOList"/>
						<dsp:getvalueof var="orderedSpecsMap" param="orderedSpecsMap"/>
						<dsp:getvalueof var="specNameKeys" param="specNameKeys"/>
						<dsp:droplet name="CricketStoreSortDroplet">
							<dsp:param name="array" param="smartPlanVOList"/>
					        <dsp:param name="sortSelection" value="price:ascending"/>			      
					        <dsp:oparam name="output">
								<dsp:getvalueof var="smartPlanVOList" param="array"/>
					        </dsp:oparam>
						</dsp:droplet>	
						<dsp:droplet name="CricketStoreSortDroplet">
							<dsp:param name="array" param="featurePlanVOList"/>
					        <dsp:param name="sortSelection" value="price:ascending"/>			      
					        <dsp:oparam name="output">
								<dsp:getvalueof var="featurePlanVOList" param="array"/>
					        </dsp:oparam>
						</dsp:droplet>
					</dsp:oparam>
				</dsp:droplet>
				<thead>
					<tr class="top-heading">
						<c:if test="${numberOfFeaturePlans gt 0}">
							<th colspan="${numberOfFeaturePlans}">Feature Plans</th>
						</c:if>
						<c:if test="${numberOfSmartPlans gt 0}">
							<th colspan="${numberOfSmartPlans}">Smart Plans</th>
						</c:if>							
					</tr>
					<tr class="features-heading features-heading-top">
						<dsp:droplet name="ForEach">
							<dsp:param name="array" value="${featurePlanVOList}"/>
							<dsp:param name="elementName" value="featurePlanVO"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="planType" param="featurePlanVO.planType"/>
								<c:choose>
									<c:when test="${planType eq 'PM'}">
										<dsp:getvalueof var="planTypeText" value="/mo"/>
									</c:when>
									<c:otherwise>
										<dsp:getvalueof var="planTypeText" value="/mo"/>
									</c:otherwise>
								</c:choose>
								<dsp:getvalueof var="planPrice" param="featurePlanVO.finalPrice"/>
								<c:set var="splitPrice" value="${fn:split(planPrice, '.')}"/>
								<th>
									<sup>$</sup>${splitPrice[0]}<sub>/mo</sub>
									<dsp:getvalueof var="greenBoxContent" param="featurePlanVO.greenBoxContent"></dsp:getvalueof>
									<div class="plan-content">
										<dsp:droplet name="IsNull">
										 <dsp:param name="value" value="${greenBoxContent}"/>
										 <dsp:oparam name="false">
												<dsp:valueof value="${greenBoxContent}" valueishtml="true"></dsp:valueof>
											</dsp:oparam>
										</dsp:droplet>
									</div>										
							</th>		
							</dsp:oparam>
							</dsp:droplet>
							
							<dsp:droplet name="ForEach">
							<dsp:param name="array" value="${smartPlanVOList}"/>
							<dsp:param name="elementName" value="smartPlanVO"/>
							<dsp:oparam name="output">
						 
								<dsp:getvalueof var="planType" param="smartPlanVO.planType"/>
								<c:choose>
									<c:when test="${planType eq 'PM'}">
										<dsp:getvalueof var="planTypeText" value="/mo"/>
									</c:when>
									<c:otherwise>
										<dsp:getvalueof var="planTypeText" value="/mo"/>
									</c:otherwise>
								</c:choose>
								<dsp:getvalueof var="planPriceSmart" param="smartPlanVO.finalPrice"/>
								<c:set var="splitSmartPrice" value="${fn:split(planPriceSmart, '.')}"/>
								<dsp:getvalueof var="dataLimit" param="smartPlanVO.dataLimit"/>
								<dsp:getvalueof var="dataMessage" param="smartPlanVO.dataMessage"/>
								<th class="bg-smartplan">
									<sup>$</sup>${splitSmartPrice[0]}<sub>${planTypeText}</sub>
									<div class="plan-content">
										<div class="data-callout">
											${dataLimit}<sub>/mo</sub>
											<p>${dataMessage}</p>
										</div>
										<dsp:getvalueof var="greenBoxContent" param="smartPlanVO.greenBoxContent"></dsp:getvalueof>
										<dsp:droplet name="IsNull">
										 <dsp:param name="value" value="${greenBoxContent}"/>
										 <dsp:oparam name="false">
												<dsp:valueof value="${greenBoxContent}" valueishtml="true"></dsp:valueof>
											</dsp:oparam>
										</dsp:droplet>
									</div>	
									
								</th>
								</dsp:oparam>
								</dsp:droplet>
					</tr>
							
				 
					<tr class="features-heading features-heading-bottom">
							<dsp:droplet name="ForEach">
							<dsp:param name="array" value="${featurePlanVOList}"/>
							<dsp:param name="elementName" value="featurePlanVO"/>
							<dsp:oparam name="output">
							<th class="bg-smartplan">
								<dsp:getvalueof var="planType" param="featurePlanVO.planType"/>
									<dsp:getvalueof var="planProductId" param="featurePlanVO.productId"/>
									<dsp:getvalueof var="featurePlanSkuId" param="featurePlanVO.defaultSkuId"/>
									<c:choose>
										<c:when test="${manuallyEnteredZipCode eq true}">
											<dsp:include page="/cart/common/addToCart.jsp">
												<dsp:param name="addToCartClass" value="button small"/>
												<dsp:param name="productId" value="${planProductId}"/>
												<dsp:param name="skuId" value="${featurePlanSkuId}"/>
												<dsp:param name="quantity" value="1"/>
												<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
											</dsp:include>											
										</c:when>
										<c:otherwise>
											<a class="grey-add-cart button small disabled secondary has-tooltip" data-tooltip title="Please enter your home zip code to see prices and add to cart." href="#">Add to Cart</a>
										</c:otherwise>
									</c:choose>
									<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
										<dsp:param name="id" value="${planProductId}"/>
										<dsp:param name="itemDescriptorName" value="plan-product"/>
										<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
										</dsp:oparam>
									</dsp:droplet>
									<dsp:getvalueof var="phoneId" param="phoneId"></dsp:getvalueof>
									<c:if test="${phoneId ne null}">
										<div class="learn-more">
									</c:if>
									<a href="${contextpath}${canonicalUrl}" class="circle-arrow white">Learn More</a>
									<c:if test="${phoneId ne null}">
										</div>
									</c:if>
								</th>
							</dsp:oparam>
						</dsp:droplet>
						<dsp:droplet name="ForEach">
							<dsp:param name="array" value="${smartPlanVOList}"/>
							<dsp:param name="elementName" value="smartPlanVO"/>
							<dsp:oparam name="output">
							<dsp:getvalueof var="planId" param="smartPlanVO.productId"/>
							 <dsp:getvalueof var="planSkuId" param="smartPlanVO.defaultSkuId"/>
									<th class="bg-smartplan">
									<c:choose>
										<c:when test="${manuallyEnteredZipCode eq true}">
											<dsp:include page="/cart/common/addToCart.jsp">
												<dsp:param name="addToCartClass" value="button small"/>
												<dsp:param name="productId" value="${planId}"/>
												<dsp:param name="skuId" value="${planSkuId}"/>
												<dsp:param name="quantity" value="1"/>
												<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
											</dsp:include>												
										</c:when>
										<c:otherwise>
											<a class="grey-add-cart button small disabled secondary has-tooltip" data-tooltip title="Please enter your home zip code and add to cart." href="#">Add to Cart</a>
										</c:otherwise>
									</c:choose>
									<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
										<dsp:param name="id" value="${planId}"/>
										<dsp:param name="itemDescriptorName" value="plan-product"/>
										<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
										</dsp:oparam>
									</dsp:droplet>
									<dsp:getvalueof var="phoneId" param="phoneId"></dsp:getvalueof>
									<c:if test="${phoneId ne null}">
										<div class="learn-more">
									</c:if>
									<a href="${contextpath}${canonicalUrl}" class="circle-arrow white">Learn More</a>
									<c:if test="${phoneId ne null}">
										</div>
									</c:if>
								</th>
							</dsp:oparam>
						</dsp:droplet>
						</tr>
					  
					
				</thead>
				<tbody>
					<dsp:droplet name="ForEach">
						<dsp:param name="array" value="${specNameKeys}"/>
						<dsp:param name="elementName" value="specNameKey"/>
						<dsp:oparam name="output">
							<dsp:getvalueof var="specKey" param="specNameKey"/>
							<tr>
								<th colspan="${numberOfFeaturePlans + numberOfSmartPlans}">
									<h3>
										<dsp:getvalueof var="specNameKey" param="specNameKey"/>
										<c:choose>
											<c:when test="${specNameKey eq 'Muve Music®'}">
												Muve Music<sup>®</sup>
											</c:when>
											<c:otherwise>
												<dsp:valueof value="${specNameKey}"></dsp:valueof>
											</c:otherwise>
										</c:choose>
										
									</h3>
								</th>
							</tr>
							<tr>
								<dsp:droplet name="ForEach">
									<dsp:param name="array" value="${featurePlanVOList}"/>
									<dsp:param name="elementName" value="featurePlanVO"/>
									<dsp:oparam name="output">
										<dsp:getvalueof var="planIdFeature" param="featurePlanVO.productId"/>
										<dsp:getvalueof var="keyForFeaturePlan" value="${planIdFeature}||${specKey}"/>
										<dsp:getvalueof var="specification" value="${orderedSpecsMap[keyForFeaturePlan]}"/>
										<dsp:getvalueof var="specValue" value="${specification.specValue}"/>
										<dsp:getvalueof var="specType" value="${specification.specType}"/>
										<dsp:getvalueof var="booleanSpecValue" value="${specification.specBoolValue}"/>
										<dsp:getvalueof var="nodisplay" value="${false}"/>
										<c:if test="${specValue eq 'NA'}">
											<dsp:getvalueof var="nodisplay" value="${true}"/>
										</c:if>
										<c:choose>
											<c:when test="${specType eq 'TEXT'}">
												<c:choose>
													<c:when test="${nodisplay eq true}">
														<td></td>
													</c:when>
													<c:otherwise>
														<td>${specValue}</td>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:when test="${specType eq 'BOOLEAN'}">
												<c:choose>
													<c:when test="${booleanSpecValue eq true}">
														<td class="yes">Yes</td>
													</c:when>
													<c:otherwise>
														<td></td>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:when test="${specType eq 'INFOICON'}">
												<c:choose>
													<c:when test="${nodisplay eq true}">
														<td></td>
													</c:when>
													<c:when test="${specKey eq 'Muve Music'}">
													
													<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
													<dsp:param name="imageLink" value="img/plans/logo-muve-music.jpg" />
													<dsp:oparam name="output">
														<dsp:getvalueof var="liquidpixelurl" param="url" />
															<td class="tip"><span data-tooltip class="has-tip" title="<img src='${liquidpixelurl}'/>
																<br/>
																${specValue}">
																	i
																</span>
															</td>
													</dsp:oparam>
													</dsp:droplet>
													
													</c:when>
													<c:otherwise>
														<td class="tip"><span data-tooltip class="has-tip" title="${specValue}">i</span></td>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${nodisplay eq true}">
														<td></td>
													</c:when>
													<c:otherwise>
														<td>${specValue}</td>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</dsp:oparam>
								</dsp:droplet>
								<dsp:droplet name="ForEach">
									<dsp:param name="array" value="${smartPlanVOList}"/>
									<dsp:param name="elementName" value="smartPlanVO"/>
									<dsp:oparam name="output">
										<dsp:getvalueof var="planIdSmart" param="smartPlanVO.productId"/>
										<dsp:getvalueof var="keyForSmartPlan" value="${planIdSmart}||${specKey}"/>
										<dsp:getvalueof var="specification" value="${orderedSpecsMap[keyForSmartPlan]}"/>
										<dsp:getvalueof var="specValue" value="${specification.specValue}"/>
										<dsp:getvalueof var="specType" value="${specification.specType}"/>
										<dsp:getvalueof var="booleanSpecValue" value="${specification.specBoolValue}"/>
										<dsp:getvalueof var="nodisplay" value="${false}"/>
										<c:if test="${specValue eq 'NA'}">
											<dsp:getvalueof var="nodisplay" value="${true}"/>
										</c:if>
										<c:choose>
											<c:when test="${specType eq 'TEXT'}">
												<c:choose>
													<c:when test="${nodisplay eq true}">
														<td></td>
													</c:when>
													<c:otherwise>
														<td>${specValue}</td>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:when test="${specType eq 'BOOLEAN'}">
												<c:choose>
													<c:when test="${booleanSpecValue eq true}">
														<td class="yes">Yes</td>
													</c:when>
													<c:otherwise>
														<td></td>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:when test="${specType eq 'INFOICON'}">
												<c:choose>
													<c:when test="${nodisplay eq true}">
														<td></td>
													</c:when>
													<c:when test="${specKey eq 'Muve Music'}">
														<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
														<dsp:param name="imageLink" value="img/plans/logo-muve-music.jpg" />
														<dsp:oparam name="output">
															<dsp:getvalueof var="liquidpixelurl" param="url" />
																<td class="tip">
																<span data-tooltip class="has-tip" title="<img src='${liquidpixelurl}'/>
																<br/>
																${specValue}">
																	i
																</span>
																</td>	
														</dsp:oparam>
														</dsp:droplet>
														
													</c:when>
													<c:otherwise>
														<td class="tip"><span data-tooltip class="has-tip" title="${specValue}">i</span></td>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${nodisplay eq true}">
														<td></td>
													</c:when>
													<c:otherwise>
														<td>${specValue}</td>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</dsp:oparam>
								</dsp:droplet>
							</tr>
						</dsp:oparam>
					</dsp:droplet>																																																											
				</tbody>
			</table>
		</div>
	</section>
	
	<!-- Plans Listing Mobile View -->
	<div id="accordion-content" class="row show-for-small">
		<h2>Smart Plans</h2>
		<div class="large-12 small-12 columns">
			<div class="section-container accordion" data-section="accordion">
				<dsp:droplet name="ForEach">
					<dsp:param name="array" value="${smartPlanVOList}"/>
					<dsp:param name="elementName" value="mobileSmartPlan"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="planPrice" param="mobileSmartPlan.finalPrice"/>
						<c:set var="splitPrice" value="${fn:split(planPrice, '.')}"/>
						<dsp:getvalueof var="mobileSmartPlanProductId" param="mobileSmartPlan.productId"/>
						<dsp:getvalueof var="mobileSmartPlanSkuId" param="mobileSmartPlan.defaultSkuId"/>
						<dsp:getvalueof var="count" param="count"/>
						<c:choose>
							<c:when test="${count eq 1}">
								<dsp:getvalueof var="activeClass" value="active"/>
							</c:when>
							<c:otherwise>
								<dsp:getvalueof var="activeClass" value=""/>
							</c:otherwise>
						</c:choose>
						<!-- START This is used for SEO Url droplet. -->
							<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
								<dsp:param name="id" value="${mobileSmartPlanProductId}"/>
								<dsp:param name="itemDescriptorName" value="plan-product"/>
								<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
								</dsp:oparam>
							</dsp:droplet>
						<!-- END This is used for SEO Url droplet. -->
						<section class="${activeClass}">
							<p class="title" data-section-title>
								<a href="${contextpath}${canonicalUrl}">
									$ ${splitPrice[0]} plan
								</a>
							</p>
							<div class="content" data-section-content>
								<p class="pricing"><sup>$</sup>${splitPrice[0]}<sub>/mo</sub></p>
								<div class="plan-content">							
									<div class="data-callout">
										<dsp:getvalueof var="dataLimit" param="mobileSmartPlan.dataLimit"/>
										<dsp:getvalueof var="dataMessage" param="mobileSmartPlan.dataMessage"/>
										${dataLimit}<sub>/mo</sub>
										<p>${dataMessage}</p>
									</div>									
									<dsp:getvalueof var="greenBoxContent" param="mobileSmartPlan.greenBoxContent"></dsp:getvalueof>
									<dsp:droplet name="IsNull">
									<dsp:param name="value" value="${greenBoxContent}"/>
									 <dsp:oparam name="false">
									<dsp:valueof value="${greenBoxContent}" valueishtml="true"></dsp:valueof>
											</dsp:oparam>
									</dsp:droplet>						
									
								</div>
								<c:choose>
									<c:when test="${manuallyEnteredZipCode eq true}">
										<dsp:include page="/cart/common/addToCart.jsp">
											<dsp:param name="addToCartClass" value="button small"/>
											<dsp:param name="productId" value="${mobileSmartPlanProductId}"/>
											<dsp:param name="skuId" value="${mobileSmartPlanSkuId}"/>
											<dsp:param name="quantity" value="1"/>
											<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
										</dsp:include>											
									</c:when>
									<c:otherwise>
										<a class="grey-add-cart button small disabled secondary has-tooltip" data-tooltip title="Please enter your home zip code to see prices and add to cart." href="#">Add to Cart</a>
									</c:otherwise>
								</c:choose>
								<!-- START This is used for SEO Url droplet. -->
								<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
									<dsp:param name="id" value="${mobileSmartPlanProductId}"/>
									<dsp:param name="itemDescriptorName" value="plan-product"/>
									<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
									<dsp:oparam name="output">
										<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
									</dsp:oparam>
								</dsp:droplet>
								<!-- END This is used for SEO Url droplet. -->
								<dsp:getvalueof var="phoneId" param="phoneId"></dsp:getvalueof>
								<c:if test="${phoneId ne null}">
									<div class="learn-more">
								</c:if>
								<a href="${contextpath}${canonicalUrl}" class="circle-arrow white">Learn More</a>
								<c:if test="${phoneId ne null}">
									</div>
								</c:if>
							</div>
						</section>
					</dsp:oparam>
				</dsp:droplet>				
			</div>
			
			<h2>Feature Plans</h2>
			<div class="section-container accordion" data-section="accordion">
				<dsp:droplet name="ForEach">
					<dsp:param name="array" value="${featurePlanVOList}"/>
					<dsp:param name="elementName" value="mobileFeaturePlan"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="planPrice" param="mobileFeaturePlan.finalPrice"/>
						<dsp:getvalueof var="mobileFeaturePlanProductId" param="mobileFeaturePlan.productId"/>
						<dsp:getvalueof var="mobileFeaturePlanSkuId" param="mobileFeaturePlan.defaultSkuId"/>
						<c:set var="splitPrice" value="${fn:split(planPrice, '.')}"/>
						<!-- START This is used for SEO Url droplet. -->
							<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
								<dsp:param name="id" value="${mobileFeaturePlanProductId}"/>
								<dsp:param name="itemDescriptorName" value="plan-product"/>
								<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
								</dsp:oparam>
							</dsp:droplet>
						<!-- END This is used for SEO Url droplet. -->
						<section>
							<p class="title" data-section-title>
								<a href="${contextpath}${canonicalUrl}">
									$ ${splitPrice[0]} plan
								</a>
							</p>
							<div class="content feature-plan" data-section-content>
								<dsp:getvalueof var="planPrice" param="mobileFeaturePlan.finalPrice"/>
								<c:set var="splitPrice" value="${fn:split(planPrice, '.')}"/>
								<p class="pricing">
								<sup>$</sup>${splitPrice[0]}<sub>/mo</sub></p>								
								<div class="plan-content">								
									<dsp:getvalueof var="greenBoxContent" param="mobileFeaturePlan.greenBoxContent"></dsp:getvalueof>
									<dsp:droplet name="IsNull">
									<dsp:param name="value" value="${greenBoxContent}"/>
									 <dsp:oparam name="false">
									<dsp:valueof value="${greenBoxContent}" valueishtml="true"></dsp:valueof>
											</dsp:oparam>
									</dsp:droplet>					
								</div>
								<dsp:getvalueof var="mobileFeaturePlanProductId" param="mobileFeaturePlan.productId"/>
								<dsp:getvalueof var="mobileFeaturePlanSkuId" param="mobileFeaturePlan.defaultSkuId"/>
								<c:choose>
									<c:when test="${manuallyEnteredZipCode eq true}">
										<dsp:include page="/cart/common/addToCart.jsp">
											<dsp:param name="addToCartClass" value="button small"/>
											<dsp:param name="productId" value="${mobileFeaturePlanProductId}"/>
											<dsp:param name="skuId" value="${mobileFeaturePlanSkuId}"/>
											<dsp:param name="quantity" value="1"/>
											<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
										</dsp:include>											
									</c:when>
									<c:otherwise>
										<a class="grey-add-cart button small disabled secondary has-tooltip" data-tooltip title="Please enter your home zip code to see prices and add to cart." href="#">Add to Cart</a>
									</c:otherwise>
								</c:choose>
								<!-- START This is used for SEO Url droplet. -->
								<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
									<dsp:param name="id" value="${mobileFeaturePlanProductId}"/>
									<dsp:param name="itemDescriptorName" value="plan-product"/>
									<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
									<dsp:oparam name="output">
										<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
									</dsp:oparam>
								</dsp:droplet>
								<!-- END This is used for SEO Url droplet. -->
								<dsp:getvalueof var="phoneId" param="phoneId"></dsp:getvalueof>
								<c:if test="${phoneId ne null}">
									<div class="learn-more">
								</c:if>
								<a href="${contextpath}${canonicalUrl}" class="circle-arrow white">Learn More</a>
								<c:if test="${phoneId ne null}">
									</div>
								</c:if>
							</div>
						</section>
					</dsp:oparam>
				</dsp:droplet>
			</div>			
		</div>
	</div>
</dsp:page>