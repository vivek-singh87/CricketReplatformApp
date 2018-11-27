<?php // Prototype Only - grab absolute path for includes 
$root = $_SERVER["DOCUMENT_ROOT"];
?>

<!DOCTYPE html>
<!--[if lt IE 7]><html class="no-js lt-ie10 lt-ie9 lt-ie8 lt-ie7" lang="en"><![endif]-->
<!--[if lte IE 7]><html class="no-js ie7 lt-ie10 lt-ie9 lt-ie8" lang="en"><![endif]-->
<!--[if lte IE 8]><html class="no-js ie8 lt-ie10 lt-ie9" lang="en"><![endif]-->
<!--[if lte IE 9]><html class="no-js ie9 lt-ie10" lang="en"><![endif]-->
<!--[if gt IE 9]><!--> <html class="no-js" lang="en"><!--<![endif]-->
<!--[if !IE]><!--><script>if(/*@cc_on!@*/false){document.documentElement.className+=' ie10';}</script><!--<![endif]-->
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta name="description" content="" />
<!-- Favicons-->
<link rel="shortcut icon" href="/img/favicons/favicon.ico">
<link rel="apple-touch-icon" href="/img/favicons/apple-touch-icon.png">
<link rel="apple-touch-icon" sizes="57x57" href="/img/favicons/apple-touch-icon-57x57.png">
<link rel="apple-touch-icon" sizes="72x72" href="/img/favicons/apple-touch-icon-72x72.png">
<link rel="apple-touch-icon" sizes="114x114" href="/img/favicons/apple-touch-icon-114x114.png">
<link rel="apple-touch-icon" sizes="144x144" href="/img/favicons/apple-touch-icon-144x144.png">
<title>Cricket</title>
<!--[if lte IE 8]>
<link rel="stylesheet" href="/css/fixed-width.css">
<link rel="stylesheet" href="/css/fixed-width2.css">
<link rel="stylesheet" href="/css/fixed-width3.css">
<![endif]-->
<!--[if gt IE 8]><!-->
<link rel="stylesheet" href="/lib/swiper/idangerous.swiper.css" />
<link rel="stylesheet" href="/css/cricket.css" />
<link type="text/css" rel="stylesheet" href="/css/print.css" media="print" />

<!--<![endif]-->
<!-- LivePerson chat -->
<link rel="stylesheet" href="/css/chatGui.css" type="text/css" media="print, projection, screen">

<script src="/js/vendor/custom.modernizr.js"></script>

<!-- TAGGING: Tagging Libraries -->
<script type="text/javascript" src="//libs.coremetrics.com/eluminate.js"></script>
<!-- Note: The client id set here is for staging, production environment ID = 90383849 -->
<script type="text/javascript">cmSetClientID("60383849",true,"data.coremetrics.com","mycricket.com");</script>
<script type="text/javascript" src="//nexus.ensighten.com/cricket/Bootstrap.js"></script>



</head>
<body>
<div id="chat-button" class="hide-for-small">
	<div id="cricket-modal-chat"></div>
	<div id="chat-close"></div>
</div>
<div id="outer-wrap">
<div id="inner-wrap">

	<header id="header">
	<!--Specific top 'strip' area to show location -->	
		<div class="mobile-location">
		  <!-- Default State, No Zipcode entered, Not Geolocated -->
		  <p><a class="open-zip" href="#">City, State</a></p> 
		  <!-- Otherwise they have entered their zipcode or been geolocated -->
			<!-- <p><a class="open-zip" href="#">Firestone Park, CA 80820</a></p>  -->
		</div>	
		<div id="drawer-mobile-location">
			<div class="drawer-wrapper">
				<form class="form-container" name="checkMobileZipForm" id="checkMobileZipForm">						
					<h2>Enter Your Home Zip Code</h2>
					<p>See the best offers available in your area.</p>					
					<p><input type="tel" name="zipchange" id="zipchange"/></p>										
					<p><a href="#" id="btn-mobile-check-zip" class="button large">Go</a></p>
					<div class="errors-mobile-location"></div>
					<div class="mobile-zip-results">
						<h4>Please Select Your City:</h4>
						<ul class="inline-list">
							<li><a href="#">Wheat Ridge, CO</a></li>
							<li><a href="#">Denver, CO</a></li>
							<li><a href="#">Arvada, CO</a></li>
							<li><a href="#">Wheat Ridge, CO</a></li>
							<li><a href="#">Denver, CO</a></li>
							<li><a href="#">Arvada, CO</a></li>												
						</ul>		
					</div>					
					<hr/>
					<p>Are you an existing Cricket customer?</p>
					<p><a href="https://account.mycricket.com" class="btn-log-in-now">Log In Now</a></p>
				</form>
			</div>	
		</div>			
		<nav id="nav-utility">
			<div class="row">
			<!--Start of Mobile specific markup -->	
				<h3>Manage Your Account</h3>
				<form class="custom" name="mobileLoginForm" id="mobileLoginForm" method="post">	
					<div class="row">
						<div class="small-12 columns">
							<input type="tel" name="mobile-phone" id="mobile-phone" title="Phone Number" placeholder="Phone Number">
						</div>	
						<div class="small-12 columns">
							<input type="text" name="mobile-password" id="mobile-password" title="Password" placeholder="Password">
						</div>	
						<div class="small-12 errors-mobile-login columns"></div>
						<div class="small-12 columns">	
							<label for="checkbox1">
								<input type="checkbox" id="checkbox1" style="display: none;">
								<span class="custom checkbox"></span> Remember Me
							</label>
						</div>
						<div class="small-12 columns">
							<a href="#" id="btn-mobile-login" class="button small">Log In</a>
						</div>	
						<div class="small-12 columns">
							<p><a href="https://account.mycricket.com/index/resetpassword">Forgot Your Password?</a></p>
							<p><a href="http://account.mycricket.com">Register</a></p>
						</div>													
					</div>					
				</form>		
				<div class="indicator-black-west"></div>
				<hr/>
			<!--Desktop utility nav (repurposed for mobile) starts here -->	
				<ul class="large-6">
					<li id="li-coverage-map"><a href="http://www.mycricket.com/coverage/maps/wireless">Coverage</a></li>
					<li><a href="https://account.mycricket.com/cricketlocations/">Find A Store</a></li>
					<li><a href="http://www.mycricket.com/activate">Activate</a></li>
					<li><a href="#">Make A Payment</a></li>
					<li><a href="https://account.mycricket.com/cart/status">Order Status</a></li>					
					<li id="li-language"><a class="no-pipe last" href="#">Espa&#241;ol</a></li>
				</ul>
				<hr/>	
				<ul id="nav-utility-right">						
					<li id="li-username"><a href="https://account.mycricket.com/" class="no-pipe">Hello Chris</a></li>
					<li id="li-location">
					  <!-- Default State, No Zipcode entered, Not Geolocated -->
      		  <a class="no-pipe" href="#">City, State</a>
      		  <!-- Otherwise they have entered their zipcode or been geolocated -->
					  <!-- <a class="no-pipe" href="#">Firestone Park, CA 80820</a> -->
					</li>
					<li id="li-log-in"><a href="#" class="button expanded large">My Account</a></li>
					<li id="li-log-out"><a href="#" class="no-pipe">Log Out</a></li>
					<div id="drawer-utility-indicator"></div>
				</ul>				
				<div class="side-menu-close"><a href="#" class="">X</a></div>
			</div>
		</nav>	
		
	<!-- Drawers for Location and My Account Log In -->	
		<div id="drawer-location" class="drawer-utility">
			<div class="drawer-wrapper">
				<form class="form-container" name="checkZipForm" id="checkZipForm" method="post">
					<div class="row">
						<div class="large-12 columns">
							<div class="row">
								<div class="large-8 columns">
									<!--NOTE: This is form for checking zip codes -->
									<div class="row" id="zipcode-form">
										<div class="large-6 columns">
											<h3>Enter Your Home Zip Code</h3>
											<p>See the best offers available in your area.</p>		
										</div>	
										<div class="large-4 columns">
											<input type="tel" name="zip" id="zip" placeholder="" maxlength="12"/>										
										</div>
										<div class="large-2 columns">
											<a href="#" id="btn-check-zip" class="button small">Go</a>
										</div>	
									</div>											
								</div>	
								<div class="large-3 columns login-message">
									<p>Are you an existing Cricket Customer? <a href="https://account.mycricket.com">Log In now</a></p>
								</div>												
								<div class="close-drawer close-location large-1 columns">
									<a href="#" class="right">X</a>
								</div>				
							</div>
							<div class="row"><div class="large-8 large-offset-4 errors-location columns"></div></div>
							<div class="row zip-results">
								<div class="large-2 columns">
									<h4>Please Select Your City:</h4>
								</div>
								<div class="large-10 columns">
									<ul class="inline-list">
										<li><a href="#">Wheat Ridge, CO</a></li>
										<li><a href="#">Denver, CO</a></li>
										<li><a href="#">Arvada, CO</a></li>
										<li><a href="#">Wheat Ridge, CO</a></li>
										<li><a href="#">Denver, CO</a></li>
										<li><a href="#">Arvada, CO</a></li>												
									</ul>	
								</div>		
							</div>							
						</div>
					</div>	
				</form>							
			</div>	
		</div>	
		
		<div id="drawer-log-in" class="drawer-utility">
			<div class="drawer-wrapper">
				<form class="custom form-container" name="checkLoginForm" id="checkLoginForm" method="post">
					<div class="row">	
						<div class="large-11 columns">
							<h3>Manage Your Account</h3>
							<p>Make a payment, upgrade your phone or add a line of service.</p>	
						</div>
						<div class="large-1 columns close-drawer close-log-in">
							<a href="#" class="right">X</a>
						</div>						
					</div>		
					<div class="row">
						<div class="large-4 columns">
							<input type="tel" name="phone" id="phone" data-type="phone" data-required="true" placeholder="Phone Number">
						</div>	
						<div class="large-4 columns">
							<input type="text" name="password" id="password" data-required="true" placeholder="Password">
						</div>	
						<div class="large-2 columns">
							<a href="#" id="btn-login" class="button small">Log In</a>
						</div>	
						<div class="large-2 columns"></div>
					</div>	
					<div class="row"><div class="large-12 errors-login columns"></div></div>
					<div class="row">	
						<div class="large-6 columns">
							<ul>
								<li class="no-pipe">
									<label for="checkbox1">
										<input type="checkbox" id="checkbox1" style="display: none;">
										<span class="custom checkbox"></span> Remember Me
									</label>						
								</li>
								<li><a href="https://account.mycricket.com/index/resetpassword">Forgot your Password?</a></li>
								<li class="no-pipe"><a href="http://account.mycricket.com">Register</a></li>
							</ul>	
						</div>				
					</div>	
				</form>	
			</div>	
		</div>
	
<!-- Top Bar area; Logo, main navigation, cart and search icons -->						
		<nav class="top-bar row"  data-options="custom_back_text:true;back_text:Back">
			<ul class="title-area">
				<!-- Title Area -->				
				<li class="name">
					<h1><a href="http://digitalpreview.tpninc.com/"><img src="/img/logo-cricket-wireless.png" data-interchange="[/img/logo-cricket-wireless.png, (default)], [/img/logo-cricket.png, (screen and (max-width: 480px))]" alt=""/></a></h1>
				</li>				
				<li class="cart-icon"><a>Cart</a><a href="#" class="num-cart-items">1</a></li>
				<!-- Remove the class "menu-icon" to get rid of menu icon. Take out "Menu" to just have icon alone -->
				<li class="toggle-topbar menu-icon"><a href="#"><span>&nbsp;</span></a></li>
				<li class="account-icon" id="open-side-menu"><a href="#">Open</a></li>
			</ul>
			<section class="top-bar-section">
				<ul id="nav-main" class="right">
					<li id="li-mobile-search">
					  
					  <div class="row first collapse">
      				<div class="small-8 columns small-offset-1">

      					<div id="search-input">
      					  <!-- Predictive search using autocomplete plugin -->
      					  <input type="text" name="search-new" class="search-new" placeholder="Search">
      					</div>

      				</div>
      				<div class="small-3 columns left">
      				  <a class="prefix button orange-button" href="#">Search</a>
      				</div>				
      			</div>
      			<!-- This needs to be here for mobile results (using autocomplete plugin) to append to -->
      			<div id="mobile-search-results"></div>
      			
      			
					</li>	
					<li id="li-phones" class="has-dropdown"><a href="#">Phones</a>						
						<ul class="dropdown">
							<li><a href="http://digitalpreview.tpninc.com/phones/phone-listing-zipcode.php">All Phones</a></li>
							<li><a href="http://digitalpreview.tpninc.com/phones/phone-listing-zipcode.php">Smartphones</a></li>
							<li><a href="http://digitalpreview.tpninc.com/phones/phone-listing-zipcode.php">Feature Phones</a></li> <!--// Change Request: Text from "Basic" to "Feature" -->
							<li><a href="http://digitalpreview.tpninc.com/phones/accessories-listing-zipcode.php">Accessories</a></li>
						</ul>
					</li>
					<li id="li-plans" class="has-dropdown"><a href="#">Plans</a>
						<ul class="dropdown">
							<li><a href="http://www.mycricket.com/cell-phone-plans">All Plans</a></li>
							<li><a href="http://www.mycricket.com/features-and-downloads">Plan Add-ons</a></li>
							<li><a href="http://www.mycricket.com/paygo">Cricket PAYGo</a></li>
						</ul>						
					</li>
					<li id="li-why-cricket" class="has-dropdown"><a href="#">Why Cricket?</a>
						<ul class="dropdown">
							<li><a href="http://www.mycricket.com/learn/">Why Cricket?</a></li>
							<li><a href="http://www.mycricket.com/learn/cricket-wireless">About Cricket</a></li>
							<li><a href="http://www.mycricket.com/cell-phones/mobile-phone-guide">Cell Phones</a></li>
							<li><a href="http://www.mycricket.com/cell-phone-plans/mobile-phone-plan-guide">Plans</a></li>
							<li><a href="http://www.mycricket.com/muve-music">Muve Music</a></li>
							<li><a href="http://www.mycricket.com/payment-plans">Phone Payment Plans</a></li>
						</ul>					
					</li>
					<li id="li-support" class="has-dropdown"><a href="#">Support</a>
						<ul class="dropdown">
							<li><a href="http://www.mycricket.com/support">All Support</a></li>
							<li><a href="http://www.mycricket.com/support/contact-us">Contact Us</a></li>
						</ul>						
					</li>					
					<li id="li-cart">
						<a id="cart-img" href="#">Cart</a>
						<a href="#" class="num-cart-items">1</a>
					</li>
					<li id="li-search"><a href="#">Search</a></li>
				</ul> <!--/#nav-main-->			
			</section> <!--/.top-bar-section-->
			<div id="drawer-indicator"></div>
		</nav> <!--/.top-bar-->
						
	</header> <!--/#header-->
	
<!-- Drawers for Cart and Search -->	
	<div id="drawer-cart" class="drawer-utility drawer">
		<div class="drawer-wrapper">
			<div class="row">
				<div class="large-12 columns">
					<img src="/img/home/drawer-cart.png" alt="" />	
					<div class="close-drawer close-cart">
						<a href="#">X</a>
					</div>	
				</div>								
			</div>	
		</div>
	</div>	<!--/#drawer-cart-->				

	<div id="drawer-search" class="drawer-utility drawer">
		<div class="drawer-wrapper">		
			<div class="row first collapse">
				<div class="large-10 columns">
				  
					<div id="search-input">
						<input type="text" name="search-new" class="search-new" placeholder="Search">
						<!-- Loading Icon for Search-->
						<div class="acLoading"></div>
					</div>
					
				</div>
				<div class="large-1 columns">
				  <a class="prefix button orange-button" href="#">Search</a>
				</div>
				<div class="close-drawer close-search large-1 columns">
					<a href="#">X</a>
				</div>				
			</div>	
			
			<!-- Begin Predictive Search With Zip -->
			<div id="predictive-search-zip" style="display: none;">
			  
			  <!-- ALL Search Results (shows on Search Results Page template as well)-->
			  <?php include "$root/includes/search-results-all.php"; ?>
        
        <!-- Search Support Links -->
        <?php include "$root/includes/search-support-links.php"; ?>
        
			</div>
			<!-- End Predictive Search with Zip-->
			
			<!-- Begin Predictive Search No Zip -->
			<div id="predictive-search-nozip" style="display: none;">
			 
			  <!-- ALL Search Results (shows on Search Results Page template as well)-->
			  <?php include "$root/includes/search-results-all-nozip.php"; ?>
        
        <!-- Search Support Links -->
        <?php include "$root/includes/search-support-links.php"; ?>

			 
			</div>
			<!-- End Predictive Search No Zip -->
			
		</div>	
	</div>	<!--/#drawer-search-->	