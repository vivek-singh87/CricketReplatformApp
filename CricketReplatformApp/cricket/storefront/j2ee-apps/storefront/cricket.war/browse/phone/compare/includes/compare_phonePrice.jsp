<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:getvalueof var="contextpath" param="contextpath"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/com/cricket/browse/droplet/GetSeoStringDroplet"/>

<!-- Check for zip code -->
<dsp:getvalueof var="userLocationZipCode" bean="Profile.userLocationZipCode"/>	
<dsp:getvalueof var="userEnteredZipCode"  bean="CitySessionInfoObject.cityVO.manulaEntry"/>
<c:if test="${userLocationZipCode==null}">
	<tr>
		<td>&nbsp;</td>
		<td colspan="4">
			<div class="row">
				<div class="large-12 small-12 columns">
					<div data-alert class="alert-box">
						<%-- <crs:outMessage key="cricket_phonecompare_please_enter_zipcode"/> --%>
					  Please enter your zip code to add to your cart.
                <a href="#" class="circle-arrow"><%-- <crs:outMessage key="cricket_phonecompare_enter_zipcode"/> --%>Enter Zip code</a>
					</div>
				</div>
			</div>						
		</td>	
	</tr>
</c:if>
<!-- Starting ForEach Droplet1  -->
<dsp:droplet name="ForEach">
	<dsp:param name="array" param="productList" />
	<dsp:oparam name="outputStart">
		<tr>
			<th>&nbsp;</th>
	</dsp:oparam>
	<dsp:oparam name="output">
		<th>
			<dsp:getvalueof var="productId" param="element.product.id"/>
			<dsp:getvalueof var="skuId" param="element.product.childSkus[0].id"/>
			<c:if test="${userLocationZipCode!=null}">
				<p class="price">
					<c:set var="retailPrice" value='0' />
					<dsp:include page="/browse/includes/priceLookup.jsp">
						<dsp:param name="productId" value="${productId}"/>
						<dsp:param name="skuId" value="${skuId}"/>
					</dsp:include>
					<c:if test="${empty retailPrice || retailPrice eq 0}">
					   <dsp:getvalueof var="retailPrice" param="element.product.childSkus[0].listPrice"/>
					</c:if>                   				
					<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>	
					<sup>$</sup>${splitPrice[0]}.<sup>${splitPrice[1]}</sup>	
				</p> 
			</c:if>
			<dsp:droplet name="GetSeoStringDroplet">
				<dsp:param name="product" param="element.product"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="seoString" param="seoString"/>
				</dsp:oparam>
			</dsp:droplet>
			<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
				<dsp:param name="seoString" value="${seoString}"/>
				<dsp:param name="id" value="${productId}"/>
				<dsp:param name="itemDescriptorName" value="phone-product"/>
				<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="canonicalUrl" param="url" vartype="java.lang.String"/>
				</dsp:oparam>
			</dsp:droplet>
			<!-- Changes done as part of defect 5819 - updates from TPN -->
			 <a href="${contextpath}${canonicalUrl}" class="button small orange-shop-now">Shop Now</a>
			 <!-- 
			<c:choose>
				<c:when test="${userEnteredZipCode eq false}">
					<a href="#" class="button small disabled"><%-- <crs:outMessage key="cricket_phonecompare_add_to_cart"/> --%>Add To Cart</a>
				</c:when>
				<c:otherwise>				
					<a href="#" class="button small"><%-- <crs:outMessage key="cricket_phonecompare_add_to_cart"/> --%>Add To Cart</a>
				</c:otherwise>
			</c:choose> -->
		</th>
	</dsp:oparam>
	<dsp:oparam name="outputEnd">
		</tr>
	</dsp:oparam>	
</dsp:droplet>	
<!-- Ending ForEach Droplet1  -->

<!-- Starting ForEach Droplet2  -->
<dsp:droplet name="ForEach">
	<dsp:param name="array" param="productList" />
	<dsp:oparam name="outputStart">
		<tr>
			<th>&nbsp;</th>
	</dsp:oparam>
	<dsp:oparam name="output">
		<dsp:getvalueof var="productId" param="element.product.id"/>		
		<dsp:getvalueof var="skuId" param="element.product.childSkus[0].id"/>
		<th>
			<a href="${contextpath}/browse/phone/phone_details.jsp?productId=${productId}&skuId=${skuId}" class="circle-arrow"><%-- <crs:outMessage key="cricket_phonecompare_learn_more"/> --%>Learn More</a>
		</th>
	</dsp:oparam>
	<dsp:oparam name="outputEnd">
		</tr>
	</dsp:oparam>	
</dsp:droplet>
<!-- Ending ForEach Droplet2  -->
</dsp:page>