<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/commerce/inventory/InventoryLookup" />
<dsp:importbean bean="/com/cricket/browse/ThresholdMessageDroplet" />
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/OriginatingRequest" var="originatingRequest"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<dsp:getvalueof var="defaultSortParam" bean="CricketConfiguration.defaultSortParam"/>
<dsp:getvalueof var="pkgCount" param="pkgCount"/>
<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>
<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>
<dsp:getvalueof var="OOFMarketType" bean="CartConfiguration.outOfFootPrintMarketType"></dsp:getvalueof>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="allphonesCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.allPlansCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="allplansCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>
<dsp:droplet name="DimensionValueCacheDroplet">
	<dsp:param name="repositoryId" bean="CricketConfiguration.otherAddonsCategoryId"/>
	<dsp:oparam name="output">
		<dsp:getvalueof var="addonsCategoryCacheEntry" param="dimensionValueCacheEntry" />
     </dsp:oparam>
</dsp:droplet>     
<!-- Add Another Package -->	
					<c:if test="${OOFMarketType ne marketType}">
						<section class="package">
							<p class="title"><a href="#" class="package-title">Add Another 
								<c:choose>
									<c:when test="${isUserLoggedIn}">Line</c:when>
									<c:otherwise>Package</c:otherwise>
								</c:choose>
							</a></p>
							<div class="content">
								<div class="row">
									<div class="large-8 small-12 columns details-container">
										<div class="row">
											<div class="large-4 small-12 columns">
												<h3>Phone</h3>
												<div class="item-details">
												<p>No Phone Selected.</p>
												</div>
												<c:choose>
													<c:when test="${not empty defaultSortParam}">
														<p><a href="${contextpath}${allphonesCategoryCacheEntry.url}&sort=${defaultSortParam}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_add_phone"/><!-- Add Phone --></a></p>
													</c:when>
													<c:otherwise>
														<p><a href="${contextpath}${allphonesCategoryCacheEntry.url}" class="circle-arrow green"><crs:outMessage key="cricket_shoppingcart_add_phone"/><!-- Add Phone --></a></p>
													</c:otherwise>
												</c:choose>												
											</div>
											<div class="large-4 small-12 columns">
												<h3>Plan</h3>
												<div class="item-details">
												<p>No Plan Selected.</p>
												</div>
												 <p><a href="${contextpath}${allplansCategoryCacheEntry.url}&addAnotherPackage=true" class="circle-arrow green">Add Plan</a>
										
											</div>											
											<c:if test="${OOFMarketType ne marketType}"><!-- Condition added for defect 748 -->
												<div class="large-4 small-12 columns">
													<h3>Add-ons</h3>
													<div class="item-details">
													<p>No Add-ons Selected.</p>
													</div>
													<p><a href="${contextpath}${addonsCategoryCacheEntry.url}" class="circle-arrow green">Select Add-Ons</a></p>												
												</div>
											</c:if>
										</div>		
									</div> <!--/.details-container-->
									<!-- packages price details -->
									<dsp:include page="/cart/includes/display_packagePrice.jsp"></dsp:include>
								</div>
							</div> <!--/.content-->
						</section> 
					</c:if>    


</dsp:page>