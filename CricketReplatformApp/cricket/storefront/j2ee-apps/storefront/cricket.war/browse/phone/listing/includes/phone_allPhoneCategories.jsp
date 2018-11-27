<dsp:page>
	<script>
		function loadOtherCategory(otherCatUrl) {
			document.location.href=otherCatUrl;
		}
	</script>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:importbean bean="/com/cricket/browse/DetermineActiveCategory"/>
	<dsp:importbean bean="/atg/commerce/catalog/CategoryLookup"/>
	<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
	<dsp:importbean bean="/atg/dynamo/droplet/ForEach"/>
	<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="defaultSortParam" bean="CricketConfiguration.defaultSortParam"/>
	<div class="row">
	    <div class="large-12 small-12 columns subnav">
	      <h2>The Phones You Want</h2>
	      <dl class="sub-nav hide-for-small">
	      	<dsp:droplet name="CategoryLookup">
				<dsp:param name="id" bean="CricketConfiguration.phonesCategoryId"/>
				<dsp:oparam name="output">
					<dsp:droplet name="ForEach">
						<dsp:param name="array" param="element.childCategories"/>
						<dsp:param name="elementName" value="childCategory"/>
						<dsp:oparam name="output">
							<dsp:droplet name="DimensionValueCacheDroplet">
								<dsp:param name="repositoryId" param="childCategory.id"/>
								<dsp:oparam name="output">
									<dsp:getvalueof var="childCategoryCacheEntry" param="dimensionValueCacheEntry" />
									<dsp:droplet name="DetermineActiveCategory">
										<dsp:param name="cacheEntry" value="${childCategoryCacheEntry}"/>
										<dsp:param name="navParam" param="N"/>
										<dsp:param name="categoryId" param="childCategory.id"/>
										<dsp:oparam name="active">
											<dd class="active">
												<dsp:getvalueof var="childCategoryDisplayName" param="childCategory.displayName" />
									        	<dsp:a href="javascript:void();">${childCategoryDisplayName}</dsp:a>
									        </dd>
										</dsp:oparam>
										<dsp:oparam name="inactive">
											<dd>
												<dsp:getvalueof var="childCategoryDisplayName" param="childCategory.displayName" />
												<dsp:getvalueof var="editPhone" param="editPhone" />
												<c:choose>
													<c:when test="${!empty editPhone}">
														<dsp:a href="${contextpath}${childCategoryCacheEntry.url}&editPhone=${editPhone}&sort=${defaultSortParam}">${childCategoryDisplayName}</dsp:a>
													</c:when>
													<c:otherwise>
													   	<dsp:a href="${contextpath}${childCategoryCacheEntry.url}&sort=${defaultSortParam}">${childCategoryDisplayName}</dsp:a>
													</c:otherwise>
												</c:choose>
									        </dd>
										</dsp:oparam>
										<dsp:oparam name="noShow">
										</dsp:oparam>
									</dsp:droplet>
						      	</dsp:oparam>
							</dsp:droplet>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
	      </dl>
	    </div>
	  </div>
</dsp:page>