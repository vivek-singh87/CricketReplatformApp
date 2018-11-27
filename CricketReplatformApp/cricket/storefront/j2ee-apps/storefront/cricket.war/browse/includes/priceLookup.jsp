<dsp:page>
<dsp:getvalueof var="skuId" param="skuId"/>
<dsp:getvalueof var="productId" param="productId"/>
<dsp:getvalueof var="listPrice" bean="/atg/userprofiling/Profile.pricelist.id"/>
		<dsp:droplet name="/atg/commerce/pricing/priceLists/PriceDroplet">
		   <dsp:param name="product" value="${productId}" />
		   <dsp:param name="sku" value="${skuId}" />
		   <dsp:param name="priceList" value="${listPrice}" />
		   <dsp:oparam name="output">
		      <dsp:droplet name="/atg/dynamo/droplet/Switch">
		         <dsp:param name="value" param="price.pricingScheme" />
		         <dsp:oparam name="listPrice">
		           <dsp:getvalueof var="retailPrice" param="price.listPrice" scope="request"/>		         	
		         </dsp:oparam>
		      </dsp:droplet>
		   </dsp:oparam>
		</dsp:droplet>
</dsp:page>