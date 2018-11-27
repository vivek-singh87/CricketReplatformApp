<dsp:importbean bean="/com/cricket/imageurl/ImageURLLookupDroplet"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<dsp:getvalueof var="overviewTitle" param="overviewsInfo.overviewTitle"></dsp:getvalueof>
<dsp:getvalueof var="overviewDesc" param="overviewsInfo.overviewDesc"></dsp:getvalueof>
<dsp:getvalueof var="overviewTopImage" param="overviewsInfo.overviewTopImage.url"></dsp:getvalueof>
<dsp:getvalueof var="overviewBottomImage" param="overviewsInfo.overviewBottomImage.url"></dsp:getvalueof>
<dsp:getvalueof var="overviewImage" param="overviewsInfo.overviewImage.url"></dsp:getvalueof>
		<c:if test="${overviewTitle !=null || overviewTopImage!= null || overviewDesc != null || overviewbotImage != null}">
			<div class="columns large-6 small-12 copy">
			<dsp:droplet name="IsNull">
				<dsp:param name="value" value="${overviewTitle}"/>
					<dsp:oparam name="false">
			     		<h3><dsp:valueof value="${overviewTitle}" valueishtml="true"></dsp:valueof></h3>
			     	</dsp:oparam>
			     </dsp:droplet>
			     <dsp:droplet name="IsNull">
					<dsp:param name="value" value="${overviewTopImage}"/>
						<dsp:oparam name="false">
							<!-- START Liquid Pixel Image display droplet -->
								<dsp:droplet name="ImageURLLookupDroplet">
									<dsp:param name="imageLink" value="${overviewTopImage}" />
									<dsp:oparam name="output">
									<dsp:getvalueof var="liquidpixelurl" param="url"/>													  
										 <img src="${liquidpixelurl}" alt=""/>
									</dsp:oparam>
							 </dsp:droplet>
						 <!--END Liquid Pixel Image display droplet-->
						</dsp:oparam>
				</dsp:droplet>
			           <p>${overviewDesc}</p>
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