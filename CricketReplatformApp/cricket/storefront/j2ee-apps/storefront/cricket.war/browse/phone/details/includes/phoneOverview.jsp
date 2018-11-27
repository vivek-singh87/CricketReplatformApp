<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
	<section>
          <p class="title" data-section-title><a href="#overview"><crs:outMessage key="cricket_phonedetails_Overview"/></a></p>
		  <dsp:getvalueof var="productOverviews" param="Product.overviews" vartype="java.util.List"/>	
		  <c:set var="noImgOverviewCount" value="0"></c:set>
			<div class="content" data-slug="overview" data-section-content>		  
		    <c:if test="${fn:length(productOverviews) gt 0}">			  
			   <dsp:droplet name="/atg/dynamo/droplet/ForEach">
		    	<dsp:param name="array" param="Product.overviews"/>
		    	<dsp:param name="elementName" value="overviewsInfo"/>
					    <dsp:oparam name="output">	
						<dsp:getvalueof var="size" param="size"/>
						<dsp:getvalueof var="count" param="count"/>
		     				<dsp:getvalueof var="overviewTemplate" param="overviewsInfo.overviewTemplate"></dsp:getvalueof>
		     				<dsp:getvalueof var="overviewImage" param="overviewsInfo.overviewImage"></dsp:getvalueof>
		     				<c:choose>
		     					<c:when test="${size > 1}">
									<dsp:droplet name="IsNull">
										<dsp:param name="value" value="${overviewTemplate}"/>
											<dsp:oparam name="false">
												<dsp:droplet name="IsNull">
													<dsp:param name="value" value="${overviewImage}"/>
													<dsp:oparam name="false">
														<c:if test="${noImgOverviewCount == 1}">
															</div>
															<c:set var="noImgOverviewCount" value="0"></c:set>
														</c:if>
														<div class="row">
															<dsp:include page="${overviewTemplate.url}">
																<dsp:param name="overviewsInfo" param="overviewsInfo"/>
															</dsp:include>
														</div>
													</dsp:oparam>
													<dsp:oparam name="true">
														<c:choose>
														<c:when test="${noImgOverviewCount == 0}">
															<div class="row">
															<dsp:include page="${overviewTemplate.url}">
																<dsp:param name="overviewsInfo" param="overviewsInfo"/>
															</dsp:include>
															<c:set var="noImgOverviewCount" value="${noImgOverviewCount + 1}"></c:set>
															<c:if test="${count == size}">
																</div>
																<c:set var="noImgOverviewCount" value="0"></c:set>
															</c:if>
														</c:when>
														<c:when test="${noImgOverviewCount == 1}">
															<dsp:include page="${overviewTemplate.url}">
																<dsp:param name="overviewsInfo" param="overviewsInfo"/>
															</dsp:include>
															</div>
															<c:set var="noImgOverviewCount" value="0"></c:set>
														</c:when>												
														</c:choose>
													</dsp:oparam>
												</dsp:droplet>
											</dsp:oparam>
									</dsp:droplet>
								</c:when>
								<c:otherwise>
									<div class="row">
										<dsp:include page="${overviewTemplate.url}">
											<dsp:param name="overviewsInfo" param="overviewsInfo"/>
										</dsp:include>
									</div>
								</c:otherwise>
							</c:choose>
			           </dsp:oparam>
             	</dsp:droplet>	 	  
			</c:if>          
            <div id="featured" class="row">                
              <div class="columns large-12 small-12">
              	<dsp:droplet name="/atg/dynamo/droplet/ForEach">
				    	<dsp:param name="array" param="Product.keyFeatures"/>
				    	<dsp:param name="elementName" value="keyFeaturesInfo"/>
				    	<dsp:oparam name="outputStart">           
					                <h4><crs:outMessage key="cricket_phonedetails_KeyFeatures"/></h4>
					     		</dsp:oparam>
					       		<dsp:oparam name="output">
		          		 			<ul>
					                  <li><dsp:valueof param="keyFeaturesInfo"></dsp:valueof></li>
					                </ul>
					      </dsp:oparam>
          		</dsp:droplet>	
              </div>
            </div>
            <div id="included" class="row">
             <div class="columns large-12 small-12">	           
	          	 <h4><crs:outMessage key="cricket_phonedetails_IncludedAccessories"/></h4>
					<dsp:droplet name="/atg/dynamo/droplet/ForEach">
				    	<dsp:param name="array" param="Product.includedAccessories"/>
				    	<dsp:param name="elementName" value="includedAccessoriesInfo"/>						
					    <dsp:oparam name="output">
						<dsp:getvalueof var="size" param="size"></dsp:getvalueof>
					    <dsp:getvalueof var="count" param="count"></dsp:getvalueof>
							 <ul>
								<c:choose>
								    <c:when test="${count eq 1}">
								       <li class="first"><dsp:valueof param="includedAccessoriesInfo"></dsp:valueof></li>
								    </c:when>
									<c:when test="${count eq size}">
								       <li class="last"><dsp:valueof param="includedAccessoriesInfo"></dsp:valueof></li>
								    </c:when>
									<c:otherwise>
								       <li><dsp:valueof param="includedAccessoriesInfo"></dsp:valueof></li>
								    </c:otherwise>
								</c:choose>
							</ul>
				        </dsp:oparam>
	          	</dsp:droplet>	
              </div>
            </div>
		  </div>
        </section>
  </dsp:page>      