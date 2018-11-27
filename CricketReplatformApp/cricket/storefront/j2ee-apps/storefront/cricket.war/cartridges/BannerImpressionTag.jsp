<dsp:page>	
	<script type="text/javascript">	
	   	var pageID = "http://www.mycricket.com/";
		var bannner = '<dsp:valueof param="banner"/>';		
		//alert(bannner);
	   	var userIntention = '<dsp:valueof param="userIntention"/>';
	   	var customerType = '<dsp:valueof bean='/atg/cricket/util/CricketProfile.customerType'/>';	   	
		var promoId = '<dsp:valueof param="promoId"/>';
		var linkName = '<dsp:valueof param="linkName"/>';						
		var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
		var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
		var network = '<dsp:valueof bean='/com/cricket/util/LocationInfo.networkProviderName'/>';				
		var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
		var trackSitePromotionID = "hero"+"-_-"+promoId+"-_-"+linkName;	
		cmCreateManualImpressionTag ( pageID, trackSitePromotionID, null, null, pvAttrs  );
   </script>
   
</dsp:page>