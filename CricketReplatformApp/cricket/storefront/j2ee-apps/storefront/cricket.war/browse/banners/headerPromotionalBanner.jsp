<dsp:page>
<dsp:importbean bean="/atg/targeting/TargetingForEach"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<c:set var="TargeterName" value=""/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:droplet name="Switch">
      <dsp:param name="value" param="PAGE_NAME"/>
      <dsp:oparam name="PHONE_LISTING">
       	<c:set var="TargeterName" value="/atg/registry/RepositoryTargeters/Promotional/browseshop/PhoneListingPageTopBanner" />
       	<dsp:droplet name="TargetingForEach">
			<dsp:param bean="${TargeterName}" name="targeter"/>
			<dsp:param name="start" value="1"/>
	  		<dsp:param name="howMany" value="1"/>
			<dsp:oparam name="output">	
				<dsp:getvalueof param="element.url" var="imageURL"/>
			</dsp:oparam>
		</dsp:droplet>
		
		<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
				<dsp:param name="imageLink" value="${imageURL}" />
				<dsp:oparam name="output">
					<dsp:getvalueof var="liquidpixelurl" param="url" />
					<div class="static-banner hide-for-small" style="background-image: url(${liquidpixelurl});">
						<div class="content">
							<h1>The Phones<br> <span>You Want</span></h1>
							<p>Find the right phone for you <br>at Cricket</p>
						</div>	
					</div>				
				</dsp:oparam>
			</dsp:droplet>
		
      </dsp:oparam>      
      <dsp:oparam name="ACCESSORY_LISTING">
       	<c:set var="TargeterName" value="/atg/registry/RepositoryTargeters/Promotional/browseshop/AccessoriesListingTopBanner" />
       	<dsp:droplet name="TargetingForEach">
			<dsp:param bean="${TargeterName}" name="targeter"/>
			<dsp:param name="start" value="1"/>
	  		<dsp:param name="howMany" value="1"/>
			<dsp:oparam name="output">	
				<dsp:getvalueof param="element.url" var="imageURL"/>	
			</dsp:oparam>
		</dsp:droplet>
		
		<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${imageURL}" />
						<dsp:oparam name="output">
							<dsp:getvalueof var="liquidpixelurl" param="url" />							
							<div class="static-banner hide-for-small" style="background-image: url(${liquidpixelurl});">
								<div class="content">
								  <h1>Check Out <br />Our Phone <span>Accessories</span></h1>
								</div>  
							</div>
				</dsp:oparam>
			</dsp:droplet>
       
      </dsp:oparam>     
 </dsp:droplet>
</dsp:page>