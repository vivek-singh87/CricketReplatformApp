<dsp:page>
	<dsp:importbean bean="/com/cricket/search/session/UserSearchSessionInfo"/>
	<dsp:importbean bean="/atg/dynamo/droplet/Range"/>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<c:if test="${empty recordsPerPageSupport || recordsPerPageSupport eq 0}">
		<dsp:getvalueof var="recordsPerPageSupport" bean="UserSearchSessionInfo.itemsPerPage"/>
	</c:if>
	<c:if test="${empty recordsPerPageSupport || recordsPerPageSupport eq 0}">
		<dsp:getvalueof var="recordsPerPageSupport" value="12"/>
	</c:if>
	<div id="search-support-results">
		<dsp:droplet name="Range">
			<dsp:param name="array" value="${supportResultVOs}"/>
			<dsp:param name="elementName" value="supportVO"/>
			<dsp:param name="howMany" value="${recordsPerPageSupport}"/>
			<dsp:param name="start" param="sv"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="faqTitle" param="supportVO.faqTitle"/>
				<dsp:getvalueof var="faqQuestion" param="supportVO.faqQuestion"/>
				<dsp:getvalueof var="faqAnswer" param="supportVO.faqAnswer"/>
				<dsp:getvalueof var="count" param="count"/>
				<dsp:getvalueof var="size" param="size"/>
				<c:if test="${count eq 1 || (count mod 3 eq 1)}">
					<div class="row search-section support">
				</c:if>
						<div class="columns result">
							<div class="copy left">
								<h3>${faqTitle}</h3>
								<h4>${faqQuestion}</h4>
								<dsp:getvalueof var="supportTerm" param="supportVO.faqLink"/>
								<dsp:getvalueof var="supportLink" bean="CricketConfiguration.supportLink"/>
								<a class="circle-arrow" href="${supportLink}/${supportTerm}">View</a>
							</div>
						</div>
				<c:choose>
					<c:when test="${count ne size && (count mod 3 eq 0)}">
						</div>
						<hr class="hide-for-small" />
					</c:when>
					<c:when test="${count eq size && (count mod 3 eq 0)}">
						</div>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</dsp:oparam>
		</dsp:droplet>
	</div>
	<!-- Pager Include -->
	<dsp:include page="/search/listing/support/support_pagination.jsp">
		<dsp:param name="supportResults" value="${supportResultVOs}"/>
	</dsp:include>
</dsp:page>