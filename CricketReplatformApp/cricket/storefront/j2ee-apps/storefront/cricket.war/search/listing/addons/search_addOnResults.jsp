<dsp:page>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
	<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
	<dsp:importbean bean="/com/cricket/search/GetParentCatNameForAddonProduct"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
	<dsp:getvalueof var="productDetailsURL" value="/browse/plan/addon/plan_addon_details.jsp"/>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration"/>
	<dsp:getvalueof var="imageHeight" bean="ImageConfiguration.height.searchPhoneResult" />
	<dsp:getvalueof var="zipCodeKnown" bean="Profile.manuallyEnteredZipCode" vartype="java.lang.Boolean"/>
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
						
						<dsp:getvalueof var="thumbImage" value="${thumbImageList[0]}"/>
						<dsp:getvalueof var="productId" value="${productIdList[0]}"/>
						<dsp:getvalueof var="displayName" value="${displayNameList[0]}"/>
						<dsp:getvalueof var="skuId" value="${skuIdList[0]}"/>
						<dsp:getvalueof var="finalPrice" value="${finalPriceList[0]}"/>
						
						<c:if test="${empty thumbImageList || empty displayNameList || empty skuIdList || empty finalPriceList}">
							<dsp:droplet name="ProductLookup">
								<dsp:param name="id" value="${productId}"/>
								<dsp:param name="filterBySite" value="false"/>
								<dsp:param name="filterByCatalog" value="false"/>
								<dsp:param bean="/OriginatingRequest.requestLocale.locale" name="repositoryKey"/>
								<dsp:param name="elementName" value="product"/>
								<dsp:oparam name="output">
									<dsp:droplet name="GetSeoStringDroplet">
										<dsp:param name="product" param="product"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="seoString" param="seoString"/>
										</dsp:oparam>
									</dsp:droplet>
									<dsp:getvalueof var="thumbImage" param="product.thumbNailImage.url"/>
									<dsp:getvalueof var="displayName" param="product.displayName"/>
									<dsp:getvalueof var="skuId" param="product.childSkus[0].id"/>
									<dsp:getvalueof var="parentCatName" param="product.briefDescription"/>
									<c:if test="${empty parentCatName}">
										<dsp:droplet name="GetParentCatNameForAddonProduct">
											<dsp:param name="product" param="product"/>
											<dsp:oparam name="output">
												<dsp:getvalueof var="parentCatName" param="parentCatName"/>
											</dsp:oparam>
										</dsp:droplet>
									</c:if>
									<dsp:getvalueof var="retailPrice" param="product.childSKUs[0].listPrice"/>
								</dsp:oparam>
							</dsp:droplet>
						</c:if>
						<div class="columns result">
							<!-- Price logic start -->
							<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice}" var="retailPrice" />
			                <c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>
			                <c:set var="fPrice" value="${splitPrice[0]}" />
			                <c:set var="imageUrl" value="/img/search/listing/${fPrice}.png"></c:set>
							<!-- Price logic end -->
							<div class="image left">
							<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
							<dsp:param name="imageLink" value="${imageUrl}" />
							<dsp:param name="imageHeight" value="${imageHeight}"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="liquidpixelurl" param="url" />
								<img src="${liquidpixelurl}" alt="I-N-F" />
							</dsp:oparam>
							</dsp:droplet>
								
							</div>
							<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
								<dsp:param name="seoString" value="${seoString}"/>
								<dsp:param name="id" value="${productId}"/>
								<dsp:param name="itemDescriptorName" value="addOn-product"/>
								<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
								</dsp:oparam>
							</dsp:droplet>
							<div class="copy left">
								<h3>${parentCatName}</h3>
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