<dsp:page>
	<dsp:getvalueof var="contentItem" param="contentItem"/>
	<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
	<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>
	<dsp:importbean bean="/atg/store/sort/CricketStoreSortDroplet" />
	<dsp:getvalueof var="sortParam" param="Ns"/>
	<c:choose>
		<c:when test="${sortParam eq 'sku.listPrice|0'}">
			<dsp:getvalueof var="sortSelection" value="price:ascending"/>
		</c:when>
	</c:choose>
	<section id="section-results" class="zipcode phone-results">	
		<!-- Results -->
		<div class="row">	
			<div class="large-12 small-12 columns large-centered small-centered">
				<c:choose>
					<c:when test="${fn:length(contentItem.records) eq 0}">
						<p class="no-results">No products currently match the filters you have chosen. Please clear some filters to broaden your search.</p>
					</c:when>
					<c:otherwise>
						<dsp:droplet name="CricketStoreSortDroplet">
							<dsp:param name="array" value="${contentItem.records}"/>
					        <dsp:param name="sortSelection" value="${sortSelection}"/>
					        <dsp:param name="elementName" value="record"/>
					        <dsp:param name="howMany" value="10000"/>
					        <dsp:oparam name="output">
					        	<dsp:getvalueof var="record" param="record"/>
					        	<dsp:getvalueof var="productId" value="${record.attributes['product.repositoryId']}" />
								<dsp:getvalueof var="seoStringVals" value="${record.attributes['product.seoString']}"/>
								<dsp:getvalueof var="seoString" value="${seoStringVals[0]}"/>
					        	<dsp:droplet name="ProductLookup">
									<dsp:param name="id" value="${record.attributes['product.repositoryId']}"/>
									<dsp:param name="filterBySite" value="false"/>
									<dsp:param name="filterByCatalog" value="false"/>
									<dsp:param bean="/OriginatingRequest.requestLocale.locale" name="repositoryKey"/>
									<dsp:param name="elementName" value="product"/>
									<dsp:oparam name="output">
										<c:if test="${empty seoString}">
											<dsp:droplet name="GetSeoStringDroplet">
												<dsp:param name="product" param="product"/>
												<dsp:oparam name="output">
													<dsp:getvalueof var="seoString" param="seoString"/>
												</dsp:oparam>
											</dsp:droplet>
										</c:if>
										<dsp:include page="/browse/phone/listing/includes/phone_phoneDisplay.jsp">
											<dsp:param name="product" param="product"/>
											<dsp:param name="size" param="size"/>
											<dsp:param name="count" param="count"/>
											<dsp:param name="seoString" value="${seoString}"/>
											<dsp:param name="throughSearch" value="${true}"/>
										</dsp:include>
									</dsp:oparam>
								</dsp:droplet>
					        </dsp:oparam>
						</dsp:droplet>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</section>
</dsp:page>