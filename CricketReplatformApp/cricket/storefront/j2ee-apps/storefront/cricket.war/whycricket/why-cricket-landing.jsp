<dsp:page>

<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:getvalueof var="whycricketlearn" bean="CricketConfiguration.whyCricketLinks.whycricketlearn" />
<dsp:getvalueof var="whycrickettips" bean="CricketConfiguration.whyCricketLinks.whycrickettips" />
<dsp:getvalueof var="unlimitedplansshopnow" bean="CricketConfiguration.whyCricketLinks.unlimitedplansshopnow" />
<dsp:getvalueof var="unlimitedplanscompareplan" bean="CricketConfiguration.whyCricketLinks.unlimitedplanscompareplan" />
<dsp:getvalueof var="latestsmartphonesshopnow" bean="CricketConfiguration.whyCricketLinks.latestsmartphonesshopnow" />
<dsp:getvalueof var="latestsmartphonesfinenextphones" bean="CricketConfiguration.whyCricketLinks.latestsmartphonesfinenextphones" />
<dsp:getvalueof var="latestsmartphoneslearnmore" bean="CricketConfiguration.whyCricketLinks.latestsmartphoneslearnmore" />
<dsp:getvalueof var="checkcoverage" bean="CricketConfiguration.whyCricketLinks.checkcoverage" />
<dsp:getvalueof var="muvemusiclearnmore" bean="CricketConfiguration.whyCricketLinks.muvemusiclearnmore" />
<dsp:include page="/common/head.jsp">
<dsp:param name="seokey" value="whyCricketKey" />
<dsp:param name="pageType" value="whyCricketKeyLandingPage" />
</dsp:include>


<body>
<div id="outer-wrap">
<div id="inner-wrap">
   <dsp:include page="/common/header.jsp"/>
	<div id="constructor">
<!--// END OF HEADER AREA OF THE PAGE //-->		




<div id="constructor" class="section-why-cricket">

  <!-- Main Banner -->
  <section id="section-banner" class="why-cricket">
    <div class="why-power-pixels hide-for-small"></div>
    <div class="section-banner-content">
      <div class="row wrapper">
        <div class="large-5 small-12 columns left-content">
            <h1>Why <span>Cricket?</span></h1>
        </div>
        <div class="large-7 small-12 columns right-content">
          <h4>Because you want to spend less and do more With the money you save</h4>
          <ul class="first">
           <li>Half the price of the competition</li>
            <li>All the latest smartphones</li>
          </ul>
          <ul class="last">
            <li>Nationwide coverage</li>
            <li>Unlimited plans, including music</li>
          </ul>
          <p>All with "no contract" and free overnight shipping. Find the phone you really want with an unlimited plan that lets you keep more money in your pocket. At Cricket – Half is More.</p>
          <div class="row">
           <div class="column large-12 small-10">
             <a class="button orange-button" href="${whycricketlearn}">Learn more about Cricket</a>
             <a class="circle-arrow" href="${whycrickettips}">Tips for Switching</a>
          </div>
         </div>
        </div>
      </div>  
    </div> <!--/.section-banner-content-->
  </section> <!--/#section-banner-->
  <!-- END Main Banner -->
  
  <section id="plans-callout" class="plans-callout why-cricket">
   <div class="row">
     <div class="large-6 small-12 columns left-content">
     	<dsp:droplet name="DimensionValueCacheDroplet">
			<dsp:param name="repositoryId" bean="CricketConfiguration.allPlansCategoryId"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="allPlansCategoryCacheEntry" param="dimensionValueCacheEntry" />
		     </dsp:oparam>
		</dsp:droplet>
       <h2>Unlimited <span>Plans</span></h2>
       <h4>for Half the price of the competition</h4>
       <p>Switch to Cricket no-contract plans and have more in your pocket each month to do more of what matters to you.</p>
       <a href="${contextpath}${allPlansCategoryCacheEntry.url}" class="button grey-button">Shop Now</a>
       <a class="circle-arrow white" href="${unlimitedplanscompareplan}">Compare your plan</a>
       <h3 class="mixed-colors-title"><span class="yellow-green">Half</span><span class="dark-green">Is</span>More</h3>
       <ul class="small-block-grid-3 large-block-grid-4">

		<dsp:droplet
			name="/com/cricket/imageurl/ImageURLLookupDroplet">
			<dsp:param name="imageLink" value="img/why-cricket/thumb-video-1.jpg" />
			<dsp:oparam name="output">
				<dsp:getvalueof var="liquidpixelurl" param="url" />
			<li class="first"><a href="#" data-reveal-id="video-1" class="video-thumb"><img src="${liquidpixelurl}" /></a></li>
			</dsp:oparam>
		</dsp:droplet>
		<dsp:droplet
			name="/com/cricket/imageurl/ImageURLLookupDroplet">
			<dsp:param name="imageLink" value="img/why-cricket/thumb-video-2.jpg" />
			<dsp:oparam name="output">
				<dsp:getvalueof var="liquidpixelurl" param="url" />
 				<li><a href="#" data-reveal-id="video-2" class="video-thumb"><img src="${liquidpixelurl}" /></a></li>
			</dsp:oparam>
		</dsp:droplet>
		<dsp:droplet
			name="/com/cricket/imageurl/ImageURLLookupDroplet">
			<dsp:param name="imageLink" value="img/why-cricket/thumb-video-3.jpg" />
			<dsp:oparam name="output">
				<dsp:getvalueof var="liquidpixelurl" param="url" />
					<li><a href="#" data-reveal-id="video-3" class="video-thumb"><img src="${liquidpixelurl}" /></a></li>
			</dsp:oparam>
		</dsp:droplet>
       </ul>
      </div>
   </div>
  </section>
  
  <!-- Latest SmartPhones Callout -->
  <section id="latest-smartphones">
   <div class="row">
     <div class="large-6 large-offset-6 small-12 columns right-content">
		<dsp:droplet name="DimensionValueCacheDroplet">
			<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="allPhonesCategoryCacheEntry" param="dimensionValueCacheEntry" />
		     </dsp:oparam>
		</dsp:droplet>
       <h2>Latest Smartphones? <span>We’ve Got them</span></h2>
       <p>Cricket has the devices you want — from iPhone 5 to a lineup of Samsung Galaxy smartphones that let you do it all…browse, game, shop, email, you name it. Choose the one that’s right for you.</p>
       <a class="button orange-button" href="${contextpath}${allPhonesCategoryCacheEntry.url}">Shop Now</a>
       <a class="circle-arrow" href="${latestsmartphonesfinenextphones}">Find your next phone here</a>
     </div>
    </div>
  </section>
  
  <!-- Flexible Phone Payment Plans Callout -->
  <section id="flexible-payment-plans">
  
  </section>
  
  <!-- Get the Coverage You Need Callout -->
  <section id="coverage">
   <div class="row">
     <div class="large-10 large-centered small-12 columns text-center">
       <h2>Get the coverage you need</h2>
       <p class="sub-title">With nationwide coverage in all 50 states, count on Cricket to help you reach more people in more places.</p>
     </div>
    </div>
    <div class="row">
     <div class="large-6 small-12 columns left-content text-center">
     <dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
		<dsp:param name="imageLink" value="img/why-cricket/4g.jpg" />
		<dsp:oparam name="output">
			<dsp:getvalueof var="liquidpixelurl" param="url" />
			  <img src="${liquidpixelurl}" />
		</dsp:oparam>
		</dsp:droplet>
     
       <p>Cricket’s 4G LTE coverage is available in select areas, with more coming soon!</p>
     </div>
     <div class="large-6 small-12 columns right-content text-center">
      <dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
		<dsp:param name="imageLink" value="img/why-cricket/map.jpg" />
		<dsp:oparam name="output">
			<dsp:getvalueof var="liquidpixelurl" param="url" />
			  <img src="${liquidpixelurl}" />
		</dsp:oparam>
		</dsp:droplet>
       <p>Cricket's got you covered from coast to coast.</p>
     </div>
    </div>
    <div class="row">
     <div class="large-12 small-12 columns text-center">
      <a class="button orange-button" href="${checkcoverage}">Check Coverage</a>
     </div>
    </div>
  </section>
  
  <!-- Song Downloads Callout -->
  <section id="song-downloads">
   <div class="row">
     <div class="large-6 large-offset-6 small-12 columns left-content">
       <h2>UNLIMITED MUSIC WITHOUT USING ANY OF YOUR DATA</h2>
       <h4>Unlimited song downloads included in all android&trade; plans</h4>
       <div class="row">
         <div class="columns large-7 small-7">
           <ul>
             <li>Better than streaming</li>
             <li>Over 12 million songs</li>
             <li>Listen anywhere</li>
           </ul>
           <a class="button grey-button" href="${muvemusiclearnmore}">Learn More</a>
         </div>
         <div class="columns large-5 small-5">
         <dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
			<dsp:param name="imageLink" value="img/why-cricket/muve-music-logo.png" />
			<dsp:oparam name="output">
				<dsp:getvalueof var="liquidpixelurl" param="url" />
				   <img src="${liquidpixelurl}" />
			</dsp:oparam>
			</dsp:droplet>
        
         </div>
       </div>
     </div>
    </div>
  </section>


</div> <!--/#constructor-->



	<dsp:droplet name="/atg/dynamo/droplet/Cache">
		<dsp:param value="cricketFooter" name="key"/>
		<dsp:oparam name="output">
			<dsp:include page="/common/footer.jsp"/>
		</dsp:oparam>
	</dsp:droplet>
		
</div> <!--/#inner-wrap-->
</div> <!--/#outer-wrap-->

<!-- JavaScript -->

<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="WhyCricketLandingPage"/>
</dsp:include>	

<!-- jQuery -->	

<!-- jQuery -->
<script type="text/javascript" src="${contextpath}/js/vendor/jquery-ui.min.js"></script>
<script type="text/javascript" src='${contextpath}/js/chatGui-min.js'></script>

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


<!-- Modals for Why Cricket Landing Page -->
<div id="video-1" class="reveal-modal medium">
  <div class="flex-video">
    <iframe width="420" height="315" src="http://www.youtube.com/embed/Rjx2GpzqjiE" frameborder="0" allowfullscreen></iframe>
  </div>
  <a class="close-reveal-modal">&#215;</a>
</div>

<div id="video-2" class="reveal-modal medium">
  <div class="flex-video">
    <iframe width="420" height="315" src="http://www.youtube.com/embed/5ZCRlxGxYw8?list=PLxIy_TVfwk8Q9tELrziBveXit3AxGPASz" frameborder="0" allowfullscreen></iframe>
  </div>
  <a class="close-reveal-modal">&#215;</a>
</div>

<div id="video-3" class="reveal-modal medium">
  <div class="flex-video">
    <iframe width="420" height="315" src="http://www.youtube.com/embed/8bgPuEQ9mzQ?list=PLxIy_TVfwk8Q9tELrziBveXit3AxGPASz" frameborder="0" allowfullscreen></iframe>
  </div>
  <a class="close-reveal-modal">&#215;</a>
</div>


</body>
</html>
</dsp:page>