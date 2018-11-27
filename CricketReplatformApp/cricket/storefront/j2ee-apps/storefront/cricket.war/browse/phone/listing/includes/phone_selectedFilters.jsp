<style>
	.filter-bar2 a {background:transparent url(../img/icon-red-circle-x.jpg) no-repeat 1px 50%;float:left;color:#e53924;padding:.6em 1em .6em 1.6em;text-transform:uppercase;font-size:.8em}
</style>
<dsp:page>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<div class="row" id="selectedFilters">
		<div class="large-12 small-12 columns filter-bar2">
			<c:forEach var="dimCrumb" items="${dimCrumbs}" varStatus="countCrumb">
				<c:if test="${dimCrumb.dimensionName ne 'product.category' && dimCrumb.dimensionName ne 'product.type'}">
					<dsp:getvalueof var="navAction" value="${dimCrumb.removeAction}"/>
					<dsp:a href="javascript:hitEndecaWithFilterQuery('${contextpath}${navAction.contentPath}${navAction.navigationState}')">${dimCrumb.label}</dsp:a>
				</c:if>
			</c:forEach>
			<c:forEach var="rangeCrumb" items="${rangeCrumbs}" varStatus="countCrumb">
				<c:if test="${rangeCrumb.name eq 'sku.listPrice'}">
					<dsp:getvalueof var="navActionRange" value="${rangeCrumb.removeAction}"/>
					<dsp:a href="javascript:hitEndecaWithFilterQuery('${contextpath}${navActionRange.contentPath}${navActionRange.navigationState}')">
						<c:choose>
							<c:when test="${rangeCrumb.upperBound eq 0.0}">
								<fmt:formatNumber value="${rangeCrumb.lowerBound}" maxFractionDigits="0"/> +
							</c:when>
							<c:otherwise>
								<fmt:formatNumber value="${rangeCrumb.lowerBound}" maxFractionDigits="0"/> - <fmt:formatNumber value="${rangeCrumb.upperBound}" maxFractionDigits="0"/>
							</c:otherwise>
						</c:choose>
					</dsp:a>
				</c:if>
			</c:forEach>
		</div>
	</div>
</dsp:page>