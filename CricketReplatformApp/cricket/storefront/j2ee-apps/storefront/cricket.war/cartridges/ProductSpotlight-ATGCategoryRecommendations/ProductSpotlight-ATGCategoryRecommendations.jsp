
<dsp:page>  
  
  <dsp:importbean bean="/atg/endeca/assembler/cartridge/CrossCartridgeItemsLookup"/>
  <dsp:importbean bean="/atg/endeca/assembler/cartridge/CrossCartridgeItemsLookupDroplet"/>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>

</dsp:page>
