<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:getvalueof var="contextpath" param="contextpath"/>
<!-- Starting ForEach Droplet  -->
	<dsp:droplet name="ForEach">
		<dsp:param name="array" param="productList" />		
			<dsp:oparam name="output">
				<dsp:getvalueof var="productId" param="element.product.id"/>
				<dsp:getvalueof var="skuId" param="element.product.childSkus[0].id"/>
				<th><a href="${contextpath}/browse/phone/phone_details.jsp?productId=${productId}&skuId=${skuId}">
					<dsp:getvalueof var="productName" param="element.product.displayName"/>
						<dsp:droplet name="/com/cricket/browse/droplet/DisplayNameDroplet">
							<dsp:param name="displayName" value="${productName}" />					
							<dsp:oparam name="output">
								<dsp:getvalueof var="displayName" param="displayName"/>
								<dsp:getvalueof var="hasSpecialSymbol" param="hasSpecialSymbol"/>
								<dsp:getvalueof var="firstString" param="firstString"/>
								<dsp:getvalueof var="secondString" param="secondString"/>
								<dsp:getvalueof var="specialSymbol" param="specialSymbol"/>
								<c:choose>
									<c:when test="${hasSpecialSymbol eq true}">	
										${firstString}<sup>${specialSymbol}</sup>${secondString}							
									</c:when>	
									<c:otherwise>
										${displayName}
									</c:otherwise>
								</c:choose>
							</dsp:oparam>
						</dsp:droplet>
				</a></th>
			</dsp:oparam>		
	</dsp:droplet>
	</dsp:page>