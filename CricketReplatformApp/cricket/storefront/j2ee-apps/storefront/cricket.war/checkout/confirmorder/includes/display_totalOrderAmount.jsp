<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:getvalueof var="itemCount" bean="ShoppingCart.current.commerceItemCount"/>
<dsp:getvalueof var="orderTotal" param="orderTotal"/>
<dsp:getvalueof var="monthlyTotal" param="monthlyTotal"/>
<dsp:getvalueof var="order" bean="ShoppingCart.current"/>
<dsp:importbean bean="/com/cricket/commerce/order/util/CricketPaymentData"/>
<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
<dsp:getvalueof var="isAutoBillPayment" bean="UserSessionBean.autoBillPayment"/>	
<dsp:getvalueof var="workOrderType" bean='ShoppingCart.current.workOrderType' />
<c:choose>
<c:when test="${itemCount != 0 }">	
							
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
				<dsp:getvalueof var="abpConfirmOrder"  bean="CricketPaymentData.abpFlag" />
				<dsp:getvalueof var="customerType" bean='/atg/cricket/util/CricketProfile.customerType' />
				<dsp:getvalueof var="networkProvider" bean='/atg/userprofiling/Profile.networkProvider' />
				<dsp:getvalueof var="ProfilemarketType" bean="/atg/userprofiling/Profile.marketType"/>
			<%--	<c:choose>
					<c:when	test="${(networkProvider eq 'CRICKET' && workOrderType ne 'OXC')}">
					</c:when>
					<c:otherwise>					 
						<div class="incentive">
							<p><sup>*</sup><crs:outMessage key="cricket_shoppingcart_automatic_bill_message"/></p>
						</div>
					</c:otherwise>
				</c:choose>	
				--%>
				
				<c:if test="${ProfilemarketType ne 'OOF' && isAutoBillPayment eq true && workOrderType ne 'RRC'}">
						<div class="incentive">
							<p><sup>*</sup>	<crs:outMessage key="cricket_checkout_$5_discount_message" /></p>
						</div>		
				</c:if>
					
		</c:when>
</c:choose>
</dsp:page>