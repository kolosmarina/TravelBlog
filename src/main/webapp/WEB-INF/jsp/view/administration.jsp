<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Administration</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <%--ОТОБРАЖЕНИЕ НАЗВАНИЯ ТАБЛИЦЫ--%>
    <h2 class="text-center"><fmt:message key="text.nameTable"/></h2>
    <%--ТАБЛИЦА ОТОБРАЖЕНИЕ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ--%>
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th scope="col" style="width: 20%"><fmt:message key="column.userId"/></th>
            <th scope="col" style="width: 30%"><fmt:message key="column.nickName"/></th>
            <th scope="col" style="width: 20%"><fmt:message key="column.isActive"/></th>
            <th scope="col" style="width: 30%"><fmt:message key="column.changeStatus"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.users}" var="user">
        <tr>
            <th scope="row">${user.id}</th>
            <td>${user.nickname}</td>
            <c:choose>
                <c:when test="${!user.isActive}">
                    <form name="unblockUser"
                          action="${pageContext.request.contextPath}/travelling" method="post">
                        <input type="hidden" name="command" value="change_user_status"/>
                        <input type="hidden" name="userId" value="${user.id}"/>
                        <input type="hidden" name="isActiveUser" value="true"/>
                        <td><p class="text-danger">${user.isActive}</p></td>
                        <td><input type="submit" class="btn btn-outline-primary"
                                   value="<fmt:message key="button.unblock"/>">
                        </td>
                    </form>
                </c:when>
                <c:when test="${user.isActive}">
                    <form name="blockUser"
                          action="${pageContext.request.contextPath}/travelling" method="post">
                        <input type="hidden" name="command" value="change_user_status"/>
                        <input type="hidden" name="userId" value="${user.id}"/>
                        <input type="hidden" name="isActiveUser" value="false"/>
                        <td><p class="text-success">${user.isActive}</p></td>
                        <td><input type="submit" class="btn btn-outline-danger"
                                   value="<fmt:message key="button.block"/>">
                        </td>
                    </form>
                </c:when>
            </c:choose>
            </c:forEach>
        </tr>
    </table>
</body>
</html>
