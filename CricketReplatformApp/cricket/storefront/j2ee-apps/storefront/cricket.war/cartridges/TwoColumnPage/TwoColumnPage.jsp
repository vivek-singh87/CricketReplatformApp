<dsp:page>
	<dsp:getvalueof var="timeMonitoring" bean="/com/cricket/configuration/CricketConfiguration.timeMonitoring"/> 
	<c:if test="${timeMonitoring == true}">
		<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
			<dsp:param name="isEndTime" value="false"/>
			<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>
			<dsp:param name="contentItemType" value="TwoColumnPage"/>	
			<dsp:oparam name="output">
				<dsp:getvalueof var="startTime" param="startTime"/>
			</dsp:oparam>
		</dsp:droplet>
	</c:if>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>	
	<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
  	<dsp:getvalueof var="atgSort" param="sort" scope="request"/>
  	<dsp:getvalueof var="nsValue" param="Ns" scope="request"/>
	<dsp:include page="/common/head.jsp">
		<dsp:param name="seokey" value="${contentItem.MainContent[0].categoryId}" />
		<dsp:param name="pageType" value="listingPage" />
	</dsp:include>
	<input type="hidden" value="${contextPath}" id="contextPathValue"/>
	<input type="hidden" value="notYet" id="filterSelected"/>
	<input type="hidden" value="" id="sortOption"/>
  	<input type="hidden" value="${nsValue}" id="nsValue"/>
	<body>
		<div id="outer-wrap">
			<div id="inner-wrap">
				<dsp:include page="/common/header.jsp"/>
				<dsp:include page="/common/endeca/productList.jsp">
					<dsp:param name="contentItem" value="${contentItem}"/>
				</dsp:include>
				<dsp:droplet name="/atg/dynamo/droplet/Cache">
					<dsp:param value="cricketFooter" name="key"/>
					<dsp:oparam name="output">
						<dsp:include page="/common/footer.jsp"/>
					</dsp:oparam>
				</dsp:droplet>
				<!-- JavaScript -->
				<!-- jQuery -->
				<script type="text/javascript" src="${contextPath}/js/vendor/jquery-ui.min.js"></script>
				<script type="text/javascript" src='${contextPath}/js/chatGui-min.js'></script>
				<dsp:include page="/common/includes/click_to_chat.jsp">
					<dsp:param name="PAGE_TYPE" value="Listing Page"/>
				</dsp:include>
				<!-- Foundation 4 -->
				<script src="${contextPath}/js/foundation/foundation.min.js"></script> 
				<script> $(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips reveal'); var w = $(document).width(); if(w>769) { $(document).foundation('section', { callback: function (){ var containerPos = $('.section-container .active').offset().top; $('html, body').animate({ scrollTop: containerPos }, 0); } }); } </script>
				
				<!-- Client Side Validation -->
				<script src="${contextPath}/js/vendor/jquery.validate.min.js"></script>
				
				<!-- SWIPER Library; Used for Phones and Plans on small screens -->
				<script src="${contextPath}/lib/swiper/idangerous.swiper.js"></script>
				
				<!-- Auto complete Plugin for Search -->
				<script src="${contextPath}/lib/autocomplete/jquery.autocomplete.js" type="text/javascript"></script>
				
				<!-- Cricket specific JS -->
				<script src="${contextPath}/js/cricket.min.js"></script> <!-- Global Utilities -->
				<script src="${contextPath}/js/cricket/ajaxReloadEndecaResponse.js"></script>
			</div>
		</div>
		<dsp:include page="/common/includes/openCartDrawer.jsp"/>
		<c:if test="${timeMonitoring == true}">
			<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
				<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>	
				<dsp:param name="contentItemType" value="TwoColumnPage"/>
				<dsp:param name="startTime" value="${startTime}"/>
				<dsp:param name="isEndTime" value="true"/>
				<dsp:oparam name="output">
				</dsp:oparam>
			</dsp:droplet>
		</c:if>
</body>	
</html>
</dsp:page>
