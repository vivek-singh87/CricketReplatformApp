<dsp:page>
	<dsp:importbean bean="/atg/targeting/TargetingRange"/>
	<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
	<dsp:getvalueof var="heightDesk" bean="ImageConfiguration.height.phoneDetailRecommendedProduct" />
	<dsp:getvalueof var="heightMobile" bean="ImageConfiguration.height.mobileSearchPhoneResult" />
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<hr>
	<section id="similar-phones" class="three-callouts" style="display:none">
	<div class="row">
		<div class="large-8 small-12 columns">
			<h2>Our <span>Phones</span></h2>
		</div>
		<div class="large-4 small-12 hide-for-small columns">
			<a href="#" class="circle-arrow right">See All Phones</a>
		</div>
	</div>
	<a href="#" class="prev show-for-small">Prev</a>
	<a href="#" class="next show-for-small">Next</a>		
	<div class="swiper-container show-for-small">
		<div class="swiper-wrapper">
			<dsp:droplet name="TargetingRange">
				<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/SearchPagePhonesYouWant" name="targeter"/>
				<dsp:param name="start" value="1"/>
		  		<dsp:param name="howMany" value="5"/>
		  		<dsp:param name="elementName" value="phone"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="productId" param="phone.id"/>
					<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
						<dsp:param name="id" value="${productId}"/>
						<dsp:param name="itemDescriptorName" value="phone-product"/>
						<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
						<dsp:oparam name="output">
							<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
						</dsp:oparam>
					</dsp:droplet>
					<dsp:getvalueof param="phone.fullImage.url" var="imageURL"/>
					<div class="swiper-slide text-center"> 
						<a href="${contextPath}${canonicalUrl}">
							<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
								<dsp:param name="imageLink" value="${imageURL}" />
								<dsp:param name="imageHeight" value="${heightMobile}"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="liquidpixelurl" param="url"/>
								</dsp:oparam>
							</dsp:droplet>
							<img src="${liquidpixelurl}" alt=""/>
						</a>
						<h4><dsp:valueof param="phone.displayName"/></h4>
						<a href="${contextPath}${canonicalUrl}" class="button">Shop Now</a>
					</div>
				</dsp:oparam>
			</dsp:droplet>
		</div>	
	</div>	
	<div class="row cta show-for-small">
		<div class="small-12 columns">
			<p><a href="#" class="circle-arrow">See All Phones</a></p>
		</div>
	</div>
	<!-- Similar Phones - Desktop -->		
	<div class="row hide-for-small">
		<dsp:droplet name="TargetingRange">
			<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/SearchPagePhonesYouWant" name="targeter"/>
			<dsp:param name="start" value="1"/>
	  		<dsp:param name="howMany" value="5"/>
	  		<dsp:param name="elementName" value="phone"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="productId" param="phone.id"/>
				<dsp:getvalueof param="phone.fullImage.url" var="imageURL"/>
				<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
					<dsp:param name="id" value="${productId}"/>
					<dsp:param name="itemDescriptorName" value="phone-product"/>
					<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
					</dsp:oparam>
				</dsp:droplet>
				<div class="large-3 small-12 columns text-center">
					<a href="${contextPath}${canonicalUrl}">
						<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
							<dsp:param name="imageLink" value="${imageURL}" />
							<dsp:param name="imageHeight" value="${heightDesk}"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="liquidpixelurl" param="url"/>
							</dsp:oparam>
						</dsp:droplet>
						<img src="${liquidpixelurl}" alt=""/>
					</a>
					<h4><dsp:valueof param="phone.displayName"/></h4>
					<a href="${contextPath}${canonicalUrl}" class="button">Shop Now</a>
				</div>
			</dsp:oparam>
		</dsp:droplet>						
	</div>		
</section>
</dsp:page>