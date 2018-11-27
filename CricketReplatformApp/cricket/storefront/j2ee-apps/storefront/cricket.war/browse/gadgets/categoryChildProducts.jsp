<dsp:page>
	<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
	<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>	
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:importbean bean="/com/cricket/configuration/TimeMonitoring"/>
	<dsp:importbean bean="/com/cricket/browse/ProductListingDroplet"/>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:importbean bean="/atg/dynamo/droplet/IsEmpty"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/browse/FilterProductsBasedOnLocation"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean"/>
	<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
	<dsp:getvalueof var="manuallyEnteredZipCode" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
	<dsp:getvalueof var="marketCode" bean="Profile.marketId"/>
	<dsp:getvalueof var="packageId" param="packageId"/>
	<dsp:getvalueof var="intention" param="intention"/>
   	<dsp:getvalueof var="intentionbean" bean="UpgradeItemDetailsSessionBean.userIntention"/> 
	<dsp:importbean bean="/atg/cricket/util/CricketProfile"/>
   	
	<c:set var="arraySplitSize" value="${contentItem.recsPerPage}"/>
	<c:set var="viewAll" value="true"/>
	<dsp:getvalueof var="p" param="p"/>
	<c:choose>
		<c:when test="${p gt 0}">
			<dsp:getvalueof var="p" param="p"/>
		</c:when>
		<c:otherwise>
			<dsp:getvalueof var="p" value="1"/>
		</c:otherwise>
	</c:choose>
	<dsp:droplet name="IsEmpty">
		<dsp:param name="value" value="${CATEGORY_TYPE}"/>
		<dsp:oparam name="true">
			<dsp:getvalueof var="CATEGORY_TYPE" param="CATEGORY_TYPE"/>
		</dsp:oparam>
	</dsp:droplet>
	<c:choose>
		<c:when test="${intention eq 'upgradePlan' || intention eq 'upgradeFeature' || intentionbean eq 'upgradePlan' || intentionbean eq 'upgradeFeature'}">
			<c:choose>
				<c:when test="${CATEGORY_TYPE eq 'PHONES' || CATEGORY_TYPE eq 'PLAN_ADDONS'}">
					<dsp:getvalueof var="planId" bean="CricketProfile.ratePlanCode"/>
					<dsp:getvalueof var="marketCode" bean="CricketProfile.marketCode"/>
					<c:set var="timeStamp" value="<%= new java.util.Date().getTime() %>"/>
				</c:when>
				<c:when test="${CATEGORY_TYPE eq 'PLANS'}">
					<dsp:getvalueof var="phoneId" param="phoneId"/>
					<c:set var="timeStamp" value="<%= new java.util.Date().getTime() %>"/>
				</c:when>
				<c:otherwise>
					<dsp:getvalueof var="phoneId" param="phoneProductId"/>
				</c:otherwise>
			</c:choose>
		</c:when>   	 	
   	  	<c:otherwise>
	   	  	<c:choose>
				<c:when test="${CATEGORY_TYPE eq 'PHONES' || CATEGORY_TYPE eq 'PLAN_ADDONS'}">
					<dsp:getvalueof var="planId" param="planId"/>
				</c:when>
				<c:when test="${CATEGORY_TYPE eq 'PLANS'}">
					<dsp:getvalueof var="phoneId" param="phoneId"/>
				</c:when>
				<c:otherwise>
					<dsp:getvalueof var="phoneId" param="phoneProductId"/>
				</c:otherwise>
			</c:choose>
   	  	</c:otherwise>
   	 </c:choose>	
	
	<dsp:getvalueof var="sortKey" param="sort"/>
	<dsp:getvalueof var="intention" param="intention"/>
	<dsp:getvalueof var="isDynamicAddOn" param="isDynamicAddOn"/>
	<dsp:getvalueof var="editPlan" param="editPlan"/>
	<dsp:getvalueof var="editPhone" param="editPhone"/>
	<dsp:getvalueof var="modelNumber" param="modelNumber"/>
	<dsp:droplet name="/atg/dynamo/droplet/Cache">
		<dsp:param value="productList_${contentItem.categoryId}_${planId}_${phoneId}_${intention}_${CATEGORY_TYPE}_${marketCode}_${isUserLoggedIn}_${sortKey}_${manuallyEnteredZipCode}_${packageId}_${isDynamicAddOn}_${sessionComapreProdIds}_${editPlan}_${editPhone}_${modelNumber}_${timeStamp}" name="key"/>
		<dsp:oparam name="output">
			<dsp:droplet name="ProductListingDroplet">
				<dsp:param name="categoryId" value="${contentItem.categoryId}"/>
				<dsp:param name="planProductId" value="${planId}"/>
				<dsp:param name="phoneProductId" value="${phoneId}"/>
				<dsp:param name="editPhone" value="${editPhone}"/>
				<dsp:param name="intention" param="intention"/>
				<dsp:param name="CATEGORY_TYPE" value="${CATEGORY_TYPE}"/>	
				<dsp:oparam name="output">
					<dsp:getvalueof var="productList" param="productList"/>			
					
					<c:choose>
						<c:when test="${viewAll eq 'true'}">
							<c:set var="howMany" value="5000" />
							<c:set var="start" value="${1}" />
						</c:when>
						<c:when test="${(fn:length(productList)) <= arraySplitSize}">
							<c:set var="howMany" value="${arraySplitSize}" />
							<c:set var="start" value="${1}" />
						</c:when>
						<c:otherwise>
							<c:set var="howMany" value="${arraySplitSize}" />
							<c:set var="start" value="${((p - 1) * howMany) + 1}" />
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${CATEGORY_TYPE eq 'PHONES'}">
							<dsp:include page="/browse/gadgets/phone_results_category_request.jsp">
								<dsp:param name="p" value="${p}"/>
								<dsp:param name="howMany" value="${howMany}"/>
								<dsp:param name="start" value="${start}"/>
								<dsp:param name="productList" value="${productList}"/>
								<dsp:param name="sortSelection" param="sort"/>
							</dsp:include>
						</c:when>
						<c:when test="${CATEGORY_TYPE eq 'ACCESSORY'}">
							<c:choose>
								<c:when test="${fn:length(productList) gt 0}">
									<dsp:include page="/browse/gadgets/accessory_results_category_request.jsp">
										<dsp:param name="p" value="${p}"/>
										<dsp:param name="howMany" value="${howMany}"/>
										<dsp:param name="start" value="${start}"/>
										<dsp:param name="productList" value="${productList}"/>
										<dsp:param name="sortSelection" param="sort"/>
									</dsp:include>
								</c:when>
								<c:otherwise>
									<section id="section-results" class="${zipcodeClass}">	
										<!-- No results -->
										<div class="row">	
											<div class="large-12 small-12 columns large-centered small-centered">
												<h2 style="margin-left:30px">There are no accessories for this selection</h2>
											</div>
										</div>
								</c:otherwise>
							</c:choose>
							
						</c:when>
						<c:when test="${CATEGORY_TYPE eq 'PLANS'}">
							<dsp:include page="/browse/gadgets/plan_results_category_request.jsp">
								<dsp:param name="p" value="${p}"/>
								<dsp:param name="howMany" value="${howMany}"/>
								<dsp:param name="start" value="${start}"/>
								<dsp:param name="productList" value="${productList}"/>
								<dsp:param name="sortSelection" param="sort"/>
							</dsp:include>
						</c:when>
						<c:when test="${CATEGORY_TYPE eq 'PLAN_ADDONS'}">
							<dsp:include page="/browse/gadgets/addon_results_category_request.jsp">
								<dsp:param name="p" value="${p}"/>
								<dsp:param name="howMany" value="${howMany}"/>
								<dsp:param name="start" value="${start}"/>
								<dsp:param name="productList" value="${productList}"/>
								<dsp:param name="sortSelection" param="sort"/>
							</dsp:include>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
	</dsp:droplet>
</dsp:page>