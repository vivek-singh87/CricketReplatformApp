<dsp:page>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/atg/dynamo/droplet/Cache"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="bannerProfileProperty" bean="CricketConfiguration.bannerProfileProperty"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:getvalueof var="profileProperty" bean="Profile.${bannerProfileProperty}"/>
<dsp:include page="/common/head.jsp">
	<dsp:param name="seokey" value="homeKey" />
	<dsp:param name="pageType" value="homePage" />
</dsp:include>
<body>
<div id="outer-wrap">
<div id="inner-wrap">
   <dsp:include page="/common/header.jsp"/>
	<div id="constructor">
<!--// END OF HEADER AREA OF THE PAGE //-->				
		
<!--// MAIN CONTENT AREA OF THE PAGE //-->
	<dsp:droplet name="Cache">
		<dsp:param value="homePageTopBanner_${profileProperty}_${isUserLoggedIn}" name="key"/>
		<dsp:oparam name="output">
			<dsp:include page="/home/includes/promotionBanner.jsp"/>
		</dsp:oparam>
	</dsp:droplet>
	
	<dsp:include page="/home/includes/whyCricketBanner.jsp"/>
	
	<dsp:droplet name="Cache">
		<dsp:param value="homePageFeaturedPhonesAndPlans_${profileProperty}" name="key"/>
		<dsp:oparam name="output">
			<dsp:include page="/home/includes/featuredPhones.jsp"/>
			<dsp:include page="/home/includes/featuredPlans.jsp"/>	
		</dsp:oparam>
	</dsp:droplet>

	<dsp:droplet name="Cache">
		<dsp:param value="homePageBottomBanner_${profileProperty}_${isUserLoggedIn}" name="key"/>
		<dsp:oparam name="output">
			<dsp:include page="/home/includes/musicBanner.jsp"/>
		</dsp:oparam>
	</dsp:droplet>
	

<!--// END OF MAIN CONTENT AREA AREA OF THE PAGE //-->	
		
<!--// FOOTER AREA OF THE PAGE //-->
	
	</div> <!--/#constructor-->
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


<script type="text/javascript" src="${contextpath}/js/vendor/jquery-ui.min.js"></script>
<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="Home"/>
</dsp:include>
<!-- Foundation 4 -->
<script src="${contextpath}/js/foundation/foundation.min.js"></script> 
<script>
$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips');
</script>

<!-- Client Side Validation -->
<script src="${contextpath}/js/vendor/jquery.validate.min.js"></script>

<!-- SWIPER Library; Used for Phones and Plans on small screens -->
<script src="${contextpath}/lib/swiper/idangerous.swiper-2.0.min.js"></script>

<dsp:include page="/common/includes/openCartDrawer.jsp"/>

<!-- Cricket specific JS -->
<!-- <script src="js/cricket.min.js"></script> -->
<script src="${contextpath}/js/cricket.min.js"></script> <!-- Global Utilities -->

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

<script> 
function rdiCoremetricsPageview( pvAttrs ) { cmCreatePageviewTag( 'http://www.mycricket.com/', 'http://www.mycricket.com/', null, null, rdiCmToAttrString( pvAttrs )); }
</script>
<script type="text/javascript">
	var pageID = "http://www.mycricket.com/";
	var categoryID = "http://www.mycricket.com/";
	//must be dynamic values
	var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
	var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
	var network = '<dsp:valueof value='${network}'/>';
	var userIntention = '<dsp:valueof value='${userIntention}'/>';
	var customerType = '<dsp:valueof value='${customerType}'/>';
	var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
	var linkName = '<dsp:valueof value='${linkName}'/>'
	cmCreatePageviewTag(pageID,categoryID,null,null,pvAttrs);
	
	var pipeSeperatedTop = '<dsp:valueof value='${promoIdsPipeSeperatedTop}'/>';
	var pipeSeperatedBottom = '<dsp:valueof value='${promoIdsPipeSeperatedBottom}'/>';
	
	var pipeSeperatedTopSplit1 = pipeSeperatedTop.split("|");
	var pipeSeperatedBottomSplit1 = pipeSeperatedBottom.split("|");
	
	for (var i=0;i<pipeSeperatedTopSplit1.length;i++) {
		pipeSeperatedTopSplit2 = pipeSeperatedTopSplit1[i].split("^");
		var trackSitePromotionID = "";
		if(pipeSeperatedTopSplit2[1] == 'empty') {
			trackSitePromotionID = "hero"+"-_-" + "" + "-_-" + linkName;	
		} else {
			trackSitePromotionID = "hero" + "-_-" + pipeSeperatedTopSplit2[1] + "-_-" + linkName;
		}
		cmCreateManualImpressionTag ( pageID, trackSitePromotionID, null, null, pvAttrs  );
	}
	for (var i=0;i<pipeSeperatedBottomSplit1.length;i++) {
		pipeSeperatedBottomSplit2 = pipeSeperatedBottomSplit1[i].split("^");
		var trackSitePromotionID2 = "";
		if(pipeSeperatedBottomSplit2[1] == 'empty') {
			trackSitePromotionID2 = "hero"+"-_-" + "" + "-_-" + linkName;	
		} else {
			trackSitePromotionID2 = "hero" + "-_-" + pipeSeperatedBottomSplit2[1] + "-_-" + linkName;
		}
		cmCreateManualImpressionTag ( pageID, trackSitePromotionID2, null, null, pvAttrs  );
	}
</script>

</body>
<!--// END OF FOOTER AREA OF THE PAGE //-->
</html>
</dsp:page>