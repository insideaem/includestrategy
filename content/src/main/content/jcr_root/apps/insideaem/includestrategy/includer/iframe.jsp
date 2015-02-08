<%@page session="false" import="com.insideaem.includestrategy.api.*"%><%
%><%@include file="/libs/foundation/global.jsp" %>

<iframe src="<%=request.getAttribute("INCLUDE_STRATEGY_URL")%>" frameborder="0" width="600" height="600"></iframe>