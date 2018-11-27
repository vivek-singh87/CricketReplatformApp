// Phone Sections Specific JS
$(function(){
  
  
// Add active state when we're in the phones section - probably only for our prototype, won't
// be needed in ATG version.
/*if (url.indexOf('phones') > -1) {
    $("#li-phones a").addClass("active");
}
	*/
	
// hide/show dropdown menu for Accessory sub-nav in all but mobile
function toggleAccessoriesSubnav() {
  // Only run this if you're on the Accessories Listing Page
  if ($(".section-accessories-listing").length > 0) {
  	$(".sub-nav dd a").on('click', function(e){
  		e.preventDefault();
			$('.sub-nav dd').removeClass("active");
			$(this).parent().addClass("active");
		
			if( $('.row.bydevice').hasClass("hide") ){
				$('.row.bydevice').removeClass("hide");
			} else {
				$('.row.bydevice').addClass("hide");
			}
  	});
  }
}

// Swap image/colours on Product Details page
function productImageGallery() {
  if($("#constructor").hasClass("section-phones-details") || $("#constructor").hasClass("section-accessory-details")){

  //if check if there are more than 1 color swatch
  var colorsLength = $('#product-content p.colors').children().length;
  var colorsTagCheck = $('#product-content p.colors'); //confirm p.colors is there - this is incase jsp just does not add this tag if it does not exist

  //adding check for P.colors check first as I don't know what will be on page from jsp standpoint
  if(colorsTagCheck){
    if (colorsLength > 1) {
      $('p.colors').show();
      $('.color-title').show();
    } else {
      $('p.colors').hide();
      $('.color-title').hide();
    }
  } else {
    $('p.colors').hide();
    $('.color-title').hide();
  }
  //if check if there are more than 1 image
  var productAssetsLength = $('#product-thumbs ul').children().length;
  var productULcheck = $('#product-thumbs ul');  //adding 2nd check as I don't know what will be on page from jsp standpoint

   // adding check for UL first as I don't know what will be on page from jsp standpoint
   // Also checking that it isn't mobile otherwise there is a big gap (not showing thumbs on mobile)
  if( !($('#product-thumbs ul').hasClass("hide-for-small"))){
    if (productAssetsLength > 1){ //if more than 1 thumb then show swatches
     $('#product-thumbs').show(); 
     $('#product-image img').css("margin-bottom", "100px");
    } else {
      $('#product-thumbs').hide();
      $('#product-image img').css("margin-bottom", "0");
    }  
  } else {
    $('#product-thumbs').hide();
    $('#product-image img').css("margin-bottom", "0");
  }
    
	$("#product-thumbs a").on('click', function(e){
		e.preventDefault();
		// Clear all A of the "active" class
		$('#product-thumbs a').removeClass("active");
		$(this).addClass("active");
		
		var thumbSrc = $("img", this).attr("src");
		var imageSrc = thumbSrc.replace(/thumb\//g, '');
		
		// Swap Main Image
		$("#product-image img").hide().attr("src", imageSrc).fadeIn(500);
	});

	$(".color-swatch").on('click', function(e){
		e.preventDefault();
		
		// First get "clicked" swatch value; essentially the product sku
		var swatchSKU = $(this).attr("id").replace(/swatch-/g, '');
					
		// What this script does: 
		// 1. Update product sku hidden input to pass value
		// 2. Change main image
		// 3. Change thumb images
		
		// Test the value of sku to see if it is new (oops, rhyming penalty, I've hung my head in shame momentarily. )
		var sku = $("#product-sku").val();

		// Condition: If clicked sku and current sku are the same, do nothing, else run our script
		if(swatchSKU==sku) 
		{
			return false;
		}
		else
		{
			// Set updated sku value to hidden input
			$("#product-sku").val(swatchSKU);
			
			// Reset any "active" states on thumb images
			$('#product-thumbs a').removeClass("active");
			$('#product-thumbs a').first().addClass("active");
						
			// Set an "active" state for styling purposes
			$(".colors a").removeClass("active");
			$(this).addClass("active");
			
			// Set main image and thumbs img paths to variables
			if($("#constructor").hasClass("section-accessory-details"))
			{
				var imageSrc = "/img/accessory-detail/products/" + swatchSKU + "/1.png";
				var thumbSrc = "/img/accessory-detail/products/" + swatchSKU + "/thumb/";					
			}
			else // use for Phone Details
			{
				var imageSrc = "/img/phone-detail/products/" + swatchSKU + "/1.png";
				var thumbSrc = "/img/phone-detail/products/" + swatchSKU + "/thumb/";
			}
			
			// Swap Main Image
			$("#product-image img").hide().attr("src", imageSrc).fadeIn(500);
		
			// Update all product thumbs available; if thumbs needs to be dynamic, need to rewrite this so HTML is written to page
			var productThumbs = $('#product-thumbs img'); //array of item images
			var counter = 1;
			$.each(productThumbs, function(index){
				if(index == productThumbs.length) return false;
				$(productThumbs[index]).attr('src', thumbSrc + counter + '.png');
				counter++;
			});					
		}

	});
}
}

// Function used on Phone Compare page to invoke "sticky header" when user scrolls down page - headings stay in place
function UpdateTableHeaders() {
 $(".persist-area").each(function() {  
   var el             = $(this),
       offset         = el.offset(),
       scrollTop      = $(window).scrollTop(),
       floatingHeader = $(".floatingHeader", this)
   
   if ((scrollTop > offset.top) && (scrollTop < offset.top + el.height())) {
       floatingHeader.css({
        "visibility": "visible"
       });
   } else {
       floatingHeader.css({
        "visibility": "hidden"
       });      
   };
 });
}


// Onload apply "sticky header" method to Phone Compare table
function comparePhonesStickyBar() {
 	if($("#constructor").hasClass("section-phone-compare")) { 
     var clonedHeaderRow;
  
     $(".persist-area").each(function() {
         clonedHeaderRow = $(".persist-header", this);
         clonedHeaderRow
           .before(clonedHeaderRow.clone())
           .css("width", clonedHeaderRow.width())
           .addClass("floatingHeader"); 
     });
     
     $(window)
      .scroll(UpdateTableHeaders)
      .trigger("scroll");
    }
}


// When you select a filter, it shows in the filter bar
function filterSelect() {

  var $container = $('#phone-filters-dropdown');
  var filterBar = $('.filter-bar');

  // Checkboxes were retaining state on page load, so lets clear them all
  $container.find('input').attr('checked', false).siblings('span').removeClass('checked');

  var showFilterBar = function () {
    filterBar.show();
  }

  var hideFilterBar = function () {
    filterBar.hide();
  }

  // Add Filter to Filter Bar and set "rel" attribute so we know which one to remove later
  var addToFilterBar = function (filter) {
    var filterText = filter.text();
    var link = $('<a href="#">' + filterText + '</a>');
    link.prop('rel', filter.prop('for'));
    filterBar.append(link);
  }

  // We need to remove the filter from the Filter Bar and also uncheck the corresponding checkbox
  var removeFromFilterBar = function(filterName) {
    var checkbox = $container.find('label[for=' + filterName + ']');
    var link = filterBar.find('a[rel=' + filterName + ']');

    checkbox.find('span').removeClass('checked');
    checkbox.find('input').prop('checked', false);
    link.remove();
  }

  var evaluateFilters = function (event) {
    var filter = $(event.currentTarget);
    var filterInput = filter.find('input');
    var filterCheck = $container.find('input:checked').length;
    var filterChecked = filterInput.prop('checked');
    var filterName = filter.prop('for');

    if(filterChecked) {
      addToFilterBar(filter);
    } else {
      removeFromFilterBar(filterName);
    }

    // Hide or Show Bar Depending if Filters are Checked
    if (filterCheck > 0) {
      showFilterBar();
    } else {
      hideFilterBar();
    } 

  }

  // Filter Dropdown Checkboxes on Phones landing page
  $container.find('label').click( function(event){
    // Foundation's events fire after jQuery for some reason, so 
    // we need to delay before we can count the number of checked things.
    window.setTimeout(
      function () {
        evaluateFilters(event); }
    , 100);
  });


  // X box on filters removes them and also unchecks proper box
  filterBar.on('click', 'a', function(event) {
    event.preventDefault();
    var filter = $(event.currentTarget);
    var filterName = filter.prop('rel');
    removeFromFilterBar(filterName);

    // remove filter bar if no links remain
    if ($container.find('input:checked').length > 0) {
      showFilterBar();
    } else {
      hideFilterBar();
    }

  });
}

// We need to do a lot of stuff for phone listing pages
// This shows/hides the comparison bar when more then two items are checked, 
// disables checkboxes when more then 4 are checked
(function ( $ ) {

  var PhoneComparison = function (scope) {
    var comparisonBar = $('#compare-bar'),
        checkboxes = scope.find('div.compare');

    // Checkboxes were retaining state on page load, so lets clear them all
    scope.find('input').attr('checked', false).siblings('span').removeClass('checked');

    // Reflow so sticky bar works
    $('#compare-bar').foundation('magellan','reflow');


    var showComparisonBar = function () {
       comparisonBar.css("visibility", "visible");
       comparisonBar.addClass("shown");
    }

    var hideComparisonBar = function () {
       comparisonBar.css("visibility", "hidden");
       comparisonBar.removeClass("shown");
    }

    var lockCheckboxes = function () {
      checkboxes.find('input:unchecked')
        .attr('disabled', true)
        .siblings('span').addClass('disabled');
    }

    var unlockCheckboxes = function () {
      checkboxes.find('input')
        .attr('disabled', false)
        .siblings('span').removeClass('disabled');
    }
    
    var addTooltip = function () {
      checkboxes.find('input:unchecked').parent()
        .addClass("has-tip")
        .attr('data-tooltip', '')
        .attr('title', 'Sorry, you can only compare up to four phones at a time.');
    }
    
    var removeTooltip = function () {
      checkboxes.find('input').parent()
      .removeClass("has-tip")
      .removeAttr('data-tooltip')
      .removeAttr('title');
    }

    // If they click the clear button, remove checkboxes from phones
    var clearCheckboxes = function () {
      checkboxes.find('input').attr('checked', false)
      .siblings('span').removeClass('checked');
    }

    var addToComparisonBar = function (phone) {
      var phoneImage = phone.find('img'),
          image = $('<img>', { 'src': phoneImage.attr('src'), 'width': '16', 
          'height': '33' });
      comparisonBar.append(image);
    }

    var removeFromComparisonBar = function(phone) {
      var phoneImage = phone.find('img');
      comparisonBar.find('img[src="' + phoneImage.attr('src') + '"]').remove();
    }

    var evaluate = function (event) {
      var checked = checkboxes.find('input:checked').length;
      var phone = $(event.target).parents('.phone'),
          phoneChecked = phone.find('input').prop('checked');

      if(phoneChecked) {
        addToComparisonBar(phone);
      } else {
        removeFromComparisonBar(phone);
      }

      // They can't select more then 4 phones for comparison
      if(checked >= 4) {
        lockCheckboxes();
        addTooltip();
      } else {
        unlockCheckboxes();
        removeTooltip();
      }

      // Show the bar if they check 2 or more phones
      if(checked >= 2) {
        showComparisonBar();
      } else {
        hideComparisonBar();
      }

      // There is a clear button that removes all selected phones
      $(".clear").click( function (e) {
        e.preventDefault();
        clearCheckboxes();
        unlockCheckboxes();
        removeFromComparisonBar(phone);
        hideComparisonBar();
      });

    }

    scope.click( function (event) {
      // Foundation's events fire after jQuery for some reason, so 
      // we need to delay before we can count the number of checked things.
      window.setTimeout(
        function () { evaluate(event); }
      , 100);
    });
  };

  var Phone = function (scope) {
    this.isChecked = function () {
      scope.find('input').prop('checked');
    }

    this.image = function () {
      scope.find('img');
    }
  }

  $.fn.phoneCompare = function phoneCompare () {
    return new PhoneComparison(this);
  }

}( jQuery ));



// Initializing Things here
toggleAccessoriesSubnav();
filterSelect();
comparePhonesStickyBar();
productImageGallery();

$('.phone-results').phoneCompare();


// Phones Detail page (Plans with this phone) swiper
	var swiperPlans = $('#swiper-plans').swiper({
		mode:'horizontal',
		slidesPerView: 3,
		visibilityFullFit: true,
		watchActiveIndex: true,
		initialSlide: 0,
		onImagesReady : function(e)
		{
			addClassesPlans(e)
		},		
		onSlideChangeEnd : function(e) {
			addClassesPlans(e)
		}		
	});	
	$('.swiper-plans-prev').on('click', function(e){
		e.preventDefault();
		swiperPlans.swipePrev();
		addClassesPlans(e);		
	});
	$('.swiper-plans-next').on('click', function(e){
		e.preventDefault();
		swiperPlans.swipeNext();
		addClassesPlans(e)
	}); 	

	function addClassesPlans(e) {
		// Count # of slides
		var totalSlides = $('#swiper-plans .swiper-slide').length;
		var activeIndex = e.activeIndex;

		if(totalSlides > 3)
		{
			// Add Disabled class to first & last slides when you're on them
			if (activeIndex == 0) 
			{
				//console.log("first slide!");
				$('.swiper-plans-prev').addClass("disabled");
			}
			else if (activeIndex == totalSlides - 3) 
			{
				//console.log("last slide!");
				$('.swiper-plans-next').addClass("disabled");
			}
			else 
			{
				//console.log("in the middle");
				$('.swiper-plans-next').removeClass("disabled");
				$('.swiper-plans-prev').removeClass("disabled");
			}
		} 
		else 
		{
			$('.swiper-plans-next').addClass("disabled");
			$('.swiper-plans-prev').addClass("disabled");
		}
	}

	// Function added specific to stop form submitting when click checkbox in filter dropdown
	$(".has-dropdown .label-wrapper label").on("click", function(){

		$("#phone-filters-dropdown").closest("ul.dropdown").addClass("hoverme");

		//$(".top-bar-section .has-dropdown.hover > .dropdown").css({"visibility":"visible"});
		$(".section-phones-listing .phone-radios, .section-phones-listing .phone-results, .section-phones-listing .static-banner, .section-phones-listing .section-filters").on("click, mouseover", function() {
			$("#phone-filters-dropdown").closest("ul.dropdown").removeClass("hoverme");
		});
	});
	
	// This is for defect 572, we need to swap section class from auto to tabs on ie8
	if($("html").hasClass("lt-ie9"))
	{

	}
	else
	{	// Test for page names; plans listing, phones details, plans details, accessory details pages
		if( $("#constructor").hasClass("section-plans-listing") || $("#constructor").hasClass("section-plan-details") || $("#constructor").hasClass("section-phones-details") ||$ ("#constructor").hasClass("section-accessory-details") )
		{
			$("#tab-content .row .columns .section-container").removeClass("tabs").addClass("auto");
			$("#tab-content .row .columns .section-container").attr("data-section","auto");
		}		
	}
	
	
	function showPhoneFilter()
	{
		$("#section-filters .top-bar").css("max-height", "49px");
		$("#section-filters .top-bar-section").css("left", "-100%");
		$("#section-filters .top-bar-section ul li.has-dropdown").addClass("moved");	
	}
	
	$("#section-filters .top-bar-section li.moved ul li.js-genated h5 a").on('click', function(){
		alert("here");
		$("#section-filters .top-bar-section").css("left", "0");
	});	

	// This is added functionality to support TM request for a way to open Phone filter on page load
	// How To: Call showPhoneFilter(); method to open 
	function showPhoneFilter()
	{
		$("#section-filters .top-bar").css("max-height", "49px");
		$("#section-filters .top-bar-section").css("left", "-100%");
		$("#section-filters .top-bar-section ul li.has-dropdown").addClass("moved");	
	}

	$("#section-filters li.js-generated h5 a").on('click', function(){
		setTimeout(function () {
          $("#section-filters .top-bar-section").css("left", "0px");
        }, 400);
		
	});	
		
});