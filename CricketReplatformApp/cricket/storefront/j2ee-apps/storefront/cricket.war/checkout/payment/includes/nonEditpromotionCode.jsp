<dsp:page>
	<dsp:importbean bean="/com/cricket/pricing/CricketCouponFormHandler"/>
	<dsp:importbean bean="/com/cricket/commerce/dynamo/droplet/CouponLookupDroplet"/>
	<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
	<dsp:getvalueof var="order" bean="ShoppingCart.current"/>
	<dsp:getvalueof var="editCoupon" param="editCoupon"/>
	<dsp:getvalueof var="couponCode" bean="CricketCouponFormHandler.currentCouponCode"/>
	<dsp:setvalue value="${couponCode}" bean="UserSessionBean.couponCode"/>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>


<h3>	<crs:outMessage key="cricket_checkout_promotion_code"/> <!-- Promotion Code --></h3>
<c:choose>
    <%-- Is there a coupon code applied? --%>
    <c:when test="${not empty couponCode}">     
    <p class="promo-code">${couponCode}
		<dsp:droplet name="CouponLookupDroplet">	
					<dsp:param name="id" value="${couponCode}"/>
					<dsp:param name="elementName" value="coupon"/>
						<dsp:oparam name="output">
							<dsp:droplet name="ForEach">
								<dsp:param name="array" param="coupon.promotions"/>
								<dsp:param name="elementName" value="promotion"/>
								<dsp:oparam name="output">								
										-<dsp:valueof param="promotion.description"/>									
								</dsp:oparam>
							</dsp:droplet>
						</dsp:oparam>
						</dsp:droplet>
	</p>
	<a class="edit" href="${contextpath}/checkout/payment/paymentDetails.jsp">
			<crs:outMessage key="cricket_checkout_edit"/><!-- Edit -->
	</a>
   
    </c:when>
    <c:otherwise>
			 <p class="promo-code"><crs:outMessage key="cricket_checkout_no_coupon_applied"/><!-- No Coupon Applied --></p>
			<a class="edit" href="${contextpath}/checkout/payment/paymentDetails.jsp">
			<crs:outMessage key="cricket_checkout_edit"/><!-- Edit -->
		</a>
    </c:otherwise>
 </c:choose>
	
	

</dsp:page>