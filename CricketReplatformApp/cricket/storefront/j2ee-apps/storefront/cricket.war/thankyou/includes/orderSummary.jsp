<dsp:page> 
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/commerce/order/util/CricketPaymentData"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayShoppingCartDroplet"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayMdnPhoneNumberDroplet"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/order/purchase/RepriceOrderDroplet"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="originatingRequestURL" bean="/OriginatingRequest.requestURI"/>
<dsp:getvalueof var="itemCount" bean="ShoppingCart.last.commerceItemCount"/>
<dsp:getvalueof var="orderPriceInfo" bean="ShoppingCart.last.priceInfo"/>
<dsp:getvalueof var="abpConfirmOrder"  bean="CricketPaymentData.abpFlag" />
<dsp:getvalueof var="isAutoBillPayment" bean="UserSessionBean.autoBillPayment"/>	
<dsp:getvalueof var="workOrderType" bean='ShoppingCart.last.workOrderType' />
<dsp:getvalueof var="ProfilemarketType" bean="Profile.marketType"/>
<dsp:getvalueof var="customerType" bean='/atg/cricket/util/CricketProfile.customerType' />
<c:set scope="request" var="totalAmt" value="0"/>
<c:set scope="request" var="monthlyUpgardeTotal" value="0"/>
<!-- START: CART HTML -->
 <dsp:droplet name="DisplayShoppingCartDroplet">
	<dsp:param name="order" bean="ShoppingCart.last"/>
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
<dsp:getvalueof var="order" bean="ShoppingCart.last"/>
  <!-- Desktop layout for Overview -->	
		<div class="large-4 small-12 columns summary hide-for-small">		
		
		<div class="row">
              <div>
                <div class="divider package show-for-small"></div>
              </div>
              
						 <c:if test="${workOrderType ne 'RRC'}">
              <table cellspacing="0" cellpadding="0" border="0">
                <thead>
                  <tr>
                    <th><h3>Order Summary</h3>
					<span><dsp:include page="/thankyou/includes/display_deliveryDate.jsp" flush="true"></dsp:include></span>
					</th>
                  </tr>
                </thead>  
              </table>
              </c:if>
        </div>
			<div class="row summary-drawer">
								<dsp:droplet name="ForEach">
									<dsp:param name="array" value="${packages}"/>
									<dsp:oparam name="output">
											<!-- START - Display PACKAGES-->	
											  <table cellspacing="0" cellpadding="0" border="0">
													<p class="title" data-section-title>
													<th class="package-title">
													<crs:outMessage key="cricket_checkout_package"/><!-- Package --> <dsp:valueof param="count"/>
													</th>
													<th class="text-center"><c:if test="${isUserLoggedIn}">Monthly Service</c:if></th>
                   									<th class="text-center"><c:if test="${isUserLoggedIn}">Due Today</c:if></th>
													<dsp:include page="/checkout/includes/display_packagePrice.jsp">
														<dsp:param name="packages" param="element"/>
														<dsp:param name="pkgCount" param="count"/>
													</dsp:include>
													<c:set var="packagesTotal" value="${packagesTotal + totalAmt}"/>
													<c:set var="monthlyPkgTotal" value="${monthlyPkgTotal + totalMonthlyPlanAmt}"/>
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
														<dsp:param name="profileMdn" bean="ShoppingCart.last.upgradeMdn"/>
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
																								<dsp:param name="profileMdn" bean="ShoppingCart.last.upgradeMdn"/>
																								<dsp:oparam name="empty">
																								</dsp:oparam>
																								<dsp:oparam name="output">
																								 <dsp:valueof param="formatedMdnNumber"/>
																								</dsp:oparam>
																								</dsp:droplet>
																	
																	</th>	
																		<!-- upgrade Phone price details -->
																		<dsp:include page="/thankyou/includes/display_upgradePlanPrice.jsp">
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
																								<dsp:param name="profileMdn" bean="ShoppingCart.last.upgradeMdn"/>
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
						<table cellspacing="0" cellpadding="0" border="0">
							<tbody>
								<tr class="tax-total total">
									<th><crs:outMessage key="cricket_checkout_sales_tax"/><!-- Sales Tax --></th>
									<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.taxPriceInfo.amount}" var="tax" />
									<td>$${tax}</td>
								</tr>
							</tbody>
						</table>
						<div class="divider package"></div>
						<c:if test="${workOrderType ne 'RRC'}">							
							<table cellspacing="0" cellpadding="0" border="0">
							<tbody>
							  <tr class="shipping-total total">
								<th>Shipping Fee</th>
								<td>$0.00</td>
							  </tr>
							</tbody>
						  </table>
					  </c:if>
					</div>
					<c:set var="orderTotal" value="${packagesTotal + upgradePhoneTotal + upgradePlanTotal + upgradeAddonsTotal + accessoriesTotal}"/>					
					<c:set var="monthlyTotal" value="${monthlyPkgTotal + monthlyUpgardeTotal + monthlyUpgradeAddonsTotal}"/>
					<!-- Total Amount - Order -->					
					<dsp:include page="/thankyou/includes/display_totalOrderAmount.jsp">
						<dsp:param name="orderTotal" value="${orderTotal}" />
						<dsp:param name="monthlyTotal" value="${monthlyTotal}" />
					</dsp:include>											
					
					
 					<c:if test="${isAutoBillPayment eq true && workOrderType ne 'RRC'}">
						  <div class="incentive" id="autoBillPaymentMessageDesktop">
							<p><sup>*</sup>	<crs:outMessage key="cricket_checkout_$5_discount_message" /></p>
						</div>  
					</c:if>	
			</div>
			<!-- Mobile layout for Overview -->
				<section id="tab-content">
				  <div class="row show-for-small">
				    <div class="columns large-12 small-12">
				      <div class="section-container auto" data-section="auto">
				        <section id="overview" class="active no-icon">
				          <p class="title" data-section-title><a href="#panel1"><crs:outMessage key="cricket_checkout_order_summary"/><!-- Order Summary --></a></p>
				          <div class="content" data-section-content>
				          	<div class="row">
						          <div class="small-12 columns summary">												
											<dsp:droplet name="ForEach">
												<dsp:param name="array" value="${packages}"/>
												<dsp:oparam name="output">
													<!-- START - Display PACKAGES-->	
														<table cellspacing="0" cellpadding="0" border="0">	
														<tbody>											
															<section id="package1" class="active">
																<p class="title" data-section-title>
																<th class="package-title"><crs:outMessage key="cricket_checkout_package"/><!-- Package --> <dsp:valueof param="count"/></th>
																<th class="text-center"><c:if test="${isUserLoggedIn}">Monthly Service</c:if></th>
                   												<th class="text-center"><c:if test="${isUserLoggedIn}">Due Today</c:if></th>
																<dsp:include page="/checkout/includes/display_packagePrice.jsp">
																	<dsp:param name="packages" param="element"/>
																	<dsp:param name="pkgCount" param="count"/>
																</dsp:include>
																<c:set var="packagesTotal" value="${packagesTotal + totalAmt}"/>
																</section>
														</tbody>
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
																					<dsp:param name="profileMdn" bean="ShoppingCart.last.upgradeMdn"/>
																					<dsp:oparam name="empty">
																					</dsp:oparam>
																					<dsp:oparam name="output">
																						<dsp:valueof param="formatedMdnNumber"/>
																					</dsp:oparam>
																				</dsp:droplet>
																			</th><!-- upgrade Phone price details -->
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
																		<th colspan="3" class="package-title"><crs:outMessage key="cricket_checkout_upgrade_planfor"/><!-- Plan Upgrade For: -->
																		<dsp:droplet name="DisplayMdnPhoneNumberDroplet">
																		<dsp:param name="profileMdn" bean="ShoppingCart.last.upgradeMdn"/>
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
														<dsp:droplet name="ForEach">
															<dsp:param name="array" value="${changeAddons}"/>
															<dsp:oparam name="output">
															<dsp:getvalueof var="count" param="count">
															<c:set var="changeAddonId">
																	<dsp:valueof param="element.Id"/>
															</c:set>
															<c:if test="${count == 1}">
																<table cellspacing="0" cellpadding="0" border="0">	
																	<tbody>				
																		<th class="package-title"><crs:outMessage key="cricket_checkout_change_addonfor"/></th>
																			<dsp:droplet name="DisplayMdnPhoneNumberDroplet">
																			<dsp:param name="profileMdn" bean="ShoppingCart.last.upgradeMdn"/>
																			<dsp:oparam name="empty">
																			</dsp:oparam>
																			<dsp:oparam name="output">
																				<dsp:valueof param="formatedMdnNumber"/>
																			</dsp:oparam>
																			</dsp:droplet>		
																			<!-- upgrade Phone price details -->
																		<dsp:include page="/checkout/payment/includes/display_upgradeAddonsPrice.jsp">
																			<dsp:param name="changeAddons" value="${changeAddons}"/>
																			<dsp:param name="removedAddon" value="${removedAddon}"/>
																		</dsp:include>
																		<c:set var="upgradeAddonsTotal" value="${upgradeAddonsTotal +upgradeAddonAmt }"/>
																		<c:set var="monthlyUpgradeAddonsTotalMobile" value="${monthlyUpgradeAddonsTotalMobile + totalMonthlyAddonAmt}"/>
																		<div class="divider package"></div>	
																	</tbody>
																</table>																	
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
											<table cellspacing="0" cellpadding="0" border="0">
												<tbody>
												<tr class="tax-total total">
													<th><crs:outMessage key="cricket_checkout_sales_tax"/><!-- Sales Tax --></th>
													<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.taxPriceInfo.amount}" var="tax" />
													<td>$${tax}</td>
												</tr>													
												</tbody>
											</table>
											<div class="divider package"></div>
											<c:if test="${workOrderType ne 'RRC'}">												
												<table cellspacing="0" cellpadding="0" border="0">
												<tbody>
												  <tr class="shipping-total total">
													<th>Shipping Fee</th>
													<td>$0.00</td>
												  </tr>
												</tbody>
											  </table>
										  </c:if>
											<div>
										</div> <!--closes row-->
									</div>
				        </section>
				      </div>
				    </div>
				  </div>
				</section>
				<!-- END Mobile Overview -->
			<section id="overview-total">
	      	<div class="row show-for-small summary">
				    <div class="columns large-12 small-12">
					
					<c:set var="orderTotal" value="${packagesTotal + upgradePhoneTotal + upgradePlanTotal + upgradeAddonsTotal + accessoriesTotal}"/>
					<c:set var="monthlyTotal" value="${monthlyPkgTotal + monthlyUpgardeTotal + monthlyUpgradeAddonsTotalMobile}"/>
					<!-- Total Amount - Order -->	
			
					<dsp:include page="/thankyou/includes/display_totalOrderAmount.jsp">
						<dsp:param name="orderTotal" value="${orderTotal}" />
						<dsp:param name="monthlyTotal" value="${monthlyTotal}" />
					</dsp:include>	
 					<c:if test="${(ProfilemarketType ne 'OOF' && customerType ne 'O' && workOrderType ne 'OXC' && workOrderType ne 'RRC')}">
						 <div class="incentive" id="autoBillPaymentMessageMobile">
							<p><sup>*</sup>	<crs:outMessage key="cricket_checkout_$5_discount_message" /></p>
						</div>
					</c:if>					
				    	
				    </div>
				  </div>
	      </section>
				
</dsp:page>