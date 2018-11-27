<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/pricing/CricketCouponFormHandler" />

<dsp:getvalueof var="itemCount" bean="ShoppingCart.current.commerceItemCount"/>
<dsp:getvalueof var="orderTotal" param="orderTotal"/>
<dsp:getvalueof var="monthlyTotal" param="monthlyTotal"/>
<dsp:getvalueof var="order" bean="ShoppingCart.current"/>
<dsp:getvalueof var="workOrderType" bean='ShoppingCart.current.workOrderType' />
<dsp:getvalueof var="userIntention" bean="UpgradeItemDetailsSessionBean.userIntention"/>

<c:choose>
<c:when test="${itemCount != 0 }">
		<div class="row item-line-promo">
				<dsp:include page="/cart/includes/promotionApply.jsp"></dsp:include>												
		</div>
		<div class="row item-line-shipping">
				<c:forEach var="priceAdjustment" varStatus="status" items="${order.priceInfo.adjustments}" >
					<dsp:tomap var="pricingModel" value="${priceAdjustment.pricingModel}"/>
							<div class="large-8 small-8 columns" style="height: auto; width: 303px;">
								<p style="color: #00a663;"><c:out value="${pricingModel.description}"/></p>
							</div>	
								<hr>		
				</c:forEach>			
				<dsp:getvalueof var="discount"  param="order.priceInfo.discountAmount"/>		
				 <fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.priceInfo.discountAmount}" var="discountAmount" />
				<c:if test="${discountAmount > 00.00}">
							
					<div class="large-8 small-8 columns">
						<p><crs:outMessage key="cricket_checkout_promotion_code_discount"/>:<!-- Order Discount: --></p>
					</div>	
					
					<div class="large-4 small-4 columns">
						<p class="amount">$${discountAmount}</p>
					</div>	
				</c:if>				
		</div>
		<c:if test="${userIntention ne 'upgradePlan' && userIntention ne 'upgradeFeature'}">		
			<div class="row item-line-shipping">
				<div class="large-8 small-8 columns">
					<p><crs:outMessage key="cricket_shoppingcart_shipping_fee"/><!-- Shipping Fee: --></p>
				</div>	
				<div class="large-4 small-4 columns">
					<p class="amount">$0.00</p>
				</div>												
			</div>	
		</c:if>
		
									
		<div class="row item-line-total">
			<div class="large-8 small-8 columns">
				<p><crs:outMessage key="cricket_shoppingcart_order_total"/><!-- Order Total: --></p>
			</div>	
			<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${order.priceInfo.total}" var="orderTotal" />
			<div class="large-4 small-4 columns">
				<p class="amount">
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
				</p>
			</div>												
		</div>
		<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${monthlyTotal}" var="monthlyTotal" />		
		<c:if test="${not empty monthlyTotal && monthlyTotal gt 0.00}">
		<div class="row item-line-monthly">
			<div class="large-8 small-8 columns">
				<p><crs:outMessage key="cricket_shoppingcart_estimated_monthly_total"/><!-- Estimated Monthly Total: --></p>
			</div>
			<div class="large-4 small-4 columns">
				<p class="amount">$${monthlyTotal}</p>
			</div>												
		</div>
		</c:if>
		<div class="row footnote">
			<div class="large-12 small-12 columns">
				<p><crs:outMessage key="cricket_shoppingcart_sales_tax_message"/><!-- Sales tax to be determined upon checkout. --></p> 
			</div>
		</div>
		<div class="row footnote" id="cartAutoBillPaymentMessage" style="display : none">
	
			<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
			<c:if test="${!isUserLoggedIn}">
			
				<c:if test="${workOrderType ne 'RRC'}">
					<div class="large-12 small-12 columns">
						<p><crs:outMessage key="cricket_checkout_$5_discount_message"/><!-- Sales tax to be determined upon checkout. --></p> 
					</div>
				</c:if>
			</c:if>

			</div>	
</c:when>
<c:otherwise>
		<h3><crs:outMessage key="cricket_shoppingcart_add_phoneplan_tocheckout"/><!-- Add a phone and plan to checkout. --></h3>
		<p><crs:outMessage key="cricket_shoppingcart_sales_tax_message"/><!-- Sales tax to be determined upon checkout. --></p>
</c:otherwise>
</c:choose>
</dsp:page>