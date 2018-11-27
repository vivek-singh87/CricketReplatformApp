<dsp:page>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>

<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>
	
		<!-- No Phone or Plan Selected Message -->
		<dsp:getvalueof var="order" bean="ShoppingCart.current"/>
		<c:set var="phone" value="0"></c:set>
		<c:set var="plan" value="0"></c:set>
		<c:set var="addon" value="0"></c:set>
		<c:if test="${! empty order.cktPackages}">			
			<c:forEach var="commItem" items="${order.commerceItems}" varStatus="countValue">				
				<dsp:droplet name="/atg/commerce/catalog/ProductLookup">
				<dsp:param name="filterBySite" value="false"/>
       			<dsp:param name="filterByCatalog" value="false"/>
					<dsp:param name="id" value="${commItem.auxiliaryData.productId}"/>
					<dsp:param name="elementName" value="product"/>	
					<dsp:oparam name="output">
						<dsp:getvalueof var="product" param="product"/>
						<c:if test="${product.type eq 'plan-product'}">
							<c:set var="plan" value="${plan+1}"/>
						</c:if>
						<c:if test="${product.type eq 'phone-product'}">
							<c:set var="phone" value="${phone+1}"/>
						</c:if>
						<c:if test="${product.type eq 'addOn-product'}">
							<c:set var="addon" value="${addon+1}"/>
						</c:if>
					</dsp:oparam>
				</dsp:droplet>				
			</c:forEach>
		</c:if>
		<dsp:droplet name="DimensionValueCacheDroplet">
			<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="allPhonesCategoryCacheEntry" param="dimensionValueCacheEntry" />
	      </dsp:oparam>
		</dsp:droplet>
		<dsp:droplet name="DimensionValueCacheDroplet">
			<dsp:param name="repositoryId" bean="CricketConfiguration.allPlansCategoryId"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="allPlansCategoryCacheEntry" param="dimensionValueCacheEntry" />
	      </dsp:oparam>
		</dsp:droplet>
		
		<!-- Our of Footprint Alert Message -->
		<c:choose>
			<c:when test="${marketType eq 'OOF'}">
				<div class="row">
			      <div class="large-12 small-12 columns no-padding">
			        <div data-alert class="alert-box">
			          Cricket does not currently offer plan add-ons in your area.
			        </div>
			      </div>
			    </div>
			</c:when>
			<c:otherwise>
				<div class="row">
				  <div class="large-12 small-12 columns no-padding">				    
			    		<c:if test="${phone eq '0' && plan eq '0' }">
			    			<div data-alert class="alert-box">
			    				Please <a href="${contextpath}<c:out value='${allPhonesCategoryCacheEntry.url}'/>" class="no-drawer">select a phone</a> and 
			    				<a href="${contextpath}<c:out value='${allPlansCategoryCacheEntry.url}'/>" class="no-drawer">select a plan</a> to shop for add-ons.
			    			</div>
			    		</c:if>
			    		<c:if test="${plan lt phone && addon eq '0'}">
			    			<div data-alert class="alert-box">
			    				Please <a href="${contextpath}<c:out value='${allPlansCategoryCacheEntry.url}'/>" class="no-drawer">select a plan</a> to shop for add-ons.
			    			</div>
			    		</c:if>
			    		<c:if test="${phone lt plan && addon eq '0'}">
			    			<div data-alert class="alert-box">
			    				Please <a href="${contextpath}<c:out value='${allPhonesCategoryCacheEntry.url}'/>" class="no-drawer">select a phone</a> to shop for add-ons.
			    			</div>
			    		</c:if>		    	
				 	 </div>
				</div>
			</c:otherwise>
		</c:choose>
</dsp:page>