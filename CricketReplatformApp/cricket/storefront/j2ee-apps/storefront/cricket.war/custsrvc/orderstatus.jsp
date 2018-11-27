<dsp:page>

<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:include page="/common/head.jsp"/>
<dsp:importbean bean="/com/cricket/common/util/formhandler/CustSrvcOrderDetailsFormHandler"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayShoppingCartDroplet"/>
<dsp:importbean bean="/com/cricket/common/util/droplet/GetBillingOrderDetailsDroplet"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>

<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="billingOrderNumber" bean="CustSrvcOrderDetailsFormHandler.responseVO.billingOrderNumber"/>
<dsp:getvalueof var="trackingNumber" bean="CustSrvcOrderDetailsFormHandler.responseVO.trackingNumber"/>
<dsp:getvalueof var="submittedDate" bean="CustSrvcOrderDetailsFormHandler.responseVO.submittedDate" />
<dsp:getvalueof var="orderStatus" bean="CustSrvcOrderDetailsFormHandler.responseVO.orderStatus"/>
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
				<div class="row">
					<dsp:include page="/custsrvc/includes/orderSummary.jsp" flush="true">
						<dsp:param name="order" value="${order}"/>
					</dsp:include>
				</div> <!--  Row -->
				
				<div class="row">

			            <table>
			              <tr>
			                <td><label>Order Data:</label></td></tr><tr>
			                <td>billingSystemOrderId: <c:if test="${!empty order.billingSystemOrderId}">${order.billingSystemOrderId}</c:if></td></tr><tr>
			                <td>ATGOrderId: <c:if test="${!empty order.id}">${order.id}</c:if></td></tr><tr>
			                <td>vestaSystemOrderId: <c:if test="${!empty order.vestaSystemOrderId}">${order.vestaSystemOrderId}</c:if></td></tr><tr>
			                <td>billSysPaymentRefId: <c:if test="${!empty order.billSysPaymentRefId}">${order.billSysPaymentRefId}</c:if></td></tr><tr>
			                <td>billSysPaymentApprTransId: <c:if test="${!empty order.billSysPaymentApprTransId}">${order.billSysPaymentApprTransId}</c:if></td></tr><tr>
			                <td>estimatedDeliveryDate: <c:if test="${!empty order.estimatedDeliveryDate}">${order.estimatedDeliveryDate}</c:if></td></tr><tr>
			                <td>cricCustomerId: <c:if test="${!empty order.cricCustomerId}">${order.cricCustomerId}</c:if></td></tr><tr>
			                <td>cricCustmerBillingNumber: <c:if test="${!empty order.cricCustmerBillingNumber}">${order.cricCustmerBillingNumber}</c:if></td></tr><tr>
			                <td>vestaTransactionId: <c:if test="${!empty order.vestaTransactionId}">${order.vestaTransactionId}</c:if></td></tr><tr>
			                <td>createdByOrderId: <c:if test="${!empty order.createdByOrderId}">${order.createdByOrderId}</c:if></td></tr><tr>
			                <td>submittedDate: <c:if test="${!empty order.submittedDate}">${order.submittedDate}</c:if></td></tr><tr>
							
							
							</tr>
							</table>
							<table>
							<tr>
			                
			                <td>Shipping Address</td></tr><tr>
			                <td>ShippingMethod: <c:if test="${!empty order.shippingGroups[0].shippingMethod}">${order.shippingGroups[0].shippingMethod}</c:if></td></tr><tr>
			                <td>SpecialInstructions: <c:if test="${!empty order.shippingGroups[0].specialInstructions}">${order.shippingGroups[0].specialInstructions}</c:if></td></tr><tr>
			                <td>firstName: <c:if test="${!empty order.shippingGroups[0].shippingAddress.firstName}">${order.shippingGroups[0].shippingAddress.firstName}</c:if></td></tr><tr>
			                <td>lastName: <c:if test="${!empty order.shippingGroups[0].shippingAddress.lastName}">${order.shippingGroups[0].shippingAddress.lastName}</c:if></td></tr><tr>
			                <td>address1: <c:if test="${!empty order.shippingGroups[0].shippingAddress.address1}">${order.shippingGroups[0].shippingAddress.address1}</c:if></td></tr><tr>
			                <td>address2: <c:if test="${!empty order.shippingGroups[0].shippingAddress.address2}">${order.shippingGroups[0].shippingAddress.address2}</c:if></td></tr><tr>
			                <td>city: <c:if test="${!empty order.shippingGroups[0].shippingAddress.city}">${order.shippingGroups[0].shippingAddress.city}</c:if></td></tr><tr>
			                <td>state: <c:if test="${!empty order.shippingGroups[0].shippingAddress.state}">${order.shippingGroups[0].shippingAddress.state}</c:if></td></tr><tr>
			                <td>postalCode: <c:if test="${!empty order.shippingGroups[0].shippingAddress.postalCode}">${order.shippingGroups[0].shippingAddress.postalCode}</c:if></td></tr><tr>
			                <td>trackingNumber: <c:if test="${!empty order.shippingGroups[0].trackingNumber}">${order.shippingGroups[0].trackingNumber}</c:if></td></tr><tr>
			                
							</tr><tr>
							
							</tr>
							</table>
							
							

							<table>
			                <tr></tr>
			                <td>Billing Info</td>
			                </tr><tr>
			                <td>creditCardNumber: <c:if test="${!empty order.paymentGroups[0].creditCardNumber}">${order.paymentGroups[0].creditCardNumber}</c:if></td></tr><tr>
			                <td>creditCardType: <c:if test="${!empty order.paymentGroups[0].creditCardType}">${order.paymentGroups[0].creditCardType}</c:if></td></tr><tr>
			                <td>expirationMonth: <c:if test="${!empty order.paymentGroups[0].expirationMonth}">${order.paymentGroups[0].expirationMonth}</c:if></td></tr><tr>
			                <td>expirationYear: <c:if test="${!empty order.paymentGroups[0].expirationYear}">${order.paymentGroups[0].expirationYear}</c:if></td></tr><tr>
			                <td>ccFirstName: <c:if test="${!empty order.paymentGroups[0].ccFirstName}">${order.paymentGroups[0].ccFirstName}</c:if></td></tr><tr>
			                <td>ccLastName: <c:if test="${!empty order.paymentGroups[0].ccLastName}">${order.paymentGroups[0].ccLastName}</c:if></td></tr><tr>
			                <td>address1: <c:if test="${!empty order.paymentGroups[0].billingAddress.address1}">${order.paymentGroups[0].billingAddress.address1}</c:if></td></tr><tr>
			                <td>address2: <c:if test="${!empty order.paymentGroups[0].billingAddress.address2}">${order.paymentGroups[0].billingAddress.address2}</c:if></td></tr><tr>
			                <td>city: <c:if test="${!empty order.paymentGroups[0].billingAddress.city}">${order.paymentGroups[0].billingAddress.city}</c:if></td></tr><tr>
			                <td>stateAddress: <c:if test="${!empty order.paymentGroups[0].billingAddress.state}">${order.paymentGroups[0].billingAddress.state}</c:if></td></tr><tr>
			                <td>postalCode: <c:if test="${!empty order.paymentGroups[0].billingAddress.postalCode}">${order.paymentGroups[0].billingAddress.postalCode}</c:if></td></tr><tr>
			                <td>vestaToken: <c:if test="${!empty order.paymentGroups[0].vestaToken}">${order.paymentGroups[0].vestaToken}</c:if></td></tr><tr>
			                <td>transactionId: <c:if test="${!empty order.paymentGroups[0].transactionId}">${order.paymentGroups[0].transactionId}</c:if></td></tr><tr>
							</tr>
							</table>

				</div>
				
				
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
</div> <!--/#inner-wrap-->
</div> <!--/#outer-wrap-->
</body>
</dsp:page>