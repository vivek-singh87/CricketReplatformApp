<dsp:page>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
<dsp:importbean bean="/com/cricket/endeca/droplet/PopulatePhoneBrandsList"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<input type="hidden" value="${contextpath}" id="contextPathValue"/>
<input type="hidden" value="viewAll" id="accessorySection"/>
	<dsp:droplet name="DimensionValueCacheDroplet">
		<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="allPhonesCategoryCacheEntry" param="dimensionValueCacheEntry" />
      </dsp:oparam>
	</dsp:droplet>
	<dsp:droplet name="DimensionValueCacheDroplet">
		<dsp:param name="repositoryId" bean="CricketConfiguration.accessoriesCategoryId"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="accessoriesCategoryCacheEntry" param="dimensionValueCacheEntry" />
      </dsp:oparam>
	</dsp:droplet>
	<dsp:droplet name="PopulatePhoneBrandsList">
		<dsp:param name="queryParam" value="${allPhonesCategoryCacheEntry.url}"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="brandMap" param="brandMap" scope="request"/>
		</dsp:oparam>
	</dsp:droplet>
	<div class="row">
		<div class="large-12 small-12 columns subnav">
			<h2>Check Out Our Phone Accessories</h2>
			<dl class="sub-nav">
				<span onclick="javascript:showAllAccessories('${contextpath}${accessoriesCategoryCacheEntry.url}');">
					<dd class="active"><a href="javascript:void();">View All Accessories</a></dd>
				</span>
				<span onclick="showPhoneBrands();">
					<dd><a href="javascript:void();">View Accessories By Phone</a></dd>
				</span>
			</dl>
		</div>
	</div>
	<div id="viewAccessoriesByPhone" class="row bydevice hide">
		<div class="large-12 small-12 columns">
			<dl class="dropdown-selectors">
				<dd class="active"><span>Phone Brand</span>
					<select name="mydropdown" id="" onchange="javascript:populatePhonesDropDown(this.value);">
						<option value="Select-Brand">Select-Brand</option>
						<dsp:droplet name="ForEach">
							<dsp:param name="array" value="${brandMap}"/>
							<dsp:param name="elementName" value="brandItem"/>
							<dsp:oparam name="output">
								<dsp:getvalueof var="brand" param="key"/>
								<option value="${brand}">${brand}</option>
							</dsp:oparam>
						</dsp:droplet>
					</select>
				</dd>
				<dd><span>Phone Model</span>
					<select name="mydropdown" id="selectPhoneDropDown" onchange="javascript:showAssociatedAccessories(this.value);">
						<option value="Apple-iPhone-5">Select-Brand-First</option>
					</select>
				</dd>
			</dl>
		</div>
	</div>
	<dsp:droplet name="ForEach">
		<dsp:param name="array" value="${brandMap}"/>
		<dsp:param name="elementName" value="brandItem"/>
		<dsp:oparam name="output">
			<dsp:getvalueof var="brand" param="key"/>
			<dsp:droplet name="ForEach">
				<dsp:param name="array" param="brandItem"/>
				<dsp:param name="elementName" value="productInfo"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="count" param="count"/>
					<dsp:getvalueof var="size" param="size"/>
					<c:if test="${count eq 1}">
						<input type="hidden" id="brandSize${brand}" value="${size}"/>
					</c:if>
					<dsp:getvalueof var="productId" param="key"/>
					<dsp:getvalueof var="productName" param="productInfo"/>
					<input style="display:none" type="hidden" id="idName${brand}${count}" value="${productId}||${productName}"/>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
	</dsp:droplet>
	<input id="selectedPhoneForAccessory" value="" type="hidden"/>
</dsp:page>