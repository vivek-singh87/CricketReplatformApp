<dsp:page>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/atg/cricket/util/CricketProfile"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:getvalueof var="usrAccountNumber" bean="CricketProfile.accountNumber"/>

	<!-- Calling InquireAccount -->
	<c:if test="${not empty usrAccountNumber}">
		<dsp:include page="/browse/plan/addon/listing/callGetUserAddonsDroplet.jsp"/>
	</c:if>

	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<dsp:getvalueof var="productCategoryId" value="${contentItem.MainContent[0].categoryId}" scope="request"/>
	
	<div id="constructor" class="section-plans-addons-listing">
	  
		<!-- Static Banner -->
		<dsp:include page="/browse/plan/addon/listing/plan_AddonStaticBanner.jsp"/>
	
		<div id="plan-callout">
			<dsp:getvalueof var="CATEGORY_TYPE" value="PLAN_ADDONS" scope="request"/>
			<div id="ajaxReloadbleEndecaContent">
				
				<!-- never ever remove the below comment it is required for rendering the page using javascript -->
				<!-- identifierForEndecaEntireContentReload -->
				 <dsp:getvalueof var="userIntention" bean="UpgradeItemDetailsSessionBean.userIntention"/>
				 <dsp:getvalueof var="addLineIntention" bean="CartConfiguration.addLineIntention"/>
				<c:choose>
					<c:when test="${not empty usrAccountNumber}">
					<!-- Existing Customer -->
					<c:if test="${userIntention ne addLineIntention}">
			    		<dsp:include page="/browse/plan/addon/listing/plan_AddonExistingCustomer.jsp"/>
			    	</c:if>
		    	</c:when>
				<c:otherwise>
				<!-- Messages -->
		    	<dsp:include page="/browse/plan/addon/listing/plan_AddonListing_messaging.jsp"/>
				</c:otherwise>
				</c:choose>
				 
		    	
				
				<%-- Render the main content --%>
				<div id="mainEndecaContent">
					<!-- never ever remove the below comment it is required for rendering the page using javascript -->
					<!-- identifierForEndecaMainContentReload -->
					<c:forEach var="element" items="${contentItem.MainContent}">
						<dsp:renderContentItem contentItem="${element}"/>
					</c:forEach>
					<!-- identifierForEndecaMainContentReload -->
				</div>
				<!-- identifierForEndecaEntireContentReload -->
		
				<!-- Static Banner -->
				<dsp:include page="/browse/plan/addon/listing/plan_AddonStaticFooter.jsp"/>
			</div>
		</div>
	</div><!--/#constructor-->
	
		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="userIntention"  bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean.userIntention"/>
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="userIntention"  value="New Activation"/>
		</c:if>
		<c:if test="${empty userIntention}">
			<dsp:getvalueof var="userIntention"  value="null"/>
		</c:if>
		
		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="network" bean="/com/cricket/util/LocationInfo.networkProviderName"/>
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="network" bean='/atg/userprofiling/Profile.networkProvider' />
		</c:if>
		<c:if test="${empty network}">
			<dsp:getvalueof var="network"  value="null"/>
		</c:if>
		
		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="customerType"  bean="/atg/cricket/util/CricketProfile.customerType"/>
			<c:set var="customerType" value="EXISTING" />
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="customerType"  value="NEW"/>
		</c:if>
		<c:if test="${empty customerType}">
			<dsp:getvalueof var="customerType"  value="null"/>
		</c:if>
<script type="text/javascript">
	var pageID = "PLAN AddOn";
	var categoryID = "SHOP:FEATURES AND DOWNLOADS";
	//must be dynamic values
	var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
	var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
	var network = '<dsp:valueof value='${network}'/>';
	var userIntention = '<dsp:valueof value='${userIntention}'/>';
	var customerType = '<dsp:valueof value='${customerType}'/>';
	var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
	cmCreatePageviewTag(pageID,categoryID,null,null,pvAttrs);
</script>
</dsp:page>