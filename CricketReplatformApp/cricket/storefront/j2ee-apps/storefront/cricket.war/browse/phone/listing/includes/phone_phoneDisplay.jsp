<dsp:page>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
	<dsp:importbean bean="/com/cricket/browse/ThresholdMessageDroplet"/>
	<dsp:importbean bean="/atg/commerce/inventory/InventoryLookup"/>
	<dsp:importbean bean="/com/cricket/browse/IsFinancingAvailableDroplet"/>
	<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
	<dsp:getvalueof var="userLocationCity" bean="Profile.userLocationCity"></dsp:getvalueof>
	<dsp:getvalueof var="OOFMarketType" bean="CartConfiguration.outOfFootPrintMarketType"></dsp:getvalueof>
	<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>
	<dsp:getvalueof var="paymentPlansLink" bean="CricketConfiguration.shopPagesLinks.paymentPlans" />
	<dsp:getvalueof var="size" param="size"/>
	<dsp:getvalueof var="count" param="count"/>
	<dsp:getvalueof var="throughSearch" param="throughSearch"/>
	<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
	<c:choose>
		<c:when test="${throughSearch eq true}">
			<dsp:getvalueof var="productId" param="product.id"/>
			<dsp:getvalueof var="skuId" param="product.childSkus[0].id"/>
			<dsp:getvalueof var="imageURL" param="product.fullImage.url"/>
			<dsp:getvalueof var="seoString" param="seoString"/>
		</c:when>
		<c:otherwise>
			<dsp:getvalueof var="productId" param="product.productId"/>
			<dsp:getvalueof var="seoString" param="product.seoString"/>
			<dsp:getvalueof var="skuId" param="product.defaultSkuId"/>
			<dsp:getvalueof var="largeImagesList" param="product.largeImages"/>
			<c:if test="${fn:length(largeImagesList) > 0}">				
				<dsp:getvalueof var="imageURL" value="${largeImagesList[0]}"/>
			</c:if>	
		</c:otherwise>
	</c:choose>
	<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
		<dsp:param name="id" value="${productId}"/>
		<dsp:param name="seoString" value="${seoString}"/>
		<dsp:param name="itemDescriptorName" value="phone-product"/>
		<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
		</dsp:oparam>
	</dsp:droplet>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="packageId" param="packageId"/>
	<c:if test="${!empty packageId}">		
		<c:set var="packageVal" value="?packageId=${packageId}" />
	</c:if>
	<dsp:getvalueof var="upgradeItemFlow" param="upgradeItemFlow"/>
	<c:if test="${empty packageId && !empty upgradeItemFlow}">		
		<c:set var="upgradeItemVal" value="?upgradeItemFlow=${upgradeItemFlow}" />
	</c:if>
	<c:if test="${!empty packageId && !empty upgradeItemFlow}">		
		<c:set var="upgradeItemVal" value="&upgradeItemFlow=${upgradeItemFlow}" />
	</c:if>
	<dsp:getvalueof var="editPhone" param="editPhone"/>
	<c:if test="${!empty editPhone}">
		<c:choose>
			<c:when test="${empty packageVal && empty upgradeItemVal}">
				<c:set var="editPhoneVal" value="?editPhone=${editPhone}" />
			</c:when>
			<c:otherwise>
				<c:set var="editPhoneVal" value="&editPhone=${editPhone}" />
			</c:otherwise>
		</c:choose>
	</c:if>
	<c:set var="detailsURL" value="${contextpath}${canonicalUrl}${packageVal}${upgradeItemVal}${editPhoneVal}" />
	<div class="large-4 small-12 columns phone result-item">
		<div class="result-content">
			<h4>
				<dsp:a href="${detailsURL}">
					<dsp:getvalueof var="displayName" param="product.displayName"/>		
					<dsp:droplet name="/com/cricket/browse/droplet/DisplayNameDroplet">
						<dsp:param name="displayName" value="${displayName}" />					
						<dsp:oparam name="output">
							<dsp:getvalueof var="productDisplayName" param="displayName"/>				
							<dsp:getvalueof var="hasSpecialSymbol" param="hasSpecialSymbol"/>
							<dsp:getvalueof var="firstString" param="firstString"/>
							<dsp:getvalueof var="secondString" param="secondString"/>
							<dsp:getvalueof var="specialSymbol" param="specialSymbol"/>
							<c:choose>
							<c:when test="${hasSpecialSymbol eq true}">	
								<h4>${firstString}<sup>${specialSymbol}</sup>${secondString}</h4>								
							</c:when>	
							<c:otherwise>
								<h4>${productDisplayName}</h4>
							</c:otherwise>
						</c:choose>
							
						</dsp:oparam>
					</dsp:droplet>
				</dsp:a>
			</h4>
					
			<a href="${detailsURL}">
		<!--		<dsp:img style="height: 264px; width: 126px" src="${contextpath}/${imageURL}" alt=""/> -->
				<!-- START Liquid Pixel Image display droplet -->
				<dsp:getvalueof var="height" bean="ImageConfiguration.height.phoneListingThumb" />
							<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
							<dsp:param name="imageLink" value="${imageURL}" />
							<dsp:param name="imageHeight" value="${height}"/>
							<dsp:oparam name="output">
							<dsp:getvalueof var="liquidpixelurl" param="url"/>								  
							<%-- 	<img style="height: 264px; width: 126px" src="${liquidpixelurl}" alt=""/> --%>
								<img src="${liquidpixelurl}" alt=""/>
							</dsp:oparam>
					 </dsp:droplet>
					 <!--END Liquid Pixel Image display droplet-->
			</a>		
			<dsp:getvalueof var="userLocationZipCode" bean="Profile.userLocationZipCode"/>	
			<!-- Get discount and price values for product -->
			<c:set var="fPrice" value="0" />
			<dsp:include page="/browse/includes/discountTag_price.jsp">
				<dsp:param name="productId" value="${productId}"/>
				<dsp:param name="skuId" value="${skuId}" />
			</dsp:include>
			<c:set var="fPrice" value="${finalPrice}" />
  		   	<!--Display Discount image based on the promotion applicable -->			
			<c:if test="${zipCodeKnown eq true}">				
				<c:if test="${not empty percentOff && percentOff ne 0}">
					<span class="discount"><fmt:formatNumber type="number" pattern="###" value="${percentOff}" />% off</span>
				</c:if>			
			</c:if>
			<!--
			<c:if test="${userLocationZipCode!=null}">
				<dsp:getvalueof var="discType" param="product.discountType" />				
				<c:if test="${not empty discType}">
					<dsp:getvalueof var="discAmt" param="product.discountAmt" />
					<c:choose>
						<c:when test="${discType eq 'percentOff'}">
							<span class="discount"><fmt:formatNumber type="number" pattern="###" value="${discAmt}" />% off</span>
						</c:when>
						<c:when test="${discType eq 'amountOff'}">
							<span class="discount">$<fmt:formatNumber type="number" pattern="###" value="${discAmt}" /> off</span>							
						</c:when>
					</c:choose>				
				</c:if>				
			</c:if> -->						
  		    <!-- Display Price if the zipcode is known -->    
			<c:if test="${userLocationZipCode!=null}">
				<c:set var="fPrice" value="${fPrice}" />
				<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${fPrice}" var="fPrice" />
				
				<dsp:getvalueof var="isFinanceAvailable" param="product.financingAvailable"/>
								
				<c:set var="splitPrice" value="${fn:split(fPrice, '.')}"/>	
				
				<p class="green-price">
					<sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup>
					<c:choose>
					      <c:when  test='${fn:containsIgnoreCase(displayName, "Apple")}'>					     
					      </c:when>
					      <c:otherwise>
					  		  <a href="#legal" class="financing scroll-link">Important Device Purchase Information</a>	
					      </c:otherwise>
					</c:choose>							
				</p>
			</c:if>
			
			<!-- Changes done as part of defect 5819 - updates from TPN -->
			<a href="${detailsURL}" class="button medium">Shop Now</a>
			<!--  
			<dsp:droplet name="/atg/commerce/catalog/SKULookup">
				<dsp:param name="id" value="${skuId}"/>
				<dsp:param name="elementName" value="sku"/>	
					<dsp:oparam name="output">											 				
						<dsp:droplet name="InventoryLookup">
						   <dsp:param name="itemId" value="${skuId}"/>
					  			<dsp:oparam name="output">
							     	<dsp:droplet name="ThresholdMessageDroplet">
								     	<dsp:param name="threshold" param="threshold"/>
								     	<dsp:param name="stockLevel" param="inventoryInfo.stockLevel"/>
										<dsp:param name="thresholds" param="sku.thresholds"/> 
								     	<dsp:oparam name="output">
								     	 	<dsp:getvalueof var="isOutOfStock" param="isOutOfStock"/>
											<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
								     	</dsp:oparam>
							    	 </dsp:droplet>
					       	 	</dsp:oparam>
					     </dsp:droplet>										       
					</dsp:oparam>
			</dsp:droplet>
			<c:choose>
				<c:when test="${isOutOfStock eq true}">					
					<a class="grey-add-cart button disabled secondary" href="#" onclick="return false">Add to Cart</a>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${zipCodeKnown eq false}">						
							<a class="grey-add-cart button disabled secondary" href="#" onclick="return false">Add to Cart</a>
						</c:when>	
						<c:otherwise>				  
							<dsp:include page="/cart/common/addToCart.jsp">							
								<dsp:param name="addToCartClass" value="${addToCartClass}"/>
								<dsp:param name="productId" value="${productId}"/>
								<dsp:param name="skuId" value="${skuId}"/>
								<dsp:param name="quantity" value="1"/>
								<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
							</dsp:include>
						</c:otherwise>
					</c:choose>	
				</c:otherwise>
			</c:choose>
			<a href="${detailsURL}" class="circle-arrow">Learn More</a>
			-->
			<c:choose>
				<c:when test="${fn:contains(sessionComapreProdIds, productId)}">
					<dsp:getvalueof var="checkedClass" value="customChecked"/>
					<dsp:getvalueof var="toolTipContent" value=""/>
					<dsp:getvalueof var="toolTipClass" value=""/>
				</c:when>
				<c:otherwise>
					<dsp:getvalueof var="checkedClass" value="customUnchecked"/>
					<c:choose>
						<c:when test="${compareBucketCount gt 3}">
							<dsp:getvalueof var="checkedClass" value="customUnchecked customDisabled"/>
							<dsp:getvalueof var="toolTipContent" value="Sorry, you can only compare up to four phones at a time."/>
							<dsp:getvalueof var="toolTipClass" value="has-tip has-tipCustom"/>
							<dsp:getvalueof var="dataToolTip" value="data-tooltip=''"/>
						</c:when>
						<c:otherwise>
							<dsp:getvalueof var="toolTipContent" value=""/>
							<dsp:getvalueof var="toolTipClass" value=""/>
							<dsp:getvalueof var="dataToolTip" value=""/>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
			<div class="compareCustom hide-for-small">
	    		<form class="custom2">
					<%-- <label for="checkbox1" class="${toolTipClass}" data-tooltip="" onclick="javascript:handleAddToCompare('${productId}' , '${liquidpixelurl}');" title="${toolTipContent}"> --%>
					<label for="checkbox1" onclick="javascript:handleAddToCompare('${productId}' , '${liquidpixelurl}');" class="${toolTipClass}" ${dataToolTip} title="${toolTipContent}">
						<input type="checkbox" id="${productId}" style="display: none;">
						<span id="compareBox${productId}" class="${checkedClass}"></span> Compare
					</label>
				</form>
			</div>
		</div>
	</div>		
</dsp:page>