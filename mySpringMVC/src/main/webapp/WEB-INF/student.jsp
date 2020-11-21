<%@page import="demo.mySpringMVC.MyStudent"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ page contentType="text/html;charset=utf-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>学生管理</title>
<style>
@charset "UTF-8";

TD.data {
	BACKGROUND-COLOR: #eeeee0;
	BORDER-BOTTOM: #ccc99 1px solid;
	BORDER-LEFT: #ffffff 1px solid;
	BORDER-RIGHT: #cccc99 1px solid;
	BORDER-TOP: #ffffff 1px solid;
	HEIGHT: 23px
}

TD.title {
	BACKGROUND-COL0R: #3988E4;
	BORDER-BOTTOM: #000033 1px solid;
	BORDER-LEFT: #669999 1px solid;
	BORDER-RIGHT: #000033 1px solid;
	BORDER-TOP: #669999 1px solid;
	COLOR: #000000;
	PADDING-BOTTOM: 0px;
	PADDING-LEFT: 10px;
	PADDING-RIGHT: 10px;
	PADDING-TOP: 0px
}

TD.header {
	BACKGROUND-COLOR: #ffffff;
	BORDER-BOTTOM: #cccccc 1px solid;
	BORDER-LEFT: #ffffff 1px solid;
	BORDER-RIGHT: #cccccc 1px solid;
	BORDER-TOP: #ffffff 1px solid;
	HEIGHT: 23px
}

TD {
	FONT-FAMILY: 宋体，Verdana, Helvetica, sans-serif;
	FONT-SIZE: 12px
}

body {
	background-color: #eeeeee;
	SCROLLBAR-FACE-COLOR: #dce0e2;
	SCROLLBAR-HIGHLIGHT-COLOR: #ffffff;
	SCROLLBAR-SHADOW-COLOR: #687888;
	SCROLLBAR-3DLIGHT-COLOR: #687888;
	SCROLLBAR-ARROW-COLOR: #6e7e88;
	SCROLLBAR-TRACK-COL0R: #bcbfc0;
	SCROLLBAR-DARKSHADOW-COLOR: #dce0e2;
	FONT-SIZE: 12px
}

A:link {
	COLOR: blue;
	TEXT-DECORATION: none
}

A:visited {
	COLOR: blue;
	TEXT-DECORATION: none
}

A:hover {
	COLOR: blue;
	TEXT-DECORATION: none
}
p{
	align:center
	
}
h2{
	align:center;
	Font-SIZE:20px
}
</style>
</head>
<body>
<%
   ArrayList<MyStudent> list = (ArrayList<MyStudent>) request.getAttribute("list");
   



%>
<div align="center">
<table width="800" cellpadding="1" cellspacing="1">
<tr>
					<td colspan="9" align="center" class="title" height="30">学生管理</td>
				</tr>
				
				<tr>
				<td align="center" class="header">学号</td>
				<td align="center" class="header">姓名</td>
				<td align="center" class="header">年龄</td>
				<td align="center" class="header">班级</td>
				<td align="center" class="header" colspan="2">操作</td>
				
				</tr>
				
				<%
				
				for(int i=0;i<list.size();i++){
				%>
				
				<tr>
				 
				<td align="center" class="data"><%=list.get(i).getIdString() %></td>
				<td align="center" class="data" ><%=list.get(i).getNameString() %></td>
				<td align="center" class="data" ><%=list.get(i).getAge() %></td>
				<td align="center" class="data" ><%=list.get(i).getClassString() %></td>
				<td align="center" class="data" >
				<form action="/mySpringMVC/web/edit.do" method="post">
				<input type="hidden" name="id" value="<%=list.get(i).getIdString() %>"/>
			    <input type="submit" value="更新"/>
			    </form></td>
				<td align="center" class="data" >
				
				<form action="/mySpringMVC/web/view.do" method="post">
				<input type="hidden" name="_method" value="delete"/>
				<input type="hidden" name="id" value="<%=list.get(i).getIdString() %>"/>
			    <input type="submit" value="删除"/>
				</form>
				</td>
			    
				</tr>
				
				<%} %>

</table>
<form action="/mySpringMVC/web/edit.do" method="post">
			
<input type="submit" value="添加"/>
</form>
</div>
</body>
</html>