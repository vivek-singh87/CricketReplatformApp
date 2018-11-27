<dsp:page>
	<dsp:getvalueof var="contentItem" param="contentItem"/>
	<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
		<%--<dsp:include page="productListRangePagination.jsp">
			<dsp:param name="top" value="true" />
            		<dsp:param name="contentItem" value="${content}"/>
		</dsp:include> --%>
		<section id="section-results" class="zipcode phone-results">	
			<!-- Results -->
			<div class="row">	
				<div class="large-12 small-12 columns large-centered small-centered">
					<c:forEach var="record" items="${contentItem.records}"  varStatus="loopStatus">
			            <dsp:getvalueof var="count" value="${loopStatus.count}"/>
						<dsp:getvalueof var="productId" value="${record.attributes['product.repositoryId']}" />
						<dsp:droplet name="ProductLookup">
							<dsp:param name="id" value="${record.attributes['product.repositoryId']}"/>
							<dsp:param name="filterBySite" value="false"/>
							<dsp:param name="filterByCatalog" value="false"/>
							<dsp:param bean="/OriginatingRequest.requestLocale.locale" name="repositoryKey"/>
							<dsp:oparam name="output">****************
								<dsp:include page="/browse/phone/listing/includes/phone_phoneDisplay.jsp">
									<dsp:param name="product" param="element"/>
									<dsp:param name="size" param="size"/>
									<dsp:param name="count" param="count"/>
								</dsp:include>
							</dsp:oparam>
						</dsp:droplet>
					</c:forEach>
					
				</div>
			</div>
		
</dsp:page>