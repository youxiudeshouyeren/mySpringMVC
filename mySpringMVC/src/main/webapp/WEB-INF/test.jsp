<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>View</title>
</head>
<body>
<h1>hello me</h1>
 <%
                  out.println("Hello 史家兴！");
           %>
<a href="/mySpringMVC/web/test.do">点</a>
<%
out.println("你的 IP 地址 " + request.getRemoteAddr());
%>
<%
String name=(String)request.getAttribute("name");
out.write("谁是狗"+name);
%>
</body>
</html>
