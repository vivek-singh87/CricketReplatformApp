<dsp:page>

	<dsp:importbean bean="/atg/commerce/ShoppingCart" />
	<dsp:importbean bean="/atg/userprofiling/Profile" />
	<dsp:importbean bean="/com/cricket/user/session/UserSessionBean" />
	<dsp:importbean
		bean="/com/cricket/commerce/order/droplet/DisplayMdnPhoneNumberDroplet" />
	<dsp:importbean
		bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean" />
	<dsp:importbean bean="/OriginatingRequest" var="originatingRequest" />
	<dsp:importbean bean="/atg/targeting/TargetingForEach" />
	<dsp:importbean
		bean="/com/cricket/commerce/order/configuration/CartConfiguration" />
	<dsp:importbean bean="/atg/cricket/util/CricketProfile" />
	<dsp:getvalueof var="orderPriceInfo" bean="/atg/commerce/ShoppingCart.last.priceInfo" />
	
	<dsp:importbean
		bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet" />
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration" />
	<dsp:importbean
		bean="/com/cricket/commerce/order/droplet/DisplayShoppingCartDroplet" />
	<dsp:importbean bean="/OriginatingRequest" var="originatingRequest" />
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach" />
	<dsp:importbean bean="/atg/dynamo/droplet/Switch" />
	<dsp:importbean bean="/atg/commerce/catalog/ProductLookup" />
	<dsp:importbean
		bean="/atg/commerce/order/purchase/CartModifierFormHandler" />

	<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath" />
	<dsp:getvalueof var="itemCount"
		bean="ShoppingCart.last.commerceItemCount" />
	<dsp:droplet name="DisplayShoppingCartDroplet">
		<dsp:param name="order" bean="ShoppingCart.last" />
		<dsp:oparam name="output">
			<dsp:getvalueof var="accessories" param="accessories" />
			<dsp:getvalueof var="packages" param="packages" />
			<dsp:getvalueof var="upgradePhone" param="upgradePhone" />
			<dsp:getvalueof var="changePlan" param="changePlan" />
			<dsp:getvalueof var="changeAddons" param="changeAddons" />
			<dsp:getvalueof var="removedAddon"  param="removedAddon"/>
			<dsp:getvalueof var="requestURI"
				bean="/OriginatingRequest.requestURI" />
		</dsp:oparam>
	</dsp:droplet>
	<dsp:droplet name="ForEach">
		<dsp:param name="array" value="${packages}" />
		<dsp:oparam name="output">
			<div class="row">
				<div class="columns large-12 small-12">
					<p class="title" data-section-title>
						<crs:outMessage key="cricket_checkout_package" />
						<!-- Package -->
						<dsp:valueof param="count" />
					</p>
				</div>
				<!-- Desktop Content-->
				<div class="row items">
					<div class="row">
						<!-- packages details -->
						<dsp:include page="/checkout/includes/display_packageLineItem.jsp">
							<dsp:param name="packages" param="element" />
						</dsp:include>
					</div>
				</div>
			</div>
		</dsp:oparam>
		<dsp:oparam name="empty">
			<!-- upgrade Phone section -->
			<c:if test="${!empty upgradePhone}">
				<dsp:droplet name="ForEach">
					<dsp:param name="array" value="${upgradePhone}" />
					<dsp:oparam name="output">
						<c:set var="upgradePhoneId">
							<dsp:valueof param="element.Id" />
						</c:set>
						<dsp:include
							page="/checkout/includes/display_upgradePhoneLineItem.jsp">
							<dsp:param name="upgradePhone" value="${upgradePhone}" />
						</dsp:include>
					</dsp:oparam>
				</dsp:droplet>

			</c:if>
			<!-- upgrade Plan section -->
			<c:if test="${!empty changePlan}">

				<dsp:droplet name="ForEach">
					<dsp:param name="array" value="${changePlan}" />
					<dsp:oparam name="output">
						<dsp:getvalueof var="count" param="count">
							<c:set var="changePlanId">
								<dsp:valueof param="element.Id" />
							</c:set>
							<c:if test="${count == 1}">								
								<dsp:include
									page="/checkout/includes/display_upgradePlanLineItem.jsp">
									<dsp:param name="changePlan" value="${changePlan}" />
									<dsp:param name="removedAddon" value="${removedAddon}"/>
								</dsp:include>
							</c:if>
						</dsp:getvalueof>
					</dsp:oparam>
				</dsp:droplet>
			</c:if>
			<!-- change Addons section -->
			<c:if test="${!empty changeAddons || (empty changePlan && !empty removedAddon)}">
			<c:choose>
				<c:when test="${!empty changeAddons }">
					<dsp:getvalueof var="addons"  value="${changeAddons}"/>
				</c:when>
				<c:otherwise>
					<dsp:getvalueof var="addons"  value="${removedAddon}"/>
				</c:otherwise>
			</c:choose>
				<dsp:droplet name="ForEach">
					<dsp:param name="array" value="${addons}" />
					<dsp:oparam name="output">
						<dsp:getvalueof var="count" param="count">
							<c:set var="changeAddonId">
								<dsp:valueof param="element.Id" />
							</c:set>
							<c:if test="${count == 1}">		
								<div class="content">
									<div class="row">
										<!-- upgrade Phone details -->
										<dsp:include
											page="/checkout/includes/display_upgradeAddonsLineItem.jsp">
											<dsp:param name="changeAddons" value="${changeAddons}" />
											<dsp:param name="removedAddon" value="${removedAddon}"/>
										</dsp:include>
									</div>
								</div>
							</c:if>
						</dsp:getvalueof>
					</dsp:oparam>
				</dsp:droplet>
			</c:if>
		</dsp:oparam>
	</dsp:droplet>
	<!-- ROW ACCESSORIES -->
	<dsp:droplet name="DimensionValueCacheDroplet">
		<dsp:param name="repositoryId"
			bean="CricketConfiguration.accessoriesCategoryId" />
		<dsp:oparam name="output">
			<dsp:getvalueof var="accessoriesCategoryCacheEntry"
				param="dimensionValueCacheEntry" />
		</dsp:oparam>
	</dsp:droplet>
	<div class="row">
		<c:choose>
			<c:when test="${accessories eq null }">
			</c:when>
			<c:otherwise>
				<div class="columns large-12 small-12">
					<p class="title" data-section-title>
						<crs:outMessage key="cricket_checkout_ACCESSORIES" />
						<!-- ACCESSORIES -->
					</p>
				</div>
				<div class="row items">
					<!-- accessories details -->
					<dsp:include
						page="/checkout/includes/display_accessoriesLineItem.jsp">
						<dsp:param name="accessoriesURL"
							value="${contextpath}${accessoriesCategoryCacheEntry.url}" />
						<dsp:param name="accessory" value="${accessories}" />
					</dsp:include>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	<!-- END ROW ACCESSORIES -->
</dsp:page>