<dsp:page>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/browse/IsFinancingAvailableDroplet"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<dsp:getvalueof var="userLocationCity" bean="Profile.userLocationCity"></dsp:getvalueof>
<dsp:getvalueof var="OOFMarketType" bean="CartConfiguration.outOfFootPrintMarketType"></dsp:getvalueof>
<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>
<dsp:getvalueof var="paymentPlansLink" bean="CricketConfiguration.shopPagesLinks.paymentPlans" />
<dsp:getvalueof var="mailRebatePromotionLink" bean="CricketConfiguration.mailRebatePromotionLink" />
<!-- Displays the Price, Promotions and Final Price for a selected sku -->
	<div class="prices">
	   Retail Price: 
        <span class="right">
           <dsp:include page="/browse/includes/priceLookup.jsp">
           		<dsp:param name="productId" param="product.id"/>
				<dsp:param name="skuId" param="sku.id" />
			</dsp:include>
			<c:if test="${empty retailPrice || retailPrice eq 0}">
				<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>
			</c:if>
			$${retailPrice}
        </span><br />        
        <dsp:include page="/browse/includes/promotionsLookUp.jsp">
			<dsp:param name="product" param="product" />
			<dsp:param name="sku" param="sku" />
			<dsp:param name="retailPrice" value="${retailPrice}"/>
		</dsp:include>                     
	</div>
	<div class="final-price">
		<p class="left">Final Price: </p>
		<p class="right text-right">
			<dsp:getvalueof var="isFinanceAvailable" param="isFinanceAvailable"/>
           	<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${totalPromoDiscountAmt}" var="totalPromoDiscountAmt" />						
           	<c:set var="finalPrice" value="${retailPrice - totalPromoDiscountAmt}"/>		
			<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${finalPrice}" var="finalPrice" />	
			<c:set var="splitPrice" value="${fn:split(finalPrice, '.')}"/>
			<span class="green-price"><sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup></span>	
			<c:if test="${not empty rebateAmt}">
				<span class="sale-price">Includes a $<fmt:formatNumber type="number" pattern="###" value="${rebateAmt}"/><a class="rebate" href="${mailRebatePromotionLink}"> mail-in offer.</a></span>
			</c:if>		
			<dsp:getvalueof var="displayName" param="product.displayName"/>
			<c:choose>
			      <c:when  test='${fn:containsIgnoreCase(displayName, "Apple")}'>					     
			      </c:when>
			      <c:otherwise>
			  		  <a href="#legal" class="financing scroll-link">Important Device Purchase Information</a>	
			      </c:otherwise>
			</c:choose>		             
	<%-- 	<c:if test="${isFinanceAvailable}">
				<c:if test="${finalPrice >= 99.00 }">
					<dsp:droplet name="IsFinancingAvailableDroplet">
					<dsp:param name="userLocationCity" value="${userLocationCity}"/>
					<dsp:param name="OOFMarketType" value="${OOFMarketType}"/>
					<dsp:param name="marketType" value="${marketType}"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="showFinanceOption" param="showFinanceOption"></dsp:getvalueof>
						<c:if test="${showFinanceOption eq 'true'}">
							<a href="${paymentPlansLink}" class="financing">Payment Plan Available</a>
						</c:if>
					</dsp:oparam>
					</dsp:droplet>
				</c:if>
			</c:if> --%>
		</p>
   </div>	
</dsp:page>