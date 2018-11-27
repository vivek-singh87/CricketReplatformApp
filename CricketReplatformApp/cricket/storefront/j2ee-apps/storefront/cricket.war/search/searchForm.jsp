<dsp:page>
	<dsp:getvalueof var="contextPath" vartype="java.lang.String"  bean="/OriginatingRequest.contextPath"/>
	<dsp:importbean bean="/atg/endeca/assembler/SearchFormHandler"/>
	<dsp:importbean bean="/atg/endeca/assembler/cartridge/manager/DefaultActionPathProvider"/>
	<dsp:importbean bean="/atg/multisite/Site" var="currentSite"/>
	
	<dsp:form action="${contextPath}/browse" id="searchForm" requiresSessionConfirmation="false" style="height:auto">
		<input id="dySearchForm" type="hidden" name="Dy" value="1"/>
		<input id="nrPPSearchForm" type="hidden" name="Nrpp" value="10000"/>
		<input id="ntySearchForm" type="hidden" name="Nty" value="1"/>
        <input type="text" name="Ntt" id="search-new" class="search-new" placeholder="Search" autocomplete="off">
        <dsp:input type="hidden" id="siteIdsSearchForm" value="${currentSite.id}" bean="SearchFormHandler.siteIds" name="siteIds"/>
        <dsp:input style="display:none" type="submit" bean="SearchFormHandler.search" name="search" 
          value="submit" id="atg_store_searchSubmit" title="submit" iclass="btn-search"/>
        <!-- <div id="predictiveSearchOptions" style="height:auto;z-index:999999">
		</div> -->
		
	</dsp:form>
	<input id="contextPathSearchForm" value="${contextPath}" type="hidden"/>
</dsp:page>