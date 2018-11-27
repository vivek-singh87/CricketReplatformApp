<dsp:page>

	<dsp:include page="/common/head.jsp">
		<dsp:param name="seokey" value="billingOrderIdKey" />
	</dsp:include>
	<dsp:importbean bean="/com/cricket/common/util/formhandler/RetrieveBillingOrderDetailsFormHandler"/>
	<dsp:getvalueof var="contextPath" bean="/OriginatingRequest.contextPath"/>
	<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>

<!-- Default form error handling support -->
    <dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
      <dsp:oparam name="output">
        <b><dsp:valueof param="message"/></b><br>
      </dsp:oparam>
      <dsp:oparam name="outputStart">
        <LI>
      </dsp:oparam>
      <dsp:oparam name="outputEnd">
        </LI>
      </dsp:oparam>
    </dsp:droplet>
	<script>
		function submitOrderIdForm() {
			var html = "";
			var billingOrderID = $.trim($('#order_numI').val());
			if(billingOrderID != null && billingOrderID.length >0)
			{
				$('#setbillingOrderID').val(billingOrderID);
				$('#changeLoctionRedirectURL').val(document.URL);
				$('#retrieveOrderSubmit').trigger('click');
			}
			else 
			{
				$(".errors-location").html("<p>Please enter a valid Billing order Id.</p>");
				$(".errors-location").show();
				//document.getElementById('erroremptyorder').innerHTML = "<p>Please enter a valid Billing order Id.</p>";
			}
		}
	</script>
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
			    </div>
			  </div><!-- /#statusform-header-->
			  
				  <div class="row">
				    <div id="statusform-content" class="columns large-12 small-12">
				      
					      <div class="row">
					        <div class="columns large-12 small-12">	
					        	      
					          <dsp:form style="display:none" action="${pageContext.request.requestURL}" name="getOrderSummaryForm" id="getOrderSummaryForm"  method="post">
		                			<dsp:input id="changeLoctionRedirectURL" type="hidden" value="" bean="RetrieveBillingOrderDetailsFormHandler.redirectURL"/>	
									<dsp:input id="setbillingOrderID" type="hidden" value="" bean="RetrieveBillingOrderDetailsFormHandler.billingOrderID"/>					
									<dsp:input id="successUrl" type="hidden" value="billingOrderSummary.jsp" bean="RetrieveBillingOrderDetailsFormHandler.successURL"/>
									<dsp:input id="errorUrl" type="hidden" value="${pageContext.request.requestURL}" bean="RetrieveBillingOrderDetailsFormHandler.errorURL"/>
									<dsp:input id="retrieveOrderSubmit" type="submit" bean="RetrieveBillingOrderDetailsFormHandler.retrieveOrder" value="submit2"/>
							  </dsp:form>
								
					          <p>Tracking your Cricket Order is easy. Just enter the order confirmation number you received at checkout below.</p>
					          
					        </div>
					      </div>
				      
					      <div class="row">
					        <div class="columns large-12 small-12">
					          <p>Order Confirmation Number:</p>
					        </div>
					      </div>
					      
					      <div class="row">
							<dsp:form iclass="form-container" name="getOrderIDForm" id="getOrderIDForm" action="#">
					            <div class="columns large-5 small-9">
					            	<dsp:input id="order_numI" name="order_numI" type="text" bean="RetrieveBillingOrderDetailsFormHandler.billingOrderID"/>
					            </div>
					            <div class="columns large-2 small-3">
					            	<a class="button prefix orange-button" href="#" onclick="submitOrderIdForm()">submit</a>
					            </div>
								<div class="columns large-1 small-1"></div>
					        </dsp:form>
					      </div><!-- /.row -->
					      
					      <div class="row">
					        <div class="columns large-12 small-12 errors-location">
					          <div id="erroremptyorder"><!--<p>No order exists for this order ID.</p>--></div>
						          <dsp:droplet name="Switch">
									<dsp:param name="value" bean="RetrieveBillingOrderDetailsFormHandler.responseVO.ifOrderExist"/>
									<dsp:oparam name="false">
									<font color="red">No order exists for this order ID.</font>
									</dsp:oparam>								
								   </dsp:droplet>	
					        </div>
					      </div>
				      
					</div><!-- /#statusform-content .columns -->
				</div><!-- /.row-->
			</section>
		</div> <!-- /#constructor-->
		
		<!--// START FOOTER AREA //-->
		<dsp:include page="/common/footer.jsp"/>
		</div> <!--/#inner-wrap-->
		</div> <!--/#outer-wrap-->
		<dsp:include page="/common/includes/click_to_chat.jsp">
			<dsp:param name="PAGE_TYPE" value="OrderStatusPage"/>
		</dsp:include>
		
		
		<script type="text/javascript" src="${contextpath}/js/vendor/jquery-ui.min.js"></script>
		<script type="text/javascript" src='${contextpath}/js/chatGui-min.js'></script>
		
		<!-- Foundation 4 -->
		<script src="${contextpath}/js/foundation/foundation.min.js"></script> 
		<script>
		$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips');
		</script>
		
		<!-- Client Side Validation -->
		<script src="${contextpath}/js/vendor/jquery.validate.min.js"></script>
		
		<!-- SWIPER Library; Used for Phones and Plans on small screens -->
		<script src="${contextpath}/lib/swiper/idangerous.swiper.js"></script>
		
		<!-- Auto complete Plugin for Search -->
		<script src="${contextpath}/lib/autocomplete/jquery.autocomplete.js" type="text/javascript"></script>
		
		<!-- Cricket specific JS -->
		<script src="${contextpath}/js/cricket.min.js"></script> <!-- Global Utilities -->
	
	</body>
</dsp:page>