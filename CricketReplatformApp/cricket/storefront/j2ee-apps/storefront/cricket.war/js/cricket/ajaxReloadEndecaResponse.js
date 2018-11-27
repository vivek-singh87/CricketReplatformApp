var contextPath = $('#contextPathValue').val();
var accessoryListing = $('#accessoryListing').val();
var windowPath = window.location.pathname;
var endecaQueryString1 = windowPath + "?N=";
var endecaQueryStringSearchPage= windowPath + "?Dy=1&N=";
var NFilters="";
var NfFilters="";
var NrFilters="";
var ajaxFormRegistered = false;
/*function hitEndecaWithFilterQuery(url) {
	var mask = createMask();
	$("#acLoadingCustom").show();
	$("#outer-wrap").load(url + new Date().getTime() + " #inner-wrap", function() {
		$("#acLoadingCustom").hide();
		mask.remove();
		restoreLostBindings();
	});
}*/
function hitEndecaWithFilterQuery(url) {
	var mask = createMask();
	$("#acLoadingCustom").show();
	$.get(url, function(data) {
		var contentArray = data.split("<!-- identifierForEndecaEntireContentReload -->");
		var refreshableData = contentArray[1];
		document.getElementById("ajaxReloadbleEndecaContent").innerHTML=refreshableData;
		//$("#ajaxReloadbleEndecaContent").html(refreshableData);
		$("#acLoadingCustom").hide();
		mask.remove();
		restoreLostBindings();
	});
}
	
function restoreLostBindings() {
	$("#li-location a, .close-location, .alert-box a:not(.no-drawer), .open-zip-drawer").on("click",function(){openLocationDrawer();});
	$("#li-cart a, .close-cart").on("click",function(){openCartDrawer();});
	$("#li-search a, .close-search").on("click",function(){openSearchDrawer();});
	$(".acLoading").hide();
	$(".search-new").keyup(function(){var searchTerm = $(this).val();if($(this).val().length > 2){populateTypeAheadResults();}else {$(".results-dropdown").hide();$(".acLoading").hide();document.getElementById('typeAheadSearchResultsUL').innerHTML="";}});
	if(accessoryListing == "accessoryListing") {
		$(".sub-nav dd a").on("click",function(e){e.preventDefault();if($(".hide-for-small").is(":visible")){$(".sub-nav dd").removeClass("active");$(this).parent().addClass("active");if($(".row.bydevice").hasClass("hide")){$(".row.bydevice").removeClass("hide");}else{$(".row.bydevice").addClass("hide");}}});
	}
	initLayout();
	$(".has-dropdown .label-wrapper label").on("click",function(){$("#phone-filters-dropdown").closest("ul.dropdown").addClass("hoverme");$(".section-phones-listing .phone-radios, .section-phones-listing .phone-results, .section-phones-listing .static-banner, .section-phones-listing .section-filters").on("click, mouseover",function(){$("#phone-filters-dropdown").closest("ul.dropdown").removeClass("hoverme");});});
}

function createMask() {
	var mask = $('<div></div>')
	  .css({
	    position: 'absolute',
	    width: '100%',
	    height: '100%',
	    top: 0,
	    left: 0,
	    'z-index': 10000
	  })
	  .appendTo(document.body)
	  .click(function(event){
	    event.preventDefault();
	    return false;
	   })
	   return mask;
}

function hitEndecaWithPaginationOrSortQuery(url) {
	$('#mainEndecaContent').addClass("darkClass");
	showSpinner('mainEndecaContent');
	$.get(url, function(data) {
		var contentArray = data.split("<!-- identifierForEndecaMainContentReload -->");
		var refreshableData = contentArray[1];
		$('#mainEndecaContent').html(refreshableData);
	});
	$('#mainEndecaContent').removeClass("darkClass");
}

function prepareEndecaMultiselectFilterQuery(query) {
	var tempN;
	var tempNf;
	var tempNr;
	var currentFilterN;
	var currentFilterNRemove;
	tempN = query.substring(query.indexOf('N=') + 2, query.indexOf('&Nf'));
	tempNf = query.substring(query.indexOf('&Nf') + 4, query.indexOf('&Nr'));
	tempNr = query.substring(query.indexOf('&Nr') + 4);
	currentFilterN = tempN.substring(tempN.lastIndexOf("+") + 1);
	if(NFilters == ""){
		NFilters = tempN;
	} else if (NFilters.indexOf(currentFilterN) != -1) {
		NFilters = NFilters.replace("+" + currentFilterN,"");
		
	} else {
		NFilters = NFilters + "+" + currentFilterN;
	}
	if(NfFilters == ""){
		NfFilters = tempNf;
	}
	if(NrFilters == ""){
		NrFilters = tempNr;
	}
}

function prepareEndecaMultiselectFilterQueryPriceRange (rangeFilter, navState) {
	var temp2Nf;
	var temp2N;
	var temp2Nr;
	temp2N = navState.substring(navState.indexOf('N=') + 2, navState.indexOf('&Nf'));
	temp2N = temp2N.substring(0,temp2N.indexOf('+'));
	temp2Nr = navState.substring(navState.indexOf('&Nr') + 4);
	temp2Nf = navState.substring(navState.indexOf('&Nf') + 4, navState.indexOf('&Nr'));
	if(NfFilters == "") {
		NfFilters = temp2Nf + "||sku.listPrice" + rangeFilter;
	} else if(NfFilters.indexOf(rangeFilter) != -1) {
		NfFilters = NfFilters.replace("||sku.listPrice" + rangeFilter,"");
	}	
	else if(NfFilters.indexOf("||sku.listPrice") == -1){
		NfFilters = NfFilters + "||sku.listPrice" + rangeFilter;
	} else {
		
	}
	if(NFilters == ""){
		NFilters = temp2N;
	}
	if(NrFilters == ""){
		NrFilters = temp2Nr;
	}
}

function hitEndecaWithCalculatedQuery() {
	var sortParamPresent = true;
	var endecaSort = "";
	var nsValue = $('#nsValue').val();
	if(nsValue == "") {
		sortParamPresent = false;
		var sortIdentifier = $('#sortOption').val();
		endecaSort = decodeSortIdentifier(sortIdentifier);
	}
	var queryUrl = "";
	if(sortParamPresent){
		queryUrl = endecaQueryString1 + NFilters + "&Nf=" + NfFilters + "&Nr=" + NrFilters + "&Nrpp=10000";
	} else {
		queryUrl = endecaQueryString1 + NFilters + "&Nf=" + NfFilters + "&Nr=" + NrFilters + "&Nrpp=10000" + "&Ns=" + endecaSort;
	}
	if(NFilters == ""){
		//no filters selected do nothing
	} else {
		hitEndecaWithFilterQuery(queryUrl);
		$('#filterSelected').val("yes");
		//document.location.href=queryUrl;
	}
	
	NFilters="";
	NfFilters="";
	NrFilters="";
}

function decodeSortIdentifier(sortIdentifier) {
	var endecaSort = "";
	if(sortIdentifier == "featured") {
	} else if(sortIdentifier == "brand") {
		endecaSort = "product.manufacturer.displayName|0";
	} else if(sortIdentifier == "price") {
		endecaSort = "sku.listPrice|0";
	}
	return endecaSort;
}

function hitEndecaWithCalculatedQuerySearch() {
	var queryUrl = endecaQueryStringSearchPage + NFilters + "&Nf=" + NfFilters + "&Nr=" + NrFilters;
	if(NFilters == ""){
		//no filters selected do nothing
	} else {
		hitEndecaWithFilterQuery(queryUrl);
		$('#filterSelected').val("yes");
		//document.location.href=queryUrl;
	}
	
	NFilters="";
	NfFilters="";
	NrFilters="";
}

function showSpinner(divId) {
	
	var opts = {
			  lines: 13, // The number of lines to draw
			  length: 20, // The length of each line
			  width: 10, // The line thickness
			  radius: 30, // The radius of the inner circle
			  corners: 1, // Corner roundness (0..1)
			  rotate: 0, // The rotation offset
			  direction: 1, // 1: clockwise, -1: counterclockwise
			  color: '#04B404', // #rgb or #rrggbb
			  speed: 1, // Rounds per second
			  trail: 60, // Afterglow percentage
			  shadow: false, // Whether to render a shadow
			  hwaccel: false, // Whether to use hardware acceleration
			  className: 'spinner', // The CSS class to assign to the spinner
			  zIndex: 2e9, // The z-index (defaults to 2000000000)
			  top: 'auto', // Top position relative to parent in px
			  left: 'auto' // Left position relative to parent in px
			};
			var target = document.getElementById(divId);
			var spinner = new Spinner(opts).spin(target);
}
//compare functionality starts
function handleAddToCompare(productId, url) {
	var sessionCompareProds = $('#sessionCompareProdIds').val();
	var firstBlockExecuted = false;
	var prodsAddedString = $('#compareBucketCount').val();
	var compareProdIds = $('#compareBucketProdIds').val();
	var prodsAdded = parseInt(prodsAddedString);
	var currentCompareBoxId = "compareBox" + productId;
	//alert("index||" + compareProdIds.indexOf(productId) + " allIds||" + compareProdIds + " currentId||" + productId);
	if (compareProdIds.indexOf(productId) == -1 && prodsAdded < 4) {
		addImageToCompareBar(url, productId);
		if (prodsAdded == 0) {
			compareProdIds = productId;
		} else {
			compareProdIds = compareProdIds + "," + productId;
		}
		prodsAdded = prodsAdded + 1;
		$("#"+currentCompareBoxId).toggleClass('customUnchecked');
		$("#"+currentCompareBoxId).toggleClass('customChecked');
		firstBlockExecuted = true;
	} 
	if (compareProdIds.indexOf(productId) != -1 && !firstBlockExecuted) {
		removeImageFromCompareBar(url, productId);
		var compareProdArray = compareProdIds.split(",");
		var compareProdsNew = "";
		for(i=0;i<compareProdArray.length;i++) {
			if(productId != compareProdArray[i]) {
				if(compareProdsNew == "") {
					compareProdsNew = compareProdArray[i];
				} else {
					compareProdsNew = compareProdsNew + "," + compareProdArray[i];
				}
			}
		}
		if(prodsAdded == 4) {
			$( ".customUnchecked" ).each(function() {
				$( this ).removeClass("customDisabled");
				$( this ).parent()
				.removeClass("has-tip")
				.removeAttr('data-tooltip')
				.removeAttr('title');
			});
		}
		prodsAdded = prodsAdded - 1;
		$("#"+currentCompareBoxId).toggleClass('customChecked');
		$("#"+currentCompareBoxId).toggleClass('customUnchecked');
		compareProdIds = compareProdsNew;
	}
	if (prodsAdded > 3) {
		$( ".customUnchecked" ).each(function() {
			$( this ).addClass( "customDisabled" );
			$( this ).parent()
			.addClass("has-tip has-tipCustom")
	        .attr('data-tooltip', '')
	        .attr('title', 'Sorry, you can only compare up to four phones at a time.');
		});
	}
	$('#compareBucketCount').val(prodsAdded);
	$('#compareBucketProdIds').val(compareProdIds);
	var showCompareIdenitifier = $('#showCompareIdenitifier').val();
	if (prodsAdded > 1 && showCompareIdenitifier == "compareHidden") {
		$('#compare-bar-custom').show();
		$('#showCompareIdenitifier').val('compareShown');
	}
	if (prodsAdded < 2 && showCompareIdenitifier == "compareShown") {
		$('#compare-bar-custom').hide();
		$('#showCompareIdenitifier').val('compareHidden');
	}
}

function removeImageFromCompareBar(imageUrl, productId){
	var removeImageId = "compImg" + productId;
	var image_x = document.getElementById(removeImageId);
	image_x.parentNode.removeChild(image_x);
	$('#compareLiquidPixelUrl').val(imageUrl);
	$('#compareProdId').val(productId);
	$('#compareAction').val("remove");
	ajaxSubmitCompareForm();
}

function addImageToCompareBar(imageUrl, productId) {
	$('#compareLiquidPixelUrl').val(imageUrl);
	$('#compareProdId').val(productId);
	$('#compareAction').val("add");
	ajaxSubmitCompareForm();
	var imageHtml = $("#compare-bar-custom").html();
	imageHtml += '<img id="compImg' + productId +'" src="' + imageUrl + '" style="width: 16px; height: 33px; margin: 0 0.5em"/>';
	document.getElementById('compare-bar-custom').innerHTML = imageHtml;
}

function ajaxSubmitCompareForm() {
	//$("#compareAddtoSession").ajaxSubmit();
	//$("#compareAddtoSession").ajaxSubmit();
	if(ajaxFormRegistered) {
		$('#addToSessionCompareButton').trigger('click');
	} else {
		setTimeout(function(){
			ajaxSubmitCompareForm();
		},3000);
	}
	return false;
}

$(function(){
	identifySearchPage();
	$("#compareAddtoSession").ajaxForm();
	ajaxFormRegistered = true;
	$(window).scroll(function(){
        if ($(window).scrollTop() > 500){
        	$('#compare-bar-custom').css('position','fixed');
        	$('#compare-bar-custom').css('top','0px');
        	$('#compare-bar-custom').addClass("fixed");
        }
        if ($(window).scrollTop() < 500){
        	$('#compare-bar-custom').css('position','');
        	$('#compare-bar-custom').css('top','0px');
        	$('#compare-bar-custom').removeClass("fixed");
        }
    });
});

function handleSubmitCompareForm() {
	var compareProdIds = $('#compareBucketProdIds').val();
	$('#myproductlistid').val(compareProdIds);
	$('#submitCompareFormButton').trigger('click');
}

function clearCompareSession() {
	var compareProdIdsToClear = $('#compareBucketProdIds').val();
	var compareProdIdsArray = compareProdIdsToClear.split(",");
	for(i=0;i<compareProdIdsArray.length;i++) {
		var removeImageId = "compImg" + compareProdIdsArray[i];
		var image_x = document.getElementById(removeImageId);
		image_x.parentNode.removeChild(image_x);
	}
	$('#compareBucketCount').val("0");
	$('#compareBucketProdIds').val("");
	$('#compare-bar-custom').hide();
	$( ".customChecked" ).each(function() {
		$( this ).removeClass("customChecked");
		$( this ).addClass( "customUnchecked" );
	});
	$( ".customDisabled" ).each(function() {
		$( this ).removeClass("customDisabled");
	});
	$('#compareAction').val("removeAll");
	$( ".customUnchecked" ).each(function() {
		$( this ).parent()
		.removeClass("has-tip has-tipCustom")
        .removeAttr('data-tooltip')
        .removeAttr('title');
	});
	ajaxSubmitCompareForm();
}
//compare functionality ends

//accessory filter by phone brand starts
var alreadyFiltered = false;
function populatePhonesDropDown(selectedBrand) {
	var html = "";
	if(selectedBrand == "Select-Brand") {
		html = "<option value='select'>Select-Brand-First</option>";
	} else {
		var numberOfPhones = $('#brandSize' + selectedBrand).val();
		html = "<option value='select'>Select-Phone</option>";
		for(var i=1;i<=numberOfPhones;i++) {
			var idAndName = $('#idName' + selectedBrand + i).val();
			var idAndNameArray = idAndName.split("||");
			html+= "<option value='"+ idAndNameArray[0] +"'>"+ idAndNameArray[1] +"</option>";
		}
	}
	$('select[id="selectPhoneDropDown"]').html(html);
	//document.getElementById('selectPhoneDropDown').innerHTML = html;
}

function showAssociatedAccessories(selectedPhoneId) {
	$('#selectedFilters').html("");
	$('#accessorySection').val("byPhone");
	$(".dropdown").hide();
	var sortIdentifier = $('#sortOption').val();
	var sortOption = decodeSortIdentifierATG(sortIdentifier);
	alreadyFiltered = true;
	var contextPath = $('#contextPathValue').val();
	$('#selectedPhoneForAccessory').val(selectedPhoneId);
	var url = contextPath + "/browse/gadgets/categoryChildProducts.jsp?phoneProductId=" + selectedPhoneId + "&intention=BUY_ASSOC_ACCESSORY&CATEGORY_TYPE=ACCESSORY&sort=" + sortOption;
	var mask = createMask();
	$("#acLoadingCustom").show();
	$.get(url, function(data) {
		document.getElementById('mainEndecaContent').innerHTML = data;
		$("#acLoadingCustom").hide();
		mask.remove();
		restoreLostBindings();
		removeDisplayNone();
	});
}

function removeDisplayNone(){
	$(".dropdown").each(function() {
		$( this ).css('display', '');
	});
}

function showAllAccessories(url) {
	var sortIdentifier = $('#sortOption').val();
	var sortOption = decodeSortIdentifierATG(sortIdentifier);
	$("#viewAccessoriesByPhone").addClass("hide");
	var section = $('#accessorySection').val();
	$(".dropdown").show();
	if(section == "byPhone") {
		$("#acLoadingCustom").show();
		createMask();
		window.location.href = url + "&sort=" + sortOption;
	}
}

function toggleAccessoriesSubnavCustom() {
	  if ($(".section-accessories-listing").length > 0) {
  		e.preventDefault();
  		if( $('.hide-for-small').is(":visible") ){

  			$('.sub-nav dd').removeClass("active");
  			$(this).child().addClass("active");
			
  			if( $('.row.bydevice').hasClass("hide") ){
  				$('.row.bydevice').removeClass("hide");
  			} else {
  				$('.row.bydevice').addClass("hide");
  			}
		}
	}
}

function showPhoneBrands() {
	$( this ).parent().addClass("active");
	$("#viewAccessoriesByPhone").removeClass("hide");
}

function decodeSortIdentifierATG(sortIdentifier) {
	var endecaSort = "";
	if(sortIdentifier == "featured") {
	} else if(sortIdentifier == "brand") {
		endecaSort = "brand:ascending";
	} else if(sortIdentifier == "price") {
		endecaSort = "price:ascending";
	}
	return endecaSort;
}

function identifySearchPage() {
	var searchIdentifier = $('#searchPageIdentifier').val();
	if(searchIdentifier == 'searchListingPage') {
		$('#searchSymbol').addClass("open");
	}
}
//accessory filter by phone brand ends