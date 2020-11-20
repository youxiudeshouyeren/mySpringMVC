<html>
<head>
    <title>View</title>
</head>
<body>
<h1>hello me</h1>
<br>
<br>
<a href="test/order/11">测试</a>
<br>
<br>

<form
 action="/mySpringMVC/web/test.do" method="post">
    <input type="hidden" name="_method" value="post">
    <input type="submit" value="testPost">
</form>


<br>
<br>
<form action="/mySpringMVC/web/test.do" method="post">
    <input type="hidden" name="_method" value="get">
    <input type="submit" value="testGET">
</form>


<br>
<br>
<form action="/mySpringMVC/web/test.do" method="post">
    <input type="hidden" name="_method" value="put">
    <input type="submit" value="testPUT">
</form>


<br>
<br>
<form action="/mySpringMVC/web/test.do" method="post">
    <input type="hidden" name="_method" value="delete">
    <input type="submit" value="testDELETE">
</form>
</body>
</html>
