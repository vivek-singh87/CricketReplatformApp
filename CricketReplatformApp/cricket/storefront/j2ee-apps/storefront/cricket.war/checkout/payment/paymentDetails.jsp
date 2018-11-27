<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/Switch" />
<dsp:importbean bean="/atg/commerce/order/purchase/PaymentGroupFormHandler"/>
<dsp:importbean bean="/com/cricket/pricing/CricketCouponFormHandler"/>
<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/com/cricket/cart/formHandler/CricketAutoBillPaymentFormHandler"/>
<dsp:include page="/common/head.jsp">
	<dsp:param name="seokey" value="paymentDetailKey" />
	 <dsp:param name="protocalType" value="secure"/>
</dsp:include>
<!-- LivePerson chat -->
<script src="${contextpath}/js/customcricketstore.js"></script>
<!-- jQuery -->
<script type="text/javascript" src="${contextpath}/js/vendor/jquery-ui.min.js"></script>
<script type="text/javascript" src='${contextpath}/js/chatGui-min.js'></script>
<!-- TAGGING: Tagging Libraries -->
<!-- Note: The client id set here is for staging, production environment ID = 90383849 -->
<script>
$(function(){
	var isAnonymousUser = '<dsp:valueof bean="UserSessionBean.autoBillPayment"/>';
	if(isAnonymousUser == 'false'){
		document.getElementById('autoBillPaymentText').style.display = 'none';
		document.getElementById('autoBillPaymentTextMobile').style.display = 'none';	
	}
	$("#autoBillPaymentFormCheckout").ajaxForm();
	});
function setAtutoBillPayment(val){
	if(val==false){	
		document.getElementById('autoBillPaymentCheckout').value = 'false';
		document.getElementById('autoBillPaymentText').style.display = 'none';		
		document.getElementById('autoBillPaymentTextMobile').style.display = 'none';	
	}
	if(val==true){
		document.getElementById('autoBillPaymentCheckout').value = 'true';
		document.getElementById('autoBillPaymentText').style.display = 'block';
		document.getElementById('autoBillPaymentTextMobile').style.display = 'block';	
	}	
	submitAutoBillPaymentFormCheckout();
}
function submitAutoBillPaymentFormCheckout(){	
	$('#submitAutoBillPaymentCheckout').trigger('click');
	}

function submitPromotionCheckout(formId){
	var promoCode = document.getElementById('promo-code').value;

	document.getElementById('promoCode-submit').value = promoCode; 	
	document.getElementById('promotion').submit();
}
</script>
<body>
<dsp:include page="includes/paymentMsgModal.jsp"/>
<div id="outer-wrap">
<div id="inner-wrap">
	<dsp:include page="/common/header.jsp">
		 <dsp:param name="protocalType" value="secure"/>
	</dsp:include>	 <!--/#header-->
	<!--// END HEADER AREA //-->
<div id="constructor" class="section-checkout step2">
	<!-- Progress Bar Desktop-->
	<section id="section-progress">
			<dsp:include page="/checkout/includes/progressBar.jsp" flush="true">
			<dsp:param name="activeProgressBar" value="2"/>
		</dsp:include>
	</section> <!--/#section-progess-->
	<!-- END Main Banner -->
	<!-- Checkout Payment Info -->
	<section id="section-checkout-form">
		<div class="row">			
			<div class="large-12 small-12 columns details-content">
				<div class="large-8 small-12 columns form">
				<dsp:form id="payment-info" class="custom" name="payment-info" method="post" action="">
					<fieldset>
						<!-- Payment Information Form-->
						<dsp:include page="/checkout/payment/includes/payment_information.jsp" flush="true"></dsp:include>
					<!-- Auto Bill Payment Page - Only for New customer-->
										<dsp:droplet name="Switch">
											<dsp:param name="value" bean="Profile.isUserLoggedIn" />
											<dsp:oparam name="false">
												<!-- Hide AutoBillPayment option for the following scenarios-->
												<dsp:getvalueof var="customerType" bean='/atg/cricket/util/CricketProfile.customerType' />
												<dsp:getvalueof var="networkProvider" bean='/atg/userprofiling/Profile.networkProvider' />
												<dsp:getvalueof var="workOrderType" bean='ShoppingCart.current.workOrderType' />
										 
												<c:choose>
												<c:when	test="${(networkProvider eq 'CRICKET' && workOrderType ne 'OXC')}">
													<dsp:include page="/cart/includes/autoBillPaymentInPaymentPage.jsp"
														flush="true"></dsp:include>
												</c:when>
												</c:choose>												
											</dsp:oparam>
										</dsp:droplet>
							<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
							  <dsp:param name="inUrl" value="${contextpath}/checkout/confirm/confirmOrder.jsp"/>
							  <dsp:oparam name="output">
							    <dsp:input bean="PaymentGroupFormHandler.newPaymentGroupSuccessURL"
							         paramvalue="secureUrl" type="hidden"/>
							  </dsp:oparam>
							</dsp:droplet>
							<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
							  <dsp:param name="inUrl" value="${contextpath}/checkout/payment/paymentDetails.jsp"/>
							  <dsp:oparam name="output">
							  <dsp:input
							         bean="PaymentGroupFormHandler.newPaymentGroupErrorURL"
							         paramvalue="secureUrl" type="hidden"/>
							  </dsp:oparam>
							</dsp:droplet>
						     <dsp:input bean="PaymentGroupFormHandler.newPaymentGroup" priority="-10" type="hidden" value="Enter Credit Card"/>		
  					</fieldset>
				</dsp:form>
				<dsp:form id="promotion"  method="post" >
					<dsp:input bean="CricketCouponFormHandler.couponCode" id="promoCode-submit" name="promo-code" type="hidden" value=""></dsp:input>
					<dsp:input bean="CricketCouponFormHandler.claimCoupon" priority="-10" type="hidden" value="Enter Credit Card"/>
					<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
							  <dsp:param name="inUrl" value="${contextpath}/checkout/payment/paymentDetails.jsp"/>
							  <dsp:oparam name="output">
							    <dsp:input bean="CricketCouponFormHandler.applyCouponSuccessURL"
							         paramvalue="secureUrl" type="hidden"/>
							  </dsp:oparam>
							</dsp:droplet>
							<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
							  <dsp:param name="inUrl" value="${contextpath}/checkout/payment/paymentDetails.jsp"/>
							  <dsp:oparam name="output">
							  <dsp:input
							         bean="PaymentGroupFormHandler.newPaymentGroupErrorURL"
							         paramvalue="secureUrl" type="hidden"/>
							  </dsp:oparam>
							</dsp:droplet>
				<%-- 	<dsp:input bean="CricketCouponFormHandler.applyCouponSuccessURL" value="${contextpath}/checkout/payment/paymentDetails.jsp" type="hidden" />
					<dsp:input bean="CricketCouponFormHandler.applyCouponErrorURL" value="${contextpath}/checkout/payment/paymentDetails.jsp" type="hidden" />	
				--%>
				</dsp:form> 
			</div>
						<!-- Order Summary for Desktop-->					
		<dsp:include page="/checkout/payment/includes/orderSummary.jsp" flush="true"></dsp:include>
<dsp:form style="display:none" id="autoBillPaymentFormCheckout"  name="autoBillPaymentFormCheckout" action="" method="post" >
<dsp:input bean="CricketAutoBillPaymentFormHandler.autoBillPayment" id="autoBillPaymentCheckout" name="autoBillPayment" type="hidden" value=""/>
<dsp:input bean="CricketAutoBillPaymentFormHandler.submitAutoBillPayment" id="submitAutoBillPaymentCheckout" type="submit" value=""/>		
</dsp:form> 
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
 <!--/#footer-utility-->
</div> <!--/#inner-wrap-->
</div> <!--/#outer-wrap-->
<!-- JavaScript -->	
<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="PaymentDetails"/>
</dsp:include>
<!-- Foundation 4 -->
<script src="${contextpath}/js/foundation/foundation.min.js"></script> 
<script>
$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips');
</script>
<script>
function submitFormPayment(formId){
 //alert(formId);
	document.getElementById(formId).submit()
}
</script>
<!-- Client Side Validation -->
<script src="${contextpath}/js/vendor/jquery.validate.min.js"></script>
<!-- SWIPER Library; Used for Phones and Plans on small screens -->
<script src="${contextpath}/lib/swiper/idangerous.swiper.js"></script>
<!-- Auto complete Plugin for Search -->
<script src="${contextpath}/lib/autocomplete/jquery.autocomplete.js" type="text/javascript"></script>
<!-- Cricket specific JS -->
<script src="${contextpath}/js/cricket.min.js"></script> <!-- Global Utilities -->
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
			<dsp:getvalueof var="customerTypeVar"  bean="/atg/cricket/util/CricketProfile.customerType"/>
			<c:set var="customerTypeVar" value="EXISTING" />
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="customerTypeVar"  value="NEW"/>
		</c:if>
		<c:if test="${empty customerTypeVar}">
			<dsp:getvalueof var="customerTypeVar"  value="null"/>
		</c:if>
<script type="text/javascript">
	var pageID = "Cart:Checkout:Payment";
	var categoryID = "Cart";
	//must be dynamic values					
	var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
	var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
	var network = '<dsp:valueof value='${network}'/>';
	var userIntention = '<dsp:valueof value='${userIntention}'/>';
	var customerType = '<dsp:valueof value='${customerTypeVar}'/>';
	var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
	cmCreatePageviewTag(pageID,categoryID,null,null,pvAttrs);
</script>
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
</body>
</html>
</dsp:page>