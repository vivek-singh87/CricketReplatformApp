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
<c:set scope="request" var="totalMonthlyPlanAmt" value="0"/>
	<div class="large-4 small-12 columns summary-container">
		<hr class="show-for-small"/>
		<table class="summary">
			<thead>
				<tr>
					<th></th>
					<th><c:if test="${isUserLoggedIn}">
						<crs:outMessage key="cricket_shoppingcart_monthly_service"/><!-- Monthly Service -->
						</c:if>
					</th>
					<th><crs:outMessage key="cricket_shoppingcart_due_today"/><!-- Due Today --></th>	
				</tr>	
			</thead>
		<!--NOTE: Just separate items in the table with TBODY; CSS will apply dotted bottom border -->		
			<c:set var="totalPhoneAmt" value="0" />	
							
			<tbody>
				<tr>
					<th class="heading" colspan="3"><crs:outMessage key="cricket_shoppingcart_phone"/><!-- Phone --></th>
				</tr>
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
				</tr><c:set var="totalDiscountAmt" scope="request" value="0" />
				<c:if test="${!empty phoneDiscount}">
					<c:forEach var="adjustment" items="${phoneDiscount}">
						<c:set var="promoType" value="${adjustment.pricingModel.itemDiscountType}"/>
						<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${adjustment.totalAdjustment}" var="discountAmt" />
						<c:set var="totalDiscountAmt"  value="${totalDiscountAmt+discountAmt}"/>					
						<c:choose>
							<c:when test="${promoType eq 'Web Instant Discount'}">
				<tr>	
					<th><em><crs:outMessage key="cricket_shoppingcart_instant_webdiscount"/><!-- Instant Web Discount --> </em></th>
					<td><c:if test="${isUserLoggedIn}">-</c:if></td>
					<td><em>$${discountAmt}</em></td>		
				</tr>
							</c:when>
							<c:when test="${promoType eq 'Instant Discount'}">
				<tr>	
					<th><em><crs:outMessage key="cricket_shoppingcart_instant_discount"/><!-- Instant Discount --></em></th>
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
					<th><crs:outMessage key="cricket_shoppingcart_needto_selectphone"/><!-- Need to Select Phone --> </th>
					<td><c:if test="${isUserLoggedIn}">-</c:if></td>
					<td>$0.00</td>		
				</tr>	
				</c:otherwise>
				</c:choose>
			</tbody>
			<c:set var="totalPhoneAmt" value="${totalPhoneAmt+phoneAmt+totalDiscountAmt}"/>
			<c:set var="totalPlanAmt" value="0"/>
			<c:set var="totalAddOnAmt" value="0" /> 
			
			
			<tbody>	
				<tr>
					<th colspan="3" class="heading">				
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
							</c:when>
							<c:otherwise>$${addOnAmt}</c:otherwise>
						</c:choose>
					</td>
							</c:if>
						</c:forEach>
					</c:if>
				</tr>
				<c:choose>
					<c:when test="${isUserLoggedIn}">
						<c:set var="doubletotalAddOnAmt" value="${totalAddOnAmt+doubleAddOnAmt}"/>
						<c:set var="totalAddOnAmt" value="${totalAddOnAmt+addOnAmt}"/>
					</c:when>
					<c:otherwise><c:set var="totalAddOnAmt" value="${totalAddOnAmt+addOnAmt}"/></c:otherwise>
				</c:choose>
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
										<th><em><crs:outMessage key="cricket_shoppingcart_multiline_discount"/><!-- Multi Line Discount --></em></th>
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
				<c:if test="${administrationFee ne '0.00' }">
					<th><crs:outMessage key="cricket_shoppingcart_administration_fee"/><!-- Administration Fee --></th>
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
					<th><crs:outMessage key="cricket_shoppingcart_needto_selectplan"/><!-- Need to Select Plan --></th>
					<td><c:if test="${isUserLoggedIn}">$0.00</c:if></td>
					<td>$0.00</td>		
				</tr>	
				</c:otherwise>
				</c:choose>
			</tbody>
			<c:set scope="request" var="activationFee" value="0"/>
			<c:choose>
			<c:when test="${packages.activationFee eq 0}" ></c:when>
			<c:otherwise>
			<tbody>													
				<tr>	
					<th><crs:outMessage key="cricket_shoppingcart_onetime_activation_fee"/><!-- One-time Activation Fee --></th>
					<td><c:if test="${isUserLoggedIn}">-</c:if></td>
					<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${packages.activationFee}" var="activationFee" />
					<td>
						<c:choose>
							<c:when test="${not empty activationFee}">$${activationFee}</c:when>
							<c:otherwise>$0.00</c:otherwise>
						</c:choose>
					</td>		
				</tr>																								
			</tbody>
			</c:otherwise>
			</c:choose>
			<c:set scope="request" var="totalAmt" value="0"/>
			<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${totalPlanAmt+totalPhoneAmt+activationFee}" var="totalAmt" />
			<tbody class="subtotal">													
				<tr>
					<th>
						<strong><crs:outMessage key="cricket_shoppingcart_package"/><!-- Package --> ${pkgCount}<crs:outMessage key="cricket_shoppingcart_subtotal"/> <!-- Subtotal --></strong>
					</td>
					<td></td>
					<td>
						<strong>$${totalAmt}</strong>
					</td>		
				</tr>																								
			</tbody>
			<c:if test="${isUserLoggedIn}">
				<tfoot>
				<tr>
					<td colspan="3" class="disclaimer"><crs:outMessage key="cricket_shoppingcart_AAL_message"/><!-- Payment Due Today is approximate based on two full months of service plus tax. 
						When your order ships, you will be charged for one month of service plus the days remaining in your current bill cycle. 
						Your actual amount charged will be included in your shipping confirmation email. -->
					</td>
				</tr>	
				</tfoot>
		</c:if>
		</table>	
	</div> <!--/.summary-container-->
</dsp:page>