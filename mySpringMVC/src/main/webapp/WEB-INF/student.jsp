<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page language="java" import="java.util.*" %>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>查看总成绩</title>
</head>
<body>
<%
   ArrayList<String[]> list = (ArrayList<String[]>) request.getAttribute("list");
   



%>
<div align="center">
<table width="800" cellpadding="1" cellspacing="1">
<tr>
					<td colspan="9" align="center" class="title" height="30">总成绩排名</td>
				</tr>
				
				<tr>
				<td align="center" class="header">学号</td>
				<td align="center" class="header">姓名</td>
				<td align="center" class="header">年龄</td>
				<td align="center" class="header">班级</td>
				</tr>
				
				<%
				
				for(int i=0;i<list.size();i++){
				%>
				<tr>
				<td align="center" class="data"><%=list.get(i)[0] %></td>
				<td align="center" class="data" ><%=list.get(i)[1] %></td>
				<td align="center" class="data" ><%=list.get(i)[2] %></td>
				<td align="center" class="data" ><%=list.get(i)[3] %></td>
				</tr>
				<%} %>

</table>
</div>
</body>
</html>