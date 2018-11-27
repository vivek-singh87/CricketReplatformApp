// Plan Sections Specific JS
$(function(){
  
  // Add active state when we're in the plans section - probably only for our prototype, won't
  // be needed in ATG version.
  /*var url = location.pathname;
  if (url.indexOf('plans') > -1) {
      $("#li-plans a").addClass("active");
  } */
  
  // Expand/Collapse All Panels on Addons Listing Page
  // On initial page load first slide is open, we need to close it before toggling the others
  function toggleAccordion() {
  	$('.expand-all').on('click', function(e) {
  		e.preventDefault();
  		// Close whatever accordion is currently open
      $(this).closest("#accordion-content").find("section.active").removeClass("active");
      $(this).toggleClass("collapse-toggle");
      
      if ($(this).hasClass("collapse-toggle")) {
        $("#accordion-content .accordion section").addClass("active");
        $(this).html("Collapse All");
      } else {
        $("#accordion-content .accordion section").removeClass('active');
        $(this).html("Expand All");
      }
      
  	});
  }
  
  // We have the ability to remove plans from an existing customers Plans Page
  // If they click "remove" on desktop it animates and removes the plan, if they click
  // remove on mobile, it removes the entire slide and removes the arrows when there is 0 or 1 slide left
  function removeExistingPlans() {
    
    // Remove Button for Desktop
    $(".remove").click( function(e) {
      e.preventDefault();
      $(this).parent(".plan-item").animate({height: "toggle", width: "toggle", opacity: "toggle"}, 500);
    });
    
    // Mobile needs to remove the slide
    $(".remove-mobile").click( function(e) {
      e.preventDefault();
      var $removeContainer = $("#plan-callout .swiper-container");
      var index = $removeContainer.data('swiper').activeIndex;
      $removeContainer.data('swiper').removeSlide(index);
      var totalSlides = $removeContainer.data('swiper').slides.length;
      $removeContainer.data('swiper').reInit();
      
      // We have one or no slides left, don't show arrows
      if (totalSlides <= 1) {
        $removeContainer.siblings(".next").addClass("disabled");
        $removeContainer.siblings(".prev").addClass("disabled");
      }
    });
  }
  
  // We need to dynamically adjust super long titles inside of "Customize Your Options" - plan detail pages
  function adjustTitleSize() {
    $(".additional-feature-container").each(function() {
      var $quote = $(this).find("span.additional-feature-description");
      // regex to look for dashes and spaces
      var $numWords = $quote.text().split(/[-\s]/).length;
      $limitWord = 5; // Anything over 5 words, make the font smaller

      if ($numWords >= $limitWord) {
        $quote.addClass("smaller");
      }
    });
  }


 function sizeFeatureHeading()
 {
	if($("html").hasClass("lt-ie10") )
	{
		
	} 
	else 
	{	
		var featuresHeadingTopHeight = $(".features-heading-top").height();
		var featuresHeadingBottomHeight = $(".features-heading-bottom").height();
		var combinedHeight = featuresHeadingTopHeight + featuresHeadingBottomHeight;
		if(combinedHeight > 352)
		{
			$(".features-heading th").css("background","#109449");
			$(".features-heading th.bg-smartplan").css("background","#067533");
		}	
		else {
			var topUp = 352 - featuresHeadingTopHeight;
			$(".features-heading-bottom").height(topUp);
		}	
	}	
 } 
  //  Init
  toggleAccordion();
  removeExistingPlans();
  adjustTitleSize();
  
	// Check Plans heading height to size properly
	if($("#constructor").hasClass("section-plans-listing"))
	{
		sizeFeatureHeading();
	}
	
  
	
});