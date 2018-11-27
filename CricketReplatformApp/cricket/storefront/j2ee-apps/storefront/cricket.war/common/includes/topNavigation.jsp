<dsp:page>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:importbean bean="/atg/dynamo/droplet/Cache"/>
	<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
	<dsp:getvalueof var="isUserLoggedIn" bean="Profile.isUserLoggedIn"/>

<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>
<dsp:getvalueof var="OOFMarketType" bean="CartConfiguration.outOfFootPrintMarketType"></dsp:getvalueof>
	<c:set var="cartQuantity" value="0"/>

		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="userIntention"  bean="/com/cricket/commerce/order/session/UpgradeItemDetailsSessionBean.userIntention"/>
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="userIntention"  value="New Activation"/>
		</c:if>
		<c:if test="${empty userIntention}">
			<dsp:getvalueof var="userIntention"  value="null"/>
		</c:if>
		
		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="network" bean="/com/cricket/util/LocationInfo.networkProviderName"/>
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="network" bean='/atg/userprofiling/Profile.networkProvider' />
		</c:if>
		<c:if test="${empty network}">
			<dsp:getvalueof var="network"  value="null"/>
		</c:if>
		
		<c:if test="${isUserLoggedIn eq true}">
			<dsp:getvalueof var="customerTypeVar"  bean="/atg/cricket/util/CricketProfile.customerType"/>
			<c:set var="customerTypeVar" value="EXISTING" />
		</c:if>
		<c:if test="${isUserLoggedIn eq false}">
			<dsp:getvalueof var="customerTypeVar"  value="NEW"/>
		</c:if>
		<c:if test="${empty customerTypeVar}">
			<dsp:getvalueof var="customerTypeVar"  value="null"/>
		</c:if>
				<dsp:droplet name="ForEach">
					<dsp:param name="array" bean="/atg/commerce/ShoppingCart.current.commerceItems"/>
					<dsp:oparam name="output">
					<%-- <c:set var="citemQty">
					</c:set> --%>
					<dsp:getvalueof var="citemQty" param="element.quantity"/>
					<c:set var="cartQuantity" value="${cartQuantity + citemQty}"/>
					</dsp:oparam>
					</dsp:droplet>
	<script>
		function hideAutoSuggestResults() {
			$("#predictive-search-zip").hide();
		}
		
		function fireShopAction5s() {
			var pageID = "CART";
			var categoryID = "SHOP:CART";
			//must be dynamic values					
			var city = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.city'/>';
			var zipCode = '<dsp:valueof bean='/com/cricket/integration/maxmind/CitySessionInfoObject.cityVO.postalCode'/>';
			var network = '<dsp:valueof value='${network}'/>';
			var userIntention = '<dsp:valueof value='${userIntention}'/>';
			var customerType = '<dsp:valueof value='${customerTypeVar}'/>';
			var pvAttrs =city+"-_- "+zipCode+"-_- "+network+"-_- "+customerType+"-_- "+userIntention;
			var totalItemsInCart = '<dsp:valueof value="${cartQuantity}" />';
			cmCreatePageviewTag(pageID,categoryID,null,null,pvAttrs);
			for(i=0; i < totalItemsInCart; i++ ) {
				var productName = $("#productName" + i).val();
				var productId = $("#productId" + i).val();
				var quantity = $("#quantity" + i).val();
				var unitPrice = $("#unitPrice" + i).val();
				var product = $("#producttype" + i).val();
				if (product.indexOf('phone-product') != -1){
					var parentCategoryId = "HANDSETS";
				}
				else {
					if(product.indexOf('accessory-product') != -1){
						var parentCategoryId = "ACCESSORIES";
					}
					else{
						if(product.indexOf('plan-product') != -1){
							var parentCategoryId = "VOICE PLANS";
						}
						else{
							var parentCategoryId = "PLAN ADD ONS";
						}			
					}
				}
				cmCreateShopAction5Tag(productId, productName, quantity, unitPrice, parentCategoryId, pvAttrs);
			}
			cmDisplayShops();
		}
	</script>

	<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
	<dsp:importbean bean="/com/cricket/browse/SortCatalogItemsDroplet"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/atg/cricket/util/CricketProfile"/>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:getvalueof var="aboutCricketLink" bean="CricketConfiguration.homePageLinks.aboutCricket" />
	<dsp:getvalueof var="payGoLink" bean="CricketConfiguration.homePageLinks.cricketPayGo" />
	<dsp:getvalueof var="whyCricketLink" bean="CricketConfiguration.homePageLinks.whyCricket" />
	<dsp:getvalueof var="phonePaymentPlansLink" bean="CricketConfiguration.homePageLinks.phonePaymentPlans" />
	<dsp:getvalueof var="allSupportLink" bean="CricketConfiguration.homePageLinks.allSupport" />
	<dsp:getvalueof var="contactUsLink" bean="CricketConfiguration.homePageLinks.contactUs" />
	<dsp:getvalueof var="cellPhonesLink" bean="CricketConfiguration.homePageLinks.cellPhones" />
	<dsp:getvalueof var="plansLink" bean="CricketConfiguration.homePageLinks.plans" />
	<dsp:getvalueof var="muveMusicLink" bean="CricketConfiguration.homePageLinks.muveMusic" />
	<dsp:getvalueof var="phonePaymentPlansLink" bean="CricketConfiguration.homePageLinks.phonePaymentPlans" />
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="defaultSortParam" bean="CricketConfiguration.defaultSortParam"/>
	<dsp:getvalueof var="rootcontextpath" bean="CricketConfiguration.rootcontextpath"/>
	<dsp:getvalueof var="customerType" bean="CricketProfile.customerType"/>
	<dsp:getvalueof var="protocalType" param="protocalType"/>
	
	<nav class="top-bar row"  data-options="custom_back_text:true;back_text:Back">
		<ul class="title-area">
			<div class="acResults results-dropdown" id="globalSearchBox">
				<ul id="typeAheadSearchResultsUL">
				</ul>
			</div>
			<div class="acResults results-dropdown searchAgain" id="againSearchBox">
				<ul id="typeAheadSearchResultsULAgain">
				</ul>
			</div>
			
			<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
				<dsp:param name="inUrl" value="${rootcontextpath}"/>
				<dsp:oparam name="output">
			    	<dsp:getvalueof var="rootcontextpath" param="nonSecureUrl" />
			        <dsp:getvalueof var="securerootcontextpath" param="secureUrl" />
			  	</dsp:oparam>
			</dsp:droplet>
			<c:set var="splitUrl" value="${fn:split(rootcontextpath, ';')}"/>
			<li class="name">
				<div>
					<a href="${rootcontextpath}">
					<c:choose>
					      <c:when  test="${protocalType eq 'secure'}">
					        <img src="${securerootcontextpath}/img/logo-cricket-wireless.png" data-interchange="[${securerootcontextpath}/img/logo-cricket-wireless.png, (default)], [${securerootcontextpath}/img/logo-cricket.png, (screen and (max-width: 480px))]" alt=""/>
					      </c:when>
					      <c:otherwise>
							<img src="${splitUrl[0]}/img/logo-cricket-wireless.png" data-interchange="[${splitUrl[0]}/img/logo-cricket-wireless.png, (default)], [${splitUrl[0]}/img/logo-cricket.png, (screen and (max-width: 480px))]" alt=""/>
					      </c:otherwise>
					   </c:choose>
					</a>
				</div>
			</li>
			<li class="cart-icon">
				<a href="#">Cart</a>
				<a href="#" class="num-cart-items">${cartQuantity}</a>
			</li>
			<!-- Remove the class "menu-icon" to get rid of menu icon. Take out "Menu" to just have icon alone -->
			<li class="toggle-topbar menu-icon" onclick="hideAutoSuggestResults();"><a href="javascript:void(0);"><span>&nbsp;</span></a></li>
			<li class="account-icon" id="open-side-menu"><a href="#">Open</a></li>
		</ul>
		
		<section class="top-bar-section">
				<ul id="nav-main" class="right">
					<dsp:include page="/search/mobile/mobileSearch.jsp"/>
					<dsp:droplet name="Cache">
						<dsp:param value="topNavigation_${marketType}_${customerType}" name="key"/>
						<dsp:oparam name="output">
							<dsp:droplet name="SortCatalogItemsDroplet">
								<dsp:param name="catalogItems" bean="Profile.catalog.rootCategories"/>
								<dsp:oparam name="output">
							    <dsp:droplet name="ForEach">
							    	<dsp:param name="array" param="sortedCatalogItems"/>
							    	<dsp:param name="elementName" value="rootCategory"/>
							    	<dsp:oparam name="output">
							    		<dsp:getvalueof var="categoryId" param="rootCategory.id"/>
				    					<dsp:getvalueof var="featuredCategoryId" bean="CricketConfiguration.featurePhonesCategoryId"/>
				    					<c:if test="${categoryId ne featuredCategoryId}">
								    		<li id="li-phones" class="has-dropdown">
									    		<a href='#'>
									    			<dsp:valueof param="rootCategory.displayName"/>
									    		</a>
									    		<ul class="dropdown">
									    			<dsp:droplet name="ForEach">
									    				<dsp:param name="array" param="rootCategory.childCategories"/>
									    				<dsp:param name="elementName" value="childCategory"/>
									    				<dsp:oparam name="output">
									    					<dsp:getvalueof var="cacheEntryUrl" value="" />
									    					<dsp:getvalueof var="childCategoryId" param="childCategory.id"/>
									    					<dsp:getvalueof var="addonsCategoryId" bean="CricketConfiguration.addonsCategoryId"/>
									    					<dsp:getvalueof var="payGoCategoryId" bean="CricketConfiguration.payGoCategoryId"/>
															<dsp:getvalueof var="categoryName" param="childCategory.displayName"/>
															<c:if test="${categoryName ne 'Plan Add-ons' || OOFMarketType ne marketType}"><!-- Condition added for defect 748 -->
									    					<c:if test="${customerType eq null || customerType ne 'O' || childCategoryId ne addonsCategoryId}" >
										    					<c:choose>
										    						<c:when test="${childCategoryId eq addonsCategoryId}">
										    							<dsp:getvalueof var="otherAddonsCategoryId" bean="CricketConfiguration.otherAddonsCategoryId"/>
										    							<dsp:droplet name="DimensionValueCacheDroplet">
																			<dsp:param name="repositoryId" value="${otherAddonsCategoryId}"/>
																			<dsp:oparam name="output">
																				<dsp:getvalueof var="childCategoryCacheEntry" param="dimensionValueCacheEntry" />
																				<dsp:getvalueof var="cacheEntryUrl" value="${childCategoryCacheEntry.url}" />
																	      	</dsp:oparam>
																		</dsp:droplet>
										    						</c:when>
										    						<c:otherwise>
										    							<dsp:droplet name="DimensionValueCacheDroplet">
																			<dsp:param name="repositoryId" param="childCategory.id"/>
																			<dsp:oparam name="output">
																				<dsp:getvalueof var="childCategoryCacheEntry" param="dimensionValueCacheEntry" />
																				<dsp:getvalueof var="cacheEntryUrl" value="${childCategoryCacheEntry.url}" />
																	      	</dsp:oparam>
																		</dsp:droplet>
										    						</c:otherwise>
										    					</c:choose>
																<c:choose>
																	<c:when test="${childCategoryId eq payGoCategoryId}">
											    						<li>
											    							<a href="${payGoLink}">
											    								<dsp:valueof param="childCategory.displayName"/>
											    							</a>
											    						</li>
											    					</c:when>
											    					<c:otherwise>
											    						
											    						<dsp:getvalueof var="linkURL" value="${contextPath}${childCategoryCacheEntry.url}" />
											    						
											    						<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
													  					  <dsp:param name="inUrl" value="${contextPath}${childCategoryCacheEntry.url}"/>
													 					  <dsp:oparam name="output">
																		    <dsp:getvalueof var="linkURL" param="nonSecureUrl" />
																		  </dsp:oparam>
																		</dsp:droplet>
											    					
											    						<li>
												    						<c:choose>
												    							<c:when test="${linkURL}${not empty defaultSortParam}">
												    								<a href="&sort=${defaultSortParam}">
														    							<dsp:valueof param="childCategory.displayName"/>
														    						</a>
												    							</c:when>
												    							<c:otherwise>
												    								<a href="${linkURL}">
														    							<dsp:valueof param="childCategory.displayName"/>
														    						</a>
												    							</c:otherwise>
												    						</c:choose>								    						
												    					</li>
											    					</c:otherwise>
										    					</c:choose>
										    				</c:if>	
															</c:if>
									    				</dsp:oparam>
									    			</dsp:droplet>
									    		</ul>
											</li>
										</c:if>
							    	</dsp:oparam>
							    </dsp:droplet>
							    </dsp:oparam>
							    </dsp:droplet>
						</dsp:oparam>
					</dsp:droplet>
					    <dsp:getvalueof var="whyCricURL" value="${contextPath}/whycricket/why-cricket-landing.jsp" />
									    						
	 					<dsp:droplet name="/atg/dynamo/droplet/ProtocolChange">
	 					  <dsp:param name="inUrl" value="${whyCricURL}"/>
						  <dsp:oparam name="output">
						    <dsp:getvalueof var="whyCricURL" param="nonSecureUrl" />
						  </dsp:oparam>
						</dsp:droplet>
						
					<li class="has-dropdown"><a href="#">Why Cricket?</a>
						<ul class="dropdown">
							<li><a href="${whyCricURL}">Why Cricket?</a></li>
							<li><a href="${aboutCricketLink}">About Cricket</a></li>
							<li><a href="${cellPhonesLink}">Cell Phones</a></li>
							<li><a href="${plansLink}">Plans</a></li>
							<li><a href="${muveMusicLink}">Muve Music<sup>&reg;</sup></a></li>

						</ul>					
					</li>
					<li class="has-dropdown"><a href="#">Support</a>
						<ul class="dropdown">
							<li><a href="${allSupportLink}">All Support</a></li>
							<li><a href="${contactUsLink}">Contact Us</a></li>
						</ul>						
					</li>					
					<li id="li-cart" onclick="fireShopAction5s();">
						<a id="cart-img" href="#">Cart</a>
						<a href="#" class="num-cart-items">${cartQuantity}</a>
					</li>
					<li id="li-search"><a id="searchSymbol" href="#">Search</a></li>
				</ul> <!--/#nav-main-->			
			</section> <!--/.top-bar-section-->
		<div id="drawer-indicator"></div>
	</nav> <!--/.top-bar-->
</dsp:page>