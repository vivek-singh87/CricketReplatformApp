<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>

<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:getvalueof var="packages" param="packages"/>
<dsp:getvalueof var="phone" value="${packages.phonePriceInfo}"/>
<dsp:getvalueof var="phoneDiscount" value="${packages.phoneDiscountAdjustments}"/>
<dsp:getvalueof var="planDiscount" value="${packages.planDiscountAdjustments}"/>
<dsp:getvalueof var="plan" value="${packages.planPriceInfo}"/>
<dsp:getvalueof var="addOnList" value="${packages.addOnPriceInfo}"/>
<dsp:getvalueof var="pkgCount" param="pkgCount"/>
<c:set scope="request" var="totalAmt" value="0"/>
<c:set scope="request" var="totalMonthlyPlanAmt" value="0"/>			
<c:set var="totalPhoneAmt" value="0" />	
		<!--NOTE: Just separate items in the table with TBODY; CSS will apply dotted bottom border -->		
			
				<tr class="package-item">
					<th class="package-item-title" " colspan="3"> <crs:outMessage key="cricket_checkout_phone"/><!-- Phone --></th>
				</tr>
				<c:set scope="request" var="totalDiscountAmt" value="0"/>			
				<c:choose>
				<c:when test="${!empty phone}">
				<tr>
					<dsp:droplet name="ProductLookup">
					<dsp:param name="filterBySite" value="false"/>
					<dsp:param name="filterByCatalog" value="false"/>
					<dsp:param name="id" value="${packages.phoneCommerceItem.productId}"/>
					<dsp:oparam name="output">
					<th><dsp:valueof param='element.displayName'/></th>
					</dsp:oparam>
					</dsp:droplet>
					<td><c:if test="${isUserLoggedIn}">-</c:if></td>
					<c:if test="${!empty phone.adjustments}">
						<c:forEach var="adjustment" items="${phone.adjustments}" varStatus="countValue">
							<c:if test="${countValue.count eq 1}">
								<c:set var="phoneAmt" scope="request" value="${adjustment.totalAdjustment}"/>
					<td>$${phoneAmt}</td>
							</c:if>				
						</c:forEach>
					</c:if>					
				</tr>
				<c:set var="totalDiscountAmt" scope="request" value="0" />
				<c:if test="${!empty phoneDiscount}">
					<c:forEach var="adjustment" items="${phoneDiscount}">
						<c:set var="promoType" value="${adjustment.pricingModel.itemDiscountType}"/>
						<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${adjustment.totalAdjustment}" var="discountAmt" />
						<c:set var="totalDiscountAmt"  value="${totalDiscountAmt+discountAmt}"/>					
						<c:choose>
							<c:when test="${promoType eq 'Web Instant Discount'}">
									<tr>	
										<th><em><crs:outMessage key="cricket_checkout_instant_web_discount"/> <!-- Instant Web Discount --></em></th>
										<td><c:if test="${isUserLoggedIn}">-</c:if></td>
										<td><em>$${discountAmt}</em></td>		
									</tr>
								
							</c:when>
							<c:when test="${promoType eq 'Instant Discount'}">
									<tr>	
										<th><em><crs:outMessage key="cricket_checkout_instant_discount"/> <!-- Instant Discount --></em></th>
										<td><c:if test="${isUserLoggedIn}">-</c:if></td>
										<td><em>$${discountAmt}</em></td>		
									</tr>
							</c:when>
							<c:otherwise>
							<tr>	
								<th><em>${adjustment.pricingModel.displayName}</em></th>
								<td><c:if test="${isUserLoggedIn}">-</c:if></td>
								<td><em>$${discountAmt}</em></td>		
							</tr>
							</c:otherwise>
						</c:choose>		
					</c:forEach>
				</c:if>
				</c:when>
				<c:otherwise>
				<tr>	
					<c:set var="phoneAmt" value="0" />
					<c:set var="totalDiscountAmt" value="0" />
					<th><crs:outMessage key="cricket_checkout_need_select_phone"/><!-- Need to Select Phone --> </th>
					<td><c:if test="${isUserLoggedIn}">-</c:if></td>
					<td>$0.00</td>		
				</tr>	
				</c:otherwise>
				</c:choose>
			<tr class="icon-divider"><th></th></tr>		
			<c:set var="totalPhoneAmt" value="${totalPhoneAmt+phoneAmt+totalDiscountAmt}"/>
			<c:set var="totalPlanAmt" value="0"/>
			<c:set var="totalAddOnAmt" value="0" />
			<tbody>	
				<tr class="package-item">
					<th class="package-item-title title-spacer">
						<c:choose>
							<c:when test="${isUserLoggedIn}">
								Plan
							</c:when>
							<c:otherwise>
								<crs:outMessage key="cricket_shoppingcart_monthly_service"/><!-- Monthly Service -->
							</c:otherwise>
						</c:choose>					
					</th>
				</tr>
				<c:choose>
				<c:when test="${!empty plan || !empty addOnList}">
				<tr>
					<dsp:droplet name="ProductLookup">
						<dsp:param name="filterBySite" value="false"/>
						<dsp:param name="filterByCatalog" value="false"/>
						<dsp:param name="id" value="${packages.planCommerceItem.productId}"/>
						<dsp:oparam name="output">
							<th><dsp:valueof param='element.displayName'/></th>
						</dsp:oparam>
					</dsp:droplet>
					<c:if test="${!empty plan.adjustments}">
						<c:forEach var="adjustment" items="${plan.adjustments}" varStatus="countValue">
							<c:if test="${countValue.count eq 1}">
								<c:set var="planAmt" scope="request" value="${adjustment.totalAdjustment}"/>
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${planAmt}" var="planAmt" />
					<td><c:if test="${isUserLoggedIn}">$${planAmt}</c:if></td>
					<td>
						<c:choose>
							<c:when test="${isUserLoggedIn}">
								<c:set var="doublePlanAmt" value="${planAmt * 2}"/>
								<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${doublePlanAmt}" var="doublePlanAmt" />
								$${doublePlanAmt}
							</c:when>
							<c:otherwise>$${planAmt}</c:otherwise>
						</c:choose>
					</td>
							</c:if>
						</c:forEach>
					</c:if>	
				</tr>				
				<c:if test="${!empty addOnList }" >
				<c:set var="totalAddOnAmt" value="0" scope="request" />
				<c:forEach var="addOns" items="${addOnList}" varStatus="countValue">			
					<tr>
						<dsp:droplet name="ProductLookup">
							<dsp:param name="filterBySite" value="false"/>
							<dsp:param name="filterByCatalog" value="false"/>
							<dsp:param name="id" value="${packages.addOnsCommerceItems[countValue.count-1].productId}"/>
							<dsp:oparam name="output">
								<th><dsp:valueof param="element.displayName"/></th>
							</dsp:oparam>
						</dsp:droplet>
						<c:if test="${!empty addOns.adjustments}">						
							<c:forEach var="adjust" items="${addOns.adjustments}" varStatus="countValue">
								<c:if test="${countValue.count eq 1}">
									<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${adjust.totalAdjustment}" var="addOnAmt" />
								<td><c:if test="${isUserLoggedIn}">$${addOnAmt}</c:if></td>
								<td>
									<c:choose>
										<c:when test="${isUserLoggedIn}">
											<c:set var="doubleAddOnAmt" value="${addOnAmt * 2}"/>
											<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${doubleAddOnAmt}" var="doubleAddOnAmt" />
											$${doubleAddOnAmt}
											<c:set var="doubletotalAddOnAmt" value="${totalAddOnAmt+doubleAddOnAmt}"/>
											<c:set var="totalAddOnAmt" value="${totalAddOnAmt+addOnAmt}"/>
										</c:when>
										<c:otherwise>
											$${addOnAmt}
											<c:set var="totalAddOnAmt" value="${totalAddOnAmt+addOnAmt}"/>
										</c:otherwise>
									</c:choose>
								</td>
								</c:if>
							</c:forEach>
						</c:if>
					</tr>
					</c:forEach>
				</c:if>
				
				<c:if test="${!empty planDiscount}">
				<c:set var="planDiscountAmt" value="0" /> 	
					<c:forEach var="adjustment" items="${planDiscount}">
						<c:set var="promoType" value="${adjustment.pricingModel.itemDiscountType}"/>
						<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${adjustment.totalAdjustment}" var="discountAmt" />
						<c:set var="planAmt"  value="${planAmt+discountAmt}"/>	
						<c:set var="planDiscountAmt"  value="${planDiscountAmt+discountAmt}"/>				
						<c:choose>
							<c:when test="${promoType eq 'Multi Line Discount'}">
				<tr>	
					<th><crs:outMessage key="cricket_shoppingcart_multiline_discount"/><!-- Multi Line Discount --></th>
					<td><c:if test="${isUserLoggedIn}">$${discountAmt}</c:if></td>
					<td>$${discountAmt}</td>		
				</tr>
							</c:when>
							<c:otherwise>
				<tr>	
					<th><em>${adjustment.pricingModel.displayName}</em></th>
					<td><c:if test="${isUserLoggedIn}">$${discountAmt}</c:if></td>
					<td><em>$${discountAmt}</em></td>		
				</tr>
							</c:otherwise>		
						</c:choose>	
					</c:forEach>
				</c:if>		
				
				<tr>	
				<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${plan.adminFee}" var="administrationFee" />
				<c:if test="${administrationFee ne '0.00'}">
					<th><crs:outMessage key="cricket_checkout_administration_fee"/><!-- Administration Fee --></th>
					
						<td><c:if test="${isUserLoggedIn}">$${administrationFee}</c:if></td>	
						<td>
							<c:choose>
								<c:when test="${isUserLoggedIn}">
									<c:set var="doubleAdministrationFee" value="${administrationFee * 2}"/>
									<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${doubleAdministrationFee}" var="doubleAdministrationFee" />
									$${doubleAdministrationFee}
								</c:when>
								<c:otherwise>$${administrationFee}</c:otherwise>
							</c:choose>
						</td>
					</c:if>	
				</tr>		
				<c:set var="totalPlanAmt" scope="request" value="0" />
				<c:choose>
					<c:when test="${isUserLoggedIn}">
						<c:set var="totalPlanAmt" value="${totalPlanAmt+doublePlanAmt+doubleAdministrationFee+doubletotalAddOnAmt+planDiscountAmt}"/>
					</c:when>
					<c:otherwise><c:set var="totalPlanAmt" value="${totalPlanAmt+planAmt+administrationFee+totalAddOnAmt}"/></c:otherwise>
				</c:choose>
				<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${planAmt + totalAddOnAmt + administrationFee}" var="totalMonthlyPlanAmt" />
				</c:when>
				<c:otherwise>
				<tr>	
					<th><crs:outMessage key="cricket_checkout_need_select_plan"/><!-- Need to Select Plan --></th>
					<td><c:if test="${isUserLoggedIn}">$0.00</c:if></td>
					<td>$0.00</td>		
				</tr>	
				</c:otherwise>
				</c:choose>
			</tbody>
			<tr class="icon-divider"><th></th></tr>
			<c:set scope="request" var="activationFee" value="0"/>
			<c:choose>
				<c:when test="${packages.activationFee eq 0}" ></c:when>
				<c:otherwise>
					<tbody>	  

						<tr class="package-item">
							<th class="title-spacer"><crs:outMessage key="cricket_checkout_activation_fee"/><!-- One-time Activation Fee --></th>				
							<td><c:if test="${isUserLoggedIn}">-</c:if></td>
							<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${packages.activationFee}" var="activationFee" />
							<td>$${activationFee}</td>							
						</tr>											   													
					</tbody>
				</c:otherwise>
			</c:choose>
			<c:set scope="request" var="totalAmt" value="0"/>
			<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${totalPlanAmt+totalPhoneAmt+activationFee}" var="totalAmt" />
			<tbody class="subtotal">													
				<tr class="package-total total">
					<th style="text-align:left;">
						<strong>
							<crs:outMessage key="cricket_checkout_package"/><!-- Package --> ${pkgCount} 
							<crs:outMessage key="cricket_checkout_subtotal"/> <!-- Subtotal -->
						</strong>
					</th>
					<td></td>
					<td>
						<strong>$${totalAmt} </strong>
						<c:set scope="request" var="pckSubTotal" value="${totalAmt}"/>
						
					</td>		
				</tr>
				 
			</tbody>
	 <!--/.summary-container-->
</dsp:page>