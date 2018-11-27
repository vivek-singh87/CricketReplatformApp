<dsp:page>
	<dsp:importbean bean="/atg/store/sort/CricketStoreSortDroplet" />
	<section id="section-results" class="${zipcodeClass}">	
		<!-- Results -->
		<div class="row">	
			<div class="large-12 small-12 columns large-centered small-centered">
				<dsp:droplet name="CricketStoreSortDroplet">
					<dsp:param name="array" param="productList"/>
			        <dsp:param name="howMany" param="howMany"/>
			        <dsp:param name="start" param="start"/>
			        <dsp:param name="sortSelection" param="sortSelection"/>
			        <dsp:oparam name="output">
			        	<dsp:include page="/browse/phone/listing/includes/phone_phoneDisplay.jsp">
							<dsp:param name="product" param="element"/>
							<dsp:param name="size" param="size"/>
							<dsp:param name="count" param="count"/>
							<dsp:param name="throughSearch" value="${false}"/>
						</dsp:include>
			        </dsp:oparam>
			        <dsp:oparam name="empty">
			        	<p class="no-results">No products currently match the filters you have chosen. Please clear some filters to broaden your search.</p>
			        </dsp:oparam>
				</dsp:droplet>
			</div>
		</div>
	</section>
</dsp:page>