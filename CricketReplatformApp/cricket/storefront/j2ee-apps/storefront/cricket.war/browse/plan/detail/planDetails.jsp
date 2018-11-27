<dsp:page>

<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>

	<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
		 <dsp:param name="id" param="productId"/>
		 <dsp:param name="filterBySite" value="false"/>
       	 <dsp:param name="filterByCatalog" value="false"/>
		 <dsp:param name="elementName" value="Product"/>
			<dsp:oparam name="output">
				<dsp:droplet name="GetSeoStringDroplet">
					<dsp:param name="product" param="Product"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="seoString" param="seoString"/>
					</dsp:oparam>
				</dsp:droplet>
				<dsp:getvalueof var="planName" param="Product.displayName"/>
				<dsp:getvalueof var="productSEODesc" param="Product.SEODescription"/>
				<dsp:getvalueof var="productSEOKey" param="Product.SEOKeywords"/>
			</dsp:oparam>
	</dsp:droplet>
	<dsp:include page="/common/head.jsp">
		<dsp:param name="seokey" value="${planName}" />
		<dsp:param name="pageType" value="planDetails"/>
	</dsp:include>

	<meta name="description" content="${productSEODesc}" />
	<meta name="keywords" content="${productSEOKey}"/>

<script type="text/javascript"> 
 //cmCreatePageviewTag('http://www.mycricket.com/', 'http://www.mycricket.com/', null, null, pvAttrs);
</script>
<body>
<div id="outer-wrap">
<div id="inner-wrap">
<dsp:include page="/common/header.jsp"/>
 <!--/#header-->	
<!--// END HEADER AREA //-->



<div id="constructor" class="section-plan-details">
	
<!--START Including the pages-->
<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
		<dsp:param name="id" param="productId"/>
		<dsp:param name="filterBySite" value="false"/>
       	<dsp:param name="filterByCatalog" value="false"/>
		<dsp:param name="elementName" value="Product"/>
			<dsp:oparam name="output">
			<dsp:getvalueof var="productId" param="Product.repositoryId"/>
			<dsp:getvalueof var="planName" param="Product.displayName"/>
				 <dsp:getvalueof var="zipcodevalue" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
				 
				 <!--On basis of Zipcode provided include PLAN overview pages-->
				<c:choose>
	  				<c:when test="${zipcodevalue eq false}">
			 			<!--This is the included page in plan detail page to display the name, images, available colors, phone price, add to cart button. -->
						<dsp:include page="/browse/plan/detail/includes/displayPlanDetail.jsp"/>	
					</c:when>
					<c:otherwise>	
					<!--This is the included page in plan detail page to if Enter Zipcode data display the name, images, available colors, phone price, add to cart button. -->
						<dsp:include page="/browse/plan/detail/includes/displayPlanDetailwithZipcode.jsp"/>	
					</c:otherwise>
				</c:choose> 
				
						<!-- include Plan Overview page -->
					<dsp:include page="/browse/plan/detail/includes/planOverView.jsp"/>	
					
				
						<!-- include Plan Addons phones -->
					<dsp:include page="/browse/plan/detail/includes/planAddons.jsp"/>
					
						<!-- included Recomended phones -->
					<dsp:include page="/browse/plan/detail/includes/planCompatiblePhones.jsp"/>	
	
	
</dsp:oparam>
</dsp:droplet>
<!--END Including the pages-->
	<dsp:getvalueof var="productCategoryId"  param="productCategoryId"/>
</div>
<!--// START FOOTER AREA //-->
	
	<dsp:include page="/common/footer.jsp"/>	
		
</div> <!--/#inner-wrap-->
</div> <!--/#outer-wrap-->
	
	
<!-- JavaScript -->	

<!-- jQuery -->	
<script type="text/javascript" src="${contextPath}/js/vendor/jquery-ui.min.js"></script>

<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="Plans"/>
</dsp:include>

<!-- Foundation 4 -->
<script src="${contextPath}/js/foundation/foundation.min.js"></script> 
<script>
$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips reveal');
</script>

<!-- Client Side Validation -->
<script src="${contextPath}/js/vendor/jquery.validate.min.js"></script>

<!-- SWIPER Library; Used for Phones and Plans on small screens -->
<script src="${contextPath}/lib/swiper/idangerous.swiper.js"></script>

<!-- Auto complete Plugin for Search -->
<script src="${contextPath}/lib/autocomplete/jquery.autocomplete.js" type="text/javascript"></script>

<!-- Cricket specific JS -->
<!-- <script src="js/cricket.min.js"></script> -->
<script src="${contextPath}/js/cricket.min.js"></script> <!-- Global Utilities -->

<!--  Google javascripts for mobile device -->
<script src="${contextPath}/js/customcricketstore.js" type="text/javascript" charset="utf-8"></script>

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
	var productName = '<dsp:valueof value='${planName}'/>';
	var productId = '<dsp:valueof value='${productId}'/>';
	var productCategoryId = "VOICE PLANS";
	var pageID = "PLAN DETAILS"+" "+productName;
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