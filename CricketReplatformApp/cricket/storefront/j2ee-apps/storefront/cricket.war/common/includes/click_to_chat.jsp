<dsp:page>
<dsp:getvalueof var="PAGE_TYPE" param="PAGE_TYPE"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:getvalueof var="itemCount" bean="ShoppingCart.current.commerceItemCount"/>
<dsp:getvalueof var="order" bean="ShoppingCart.current"/>
<dsp:getvalueof var="zipCode" bean="/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode"/>
<dsp:getvalueof var="customerType" bean="/atg/cricket/util/CricketProfile.customerType"/>
<script src="${contextpath}/js/chatGui-min.js"></script>
<!-- BEGIN LivePerson Monitor Tag -->
	<script src="${contextpath}/js/mtagconfig.js"></script>
	<!-- END LivePerson Monitor Tag -->
	<script type="text/javascript">
		var lpMTagConfig = lpMTagConfig || {}; lpMTagConfig.vars = lpMTagConfig.vars || [];
		
		lpMTagConfig.lpServer = "sales.liveperson.net";
		lpMTagConfig.lpTagSrv = lpMTagConfig.lpServer;
		lpMTagConfig.lpNumber = "4968591";
		//lpMTagConfig.lpNumber = "25026695";
		//lpMTagConfig.lpNumber = "73038078";
		lpMTagConfig.deploymentID = "2";

		lpAddVars("page","unit","sales");
		lpAddVars("session","language","english");
		
		lpAddVars("page","lpSection","${PAGE_TYPE}");
		lpAddVars("page","lpOrderTotal","${order.priceInfo.total}");
		lpAddVars("page","lpOrderNumber","${order.id}");
		lpAddVars("page","lpErrorCounter","0");
		lpAddVars("page","lpCartTotal","${order.priceInfo.rawSubtotal}");
		lpAddVars("page","lpCartCount","${itemCount}");
		lpAddVars("page","OrderTotal","${order.priceInfo.total}");

		lpAddVars("session","lpZipCode","${zipCode}");
		lpAddVars("session","lpOPFlag","0");
		lpAddVars("session","lpCustomerType","${customerType}");
		
		
	</script>

	
</dsp:page>