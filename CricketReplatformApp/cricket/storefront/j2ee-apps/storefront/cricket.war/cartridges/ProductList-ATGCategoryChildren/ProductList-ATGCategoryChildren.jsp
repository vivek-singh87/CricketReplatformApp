
<dsp:page>
	<dsp:getvalueof var="timeMonitoring" bean="/com/cricket/configuration/CricketConfiguration.timeMonitoring"/> 
	<c:if test="${timeMonitoring == true}">
		<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
			<dsp:param name="isEndTime" value="false"/>
			<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>
			<dsp:param name="contentItemType" value="CategoryChildren"/>	
			<dsp:oparam name="output">
				<dsp:getvalueof var="startTime" param="startTime"/>
			</dsp:oparam>
		</dsp:droplet>
	</c:if>
	<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
	<dsp:importbean bean="/atg/commerce/catalog/CategoryLookup"/>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
	
  	
	
		<dsp:include page="/browse/gadgets/categoryChildProducts.jsp">
		    <dsp:param name="contentItem" value="${contentItem}"/>
		    <dsp:param name="p" param="p"/>
		    <dsp:param name="viewAll" value="false"/>
		</dsp:include>
 	<c:if test="${timeMonitoring == true}">
		<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
			<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>	
			<dsp:param name="contentItemType" value="CategoryChildren"/>
			<dsp:param name="startTime" value="${startTime}"/>
			<dsp:param name="isEndTime" value="true"/>
			<dsp:oparam name="output">
			</dsp:oparam>
		</dsp:droplet>
	</c:if>
</dsp:page>

