
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<select >


    <option></option>
    <c:forEach items="${clueStateList}" var="s">
        <option value="${s.value}">${s.text}</option>
    </c:forEach>
</select>
</body>
</html>
