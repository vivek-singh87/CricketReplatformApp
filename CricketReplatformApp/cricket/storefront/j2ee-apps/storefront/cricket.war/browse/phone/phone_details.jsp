<dsp:page>

<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/com/cricket/browse/PhoneDetailsDroplet"/>
<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="bannerProfileProperty" bean="CricketConfiguration.bannerProfileProperty"/>
<dsp:getvalueof var="profileProperty" bean="Profile.${bannerProfileProperty}"/>
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
		<dsp:getvalueof var="productName" param="Product.displayName"/>
		<dsp:getvalueof var="productId" param="Product.repositoryId"/>
		<dsp:getvalueof var="productSEODesc" param="Product.SEODescription"/>
		<dsp:getvalueof var="productSEOKey" param="Product.SEOKeywords"/>
	</dsp:oparam>
</dsp:droplet>
<dsp:include page="/common/head.jsp">
	<dsp:param name="seokey" value="${productName}" />
	<dsp:param name="pageType" value="phoneDetails"/>
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

	<dsp:include page="/common/header.jsp"/>
	

	<div id="constructor" class="section-phones-details">
	
<!--// MAIN CONTENT AREA OF THE PAGE //-->

	<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
		<dsp:param name="id" param="productId"/>
		<dsp:param name="filterBySite" value="false"/>
       	<dsp:param name="filterByCatalog" value="false"/>
		<dsp:param name="elementName" value="Product"/>
			<dsp:oparam name="output">
			<dsp:getvalueof var="manuallyEnteredZipCode" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
			<dsp:getvalueof var="productName" param="Product.displayName"/>
			<dsp:getvalueof var="productId" param="Product.repositoryId"/>
			<c:choose>
	  				<c:when test="${manuallyEnteredZipCode eq false}">
			 			<!--This is the included page in product detail page to display the name, images, available colors, add to cart button should be disable   -->
			 			<div id="phoneDisplayTopSection">
				 			<dsp:include page="/browse/phone/details/includes/displayPhoneName.jsp">						
								<dsp:param name="Product" param="Product"/>
							</dsp:include>
						</div>
					</c:when>
					<c:otherwise>	
					<!--This is the included page in product detail page to if Enter Zipcode  we will display the price, discounts and Finance available shipping information with enabled Add To Cart Button.  -->
					<div id="phoneDisplayTopSection">						
						<dsp:include page="/browse/phone/details/includes/displayPhoneNamewithZipcode.jsp">
							<dsp:param name="Product" param="Product"/>
						</dsp:include>
					</div>
					</c:otherwise>
				</c:choose>
				
	 				<!--This is the included page in product detail page. This jsp page will show the specification of the phone. It will display phone features, cricket services support, memory, camera etc.. -->
				<dsp:include page="/browse/phone/details/includes/phoneSpecificationTab.jsp">
						<dsp:param name="Product" param="Product"/>
				</dsp:include>
				
	 				<!--This is the included page in phone details page which displays additional accessories carousel for the selected phone. Each accessory product contains accessory image, name, long description, features, price and Add to Cart Button. -->
				<dsp:include page="/browse/phone/details/additinalAccessories.jsp">
					<dsp:param name="Product" param="Product"/>
				</dsp:include>
				
	 				<!--This is the included page in phone_details.jsp which displays phones those are similar to the selected phone. Each phone product contains phone image, name and features. --> 
				<dsp:include page="/browse/phone/details/similarPhones.jsp">
					<dsp:param name="Product" param="Product"/>
				</dsp:include>
				
	 				<!--This is the included page in phone_details.jsp which displays plans those are compatible with the selected phone. Each plan product contains plan price, name, features, Add to Cart button and learn more link.-->			
				<dsp:include page="/browse/phone/details/plansWithPhone.jsp">
					<dsp:param name="Product" param="Product"/>
				</dsp:include>
				
				<!--This JSP is being included in phone details page JSP to display the Legal Information for phone products in this Site-->	
				<dsp:include page="/common/includes/legalContent.jsp">
					<dsp:param name="PAGE_TYPE" value="PHONE"/>
				</dsp:include>
				
	 				<!--This JSP is being included in phone details page JSP to display the promotional banner which is specific to location-->	
	 			<dsp:droplet name="/atg/dynamo/droplet/Cache">
					<dsp:param value="phoneDetailsBottom_${profileProperty}_${isUserLoggedIn}" name="key"/>
					<dsp:oparam name="output">
						<dsp:include page="/browse/banners/footerPromotionalBanner.jsp">
							<dsp:param name="PAGE_NAME" value="PHONE_DETAILS"/>
						</dsp:include>	
					</dsp:oparam>
				</dsp:droplet>
		</dsp:oparam>
	</dsp:droplet>
	<dsp:getvalueof var="productCategoryId"  param="productCategoryId"/>
<!--// END OF MAIN CONTENT AREA AREA OF THE PAGE //-->	
		
	</div> <!--/#constructor-->
	<!--// END CONTENT AREA //-->
		

<!--// START FOOTER AREA //-->
	
	<dsp:droplet name="/atg/dynamo/droplet/Cache">
		<dsp:param value="cricketFooter" name="key"/>
		<dsp:oparam name="output">
			<dsp:include page="/common/footer.jsp"/>
		</dsp:oparam>
	</dsp:droplet>
		
</div> <!--/#inner-wrap-->
</div> <!--/#outer-wrap-->
	


<!-- Foundation 4 -->
<!-- jQuery -->	
<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="Phones"/>
</dsp:include>
<script src="${contextPath}/js/foundation/foundation.min.js"></script> 
<script>
$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips reveal');
</script>

<!-- Client Side Validation -->
<script src="${contextPath}/js/vendor/jquery.validate.min.js"></script>
	
<!-- SWIPER Library; Used for Phones and Plans on small screens -->
<script src="${contextPath}/lib/swiper/idangerous.swiper-2.0.min.js"></script>

<!-- Cricket specific JS -->
<script src="${contextPath}/js/cricket.min.js"></script>

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
	var productName = '<dsp:valueof value='${productName}'/>';
	var productId = '<dsp:valueof value='${productId}'/>';
	var productCategoryId = "HANDSETS";
	var pageID = "PHONES"+" "+productName;
	var categoryID = "SHOP:PHONES";
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