// Search Specific JS
$(function(){
  
  // Add active state when we're in the search section - probably only for our prototype, won't
  // be needed in production version.
  if(window.location.href.indexOf("results") > -1) {
      $("#li-search a").addClass("open");
   }
   
   if(window.location.href.indexOf("invalid") > -1) {
       $("#li-search a").addClass("open");
    }

// FUNCTION: searchInit()
//NOTES: Used for autocomplete in global search box, might not be used in production version depending on implementation
function searchInit() {
  
  // Adding some classes to wrapper elements for prototype only, once design has been decided on this will no
  // longer be needed (only used to show different options to client)
  var url = location.pathname;
  // 3 & 4 are no-zip versions of search results
  if (url.indexOf('predictive-search-3') > -1) {
      $("#outer-wrap").addClass("predictive-search-nozip");
      $("#li-mobile-search").parent("#nav-main").addClass("predictive-search-nozip");
  // 2 shows a loader and a zip version
  }else if (url.indexOf('predictive-search-2') > -1) {
      $("#outer-wrap").addClass("predictive-search-zip predictive-search-loading");
      $("#li-mobile-search").parent("#nav-main").addClass("predictive-search-zip predictive-search-loading");
  // 4 shows a loader and a no-zip version
  }else if (url.indexOf('predictive-search-4') > -1) {
      $("#outer-wrap").addClass("predictive-search-nozip predictive-search-loading");
      $("#li-mobile-search").parent("#nav-main").addClass("predictive-search-nozip predictive-search-loading");
  // otherwise just show normal zip version without loader everywhere else
  } else {
    $("#outer-wrap").addClass("predictive-search-zip");
    $("#li-mobile-search").parent("#nav-main").addClass("predictive-search-zip");
  }
  
  // We're only showing the loading icon in only 2 of the templates, remove it from all others for now
  if ($("#outer-wrap, #li-mobile-search").hasClass("predictive-search-loading")) {
    $(".acLoading").hide();
  } else {
    $(".acLoading").remove();
  }
  
  
  // Autocomplete Widget from here:
  // https://github.com/dyve/jquery-autocomplete 
  $(".search-newOLD").autocomplete({
      url: '/search/results.html',
      showResult: function(value, data) {
        if($("body").hasClass("mobile")) {
          
          if ($("#nav-main").hasClass("predictive-search-nozip")) {
            return '<span class="mobile-result"><a href="/search/search-results-all-nozip.php">' + value + '</a></span>';
          } else {
            return '<span class="mobile-result"><a href="/search/search-results-all.php">' + value + '</a></span>';
          }  
          
        } else {
          return '<span>' + value + '</span>';
        }
          
      },
      onItemSelect: function(item) {
        
        // Hide Results Dropdown - moved out of jquery.autocomplete.js line 1045
        // Was originally hiding on un-focus, now hiding on click of search result item
        $(".acResults").hide();
          
        // Show/Hide appropriate div for each design option (prototype only)
        if ($("#outer-wrap").hasClass("predictive-search-zip")) {
          $("#drawer-search").find("#predictive-search-zip").show();
        } else if ($("#outer-wrap").hasClass("predictive-search-nozip")) {
          $("#drawer-search").find("#predictive-search-nozip").show();
        }
          
        // Now readjust height for Search Wrapper
        var wrapper = $("#drawer-search .drawer-wrapper").height();
        $("#drawer-search").css("height", wrapper);
        // more nicely animated - IE7/8 might not like this so commenting out for now
        //$("#drawer-search").animate({height: wrapper}, 500);
          
      },
      mustMatch: true,
      //maxItemsToShow: 0,
      autoWidth: '',
      loadingClass: 'acLoading',
      useCache: false,
      selectFirst: false,
      autoFill: false,
      selectOnly: true,
      remoteDataType: 'text',
	cellSeparator: "|"
  });
  
}

  //  Init
  //searchInit();
  
	$(".acLoading").hide();
	
	// $(".search-new").keyup(function(){
	// 	
	// 	// Note to TechM: Capture what is typed in search box in this variable if you need it.
	// 	var searchTerm = $(this).val();
	// 				
	// 	// After three keystrokes, show the results dropdown
	// 	if($(this).val().length > 2)
	// 	{
	// 		$(".results-dropdown").show();
	// 	}
	// 	else {
	// 		$(".results-dropdown").hide();
	// 		$(".acLoading").hide();
	// 	}		
	// });

	$(".search-new").keyup(function(){// For global search box
             
              // Note to TechM: Capture what is typed in search box in this variable if you need it.
              var searchTerm = $(this).val();
                                 
              // After three keystrokes, show the results dropdown
              if($(this).val().length > 2)
              {
                     populateTypeAheadResults();
                     //$(".results-dropdown").show();
              }
              else {
                     $(".results-dropdown").hide();
                     $(".acLoading").hide();
                     document.getElementById('typeAheadSearchResultsUL').innerHTML="";
              }            
       });
      
       $("#search-full").keyup(function(){// For search again bar on search results page
             
              // Note to TechM: Capture what is typed in search box in this variable if you need it.
              var searchTerm = $(this).val();
                                 
              // After three keystrokes, show the results dropdown
              if($(this).val().length > 2)
              {
                     populateTypeAheadResultsAgain();
                     //$(".results-dropdown").show();
              }
              else {
                     $(".results-dropdown").hide();
                     $(".acLoading").hide();
                     document.getElementById('typeAheadSearchResultsUL').innerHTML="";
              }            
       });
});
