<dsp:page>
	<dsp:importbean bean="/atg/store/sort/CricketStoreSortDroplet" />	
	<section id="section-results" class="${zipcodeClass}">	
		<!-- Results -->
		<div class="row">	
			<div class="large-12 small-12 columns large-centered small-centered">
			<dsp:droplet name="/atg/dynamo/droplet/IsEmpty">
			   <dsp:param name="value" param="productList"/>
			   <dsp:oparam name="false">
				 <dsp:droplet name="CricketStoreSortDroplet">
					<dsp:param name="array" param="productList"/>
			        <dsp:param name="howMany" param="howMany"/>
			        <dsp:param name="start" param="start"/>
			        <dsp:param name="sortSelection" param="sortSelection"/>
			        <dsp:oparam name="output">
			        	<dsp:include page="/browse/accessories/listing/includes/accessory_accessoryDisplay.jsp">
							<dsp:param name="product" param="element"/>
							<dsp:param name="size" param="size"/>
							<dsp:param name="count" param="count"/>
							<dsp:param name="throughSearch" value="${false}"/>
						</dsp:include>			        	
			        </dsp:oparam>
				 </dsp:droplet>
			   </dsp:oparam>
			   <dsp:oparam name="true">
				 <p class="no-results">No Accessories found for your selection.</p>
			   </dsp:oparam>
			</dsp:droplet>
				
			</div>
		</div>
		
		<%-- <div class="row show-for-small">
			<div class="columns small-12 text-center three-callouts">    
				<a class="prev show-for-small" href="#">Prev</a>
				<a class="next show-for-small" href="#">Next</a>
				<div class="swiper-container show-for-small">	
        			<div class="swiper-wrapper">
        				<dsp:droplet name="CricketStoreSortDroplet">
							<dsp:param name="array" param="productList"/>
					        <dsp:param name="howMany" param="howMany"/>
					        <dsp:param name="start" param="start"/>
					        <dsp:param name="sortSelection" param="sortSelection"/>
					        <dsp:oparam name="output">
					        	<dsp:include page="/browse/accessories/listing/includes/accessory_accessoryDisplay_mobile.jsp">
									<dsp:param name="product" param="element"/>
									<dsp:param name="size" param="size"/>
									<dsp:param name="count" param="count"/>
									<dsp:param name="throughSearch" value="${false}"/>
								</dsp:include>			        	
					        </dsp:oparam>
						 </dsp:droplet>
        			</div>
        		</div>	
			</div>
		</div> --%>
	</section>
</dsp:page>