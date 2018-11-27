<dsp:page>
<dsp:importbean bean="/com/cricket/common/util/droplet/GetLocationInfoFromZipCode"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<json:object>
	<json:array name="cityInfos">
		<dsp:droplet name="GetLocationInfoFromZipCode">
			<dsp:param name="zipCode" param="zipCode"/>
			<dsp:oparam name="output">
				<dsp:droplet name="ForEach">
					<dsp:param name="array" param="geoLocationList"/>
					<dsp:oparam name="output">
						<json:object>
							<json:property name="cityName">
								<dsp:valueof param="element.cityAliasMixedCase"/>
							</json:property>
							<json:property name="state">
								<dsp:valueof param="element.state"/>
							</json:property>
							<json:property name="country">
								<dsp:valueof param="element.country"/>
							</json:property>
							<json:property name="areaCode">
								<dsp:valueof param="element.areacode"/>
							</json:property>
							<json:property name="zipCode">
								<dsp:valueof param="element.zipCode"/>
							</json:property>
							<json:property name="latitude">
								<dsp:valueof param="element.latitude"/>
							</json:property>
							<json:property name="longitude">
								<dsp:valueof param="element.longitude"/>
							</json:property>
							<json:property name="timezone">
								<dsp:valueof param="element.timezone"/>
							</json:property>
						</json:object>
					</dsp:oparam>
				</dsp:droplet>
			</dsp:oparam>
			<dsp:oparam name="NoCoverage">		
				<json:object>
							<json:property name="NoCoverage">NoCoverage</json:property>
							<json:property name="cityName">
								<dsp:valueof param="CITYVO.city"/>
							</json:property>
							<json:property name="state">
								<dsp:valueof param="CITYVO.state"/>
							</json:property>
							<json:property name="country">
								<dsp:valueof param="CITYVO.country"/>
							</json:property>
							<json:property name="areaCode">
								<dsp:valueof param="CITYVO.areacode"/>
							</json:property>
							<json:property name="zipCode">
								<dsp:valueof param="CITYVO.postalCode"/>
							</json:property>
							<json:property name="latitude">
								<dsp:valueof param="CITYVO.latitude"/>
							</json:property>
							<json:property name="longitude">
								<dsp:valueof param="CITYVO.longitude"/>
							</json:property>
							<json:property name="timezone">
								<dsp:valueof param="CITYVO.timezone"/>
							</json:property>
				</json:object>		
			</dsp:oparam>
			<dsp:oparam name="empty">				
			</dsp:oparam>
		</dsp:droplet>
	</json:array>
</json:object>
</dsp:page>