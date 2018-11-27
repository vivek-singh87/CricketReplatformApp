<dsp:page>
<dsp:importbean bean="/atg/commerce/endeca/cache/DimensionValueCacheDroplet"/>
<dsp:importbean bean="/com/cricket/configuration/CricketConfiguration"/>
<dsp:getvalueof var="contextpath" bean="OriginatingRequest.contextPath"/>
<section id="tab-content">
  <div class="row">
    <div class="columns large-12 small-12">
      <div class="section-container tabs" data-section="tabs" data-options="deep_linking: true">
  		<dsp:droplet name="DimensionValueCacheDroplet">
			<dsp:param name="repositoryId" bean="CricketConfiguration.allPhonesCategoryId"/>
			<dsp:oparam name="output">
				<dsp:getvalueof var="phonesCategoryIdCacheEntry" param="dimensionValueCacheEntry" />
	      </dsp:oparam>
		</dsp:droplet>
		<a href="${contextpath}${phonesCategoryIdCacheEntry.url}" class="circle-arrow right hide-for-small"><crs:outMessage key="cricket_phonedetails_SeeAllPhones"/></a>
		        
    <!--This is the included page in product detail page. This jsp page will show the overview of the phone from product overview properties and also display the corresponding images. -->
         <dsp:include page="/browse/phone/details/includes/phoneOverview.jsp"/>        
      
    <!-- This is the included page in product detail page. This jsp page will show the specification of the phone. It will display phone features, cricket services support, memory, camera etc.. -->
        <section id="specifications">
	          <p class="title" data-section-title><a href="#specifications"><crs:outMessage key="cricket_phonedetails_Specifications"/></a></p>
	          <div class="content" data-slug="specifications" data-section-content>
	            <div class="copy row"> 
	               <!-- <div class="columns large-6 small-12">  --> 
	               <dsp:droplet name="/com/cricket/browse/SplitPhoneSpecsDroplet">
			    	<dsp:param name="phoneSpecs" param="Product.phoneSpecifications"/>
			    	<dsp:oparam name="output">
						<div class="columns large-6 small-12">
					       <dsp:droplet name="/atg/dynamo/droplet/ForEach">
					    	<dsp:param name="array" param="phoneSpecsLeft"/>
					    	<dsp:param name="elementName" value="specificationsInfo"/>				              	
							<dsp:oparam name="output">
								<dsp:getvalueof var="count" param="count"/>
									<dsp:getvalueof var="size" param="size"/>
											<dsp:droplet name="/atg/dynamo/droplet/ForEach">
										    	<dsp:param name="array" param="specificationsInfo.specifications"/>
										    	<dsp:param name="elementName" value="specification"/>
											    	<dsp:oparam name="outputStart">
											    		<h4><dsp:valueof param="specificationsInfo.specGroupName"></dsp:valueof></h4>									    		
											    	</dsp:oparam>
											    	<dsp:oparam name="output">						           
									                 	<ul>
										                    <li>
										                      <span class="left"><dsp:valueof param="specification.specName"></dsp:valueof></span>
										                      <span class="right details"><dsp:valueof param="specification.specValue"></dsp:valueof></span>
										                    </li>
									                  	</ul>	
											    	</dsp:oparam>									   
									    	</dsp:droplet> 
					           	 </dsp:oparam>
			   				 </dsp:droplet> 
		   				 </div>
						<div class="columns large-6 small-12">
					       <dsp:droplet name="/atg/dynamo/droplet/ForEach">
					    	<dsp:param name="array" param="phoneSpecsRight"/>
					    	<dsp:param name="elementName" value="specificationsInfo"/>				              	
							<dsp:oparam name="output">
								<dsp:getvalueof var="count" param="count"/>
									<dsp:getvalueof var="size" param="size"/>
											<dsp:droplet name="/atg/dynamo/droplet/ForEach">
										    	<dsp:param name="array" param="specificationsInfo.specifications"/>
										    	<dsp:param name="elementName" value="specification"/>
											    	<dsp:oparam name="outputStart">
											    		<h4><dsp:valueof param="specificationsInfo.specGroupName"></dsp:valueof></h4>									    		
											    	</dsp:oparam>
											    	<dsp:oparam name="output">						           
									                 	<ul>
										                    <li>
										                      <span class="left"><dsp:valueof param="specification.specName"></dsp:valueof></span>
										                      <span class="right details"><dsp:valueof param="specification.specValue"></dsp:valueof></span>
										                    </li>
									                  	</ul>	
											    	</dsp:oparam>
											    	<dsp:oparam name="outputEnd">
											    	</dsp:oparam>										   
									    	</dsp:droplet> 
					           	 </dsp:oparam>
			   				 </dsp:droplet> 
		   				 </div>
			    	</dsp:oparam>
			    	</dsp:droplet>                
			             <!-- </div>	 -->			             
			          </div>
			       </div>
        </section>
       
  <!--This is the included page in product detail page. This jsp page will show the quick start and download link to download user manual.. -->
       <dsp:include page="/browse/phone/details/includes/phoneHelpResource.jsp"/>
       
      </div>
    </div>
  </div>
</section>
</dsp:page>