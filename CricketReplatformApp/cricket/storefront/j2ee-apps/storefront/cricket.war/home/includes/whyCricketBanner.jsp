<dsp:page>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:getvalueof var="whyCricketLink" bean="CricketConfiguration.homePageLinks.whyCricket" />
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<section id="why-cricket-callout">
	 <div class="row wrapper">
	   <div class="column large-6 small-12"><h1>
	     Cricket Wireless<span>lets you experience more</span></h1>
	     <p>Cricket gives you everything you want in a Smartphone Plan for half the price of the competition.</p>
	   </div>
	   <div class="column large-6 small-12 copy">
	     <ul>
	       	<li>Unlimited plans, including music</li>
					<li>Half the price of the competition</li>
					<li>All the latest smartphones</li>
				</ul>
				<ul>
					<li>Nationwide coverage</li>
					<li>No Contracts</li>
					<li>Free Overnight Shipping</li>
	     </ul>
	     <div class="row">
    	   <div class="column large-12 small-12"><a class="circle-arrow white" href="${contextPath}/whycricket/why-cricket-landing.jsp">Why Cricket?</a></div>
    	 </div>
	   </div>
	 </div>

	</section>
</dsp:page>