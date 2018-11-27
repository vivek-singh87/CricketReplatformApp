<dsp:page>
	<dsp:importbean bean="/com/cricket/search/session/UserSearchSessionInfo"/>
	<dsp:importbean bean="/com/cricket/search/CricketPaginationDroplet"/>
	<dsp:getvalueof var="recordsPerPage" value="${recordsPerPageSupport}" vartype="java.lang.Integer"/>
	<dsp:getvalueof var="totalRecords" value="${fn:length(supportResultVOs)}" vartype="java.lang.Integer"/>
	<dsp:getvalueof var="start" param="sv" vartype="java.lang.Integer"/>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<c:if test="${empty start}">
		<dsp:getvalueof var="start" value="${1}" vartype="java.lang.Integer"/>
	</c:if>
	<dsp:getvalueof var="generalSearchQuery" bean="UserSearchSessionInfo.genTextBoxSearchQuery"/>
	<dsp:getvalueof var="searchKeyWord" bean="UserSearchSessionInfo.searchQuery"/>
	<div class="row">
		<div class="columns large-12 small-12">
			<div class="search-pager right">
				<dsp:droplet name="CricketPaginationDroplet">
					<dsp:param name="totalRecords" value="${totalRecords}"/>
					<dsp:param name="recordsPerPage" value="${recordsPerPage}"/>
					<dsp:param name="start" value="${start}"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="paginationVOs" param="paginationVOs"/>
						<dsp:getvalueof var="totalPages" param="totalPages" vartype="java.lang.Integer"/>
						<dsp:getvalueof var="nextVO" param="nextVO"/>
						<dsp:getvalueof var="previousVO" param="previousVO"/>
						<c:choose>
							<c:when test="${previousVO.active eq true}">
								<c:set var="linkActionPrev" value="${contextPath}${generalSearchQuery}&Ntt=${searchKeyWord}&searchPage=searchPage&activeLink=support&sv=${previousVO.startValue + 1}" />
								<a href="${linkActionPrev}" class="button">Previous</a>
							</c:when>
							<c:otherwise>
								<a href="javascript:void(0);" class="button disabled">Previous</a>
							</c:otherwise>
						</c:choose>
						<c:forEach begin="0" end="${totalPages - 1}" varStatus="loop">
							<c:choose>	
								<c:when test="${paginationVOs[loop.index].active eq true}">
									<a class="current" href="">${loop.count}</a>
								</c:when>
								<c:otherwise>
									<c:set var="linkActionThisPage" value="${contextPath}${generalSearchQuery}&Ntt=${searchKeyWord}&searchPage=searchPage&activeLink=support&sv=${paginationVOs[loop.index].startValue + 1}" />
						    		<a href="${linkActionThisPage}">${loop.count}</a>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<c:choose>
							<c:when test="${nextVO.active eq true}">
								<c:set var="linkActionNext" value="${contextPath}${generalSearchQuery}&Ntt=${searchKeyWord}&searchPage=searchPage&activeLink=support&sv=${nextVO.startValue + 1}" />
								<a href="${linkActionNext}" class="button">Next</a>
							</c:when>
							<c:otherwise>
								<a href="javascript:void(0);" class="button disabled">Next</a>
							</c:otherwise>
						</c:choose>
					</dsp:oparam>
				</dsp:droplet>
			</div>
		</div>
	</div>


	<!-- <div class="row">
		<div class="columns large-12 small-12">
			<div class="search-pager right">
				<a class="button previous">Previous</a>
				<a class="current">1</a>
				<a href="#">2</a>
				<a href="#">3</a>
				<a href="#">4</a>
				...
				<a href="#">10</a>
				<a href="#">11</a>
				<a class="button next">Next</a>
			</div>
		</div>
	</div> -->
</dsp:page>