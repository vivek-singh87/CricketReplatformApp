<dsp:page>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<dsp:importbean bean="/com/cricket/endeca/droplet/DeterminePageTypeFromNavParam"/>
	<dsp:getvalueof var="searchCartridgeInvoked" value="invoked" vartype="java.lang.String" scope="request"/>
	<dsp:droplet name="DeterminePageTypeFromNavParam">
		<dsp:param name="navState" param="N"/>
		<dsp:param name="searchPage" param="searchPage"/>
		<dsp:oparam name="searchList">
			<dsp:getvalueof var="correspondingSearchPageUrl" value="/search/gadgets/search_results_keyword_search.jsp" scope="request"/>
			<dsp:getvalueof var="searchPageType" param="categoryType" scope="request"/>
			<dsp:include page="/search/listing/search_listing.jsp">
			    <dsp:param name="contentItem" value="${contentItem}"/>
			    <dsp:param name="p" param="p"/>
			    <dsp:param name="viewAll" value="false"/>
			    <dsp:param name="howMany" value="${howMany}"/>
				<dsp:param name="start" value="${start}"/>
			</dsp:include>
		</dsp:oparam>
		<dsp:oparam name="categoryList">
			<dsp:getvalueof var="correspondingSearchPageUrl" param="correspondingSearchPageUrl" scope="request"/>
			<dsp:getvalueof var="templateUrl" param="templateUrl" scope="request"/>
			<dsp:include page="${templateUrl}">
				<dsp:param name="contentItem" value="${contentItem}"/>
			    <dsp:param name="p" param="p"/>
			    <dsp:param name="viewAll" value="false"/>
			    <dsp:param name="howMany" value="${howMany}"/>
				<dsp:param name="start" value="${start}"/>
			</dsp:include>
		</dsp:oparam>
	</dsp:droplet>
</dsp:page>