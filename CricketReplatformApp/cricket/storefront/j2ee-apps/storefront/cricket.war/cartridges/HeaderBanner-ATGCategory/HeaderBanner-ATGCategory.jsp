
<dsp:page>

	<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
	<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/> 

	<%-- We only want to render the cartridge if a background image exists --%>
	<c:if test="${not empty contentItem.backgroundBannerURL}">

		<c:set var="style" value="background:url(${contentItem.backgroundBannerURL}) no-repeat scroll left bottom transparent; margin-bottom:-40px;"/>
	   	<div>
	   		<c:if test="${not empty contentItem.promotionalContentId}">
				<dsp:droplet name="/atg/commerce/promotion/PromotionalContentLookup">      
					<dsp:param name="id" value="${contentItem.promotionalContentId}"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="templateUrl" param="element.template.url" />
						<c:if test="${not empty templateUrl}">
	                    	<dsp:getvalueof var="pageurl" vartype="java.lang.String" param="element.template.url">
		                    	<dsp:include page="${pageurl}">
			                        <dsp:param name="promotionalContent" param="element" />
			                        <dsp:param name="imageHeight" value="134" />
			                        <dsp:param name="imageWidth" value="752" />
		                      	</dsp:include>
	                   		</dsp:getvalueof>
						</c:if>
					</dsp:oparam>
				</dsp:droplet>
			</c:if>
			<dsp:getvalueof var="currentCategoryId" bean="/atg/endeca/assembler/cartridge/StoreCartridgeTools.currentCategoryId"/>
	        <h2 class="title">
	          <c:out value="${contentItem.headerTitle}" /> 
	        </h2>
	   	</div>
	</c:if>
</dsp:page>