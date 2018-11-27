<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:getvalueof var="upgradePhoneList" param="upgradePhone"/>
<div class="large-4 small-12 columns summary-container">
	<hr class="show-for-small"/>
	<table class="summary">
		<thead>
			<tr>
				<th></th>
				<th></th>	
				<th><crs:outMessage key="cricket_shoppingcart_due_today"/><!-- Due Today --></th>	
			</tr>	
		</thead>
	<!--NOTE: Just separate items in the table with TBODY; CSS will apply dotted bottom border -->		
	<c:set var="totalPhoneAmt" value="0" />	
		<tbody>
			<tr>
				<th colspan="3" class="heading"><crs:outMessage key="cricket_shoppingcart_phone_upgrade"/><!-- Phone Upgrade --></th>
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
					<th><em><crs:outMessage key="cricket_shoppingcart_instant_webdiscount"/><!-- Instant Web Discount --></em></th>
					<td></td>
					<td><em>$${discountAmt}</em></td>		
				</tr>
							</c:when>
							<c:when test="${promoType eq 'Instant Discount'}">
				<tr>	
					<th><em><crs:outMessage key="cricket_shoppingcart_instant_discount"/><!-- Instant Discount --></em></th>
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
		</tbody>												
		<tbody class="subtotal">		
		<c:set var="totalPhoneAmt" value="${totalPhoneAmt+upgradeAmt+phoneDiscountAmt}"/>												
			<tr>
				<th colspan="2"><strong><crs:outMessage key="cricket_shoppingcart_payment_duetoday"/><!-- Payment Due Today --></strong></th>
				<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${upgradePhoneAmt+upgradeAmt+phoneDiscountAmt}" var="upgradePhoneAmt" />
				<td><strong>$${upgradePhoneAmt}</strong></td>
			</tr>																								
		</tbody>
		<tfoot>
			<tr>
				<td colspan="3" class="disclaimer"><crs:outMessage key="cricket_shoppingcart_upgrade_phone_message"/><!-- It is possible that your upgrade phone will require a plan change.  If so, our customer service rep can assist you when you call to activate your new phone on your account. --></td>
			</tr>	
		</tfoot>		
	</table>	
</div> <!--/.summary-container-->
</dsp:page>