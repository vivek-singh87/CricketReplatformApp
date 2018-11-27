<dsp:page>
<dsp:importbean bean="/atg/endeca/assembler/SearchFormHandler"/>
<dsp:importbean bean="/atg/multisite/Site" var="currentSite"/>
<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>

<script>
$(function(){
	$("#mobileSearchText").keyup(function() {
		var searchTerm = $(this).val();
		// After three keystrokes, show the results dropdown
		if($(this).val().length > 2) {
			populateTypeAheadResultsMobile();
			//$(".results-dropdown").show();
		}
		else {
			$(".results-dropdown").hide();
			$(".acLoading").hide();
		}		
	});
});
function submitMobileSearchForm() {
	var searchTerm = $("#mobileSearchText").val();
	$("#searchTextMobile").val(searchTerm);
	$('#mobile_searchSubmit').trigger('click');
}
</script>

<dsp:form action="${contextPath}/browse" id="searchFormMobile" requiresSessionConfirmation="false">
	<input id="dySearchFormMobile" type="hidden" name="Dy" value="1"/>
	<input id="nrPPSearchFormMobile" type="hidden" name="Nrpp" value="10000"/>
	<input id="ntySearchFormMobile" type="hidden" name="Nty" value="1"/>
	<input type="hidden" name="Ntt" id="searchTextMobile"/>
	<dsp:input type="hidden" id="siteIdsSearchFormMobile" value="${currentSite.id}" bean="SearchFormHandler.siteIds" name="siteIds"/>
	<dsp:input style="display:none" type="submit" bean="SearchFormHandler.search" name="search" 
         value="submit" id="mobile_searchSubmit" title="submit" iclass="btn-search"/>
</dsp:form>
<input type="hidden" value="${contextPath}" id="contextPathSearchFormMobile"/>

<li id="li-mobile-search">					  
	<div class="row first collapse">
		<div class="small-8 columns small-offset-1">
			<div id="search-input">
			  <!-- Predictive search using autocomplete plugin -->
			  <input type="text" id="mobileSearchText" name="search-new" class="search-new-mobile" placeholder="Search">
			</div>
		</div>
		<div class="small-3 columns left">
		  <a class="prefix button orange-button" href="javascript:void();" onclick="javascript:submitMobileSearchForm();">Search</a>
		</div>				
	</div>
	<!-- This needs to be here for mobile results (using autocomplete plugin) to append to -->
	<div class="acLoading" style="margin-top:5px"></div>
	<div id="mobile-search-results">
	</div>						
</li>
</dsp:page>