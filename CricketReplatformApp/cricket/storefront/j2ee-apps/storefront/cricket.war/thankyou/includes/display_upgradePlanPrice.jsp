<dsp:page>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup" />
	<dsp:importbean	bean="/com/cricket/commerce/order/droplet/DisplayNonPackageAddonItemInCart" />
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach" />
	<dsp:importbean bean="/atg/commerce/ShoppingCart" />
	<dsp:getvalueof var="changePlanList" param="changePlan" />
	<dsp:getvalueof var="removedAddonList" param="removedAddon"/>
	<c:set scope="request" var="totalUpgradePlanAmt" value="0" />
	<c:set scope="request" var="totalPlanProratedPrice" value="0" />
	<c:set scope="request" var="upgradePlanValue" value="0" />
	<c:set scope="request" var="totalAddOnAmt" value="0" />	
	<c:set scope="request" var="totalAddonProratedPrice" value="0" />	
	
	<!--NOTE: Just separate items in the table with TBODY; CSS will apply dotted bottom border -->		
				<tr class="package-item">
                    <th class="package-item-title"></th>
                    <th class="package-item-title text-center"><crs:outMessage key="cricket_checkout_monthly_service"/><!-- Monthly Service --></th>
                    <th class="package-item-title text-center"><crs:outMessage key="cricket_checkout_due_today"/><!-- Due Today --></th>
                  </tr>
			 <tr class="package-item">
                    <th class="package-item-title">New Plan</th>
                  </tr>
<!--NOTE:START Display Plan changes -->		
					<c:if test="${!empty changePlanList }" >
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
										<td class="text-center">$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${adjustment.totalAdjustment}"/></td>
									</c:if>
								</c:forEach>
								</c:if>							
									<dsp:getvalueof var="planProratedPrice" value="${changePlan.priceInfo.proRatedPrice}"/>
										<c:set var="totalPlanProratedPrice" scope="request"
											value="${totalPlanProratedPrice + planProratedPrice}" />
						
								<td class="text-center">$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${planProratedPrice}"/></td>	
							</tr>
							 
						</c:forEach>
						 <tr class="icon-divider"><th></th></tr>
					</c:if>	
<!--NOTE:END Display Plan changes -->	
 
<!--NOTE:START Display New Addons -->
<dsp:droplet name="DisplayNonPackageAddonItemInCart">
<dsp:param name="order" bean="ShoppingCart.last" />
	<dsp:oparam name="output">
		<dsp:droplet name="ForEach">
			<dsp:param name="array" param="addons" />
			<dsp:param name="elementName" value="addonCommerceItem" />
			<dsp:oparam name="output">								
				<dsp:getvalueof var="addonCommerceItem" param="addons" />
			</dsp:oparam>
		</dsp:droplet>
	</dsp:oparam>
</dsp:droplet>
<c:choose>
	<c:when test="${!empty addonCommerceItem }">	
		<tr class="package-item">
                    <th class="package-item-title">New Addons</th>
         </tr>
		 
	</c:when>
</c:choose>	
					<dsp:droplet name="DisplayNonPackageAddonItemInCart">
					<dsp:param name="order" bean="ShoppingCart.last" />
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
														<c:set var="totalAddOnAmt" scope="request" value="${totalAddOnAmt + adjustment.totalAdjustment}" />
														<td class="text-center">$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${adjustment.totalAdjustment}"/></td>
													</c:if>
												</c:forEach>
											</c:if>
											<dsp:getvalueof var="addonProratedPrice" param="addonCommerceItem.priceInfo.proRatedPrice"/>
											<c:set var="totalAddonProratedPrice" scope="request"
															value="${totalAddonProratedPrice + addonProratedPrice}" />						
											<td class="text-center">$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${addonProratedPrice}"/></td>
										</tr>
									</dsp:oparam>
								</dsp:droplet>
							</dsp:oparam>
						</dsp:droplet>
					</dsp:oparam>
				</dsp:droplet>
<!--NOTE:END Display New Addons -->

<!--NOTE: Display Removed Addons -->
<c:choose>
	<c:when test="${!empty removedAddonList }">
		<dsp:getvalueof var="addonsRemove"  value="${removedAddonList}"/>
		<tr class="icon-divider"><th></th></tr>
<tr class="package-item">
   <th class="package-item-title">Removed Addons</th>
  </tr>
	</c:when>
</c:choose>
<c:choose>
<c:when test="${!empty addonsRemove}">
	<dsp:droplet name="ForEach">
			<dsp:param name="array" value="${addonsRemove}"/>
			<dsp:oparam name="output">
			<dsp:getvalueof var="changeAddon" param="element"/>
			<dsp:getvalueof var="itemListPrice" value="${changeAddon.priceInfo.listPrice}" scope="request"/>
			<dsp:getvalueof var="changeAddonItemAmount" value="${changeAddon.priceInfo.amount}" scope="request"/>
			<dsp:getvalueof var="itemAdministrationFee" value="${changeAddon.priceInfo.adminFee}" scope="request"/>
			<c:set var="totalAdministrationFee"  value="${totalAdministrationFee + itemAdministrationFee}"/>
			
				<dsp:droplet name="ProductLookup">
				<dsp:param name="id" value="${changeAddon.auxiliaryData.productId}"/>
				<dsp:param name="filterBySite" value="false"/>
       			<dsp:param name="filterByCatalog" value="false"/>
				<dsp:param name="elementName" value="addOns"/>
					<dsp:oparam name="output"><tr>	
					<th><dsp:valueof param="addOns.displayName"/></th>
					<c:choose>
					<c:when test="${empty itemListPrice || itemListPrice eq 0}">
					<dsp:include page="/browse/includes/priceLookup.jsp">
			            	<dsp:param name="productId"  value="${changeAddon.auxiliaryData.productId}" />
			               	<dsp:param name="skuId"  value="${changeAddon.catalogRefId}"/>
			             </dsp:include>
						 <c:if test="${empty retailPrice || retailPrice eq 0}">									
									<dsp:droplet name="/atg/commerce/catalog/SKULookup">
										<dsp:param name="id" value="${changeAddon.catalogRefId}"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
										</dsp:oparam>
									</dsp:droplet>
								</c:if>
					<td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice}"/> </td>
					</c:when>
					<c:otherwise>
					<td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${itemListPrice}"/></td>
					</c:otherwise>
					</c:choose>
					<td>$${changeAddonItemAmount}
					</tr>
					</dsp:oparam>
				</dsp:droplet>
				</dsp:oparam>
				</dsp:droplet>
					
			</c:when>
</c:choose>	
<!--NOTE:END Display Removed Addons -->	
 <tr class="icon-divider"><th></th></tr>
					<c:if test="${!empty changePlanList }">
					<c:forEach var="changePlan" items="${changePlanList}">
							<tr>
								<c:set var="adminFee" value="${changePlan.priceInfo.adminFee}" />
								<th><crs:outMessage key="cricket_checkout_administration_fee"/><!-- Administration Fee --></th>
								<td class="text-center"><fmt:formatNumber type="number"  maxFractionDigits="2" minFractionDigits="2" value="${adminFee}" var="adminFee"/>
								$${adminFee}</td>
								<td class="text-center">$${adminFee}</td>
							</tr>
						</c:forEach>
					</c:if>                          						
				
             <tr class="icon-divider"><th></th></tr>
				<tr class="package-total total">
                    <th><crs:outMessage key="cricket_checkout_subtotal"/><!--Subtotal--></th>
                    <td>&nbsp;</td>				
					<fmt:formatNumber type="number" scope="request"
						maxFractionDigits="2" minFractionDigits="2"
						value="${totalPlanProratedPrice+adminFee+totalAddonProratedPrice}"
						var="upgradePlanTotal" />
					<fmt:formatNumber type="number" scope="request"	maxFractionDigits="2" minFractionDigits="2"	value="${upgradePlanValue+totalAddOnAmt}" var="totalUpgradePlanAmt" />					
                    <td class="text-center">$${upgradePlanTotal}</td>
                </tr>
		 <!--/.summary-container-->
</dsp:page>