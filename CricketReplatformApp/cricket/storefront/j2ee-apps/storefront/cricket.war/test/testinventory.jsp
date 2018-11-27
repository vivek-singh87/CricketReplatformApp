<dsp:page>

<dsp:importbean bean="/com/cricket/test/TestFormHandler"/>

<head><title>manage Test JSP Index Page</title></head>
<body>
<dsp:form>
		<dsp:input type="submit" bean="TestFormHandler.submitInventory" name="Submit" value="Submit">Submit Inventory</dsp:input>
		<%--<dsp:input type="submit" bean="TestFormHandler.submitPIM" name="Submit" value="Submit">Submit PIM</dsp:input>--%>
</dsp:form>
</body>
</dsp:page>
<%-- @version $Id: //product/Eclipse/version/10.2/plugins/atg.project/templates/index.jsp#1 $$Change: 735822 $--%>
