<dsp:page>
	<dsp:getvalueof var="tax" param="taxPriceInfo" />
	<table cellspacing="0" cellpadding="0" border="0">
		<tbody>
			<tr class="tax-total total">
				<th><crs:outMessage key="cricket_checkout_sales_tax"/><!-- Sales Tax --></th>
				<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${tax.amount}" var="tax" />
				<td>$${tax}</td>
			</tr>
		</tbody>
	</table>
	<div class="divider package"></div>
</dsp:page>