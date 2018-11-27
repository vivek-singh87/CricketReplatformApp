<dsp:page>
	<dsp:importbean bean="/com/cricket/search/CricketPaginationDroplet"/>
	<c:set var="totalRecords" value="${contentItem.totalNumRecs}" />
	<c:set var="recordsPerPage" value="${contentItem.recsPerPage}"/>
	<c:set var="start" value="${contentItem.firstRecNum}"/>
	<c:if test="${totalRecords > recordsPerPage}">
		<div class="row">
			<div class="columns large-12 small-12">
				<div class="search-pager right">
					<c:set var="pageTemplate" value="${contentItem.pagingActionTemplate.navigationState}"/>
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
									<c:set var="linkActionPrev" value="${fn:replace(fn:replace(pageTemplate, '%7Boffset%7D', previousVO.startValue), '%7BrecordsPerPage%7D',  recordsPerPage)}" />
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
										<c:set var="linkActionThisPage" value="${fn:replace(fn:replace(pageTemplate, '%7Boffset%7D', paginationVOs[loop.index].startValue), '%7BrecordsPerPage%7D',  recordsPerPage)}" />
							    		<a href="${linkActionThisPage}">${loop.count}</a>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
								<c:when test="${nextVO.active eq true}">
									<c:set var="linkActionNext" value="${fn:replace(fn:replace(pageTemplate, '%7Boffset%7D', nextVO.startValue), '%7BrecordsPerPage%7D',  recordsPerPage)}" />
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
	</c:if>
</dsp:page>