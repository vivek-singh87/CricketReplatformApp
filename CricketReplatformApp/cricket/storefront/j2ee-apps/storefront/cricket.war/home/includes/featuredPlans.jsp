<dsp:page>
<dsp:importbean bean="/atg/targeting/TargetingRange"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>

<dsp:getvalueof var="contextpath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>

<!-- Plans -->
	<section id="section-plans" class="three-callouts">
		<div class="row">
			<div class="large-8 small-12 columns">
				<h2>Unlimited <span>Plans</span></h2>
			</div>
			<div class="large-4 small-12 hide-for-small columns">
				<dsp:droplet name="DimensionValueCacheDroplet">
					<dsp:param name="repositoryId" bean="CricketConfiguration.allPlansCategoryId"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="allPlansCategoryCacheEntry" param="dimensionValueCacheEntry" />
			      </dsp:oparam>
				</dsp:droplet>
				<a href="${contextpath}${allPlansCategoryCacheEntry.url}" class="circle-arrow right">See All Plans</a>
			</div>
		</div>	
		<a href="#" class="prev show-for-small">Prev</a>
		<a href="#" class="next show-for-small">Next</a>		
		<div class="swiper-container show-for-small">
			<div class="swiper-wrapper">
			<dsp:getvalueof var="mobileSuffix" bean="CricketConfiguration.mobileSuffix"/>
				<dsp:droplet name="TargetingRange">
					<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/HomePageFeaturedPlans${mobileSuffix}" name="targeter"/>
					<dsp:param name="start" value="1"/>
			  		<dsp:param name="howMany" value="3"/>
					<dsp:oparam name="output">
						<div class="swiper-slide">
							<dsp:getvalueof var="image" param="element.largeImage.url"/>
							<dsp:getvalueof var="imageLink" value="${contextpath}/${image}"/>
							<dsp:getvalueof var="planProductId" param="element.id"/>
							<dsp:getvalueof var="skuId" param="element.childSkus[0].id"/>
							<!-- START This is used for SEO Url droplet. -->
							<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
								<dsp:param name="id" value="${planProductId}"/>
								<dsp:param name="itemDescriptorName" value="plan-product"/>
								<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
								</dsp:oparam>
							</dsp:droplet>
							<!-- END This is used for SEO Url droplet. -->
							<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
								<dsp:param value="${image}" name="imageLink"/>
								<dsp:oparam name="output">
								<dsp:getvalueof var="value" param="url"/>
									<a onclick="cmCreateManualLinkClickTag('#')" href="${contextpath}${canonicalUrl}"><img src="<c:out value='${value}'/>" alt=""/></a>
								</dsp:oparam>
							</dsp:droplet>
						</div>
					</dsp:oparam>
				</dsp:droplet>
			</div>
		</div>	
		<div class="row cta show-for-small">
			<div class="small-12 columns">				
				<p><a href="${contextpath}${allPlansCategoryCacheEntry.url}" class="circle-arrow">See All Plans</a></p>
			</div>
		</div>		
		<div class="row hide-for-small">
			<dsp:droplet name="TargetingRange">
				<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/HomePageFeaturedPlans" name="targeter"/>
				<dsp:param name="start" value="1"/>
		  		<dsp:param name="howMany" value="3"/>
				<dsp:oparam name="output">
					<div class="large-4 small-12 columns">
						<dsp:getvalueof var="image" param="element.largeImage.url"/>
						<dsp:getvalueof var="imageLink" value="${contextpath}/${image}"/>
						<dsp:getvalueof var="planProductId" param="element.id"/>
						<dsp:getvalueof var="skuId" param="element.childSkus[0].id"/>
						<!-- START This is used for SEO Url droplet. -->
						<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
							<dsp:param name="id" value="${planProductId}"/>
							<dsp:param name="itemDescriptorName" value="plan-product"/>
							<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="canonicalUrl2" param="url" vartype="java.lang.String"/>
							</dsp:oparam>
						</dsp:droplet>
						<!-- END This is used for SEO Url droplet. -->
						<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
							<dsp:param name="imageLink" value="${image}" />
							<dsp:oparam name="output">
								<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
									<dsp:param name="id" value="${planProductId}"/>
									<dsp:param name="itemDescriptorName" value="plan-product"/>
									<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
									<dsp:oparam name="output">
										<dsp:getvalueof var="canonicalUrl2" param="url" vartype="java.lang.String"/>
									</dsp:oparam>
								</dsp:droplet>
								<dsp:getvalueof var="liquidpixelurl" param="url"/>
								<dsp:getvalueof var="value" param="url"/>
									<a onclick="cmCreateManualLinkClickTag('#')" href="${contextpath}${canonicalUrl2}"><img src="${liquidpixelurl}" alt="$60 a month, 2.5GB Full-Speed Data, Talk, Text Plus Music - Click to View Details"/></a>
								</dsp:oparam>
						</dsp:droplet>
					</div>
				</dsp:oparam>
			</dsp:droplet>
			<%--<div class="large-4 small-12 columns">
				<a href="#"><img src="${contextpath}/img/home/plans-1.jpg" alt=""/></a>
			</div>	
			<div class="large-4 small-12 columns">
				<a href="#"><img src="${contextpath}/img/home/plans-2.jpg" alt=""/></a>
			</div>	
			<div class="large-4 small-12 columns">
				<a href="#"><img src="${contextpath}/img/home/plans-3.jpg" alt=""/></a>
			</div>--%>					
		</div>		
	</section>
	</dsp:page>