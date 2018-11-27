<dsp:page>

	<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:importbean bean="/atg/commerce/catalog/CategoryLookup"/>
	
	<dsp:getvalueof var="facebookLink" bean="CricketConfiguration.socialMediaLinks.facebook" />
	<dsp:getvalueof var="youtubeLink" bean="CricketConfiguration.socialMediaLinks.youtube" />
	<dsp:getvalueof var="instagramLink" bean="CricketConfiguration.socialMediaLinks.instagram" />
	<dsp:getvalueof var="flickerLink" bean="CricketConfiguration.socialMediaLinks.flicker" />
	<dsp:getvalueof var="twitterLink" bean="CricketConfiguration.socialMediaLinks.twitter" />
	
	<dsp:getvalueof var="findAStoreLink" bean="CricketConfiguration.homePageLinks.findStore" />
	
	<dsp:getvalueof var="mailInOffersLink" bean="CricketConfiguration.homePageLinks.mailInOffers" />
	<dsp:getvalueof var="faqsLink" bean="CricketConfiguration.homePageLinks.faqs" />
	<dsp:getvalueof var="returnPolicyLink" bean="CricketConfiguration.homePageLinks.returnPolicy" />
	<dsp:getvalueof var="howToVideosLink" bean="CricketConfiguration.homePageLinks.howToVideos" />
	<dsp:getvalueof var="contactUsLink" bean="CricketConfiguration.homePageLinks.contactUs" />
	<dsp:getvalueof var="phonePaymentPlansLink" bean="CricketConfiguration.homePageLinks.phonePaymentPlans" />
	<dsp:getvalueof var="aboutCricketLink" bean="CricketConfiguration.homePageLinks.aboutCricket" />
	<dsp:getvalueof var="whyCricketLink" bean="CricketConfiguration.homePageLinks.whyCricket" />
	<dsp:getvalueof var="musicLink" bean="CricketConfiguration.homePageLinks.music" />
	<dsp:getvalueof var="muveMusic" bean="CricketConfiguration.homePageLinks.muveMusic" />
	<dsp:getvalueof var="becomeDealerLink" bean="CricketConfiguration.homePageLinks.becomeDealer" />
	<dsp:getvalueof var="becomeSupplierLink" bean="CricketConfiguration.homePageLinks.becomeSupplier" />
	<dsp:getvalueof var="corporateInfoLink" bean="CricketConfiguration.homePageLinks.corporateInfo" />
	<dsp:getvalueof var="careersLink" bean="CricketConfiguration.homePageLinks.careers" />
	<dsp:getvalueof var="the411Link" bean="CricketConfiguration.homePageLinks.the411" />
	<dsp:getvalueof var="newsLink" bean="CricketConfiguration.homePageLinks.news" />
	<dsp:getvalueof var="cricketNewsLink" bean="CricketConfiguration.homePageLinks.cricketNews" />
	<dsp:getvalueof var="eventsLink" bean="CricketConfiguration.homePageLinks.events" />
	<dsp:getvalueof var="newsLink" bean="CricketConfiguration.homePageLinks.news" />
	
	<dsp:getvalueof var="sendTextLink" bean="CricketConfiguration.homePageLinks.sendText" />
	<dsp:getvalueof var="privacyPolicyLink" bean="CricketConfiguration.homePageLinks.privacyPolicy" />
	<dsp:getvalueof var="legalLinksLink" bean="CricketConfiguration.homePageLinks.legalLinks" />
	<dsp:getvalueof var="siteMapLink" bean="CricketConfiguration.homePageLinks.siteMap" />
	<dsp:getvalueof var="videosLink" bean="CricketConfiguration.homePageLinks.videos" />
	<dsp:getvalueof var="signUpLink" bean="CricketConfiguration.homePageLinks.signUp" />
	<input id="signUpLink" type="hidden" value="${signUpLink}"/>
	
	<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
		<dsp:param name="inUrl" value="/"/>
		<dsp:oparam name="output">
	    	<dsp:getvalueof var="newContextPath" param="nonSecureUrl" />
	  	</dsp:oparam>
	</dsp:droplet>
	<dsp:getvalueof var="newContextPath" value="${fn:substring(newContextPath, 0, fn:length(newContextPath)-1)}" />
	<dsp:getvalueof var="contextPath" value="${newContextPath}"/>
	<script>
		function redirectForEmailSignup(email) {
			var emailId = document.getElementById('email-sign-up').value;
			if (null != emailId && emailId != '' && emailId.indexOf('@') != -1) {
				window.location.href=email + "?footerSignupEmail=" + document.getElementById('email-sign-up').value;
			}
		}
		function callValidateEmailSignUP(){
			var emailsignup = $("#email-sign-up").val();
	
			if(emailsignup==""){
				$(".errors-email-signup").html("");
				setPlaceHolderValueIE();				
			}		
		}
		function enterButton(handle, event) {
			var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			var email = $('#email-sign-up').val();
			if(regex.test(email)) {
				if (null != event && event.keyCode == 13) {
					var emailLink = $('#signUpLink').val();
					redirectForEmailSignup(emailLink);
				}
			}

	        return false;
	    }
		function validateEmail() {
			var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			var email = $('#email-sign-up').val();
			if(regex.test(email)) {
				var emailLink = $('#signUpLink').val();
				redirectForEmailSignup(emailLink);	
			} else {
				$("#emailSignupForm").submit();
			}
	        return false;
		}
		
		function setPlaceHolderValueIE(){		
			var rv = -1;
			if (navigator.appName == 'Microsoft Internet Explorer')
			  {
				var userAgent = navigator.userAgent;
				var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
				if (re.exec(userAgent) != null)
				  rv = parseFloat( RegExp.$1 );
			  }
			  if(rv >= 0){		 
				$("#email-sign-up").val('Enter email address');
			  }
		}
	</script>
	
	<footer id="footer">	
		
		<div id="footer-connect">		
			<div class="row">	
				<div class="large-8 small-12 columns">
					<form name="emailSignupForm" id="emailSignupForm" method="post">
						<div class="row">				
							<div class="large-7 small-12 columns">
								<h3><label for="email-sign-up"><crs:outMessage key="cricket_footer_signUpDeals" /></label></h3>	
							</div>
							<div class="large-5 small-12 columns">
								<fieldset>	
									<div class="row collapse">
										<div class="small-10 large-10 columns">
											<input type="email" name="email-sign-up" id="email-sign-up" onblur="javascript:callValidateEmailSignUP();" onkeypress="javascript:enterButton(this,event);" placeholder='Enter email address' >
										</div>
										<div class="small-2 large-2 columns">
											<a href="javascript:void();" id="btn2-email-signup" class="button small" onclick="validateEmail();">></a>
										</div>
									</div>
								</fieldset>	
							</div>									
						</div>
						<div class="row"><div class="large-5 large-offset-7 small-12 errors-email-signup columns"></div></div>
					</form>
				</div>	
				<div class="large-4 small-12 columns">
					<ul class="social-links">
						<li id="li-twitter"><a href="${twitterLink}" target="_blank">Twitter</a></li>
						<li id="li-facebook"><a href="${facebookLink}" target="_blank">Facebook</a></li>
						<li id="li-youtube"><a href="${youtubeLink}" target="_blank">YouTube</a></li>
						<li id="li-instagram"><a href="${instagramLink}" target="_blank">Instagram</a></li>
						<li id="li-flickr" class="last"><a href="${flickerLink}" target="_blank">Flickr</a></li>
					</ul>
				</div>				
			</div>
		</div>	<!--/#footer-connect-->
<!--END: HTML for Click to Chat view -->

<!--NOTE: Show this section of HTML if Click to Chat is UNavailable-->
		<div id="footer-callouts" class="row">			
			<div class="large-4 small-12 columns">
						<div class="panel callout">
							<h3>Talk to Sales</h3>
							<ul class="inline-list">
								<li><span>1-800-975-3708</span></li>
							<!--NOTE: Show this LI of HTML if Click to Chat is available-->	
								<li class="hide-for-small border-link" id="click-chat"> <div id="cricket-modal-chat"></div> </li>								
							</ul>	
						</div>	
			</div>	
			<div class="large-4 small-12 columns">
				<div class="panel callout">
					<h3><crs:outMessage key="cricket_footer_needSupport" /></h3>
					<ul class="inline-list">
						<li><span><crs:outMessage key="cricket_footer_needSupportContactNo" /></span></li>
						<li class="border-link"><a href="${faqsLink}" class="circle-arrow">FAQs</a></li>	
					</ul>															
				</div>	
			</div>	
			<div class="large-4 small-12 columns">
				<div class="panel callout">
					<h3><crs:outMessage key="cricket_footer_wantToVisitUs" /></h3>
					<ul class="inline-list">
						<li class="find-store"><a class="circle-arrow" href="${findAStoreLink}"><crs:outMessage key="cricket_footer_findCricketStore" /></a></li>	
					</ul>					
				</div>	
			</div>						
		</div> <!--/#footer-callouts-->	
<!--END: HTML for NON Click to Chat view -->	
		
		<div class="row">														
			<div id="footer-sitemap" class="row" data-section="accordion">
				<section class="large-2 columns">
					<h3 class="title" data-section-title><a href="#"><crs:outMessage key="cricket_footer_phonesAndPlans" /></a></h3>
					<div class="content" data-section-content>
						<ul>
							<dsp:droplet name="CategoryLookup">
								<dsp:param name="id" bean="CricketConfiguration.phonesCategoryId"/>
						    	<dsp:oparam name="output">
						    		<dsp:droplet name="ForEach">
						    			<dsp:param name="array" param="element.childCategories"/>
						    			<dsp:param name="elementName" value="childCategory"/>
						    			<dsp:oparam name="output">
						    				<dsp:droplet name="DimensionValueCacheDroplet">
												<dsp:param name="repositoryId" param="childCategory.id"/>
												<dsp:oparam name="output">
													<dsp:getvalueof var="childCategoryCacheEntry" param="dimensionValueCacheEntry" />
												</dsp:oparam>
											</dsp:droplet>
											<li><a href="${contextPath}<c:out value='${childCategoryCacheEntry.url}'/>">
													<dsp:valueof param="childCategory.displayName"/>
												</a>
											</li>
						    			</dsp:oparam>
						    		</dsp:droplet>
						    	</dsp:oparam>
						    </dsp:droplet>
						    <dsp:droplet name="DimensionValueCacheDroplet">
						    	<dsp:param name="repositoryId" bean="CricketConfiguration.allPlansCategoryId"/>
						    	<dsp:oparam name="output">
						    		<dsp:getvalueof var="allPlansCategoryCacheEntry" param="dimensionValueCacheEntry" />
						    	</dsp:oparam>
						    </dsp:droplet>
						    <li><a href="${contextPath}<c:out value='${allPlansCategoryCacheEntry.url}'/>">
									Plans
								</a>
							</li>
							<li><a href="${mailInOffersLink}">Mail-In Offers</a></li>
						</ul>
					</div>
				</section>
				<section class="large-2 columns">
					<h3 class="title" data-section-title><a href="#">Support</a></h3>
					<div class="content" data-section-content>
						<ul>
							<li><a href="${faqsLink}">FAQs</a></li>
							<li><a href="${contactUsLink}">Contact Us</a></li>
							<li><a href="${howToVideosLink}">How-To Videos</a></li>
							<li><a href="${returnPolicyLink}">Return Policy</a></li>							
						</ul>
					</div>
				</section>
				<section class="large-3 columns">
					<h3 class="title" data-section-title><a href="#"><crs:outMessage key="cricket_footer_whyCricket" /></a></h3>
					<div class="content" data-section-content>
						<ul>
							<li><a href="${aboutCricketLink}">About Cricket</a></li>
							<li><a href="${muveMusic}">Muve Music&#174;</a></li>
							<li><a href="${phonePaymentPlansLink}">Phone Payment Plans</a></li>
						</ul>
					</div>
				</section>
				<section class="large-3 columns">
					<h3 class="title" data-section-title><a href="#"><crs:outMessage key="cricket_footer_corpAndPartInfo" /></a></h3>
					<div class="content" data-section-content>
						<ul>
							<li><a href="${corporateInfoLink}">Corporate Information</a></li>
							<li><a href="${careersLink}">Careers</a></li>
							<li><a href="${becomeDealerLink}">Become a Dealer</a></li>
							<li><a href="${becomeSupplierLink}">Become a Supplier</a></li>						
						</ul>
					</div>
				</section>
				<section class="large-2 columns">
					<h3 class="title" data-section-title><a href="#"><crs:outMessage key="cricket_footer_cricketNews" /></a></h3>
					<div class="content" data-section-content>
						<ul>
							<li><a href="${cricketNewsLink}">News</a></li>
							<li><a href="${eventsLink}">Events</a></li>
							<li><a href="${the411Link}">The 411</a></li>
							<li><a href="${musicLink}">Music</a></li>
							<li><a href="${videosLink}">Videos</a></li>
						</ul>
					</div>
				</section>															
			</div>	
		</div>
		
		<div id="footer-text-message" class="row">
			<hr/>
			<div class="large-6 small-12 columns">				
				<h3>Send A Text Message Online</h3>
			</div>
			<div class="large-6 small-12 columns">
				<p><a class="circle-arrow" href="${sendTextLink}">Send A Text</a></p>
			</div>			
		</div>			
	</footer>
	
	<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
		<dsp:param name="inUrl" value="../../global/siteMap.jsp"/>
		<dsp:oparam name="output">
	    	<dsp:getvalueof var="siteMapURL" param="nonSecureUrl" />
	  	</dsp:oparam>
	</dsp:droplet>
	
	<div id="footer-utility">
		<div class="row">
			<div class="large-6 small-12 columns">
				<ul>	
					<li><a href="${privacyPolicyLink}">Privacy Policy</a></li>
					<li><a href="${legalLinksLink}">Legal Links</a></li>
					<li class="last"><a href="${siteMapURL}">Site Map</a></li>										
				</ul>
			</div>
			<div class="large-6 small-12 columns">
				<p class="right">&copy; Cricket Communications, Inc.</p>
			</div>	
		</div>	
		<div class="row" id="disclaimer-container">
			<div class="large-10 small-12 columns">
				<p style="display:block;"><crs:outMessage key="cricket_footer_legal" /></p>
				<!-- <p style="display:block;">Terms, conditions and other restrictions apply. Subject to credit approval. Comparison based on AT&T Unlimited Nation Plan plus DataPro Smartphone package as of July 2013 and Verizon's Unlimited Nationwide Plan plus Smartphone Data as of July 2013. Third-party trademarks are the property of their respective owners. &copy; 2013 Cricket Communications, Inc.</p> -->
			</div>	
			<div id="verisign-container" class="large-2 small-6 columns">
			<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
			<dsp:param name="imageLink" value="img/fpo-verisign.png" />
			 <dsp:param name="protocalType" param="protocalType"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="liquidpixelurl" param="url" />					
				<img class="right" src="${liquidpixelurl}" alt="" />
			</dsp:oparam>
			</dsp:droplet>
				
				<!-- // Commenting out verisign seal code and have placeholder image for now as does not seem to work off the mycricket.com domain -->
 				<!--script src="https://seal.verisign.com/getseal?host_name=account.mycricket.com&size=S&use_flash=NO&use_transparent=NO&lang=en"></script-->
                <!--p><a href="http://www.verisign.com/ssl-certificate/" target="_blank"  style="font-size: 8px; color: #000; text-decoration: none; white-space: nowrap;">ABOUT SSL CERTIFICATES</a></p-->				
			</div>	
		</div>	
	</div> <!--/#footer-utility-->	
<%-- <input type="hidden" name="openCart" id="openCartDrawer" value="<dsp:valueof bean='/atg/commerce/order/purchase/CartModifierFormHandler.openCart'/>" /> --%>
</dsp:page>