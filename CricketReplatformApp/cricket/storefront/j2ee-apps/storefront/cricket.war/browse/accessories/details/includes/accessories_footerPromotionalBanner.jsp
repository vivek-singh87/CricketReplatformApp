<dsp:page>
	<!-- Bottom CTA - same for all the phone pages -->
	<!-- Static Banner -->
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
		<dsp:param name="imageLink" value="img/accessories-listing/accessories-landing-footer-CTA.jpg" />
		<dsp:oparam name="output">
			<dsp:getvalueof var="liquidpixelurl" param="url" />
				<div id="why-cricket" class="static-banner footer hide-for-small" style="background-image: url(${liquidpixelurl});">
				  <div class="content">
				    <h1>Why Cricket?<br /><span>Because you can save $5 a month  with auto bill pay</span></h1>
				    <a href="#" class="circle-arrow white">Learn more</a>
				  </div>	
				</div>	
		</dsp:oparam>
		</dsp:droplet>
	
	<!-- Mobile Version -->
	<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
		<dsp:param name="imageLink" value="img/accessories-listing/accessories-landing-footer-CTA-mobile.jpg" />
		<dsp:oparam name="output">
			<dsp:getvalueof var="liquidpixelurl" param="url" />
			<div class="static-banner footer show-for-small" style="background-image: url(${liquidpixelurl}); background-size: cover;"></div>
		</dsp:oparam>
		</dsp:droplet>
	
</dsp:page>
