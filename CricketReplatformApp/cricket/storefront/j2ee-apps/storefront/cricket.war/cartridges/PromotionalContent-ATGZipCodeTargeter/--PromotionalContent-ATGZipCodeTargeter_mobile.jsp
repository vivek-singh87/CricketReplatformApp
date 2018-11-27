<dsp:page>
<dsp:importbean bean="/atg/targeting/TargetingForEach"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>

	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
	<dsp:getvalueof var="targeterName" vartype="java.lang.String"  value="${contentItem.componentPath}"/>
	<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>
        <c:if test="${marketType eq 'OOF'}">
              <c:set var="linkName" value="${marketType}" scope="request"/>
        </c:if>                                   
        <c:if test="${marketType eq 'IF'}">
               <c:set var="linkName" value="${marketType}" scope="request"/>
        </c:if>
        
        <c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="userIntention"  bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean.userIntention"/>
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="userIntention"  value="New Activation"/>
		</c:if>
		<c:if test="${empty userIntention}">
			<dsp:getvalueof var="userIntention"  value="null"/>
		</c:if>	
	<dsp:getvalueof var="timeForToggle" value="${contentItem.TimerToRefresh}" scope="request"/>
	<dsp:getvalueof var="maxItem" value="${contentItem.itemCount}"/>
	<c:if test="${empty promoIdsPipeSeperatedTop}">
        	<dsp:getvalueof var="promoIdsPipeSeperatedTop" value="" scope="request" vartype="java.lang.String"/>
        </c:if>
        <dsp:getvalueof var="promoIdsPipeSeperatedBottom" value="" scope="request" vartype="java.lang.String"/>
	<dsp:getvalueof var="mobileSuffix" bean="CricketConfiguration.mobileSuffix"/>
	
	<ul id="orbit-main" data-orbit data-options="timer_speed:${timeForToggle};">
		<dsp:droplet name="TargetingForEach">
		<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/${targeterName}${mobileSuffix}" name="targeter"/>
			<dsp:param name="start" value="1"/>
	  		<dsp:param name="howMany" value="${maxItem}"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="count" param="count"/>
				<dsp:getvalueof var="image" param="element.url"/>
				<dsp:getvalueof var="landingUrl" param="element.landingUrl"/>
				<dsp:getvalueof var="promoId" param="element.promoId"/>
				<c:choose>
						<c:when test="${targeterName eq 'HomePagePromotionalBannerTargeterTop'}">
							<c:choose>
								<c:when test="${count eq 1}">
									<c:choose>
										<c:when test="${empty promoId}">
											<dsp:getvalueof var="promoIdsPipeSeperatedTop" value="${count}^empty" scope="request" vartype="java.lang.String"/>
										</c:when>
										<c:otherwise>
											<dsp:getvalueof var="promoIdsPipeSeperatedTop" value="${count}^${promoId}" scope="request" vartype="java.lang.String"/>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<dsp:getvalueof var="promoIdsPipeSeperatedTopBefore" value="${promoIdsPipeSeperatedTop}"/>
									<c:choose>
										<c:when test="${empty promoId}">
											<dsp:getvalueof var="promoIdsPipeSeperatedTop" value="${promoIdsPipeSeperatedTopBefore}|${count}^empty" scope="request" vartype="java.lang.String"/>
										</c:when>
										<c:otherwise>
											<dsp:getvalueof var="promoIdsPipeSeperatedTop" value="${promoIdsPipeSeperatedTopBefore}|${count}^${promoId}" scope="request" vartype="java.lang.String"/>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${count eq 1}">
									<c:choose>
										<c:when test="${empty promoId}">
											<dsp:getvalueof var="promoIdsPipeSeperatedBottom" value="${count}^empty" scope="request" vartype="java.lang.String"/>
										</c:when>
										<c:otherwise>
											<dsp:getvalueof var="promoIdsPipeSeperatedBottom" value="${count}^${promoId}" scope="request" vartype="java.lang.String"/>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<dsp:getvalueof var="promoIdsPipeSeperatedBottomBefore" value="${promoIdsPipeSeperatedBottom}"/>
									<c:choose>
										<c:when test="${empty promoId}">
											<dsp:getvalueof var="promoIdsPipeSeperatedBottom" value="${promoIdsPipeSeperatedBottomBefore}|${count}^empty" scope="request" vartype="java.lang.String"/>
										</c:when>
										<c:otherwise>
											<dsp:getvalueof var="promoIdsPipeSeperatedBottom" value="${promoIdsPipeSeperatedBottomBefore}|${count}^${promoId}" scope="request" vartype="java.lang.String"/>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				<%-- <dsp:getvalueof var="imageURL" value="${contextPath}/${image}"/> --%>
			<!--Start Droplet used to show images from Liquid Pixel Server -->
				<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
					<dsp:param value="${image}" name="imageLink"/>
					<dsp:oparam name="output">
					<dsp:getvalueof var="liquidpixelurl" param="url"/>
						<li><a onclick="cmCreateManualLinkClickTag('#')" href="${landingUrl}?cm_sp=hero-_-${promoId}-_-${linkName}">
								<img src="<c:out value='${liquidpixelurl}'/>" alt=""/>
							</a></li>
					</dsp:oparam>
				</dsp:droplet>
				<!--End Droplet used to show images from Liquid Pixel Server -->						
			</dsp:oparam>
		</dsp:droplet>
	</ul>
</dsp:page>