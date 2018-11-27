<dsp:page>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<dsp:getvalueof var="searchPage" param="searchPage"/>
	<dsp:getvalueof var="recordsPerPageSupport" value="${contentItem.recsPerPage}" scope="request" vartype="java.lang.Integer"/>
	<c:choose>
		<c:when test="${searchPage eq 'searchPage'}">
			<dsp:include page="/search/listing/includes/search_categoryBasedResults.jsp">
				<dsp:param name="contentItem" value="${contentItem}"/>
			</dsp:include>
		</c:when>
		<c:otherwise>
			<dsp:include page="/search/listing/includes/search_allSearchResults.jsp">
				<dsp:param name="contentItem" value="${contentItem}"/>
			</dsp:include>
		</c:otherwise>
	</c:choose>
	
</dsp:page>