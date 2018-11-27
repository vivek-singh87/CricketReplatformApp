<dsp:page>4444444
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  <dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
  
  <dsp:getvalueof var="content" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/> 
  <dsp:getvalueof var="items" value="${content.items}"/> 
  <c:out value="${items}"/>----<c:out value="${content}"/>
  <c:forEach items="${items}" var="item">
  	<c:out value="${item.imageURL}"/>dddddd
  </c:forEach>
  
  
</dsp:page>