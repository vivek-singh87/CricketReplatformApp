
<dsp:page>
<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
<script>
	function submitSearchForm() {
		$('#atg_store_searchSubmit').trigger('click');
	}
	
	function closeTypeAheadSection() {
		$("#predictive-search-zip").hide();
	}
</script>
<script src="${contextpath}/js/search.js"></script>
<div id="drawer-search" class="drawer-utility drawer">
		<div class="drawer-wrapper">		
			<div class="row first collapse">
				<div class="large-10 columns">
					<div id="search-input">
						<dsp:include page="../../search/searchForm.jsp"/>
						<%--<input type="text" name="search" id="search" placeholder="Search">
						<a class="btn-search" href="#">Search</a> --%>
						<!-- Loading Icon for Search-->
						<div class="acLoading"></div>
					</div>
				</div>
				<div class="large-1 columns">
				  <a class="prefix button orange-button" href="javascript:submitSearchForm();">Search</a>
				</div>
				<div class="close-drawer close-search large-1 columns">
					<span onclick="closeTypeAheadSection();"><a href="#">X</a></span>
				</div>				
			</div>	
		</div>	
	</div>	<!--/#drawer-search-->
	<div id="predictive-search-zip" style="display: none;">
		
	</div>
</dsp:page>