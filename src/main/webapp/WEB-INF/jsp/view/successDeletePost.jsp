<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SuccessDeletePost</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<%@ include file="header.jsp" %>

<%--ОТОБРАЖЕНИЕ ФОРМЫ ДЛЯ ПЕРЕХОДА НА ГЛАВНУЮ СТРАНИЦУ--%>
<div class="container">
    <div class="row">
        <div class="col-sm-9 ">
            <%--СООБЩЕНИЕ ОБ УСПЕШНОМ УДАЛЕНИИ ПОСТА--%>
            <h2 class="text-left"><fmt:message key="text.successDeletePost"/></h2>
        </div>
        <div class="col-sm-3">
            <form name="MainPage" action="${pageContext.request.contextPath}/travelling">
                <input type="hidden" name="command" value="open_main_page"/>
                <button type="submit" class="btn btn-outline-success"><fmt:message key="button.MainPage"/></button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
