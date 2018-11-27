<dsp:page>
<dsp:importbean bean="/atg/targeting/TargetingForEach"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:getvalueof var="contextpath" bean="/OriginatingRequest.contextPath"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="mobileSuffix" bean="CricketConfiguration.mobileSuffix"/>
<c:set var="TargeterName" value=""/>
<c:set var="PhoneSpecific" value="false"/>

<dsp:droplet name="Switch">
	<dsp:param name="value" param="PAGE_NAME"/>
		<dsp:oparam name="PHONE_LISTING">
			<c:set var="TargeterName" value="/atg/registry/RepositoryTargeters/Promotional/browseshop/PhoneListingPageBottomBanner" />
	       	<dsp:droplet name="TargetingForEach">
				<dsp:param bean="${TargeterName}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>					
					
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
							<dsp:getvalueof var="liquidpixelurl" param="url" />
							<div class="static-banner footer hide-for-small" style="background-image: url(${liquidpixelurl});">
								<c:choose>
									<c:when test="${landingUrl != null}">
										<a href="${landingUrl}"><div class="spacer"></div></a>
									</c:when>
									<c:otherwise>
										<a href="" onclick="return false" style="cursor: default">
										<div class="spacer"></div></a>
									</c:otherwise>
								</c:choose>
							</div>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
		
			<dsp:droplet name="TargetingForEach">
				<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/PhoneListingPageBottomBanner${mobileSuffix}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div class="static-banner footer show-for-small" style="background-image: url(${liquidpixelurl}); background-size: cover;">
								<c:choose>
									<c:when test="${landingUrl != null}">
										<a href="${landingUrl}"><div class="spacer"></div></a>
									</c:when>
									<c:otherwise>
										<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
									</c:otherwise>
								</c:choose>
							</div>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
      </dsp:oparam>
      <dsp:oparam name="PHONE_DETAILS">
			<c:set var="TargeterName" value="/atg/registry/RepositoryTargeters/Promotional/browseshop/PhoneDetailsPageBottomBanner" />
	       	<dsp:droplet name="TargetingForEach">
				<dsp:param bean="${TargeterName}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div class="static-banner footer hide-for-small" style="background-image: url(${liquidpixelurl});">
								<c:choose>
									<c:when test="${landingUrl != null}">
										<a href="${landingUrl}"><div class="spacer"></div></a>
									</c:when>
									<c:otherwise>
										<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
									</c:otherwise>
								</c:choose>
							</div>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
		
			<dsp:droplet name="TargetingForEach">
				<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/PhoneDetailsPageBottomBanner${mobileSuffix}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div class="static-banner footer show-for-small" style="background-image: url(${liquidpixelurl}); background-size: cover;">
									<c:choose>
										<c:when test="${landingUrl != null}">
											<a href="${landingUrl}"><div class="spacer"></div></a>
										</c:when>
										<c:otherwise>
											<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
										</c:otherwise>
									</c:choose>
								</div>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
      </dsp:oparam>
      <dsp:oparam name="PHONE_COMPARISION">
			<c:set var="TargeterName" value="/atg/registry/RepositoryTargeters/Promotional/browseshop/PhoneComparePageBottomBanner" />
	       	<dsp:droplet name="TargetingForEach">
				<dsp:param bean="${TargeterName}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div class="static-banner footer hide-for-small" style="background-image: url(${liquidpixelurl});">
								<c:choose>
									<c:when test="${landingUrl != null}">
										<a href="${landingUrl}"><div class="spacer"></div></a>
									</c:when>
									<c:otherwise>
										<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
									</c:otherwise>
								</c:choose>
							</div>
						</dsp:oparam>
					</dsp:droplet>
					
				</dsp:oparam>
			</dsp:droplet>
		
			<dsp:droplet name="TargetingForEach">
				<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/PhoneComparePageBottomBanner${mobileSuffix}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div class="static-banner footer show-for-small" style="background-image: url(${liquidpixelurl});">
								<c:choose>
									<c:when test="${landingUrl != null}">
										<a href="${landingUrl}"><div class="spacer"></div></a>
									</c:when>
									<c:otherwise>
										<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
									</c:otherwise>
								</c:choose>
							</div>
						</dsp:oparam>
					</dsp:droplet>
					
				</dsp:oparam>
			</dsp:droplet>
      </dsp:oparam>
      <dsp:oparam name="ACCESSORY_LISTING">
			<c:set var="TargeterName" value="/atg/registry/RepositoryTargeters/Promotional/browseshop/AccessoriesListingBottomBanner" />
	       	<dsp:droplet name="TargetingForEach">
				<dsp:param bean="${TargeterName}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div id="why-cricket" class="static-banner footer hide-for-small" style="background-image: url(${liquidpixelurl});">
						    	<c:choose>
									<c:when test="${landingUrl != null}">
										<a href="${landingUrl}"><div class="spacer"></div></a>
									</c:when>
									<c:otherwise>
										<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
									</c:otherwise>
								</c:choose>
							</div>
						</dsp:oparam>
					</dsp:droplet>
					
				</dsp:oparam>
			</dsp:droplet>
		
			<dsp:droplet name="TargetingForEach">
				<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/AccessoriesListingBottomBanner${mobileSuffix}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div class="static-banner footer show-for-small" style="background-image: url(${liquidpixelurl}); background-size: cover;">
				  				<c:choose>
									<c:when test="${landingUrl != null}">
										<a href="${landingUrl}"><div class="spacer"></div></a>
									</c:when>
									<c:otherwise>
										<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
									</c:otherwise>
								</c:choose>
							</div>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
      </dsp:oparam>
      <dsp:oparam name="ACCESSORY_DETAILS">
			<c:set var="TargeterName" value="/atg/registry/RepositoryTargeters/Promotional/browseshop/AccessoryDetailsBottomBanner" />
	       	<dsp:droplet name="TargetingForEach">
				<dsp:param bean="${TargeterName}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div id="why-cricket" class="static-banner footer hide-for-small" style="background-image: url(${liquidpixelurl});">
						    	<c:choose>
									<c:when test="${landingUrl != null}">
										<a href="${landingUrl}"><div class="spacer"></div></a>
									</c:when>
									<c:otherwise>
										<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
									</c:otherwise>
								</c:choose>
							</div>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
		
			<dsp:droplet name="TargetingForEach">
				<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/AccessoryDetailsBottomBanner${mobileSuffix}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div class="static-banner footer show-for-small" style="background-image: url(${liquidpixelurl}); background-size: cover;">
					  				<c:choose>
										<c:when test="${landingUrl != null}">
											<a href="${landingUrl}"><div class="spacer"></div></a>
										</c:when>
										<c:otherwise>
											<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
										</c:otherwise>
									</c:choose>
								</div>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
      </dsp:oparam>
      <dsp:oparam name="PLAN_ADDON_LISTING">
			<c:set var="TargeterName" value="/atg/registry/RepositoryTargeters/Promotional/browseshop/PlanAddonListingBottomBanner" />
	       	<dsp:droplet name="TargetingForEach">
				<dsp:param bean="${TargeterName}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div class="static-banner footer hide-for-small" style="background-image: url(${liquidpixelurl});">
						    	<c:choose>
									<c:when test="${landingUrl != null}">
										<a href="${landingUrl}"><div class="spacer"></div></a>
									</c:when>
									<c:otherwise>
										<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
									</c:otherwise>
								</c:choose>
							</div>
						</dsp:oparam>
					</dsp:droplet>
					
				</dsp:oparam>
			</dsp:droplet>
		
			<dsp:droplet name="TargetingForEach">
				<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/PlanAddonListingBottomBanner${mobileSuffix}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
								<div id="small-mobile-cta" class="static-banner footer show-for-small" style="background-image: url(${liquidpixelurl}); background-size: cover;">
					  				<c:choose>
										<c:when test="${landingUrl != null}">
											<a href="${landingUrl}"><div class="spacer"></div></a>
										</c:when>
										<c:otherwise>
											<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
										</c:otherwise>
									</c:choose>
								</div>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
      </dsp:oparam>
      <dsp:oparam name="PLAN_ADDON_DETAILS">
			<c:set var="TargeterName" value="/atg/registry/RepositoryTargeters/Promotional/browseshop/PlanAddonDetailsBottomBanner" />
	       	<dsp:droplet name="TargetingForEach">
				<dsp:param bean="${TargeterName}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div id="why-cricket" class="static-banner footer hide-for-small" style="background-image: url(${liquidpixelurl});">
							    	<c:choose>
										<c:when test="${landingUrl != null}">
											<a href="${landingUrl}"><div class="spacer"></div></a>
										</c:when>
										<c:otherwise>
											<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
										</c:otherwise>
									</c:choose>
								</div>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
		
			<dsp:droplet name="TargetingForEach">
				<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/PlanAddonDetailsBottomBanner${mobileSuffix}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div class="static-banner footer show-for-small" style="background-image: url(${liquidpixelurl}); background-size: cover;">
		  						<a href="${landingUrl}"><div class="spacer"></div></a>
							</div>
						</dsp:oparam>
					</dsp:droplet>
					
				</dsp:oparam>
			</dsp:droplet>
      </dsp:oparam>
      <dsp:oparam name="PLAN_LISTING">
			<c:set var="TargeterName" value="/atg/registry/RepositoryTargeters/Promotional/browseshop/PlanListingBottomBanner" />
	       	<dsp:droplet name="TargetingForEach">
				<dsp:param bean="${TargeterName}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
							<div id="section-lifestyle" class="static-banner footer hide-for-small" style="background-image: url(${liquidpixelurl});">
						    	<c:choose>
									<c:when test="${landingUrl != null}">
										<a href="${landingUrl}"><div class="spacer"></div></a>
									</c:when>
									<c:otherwise>
										<a href="" onclick="return false" style="cursor:default"><div class="spacer"></div></a>
									</c:otherwise>
								</c:choose>
							</div>
						</dsp:oparam>
					</dsp:droplet>
				</dsp:oparam>
			</dsp:droplet>
			
			
			<dsp:droplet name="TargetingForEach">
				<dsp:param bean="/atg/registry/RepositoryTargeters/Promotional/browseshop/PlanListingBottomBanner${mobileSuffix}" name="targeter"/>
				<dsp:param name="start" value="1"/>
	  			<dsp:param name="howMany" value="1"/>
	  			<dsp:param name="elementName" value="targetedContent"/>
				<dsp:oparam name="output">
					<dsp:getvalueof var="imageURL" param="targetedContent.url"/>
	  				<dsp:getvalueof var="landingUrl" param="targetedContent.landingUrl"/>
					<dsp:getvalueof var="finalImageURL" value="${imageURL}"></dsp:getvalueof>
					<dsp:droplet name="/com/cricket/imageurl/ImageURLLookupDroplet">
						<dsp:param name="imageLink" value="${finalImageURL}" />
						<dsp:oparam name="output">
						<dsp:getvalueof var="liquidpixelurl" param="url"/>
						<div class="static-banner footer show-for-small" style="background-image: url(${liquidpixelurl}); background-size: cover;">
			  				<a href="${landingUrl}"><div class="spacer"></div></a>
						</div>
						</dsp:oparam>
					</dsp:droplet>
					
				</dsp:oparam>
			</dsp:droplet>
      </dsp:oparam>
 </dsp:droplet>
</dsp:page>