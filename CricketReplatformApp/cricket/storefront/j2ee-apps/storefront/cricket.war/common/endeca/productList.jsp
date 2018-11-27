<dsp:page>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/com/cricket/browse/DetermineCategoryTemplate"/>
<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
	<dsp:droplet name="DetermineCategoryTemplate">
		<dsp:param name="categoryId" value="${contentItem.MainContent[0].categoryId}"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="templateUrl" param="templateUrl" scope="request"/>
		</dsp:oparam>
	</dsp:droplet>
	<dsp:include page="${templateUrl}">
		<dsp:param name="contentItem" value="${contentItem}"/>
	</dsp:include>
</dsp:page>
	