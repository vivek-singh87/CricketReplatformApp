<dsp:page>
	<c:if test="${totalNumRecs gt 0 || totalNumRecs ne '0'}">
		<div class="row">
			<div class="large-12 small-12 columns subnav hide-for-small">
				<dl class="sub-nav">
					<dsp:include page="/search/listing/includes/search_catNavs.jsp">
						<dsp:param name="content" value="${contentItem.SecondaryContent[1].contents[0].navigation}"/>
					</dsp:include>
				</dl>
			</div>
		</div>
	</c:if>
</dsp:page>