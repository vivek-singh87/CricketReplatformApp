<dsp:importbean bean="/com/cricket/imageurl/ImageURLLookupDroplet"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<dsp:getvalueof var="overviewTitle" param="overviewsInfo.overviewTitle"></dsp:getvalueof>
<dsp:getvalueof var="overviewDesc" param="overviewsInfo.overviewDesc"></dsp:getvalueof>
<dsp:getvalueof var="overviewTopImage" param="overviewsInfo.overviewTopImage.url"></dsp:getvalueof>
<dsp:getvalueof var="overviewBottomImage" param="overviewsInfo.overviewBottomImage.url"></dsp:getvalueof>
<dsp:getvalueof var="overviewImage" param="overviewsInfo.overviewImage.url"></dsp:getvalueof>
		
		<c:if test="${overviewTitle !=null || overviewTopImage!= null || overviewDesc != null || overviewbotImage != null}">
			<div class="columns large-6 small-12 copy">
			     <p><span style="font-family: GothamBold;text-transform: uppercase;font-size: 1.9em;line-height: 1.5em;letter-spacing: -0.05em;"><dsp:valueof value="${overviewTitle}" valueishtml="true"></dsp:valueof></span>&nbsp;
			           ${overviewDesc}</p>  
			    <dsp:droplet name="IsNull">
					<dsp:param name="value" value="${overviewBottomImage}"/>
						<dsp:oparam name="false">
				<!-- START Liquid Pixel Image display droplet -->
					<dsp:droplet name="ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${overviewBottomImage}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>													  
							 <img src="${liquidpixelurl}" alt=""/>
						</dsp:oparam>
				 </dsp:droplet>
			 	<!--END Liquid Pixel Image display droplet-->
			 	</dsp:oparam>
			 	</dsp:droplet>
	         </div>
	     </c:if>
         <div class="columns large-6 hide-for-small image">
         <dsp:droplet name="IsNull">
		<dsp:param name="value" value="${overviewImage}"/>
			<dsp:oparam name="false">
		          		<!-- START Liquid Pixel Image display droplet -->
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