<dsp:page>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<section id="tab-content">
  <div class="row">
    <div class="columns large-12 small-12">
      <div class="section-container tabs" data-section="tabs">
      	<dsp:droplet name="DimensionValueCacheDroplet">
				<dsp:param name="repositoryId" bean="CricketConfiguration.accessoriesCategoryId"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="accessoriesCategoryCacheEntry" param="dimensionValueCacheEntry" />
		      </dsp:oparam>
			</dsp:droplet>
		<a href="${contextpath}${accessoriesCategoryCacheEntry.url}" class="circle-arrow right hide-for-small"><crs:outMessage key="cricket_accessoridetails_SeeAllAccessories"/><!-- See All Accessories --></a>
        <section id="overview">
        <c:set var="noImgOverviewCount" value="0"></c:set>
          <p class="title" data-section-title><a><crs:outMessage key="cricket_accessoridetails_Overview"/><!-- Overview --></a></p>
          <div class="content" data-section-content>           
             <dsp:droplet name="/atg/dynamo/droplet/ForEach">
		    	<dsp:param name="array" param="accessory.overviews"/>
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
		    	 	<%-- <div class="columns large-6 small-12 copy">
		    			<dsp:valueof valueishtml="true" param="accessory.overviewContent.data"></dsp:valueof>
		    		</div> --%>
            <div id="included" class="row">
            <!-- This particular section included in accessories details page to show the compatible phone with this accessories. It will display the phone name. -->
			 <div class="columns large-12 small-12">
				<h4><crs:outMessage key="cricket_accessoridetails_CompatiblePhones"/><!-- Compatible Phones: --></h4>
				<ul>
					 <dsp:droplet name="/atg/dynamo/droplet/ForEach">
						<dsp:param name="array" param="accessory.compatiblePhones"/>
						<dsp:param name="elementName" value="compatiblePhonesInfo"/>													
							<dsp:oparam name="output">
								<dsp:getvalueof var="size" param="size"></dsp:getvalueof>
								<dsp:getvalueof var="count" param="count"></dsp:getvalueof>									
										<c:choose>
											<c:when test="${size eq 1}">
												<li class="last"><dsp:valueof param="compatiblePhonesInfo.displayName"/></li>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${count eq 1}">
													   <li class="first"><dsp:valueof param="compatiblePhonesInfo.displayName"/></li>
													</c:when>
													<c:when test="${count eq size}">
													   <li class="last"><dsp:valueof param="compatiblePhonesInfo.displayName"/></li>
													</c:when>
													<c:otherwise>
													   <li><dsp:valueof param="compatiblePhonesInfo.displayName"/></li>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>			
						   </dsp:oparam>						  
					</dsp:droplet>
					</ul>
				</div>					
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</section>
</dsp:page>