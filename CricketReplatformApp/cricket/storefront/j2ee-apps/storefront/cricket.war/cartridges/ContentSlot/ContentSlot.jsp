<dsp:page>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
  
  <dsp:getvalueof var="content" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/> 
  <dsp:getvalueof var="items" value="${content.items}"/>
  <endeca:includeSlot contentItem="${content}">
  <c:forEach var="element" items="${content.contents}">
      <dsp:renderContentItem contentItem="${element}"/>
  </c:forEach>
  </endeca:includeSlot>
  
</dsp:page>