<dsp:page>

<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>

	<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
		 <dsp:param name="id" param="productId"/>
		 <dsp:param name="filterBySite" value="false"/>
       	 <dsp:param name="filterByCatalog" value="false"/>
		 <dsp:param name="elementName" value="product"/>
			<dsp:oparam name="output">
				<dsp:droplet name="GetSeoStringDroplet">
					<dsp:param name="product" param="product"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="seoString" param="seoString"/>
					</dsp:oparam>
				</dsp:droplet>
				<dsp:getvalueof var="planAddOnName" param="product.displayName"/>
				<dsp:getvalueof var="productSEODesc" param="product.SEODescription"/>
				<dsp:getvalueof var="productSEOKey" param="product.SEOKeywords"/>
			</dsp:oparam>
	</dsp:droplet>
	<dsp:include page="/common/head.jsp">
		<dsp:param name="seokey" value="${planAddOnName}" />
		<dsp:param name="pageType" value="planAddOnDetails"/>
		<dsp:param name="seoString" value="${seoString}" />
	</dsp:include>
	
		<meta name="description" content="${productSEODesc}" />
		<meta name="keywords" content="${productSEOKey}"/>
	
<body>
		<script type="text/javascript">
			//cmCreatePageviewTag('http://www.mycricket.com/', 'http://www.mycricket.com/', null, null, pvAttrs);
		</script>
		<div id="outer-wrap">
			<div id="inner-wrap">
				<!--This JSP is being included in plan addon detail page JSP to display the header part of the page-->	
				<dsp:include page="/common/header.jsp"/>
				<!--// END HEADER AREA //-->
				<!--// START CONTENT AREA //-->			
				<div id="constructor" class="section-plan-addons-details">
							
					<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
						<dsp:param name="id" param="productId"/>
						<dsp:param name="filterBySite" value="false"/>
       					<dsp:param name="filterByCatalog" value="false"/>
						<dsp:param name="elementName" value="product"/>
							<dsp:oparam name="output">
							<dsp:getvalueof var="productId" param="product.repositoryId"/>
							<dsp:getvalueof var="planAddOnName" param="product.displayName"/>
								<!--This is the included page in plan addon detail page to display the name, images, available, add to cart button should be disable   -->
								<dsp:include page="/browse/plan/addon/addon_display.jsp"></dsp:include>
								 <!--This is the included page in plan addon detail page. This jsp page will show the overview of the addon from addon overview properties and also display the corresponding images. -->
								<dsp:include page="/browse/plan/addon/addon_overview.jsp"></dsp:include>							
							</dsp:oparam>
					</dsp:droplet>
					<dsp:getvalueof var="productCategoryId"  param="productCategoryId"/>
					<!-- Static Banner -->
					<!--This JSP is being included in plan addon detail page JSP to display the promotional banner which is specific to location-->	
					<dsp:include page="/browse/plan/addon/addon_details_staticbanner_footer.jsp"></dsp:include>
				</div>
				<!--/#constructor-->
				<!--// END CONTENT AREA //-->

				<!--// START FOOTER AREA //-->
					<!--This JSP is being included in plan addon detail page JSP to display the footer part of the page-->	
				<dsp:droplet name="/atg/dynamo/droplet/Cache">
					<dsp:param value="cricketFooter" name="key"/>
					<dsp:oparam name="output">
						<dsp:include page="/common/footer.jsp"/>
					</dsp:oparam>
				</dsp:droplet>
			</div>
			<!--/#inner-wrap-->
		</div>
		<!--/#outer-wrap-->


<!-- JavaScript -->

<!-- jQuery -->
<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="Plan_Addon"/>
</dsp:include>
<!-- Foundation 4 -->
<script src="${contextpath}/js/foundation/foundation.min.js"></script>
<script>
	$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips');
</script>

<script> $(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips reveal'); var w = $(document).width(); if(w>769) { $(document).foundation('section', { callback: function (){ var containerPos = $('.section-container .active').offset().top; $('html, body').animate({ scrollTop: containerPos }, 0); } }); } </script>

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
	var productName = '<dsp:valueof value='${planAddOnName}'/>';
	var productId = '<dsp:valueof value='${productId}'/>';
	var productCategoryId = "PLAN ADD ONS";
	var pageID = "PLAN AddOn DETAILS"+" "+productName;
	var categoryID = "SHOP:PLANS";
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

</dsp:page>