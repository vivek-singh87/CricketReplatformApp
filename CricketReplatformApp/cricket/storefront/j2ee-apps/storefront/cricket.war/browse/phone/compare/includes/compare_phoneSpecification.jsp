<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/com/cricket/product/comparision/droplet/FeatureDisplayDroplet"/>
<dsp:importbean bean="/com/cricket/product/comparision/droplet/GetCompareSpecsListDroplet"/>
<dsp:importbean bean="/com/cricket/product/comparision/droplet/SpecificationDroplet"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:importbean bean="/atg/store/droplet/StoreText"/>

<!-- Starting ForEach Droplet1  -->
<%-- <dsp:droplet name="ForEach">
	 <dsp:param name="array" param="productList"/>
	 <dsp:oparam name="output"> --%>
	 <!-- Starting FeatureDisplayDroplet  -->
		<%-- <dsp:droplet name="FeatureDisplayDroplet">
		 <dsp:param name="product" param="element.product"/>
		 <dsp:param name="size" param="size"/>
		 <dsp:param name="count" param="count"/>
		 <dsp:oparam name="SpecificationsOutput"> --%>
		 		<dsp:droplet name="GetCompareSpecsListDroplet">
				 <dsp:param name="productList" param="productList"/>
				 <dsp:oparam name="output">
			 		<dsp:droplet name="ForEach">
					<dsp:param name="array" param="compareSpecList"/>
					<dsp:oparam name="outputStart">
						<tr>
							<th class="row-heading" colspan="5"><h3>Features</h3></th>
						</tr>
					</dsp:oparam>
					<dsp:oparam name="output">	
						<dsp:getvalueof var="SpecName" param="element"/>	 		
			 			<c:choose>
			 			<c:when test="${rowCounter.last eq 'fasle' }">
			 				<tr>	
		 				</c:when>
		 				<c:otherwise>
		 					<tr class="row-last">
		 				</c:otherwise>
			 			</c:choose>
			 					<th><dsp:valueof value="${SpecName}" valueishtml="true"></dsp:valueof></th>
			 					
			 					<!-- Starting ForEach Droplet2  -->
			 					<dsp:droplet name="ForEach">
								<dsp:param name="array" param="productList" />
								<dsp:oparam name="output">
								<!-- Starting SpecificationDroplet -->
									<dsp:droplet name="SpecificationDroplet">
									<dsp:param name="product" param="element.product"/>
									<dsp:param name="specification" value="${SpecName}"/>
									<dsp:oparam name="Output">
										<dsp:getvalueof var="SpecValue" param="SpecValue"/>
										<td>
											${SpecValue}
										</td>
									</dsp:oparam>
									</dsp:droplet>
									 <!-- Ending SpecificationDroplet  -->
								</dsp:oparam>
								</dsp:droplet>
								<!-- Ending ForEach Droplet2  --> 
							</tr>
					</dsp:oparam>
					</dsp:droplet>		
			</dsp:oparam>
		</dsp:droplet> 		
		<%--  </dsp:oparam>
		</dsp:droplet> --%>
		 <!-- Ending FeatureDisplayDroplet  -->
	 <%-- </dsp:oparam>
</dsp:droplet>	 --%>	
<!-- Ending ForEach Droplet2  -->
</dsp:page>