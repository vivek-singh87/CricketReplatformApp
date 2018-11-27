<dsp:page>
<dsp:importbean bean="/atg/commerce/ShoppingCart"/>
<dsp:getvalueof var="telesalesCode" bean="ShoppingCart.current.teleSaleCode"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<script src="${contextpath}/js/customcricketstore.js"></script>
	<div class="columns large-4 small-12 telesales">

		<form id="telesales-info" class="custom" name="customer-info"
			method="post" action="">
			<fieldset>
				<div class="row">
					<div class="columns large-6 small-12">
						<p>
							<crs:outMessage key="cricket_checkout_telesales" />
							<!-- Telesales -->
						</p>
						
						<span class="italic"> 
						<crs:outMessage	key="cricket_checkout_forinternaluse" /> <!-- For internal use only. --></span>
					</div>
				<c:if test="${telesalesCode eq null}">
					<div class="columns large-4 small-12">
						<input id="telesalesCode" name="telesalesCode" type="text" tabIndex="1"	width="10" class="left">
					</div>
					<div class="columns large-2 small-1">
						<a class="edit left" id="applyTelesalesCode" href="#" onclick="applyTelesalesCode();">
							<crs:outMessage	key="cricket_checkout_apply" />
								<!-- Apply -->
						</a>
					</div>
				</c:if>
				<c:if test="${telesalesCode ne null}">
					<div class="columns large-4 small-12">
						${telesalesCode}
					</div>
					<div class="columns large-2 small-1">
						<a class="edit left" id="removeTelesalesCode" href="#" onclick="removeTelesalesCode();">
							Remove
								<!-- Apply -->
						</a>
					</div>
				</c:if>
				</div>
			</fieldset>
		</form>
	</div>
</dsp:page>