
<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/atg/cricket/util/CricketProfile" />
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/userprofiling/ProfileFormHandler" />
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:getvalueof var="forgotPasswordLink" bean="CricketConfiguration.homePageLinks.forgotPasswordLink" />
<dsp:getvalueof var="cricketMyAccountHostURL" bean="CricketConfiguration.homePageLinks.myAccount" />
<dsp:getvalueof var="myAccountLogin" bean="CricketConfiguration.homePageLinks.myAccountLogin" />
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>

<script>
function callFnMyAccountLogin(inputElement, event) {  
	if (event.keyCode == 13) {						
		document.getElementById('myaccountlogin').submit();						
	}  
} 

</script>
<div id="drawer-log-in" class="drawer-utility">
	<div class="drawer-wrapper">
		<dsp:droplet name="Switch">
			<dsp:param name="value" bean="Profile.transient"/>
			<dsp:oparam name="true">		
				<form class="custom form-container" id="myaccountlogin" name="manageaccountform" method="post" action="${myAccountLogin}">
				<input type="hidden" name="action" value="Login" />
					<div class="row">	
						<div class="large-11 columns">
							<p class="heading">Manage Your Account</p>
							<%--<h3>Manage Your Account</h3> --%>
							<p>Make a payment, upgrade your phone or add a line of service.</p>	
						</div>
						<div class="large-1 columns close-drawer close-log-in">
							<a href="#" class="right">X</a>
						</div>						
					</div>		
					<div class="row">
						<div class="large-4 columns">
							<label class="password-label-ie" for="uidDesktop">Phone Number</label>
							<input type="text" name="uid" id="uidDesktop" data-type="phone" data-required="true" placeholder="Phone Number" onkeypress="javascript:callFnMyAccountLogin(this, event);" class="ie-ignore">
						</div>	
						<div class="large-4 columns">
							<label class="password-label-ie" for="pwdDesktop">Password</label>
							<input type="password" name="pwd" id="pwdDesktop" data-required="true" placeholder="Password" onkeypress="javascript:callFnMyAccountLogin(this, event);" class="ie-ignore">
						</div>	
						<div class="large-2 columns">
							<a href="#" id="btn-login" class="button small" onClick="document.getElementById('myaccountlogin').submit();">Log In</a>
						</div>	
						<div class="large-2 columns"></div>
					</div>	
					<div class="row"><div class="large-12 errors-login columns"></div></div>
					<div class="row">	
						<div class="large-6 columns">
							<ul>
								<li class="no-pipe">
									<label for="checkbox1">
										<input type="checkbox" id="checkbox1" style="display: none;">
										<span class="custom checkbox" onclick="rememberMe(this,'desktop');"></span> Remember Me
									</label>						
								</li>
								<li><a href="${forgotPasswordLink}">Forgot your Password?</a></li>
								<li class="no-pipe"><a href="${cricketMyAccountHostURL}">Register</a></li>
							</ul>	
						</div>				
					</div>	
				</form>			
			</dsp:oparam>
			<dsp:oparam name="false">		
			</dsp:oparam>
		</dsp:droplet>
	</div>	
</div>
</dsp:page>		