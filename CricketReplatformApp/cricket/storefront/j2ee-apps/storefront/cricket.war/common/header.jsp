<dsp:page>

	<dsp:importbean bean="/atg/userprofiling/ProfileFormHandler" />
	<dsp:importbean bean="/atg/cricket/util/CricketProfile" />
	<dsp:importbean bean="/atg/dynamo/droplet/IsEmpty" />
	<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<input type="hidden" value="${contextpath}" id="contextPathValue"/>
	<%--<script src="/cart/locationInfo?cm=1&rdiCm=1"></script>--%>
	<script type="text/javascript"> 
	 //cmCreatePageviewTag('http://www.mycricket.com/', 'http://www.mycricket.com/', null, null, pvAttrs);
	</script>
	<header id="header">
		<%-- Calling the ProfileformHandler.Login in backend --%>
	<dsp:droplet name="Switch">
		<dsp:param name="value" bean="Profile.transient"/>
		<dsp:oparam name="true">
		<dsp:droplet name="IsEmpty">
		<dsp:param name="value" bean="CricketProfile.userId" />
 
		<dsp:oparam name="false">
			<dsp:droplet name="IsEmpty">
				<dsp:param name="value" bean="CricketProfile.password" />
				<dsp:oparam name="false">
				<c:set var="firstAccess">
				<dsp:valueof bean="CitySessionInfoObject.loggedIn"/> 
				</c:set>
				<c:if test="${firstAccess eq true}">
					<dsp:setvalue bean="ProfileFormHandler.value.login"	beanvalue="CricketProfile.userId" />
					<dsp:setvalue bean="ProfileFormHandler.value.password" beanvalue="CricketProfile.password" />
					<dsp:setvalue bean="ProfileFormHandler.login" value="login"/>
				</c:if>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
	</dsp:droplet>
	</dsp:oparam>
	<dsp:oparam name="false">
	</dsp:oparam>
	</dsp:droplet>
	<%-- End of Calling ProfileFormhandler --%>
	<!-- Drawers for Cart and Search -->
	  <dsp:include page="/common/includes/utilityNavigation.jsp"/>
	<!-- Drawers for Location and My Account Log In -->	
	  <dsp:include page="/common/includes/drawerLocation.jsp"/>
	  <dsp:include page="/common/includes/drawerLogin.jsp"/>
	  
<!-- Top Bar area; Logo, main navigation, cart and search icons -->		
     <dsp:include page="/common/includes/topNavigation.jsp">
      <dsp:param name="protocalType" param="protocalType"/>
     
     </dsp:include>
	 
					
	</header> <!--/#header-->
	
	
<div class="acLoadingCustom" id="acLoadingCustom" style="display:none;"></div>
				
    <dsp:include page="/cart/drawerCart.jsp"/>
    <dsp:include page="/common/includes/drawerSearch.jsp"/>

</dsp:page>