<dsp:page>
	<div id="available-phones" class="row">
            <div class="columns large-12 small-12">
            	<dsp:droplet name="/atg/dynamo/droplet/ForEach">
					<dsp:param name="array" param="product.compatiblePlans"/>
					<dsp:param name="elementName" value="plan"/>
					<dsp:getvalueof var="size" param="size"></dsp:getvalueof>
					<!--START This condition is used if  addons having the compatible plan or not  -->
						<dsp:oparam name="outputStart">
							<c:choose>
								<c:when test="${size eq 0}">
								</c:when>
								<c:otherwise>	
									<h4><crs:outMessage key="cricket_addondetails_RecommemndedPlans"/></h4>
									<p>
								</c:otherwise>	
							</c:choose>
						</dsp:oparam>		
						<dsp:oparam name="output">
							<!--This is the included page in plan addon details.jsp which displays plans those are compatible plan to the selected addon. -->	               
							<dsp:getvalueof var="count" param="count"></dsp:getvalueof>
							<dsp:getvalueof var="planName" param="plan.displayName"></dsp:getvalueof>
							<c:if test='${fn:containsIgnoreCase(planName, " Plan")}'>
								<c:choose>
									<c:when test="${count eq size}">
										${fn:replace(planName, ' Plan', '')}&nbsp;PLANS
									</c:when>
									<c:otherwise>
									   ${fn:replace(planName, ' Plan', '')},&nbsp;
									</c:otherwise>
								</c:choose>
							</c:if>
						</dsp:oparam>
						<dsp:oparam name="outputEnd">
							</p>						
						</dsp:oparam>
							<!--END This condition is used if  addons having the compatible plan or not  -->
				</dsp:droplet>
            </div>
      </div>
</dsp:page>