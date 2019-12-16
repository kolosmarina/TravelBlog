<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>

<html>
<head>
    <title>WorldPartPosts</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-sm-10 text-center">
            <%--СОРТИРОВКА ПОСТОВ ПО ДАТЕ ИЛИ ПО ПОПУЛЯРНОСТИ ИЛИ ПОКАЗ СОБСТВЕННЫХ ПОСТОВ--%>
            <div class="row">
                <div class="col-sm-4 text-center">
                    <p style="font-weight: bold;">
                        <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldPart}&sortBy=date&command=${param.command}"><fmt:message
                                key="ref.new"/></a></p>
                    <p>
                </div>
                <div class="col-sm-4 text-center">
                    <c:if test="${not empty sessionScope.user}">
                    <p style="font-weight: bold;">
                        <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldPart}&sortBy=own&command=${param.command}&userId=${sessionScope.user.id}"><fmt:message
                                key="ref.own"/></a></p>
                    <p>
                        </c:if>
                </div>
                <div class="col-sm-4 text-center">
                    <p style="font-weight: bold;">
                        <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldPart}&sortBy=popularity&command=${param.command}"><fmt:message
                                key="ref.popular"/></a></p>
                </div>
            </div>
            <%--ПЕРЕХОД ПО СТРАНИЦАМ--%>
            <div class="row">
                <div class="col-sm-3 text-sm-left">
                    <c:if test="${requestScope.prevPageNumber>0}">
                        <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldPart}&sortBy=${requestScope.sortBy}&pageNumber=${requestScope.prevPageNumber}&command=${param.command}&popularTagId=${requestScope.popularTagId}&popularUserId=${requestScope.popularUserId}">
                            <span class="ref">&#171;</span>
                        </a>
                    </c:if>
                </div>
                <div class="col-sm-3 offset-sm-6 text-sm-right">
                    <c:set var="lastPageNumber" value="${requestScope.lastPageNumber}"/>
                    <c:set var="nextPageNumber" value="${requestScope.nextPageNumber}"/>
                    <c:if test="${nextPageNumber<=lastPageNumber}">
                        <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldPart}&sortBy=${requestScope.sortBy}&pageNumber=${requestScope.nextPageNumber}&command=${param.command}&popularTagId=${requestScope.popularTagId}&popularUserId=${requestScope.popularUserId}">
                            <span class="ref">&#187;</span>
                        </a>
                    </c:if>
                </div>
            </div>
            <%--ОТОБРАЖЕНИЕ ВСЕХ ПОСТОВ И ПЕРЕХОД К ОПРЕДЕЛЕННОМУ ПОСТУ--%>
            <div class="card-columns">
                <c:forEach items="${requestScope.posts}" var="post">
                    <jsp:useBean id="dateValue" class="java.util.Date"/>
                    <jsp:setProperty name="dateValue" property="time" value="${post.publishDate}"/>
                    <div class="card border-primary">
                        <img class="card-img-top"
                             src="${pageContext.request.contextPath}/images?&photoId=${post.mainPhotoId}"
                             alt="${post.mainPhotoId}">
                        <div class="card-body">
                            <h5 class="card-title"><fmt:formatDate value="${dateValue}"
                                                                   pattern="dd.MM.yyyy HH:mm"/></h5>
                            <p class="card-text">${post.name}</p>
                            <a href="${pageContext.request.contextPath}/travelling?postId=${post.id}&command=open_post_page"
                               class="btn btn-outline-success"><fmt:message key="ref.choose"/></a>
                                <%--СОЗДАНИЕ КНОПКИ ДЛЯ ЗАРЕГИСТРИРОВАННОГО ПОЛЬЗОВАТЕЛЯ НА УДАЛЕНИЕ СВОЕГО ПОСТА--%>
                            <c:if test="${(not empty sessionScope.user)&&(sessionScope.user.id==post.userId)}">
                                <a href="${pageContext.request.contextPath}/travelling?postId=${post.id}&command=delete_post"
                                   class="btn btn-outline-success"><fmt:message key="button.toDelete"/></a>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <div class="col-sm-2 text-center">
            <%--ДЛЯ СОЗДАНИЯ ПОСТА ДЛЯ ЗАРЕГЕСТРИРОВАННЫХ ПОЛЬЗОВАТЕЛЕЙ--%>
            <div class="row">
                <div class="col-sm-12 text-center">
                    <c:if test="${not empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/travelling?command=open_new_post_page&worldPart=${requestScope.worldPart}"
                           class="btn btn-outline-success"><fmt:message key="button.createPost"/></a>
                    </c:if>
                </div>
            </div>
            <br>
            <%--ОТОБРАЖЕНИЕ САМЫХ ПОПУЛЯРНЫХ ПОЛЬЗОВАТЕЛЕЙ И ПЕРЕХОД К ИХ ПОСТАМ--%>
            <div class="row">
                <div class="col-sm-12 text-center">
                    <p style="text-transform: uppercase; font-weight: bold;"><fmt:message key="column.users"/><br>&#8681;
                    </p>
                    <c:forEach items="${requestScope.users}" var="user" varStatus="loop">
                        <p style="font-size:${20-loop.index*2}px"><a
                                href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldPart}&popularUserId=${user.id}&command=${param.command}"
                                style="font-weight: bold;">${user.nickname}</a>
                        </p>
                    </c:forEach>
                </div>
            </div>
            <%-- ОТОБРАЖЕНИЕ САМЫХ ПОПУЛЯРНЫХ ТЕГОВ И ПЕРЕХОД К ПОСТАМ ПО НИМ--%>
            <div class="row">
                <div class="col-sm-12 text-center">
                    <p style="text-transform: uppercase; font-weight: bold;"><fmt:message key="column.tags"/><br>&#8681;
                    </p>
                    <c:forEach items="${requestScope.tags}" var="tag" varStatus="loop">
                        <p style="font-size:${20-loop.index*2}px"><a
                                href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldPart}&popularTagId=${tag.id}&command=${param.command}"
                                style="font-weight: bold;">#${tag.value}</a>
                        </p>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <%--СОБСТВЕННЫЕ ТЕГИ--%>
    <div class="row">
        <div class="col-sm-12 text-center">
            <ctg:general-info color="blue"/>
        </div>
    </div>
</div>
</body>
</html>
