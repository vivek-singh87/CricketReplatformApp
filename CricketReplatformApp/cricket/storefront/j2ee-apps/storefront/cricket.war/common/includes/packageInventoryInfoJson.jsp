<dsp:page>
<dsp:importbean bean="/com/cricket/browse/ThresholdMessageDroplet"/>
<json:object>
	<json:property name="outOfStock">
		<dsp:droplet name="ThresholdMessageDroplet">
			<dsp:param name="skuId" param="phoneSkuId"/>
			<dsp:param name="order" bean="/atg/commerce/ShoppingCart.current"/>
			<dsp:oparam name="output">
				<dsp:valueof param="isOutOfStock"/>
			</dsp:oparam>
		</dsp:droplet>
    </json:property>
</json:object>
</dsp:page>