// Cart Specific JS
$(function(){

	if($("#constructor").hasClass("cart-static"))
	{
		$("#li-cart a, .close-cart").unbind("click");
		$("#drawer-cart").css({"height":"auto"}).toggleClass("open");
		$("#drawer-cart").toggleClass("open");
		$("#li-location a, #li-log-in a, #li-search a").removeClass("open");
		$("#li-cart a#cart-img").toggleClass("open").css({"pointer":"crosshair"});
		$("#drawer-utility-indicator").removeClass();			
		$("#drawer-indicator").removeClass("di-search").toggleClass("di-cart");		
		$(".close-cart").css("visibility","hidden");
	}

	if($(document).width() > 767)
	{
		// This function tests the height of the details and summary columns and sets both heights to the highest so middle grey pixel stretches
		$("#drawer-cart section .content > .row").each(function(){  

			// Since some accordions are closed, need to open and get height of those as well
			if($(this).parent().parent().hasClass("active"))
			{
				var detailsHeight = $(".details-container", this).height();
				var summaryHeight = $(".summary-container", this).height();		
			}
			else
			{
				$(this).parent().parent().addClass("active");
				var detailsHeight = $(".details-container", this).height();
				var summaryHeight = $(".summary-container", this).height();
				$(this).parent().parent().removeClass("active");
				// alert("details height is " + detailsHeight);
				// alert("sumamry height is " + summaryHeight);						
			}
		
			// Now test to see if details is higher, and set summary height accordingly
			if(detailsHeight > summaryHeight)
			{
				$(".summary-container", this).height(detailsHeight);
			} 
		
		}); 
	}

	// For Quanity input field, test to see what value and apply text to the A href title "quantity-button"
	// And also add a class to the button for javascript use
	$('#drawer-cart .quantity').keyup(function() {		
		var buttonText = $(this).parent().find("a.quantity-button");		
 		if($(this).val()=="0")
		{
			buttonText.removeClass("btn-remove btn-update").addClass("btn-remove").text("Remove");
		}		
 		else if($(this).val() > "1")
		{
			buttonText.removeClass("btn-remove btn-update").addClass("btn-update").text("Update");
		}
		else {
			buttonText.removeClass("btn-remove btn-update").text("");
		}			
	});

	// Also need to test initial document ready to apply proper text
	$('#drawer-cart .quantity').each(function() {		
		var buttonText = $(this).parent().find("a.quantity-button");
 		if($(this).val()=="0")
		{
			buttonText.removeClass("btn-remove btn-update").addClass("btn-remove").text("Remove");
		}		
 		else if($(this).val() > "1")
		{
			buttonText.removeClass("btn-remove btn-update").addClass("btn-update").text("Update");
		}
		else {
			buttonText.removeClass("btn-remove btn-update").text("");
		}			
	});
	
});	