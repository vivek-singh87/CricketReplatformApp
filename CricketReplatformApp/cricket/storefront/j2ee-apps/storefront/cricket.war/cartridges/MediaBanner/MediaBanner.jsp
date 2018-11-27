<dsp:page>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
  
  <dsp:getvalueof var="content" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/> 
  <dsp:getvalueof var="items" value="${content.items}"/> 
  <c:forEach items="${items}" var="item">
  	<c:out value="${item.imageURL}"/>
  </c:forEach>
  
  
</dsp:page>