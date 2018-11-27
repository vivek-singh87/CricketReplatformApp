var html = "";
var contextPath = $("#contextPathHead").val();
function populateTypeAheadResultsMobile() {
	var keyWord = $("#mobileSearchText").val();
	var url = contextPath + "/assembler?Dy=1&assemblerContentCollection=/content/Shared/Auto-Suggest%20Panels&format=json&Ntt="+keyWord+"*";
	getEndecaResponse(url, "mobile", "global");
}

function populateTypeAheadResults() {
	var keyWord = $("#search-new").val();
	var url = contextPath + "/assembler?Dy=1&assemblerContentCollection=/content/Shared/Auto-Suggest%20Panels&format=json&Ntt="+keyWord+"*";
	getEndecaResponse(url, "desktop", "global");
}

function populateTypeAheadResultsAgain() {
	$(".acResults results-dropdown").addClass('searchAgain');
	var keyWord = $("#search-full").val();
	var url = contextPath + "/assembler?Dy=1&assemblerContentCollection=/content/Shared/Auto-Suggest%20Panels&format=json&Ntt="+keyWord+"*";
	getEndecaResponse(url, "desktop", "again");
}

function getEndecaResponse(url, browser, box) {
	$.get(url, function(data) {
		var dimSearchResult = null;
        var autoSuggestCartridges = data.contents[0].autoSuggest;
        if(autoSuggestCartridges == null || autoSuggestCartridges.length == 0) {
        } else {
	        for(var j = 0; j < autoSuggestCartridges.length; j++) {
	            var cartridge = autoSuggestCartridges[j];
	            if(cartridge['@type'] == "DimensionSearchAutoSuggestItem") {
	                dimSearchResult = cartridge;
	                break;
	            }
	        }
	        if (dimSearchResult != null) {
	        	generateHtmlContent(dimSearchResult, browser);
	        	if(html!= null && html.length >0) {
	        		if(box == "global") {
	        			document.getElementById('typeAheadSearchResultsUL').innerHTML=html;
	        			$("#againSearchBox").hide();
	        			$("#globalSearchBox").show();
	        		} else {
	        			var topClosed = 200;
	        			var topOpen = 303;
	        			if($("#searchFormPNF").length > 0) {
	        				topClosed = 285;
	        				topOpen = 387;
	        			}
	        			document.getElementById('typeAheadSearchResultsULAgain').innerHTML=html;
	        			$("#globalSearchBox").hide();
	        			$("#againSearchBox").show();
	        			var searchDrawerClass = $('#drawer-search').attr('class');
	        			if(searchDrawerClass.indexOf("open") != -1) {
	        				$("#againSearchBox").css({top: topOpen});
	        			} else {
	        				$("#againSearchBox").css({top: topClosed});
	        			}
	        			
	        		}
	        	}
	        	html="";
	        }
        }
	});
}

function generateHtmlContent(dimSearchResult, browser) {
	if(dimSearchResult != null && dimSearchResult.dimensionSearchGroups.length > 0) {
		var dimSearchGroupList = dimSearchResult.dimensionSearchGroups;
		 for(var i = 0; i < dimSearchGroupList.length; i++) {
             var dimResultGroup = dimSearchGroupList[i];
             var displayName = dimResultGroup.displayName;
             for(var j = 0; j < dimResultGroup.dimensionSearchValues.length; j++) { 
                 var dimResult = dimResultGroup.dimensionSearchValues[j];
                 var action = dimResult.contentPath + dimResult.navigationState;
                 var text = dimResult.label;
                 if(browser == "desktop") {
	                 if(html.indexOf("<li><a href='javascript:submitTypeAheadForm(\"" + text + "\")'>" + text + "</a></li>") == -1) {
	                	 html += "<li><a href='javascript:submitTypeAheadForm(\"" + text + "\")'>" + text + "</a></li>";
	                 }
                 } else {
                	 if(html.indexOf("<li><a href='javascript:submitTypeAheadFormMobile(\"" + text + "\")'>" + text + "</a></li>") == -1) {
	                	 html += "<li><a href='javascript:submitTypeAheadFormMobile(\"" + text + "\")'>" + text + "</a></li>";
	                 }
                 }
             }
         }
    }
}

function submitTypeAheadForm (query) {
	$("#search-new").val(query);
	$(".results-dropdown").hide();
	$("#acLoadingCustom").show();
	//$("#drawer-search").css("height","145px");
	var dySearch = $("#dySearchForm").val();
	var nrppSearch = $("#nrPPSearchForm").val();
	var ntySearch = $("#ntySearchForm").val();
	var siteIdsSearch = $("#siteIdsSearchForm").val();
	var url = contextPath + "/browse" + "?_dyncharset=UTF-8&Dy=" +  dySearch + "&Nty=" + ntySearch + "&Ntt=" + query + "&siteIds=" + siteIdsSearch + "&Nrpp=" + nrppSearch;
		//"/cricket/storeUS/browse?_dyncharset=UTF-8&Dy=1&Nty=1&Ntt=cricket-broadband&siteIds=cricketStore&_D%3AsiteIds=+&_D%3Asearch=+&_DARGS=%2Fcricket%2Fsearch%2FsearchForm.jsp";
	$.get(url, function(data) {
		var contentArray = data.split("<!-- IdentifierForTypeAheadSearchAjaxReload -->");
		var refreshableData = contentArray[1];
		$('#predictive-search-zip').html(refreshableData);
		$("#acLoadingCustom").hide();
		$("#drawer-search").removeClass("closed");
		$("#drawer-search").addClass("open");
		$("#drawer-search").css({height: 102});
		$('#predictive-search-zip').show();
	});
}

function submitTypeAheadFormMobile (query) {
	$("#mobileSearchText").val(query);
	$(".results-dropdown").hide();
	$("#acLoadingCustom").show();
	//$("#li-mobile-search").css("height","94px");
	var dySearch = $("#dySearchFormMobile").val();
	var nrppSearch = $("#nrPPSearchFormMobile").val();
	var ntySearch = $("#ntySearchFormMobile").val();
	var siteIdsSearch = $("#siteIdsSearchFormMobile").val();
	var url = contextPath + "/browse" + "?_dyncharset=UTF-8&Dy=" +  dySearch + "&Nty=" + ntySearch + "&Ntt=" + query + "&siteIds=" + siteIdsSearch + "&Nrpp=" + nrppSearch;
		//"/cricket/storeUS/browse?_dyncharset=UTF-8&Dy=1&Nty=1&Ntt=cricket-broadband&siteIds=cricketStore&_D%3AsiteIds=+&_D%3Asearch=+&_DARGS=%2Fcricket%2Fsearch%2FsearchForm.jsp";
	$.get(url, function(data) {
		var contentArray = data.split("<!-- IdentifierForTypeAheadSearchAjaxReload -->");
		var refreshableData = contentArray[1];
		$('#predictive-search-zip').html(refreshableData);
		$("#acLoadingCustom").hide();
		$("#drawer-search").removeClass("closed");
		$("#drawer-search").addClass("open");
		$("#drawer-search").show();
		$('#predictive-search-zip').show();
	});
}
