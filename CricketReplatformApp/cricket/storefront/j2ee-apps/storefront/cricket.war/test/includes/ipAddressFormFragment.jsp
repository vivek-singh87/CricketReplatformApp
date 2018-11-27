<dsp:page>
	<script>
		function checkMaxmindDBForIP() {
			var contextPath = "/";
			var ipAddress = $('#ipAddress_toCheck').val();
			$.ajax({
	            url: "/common/includes/ipInfoJson.jsp?ipAddress=" + ipAddress,
	            type: "post",
	            dataType: "json",
	            error:function(){
	            	document.getElementById('ipAddressResults').innerHTML = "<li>Sorry, we could not determine your City</li>";
	            },
	            success:function(data) {
	            	if(data.present == 'true') {
		            	var html = "<table><th colspan='2'>";
		            	html+="Results For IP:: " + ipAddress + "</th>";
		            	html += "<tr><td>City:: </td><td>" + data.city + "</td></tr>";
		            	html += "<tr><td>Region:: </td><td>" + data.region + "</td></tr>";
		            	html += "<tr><td>Country:: </td><td>" + data.country + "</td></tr>";
		            	html += "<tr><td>Postal Code:: </td><td>" + data.postalCode + "</td></tr>";
		            	html += "<tr><td>Latitude:: </td><td>" + data.latitude + "</td></tr>";
		            	html += "<tr><td>Longitude:: </td><td>" + data.longitude + "</td></tr>";
		            	html+="</table>"
		            	document.getElementById('ipAddressResults').innerHTML = html;
	            	} else {
	            		document.getElementById('ipAddressResults').innerHTML = "<li>Sorry, we could not determine your City</li>";
	            	}
	            }
			})
		}
	</script>
	<section id="statusform">
		<div id="statusform-header" class="row">
			<div class="columns large-12 small-12">
				<h1>GeoIP <span>Maxmind Utility</span></h1>
				<p class="order-number"></p>
			</div>
	    </div><!-- /#statusform-header-->
		<div class="row">
			<div id="statusform-content" class="columns large-12 small-12">
				<div class="row">
					<div class="columns large-12 small-12">
						<p>Please enter your IP Address below.</p>
					</div>
				</div>
				<div class="row">
					<div class="columns large-12 small-12">
						<p>IP Address:</p>
					</div>
				</div>
				<div class="row">
					<div class="form-container" id="getOrderIDForm">
						<div class="columns large-5 small-9">
							<input type="text" value="" name="ipAddress" id="ipAddress_toCheck" onKeyDown="if(event.keyCode==13) checkMaxmindDBForIP();">
						</div>
			            <div class="columns large-2 small-3">
			            	<a onclick="checkMaxmindDBForIP()" href="javascript:void();" class="button prefix orange-button">submit</a>
			            </div>
						<div class="columns large-1 small-1">
						</div>
					</div>
				</div>
			</div><!-- /#statusform-content .columns -->
		</div><!-- /.row-->
		<div class="row" id="ipAddressResults">
			<table>
				<tr>
				</tr>
			</table>
		</div> <!--  Row -->
	</section>
</dsp:page>