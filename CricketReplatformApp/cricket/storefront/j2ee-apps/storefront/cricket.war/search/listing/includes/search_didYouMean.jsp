<dsp:page>
	<dsp:getvalueof var="contextPath" bean="/OriginatingRequest.contextPath"/>
	<dsp:importbean bean="/com/cricket/search/session/UserSearchSessionInfo"/>
	<script>
		function loadDidYouMean() {
			var contextRoot = $('#contextRootJS').val();
			var searchKeyWord = $('#searchKeyWordJS').val();
			var genSearchQuery = $('#genSearchQueryJS').val();
			var url = contextRoot + "/assembler?Dy=1&assemblerContentCollection=/content/Shared/Did You Mean&format=json&Ntt=" + searchKeyWord;
			var htmlSearchSuggest = "";
			$.get(url, function(data) {
				var autoSuggestTerms = data.contents[0];
				if(autoSuggestTerms['@type'] == 'SearchAdjustments') {
					var adjustedSearches = autoSuggestTerms.suggestedSearches;
					if (adjustedSearches != null) {
						var adjustedTerms = adjustedSearches[searchKeyWord];
						for(var j = 0; j < adjustedTerms.length; j++) {
							var term = adjustedTerms[j];
							var navAction = term.navigationState;
							navAction = navAction.replace("&format=json","");
							htmlSearchSuggest += '<li><a href="' + contextRoot + '/browse/' + navAction + '">';
							htmlSearchSuggest += term.label;
							htmlSearchSuggest += '</a></li>';
						}
					}
				}
				if(htmlSearchSuggest != "") {
					$('#searchSuggestTerms').html(htmlSearchSuggest);
					$('#didYouMeanSection').show();
				} else {
					$('#similar-phones').show();
					$('#phone-plans').show();
				}
			});
			
		}	
		$(function(){
			loadDidYouMean();
		});
	</script>
	<dsp:getvalueof var="searchTerm" param="Ntt"/>
	<dsp:getvalueof var="genSearchQuery" bean="UserSearchSessionInfo.genTextBoxSearchQuery"/>
	<input type="hidden" id="contextRootJS" value="${contextPath}"/>
	<input type="hidden" id="searchKeyWordJS" value="${searchTerm}"/>
	<input type="hidden" id="genSearchQueryJS" value="${genSearchQuery}"/>
	<div class="row">
		<div class="columns large-12 small-12">
			<p class="sorry">Sorry! No results were found.</p>
		</div>
	</div>
	<div class="row">
		<div class="columns large-12 small-12" id="didYouMeanSection" style="display:none">
			<h3>Did you Mean:</h3>
			<ul class="search-recommendations" id="searchSuggestTerms">
			</ul>
		</div>
	</div>
</dsp:page>