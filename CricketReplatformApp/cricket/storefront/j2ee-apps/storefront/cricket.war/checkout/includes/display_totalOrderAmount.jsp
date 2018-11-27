<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:getvalueof var="itemCount" bean="ShoppingCart.current.commerceItemCount"/>
<dsp:getvalueof var="orderTotal" param="orderTotal"/>
<dsp:getvalueof var="monthlyTotal" param="monthlyTotal"/>
<dsp:getvalueof var="order" bean="ShoppingCart.current"/>
<dsp:getvalueof var="fromPage" param="fromPage"/>
<dsp:getvalueof var="workOrderType" bean='ShoppingCart.current.workOrderType' />
<c:choose>
<c:when test="${itemCount != 0 }">	
					<c:if test="${fromPage ne null && fromPage eq 'shippingBillingPage'}">
						<table cellspacing="0" cellpadding="0" border="0">
							<tbody>
								<tr class="tax-total">
									<th><crs:outMessage key="cricket_checkout_sales_tax"/><!-- Sales Tax --></th>
									<td>TBD</td>
								</tr>
							</tbody>
						</table>
						<div class="divider package"></div>
					</c:if>
					<c:if test="${workOrderType ne 'RRC'}">		
						<table cellspacing="0" cellpadding="0" border="0">
							 <tbody>
								<tr class="shipping-total">
									<th><crs:outMessage key="cricket_checkout_shipping_fee"/><!-- Shipping Fee --></th>
									<td>$0.00</td>
								</tr>
							</tbody>
						</table>
						<div class="divider package"></div>
					</c:if>
					
				
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
				<table cellspacing="0" cellpadding="0" border="0">
              <tbody>
			  <%--   <tr class="order-total">
                  <th>SubTotal:</th>
				  <fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.priceInfo.rawSubtotal}" var="SubTotal" />
                  <td>$${SubTotal}</td>
                </tr>		 --%>	
				
                <tr class="order-total">
                  <th><crs:outMessage key="cricket_checkout_order_total"/><!-- Order Total: --></th>
					<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.priceInfo.total}" var="orderTotal" />
                  <td>
                  <dsp:droplet name="/com/cricket/commerce/order/droplet/CricketCheckChangeAddonAndPlanDroplet">
					<dsp:param name="order"  bean="ShoppingCart.current"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="changeAddonAndPlan" param="changeAddonAndPlan"/>
						<c:choose>
							<c:when test="${changeAddonAndPlan eq 'true'}"><crs:outMessage key="cricket_shoppingcart_TBD"/><!-- TBD --></c:when>
							<c:otherwise>$${orderTotal}</c:otherwise>
						</c:choose>					
					</dsp:oparam>
					</dsp:droplet>
                  </td>
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