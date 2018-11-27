<dsp:page>
<dsp:importbean bean="/atg/targeting/TargetingForEach"/>
<dsp:importbean bean="/atg/targeting/TargetingFirst"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
	<dsp:getvalueof var="targeterName" vartype="java.lang.String"  value="${contentItem.componentPath}"/>
	<dsp:getvalueof var="linkName" bean="Profile.marketType" scope="request"></dsp:getvalueof>
	<c:choose>
		<c:when test="${targeterName eq 'HomePagePromotionalBannerTargeterTop'}">
			<dsp:getvalueof var="bottomBannerExtraAttributesMobile" value=""/>
		</c:when>
		<c:otherwise>
			<dsp:getvalueof var="bottomBannerExtraAttributesMobile" value="navigation_arrows: true;"/>
		</c:otherwise>
	</c:choose>
        
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

			<ul id="orbit-main" data-orbit data-options="timer_speed:${timeForToggle};resume_on_mouseout:true;slide_number: false;${bottomBannerExtraAttributesMobile}">
				<dsp:droplet name="TargetingFirst">
					<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/${targeterName}" name="targeter"/>
					<dsp:param name="start" value="1"/>
					<dsp:param name="howMany" value="${maxItem}"/>
					<dsp:param name="sortProperties" value="+displayPriority"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="count" param="count"/>
						<dsp:getvalueof var="image" param="element.url"/>
						<dsp:getvalueof var="landingUrl" param="element.landingUrl"/>
						<dsp:getvalueof var="promoId" param="element.promoId"/>
						<%-- <dsp:getvalueof var="imageURL" value="${contextPath}/${image}"/> --%>
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
						                                                                         
					</dsp:oparam>
				</dsp:droplet>
				<!--End Droplet used to show images from Liquid Pixel Server -->                                                              
			</ul>
</dsp:page>
