<dsp:page>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<c:forEach var="element" items="${contentItem.SecondaryContent}">
		<dsp:renderContentItem contentItem="${element}"/>
	</c:forEach>
</dsp:page>