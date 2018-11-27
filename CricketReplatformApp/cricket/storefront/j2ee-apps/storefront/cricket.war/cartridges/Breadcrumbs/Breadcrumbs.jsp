<%--
  Breadcrumbs
  
  Renders refinement that have been selected. Selected refinements can consist
  of search refinements, dimension refinements or range filter refinements.
  
  There are a number of different types of breadcrumb that can be returned
  inside this content item:
    refinementCrumbs - As a result of selecting a dimension
    searchCrumbs - As a result of performing a search
    rangeFilterCrumbs - As a result of applying a range filter
--%>
<dsp:page>
	<dsp:getvalueof var="timeMonitoring" bean="/com/cricket/configuration/CricketConfiguration.timeMonitoring"/> 
	<c:if test="${timeMonitoring == true}">
		<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
			<dsp:param name="isEndTime" value="false"/>
			<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>
			<dsp:param name="contentItemType" value="BreadCrumbs"/>	
			<dsp:oparam name="output">
				<dsp:getvalueof var="startTime" param="startTime"/>
			</dsp:oparam>
		</dsp:droplet>
	</c:if>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  
  <dsp:getvalueof var="content" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/> 
  <dsp:getvalueof var="contextPath" vartype="java.lang.String" value="${originatingRequest.contextPath}"/>

  <c:if test="${not empty content.refinementCrumbs || not empty content.rangeFilterCrumbs || not empty content.searchCrumbs}">    
      

      <%-- Display currently selected refinements if there are any  --%>
      <dsp:getvalueof var="dimCrumbs" value="${content.refinementCrumbs}" scope="request"/>
      <dsp:getvalueof var="rangeCrumbs" value="${content.rangeFilterCrumbs}" scope="request"/>
		<%-- <c:forEach var="dimCrumb" items="${content.refinementCrumbs}">
			<dsp:getvalueof var="navAction" value="${dimCrumb.removeAction}"/>
			<dsp:valueof value="${dimCrumb.label}"/><dsp:a href="javascript:hitEndecaWithFilterQuery('${contextPath}${navAction.contentPath}${navAction.navigationState}')">(remove)</dsp:a>
		</c:forEach>  --%>
  </c:if>
  <c:if test="${timeMonitoring == true}">
	<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
		<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>	
		<dsp:param name="contentItemType" value="BreadCrumbs"/>
		<dsp:param name="startTime" value="${startTime}"/>
		<dsp:param name="isEndTime" value="true"/>
		<dsp:oparam name="output">
		</dsp:oparam>
	</dsp:droplet>
</c:if>
</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/10.2/Storefront/j2ee/store.war/cartridges/Breadcrumbs/Breadcrumbs.jsp#5 $$Change: 796430 $--%>
