<dsp:page>

	<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
	<dsp:importbean bean="/com/cricket/endeca/droplet/DeterminePageTypeFromNavParam"/>
	<dsp:importbean bean="/atg/multisite/Site"/>
	<dsp:importbean bean="/atg/search/droplet/GetClickThroughId"/>
	<dsp:getvalueof var="contentItem" param="contentItem"/>
	<c:set var="arraySplitSize" value="${contentItem.recsPerPage}"/>
	<dsp:getvalueof var="resultSize" value="${contentItem.totalNumRecs}"/>
	<c:set var="viewAll" value="false"/>

	<dsp:droplet name="DeterminePageTypeFromNavParam">
		<dsp:param name="navState" param="N"/>
		<dsp:oparam name="searchList">
			<dsp:include page="/search/gadgets/searchListSortPaginate.jsp">
			    <dsp:param name="contentItem" value="${contentItem}"/>
			    <dsp:param name="p" param="p"/>
			    <dsp:param name="viewAll" value="false"/>
			    <dsp:param name="howMany" value="${howMany}"/>
				<dsp:param name="start" value="${start}"/>
			</dsp:include>
		</dsp:oparam>
		<dsp:oparam name="categoryList">
			<dsp:include page="/search/gadgets/categoryListSortPaginate.jsp">
				<dsp:param name="contentItem" value="${contentItem}"/>
			    <dsp:param name="p" param="p"/>
			    <dsp:param name="viewAll" value="false"/>
			    <dsp:param name="howMany" value="${howMany}"/>
				<dsp:param name="start" value="${start}"/>
			</dsp:include>
		</dsp:oparam>
	</dsp:droplet>
	
		


</dsp:page>