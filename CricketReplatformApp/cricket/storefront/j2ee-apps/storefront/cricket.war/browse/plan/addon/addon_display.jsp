<dsp:page>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayAddToCartForAddOnDroplet"/>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/commerce/order/configuration/CartConfiguration"/>
<dsp:getvalueof var="OOFMarketType" bean="CartConfiguration.outOfFootPrintMarketType"></dsp:getvalueof>
<dsp:getvalueof var="marketType" bean="Profile.marketType"></dsp:getvalueof>

<section id="product-details">
<!-- Desktop -->
  <div class="row">
    <div class="columns large-12 small-12 large-centered">
      <div class="row">
        <div id="product-image" class="columns large-5 small-12 text-center hide-for-small">
          <p><dsp:valueof param="product.description"></dsp:valueof></p>
        </div>
        <div id="product-content" class="columns large-5 small-12 content ">
          <h1 class=""><dsp:valueof param="product.displayName"></dsp:valueof></h1>
            <div class="final-price">
              <p class="left">Monthly Price: </p>
              <p class="right text-right">
                <dsp:include page="/browse/includes/priceLookup.jsp">
                	<dsp:param name="skuId" param="product.childSKUs[0].id" />
                	<dsp:param name="productId" param="product.id" />
                </dsp:include>
                <c:if test="${empty retailPrice || retailPrice eq 0}">
					<dsp:getvalueof var="retailPrice" param="product.childSKUs[0].listPrice"/>
				</c:if>
				<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice}" var="retailPrice" />
                <c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>
                <span class="green-price"><sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup></span>
              </p>
          </div>
		
		<dsp:getvalueof var="manuallyEnteredZipCode" bean="CitySessionInfoObject.cityVO.manulaEntry"/>
		<c:choose>
			<c:when test="${OOFMarketType eq marketType}">
				<a class="grey-add-cart button full-width-btn right disabled secondary">Add to Cart</a>
			</c:when>
	  		<c:when test="${manuallyEnteredZipCode eq true}">
				<div>
					<dsp:include page="/cart/common/addToCart.jsp">
						<dsp:param name="addToCartClass" value="button full-width-btn right"/>
						<dsp:param name="productId" param="product.id"/>
						<dsp:param name="skuId" param="product.childSKUs[0].id"/>
						<dsp:param name="quantity" value="1"/>
						<dsp:param name="addOnValue" value="true"/>
						<dsp:param name="url" bean="/OriginatingRequest.requestURIWithQueryString"/>
					</dsp:include>					
          		</div> 
			</c:when>
			<c:when test="${manuallyEnteredZipCode eq false}">
				<a class="grey-add-cart button full-width-btn right disabled secondary has-tip" data-tooltip="" title="Please enter your zip code to continue shopping.">Add to Cart</a>
			</c:when>
		</c:choose>
		
        </div>
      </div>
    </div>
  </div>
  <!--This is the included page in plan addon detail page to display the name, images, price add to cart button should be disable   -->
  <dsp:include page="/browse/plan/addon/addon_details_messaging.jsp"></dsp:include>
</section>
</dsp:page>