<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>

		<table id="endecaTable" width="100%">
			<th colspan="2">
				Endeca Data
			</th>
	
	   		<tr>
	
	 		<%-- Render the header --%>
		      <c:if test="${not empty contentItem.HeaderContent}">
		        <c:forEach var="element" items="${contentItem.HeaderContent}">
		          <dsp:renderContentItem contentItem="${element}"/>
		        </c:forEach>
		      </c:if>
		       <%-- Render the left content --%>
		      <td height="auto" width="20%" style="border: 1px solid #7a7; vertical-align: top;">
		      <c:forEach var="element" items="${contentItem.SecondaryContent}">
		        	<dsp:renderContentItem contentItem="${element}"/>
		      </c:forEach>
		      </td>
			  <%-- Render the main content --%>
			  
			  
			<td height="auto" width="80%" style="border: 1px solid #7a7; vertical-align: top;">
				<div id="mainEndecaContent">
					<!-- identifierForEndecaMainContentReload -->
						<c:forEach var="element" items="${contentItem.MainContent}">
							<dsp:renderContentItem contentItem="${element}"/>
						</c:forEach>
					<!-- identifierForEndecaMainContentReload -->
				</div>
			</td>
		      
		      
			 
		     </tr>
		</table>
	