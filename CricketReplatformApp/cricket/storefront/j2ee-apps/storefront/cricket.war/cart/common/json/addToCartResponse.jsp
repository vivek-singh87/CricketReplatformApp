<dsp:page>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/atg/dynamo/droplet/ErrorMessageForEach"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<json:object>
<dsp:droplet name="Switch">
<dsp:param bean="CartModifierFormHandler.formError" name="value"/>
<dsp:oparam name="true">
<json:property name="success">
       false
   </json:property>
   <json:property name="errorExists">
       true
   </json:property>
    <json:property name="isModalError">
       <dsp:valueof bean="CartModifierFormHandler.modalError"/>
   </json:property>
   <json:property name="reloadDrawerCartDiv">
       <dsp:valueof bean="CartModifierFormHandler.reloadDrawerCartDiv"/>
   </json:property>
   <json:property name="redirectURL">
       <dsp:valueof bean="CartModifierFormHandler.redirectURL"/>
   </json:property>
   	<json:array name="formExceptions">
		<dsp:droplet name="ErrorMessageForEach">
			<dsp:param name="exceptions" bean="CartModifierFormHandler.formExceptions" />
			<dsp:oparam name="output">
			 <json:object>
				<json:property name="errorMessage">
					<dsp:valueof param="message"/>
				</json:property>
			 </json:object>
			</dsp:oparam>
		</dsp:droplet>
	</json:array>
</dsp:oparam>
<dsp:oparam name="false">
   <json:property name="success">
       true
   </json:property> 
    <json:property name="errorExists">
       false
   </json:property>  
    <json:property name="redirectURL">
       <dsp:valueof bean="CartModifierFormHandler.redirectURL"/>
   </json:property>
   <json:property name="isCartOpen">
       <dsp:valueof bean="CartModifierFormHandler.cartOpen"/>
   </json:property>
</dsp:oparam>
</dsp:droplet>
</json:object>
</dsp:page>
