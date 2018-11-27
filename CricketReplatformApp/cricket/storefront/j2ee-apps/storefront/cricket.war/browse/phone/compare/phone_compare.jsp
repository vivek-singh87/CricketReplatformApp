<dsp:page>


<dsp:importbean bean="/atg/commerce/catalog/comparison/ProductListHandler"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/catalog/comparison/ProductList"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:include page="/common/head.jsp">
<dsp:param name="seokey" value="phoneCompareKey" />
</dsp:include>
<body>
<script type="text/javascript"> 
 //cmCreatePageviewTag('http://www.mycricket.com/', 'http://www.mycricket.com/', null, null, pvAttrs);
</script>
<div id="outer-wrap">
<div id="inner-wrap">

	<dsp:include page="/common/header.jsp"/>
<!--// END HEADER AREA //-->



<!--// START CONTENT AREA //-->

<dsp:getvalueof var="queryStringEL" bean="/OriginatingRequest.queryString" vartype="java.lang.String"/>
<dsp:getvalueof var="paramName" value="backLink" vartype="java.lang.String"/>
<dsp:getvalueof var="backLink" param="backLink" vartype="java.lang.String"/>
<c:choose>
	<c:when test="${not empty backLink}">
		<dsp:getvalueof var="backToPhoneCacheValue" value="${backLink}"/>
	</c:when>
	<c:otherwise>
		<dsp:getvalueof var="startSplit" value="${fn:indexOf(queryStringEL, paramName)}"/>
		<dsp:getvalueof var="splittedQuery" value="${fn:substring(queryStringEL, startSplit, fn:length(queryStringEL))}"/>
		<dsp:getvalueof var="startFinalSplit" value="${fn:length(paramName) + 3}"/>
		<dsp:getvalueof var="endFinalSplit" value="${fn:indexOf(splittedQuery, '&')}"/>
		<dsp:getvalueof var="backToPhoneCacheValue" value="${fn:substring(splittedQuery, startFinalSplit, endFinalSplit)}"/>
	</c:otherwise>
</c:choose>
<div id="constructor" class="section-phone-compare">
  
  <!-- Phone Compare doesn't exist for mobile, show this message instead -->
  <!-- Phone Compare doesn't exist for mobile, show this message instead -->
  <div class="row show-for-small">
    <div class="small-12 columns text-center">
      <p>We're sorry, comparing phones is functional on desktop sized screens only.</p>
    </div>
  </div>
	
	<section id="change-phones" class="row hide-for-small">
		<div class="large-12 columns">
		<!-- Starting DimensionValueCacheDroplet -->
			<%-- <dsp:droplet name="DimensionValueCacheDroplet">
				<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="phonesCategoryIdCacheEntry" param="dimensionValueCacheEntry" />
		      </dsp:oparam>
			</dsp:droplet> --%>
			<!-- Ending DimensionValueCacheDroplet -->
			<a href="${contextpath}/browse?N=${backToPhoneCacheValue}" class="button small">Back To Phones<%-- <crs:outMessage key="cricket_phonecompare_back_to_phone"/> --%></a>
			
		</div>	
	</section>

					<section id="section-compare-list" class="hide-for-small">
						<div class="row">
							<table class="table-phone-compare-list persist-area">
								<thead>
									<tr class="persist-header">
										<th>&nbsp;</th>
										<dsp:include
											page="/browse/phone/compare/includes/compare_phoneDisplayName.jsp">
											<dsp:param name="productList" bean="ProductList.items" />
											<dsp:param name="contextpath" value="${contextpath}" />
										</dsp:include>
									</tr>

									<tr class="top-heading">
										<!-- To Display compared phone images  -->
										<dsp:include
											page="/browse/phone/compare/includes/compare_phoneImage.jsp">
											<dsp:param name="productList" bean="ProductList.items" />
										</dsp:include>
									</tr>

									<!-- To Display compared phone prices  -->
									<dsp:include
										page="/browse/phone/compare/includes/compare_phonePrice.jsp">
										<dsp:param name="productList" bean="ProductList.items" />
										<dsp:param name="contextpath" value="${contextpath}" />
									</dsp:include>


								</thead>
								<tbody>
									<!-- To Display compared phone specifications  -->
									<dsp:include
										page="/browse/phone/compare/includes/compare_phoneSpecification.jsp">
										<dsp:param name="productList" bean="ProductList.items" />
									</dsp:include>
								</tbody>
							</table>
						</div>
					</section>

					<dsp:include page="/common/includes/legalContent.jsp">
				<dsp:param name="PAGE_TYPE" value="PHONE"/>
	</dsp:include>
	 <dsp:include page="/browse/banners/footerPromotionalBanner.jsp">
			<dsp:param name="PAGE_NAME" value="PHONE_COMPARISION"/>
	 </dsp:include> 
		
</div> <!--/#constructor-->	

<!--// END CONTENT AREA //-->


<!--// START FOOTER AREA //-->
<dsp:include page="/common/footer.jsp"/>

</div> <!--/#inner-wrap-->
</div> <!--/#outer-wrap-->


<!-- JavaScript -->	

<!-- jQuery -->	

<script src="${contextpath}/js/vendor/jquery-ui.min.js"></script>
<script src="${contextpath}/js/chatGui-min.js"></script>
<dsp:include page="/common/includes/click_to_chat.jsp">
	<dsp:param name="PAGE_TYPE" value="Phone_Compare"/>
</dsp:include>
<!-- Foundation 4 -->
<script src="${contextpath}/js/foundation/foundation.min.js"></script> 
<script>
$(document).foundation('forms interchange magellan reveal topbar orbit alerts section tooltips reveal');
</script>

<!-- Client Side Validation -->
<script src="${contextpath}/js/vendor/jquery.validate.min.js"></script>

<!-- SWIPER Library; Used for Phones and Plans on small screens -->
<script src="${contextpath}/lib/swiper/idangerous.swiper-2.0.min.js"></script>

<!-- Cricket specific JS -->
<script src="${contextpath}/js/cricket.min.js"></script>

<!--  Google javascripts for mobile device -->
<script src="${contextpath}/js/customcricketstore.js" type="text/javascript" charset="utf-8"></script>
<dsp:include page="/common/includes/openCartDrawer.jsp"/> 
</body>
</html>
</dsp:page>