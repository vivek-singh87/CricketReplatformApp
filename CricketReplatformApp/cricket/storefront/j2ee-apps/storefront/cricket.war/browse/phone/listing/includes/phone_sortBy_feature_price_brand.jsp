<dsp:page>
	<script>
	//phoneProductId=" + selectedPhoneId + "&intention=BUY_ASSOC_ACCESSORY&CATEGORY_TYPE=ACCESSORY"
		function submitSortForm(sortParam, spanId) {
			populateSortHiddenField(spanId);
			manageSpanCheckBoxes(spanId);
			var selectedPhoneId = $('#selectedPhoneForAccessory').val();
			if(selectedPhoneId != null && selectedPhoneId != '') {
				var contextPath = $('#contextPathValue').val();
				$('#selectedPhoneForAccessory').val(selectedPhoneId);
				var mask = createMask();
				$("#acLoadingCustom").show();
				var url = contextPath + "/browse/gadgets/categoryChildProducts.jsp?phoneProductId=" + selectedPhoneId + "&sort=" + sortParam + "&intention=BUY_ASSOC_ACCESSORY&CATEGORY_TYPE=ACCESSORY";
				$.get(url, function(data) {
					document.getElementById('mainEndecaContent').innerHTML = data;
					$("#acLoadingCustom").hide();
					mask.remove();
					restoreLostBindings();
					removeDisplayNone();
				});
			} else {
				var urlWithParams = document.URL;
				if(urlWithParams.indexOf("sort=") != -1) {
					var splitUrl = urlWithParams.split("?");
					var paramPairs = splitUrl[1].split("&");
					var allParams = "";
					for(i=0; i<paramPairs.length; i++) {
						var keyValue = paramPairs[i].split("=");
						if(keyValue[0] != "sort") {
							if(i == 0) {
								allParams += "?" + keyValue[0] + "=" + keyValue[1];
							} else {
								allParams += "&" + keyValue[0] + "=" + keyValue[1];
							}
						} else {
							if(i == 0) {
								allParams += "?sort=" + sortParam;
							} else {
								allParams += "&sort=" + sortParam;
							}
						}
					}
					var sortAction = splitUrl[0] + allParams;
				} else {
					if(urlWithParams.indexOf("?") != -1) {
						sortAction = urlWithParams + "&sort=" + sortParam;
					} else {
						sortAction = urlWithParams + "?sort=" + sortParam;
					}
				}
				//window.location.href = sortAction;
				hitEndecaWithFilterQuery(sortAction);
			}
		}
		
		function submitSortFormSearch(sortAction, spanId) {
			populateSortHiddenField(spanId);
			manageSpanCheckBoxes(spanId);
			var accessorySection = $('#accessorySection').val();
			if(accessorySection == 'byPhone') {
				if(spanId == 'sortby2Span'){
					submitSortForm("price:ascending",'sortby5Span');
				}
			} else {
				var windowPath = window.location.pathname;
				hitEndecaWithFilterQuery(windowPath + sortAction);
			}
			
		}
		
		function manageSpanCheckBoxes(spanId) {
			$(".customCheckedRadio").each(function() {
				$( this ).removeClass("customCheckedRadio");
				$( this ).addClass("customUnchecked customUncheckedRadio");
			});
			$("#" + spanId).addClass("customCheckedRadio");
		}
		
		function populateSortHiddenField(spanId) {
			if(spanId == 'sortby1Span' || spanId == 'sortby4Span') {
				$('#sortOption').val("featured");
			} else if(spanId == 'sortby2Span' || spanId == 'sortby5Span') {
				$('#sortOption').val("price");
			} else if(spanId == 'sortby3Span' || spanId == 'sortby6Span') {
				$('#sortOption').val("brand");
			}
		}
	</script>
	<dsp:getvalueof var="contentItem" param="contentItem"/>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="sortParam" param="Ns"/>
	<dsp:getvalueof var="sortParamAtg" param="sort"/>
	<dsp:getvalueof var="pageTypeFilter" param="PAGE_TYPE_FILTER"/>
	<dsp:getvalueof var="activeLink" param="activeLink"/>
	<c:if test="${activeLink eq 'Accessories'}">
		<dsp:getvalueof var="pageTypeFilter" value="ACCESSORY"/>
	</c:if>
	<c:choose>
		<c:when test="${searchCartridgeInvoked eq 'invoked'}">
			<li class="has-form">
				<dsp:form iclass="custom2 phone-radios" id="sortByForm" action="">
					<div class="row collapse">
						<div class="columns">
							<span class="sort-label">Sort By:</span>
							<c:forEach var="sortOption" items="${contentItem.contents[0].sortOptions}">
								<dsp:getvalueof var="selectedClass" value=""/>
								<c:choose>
									<c:when test="${sortOption.label eq 'common.topPicks'}">
										<label for="sortby1">
											<input name="sort" type="radio" id="sortby1" style="display:none;" value="">
											<c:choose>
												<c:when test="${empty sortParam}">
													<dsp:getvalueof var="selectedClass" value="customCheckedRadio"/>
												</c:when>
												<c:otherwise>
													<dsp:getvalueof var="selectedClass" value="customUnchecked customUncheckedRadio"/>
												</c:otherwise>
											</c:choose>
											<span id="sortby1Span" class="${selectedClass}" onclick="javascript:submitSortFormSearch('${sortOption.navigationState}','sortby1Span');"></span> Featured
											<dsp:getvalueof var="selectedClass" value=""/>
							            </label>
									</c:when>
									<c:when test="${sortOption.label eq 'sort.priceLH'}">
										<label for="sortby2">
											<c:choose>
												<c:when test="${sortParam eq 'sku.listPrice|0'}">
													<dsp:getvalueof var="selectedClass" value="customCheckedRadio"/>
												</c:when>
												<c:otherwise>
													<dsp:getvalueof var="selectedClass" value="customUnchecked customUncheckedRadio"/>
												</c:otherwise>
											</c:choose>
											<span id="sortby2Span" class="${selectedClass}" onclick="javascript:submitSortFormSearch('${sortOption.navigationState}','sortby2Span');"></span> Price
											<dsp:getvalueof var="selectedClass" value=""/>
							            </label>
									</c:when>
									<c:when test="${sortOption.label eq 'sort.nameAZ'}">
										<c:if test="${pageTypeFilter ne 'ACCESSORY'}">
											<label for="sortby3">
												<c:choose>
												<c:when test="${sortParam eq 'product.manufacturer.displayName|0'}">
													<dsp:getvalueof var="selectedClass" value="customCheckedRadio"/>
												</c:when>
												<c:otherwise>
													<dsp:getvalueof var="selectedClass" value="customUnchecked customUncheckedRadio"/>
												</c:otherwise>
											</c:choose>
												<span id="sortby3Span" class="${selectedClass}" onclick="javascript:submitSortFormSearch('${sortOption.navigationState}','sortby3Span');"></span> Brand
												<dsp:getvalueof var="selectedClass" value=""/>
								            </label>
							            </c:if>
									</c:when>
								</c:choose>
								
							</c:forEach>
						</div>
					</div>
				</dsp:form>
			</li>
		</c:when>
		<c:otherwise>
			<dsp:getvalueof var="atgSortParam" param="sort"/>
			<c:choose>
				<c:when test="${CATEGORY_TYPE eq 'ACCESSORY' && searchCartridgeInvoked ne 'invoked' && (empty sortParamAtg || sortParamAtg eq 'brand:ascending')}">
					<dsp:getvalueof var="showFeatured" value="yes"/>
				</c:when>
			</c:choose>
			<li class="has-form">
		      <form class="custom2 phone-radios" id="sortByForm">
		        <div class="row collapse">
		          <div class="columns">
		            <span class="sort-label">Sort By:</span>
		            <label for="sortby4">
		              <input name="sortby4" type="radio" id="sortby4" style="display:none;" value="">
		              	<c:choose>
		              		<c:when test="${showFeatured eq 'yes'}">
		              			<dsp:getvalueof var="atgSelectedClass" value="customCheckedRadio"/>
		              		</c:when>
		              		<c:otherwise>
		              			<c:choose>
									<c:when test="${empty atgSortParam}">
										<dsp:getvalueof var="atgSelectedClass" value="customCheckedRadio"/>
									</c:when>
									<c:otherwise>
										<dsp:getvalueof var="atgSelectedClass" value="customUnchecked customUncheckedRadio"/>
									</c:otherwise>
								</c:choose>
		              		</c:otherwise>
		              	</c:choose>
		              <span id="sortby4Span" class="${atgSelectedClass}" onclick="javascript:submitSortForm('','sortby4Span');"></span> Featured
		            </label>
		            <label for="sortby5">
						<input name="sortby5" type="radio" id="sortby5" value="price:descending" style="display:none;">
						<c:choose>
							<c:when test="${atgSortParam eq 'price:ascending'}">
								<dsp:getvalueof var="atgSelectedClass" value="customCheckedRadio"/>
							</c:when>
							<c:otherwise>
								<dsp:getvalueof var="atgSelectedClass" value="customUnchecked customUncheckedRadio"/>
							</c:otherwise>
						</c:choose>
		              	<span id="sortby5Span" class="${atgSelectedClass}" onclick="javascript:submitSortForm('price:ascending','sortby5Span');"></span> Price
		            </label>
		            <c:if test="${pageTypeFilter ne 'ACCESSORY'}">
			            <label for="sortby6">
							<input name="sortby6" type="radio" id="sortby6" style="display:none;">
							<c:choose>
								<c:when test="${atgSortParam eq 'brand:ascending'}">
									<dsp:getvalueof var="atgSelectedClass" value="customCheckedRadio"/>
								</c:when>
								<c:otherwise>
									<dsp:getvalueof var="atgSelectedClass" value="customUnchecked customUncheckedRadio"/>
								</c:otherwise>
							</c:choose>
							<span id="sortby6Span" class="${atgSelectedClass}" onclick="javascript:submitSortForm('brand:ascending','sortby6Span');"></span> Brand
			            </label>
		            </c:if>
		          </div>
		        </div>
		      </form>
		    </li>
		    <input type="hidden" value="${contentItem.categoryAction}" id="sortActionPath"/>
		    <input type="hidden" value="" id="sortSelection"/>
		</c:otherwise>
	</c:choose>
</dsp:page>