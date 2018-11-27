<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:getvalueof var="removedAddonList" param="removedAddon"/>

<c:set scope="request" var="totalMonthlyRemovedAddonAmt" value="0"/>
<c:set scope="request" var="totalRemovedAddonTotal" value="0"/>
<div class="large-4 small-12 columns summary-container">
	<hr class="show-for-small"/>
	<table class="summary">
		<thead>
			<tr>
				<th></th>
				<th><crs:outMessage key="cricket_shoppingcart_monthly_service"/><!-- Monthly Service --></th>	
				<th><crs:outMessage key="cricket_shoppingcart_due_today"/><!-- Due Today --></th>	
			</tr>	
		</thead>
	<!--NOTE: Just separate items in the table with TBODY; CSS will apply dotted bottom border -->		
		<tbody>
			<tr>
				<th colspan="3" class="heading">Removed Addons</th>
			</tr>
			<c:choose>
				<c:when test="${!empty removedAddonList}">
			
				
			<dsp:droplet name="ForEach">
			<dsp:param name="array" value="${removedAddonList}"/>
			<dsp:oparam name="output">
			<dsp:getvalueof var="removedAddonItem" param="element"/>
			<dsp:getvalueof var="itemListPrice" value="${removedAddonItem.priceInfo.listPrice}" scope="request"/>
			<dsp:getvalueof var="changeAddonItemAmount" value="${removedAddonItem.priceInfo.amount}" scope="request"/>
			<dsp:getvalueof var="itemAdministrationFee" value="${removedAddonItem.priceInfo.adminFee}" scope="request"/>
			<c:set var="totalRemovedAddonAdministrationFee"  value="${totalRemovedAddonAdministrationFee + itemAdministrationFee}"/>
			<c:set var="totalRemovedAddonTotal"  value="${totalRemovedAddonTotal + changeAddonItemAmount}"/>
				<dsp:droplet name="ProductLookup">
				<dsp:param name="id" value="${removedAddonItem.auxiliaryData.productId}"/>
				<dsp:param name="filterBySite" value="false"/>
       			<dsp:param name="filterByCatalog" value="false"/>
				<dsp:param name="elementName" value="addOns"/>
					<dsp:oparam name="output"><tr>	
					<th><dsp:valueof param="addOns.displayName"/></th>
					<c:choose>
					<c:when test="${empty itemListPrice || itemListPrice eq 0}">
					<dsp:include page="/browse/includes/priceLookup.jsp">
			            	<dsp:param name="productId"  value="${removedAddonItem.auxiliaryData.productId}" />
			               	<dsp:param name="skuId"  value="${removedAddonItem.catalogRefId}"/>
			             </dsp:include>
						 <c:if test="${empty retailPrice || retailPrice eq 0}">									
									<dsp:droplet name="/atg/commerce/catalog/SKULookup">
										<dsp:param name="id" value="${removedAddonItem.catalogRefId}"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
										</dsp:oparam>
									</dsp:droplet>
								</c:if>
					<td>-$${retailPrice}</td>
					</c:when>
					<c:otherwise>
					<td>-$${itemListPrice}</td>
					</c:otherwise>
					</c:choose>
					<dsp:getvalueof var="removedAddonProratedPrice" value="${removedAddonItem.priceInfo.proRatedPrice}"/>
											<c:set var="totalRemovedAddonProratedPrice" scope="request"
															value="${totalRemovedAddonProratedPrice + removedAddonProratedPrice}" />
					<td>$${removedAddonProratedPrice}</td>
					</tr>
					</dsp:oparam>
				</dsp:droplet>
				</dsp:oparam>
				</dsp:droplet>
					
			</c:when>
			</c:choose>											
		</tbody>	
		<tbody>
			<tr>	
				<th><crs:outMessage key="cricket_shoppingcart_administration_fee"/><!-- Administration Fee --></th>
				<td>$${totalRemovedAddonAdministrationFee}</td>
				<td>-</td>		
			</tr>																							
		</tbody>											
		<tbody class="subtotal">														
			<tr>
				<th colspan="2"><strong><crs:outMessage key="cricket_shoppingcart_payment_due_today"/><!-- Payment Due Today --></strong></th>
				<td><strong>$${totalRemovedAddonProratedPrice}<!-- TBD --></strong></td>		
			</tr>																								
		</tbody>
		<fmt:formatNumber type="number" scope="request" maxFractionDigits="2" minFractionDigits="2" value="${totalRemovedAddonTotal + totalMonthlyRemovedAddonAmt}" var="totalMonthlyRemovedAddonAmt" />
		<tfoot>
			<tr>
				<td colspan="3" class="disclaimer"><crs:outMessage key="cricket_shoppingcart_upgrade_addons_message"/><!-- Messaging about not being able to upgrade phone at this time. And when the prorated amount will be calculated and how. --></td>
			</tr>	
		</tfoot>		
	</table>	
</div> <!--/.summary-container-->


</dsp:page>