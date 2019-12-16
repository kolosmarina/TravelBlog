<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registration</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <%--ОТОБРАЖЕНИЕ ФОРМЫ ДЛЯ ВВОДА ДАННЫХ ДЛЯ НОВОГО ПОЛЬЗОВАТЕЛЯ, НЕЗАЛОГИНЕННОГО--%>
    <c:if test="${empty sessionScope.user}">
        <h2 class="text-center"><fmt:message key="text.registration"/></h2>
        <form name="registrationForm" action="${pageContext.request.contextPath}/travelling" method="post">
            <input type="hidden" name="command" value="registration"/>
            <div class="row">
                <label class="control-label offset-sm-1 col-sm-2" for="email"><fmt:message key="label.email"/>
                    <span class="custom">&#42;</span>
                </label>
                <div class="col-sm-8">
                    <input type="email" class="form-control" id="email" name="email"
                           placeholder="custom_email@gmail.com"
                           required pattern="(^[\w-]+\.)*[\w-]+@[\w-]+(\.[\w-]+)*\.[a-zA-Z]{2,6}" value=${param.email}>
                </div>
            </div>
            <div class="row">
                <small id="emailHelp" class="form-text text-muted col-sm-8 offset-sm-3"><fmt:message
                        key="help.email"/></small>
            </div>
            <div class="row">
                <label class="control-label offset-sm-1 col-sm-2" for="password"><fmt:message key="label.password"/>
                    <span class="custom">&#42;</span>
                </label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="password" name="password"
                           placeholder="Password" required pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[\w]{5,25}$">
                </div>
            </div>
            <div class="row">
                <small id="passwordHelp" class="form-text text-muted col-sm-8 offset-sm-3"><fmt:message
                        key="help.password"/></small>
            </div>
            <div class="row">
                <label class="control-label offset-sm-1 col-sm-2" for="nickname"><fmt:message key="label.nickName"/>
                    <span class="custom">&#42;</span>
                </label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="nickname" name="nickname"
                           placeholder="Nickname" required pattern="^[A-Za-z][\w]{4,20}$" value=${param.nickname}>
                </div>
            </div>
            <div class="row">
                <small id="nicknameHelp" class="form-text text-muted col-sm-8 offset-sm-3"><fmt:message
                        key="help.nickName"/></small>
            </div>
            <div class="row">
                <div class="col-sm-8 offset-sm-3">
                    <input type="submit" class="btn btn-outline-success" value="<fmt:message key="button.submit"/>">
                    <input type="reset" class="btn btn-outline-secondary" value="<fmt:message key="button.clearForm"/>">
                </div>
            </div>
        </form>
        <%--ПЕРЕДАЕТСЯ СООБЩЕНИЕ ПОСЛЕ НЕУДАЧНОЙ ПОПЫТКИ ВВОДА ЛОГИНА И/ИЛИ ПАРОЛЯ, И/ИЛИ ИМЕНИ ПОЛЬЗОВАТЕЛЯ--%>
        <div class="row">
            <div class="col-sm-8 offset-sm-3">
                <p>
                    <span class="custom">${requestScope.errorRegistrationPassMessage}</span>
                </p>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-8 offset-sm-3">
                <p>
                    <span class="custom">${requestScope.invalidEmail}</span>
                </p>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-8 offset-sm-3">
                <p>
                    <span class="custom">${requestScope.invalidPassword}</span>
                </p>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-8 offset-sm-3">
                <p>
                    <span class="custom">${requestScope.invalidNickname}</span>
                </p>
            </div>
            <div class="col-sm-8 offset-sm-3">
                <p>
                    <span style="font-weight: bold">${requestScope.successRegistration}</span>
                </p>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>
