<dsp:page>
	<script>
		function submitSearchFormAgain() {
			$('#atg_store_searchSubmit_again').trigger('click');
		}
	</script>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:getvalueof var="contentItem" param="contentItem"/>
	<dsp:importbean bean="/com/cricket/search/session/UserSearchSessionInfo"/>
	<dsp:getvalueof var="supportLength" value="${fn:length(supportResultVOs)}" vartype="java.lang.Integer" scope="request"/>
	<dsp:getvalueof var="totalNumRecsPC" vartype="java.lang.Integer" value="${contentItem.MainContent[0].contents[0].totalNumRecs}" scope="request"/>
	<dsp:getvalueof var="totalNumRecs" value="${supportLength + totalNumRecsPC}" scope="request"/>
	<dsp:importbean bean="/atg/endeca/assembler/cartridge/manager/DefaultActionPathProvider"/>
	<dsp:importbean bean="/atg/multisite/Site" var="currentSite"/>
	<dsp:importbean bean="/atg/endeca/assembler/SearchFormHandler"/>
	<dsp:getvalueof var="countFromSession" bean="UserSearchSessionInfo.totalCount"/>
	<dsp:getvalueof var="searchPage" param="searchPage"/>
	<c:choose>
		<c:when test="${searchPage eq 'searchPage'}">
			<dsp:getvalueof var="finalToTalCount" value="${countFromSession}"/>
		</c:when>
		<c:otherwise>
			<dsp:getvalueof var="finalToTalCount" value="${totalNumRecs}"/>
		</c:otherwise>
	</c:choose>
	<section id="search-header">
		<div class="row">
			<div class="columns large-12 small-12">
				<h1>Search Results for <span><dsp:valueof param="Ntt"/> <span class="number">(${finalToTalCount})</span></span></h1>
				
				<dsp:form action="${contextPath}/browse" id="searchFormAgain" requiresSessionConfirmation="false" style="height:auto">
					<dsp:input type="hidden" value="${currentSite.id}" bean="SearchFormHandler.siteIds" name="siteIds"/>
			        <dsp:input style="display:none" type="submit" bean="SearchFormHandler.search" name="search" 
			          value="submit" id="atg_store_searchSubmit_again" title="submit" iclass="btn-search"/>
					<input type="hidden" name="Dy" value="1"/>
					<input type="hidden" name="Nty" value="1"/>
					<input id="nrPPSearchForm" type="hidden" name="Nrpp" value="10000"/>
					<div class="row collapse">
						<div class="columns large-11 small-9">
							<input type="text" name="Ntt" id="search-full" placeholder="Search again" autocomplete="off" value="Search again">							
						</div>
						<div class="columns large-1 small-3">
							<a onclick="submitSearchFormAgain();" class="button prefix orange-button" href="#">Search</a>
						</div>
					</div>
				</dsp:form>
			</div>
		</div>
		<c:if test="${totalNumRecs eq 0 || totalNumRecs eq '0'}">
			<dsp:getvalueof var="zeroResultSearch" value="yes" scope="request"/>
			<dsp:include page="/search/listing/includes/search_didYouMean.jsp">
				<dsp:param name="contentItem" value="${contentItem}"/>
			</dsp:include>
		</c:if>
	</section>
	
</dsp:page>