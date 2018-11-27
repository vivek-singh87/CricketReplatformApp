<%-- 
  This page lays out the elements that make up the search results page.
    
  Required Parameters:
    contentItem
      The content item - results list type 
   
  Optional Parameters:

--%>
<dsp:page>
  <dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
  <dsp:importbean bean="/atg/multisite/Site"/>
  <dsp:importbean bean="/atg/search/droplet/GetClickThroughId"/>
  <dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
  
  <dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
      <%-- Render the search results.--%>
		<dsp:include page="${correspondingSearchPageUrl}">
		    <dsp:param name="contentItem" value="${contentItem}"/>
		    <dsp:param name="p" param="p"/>
		    <dsp:param name="viewAll" value="false"/>
		</dsp:include> 

</dsp:page>
<%-- @version $Id: //hosting-blueprint/B2CBlueprint/version/10.2/Storefront/j2ee/store.war/cartridges/ResultsList/ResultsList.jsp#4 $$Change: 796430 $--%>
