<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:importbean bean="/com/cricket/commerce/order/droplet/DisplayShoppingCartDroplet"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/atg/commerce/catalog/ProductLookup"/>
<dsp:importbean bean="/atg/commerce/order/purchase/CartModifierFormHandler" />
<dsp:getvalueof var="originatingRequestURL" bean="/OriginatingRequest.requestURI"/>
<dsp:importbean bean="/com/cricket/imageurl/ImageConfiguration" var="ImageConfiguration"/>
<c:set var="retailPrice" value="0"/>
<dsp:getvalueof var="accessory" param="accessory"/>
		<!--Start ForEach Droplet -->
		<dsp:droplet name="ForEach">
			<dsp:param name="array" param="accessory"/>
			<dsp:oparam name="output">
				
				<dsp:getvalueof var="size" param="size"/>
				<dsp:getvalueof var="count" param="count"/>		
				<!--Start ProductLookup Droplet -->
				<dsp:droplet name="ProductLookup">
					<dsp:param name="id" param="element.auxiliaryData.productId"/>
					<dsp:param name="filterBySite" value="false"/>
					<dsp:param name="filterByCatalog" value="false"/>
					<dsp:param name="elementName" value="productAccessory"/>
					<dsp:oparam name="output">
						<dsp:getvalueof var="productAccessory" param="productAccessory"/>													
				   
							<div class="accessory-item columns large-7 small-12">
								<dsp:getvalueof var="imageURL" value="${productAccessory.fullImage.url}"/>
								<c:if test="${fn:length(imageURL) > 0}">				
									<!--Start ImageURLLookupDroplet used to show images from Liquid Pixel Server -->
									<dsp:getvalueof var="height" bean="ImageConfiguration.height.checkoutThumbnailImage" />		
									<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
										<dsp:param name="imageLink" value="${imageURL}" />
										<dsp:param name="imageHeight" value="${height}" />
										<dsp:param name="protocalType" value="secure"/>
										<dsp:oparam name="output">
										<dsp:getvalueof var="liquidpixelurl" param="url"/>
											<dsp:img src="${liquidpixelurl}" alt="" />
										</dsp:oparam>
									</dsp:droplet>
									<!--End ImageURLLookupDroplet used to show images from Liquid Pixel Server -->
								</c:if>											
									<h4>${productAccessory.displayName}</h4>
									<%-- <dsp:droplet name="/atg/commerce/catalog/SKULookup">
										<dsp:param name="id" param="element.catalogRefId"/>
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:droplet name="/atg/commerce/inventory/InventoryLookup">
											   <dsp:param name="itemId" param="sku.id"/>
												<dsp:oparam name="output">
													<dsp:droplet name="/com/cricket/browse/ThresholdMessageDroplet">
														<dsp:param name="threshold" param="threshold"/>
														<dsp:param name="stockLevel" param="inventoryInfo.stockLevel"/>
														<dsp:param name="thresholds" param="sku.thresholds"/> 
														<dsp:oparam name="output">
															<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
															<c:if test="${!empty thresholdMessage}">
																<p class="disclaimer">*${thresholdMessage}</p>
															</c:if>	
														</dsp:oparam>
													 </dsp:droplet>
												</dsp:oparam>
											</dsp:droplet>
										</dsp:oparam>
									</dsp:droplet> --%>
									<dsp:droplet name="/com/cricket/browse/ThresholdMessageDroplet">
										<dsp:param name="skuId" param="element.catalogRefId"/>
										<dsp:param name="order" bean="/atg/commerce/ShoppingCart.current"/> 
										<dsp:param name="flow" value="checkout"/>
										<dsp:oparam name="output">
											<dsp:getvalueof var="thresholdMessage" param="thresholdMessage"/>
											<dsp:getvalueof var="isOutOfStock" param="isOutOfStock"/>
											<c:if test="${!empty thresholdMessage}">
												<p class="disclaimer">*${thresholdMessage}</p>
											</c:if>
										</dsp:oparam>
									</dsp:droplet>
									</div>
									<dsp:getvalueof var="CommerceItemId" param="element.id">
									</dsp:getvalueof>
					</dsp:oparam>
				</dsp:droplet>									
							
					<div class="accessory-item columns large-5 small-12">
					    <table cellspacing="0" cellpadding="0" border="0">
                          <tbody>
                            <tr>
                              <th><crs:outMessage key="cricket_checkout_price"/><!-- Price --></th>
                              <th><crs:outMessage key="cricket_checkout_quantity"/><!-- Quantity --></th>
                            </tr>
                            <tr>
								<td>
								  <dsp:include page="/browse/includes/priceLookup.jsp">
										<dsp:param name="productId" param="element.auxiliaryData.productId"/>
										<dsp:param name="skuId" param="element.catalogRefId"/>
									</dsp:include>								
									<dsp:droplet name="/atg/commerce/catalog/SKULookup">
										<dsp:param name="id" param="element.catalogRefId"/>
										<dsp:param name="filterBySite" value="false" />
										<dsp:param name="filterByCatalog" value="false" />
										<dsp:param name="elementName" value="sku"/>	
										<dsp:oparam name="output">
											<dsp:getvalueof var="retailPrice" param="sku.listPrice"/>									
										</dsp:oparam>
									</dsp:droplet>
								$${retailPrice }
								</td>
                              <td>
									<c:set var="updateAccessoryId">
									<dsp:valueof param="element.id"/>
									</c:set>
									<c:set var="quantity">
										<dsp:valueof param="element.quantity"/>
									</c:set>
									${quantity}
							  </td>
                            </tr>
                          </tbody>
                        </table>	
					</div>
				<c:if test="${count < size }">
					<hr/>
				</c:if>			
			</dsp:oparam>
		</dsp:droplet>
		<!--End ForEach Droplet -->

</dsp:page>