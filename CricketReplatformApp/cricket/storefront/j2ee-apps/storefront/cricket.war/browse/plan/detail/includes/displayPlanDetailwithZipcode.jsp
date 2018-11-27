<dsp:page>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:importbean bean="/com/cricket/browse/GetPlanGroupDroplet"/>
<!-- Plan overview detail  section with Zipcode-->
	<dsp:droplet name="GetPlanGroupDroplet">
	<dsp:param name="planGroupItems" param="Product.planGroups"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="planGroup" param="planGroup"></dsp:getvalueof>
	</dsp:oparam>
	</dsp:droplet>
	<section id="section-banner">
	<!--// NOTE: If this plan details are for a smartplan, please add CSS class of "smartplan" like so, if not leave it off to show correct bg image -->		
		<div class="section-banner-content smartplan">
			<!-- START Mobile Version for Banner-->
			<div class="row show-for-small small-header">
				<div class="columns small-12 text-center">
					<h2><dsp:valueof param="Product.displayName"/><span>/mo.</span></h2>
				</div>
			</div>
			<!-- END Mobile Version for Banner-->
			<div class="row">
				<div class="large-8 small-12 columns">
					<div id="hero-price-container">
						<span id="hero-price">
						<dsp:include page="/browse/includes/priceLookup.jsp">
							<dsp:param name="skuId" param="Product.childSKUs[0].id" />
							<dsp:param name="productId" param="Product.id" />
						</dsp:include>
						<c:if test="${empty retailPrice || retailPrice eq 0}">
							<dsp:getvalueof var="retailPrice" param="Product.childSKUs[0].listPrice"/>
						</c:if>
						<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice}" var="retailPrice" />
						<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>	
							<sup>$</sup><c:out value="${splitPrice[0]}"/>
						</span>
						<span id="price-term">	/mo</span>					
							<dsp:droplet name="/atg/dynamo/droplet/Switch">
							      <dsp:param name="value" param="Product.ratePlanType"/>
							      <dsp:oparam name="D">
									<div class="data-callout">
										<dsp:valueof value="${planGroup.dataLimit}"/><span>/mo</span>
										<p><dsp:valueof value="${planGroup.dataMessage}"/></p>
									</div>						       	
							      </dsp:oparam>
							 </dsp:droplet>						 
							 <dsp:droplet name="IsNull">
								<dsp:param name="value" value="${planGroup.greenBoxContent}"/>
								<dsp:oparam name="false">
									<span id="hero-price-description">
										<dsp:valueof value="${planGroup.greenBoxContent}" valueishtml="true"></dsp:valueof>
									</span>
								</dsp:oparam>
							</dsp:droplet>
					</div>
				</div>
				
				<!-- START Display Plan Detail/Price and add to cart button -->
				<div class="large-4 small-12 columns">
					<div id="hero-plan-detail-container">
								<h1><div class="hide-for-small"><dsp:valueof param="Product.displayName"/><span>/mo.</span></div></h1>
						<div class="hero-plan-detail-content-section">
							<div id="hero-plan-monthly-label">Monthly Price:</div>
							<div id="hero-plan-monthly-price">
							<dsp:include page="/browse/includes/priceLookup.jsp">
								<dsp:param name="skuId" param="Product.childSKUs[0].id" />
								<dsp:param name="productId" param="Product.id" />
							</dsp:include>
							<c:if test="${empty retailPrice || retailPrice eq 0}">
								<dsp:getvalueof var="retailPrice" param="Product.childSKUs[0].listPrice"/>
								
							</c:if>
							<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice}" var="retailPrice" />
							<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>
							<sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup>
						</div>
						</div>
						<div class="hero-plan-detail-content-section">
							<dsp:include page="/cart/common/addToCart.jsp">
								<dsp:param name="addToCartClass" value="button large add-to-cart"/>
								<dsp:param name="productId" param="Product.id"/>
								<dsp:param name="skuId" param="Product.childSKUs[0].id"/>
								<dsp:param name="quantity" value="1"/>
								<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
							</dsp:include>
						</div>
					</div>
				</div>
				<!-- END Display Plan Detail/Price and add to cart button -->
			</div>
			<!-- START Display Other related plans -->
			<div class="row">
				<div class="large-12 small-12 columns">
					<div id="hero-other-plan-container">
						<span id="other-plan-header">
							<%-- <crs:outMessage key="cricket_plandetails_withZipcode_other_smart_plans"/> --%>
							<dsp:getvalueof var="ratePlanType" param="Product.ratePlanType"/>	
							<dsp:getvalueof var="relatedPrdts" param="Product.fixedRelatedProducts"/>							
							<c:if test="${!empty relatedPrdts}">					
								<c:choose>
									<c:when test="${ratePlanType eq 'V'}">
										Other Feature Plans:
									</c:when>
									<c:when test="${ratePlanType eq 'D'}">
										Other Smart Plans:
									</c:when>
								</c:choose>
							</c:if>
						</span>
						<dsp:droplet name="/atg/dynamo/droplet/ForEach">
							<dsp:param name="array" param="Product.fixedRelatedProducts"/>
								<dsp:param name="elementName" value="fixedRelatedProducts"/>					
								<dsp:oparam name="output">	
									<dsp:getvalueof var="productId" param="fixedRelatedProducts.id"></dsp:getvalueof>
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
									<a class="button medium other-button" href="${contextpath}${canonicalUrl}">																
										<dsp:getvalueof var="retailPrice" param="fixedRelatedProducts.childSKUs[0].listPrice"/>
										<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice}" var="retailPrice" />
										<c:set var="otherSplitPrice" value="${fn:split(retailPrice, '.')}"/>	
											$<c:out value="${otherSplitPrice[0]}"/> PLAN
										</span>
									</a>
								</dsp:oparam>
						</dsp:droplet>
						<br/>
						<dsp:droplet name="DimensionValueCacheDroplet">
							<dsp:param name="repositoryId" bean="CricketConfiguration.allPlansCategoryId"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="plansCategoryCacheEntry" param="dimensionValueCacheEntry" />
						  </dsp:oparam>
						</dsp:droplet>					
							<a class="circle-arrow" href="${contextpath}${plansCategoryCacheEntry.url}">See all plans</a>												
					</div>
				</div>
			</div>
			<!-- END Display Other related plans -->
		</div>
	</section>


</dsp:page>