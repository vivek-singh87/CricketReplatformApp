<dsp:page>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="findAStoreLink" bean="CricketConfiguration.homePageLinks.findStore" />
<dsp:getvalueof var="myAccountURL" bean="CricketConfiguration.homePageLinks.myAccountURL" />
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<div style="height:auto" id="flex-bucket" class="static-banner footer">	
	  <div class="row">
	    <div class="columns large-12 small-12">
	      <h1><span>add features</span> on the go<br /> with flex bucket</h1>
	    </div>
	  </div>
	  <div class="row">
	    <div id="flex-left" class="content columns large-6 small-12">
	      <p>Keeping money in your Flex Bucket lets you buy mobile services whenever you need them. Get what you want including:</p>
	      <ul class="square">
	        <li>RINGTONES, GAMES, AND GRAPHICS</li>
	        <li>NATIONWIDE ROAMING</li>
	        <li>PAY-AS-YOU-GO DATA</li>
	        <li>INTERNATIONAL CALLING AND MESSAGING</li>
	      </ul>
	    </div>
	    <div id="flex-right" class="content columns large-6 small-12">
	      <h3>THERE ARE MANY WAYS TO ADD FUNDS TO YOUR FLEX BUCKET:</h3>
	      <ol>
	      	<li>Go to My Account on your Cricket phone(via My Account icon or mobile web)</li>
	        <li>Dial *PAY (*729) from your Cricket phone</li>
	        <li>Call 1-800-CRICKET</li>
	        <li><a style="display:inline" href="${findAStoreLink}">Find a store</a> for a payment location near you</li>
	        <li>Log into <a style="display:inline" href="${myAccountURL}">My Account</a> and click "Add New Features" to add a monthly Flex Bucket fund</li>
	      </ol>
	      <p>Your Flex Bucket balance will rollover from month to month expiring one year after the date of purchase.</p>
	    </div>
	  </div>
	</div>

	<dsp:include page="/browse/banners/footerPromotionalBanner.jsp">
		<dsp:param name="PAGE_NAME" value="PLAN_ADDON_LISTING"/>
	</dsp:include> 

</dsp:page>