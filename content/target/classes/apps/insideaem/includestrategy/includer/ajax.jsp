<%@page session="false" import="com.insideaem.includestrategy.api.*"%><%
%><%@include file="/libs/foundation/global.jsp" %>
<%
	long id = System.currentTimeMillis();
%>
<div id="<%=id%>"></div>

<script type="text/javascript">
	jQuery('#<%=id%>').load('<%=request.getAttribute("INCLUDE_STRATEGY_URL")%>');
</script>