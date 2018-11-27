<dsp:page>
<dsp:getvalueof var="contextpath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>

<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="bannerProfileProperty" bean="CricketConfiguration.bannerProfileProperty"/>
<dsp:getvalueof var="profileProperty" bean="Profile.${bannerProfileProperty}"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<dsp:getvalueof var="productCategoryId" value="${contentItem.MainContent[0].categoryId}" scope="request"/>
	<dsp:getvalueof var="userEnteredZipCode" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
	<div id="constructor" class="section-accessories-listing">
		<dsp:droplet name="/atg/dynamo/droplet/Cache">
			<dsp:param value="accessoryListingTop_${profileProperty}_${isUserLoggedIn}" name="key"/>
			<dsp:oparam name="output">
				<dsp:include page="/browse/banners/headerPromotionalBanner.jsp">
					<dsp:param name="PAGE_NAME" value="ACCESSORY_LISTING"/>
				</dsp:include>
			</dsp:oparam>
		</dsp:droplet>
		<dsp:getvalueof var="CATEGORY_TYPE" value="ACCESSORY" scope="request"/>
		<input type="hidden" value="accessoryListing" id="accessoryListing"/>
		<div id="ajaxReloadbleEndecaContent">
				
			<!-- never ever remove the below comment it is required for rendering the page using javascript -->
			<!-- identifierForEndecaEntireContentReload -->
			<%-- Render the left content --%>
			<div id="section-filters">
	  			<dsp:include page="/browse/accessories/listing/includes/accessory_allAccessoryCategories.jsp"/>
	  			<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
					<c:if test="${isUserLoggedIn eq false}">
						<dsp:include page="/browse/accessories/common/includes/accessory_withoutzipcode.jsp"/>
					</c:if>
				<div class="row">
		    		<div class="large-12 small-12 columns filters">
						<!-- Phone Filters using Topbar -->
						<!-- Phone Filters using Topbar -->
						<nav class="top-bar">
							<section class="top-bar-section">
			  		  		<!-- Left Nav Section -->
								<ul class="left">
									<dsp:include page="/browse/phone/listing/includes/phone_filterDropdown.jsp">
						  				<dsp:param name="contentItem" value="${contentItem}"/>
						  			</dsp:include>
						  			<dsp:include page="/browse/phone/listing/includes/phone_sortBy_feature_price_brand.jsp">
						  				<dsp:param name="contentItem" value="${contentItem.MainContent[0]}"/>
						  				<dsp:param name="PAGE_TYPE_FILTER" value="ACCESSORY"/>
							  		</dsp:include>
						  		</ul>
							</section>
						</nav>
					</div>
				</div>
				<!-- Filter Selected Bar - items dynamically added through JS -->
				<dsp:include page="/browse/phone/listing/includes/phone_selectedFilters.jsp"/>
				<c:if test="${userEnteredZipCode eq false}">
					<div class="row show-for-small">
						<div class="large-12 small-12 columns no-padding">
							<div class="alert-box" data-alert="">
								Please enter your <a href="#">zip code</a> to continue shopping.
							</div>
						</div>
					</div>
				</c:if>
			</div>
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
		</div>
		<dsp:include page="/common/includes/legalContent.jsp">
			<dsp:param name="PAGE_TYPE" value="ACCESSORY"/>
		</dsp:include>
		<dsp:droplet name="/atg/dynamo/droplet/Cache">
			<dsp:param value="accessoryListingBottom_${profileProperty}_${isUserLoggedIn}" name="key"/>
			<dsp:oparam name="output">
				<dsp:include page="/browse/banners/footerPromotionalBanner.jsp">
					<dsp:param name="PAGE_NAME" value="ACCESSORY_LISTING"/>
				</dsp:include>
			</dsp:oparam>
		</dsp:droplet>
		
	</div>
	
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
		
<dsp:include page="/common/includes/openCartDrawer.jsp"/>
<script type="text/javascript">
	var pageID = "ACCESSORIES";
	var categoryID = "SHOP:ACCESSORIES";
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