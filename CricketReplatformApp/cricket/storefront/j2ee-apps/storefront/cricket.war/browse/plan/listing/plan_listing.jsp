<dsp:page>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<dsp:getvalueof var="manuallyEnteredZipCode" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/util/CartCookieInfo"/>
<dsp:getvalueof var="bannerProfileProperty" bean="CricketConfiguration.bannerProfileProperty"/>
<dsp:getvalueof var="profileProperty" bean="Profile.${bannerProfileProperty}"/>
<dsp:getvalueof var="contextpath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>

	<dsp:getvalueof var="addAddonWithoutPackageError" bean="CartCookieInfo.addAddonWithoutPackageError" />
	<c:if test="${addAddonWithoutPackageError}">
		<div class="row">
			<div class="large-12 small-12 columns">
				<div data-alert class="alert-box">
					<span class="alert-icon"><strong> <crs:outMessage key="cricket_shoppingcart_notification"/><!-- Notification: --></strong><crs:outMessage key="cricket_shoppingcart_addPackage_beforeFeature"/><!--Please add a phone and plan to package before adding feature.--> </span>
				</div>
			</div>
		</div>
		<dsp:setvalue bean="CartCookieInfo.addAddonWithoutPackageError" value="false"/>
	</c:if>
<div id="constructor" class="section-plans-listing">
	<dsp:getvalueof var="CATEGORY_TYPE" value="PLANS" scope="request"/>
	<dsp:getvalueof var="productCategoryId" value="${contentItem.MainContent[0].categoryId}" scope="request"/>
	<dsp:include page="/browse/banners/planListingTopBanner.jsp"/>
	
	<c:choose>
		<c:when test="${manuallyEnteredZipCode eq true}">			
		</c:when>
		<c:otherwise>
			<div class="row">
	          <div class="large-12 small-12 columns" style="margin-top: 20px;">
	               <div data-alert class="alert-box">
	                   <%-- <crs:outMessage key="cricket_planlisting_Pleaseenteryourzipcode"/> --%>  Please enter your zip code to add to your cart.  <a class="circle-arrow" href="#"><crs:outMessage key="cricket_planlisting_EnterZipcode"/></a>
	               </div>
	          </div>
     		</div>
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
	
	<dsp:include page="/browse/plan/listing/includes/plan_recommendedPhones.jsp">
	</dsp:include>
	
	<dsp:include page="/common/includes/legalContent.jsp">
				<dsp:param name="PAGE_TYPE" value="PLAN"/>
			</dsp:include>
	<dsp:droplet name="/atg/dynamo/droplet/Cache">
		<dsp:param value="planListingBottom_${profileProperty}_${isUserLoggedIn}" name="key"/>
		<dsp:oparam name="output">
			<dsp:include page="/browse/banners/planListingBottomBanner.jsp">
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

<script type="text/javascript">
	var pageID = "PLANS";
	var categoryID = "SHOP:PLANS";
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