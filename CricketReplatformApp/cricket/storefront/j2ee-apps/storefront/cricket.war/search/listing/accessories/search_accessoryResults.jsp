<dsp:page> 
	<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<dsp:getvalueof var="productDetailsURL" value="/browse/accessories/details/accessories_details.jsp"/>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration"/>
	<dsp:getvalueof var="imageHeight" bean="ImageConfiguration.height.searchPhoneResult" />
	<dsp:getvalueof var="zipCodeKnown" bean="CitySessionInfoObject.cityVO.manulaEntry" vartype="java.lang.Boolean"/>
	<div id="search-phone-results">
		<dsp:droplet name="ForEach">
			<dsp:param name="array" value="${contentItem.records}"/>
			<dsp:param name="elementName" value="phoneRecord"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="phoneRecord" param="phoneRecord"/>
				<dsp:getvalueof var="count" param="count"/>
				<dsp:getvalueof var="size" param="size"/>
				<c:if test="${count eq 1 || (count mod 3 eq 1)}">
					<div class="row search-section">
				</c:if>
					<!-- PhoneVO display starts from here -->
						<dsp:getvalueof var="thumbImageList" value="${phoneRecord.attributes['product.fullImage.url']}"/>
						<dsp:getvalueof var="productIdList" value="${phoneRecord.attributes['product.repositoryId']}"/>
						<dsp:getvalueof var="displayNameList" value="${phoneRecord.attributes['product.displayName']}"/>
						<dsp:getvalueof var="skuIdList" value="${phoneRecord.attributes['sku.repositoryId']}"/>
						<dsp:getvalueof var="finalPriceList" value="${phoneRecord.attributes['sku.activePrice']}"/>
						
						<dsp:getvalueof var="thumbImage" value="${thumbImageList[0]}"/>
						<dsp:getvalueof var="productId" value="${productIdList[0]}"/>
						<dsp:getvalueof var="displayName" value="${displayNameList[0]}"/>
						<dsp:getvalueof var="skuId" value="${skuIdList[0]}"/>
						<dsp:getvalueof var="finalPrice" value="${finalPriceList[0]}"/>
						
						<c:if test="${empty thumbImageList || empty displayNameList || empty skuIdList || empty finalPriceList}">
							<dsp:droplet name="ProductLookup">
								<dsp:param name="id" value="${phoneRecord.attributes['product.repositoryId']}"/>
								<dsp:param name="filterBySite" value="false"/>
								<dsp:param name="filterByCatalog" value="false"/>
								<dsp:param bean="/OriginatingRequest.requestLocale.locale" name="repositoryKey"/>
								<dsp:param name="elementName" value="product"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="thumbImage" param="product.fullImage.url"/>
									<dsp:getvalueof var="displayName" param="product.displayName"/>
									<dsp:getvalueof var="skuId" param="product.childSkus[0].id"/>
									<dsp:include page="/browse/includes/discountTag_price.jsp">
										<dsp:param name="productId" value="${productId}"/>
										<dsp:param name="skuId" value="${skuId}" />
									</dsp:include>
									<dsp:getvalueof var="finalPrice" value="${finalPrice}"/>
								</dsp:oparam>
							</dsp:droplet>
						</c:if>
						<div class="columns result">
							<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
								<dsp:param name="imageLink" value="${thumbImage}" />
								<dsp:param name="imageHeight" value="${imageHeight}"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="lpImageUrl" param="url"/>
								</dsp:oparam>
						 	</dsp:droplet>
							<div class="image left">
								<img src="${lpImageUrl}" alt="I-N-F"/>
							</div>
							<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
								<dsp:param name="id" value="${productId}"/>
								<dsp:param name="itemDescriptorName" value="accessory-product"/>
								<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
								</dsp:oparam>
							</dsp:droplet>
							<div class="copy left">
								<h4>
									<a href="${contextPath}${canonicalUrl}">
										<dsp:getvalueof var="displayName" value="${displayName}"/>									  					
										<dsp:droplet name="/com/cricket/browse/droplet/DisplayNameDroplet">
											<dsp:param name="displayName" value="${displayName}" />					
											<dsp:oparam name="output">
												<dsp:getvalueof var="productDisplayName" param="displayName"/>				
												<dsp:getvalueof var="hasSpecialSymbol" param="hasSpecialSymbol"/>
												<dsp:getvalueof var="firstString" param="firstString"/>
												<dsp:getvalueof var="secondString" param="secondString"/>
												<dsp:getvalueof var="specialSymbol" param="specialSymbol"/>
												<c:choose>
													<c:when test="${hasSpecialSymbol eq true}">	
														${firstString}<sup>${specialSymbol}</sup>${secondString}							
													</c:when>	
													<c:otherwise>
														${productDisplayName}
													</c:otherwise>
												</c:choose>
											</dsp:oparam>
										</dsp:droplet>
									</a>
								</h4>
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${finalPrice}" var="fPrice" />
								<c:set var="splitPrice" value="${fn:split(fPrice, '.')}"/>
								<p class="green-price"><sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup></p>
								<a href="${contextPath}${canonicalUrl}" class="button orange-shop-now">Shop Now</a>
								<a href="${contextPath}${canonicalUrl}" class="circle-arrow">Learn More</a>
							</div>
						</div>
					<!-- PhoneVO display ends here -->
				<c:choose>
					<c:when test="${count ne size && (count mod 3 eq 0)}">
						</div>
						<hr class="hide-for-small" />
					</c:when>
					<c:when test="${count eq size && (count mod 3 eq 0)}">
						</div>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</dsp:oparam>
		</dsp:droplet>
	</div>
	<!-- Pager Include -->
	<dsp:include page="/search/listing/common/search_pagination.jsp">
		<dsp:param name="contentItem" value="${contentItem}"/>
	</dsp:include>
</dsp:page>