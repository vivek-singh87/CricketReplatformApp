<dsp:page>
	<dsp:importbean bean="/atg/commerce/catalog/ProductLookup" />
	<dsp:importbean
		bean="/com/cricket/commerce/order/droplet/DisplayNonPackageAddonItemInCart" />
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach" />
	<dsp:importbean bean="/atg/commerce/ShoppingCart" />
	<dsp:getvalueof var="changePlanList" param="changePlan" />
	<c:set scope="request" var="totalUpgradePlanAmt" value="0" />
	<div class="large-4 small-12 columns summary-container">
		<hr class="show-for-small" />
		<table class="summary">
			<thead>
				<tr>
					<th></th>
					<th><crs:outMessage key="cricket_shoppingcart_monthly_service" />
						<!-- Monthly Service --></th>
					<th><crs:outMessage key="cricket_shoppingcart_due_today"/><!-- Due Today --></th>
				</tr>
			</thead>
			<!--NOTE: Just separate items in the table with TBODY; CSS will apply dotted bottom border -->
			<tbody>
				<tr>
					<th colspan="3" class="heading"><crs:outMessage key="cricket_shoppingcart_plan"/><!-- Plan --></th>
				</tr>
				<c:if test="${!empty changePlanList }">
					<c:forEach var="changePlan" items="${changePlanList}">
						<tr>
							<dsp:droplet name="ProductLookup">
								<dsp:param name="id"
									value="${changePlan.auxiliaryData.productId}" />
								<dsp:param name="filterBySite" value="false" />
								<dsp:param name="filterByCatalog" value="false" />
								<dsp:oparam name="output">
									<th><dsp:valueof param='element.displayName' /></th>
								</dsp:oparam>
							</dsp:droplet>
							<c:if test="${!empty changePlan.priceInfo.adjustments}">
								<c:forEach var="adjustment"
									items="${changePlan.priceInfo.adjustments}"
									varStatus="countValue">
									<c:if test="${countValue.count eq 1}">
										<c:set var="upgradePlanValue" scope="request"
											value="${upgradePlanValue + adjustment.totalAdjustment}" />
										<td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${adjustment.totalAdjustment}"/></td>
									</c:if>
								</c:forEach>
							</c:if>
							<dsp:getvalueof var="planProratedPrice" value="${changePlan.priceInfo.proRatedPrice}"/>
							<c:set var="totalPlanProratedPrice" scope="request"
											value="${totalPlanProratedPrice + planProratedPrice}" />
							<td><crs:outMessage key="cricket_shoppingcart_TBD"/><!-- TBD -->
							<%-- $<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${planProratedPrice}"/> --%></td>
						</tr>
					</c:forEach>
				</c:if>
				<dsp:droplet name="DisplayNonPackageAddonItemInCart">
					<dsp:param name="order" bean="ShoppingCart.current" />
					<dsp:oparam name="output">
						<dsp:droplet name="ForEach">
							<dsp:param name="array" param="addons" />
							<dsp:param name="elementName" value="addonCommerceItem" />
							<dsp:oparam name="output">
								<dsp:getvalueof var="itemAdjustments"
									param="addonCommerceItem.priceInfo.adjustments" />
								<dsp:getvalueof var="addonCommerceItem" param="addons" />
								<dsp:droplet name="ProductLookup">
									<dsp:param name="id"
										param="addonCommerceItem.auxiliaryData.productId" />
									<dsp:param name="filterBySite" value="false" />
									<dsp:param name="filterByCatalog" value="false" />
									<dsp:param name="elementName" value="addOnProduct" />
									<dsp:oparam name="output">
										<dsp:getvalueof var="addOns" param="addOnProduct" />
										<tr>
											<th><dsp:valueof param="addOnProduct.displayName" /></th>
											<c:if test="${!empty itemAdjustments}">
												<c:forEach var="adjustment" items="${itemAdjustments}"
													varStatus="countValue">
													<c:if test="${countValue.count eq 1}">
														<c:set var="totalAddOnAmt" scope="request"
															value="${totalAddOnAmt + adjustment.totalAdjustment}" />
														<td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${adjustment.totalAdjustment}"/></td>
													</c:if>
												</c:forEach>
											</c:if>
											<dsp:getvalueof var="addonProratedPrice" param="addonCommerceItem.priceInfo.proRatedPrice"/>
											<c:set var="totalAddonProratedPrice" scope="request"
															value="${totalAddonProratedPrice + addonProratedPrice}" />
											<td><crs:outMessage key="cricket_shoppingcart_TBD"/><!-- TBD -->
											<%-- $<fmt:formatNumber type="number"  maxFractionDigits="2" minFractionDigits="2" value="${addonProratedPrice}"/> --%></td>
										</tr>
									</dsp:oparam>
								</dsp:droplet>
							</dsp:oparam>
						</dsp:droplet>
					</dsp:oparam>
				</dsp:droplet>

			</tbody>
			<tbody>
				<c:if test="${!empty changePlanList }">
					<c:forEach var="changePlan" items="${changePlanList}">
						<tr>
							<c:set var="adminFee"
								value="${changePlan.priceInfo.adminFee}" />
							<th>Administration Fee</th>
							<td><fmt:formatNumber type="number"  maxFractionDigits="2" minFractionDigits="2" value="${adminFee}" var="adminFee"/>
								$${adminFee}</td>
							<td>$${adminFee}</td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
			<tbody class="subtotal">
				<tr>
					<fmt:formatNumber type="number" scope="request"
						maxFractionDigits="2" minFractionDigits="2"
						value="${totalPlanProratedPrice+adminFee+totalAddonProratedPrice}"
						var="upgradePlanTotal" />
					<fmt:formatNumber type="number" scope="request"
						maxFractionDigits="2" minFractionDigits="2"
						value="${upgradePlanValue+totalAddOnAmt}" var="totalUpgradePlanAmt" />
					<th colspan="2"><strong><crs:outMessage key="cricket_shoppingcart_payment_duetoday"/><!-- Payment Due Today --></strong></th>
					<td><strong><crs:outMessage key="cricket_shoppingcart_TBD"/><!-- TBD --><!-- $${upgradePlanTotal} --></strong></td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="3" class="disclaimer"><crs:outMessage key="cricket_shoppingcart_upgradeplan_message"/><!-- Payment Due Today will be calculated during Checkout. --></td>
				</tr>
			</tfoot>
		</table>
	</div>
	<!--/.summary-container-->
</dsp:page>