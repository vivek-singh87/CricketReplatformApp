<dsp:page>
<c:set var="retailPrice" value="0"/>
<c:set var="percentOff" value="0" scope="request" />
<dsp:getvalueof var="productId" param="productId"/>
<dsp:getvalueof var="skuId" param="skuId"/>

	<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
		<dsp:param name="filterBySite" value="false"/>
		<dsp:param name="filterByCatalog" value="false"/>
		<dsp:param name="id" param="productId"/>
		<dsp:param name="elementName" value="product"/>	
		<dsp:oparam name="output">
			<dsp:getvalueof var="product" param="product"/>	
		</dsp:oparam>
	</dsp:droplet>
	<c:if test="${empty skuId && !empty product}">
		<c:set var="skuId" value="${product.childSkus[0].id}"/>
	</c:if>
	<!-- Price Data -->
	<dsp:droplet name="/atg/commerce/catalog/SKULookup">
		<dsp:param name="filterBySite" value="false"/>
		<dsp:param name="filterByCatalog" value="false"/>
		<dsp:param name="id" value="${skuId}"/>
		<dsp:param name="elementName" value="sku"/>	
		<dsp:oparam name="output">
			<dsp:getvalueof var="sku" param="sku"/>										
		</dsp:oparam>
	</dsp:droplet>
	<dsp:include page="/browse/includes/priceLookup.jsp">
		<dsp:param name="productId" value="${productId}"/>
		<dsp:param name="skuId" value="${skuId}"/>
	</dsp:include>
	<c:if test="${empty retailPrice || retailPrice eq 0}">		
		<dsp:getvalueof var="retailPrice" value="${sku.listPrice}" />			
	</c:if>
	<c:set var="retailPrice" value="${retailPrice}" scope="request"/>
	
	<!-- Promotion Data -->	
	<c:if test="${not empty sku}">
		<dsp:include page="/browse/includes/promotionsLookUp.jsp">
			<dsp:param name="product" value="${product}" />
			<dsp:param name="sku" value="${sku}" />
			<dsp:param name="retailPrice" value="${retailPrice}"/>
			<dsp:param name="pageValue" value="phoneList" />
		</dsp:include>
	</c:if>
	<!-- Calculate the percentage off -->	
	<c:if test="${not empty totalPromoDiscountAmt && totalPromoDiscountAmt ne 0}">
		<c:set var="percentOff" value="${((retailPrice -(retailPrice-totalPromoDiscountAmt)) / retailPrice)*100}" />
		<fmt:formatNumber type="number" pattern="###" value="${percentOff}" var="percentOff" scope="request" />
	</c:if>
	<c:set var="finalPrice" value="${retailPrice - totalPromoDiscountAmt}" scope="request"/>
</dsp:page>