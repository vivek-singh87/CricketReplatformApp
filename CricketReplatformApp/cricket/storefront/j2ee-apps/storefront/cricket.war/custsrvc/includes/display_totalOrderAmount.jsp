<dsp:page>
<dsp:getvalueof var="order" param="order"/>
<dsp:getvalueof var="itemCount" value="${order.commerceItemCount}"/>
<dsp:getvalueof var="orderTotal" param="orderTotal"/>
<dsp:getvalueof var="monthlyTotal" param="monthlyTotal"/>

<c:choose>
<c:when test="${itemCount != 0 }">		
						
					
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
							<tr class="icon-divider"><th></th></tr>
								<tr class="shipping-total">
									<th><em><crs:outMessage key="cricket_checkout_promotion_code_discount"/></em></th>
									<td>$${discountAmount}</td>
								</tr>
								<div class="divider package"></div>
							</c:if>	
						</tbody>
					</table>			
					<br>						
					<div class="divider package"></div>	
				<table cellspacing="0" cellpadding="0" border="0">
              <tbody>
                <tr class="order-total">
                  <th><crs:outMessage key="cricket_checkout_order_total"/><!-- Order Total: --></th>
				  <fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.priceInfo.total}" var="orderTotal" />
                  <td>$${orderTotal}</td>
                </tr>
				<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${monthlyTotal}" var="monthlyTotal" />	
				
				<c:if test="${not empty monthlyTotal && monthlyTotal gt 0.00}">
					<tr class="estimated-monthly-total total">
					  <th><crs:outMessage key="cricket_checkout_estimated_monthly_total"/><!-- Estimated Monthly Total --><sup><crs:outMessage key="cricket_checkout_star_symbol"/><!-- * --></sup><crs:outMessage key="cricket_checkout_semicolon_symbol"/><!-- : --> </th>
					  <td>$${monthlyTotal}</td>
					</tr>
				</c:if>
				
              </tbody>
            </table>			
			</c:when>
</c:choose>
</dsp:page>