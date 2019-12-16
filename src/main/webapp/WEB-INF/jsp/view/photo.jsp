<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Photo</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <%--ПЕРЕХОД МЕЖДУ ФОТО--%>
    <div class="row">
        <div class="col-sm-4 text-sm-left">
            <c:if test="${requestScope.hasPrevPhoto}">
                <a href="${pageContext.request.contextPath}/travelling?photoId=${requestScope.prevPhotoId}&command=open_photo_page">
                    <span class="ref">&#171;</span></a>
            </c:if>
        </div>
        <div class="col-sm-4 text-sm-right">
            <c:if test="${requestScope.hasNextPhoto}">
                <a href="${pageContext.request.contextPath}/travelling?photoId=${requestScope.nextPhotoId}&command=open_photo_page">
                    <span class="ref">&#187;</span></a>
            </c:if>
        </div>
        <%--НАЗВАНИЕ КОЛОНКИ ОПИСАНИЕ ФОТО--%>
        <div class="col-sm-4 text-sm-center">
            <span style="text-transform: uppercase; font-weight: bold;"><fmt:message key="text.description"/></span>
        </div>
    </div>
    <%--ЗАГРУЗКА ФОТО--%>
    <div class="row">
        <div class="col-sm-8 text-center">
            <img src="${pageContext.request.contextPath}/images?photoId=${requestScope.photo.id}"
                 alt="${requestScope.photo.id}" class="img-thumbnail">
        </div>
        <%--ОПИСАНИЕ ФОТО--%>
        <div class="col-sm-4 text-center">
            <div class="row">
                <div class="col-sm-12 text-sm-center">
                    ${requestScope.photo.description}
                </div>
            </div>
            <br>
            <%--НАЗВАНИЕ КОЛОНКИ ТЕГИ--%>
            <div class="row">
                <div class="col-sm-12 text-sm-center">
                    <span style="text-transform: uppercase; font-weight: bold;"><fmt:message key="text.tags"/></span>
                </div>
            </div>
            <%--ИСПОЛЬЗУЕМЫЕ ТЕГИ--%>
            <div class="row">
                <div class="col-sm-12 text-sm-center">
                    <c:forEach items="${requestScope.tags}" var="tag">
                        #${tag.value}
                    </c:forEach>
                </div>
            </div>
            <br>
            <%--УДАЛИТЬ ИЛИ ПОСТАВИТЬ ЛАЙК--%>
            <c:if test="${not empty sessionScope.user}">
            <div class="row">
                <div class="col-sm-6 text-sm-right">
                    <c:if test="${requestScope.like==null}">
                        <a href="${pageContext.request.contextPath}/travelling?photoId=${requestScope.photo.id}&command=save_like">&#x1f499;</a>
                    </c:if>
                    <c:if test="${requestScope.like!=null}">
                        <a href="${pageContext.request.contextPath}/travelling?photoId=${requestScope.photo.id}&command=delete_like">&#10084;</a>
                    </c:if>
                </div>
                </c:if>
                <%--ДЛЯ ОТОБРАЖЕНИЯ КОЛИЧЕСТВА ЛАЙКОВ БЕЗ ВОЗМОЖНОСТИ ПОСТАВИТЬ ЛАЙК--%>
                <c:if test="${empty sessionScope.user}">
                <div class="row">
                    <div class="col-sm-6 text-sm-right">
                        <strong>&#x1f499;</strong>
                    </div>
                    </c:if>
                    <%--КОЛИЧЕСТВО ЛАЙКОВ ДЛЯ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ--%>
                    <div class="col-sm-6 text-sm-left">
                        <span style="text-transform: uppercase; font-weight: bold;">${requestScope.photo.likesNumber}</span>
                    </div>
                </div>
                <br>
                <%--КОММЕНТИРОВАТЬ--%>
                <c:if test="${not empty sessionScope.user}">
                    <div class="row">
                        <div class="col-sm-12 text-sm-center">
                            <form action="${pageContext.request.contextPath}/travelling" method="post">
                                <input type="hidden" name="photoId" value="${requestScope.photo.id}">
                                <input type="hidden" name="command" value="save_comment"/>
                                <div class="row">
                                    <label class="control-label col-sm-1" for="valueComment"></label>
                                    <div class="col-sm-11">
                                        <p><textarea rows="5" class="form-control" id="valueComment" name="valueComment"
                                                     placeholder="<fmt:message key="text.newComment"/>" required maxlength="255"></textarea>
                                        </p>
                                    </div>
                                </div>
                                <div class="row">
                                    <small id="commentHelp" class="form-text text-muted col-sm-10 offset-sm-2">
                                        <fmt:message key="text.warning"/></small>
                                </div>
                                <div class="row">
                                    <div class="col-sm-10 offset-sm-2">
                                        <input type="submit" class="btn btn-outline-success"
                                               value="<fmt:message key="button.toPublish"/>">
                                        <input type="reset" class="btn btn-outline-secondary"
                                               value="<fmt:message key="button.clearForm"/>">
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <%--ПЕРЕДАЕТСЯ СООБЩЕНИЕ В СЛУЧАЕ, ЕСЛИ ПОЛЬЗОВАТЕЛЬ отправил пустое сообщение--%>
                    <div class="row">
                        <div class="col-sm-12 text-sm-center">
                            <p>
                                <span class="custom">${requestScope.emptyComment}</span>
                            </p>
                        </div>
                    </div>
                    <br>
                </c:if>
            </div>
        </div>
        <%--КОММЕНТАРИИ--%>
        <c:forEach items="${requestScope.comments}" var="comment">
            <jsp:useBean id="dateValue" class="java.util.Date"/>
            <jsp:setProperty name="dateValue" property="time" value="${comment.publishDate}"/>
            <div class="row">
                <div class="col-sm-2 text-sm-left"><c:out value="${comment.userNickname}"/></div>
                <div class="col-sm-2 text-sm-left"><fmt:formatDate value="${dateValue}"
                                                                   pattern="dd.MM.yyyy HH:mm"/></div>
                <div class="col-sm-7 text-sm-left"><c:out value="${comment.value}"/></div>
                <div class="col-sm-1 text-sm-left">
                    <c:if test="${not empty sessionScope.user&&sessionScope.user.id==comment.userId}">
                        <a href="${pageContext.request.contextPath}/travelling?commentId=${comment.id}&command=delete_comment"
                           style="color:red;">
                            <fmt:message key="button.deleteComment"/></a>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
</body>
</html>
