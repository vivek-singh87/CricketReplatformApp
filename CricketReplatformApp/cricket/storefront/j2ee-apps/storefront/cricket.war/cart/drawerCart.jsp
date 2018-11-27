<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/user/session/UserSessionBean"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayShoppingCartDroplet"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayCheckoutButtonDroplet"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayMdnPhoneNumberDroplet"/>
<dsp:importbean bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/targeting/TargetingForEach"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/com/cricket/pricing/CricketCouponFormHandler"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<dsp:getvalueof var="queryStringurl" bean="OriginatingRequest.requestURIWithQueryString"/>
<dsp:importbean bean="/atg/cricket/util/CricketProfile"/>
<dsp:importbean bean="/atg/commerce/order/purchase/RepriceOrderDroplet"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="originatingRequestURL" bean="/OriginatingRequest.requestURI"/>
<dsp:getvalueof var="itemCount" bean="ShoppingCart.current.commerceItemCount"/>
<dsp:getvalueof var="orderPriceInfo" bean="ShoppingCart.current.priceInfo"/>
<dsp:getvalueof var="myAccountLogin" bean="CricketConfiguration.homePageLinks.myAccountLogin" />
<dsp:importbean bean="/com/cricket/cart/formHandler/CricketAutoBillPaymentFormHandler"/>
<dsp:importbean bean="/atg/cricket/util/CricketProfile"/>
<dsp:getvalueof var="userIntention" bean="UpgradeItemDetailsSessionBean.userIntention"/>
<dsp:getvalueof var="addLineIntention" bean="CartConfiguration.addLineIntention"/>
<c:set var="packageCount" value="0"/>
<c:if test="${itemCount > 0 && (null == orderPriceInfo || empty orderPriceInfo)}">
<dsp:droplet name="RepriceOrderDroplet">
  <dsp:param value="ORDER_SUBTOTAL" name="pricingOp"/>
</dsp:droplet>
</c:if>
<!-- START: CART HTML -->
 <dsp:droplet name="DisplayShoppingCartDroplet">
	<dsp:param name="order" bean="ShoppingCart.current"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="accessories"  param="accessories"/>	
		<dsp:getvalueof var="packages"  param="packages"/>
		<dsp:getvalueof var="upgradePhone"  param="upgradePhone"/>
		<dsp:getvalueof var="changePlan"  param="changePlan"/>
		<dsp:getvalueof var="changeAddons"  param="changeAddons"/>
		<dsp:getvalueof var="removedAddon"  param="removedAddon"/>
		<dsp:getvalueof var="requestURI" bean="/OriginatingRequest.requestURI"/>
	</dsp:oparam>
</dsp:droplet>
<dsp:droplet name="/com/cricket/browse/droplet/CricketCheckProductDroplet">
	<dsp:param name="modelNum" bean="CricketProfile.deviceModel"/>
	<dsp:param name="ratePlanCode" bean="CricketProfile.ratePlanCode"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="product"  param="product"/>
	</dsp:oparam>
	<dsp:oparam name="error">
		<dsp:getvalueof var="product"  param="product"/>
		<c:if test="${!product && userIntention ne addLineIntention}">
			<script>
			 window.onload = function(){
				 				sorryErrorModal();
				 					   };
			</script>
		</c:if>
	</dsp:oparam>
</dsp:droplet>

<html>
<body>
<script>

var contextpath ='<dsp:valueof bean="OriginatingRequest.contextPath"/>';
var cartFormIdForSubmit = '';
var clearUserIntention = '';
var removeItemsInCart = '';
var itemType = '';
var submitFromUserIntentionModal = '';
var deletePackageFormId = '';
var updateAccessoryQuatityFormId = '';
var abp = <dsp:valueof bean="UserSessionBean.autoBillPayment"/>;

function sorryErrorModal(){
	var url = window.location.href;
	if(url.indexOf("cell-phone-plans") > -1){
		$("#sorryErrorModal").click();
	}
	
}
$(function(){
	$("#autoBillPaymentForm").ajaxForm();
	});

function setAutoBill(val){
	if(val.checked==false){
		$('#autoBillPayment').val('false');
		document.getElementById('cartAutoBillPaymentMessage').style.display = 'none';		
	}
	if(val.checked==true){
		$('#autoBillPayment').val('true');
		document.getElementById('cartAutoBillPaymentMessage').style.display = 'block';
	}	
	submitAutoBillPaymentForm();
}

function submitAutoBillPaymentForm(){	
	$('#submitAutoBillPaymentSubmit').trigger('click');
	}
	
 function createMask() {
	var mask = $('<div></div>')
	  .css({
	    position: 'absolute',
	    width: '100%',
	    height: '100%',
	    top: 0,
	    left: 0,
	    'z-index': 10000
	  })
	  .appendTo(document.body)
	  .click(function(event){
	    event.preventDefault();
	    return false;
	   })
	   return mask;
}
var mask = '';
function submitFormAddToCart(formId){

	//showSpinner('outer-wrap');
	var isAnonymousUser = '<dsp:valueof bean="Profile.transient"/>';
	var userIntention = '<dsp:valueof bean="UpgradeItemDetailsSessionBean.userIntention"/>';
	var addLineIntention = '<dsp:valueof bean="CartConfiguration.addLineIntention"/>';
	var customerType = '<dsp:valueof bean="CricketProfile.customerType"/>';
	cartFormIdForSubmit = formId;
	if (isAnonymousUser != null && isAnonymousUser != '' && isAnonymousUser == 'false'
			&& (userIntention == null || userIntention == '') && submitFromUserIntentionModal != 'true') {
		itemType = $("#"+formId).find("#itemType").val();
		if (itemType != null && itemType != '' && (itemType == 'phone-product' || itemType == 'plan-product' || itemType=='addOn-product')) {
			if (itemType == 'phone-product') {
				$("#phoneSelectionMessage").show();
				$("#phoneUpgradeExistingLOS").show();
				$("#phoneUpgradelogout").show();
			}
			if (itemType == 'plan-product') {
				$("#planSelectionMessage").show();
				$("#planUpgradeExistingLOS").show();
				$("#planUpgradelogout").show();
			}
			if (itemType == 'addOn-product') {
				$("#addonSelectionMessage").show();
				$("#addonUpgradeExistingLOS").show();
				$("#addonUpgradelogout").show();
			}
			if(customerType != null && (customerType == 'O' || customerType == 'H')){
				$("#sorryErrorModal").click();
			}else{
				$("#userIntentionModal").click();
			}
			return false;
		}
	}
	var product = document.getElementById('productSorryErrorValue').value;
	var url = window.location.href;
	if(product == 'false' && url.indexOf("cell-phone-plans") > -1 && userIntention != addLineIntention){
		$("#sorryErrorModal").click();
	}else{
		mask = createMask();
		$("#acLoadingCustom").show();
	    var options = {
	            success: successMethod,
	            error: errorMethod,
	            dataType: "json"
	        };
	        $('#' + formId).ajaxSubmit(options);
	}
	

}
function reLoadPage(urlValue){
	if(urlValue.indexOf("openCart") > -1) {		
		if (urlValue.indexOf("#") > -1) {
			urlValue = urlValue.replace("#","");
		}
	   //window.location.reload(); 
	   window.location.href=urlValue;
	   return false;
	} else if (urlValue.indexOf("?") > -1) {
		if (urlValue.indexOf("#") > -1) {
			var newurl = urlValue.replace("#","&openCart=true");
			window.location=newurl;
			return false;
			//window.location.reload();  
		} else {
			urlValue += '&openCart=true';
			window.location=urlValue;
			//window.location.reload();  
			return false;
		}	
	}else{
		if (urlValue.indexOf("#") > -1) {
			var newurl = urlValue.replace("#","?openCart=true");
			window.location=newurl;
			return false;
		} else {
			urlValue += '?openCart=true';
			window.location=urlValue;
			//window.location.reload();  
			return false;
		}
	}
}
function successMethod(responseText, statusText, xhr, $form)  {
	var showSpinner = 'true';
	if (responseText.success == 'true') {
		//alert('sucess' + responseText.redirectURL);
		if (responseText.redirectURL != null && responseText.redirectURL != '') {
			var url = responseText.redirectURL;	
			if(responseText.redirectURL.indexOf("amp;") > -1){
				var regex = new RegExp("amp;", 'g');
				url = url.replace(regex, '');
			}
			if(responseText.isCartOpen == 'true'){
				reLoadPage(url);
			}
			else{
				window.location.href=url;
			}
		} else {
			reLoadPage(window.location.href);
		}
	} else if (responseText.success == 'false') {
		//alert('fail');
		//$("#testId").html("hi");
		var message = '';
		if (responseText.isModalError == 'true') {
			//alert('modalError');		
			$.each(responseText.formExceptions, function( key, value ) {
				$.each(value, function( key1, value1 ) {
					message = value1; 
				});
			});
			if (message != '') {
				var SorryModalWindow = 'false';
				if (message == 'addOtherProductsInUpgradePhoneFlow' 
						|| message == 'addOtherProductsInChangePlanFlow'
						|| message == 'addOtherProductsInChangeFeatureFlow'
						|| message == 'completeUpgradePhoneTransaction'
						|| message == 'completeChangePlanTransaction'
						|| message == 'completeChangeFeatureTransaction') {
					clearUserIntention = 'true';
					removeItemsInCart = 'true';
				}
				if (message == 'completePackageTransactionFirst') {
					removeItemsInCart = 'true';	
				}
				if (message == 'completePackageTransactionFirst') {
					removeItemsInCart = 'true';	
				}
				if (message == 'payGoCustUpgradePhoneError' 
						|| message == 'payGoCustUpgradePlanError'
						|| message == 'payGoCustAddLineError'
						|| message == 'hargrayCustAddLineError'
						|| message == 'fourPFivePlanRatePlanChangeError') {
					SorryModalWindow = 'true';
				}
				if (SorryModalWindow == 'true') {
					$("#sorryErrorModal").click();
				} else {
					var prompt = $("#"+message).find("#promptMessage").html();
					var option1 = $("#"+message).find("#option1").html();
					var option2 = $("#"+message).find("#option2").html();
					$("#notification-style-1-b").find("#promptId").html(prompt);
					$("#notification-style-1-b").find("#option1Display").html(option1);
					$("#notification-style-1-b").find("#option2Display").html(option2);
					$("#openErrorModal").click();
				}
			}
		} else {
			//alert(' not modalError');
			$.each(responseText.formExceptions, function( key, value ) {
				$.each(value, function( key1, value1 ) {
					message = message + value1; 
				});
			});
			var reloadDrawerCartDiv = responseText.reloadDrawerCartDiv;			
			if (reloadDrawerCartDiv == 'true') {
				showSpinner = 'false';
				$("#li-cart").load(contextPath+"/common/includes/topNavigation.jsp #li-cart");
				$("#drawer-cart").load(contextPath+"/cart/drawerCart.jsp #ajaxReloadContent", function() {
					$("#ErrorMessageDiv").html("<strong>Notification:</strong>&nbsp;" + message);
					$("#errorNotificationDiv").show();
					restoreLostBindingsForCart();
					mask.remove();
					$("#acLoadingCustom").hide();
					$("#errorNotificationDiv").focus();
					if (!$('#drawer-cart').hasClass('open')) {
							  $("#cart-img").click();
						}
				});
			} else {
				if (responseText.redirectURL != null && responseText.redirectURL != '') {
					window.location.href=responseText.redirectURL;
				} else {
				  if (responseText.redirectURL != null && responseText.redirectURL != '') {
					  window.location.href=responseText.redirectURL;
					} else {
						$("#ErrorMessageDiv").html("<strong>Notification:</strong>&nbsp;" + message);
						$("#errorNotificationDiv").show();
						$("#errorNotificationDiv").focus();
						if (!$('#drawer-cart').hasClass('open')) {
							  $("#cart-img").click();
						}
					}
				}
			}
		}
	}
	//modalAlert(this);
	if(showSpinner=='true'){
	mask.remove();
		$("#acLoadingCustom").hide();
	}
}


function restoreLostBindingsForCart() {
       $("#li-location a, .close-location, .alert-box a:not(.no-drawer), .open-zip-drawer").on("click",function(){openLocationDrawer();});
       $("#li-cart a, .close-cart").on("click",function(){openCartDrawer();});
       $("#li-search a, .close-search").on("click",function(){openSearchDrawer();});
       $(".acLoading").hide();
       $(".search-new").keyup(function(){var searchTerm = $(this).val();if($(this).val().length > 2){populateTypeAheadResults();}else {$(".results-dropdown").hide();$(".acLoading").hide();document.getElementById('typeAheadSearchResultsUL').innerHTML="";}});
       //alert(accessoryListing);
       if(accessoryListing = "accessoryListing") {
              $(".sub-nav dd a").on("click",function(e){e.preventDefault();if($(".hide-for-small").is(":visible")){$(".sub-nav dd").removeClass("active");$(this).parent().addClass("active");if($(".row.bydevice").hasClass("hide")){$(".row.bydevice").removeClass("hide");}else{$(".row.bydevice").addClass("hide");}}});
       }
       $("#drawer-cart .accordion section p.title").on("click",function (e) {
           e.preventDefault();
           if ($(this).parent().hasClass("active")) {
               $(this).parent().removeClass("active");
           } else {
               $(this).parent().addClass("active");
           }
       });
}

function errorMethod(responseText, statusText, xhr, $form)  {    
	//alert('failure');
	mask.remove();
	$("#acLoadingCustom").hide();
	 window.location.reload(); 
	}


function ApplyPromotion(){

	var PageLocation = document.URL;
	PageLocation = PageLocation.replace("&editCoupon=true","");
	PageLocation = PageLocation.replace("?editCoupon=true","");	
	var applyCouponURL = PageLocation.replace("#","");
	if (applyCouponURL.indexOf("?") != -1) {
		if (applyCouponURL.indexOf("openCart=true") != -1) {
			$('#applyCouponURL1').val(applyCouponURL);
		} else {
			$('#applyCouponURL1').val(applyCouponURL + "&openCart=true");
		}				
	} else {
		$('#applyCouponURL1').val(applyCouponURL + "?openCart=true");		
	}
	document.cartPromotion.action = document.getElementById('applyCouponURL1').value;
	var cartPromoCode = document.getElementById('promo-code-cart').value;	
	document.getElementById('cartPromoCode-submit').value = cartPromoCode;
	document.getElementById('cartPromotion').submit();
}

function submitFormUpgrade(formId){
	deletePackageFormId = formId;
	$("#packageRemoveModal").click();
	//document.getElementById(formId).submit();
}

</script>
<dsp:include page="/cart/includes/updatingAccessoryQuantityToZeroConfirmationModal.jsp"/>
<dsp:include page="/cart/includes/updatingAccessoryQuantityExceedingInventory.jsp"/>
<dsp:include page="/cart/includes/duplicatingPackageExceedingInventory.jsp"/>
<dsp:include page="/cart/includes/couponErrorModal.jsp"/>
<dsp:include page="/cart/includes/removePackageConfirmationModal.jsp"/>
<dsp:include page="/cart/includes/userIntentionSelectionModal.jsp"/>
<dsp:include page="/cart/includes/sorryErrorModal.jsp"/>
<dsp:include page="/cart/includes/errorModal.jsp"/>
<dsp:include page="/cart/includes/errorMessages.jsp"/>
<input type="hidden" name="productSorryErrorValue" id="productSorryErrorValue" value="${product}"/>
<!-- Drawers for Cart and Search -->	

	<div id="drawer-cart" class="drawer-utility drawer">
	<span id="ajaxReloadContent">
		<div class="drawer-wrapper">
			<div class="row">
				<div class="large-12 small-12 columns cart-container">
					
					<div class="close-drawer close-cart">
						<a href="#"> <crs:outMessage key="cricket_shoppingcart_closecart_symbol"/><!-- X --></a>
					</div>	
				<!--NOTE: Show this is new customer/not logged in -->	
					<div class="customer-check">
						<p>
						<dsp:droplet name="Switch">
						<dsp:param name="value" bean="Profile.transient"/>
							<dsp:oparam name="false">
									For full account details go to
									<br>
									<a href="${myAccountLogin}">My Account page</a>
							</dsp:oparam>
							<dsp:oparam name="true">
								 <crs:outMessage key="cricket_shoppingcart_alreadycustomer"/><!--  Already a customer? --><br/>
								<a href="${myAccountLogin}"><crs:outMessage key="cricket_shoppingcart_login"/><!-- Log In --></a>
							</dsp:oparam>
						</dsp:droplet>
						</p>
					</div>
										
					<header>
						<h3> <crs:outMessage key="cricket_shoppingcart_your"/><!-- Your --> <span> <crs:outMessage key="cricket_shoppingcart_shopping_cart"/><!-- Shopping Cart --></span></h3>
						
						<!-- droplet used to show form Notification from Server -->
						<dsp:droplet name="/atg/dynamo/droplet/Switch">
							<dsp:param bean="CartModifierFormHandler.formError" name="value"/>
							<dsp:oparam name="true">
								<dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
									<dsp:param name="exceptions" bean="CartModifierFormHandler.formExceptions" />
									<dsp:oparam name="output">
										<dsp:getvalueof var="errorMessage" param="message"/>
										<div class="row">
											<div class="large-12 small-12 columns">
												<div data-alert class="alert-box">
													<span class="alert-icon"><strong> <crs:outMessage key="cricket_shoppingcart_notification"/><!-- Notification: --></strong>${errorMessage} </span>
												</div>
											</div>
										</div>	
									</dsp:oparam>
								</dsp:droplet>
							</dsp:oparam>
						</dsp:droplet>
							<div class="row" id="errorNotificationDiv" tabindex="1" style="display:none">
											<div class="large-12 small-12 columns">
												<div data-alert class="alert-box">
													<span class="alert-icon" id="ErrorMessageDiv"></span>
												</div>
											</div>
										</div>	
										<c:if test="${specialURLError != null && specialURLError != ''}">
										<div class="row" id="errorNotificationDiv">
											<div class="large-12 small-12 columns">
												<div data-alert class="alert-box">
													<span class="alert-icon" id="ErrorMessageDiv">${sessionScope.specialURLError}</span>
												</div>
											</div>
										</div>	
										<c:remove var="specialURLError"/>  
										</c:if>
					<!--NOTE: Display Upsell promotional messages -->
							<dsp:droplet name="/atg/commerce/promotion/ClosenessQualifierDroplet">
								<dsp:param name="type" value="all" />
								<dsp:param name="elementName" value="closenessQualifiers" />
								<dsp:oparam name="output">
									<dsp:droplet name="/atg/dynamo/droplet/ForEach">
										<dsp:param name="array" param="closenessQualifiers" />
										<dsp:param name="elementName" value="closenessQualifier" />
										<dsp:oparam name="output">
											<dsp:droplet
												name="/com/cricket/browse/droplet/PromotionUpsellAmountDroplet">
												<dsp:param name="promotionId"
													param="closenessQualifier.promotion.id" />
												<dsp:oparam name="output">
													<dsp:getvalueof var="Amount" param="upsellAmount" />
													<fmt:formatNumber type="number" maxFractionDigits="2"
														minFractionDigits="2" value="${Amount}" var="Amount" />
													<c:set var="splitPrice" value="${fn:split(Amount, '.')}" />
													<c:set var="upsellAmount" value="${splitPrice[0]}" />
												</dsp:oparam>
											</dsp:droplet>
											<%--    <dsp:getvalueof var="spendValue" param="closenessQualifier.promotion.templateValues.spend_value"/> --%>
											<dsp:getvalueof var="message" param="closenessQualifier.name" />
											<dsp:getvalueof var="orderValue"
												bean="ShoppingCart.current.priceInfo.total" />
											<fmt:formatNumber type="number" maxFractionDigits="0"
												minFractionDigits="0" value="${upsellAmount-orderValue}"
												var="balanceAmt" />
											<c:set var="message"
												value="${fn:replace(message,'<amount>', balanceAmt)}" />
											<div class="row">
												<div class="large-12 small-12 columns">
													<div data-alert class="alert-box">
														<span class="alert-icon"><strong><crs:outMessage
																	key="cricket_shoppingcart_notification" />
																<!-- Notification: --></strong>&nbsp;${message}</span>
													</div>
												</div>
											</div>
										</dsp:oparam>
									</dsp:droplet>
								</dsp:oparam>
							</dsp:droplet>

							<dsp:getvalueof var="itemCount" bean="ShoppingCart.current.commerceItemCount"/>
						<c:choose>
							<c:when test="${itemCount eq '0' && isUserLoggedIn}">
								<dsp:getvalueof var="firstName" bean="/atg/cricket/util/CricketProfile.firstName"/>
								<dsp:getvalueof var="lastName" bean="/atg/cricket/util/CricketProfile.lastName"/>
								<p>Welcome ${firstName} ${lastName}. Upgrade your phone, change your plan or add a new line of service below.</p>
							</c:when>						
							<c:when test="${itemCount eq '0' }">
								<p><crs:outMessage key="cricket_shoppingcart_emty_message"/><!-- Your shopping cart is empty. Get started by selecting a phone. --></p>
							</c:when>
							<c:otherwise>
								<c:forEach var="pack" items="${packages}">
									<dsp:getvalueof var="phone" value="${pack.value.phoneCommerceItem}"/>
									<dsp:getvalueof var="plan" value="${pack.value.planCommerceItem}"/>
								</c:forEach>
								<c:if test="${!empty phone && empty plan}">
									<p><crs:outMessage key="cricket_shoppingcart_emtyplan_message"/><!-- Now that you have selected a phone, complete your package by adding a plan. --></p>
								</c:if>
								<c:if test="${empty phone && !empty plan}">
									<p><crs:outMessage key="cricket_shoppingcart_emtyphone_message"/><!-- Now that you have selected a plan, complete your package by adding a phone. --></p>
								</c:if>
								<c:if test="${!empty phone && !empty plan}">
									<p><crs:outMessage key="cricket_shoppingcart_emtyaddon_message"/><!-- Now that you have selected a phone and a plan: add accessories or add-ons, add another package or check out. --></p>
								</c:if>
								<c:if test="${!empty accessories && empty phone &&  empty plan}">
									<p><crs:outMessage key="cricket_shoppingcart_addphone_message"/><!-- Add a phone and plan to your accessory. --></p>
								</c:if>
							</c:otherwise>
						</c:choose>
						
					</header>

					<div class="section-container accordion">
					<!-- Package 1 -->	
					<dsp:droplet name="ForEach">
					<dsp:param name="array" value="${packages}"/>
					<dsp:oparam name="output">
						<section id="package1" class="active">
							<p class="title" data-section-title>
								<a href="#" class="package-title"><crs:outMessage key="cricket_shoppingcart_package"/><!-- Package --> <dsp:valueof param="count"/>
									<span class="package-title-full">: 
										<dsp:droplet name="ProductLookup">
											<dsp:param name="id" param="element.phoneCommerceItem.productId"/>
											<dsp:param name="elementName" value="phone"/>
											<dsp:oparam name="output">
												<dsp:valueof param="phone.displayName"><crs:outMessage key="cricket_shoppingcart_select_phone"/><!-- Select Phone --></dsp:valueof> |
											</dsp:oparam>
										</dsp:droplet>
										<dsp:droplet name="ProductLookup">
											<dsp:param name="id" param="element.planCommerceItem.productId"/>
											<dsp:param name="elementName" value="plan"/>
											<dsp:oparam name="output">
												<dsp:valueof param="plan.displayName"><crs:outMessage key="cricket_shoppingcart_select_plan"/><!-- Select Plan --></dsp:valueof> |
											</dsp:oparam>
										</dsp:droplet>
										<crs:outMessage key="cricket_shoppingcart_selected_addons"/><!-- Selected Add-Ons -->
									</span>
								</a>
								<c:set var="packageId">
								<dsp:valueof param="element.packageId"/>
								</c:set>
								<dsp:getvalueof var="packagePhoneSkuId" param="element.phoneCommerceItem.catalogRefId"/>
								<dsp:getvalueof var="countOfThePackage" param="count"/>
								<input type="hidden" id="packagePhoneSkuId_${countOfThePackage}" value="${packagePhoneSkuId}"/>
								<a href="#" class="duplicate-label" onclick="submitDuplicateCartForm('duplicateCart${packageId}', '${countOfThePackage}');"><crs:outMessage key="cricket_shoppingcart_duplicate"/><!-- Duplicate --></a><a href="#" class="delete-label" onclick="submitDeleteCartForm('deleteCart${packageId}');"><crs:outMessage key="cricket_shoppingcart_delete"/><!-- Delete --></a>
							<dsp:form name="duplicateCart${packageId}" id="duplicateCart${packageId}" formid="duplicateCart${packageId}">
								<dsp:input type="hidden" bean="CartModifierFormHandler.duplicatePackageId" value="${packageId}"/>
								<dsp:input type="hidden" id="duplicateSuccessUrl" bean="CartModifierFormHandler.duplicatePackageSuccessUrl"/>
								<dsp:input type="hidden" id="duplicateErrorUrl" bean="CartModifierFormHandler.duplicatePackageErrorUrl"/> 
								<dsp:input type="hidden" bean="CartModifierFormHandler.duplicatePackage" value="submit"/>
							</dsp:form>
								
							<dsp:form name="deleteCart${packageId}" id="deleteCart${packageId}" formid="deleteCart${packageId}">
								<dsp:input type="hidden" bean="CartModifierFormHandler.deletePacakgeId" value="${packageId}"/>
								<dsp:input type="hidden" id="deleteSuccessUrl" bean="CartModifierFormHandler.deletePackageSuccessURL" />
								<dsp:input type="hidden" id="deleteErrorUrl" bean="CartModifierFormHandler.deletePackageFailureURL" /> 
								<dsp:input type="hidden" bean="CartModifierFormHandler.deletePackage" value="submit"/>
							</dsp:form>
							</p>
							<div class="content clear" data-section-content>
								<div class="row">
								<!-- packages details -->
								<dsp:include page="/cart/includes/display_packageLineItem.jsp">
									<dsp:param name="packages" param="element"/>
								</dsp:include>
								<!-- packages price details -->
								<dsp:include page="/cart/includes/display_packagePrice.jsp">
									<dsp:param name="packages" param="element"/>
									<dsp:param name="pkgCount" param="count"/>
								</dsp:include>
								<c:set var="packagesTotal" value="${packagesTotal + totalAmt}"/>
								<c:set var="monthlyPkgTotal" value="${monthlyPkgTotal + totalMonthlyPlanAmt}"/>
								</div>
							</div> <!--/.content-->
						</section>
					</dsp:oparam>
					<dsp:oparam name="empty">
						<!-- packages section -->
						
						<c:if test="${empty packages && empty upgradePhone && empty changePlan && empty changeAddons && empty accessories && empty removedAddon}">
							<c:if test="${!isUserLoggedIn}">
							<section id="package1" class="active">
								<p class="title" data-section-title><a href="#" class="package-title"><crs:outMessage key="cricket_shoppingcart_package1"/><!-- Package 1 --></a></p>
								<div class="content clear" data-section-content>
									<div class="row">
									<!-- packages details -->
									<dsp:include page="/cart/includes/display_packageLineItem.jsp">
										<dsp:param name="packages" value="${packages}"/>
									</dsp:include>
									
									<!-- packages price details -->
									<dsp:include page="/cart/includes/display_packagePrice.jsp"></dsp:include>
									</div>
								</div> <!--/.content-->
							</section>
							</c:if>
							<dsp:include page="/cart/includes/addAnotherPackage.jsp"></dsp:include>
						</c:if>						
					<!-- upgrade Phone section -->
						<c:if test="${!empty upgradePhone}">
						<dsp:droplet name="ForEach">
						<dsp:param name="array" value="${upgradePhone}"/>
						<dsp:oparam name="output">
						<c:set var="upgradePhoneId">
								<dsp:valueof param="element.Id"/>
						</c:set>
							<section id="package1" class="active">
							<p class="title"><a href="#" class="package-title"><crs:outMessage key="cricket_shoppingcart_upgradephone_for"/><!-- Upgrade Phone For: --> 
							<dsp:droplet name="DisplayMdnPhoneNumberDroplet">
							<dsp:param name="profileMdn" bean="ShoppingCart.current.upgradeMdn"/>
							<dsp:oparam name="empty">
							</dsp:oparam>
							<dsp:oparam name="output">
							<dsp:valueof param="formatedMdnNumber"/>
							</dsp:oparam>
							</dsp:droplet>
							</a><a href="#" class="delete-label" onclick="submitFormUpgrade('RemoveCart${upgradePhoneId}');"><crs:outMessage key="cricket_shoppingcart_cancel"/><!-- Cancel --></a></p>
							<div class="content">
								<div class="row">
								<!-- upgrade Phone details -->
								<dsp:include page="/cart/includes/display_upgradePhoneLineItem.jsp">
									<dsp:param name="upgradePhone" value="${upgradePhone}"/>
								</dsp:include> 
								<!-- upgrade Phone price details -->
								<dsp:include page="/cart/includes/display_upgradePhonePrice.jsp">
									<dsp:param name="upgradePhone" value="${upgradePhone}"/>
								</dsp:include>
								<c:set var="upgradePhoneTotal" value="${upgradePhoneTotal + upgradePhoneAmt}"/>
								</div>
								<dsp:form name="RemoveCart${upgradePhoneId}" id="RemoveCart${upgradePhoneId}" formid="RemoveCart${upgradePhoneId}">
									<dsp:input type="hidden" bean="CartModifierFormHandler.removalCommerceIds" value="${upgradePhoneId}"/>
									<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemType" beanvalue="CartConfiguration.upgradePhoneIntention"/>
									<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemFromOrder" value="submit"/>
								</dsp:form>
							</div> <!--/.content-->
						</section>		
						</dsp:oparam>
						</dsp:droplet>				
						</c:if>	
						<!-- change Plan section -->
						<c:if test="${!empty changePlan}">
								<dsp:droplet name="ForEach">
								<dsp:param name="array" value="${changePlan}"/>
								<dsp:oparam name="output">
								<dsp:getvalueof var="count" param="count">
								<c:set var="changePlanId">
										<dsp:valueof param="element.Id"/>
								</c:set>
								<c:if test="${count == 1}">
									<section id="package1" class="active">
									<p class="title"><a href="#" class="package-title"><crs:outMessage key="cricket_shoppingcart_plan_upgradefor"/><!-- Plan Upgrade For: --> 
									<dsp:droplet name="DisplayMdnPhoneNumberDroplet">
									<dsp:param name="profileMdn" bean="ShoppingCart.current.upgradeMdn"/>
									<dsp:oparam name="empty">
									</dsp:oparam>
									<dsp:oparam name="output">
									 <dsp:valueof param="formatedMdnNumber"/>
									</dsp:oparam>
									</dsp:droplet></a><a href="#" class="delete-label"  onclick="submitFormUpgrade('RemoveCart${changePlanId}');"><crs:outMessage key="cricket_shoppingcart_cancel"/><!-- Cancel --></a></p>
									<div class="content">
										<div class="row">
										<!-- upgrade Phone details -->
										<dsp:include page="/cart/includes/display_upgradePlanLineItem.jsp">
											<dsp:param name="changePlan" value="${changePlan}"/>
											<dsp:param name="removedAddon" value="${removedAddon}"/>
										</dsp:include>
										<!-- upgrade Phone price details -->
										<dsp:include page="/cart/includes/display_upgradePlanPrice.jsp">
											<dsp:param name="changePlan" value="${changePlan}"/>
											<dsp:param name="removedAddon" value="${removedAddon}"/>
										</dsp:include>
										<c:set var="upgradePlanTotal" value="${upgradePlanTotal + upgradePlanAmt}"/>
										<c:set var="monthlyUpgardeTotal" value="${monthlyUpgardeTotal + totalUpgradePlanAmt}"/>
										</div>
										<dsp:form name="RemoveCart${changePlanId}" id="RemoveCart${changePlanId}" formid="RemoveCart${changePlanId}">
											<dsp:input type="hidden" bean="CartModifierFormHandler.removalCommerceIds" value="${changePlanId}"/>
											<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemType" beanvalue="CartConfiguration.upgradePlanIntention"/>
											<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemFromOrder" value="submit"/>
										</dsp:form>
									</div> <!--/.content-->
								</section>
								</c:if>
								</dsp:getvalueof>
								</dsp:oparam>
								</dsp:droplet>								
						</c:if>
						<!-- change Addons section -->
						<c:if test="${!empty changeAddons || (empty changePlan && !empty removedAddon)}">
							<c:choose>
								<c:when test="${!empty changeAddons }">
									<dsp:getvalueof var="addons"  value="${changeAddons}"/>
								</c:when>
								<c:otherwise>
									<dsp:getvalueof var="addons"  value="${removedAddon}"/>
								</c:otherwise>
							</c:choose>
								<dsp:droplet name="ForEach">
								<dsp:param name="array" value="${addons}"/>
								<dsp:oparam name="output">
								<dsp:getvalueof var="count" param="count">
								<c:set var="changeAddonId">
										<dsp:valueof param="element.Id"/>
								</c:set>
								<c:if test="${count == 1}">
									<section id="package1" class="active">
									<p class="title"><a href="#" class="package-title"><crs:outMessage key="cricket_shoppingcart_addon_changefor"/><!-- Add-On Change For:  -->
									<dsp:droplet name="DisplayMdnPhoneNumberDroplet">
									<dsp:param name="profileMdn" bean="ShoppingCart.current.upgradeMdn"/>
									<dsp:oparam name="empty">
									</dsp:oparam>
									<dsp:oparam name="output">
										<dsp:valueof param="formatedMdnNumber"/>
									</dsp:oparam>
									</dsp:droplet>
									</a><a href="#" class="delete-label" onclick="submitFormUpgrade('RemoveCart${changeAddonId}');"><crs:outMessage key="cricket_shoppingcart_cancel"/><!-- Cancel --></a></p>
									<div class="content">
										<div class="row">
										<!-- upgrade Phone details -->
										<dsp:include page="/cart/includes/display_upgradeAddonsLineItem.jsp">
											<dsp:param name="changeAddons" value="${changeAddons}"/>
											<dsp:param name="removedAddon" value="${removedAddon}"/>
										</dsp:include>								
										<!-- upgrade Phone price details -->
										<dsp:include page="/cart/includes/display_upgradeAddonsPrice.jsp">
											<dsp:param name="changeAddons" value="${changeAddons}"/>
											<dsp:param name="removedAddon" value="${removedAddon}"/>
										</dsp:include>
										<c:set var="upgradeAddonsTotal" value="${upgradeAddonsTotal +upgradeAddonAmt }"/>
										<c:set var="monthlyUpgradeAddonsTotal" value="${monthlyUpgradeAddonsTotal + totalMonthlyAddonAmt}"/>
										</div>
										<dsp:form name="RemoveCart${changeAddonId}" id="RemoveCart${changeAddonId}" formid="RemoveCart${changeAddonId}">
											<dsp:input type="hidden" bean="CartModifierFormHandler.removalCommerceIds" value="${changeAddonId}"/>
											<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemType" beanvalue="CartConfiguration.upgradeAddonIntention"/>
											<dsp:input type="hidden" bean="CartModifierFormHandler.removeItemFromOrder" value="submit"/>
										</dsp:form>
									</div> <!--/.content-->
								</section>	
								</c:if>
								</dsp:getvalueof>
								</dsp:oparam>
								</dsp:droplet>				
						</c:if>
					</dsp:oparam>
					<dsp:oparam name="outputEnd">
					<dsp:getvalueof var="size" param="size" />
						<c:if test="${size != 0 && size < 5 && packageCount == 0}">
							<c:set var="packageCount" value="${packageCount + 1}"/>
							<dsp:include page="/cart/includes/addAnotherPackage.jsp"></dsp:include>
						</c:if>
					</dsp:oparam>
					</dsp:droplet>
			      		<dsp:droplet name="DimensionValueCacheDroplet">
							<dsp:param name="repositoryId" bean="CricketConfiguration.accessoriesCategoryId"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="accessoriesCategoryCacheEntry" param="dimensionValueCacheEntry" />
					      </dsp:oparam>
						</dsp:droplet>
						<c:choose>
							<c:when test="${accessories eq null }">
								<!--NOTE: Example of just a link in between accordion items -->
								<a href="${contextpath}${accessoriesCategoryCacheEntry.url}" class="add-item"><crs:outMessage key="cricket_shoppingcart_add_accessory"/><!-- Add An Accessory --></a>	
							</c:when>
							<c:otherwise>
								<section id="accessories1" class="active">
									<p class="title accessory" data-section-title><a href="#" class="package-title"><crs:outMessage key="cricket_shoppingcart_accessories"/><!-- Accessories --></a></p>
									<div class="content" data-section-content>
										<div class="row">
											<!-- accessories details -->
											<dsp:include page="/cart/includes/display_accessoriesLineItem.jsp">
												<dsp:param name="accessoriesURL" value="${contextpath}${accessoriesCategoryCacheEntry.url}"/>
												<dsp:param name="accessory" value="${accessories}"/>
											</dsp:include>
											<!-- accessories price details -->
											<dsp:include page="/cart/includes/display_accessoriesPrice.jsp">
												<dsp:param name="accessory" value="${accessories}"/>
											</dsp:include>
											<c:set var="accessoriesTotal" value="${accessoriesTotal + accessoriesAmt}"/>
										</div>
									</div> <!--/.content-->
								</section>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${isUserLoggedIn &&  itemCount eq '0'}"></c:when>
							<c:otherwise>
					<section class="accordion-footer">

						<div class="content">
								<div class="row">
									<div class="large-8 small-12 details-container columns">
										<dsp:getvalueof var="itemCount" bean="ShoppingCart.current.commerceItemCount"/>
										<dsp:getvalueof var="userIntention" bean="UpgradeItemDetailsSessionBean.userIntention"/>
										<dsp:getvalueof var="upgradePhoneIntention" bean="CartConfiguration.upgradePhoneIntention"/>
										<dsp:getvalueof var="upgradePlanIntention" bean="CartConfiguration.upgradePlanIntention"/>
										<dsp:getvalueof var="upgradeAddonIntention" bean="CartConfiguration.upgradeAddonIntention"/>
										<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>
										<dsp:getvalueof var="OOFMarketType" bean="CartConfiguration.outOfFootPrintMarketType"></dsp:getvalueof>
										<c:choose>
											<c:when test="${isUserLoggedIn}">
												<c:if test="${itemCount != 0 && (!empty userIntention && (userIntention eq upgradePhoneIntention || userIntention eq upgradePlanIntention || userIntention eq upgradeAddonIntention))}">
													<p><crs:outMessage key="cricket_shoppingcart_muv_message"/></p>
													<br>
													<p><crs:outMessage key="cricket_shoppingcart_muv_message2"/></p>
												</c:if>
											</c:when>
											<c:otherwise>
												<!-- auto bill payment details -->
												<c:if test="${not empty packages && OOFMarketType ne marketType}">
													<dsp:include page="/cart/includes/auto_bill_payment.jsp"></dsp:include>
													<hr/>			
												</c:if>
											</c:otherwise>
										</c:choose>
										<c:if test="${empty userIntention || (userIntention ne upgradePhoneIntention && userIntention ne upgradePlanIntention && userIntention ne upgradeAddonIntention)}">
											<!-- existing phones price details -->
											<c:if test="${empty accessories || !empty packages}">
												<dsp:include page="/cart/includes/existing_phone.jsp"/>
											</c:if>
											
										</c:if>
									</div>
									
									<div class="large-4 small-12 summary-container check-out-container columns">
										<!-- order summary -->
									<c:set var="orderTotal" value="${packagesTotal + upgradePhoneTotal + upgradePlanTotal + upgradeAddonsTotal + accessoriesTotal}"/>
									<c:set var="monthlyTotal" value="${monthlyPkgTotal + monthlyUpgardeTotal + monthlyUpgradeAddonsTotal}"/>
									<dsp:include page="/cart/includes/orderSummary.jsp">
										<dsp:param name="orderTotal" value="${orderTotal}" />
										<dsp:param name="monthlyTotal" value="${monthlyTotal}" />
									</dsp:include>
									<dsp:getvalueof var="itemCount" bean="ShoppingCart.current.commerceItemCount"/>
								<dsp:droplet name="DisplayCheckoutButtonDroplet">
								<dsp:param name="order" bean="ShoppingCart.current"/>
									<dsp:oparam name="output">	
									<dsp:getvalueof var="toolMessage" param="toolTipMessage"/>
										<dsp:droplet name="Switch">
										<dsp:param name="value" param="showCheckOutButton"/>
											<dsp:oparam name="true">
											
											
											<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
													  <dsp:param name="inUrl" value="/checkout/shippingbilling/shippingbillingdetails.jsp"/>
													  <dsp:oparam name="output">
														<dsp:getvalueof  var="successUrl" param="secureUrl" />
													  </dsp:oparam>
													</dsp:droplet>
											
						 
											<dsp:form name="cartPage" method="post" id="cartPageId" action="${successUrl}" requiresSessionConfirmation="false">    
											<div class="row">
											<div class="large-12 small-12 columns">
												<a href="#" class="button small orange" style="width:100%"  onclick="submitFormCart('cartPageId');"><crs:outMessage key="cricket_shoppingcart_checkout"/></a> <!-- Check Out -->
													
												<%--	<dsp:input bean="CartModifierFormHandler.moveToPurchaseInfoSuccessURL" type="hidden" value="${contextpath}/checkout/shippingbilling/shippingbillingdetails.jsp"/>
												--%>	<dsp:input id="moveToPurchaseInfoErrorURL" type="hidden" value="" bean="CartModifierFormHandler.moveToPurchaseInfoErrorURL"/>
													<dsp:input bean='CartModifierFormHandler.moveToPurchaseInfo' type="hidden" value="true"/>
												
											</div>
											</div>
											</dsp:form>						
											</dsp:oparam>
											<dsp:oparam name="false">
											
											<p><a class="button small disabled has-tip" data-tooltip title="${toolMessage}"><crs:outMessage key="cricket_shoppingcart_checkout"/></a></p>							
											</dsp:oparam>
										</dsp:droplet>
									</dsp:oparam>
								</dsp:droplet>				
								
							<dsp:form id="cartPromotion"  name="cartPromotion" action="" method="post" >
								<dsp:input bean="CricketCouponFormHandler.couponCode" id="cartPromoCode-submit" name="promo-code-cart" type="hidden" value=""></dsp:input>
								<dsp:input bean="CricketCouponFormHandler.claimCoupon" type="hidden" value="Enter Credit Card"/>	
								<dsp:input id="applyCouponURL1" bean="CricketCouponFormHandler.applyCouponSuccessURL" type="hidden" value=""/>		
							</dsp:form> 
							
							<dsp:form style="display:none" id="autoBillPaymentForm"  name="autoBillPaymentForm" action="" method="post" >
								<dsp:input bean="CricketAutoBillPaymentFormHandler.autoBillPayment" id="autoBillPayment" name="autoBillPayment" type="hidden" value=""/>
								<dsp:input id="submitAutoBillPaymentSubmit"  bean="CricketAutoBillPaymentFormHandler.submitAutoBillPayment" type="submit" value=""/>		
							</dsp:form> 		
									
									</div>
								</div>			
							</div>					
						</section>
					</c:otherwise>
						</c:choose>
					</div> <!--/.accordion -->	
					<c:set var="TargeterNameMobile" value="/atg/registry/RepositoryTargeters/Promotional/CartPromotions/CartPromotionalBanner" />
					<dsp:droplet name="TargetingForEach">
						<dsp:param bean="${TargeterNameMobile}" name="targeter"/>
						<dsp:param name="start" value="1"/>
						<dsp:param name="howMany" value="1"/>
						<dsp:oparam name="output">	
							<dsp:getvalueof param="element.url" var="mobileImageURL"/>
						</dsp:oparam>
					</dsp:droplet>
					<c:set var="TargeterNameDesktop" value="/atg/registry/RepositoryTargeters/Promotional/CartPromotions/CartBanerDesktop" />
					<dsp:droplet name="TargetingForEach">
						<dsp:param bean="${TargeterNameDesktop}" name="targeter"/>
						<dsp:param name="start" value="1"/>
						<dsp:param name="howMany" value="1"/>
						<dsp:oparam name="output">	
							<dsp:getvalueof param="element.url" var="DesktopImageURL"/>
						</dsp:oparam>
					</dsp:droplet>
				
					<c:if test="${isUserLoggedIn && itemCount != 0 }">
						<img class="cart-banner" src="${contextpath}${DesktopImageURL}"  data-interchange="[${contextpath}${mobileImageURL}, (default)], [${contextpath}${DesktopImageURL}, (small)]" alt=""/>
					</c:if>
					<c:if test="${!isUserLoggedIn}">
						<img class="cart-banner" src="${contextpath}${DesktopImageURL}"  data-interchange="[${contextpath}${mobileImageURL}, (default)], [${contextpath}${DesktopImageURL}, (small)]" alt=""/>
					</c:if>

					<c:if test="${isUserLoggedIn}">
						<p class="notes">
							Go to
							<a href="${myAccountLogin}">My Account</a>
							to upgrade your phone, change your plan or add-ons.
						</p>					
					</c:if>


				</div>								
			</div>	
		</div>
		</span>
	</div>	<!--/#drawer-cart-->					
<script>
function submitFormCart(formId){
	document.getElementById('moveToPurchaseInfoErrorURL').value = document.URL;
	document.getElementById(formId).submit()
}
</script>
<%-- <script src="${contextpath}/js/foundation/foundation.min.js"></script> 
<script>
$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips');
</script> --%>
<!-- END: CART HTML -->
		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="userIntention"  bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean.userIntention"/>
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="userIntention"  value="New Activation"/>
		</c:if>
		<c:if test="${empty userIntention}">
			<dsp:getvalueof var="userIntention"  value="null"/>
		</c:if>
<dsp:droplet name="/atg/dynamo/droplet/ForEach">
	<dsp:param name="array" bean="ShoppingCart.current.commerceItems"/> 
	<dsp:param name="elementName" value="item"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="index" param="index"/>
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
						<dsp:getvalueof var="productName" value="${producttype.displayName}"/>
					</dsp:oparam>				
			</dsp:droplet>			
			<input type="hidden" name="producttype" id="producttype${index}" value="${producttype}" />
			<input type="hidden" name="productId" id="productId${index}" value="${productId}" />
			<input type="hidden" name="productName" id="productName${index}" value="${productName}" />
			<input type="hidden" name="quantity" id="quantity${index}" value="${quantity}" />
			<input type="hidden" name="unitPrice" id="unitPrice${index}" value="${unitPrice}" />
		</dsp:oparam>			
</dsp:droplet>
<!-- <script type="text/javascript">
	cmDisplayShops();
</script> -->
<script>
function submitDuplicateCartForm(formId, packageCount){
	checkInventoryBeforeDuplicate(formId, packageCount);
}

function checkInventoryBeforeDuplicate(formId, packageCount) {
	var skuIdHiddenInput = "packagePhoneSkuId_" + packageCount;
	var skuId = $("#"+skuIdHiddenInput).val();
		
	 $.ajax({
	        url: contextPath + "/common/includes/packageInventoryInfoJson.jsp?phoneSkuId=" + skuId,
	        type: "post",
	        dataType: "json",
	        error:function(){
	        },
	        success:function(data){
				var isOutOfStock = data.outOfStock;
				if(isOutOfStock == 'true') {
					$("#duplicatingPackageToExceedInventoryModal").click();
				} else {
					$("#"+formId).find ('#duplicateSuccessUrl').val(window.location);
					$("#"+formId).find ('#duplicateErrorUrl').val(window.location); 
					document.getElementById(formId).submit();
				}
	        }
	     });
}
function submitDeleteCartForm(formId){
                $("#"+formId).find ('#deleteSuccessUrl').val(window.location);
                $("#"+formId).find('#deleteErrorUrl').val(window.location);
                deletePackageFormId = formId;
                $("#packageRemoveModal").click();
                //document.getElementById(formId).submit();
}
	
$(document).ready(function() {
	if(abp == true){
		 if(document.getElementById('cartAutoBillPaymentMessage') !== null ){
             document.getElementById('cartAutoBillPaymentMessage').style.display = 'block';
		}
	}
	
	var openCart = '<dsp:valueof param="openCart"/>';
	if (openCart != null && openCart != '' && openCart == 'true') {
		openCartDrawer();
		fireShopAction5s();
	}
});
</script>
</body>
</html>
</dsp:page>