<dsp:page>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/browse/GetPlanGroupDroplet"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<section id="section-plans-list">           
		<div class="row">
			<div class="large-9 small-12 columns feature-tab-container">
				<div id="features-tab"><%-- <crs:outMessage key="cricket_planoverview_included"/> --%>Included</div>
			</div>			
		</div>
		<div class="row">
			<div class="large-12 small-12 columns">
				<div id="feature-list-container">			
					<dsp:droplet name="GetPlanGroupDroplet">
						<dsp:param name="planGroupItems" param="Product.planGroups"/>
						<dsp:oparam name="output">
						 <dsp:droplet name="/atg/dynamo/droplet/ForEach">
							<dsp:param name="array" param="planGroup.planFeatures"/>			
							<dsp:param name="elementName" value="planFeature"/>    
										
							<dsp:oparam name="output"> 
							<div class="row">
								<div class="large-1 small-12 columns feature-logo">
								<dsp:getvalueof var="image" param="planFeature.featureImage.url"/>
								<!-- START display image from Liquid pixel server-->
									<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
											<dsp:param name="imageLink" value="${image}" />
											<dsp:oparam name="output">
											<dsp:getvalueof var="liquidpixelurl" param="url"/>
											
												<img src="${liquidpixelurl}" /></img>
											
											</dsp:oparam>
									 </dsp:droplet>
								<!-- END display image from Liquid pixel server-->
								</div>
								
								<div class="large-2 small-12 columns feature feature-name">
									<dsp:valueof param="planFeature.featureName"/>
									<dsp:droplet name="/atg/dynamo/droplet/IsEmpty">
										<dsp:param name="value" param="planFeature.featureAdditionalInfo"/>
										<dsp:oparam name="false">
											<span><dsp:valueof param="planFeature.featureAdditionalInfo"/></span>
										</dsp:oparam>
									</dsp:droplet>
								</div>
								 <dsp:droplet name="/atg/dynamo/droplet/ForEach">
									<dsp:param name="array" param="planFeature.planFeatureSpecs"/>			
									<dsp:param name="elementName" value="planFeatureSpec"/>   
									<dsp:getvalueof var="size" param="size"/>
									<dsp:getvalueof var="count" param="count"/>
									<dsp:oparam name="outputStart">
										<c:set var="limit" value="${size}"/>
									</dsp:oparam>
										<dsp:oparam name="output"> 
											<div class="large-3 small-12 columns feature" style="float:left;">
												<ul>
														<dsp:getvalueof var="info" param="planFeatureSpec.featureSpecAdditionalInfo"/>
														<c:choose>
															<c:when test="${info eq null}">
															<li><dsp:valueof param="planFeatureSpec.featureSpecText"/> </li>													
															</c:when>
															<c:otherwise>	
															<li ><dsp:valueof param="planFeatureSpec.featureSpecText"/> 
																<span class="has-tip" data-tooltip title="${info}">i</span>
															</li>
															</c:otherwise>
														</c:choose>								
												</ul>
											</div>
											<!--START Div to adjust frontend Listing Features -->
											<c:if test="${count mod 3 == 0 && limit >1}">										
												<div style="width: 27%; float:left; margin: 1px;"></div>
											</c:if>
											<c:set var="limit" value="${limit-1}"/>
											<!--END Div to adjust frontend Listing Features -->										
										</dsp:oparam>
								</dsp:droplet>
							</div>
							
						 </dsp:oparam>
						 </dsp:droplet>
						</dsp:oparam>
					</dsp:droplet>
				</div>
			</div>
		</div>
	
	</section>
	</dsp:page>