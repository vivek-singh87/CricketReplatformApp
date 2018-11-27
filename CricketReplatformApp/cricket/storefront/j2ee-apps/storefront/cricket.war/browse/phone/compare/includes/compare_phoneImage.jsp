<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<!-- Starting ForEach Droplet  -->
<dsp:droplet name="ForEach">
	 <dsp:param name="array" param="productList" />
	 <dsp:oparam name="outputStart">
		<tr class="top-heading">
			<th>
				<h1>
					<strong>Compare<%-- <crs:outMessage key="cricket_phonecompare_compare"/> --%></strong> 
					<span>Our<%-- <crs:outMessage key="cricket_phonecompare_our"/> --%><em>Phones<%-- <crs:outMessage key="cricket_phonecompare_phones"/> --%></em></span>
				</h1>
			</th>
	 </dsp:oparam>
	 <dsp:oparam name="output">
	 		<dsp:getvalueof var="productId" param="element.product.id"/>
	 		<dsp:getvalueof var="skuId" param="element.product.childSkus[0].id"/>
			<th>
				<dsp:getvalueof var="largeImages" param="element.product.fullImage.url"/>
				<dsp:getvalueof var="height" bean="ImageConfiguration.height.phoneCompareImage" />
				<c:if test="${fn:length(largeImages) > 0}">				
					<!--Start Droplet used to show images from Liquid Pixel Server -->
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${largeImages}" />
						<dsp:param name="imageHeight" value="${height}"/>
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<%-- <dsp:img src="${liquidpixelurl}" alt="" style="height: 264px; width: 126px"/> --%>
							<a href="${contextpath}/browse/phone/phone_details.jsp?productId=${productId}&skuId=${skuId}"><img src="${liquidpixelurl}" alt="" /></a>
						</dsp:oparam>
					</dsp:droplet>
					<!--End Droplet used to show images from Liquid Pixel Server -->
				</c:if>

			</th>
	 </dsp:oparam>
	 <dsp:oparam name="outputEnd">
		</tr>
	 </dsp:oparam>		
</dsp:droplet>
<!-- Ending ForEach Droplet  -->
</dsp:page>