<dsp:page>
<dsp:importbean bean="/atg/cricket/util/CricketProfile"/>

<dsp:droplet name="/com/cricket/browse/GetUserAddonsDroplet">
<dsp:param name="billingAccountNumber" bean="CricketProfile.accountNumber"/>
<dsp:oparam name="output">

<%-- ***<dsp:valueof bean="CricketProfile.additionalOfferingProducts"></dsp:valueof>
###<dsp:valueof bean="CricketProfile.bundledOfferingProducts"></dsp:valueof>

By doing this the CricketProfile session component is set with the above two properties additionalOfferingProducts and bundledOfferingProducts --%>
</dsp:oparam>
</dsp:droplet>
</dsp:page>