<dsp:page>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<dsp:include page="/common/head.jsp"/>
	<body>
		<div id="outer-wrap">
			<div id="inner-wrap">
				<dsp:include page="/common/header.jsp"/>
				<!--// END HEADER AREA //-->
				<div id="constructor" class="order-status">
					<dsp:include page="includes/ipAddressFormFragment.jsp"/>
					<!-- include ip address jsp -->
				</div>
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