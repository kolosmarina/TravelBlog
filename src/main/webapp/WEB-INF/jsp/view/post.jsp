<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Post</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <%--ПЕРЕХОД ПО СТРАНИЦАМ--%>
    <div class="row">
        <div class="col-sm-3 text-sm-left">
            <c:if test="${requestScope.prevPageNumber>0}">
                <a href="${pageContext.request.contextPath}/travelling?postId=${requestScope.postId}&pageNumber=${requestScope.prevPageNumber}&command=${param.command}&isNewPost=${requestScope.isNewPost}">
                    <span class="ref">&#171;</span></a>
            </c:if>
        </div>
        <div class="col-sm-3 offset-sm-6 text-sm-right">
            <c:set var="lastPage" value="${requestScope.lastPageNumber}"/>
            <c:set var="nextPage" value="${requestScope.nextPageNumber}"/>
            <c:if test="${nextPage<=lastPage}">
                <a href="${pageContext.request.contextPath}/travelling?postId=${requestScope.postId}&pageNumber=${requestScope.nextPageNumber}&command=${param.command}&isNewPost=${requestScope.isNewPost}">
                    <span class="ref">&#187;</span></a>
            </c:if>
        </div>
    </div>
    <%--ОТОБРАЖЕНИЕ ВСЕХ ФОТО И ПЕРЕХОД К ОПРЕДЕЛЕННОЙ ФОТО--%>
    <div class="card-columns">
        <c:if test="${!requestScope.isNewPost}">
            <c:forEach items="${requestScope.photos}" var="photo">
                <div class="card border-dark">
                    <img class="card-img-top" src="${pageContext.request.contextPath}/images?photoId=${photo.id}"
                         alt="${photo.id}">
                    <div class="card-body text-center">
                        <h5 class="card-title">${photo.description}</h5>
                        <p class="card-text"><strong>&#x1f499;</strong><%--<fmt:message key="text.likesNumber"/>: --%>${photo.likesNumber}</p>
                        <a href="${pageContext.request.contextPath}/travelling?photoId=${photo.id}&command=open_photo_page"
                           class="btn btn btn-outline-success"><fmt:message
                                key="ref.photo"/></a>
                    </div>
                </div>
            </c:forEach>
        </c:if>
        <%--ОТОБРАЖЕНИЕ ФОТО НОВОГО ПОСТА С ВЫБОРОМ ЗАГЛАВНОЙ ФОТО--%>
        <c:if test="${requestScope.isNewPost}">
            <form action="${pageContext.request.contextPath}/travelling" method="post">
                <input type="hidden" name="postId" value="${requestScope.postId}">
                <input type="hidden" name="photoId" value="${requestScope.photo.id}">
                <input type="hidden" name="command" value="set_main_photo"/>
                <c:forEach items="${requestScope.photos}" var="photo">
                    <div class="card border-warning">
                        <img class="card-img-top" src="${pageContext.request.contextPath}/images?photoId=${photo.id}"
                             alt="${photo.id}">
                        <div class="card-body">
                            <label>
                                <input type="radio" name="mainPhoto" value="${photo.id}" checked>
                                <fmt:message
                                        key="text.setMainPhoto"/>
                            </label>
                            <h5 class="card-title"><fmt:message key="text.description"/>: ${photo.description}</h5><br>
                        </div>
                    </div>
                </c:forEach>
                <br>
                <button type="submit" class="btn btn-info"><fmt:message key="button.saveChanges"/></button>
            </form>
        </c:if>
    </div>
</div>
</body>
</html>
