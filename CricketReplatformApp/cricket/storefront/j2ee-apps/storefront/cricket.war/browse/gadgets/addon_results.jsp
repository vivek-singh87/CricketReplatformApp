<dsp:page>
 
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/browse/PrepareAddonVoMap" />	 
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach" />
	<dsp:importbean bean="/com/cricket/browse/PrepareAddonVoMap" />
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	 

<dsp:getvalueof var="finalMap" param="finalMap"/>
<dsp:getvalueof var="isDynamicAddOn" param="isDynamicAddOn"/>
 <div id="accordion-content" class="row">
			  <div class="row hide-for-small">
			    <div class="large-12 small-12 columns">
			      <a href="#" class="expand-all text-right">Expand All</a>
			    </div>
			  </div>
			  
			  <div class="row">
			    <div class="large-12 small-12 columns">
			      <div class="section-container accordion" data-section="accordion">
			 		  <!--This Foreach droplet is used for displaying all addons-->
				      <c:forEach items="${finalMap}" var="entry" varStatus="entrycount">
				      	<c:choose>
				      		<c:when test="${entrycount.count == 1}">
				      			<section class="active">
				      		</c:when>
				      		<c:otherwise>
				      			<section>
				      		</c:otherwise>
				      	</c:choose>
				          <p class="title" data-section-title><a href="#">${entry.key}</a></p>
				          <div class="content" data-section-content>
				           <!-- Desktop Content-->
				            <div class="hide-for-small">
				               <dsp:param name="productList" value="${entry.value}"/>
				               <!--This droplet is called to display each addon product-->
				               	<dsp:droplet name="ForEach">
				               		<dsp:param name="array" param="productList"/>
				               		<dsp:oparam name="output">
				               			<dsp:include page="/browse/plan/addon/listing/addon_addonDisplay.jsp">
											<dsp:param name="product" param="element"/>
											<dsp:param name="size" param="size"/>
											<dsp:param name="count" param="count"/>
											<dsp:param name="isDynamicAddOn" value="${isDynamicAddOn}"/>
										</dsp:include>
				               		</dsp:oparam>
				               	</dsp:droplet>
				            </div>
				            
				            <!-- Mobile Swiper Content -->
				            <div class="columns small-12 text-center three-callouts show-for-small">   
	                					<!--This droplet is called to display each addon product for mobile-->
				               			<dsp:droplet name="ForEach">
				               				<dsp:param name="array" param="productList"/>
				               				<dsp:oparam name="outputStart">
				               					 <dsp:getvalueof var="addOnListSize" param="size"></dsp:getvalueof>
				               					 <c:if test="${addOnListSize > 1}">
					              			 		 <a href="#" class="prev show-for-small">Prev</a>
					              			  		 <a href="#" class="next show-for-small">Next</a>
					              			  	</c:if>
					              				<div class="swiper-container show-for-small"> 
					                				<div class="swiper-wrapper">
				               				</dsp:oparam>
				               				<dsp:oparam name="output">
					               				<dsp:include page="/browse/plan/addon/listing/mobile_addon_addonDisplay.jsp">
													<dsp:param name="product" param="element"/>
													<dsp:param name="size" param="size"/>
													<dsp:param name="count" param="count"/>
													<dsp:param name="isDynamicAddOn" value="${isDynamicAddOn}"/>
												</dsp:include>
				               				</dsp:oparam>
				               				<dsp:oparam name="outputEnd">
	                							</div>
	                							</div>
				               				</dsp:oparam>
				               			</dsp:droplet>
	                		</div>
				          </div>
				        </section>
					  </c:forEach>
			      </div>
			    </div>
			  </div>
			</div>
			
			</dsp:page>