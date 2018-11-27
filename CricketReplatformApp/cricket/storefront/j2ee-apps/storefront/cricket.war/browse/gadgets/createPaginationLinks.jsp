<dsp:page>
	<%--<dsp:getvalueof var="p" param="p"/>
	<dsp:getvalueof var="howMany" param="howMany"/>
	<dsp:getvalueof var="start" param="start"/>
	<dsp:getvalueof var="size" param="size"/>
	<dsp:getvalueof var="navParam" param="N"/>
	<c:choose>
		<c:when test="${(size mod howMany) gt 0}">
			<fmt:formatNumber var="pagesFract" value="${size/howMany}" maxFractionDigits="0" />
			<dsp:getvalueof var="pages" value="${pagesFract + 1}"/>
		</c:when>
		<c:otherwise>
			<dsp:getvalueof var="pages" value="${size/howMany}"/>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${pages eq p}">
			<c:out value="showing ${start} - ${size} of ${size}"/>
		</c:when>
		<c:otherwise>
			<c:out value="showing ${start} - ${(p * howMany)} of ${size}"/>
		</c:otherwise>
	</c:choose>
	<dsp:include page="/browse/gadgets/createSortMenu.jsp">
		<dsp:param name="sortSelection" param="sortSelection"/>
	</dsp:include>
	<b> page </b>
	<c:forEach var="i" begin="1" end="${pages}">
		<c:choose>
			<c:when test="${i eq p}">
				<c:out value="${i}"/>
			</c:when>
			<c:otherwise>
				<dsp:a href="javascript:hitEndecaWithPaginationOrSortQuery('${contextPath}/browse/?N=${navParam}&p=${i}')"><c:out value="${i}"/></dsp:a>
			</c:otherwise>
		</c:choose>
	   
	</c:forEach> --%>
	
</dsp:page>