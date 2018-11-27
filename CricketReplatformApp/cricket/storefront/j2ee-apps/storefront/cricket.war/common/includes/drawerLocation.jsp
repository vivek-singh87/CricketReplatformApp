<dsp:page>
	<dsp:getvalueof var="timeMonitoring" bean="/com/cricket/configuration/CricketConfiguration.timeMonitoring"/> 
	<c:if test="${timeMonitoring == true}">
		<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
			<dsp:param name="isEndTime" value="false"/>
			<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>
			<dsp:param name="contentItemType" value="locationDrawer"/>	
			<dsp:oparam name="output">
				<dsp:getvalueof var="startTime" param="startTime"/>
			</dsp:oparam>
		</dsp:droplet>
	</c:if>
	<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
	<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
	<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/atg/userprofiling/ProfileFormHandler" />
	<dsp:importbean bean="/com/cricket/common/util/formhandler/ChangeUserLocationBasedOnZipCode"/>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:importbean bean="/com/cricket/search/session/UserSearchSessionInfo"/>
	<dsp:getvalueof var="thisPage" bean="/OriginatingRequest.requestURI"/>
	<dsp:getvalueof var="params" bean="/OriginatingRequest.queryString"/>
	<dsp:getvalueof var="protocol" bean="/OriginatingRequest.scheme"/>
	<dsp:getvalueof var="serverName" bean="/OriginatingRequest.serverName"/>
	<dsp:getvalueof var="serverPort" bean="/OriginatingRequest.serverPort"/>
	<dsp:getvalueof var="contextPath" bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="myAccountLogin" bean="CricketConfiguration.homePageLinks.myAccountLogin" />
	<dsp:getvalueof var="cricketMyAccountURL" bean="CricketConfiguration.homePageLinks.myAccount" />
	<dsp:getvalueof var="searchTerm" param="Ntt"/>
	<dsp:getvalueof var="generalSearchQuery" bean="UserSearchSessionInfo.genTextBoxSearchQuery"/>
	<dsp:getvalueof var="rootcontextpath" bean="CricketConfiguration.rootcontextpath"/>
	<dsp:getvalueof var="shoppingCartCount" bean="ShoppingCart.current.commerceItemCount"/>
	<input type="hidden" id="shoppingCartCount" value="${shoppingCartCount}"/>
	<input type="hidden" id="searchTerm" value="${searchTerm}"/>
	<input type="hidden" id="generalSearchQuery" value="${generalSearchQuery}"/>
	<input type="hidden" id="contextPathSearchQuery" value="${contextPath}"/>
	<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
		<dsp:param name="inUrl" value="${rootcontextpath}"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="rootcontextpath" param="nonSecureUrl" />
		</dsp:oparam>
	</dsp:droplet>
	<input type="hidden" id="rootcontextpath" value="${rootcontextpath}"/>
	
	<div id="drawer-location" class="drawer-utility">
			<div class="drawer-wrapper">					
				<dsp:form iclass="form-container" name="checkZipForm" id="checkZipForm">
					<div class="row">
						<div class="large-12 columns">													
							<dsp:droplet name="Switch">
								<dsp:param name="value" bean="Profile.transient"/>
								<dsp:oparam name="false">								   	
								  	<div class="row">
										<div class="large-8 columns">
											<p>This location is based on your account address. To view products in other locations, <a href="javascript:logoutSubmitDesk(document.ATGlogOutFormDesk)" >please log out</a>.</p>
											<p>If you would like to update your account address please call <strong>1-800-CRICKET</strong>.</p>	
										</div>	
										<div class="large-3 columns login-message"></div>
										<div class="close-drawer close-location large-1 columns">
											<a href="#" class="right">X</a>
										</div>				
									</div>									
								</dsp:oparam>
								<dsp:oparam name="true">
									<div class="row">
										<div class="large-8 columns">
											<!--NOTE: This is form for checking zip codes -->
											<div class="row" id="zipcode-form">
												<div class="large-6 columns">
													<p class="heading">Enter Your Home Zip Code</p>
													<%--<h3>Enter Your Home Zip Code</h3> --%>
													<p>See the best offers available in your area.</p>		
												</div>	
												<div class="large-4 columns">
													<input type="tel" name="zip" id="zip" onkeypress="javascript:callFnToGetCity(this, event);" placeholder="" maxlength="12"/>									
												</div>
												<div class="large-2 columns">
													<a href="javascript:getCityForZipCode();" id="btn-check-zip" class="button small">Go</a>
												</div>	
											</div>											
										</div>	
										<div class="large-3 columns login-message">
											<p>Are you an existing Cricket Customer? <a href="${myAccountLogin}">Log In now</a></p>
										</div>												
										<div class="close-drawer close-location large-1 columns">
											<a href="#" class="right">X</a>
										</div>				
									</div>							
								</dsp:oparam>
							</dsp:droplet>
							<div class="row"><div class="large-8 large-offset-4 errors-location columns"></div></div>



									<div class="row zip-results">
									<div class="large-2 columns">
									<p class="heading" id="selectcity2">Please Select Your City:</p>
									<%--<h4 id="selectcity2">Please Select Your City:</h4> --%>
									</div>
									<div class="large-4 columns" id="cityList">										
									</div>
									<div class="large-2 columns">
										 <a href='javascript:updateCity();' class="button small">Update</a>
									</div>
									<div class="large-2 columns"></div>
									</div>
													
						</div>
					</div>
				</dsp:form>	
				<dsp:form style="display:none" action="${contextPath}/browse/phone/listing/compareAjaxHandle.jsp" name="changeLocationFormDeskTop" id="changeLocationFormDeskTop" method="get">
					<dsp:input name="clearCartItems" id="clearCartItems" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.clearCartItems"/>
					<dsp:input name="nnc" id="noNetworkCoverageDesk" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.noNetworkCoverage"/>
					<dsp:input name="clc" id="changeLocCityDesk" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.city"/>
					<dsp:input name="cls" id="changeLocStateDesk" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.state"/>
					<dsp:input name="clcon" id="changeLocCountryDesk" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.country"/>
					<dsp:input name="cla" id="changeLocAreaCodeDesk" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.areaCode"/>
					<dsp:input name="clp" id="changeLocPostalCodeDesk" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.postalCode"/>
					<dsp:input name="clcc" id="changeLocCountryCodeDesk" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.countryCode"/>
					<dsp:input name="cllat" id="changeLocLatitudeDesk" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.latitude"/>
					<dsp:input name="cllon" id="changeLocLongitudeDesk" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.longitude"/>
					<dsp:input name="clt" id="changeLoctimezoneDesk" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.timezone"/>
					<dsp:input name="clr" id="changeLoctionRedirectURL" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.redirectURL"/>					
					<dsp:input name="clsubmit" id="changeLocationSubmitDesk" type="submit" bean="ChangeUserLocationBasedOnZipCode.changeUserLocation" value="submit2"/>
				</dsp:form>	
				<dsp:form action="/" name="ATGlogOutFormDesk" id="ATGlogOutFormDesk" formid="ATGlogOutFormDesk">
					<dsp:input type="hidden" bean="ProfileFormHandler.logout" value="Submit"/>
				</dsp:form>								
			</div>	
		</div>
		<c:if test="${timeMonitoring == true}">
			<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
				<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>	
				<dsp:param name="contentItemType" value="locationDrawer"/>
				<dsp:param name="startTime" value="${startTime}"/>
				<dsp:param name="isEndTime" value="true"/>
				<dsp:oparam name="output">
				</dsp:oparam>
			</dsp:droplet>
		</c:if>
</dsp:page>
		