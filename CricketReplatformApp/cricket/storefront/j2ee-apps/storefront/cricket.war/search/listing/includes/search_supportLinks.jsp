<dsp:page>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:getvalueof var="allSupportLink" bean="CricketConfiguration.homePageLinks.allSupport" />
	<dsp:getvalueof var="myAccountLink" bean="CricketConfiguration.homePageLinks.myAccount" />
	<dsp:getvalueof var="phoneTroubleShootingLink" bean="CricketConfiguration.homePageLinks.phoneTroubleShooting" />
	<hr/>
	<section id="search-support-links">
		<div class="row">
			<div class="columns large-12">
				<a href="${allSupportLink}" class="circle-arrow">Support</a>
				<a href="${myAccountLink}" class="circle-arrow">Account Management</a>
				<a href="${phoneTroubleShootingLink}" class="circle-arrow">Phone Troubleshooting</a>
			</div>
		</div>
	</section>
	<c:if test="${zeroResultSearch eq 'yes'}">
		<!-- <section id="similar-phones" class="three-callouts">
			<div class="row">
				<div class="large-8 small-12 columns">
					<h2>Our <span>Phones</span></h2>
				</div>
				<div class="large-4 small-12 hide-for-small columns">
					<a href="#" class="circle-arrow right">See All Phones</a>
				</div>
			</div>
			<a href="#" class="prev show-for-small">Prev</a>
			<a href="#" class="next show-for-small">Next</a>		
			<div class="swiper-container show-for-small">
				<div class="swiper-wrapper">
				First Slide
				<div class="swiper-slide text-center"> 
					<a href="#"><img src="img/phone-detail/similar-phone-1.png" alt=""/></a>
					<h4>Apple iPhone 5</h4>
					<a href="#" class="button">Shop Now</a>
				</div>
				Second Slide
				<div class="swiper-slide text-center">
					<a href="#"><img src="img/phone-detail/similar-phone-2.png" alt=""/></a>
					<h4>Samsung Galaxy S<sup>&reg;</sup> 3</h4>
					<a href="#" class="button">Shop Now</a>
				</div>
				Third Slide
				<div class="swiper-slide text-center"> 
					<a href="#"><img src="img/phone-detail/similar-phone-3.png" alt=""/></a>
					<h4>HTC Desire C</h4>
					<a href="#" class="button">Shop Now</a>
				</div>
				Fourth Slide
				<div class="swiper-slide text-center"> 
					<a href="#"><img src="img/phone-detail/similar-phone-4.png" alt=""/></a>
					<h4>HTC One V</h4>
					<a href="#" class="button">Shop Now</a>
				</div>
				</div>	
			</div>	
			<div class="row cta show-for-small">
				<div class="small-12 columns">
					<p><a href="#" class="circle-arrow">See All Phones</a></p>
				</div>
			</div>
			Similar Phones - Desktop		
			<div class="row hide-for-small">	
				<div class="large-3 small-12 columns text-center">
					<a href="#"><img src="img/phone-detail/similar-phone-1.png" alt=""/></a>
					<h4>Apple iPhone 5</h4>
					<a href="#" class="button">Shop Now</a>
				</div>	
				<div class="large-3 small-12 columns text-center">
					<a href="#"><img src="img/phone-detail/similar-phone-2.png" alt=""/></a>
					<h4>Samsung Galaxy S<sup>&reg;</sup> 3</h4>
					<a href="#" class="button">Shop Now</a>
				</div>	
				<div class="large-3 small-12 columns text-center">
					<a href="#"><img src="img/phone-detail/similar-phone-3.png" alt=""/></a>
					<h4>HTC Desire C</h4>
					<a href="#" class="button">Shop Now</a>
				</div>
				<div class="large-3 small-12 columns text-center">
					<a href="#"><img src="img/phone-detail/similar-phone-4.png" alt=""/></a>
					<h4>HTC One V</h4>
					<a href="#" class="button">Shop Now</a>
				</div>						
			</div>		
		</section>
		
		Plans
		<section id="phone-plans" class="three-callouts">
			<div class="row">
				<div class="large-8 small-12 columns">
					<h2>Our <span>Plans</span></h2>
				</div>
				<div class="large-4 small-12 hide-for-small columns">
					<a href="#" class="circle-arrow right">See All Plans</a>
				</div>
			</div>
			
			Mobile Version
			<a href="#" class="prev show-for-small">Prev</a>
			<a href="#" class="next show-for-small">Next</a>
			<div class="swiper-container show-for-small">
				<div class="swiper-wrapper text-center">
				First Slide
				<div class="swiper-slide"> 
				  <div id="plan-50" class="large-4 small-12 columns text-center">
		  			<div class="plan">
		  			  <h4><sup>$</sup>50<sub>/mo</sub></h4>
		  			  <p><span class="line1">1GB FULL-SPEED</span> <span class="line2">DATA, TALK, TEXT</span> <span class="line3">PLUS MUSIC</span></p>
		  			  <a class="button disabled secondary">Add to Cart</a>
		  			</div>
		  			<a href="#" class="circle-arrow">View Details</a>
		  		</div>
				</div>
				Second Slide
				<div class="swiper-slide">
				  <div id="plan-60" class="large-4 small-12 columns text-center">
		  		  <div class="plan">
		  			  <h4><sup>$</sup>60<sub>/mo</sub></h4>
		  			  <p><span class="line1">2.5GB FULL-SPEED</span> <span class="line2">DATA, TALK, TEXT</span> <span class="line3">PLUS MUSIC</span></p>
		  			  <a class="button disabled secondary">Add to Cart</a>
		  			</div>
		  			<a href="#" class="circle-arrow">View Details</a>
		  		</div>
				</div>
				Third Slide
				<div class="swiper-slide"> 
				  <div id="plan-70" class="large-4 small-12 columns text-center">
		  			<div class="plan">
		  			  <h4><sup>$</sup>70<sub>/mo</sub></h4>
		  			  <p><span class="line1">5GB FULL-SPEED</span> <span class="line2">DATA, TALK, TEXT</span> <span class="line3">PLUS MUSIC</span></p>
		  			  <a class="button disabled secondary">Add to Cart</a>
		  			</div>
		  			<a href="#" class="circle-arrow">View Details</a>
		  		</div>
				</div>
				</div>	
			</div>	
			<div class="row show-for-small">
				<div class="small-12 columns text-center">
					<p><a href="#" class="circle-arrow">See All Plans</a></p>
				</div>
			</div>
		  Desktop Version
			<div class="row hide-for-small">	
				<div id="plan-50" class="large-4 small-12 columns text-center">
					<div class="plan">
					  <h4><sup>$</sup>50<sub>/mo</sub></h4>
					  <p><span class="line1">2.5GB FULL-SPEED</span> <span class="line2">DATA, TALK, TEXT</span> <span class="line3">PLUS MUSIC</span></p>
					  <a class="button disabled secondary">Add to Cart</a>
					</div>
					<a href="#" class="circle-arrow">View Details</a>
				</div>	
				<div id="plan-60" class="large-4 small-12 columns text-center">
				  <div class="plan">
					  <h4><sup>$</sup>60<sub>/mo</sub></h4>
					  <p><span class="line1">5.0GB FULL-SPEED</span> <span class="line2">DATA, TALK, TEXT</span> <span class="line3">PLUS MUSIC</span></p>
					  <a class="button disabled secondary">Add to Cart</a>
					</div>
					<a href="#" class="circle-arrow">View Details</a>
				</div>	
				<div id="plan-70" class="large-4 small-12 columns text-center">
					<div class="plan">
					  <h4><sup>$</sup>70<sub>/mo</sub></h4>
					  <p><span class="line1">10GB FULL-SPEED</span> <span class="line2">DATA, TALK, TEXT</span> <span class="line3">PLUS MUSIC</span></p>
					  <a class="button disabled secondary">Add to Cart</a>
					</div>
					<a href="#" class="circle-arrow">View Details</a>
				</div>
			</div>		
		</section> -->
	</c:if>
</dsp:page>