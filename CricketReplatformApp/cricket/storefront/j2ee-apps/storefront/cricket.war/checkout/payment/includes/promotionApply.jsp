<dsp:page>
	<dsp:importbean bean="/com/cricket/pricing/CricketCouponFormHandler" />
	<dsp:importbean bean="/com/cricket/commerce/dynamo/droplet/CouponLookupDroplet" />
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach" />
	<dsp:importbean bean="/atg/commerce/ShoppingCart" />
	<dsp:getvalueof var="order" bean="ShoppingCart.current" />
	<dsp:getvalueof var="requestURI" bean="/OriginatingRequest.requestURI" />
	<dsp:getvalueof var="editCoupon" param="editCoupon" />
	<dsp:getvalueof var="couponCode" bean="CricketCouponFormHandler.currentCouponCode" />
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath" />
	

	<!-- Droplet to show Form expections -->
	<dsp:droplet name="/atg/dynamo/droplet/Switch">
		<dsp:param bean="CricketCouponFormHandler.formError" name="value" />
		<dsp:oparam name="true">
			<dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
				<dsp:param name="exceptions"
					bean="CricketCouponFormHandler.formExceptions" />
				<dsp:oparam name="output">
					<dsp:getvalueof var="errorMessage" param="message" />
					<c:set var="promoErrorMessage" value="${errorMessage}" />
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
	</dsp:droplet>
	<!--Coupon Error Popup window : PaymentMsgModal -->

	<c:choose>
		<c:when test="${not empty couponCode}">
			<c:choose>
				<c:when test="${not empty editCoupon}">
					<div class="large-4 small-8 columns left">
						<label for="promo-code">
						<crs:outMessage	key="cricket_checkout_promotional_code" />
							<!-- Promotional Code --></label>
						<dsp:input bean="CricketCouponFormHandler.couponCode" tabIndex="7" id="promo-code" autocomplete="off" name="promo-code" type="text" value="${couponCode}" maxlength="10"></dsp:input>

						<dsp:droplet name="CouponLookupDroplet">
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
						</dsp:droplet>

						<div class="promoerror">
							<crs:outMessage key="${promoErrorMessage}" />
						</div>
						<br>
					</div>
					<div class="large-2 small-4 columns left qtip">
						<dsp:a href="#" id="promo-value12" onclick="submitPromotionCheckout(promotion);">					
							<crs:outMessage key="cricket_checkout_apply" />
							<!-- Apply -->
						</dsp:a>
					</div>
				</c:when>

				<c:otherwise>
					<div class="large-4 small-8 columns left">
						<label for="promo-code"> <crs:outMessage
								key="cricket_checkout_promotional_code" />
							<!-- Promotional Code --></label>

						<dsp:input bean="CricketCouponFormHandler.couponCode" tabIndex="7" id="promo-code" name="promo-code" type="hidden" value="${couponCode}" maxlength="10"></dsp:input>
						<input type="text" value="${couponCode}" disabled="disabled">
						<dsp:droplet name="CouponLookupDroplet">
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
						</dsp:droplet>
						<div class="promoerror">
							<crs:outMessage key="${promoErrorMessage}" />
						</div>
						<br>
					</div>
					<div class="large-2 small-4 columns left qtip">
						<dsp:a href="${requestURI}">
							<dsp:param name="editCoupon" value="true" />
							<crs:outMessage key="cricket_checkout_edit" />
							<!-- Edit -->
						</dsp:a>
					</div>
				</c:otherwise>

			</c:choose>
		</c:when>

		<c:otherwise>
			<div class="large-4 small-8 columns left">
				<label for="promo-code"><crs:outMessage
						key="cricket_checkout_promotional_code" />
					<!-- Promotional Code --></label>
				<dsp:input bean="CricketCouponFormHandler.couponCode" tabIndex="7" id="promo-code" name="promo-code" type="text" value="" maxlength="10"></dsp:input>
				<div class="promoerror">
					<crs:outMessage key="${promoErrorMessage}" />
				</div>
				<br>
			</div>
			<div class="large-2 small-4 columns left qtip">
				<dsp:a href="#" id="promo-value12"
					onclick="submitPromotionCheckout(promotion);">
					<crs:outMessage key="cricket_checkout_apply" />
					<!-- Apply -->
				</dsp:a>
			</div>
		</c:otherwise>
	</c:choose>
</dsp:page>