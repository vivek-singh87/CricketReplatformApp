<dsp:page>
	<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
	<dsp:getvalueof var="userEnteredZipCode" bean="CitySessionInfoObject.cityVO.manulaEntry" />
	<c:if test="${userEnteredZipCode eq false}">
		<div class="row hide-for-small">
			<div class="large-12 small-12 columns no-padding">
				<div data-alert class="alert-box">
					Please enter your zip code to continue shopping. 
					<a href="#" class="circle-arrow">Enter Zip code</a> 
				</div>
			</div>
		</div>
	</c:if>
</dsp:page>