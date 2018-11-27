<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:getvalueof var="isDownGrade" bean="ShoppingCart.current.downGrade"/>
<dsp:getvalueof var="workOrderType" bean="ShoppingCart.current.workOrderType"/>

<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:getvalueof var="billingAddressType" bean="ShoppingCart.current.billingAddressType"/>
<dsp:getvalueof var="shippingAddressType" bean="ShoppingCart.current.shippingAddressType"/>	
<input type="hidden" name="billingAddressType" id="billingAddressTypeID" value="${billingAddressType}" />
<input type="hidden" name="shippingAddressType" id="shippingAddressTypeID" value="${shippingAddressType}" />

<dsp:include page="/common/head.jsp">
	<dsp:param name="seokey" value="shippingBillingDetailsKey" />
	 <dsp:param name="protocalType" value="secure"/>
</dsp:include>
 <script src="${contextpath}/js/customcricketstore.js"></script>
<!-- jQuery -->
<script type="text/javascript" src="${contextpath}/js/vendor/jquery-ui.min.js"></script>
<body>
<dsp:include page="/checkout/shippingbilling/includes/msgModal.jsp"/>
<div id="outer-wrap">
<div id="inner-wrap">
<dsp:include page="/common/header.jsp">
 <dsp:param name="protocalType" value="secure"/>
</dsp:include>	
	<dsp:importbean bean="/atg/dynamo/droplet/Switch" />
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler" />
	<dsp:importbean bean="/atg/commerce/order/purchase/ShippingGroupFormHandler" />
 <!--/#header-->
<div id="constructor" class="section-checkout">
	<!-- Progress Bar Desktop-->
	<section id="section-progress">
		<dsp:include page="/checkout/includes/progressBar.jsp" flush="true">
			<dsp:param name="activeProgressBar" value="1"/>
		</dsp:include>
	</section> <!--/#section-progess-->
	<!-- END Main Banner -->
	<!-- Checkout bill/ship info -->
	<section id="section-checkout-form">
		<div class="row">		
			<div class="large-12 small-12 columns details-content">
				<div class="large-8 small-12 columns form">
					<dsp:form id="clear-cart" name="clear-cart" method="get" formId="clear-cart">
						<dsp:input type="hidden" name="clearCartItems" id="clearCartItems" bean="CartModifierFormHandler.clearCartItems" value="submit"/>
						<%--<dsp:input type="hidden" bean="CartModifierFormHandler.clearFormSuccessURl" value="../../index.jsp"/>
						<dsp:input type="hidden" bean="CartModifierFormHandler.clearFormErrorURl" value="../shippingbilling/shippingbillingdetails.jsp"/>--%>
					</dsp:form>
					<dsp:form id="customer-info" class="custom" name="customer-info" method="get" formId="customer-info">
						<fieldset>
						<dsp:droplet name="/atg/dynamo/droplet/Switch">
						  <dsp:param name="value" bean="Profile.transient"/>
						  <dsp:oparam name="true">
								<dsp:include page="includes/newAcountHolderAddress.jsp" flush="true"></dsp:include>
								<dsp:include page="includes/newShippingAddress.jsp" flush="true"></dsp:include>
								<dsp:include page="includes/newShippingMethod.jsp" flush="true"></dsp:include>
								<dsp:include page="includes/newBillingAddress.jsp" flush="true"></dsp:include>
							</dsp:oparam>
						  <dsp:oparam name="false">
								<dsp:include page="includes/nonEditAccountHolderAddress.jsp" flush="true"></dsp:include>
								<c:choose>
									<c:when test="${workOrderType ne null && workOrderType eq 'RRC' }" >
									</c:when>
									<c:otherwise>
										<dsp:include page="includes/newShippingAddress.jsp" flush="true"></dsp:include>
										<dsp:include page="includes/newShippingMethod.jsp" flush="true"></dsp:include>
									</c:otherwise>
								</c:choose>
								<dsp:include page="includes/newBillingAddress.jsp" flush="true"></dsp:include>
						</dsp:oparam>
					</dsp:droplet>
  					</fieldset>
  					<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
  					<c:choose>
  					  <c:when test="${isDownGrade ne null && isDownGrade eq 'true' }" >
  					  <dsp:param name="inUrl" value="${contextpath}/checkout/confirm/confirmOrder.jsp"/>
  					  </c:when>
  					  <c:otherwise>
  					   <dsp:param name="inUrl" value="${contextpath}/checkout/payment/paymentDetails.jsp"/>
  					  </c:otherwise>
  					</c:choose>
 					  <dsp:oparam name="output">
					    <dsp:input
					         bean="ShippingGroupFormHandler.shippingBillingAddressSuccessURL"
					         paramvalue="secureUrl" type="hidden"/>
					  </dsp:oparam>
					</dsp:droplet>
					
					<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
					  <dsp:param name="inUrl" value="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp"/>
					  <dsp:oparam name="output">
					    <dsp:input bean="ShippingGroupFormHandler.shippingBillingAddressErrorURL" paramvalue="secureUrl" type="hidden"/>
					  </dsp:oparam>
					</dsp:droplet>
  				<%-- 	 <dsp:input type="hidden" bean="ShippingGroupFormHandler.shippingBillingAddressSuccessURL" value="${contextpath}/checkout/payment/paymentDetails.jsp"/>
					 <dsp:input type="hidden" bean="ShippingGroupFormHandler.shippingBillingAddressErrorURL" value="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp"/>
				--%>
					<dsp:input type="hidden" bean="ShippingGroupFormHandler.shippingandBillingAddress" value="submit"/>
					</dsp:form>
				</div>
					<dsp:include page="includes/orderSummary.jsp" flush="true">
					 <dsp:param name="fromPage" value="shippingBillingPage"/>
					</dsp:include>
			</div>	
		</div>	
	</section>
</div> <!--/#constructor-->
<!--// START FOOTER AREA //-->
<dsp:droplet name="/atg/dynamo/droplet/Cache">
	<dsp:param value="cricketFooter" name="key"/>
	<dsp:oparam name="output">
		<dsp:include page="/common/footer.jsp">
		<dsp:param name="protocalType" value="secure"/>
		</dsp:include>
	</dsp:oparam>
</dsp:droplet>
</div> <!--/#inner-wrap-->
</div> <!--/#outer-wrap-->
<!-- JavaScript -->	
<!-- jQuery -->
<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="ShippingbillingDetails"/>
</dsp:include>	
<script type="text/javascript" src='${contextpath}/js/chatGui-min.js'></script>
<!-- Foundation 4 -->
<script src="${contextpath}/js/foundation/foundation.min.js"></script> 
<script>
$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips');
</script>
<!-- Client Side Validation -->
<script src="${contextpath}/js/vendor/jquery.validate.min.js"></script>
<!-- SWIPER Library; Used for Phones and Plans on small screens -->
<script src="${contextpath}/lib/swiper/idangerous.swiper.js"></script>
<!-- Auto complete Plugin for Search -->
<script src="${contextpath}/lib/autocomplete/jquery.autocomplete.js" type="text/javascript"></script>
<!-- Cricket specific JS -->
<script src="${contextpath}/js/cricket.min.js"></script> <!-- Global Utilities -->
<%-- <script src="${contextpath}/js/checkout.js"></script> --%>
 </script> <!-- Global Utilities -->
		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="userIntention"  bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean.userIntention"/>
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="userIntention"  value="New Activation"/>
		</c:if>
		<c:if test="${empty userIntention}">
			<dsp:getvalueof var="userIntention"  value="null"/>
		</c:if>
		
		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="network" bean="/com/cricket/util/LocationInfo.networkProviderName"/>
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="network" bean='/atg/userprofiling/Profile.networkProvider' />
		</c:if>
		<c:if test="${empty network}">
			<dsp:getvalueof var="network"  value="null"/>
		</c:if>
		
		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="customerType"  bean="/atg/cricket/util/CricketProfile.customerType"/>
			<c:set var="customerType" value="EXISTING" />
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="customerType"  value="NEW"/>
		</c:if>
		<c:if test="${empty customerType}">
			<dsp:getvalueof var="customerType"  value="null"/>
		</c:if>
<dsp:include page="/common/includes/openCartDrawer.jsp"/>
	<dsp:droplet name="/atg/dynamo/droplet/ForEach">
	<dsp:param name="array" bean="ShoppingCart.current.commerceItems"/> 
	<dsp:param name="elementName" value="item"/>
		<dsp:oparam name="output">
				<dsp:getvalueof var="productId" param="item.auxiliaryData.productId"/>			
				<dsp:getvalueof var="quantity" param="item.quantity"/>
				<dsp:getvalueof var="unitPrice" param="item.priceInfo.listPrice"/>
				<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
					<dsp:param name="id" value="${productId}"/>
					<dsp:param name="filterBySite" value="false"/>
					<dsp:param name="filterByCatalog" value="false"/>
					<dsp:param name="elementName" value="product"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="producttype" param="product"/>
						<dsp:getvalueof var="parentCategoryId" value="${producttype.parentCategory.id}"/>
						<dsp:getvalueof var="productName" value="${producttype.displayName}"/>								
					</dsp:oparam>				
				</dsp:droplet>
				<input type="hidden" name="productId" id="productId" value="<dsp:valueof value='${productId}'/>" />
				<input type="hidden" name="productName" id="productName" value="<dsp:valueof value='${productName}'/>" />
				<input type="hidden" name="quantity" id="quantity" value="<dsp:valueof value='${quantity}'/>" />
				<input type="hidden" name="unitPrice" id="unitPrice" value="<dsp:valueof value='${unitPrice}'/>" />
				<input type="hidden" name="parentCategoryId" id="parentCategoryId" value="<dsp:valueof value='${parentCategoryId}'/>" />
				<%--
					<dsp:include page="/cart/shopActionTagCart.jsp">
					   <dsp:param name="producttype" value="${producttype}"/>
	                   <dsp:param name="productId" value="${productId}"/>
	                   <dsp:param name="productName" value="${productName}"/>
	                   <dsp:param name="network" value="${network}"/>
	                   <dsp:param name="customerType" value="${customerType}"/>
	                   <dsp:param name="userIntention" value="${userIntention}"/>
	                   <dsp:param name="quantity" value="${quantity}"/>
	                   <dsp:param name="unitPrice" value="${unitPrice}"/>
	                   <dsp:param name="parentCategoryId" value="${parentCategoryId}"/>
	              	</dsp:include>
              	--%>			
		</dsp:oparam>
	</dsp:droplet>
<script type="text/javascript">
	cmDisplayShops();
</script>
<script type="text/javascript">
	var pageID = "Cart:Checkout:Shipping";
	var categoryID = "Cart";
	//must be dynamic values					
	var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
	var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
	var network = '<dsp:valueof value='${network}'/>';
	var userIntention = '<dsp:valueof value='${userIntention}'/>';
	var customerType = '<dsp:valueof value='${customerType}'/>';
	var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
	cmCreatePageviewTag(pageID,categoryID,null,null,pvAttrs);
	$(document).ready( function() {
		var billingAddressTypeval=$("#billingAddressTypeID").val();
		var shippingAddressTypeval=$("#shippingAddressTypeID").val();
		
		if((billingAddressTypeval!=undefined && billingAddressTypeval!=null && billingAddressTypeval=='DifferentAddress')){		
			$("#different-billing-address").show();
		} else{
			$("#different-billing-address").hide();
		}
		
		if((shippingAddressTypeval!=undefined && shippingAddressTypeval!=null && shippingAddressTypeval=='DifferentAddress')){		
			$("#different-shipping-address").show();
		} else{
			$("#different-shipping-address").hide();
		}
	});
</script>
</body>
</html>
</dsp:page>