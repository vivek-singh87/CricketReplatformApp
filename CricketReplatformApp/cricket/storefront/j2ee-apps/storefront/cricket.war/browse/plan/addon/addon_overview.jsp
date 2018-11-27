<dsp:page>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<section id="tab-content">
  <div class="row">
    <div class="columns large-12 small-12">
      <div class="section-container tabs" data-section="tabs">
	
		<dsp:getvalueof var="otherAddonsCategoryId" bean="CricketConfiguration.otherAddonsCategoryId"/>
		<dsp:droplet name="DimensionValueCacheDroplet">
			<dsp:param name="repositoryId" value="${otherAddonsCategoryId}"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="addonsCategoryCacheEntry" param="dimensionValueCacheEntry" />
	     	</dsp:oparam>
		</dsp:droplet>
		<a href="${contextpath}<c:out value='${addonsCategoryCacheEntry.url}'/>" class="circle-arrow">See All Plan Add-ons</a>
      
        <section id="overview">
          <p class="title" data-section-title><a href="#panel1">Overview</a></p>
          <div class="content" data-section-content>
            <div class="row">
              <div class="columns large-12 small-12 copy">
              	<dsp:getvalueof var="longDescription" param="product.longDescription"></dsp:getvalueof>
              	<dsp:droplet name="IsNull">
					<dsp:param name="value" value="${longDescription}"/>
					<dsp:oparam name="false">
		                <dsp:valueof value="${longDescription}" valueishtml="true"></dsp:valueof>
					</dsp:oparam>
					<dsp:oparam name="true">
						<dsp:getvalueof var="productOverviews" param="product.overviews" vartype="java.util.List"/>	
						<c:if test="${fn:length(productOverviews) gt 0}">			  
						   <dsp:droplet name="/atg/dynamo/droplet/ForEach">
					    	<dsp:param name="array" param="product.overviews"/>
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
					</dsp:oparam>
				</dsp:droplet>
                <p class="tabs-legal"><dsp:valueof param="product.availabilityMessage"/></p>
              </div>
            </div>
            <!--This is the included page in plan addon details.jsp which displays phones those are compatible to the selected addon. -->
            <dsp:include page="/browse/plan/addon/availablePhones.jsp"></dsp:include>
            <!--This is the included page in plan addon details.jsp which displays plans those are compatible plan to the selected addon. -->
            <dsp:include page="/browse/plan/addon/availablePlans.jsp"></dsp:include>

          </div>
        </section>
      </div>
    </div>
  </div>

</section>
</dsp:page>