<dsp:page>
<dsp:importbean bean="/atg/dynamo/droplet/IsEmpty"/>

<dsp:droplet name="/atg/dynamo/droplet/Switch">
		<dsp:param name="value" param="pageType"/>
		<dsp:oparam name="phoneDetails">
			<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
				<dsp:param name="id" param="productId"/>
				<dsp:param name="seoString" param="seoString"/>
				<dsp:param name="itemDescriptorName" value="phone-product"/>
				<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="partCanonicalUrl" param="url" vartype="java.lang.String"/>
					<c:set var="hostName" ><crs:outMessage key='canonical_key_homepage'/></c:set>
					<dsp:getvalueof var="canonicalUrl" value="${hostName}${partCanonicalUrl}" vartype="java.lang.String"/>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
		<dsp:oparam name="accessoryDetails">
			<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
				<dsp:param name="id" param="productId"/>
				<dsp:param name="seoString" param="seoString"/>
				<dsp:param name="itemDescriptorName" value="accessory-product"/>
				<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="partCanonicalUrl" param="url" vartype="java.lang.String"/>
					<c:set var="hostName" ><crs:outMessage key='canonical_key_homepage'/></c:set>
					<dsp:getvalueof var="canonicalUrl" value="${hostName}${partCanonicalUrl}" vartype="java.lang.String"/>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
		<dsp:oparam name="planDetails">
			<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
				<dsp:param name="id" param="productId"/>
				<dsp:param name="itemDescriptorName" value="plan-product"/>
				<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="partCanonicalUrl" param="url" vartype="java.lang.String"/>
					<c:set var="hostName" ><crs:outMessage key='canonical_key_homepage'/></c:set>
					<dsp:getvalueof var="canonicalUrl" value="${hostName}${partCanonicalUrl}" vartype="java.lang.String"/>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
		<dsp:oparam name="planAddOnDetails">
			<dsp:droplet name="/atg/repository/seo/CanonicalItemLink">
				<dsp:param name="id" param="productId"/>
				<dsp:param name="itemDescriptorName" value="addOn-product"/>
				<dsp:param name="repositoryName" value="/atg/commerce/catalog/ProductCatalog"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="partCanonicalUrl" param="url" vartype="java.lang.String"/>
					<c:set var="hostName" ><crs:outMessage key='canonical_key_homepage'/></c:set>
					<dsp:getvalueof var="canonicalUrl" value="${hostName}${partCanonicalUrl}" vartype="java.lang.String"/>
				</dsp:oparam>
			</dsp:droplet>
		</dsp:oparam>
		<dsp:oparam name="homePage">
			<c:set var="hostName" ><crs:outMessage key='canonical_key_homepage'/></c:set>
			<dsp:getvalueof var="canonicalUrl" value="${hostName}" vartype="java.lang.String"/>
		</dsp:oparam>
		<dsp:oparam name="whyCricketKeyLandingPage">
			<c:set var="hostName" ><crs:outMessage key='canonical_key_homepage'/></c:set>
			<c:set var="whyCricketLandingPage" ><crs:outMessage key='canonical_key_whyCricket'/></c:set>
			<dsp:getvalueof var="canonicalUrl" value="${hostName}/${whyCricketLandingPage}" vartype="java.lang.String"/>
		</dsp:oparam>
		<dsp:oparam name="listingPage">
			<c:set var="hostName" ><crs:outMessage key='canonical_key_homepage'/></c:set>
			<c:set var="jumpText" ><crs:outMessage key='canonical_key_jump'/></c:set>
			<dsp:getvalueof bean="/OriginatingRequest.requestURI" var="listingURL"/>
			<dsp:getvalueof var="canonicalUrl" value="${hostName}${listingURL}${jumpText}" vartype="java.lang.String"/>
		</dsp:oparam>
</dsp:droplet>
<link rel="canonical" href="${canonicalUrl}"/>
</dsp:page>