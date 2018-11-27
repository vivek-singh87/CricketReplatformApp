<dsp:page>

<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>

<dsp:include page="/common/head.jsp">
	<dsp:param name="seokey" value="billingOrderSummaryKey" />
</dsp:include>
<dsp:importbean bean="/com/cricket/common/util/formhandler/RetrieveBillingOrderDetailsFormHandler"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayShoppingCartDroplet"/>
<dsp:importbean bean="/com/cricket/common/util/droplet/GetBillingOrderDetailsDroplet"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>

<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="billingOrderNumber" bean="RetrieveBillingOrderDetailsFormHandler.responseVO.billingOrderNumber"/>
<dsp:getvalueof var="trackingNumber" bean="RetrieveBillingOrderDetailsFormHandler.responseVO.trackingNumber"/>
<dsp:getvalueof var="submittedDate" bean="RetrieveBillingOrderDetailsFormHandler.responseVO.submittedDate" />
<dsp:getvalueof var="orderStatus" bean="RetrieveBillingOrderDetailsFormHandler.responseVO.orderStatus"/>
<c:set var="counter" value="0" /> 
 
	<dsp:droplet name="GetBillingOrderDetailsDroplet">
		<dsp:param name="billingOrderId" value="${billingOrderNumber}"/>
		<dsp:oparam name="output">	
			<dsp:getvalueof var="shippingmethod"  param="shippingmethod"/>
			<dsp:getvalueof var="order"  param="order"/>			
		</dsp:oparam>
	</dsp:droplet>
	<c:if test="${order ne null}">
		<dsp:droplet name="DisplayShoppingCartDroplet">
			<dsp:param name="order" value="${order}"/>
			<dsp:oparam name="output">	
				<dsp:getvalueof var="accessories"  param="accessories"/>	
				<dsp:getvalueof var="packages"  param="packages"/>
				<dsp:getvalueof var="upgradePhone"  param="upgradePhone"/>
				<dsp:getvalueof var="changePlan"  param="changePlan"/>
				<dsp:getvalueof var="changeAddons"  param="changeAddons"/>
			</dsp:oparam>
		</dsp:droplet>
	</c:if>	

<body>
<div id="outer-wrap">
<div id="inner-wrap">
  <dsp:include page="/common/header.jsp"/>
 <!--// END HEADER AREA //-->

	<div id="constructor" class="order-status">
	
	<!-- KM - New Responsive Markup -->
	  <section id="statusform">
	
	    <div id="statusform-header" class="row">
	      <div class="columns large-12 small-12">
	        <h1>Check <span>Order Status</span></h1>
	        <p class="order-number">Order Confirmation Number: {<c:if test="${billingOrderNumber ne null}">${billingOrderNumber}</c:if>}</p>
	      </div>
	    </div><!-- /#statusform-header-->
	    
			    <div class="row">
			      <div id="statusform-content" class="columns large-12 small-12">
				
					<dsp:droplet name="ForEach">
						<dsp:param name="array" value="${packages}"/>
						<dsp:param name="elementName" value="package"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="package" param="package"/>
								<dsp:getvalueof var="phone" value="${package.phonePriceInfo}"/>
								<dsp:getvalueof var="plan" value="${package.planPriceInfo}"/>
								<dsp:getvalueof var="addOnList" value="${package.addOnPriceInfo}"/>
									
										<div class="row">
									          <div class="columns large-12 small-12">
									            <h2>{<crs:outMessage key="cricket_checkout_package"/> <dsp:valueof param="count"/>}</h2>
									          </div>
								        </div><!-- /.row-->
																			
										<div class="row">
									          <div class="columns large-6 small-12">
										            <table>
										              <tr>
										                <td><label>Phone:</label></td>
											                <td><c:if test="${!empty phone}">
																	<dsp:droplet name="ProductLookup">
																		<dsp:param name="filterBySite" value="false"/>
																		<dsp:param name="filterByCatalog" value="false"/>
																		<dsp:param name="id" value="${package.phoneCommerceItem.productId}"/>
																		<dsp:param name="elementName" value="Product"/>
																		<dsp:oparam name="output">
																			<dsp:valueof param="Product.displayName"/>
																		</dsp:oparam>
																	</dsp:droplet>
																</c:if>
															</td>
										              </tr>
										              <tr>
										                <td><label>Plan:</label></td>
										                <td>
										                	<c:if test="${!empty plan}">
																<dsp:droplet name="ProductLookup">
																	<dsp:param name="id" value="${package.planCommerceItem.productId}"/>
																	<dsp:param name="elementName" value="Plan"/>
																		<dsp:oparam name="output">
																			<dsp:valueof param='Plan.displayName'/>
																		</dsp:oparam>
																</dsp:droplet>
															</c:if>
														</td>
										              </tr>
										              <tr>
										                <td><label>Features:</label></td>
										                <td>
										                	<c:if test="${!empty addOnList }" >
																<c:forEach var="addOns" items="${addOnList}" varStatus="countValue">		
																	<dsp:droplet name="ProductLookup">
																		<dsp:param name="id" value="${package.addOnsCommerceItems[countValue.count-1].productId}"/>
																		<dsp:param name="elementName" value="Features"/>
																			<dsp:oparam name="output">
																				<dsp:valueof param="Features.displayName"/>
																			</dsp:oparam>
																	</dsp:droplet>
																</c:forEach>
															</c:if>
														</td>
										              </tr>
										              <tr>
										                <td><label>Accessories:</label></td>
										                <td>
										                	<c:if test="${!empty accessories}" >
																<c:forEach var="accessory" items="${accessories}">
																	<dsp:droplet name="ProductLookup">
																		<dsp:param name="id" value="${accessory.auxiliaryData.productId}"/>
																		<dsp:param name="elementName" value="Accessories"/>
																			<dsp:oparam name="output">	
																				<dsp:valueof param="Accessories.displayName"/>
																			</dsp:oparam>
																	</dsp:droplet>
																</c:forEach>
															</c:if>	
														</td>
										              </tr>
										            </table>
									           </div>
									           <div class="columns large-6 small-12">           	
									            <table>
									              <tr>
									                <td><label>Shipping Method:</label></td>
									                <td><c:if test="${!empty shippingmethod}">${shippingmethod}</c:if></td>
									              </tr>
									              <tr>
									                <td><label>Tracking Number:</label></td>
									                <td>{<c:if test="${!empty trackingNumber}">${trackingNumber}</c:if>}</td>
									              </tr>
									            </table>
									          </div>
								        </div><!-- /.row-->
							</dsp:oparam>
						</dsp:droplet>
					
					</div><!-- /#statusform-content .columns -->
				 </div><!-- /.row-->
								
				<div class="row">
			      <div id="order-activity" class="columns large-12 small-12">
			        
			        <div class="row">
			          <div class="columns large-12 small-12">
			            <h2>Activity</h2>
			          </div>
			        </div>
			
			        <div class="row">
			          <div class="columns large-6 small-12">
			            <table>
			              <tr>
			                <td><label>Date:</label></td>
			                <td><c:if test="${!empty submittedDate}">
									<fmt:formatDate pattern="MM/dd/yyyy" value="${submittedDate}" />
								</c:if>
							</td>
			              </tr>
			            </table>
			          </div>
			          <div class="columns large-6 small-12">
			            <table>
			              <tr>
			                <td><label>Activity:</label></td>
			                <td><c:if test="${!empty orderStatus}">${orderStatus}</c:if></td>
			              </tr>
			            </table>
			          </div>
			        </div><!-- /.row-->
			      </div><!-- /#order-activity -->
			    </div><!-- /.row-->
	
  		</section>
	  	<!-- KM - end of new responsive markup-->
	</div> <!-- /#constructor-->    
	<!--// START FOOTER AREA //-->
	<dsp:droplet name="/atg/dynamo/droplet/Cache">
		<dsp:param value="cricketFooter" name="key"/>
		<dsp:oparam name="output">
			<dsp:include page="/common/footer.jsp"/>
		</dsp:oparam>
	</dsp:droplet>
<script src="${contextPath}/js/foundation/foundation.min.js"></script> 
<script>
$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips reveal');
</script>

<!-- Client Side Validation -->
<script src="${contextPath}/js/vendor/jquery.validate.min.js"></script>
	
<!-- SWIPER Library; Used for Phones and Plans on small screens -->
<script src="${contextPath}/lib/swiper/idangerous.swiper-2.0.min.js"></script>

<!-- Cricket specific JS -->
<script src="${contextPath}/js/cricket.min.js"></script>

<!--  Google javascripts for mobile device -->
<script src="${contextPath}/js/customcricketstore.js" type="text/javascript" charset="utf-8"></script>        
	
</div> <!--/#inner-wrap-->
</div> <!--/#outer-wrap-->
</body>
</dsp:page>