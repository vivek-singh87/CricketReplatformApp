<dsp:page>
<dsp:importbean bean="/atg/cricket/util/CricketProfile"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayUserAddonsForRemoval"/>
<dsp:getvalueof var="userPurchasedAddonProducts" bean="CricketProfile.userPurchasedOfferingProducts"></dsp:getvalueof>
<dsp:droplet name="DisplayUserAddonsForRemoval">
<dsp:param name="array" value="${userPurchasedAddonProducts}"/>
<dsp:param name="order" bean="ShoppingCart.current"/>
<dsp:oparam name="output">
	<dsp:getvalueof var="userAddonsForDisplay" param="userAddonsForDisplay"/>
</dsp:oparam>
</dsp:droplet>
 

	<div class="row">
		<div class="column large-12 small-12 panel">
	      <div class="row">
		        <div class="column large-12 small-12">
		        <dsp:getvalueof var="mdn" bean="CricketProfile.mdn"></dsp:getvalueof>
		        <c:set var="str1" value="${fn:substring(mdn, 0, 3)}"/>
			    <c:set var="str2" value="${fn:substring(mdn, 3, 6)}"/>
			    <c:set var="str3" value="${fn:substring(mdn, 6, 10)}"/>
		       <h2>YOUR CURRENT ADD-ONS FOR <span>${str1}-${str2}-${str3}</span></h2>
		          <p>Your current add-ons are listed here.<br />
		          Select to remove an add-on, or add a new add-on to this number.</p>
		  <c:if test="${!empty userAddonsForDisplay}">    		
		      	<!-- Desktop -->
		          <div class="hide-for-small">
		          		<dsp:droplet name="ForEach">
						<dsp:param name="array" value="${userAddonsForDisplay}"/>
							<dsp:oparam name="output">
							
								<dsp:droplet name="ProductLookup">
									<dsp:param name="id" param="element"/>
									<dsp:param name="filterBySite" value="false"/>
			       					<dsp:param name="filterByCatalog" value="false"/>
									<dsp:param name="elementName" value="product"/>
										<dsp:oparam name="output">
										<dsp:getvalueof var="productId" param="product.repositoryId"/>
												<div class="plan-item">
									              <h3><dsp:valueof param="product.briefDescription"/></h3>
									              <p><dsp:valueof param="product.displayName"/></p>
									            <dsp:droplet name="/com/cricket/browse/droplet/IsMandatoryAddonDroplet">
													<dsp:param name="AddonId" value="${productId}" />
													<dsp:oparam name="output">
													<dsp:getvalueof var="IsMandatory" param="IsMandatory"/>
													</dsp:oparam>
												</dsp:droplet>
												<c:if test="${IsMandatory eq 'false'}">
												 <a href="javascript:void();" class="remove" onclick="submitFormAddToCart('removecommerceItem${productId}');">Remove</a>
												 <dsp:form name="removecommerceItem${productId}" id="removecommerceItem${productId}" formid="removecommerceItem${productId}" style="display:none">
													<dsp:input type="hidden" bean="CartModifierFormHandler.productId" value="${productId}"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.catalogRefIds"  paramvalue="product.childSkus[0].id"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.quantity" value="1"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.addAsRemovedCommerceItem" value="true"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.addItemToOrderSuccessURL" value="${contextpath}/cart/common/json/addToCartResponse.jsp"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.addItemToOrderErrorURL" value="${contextpath}/cart/common/json/addToCartResponse.jsp"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.addItemToOrder" value="submit"/>
												   </dsp:form>
												</c:if>
									            </div>
										</dsp:oparam>
								</dsp:droplet>
								
							</dsp:oparam>
						</dsp:droplet>
		          </div>
		      
		          <!-- Mobile Swiper Content -->
		          <div class="columns small-12 text-center three-callouts show-for-small">    
		            <a href="#" class="prev show-for-small">Prev</a>
		            <a href="#" class="next show-for-small">Next</a>
		            <div class="swiper-container show-for-small"> 
		              <div class="swiper-wrapper">
		              
	              		<dsp:droplet name="ForEach">
						<dsp:param name="array" value="${userAddonsForDisplay}"/>
							<dsp:oparam name="output">
							
								<dsp:droplet name="ProductLookup">
									<dsp:param name="id" param="element"/>
									<dsp:param name="filterBySite" value="false"/>
			       					<dsp:param name="filterByCatalog" value="false"/>
									<dsp:param name="elementName" value="product"/>
										<dsp:oparam name="output">
										<dsp:getvalueof var="productId" param="product.repositoryId"/>
												<div class="swiper-slide plan-item">
									              <h3><dsp:valueof param="product.briefDescription"/></h3>
									              <p><dsp:valueof param="product.displayName"/></p>
									              <a href="javascript:void();" class="remove" onclick="submitFormAddToCart('removecommerceItem${productId}');">Remove</a>
									              <dsp:form name="removecommerceItem${productId}" id="removecommerceItem${productId}" formid="removecommerceItem${productId}" style="display:none">
													<dsp:input type="hidden" bean="CartModifierFormHandler.productId" value="${productId}"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.catalogRefIds"  paramvalue="product.childSkus[0].id"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.quantity" value="1"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.addAsRemovedCommerceItem" value="true"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.addItemToOrderSuccessURL" value="${contextpath}/cart/common/json/addToCartResponse.jsp"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.addItemToOrderErrorURL" value="${contextpath}/cart/common/json/addToCartResponse.jsp"/>
													<dsp:input type="hidden" bean="CartModifierFormHandler.addItemToOrder" value="submit"/>
												   </dsp:form>
									            </div>
										</dsp:oparam>
								</dsp:droplet>
								
							</dsp:oparam>
						</dsp:droplet>
		            
		              </div>
		            </div>
		          </div>
		          
		        </div>
				</c:if>
	      </div>
	  	</div>
	</div>
	
</dsp:page>