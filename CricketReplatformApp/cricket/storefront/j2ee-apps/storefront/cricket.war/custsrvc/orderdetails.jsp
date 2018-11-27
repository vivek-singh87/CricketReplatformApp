<dsp:page>
<div id="constructor" class="section-checkout step3 thankyou">
  <section id="section-progress">
    <div class="section-progress-content">
      <div class="row">
        <div class="large-12 small-12 columns">
        </div>    
      </div>  
    </div> <!--/.section-progress-content-->    
  <!-- Checkout bill/ship info -->
  <section id="section-checkout-form">
    <div class="row">   
         <div class="row divider-thankyou">
          <div class="divider thankyou large-12 small-12"></div>
        </div>
          <!-- SUMMARY DESKTOP-->        
             <dsp:include page="/custsrvc/includes/orderSummary.jsp" flush="true">
					<dsp:param name="order" value="${order}"/>
             </dsp:include>           
          <!-- Mobile layout for Overview -->
     </div>
  </section>
</div> <!--/#constructor-->
<!--// START FOOTER AREA //-->

</dsp:page>