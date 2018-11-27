<dsp:page>
	<dsp:importbean bean="/atg/targeting/TargetingForEach"/>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:getvalueof var="mobileSuffix" bean="CricketConfiguration.mobileSuffix"/>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<!-- Static Banner -->
	<dsp:droplet name="TargetingForEach">
		<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/SearchListingPageBottomBanner" name="targeter"/>
		<dsp:param name="start" value="1"/>
		<dsp:param name="howMany" value="1"/>
		<dsp:param name="elementName" value="targetedContent"/>
		<dsp:oparam name="output">
			<dsp:getvalueof param="targetedContent.url" var="imageURL"/>	
			<dsp:getvalueof param="targetedContent.landingUrl" var="landingUrl"/>
			<div class="static-banner hide-for-small" style="background-image: url(${contextpath}/${imageURL});">
			    <a class="circle-arrow white" href="${landingUrl}"></a>
			</div>
		</dsp:oparam>
	</dsp:droplet>

	<!-- Mobile Version -->
	<dsp:droplet name="TargetingForEach">
		<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/SearchListingPageBottomBanner${mobileSuffix}" name="targeter"/>
		<dsp:param name="start" value="1"/>
		<dsp:param name="howMany" value="1"/>
		<dsp:param name="elementName" value="targetedContent"/>
		<dsp:oparam name="output">
			<dsp:getvalueof param="targetedContent.url" var="imageURL"/>	
			<dsp:getvalueof param="targetedContent.landingUrl" var="landingUrl"/>
			<div class="static-banner show-for-small" style="background-image: url(${contextpath}/${imageURL}); background-size: cover;">
			    <a class="circle-arrow white" href="${landingUrl}"></a>
			</div>
		</dsp:oparam>
	</dsp:droplet>
	
</dsp:page>