<dsp:page>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/search/droplet/GetProductTypesFromSearchResultsDroplet"/>
	<dsp:importbean bean="/com/cricket/search/SegregateSearchResultsBasedOnCategory"/>
	<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
	<dsp:importbean bean="/com/cricket/search/GetSupportResultsFromKeyWord"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<dsp:getvalueof var="isFromSearch" value="true" vartype="java.lang.Boolean" scope="request"/>
	<dsp:getvalueof var="searchPage" param="searchPage"/>
	<dsp:getvalueof var="renderCatFilter" value="true" scope="request"/>
	<dsp:getvalueof var="activeLink" param="activeLink"/>
	<dsp:getvalueof var="contextpath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	
	<input type="hidden" value="searchListingPage" id="searchPageIdentifier"/>
	<dsp:droplet name="GetSupportResultsFromKeyWord">
		<dsp:param name="keyWordQuery" param="Ntt"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="supportResultVOs" param="supportResultVOs" scope="request" vartype="java.util.List"/>
		</dsp:oparam>
	</dsp:droplet>
	<%-- ${contentItem.MainContent[0].contents[0].records} --%>
	<!-- There are chances when only one type of product is returned in that case product.type dimension is not returned by endeca -->
	<dsp:droplet name="GetProductTypesFromSearchResultsDroplet">
		<dsp:param name="records" value="${contentItem.MainContent[0].contents[0].records}"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="productTypeSingleDimension" param="productType" scope="request"/>
		</dsp:oparam>
	</dsp:droplet>
	<div id="constructor" class="section-search-results">
		<dsp:include page="/search/listing/includes/search_header.jsp">
			<dsp:param name="contentItem" value="${contentItem}"/>
		</dsp:include>
		<div id="ajaxReloadbleEndecaContent">
			<!-- identifierForEndecaEntireContentReload -->
			<section id="search-categories">
				<div class="row">
					<div class="large-12 columns small-12">
						<div id="section-filters">
							<dsp:include page="/search/listing/includes/search_allSearchCategories.jsp">
								<dsp:param name="contentItem" value="${contentItem}"/>
							</dsp:include>
							<c:if test="${searchPage eq 'searchPage' && activeLink ne 'Plans' && activeLink ne 'AddOns' && activeLink ne 'support'}">
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
							</c:if>
							<c:forEach var="element" items="${contentItem.MainContent}">
								<dsp:renderContentItem contentItem="${element}"/>
							</c:forEach>
						</div>
					</div>
				</div>
			</section>
			<!-- identifierForEndecaEntireContentReload -->
		</div>
		<dsp:include page="/search/listing/includes/search_supportLinks.jsp"/>
		<c:if test="${totalNumRecs eq 0 || totalNumRecs eq '0'}">
			<dsp:include page="/search/listing/includes/search_phonesYouWant.jsp"/>
			<dsp:include page="/search/listing/includes/search_ourPlans.jsp"/>
		</c:if>
		<dsp:include page="/search/listing/includes/search_whyCricketBottomBanner.jsp"/>
		
		
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
	
<input type="hidden" id="searchTerm" value="<dsp:valueof param='Ntt'/>" />
<input type="hidden" id="totalRecs" value="${totalNumRecs}"/>
<script type="text/javascript">
var pageID = 'SiteSearch Search';
var categoryID = 'Site Search';
var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
var network = '<dsp:valueof value='${network}'/>';
var userIntention = '<dsp:valueof value='${userIntention}'/>';
var customerType = '<dsp:valueof value='${customerType}'/>';
var searchTerm = $("#searchTerm").val();
var searchResults = $("#totalRecs").val();
var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
cmCreatePageviewTag(pageID,categoryID, searchTerm, searchResults,pvAttrs);
</script>
</dsp:page>