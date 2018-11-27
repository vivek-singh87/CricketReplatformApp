<dsp:page>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="activeCategory" param="activeLink"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<c:choose>
		<c:when test="${activeCategory eq 'Phones'}">
			<dsp:include page="/search/listing/phones/search_phoneResults.jsp">
				<dsp:param name="contentItem" value="${contentItem}"/>
			</dsp:include>
		</c:when>
		<c:when test="${activeCategory eq 'Accessories'}">
			<dsp:include page="/search/listing/accessories/search_accessoryResults.jsp">
				<dsp:param name="contentItem" value="${contentItem}"/>
			</dsp:include>
		</c:when>
		<c:when test="${activeCategory eq 'Plans'}">
			<dsp:include page="/search/listing/plans/search_planResults.jsp">
				<dsp:param name="contentItem" value="${contentItem}"/>
			</dsp:include>
		</c:when>
		<c:when test="${activeCategory eq 'AddOns'}">
			<dsp:include page="/search/listing/addons/search_addOnResults.jsp">
				<dsp:param name="contentItem" value="${contentItem}"/>
			</dsp:include>
		</c:when>
		<c:when test="${activeCategory eq 'support'}">
			<dsp:include page="/search/listing/support/search_supportResults.jsp">
			</dsp:include>
		</c:when>
		
	</c:choose>
</dsp:page>