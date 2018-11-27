<dsp:page>
<dsp:getvalueof var="originatingRequestURL" bean="/OriginatingRequest.requestURI"/>
<dsp:getvalueof var="contentItem" vartype="com.endeca.infront.assembler.ContentItem" value="${originatingRequest.contentItem}"/>
<dsp:getvalueof var="sortSelection" param="sortSelection"/>
<td>
	<dsp:form id="sortByForm" action="${originatingRequestURL}${contentItem.categoryAction}">
		<label for="sortBySelect">
			Sort By:  
		</label>
		<select style="width: 50%" id="sortBySelect" name="sort" onchange="this.form.submit()" >
			
			<option value="" ${(empty sortSelection) ? 'selected="selected"' : ''}>
                  Top Picks
			</option>
			
			<option value="displayName:ascending" ${sortSelection=='displayName:ascending' ? 'selected="selected"' : ''}>
				A - Z
			</option>
			
			<option value="displayName:descending" ${sortSelection=='displayName:descending' ? 'selected="selected"' : ''}>
				Z - A
			</option>
			
			<option value="price:ascending" ${sortSelection=='price:ascending' ? 'selected="selected"' : ''}>
				Price L - H
			</option>
			
			<option value="price:descending" ${sortSelection=='price:descending' ? 'selected="selected"' : ''}>
				Price H - L
			</option>
		</select>
		<input type="hidden" value="${contentItem.categoryDimensionId}" name="N" />
		<input type="hidden" value="" name="p"/>
	
	</dsp:form>
</td>
</dsp:page>