<dsp:page>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:importbean bean="/com/cricket/search/session/UserSearchSessionInfo"/>
	<dsp:getvalueof var="searchPage" param="searchPage"/>
	<dsp:getvalueof var="content" param="content"/>
	<dsp:getvalueof var="activeLink" param="activeLink"/>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="productTypeDimPrsnt" value="no"/>
	<dsp:getvalueof var="generalSearchQuery" bean="UserSearchSessionInfo.genTextBoxSearchQuery" scope="request"/>
	<dsp:getvalueof var="Ntt" param="Ntt" vartype="java.lang.String"/>
	<c:choose>
		<c:when test="${searchPage ne 'searchPage' && activeLink ne 'All-Results'}">
			<dd class="active"><a href="">All Results (${totalNumRecs})</a></dd>
			<c:forEach var="dimension" items="${content}">
				<c:if test="${dimension.displayName eq 'product.type'}">
					<dsp:getvalueof var="productTypeDimPrsnt" value="yes"/>
					<dsp:getvalueof var="categoryTypeRefinements" value="${dimension.refinements}" scope="request"/>
					<dsp:droplet name="ForEach">
						<dsp:param name="array" bean="UserSearchSessionInfo.keyOrder"/>
						<dsp:param name="elementName" value="currentKey"/>
						<dsp:oparam name="output">
							<dsp:getvalueof var="currentKey" param="currentKey"/>
							<c:forEach var="refinement" items="${dimension.refinements}">
								<dsp:getvalueof var="originalRefinementState" value="${refinement.navigationState}"/>
								<dsp:getvalueof var="editedRefinementState" value="${fn:replace(originalRefinementState,'&Nrpp=10000','')}"/>
								<c:choose>
									<c:when test="${currentKey eq 'Phones' && refinement.label eq 'phone-product'}">
										<dd>
											<dsp:getvalueof var="allPhonesLink" value="${refinement.navigationState}&searchPage=searchPage&activeLink=${currentKey}" scope="request"/>
											<a href="${editedRefinementState}&searchPage=searchPage&activeLink=${currentKey}">
												Phones (${refinement.count})
											</a>
										</dd>
									</c:when>
									<c:when test="${currentKey eq 'Accessories' && refinement.label eq 'accessory-product'}">
										<dd>
											<dsp:getvalueof var="allAccessoriesLink" value="${refinement.navigationState}&searchPage=searchPage&activeLink=${currentKey}" scope="request"/>
											<a href="${editedRefinementState}&searchPage=searchPage&activeLink=${currentKey}">
												Accessories (${refinement.count})
											</a>
										</dd>
									</c:when>
									<c:when test="${currentKey eq 'Plans' && refinement.label eq 'plan-product'}">
										<dd>
											<dsp:getvalueof var="allPlansLink" value="${refinement.navigationState}&searchPage=searchPage&activeLink=${currentKey}" scope="request"/>
											<a href="${editedRefinementState}&searchPage=searchPage&activeLink=${currentKey}">
												Plans (${refinement.count})
											</a>
										</dd>
									</c:when>
									<c:when test="${currentKey eq 'AddOns' && refinement.label eq 'addOn-product'}">
										<dd>
											<dsp:getvalueof var="allAddonsLink" value="${refinement.navigationState}&searchPage=searchPage&activeLink=${currentKey}" scope="request"/>
											<a href="${editedRefinementState}&searchPage=searchPage&activeLink=${currentKey}">
												Plan Add-ons (${refinement.count})
											</a>
										</dd>
									</c:when>
								</c:choose>
							</c:forEach>
						</dsp:oparam>
					</dsp:droplet>
				</c:if>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<dsp:getvalueof var="catLinkMap" bean="UserSearchSessionInfo.catLinkMap"/>
			<dsp:getvalueof var="allResultsVO" value="${catLinkMap['All-Results']}"/>
			<c:if test="${activeLink eq 'All-Results'}">
				<dsp:getvalueof var="activeClass" value="active"/>
			</c:if>
			<dd class="${activeClass}">
				<a href="${contextPath}${allResultsVO.categoryNavLink}&Nrpp=10000&activeLink=All-Results">
					All Results (${allResultsVO.categoryCount})
				</a>
			</dd>
			<dsp:droplet name="ForEach">
				<dsp:param name="array" bean="UserSearchSessionInfo.keyOrder"/>
				<dsp:param name="elementName" value="currentKey2"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="currentKey2" param="currentKey2"/>
					<dsp:getvalueof var="resultsVO" value="${catLinkMap[currentKey2]}"/>
					<c:choose>
						<c:when test="${empty resultsVO || resultsVO.categoryCount eq 0}">
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${currentKey2 eq 'Phones'}">
									<dsp:getvalueof var="productTypeDimPrsnt" value="yes"/>
									<dsp:getvalueof var="allPhonesLink" value="${resultsVO.categoryNavLink}&activeLink=${currentKey2}" scope="request"/>
									<dsp:getvalueof var="labelDisplay" value="Phones"/>
								</c:when>
								<c:when test="${currentKey2 eq 'Accessories'}">
									<dsp:getvalueof var="productTypeDimPrsnt" value="yes"/>
									<dsp:getvalueof var="allAccessoriesLink" value="${resultsVO.categoryNavLink}&activeLink=${currentKey2}" scope="request"/>
									<dsp:getvalueof var="labelDisplay" value="Accessories"/>
								</c:when>
								<c:when test="${currentKey2 eq 'Plans'}">
									<dsp:getvalueof var="productTypeDimPrsnt" value="yes"/>
									<dsp:getvalueof var="allPlansLink" value="${resultsVO.categoryNavLink}&activeLink=${currentKey2}" scope="request"/>
									<dsp:getvalueof var="labelDisplay" value="Plans"/>
								</c:when>
								<c:when test="${currentKey2 eq 'AddOns'}">
									<dsp:getvalueof var="productTypeDimPrsnt" value="yes"/>
									<dsp:getvalueof var="allAddonsLink" value="${resultsVO.categoryNavLink}&activeLink=${currentKey2}" scope="request"/>
									<dsp:getvalueof var="labelDisplay" value="Plan Add-ons"/>
								</c:when>
								<c:when test="${currentKey2 eq 'Support'}">
									<dsp:getvalueof var="labelDisplay" value="Support"/>
								</c:when>
								<c:otherwise>
									<dsp:getvalueof var="labelDisplay" value="Phones"/>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${activeLink eq currentKey2}">
									<dd class="active">
										<a href="">
											${labelDisplay} (${resultsVO.categoryCount})
										</a>
									</dd>
								</c:when>
								<c:otherwise>
									<dd>
										<a href="${resultsVO.categoryNavLink}&activeLink=${currentKey2}">
											${labelDisplay} (${resultsVO.categoryCount})
										</a>
									</dd>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</dsp:oparam>
			</dsp:droplet>
		</c:otherwise>
	</c:choose>
	<c:if test="${productTypeDimPrsnt eq 'no' && not empty productTypeSingleDimension}">
		<dsp:getvalueof var="activeLinkNoDim" param="activeLink"/>
			<c:choose>
				<c:when test="${productTypeSingleDimension eq 'Phones'}">
					<c:choose>
						<c:when test="${activeLinkNoDim eq 'Phones'}">
							<dd class="active">
								<a href="">
									Phones (${totalNumRecsPC})
								</a>
							</dd>
						</c:when>
						<c:otherwise>
							<dd>
								<dsp:getvalueof var="allPhonesLink" value="${contextPath}${generalSearchQuery}&Ntt=${Ntt}&searchPage=searchPage&activeLink=Phones" scope="request"/>
								<a href="${contextPath}${generalSearchQuery}&Ntt=${Ntt}&searchPage=searchPage&activeLink=Phones">
									Phones (${totalNumRecsPC})
								</a>
							</dd>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:when test="${productTypeSingleDimension eq 'Plans'}">
					<c:choose>
						<c:when test="${activeLinkNoDim eq 'Plans'}">
							<dd class="active">
								<a href="">
									Plans (${totalNumRecsPC})
								</a>
							</dd>
						</c:when>
						<c:otherwise>
							<dd>
								<dsp:getvalueof var="allPlansLink" value="${contextPath}${generalSearchQuery}&Ntt=${Ntt}&searchPage=searchPage&activeLink=Plans" scope="request"/>
								<a href="${contextPath}${generalSearchQuery}&Ntt=${Ntt}&searchPage=searchPage&activeLink=Plans">
									Plans (${totalNumRecsPC})
								</a>
							</dd>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:when test="${productTypeSingleDimension eq 'AddOns'}">
					<c:choose>
						<c:when test="${activeLinkNoDim eq 'AddOns'}">
							<dd class="active">
								<a href="">
									Plan Add-ons (${totalNumRecsPC})
								</a>
							</dd>
						</c:when>
						<c:otherwise>
							<dd>
								<dsp:getvalueof var="allAddonsLink" value="${contextPath}${generalSearchQuery}&Ntt=${Ntt}&searchPage=searchPage&activeLink=AddOns" scope="request"/>
								<a href="${contextPath}${generalSearchQuery}&Ntt=${Ntt}&searchPage=searchPage&activeLink=AddOns">
									Plan Add-ons (${totalNumRecsPC})
								</a>
							</dd>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:when test="${productTypeSingleDimension eq 'Accessories'}">
					<c:choose>
						<c:when test="${activeLinkNoDim eq 'Accessories'}">
							<dd class="active">
								<a href="">
									Accessories (${totalNumRecsPC})
								</a>
							</dd>
						</c:when>
						<c:otherwise>
							<dd>
								<dsp:getvalueof var="allAccessoriesLink" value="${contextPath}${generalSearchQuery}&Ntt=${Ntt}&searchPage=searchPage&activeLink=Accessories" scope="request"/>
								<a href="${contextPath}${generalSearchQuery}&Ntt=${Ntt}&searchPage=searchPage&activeLink=Accessories">
									Accessories (${totalNumRecsPC})
								</a>
							</dd>
						</c:otherwise>
					</c:choose>
				</c:when>
			</c:choose>
	</c:if>
	
	
	<dsp:getvalueof var="activeLink4" param="activeLink"/>
	<c:if test="${fn:length(supportResultVOs) gt 0}">
	<c:if test="${activeLink4 eq 'support'}">
		<dsp:getvalueof var="supportActiveClass" value="active"/>
	</c:if>
	<dd class="${supportActiveClass}">
		<c:choose>
			<c:when test="${empty Ntt}">
				<dsp:getvalueof var="Ntt" bean="UserSearchSessionInfo.searchQuery"/>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${activeLink4 eq 'support'}">
				<a href="#">
					Support (${fn:length(supportResultVOs)})
				</a>
			</c:when>
			<c:otherwise>
				<a href="${contextPath}${generalSearchQuery}&Ntt=${Ntt}&searchPage=searchPage&activeLink=support">
					Support (${fn:length(supportResultVOs)})
				</a>
			</c:otherwise>
		</c:choose>
		
	</dd>
	</c:if>
</dsp:page>