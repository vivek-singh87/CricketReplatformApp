<%--
  Renders Promotional Content Slots
--%>
<dsp:page>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  
  <dsp:getvalueof var="content" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/> 
  <dsp:getvalueof var="items" value="${content.items}"/> 
  <c:if test="${not empty items}">  
  <%--<c:out value="${items}"/> --%>
    <%--<c:forEach items="${items}" var="item">  
      <dsp:getvalueof var="pageurl" idtype="java.lang.String" 
                      value="${item.template.url}"/>
      <dsp:include page="${pageurl}">
        <dsp:param name="promotionalContent" value="${item}"/>
      </dsp:include>
    </c:forEach>  --%>
  </c:if>
  
</dsp:page>
