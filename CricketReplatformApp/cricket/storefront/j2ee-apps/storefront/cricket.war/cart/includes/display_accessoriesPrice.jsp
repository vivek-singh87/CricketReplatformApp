<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:getvalueof var="accessoryList" param="accessory"/>
<c:set scope="request" var="accessoriesAmt" value="0"/>
	<div class="large-4 small-12 columns summary-container upgrade">
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
			<tbody>
				<tr>
					<th colspan="3" class="heading"><crs:outMessage key="cricket_shoppingcart_Accessories"/><!-- Accessories --></th>
				</tr>
				<c:if test="${!empty accessoryList }" >
				<c:forEach var="accessory" items="${accessoryList}">
				<tr>
					<dsp:droplet name="ProductLookup">
					<dsp:param name="filterBySite" value="false"/>
					<dsp:param name="filterByCatalog" value="false"/>
					<dsp:param name="id" value="${accessory.auxiliaryData.productId}"/>
					<dsp:oparam name="output">
					<th colspan="2"><dsp:valueof param="element.displayName"/></th>
					</dsp:oparam>
					</dsp:droplet>
					<c:if test="${!empty accessory.priceInfo.adjustments}">
						<c:forEach var="adjustment" items="${accessory.priceInfo.adjustments}" varStatus="countValue">
							<c:if test="${countValue.count eq 1}">
								<c:set var="accessoriesAmt" scope="request" value="${accessoriesAmt + adjustment.totalAdjustment}"/>
					<td>$${adjustment.totalAdjustment}</td>
							</c:if>				
						</c:forEach>
					</c:if>	
				</tr>
				<c:set var="pricingDetails" value="${accessory.priceInfo.currentPriceDetails}"/>
				
				<c:if test="${!empty pricingDetails}">
				<c:forEach var="amountInfo" items="${pricingDetails}">
				<c:set var="accessoryAdjustments" value="${amountInfo.adjustments}"/>
				
				<c:if test="${!empty accessoryAdjustments}">
					<c:forEach var="adjustment" items="${accessoryAdjustments}">
					<c:if test="${adjustment.adjustmentDescription eq 'Item Discount'}">
						<c:set var="promoType" value="${adjustment.pricingModel.itemDiscountType}"/>
						<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${adjustment.totalAdjustment}" var="accessoryDiscountAmt" />
						<c:set var="totalAccessoryDiscountAmt"  value="${totalAccessoryDiscountAmt+adjustment.totalAdjustment}"/>					
						<c:choose>
							<c:when test="${promoType eq 'Web Instant Discount'}">
				<tr>	
					<th><em><crs:outMessage key="cricket_shoppingcart_instant_webdiscount"/><!-- Instant Web Discount --> </em></th>
					<td></td>
					<td><em>$${accessoryDiscountAmt}</em></td>		
				</tr>
							</c:when>
							<c:when test="${promoType eq 'Instant Discount'}">
				<tr>	
					<th><em><crs:outMessage key="cricket_shoppingcart_instant_discount"/><!-- Instant Discount --></em></th>
					<td></td>
					<td><em>$${accessoryDiscountAmt}</em></td>		
				</tr>
							</c:when>
							<c:otherwise>
				<tr>	
					<th><em>${adjustment.pricingModel.displayName}</em></th>
					<td></td>
					<td><em>$${accessoryDiscountAmt}</em></td>		
				</tr>
							</c:otherwise>
						</c:choose>
						</c:if>
					</c:forEach>
				</c:if>
				</c:forEach>
				</c:if>
				<tr />
				</c:forEach>
				</c:if>														
			</tbody>			
			<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${accessoriesAmt  + totalAccessoryDiscountAmt}" var="accessoriesAmtFmt" />						
			<tbody class="subtotal">														
				<tr>
					<th colspan="2"><strong><crs:outMessage key="cricket_shoppingcart_payment_duetoday"/><!-- Payment Due Today --></strong></th>
					<td><strong>$${accessoriesAmtFmt}</strong></td>		
				</tr>																								
			</tbody>		
		</table>	
	</div> <!--/.summary-container-->			
</dsp:page>