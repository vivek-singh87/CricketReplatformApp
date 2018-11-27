<dsp:page>
<dsp:importbean bean="/atg/targeting/TargetingForEach"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<!-- Static Banner -->
<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
<dsp:param name="imageLink" value="img/phone-listing/phones-landing-footer-CTA.jpg" />
<dsp:oparam name="output">
	<dsp:getvalueof var="liquidpixelurl" param="url" />
		<div class="static-banner footer hide-for-small" style="background-image: url(${liquidpixelurl});">
		  <div class="content">
		    <h1><span>Take it</span><br />Home Today</h1>
		    <p>Cricket makes it easy for you to take home your favorite phone. Right now, get a Samsung Galaxy S4 with only$25 down.</p>
		    <a href="#" class="circle-arrow">Learn more</a>
		  </div>	
		</div>	
</dsp:oparam>
</dsp:droplet>

<!-- 	
	<dsp:droplet name="TargetingForEach">
		<dsp:param bean="/atg/registry/RepositoryTargeters/browseshop/PhoneDetailsPageBottomBanner" name="targeter"/>
		<dsp:param name="start" value="1"/>
  		<dsp:param name="howMany" value="1"/>
		<dsp:oparam name="output">			
		<div class="static-banner footer hide-for-small">
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