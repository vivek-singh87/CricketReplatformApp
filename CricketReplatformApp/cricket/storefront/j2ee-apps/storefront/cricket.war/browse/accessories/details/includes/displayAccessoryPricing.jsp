<dsp:page>
<!-- Displays the Price, Promotions and Final Price for a selected sku -->
	<div class="final-price">
          <p class="left"><%-- <crs:outMessage key="cricket_accessoridetails_FinalPrice"/> --%>Final Price: </p>
          <p class="right text-right">
            <span class="green-price">
            		<dsp:include page="/browse/includes/priceLookup.jsp">
							<dsp:param name="skuId" param="sku.Id" />
					</dsp:include>
					<c:if test="${empty retailPrice || retailPrice eq 0}">
						<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>
					</c:if>		
					<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${retailPrice}" var="retailPrice" />
					<c:set var="splitPrice" value="${fn:split(retailPrice, '.')}"/>
					<sup>$</sup>${splitPrice[0]}<sup>${splitPrice[1]}</sup>
			</span>
          </p>
     </div>
</dsp:page>