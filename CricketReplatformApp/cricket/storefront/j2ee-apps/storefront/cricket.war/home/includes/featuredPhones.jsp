<dsp:page>
<dsp:importbean bean="/atg/endeca/assembler/droplet/InvokeAssembler"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<!-- Phones -->
	<section id="section-devices" class="three-callouts">
		<div class="row">
			<div class="large-8 small-12 columns">
				<h2>Phones <span>You Want</span></h2>
			</div>
			<div class="large-4 small-12 hide-for-small columns">
				<dsp:droplet name="DimensionValueCacheDroplet">
					<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="allPhonesCategoryCacheEntry" param="dimensionValueCacheEntry" />
			      </dsp:oparam>
				</dsp:droplet>
				<a href="${contextpath}${allPhonesCategoryCacheEntry.url}" class="circle-arrow right">See All Phones</a>
			</div>
		</div>	
	<!--Phone lineup "swiper" for smaller screens -->			
		<a href="#" class="prev show-for-small">Prev</a>
		<a href="#" class="next show-for-small">Next</a>
		<div class="swiper-container show-for-small">
			<div class="swiper-wrapper">
				<dsp:droplet name="InvokeAssembler">
			    	<dsp:param name="contentCollection" value="/content/Shared/HomePageFeaturedPhones_mobile"/>
			    	<dsp:oparam name="output">
			      		<dsp:getvalueof var="promoBanner_mobile" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
			    	</dsp:oparam>
			  	</dsp:droplet>
			  	<c:if test="${not empty promoBanner_mobile}">
			    	<dsp:renderContentItem contentItem="${promoBanner_mobile}"/>
				</c:if>
			</div>
		</div>	
		<div class="row cta show-for-small">
			<div class="small-12 columns">
				<p><a href="${contextpath}${allPhonesCategoryCacheEntry.url}" class="circle-arrow">See All Phones</a></p>
			</div>
		</div>	
	<!--Phone lineup for Larger screens-->		
		 	<a href="#" class="prev carousel-prev hide-for-small">Prev</a>
             <a href="#" class="next carousel-next hide-for-small">Next</a>                                          
              <div id="homepage-phones" class="row hide-for-small">
					<div class="row hide-for-small swiper-wrapper">
						<dsp:droplet name="InvokeAssembler">
					    	<dsp:param name="contentCollection" value="/content/Shared/HomePageFeaturedPhones"/>
					    	<dsp:oparam name="output">
					      		<dsp:getvalueof var="promoBanner" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
					    	</dsp:oparam>
					  	</dsp:droplet>
					  	<c:if test="${not empty promoBanner}">
					    	<dsp:renderContentItem contentItem="${promoBanner}"/>
						</c:if>
					</div>
               </div>                                                                                       
		</section>
	</dsp:page>