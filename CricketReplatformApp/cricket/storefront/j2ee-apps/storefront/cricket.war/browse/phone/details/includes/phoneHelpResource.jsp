 <dsp:page>
 <dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	 <section>
	         <p class="title" data-section-title><a href="#help"><crs:outMessage key="cricket_phonedetails_HelpResources"/><!-- Quick Start --></a></p>
	         <div class="content" data-slug="help" data-section-content>
	           <div class="copy">
	            <!--  Place the complete HTML content in to the BCC and display the content for quick start-->
	            <h4><crs:outMessage key="cricket_phonedetails_Quick_Start"/></h4>	           
			      <dsp:valueof valueishtml="true" param="Product.quickStart.data"></dsp:valueof>
			      <!--   Place the PDF file in server and give the URL in to the BCC and display the link for User Manul Downloads-->
	           			<dsp:getvalueof var="userManualPDFUrl" param="Product.userManual.url"></dsp:getvalueof>
	           			<dsp:droplet name="IsNull">
								<dsp:param name="value" value="${userManualPDFUrl}"/>
									<dsp:oparam name="false">
									<h4><crs:outMessage key="cricket_phonedetails_Downloads"/></h4>
	           							<p>
		           							<a href="${contextpath}${userManualPDFUrl}" class="pdf" target="_blank"><crs:outMessage key="cricket_phonedetails_UserManual"/></a>
		           						</p>
		           					</dsp:oparam>
		           					<dsp:oparam name="true">
		           					</dsp:oparam>
	           			</dsp:droplet>
	           		<!-- NEW: Added lines 470-473 below with new content
	           		Here we have to provide the complete HTML content in to the BCC and display the link for Accessibility Information-->
		            <h4><crs:outMessage key="cricket_phonedetails_AccessibilityInformation"/><!-- Accessibility Information --></h4>
		          	 <dsp:valueof valueishtml="true" param="Product.accessibilityInfo.data"></dsp:valueof>
	           </div>
	         </div>
	</section>
</dsp:page>