<dsp:page>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>

	<dsp:include page="/common/head.jsp">
		<dsp:param name="seokey" value="muveMusicKey" />
	</dsp:include>
	<body>

	<div id="outer-wrap">
		<div id="inner-wrap">
   			<dsp:include page="/common/header.jsp"/>
			<div id="constructor">
				<div style="width:500px; height:100px; margin-left:500px;margin-top:50px">
					This page in under construction
				</div>
			</div>
			<dsp:droplet name="/atg/dynamo/droplet/Cache">
				<dsp:param value="cricketFooter" name="key"/>
				<dsp:oparam name="output">
					<dsp:include page="/common/footer.jsp"/>
				</dsp:oparam>
			</dsp:droplet>
		
		</div> <!--/#inner-wrap-->
	</div> <!--/#outer-wrap-->
	
	
<!-- JavaScript -->	

<!-- jQuery -->	
<!-- <script src="js/vendor/jquery-1.9.0.min.js"></script>  -->
<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="Muve Music"/>
</dsp:include>
<!-- Foundation 4 -->
<script src="js/foundation/foundation.min.js"></script>
<script>
	$(document).foundation('forms interchange reveal section topbar');
	$(document).foundation('orbit', {
		  timer_speed: timeForToggle,
		  pause_on_hover: true,
		  resume_on_mouseout: true,
		  animation_speed: 500,
		  bullets: true,
		  stack_on_small: true,
		  container_class: 'orbit-container',
		  stack_on_small_class: 'orbit-stack-on-small',
		  next_class: 'orbit-next',
		  prev_class: 'orbit-prev',
		  timer_container_class: 'orbit-timer',
		  timer_paused_class: 'paused',
		  timer_progress_class: 'orbit-progress',
		  slides_container_class: 'orbit-slides-container',
		  bullets_container_class: 'orbit-bullets',
		  bullets_active_class: 'active',
		  slide_number_class: 'orbit-slide-number',
		  caption_class: 'orbit-caption',
		  active_slide_class: 'active',
		  orbit_transition_class: 'orbit-transitioning'
		});
</script>
<!-- Client Side Validation -->
<script src="js/vendor/jquery.validate.min.js"></script>
	
<!-- SWIPER Library; Used for Phones and Plans on small screens -->
<script src="${contextpath}/lib/swiper/idangerous.swiper-2.0.min.js"></script>

<!-- Cricket specific JS -->
<script src="js/cricket.js"></script>
</body>

</html>
<!--// END OF FOOTER AREA OF THE PAGE //-->


</dsp:page>