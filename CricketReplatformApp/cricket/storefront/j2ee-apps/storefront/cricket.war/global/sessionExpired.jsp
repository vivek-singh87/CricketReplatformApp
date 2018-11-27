<dsp:page>

	<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:importbean bean="/atg/commerce/catalog/CategoryLookup"/>
	<dsp:importbean bean="/atg/endeca/assembler/SearchFormHandler"/>
	
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
	<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
	<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="otherAddonsCategoryId" bean="CricketConfiguration.otherAddonsCategoryId"/>
	<dsp:getvalueof var="coverageLink" bean="CricketConfiguration.homePageLinks.coverage" />
	<dsp:getvalueof var="activateLink" bean="CricketConfiguration.homePageLinks.activate" />
	<dsp:getvalueof var="makePaymentLink" bean="CricketConfiguration.homePageLinks.makePayment" />
	<dsp:getvalueof var="myAccountLogin" bean="CricketConfiguration.homePageLinks.myAccountLogin" />
	
	<dsp:getvalueof var="allSupportLink" bean="CricketConfiguration.homePageLinks.allSupport" />
	<dsp:getvalueof var="myAccountLink" bean="CricketConfiguration.homePageLinks.myAccount" />
	<dsp:getvalueof var="phoneTroubleShootingLink" bean="CricketConfiguration.homePageLinks.phoneTroubleShooting" />
	<dsp:getvalueof var="rootcontextpath" bean="CricketConfiguration.rootcontextpath"/>
	<dsp:importbean bean="/atg/multisite/Site" var="currentSite"/>
	
<dsp:include page="/common/head.jsp">
	<dsp:param name="seokey" value="invalidPageKey" />
</dsp:include>

<body>
<div id="outer-wrap">
<div id="inner-wrap">
<dsp:include page="/common/header.jsp"/>
 <!--/#header-->	
<!--// END HEADER AREA //-->
<dsp:form action="${contextpath}/browse" id="searchFormPNF" requiresSessionConfirmation="false" style="display:none">
		<input id="dySearchFormPNF" type="hidden" name="Dy" value="1"/>
		<input id="nrPPSearchFormPNF" type="hidden" name="Nrpp" value="10000"/>
		<input id="ntySearchFormPNF" type="hidden" name="Nty" value="1"/>
        <input type="text" name="Ntt" id="search-newPNF" placeholder="Search" autocomplete="off">
        <dsp:input type="hidden" id="siteIdsSearchFormPNF" value="${currentSite.id}" bean="SearchFormHandler.siteIds" name="siteIds"/>
        <dsp:input type="submit" bean="SearchFormHandler.search" name="search" 
          value="submit" id="atg_store_searchSubmitPNF" title="submit" iclass="btn-search"/>
</dsp:form>
<div id="constructor" class="section-search-results">
  
	<section id="search-header">
	  <div class="row">
	    <div class="columns large-12 small-12">
	      <h1>Session Expired</h1>
	      <div class="row">
	        <div class="columns large-12 small-12">
	          <p class="sorry">It appears your session has expired.</p>
	        </div>
	      </div>
	      <div class="row collapse">
	        <div class="columns large-11 small-9">
	          <input type="text" name="search-full" id="search-full" placeholder="Search" onkeyup="detectEnterPNFSearchForm(event)">
	        </div>
	        <div class="columns large-1 small-3">
	          <a class="button prefix orange-button" href="javascript:void()" onclick="submitPNFSearchForm()">Search</a>
	        </div>
	      </div>
	    </div>
	  </div>

	</section>
	
	<hr>

	<!-- Search Support Links -->
	<section id="search-support-links">
  <div class="row">
    <div class="columns large-12">
      	<a href="${allSupportLink}" class="circle-arrow">Support</a>
		<a href="${myAccountLink}" class="circle-arrow">Account Management</a>
		<a href="${phoneTroubleShootingLink}" class="circle-arrow">Phone Troubleshooting</a>
    </div>
  </div>
</section>
	<hr>

	<section id="search-header">
		<div class="row">
			<div class="columns large-12 small-12">
				<h1>Site Map</h1>
			</div>
		</div>
	</section>

	<section class="site-map">

		<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
			<dsp:param name="inUrl" value="${rootcontextpath}"/>
			<dsp:oparam name="output">
		    	<dsp:getvalueof var="rootcontextpath" param="nonSecureUrl" />
		  	</dsp:oparam>
		</dsp:droplet>
		
		<div class="row">
			<div class="columns large-3 small-6">
				<h3><a href="${rootcontextpath}">Home</a></h3>
				<ul>
					<li><a href="${findAStoreLink}">Find A Store</a></li>
					<li><a href="${contactUsLink}">Contact Us</a></li>
					<li><a href="javascript:void()" onclick="cartOpen()">My Shopping Cart</a></li>
					<li><a href="${coverageLink}">Coverage</a></li>
					<li><a href="${signUpLink}">Email Signup</a></li>
				</ul>	
			</div>
			
			<dsp:droplet name="DimensionValueCacheDroplet">
				<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="phonesCategoryIdCacheEntry" param="dimensionValueCacheEntry" />
		      </dsp:oparam>
			</dsp:droplet>
								
			<div class="columns large-3 small-6">
				<h3><a href="${contextpath}${phonesCategoryIdCacheEntry.url}">Phones</a></h3>
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
				    <li><a href="${mailInOffersLink}">Mail-In Offers</a></li>
				</ul>	
			</div>
			
			<dsp:droplet name="DimensionValueCacheDroplet">
		    	<dsp:param name="repositoryId" bean="CricketConfiguration.allPlansCategoryId"/>
		    	<dsp:oparam name="output">
		    		<dsp:getvalueof var="allPlansCategoryCacheEntry" param="dimensionValueCacheEntry" />
		    	</dsp:oparam>
		    </dsp:droplet>
		    <dsp:droplet name="DimensionValueCacheDroplet">
				<dsp:param name="repositoryId" value="${otherAddonsCategoryId}"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="addonsCategoryCacheEntry" param="dimensionValueCacheEntry" />
			    </dsp:oparam>
			</dsp:droplet>
			
			<div class="columns large-3 small-6">
				<h3><a href="${contextPath}<c:out value='${allPlansCategoryCacheEntry.url}'/>">Plans</a></h3>
				<ul>
					<li><a href="${contextPath}<c:out value='${allPlansCategoryCacheEntry.url}'/>">All Plans</a>
						<ul>
							<li><a href="${contextpath}${addonsCategoryCacheEntry.url}">Plan Add-ons</a></li>
						</ul>	
					</li>
				</ul>
			</div>
			
			<dsp:getvalueof var="cellPhonesLink" bean="CricketConfiguration.homePageLinks.cellPhones" />
			<dsp:getvalueof var="plansLink" bean="CricketConfiguration.homePageLinks.plans" />
			<dsp:getvalueof var="muveMusicLink" bean="CricketConfiguration.homePageLinks.muveMusic" />
			<dsp:getvalueof var="phonePaymentPlansLink" bean="CricketConfiguration.homePageLinks.phonePaymentPlans" />
			<dsp:getvalueof var="aboutCricketLink" bean="CricketConfiguration.homePageLinks.aboutCricket" />
			<dsp:getvalueof var="whyCricURL" value="${contextPath}/whycricket/why-cricket-landing.jsp" />
			
			<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
			  <dsp:param name="inUrl" value="${whyCricURL}"/>
			  <dsp:oparam name="output">
			    <dsp:getvalueof var="whyCricURL" param="nonSecureUrl" />
			  </dsp:oparam>
			</dsp:droplet>
			
			<div class="columns large-3 small-6">
				<h3><a href="${whyCricURL}">Why Cricket</a></h3>
				<ul>
					<li><a href="${aboutCricketLink}">About Cricket</a>
  					<ul>
  						<li><a href="${cellPhonesLink}">Cell Phones</a></li>
						<li><a href="${plansLink}">Plans</a></li>
						<li><a href="${muveMusicLink}">Muve Music<sup>&reg;</sup></a></li>
						<li><a href="${phonePaymentPlansLink}">Phone Payment Plans</a></li>
  						<li><a href="${coverageLink}">Coverage</a></li>
  					</ul>
					</li>
				</ul>	
			</div>									

			<div class="columns large-3 small-6">
				<h3><a href="${allSupportLink}">Support</a></h3>
				<ul>
					<li><a href="${faqsLink}">FAQs</a></li>
					<li><a href="${contactUsLink}">Contact Us</a></li>
					<li><a href="${howToVideosLink}">How-To Videos</a></li>
					<li><a href="${returnPolicyLink}">Return Policy</a></li>
				</ul>	
			</div>
			
			<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
				<dsp:param name="inUrl" value="../../billingordersummary/enterBillingOrderID.jsp"/>
				<dsp:oparam name="output">
			    	<dsp:getvalueof var="orderStatusURL" param="nonSecureUrl" />
			  	</dsp:oparam>
			</dsp:droplet>
			
			<div class="columns large-3 small-6">
				<h3><a href="${myAccountLogin}">My Account</a></h3>
				<ul>
					<li><a href="${myAccountLogin}">Sign In</a></li>
					<li><a href="${makePaymentLink}">Make A Payment</a></li>
					<li><a href="${activateLink}">Activate</a>
					</li><dsp:a href="${orderStatusURL}">Check Order Status</dsp:a></li>
				</ul>	
			</div>
			
			<div class="columns large-3 small-6">
				<h3 style="color: #008752">Corporate &amp; Partner Information</h3>
				<ul>
					<li><a href="${corporateInfoLink}">Corporate Information</a></li>
					<li><a href="${careersLink}">Careers</a></li>
					<li><a href="${becomeDealerLink}">Become a Dealer</a></li>
					<li><a href="${becomeSupplierLink}">Become a Supplier</a></li>
				</ul>	
			</div>
			
			<div class="columns large-3 small-6">
				<h3 style="color: #008752">Cricket News</h3>
				<ul>
					<li><a href="${cricketNewsLink}">News</a></li>
					<li><a href="${eventsLink}">Events</a></li>
					<li><a href="${the411Link}">The 411</a></li>
					<li><a href="${musicLink}">Music</a></li>
					<li><a href="${videosLink}">Videos</a></li>
				</ul>	
			</div>									
		</div>				
	</section>
</div>


<!--// START FOOTER AREA //-->
	
	<dsp:include page="/common/footer.jsp"/>	
		
</div> <!--/#inner-wrap-->
</div> <!--/#outer-wrap-->
	
	
<!-- JavaScript -->	

<!-- jQuery -->	
<script type="text/javascript" src="${contextPath}/js/vendor/jquery-ui.min.js"></script>
<script type="text/javascript" src="${contextPath}/js/customcricketstore.js"></script>

<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="Plans"/>
</dsp:include>

<!-- Foundation 4 -->
<script src="${contextPath}/js/foundation/foundation.min.js"></script> 
<script>
$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips');
</script>

<!-- Client Side Validation -->
<script src="${contextPath}/js/vendor/jquery.validate.min.js"></script>

<!-- SWIPER Library; Used for Phones and Plans on small screens -->
<script src="${contextPath}/lib/swiper/idangerous.swiper.js"></script>

<!-- Auto complete Plugin for Search -->
<script src="${contextPath}/lib/autocomplete/jquery.autocomplete.js" type="text/javascript"></script>

<!-- Cricket specific JS -->
<!-- <script src="js/cricket.min.js"></script> -->
<script src="${contextPath}/js/cricket.min.js"></script> <!-- Global Utilities -->

<script src="${contextPath}/js/search.js"></script><!-- Search Section Specific JS -->
<script src="${contextPath}/js/phones.js"></script><!-- Phones Section Specific JS -->
<script src="${contextPath}/js/plans.js"></script><!-- Plans Section Specific JS -->
<script src="${contextPath}/js/checkout-min.js"></script><!-- Checkout Section Specific JS -->
<script src="${contextPath}/js/cart.js"></script><!-- Cart Section Specific JS -->

<!--  Google javascripts for mobile device -->
<script src="${contextPath}/js/geo/geo-min.js" type="text/javascript" charset="utf-8"></script>
<script src="${contextPath}/js/geo/geo_position_js_simulator.js" type="text/javascript" charset="utf-8"></script>
<script src="${contextPath}/js/customcricketstore.js" type="text/javascript" charset="utf-8"></script>
<script src="${contextPath}/js/cricket/cricket-endeca-auto-suggest.js"></script>

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
		
<dsp:include page="/common/includes/openCartDrawer.jsp"/> 
<script type="text/javascript">
	var productName = '<dsp:valueof value='${planName}'/>';
	var productId = '<dsp:valueof value='${productId}'/>';
	var productCategoryId = "VOICE PLANS";
	var pageID = "PLAN DETAILS"+" "+productName;
	var categoryID = "SHOP:PLANS";
	//must be dynamic values	
	var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
	var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
	var network = '<dsp:valueof value='${network}'/>';
	var userIntention = '<dsp:valueof value='${userIntention}'/>';
	var customerType = '<dsp:valueof value='${customerType}'/>';
	var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
	cmCreatePageviewTag(pageID,categoryID,null,null,pvAttrs);
	cmCreateProductviewTag(productId,productName,productCategoryId,pvAttrs );
</script>
</dsp:page>