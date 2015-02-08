<%@page session="false" import="com.insideaem.includestrategy.api.*"%><%
%><%@include file="/libs/foundation/global.jsp" %><%
%><%
	IncludeStrategyResource includeStrategyResource = (IncludeStrategyResource)resource;
	IncludeStrategy includeStrategy = includeStrategyResource.getIncludeStrategy();
%><%=includeStrategy%><br/><cq:include script="<%=includeStrategy.getType()+".jsp"%>"/>