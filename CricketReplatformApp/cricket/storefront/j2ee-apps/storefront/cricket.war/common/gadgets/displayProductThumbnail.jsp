<dsp:page>
	<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="size" param="size"/>
	<dsp:getvalueof var="count" param="count"/>
	<c:if test="${count mod 3 == 1}">
		<tr>
	</c:if>
		<td style="border: 1px solid #7a7">
			<dsp:valueof param="product.displayName"/>
			<dsp:getvalueof var="imageURL" param="product.fullImage.url"/>
			<!--   <dsp:img height="485" width="235" src="${contextpath}/${imageURL}"/> -->
			<dsp:getvalueof var="height" bean="ImageConfiguration.height.phoneListingThumb" />
				<!--Start Droplet used to show images from Liquid Pixel Server -->
				<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
					<dsp:param name="imageLink" value="${imageURL}" />
					<dsp:param name="imageHeight" value="${height}"/>
					<dsp:oparam name="output">
					<dsp:getvalueof var="liquidpixelurl" param="url"/>
							 <%--  <dsp:img height="485" width="235" src="${liquidpixelurl}"/> --%>
							   <dsp:img src="${liquidpixelurl}"/>
							 
					</dsp:oparam>
				</dsp:droplet>
				<!--End Droplet used to show images from Liquid Pixel Server -->
													
			
		</td>
	<c:if test="${count mod 3 == 0 || count == size }">
		</tr>
	</c:if>
				
</dsp:page>