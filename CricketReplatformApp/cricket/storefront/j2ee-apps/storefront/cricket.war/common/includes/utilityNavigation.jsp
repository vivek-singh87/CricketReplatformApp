
<dsp:page>
<dsp:getvalueof var="timeMonitoring" bean="/com/cricket/configuration/CricketConfiguration.timeMonitoring"/> 
<c:if test="${timeMonitoring == true}">
	<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
		<dsp:param name="isEndTime" value="false"/>
		<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>
		<dsp:param name="contentItemType" value="utilityLinks"/>	
		<dsp:oparam name="output">
			<dsp:getvalueof var="startTime" param="startTime"/>
		</dsp:oparam>
	</dsp:droplet>
</c:if>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/atg/userprofiling/ProfileFormHandler" />
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/com/cricket/common/util/formhandler/ChangeUserLocationBasedOnZipCode"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsNull"/>
<dsp:getvalueof var="espanolLink" bean="CricketConfiguration.homePageLinks.espanol" />
<dsp:getvalueof var="coverageLink" bean="CricketConfiguration.homePageLinks.coverage" />
<dsp:getvalueof var="findAStoreLink" bean="CricketConfiguration.homePageLinks.findStore" />
<dsp:getvalueof var="activateLink" bean="CricketConfiguration.homePageLinks.activate" />
<dsp:getvalueof var="makePaymentLink" bean="CricketConfiguration.homePageLinks.makePayment" />
<dsp:getvalueof var="myAccountLink" bean="CricketConfiguration.homePageLinks.myAccount" />
<dsp:getvalueof var="myAccountURL" bean="CricketConfiguration.homePageLinks.myAccountURL" />
<dsp:getvalueof var="forgotPasswordLink" bean="CricketConfiguration.homePageLinks.forgotPasswordLink" />
<dsp:getvalueof var="cricketMyAccountHostURL" bean="CricketConfiguration.homePageLinks.myAccount" />
<dsp:getvalueof var="myAccountLogin" bean="CricketConfiguration.homePageLinks.myAccountLogin" />
<dsp:getvalueof var="rootcontextpath" bean="CricketConfiguration.rootcontextpath"/>	
<dsp:getvalueof var="nonSecureHost" bean="CricketConfiguration.nonSecureHost"/>	
<dsp:getvalueof var="nonSecureProtocol" bean="CricketConfiguration.nonSecureProtocol"/>	
<dsp:getvalueof var="nonSecurePort" bean="CricketConfiguration.nonSecurePort"/>	
<dsp:getvalueof var="thisPage" bean="/OriginatingRequest.requestURI"/>
<dsp:getvalueof var="params" bean="/OriginatingRequest.queryString"/>
<dsp:getvalueof var="protocol" bean="/OriginatingRequest.scheme"/>
<dsp:getvalueof var="serverName" bean="/OriginatingRequest.serverName"/>
<dsp:getvalueof var="serverPort" bean="/OriginatingRequest.serverPort"/>
<dsp:getvalueof var="contextPath" bean="/OriginatingRequest.contextPath"/>
<dsp:include page="/common/includes/inquireAccountErrorModal.jsp"/>
<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
		<dsp:param name="inUrl" value="${rootcontextpath}"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="rootcontextpath" param="nonSecureUrl" />
		</dsp:oparam>
	</dsp:droplet>
	<input type="hidden" id="rootcontextpath" value="${rootcontextpath}"/>
<script src="${contextpath}/js/espanol-link.js"></script>

	<c:set var="geoIpDetecred">
		<dsp:valueof bean="CitySessionInfoObject.cityVO.geoIpDetecred"/>
	</c:set>
	
		<div class="mobile-location">
		<dsp:getvalueof var="isDefaultLocation" bean="Profile.isDefaultLocation"/>		
			<dsp:droplet name="Switch">
				<dsp:param name="value" bean="CitySessionInfoObject.cityVO.defaultLocation"/>
				<dsp:oparam name="true">
				<!-- Defect-5288(As Andrew confirmed with kinsey,When no IP has been detected, "City, State" should be displayed. Without the default text, the zip code pin icon is collapsed down so it is very difficult to click the pin. -->
					<div class="location-center"><!-- Defect-608 -->
                        <p><a class="open-zip" href="#">City, State</a></p> 
                    </div>
				</dsp:oparam>
				<dsp:oparam name="false">
					<div class="location-center">
						<p><a class="open-zip" href="#">				
							<dsp:valueof bean="CitySessionInfoObject.cityVO.city">City</dsp:valueof>, 
							<dsp:valueof bean="CitySessionInfoObject.cityVO.state">State</dsp:valueof> 
							<c:if test="${geoIpDetecred eq false}">  
								<dsp:valueof bean="CitySessionInfoObject.cityVO.postalCode"/>
							</c:if>					
						</a></p>
					</div>
				</dsp:oparam>
			</dsp:droplet>
		</div>	
		<div id="drawer-mobile-location">
			<div class="drawer-wrapper">			   
				<dsp:form class="form-container" name="checkMobileZipForm" id="checkMobileZipForm" formid="checkMobileZipForm">	
					<dsp:droplet name="Switch">
						<dsp:param name="value" bean="Profile.transient"/>
						<dsp:oparam name="false">
							<p>This location is based on your account address. To view products in other locations, <a href="${cricketMyAccountHostURL}?action=Logout">please log out</a>.</p>
							<p>If you would like to update your account address please call <strong>1-800-CRICKET</strong>.</p>							
						</dsp:oparam>
						<dsp:oparam name="true">
							<p class="heading">Enter Your Home Zip Code</p>
							<%--<h2>Enter Your Home Zip Code</h2> --%>
							<p>See the best offers available in your area.</p>				
							<p><input type="tel" name="zipchange" id="zipchange" onkeypress="javascript:callFnCityForZipCodeMobile(this, event);"/></p>										
							<p><a href="javascript:getCityForZipCodeMobile();" id="btn-mobile-check-zip" class="button large">Go</a></p>
							<div class="errors-mobile-location"></div>
							
							
							<div class="mobile-zip-results">
								<p class="heading" id="selectcity1">Please Select Your City:</p>
								<%--<h4 id="selectcity1">Please Select Your City:</h4> --%>
									<div id="cityListMobile"">
									</div>									
								<p><a href='javascript:updateCityMobile();' class="button large">Update</a></p>                           
							</div> 
							
							
							
											
							<hr/>
							<p>Are you an existing Cricket customer?</p>
							<p><a href="${cricketMyAccountHostURL}" class="btn-log-in-now">Log In Now</a></p>				
						</dsp:oparam>
					</dsp:droplet>					
				</dsp:form>
				<dsp:form style="display:none" action="${contextPath}/browse/phone/listing/compareAjaxHandle.jsp" name="changeLocationForm" id="changeLocationForm" method="get" formid="changeLocationForm">
					<dsp:input name="clearCartItems" id="clearCartItemsMobile" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.clearCartItems"/>
					<dsp:input name="nnc2" id="noNetworkCoverageMob" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.noNetworkCoverage"/>
					<dsp:input name="clc2" id="changeLocCity" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.city"/>
					<dsp:input name="cls2" id="changeLocState" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.state"/>
					<dsp:input name="clcon2" id="changeLocCountry" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.country"/>
					<dsp:input name="cla2" id="changeLocAreaCode" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.areaCode"/>
					<dsp:input name="clp2" id="changeLocPostalCode" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.postalCode"/>
					<dsp:input name="clcc2" id="changeLocCountryCode" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.countryCode"/>
					<dsp:input name="cllat2" id="changeLocLatitude" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.latitude"/>
					<dsp:input name="cllon2" id="changeLocLongitude" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.longitude"/>
					<dsp:input name="clt2" id="changeLoctimezone" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.timezone"/>
					<dsp:input name="clr2" id="changeLoctionRedirectURL" type="hidden" value="" bean="ChangeUserLocationBasedOnZipCode.redirectURL"/>					
					<dsp:input name="clsubmit2" id="changeLocationSubmit" type="submit" bean="ChangeUserLocationBasedOnZipCode.changeUserLocation" value="submit2"/>
				</dsp:form>	
				
			</div>	
		</div>
		<!--End of Mobile specific zipcode functionality -->  
		<nav id="nav-utility">
			<div class="row">			
			<!--Start of Mobile specific markup -->
				<p class="heading">Manage Your Account</p>
				<%--<h3>Manage Your Account</h3> --%>
				<dsp:droplet name="Switch">
						<dsp:param name="value" bean="Profile.transient"/>
						<dsp:oparam name="true">
						<form class="custom" name="mobileLoginForm" id="mobileLoginForm" method="post" action="${myAccountLogin}">  
						<input type="hidden" name="action" value="Login" />						
							<div class="row">
								<div class="small-12 columns">
												<input type="text" name="uid" id="uidMobile" data-type="phone" data-required="true" placeholder="Phone Number" onkeypress="javascript:callFnMyAccountLoginMobile(this, event);">
								</div>   
								<div class="small-12 columns">
												<input type="password" name="pwd" id="pwdMobile" data-required="true" placeholder="Password" onkeypress="javascript:callFnMyAccountLoginMobile(this, event);">
								</div>   
								<div class="small-12 errors-mobile-login columns"></div>
								<div class="small-12 columns"> 
												<label for="checkbox1">
																<input type="checkbox" id="checkbox1" style="display: none;">
																<span class="custom checkbox" onclick="rememberMe(this,'mobile');"></span> Remember Me
												</label>
								</div>
								<div class="small-12 columns">
										<a href="#" id="btn-mobile-login" class="button small" onClick="document.getElementById('mobileLoginForm').submit();">Log In</a>
								</div>   
								<div class="small-12 columns">
												<p><a href="${forgotPasswordLink}">Forgot Your Password?</a></p>
												<p><a href="${cricketMyAccountHostURL}">Register</a></p>
								</div>                                                                                                                                                                                                   
							</div>                                                                   
						</form> 
						</dsp:oparam>
					</dsp:droplet>				
				<div class="indicator-black-west"></div>
				<hr/>
			
		<%-- 	<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
				<dsp:param name="inUrl" value="${rootcontextpath}billingordersummary/enterBillingOrderID.jsp"/>
				<dsp:oparam name="output">
			    	<dsp:getvalueof var="orderStatusURL" param="nonSecureUrl" /> 
			  	</dsp:oparam>
			</dsp:droplet>
 			<c:choose>
			  <c:when test="${orderStatusURL == null || empty orderStatusURL}">
			   	 <c:set var="atgOrderStatusLink" value="${nonSecureProtocol}://${nonSecureHost}:${nonSecurePort}/billingordersummary/enterBillingOrderID.jsp"/>
 			  </c:when>
			  <c:otherwise>
			    <c:set var="atgOrderStatusLink" value="${orderStatusURL}"/>
 			  </c:otherwise>
			</c:choose> --%>
  			<!--Desktop utility nav (repurposed for mobile) starts here -->
				<ul class="large-6">					
					<li id="li-coverage-map"><a href="${coverageLink}">Coverage</a></li>
					<li><a href="${findAStoreLink}"><crs:outMessage key="cricket_header_findAStore" /></a></li>
					<li><a href="${activateLink}"><crs:outMessage key="cricket_header_activate" /></a></li>
					<li><a href="${makePaymentLink}"><crs:outMessage key="cricket_header_makeAPayment" /></a></li>
 						<li ><dsp:a href="${nonSecureProtocol}://${nonSecureHost}:${nonSecurePort}/billingordersummary/enterBillingOrderID.jsp"><crs:outMessage key="cricket_header_orderStatus" /></dsp:a></li>		
					<%-- <li id="li-language"><a class="no-pipe last" href="${espanolLink}">Espa&#241;ol</a></li> --%>
					<li id="li-language">
						<a class="no-pipe last" mporgnav href="${espanolLink}" onclick="return switchLanguage('es');">
						Espa&#241;ol</a>
					</li>
				</ul>
				<hr/>	
				<ul id="nav-utility-right">
					<dsp:getvalueof var="firstName" bean="/atg/cricket/util/CricketProfile.firstName"/>
					<dsp:getvalueof var="lastName" bean="/atg/cricket/util/CricketProfile.lastName"/>
					<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
					<dsp:getvalueof var="transient" bean="Profile.transient"/>
					<c:if test="${transient eq false}">
						<li id="li-username" style="display: inline-block;">
							<a href="${myAccountURL}" class="no-pipe">Hello ${firstName}</a>
						</li>
					</c:if>
					<dsp:droplet name="Switch">
						<dsp:param name="value" bean="CitySessionInfoObject.cityVO.defaultLocation"/>
						<dsp:oparam name="true">
							<li id="li-location"><a class="no-pipe" href="#">City, State</a></li>							
						</dsp:oparam>
						<dsp:oparam name="false">					
							<li id="li-location">												
								<a class="no-pipe" onclick="#" href="#">								
									<dsp:valueof bean="CitySessionInfoObject.cityVO.city">City</dsp:valueof>, 
									<dsp:valueof bean="CitySessionInfoObject.cityVO.state">State</dsp:valueof> 
									<c:if test="${geoIpDetecred eq false}">
									<dsp:valueof bean="CitySessionInfoObject.cityVO.postalCode"/>
									</c:if>								
								</a>
							</li>											
						</dsp:oparam>
					</dsp:droplet>
					<dsp:droplet name="Switch">
						<dsp:param name="value" bean="Profile.transient"/>
						<dsp:oparam name="true">
							<li id="li-log-in">
								<a href="#" class="button expanded large">My Account</a>
							</li>
						</dsp:oparam>
						<dsp:oparam name="false">	
							<li id="li-log-out" style="display: inline-block;">
						 <a href="javascript:logoutSubmit(document.ATGlogOutForm)">Log Out</a></li>
						<dsp:form action="/" name="ATGlogOutForm" id="ATGlogOutForm" method="post" formid="ATGlogOutForm">
						  <dsp:input type="hidden" bean="ProfileFormHandler.logout" value="Submit"/>
						</dsp:form>						
							<div id="drawer-utility-indicator" class=""></div>						 
						</dsp:oparam>
					</dsp:droplet>
				</ul>				
				<div class="side-menu-close"><a href="#" class="">X</a></div>
			</div>
		</nav>
		<script type="text/javascript" id="mpelid" src="//espanol.mycricket.com/mpel/mpel.js"></script>
		<c:if test="${timeMonitoring == true}">
			<dsp:droplet name="/com/cricket/configuration/TimeMonitoring">
				<dsp:param name="categoryType" value="${CATEGORY_TYPE}"/>	
				<dsp:param name="contentItemType" value="utilityLinks"/>
				<dsp:param name="startTime" value="${startTime}"/>
				<dsp:param name="isEndTime" value="true"/>
				<dsp:oparam name="output">
				</dsp:oparam>
			</dsp:droplet>
		</c:if>
</dsp:page>		