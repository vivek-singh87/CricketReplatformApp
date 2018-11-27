<dsp:page>
<dsp:importbean bean="/com/cricket/browse/ThresholdMessageDroplet"/>
<json:object>
	<json:property name="outOfStock">
		<dsp:droplet name="ThresholdMessageDroplet">
		   <dsp:param name="quantity" param="quantity"/>
		   <dsp:param name="skuId" param="accessorySkuId"/>
		   <dsp:param name="mode" value="accQuantityUpdate"/>
		   <dsp:oparam name="output">
				<dsp:valueof param="isOutOfStock"/>
		   </dsp:oparam>
	    </dsp:droplet>
    </json:property>
</json:object>
</dsp:page>