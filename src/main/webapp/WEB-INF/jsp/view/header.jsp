<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <%@ include file="locale.jsp" %>
    <title>Header</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <script src="${pageContext.request.contextPath}/js/jquery.slim.js"></script>
    <script src="${pageContext.request.contextPath}/js/popper.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</head>
<body>
<header>
    <div class="container">
        <nav class="navbar navbar-expand-lg navbar-light">
            <a class="navbar-brand"
               href="${pageContext.request.contextPath}/travelling?command=open_main_page">&#8962;<fmt:message
                    key="label.brand"/>&#9992;</a>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <c:if test="${empty sessionScope.user}">
                            <a class="nav-link"
                               href="${pageContext.request.contextPath}/travelling?command=open_login_page">
                                <fmt:message key="button.logIn"/></a>
                        </c:if>
                        <c:if test="${not empty sessionScope.user}">
                            <a class="nav-link disabled" href="#"><fmt:message
                                    key="status.text"/>: <c:out value="${sessionScope.user.nickname}"/></a>
                        </c:if>
                    </li>
                    <li class="nav-item">
                        <c:if test="${empty sessionScope.user}">
                            <a class="nav-link"
                               href="${pageContext.request.contextPath}/travelling?command=open_registration_page">
                                <fmt:message key="button.signUp"/></a>
                        </c:if>
                        <c:if test="${not empty sessionScope.user}">
                            <a class="nav-link"
                               href="${pageContext.request.contextPath}/travelling?command=logout">
                                <fmt:message key="button.logout"/></a>
                        </c:if>
                    </li>
                    <li class="nav-item">
                        <c:if test="${not empty sessionScope.user && sessionScope.user.userRole.name()=='ADMIN'}">
                            <a class="nav-link"
                               href="${pageContext.request.contextPath}/travelling?command=administration">
                                <fmt:message key="button.forAdmin"/></a>
                        </c:if>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <fmt:message key="button.chooseLanguage"/>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/travelling?command=change_locale&locale=<fmt:message key="locale.en"/>"><fmt:message
                                    key="languageUS.select"/></a>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/travelling?command=change_locale&locale=<fmt:message key="locale.be"/>"><fmt:message
                                    key="languageBY.select"/></a>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/travelling?command=change_locale&locale=<fmt:message key="locale.ru"/>"><fmt:message
                                    key="languageRU.select"/></a>
                        </div>
                    </li>
                </ul>
            </div>
        </nav>
    </div>
</header>
</body>
</html>
