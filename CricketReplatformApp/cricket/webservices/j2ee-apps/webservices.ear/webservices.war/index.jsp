<%@page import="java.util.*"%>
<h3>
Axis Version = <%=org.apache.axis.Version.getVersion()%>
</h3>
<style>
	table * {
		font-family:Arial;
		font-size:10px;
	}
	table {
		border: 1px solid black;
		border-collapse:collapse;
	}
	td {
		border: 1px solid black;
	}
</style>

<table border="0">
<% 
	for(Map.Entry entry : System.getProperties().entrySet()) {
		%><tr><td><%=entry.getKey()%></td><td><%=entry.getValue()%></td></tr><%
	}
%>
</table>