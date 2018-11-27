<dsp:page>
	<dsp:importbean bean="/atg/store/sort/RangeSortDroplet" />
	<table>
		
		<dsp:droplet name="RangeSortDroplet">
			<dsp:param name="array" param="productList"/>
	        <dsp:param name="howMany" param="howMany"/>
	        <dsp:param name="start" param="start"/>
	        <dsp:param name="sortSelection" param="sortSelection"/>
	        <dsp:oparam name="outputStart">
	        	<tr>
	        		
					<dsp:include page="/browse/gadgets/createPaginationLinks.jsp">
						<dsp:param name="p" param="p"/>
						<dsp:param name="howMany" param="howMany"/>
						<dsp:param name="start" param="start"/>
						<dsp:param name="size" param="size"/>
						<dsp:param name="sortSelection" param="sortSelection"/>
					</dsp:include>
				</tr>
	        </dsp:oparam>
	        <dsp:oparam name="output">
	        	<dsp:include page="/common/gadgets/displayProductThumbnail.jsp">
					<dsp:param name="product" param="element"/>
					<dsp:param name="size" param="size"/>
					<dsp:param name="count" param="count"/>
				</dsp:include>
	        	
	        </dsp:oparam>
		</dsp:droplet>
	</table>

</dsp:page>