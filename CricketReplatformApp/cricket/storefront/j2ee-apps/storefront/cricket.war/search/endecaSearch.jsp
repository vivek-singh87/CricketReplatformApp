<dsp:page>
	<dsp:importbean bean="/atg/endeca/assembler/SearchFormHandler"/>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<script src="${contextPath}/js/vendor/jquery-1.9.0.min.js"></script>
	<script>
		$(function(){
			//alert(window.location.href);
			var url = window.location.href;
			var urlParts = new Array();
			urlParts = url.split("?");
			var queryString = urlParts[1];
			var params = new Array();
			params = queryString.split("&");
			if(params.length > 0) {
				for(i=0; i<params.length; i++){
					var pairs = new Array();
					pairs = params[i].split("=");
					if(pairs.length > 1) {
						var key = pairs[0];
						var value = pairs[1];
						if(key == "q") {
							$('#search-legacy').val(value);
						}
					} else {
						//invalid url formation 
					}
				}
			}
			$('#atg_store_searchSubmit').trigger('click');
		});
	</script>
	
	<dsp:form action="${contextPath}/browse" id="searchForm" requiresSessionConfirmation="false" style="height:auto">
		<input id="dySearchForm" type="hidden" name="Dy" value="1"/>
		<input id="nrPPSearchForm" type="hidden" name="Nrpp" value="10000"/>
		<input id="ntySearchForm" type="hidden" name="Nty" value="1"/>
        <input type="hidden" name="Ntt" id="search-legacy" class="search-new" placeholder="Search" autocomplete="off">
        <dsp:input type="hidden" id="siteIdsSearchForm" value="${currentSite.id}" bean="SearchFormHandler.siteIds" name="siteIds"/>
        <dsp:input style="display:none" type="submit" bean="SearchFormHandler.search" name="search" 
          value="submit" id="atg_store_searchSubmit" title="submit" iclass="btn-search"/>
	</dsp:form>
</dsp:page>