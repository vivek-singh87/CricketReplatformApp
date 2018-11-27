<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:getvalueof var="changeAddonsList" param="changeAddons"/>
<dsp:getvalueof var="removedAddonList" param="removedAddon"/>
<c:set scope="request" var="totalMonthlyAddonAmt" value="0"/>
<c:set scope="request" var="totalAddonTotal" value="0"/>
<c:set scope="request" var="totalAddonProratedPrice" value="0"/>

 			  <tr class="package-item">
                    <th class="package-item-title"></th>
                    <th class="package-item-title text-center"><crs:outMessage key="cricket_checkout_monthly_service"/><!-- Monthly Service --></th>
                    <th class="package-item-title text-center"><crs:outMessage key="cricket_checkout_due_today"/><!-- Due Today --></th>
                  </tr>
 				 <tr>
					<th colspan="3" class="heading">

			<c:choose>
				<c:when test="${!empty changeAddonsList }">
					<dsp:getvalueof var="addons"  value="${changeAddonsList}"/>
					<crs:outMessage key="cricket_shoppingcart_new_addons"/><!-- New Add-ons -->
				</c:when>
			 
			</c:choose>		

				</th>
				</tr>
				<c:choose>
					<c:when test="${!empty addons}">					
						<dsp:droplet name="ForEach">
						<dsp:param name="array" value="${addons}"/>
						<dsp:oparam name="output">
						<dsp:getvalueof var="changeAddon" param="element"/>
						<dsp:getvalueof var="itemListPrice" value="${changeAddon.priceInfo.listPrice}" scope="request"/>
						<dsp:getvalueof var="changeAddonItemAmount" value="${changeAddon.priceInfo.amount}" scope="request"/>
						<dsp:getvalueof var="itemAdministrationFee" value="${changeAddon.priceInfo.adminFee}" scope="request"/>
						<c:set var="totalAdministrationFee"  value="${totalAdministrationFee + itemAdministrationFee}"/>
						<c:set var="totalAddonTotal"  value="${totalAddonTotal + itemListPrice}"/>
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
								<td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice}"/></td>
								</c:when>
								<c:otherwise>
								<td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${itemListPrice}"/></td>
								</c:otherwise>
								</c:choose>
								<dsp:getvalueof var="addonProratedPrice" value="${changeAddon.priceInfo.proRatedPrice}"/>
											<c:set var="totalAddonProratedPrice" scope="request"
															value="${totalAddonProratedPrice + addonProratedPrice}" />
								<td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${addonProratedPrice}"/></td>
								</tr>
								</dsp:oparam>
							</dsp:droplet>
							</dsp:oparam>
							</dsp:droplet>					
					</c:when>
				</c:choose>	
				
				<c:choose>
			<c:when test="${!empty removedAddonList }">
					<dsp:getvalueof var="addonsRemove"  value="${removedAddonList}"/>
					<tr>
 
						<th colspan="3" class="heading"><crs:outMessage key="cricket_shoppingcart_removed_addons"/><!-- Removed Add-ons --></th>
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
								<td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice}"/></td>
								</c:when>
								<c:otherwise>
								<td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${itemListPrice}"/></td>
								</c:otherwise>
								</c:choose>
								<dsp:getvalueof var="addonProratedPrice" value="${changeAddon.priceInfo.proRatedPrice}"/>
											<c:set var="totalAddonProratedPrice" scope="request"
															value="${totalAddonProratedPrice + addonProratedPrice}" />
								<td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${addonProratedPrice}"/></td>
								</tr>
								</dsp:oparam>
							</dsp:droplet>
							</dsp:oparam>
							</dsp:droplet>					
					</c:when>
				</c:choose>	
			</tbody>
        </table>
            <div class="divider last"></div>
            <table cellspacing="0" cellpadding="0" border="0">
              <tbody>
                <tr>
					<th><crs:outMessage key="cricket_shoppingcart_administration_fee"/><!-- Administration Fee -->
					<fmt:formatNumber var="totalAdministrationFee" type="number" maxFractionDigits="2" minFractionDigits="2" value="${totalAdministrationFee}"/></th>
					<td>$${totalAdministrationFee}</td>
					<td>$${totalAdministrationFee}</td>	
                </tr>              
              </tbody>
			  <tbody class="subtotal">														
			<tr>
				<th colspan="2"><strong><crs:outMessage key="cricket_shoppingcart_payment_due_today"/><!-- Payment Due Today --></strong></th>
				<td><strong>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${totalAddonProratedPrice}"/></strong></td>		
			</tr>																								
		</tbody>
            </table>
			<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${totalAddonTotal + totalMonthlyAddonAmt}" var="totalMonthlyAddonAmt" />
				 <!--/.summary-container-->

</dsp:page>