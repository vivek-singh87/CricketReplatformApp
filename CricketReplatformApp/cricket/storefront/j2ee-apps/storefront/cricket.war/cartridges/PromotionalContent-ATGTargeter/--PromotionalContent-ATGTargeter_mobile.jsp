<dsp:page>
<dsp:importbean bean="/atg/targeting/TargetingRange"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>

	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
	<dsp:getvalueof var="targeterName" vartype="java.lang.String"  value="${contentItem.componentPath}"/>
	<dsp:getvalueof var="mobileSuffix" bean="CricketConfiguration.mobileSuffix"/>
	
	<dsp:droplet name="TargetingRange">
		<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/${targeterName}${mobileSuffix}" name="targeter"/>
		<dsp:param name="start" value="1"/>
  		<dsp:param name="howMany" value="5"/>
		<dsp:oparam name="output">
			<div class="swiper-slide">
				<dsp:getvalueof var="productId" param="element.id"/>
				<dsp:getvalueof var="skuId" param="element.childSkus[0].id"/>
				<dsp:getvalueof param="element.fullImage.url" var="imageURL"/>
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
				<a href="${contextPath}${canonicalUrl}">
				<!--Start Droplet used to show images from Liquid Pixel Server -->
				<dsp:getvalueof var="height" bean="ImageConfiguration.height.mobileHomePhoneImage" />
				
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${imageURL}" />
						<dsp:param name="imageHeight" value="${height}"/>
						<dsp:oparam name="output">
							<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<dsp:img src="${liquidpixelurl}" alt=""/>
						</dsp:oparam>
					</dsp:droplet>
				<!--End Droplet used to show images from Liquid Pixel Server -->	
				</a>							
				<dsp:getvalueof var="phonedisplayName" param="element.displayName"/>
					
					<dsp:droplet name="/com/cricket/browse/droplet/DisplayNameDroplet">
						<dsp:param name="displayName" value="${phonedisplayName}" />					
						<dsp:oparam name="output">
							<dsp:getvalueof var="productDisplayName" param="displayName"/>				
							<dsp:getvalueof var="hasSpecialSymbol" param="hasSpecialSymbol"/>
							<dsp:getvalueof var="firstString" param="firstString"/>
							<dsp:getvalueof var="secondString" param="secondString"/>
							<dsp:getvalueof var="specialSymbol" param="specialSymbol"/>
							<c:choose>
							<c:when test="${hasSpecialSymbol eq true}">	
								<h3>${firstString}<sup>${specialSymbol}</sup>${secondString}</h3>								
							</c:when>	
							<c:otherwise>
								<h3>${productDisplayName}</h3>
							</c:otherwise>
						</c:choose>
							
						</dsp:oparam>
					</dsp:droplet>
				<p><a href="${contextPath}${canonicalUrl}" class="button medium">Shop Now</a></p>
			</div>
		</dsp:oparam>
	</dsp:droplet>
	 
	<%--<div class="large-4 small-12 columns">
		<div class="device-container">
			<a href="#"><img src="img/home/phones-1.png" alt=""/></a>
			<h3>Samsung Galaxy S4</h3>
			<p><a href="#" class="button medium">Shop Now</a></p>
		</div>
	</div>	
	<div class="large-4 small-12 columns">
		<div class="device-container">
			<a href="#"><img src="img/home/phones-2.png" alt=""/></a>
			<h3>Apple iPhone 5</h3>
			<p><a href="#" class="button medium">Shop Now</a></p>			
		</div>
	</div>	
	<div class="large-4 small-12 columns">
		<div class="device-container">
			<a href="#"><img src="img/home/phones-3.png" alt=""/></a>
			<h3>HTC One V</h3>
			<p><a href="#" class="button medium">Shop Now</a></p>			
		</div>
	</div> --%>
	
	
</dsp:page>