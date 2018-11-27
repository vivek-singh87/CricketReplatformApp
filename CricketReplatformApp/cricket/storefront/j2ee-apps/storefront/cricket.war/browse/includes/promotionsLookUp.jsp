<dsp:page>
<dsp:getvalueof var="skuId" param="skuId"/>
<dsp:param name="Product" param="Product" />
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="mypricelist" bean="Profile.pricelist"/>
<dsp:getvalueof var="pageValue" param="pageValue"/>
<dsp:getvalueof var="retailPrice" param="retailPrice"/>
<c:set var="totalPromoDiscountAmt" value="0" scope="request"/>
<c:set var="instantPromoDiscountAmt" value="0" scope="request"/>
<c:set var="webInstantPromoDiscountAmt" value="0" scope="request"/>

	<dsp:droplet name="/com/cricket/promotion/droplet/DisplayPromotionDroplet">
		<dsp:param name="product" param="product"/>
		<dsp:param name="item" param="sku"/>
		<dsp:param name="retailPrice" value="${retailPrice}"/>
		<dsp:oparam name="Output">
			<dsp:getvalueof var="promotionVO"  param="promotionInfo"/>
		</dsp:oparam>
	</dsp:droplet>	
	<c:if test="${!empty promotionVO}">
	<c:set var="promotionVOPromotion" scope="request" value="${promotionVO.promtions}"/>	
		<c:forEach var="promo" items="${promotionVO.promtions}">
			<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${promo.value}" var="discountAmt" />
				<c:if test="${empty pageValue}">
					${promo.key}: <span class="right sale-price">- $${discountAmt}</span><br />
				</c:if>			
		</c:forEach>	
		<c:if test="${promotionVO.amtMIR gt 0.0}">	
			<c:set var="rebateAmt" scope="request" value="${promotionVO.amtMIR}"/>
		</c:if>
		<c:set var="instantPromoDiscountAmt" scope="request" value="${promotionVO.instantDiscount}"/>	
		<c:set var="webInstantPromoDiscountAmt" scope="request" value="${promotionVO.webInstantDiscount}"/>	
		<c:set var="totalPromoDiscountAmt" scope="request" value="${promotionVO.totalDiscount}"/>	
	</c:if>
</dsp:page>