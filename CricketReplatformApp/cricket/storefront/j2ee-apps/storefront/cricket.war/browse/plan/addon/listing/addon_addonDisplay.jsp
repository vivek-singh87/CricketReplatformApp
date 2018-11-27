<dsp:page>
	<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>	
	<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayAddToCartForAddOnDroplet"/>
	<dsp:importbean bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean"/>
	<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:getvalueof var="isUserLoggedIn" bean="CitySessionInfoObject.loggedIn"/>
	<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
	<dsp:getvalueof var="size" param="size"/>
	<dsp:getvalueof var="count" param="count"/>
	<dsp:getvalueof var="isDynamicAddOn" param="isDynamicAddOn"/>
	<dsp:getvalueof var="productId" param="product.productId"/>
	<dsp:getvalueof var="isIncluded" param="product.included"/>
	<dsp:getvalueof var="skuId" param="product.skuId"/>
	<dsp:getvalueof var="seoString" param="product.seoString"/>
	<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="OOFMarketType" bean="CartConfiguration.outOfFootPrintMarketType"></dsp:getvalueof>
	<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>
	<dsp:getvalueof var="userIntention" bean="UpgradeItemDetailsSessionBean.userIntention"/>
	<dsp:getvalueof var="addLineIntention" bean="CartConfiguration.addLineIntention"/>
	<dsp:getvalueof var="changePlanIntention" bean="CartConfiguration.upgradePlanIntention"/>
	<dsp:getvalueof var="changeAddonIntention" bean="CartConfiguration.upgradeAddonIntention"/>
	<!--This page is included in addon_results_category_request.jsp page.This div is used to display each addon item. -->
	<!-- START This droplet is used for SEO Url droplet. -->
	<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
		<dsp:param name="seoString" value="${seoString}"/>
		<dsp:param name="id" value="${productId}"/>
		<dsp:param name="itemDescriptorName" value="addOn-product"/>
		<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
		</dsp:oparam>
	</dsp:droplet>
	<!-- END This droplet is used for SEO Url droplet. -->
	<dsp:droplet name="/com/cricket/browse/droplet/IsMandatoryAddonDroplet">
		<dsp:param name="AddonId" value="${productId}" />
		<dsp:oparam name="output">
			<dsp:getvalueof var="IsMandatory" param="IsMandatory"/>
			<dsp:getvalueof var="displayAddon" value="true"/>
		 	<c:if test="${IsMandatory eq 'true' && (userIntention eq changeAddonIntention || userIntention eq changePlanIntention)}">
				<dsp:getvalueof var="displayAddon" value="false"/>
			</c:if>
		</dsp:oparam>
	</dsp:droplet>
	
	<!-- is included  ${isIncluded}<br>
	logged in  ${isUserLoggedIn}<br>
	user intention  ${userIntention}
	displayAddon ${displayAddon} -->
	<c:choose>
		<c:when test="${userIntention eq addLineIntention && isUserLoggedIn eq true}">
			<div class="plan-item">
				<h4><a href="${contextpath}${canonicalUrl}" id="${productId}"><dsp:valueof param="product.displayName"/></a></h4>
				<dsp:getvalueof var="finalPrice" param="product.finalPrice"/>
				<p class="green-price"><sup>$</sup><fmt:formatNumber type="number" value="${finalPrice}"/><sub>/mo</sub></p>
				<c:choose>
					<c:when test="${OOFMarketType eq marketType}">
						<a class="grey-add-cart button disabled secondary">Add to Cart</a>
					</c:when>
			  		<c:otherwise>
						<dsp:include page="/cart/common/addToCart.jsp">
							<dsp:param name="addToCartClass" value="green-add-cart button"/>
							<dsp:param name="productId" value="${productId}"/>
							<dsp:param name="skuId" value="${skuId}"/>
							<dsp:param name="quantity" value="1"/>
							<dsp:param name="addOnValue" value="true"/>
							<dsp:param name="isDynamicAddOn" value="${isDynamicAddOn}"/>
							<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
						</dsp:include>
					</c:otherwise>
				</c:choose>
				<a class="circle-arrow" href="${contextpath}${canonicalUrl}">Learn more</a>
			</div>
   		</c:when>
   		<c:when test="${isUserLoggedIn eq false}">
   			<div class="plan-item">
				<h4><a href="${contextpath}${canonicalUrl}" id="${productId}"><dsp:valueof param="product.displayName"/></a></h4>
				<dsp:getvalueof var="finalPrice" param="product.finalPrice"/>
				<p class="green-price"><sup>$</sup><fmt:formatNumber type="number" value="${finalPrice}"/><sub>/mo</sub></p>
				<c:choose>
					<c:when test="${OOFMarketType eq marketType}">
						<a class="grey-add-cart button disabled secondary">Add to Cart</a>
					</c:when>
			  		<c:when test="${manuallyEnteredZipCode eq true}">
						<dsp:include page="/cart/common/addToCart.jsp">
							<dsp:param name="addToCartClass" value="green-add-cart button"/>
							<dsp:param name="productId" value="${productId}"/>
							<dsp:param name="skuId" value="${skuId}"/>
							<dsp:param name="quantity" value="1"/>
							<dsp:param name="addOnValue" value="true"/>
							<dsp:param name="isDynamicAddOn" value="${isDynamicAddOn}"/>
							<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
						</dsp:include>
					</c:when>
					<c:when test="${manuallyEnteredZipCode eq false}">
						<a class="grey-add-cart button disabled secondary has-tip" data-tooltip="" title="Please enter your zip code to continue shopping.">Add to Cart</a>
					</c:when>
				</c:choose>
				<a class="circle-arrow" href="${contextpath}${canonicalUrl}">Learn more</a>
			</div>
   		</c:when>
		<c:when test="${(isIncluded == 'null' || isIncluded eq false) && userIntention ne addLineIntention && isUserLoggedIn eq true}">
			<c:if test="${displayAddon eq true}">
				<div class="plan-item">
					<h4><a href="${contextpath}${canonicalUrl}" id="${productId}"><dsp:valueof param="product.displayName"/></a></h4>
					<dsp:getvalueof var="finalPrice" param="product.finalPrice"/>
					<p class="green-price"><sup>$</sup><fmt:formatNumber type="number" value="${finalPrice}"/><sub>/mo</sub></p>
					<!-- Enable / Disable Add to cart based on the zip code entered by user -->
					<dsp:getvalueof var="manuallyEnteredZipCode" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
					<c:choose>
						<c:when test="${OOFMarketType eq marketType}">
							<a class="grey-add-cart button disabled secondary">Add to Cart</a>
						</c:when>
				  		<c:when test="${manuallyEnteredZipCode eq true}">
							<dsp:include page="/cart/common/addToCart.jsp">
								<dsp:param name="addToCartClass" value="green-add-cart button"/>
								<dsp:param name="productId" value="${productId}"/>
								<dsp:param name="skuId" value="${skuId}"/>
								<dsp:param name="quantity" value="1"/>
								<dsp:param name="addOnValue" value="true"/>
								<dsp:param name="isDynamicAddOn" value="${isDynamicAddOn}"/>
								<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
							</dsp:include>
						</c:when>
						<c:when test="${manuallyEnteredZipCode eq false}">
							<a class="grey-add-cart button disabled secondary has-tip" data-tooltip="" title="Please enter your zip code to continue shopping.">Add to Cart</a>
						</c:when>
					</c:choose>
					<!-- <a class="green-add-cart button">Add to Cart</a> -->
					<a class="circle-arrow" href="${contextpath}${canonicalUrl}">Learn more</a>
				</div>
			</c:if>
		</c:when>
		<c:when test="${isIncluded eq true && (userIntention eq changeAddonIntention || userIntention eq changePlanIntention) && isUserLoggedIn eq true}">
			<div class="plan-item">
				<h4><a href="${contextpath}${canonicalUrl}" id="${productId}"><dsp:valueof param="product.displayName"/></a></h4>
				<div class="included"></div>
			</div>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
</dsp:page>