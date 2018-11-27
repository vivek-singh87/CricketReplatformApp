<dsp:page>
<dsp:importbean bean="/atg/targeting/TargetingForEach"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
	<!-- Static Banner -->
	
	<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
			<dsp:param name="imageLink" value="img/phone-listing/phones-landing-banner.jpg" />
			<dsp:oparam name="output">
				<dsp:getvalueof var="liquidpixelurl" param="url" />
				<div class="static-banner hide-for-small" style="background-image: url(${liquidpixelurl});">
				  <div class="content">
				    <h1>The Phones<br /> <span>You Want</span></h1>
				    <p>Find the right phone for you <br />at Cricket</p>
				  </div>	
				</div>					
			</dsp:oparam>
	</dsp:droplet>
	
<!-- 	
	<dsp:droplet name="TargetingForEach">
		<dsp:param bean="/atg/registry/RepositoryTargeters/browseshop/PhoneListingPageTopBanner" name="targeter"/>
		<dsp:param name="start" value="1"/>
  		<dsp:param name="howMany" value="1"/>
		<dsp:oparam name="output">	
			
			<div class="static-banner hide-for-small">
			  <div class="content">
			        <dsp:getvalueof param="element.fullImage.url" var="imageURL"/>
					<a href="/browse/product/productDetails.jsp">
						<dsp:img src="${contextpath}/${imageURL}" alt=""/>
					</a>
			  </div>	
			</div>			
		</dsp:oparam>
	</dsp:droplet>
	 -->
</dsp:page>