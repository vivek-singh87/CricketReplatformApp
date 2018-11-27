<dsp:page>
<dsp:importbean bean="/atg/endeca/assembler/droplet/InvokeAssembler"/>
<!-- Main Banners -->
	<div id="section-lifestyle">
		<div class="show-for-small">
			<dsp:droplet name="InvokeAssembler">
		    	<dsp:param name="contentCollection" value="/content/Shared/HomePageBottomBanner_mobile"/>
		    	<dsp:oparam name="output">
		      		<dsp:getvalueof var="promoBanner_mobile" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
		    	</dsp:oparam>
		  	</dsp:droplet>
		  	<c:if test="${not empty promoBanner_mobile}">
		    	<dsp:renderContentItem contentItem="${promoBanner_mobile}"/>
			</c:if>
		</div>
		<div class="hide-for-small">
			<dsp:droplet name="InvokeAssembler">
		    	<dsp:param name="contentCollection" value="/content/Shared/HomePageBottomBanner"/>
		    	<dsp:oparam name="output">
		      		<dsp:getvalueof var="promoBanner" vartype="com.endeca.infront.assembler.ContentItem" param="contentItem"/>
		    	</dsp:oparam>
		  	</dsp:droplet>
		  	<c:if test="${not empty promoBanner}">
		    	<dsp:renderContentItem contentItem="${promoBanner}"/>
			</c:if>
		</div>
	</div>
</dsp:page>