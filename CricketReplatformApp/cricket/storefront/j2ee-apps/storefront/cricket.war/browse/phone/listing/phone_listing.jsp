<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/comparison/ProductListHandler"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
<dsp:importbean bean="/com/cricket/browse/formhandler/AddCompareProdToSession"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageURLLookupDroplet"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="bannerProfileProperty" bean="CricketConfiguration.bannerProfileProperty"/>
<dsp:getvalueof var="profileProperty" bean="Profile.${bannerProfileProperty}"/>
<dsp:getvalueof var="contextpath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
<dsp:importbean bean="/com/cricket/util/CartCookieInfo"/>

	<dsp:form action="${contextpath}/browse/phone/listing/compareAjaxHandle.jsp" name="compareAddtoSession" id="compareAddtoSession" style="display:none" metod="get">
		<dsp:input value="" id="compareProdId" bean="AddCompareProdToSession.productId"/>
		<dsp:input value="" id="compareLiquidPixelUrl" bean="AddCompareProdToSession.liquidPixelUrl"/>
		<dsp:input value="" id="compareAction" bean="AddCompareProdToSession.action"/>
		<dsp:input type="submit" bean="AddCompareProdToSession.addToSession" value="submit" id="addToSessionCompareButton"/>
	</dsp:form>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<dsp:getvalueof var="productCategoryId" value="${contentItem.MainContent[0].categoryId}" scope="request"/>
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
	<div id="constructor" class="section-phones-listing">
		<dsp:droplet name="/atg/dynamo/droplet/Cache">
			<dsp:param value="phoneListingTop_${profileProperty}_${isUserLoggedIn}" name="key"/>
			<dsp:oparam name="output">
				<dsp:include page="/browse/banners/headerPromotionalBanner.jsp">
					<dsp:param name="PAGE_NAME" value="PHONE_LISTING"/>
				</dsp:include>
			</dsp:oparam>
		</dsp:droplet>
		<dsp:getvalueof var="CATEGORY_TYPE" value="PHONES" scope="request"/>
		<div id="ajaxReloadbleEndecaContent">
				
		<!-- never ever remove the below comment it is required for rendering the page using javascript -->
		<!-- identifierForEndecaEntireContentReload -->
		<%-- Render the left content --%>
		<dsp:getvalueof var="sessionComapreProdIds" bean="UserSessionBean.compareProductIDs" scope="request"/>
		<dsp:getvalueof var="compareBucketCount" bean="UserSessionBean.numberOfItemsInCompare" vartype="java.lang.Integer" scope="request"/>
		<input type="hidden" value="${compareBucketCount}" id="compareBucketCount"/>
		<input type="hidden" value="${sessionComapreProdIds}" id="compareBucketProdIds"/>
		<c:choose>
			<c:when test="${compareBucketCount gt 1}">
				<input type="hidden" value="compareShown" id="showCompareIdenitifier"/>
				<dsp:getvalueof var="toggleCompareDisplay" value="display:inline"/>
			</c:when>
			<c:otherwise>
				<input type="hidden" value="compareHidden" id="showCompareIdenitifier"/>
				<dsp:getvalueof var="toggleCompareDisplay" value="display:none"/>
			</c:otherwise>
		</c:choose>
		<input type="hidden" value="${sessionComapreProdIds}" id="sessionCompareProdIds"/>
		<input type="hidden" value="${contextpath}" id="contextPathEL"/>
		<dsp:getvalueof var="backLink" param="N"/>
			<div id="section-filters">
	  			<dsp:include page="/browse/phone/listing/includes/phone_allPhoneCategories.jsp"/>
				<!-- Compare Phones Bar -->
				<div class="row hide-for-small">
					<div id="compare-bar-custom" data-magellan-expedition="fixed" class="large-12 small-12 columns shown" style="${toggleCompareDisplay}">
						<p>Compare:</p>
						<div class="right">
							<a href="javascript:void();" onclick="javascript:clearCompareSession();" class="clear">clear</a>
							<a href="javascript:handleSubmitCompareForm();" class="button small orange-button">Compare</a>	
							<dsp:form id="compareForm" formid="compareForm" action="${contextpath}/browse/phone/compare/phone_compare.jsp" style="display:none">
								<dsp:input bean="ProductListHandler.productIDs" name="productIds" type="hidden" id="myproductlistid"/>
								<dsp:input bean="ProductListHandler.addProductList" name="addProductList" type="submit" value="submit" id="submitCompareFormButton"/>
								<dsp:input bean="ProductListHandler.addProductSuccessURL" name="successful" type="hidden" value="${contextpath}/browse/phone/compare/phone_compare.jsp?backLink=${backLink}"/>
							</dsp:form>
							<!--<input bean="ProductListHandler.addProductList" type="submit" class="button small orange-button" value="Compare"/>
							<input bean="ProductListHandler.successUrl" type="hidden"  value="${contextpath}/browse/phone/compare/phone-compare.jsp"/>-->
						</div>
						<dsp:droplet name="ForEach">
							<dsp:param name="array" bean="UserSessionBean.comapreImageUrls"/>
							<dsp:param name="elementName" value="compareImageUrl"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="compareImageUrl" param="compareImageUrl"/>
								<c:set var="splitCompareImageUrl" value="${fn:split(compareImageUrl, '|')}"/>
								<c:if test="${not empty compareImageUrl}">
									<img src="${splitCompareImageUrl[0]}" id="compImg${splitCompareImageUrl[1]}" style="width: 16px; height: 33px; margin: 0 0.5em">
								</c:if>
							</dsp:oparam>
						</dsp:droplet>
						<!-- <span id="smallCompareImages"></span> -->
					</div>
				</div>
					<dsp:include page="/browse/phone/common/includes/phone_withoutzipcode.jsp"/>
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
							  			</dsp:include>
							  		</ul>
								</section>
							</nav>
						</div>
					</div>
					<!-- Filter Selected Bar - items dynamically added through JS -->
					<dsp:include page="/browse/phone/listing/includes/phone_selectedFilters.jsp"/>
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
				<dsp:param name="PAGE_TYPE" value="PHONE"/>
			</dsp:include>
			
			<dsp:droplet name="/atg/dynamo/droplet/Cache">
				<dsp:param value="phoneListingBottom_${profileProperty}_${isUserLoggedIn}" name="key"/>
				<dsp:oparam name="output">
					<dsp:include page="/browse/banners/footerPromotionalBanner.jsp">
						<dsp:param name="PAGE_NAME" value="PHONE_LISTING"/>
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
	var url = document.URL;
	if(url.indexOf('cell-phones/smartphones') != -1){
		var pageID = "PHONES:SMARTPHONES";
	}
	else {
		if(url.indexOf('cell-phones/basic') != -1){
			var pageID = "PHONES:FEATUREPHONES";
		}
		else{
			var pageID = "PHONES";
		}
	}	
	var categoryID = "SHOP:PHONES";
	var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
	var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
	var network = '<dsp:valueof value='${network}'/>';
	var userIntention = '<dsp:valueof value='${userIntention}'/>';
	var customerType = '<dsp:valueof value='${customerType}'/>';
	var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
	cmCreatePageviewTag(pageID,categoryID,null,null,pvAttrs);
</script>
</dsp:page>