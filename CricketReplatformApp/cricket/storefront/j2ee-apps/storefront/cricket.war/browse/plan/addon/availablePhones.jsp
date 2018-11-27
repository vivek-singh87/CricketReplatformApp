<dsp:page>
	<div id="included" class="row">     
          <!-- change to available-plans -->            
            <div class="columns large-12 small-12">
              <h4><crs:outMessage key="cricket_addondetails_RecommemndedPhones"/></h4>
              <ul>
              <!--This is the included page in plan addon details.jsp which displays phones those are compatible to the selected addon. -->
			  <dsp:getvalueof var="compatiblePhones"  param="product.compatiblePhones"/>
             	<dsp:droplet name="/atg/dynamo/droplet/ForEach">
					<dsp:param name="array" param="product.compatiblePhones"/>
						<dsp:param name="elementName" value="phone"/>					
						<dsp:oparam name="output">
							<dsp:getvalueof var="size" param="size"></dsp:getvalueof>
							<dsp:getvalueof var="count" param="count"></dsp:getvalueof>						
							<c:choose>
								<c:when test="${size eq 1}">
									<li class="last"><dsp:valueof param="phone.displayName"/></li>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${count eq 1}">
										   <li><dsp:valueof param="phone.displayName"/></li>
										</c:when>
										<c:when test="${count eq size}">
										   <li class="last"><dsp:valueof param="phone.displayName"/></li>
										</c:when>
										<c:otherwise>
										   <li><dsp:valueof param="phone.displayName"/></li>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>		
						</dsp:oparam>
					</dsp:droplet>              
              </ul>
              <%-- <p><dsp:valueof param="product.availabilityMessage"/></p> --%>
            </div>
      </div>       
</dsp:page>