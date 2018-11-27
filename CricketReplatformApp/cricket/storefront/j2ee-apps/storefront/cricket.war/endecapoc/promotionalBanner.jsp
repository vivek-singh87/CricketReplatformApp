
<dsp:page>
<dsp:importbean bean="/atg/endeca/assembler/droplet/InvokeAssembler"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<dsp:droplet name="InvokeAssembler">
    <dsp:param name="contentCollection" value="/content/Shared/POC/PromotionalBanner"/>
    <dsp:oparam name="output">
      <dsp:getvalueof var="promoContent" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
    </dsp:oparam>
  </dsp:droplet>
  <c:if test="${not empty promoContent}">
    <dsp:renderContentItem contentItem="${promoContent}"/>
  </c:if>
  
  
</body>
</html>
</dsp:page>