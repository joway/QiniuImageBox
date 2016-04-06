<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>七牛云图床</title>
<body>
<div style="text-align: center">
	<div style="height: 60px"></div>
    <a href="/">
        <img src="http://tp2.sinaimg.cn/2651079901/180/40070305662/1">
    </a>
	<h1>七牛云图床</h1>
	<form:form action="/" method="post" commandName="files" enctype="multipart/form-data">
		选择文件:<input type="file" name="files[0]" multiple/>
		<div style="height: 20px"></div>
		<input type="submit" value="submit">
	</form:form>
    <c:forEach var="url" items="${requestScope.urls}">
        链接:<a href="${url}" target="_blank" id="url">${url}</a>
    </c:forEach>

    <div style="height: 60px"></div>


    V0.1<br>
    By:<a href="https://joway.wang/" target="_blank">Joway Wong</a>



</div>

</body>
</html>