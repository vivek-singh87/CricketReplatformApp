<dsp:importbean bean="/com/cricket/imageurl/ImageURLLookupDroplet"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<dsp:getvalueof var="overviewImage" param="overviewsInfo.overviewImage.url"></dsp:getvalueof>

<div class="columns large-12 small-12 copy">
	<dsp:droplet name="IsNull">
		<dsp:param name="value" value="${overviewImage}"/>
		<dsp:oparam name="false">
			<dsp:droplet name="ImageURLLookupDroplet">
				<dsp:param name="imageLink" value="${overviewImage}" />
				<dsp:oparam name="output">
				<dsp:getvalueof var="liquidpixelurl" param="url"/>													  
					 <img src="${liquidpixelurl}" alt=""/>
				</dsp:oparam>
			</dsp:droplet>
	</dsp:oparam>
	</dsp:droplet>
</div>