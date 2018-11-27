<dsp:page>
	<dsp:importbean bean="/com/cricket/browse/droplet/CheckMaxmindDBForIPAddress"/>
	<json:object>
		<dsp:droplet name="CheckMaxmindDBForIPAddress">
			<dsp:param name="ipAdress" param="ipAddress"/>
			<dsp:oparam name="output">
				<json:property name="present">
					true
				</json:property>
				<json:property name="city">
					<dsp:valueof param="location.city"/>
				</json:property>
				<json:property name="region">
					<dsp:valueof param="location.region"/>
				</json:property>
				<json:property name="country">
					<dsp:valueof param="location.country"/>
				</json:property>
				<json:property name="latitude">
					<dsp:valueof param="location.latitude"/>
				</json:property>
				<json:property name="longitude">
					<dsp:valueof param="location.longitude"/>
				</json:property>
				<json:property name="postalCode">
					<dsp:valueof param="location.postalCode"/>
				</json:property>
			</dsp:oparam>
			<dsp:oparam name="error">
				<json:property name="present">
					false
				</json:property>
			</dsp:oparam>
		</dsp:droplet>
	</json:object>
</dsp:page>