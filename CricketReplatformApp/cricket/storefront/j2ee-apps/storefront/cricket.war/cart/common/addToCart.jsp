<dsp:page>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:getvalueof var="contextpath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="addToCartClass" param="addToCartClass"/>
<dsp:getvalueof var="productId" param="productId"/>
<dsp:getvalueof var="isDynamicAddOn" param="isDynamicAddOn"/>
<dsp:getvalueof var="currentURL" param="url"/>
<dsp:getvalueof var="editPhone" param="editPhone"/>
<dsp:getvalueof var="editPlan" param="editPlan"/>
<a href="javascript:void();" class="${addToCartClass}" onclick="submitFormAddToCart('addToCart${productId}');">Add to Cart</a>

<dsp:form name="addToCart${productId}" id="addToCart${productId}" formid="addToCart${productId}" style="display:none" requiresSessionConfirmation="false">
	<dsp:input type="hidden" bean="CartModifierFormHandler.productId" paramvalue="productId"/>
	<dsp:input type="hidden" bean="CartModifierFormHandler.catalogRefIds"  paramvalue="skuId"/>
	<dsp:input type="hidden" bean="CartModifierFormHandler.quantity" paramvalue="quantity"/>
	<dsp:input type="hidden" bean="CartModifierFormHandler.addItemToOrderSuccessURL" value="${contextpath}/cart/common/json/addToCartResponse.jsp"/>
	<dsp:input type="hidden" bean="CartModifierFormHandler.addItemToOrderErrorURL" value="${contextpath}/cart/common/json/addToCartResponse.jsp"/>
	<dsp:input type="hidden" bean="CartModifierFormHandler.addItemToOrder" value="submit"/>
	<dsp:droplet name="ProductLookup">
	<dsp:param name="id" param="productId"/>
	<dsp:param name="filterBySite" value="false"/>
	<dsp:param name="filterByCatalog" value="false"/>
	<dsp:param name="elementName" value="Product"/>
	<dsp:oparam name="output">
	<dsp:getvalueof var="productType" param="Product.type"/>
		<input type="hidden" id="itemType" value="${productType}"/> 
	</dsp:oparam>
	</dsp:droplet>
	<c:if test="${!empty isDynamicAddOn}">
		<input type="hidden" name="dynamicAddOn" value="${isDynamicAddOn}"/>
	</c:if>
	<c:if test="${!empty editPhone || !empty editPlan }">
		<input type="hidden" name="editItem" value="true"/>
	</c:if>
	<dsp:getvalueof var="upgradeItemFlow" param="upgradeItemFlow">
	<c:if test="${!empty upgradeItemFlow}">
		<input type="hidden" name="upgradeItemFlow" id="upgradeItemFlow" value="true"/>
	</c:if>
	</dsp:getvalueof>	
	<dsp:getvalueof var="packageId" param="packageId"/>
	<c:if test="${!empty packageId}">
		<input type="hidden" name="formPackageId" id ="formPackageId" value="${packageId}"/>
	</c:if>
	<input type="hidden" name="upgradeFlow" id="upgradeFlow" value=""/>
	<input type="hidden" name="removeItemBeforeAdding" id="removeItemBeforeAdding" value="false"/>
	<input type="hidden" name="clearUserIntention" id="clearUserIntention" value="false"/>	
	<input type="hidden" name="setUserIntention" id="setUserIntention" value=""/>
	<dsp:getvalueof var="addOnValue" param="addOnValue"/>
	<c:if test="${!empty addOnValue}">
		<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
		<!-- Set Phone URL -->
		<dsp:droplet name="DimensionValueCacheDroplet">
			<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="allPhonesCategoryCacheEntry" param="dimensionValueCacheEntry" />
	      </dsp:oparam>
		</dsp:droplet>
		<input type="hidden" name="allPhoneUrl" value="${contextpath}${allPhonesCategoryCacheEntry.url}"/>
		<!-- Set Plan URL -->
		<dsp:droplet name="DimensionValueCacheDroplet">
			<dsp:param name="repositoryId" bean="CricketConfiguration.allPlansCategoryId"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="allPlansCategoryCacheEntry" param="dimensionValueCacheEntry" />
	    	</dsp:oparam>
		</dsp:droplet>
		<input type="hidden" name="allPlanUrl" value="${contextpath}${allPlansCategoryCacheEntry.url}"/>
	</c:if>
	<c:if test="${!empty currentURL}">
		<input type="hidden" name="currentURL" value="${currentURL}"/>
	</c:if>
</dsp:form>


</dsp:page>