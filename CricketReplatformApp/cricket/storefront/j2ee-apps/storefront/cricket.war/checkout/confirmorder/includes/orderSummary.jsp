<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayShoppingCartDroplet"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayCheckoutButtonDroplet"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayMdnPhoneNumberDroplet"/>
<dsp:importbean bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CommitOrderFormHandler" />
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/targeting/TargetingForEach"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/com/cricket/pricing/CricketCouponFormHandler"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<dsp:importbean bean="/atg/cricket/util/CricketProfile"/>
<dsp:importbean bean="/atg/commerce/order/purchase/RepriceOrderDroplet"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="originatingRequestURL" bean="/OriginatingRequest.requestURI"/>
<dsp:getvalueof var="itemCount" bean="ShoppingCart.current.commerceItemCount"/>
<dsp:getvalueof var="orderPriceInfo" bean="ShoppingCart.current.priceInfo"/>
<dsp:getvalueof var="workOrderType" bean='ShoppingCart.current.workOrderType' />
<c:set scope="request" var="totalAmt" value="0"/>
<c:set scope="request" var="monthlyUpgardeTotal" value="0"/>
<dsp:param name="order" bean="ShoppingCart.current"/>

<c:if test="${itemCount > 0 && (null == orderPriceInfo || empty orderPriceInfo)}">
<dsp:droplet name="RepriceOrderDroplet">
  <dsp:param value="ORDER_SUBTOTAL" name="pricingOp"/>
</dsp:droplet>
</c:if>

<!-- START: CART HTML -->
 <dsp:droplet name="DisplayShoppingCartDroplet">
	<dsp:param name="order" bean="ShoppingCart.current"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="accessories"  param="accessories"/>	
		<dsp:getvalueof var="packages"  param="packages"/>
		<dsp:getvalueof var="upgradePhone"  param="upgradePhone"/>
		<dsp:getvalueof var="changePlan"  param="changePlan"/>
		<dsp:getvalueof var="changeAddons"  param="changeAddons"/>
		<dsp:getvalueof var="removedAddon"  param="removedAddon"/>
		<dsp:getvalueof var="requestURI" bean="/OriginatingRequest.requestURI"/>
	</dsp:oparam>
</dsp:droplet>
<dsp:getvalueof var="order" bean="ShoppingCart.current"/>
  <!-- Desktop layout for Overview -->						
<c:set scope="request" var="splitPackageTotals" value=""/>		  
								<dsp:droplet name="ForEach">
									<dsp:param name="array" value="${packages}"/>
									<dsp:oparam name="output">
									 <c:set var="packageId">
													<dsp:valueof param="element.packageId"/>
											</c:set>
										<!-- START - Display PACKAGES-->	
											<table cellspacing="0" cellpadding="0" border="0">	
													<p class="title" data-section-title>
													<th class="package-title"><crs:outMessage key="cricket_checkout_package"/><!-- Package --> <dsp:valueof param="count"/></th>
													<th class="text-center"><c:if test="${isUserLoggedIn}">Monthly Service</c:if></th>
                   									<th class="text-center"><c:if test="${isUserLoggedIn}">Due Today</c:if></th>
													<dsp:include page="/checkout/includes/display_packagePrice.jsp">
														<dsp:param name="packages" param="element"/>
														<dsp:param name="pkgCount" param="count"/>														 
													</dsp:include>												 
													<c:set var="packagesTotal" value="${packagesTotal + totalAmt}"/>
													<c:set var="monthlyPkgTotal" value="${monthlyPkgTotal + totalMonthlyPlanAmt}"/>
												 	<c:set scope="request" var="packageIdSubtotal" value="${packageId}/${pckSubTotal}"/>
													<c:set scope="request" var="package12" value="${splitPackageTotals}"/>
													<c:set scope="request" var="splitPackageTotals" value="${packageIdSubtotal},${package12}"/>														
											</table>
											<div class="divider package"></div>		
										<!-- END - Display PACKAGES-->												
									</dsp:oparam>
									<dsp:oparam name="empty">
																	
									<!-- START upgrade Phone section -->
									<c:if test="${!empty upgradePhone}">
									<dsp:droplet name="ForEach">
										<dsp:param name="array" value="${upgradePhone}"/>
										<dsp:oparam name="output">
											<c:set var="upgradePhoneId">
													<dsp:valueof param="element.Id"/>
											</c:set>
											<table cellspacing="0" cellpadding="0" border="0">	
												<tbody>				
												<th class="package-title"><crs:outMessage key="cricket_checkout_upgrade_phonefor"/><!-- Upgrade Phone For: -->
													<dsp:droplet name="DisplayMdnPhoneNumberDroplet">
														<dsp:param name="profileMdn" bean="ShoppingCart.current.upgradeMdn"/>
														<dsp:oparam name="empty">
														</dsp:oparam>
														<dsp:oparam name="output">
															<dsp:valueof param="formatedMdnNumber"/>
														</dsp:oparam>
													</dsp:droplet>											
												</th>	
													<!-- upgrade Phone price details -->
													<dsp:include page="/checkout/includes/display_upgradePhonePrice.jsp">
														<dsp:param name="upgradePhone" value="${upgradePhone}"/>
													</dsp:include>
														<c:set var="upgradePhoneTotal" value="${upgradePhoneTotal + upgradePhoneAmt}"/>												
												</tbody>
											</table>
										<div class="divider package"></div>	
										</dsp:oparam>
									</dsp:droplet>										
									</c:if>	
									<!-- END upgrade Phone section -->	
									
									<!-- change Plan section -->
									<c:if test="${!empty changePlan}">
										<dsp:droplet name="ForEach">
												<dsp:param name="array" value="${changePlan}"/>
												<dsp:oparam name="output">
														<dsp:getvalueof var="count" param="count">
															<c:set var="changePlanId">
																	<dsp:valueof param="element.Id"/>
															</c:set>
															<c:if test="${count == 1}">
																<table cellspacing="0" cellpadding="0" border="0">	
																	<tbody>				
																	<th class="package-title"><crs:outMessage key="cricket_checkout_upgrade_planfor"/><!-- Plan Upgrade For: --> 
																	<dsp:droplet name="DisplayMdnPhoneNumberDroplet">
																								<dsp:param name="profileMdn" bean="ShoppingCart.current.upgradeMdn"/>
																								<dsp:oparam name="empty">
																								</dsp:oparam>
																								<dsp:oparam name="output">
																								 <dsp:valueof param="formatedMdnNumber"/>
																								</dsp:oparam>
																								</dsp:droplet>
																	
																	</th>	
																		<!-- upgrade Phone price details -->
																		<dsp:include page="/checkout/payment/includes/display_upgradePlanPrice.jsp">
																			<dsp:param name="changePlan" value="${changePlan}"/>
																			<dsp:param name="removedAddon" value="${removedAddon}"/>
																		</dsp:include>
																		<c:set var="upgradePlanTotal" value="${upgradePlanTotal + upgradePlanAmt}"/>																
																		<c:set var="monthlyUpgardeTotal" value="${monthlyUpgardeTotal + totalUpgradePlanAmt}"/>
																	</tbody>
																</table>
																<div class="divider package"></div>
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
								<dsp:param name="array" value="${addons}"/>
								<dsp:oparam name="output">
								<dsp:getvalueof var="count" param="count">
								<c:set var="changeAddonId">
										<dsp:valueof param="element.Id"/>
								</c:set>
															<c:if test="${count == 1}">
																<table cellspacing="0" cellpadding="0" border="0">	
																	<tbody>				
																	<th class="package-title"><crs:outMessage key="cricket_checkout_change_addonfor"/><!-- Add-On Change For:  -->
																	<dsp:droplet name="DisplayMdnPhoneNumberDroplet">
																								<dsp:param name="profileMdn" bean="ShoppingCart.current.upgradeMdn"/>
																								<dsp:oparam name="empty">
																								</dsp:oparam>
																								<dsp:oparam name="output">
																								 <dsp:valueof param="formatedMdnNumber"/>
																								</dsp:oparam>
																								</dsp:droplet>
																	
																	
																	</th>
																							
																	<!-- upgrade Phone price details -->
											<dsp:include page="/checkout/payment/includes/display_upgradeAddonsPrice.jsp">
											<dsp:param name="changeAddons" value="${changeAddons}"/>
											<dsp:param name="removedAddon" value="${removedAddon}"/>
										</dsp:include>
										<c:set var="upgradeAddonsTotal" value="${upgradeAddonsTotal +upgradeAddonAmt }"/>
										<c:set var="monthlyUpgradeAddonsTotal" value="${monthlyUpgradeAddonsTotal + totalMonthlyAddonAmt}"/>
																	
												  				</tbody>
															</table>	
															<div class="divider package"></div>	
															</c:if>
														</dsp:getvalueof>
												</dsp:oparam>
										</dsp:droplet>												
									</c:if>
							 							
							 	
								</dsp:oparam>
								</dsp:droplet>
								
								
							 
						<!-- START - ACCESSORIES DETAILS-->							
						<c:choose>
							<c:when test="${accessories eq null }">								
							</c:when>
							<c:otherwise>
							<table cellspacing="0" cellpadding="0" border="0">
								<tbody>
									<tr>
										<th class="package-title"><crs:outMessage key="cricket_checkout_accessories"/><!-- Accessories --></th>
									</tr>
									<!-- accessories price details -->
									<dsp:include page="/checkout/includes/display_accessoriesPrice.jsp">
										<dsp:param name="accessory" value="${accessories}"/>
									</dsp:include>
									<c:set var="accessoriesTotal" value="${accessoriesTotal + accessoriesAmt}"/>
							    </tbody>
							</table>
							<div class="divider package"></div>	
							</c:otherwise>
						</c:choose>
						<!-- END - ACCESSORIES DETAILS-->	
					<dsp:include page="/checkout/includes/display_salesTaxPrice.jsp">
						<dsp:param name="taxPriceInfo" value="${order.taxPriceInfo}"/>
					</dsp:include>	
					<c:if test="${workOrderType ne 'RRC'}">					
						<table cellspacing="0" cellpadding="0" border="0">
							 <tbody>
								<tr class="shipping-total total">
									<th><crs:outMessage key="cricket_checkout_shipping_fee"/><!-- Shipping Fee --></th>
									<td>$0.00</td>
								</tr>
							</tbody>
						</table>		
						<div class="divider package"></div>		
					</c:if>
					
					
				<dsp:getvalueof var="discount"  param="order.priceInfo.discountAmount"/>		
				 <fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.priceInfo.discountAmount}" var="discountAmount" />
					<table cellspacing="0" cellpadding="0" border="0">
						<tbody>
						<c:forEach var="priceAdjustmentcheckout" varStatus="status"	items="${order.priceInfo.adjustments}">
							<dsp:tomap var="pricingModelchekcout" value="${priceAdjustmentcheckout.pricingModel}" />
							<c:if test="${pricingModelchekcout ne null}">
							<tr class="discount">
								<th><c:out value="${pricingModelchekcout.description}" /></th>
								<td>&nbsp;</td>
							</tr>
							</c:if>
						</c:forEach>
							<c:if test="${discountAmount > 00.00}">	
								<tr class="icon-divider">
								<th></th>
								</tr>							
								<tr class="shipping-total">
									<th><em><crs:outMessage key="cricket_checkout_promotion_code_discount"/></em></th>
									<td>$${discountAmount}</td>
								</tr>
							</c:if>	
						</tbody>
					</table>
						<br>
					<input type="hidden" name="splitPackageTotals" value="${splitPackageTotals}">
					<c:set var="orderTotal" scope="request" value="${packagesTotal + upgradePhoneTotal + upgradePlanTotal + upgradeAddonsTotal + accessoriesTotal}"/>					
					<c:set var="monthlyTotal" scope="request" value="${monthlyPkgTotal + monthlyUpgardeTotal + monthlyUpgradeAddonsTotal}"/>
					  
					<!-- Total Amount - Order -->	
														
					
			
</dsp:page>