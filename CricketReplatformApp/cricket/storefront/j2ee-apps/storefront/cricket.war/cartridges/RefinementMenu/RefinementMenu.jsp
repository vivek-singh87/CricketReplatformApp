
<dsp:page>
  <dsp:importbean bean="/com/cricket/endeca/droplet/CreateDynamicPriceRanges"/>
  <dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
  <dsp:importbean bean="/com/cricket/endeca/configuration/EndecaConfiguration"/>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
  <dsp:getvalueof var="dimensionLabels" vartype="java.util.Map"  bean="EndecaConfiguration.dimensionLabels"/>
  <dsp:getvalueof var="activeLink" param="activeLink"/>
  
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
  <dsp:getvalueof var="dimensionName" value="${contentItem.dimensionName}"/>
  <dsp:getvalueof var="dimensionLabel" value="${contentItem.name}"/>
  <c:choose>
  	<c:when test="${countDims eq 1 || CATEGORY_TYPE eq 'ACCESSORY'}">
  		<dsp:getvalueof var="currentClass" value="current"/>
  	</c:when>
  	<c:otherwise>
  		<dsp:getvalueof var="currentClass" value=""/>
  	</c:otherwise>
  </c:choose>
  
	<fieldset class="${currentClass}">
		<dsp:getvalueof var="countOut" value="${countOut + 1}" scope="request" vartype="java.lang.Integer"/>
		<c:choose>
			<c:when test="${dimensionName eq 'sku.listPrice' && CATEGORY_TYPE eq 'ACCESSORY'}">
			</c:when>
			<c:when test="${dimensionName eq 'product.manufacturer.displayName' && CATEGORY_TYPE eq 'ACCESSORY'}">
			</c:when>
			<c:when test="${dimensionName eq 'sku.listPrice' && activeLink eq 'Accessories'}">
			</c:when>
			<c:when test="${dimensionName eq 'product.manufacturer.displayName' && activeLink eq 'Accessories'}">
			</c:when>
			<c:when test="${dimensionName eq 'product.type'}">
			</c:when>
			<c:when test="${dimensionName eq 'sku.listPrice'}">
				<dsp:droplet name="CreateDynamicPriceRanges">
					<dsp:param name="refinements" value="${contentItem.refinements}"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="priceRangeMap" param="priceVOMap"/>
						<dsp:getvalueof var="navStateNoPrice" param="navStateNoPrice"/>
						<dsp:droplet name="ForEach">
							<dsp:getvalueof var="sizePrice" param="size"/>
							<dsp:getvalueof var="countPrice" param="count"/>
							<dsp:oparam name="outputStart">
								<legend class="mobile-trigger">
									<c:out value="${dimensionLabel}"/>
								</legend>
								<div class="label-wrapper" id="filterSectionDivId">
							</dsp:oparam>
							<dsp:param name="array" param="keysList"/>
							<dsp:param name="elementName" value="key"/>
							<dsp:oparam name="output">
							<dsp:getvalueof var="key" param="key"/>
								<dsp:getvalueof var="count" param="count"/>
								<dsp:getvalueof var="priceRangeVO" value="${priceRangeMap[key]}"/>
								<label for="${contentItem.dimensionName}${count}" onclick="javascript:prepareEndecaMultiselectFilterQueryPriceRange('${priceRangeVO.navLink}','${navStateNoPrice}');">
									<input type="checkbox" id="${contentItem.dimensionName}${count}" style="display: none;">
									<span class="custom checkbox"></span>
									<%-- <dsp:getvalueof var="priceRangeArray" value="${fn:split(priceRangeVO.rangeDisplay, '-')}"/>
									<c:choose>
										<c:when test="${empty priceRangeArray[1]}">
											$${priceRangeArray[0]}
										</c:when>
										<c:otherwise>
											$${priceRangeArray[0]} - $${priceRangeArray[1]}
										</c:otherwise>
									</c:choose> --%>
									${priceRangeVO.rangeDisplay}
								</label>
							</dsp:oparam>
							<dsp:oparam name="outputEnd">
								</div>
							</dsp:oparam>
						</dsp:droplet>
					</dsp:oparam>
				</dsp:droplet>
			</c:when>
			<c:otherwise>
				<legend class="mobile-trigger">
					<c:out value="${dimensionLabel}"/>
				</legend>
				<div class="label-wrapper" id="filterSectionDivId">
					<c:forEach var="refinement" items="${contentItem.refinements}" varStatus="countIn">
						<label for="${contentItem.dimensionName}${countIn.count}" onclick="javascript:prepareEndecaMultiselectFilterQuery('${refinement.navigationState}');">
							<input type="checkbox" id="${contentItem.dimensionName}${countIn.count}" value="${refinement.navigationState}" style="display: none;">
							<span class="custom checkbox"></span>
							<c:out value="${refinement.label}"/>
						</label>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
	</fieldset>
	
					               
	<%--<c:if test="${not empty(contentItem.refinements)}" >
		<c:forEach var="refinement" items="${contentItem.refinements}">
			 <br><dsp:a href="javascript:hitEndecaWithFilterQuery('${contextPath}${refinement.contentPath}${refinement.navigationState}')" title="${refinement.label}">
                <c:out value="${refinement.label}"/> (<c:out value="${refinement.count}"/>)
              </dsp:a>
		</c:forEach>
	</c:if> --%>
</dsp:page>

