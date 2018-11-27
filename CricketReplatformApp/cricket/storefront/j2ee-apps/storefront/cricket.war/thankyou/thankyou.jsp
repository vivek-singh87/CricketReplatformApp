<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
<dsp:importbean bean="/com/cricket/commerce/order/util/CricketPaymentData"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/com/cricket/commerce/order/CricketAccountHolderAddressData"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:getvalueof var="workOrderType" bean='ShoppingCart.last.workOrderType' />
 <dsp:getvalueof var="isDownGradePlan" bean="ShoppingCart.last.downGrade"/>
<dsp:include page="/common/head.jsp">
	<dsp:param name="seokey" value="thankYouKey" />
	 <dsp:param name="protocalType" value="secure"/>
</dsp:include>
<body>
<div id="outer-wrap">
<div id="inner-wrap">
<dsp:include page="/common/header.jsp">
 <dsp:param name="protocalType" value="secure"/>
	</dsp:include>
	 <!--/#header-->
<div id="constructor" class="section-checkout step3 thankyou">
  <section id="section-progress">
    <div class="section-progress-content">
      <div class="row">
        <div class="large-12 small-12 columns">
            <h1><crs:outMessage key="cricket_checkout_thankyou_fororder"/><!-- Thank you for your order --></h1>
        </div>    
      </div>  
    </div> <!--/.section-progress-content-->    
  <!-- Checkout bill/ship info -->
  <section id="section-checkout-form">
    <div class="row">   
      <div class="large-12 small-12 columns details-content">
        <!-- CONFIRM -->
        <div class="row">
          <div class="row receipt">
            <div class="large-3 small-12 columns show-for-small">
              <a id="print-receipt" href="#" class="button small" onclick="window.print(); return false;"><crs:outMessage key="cricket_checkout_print_receipt"/><%-- Print Receipt --%></a>
            </div>
            <div class="large-9 small-12 columns order-number">
              <h2><crs:outMessage key="cricket_checkout_your_ordernumber"/><!-- Your order number is: --><dsp:valueof bean="ShoppingCart.last.billingSystemOrderId"/>
			  </h2>
              <p>
				<crs:outMessage key="cricket_checkout_confirmation_emailsent"/>
				 <!--  A confirmation email has been sent to:  -->
				 <dsp:getvalueof var="email" bean="/atg/userprofiling/Profile.email"/>
				  <a href="#">
					<dsp:valueof value="${email}"/>.<br/></a><crs:outMessage key="cricket_checkout_emailmessage1"/> 
				 <!--  You will receive another email when your order has been shipped.  -->
			  </p>
            </div>
			
            <div class="large-3 small-12 columns hide-for-small">
              <a id="print-receipt" href="#" class="button small" onclick="window.print(); return false;">
				 <crs:outMessage key="cricket_checkout_print_receipt"/>
				  <!-- Print Receipt -->
			  </a>
            </div>
          </div>
        </div>
        <div class="row divider-thankyou">
          <div class="divider thankyou large-12 small-12"></div>
        </div>
        <!-- MAIN FORM -->
        <div class="row">
          <!-- MAIN FORM -->
          <div class="large-8 small-12 columns form">
            <form id="customer-info-thankyou" class="custom" name="customer-info" method="post" action="">
              <fieldset>
                <div class="row">
                  <!-- All Customer Items are shown in this version -->
				   <dsp:include page="/checkout/includes/nonEditaccountHolderAddress.jsp" flush="true"></dsp:include>
				  <c:if test ="${workOrderType ne null && workOrderType ne 'RRC' }">
				    <dsp:include page="/checkout/includes/nonEditshippingAddress.jsp" flush="true"></dsp:include>
				     </c:if>
					<dsp:include page="/checkout/includes/nonEditpaymentAddress.jsp" flush="true"></dsp:include>
				  <div class="columns customer-info-item">
				    <dsp:include page="/checkout/includes/nonEditpromotionCode.jsp" flush="true"></dsp:include>                   
                  </div>
                   <c:if test ="${workOrderType ne 'RRC' }">
                  <div class="columns customer-info-item">
                    <dsp:include page="/checkout/includes/shippingMethod.jsp" flush="true"></dsp:include>
                  </div>
                 </c:if>
                  <c:if test ="${isDownGradePlan ne null && isDownGradePlan ne 'true' }">
			  			 <div class="columns customer-info-item">
				   <dsp:include page="/checkout/includes/nonEditpaymentInformation.jsp" flush="true"></dsp:include>
                  </div>
				 </c:if>
                  
                </div>
                <div class="row">
                  <div class="large-12 small-12 columns">
                  
                  <dsp:getvalueof var="abpConfirmOrder"  bean="CricketPaymentData.abpFlag" />
					<c:if test="${abpConfirmOrder eq true && workOrderType ne 'RRC'}">
							<div class="divider"></div>
							<dsp:include page="/cart/includes/autoBillPayment.jsp" flush="true">
							<dsp:param name="fromPage" value="thankyou"/>
							</dsp:include>
					</c:if>	 
						<%-- <dsp:getvalueof var="abpFlag"   bean="/com/cricket/commerce/order/util/CricketPaymentData.abpFlag"/>           
						<c:if test="${abpFlag eq true}">
								<div class="divider"></div>
								<dsp:include page="/cart/includes/autoBillPayment.jsp" flush="true">
								<dsp:param name="fromPage" value="thankyou"/>
								</dsp:include>
						</c:if>	 --%>              
                  </div>
                </div>
              </fieldset>
            </form>
			 <div class="row">                  
              <div id="order-list" class="large-12 small-12 columns">
			   <!-- Showing cart -->
					<dsp:include page="/checkout/includes/nonEditshoppingCart.jsp" flush="true"></dsp:include>
				<!-- end accordian row -->
				</div>
			</div>
          </div>
          <!-- SUMMARY DESKTOP-->        
             <dsp:include page="/thankyou/includes/orderSummary.jsp" flush="true"></dsp:include>           
          <!-- Mobile layout for Overview -->
          </div>
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
<!-- jQuery -->	
<script src="${contextpath}/js/vendor/jquery-1.9.0.min.js"></script> 
<script type="text/javascript" src="${contextpath}/js/vendor/jquery-ui.min.js"></script>
<script type="text/javascript" src='${contextpath}/js/chatGui-min.js'></script>
  <dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="Thank You"/>
</dsp:include> 
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
		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="userIntention"  bean="/com/cricket/commerce/order/CricketAccountHolderAddressData.userIntentionVar"/>
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
<script type="text/javascript">
	var pageID = "Cart:Checkout:Thankyou";
	var categoryID = "CART";
	//must be dynamic values					
	var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
	var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
	var network = '<dsp:valueof value='${network}'/>';
	var userIntention = '<dsp:valueof value='${userIntention}'/>';
	var customerType = '<dsp:valueof value='${customerType}'/>';
	var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
	cmCreatePageviewTag(pageID,categoryID,null,null,pvAttrs);
</script>
<script type="text/javascript">	
	var all_productID ='';
	var all_productName ='';
	var all_quantity ='';
	var all_unitPrice ='';
	var totalAccessoryPrice ='';
	var producttypeCat ='';
	var afterDiscountUnitPrice ='';
</script>
<dsp:droplet name="/atg/dynamo/droplet/ForEach">
	<dsp:param name="array" bean="ShoppingCart.last.commerceItems"/>
	<dsp:param name="elementName" value="item"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="count" param="count"/>
			<dsp:getvalueof var="productId" param="item.auxiliaryData.productId"/>			
			<dsp:getvalueof var="quantity" param="item.quantity"/>
			<dsp:getvalueof var="unitPrice" param="item.priceInfo.listPrice"/>
			<c:set var="totalDiscountCoreMetrics" value="0" />	
			<dsp:droplet name="/atg/dynamo/droplet/ForEach">
				<dsp:param name="array" param="item.priceInfo.adjustments"/>
				<dsp:param name="elementName" value="adjustment"/>
				<dsp:oparam name="output">				
					<dsp:getvalueof var="adjustment" param="adjustment"/>
					<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${totalDiscountCoreMetrics+adjustment.totalAdjustment}" var="totalDiscountCoreMetrics" />									
				</dsp:oparam>
			</dsp:droplet>
			<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
				<dsp:param name="filterBySite" value="false"/>
				<dsp:param name="filterByCatalog" value="false"/>
				<dsp:param name="id" value="${productId}"/>
				<dsp:param name="elementName" value="product"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="producttype" param="product"/>
					<dsp:getvalueof var="parentCategoryId" value="${producttype.parentCategory.id}"/>
					<dsp:getvalueof var="productName" value="${producttype.displayName}"/>	
					<script type="text/javascript">
						var count = '<dsp:valueof value='${count}'/>';
						producttypeCat = '<dsp:valueof value='${producttype.type}'/>';
						if (count==1) {
								all_productID ='<dsp:valueof value='${productId}'/>';
								all_productName ='<dsp:valueof value='${productName}'/>';
								all_quantity ='<dsp:valueof value='${quantity}'/>';
								if (producttypeCat == 'accessory-product'){
									afterDiscountUnitPrice = '<dsp:valueof value='${totalDiscountCoreMetrics}'/>';
								 	totalAccessoryPrice = afterDiscountUnitPrice/${quantity};									
									all_unitPrice = totalAccessoryPrice;
								} else {
									all_unitPrice ='<dsp:valueof value='${totalDiscountCoreMetrics}'/>';
								 }							
						} else {
								all_productID += "|"+'<dsp:valueof value='${productId}'/>';
								all_productName += "|"+'<dsp:valueof value='${productName}'/>';
								all_quantity += "|"+'<dsp:valueof value='${quantity}'/>';														
								if (producttypeCat == 'accessory-product'){
									afterDiscountUnitPrice = '<dsp:valueof value='${totalDiscountCoreMetrics}'/>';
								 	totalAccessoryPrice = afterDiscountUnitPrice/${quantity};
									all_unitPrice += "|"+totalAccessoryPrice;
								} else {
									all_unitPrice += "|"+'<dsp:valueof value='${totalDiscountCoreMetrics}'/>';
								}								
						   } 
					</script>							
				</dsp:oparam>				
			</dsp:droplet>
				<input type="hidden" name="productId" id="productId" value="<dsp:valueof value='${productId}'/>" />
				<input type="hidden" name="productName" id="productName" value="<dsp:valueof value='${productName}'/>" />
				<input type="hidden" name="quantity" id="quantity" value="<dsp:valueof value='${quantity}'/>" />
				<input type="hidden" name="unitPrice" id="unitPrice" value="<dsp:valueof value='${unitPrice}'/>" />
				<input type="hidden" name="parentCategoryId" id="parentCategoryId" value="<dsp:valueof value='${parentCategoryId}'/>" />
						<dsp:include page="/cart/checkOutActionTagCart.jsp">
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
		</dsp:oparam>
</dsp:droplet>
<script type="text/javascript">
	cmDisplayShops();
</script>
<!-- Transaction Type JS Variables start -->
<script type="text/javascript">
		var transactionType = '<dsp:valueof bean='/atg/commerce/ShoppingCart.last.workOrderType'/>';
</script>
<!-- Transaction Type JS Variables ends -->
<!-- Ensighten JS Variables start -->
<script type="text/javascript">
		var ns_floodlight_order_id = '<dsp:valueof bean='/atg/commerce/ShoppingCart.last.billingSystemOrderId'/>';
		var ns_floodlight_order_total = '<dsp:valueof bean='/atg/commerce/ShoppingCart.last.priceInfo.total'/>';
</script>
<!-- Ensighten JS Variables ends -->

	<dsp:getvalueof var="orderShipping"  value="0.0"/>
<script type="text/javascript"> 
var userIntention = '<dsp:valueof value='${userIntention}'/>';
var orderNo = '<dsp:valueof bean='/atg/commerce/ShoppingCart.last.billingSystemOrderId'/>';
var orderSubTotal = '<dsp:valueof bean='/atg/commerce/ShoppingCart.last.priceInfo.total'/>';
var orderShipping = '<dsp:valueof value='${orderShipping}'/>';
var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
var state = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.state'/>';
var zip = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
var country = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.countryName'/>';
var emailId = '<dsp:valueof bean='/com/cricket/commerce/order/CricketAccountHolderAddressData.accountAddress.email'/>';
var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
var network = '<dsp:valueof value='${network}'/>';
var customerType = '<dsp:valueof value='${customerType}'/>';
var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
cmCreateOrderTag(orderNo, orderSubTotal, orderShipping, emailId, city, state, zip, pvAttrs);
// Call Registration tag:
cmCreateRegistrationTag(emailId, emailId, city, state, zip, country, pvAttrs);
$(document).ready(function() {
var isAbpFlag = '<dsp:valueof bean="/com/cricket/commerce/order/util/CricketPaymentData.abpFlag"/>';
	if(isAbpFlag!=false){	
		document.getElementById('autoBillPaymentMessageDesktop').style.display = 'block';
		document.getElementById('autoBillPaymentMessageMobile').style.display = 'block';
	} else {
		document.getElementById('autoBillPaymentMessageDesktop').style.display = 'none';
		document.getElementById('autoBillPaymentMessageMobile').style.display = 'none';
	}
});
</script>
</body>
</html>
</dsp:page>