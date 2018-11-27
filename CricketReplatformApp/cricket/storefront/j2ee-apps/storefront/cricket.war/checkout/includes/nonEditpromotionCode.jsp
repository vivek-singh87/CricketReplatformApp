<dsp:page>
	<dsp:importbean bean="/com/cricket/pricing/CricketCouponFormHandler"/>
	<dsp:importbean bean="/com/cricket/commerce/dynamo/droplet/CouponLookupDroplet"/>
	<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>	
	<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
	<dsp:getvalueof var="order" bean="ShoppingCart.last"/>
	<dsp:getvalueof var="editCoupon" param="editCoupon"/>
 <dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:getvalueof var="sessionCouponCode" bean="UserSessionBean.couponCode"/>

<h3>	<crs:outMessage key="cricket_checkout_promotion_code"/> <!-- Promotion Code --></h3> 
<c:choose>
    <%-- Is there a coupon code applied? --%>
    <c:when test="${not empty sessionCouponCode}">
     
    <p class="promo-code">${sessionCouponCode}
		<dsp:droplet name="CouponLookupDroplet">	
					<dsp:param name="id" value="${sessionCouponCode}"/>
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
    </c:when>
    <c:otherwise>
	<p><crs:outMessage key="cricket_checkout_no_coupon_applied"/>	<!-- No Coupon Applied --></p>
    </c:otherwise>
 </c:choose>
	
	

</dsp:page>