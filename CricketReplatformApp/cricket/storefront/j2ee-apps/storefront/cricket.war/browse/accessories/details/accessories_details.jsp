<dsp:page>


<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="bannerProfileProperty" bean="CricketConfiguration.bannerProfileProperty"/>
<dsp:getvalueof var="profileProperty" bean="Profile.${bannerProfileProperty}"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>

	<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
		 <dsp:param name="id" param="productId"/>
		 <dsp:param name="filterBySite" value="false"/>
       	 <dsp:param name="filterByCatalog" value="false"/>
		 <dsp:param name="elementName" value="accessory"/>
			<dsp:oparam name="output">
				<dsp:droplet name="GetSeoStringDroplet">
					<dsp:param name="product" param="accessory"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="seoString" param="seoString"/>
					</dsp:oparam>
				</dsp:droplet>
				<dsp:getvalueof var="productName" param="accessory.displayName"/>
				<dsp:getvalueof var="productSEODesc" param="accessory.SEODescription"/>
				<dsp:getvalueof var="productSEOKey" param="accessory.SEOKeywords"/>
			</dsp:oparam>
	</dsp:droplet>
	<dsp:include page="/common/head.jsp">
		<dsp:param name="seokey" value="${productName}" />
		<dsp:param name="pageType" value="accessoryDetails"/>
		<dsp:param name="seoString" value="${seoString}" />
	</dsp:include>
	
	<meta name="description" content="${productSEODesc}" />
	<meta name="keywords" content="${productSEOKey}"/>
	
<script type="text/javascript"> 
 //cmCreatePageviewTag('http://www.mycricket.com/', 'http://www.mycricket.com/', null, null, pvAttrs);
</script>
<body>
<div id="outer-wrap">
<div id="inner-wrap">
	<!--/#header-->
	<dsp:include page="/common/header.jsp"/> 
	
<!--// END HEADER AREA //-->

<!--// START MAIN CONTENT AREA //-->

<div id="constructor" class="section-accessory-details">
	<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
		 <dsp:param name="id" param="productId"/>
		 <dsp:param name="filterBySite" value="false"/>
       	 <dsp:param name="filterByCatalog" value="false"/>
		 <dsp:param name="elementName" value="accessory"/>
			<dsp:oparam name="output">
					<dsp:getvalueof var="productName" param="accessory.displayName"/>
					<dsp:getvalueof var="productId" param="accessory.repositoryId"/>						
						<!--This page displays accessories large image, thumbnail images, long description, available colors, retail price, final price, promotions and discounts, mail-in rebate discounts, Add to Cart button, no. of days to ship the accessories (bases on threshold values set in BCC) -->
						<div id="accessoryDisplayTopSection">						
							<dsp:include page="/browse/accessories/details/includes/accessories_details_zipcode.jsp">
								<dsp:param name="accessory" param="accessory"/>
							</dsp:include>
						</div>					
					
					<!-- This JSP is being included in accessories details page to show the overview of the accessories. It will show the details about the accessorie -->
						<div id="accessoryDisplayTopSection">
							<dsp:include page="/browse/accessories/details/includes/accessories_overview.jsp">
								<dsp:param name="accessory" param="accessory"/>
							</dsp:include>
						</div>
						
					<!--This JSP is being included in phone details page JSP to display the Legal Information for phone products in this Site-->	
					<dsp:include page="/common/includes/legalContent.jsp">
					<dsp:param name="PAGE_TYPE" value="ACCESSORY"/>
					</dsp:include>
					
					<!-- This JSP is being included in accessories details JSP page to display the promotional banners which are specific to location. -->
					<dsp:droplet name="/atg/dynamo/droplet/Cache">
						<dsp:param value="accessoryDetailsBottom_${profileProperty}_${isUserLoggedIn}" name="key"/>
						<dsp:oparam name="output">
							<dsp:include page="/browse/banners/footerPromotionalBanner.jsp">
								<dsp:param name="PAGE_NAME" value="ACCESSORY_DETAILS"/>
							</dsp:include>
						</dsp:oparam>
					</dsp:droplet>
					 
					
			</dsp:oparam>
		</dsp:droplet>
		<dsp:getvalueof var="productCategoryId"  param="productCategoryId"/>		
				</div> <!--/#constructor-->
		
<!--// END MAIN CONTENT AREA //-->

<!--// START FOOTER AREA //-->
	
	<dsp:droplet name="/atg/dynamo/droplet/Cache">
		<dsp:param value="cricketFooter" name="key"/>
		<dsp:oparam name="output">
			<dsp:include page="/common/footer.jsp"/>
		</dsp:oparam>
	</dsp:droplet>
		
</div> <!--/#inner-wrap-->
</div> <!--/#outer-wrap-->

	
<!-- JavaScript -->	

<!-- jQuery -->	
<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="Accessory"/>
</dsp:include>


<!-- Foundation 4 -->
<script src="${contextpath}/js/foundation/foundation.min.js"></script> 
<script>
$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips reveal');
</script>

<!-- Client Side Validation -->
<script src="${contextpath}/js/vendor/jquery.validate.min.js"></script>
	
<!-- SWIPER Library; Used for Phones and Plans on small screens -->
<script src="${contextpath}/lib/swiper/idangerous.swiper-2.0.min.js"></script>

<dsp:include page="/common/includes/openCartDrawer.jsp"/>
 
<!-- Cricket specific JS -->
<script src="${contextpath}/js/cricket.min.js"></script>

<!--  Google javascripts for mobile device -->
<script src="${contextpath}/js/customcricketstore.js" type="text/javascript" charset="utf-8"></script>

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
	var productName = '<dsp:valueof value='${productName}'/>';
	var productId = '<dsp:valueof value='${productId}'/>';
	var productCategoryId = "ACCESSORIES";
	var pageID = "ACCESSORY DETAILS"+" "+productName;
	var categoryID = "SHOP:ACCESSORIES";
	//must be dynamic values	
	var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
	var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
	var network = '<dsp:valueof value='${network}'/>';
	var userIntention = '<dsp:valueof value='${userIntention}'/>';
	var customerType = '<dsp:valueof value='${customerType}'/>';
	var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
	cmCreatePageviewTag(pageID,categoryID,null,null,pvAttrs);
	cmCreateProductviewTag(productId,productName,productCategoryId,pvAttrs );
</script>
</body>	
</html>
</dsp:page>