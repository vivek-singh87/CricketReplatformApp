<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/Switch" />
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/commerce/order/util/CricketPaymentData"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<dsp:importbean bean="/com/cricket/commerce/order/purchase/CommitOrderFormHandler" /> 
<dsp:importbean bean="/com/cricket/commerce/order/purchase/CricketCheckOutFromHandler" />
<dsp:importbean bean="/com/cricket/checkout/order/CricketShippingAddressData" />
<dsp:importbean bean="/atg/commerce/order/purchase/CommitOrderFormHandler" />
<dsp:importbean bean="/com/cricket/commerce/order/CricketTelesalesFormHandler" />
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:getvalueof var="customerType" bean='/atg/cricket/util/CricketProfile.customerType' />
<dsp:getvalueof var="networkProvider" bean='/atg/userprofiling/Profile.networkProvider' />
<dsp:getvalueof var="workOrderType" bean="ShoppingCart.current.workOrderType"/>
<dsp:include page="/common/head.jsp">
	<dsp:param name="seokey" value="confirmOrderKey" />
	 <dsp:param name="protocalType" value="secure"/>
</dsp:include>
<head>
<dsp:param name="seokey" value="confirmOrderKey" />
<title>Cricket</title>
<!--[if lte IE 8]>
<link rel="stylesheet" href="${contextpath}/css/fixed-width.css">
<![endif]-->
<!--[if gt IE 8]><!-->
<link rel="stylesheet" href="${contextpath}/lib/swiper/idangerous.swiper.min.css" />
<link rel="stylesheet" href="${contextpath}/css/cricket.min.css" />
<!--<![endif]-->
<script src="${contextpath}/js/vendor/custom.modernizr.js"></script>
<script src="${contextpath}/js/customcricketstore.js"></script>
<!-- Tagging Libraries -->
<!-- <script type="text/javascript" src="//nexus.ensighten.com/cricket/Bootstrap.js"></script> -->
</head>
<body>
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
<script type="text/javascript">
	var pageID = "Cart:Checkout:Confirm";
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
function submitform()
{
 /*  document.confirmOrder.submit(); */
}
function applyTelesalesCode(){
	var telesalesCode = document.getElementById('telesalesCode').value;
	document.getElementById('telesaleCode-remove').value = 'false';
	document.getElementById('telesaleCode-submit').value = telesalesCode; 	
	document.getElementById('telesalesForm').submit();
}
function removeTelesalesCode(){
	var telesalesCode = "";
	document.getElementById('telesaleCode-remove').value = 'true';
	document.getElementById('telesaleCode-submit').value = telesalesCode; 	
	document.getElementById('telesalesForm').submit();
}
</script>
<dsp:include page="includes/confirmMsgModal.jsp"/>
<div id="outer-wrap">
<div id="inner-wrap">
	<dsp:include page="/common/header.jsp">
	 <dsp:param name="protocalType" value="secure"/>
	</dsp:include>	
	 <!--/#header-->
<!--// START CONTENT AREA //-->	
<div id="constructor" class="section-checkout step3">
	<!-- Progress Bar Desktop-->
	<section id="section-progress">
			<dsp:include page="/checkout/includes/progressBar.jsp" flush="true">
			<dsp:param name="activeProgressBar" value="3"/>
		</dsp:include>
	</section> 
	<!--/#section-progess-->
	<!-- END Main Banner -->
	<!-- Checkout bill/ship info -->
	<section id="section-checkout-form">
		<div class="row">		
			<div class="large-12 small-12 columns details-content">
			<dsp:form id="customer-info-confirm-order" class="custom" name="confirmOrder" method="post" action="${contextpath}/thankyou/thankyou.jsp">
				<div class="large-8 small-12 columns form">
						<fieldset>
							<div class="row">
								<!-- All Customer Items (Shipping Address, Billing, Payment Info )-->
								<!-- All Customer Items (shown on Step 3)-->
								<!-- START Account Address Detail-->
									<dsp:include page="/checkout/shippingbilling/includes/nonEditHolderAddress.jsp" flush="true"></dsp:include>
								<!-- END Account Address Detail-->
								<!-- START Shipping Address-->
								<c:if test ="${workOrderType ne null && workOrderType ne 'RRC' }">
									<dsp:include page="/checkout/shippingbilling/includes/nonEditShippingAddress.jsp" flush="true"></dsp:include>
								</c:if>
								<!-- END Shipping Address-->
								<!-- START Billing Address-->
									<dsp:include page="/checkout/payment/includes/nonEditpaymentAddress.jsp" flush="true"></dsp:include>
								<!-- END Billing Address-->
								<!-- START Promotional code-->
								<div class="columns customer-info-item">
									<dsp:include page="/checkout/payment/includes/nonEditpromotionCode.jsp" flush="true"></dsp:include>
								</div>
								<!-- END Promotional Code-->
								<!-- START Shipping Method-->
								<c:if test ="${workOrderType ne null && workOrderType ne 'RRC' }">
								<div class="columns customer-info-item">
									<dsp:include page="/checkout/shippingbilling/includes/shippingMethod.jsp" flush="true"></dsp:include>
								</div>
								</c:if>
								<!-- END Shipping Method-->
								<!-- START Payment Information-->
								<dsp:getvalueof var="isDownGradePlan" bean="ShoppingCart.current.downGrade"/>
								<c:if test ="${isDownGradePlan ne null && isDownGradePlan ne 'true' }">
			  					  <div class="columns customer-info-item">
			  					   <dsp:include page="/checkout/payment/includes/nonEditpaymentInformation.jsp" flush="true"></dsp:include>
								  </div>
								</c:if>
								<!-- END Payment Information-->
							</div>
							<div class="row">
								<!-- Show only for New User - AutoBill payment section-->	
									<dsp:getvalueof var="abpConfirmOrder" bean="CricketPaymentData.abpFlag" />
											<c:if test="${abpConfirmOrder eq true && isDownGradePlan ne 'true'}">
												<div class="divider"></div>
												<dsp:include page="/cart/includes/autoBillPayment.jsp" flush="true"></dsp:include>
										</c:if>	 
							</div>
  					</fieldset>		
			</div>
				<div class="large-4 small-12 columns summary">
					<div class="row">
						<div>
							<p class="summary-drawer-content">
							<!-- Place your order button-->
							<dsp:a id="btn-checkout-step-3-top-custom" href="javascript:submitform()" class="button small">
		 						<crs:outMessage key="cricket_checkout_placeorder"/><!-- Place Your Order -->
								<dsp:input bean="CommitOrderFormHandler.commitOrder" type="hidden" value="Place order now"></dsp:input>
								
										<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
							  <dsp:param name="inUrl" value="${contextpath}/thankyou/thankyou.jsp"/>
							  <dsp:oparam name="output">
							    <dsp:input bean="CommitOrderFormHandler.commitOrderSuccessURL"
							         paramvalue="secureUrl" type="hidden"/>
							  </dsp:oparam>
							</dsp:droplet>
						<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
							  <dsp:param name="inUrl" value="${contextpath}/checkout/confirm/confirmOrder.jsp"/>
							  <dsp:oparam name="output">
							    <dsp:input bean="CommitOrderFormHandler.commitOrderErrorURL"
							         paramvalue="secureUrl" type="hidden"/>
							  </dsp:oparam>
							</dsp:droplet>
								
							<!-- 	<dsp:input bean="CommitOrderFormHandler.commitOrderSuccessURL" type="hidden" value="${contextpath}/thankyou/thankyou.jsp"/> 
								<dsp:input bean="CommitOrderFormHandler.commitOrderErrorURL" type="hidden" value="${contextpath}/checkout/confirm/confirmOrder.jsp"/>  -->  	
							</dsp:a>
							<a id="btn-checkout-step3-processing-top" class="button small processing disabled">Processing</a>			
 							</p>
						</div>
						<div class="divider package show-for-small summary-drawer-content"></div>
						
						<dsp:getvalueof var="workOrderType" bean='ShoppingCart.current.workOrderType' />
					 
							<table cellspacing="0" cellpadding="0" border="0">
								<thead>
									<tr>
									<th class="title">
									<!-- Estimate delivery time-->
									<a href="#" class="open-summary-drawer summary-drawer-content">
										<crs:outMessage key="cricket_checkout_order_summary"/><!-- Order Summary --> <br/>
										  <dsp:include page="/checkout/confirmorder/includes/display_deliveryDate.jsp" flush="true"></dsp:include>
									</a>
									</th>
									</tr>
								</thead>	
							</table>
					 
					</div>
					<div class="summary-drawer-content">
					<!-- Order summary-->
						<div class="row summary-drawer closed">
							  <dsp:include page="/checkout/confirmorder/includes/orderSummary.jsp" flush="true"></dsp:include>
						</div>
					</div>
					<div class="divider package"></div>	
					<div class="summary-drawer-content">
					<dsp:include page="/checkout/confirmorder/includes/display_totalOrderAmount.jsp">
						<dsp:param name="orderTotal" value="${orderTotal}" />
						<dsp:param name="monthlyTotal" value="${monthlyTotal}" />
					</dsp:include>						
						<div>					
							<!-- Place your order button-->
							<dsp:a id="btn-checkout-step-3-bottom-custom" href="javascript:submitform()" class="button small">
							<crs:outMessage key="cricket_checkout_placeorder"/> <!-- Place Your Order -->
							<dsp:input bean="CommitOrderFormHandler.commitOrder" type="hidden" value="Place order now"></dsp:input>
								<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
							  <dsp:param name="inUrl" value="${contextpath}/thankyou/thankyou.jsp"/>
							  <dsp:oparam name="output">
							    <dsp:input bean="CommitOrderFormHandler.commitOrderSuccessURL"
							         paramvalue="secureUrl" type="hidden"/>
							  </dsp:oparam>
							</dsp:droplet>
						<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
							  <dsp:param name="inUrl" value="${contextpath}/checkout/confirm/confirmOrder.jsp"/>
							  <dsp:oparam name="output">
							    <dsp:input bean="CommitOrderFormHandler.commitOrderErrorURL"
							         paramvalue="secureUrl" type="hidden"/>
							  </dsp:oparam>
							</dsp:droplet>
						<!-- <dsp:input bean="CommitOrderFormHandler.commitOrderSuccessURL" type="hidden" value="${contextpath}/thankyou/thankyou.jsp"/> 
							<dsp:input bean="CommitOrderFormHandler.commitOrderErrorURL" type="hidden" value="${contextpath}/confirm/confirmOrder.jsp"/>	-->				
							</dsp:a>
							<a id="btn-checkout-step3-processing-bottom" class="button small processing disabled">Processing</a>
						</div>
					</div>
				</div>
				</dsp:form>
				<br>
				<div class="row show-for-large-up">
				<dsp:include page="/checkout/includes/telesalesCode.jsp" flush="true"></dsp:include>
				</div>
				<dsp:form id="telesalesForm"  method="post" >
					<dsp:input bean="CricketTelesalesFormHandler.telesalesCode" id="telesaleCode-submit" name="telesaleCode" type="hidden" value=""></dsp:input>	
					<dsp:input bean="CricketTelesalesFormHandler.removeTelesalesCode" id="telesaleCode-remove" name="removeTelesaleCode" type="hidden" value=""></dsp:input>					
					<dsp:input bean="CricketTelesalesFormHandler.applyTelesalesCode" type="hidden" value="Enter Telesales Code"/>	
					<%-- <dsp:input bean="CricketTelesalesFormHandler.applyCouponSuccessURL" value="${contextpath}/checkout/payment/paymentDetails.jsp" type="hidden" />
					<dsp:input bean="CricketTelesalesFormHandler.applyCouponErrorURL" value="${contextpath}/checkout/payment/paymentDetails.jsp" type="hidden" />	 --%>
				</dsp:form> 
			</div>	
		</div>	
	</section>
</div> <!--/#constructor-->
<!--// END CONTENT AREA //-->
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
<script type="text/javascript" src="${contextpath}/js/vendor/jquery-ui.min.js"></script>
<script type="text/javascript" src='${contextpath}/js/chatGui-min.js'></script>
<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="OrderConfirmation"/>
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
</body>
</html>
</dsp:page>