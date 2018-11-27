<dsp:page>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
	<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
	<!-- Desktop Zip Code Message -->
	<dsp:droplet name="Switch">
		<dsp:param name="value" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
		<dsp:oparam name="false">
			<div class="row hide-for-small">
				<div class="large-12 small-12 columns no-padding">
					<div data-alert class="alert-box">
						<crs:outMessage key="cricket_phonedetails_pleaseEnterzipcode"/>
						<a href="#" class="circle-arrow"><crs:outMessage key="cricket_phonedetails_EnterZipcode"/></a>
					</div>
			    </div>
			</div>
			<dsp:getvalueof var="zipCodeKnown" value="false" scope="request"/>
			<dsp:getvalueof var="zipcodeClass" value="zipcode phone-results" scope="request"/>
			<dsp:getvalueof var="addToCartClass" value="grey-add-cart button disabled secondary" scope="request"/>
		</dsp:oparam>
		<dsp:oparam name="true">
			<dsp:getvalueof var="zipCodeKnown" value="true" scope="request"/>
			<dsp:getvalueof var="zipcodeClass" value="zipcode phone-results" scope="request"/>
			<dsp:getvalueof var="addToCartClass" value="green-add-cart" scope="request"/>
		</dsp:oparam>
	</dsp:droplet>
</dsp:page>			