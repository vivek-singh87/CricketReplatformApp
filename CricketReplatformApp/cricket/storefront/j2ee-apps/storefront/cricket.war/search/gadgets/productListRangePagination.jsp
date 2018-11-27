
<dsp:page>

	<dsp:importbean bean="/atg/store/droplet/ArraySubsetHelper"/>
	
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>	
	<dsp:getvalueof var="viewAll" param="viewAll"/>
	<dsp:getvalueof var="top" param="top"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	
	<c:set var="size" value="${contentItem.totalNumRecs}" />
	<c:set var="howMany" value="${contentItem.recsPerPage}"/>
	<c:set var="start" value="${contentItem.firstRecNum}"/>
	<fmt:formatNumber var="p" value="${((start - 1) / howMany) + 1}" maxFractionDigits="0" />
  
	<c:set var="pageTemplate" value="${contentItem.pagingActionTemplate.navigationState}"/>
	<c:if test="${size > howMany}">
		<c:choose>
			<c:when test="${(size mod howMany) gt 0}">
				<fmt:formatNumber var="pagesFract" value="${size/howMany}" maxFractionDigits="0" />
				<dsp:getvalueof var="pages" value="${pagesFract + 1}"/>
			</c:when>
			<c:otherwise>
				<dsp:getvalueof var="pages" value="${size/howMany}"/>
			</c:otherwise>
		</c:choose>
		<td style="text-align:left" colspan="1">
			<c:choose>
				<c:when test="${pages eq p}">
					<c:out value="showing ${start} - ${size} of ${size}"/>
				</c:when>
				<c:otherwise>
					<fmt:formatNumber var="endPage" value="${(p * howMany)}" maxFractionDigits="0"/>
					<c:out value="showing ${start} - ${endPage} of ${size}"/>
				</c:otherwise>
			</c:choose>
		</td>
		<td style="text-align:right" colspan="2">
			<b> page </b>
			<c:forEach var="i" begin="1" end="${pages}">
				<c:choose>
					<c:when test="${i eq p}">
						<c:out value="${i}"/>
					</c:when>
					<c:otherwise>
						<c:set var="linkAction" value="${fn:replace(fn:replace(pageTemplate, '%7Boffset%7D', howMany*(i-1)), '%7BrecordsPerPage%7D',  howMany)}" />
						<dsp:a href="${contextpath}/browse/${linkAction}"><c:out value="${i}"/></dsp:a>
					</c:otherwise>
				</c:choose>
			   
			</c:forEach>
		</td>
	</c:if>
</dsp:page>

