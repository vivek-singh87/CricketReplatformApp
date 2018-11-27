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
							<dsp:oparam name="output">
								<dsp:include page="/browse/accessories/listing/includes/accessory_accessoryDisplay.jsp">
									<dsp:param name="product" param="element"/>
									<dsp:param name="size" param="size"/>
									<dsp:param name="count" param="count"/>
									<dsp:param name="throughSearch" value="${true}"/>
								</dsp:include>
							</dsp:oparam>
						</dsp:droplet>
					</c:forEach>
					
				</div>
			</div>
			
			<%-- <div class="row show-for-small">
			<div class="columns small-12 text-center three-callouts">    
				<a class="prev show-for-small" href="#">Prev</a>
				<a class="next show-for-small" href="#">Next</a>
				<div class="swiper-container show-for-small">	
        			<div class="swiper-wrapper">
        				<c:forEach var="record" items="${contentItem.records}"  varStatus="loopStatus">
				            <dsp:getvalueof var="count" value="${loopStatus.count}"/>
							<dsp:getvalueof var="productId" value="${record.attributes['product.repositoryId']}" />
							<dsp:droplet name="ProductLookup">
								<dsp:param name="id" value="${record.attributes['product.repositoryId']}"/>
								<dsp:param name="filterBySite" value="false"/>
								<dsp:param name="filterByCatalog" value="false"/>
								<dsp:param bean="/OriginatingRequest.requestLocale.locale" name="repositoryKey"/>
								<dsp:oparam name="output">
									<dsp:include page="/browse/accessories/listing/includes/accessory_accessoryDisplay_mobile.jsp">
										<dsp:param name="product" param="element"/>
										<dsp:param name="size" param="size"/>
										<dsp:param name="count" param="count"/>
										<dsp:param name="throughSearch" value="${true}"/>
									</dsp:include>
								</dsp:oparam>
							</dsp:droplet>
						</c:forEach>
        			</div>
        		</div>	
			</div>
		</div> --%>
		
</dsp:page>