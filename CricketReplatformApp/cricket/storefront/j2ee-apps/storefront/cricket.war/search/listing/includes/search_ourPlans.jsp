<dsp:page>
	<dsp:importbean bean="/atg/targeting/TargetingRange"/>
	<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
	<dsp:getvalueof var="heightDesk" bean="ImageConfiguration.height.homePhoneImage" />
	<dsp:getvalueof var="heightMobile" bean="ImageConfiguration.height.mobileHomePhoneImage" />
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<section id="phone-plans" class="three-callouts" style="display:none;margin-bottom:2em;padding-top:2em">
	<div class="row">
		<div class="large-8 small-12 columns">
			<h2>Our <span>Plans</span></h2>
		</div>
		<div class="large-4 small-12 hide-for-small columns">
			<a href="#" class="circle-arrow right">See All Plans</a>
		</div>
	</div>
	
	<!-- Mobile Version -->
	<a href="#" class="prev show-for-small">Prev</a>
	<a href="#" class="next show-for-small">Next</a>
	<div class="swiper-container show-for-small">
		<div class="swiper-wrapper text-center">
		<!--First Slide-->
			<div class="swiper-slide">
				<dsp:droplet name="TargetingRange">
					<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/SearchPageMyPlans" name="targeter"/>
					<dsp:param name="start" value="1"/>
			  		<dsp:param name="howMany" value="3"/>
			  		<dsp:param name="elementName" value="plan"/>
					<dsp:oparam name="output">
						<dsp:getvalueof param="plan.fullImage.url" var="imageURL"/>
						<dsp:getvalueof var="planProductId" param="plan.id"/>
						<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
							<dsp:param name="id" value="${planProductId}"/>
							<dsp:param name="itemDescriptorName" value="plan-product"/>
							<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
							</dsp:oparam>
						</dsp:droplet>
						<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
							<dsp:param value="${imageURL}" name="imageLink"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="liquidpixelurl" param="url"/>
							</dsp:oparam>
						</dsp:droplet>
						<div id="plan-50" class="large-4 small-12 columns text-center">
							<div class="plan" style="background-image:url(${liquidpixelurl});">
							</div>
							<a class="circle-arrow" href="${contextPath}${canonicalUrl}">View Details</a>
						</div>
					</dsp:oparam>
				</dsp:droplet>
			</div>
		</div>
	</div>
	<div class="row hide-for-small">
		<dsp:droplet name="TargetingRange">
			<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/SearchPageMyPlans" name="targeter"/>
			<dsp:param name="start" value="1"/>
	  		<dsp:param name="howMany" value="3"/>
	  		<dsp:param name="elementName" value="plan"/>
			<dsp:oparam name="output">
				<dsp:getvalueof param="plan.fullImage.url" var="imageURL"/>
				<dsp:getvalueof var="planProductId" param="plan.id"/>
				<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
					<dsp:param name="id" value="${planProductId}"/>
					<dsp:param name="itemDescriptorName" value="plan-product"/>
					<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
					</dsp:oparam>
				</dsp:droplet>
				<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
					<dsp:param value="${imageURL}" name="imageLink"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
					</dsp:oparam>
				</dsp:droplet>
				<div id="plan-50" class="large-4 small-12 columns text-center">
					<div class="plan" style="background-image:url(${liquidpixelurl});">
					</div>
					<a class="circle-arrow" href="${contextPath}${canonicalUrl}">View Details</a>
				</div>
			</dsp:oparam>
		</dsp:droplet>
	</div>
</section>
</dsp:page>