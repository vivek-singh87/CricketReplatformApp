<dsp:page>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach" />
	<dsp:importbean bean="/com/cricket/browse/PrepareAddonVoMap" />
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean"/>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="intention" param="intention"/>
   	<dsp:getvalueof var="intentionbean" bean="UpgradeItemDetailsSessionBean.userIntention"/> 
	<dsp:importbean bean="/atg/cricket/util/CricketProfile"/>
	<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
	
	<dsp:include page="/common/includes/inquireFeatureTimeOutModal.jsp"/>
	<!--This droplet is called to get the product list in the map for displaying in an accordian-->
	<c:choose>
		<c:when test="${intention eq 'upgradePlan' || intention eq 'upgradeFeature' || intentionbean eq 'upgradePlan' || intentionbean eq 'upgradeFeature'}">
			<dsp:getvalueof var="planCode" bean="CricketProfile.ratePlanCode"></dsp:getvalueof>
			<dsp:getvalueof var="modelNumber" bean="CricketProfile.deviceModel"></dsp:getvalueof>
	 	</c:when>   	 	
   	  	<c:otherwise>
   	  		<dsp:getvalueof var="planCode" param="planId"></dsp:getvalueof>
			<dsp:getvalueof var="modelNumber" param="modelNumber"></dsp:getvalueof>
	 	</c:otherwise>
   	 </c:choose>
		<dsp:getvalueof var="userPurchasedAddonProducts" bean="CricketProfile.userPurchasedOfferingProducts"/>
	<c:choose>
	<c:when test="${planCode == null || modelNumber == null}">
	<dsp:droplet name="PrepareAddonVoMap">
			<dsp:param name="productList" param="productList"/>
			<dsp:oparam name="output">
			<c:set var="isDynamicAddOn" value="false"/>
			<dsp:getvalueof var="finalMap" param="finalMap"></dsp:getvalueof>
			</dsp:oparam>
		 </dsp:droplet>
	</c:when>
	<c:otherwise>
	 	<dsp:droplet name="/com/cricket/browse/DisplayFeaturesDroplet">

   	 
   	  <c:choose>
		<c:when test="${intention eq 'upgradePlan' || intention eq 'upgradeFeature' || intentionbean eq 'upgradePlan' || intentionbean eq 'upgradeFeature'}">
   	  		<dsp:param name="transactionName" value="RRC"/>
   	  		<dsp:param name="modelNumber" bean="CricketProfile.deviceModel"/>
   	  		<dsp:param name="phoneType" value="Voice"/>
   	  		<dsp:param name="planCode" bean="CricketProfile.ratePlanCode"/>
   	  		<dsp:param name="marketCode" bean="CricketProfile.marketCode"/>
   	  		<dsp:param name="userAddons" value="${userPurchasedAddonProducts}"/>
   	 	 </c:when>   	 	
   	  	 <c:otherwise>
   	 		  <dsp:param name="transactionName" value="ACT"/>
   	 		  <dsp:param name="modelNumber" param="modelNumber"/>
   	  		  <dsp:param name="phoneType" param="phoneType"/>
   	  		  <dsp:param name="planCode" param="planId"/>
   	  		  <dsp:param name="marketCode" bean="Profile.marketId"/>
   	 	 </c:otherwise>
   	 </c:choose>
   	  <dsp:param name="hasESNHistory" value="false"/>
   	  <dsp:param name="isCricketPhone" value="true"/>
   	  <dsp:param name="orderId" bean="ShoppingCart.current.id"/>
   	  <dsp:oparam name="output">
			<dsp:droplet name="/com/cricket/browse/StorePackageFeaturesDroplet">
				<dsp:param name="packageId" param="packageId"/>
				 <dsp:param name="allIncludedAddOns" param="allIncludedAddOns"/>
				 <dsp:param name="hppAddons" param="hppAddons"/>
				<c:choose>
					<c:when test="${intention eq 'upgradePlan' || intention eq 'upgradeFeature' || intentionbean eq 'upgradePlan' || intentionbean eq 'upgradeFeature'}">
   			   	  		<dsp:param name="planId" bean="CricketProfile.ratePlanCode"/>
			   	 	 </c:when>   	 	
			   	  	 <c:otherwise>
			   	 		  	<dsp:param name="planId" param="planId"/>
			   	 	 </c:otherwise>
			   	 </c:choose>
			</dsp:droplet>
				<dsp:droplet name="/com/cricket/browse/CheckAddonForDisplayDroplet">
				<dsp:param name="optionalAddOns" param="optionalAddOns"/>
				<dsp:param name="allIncludedAddOns" param="allIncludedAddOns"/>
				<dsp:oparam name="output">
								<dsp:droplet name="PrepareAddonVoMap">
								<dsp:param name="productList" param="addOnsForDisplay"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="finalMap" param="finalMap"></dsp:getvalueof>
									 
									<c:set var="finalMap" value="${finalMap}"/>
									<c:set var="isDynamicAddOn" value="true"/>
								</dsp:oparam>
							 </dsp:droplet>
				</dsp:oparam>
				</dsp:droplet>
				
		</dsp:oparam>
		<dsp:oparam name="error">
			<script>
				$(document).ready(function() {
					$("#inquireFeatureTimeOutModel").click();
				});										
			</script>
		</dsp:oparam>
		</dsp:droplet>
	</c:otherwise>
	
	</c:choose>
 
	<dsp:include page="addon_results.jsp">	
	<dsp:param name="finalMap" value="${finalMap}"/>
	<dsp:param name="isDynamicAddOn" value="${isDynamicAddOn}"/>	
	</dsp:include>
	 
</dsp:page>