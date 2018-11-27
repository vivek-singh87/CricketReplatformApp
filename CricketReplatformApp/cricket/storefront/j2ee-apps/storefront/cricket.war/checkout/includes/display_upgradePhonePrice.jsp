<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:getvalueof var="upgradePhoneList" param="upgradePhone"/>
<c:set var="totalPhoneAmt" scope="request" value="0" />	
<c:set var="upgradeAmt" scope="request" value="0" />
<c:set var="phoneDiscountAmt" scope="request" value="0" />

	<!--NOTE: Just separate items in the table with TBODY; CSS will apply dotted bottom border -->			
		
			<tr>
				<th colspan="3" class="heading"><crs:outMessage key="cricket_checkout_phone_upgrade"/><!-- Phone Upgrade --></th>
			</tr>			
			<c:if test="${!empty upgradePhoneList }" >
					<c:forEach var="upgradePhone" items="${upgradePhoneList}">
					<tr>
						<dsp:droplet name="ProductLookup">
							<dsp:param name="id" value="${upgradePhone.auxiliaryData.productId}"/>
							<dsp:oparam name="output">
									<th><dsp:valueof param='element.displayName'/></th>
							</dsp:oparam>
						</dsp:droplet>
						<td></td>
							<c:if test="${!empty upgradePhone.priceInfo.adjustments}">
								<c:forEach var="adjustment" items="${upgradePhone.priceInfo.adjustments}" varStatus="countValue">
									<c:if test="${countValue.count eq 1}">
									
										<c:set var="upgradeAmt" scope="request" value="${upgradeAmt + adjustment.totalAdjustment}"/>										
							<td>$${adjustment.totalAdjustment}</td>
									</c:if>				
								</c:forEach>
							</c:if>		
					</tr>
				
						<c:if test="${!empty upgradePhone.priceInfo.adjustments}">
								<c:forEach var="adjustment" items="${upgradePhone.priceInfo.adjustments}">
									<c:if test="${adjustment.adjustmentDescription eq 'Item Discount'}">							
												<c:set var="promoType" value="${adjustment.pricingModel.itemDiscountType}"/>
												<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${adjustment.totalAdjustment}" var="discountAmt" />
												<c:set var="phoneDiscountAmt" scope="request" value="${phoneDiscountAmt+discountAmt}"/>					
												<c:choose>
													<c:when test="${promoType eq 'Web Instant Discount'}">
														<tr>	
															<th><em><crs:outMessage key="cricket_checkout_instant_web_discount"/><!-- Instant Web Discount --></em></th>
															<td></td>
															<td><em>$${discountAmt}</em></td>		
														</tr>
													</c:when>
													<c:when test="${promoType eq 'Instant Discount'}">
														<tr>	
															<th><em><crs:outMessage key="cricket_checkout_instant_discount"/><!-- Instant Discount --></em></th>
															<td></td>
															<td><em>$${discountAmt}</em></td>		
														</tr>														
													</c:when>	
													<c:otherwise>
															<tr>	
																<th><em>${adjustment.pricingModel.displayName}</em></th>
																<td></td>
																<td><em>$${discountAmt}</em></td>		
															</tr>
													</c:otherwise>														
												</c:choose>
									</c:if>				
								</c:forEach>
						</c:if>	
					</c:forEach>
			</c:if>																
				<c:set var="totalPhoneAmt" value="${totalPhoneAmt+upgradeAmt+phoneDiscountAmt}"/>			
		<tr class="icon-divider"><th></th></tr>				
			<tr class="package-total total">
				<th colspan="2"><strong><crs:outMessage key="cricket_checkout_phone_upgrade"/><crs:outMessage key="cricket_checkout_subtotal"/><!-- Phone Upgrade Subtotal --></strong></th>
				<c:set var="upgradePhoneAmt" scope="request" value="0" />		
			<fmt:formatNumber type="number" scope="request" maxFractionDigits="2"  minFractionDigits="2" value="${upgradePhoneAmt+upgradeAmt+phoneDiscountAmt}" var="upgradePhoneAmt" />		
				<td><strong>$${upgradePhoneAmt}</strong></td>
			</tr>		
<!--/.summary-container-->
</dsp:page>