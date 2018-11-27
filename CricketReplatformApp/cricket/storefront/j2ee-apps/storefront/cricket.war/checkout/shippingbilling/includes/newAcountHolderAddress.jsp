<dsp:page>
<dsp:importbean bean="/atg/commerce/order/purchase/ShippingGroupFormHandler" />
<dsp:importbean bean="/com/cricket/integration/maxmind/CitySessionInfoObject"/>
<dsp:input type="hidden" bean="ShippingGroupFormHandler.newAccountHolderAddress" value="true"/>	
    <div class="row">
		<div class="large-12 small-12 columns">
			<h2><crs:outMessage key="cricket_checkout_account_address"/><!-- Account Address --></h2>
			<p><crs:outMessage key="cricket_checkout_account_address_message"/><!-- Enter the home address for the person who will be using this service. --></p>
		</div>
	</div>
	<div class="row">
		<div class="large-12 small-12 columns">
		  <div class="error-container"><crs:outMessage key="cricket_checkout_correct_errors"/> <!-- Please correct the error(s) indicated below --></div>
		</div>
	</div>
    <div class="row">
         <div class="large-6 small-12 columns left">
           <label for="first-name"><crs:outMessage key="cricket_checkout_first_name"/><!-- First Name --></label>
           <dsp:input id="first-name" name="first-name" tabIndex="1" type="text" maxlength="19" bean="ShippingGroupFormHandler.accountHolderAddressData.accountAddress.firstName" required="true" />
         </div>
         <div class="large-6 small-12 columns right">
           <label for="last-name"><crs:outMessage key="cricket_checkout_last_name"/><!-- Last Name --></label>
           <dsp:input id="last-name" name="last-name" tabIndex="2" type="text" maxlength="20" bean="ShippingGroupFormHandler.accountHolderAddressData.accountAddress.lastName" required="true" />
         </div>
   	 </div>
     <div class="row">
        <div class="large-12 small-12 columns single">
            <label for="company-name"><crs:outMessage key="cricket_checkout_company_name"/> <crs:outMessage key="cricket_checkout_optional"/> <!-- Company Name (optional) --></label>
            <dsp:input id="company-name" name="company-name" tabIndex="3" type="text" maxlength="31" bean="ShippingGroupFormHandler.accountHolderAddressData.accountAddress.companyName"/>
          </div>
      </div>             
      <div class="row">
        <div class="large-8 small-12 columns left">
          <label for="address"> <crs:outMessage key="cricket_checkout_address_pobox"/><!-- Address/PO Box --></label>
          <dsp:input id="address" name="address" type="text" tabIndex="4" maxlength="30" bean="ShippingGroupFormHandler.accountHolderAddressData.accountAddress.address1" required="true" onChange="shippingMethodMsg()" />
        </div>
        <div class="large-4 small-12 columns right">
          <label for="apt-suite"> <crs:outMessage key="cricket_checkout_apt_suite"/><!-- Apt./Suite --> <crs:outMessage key="cricket_checkout_optional"/> <!--  (optional) --></label>
          <dsp:input id="apt-suite" name="apt-suite" type="text" tabIndex="5" maxlength="30" bean="ShippingGroupFormHandler.accountHolderAddressData.accountAddress.address2"/>
        </div>
      </div> 
      <div class="row">
          <div class="large-6 small-12 columns left"> 
            <label for="city">  <crs:outMessage key="cricket_checkout_city"/><!-- City  --></label>
            <dsp:input tabIndex="-1" id="city" name="city" type="text" maxlength="30" bean="ShippingGroupFormHandler.accountHolderAddressData.accountAddress.city" beanvalue="CitySessionInfoObject.cityVO.city" required="true" />
          </div>
          <div class="large-4 small-12 columns middle">
            <label for="state"> <crs:outMessage key="cricket_checkout_state"/><!-- State --></label>
             <dsp:select data-customforms="disabled" tabIndex="-1"  class="state" id="state" name="state" bean="ShippingGroupFormHandler.accountHolderAddressData.accountAddress.stateAddress"  required="true" >
                 <dsp:droplet name="/atg/dynamo/droplet/ForEach">
				  <dsp:param name="array" bean="/com/cricket/commerce/order/util/StateList.places"/>
				  <dsp:param name="elementName" value="state"/>
			  		 <dsp:oparam name="output">
				  		<c:set var="stateCode">
				   			<dsp:valueof param="state.code"/>
	   					</c:set>			   
					   <c:set var="userLocationStateCode">
							<dsp:valueof bean="CitySessionInfoObject.cityVO.state"/>
					   </c:set>	
					   	<c:choose>
							<c:when test="${userLocationStateCode eq stateCode }">
								<dsp:option value="${stateCode}" selected="true">
									<dsp:valueof param="state.displayName"/>
								</dsp:option>					
							</c:when>
							<c:otherwise>
								<dsp:option value="${stateCode}">
									<dsp:valueof param="state.displayName"/>
								</dsp:option>								
							</c:otherwise>
					   </c:choose>	
		 			</dsp:oparam>
				</dsp:droplet>
             </dsp:select>
          </div>
          <div class="large-2 small-12 columns right">
            <label for="zipcode"> <crs:outMessage key="cricket_checkout_zipcode"/> <!-- Zip Code --> </label>
            <dsp:input tabIndex="-1" id="zipcode" name="zipcode" type="text" maxlength="10" bean="ShippingGroupFormHandler.accountHolderAddressData.accountAddress.postalCode" beanvalue="CitySessionInfoObject.cityVO.postalCode" required="true" />
          </div>
        </div>
		<div class="row">
				<div class="large-6 small-12 columns left">	
					<label for="phone"> <crs:outMessage key="cricket_checkout_phonenumber"/> <!-- Phone Number --> </label>
					<dsp:input tabIndex="6" id="phone" name="phone" type="text" maxlength="14" bean="ShippingGroupFormHandler.accountHolderAddressData.accountAddress.phoneNumber" required="true" />
				</div>
				<div class="large-6 small-12 columns right">
				<label for="email"> <crs:outMessage key="cricket_checkout_email_address"/> <!--  Email Address --> </label>
					<dsp:input id="email" tabIndex="7" name="email" type="text" maxlength="200" bean="ShippingGroupFormHandler.accountHolderAddressData.accountAddress.email" required="true" />
					<label for="checkbox1" class="opt-in">
					<%-- <input id="checkbox-step1" CHECKED type="checkbox" class="hiddenField" tabIndex="8" style="display:none"/> --%>
					<dsp:input type="checkbox" bean="ShippingGroupFormHandler.accountHolderAddressData.emailnotification" value="true" class="hiddenField" tabIndex="8" style="display:none" id="checkbox-step1" checked="true"/>
					<p> <crs:outMessage key="cricket_checkout_emailmessage"/><!-- Please email me special offers and promotions. --></p>
				</label>
			</div>
		</div>
		<div class="row nopad birthdate">
			<div class="large-12 small-12 columns">
				<h2> <crs:outMessage key="cricket_checkout_birthday"/><!-- Birthday --></h2>
				<p><crs:outMessage key="cricket_checkout_birthday_message"/><!-- This helps us verify your identity for account management questions. --></p>
			</div>
		</div>
		<div class="row special-pad">
			<div class="large-3 small-4 columns">				
		        <dsp:select data-customforms="disabled" tabIndex="9" id="month" name="month" value="Month" bean="ShippingGroupFormHandler.accountHolderAddressData.month">
		       		<dsp:option value=""><crs:outMessage key="cricket_checkout_month"/><!-- Month --></dsp:option>
						<dsp:option value="1">January</dsp:option>
						<dsp:option value="2">February</dsp:option>
						<dsp:option value="3">March</dsp:option>
						<dsp:option value="4">April</dsp:option>
						<dsp:option value="5">May</dsp:option>
						<dsp:option value="6">June</dsp:option>
						<dsp:option value="7">July</dsp:option>
						<dsp:option value="8">August</dsp:option>
						<dsp:option value="9">September</dsp:option>
						<dsp:option value="10">October</dsp:option>
						<dsp:option value="11">November</dsp:option>
						<dsp:option value="12">December</dsp:option>
				</dsp:select>
			</div>
			<div class="large-3 small-4 columns">
			       	<dsp:select  data-customforms="disabled" tabIndex="10" id="day" name="day" value="Day" bean="ShippingGroupFormHandler.accountHolderAddressData.day">
			       		<dsp:option value=""> <crs:outMessage key="cricket_checkout_day"/> <!-- Day --></dsp:option>
		         			<dsp:option value="01">01</dsp:option>
							<dsp:option value="02">02</dsp:option>
							<dsp:option value="03">03</dsp:option>
							<dsp:option value="04">04</dsp:option>
							<dsp:option value="05">05</dsp:option>
							<dsp:option value="06">06</dsp:option>
							<dsp:option value="07">07</dsp:option>
							<dsp:option value="08">08</dsp:option>
							<dsp:option value="09">09</dsp:option>
							<dsp:option value="10">10</dsp:option>
							<dsp:option value="11">11</dsp:option>
							<dsp:option value="12">12</dsp:option>
							<dsp:option value="13">13</dsp:option>
							<dsp:option value="14">14</dsp:option>
							<dsp:option value="15">15</dsp:option>
							<dsp:option value="16">16</dsp:option>
							<dsp:option value="17">17</dsp:option>
							<dsp:option value="18">18</dsp:option>
							<dsp:option value="19">19</dsp:option>
							<dsp:option value="20">20</dsp:option>
							<dsp:option value="21">21</dsp:option>
							<dsp:option value="22">22</dsp:option>
							<dsp:option value="23">23</dsp:option>
							<dsp:option value="24">24</dsp:option>
							<dsp:option value="25">25</dsp:option>
							<dsp:option value="26">26</dsp:option>
							<dsp:option value="27">27</dsp:option>
							<dsp:option value="28">28</dsp:option>
							<dsp:option value="29">29</dsp:option>
							<dsp:option value="30">30</dsp:option>
							<dsp:option value="31">31</dsp:option>
					</dsp:select>
			</div>
				<div class="large-3 small-4 columns">
			        <dsp:select data-customforms="disabled" id="year" tabIndex="11" name="year" value="Year" bean="ShippingGroupFormHandler.accountHolderAddressData.year">
		        		<dsp:option label="Year" value=""><crs:outMessage key="cricket_checkout_year"/><!-- Year --></dsp:option>
		                   <dsp:option label="2014" value="2014">2014</dsp:option>
		                   <dsp:option label="2013" value="2013">2013</dsp:option>
		                   <dsp:option label="2012" value="2012">2012</dsp:option>
		                   <dsp:option label="2011" value="2011">2011</dsp:option>
		                   <dsp:option label="2010" value="2010">2010</dsp:option>
		                   <dsp:option label="2009" value="2009">2009</dsp:option>
		                   <dsp:option label="2008" value="2008">2008</dsp:option>
		                   <dsp:option label="2007" value="2007">2007</dsp:option>
		                   <dsp:option label="2006" value="2006">2006</dsp:option>
		                   <dsp:option label="2005" value="2005">2005</dsp:option>
		                   <dsp:option label="2004" value="2004">2004</dsp:option>
		                   <dsp:option label="2003" value="2003">2003</dsp:option>
		                   <dsp:option label="2002" value="2002">2002</dsp:option>
		                   <dsp:option label="2001" value="2001">2001</dsp:option>
		                   <dsp:option label="2000" value="2000">2000</dsp:option>
		                   <dsp:option label="1999" value="1999">1999</dsp:option>
		                   <dsp:option label="1998" value="1998">1998</dsp:option>
		                   <dsp:option label="1997" value="1997">1997</dsp:option>
		                   <dsp:option label="1996" value="1996">1996</dsp:option>
		                   <dsp:option label="1995" value="1995">1995</dsp:option>
		                   <dsp:option label="1994" value="1994">1994</dsp:option>
		                   <dsp:option label="1993" value="1993">1993</dsp:option>
		                   <dsp:option label="1992" value="1992">1992</dsp:option>
		                   <dsp:option label="1991" value="1991">1991</dsp:option>
		                   <dsp:option label="1990" value="1990">1990</dsp:option>
		                   <dsp:option label="1989" value="1989">1989</dsp:option>
		                   <dsp:option label="1988" value="1988">1988</dsp:option>
		                   <dsp:option label="1987" value="1987">1987</dsp:option>
		                   <dsp:option label="1986" value="1986">1986</dsp:option>
		                   <dsp:option label="1985" value="1985">1985</dsp:option>
		                   <dsp:option label="1984" value="1984">1984</dsp:option>
		                   <dsp:option label="1983" value="1983">1983</dsp:option>
		                   <dsp:option label="1982" value="1982">1982</dsp:option>
		                   <dsp:option label="1981" value="1981">1981</dsp:option>
		                   <dsp:option label="1980" value="1980">1980</dsp:option>
		                   <dsp:option label="1979" value="1979">1979</dsp:option>
		                   <dsp:option label="1978" value="1978">1978</dsp:option>
		                   <dsp:option label="1977" value="1977">1977</dsp:option>
		                   <dsp:option label="1976" value="1976">1976</dsp:option>
		                   <dsp:option label="1975" value="1975">1975</dsp:option>
		                   <dsp:option label="1974" value="1974">1974</dsp:option>
		                   <dsp:option label="1973" value="1973">1973</dsp:option>
		                   <dsp:option label="1972" value="1972">1972</dsp:option>
		                   <dsp:option label="1971" value="1971">1971</dsp:option>
		                   <dsp:option label="1970" value="1970">1970</dsp:option>
		                   <dsp:option label="1969" value="1969">1969</dsp:option>
		                   <dsp:option label="1968" value="1968">1968</dsp:option>
		                   <dsp:option label="1967" value="1967">1967</dsp:option>
		                   <dsp:option label="1966" value="1966">1966</dsp:option>
		                   <dsp:option label="1965" value="1965">1965</dsp:option>
		                   <dsp:option label="1964" value="1964">1964</dsp:option>
		                   <dsp:option label="1963" value="1963">1963</dsp:option>
		                   <dsp:option label="1962" value="1962">1962</dsp:option>
		                   <dsp:option label="1961" value="1961">1961</dsp:option>
		                   <dsp:option label="1960" value="1960">1960</dsp:option>
		                   <dsp:option label="1959" value="1959">1959</dsp:option>
		                   <dsp:option label="1958" value="1958">1958</dsp:option>
		                   <dsp:option label="1957" value="1957">1957</dsp:option>
		                   <dsp:option label="1956" value="1956">1956</dsp:option>
		                   <dsp:option label="1955" value="1955">1955</dsp:option>
		                   <dsp:option label="1954" value="1954">1954</dsp:option>
		                   <dsp:option label="1953" value="1953">1953</dsp:option>
		                   <dsp:option label="1952" value="1952">1952</dsp:option>
		                   <dsp:option label="1951" value="1951">1951</dsp:option>
		                   <dsp:option label="1951" value="1951">1951</dsp:option>
		                   <dsp:option label="1950" value="1950">1950</dsp:option>
		                   <dsp:option label="1949" value="1949">1949</dsp:option>
		                   <dsp:option label="1948" value="1948">1948</dsp:option>
		                   <dsp:option label="1947" value="1947">1947</dsp:option>
		                   <dsp:option label="1946" value="1946">1946</dsp:option>
		                   <dsp:option label="1945" value="1945">1945</dsp:option>
		                   <dsp:option label="1944" value="1944">1944</dsp:option>
		                   <dsp:option label="1943" value="1943">1943</dsp:option>
		                   <dsp:option label="1942" value="1942">1942</dsp:option>
		                   <dsp:option label="1941" value="1941">1941</dsp:option>
		                   <dsp:option label="1940" value="1940">1940</dsp:option>
		                   <dsp:option label="1939" value="1939">1939</dsp:option>
		                   <dsp:option label="1938" value="1938">1938</dsp:option>
		                   <dsp:option label="1937" value="1937">1937</dsp:option>
		                   <dsp:option label="1936" value="1936">1936</dsp:option>
		                   <dsp:option label="1935" value="1935">1935</dsp:option>
		                   <dsp:option label="1934" value="1934">1934</dsp:option>
		                   <dsp:option label="1933" value="1933">1933</dsp:option>
		                   <dsp:option label="1932" value="1932">1932</dsp:option>
		                   <dsp:option label="1931" value="1931">1931</dsp:option>
		                   <dsp:option label="1930" value="1930">1930</dsp:option>
		                   <dsp:option label="1929" value="1929">1929</dsp:option>
		                   <dsp:option label="1928" value="1928">1928</dsp:option>
		                   <dsp:option label="1927" value="1927">1927</dsp:option>
		                   <dsp:option label="1926" value="1926">1926</dsp:option>
		                   <dsp:option label="1925" value="1925">1925</dsp:option>
		                   <dsp:option label="1924" value="1924">1924</dsp:option>
		                   <dsp:option label="1923" value="1923">1923</dsp:option>
		                   <dsp:option label="1922" value="1922">1922</dsp:option>
		                   <dsp:option label="1921" value="1921">1921</dsp:option>
		                   <dsp:option label="1920" value="1920">1920</dsp:option>
		                   <dsp:option label="1919" value="1919">1919</dsp:option>
		                   <dsp:option label="1918" value="1918">1918</dsp:option>
		                   <dsp:option label="1917" value="1917">1917</dsp:option>
		                   <dsp:option label="1916" value="1916">1916</dsp:option>
		                   <dsp:option label="1915" value="1915">1915</dsp:option>
		                   <dsp:option label="1914" value="1914">1914</dsp:option>
		                   <dsp:option label="1913" value="1913">1913</dsp:option>
		                   <dsp:option label="1912" value="1912">1912</dsp:option>
		                   <dsp:option label="1911" value="1911">1911</dsp:option>
		                   <dsp:option label="1910" value="1910">1910</dsp:option>
		                   <dsp:option label="1909" value="1909">1909</dsp:option>
		                   <dsp:option label="1908" value="1908">1908</dsp:option>
		                   <dsp:option label="1907" value="1907">1907</dsp:option>
		                   <dsp:option label="1906" value="1906">1906</dsp:option>
		                   <dsp:option label="1905" value="1905">1905</dsp:option>
		                   <dsp:option label="1904" value="1904">1904</dsp:option>
					</dsp:select>
				</div>
				<div class="large-3 small-12 columns hide-for-small">
					&nbsp;
				</div>
		</div>
	<div class="divider"></div>
</dsp:page>