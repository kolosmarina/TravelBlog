<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <%--ОТОБРАЖЕНИЕ ФОРМЫ ДЛЯ ВВОДА ЛОГИНА ДЛЯ НЕЗАЛОГИНЕННОГО ПОЛЬЗОВАТЕЛЯ--%>
    <c:if test="${empty sessionScope.user}">
        <h3 class="text-center"><fmt:message key="text.signInToTravelling"/></h3>
        <form name="loginForm" action="${pageContext.request.contextPath}/travelling" method="post">
            <input type="hidden" name="command" value="authentication"/>
            <div class="row">
                <label class="control-label offset-sm-2 col-sm-2" for="email"><fmt:message key="label.email"/>
                    <span class="custom">&#42;</span>
                </label>
                <div class="col-sm-6">
                    <input type="email" class="form-control" id="email" name="email"
                           placeholder="custom_email@gmail.com"
                           required pattern="(^[\w-]+\.)*[\w-]+@[\w-]+(\.[\w-]+)*\.[a-zA-Z]{2,6}">
                </div>
            </div>
            <br>
            <div class="row">
                <label class="control-label offset-sm-2 col-sm-2" for="password"><fmt:message key="label.password"/>
                    <span class="custom">&#42;</span>
                </label>
                <div class="col-sm-6">
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="Password"
                           required>
                </div>
            </div>
            <br>
            <div class="row">
                <div class="col-sm-10 offset-sm-4">
                    <input type="submit" class="btn btn-outline-success" value="<fmt:message key="button.logIn"/>">
                    <input type="reset" class="btn btn-outline-secondary" value="<fmt:message key="button.clearForm"/>">
                </div>
            </div>
        </form>
        <%--ПЕРЕДАЕТСЯ СООБЩЕНИЕ ПОСЛЕ НЕУДАЧНОЙ ПОПЫТКИ ВВОДА ЛОГИНА И/ИЛИ ПАРОЛЯ--%>
        <div class="row">
            <div class="col-sm-10 offset-sm-4">
                <p class="custom"> ${requestScope.loginOrPasswordError} </p>
            </div>
        </div>
        <%--ПЕРЕДАЕТСЯ СООБЩЕНИЕ В СЛУЧАЕ, ЕСЛИ ПОЛЬЗОВАТЕЛЬ ЗАБЛОКИРОВАН--%>
        <div class="row">
            <div class="col-sm-10 offset-sm-4">
                <p class="custom">${requestScope.blockedPassMessage}</p>
            </div>
        </div>
        <br>
    </c:if>
</div>
</body>
</html>
