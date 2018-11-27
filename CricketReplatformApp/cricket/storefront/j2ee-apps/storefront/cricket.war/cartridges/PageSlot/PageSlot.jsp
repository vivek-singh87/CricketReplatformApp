<dsp:page>
	<dsp:getvalueof var="timeMonitoring" bean="/com/cricket/configuration/CricketConfiguration.timeMonitoring"/>
	<c:if test="${timeMonitoring == true}">
		<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
			<dsp:param name="isEndTime" value="false"/>
			<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>
			<dsp:param name="contentItemType" value="PageSlot"/>	
			<dsp:oparam name="output">
				<dsp:getvalueof var="startTime" param="startTime"/>
			</dsp:oparam>
		</dsp:droplet>
	</c:if>
	<dsp:getvalueof bean="/OriginatingRequest" var="originatingRequest"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
	<endeca:includeSlot contentItem="${contentItem}">
		<c:forEach var="element" items="${contentItem.contents}">
			<dsp:renderContentItem contentItem="${element}"/>
		</c:forEach>
	</endeca:includeSlot>
	<c:if test="${timeMonitoring == true}">
		<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
			<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>	
			<dsp:param name="contentItemType" value="PageSlot"/>
			<dsp:param name="startTime" value="${startTime}"/>
			<dsp:param name="isEndTime" value="true"/>
			<dsp:oparam name="output">
			</dsp:oparam>
		</dsp:droplet>
	</c:if>
</dsp:page>
