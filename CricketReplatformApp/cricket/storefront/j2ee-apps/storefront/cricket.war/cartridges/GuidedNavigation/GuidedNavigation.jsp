
<dsp:page>
	<dsp:getvalueof var="timeMonitoring" bean="/com/cricket/configuration/CricketConfiguration.timeMonitoring"/> 
	<c:if test="${timeMonitoring == true}">
		<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
			<dsp:param name="isEndTime" value="false"/>
			<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>
			<dsp:param name="contentItemType" value="GuidedNavigation"/>	
			<dsp:oparam name="output">
				<dsp:getvalueof var="startTime" param="startTime"/>
			</dsp:oparam>
		</dsp:droplet>
	</c:if>
    <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
    <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
    <dsp:getvalueof var="activeLink" param="activeLink"/>
	<c:choose>
		<c:when test="${CATEGORY_TYPE eq 'ACCESSORY' || activeLink eq 'Accessories'}">
			<dsp:getvalueof var="refinementOuterClass" value="columns small-12"/>
			<dsp:getvalueof var="submitButtonClass" value="columns large-centered large-12 small-12 text-center"/>
		</c:when>
		<c:otherwise>
			<dsp:getvalueof var="refinementOuterClass" value="columns large-4 small-12"/>
			<dsp:getvalueof var="submitButtonClass" value="columns large-centered large-10 small-12 text-center"/>
		</c:otherwise>
	</c:choose>
	<li class="has-dropdown"><a href="#">Filter</a>
      <ul class="dropdown">
        <div class="row">
          <div class="columns large-12 small-12">
            <form id="phone-filters-dropdown" class="custom">
	           	<div class="row">
	                <div class="${submitButtonClass}">
					<c:choose>
						<c:when test="${isFromSearch eq true}">
							<input type="button" onclick="javascript:hitEndecaWithCalculatedQuerySearch();" value="Update Results" class="button white-button" />
						</c:when>
						<c:otherwise>
							<input type="button" onclick="javascript:hitEndecaWithCalculatedQuery();" value="Update Results" class="button white-button" />
						</c:otherwise>
	                  </c:choose>
	                </div>
				</div>
				<c:choose>
					<c:when test="${CATEGORY_TYPE eq 'ACCESSORY' || activeLink eq 'Accessories'}">
						<c:if test="${not empty contentItem.navigation}">
					    	<c:forEach var="element" items="${contentItem.navigation}" varStatus="countOut">
					    		<dsp:getvalueof var="countDims" value="${countOut.count}" scope="request"/>
					    		<c:if test="${(element.name eq 'Type')}">
									<div class="row">
										<div class="${refinementOuterClass}">
											<dsp:renderContentItem contentItem="${element}"/>
										</div>
									</div>
								</c:if>
							</c:forEach>
					    </c:if>
              		</c:when>
              		<c:otherwise>
              			<div class="row">
			              	<c:if test="${not empty contentItem.navigation}">
						    	<c:forEach var="element" items="${contentItem.navigation}" varStatus="countOut">
						    		<dsp:getvalueof var="countDims" value="${countOut.count}" scope="request"/>
									<c:if test="${(countOut.count mod 2 eq 1)}">
										<div class="${refinementOuterClass}">
									</c:if>
											<dsp:renderContentItem contentItem="${element}"/>
									<c:if test="${countOut.count mod 2 eq 0 || countOut.count eq fn:length(contentItem.navigation)}">
										</div>
									</c:if>
								</c:forEach>
						    </c:if>
						</div>
              		</c:otherwise>
              </c:choose>
            </form>
          </div>
        </div>
      </ul>
    </li>
    <li class="divider"></li>
    <c:if test="${timeMonitoring == true}">
		<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
			<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>	
			<dsp:param name="contentItemType" value="GuidedNavigation"/>
			<dsp:param name="startTime" value="${startTime}"/>
			<dsp:param name="isEndTime" value="true"/>
			<dsp:oparam name="output">
			</dsp:oparam>
		</dsp:droplet>
	</c:if>
</dsp:page>

