<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>NewPost</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<%@ include file="header.jsp" %>
<%--ОТОБРАЖЕНИЕ ФОРМЫ ДЛЯ ПЕРЕХОДА НА ГЛАВНУЮ СТРАНИЦУ--%>
<div class="container">
    <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
        <input type="hidden" name="worldPart" value="${requestScope.worldPart}">
        <input type="hidden" name="photosNumberAvailableForUpload"
               value="${requestScope.photosNumberAvailableForUpload}">
        <c:if test="${requestScope.postId!=null}">
            <input type="hidden" name="postId" value="${requestScope.postId}">
        </c:if>
        <c:if test="${requestScope.postId==null}">
            <h4 class="text-center"><fmt:message key="text.CreatingPost"/></h4>
            <%----ДЛЯ СОЗДАНИЯ НАЗВАНИЯ ПОСТА--%>
            <div class="row">
                <label class="control-label col-sm-3" for="postName"><fmt:message key="text.postName"/>
                    <span class="custom">&#42;</span>
                </label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="postName" name="postName"
                           required maxlength="150" minlength="5">
                </div>
            </div>
            <div class="row">
                <small id="namePostHelp" class="form-text text-muted col-sm-9 offset-sm-3"><fmt:message
                        key="help.namePost"/></small>
            </div>
            <%--ПЕРЕДАЕТСЯ СООБЩЕНИЕ В СЛУЧАЕ, ЕСЛИ НЕ УКАЗАНО НАЗВАНИЕ ПОСТА ИЛИ/И НЕТ ФАЙЛА ДЛЯ ЗАГРУЗКИ--%>
            <div class="row">
                <div class="col-sm-9 offset-sm-3">
                    <span class="custom">${requestScope.emptyPostName}</span>
                    <span class="custom">${requestScope.emptyFiles}</span>
                </div>
            </div>
            <div class="row">
                <h5><fmt:message key="text.choosePhotoForNewPost"/> &#8628;</h5>
            </div>
        </c:if>
        <c:if test="${requestScope.postId!=null}">
            <div class="row">
                <h5><fmt:message key="text.keepUploadingPhotosForNewPost"/> &#8628;</h5>
            </div>
            <%--ПЕРЕДАЕТСЯ СООБЩЕНИЕ В СЛУЧАЕ, ЕСЛИ НЕ УКАЗАНО НЕТ ФАЙЛА ДЛЯ ЗАГРУЗКИ--%>
            <div class="row">
                <div class="col-sm-9 offset-sm-3">
                    <span class="custom">${requestScope.emptyFiles}</span>
                </div>
            </div>
        </c:if>
        <%--ДЛЯ ЗАГРУЗКИ ФОТО--%>
        <c:forEach var="i" begin="1" end="${requestScope.photosNumberAvailableForUpload}">

            <div class="row">
                <input type="file" accept=".jpg" name="photo:${i}"><br>
            </div>
            <%--ДЛЯ ЗАГРУЗКИ НАЗВАНИЯ ФОТО--%>
            <div class="row">
                <label class="control-label col-sm-2" for="description:${i}"><fmt:message
                        key="text.photoDescription"/></label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="description:${i}" name="description:${i}"
                           maxlength="150">
                </div>
            </div>
            <%--ДЛЯ ЗАГРУЗКИ ТЕГОВ ДЛЯ ВЫБОРА--%>
            <label>
                <select name="tag" id="tag" onchange="getTag(this, ${i})">
                    <option value=""><fmt:message key="column.tags"/></option>
                    <c:forEach items="${requestScope.tags}" var="tag">
                        <option value="${tag.value}">${tag.value}</option>
                    </c:forEach>
                </select>
                    <%--ДЛЯ ЗАГРУЗКИ СОБСТВЕННЫХ ТЕГОВ--%>
                <input type="text" id="newTag:${i}">
                <input type="button" onclick="addTag(${i})" value="<fmt:message key="button.addTag"/>"><br>
                <input type="hidden" id="tags-input:${i}" name="tags-input:${i}"/>
            </label>
            <h6 id="tags:${i}"></h6>
            <br/>
        </c:forEach>
        <div class="row">
            <div class="col-sm-10 offset-sm-2">
                <c:if test="${requestScope.postId==null}">
                    <input type="submit" class="btn btn-outline-success"
                           value="<fmt:message key="button.saveAndUploadPhoto"/>">
                </c:if>
                <c:if test="${requestScope.postId!=null}">
                    <input type="submit" class="btn btn-outline-success"
                           value="<fmt:message key="button.uploadPhoto"/>">
                </c:if>
                <input type="reset" class="btn btn-outline-secondary" value="<fmt:message key="button.clearForm"/>">
            </div>
        </div>
    </form>
    <%--ПЕРЕХОД НА СТРАНИЦУ СОЗДАННОГО ПОСТА ДЛЯ ВЫБОРА ЗАГЛАВНОГО ФОТО--%>
    <c:if test="${requestScope.postId!=null}">
        <form name="MainPage" action="${pageContext.request.contextPath}/travelling">
            <input type="hidden" name="command" value="open_post_page"/>
            <input type="hidden" name="isNewPost" value="true"/>
            <input type="hidden" name="postId" value="${requestScope.postId}">
            <div class="row">
                <div class="col-sm-10 offset-sm-2">
                    <input type="submit" class="btn btn-outline-success"
                           value="<fmt:message key="button.postPage"/>">
                </div>
            </div>
        </form>
    </c:if>
</div>
<script src="${pageContext.request.contextPath}/js/tag.js"></script>
</body>
</html>
