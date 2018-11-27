<dsp:page>
	<dsp:getvalueof var="contextpath"
		bean="/OriginatingRequest.contextPath" />
	
	<dsp:importbean bean="/com/cricket/pricing/CricketCouponFormHandler" />
	<dsp:getvalueof var="queryStringurl" bean="OriginatingRequest.requestURIWithQueryString"/>
	<dsp:importbean
		bean="/com/cricket/commerce/dynamo/droplet/CouponLookupDroplet" />
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach" />
	<dsp:importbean bean="/atg/commerce/ShoppingCart" />
	<dsp:getvalueof var="order" bean="ShoppingCart.current" />
	<dsp:getvalueof var="editCoupon" param="editCoupon" />
	<dsp:getvalueof var="couponCode"
		bean="CricketCouponFormHandler.currentCouponCode" />

	<dsp:droplet name="/atg/dynamo/droplet/Switch">
		<dsp:param bean="CricketCouponFormHandler.formError" name="value" />
		<dsp:oparam name="true">
			<dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
				<dsp:param name="exceptions"
					bean="CricketCouponFormHandler.formExceptions" />
				<dsp:oparam name="output">
					<dsp:getvalueof var="errorMessage" param="message" />
					<input id="storeCouponErrorMessage" type="hidden"
						value="<crs:outMessage key="${errorMessage}"/>" />
					<script>
							$(document).ready(function() {													
								var errorMessage = document.getElementById("storeCouponErrorMessage").value;
								document.getElementById("couponErrorModalMessageDiv").innerHTML=errorMessage;									
								$("#couponErrorModal").click();
							});	
					</script>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
	</dsp:droplet>

	<c:choose>
		<c:when test="${not empty couponCode}">
			<c:choose>

				<c:when test="${not empty editCoupon}">
					<div class="large-9 small-9 columns">
						<label for="promo-code">Promotional Code</label> <input
							id="promo-code-cart" autocomplete="off" name="promo-code-cart"
							type="text" value="${couponCode}">
						<!-- droplet used to show coupon promotion message -->
						<%-- <dsp:droplet name="CouponLookupDroplet">
							<dsp:param name="id" value="${couponCode}" />
							<dsp:param name="elementName" value="coupon" />
							<dsp:oparam name="output">
								<dsp:droplet name="ForEach">
									<dsp:param name="array" param="coupon.promotions" />
									<dsp:param name="elementName" value="promotion" />
									<dsp:oparam name="output">
										<p>
											<dsp:valueof param="promotion.description" />
										</p>
									</dsp:oparam>
								</dsp:droplet>
							</dsp:oparam>
						</dsp:droplet> --%>
						<!-- droplet used to show form exception when coupon not applied successfully -->
						<%-- <div class="promoerror" ><crs:outMessage key="${promoErrorMessage}"/></div><br><br> --%>

					</div>
					<div class="large-3 small-3 columns">
						<dsp:a href="#" id="promo-value12" onclick="ApplyPromotion();">
							<crs:outMessage key="cricket_shoppingcart_apply" />
							<!-- Apply -->
						</dsp:a>
					</div>
				</c:when>
				<c:otherwise>
					<div class="large-9 small-9 columns">
						<label for="promo-code"><crs:outMessage
								key="cricket_shoppingcart_promotionalcode" /> <!-- Promotional Code --></label>
						<input id="promo-code-cart" name="promo-code-cart" type="hidden"
							value="${couponCode}" /> <input type="text"
							value="${couponCode}" disabled="disabled">

						<%-- <dsp:droplet name="CouponLookupDroplet">
							<dsp:param name="id" value="${couponCode}" />
							<dsp:param name="elementName" value="coupon" />
							<dsp:oparam name="output">
								<dsp:droplet name="ForEach">
									<dsp:param name="array" param="coupon.promotions" />
									<dsp:param name="elementName" value="promotion" />
									<dsp:oparam name="output">
										<p>
											<dsp:valueof param="promotion.description" />
										</p>
									</dsp:oparam>
								</dsp:droplet>
							</dsp:oparam>
						</dsp:droplet> --%>
						<%-- <div class="promoerror" ><crs:outMessage key="${promoErrorMessage}"/></div><br><br>	 --%>
					</div>
					<div class="large-3 small-3 columns">
						<c:choose>
							<c:when test="${empty pageContext.request.queryString}">
								<dsp:a href="${pageContext.request.requestURL}?openCart=true&editCoupon=true">
									<crs:outMessage key="cricket_shoppingcart_edit" />
									<!-- Edit -->
								</dsp:a>
							</c:when>
							<c:otherwise>
								<dsp:a
									href="${queryStringurl}&openCart=true&editCoupon=true">
									<crs:outMessage key="cricket_shoppingcart_edit" />
									<!-- Edit -->
								</dsp:a>
							</c:otherwise>
						</c:choose>
					</div>
				</c:otherwise>

			</c:choose>
		</c:when>

		<c:otherwise>
			<div class="large-9 small-9 columns">
				<input placeholder="Promotional Code" id="promo-code-cart" name="promo-code-cart" type="text" value="" maxlength="10"/>
				<!-- droplet used to show form exception when coupon not applied successfully -->
				<%-- 	<div class="promoerror" ><crs:outMessage key="${promoErrorMessage}"/></div><br><br> --%>
			</div>
			<div class="large-3 small-3 columns">
				<p>
					<dsp:a href="#" id="promo-value12" onclick="ApplyPromotion();">
						<crs:outMessage key="cricket_shoppingcart_apply" />
						<!-- Apply -->
					</dsp:a>
				</p>
			</div>

		</c:otherwise>
	</c:choose>



</dsp:page>