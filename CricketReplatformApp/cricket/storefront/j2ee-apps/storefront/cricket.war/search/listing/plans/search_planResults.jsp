<dsp:page> 
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
	<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<dsp:getvalueof var="productDetailsURL" value="/browse/plan/detail/planDetails.jsp"/>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration"/>
	<dsp:getvalueof var="imageHeight" bean="ImageConfiguration.height.searchPhoneResult" />
	<dsp:getvalueof var="zipCodeKnown" bean="CitySessionInfoObject.cityVO.manulaEntry" vartype="java.lang.Boolean"/>
	<div id="search-plan-results">
		<dsp:droplet name="ForEach">
			<dsp:param name="array" value="${contentItem.records}"/>
			<dsp:param name="elementName" value="phoneRecord"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="phoneRecord" param="phoneRecord"/>
				<dsp:getvalueof var="count" param="count"/>
				<dsp:getvalueof var="size" param="size"/>
				<c:if test="${count eq 1 || (count mod 3 eq 1)}">
					<div class="row search-section plans">
				</c:if>
					<!-- PhoneVO display starts from here -->
						<dsp:getvalueof var="thumbImageList" value="${phoneRecord.attributes['product.thumbNailImage.url']}"/>
						<dsp:getvalueof var="productIdList" value="${phoneRecord.attributes['product.repositoryId']}"/>
						<dsp:getvalueof var="displayNameList" value="${phoneRecord.attributes['product.displayName']}"/>
						<dsp:getvalueof var="skuIdList" value="${phoneRecord.attributes['sku.repositoryId']}"/>
						<dsp:getvalueof var="finalPriceList" value="${phoneRecord.attributes['sku.activePrice']}"/>
						<dsp:getvalueof var="ratePlanTypeList" value="${phoneRecord.attributes['product.ratePlanType']}"/>
						
						<dsp:getvalueof var="thumbImage" value="${thumbImageList[0]}"/>
						<dsp:getvalueof var="productId" value="${productIdList[0]}"/>
						<dsp:getvalueof var="displayName" value="${displayNameList[0]}"/>
						<dsp:getvalueof var="skuId" value="${skuIdList[0]}"/>
						<dsp:getvalueof var="finalPrice" value="${finalPriceList[0]}"/>
						<dsp:getvalueof var="ratePlanType" value="${ratePlanTypeList[0]}"/>
						
						<c:if test="${empty thumbImageList || empty displayNameList || empty skuIdList || empty finalPriceList || empty ratePlanTypeList}">
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
									<dsp:getvalueof var="ratePlanType" param="product.ratePlanType"/>
									<dsp:include page="/browse/includes/priceLookup.jsp">
						           		<dsp:param name="productId" value="${productId}"/>
										<dsp:param name="skuId" value="${skuId}" />
									</dsp:include>
									<dsp:getvalueof var="finalPrice" value="${retailPrice}"/>
								</dsp:oparam>
							</dsp:droplet>
						</c:if>
						<c:choose>
							<c:when test="${ratePlanType eq 'D'}">
								<dsp:getvalueof var="typeDisplayText" value="Smart Plan"/>
							</c:when>
							<c:otherwise>
								<dsp:getvalueof var="typeDisplayText" value="Feature Plan"/>
							</c:otherwise>
						</c:choose>
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
								<dsp:param name="itemDescriptorName" value="plan-product"/>
								<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
								</dsp:oparam>
							</dsp:droplet>
							<div class="copy left">
								<h4><a href="${contextPath}${canonicalUrl}">${typeDisplayText}</a></h4>
								<c:if test="${zipCodeKnown}">
									<a class="green-add-cart button">Add to Cart</a>
								</c:if>
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