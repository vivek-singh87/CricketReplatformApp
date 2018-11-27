<%@ taglib uri="/dspTaglib" prefix="dsp" %>
<%@ page import="atg.servlet.*" %>
<dsp:page>

<dsp:importbean bean="/atg/dynamo/Configuration"/>
<dsp:importbean bean="/com/cricket/test/TestFormHandler"/>
<dsp:importbean bean="/com/cricket/integration/zipcode/ManualConsumeZipCodeFeed"/>
<dsp:form>
	consume feed <dsp:input type="submit" bean="ManualConsumeZipCodeFeed.manualConsume"/>
</dsp:form>
<head><title>manage Test JSP Index Page</title></head>
<body>
<dsp:form>
<dsp:input type="submit" bean="TestFormHandler.submitPARC" name="Submit" value="Submit">Submit PARC</dsp:input>
<dsp:input type="submit" bean="TestFormHandler.submitPIM" name="Submit" value="Submit">Submit PIM</dsp:input>
</dsp:form>
</body>
</dsp:page>
<%-- @version $Id: //product/Eclipse/version/10.2/plugins/atg.project/templates/index.jsp#1 $$Change: 735822 $--%>
