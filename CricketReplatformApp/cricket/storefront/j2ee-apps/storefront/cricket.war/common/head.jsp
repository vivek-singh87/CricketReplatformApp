<!DOCTYPE html>  
<dsp:page>

 
	<dsp:importbean bean="/atg/dynamo/droplet/IsEmpty"/>
	<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
	<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
	<dsp:importbean bean="/com/cricket/integration/maxmind/GeoIpLocationServlet"/>
	<dsp:importbean bean="/atg/userprofiling/Profile"/>
	<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
	
<!--[if lt IE 7]><html class="no-js lt-ie10 lt-ie9 lt-ie8 lt-ie7" lang="en"><![endif]-->
<!--[if lte IE 7]><html class="no-js ie7 lt-ie10 lt-ie9 lt-ie8" lang="en"><![endif]-->
<!--[if lte IE 8]><html class="no-js ie8 lt-ie10 lt-ie9" lang="en"><![endif]-->
<!--[if lte IE 9]><html class="no-js ie9 lt-ie10" lang="en"><![endif]-->
<!--[if gt IE 9]><!--> <html class="no-js" lang="en"><!--<![endif]-->
<!--[if !IE]><!--><script>if(/*@cc_on!@*/false){document.documentElement.className+=' ie10';}</script><!--<![endif]-->
<head>
<script src="${contextpath}/js/vendor/jquery-1.9.0.min.js"></script>
<dsp:getvalueof var="key" param="seokey" />

<dsp:include page="/common/canonicalUrlRenderer.jsp">
	<dsp:param name="pageType" param="pageType"/>
	<dsp:param name="seoString" param="seoString" />
</dsp:include>

 	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
	
	<!-- Favicons-->
	<dsp:link rel="shortcut icon" href="${contextpath}/img/favicons/favicon.ico"/>
	<dsp:link rel="apple-touch-icon" href="${contextpath}/img/favicons/apple-touch-icon.png"/>
	<dsp:link rel="apple-touch-icon" sizes="57x57" href="${contextpath}/img/favicons/apple-touch-icon-57x57.png"/>
	<dsp:link rel="apple-touch-icon" sizes="72x72" href="${contextpath}/img/favicons/apple-touch-icon-72x72.png"/>
	<dsp:link rel="apple-touch-icon" sizes="114x114" href="${contextpath}/img/favicons/apple-touch-icon-114x114.png"/>
	<dsp:link rel="apple-touch-icon" sizes="144x144" href="${contextpath}/img/favicons/apple-touch-icon-144x144.png"/>	

	<dsp:droplet name="IsEmpty">
	<dsp:param name="value" param="seokey"/>
	<dsp:oparam name="false">
		<dsp:droplet name="/atg/dynamo/droplet/RQLQueryRange">
			<dsp:param name="repository" value="/atg/seo/SEORepository" />
			<dsp:param name="itemDescriptor" value="SEOTags" />
			<dsp:param name="howMany" value="1" />
			<dsp:param name="cricketKey" value="${key}" />				
			<dsp:param name="queryRQL" value="key = :cricketKey" />										  	
			<dsp:oparam name="output">	
				<c:if test="${not empty key}">
					<dsp:getvalueof var="description" param="element.description"></dsp:getvalueof>		
					<dsp:getvalueof var="keywords" param="element.keywords"/>
					<meta name="description" content="${description}" />
					<meta name="keywords" content="${keywords}"/>					
		  		</c:if>
					<dsp:droplet name="/atg/dynamo/droplet/Switch">
							<dsp:param name="value" param="pageType"/>
							<dsp:oparam name="phoneDetails">
								<title><dsp:valueof value="${key}"/> | Cricket Wireless</title>
							</dsp:oparam>
							<dsp:oparam name="accessoryDetails">
								<title><dsp:valueof value="${key}"/> | Cricket Wireless</title>
							</dsp:oparam>
							<dsp:oparam name="planDetails">
								<title><dsp:valueof value="${key}"/> | Cricket Wireless</title>
							</dsp:oparam>
							<dsp:oparam name="planAddOnDetails">
								<title><dsp:valueof value="${key}"/> | Cricket Wireless</title>
							</dsp:oparam>
							<dsp:oparam name="default">
								<title><dsp:valueof param="element.title"/></title>					
							</dsp:oparam>
				   </dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>		
		</dsp:droplet>
							
  
   
<!--[if lte IE 8]>
<link rel="stylesheet" href="${contextpath}/css/fixed-width.css">
<link rel="stylesheet" href="${contextpath}/css/fixed-width2-min.css">
<link rel="stylesheet" href="${contextpath}/css/fixed-width3-min.css">
<link rel="stylesheet" href="${contextpath}/css/fixed-width4.css">
<![endif]-->
<!--[if gt IE 8]><!-->
<dsp:link rel="stylesheet" href="${contextpath}/lib/swiper/idangerous.swiper.min.css" />
<dsp:link rel="stylesheet" href="${contextpath}/css/cricket.min.css" />
<!--<![endif]-->
<link rel="stylesheet" href="${contextpath}/css/cricket/custom.css" />
<!-- LivePerson chat -->
<dsp:link rel="stylesheet" href="${contextpath}/css/chatGui.css" type="text/css" media="print, projection, screen"></dsp:link>
<script src="${contextpath}/js/vendor/custom.modernizr.js"></script>
<!-- Tagging Libraries -->
<script src="${contextpath}/js/eluminate.js"></script>

<script type="text/javascript">
var clientID ='<dsp:valueof bean='/com/cricket/configuration/CricketConfiguration.coremetricsClientID'/>';
var dataCollectionMethod ='<dsp:valueof bean='/com/cricket/configuration/CricketConfiguration.coremetricsDataCollectionMethod'/>';
var dataCollectionDomain ='<dsp:valueof bean='/com/cricket/configuration/CricketConfiguration.coremetricsDataCollectionDomain'/>';
var cookieDomain ='<dsp:valueof bean='/com/cricket/configuration/CricketConfiguration.coremetricsCookieDomain'/>';
cmSetClientID(clientID,dataCollectionMethod,dataCollectionDomain,cookieDomain);
</script>
<script type="text/javascript" src="//nexus.ensighten.com/cricket/Bootstrap.js"></script>

<dsp:getvalueof var="location" bean="CitySessionInfoObject.cityVO.city"/>
<dsp:getvalueof var="isLocationDefault" bean="CitySessionInfoObject.cityVO.defaultLocation"/>
<dsp:getvalueof var="locationInRepository" bean="CitySessionInfoObject.locationInRepository"/>
<dsp:getvalueof var="testingOn" bean="GeoIpLocationServlet.testingOn"/>
<dsp:getvalueof var="triedMobLoc" bean="CitySessionInfoObject.triedMobileLocation"/>
</head>
<body>
<input type="hidden" name="location" id="location" value="${location}"/>
<input type="hidden" name="isLocationDefault" id="isLocationDefault" value="${isLocationDefault}"/>
<input type="hidden" name="locationInRepository" id="locationInRepository" value="${locationInRepository}"/>
<input type="hidden" name="testingOn" id="testingOn" value="${testingOn}"/>
<input type="hidden" name="isMobileLocTried" id="isMobileLocTried" value="${triedMobLoc}"/>
<input type="hidden" name="contextPathHead" id="contextPathHead" value="${contextpath}"/>
<!--  Google javascripts for mobile device -->
<script src="${contextpath}/js/geo/gears_init.js" type="text/javascript" charset="utf-8"></script>
<script src="${contextpath}/js/geo/geo.js" type="text/javascript" charset="utf-8"></script>
<script src="${contextpath}/js/geo/custom-geo.js" type="text/javascript" charset="utf-8"></script>
<script src="${contextpath}/js/cricket/cricket-endeca-auto-suggest.js"></script>
<script src="${contextpath}/js/jqueryForm.min.js"></script>
<dsp:getvalueof var="protocalType" param="protocalType"/>
<c:choose>
     <c:when  test="${protocalType eq 'secure'}">
	<script src="https://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/rc4.js"></script>
     </c:when>
     <c:otherwise>
	<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/rc4.js"></script>
     </c:otherwise>
</c:choose> 
 </body>
</dsp:page>